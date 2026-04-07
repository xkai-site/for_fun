package com.example.user.repository;

import com.example.common.dto.UserDTO;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserRepository {

    private final JdbcTemplate jdbcTemplate;

    public UserRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public UserDTO findById(Long id) {
        List<UserDTO> list = jdbcTemplate.query(
                "select id, username from t_user where id = ?",
                (rs, rowNum) -> new UserDTO(rs.getLong("id"), rs.getString("username")),
                id
        );

        return list.isEmpty() ? null : list.get(0);
    }
}
