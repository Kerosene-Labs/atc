package com.smokelabs.atc.exception;

import com.smokelabs.atc.util.ErrorCode;
import com.smokelabs.atc.util.HttpStatus;

/**
 * Base level exception for ATC
 */
public class AtcException extends Exception {
    private HttpStatus httpStatus;
    private ErrorCode errorCode;
    private String message;

    public AtcException(HttpStatus httpStatus, ErrorCode errorCode) {
        this.httpStatus = httpStatus;
        this.errorCode = errorCode;
    }

    public AtcException(HttpStatus httpStatus, ErrorCode errorCode, String message) {
        super(message);
        this.httpStatus = httpStatus;
        this.errorCode = errorCode;
        this.message = message;
    }

    public HttpStatus getHttpStatus() {
        return this.httpStatus;
    }

    public ErrorCode getErrorCode() {
        return this.errorCode;
    }

    public String getMessage() {
        if (this.message == null) {
            this.message = String.format(
                    "This exception has no custom message. Here's what we know:\nError Code: %s\nThe HTTP status code is '%s %s'",
                    errorCode, httpStatus.getCode(),
                    httpStatus.getText());
        }
        return this.message;
    }
}
