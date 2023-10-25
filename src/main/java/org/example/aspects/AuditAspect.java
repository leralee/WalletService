package org.example.aspects;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.example.exception.InvalidAmountException;
import org.example.exception.TransactionExistsException;
import org.example.model.Audit;
import org.example.model.Player;
import org.example.service.AuditService;

import java.util.Optional;

@Aspect
public class AuditAspect {

    private final AuditService auditService;

    public AuditAspect(AuditService auditService) {
        this.auditService = auditService;
    }

    @Around("execution(* org.example.service.PlayerService.registerPlayer(..))")
    public Object auditRegisterPlayer(ProceedingJoinPoint joinPoint) throws Throwable {
        System.out.println("Calling method " + joinPoint.getSignature());
        long startTime = System.currentTimeMillis();

        Object result = joinPoint.proceed();

        if (result instanceof Optional && !((Optional) result).isPresent()) {
            auditService.recordAction(-1L, Audit.ActionType.REGISTRATION_FAILED);
        } else if (result instanceof Player) {
            Player player = (Player) result;
            auditService.recordAction(player.getId(), Audit.ActionType.REGISTRATION_SUCCESS);
        }

        long endTime = System.currentTimeMillis();
        System.out.println("Execution of method " + joinPoint.getSignature() +
                " finished. Execution time is " + (endTime - startTime) + " ns");

        return result;
    }

    @Around("execution(* org.example.service.PlayerService.authorizePlayer(..))")
    public Object auditAuthorizePlayer(ProceedingJoinPoint joinPoint) throws Throwable {
        System.out.println("Calling method " + joinPoint.getSignature());
        long startTime = System.currentTimeMillis();

        Object result = joinPoint.proceed();
        if (result == null) {
            auditService.recordAction(null, Audit.ActionType.LOGIN_FAILED);
        } else if (result instanceof Player) {
            Player player = (Player) result;
            auditService.recordAction(player.getId(), Audit.ActionType.LOGIN);
        }

        long endTime = System.currentTimeMillis();
        System.out.println("Execution of method " + joinPoint.getSignature() +
                " finished. Execution time is " + (endTime - startTime) + " ns");

        return result;
    }

    @Around("execution(* org.example.service.TransactionService.credit(..))")
    public Object auditCredit(ProceedingJoinPoint joinPoint) throws Throwable {


        try {
            System.out.println("Calling method " + joinPoint.getSignature());
            long startTime = System.currentTimeMillis();

            Object result = joinPoint.proceed();
            Player player = (Player) joinPoint.getArgs()[0];
            auditService.recordAction(player.getId(), Audit.ActionType.CREDIT);

            long endTime = System.currentTimeMillis();
            System.out.println("Execution of method " + joinPoint.getSignature() +
                    " finished. Execution time is " + (endTime - startTime) + " ns");

            return result;
        } catch (TransactionExistsException ex) {
            Player player = (Player) joinPoint.getArgs()[0];
            if ("Транзакция с данным ID уже существует.".equals(ex.getMessage())) {
                auditService.recordAction(player.getId(), Audit.ActionType.CREDIT_FAILED);
            } else {
                auditService.recordAction(player.getId(), Audit.ActionType.CREDIT_FAILED);
            }
            throw ex;
        } catch (InvalidAmountException ex) {
            Player player = (Player) joinPoint.getArgs()[0];
            if ("Сумма должна быть положительной.".equals(ex.getMessage())) {
                auditService.recordAction(player.getId(), Audit.ActionType.CREDIT_FAILED);
            }
            throw ex;
        }
    }

    @Around("execution(* org.example.service.TransactionService.withdraw(..))")
    public Object auditWithdraw(ProceedingJoinPoint joinPoint) throws Throwable {
        try {
            System.out.println("Calling method " + joinPoint.getSignature());
            long startTime = System.currentTimeMillis();

            Object result = joinPoint.proceed();
            Player player = (Player) joinPoint.getArgs()[0];
            auditService.recordAction(player.getId(), Audit.ActionType.WITHDRAW);

            long endTime = System.currentTimeMillis();
            System.out.println("Execution of method " + joinPoint.getSignature() +
                    " finished. Execution time is " + (endTime - startTime) + " ns");

            return result;
        } catch (TransactionExistsException ex) {
            Player player = (Player) joinPoint.getArgs()[0];
            auditService.recordAction(player.getId(), Audit.ActionType.WITHDRAW_FAILED);
            throw ex;
        }
    }




}
