package com.example.order.repository;

import com.example.common.dto.OrderDTO;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public class OrderRepository {

    private final JdbcTemplate jdbcTemplate;

    public OrderRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Long insert(Long userId, Long productId, Integer count, BigDecimal totalAmount, String status) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(
                    "insert into t_order(user_id, product_id, count, total_amount, status, create_time) values (?, ?, ?, ?, ?, now())",
                    Statement.RETURN_GENERATED_KEYS
            );
            ps.setLong(1, userId);
            ps.setLong(2, productId);
            ps.setInt(3, count);
            ps.setBigDecimal(4, totalAmount);
            ps.setString(5, status);
            return ps;
        }, keyHolder);
        return keyHolder.getKey() == null ? null : keyHolder.getKey().longValue();
    }

    public OrderDTO findById(Long id) {
        List<OrderDTO> list = jdbcTemplate.query(
                "select id, user_id, product_id, count, total_amount, status, create_time from t_order where id = ?",
                (rs, rowNum) -> {
                    OrderDTO dto = new OrderDTO();
                    dto.setId(rs.getLong("id"));
                    dto.setUserId(rs.getLong("user_id"));
                    dto.setProductId(rs.getLong("product_id"));
                    dto.setCount(rs.getInt("count"));
                    dto.setTotalAmount(rs.getBigDecimal("total_amount"));
                    dto.setStatus(rs.getString("status"));
                    dto.setCreateTime(rs.getObject("create_time", LocalDateTime.class));
                    return dto;
                },
                id
        );
        return list.isEmpty() ? null : list.get(0);
    }
}
