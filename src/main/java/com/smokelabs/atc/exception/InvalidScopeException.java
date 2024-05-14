package com.smokelabs.atc.exception;

import com.smokelabs.atc.util.ErrorCode;
import com.smokelabs.atc.util.HttpStatus;

public class InvalidScopeException extends AtcException {
    public InvalidScopeException() {
        super(HttpStatus.FORBIDDEN, ErrorCode.REQUESTING_SERVICE_HAS_INVALID_SCOPE);
    }
}
