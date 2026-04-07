package com.example.account.repository;

import com.example.common.dto.AccountDTO;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public class AccountRepository {

    private final JdbcTemplate jdbcTemplate;

    public AccountRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public AccountDTO findByUserId(Long userId) {
        List<AccountDTO> list = jdbcTemplate.query(
                "select user_id, balance from t_account where user_id = ?",
                (rs, rowNum) -> new AccountDTO(rs.getLong("user_id"), rs.getBigDecimal("balance")),
                userId
        );

        return list.isEmpty() ? null : list.get(0);
    }

    public int deduct(Long userId, BigDecimal amount) {
        return jdbcTemplate.update(
                "update t_account set balance = balance - ? where user_id = ? and balance >= ?",
                amount, userId, amount
        );
    }
}
