package com.example.common.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class ProductDeductRequest {

    @NotNull
    private Long productId;

    @NotNull
    @Min(1)
    private Integer count;

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}
