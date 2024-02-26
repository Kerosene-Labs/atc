package com.smokelabs.requestdirector.util;

public enum ErrorCode {
    MISMATCHED_HOST_HEADER("00000001");

    private final String code;

    ErrorCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return this.code;
    }
}
