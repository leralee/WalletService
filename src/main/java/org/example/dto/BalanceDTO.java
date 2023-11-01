package org.example.dto;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * Класс DTO (Data Transfer Object) для передачи информации о балансе.
 * Этот класс используется для передачи данных о совершаемых операциях с балансом между клиентом и сервером.
 */
@Component
public class BalanceDTO {
    private String action;
    private BigDecimal amount;
    private String transaction_id;

    /**
     * Получает действие, связанное с балансом.
     *
     * @return Действие в виде строки, может быть "deposit" для пополнения или "withdraw" для снятия средств.
     */
    public String getAction() {
        return action;
    }


    /**
     * Получает сумму операции.
     *
     * @return Сумма операции в виде {@link BigDecimal}.
     */
    public BigDecimal getAmount() {
        return amount;
    }


    /**
     * Получает идентификатор транзакции.
     *
     * @return Идентификатор транзакции в виде строки.
     */
    public String getTransaction_id() {
        return transaction_id;
    }

}
