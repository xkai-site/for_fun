package com.example.order.client.fallback;

import com.example.common.api.ApiResponse;
import com.example.common.dto.ProductDTO;
import com.example.common.request.ProductDeductRequest;
import com.example.common.response.OperationResult;
import com.example.order.client.ProductClient;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class ProductClientFallback implements ProductClient {

    @Override
    public ApiResponse<ProductDTO> getById(Long id) {
        ProductDTO degraded = new ProductDTO(id, "degraded-product", BigDecimal.ZERO, 0);
        return ApiResponse.success(degraded);
    }

    @Override
    public ApiResponse<OperationResult> deduct(ProductDeductRequest request) {
        return ApiResponse.success(OperationResult.fail("product service fallback"));
    }
}
