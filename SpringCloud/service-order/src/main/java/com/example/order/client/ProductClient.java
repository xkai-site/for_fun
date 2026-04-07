package com.example.order.client;

import com.example.common.api.ApiResponse;
import com.example.common.dto.ProductDTO;
import com.example.common.request.ProductDeductRequest;
import com.example.common.response.OperationResult;
import com.example.order.client.fallback.ProductClientFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "service-product", fallback = ProductClientFallback.class)
public interface ProductClient {

    @GetMapping("/products/{id}")
    ApiResponse<ProductDTO> getById(@PathVariable("id") Long id);

    @PostMapping("/products/deduct")
    ApiResponse<OperationResult> deduct(@RequestBody ProductDeductRequest request);
}
