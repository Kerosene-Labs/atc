package com.smokelabs.atc.exception;

import com.smokelabs.atc.util.ErrorCode;
import com.smokelabs.atc.util.HttpStatus;

/**
 * Represents a scenario in which the providing service does not have the
 * requested resource declared under its provides block.
 */
public class ProvidingServiceInvalidScopeException extends AtcException {
    public ProvidingServiceInvalidScopeException() {
        super(HttpStatus.FORBIDDEN, ErrorCode.PROVIDING_SERVICE_HAS_INVALID_SCOPE);
    }
}
