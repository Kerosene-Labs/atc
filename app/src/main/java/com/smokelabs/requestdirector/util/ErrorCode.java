package com.smokelabs.requestdirector.util;

public enum ErrorCode {
    SERVICE_NOT_FOUND("00000001"),
    ERROR_OCCURRED_DURING_REQUEST_HANDLING("00000002");

    private final String code;

    ErrorCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return this.code;
    }
}
