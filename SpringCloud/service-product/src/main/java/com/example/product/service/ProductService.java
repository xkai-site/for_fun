package com.example.product.service;

import com.example.common.dto.ProductDTO;
import com.example.product.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public ProductDTO getById(Long id) {
        ProductDTO product = productRepository.findById(id);
        if (product == null) {
            throw new IllegalArgumentException("Product not found: " + id);
        }
        return product;
    }

    @Transactional(rollbackFor = Exception.class)
    public void deduct(Long productId, Integer count) {
        int updated = productRepository.deductStock(productId, count);
        if (updated == 0) {
            throw new IllegalStateException("Insufficient stock for product: " + productId);
        }
    }
}
