package io.kerosenelabs.atc.exception;

import io.kerosenelabs.atc.util.ErrorCode;
import io.kerosenelabs.atc.util.HttpStatus;

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
