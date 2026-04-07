package com.example.product.controller;

import com.example.common.api.ApiResponse;
import com.example.common.dto.ProductDTO;
import com.example.common.request.ProductDeductRequest;
import com.example.common.response.OperationResult;
import com.example.product.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/{id}")
    public ApiResponse<ProductDTO> getById(@PathVariable Long id) {
        return ApiResponse.success(productService.getById(id));
    }

    @PostMapping("/deduct")
    public ApiResponse<OperationResult> deduct(@Valid @RequestBody ProductDeductRequest request) {
        productService.deduct(request.getProductId(), request.getCount());
        return ApiResponse.success(OperationResult.ok("stock deducted"));
    }
}
