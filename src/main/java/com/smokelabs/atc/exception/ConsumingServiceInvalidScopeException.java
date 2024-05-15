package com.smokelabs.atc.exception;

import com.smokelabs.atc.util.ErrorCode;
import com.smokelabs.atc.util.HttpStatus;

public class ConsumingServiceInvalidScopeException extends AtcException {
    public ConsumingServiceInvalidScopeException() {
        super(HttpStatus.FORBIDDEN, ErrorCode.REQUESTING_SERVICE_HAS_INVALID_SCOPE);
    }
}
