package com.smokelabs.atc.exception;

import com.smokelabs.atc.util.ErrorCode;
import com.smokelabs.atc.util.HttpStatus;

/**
 * Represent a scenario in which an HTTP message could not be parsed
 */
public class MalformedHttpMessage extends AtcException {
    public MalformedHttpMessage(String message) {
        super(HttpStatus.BAD_REQUEST, ErrorCode.ERROR_OCCURRED_DURING_REQUEST_HANDLING);
    }
}
