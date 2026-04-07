package com.example.account.controller;

import com.example.account.service.AccountService;
import com.example.common.api.ApiResponse;
import com.example.common.dto.AccountDTO;
import com.example.common.request.AccountDeductRequest;
import com.example.common.response.OperationResult;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/accounts")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping("/{userId}")
    public ApiResponse<AccountDTO> getByUserId(@PathVariable Long userId) {
        return ApiResponse.success(accountService.getByUserId(userId));
    }

    @PostMapping("/deduct")
    public ApiResponse<OperationResult> deduct(@Valid @RequestBody AccountDeductRequest request) {
        accountService.deduct(request.getUserId(), request.getAmount());
        return ApiResponse.success(OperationResult.ok("balance deducted"));
    }
}
