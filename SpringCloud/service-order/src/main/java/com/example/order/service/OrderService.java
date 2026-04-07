package com.example.order.service;

import com.example.common.api.ApiResponse;
import com.example.common.dto.OrderDTO;
import com.example.common.dto.ProductDTO;
import com.example.common.request.AccountDeductRequest;
import com.example.common.request.CreateOrderRequest;
import com.example.common.request.ProductDeductRequest;
import com.example.common.response.OperationResult;
import com.example.order.client.AccountClient;
import com.example.order.client.ProductClient;
import com.example.order.client.UserClient;
import com.example.order.repository.OrderRepository;
import io.seata.spring.annotation.GlobalTransactional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class OrderService {

    private final UserClient userClient;
    private final ProductClient productClient;
    private final AccountClient accountClient;
    private final OrderRepository orderRepository;

    public OrderService(UserClient userClient,
                        ProductClient productClient,
                        AccountClient accountClient,
                        OrderRepository orderRepository) {
        this.userClient = userClient;
        this.productClient = productClient;
        this.accountClient = accountClient;
        this.orderRepository = orderRepository;
    }

    @GlobalTransactional(name = "create-order-global-tx", rollbackFor = Exception.class)
    @Transactional(rollbackFor = Exception.class)
    public OrderDTO createOrder(CreateOrderRequest request) {
        ensureUserExists(request.getUserId());

        ApiResponse<ProductDTO> productResponse = productClient.getById(request.getProductId());
        ProductDTO product = requireData(productResponse, "product not available");

        BigDecimal totalAmount = product.getPrice().multiply(BigDecimal.valueOf(request.getCount()));

        AccountDeductRequest accountDeductRequest = new AccountDeductRequest();
        accountDeductRequest.setUserId(request.getUserId());
        accountDeductRequest.setAmount(totalAmount);

        ProductDeductRequest productDeductRequest = new ProductDeductRequest();
        productDeductRequest.setProductId(request.getProductId());
        productDeductRequest.setCount(request.getCount());

        requireSuccess(accountClient.deduct(accountDeductRequest), "deduct account failed");
        requireSuccess(productClient.deduct(productDeductRequest), "deduct product failed");

        Long orderId = orderRepository.insert(
                request.getUserId(),
                request.getProductId(),
                request.getCount(),
                totalAmount,
                "CREATED"
        );

        if (request.isForceFail()) {
            throw new IllegalStateException("forceFail=true, trigger rollback for Seata validation");
        }

        OrderDTO dto = orderRepository.findById(orderId);
        if (dto == null) {
            throw new IllegalStateException("order created but not found: " + orderId);
        }
        return dto;
    }

    public OrderDTO getById(Long id) {
        OrderDTO order = orderRepository.findById(id);
        if (order == null) {
            throw new IllegalArgumentException("Order not found: " + id);
        }
        return order;
    }

    private void ensureUserExists(Long userId) {
        ApiResponse<?> userResponse = userClient.getById(userId);
        if (userResponse == null || userResponse.getCode() != 0 || userResponse.getData() == null) {
            throw new IllegalStateException("user not available: " + userId);
        }
    }

    private <T> T requireData(ApiResponse<T> response, String message) {
        if (response == null || response.getCode() != 0 || response.getData() == null) {
            throw new IllegalStateException(message);
        }
        return response.getData();
    }

    private void requireSuccess(ApiResponse<OperationResult> response, String message) {
        OperationResult result = requireData(response, message);
        if (!result.isSuccess()) {
            throw new IllegalStateException(message + " (fallback or business rejection)");
        }
    }
}
