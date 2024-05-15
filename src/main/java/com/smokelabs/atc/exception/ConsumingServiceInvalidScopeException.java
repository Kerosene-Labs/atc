package com.smokelabs.atc.exception;

import com.smokelabs.atc.util.ErrorCode;
import com.smokelabs.atc.util.HttpStatus;

/**
 * Represents a scenario in which the consuming service does not have the
 * providing service's resource declared under its consumes block.
 */
public class ConsumingServiceInvalidScopeException extends AtcException {
    public ConsumingServiceInvalidScopeException() {
        super(HttpStatus.FORBIDDEN, ErrorCode.CONSUMING_SERVICE_HAS_INVALID_SCOPE);
    }
}
