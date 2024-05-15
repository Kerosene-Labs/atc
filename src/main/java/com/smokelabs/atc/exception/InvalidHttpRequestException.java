package com.smokelabs.atc.exception;

import com.smokelabs.atc.util.ErrorCode;
import com.smokelabs.atc.util.HttpStatus;

/**
 * Represents a scenario where we get an invalid HTTP request
 * 
 * ex: missing host header
 */
public class InvalidHttpRequestException extends AtcException {
    public InvalidHttpRequestException() {
        super(HttpStatus.BAD_REQUEST, ErrorCode.ERROR_OCCURRED_DURING_REQUEST_HANDLING);
    }
}
