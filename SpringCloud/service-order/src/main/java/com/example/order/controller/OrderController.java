package com.example.order.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.example.common.api.ApiResponse;
import com.example.common.dto.OrderDTO;
import com.example.common.request.CreateOrderRequest;
import com.example.order.config.OrderFeatureProperties;
import com.example.order.service.OrderService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RefreshScope
@RequestMapping("/orders")
public class OrderController {

    private static final Logger log = LoggerFactory.getLogger(OrderController.class);

    private final OrderService orderService;
    private final OrderFeatureProperties orderFeatureProperties;

    public OrderController(OrderService orderService, OrderFeatureProperties orderFeatureProperties) {
        this.orderService = orderService;
        this.orderFeatureProperties = orderFeatureProperties;
    }

    @PostMapping
    @SentinelResource(value = "createOrder", blockHandler = "createOrderBlocked", fallback = "createOrderFallback")
    public ApiResponse<OrderDTO> create(@Valid @RequestBody CreateOrderRequest request) {
        if (!orderFeatureProperties.isFeatureFlag()) {
            return ApiResponse.fail(503, "Order creation disabled by config center");
        }
        if (request.getCount() > orderFeatureProperties.getMaxCreateCount()) {
            return ApiResponse.fail(400, "Order count exceeds maxCreateCount");
        }
        return ApiResponse.success(orderService.createOrder(request));
    }

    @GetMapping("/{id}")
    public ApiResponse<OrderDTO> getById(@PathVariable Long id) {
        return ApiResponse.success(orderService.getById(id));
    }

    @GetMapping("/config")
    public ApiResponse<Map<String, Object>> configCheck() {
        Map<String, Object> payload = new HashMap<>();
        payload.put("featureFlag", orderFeatureProperties.isFeatureFlag());
        payload.put("maxCreateCount", orderFeatureProperties.getMaxCreateCount());
        return ApiResponse.success(payload);
    }

    public ApiResponse<OrderDTO> createOrderBlocked(CreateOrderRequest request, BlockException ex) {
        log.warn("request blocked by Sentinel, userId={}, productId={}", request.getUserId(), request.getProductId());
        return ApiResponse.fail(429, "Request blocked by Sentinel");
    }

    public ApiResponse<OrderDTO> createOrderFallback(CreateOrderRequest request, Throwable throwable) {
        log.warn("fallback triggered, reason={}", throwable.getMessage());
        return ApiResponse.fail(500, "Create order degraded: " + throwable.getMessage());
    }
}
