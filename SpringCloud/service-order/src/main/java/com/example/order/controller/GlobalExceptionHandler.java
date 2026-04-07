package com.example.order.controller;

import com.example.common.api.ApiResponse;
import jakarta.validation.ConstraintViolationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ApiResponse<Void> handleIllegalArgument(IllegalArgumentException ex) {
        return ApiResponse.fail(400, ex.getMessage());
    }

    @ExceptionHandler({IllegalStateException.class, MethodArgumentNotValidException.class, ConstraintViolationException.class})
    public ApiResponse<Void> handleIllegalState(Exception ex) {
        return ApiResponse.fail(500, ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ApiResponse<Void> handleOthers(Exception ex) {
        return ApiResponse.fail(500, "Unexpected error: " + ex.getMessage());
    }
}
