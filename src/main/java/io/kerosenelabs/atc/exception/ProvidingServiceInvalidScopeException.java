package io.kerosenelabs.atc.exception;

import io.kerosenelabs.atc.util.ErrorCode;
import io.kerosenelabs.atc.util.HttpStatus;

/**
 * Represents a scenario in which the providing service does not have the
 * requested resource declared under its provides block.
 */
public class ProvidingServiceInvalidScopeException extends AtcException {
    public ProvidingServiceInvalidScopeException() {
        super(HttpStatus.FORBIDDEN, ErrorCode.PROVIDING_SERVICE_HAS_INVALID_SCOPE);
    }
}
