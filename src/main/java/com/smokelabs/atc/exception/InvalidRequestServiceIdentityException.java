package com.smokelabs.atc.exception;

import com.smokelabs.atc.util.ErrorCode;
import com.smokelabs.atc.util.HttpStatus;

public class InvalidRequestServiceIdentityException extends AtcException {
    public InvalidRequestServiceIdentityException() {
        super(HttpStatus.BAD_REQUEST, ErrorCode.ERROR_OCCURRED_DURING_REQUEST_HANDLING,
                "Request is missing 'X-RD-ServiceIdentity' header, which is required for ALL requests.");
    }
}
