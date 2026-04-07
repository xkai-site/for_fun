package com.example.product.repository;

import com.example.common.dto.ProductDTO;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ProductRepository {

    private final JdbcTemplate jdbcTemplate;

    public ProductRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public ProductDTO findById(Long id) {
        List<ProductDTO> list = jdbcTemplate.query(
                "select id, name, price, stock from t_product where id = ?",
                (rs, rowNum) -> new ProductDTO(
                        rs.getLong("id"),
                        rs.getString("name"),
                        rs.getBigDecimal("price"),
                        rs.getInt("stock")
                ),
                id
        );
        return list.isEmpty() ? null : list.get(0);
    }

    public int deductStock(Long productId, Integer count) {
        return jdbcTemplate.update(
                "update t_product set stock = stock - ? where id = ? and stock >= ?",
                count, productId, count
        );
    }
}
