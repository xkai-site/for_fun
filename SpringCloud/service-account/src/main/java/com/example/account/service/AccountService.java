package com.example.account.service;

import com.example.account.repository.AccountRepository;
import com.example.common.dto.AccountDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class AccountService {

    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public AccountDTO getByUserId(Long userId) {
        AccountDTO account = accountRepository.findByUserId(userId);
        if (account == null) {
            throw new IllegalArgumentException("Account not found: " + userId);
        }
        return account;
    }

    @Transactional(rollbackFor = Exception.class)
    public void deduct(Long userId, BigDecimal amount) {
        int updated = accountRepository.deduct(userId, amount);
        if (updated == 0) {
            throw new IllegalStateException("Insufficient balance for user: " + userId);
        }
    }
}
