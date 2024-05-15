package io.kerosenelabs.atc.exception;

import io.kerosenelabs.atc.util.ErrorCode;
import io.kerosenelabs.atc.util.HttpStatus;

/**
 * Represents a scenario in which the consuming service does not have the
 * providing service's resource declared under its consumes block.
 */
public class ConsumingServiceInvalidScopeException extends AtcException {
    public ConsumingServiceInvalidScopeException() {
        super(HttpStatus.FORBIDDEN, ErrorCode.CONSUMING_SERVICE_HAS_INVALID_SCOPE);
    }
}
