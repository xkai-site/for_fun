package com.example.common.response;

public class OperationResult {

    private boolean success;
    private String message;

    public OperationResult() {
    }

    public OperationResult(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public static OperationResult ok(String message) {
        return new OperationResult(true, message);
    }

    public static OperationResult fail(String message) {
        return new OperationResult(false, message);
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
