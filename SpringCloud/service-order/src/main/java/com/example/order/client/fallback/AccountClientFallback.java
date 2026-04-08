package com.example.order.client.fallback;

import com.example.common.api.ApiResponse;
import com.example.common.dto.AccountDTO;
import com.example.common.request.AccountDeductRequest;
import com.example.common.response.OperationResult;
import com.example.order.client.AccountClient;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

// OpenFeign的兜底，在Controller中是Sentinel的兜底。
@Component
public class AccountClientFallback implements AccountClient {

    @Override
    public ApiResponse<AccountDTO> getByUserId(Long userId) {
        return ApiResponse.success(new AccountDTO(userId, BigDecimal.ZERO));
    }

    @Override
    public ApiResponse<OperationResult> deduct(AccountDeductRequest request) {
        return ApiResponse.success(OperationResult.fail("account service fallback"));
    }
}
