package org.example.logging;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class LoggingAspect {

    private static final Logger logger = LogManager.getLogger(LoggingAspect.class);

    /**
     * Совет Around, регистрирующий время выполнения метода любого сервиса в пакете org.example.service.
     * <p>
     * Данный совет охватывает все методы, определённые в сервисах указанного пакета, и замеряет время,
     * затраченное на их выполнение. Результаты замера логируются.
     * </p>
     *
     * @param joinPoint сопряжение, представляющее точку выполнения программы, где применяется совет.
     * @return возвращает объект, который является результатом выполнения оригинального метода.
     * @throws Throwable исключение, которое может быть выброшено при выполнении метода.
     */
    @Around("execution(* ru.ylab.common.service.*.*(..))")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        Object proceed = joinPoint.proceed();
        long executionTime = System.currentTimeMillis() - start;
        logger.info("{} выполен за {} ms", joinPoint.getSignature(), executionTime);
        return proceed;
    }
}
