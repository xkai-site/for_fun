package com.example.common.dto;

import java.math.BigDecimal;

public class AccountDTO {

    private Long userId;
    private BigDecimal balance;

    public AccountDTO() {
    }

    public AccountDTO(Long userId, BigDecimal balance) {
        this.userId = userId;
        this.balance = balance;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
}
