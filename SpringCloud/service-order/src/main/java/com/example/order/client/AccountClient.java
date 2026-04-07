package com.example.order.client;

import com.example.common.api.ApiResponse;
import com.example.common.dto.AccountDTO;
import com.example.common.request.AccountDeductRequest;
import com.example.common.response.OperationResult;
import com.example.order.client.fallback.AccountClientFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "service-account", fallback = AccountClientFallback.class)
public interface AccountClient {

    @GetMapping("/accounts/{userId}")
    ApiResponse<AccountDTO> getByUserId(@PathVariable("userId") Long userId);

    @PostMapping("/accounts/deduct")
    ApiResponse<OperationResult> deduct(@RequestBody AccountDeductRequest request);
}
