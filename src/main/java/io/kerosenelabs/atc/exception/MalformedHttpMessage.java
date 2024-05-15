package io.kerosenelabs.atc.exception;

import io.kerosenelabs.atc.util.ErrorCode;
import io.kerosenelabs.atc.util.HttpStatus;

/**
 * Represent a scenario in which an HTTP message could not be parsed
 */
public class MalformedHttpMessage extends AtcException {
    public MalformedHttpMessage(String message) {
        super(HttpStatus.BAD_REQUEST, ErrorCode.ERROR_OCCURRED_DURING_REQUEST_HANDLING);
    }
}
