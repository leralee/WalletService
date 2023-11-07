package org.example.audit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
//import org.example.exception.InvalidAmountException;
//import org.example.exception.TransactionExistsException;

import org.aspectj.lang.annotation.Pointcut;
import org.example.common.ActionType;
import org.example.common.AuditService;
import org.example.common.Player;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

//
///**
// * Аспект для аудита различных действий в приложении.
// * <p>
// * Использует сервис аудита для регистрации действий успешных и неудачных попыток
// * авторизации, регистрации, пополнения и снятия средств.
// * </p>
// */
@Aspect
@Component
public class AuditAspect {

    private static final Logger logger = LogManager.getLogger(AuditAspect.class);
    private AuditService auditService;

    static {
        System.out.println("from aspect");
    }

    /**
     * Конструктор аспекта аудита с указанием сервиса аудита.
     *
     * @param auditService сервис аудита, используемый для записи действий.
     */
    @Autowired
    public AuditAspect(AuditService auditService) {
        this.auditService = auditService;
    }

    @Pointcut("execution(* org.example.service.PlayerService.authorizePlayer(..))")
    public void authorizePlayerPointcut() {}

    /**
     * Совет "после возвращения", который регистрирует успешную авторизацию игрока.
     *
     * @param authorizedPlayer игрок, который был авторизован.
     */
    @AfterReturning(pointcut = "authorizePlayerPointcut()", returning = "authorizedPlayer")
    public void afterAuthorization(Player authorizedPlayer) {
        System.out.println("sucessss");
        if (authorizedPlayer != null) {
            auditService.recordAction(authorizedPlayer.getId(), ActionType.LOGIN);
        } else {
            auditService.recordAction(null, ActionType.LOGIN_FAILED);
        }
    }
//
//    /**
//     * Совет "после возвращения", который регистрирует результат регистрации игрока.
//     *
//     * @param result Опциональное значение, содержащее нового зарегистрированного игрока,
//     *               или null, если регистрация не удалась.
//     */
//    @AfterReturning(pointcut = "execution(* org.example.service.PlayerService.registerPlayer(..))", returning = "result")
//    public void afterRegisteringPlayer(Optional<Player> result) {
//        if (result.isPresent()) {
//            auditService.recordAction(result.get().getId(), Audit.ActionType.REGISTRATION_SUCCESS);
//        } else {
//            auditService.recordAction(-1L, Audit.ActionType.REGISTRATION_FAILED);
//        }
//    }
//
//    /**
//     * Совет "после возвращения", который регистрирует успешную транзакцию пополнения.
//     *
//     * @param player игрок, для которого транзакция пополнения была успешной.
//     */
//    @AfterReturning("execution(* org.example.service.TransactionService.credit(..)) && args(player,..)")
//    public void afterSuccessfulCredit(Player player) {
//        auditService.recordAction(player.getId(), Audit.ActionType.CREDIT);
//    }
//
//    /**
//     * Совет "после выброса исключения" для обработки неудачных транзакций пополнения из-за существующей транзакции.
//     *
//     * @param player игрок, пытающийся выполнить транзакцию.
//     * @param ex     исключение, выброшенное, когда транзакция уже существует.
//     */
//    @AfterThrowing(pointcut = "execution(* org.example.service.TransactionService.credit(..)) && args(player,..)", throwing = "ex")
//    public void afterFailedCreditDueToTransactionExists(Player player, TransactionExistsException ex) {
//        auditService.recordAction(player.getId(), Audit.ActionType.CREDIT_FAILED);
//    }
//
//    /**
//     * Совет "после выброса исключения" для обработки неудачных транзакций пополнения из-за неверной суммы.
//     *
//     * @param player игрок, пытающийся выполнить транзакцию.
//     * @param ex     исключение, выброшенное, когда сумма недействительна.
//     */
//    @AfterThrowing(pointcut = "execution(* org.example.service.TransactionService.credit(..)) && args(player,..)", throwing = "ex")
//    public void afterFailedCreditDueToInvalidAmount(Player player, InvalidAmountException ex) {
//        auditService.recordAction(player.getId(), Audit.ActionType.CREDIT_FAILED);
//    }
//
//    /**
//     * Совет "после возвращения", который регистрирует успешную транзакцию снятия средств.
//     *
//     * @param player игрок, для которого транзакция снятия средств была успешной.
//     */
//    @AfterReturning("execution(* org.example.service.TransactionService.withdraw(..)) && args(player,..)")
//    public void afterSuccessfulWithdraw(Player player) {
//        auditService.recordAction(player.getId(), Audit.ActionType.WITHDRAW);
//    }
//
//    /**
//     * Совет "после выброса исключения" для обработки неудачных транзакций снятия из-за существующей транзакции.
//     *
//     * @param player игрок, пытающийся выполнить транзакцию.
//     * @param ex     исключение, выброшенное, когда транзакция уже существует.
//     */
//    @AfterThrowing(pointcut = "execution(* org.example.service.TransactionService.withdraw(..)) && args(player,..)", throwing = "ex")
//    public void afterFailedWithdrawDueToTransactionExists(Player player, TransactionExistsException ex) {
//        auditService.recordAction(player.getId(), Audit.ActionType.WITHDRAW_FAILED);
//    }
//
//    /**
//     * Совет "после выброса исключения" для обработки неудачных транзакций снятия из-за неверной суммы.
//     *
//     * @param player игрок, пытающийся выполнить транзакцию.
//     * @param ex     исключение, выброшенное, когда сумма недействительна.
//     */
//    @AfterThrowing(pointcut = "execution(* org.example.service.TransactionService.withdraw(..)) && args(player,..)", throwing = "ex")
//    public void afterFailedWithdrawDueToInvalidAmount(Player player, InvalidAmountException ex) {
//        auditService.recordAction(player.getId(), Audit.ActionType.WITHDRAW_FAILED);
//    }
//
//    /**
//     * Совет Around, регистрирующий время выполнения метода любого сервиса в пакете org.example.service.
//     * <p>
//     * Данный совет охватывает все методы, определённые в сервисах указанного пакета, и замеряет время,
//     * затраченное на их выполнение. Результаты замера логируются.
//     * </p>
//     *
//     * @param joinPoint сопряжение, представляющее точку выполнения программы, где применяется совет.
//     * @return возвращает объект, который является результатом выполнения оригинального метода.
//     * @throws Throwable исключение, которое может быть выброшено при выполнении метода.
//     */
//    @Around("execution(* org.example.service.*.*(..))")
//    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
//        long start = System.currentTimeMillis();
//        Object proceed = joinPoint.proceed();
//        long executionTime = System.currentTimeMillis() - start;
//        logger.info("{} выполен за {} ms", joinPoint.getSignature(), executionTime);
//        return proceed;
//    }
}
