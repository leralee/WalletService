package org.example.dto;

import java.math.BigDecimal;

/**
 * @author valeriali on {24.10.2023}
 * @project walletService
 */
public class BalanceDTO {
    private String action;
    private BigDecimal amount;
    private String transaction_id;

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getTransaction_id() {
        return transaction_id;
    }

    public void setTransaction_id(String transaction_id) {
        this.transaction_id = transaction_id;
    }
}
