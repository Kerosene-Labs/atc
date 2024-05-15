package io.kerosenelabs.atc.exception;

import io.kerosenelabs.atc.util.ErrorCode;
import io.kerosenelabs.atc.util.HttpStatus;

public class InvalidRequestServiceIdentityException extends AtcException {
    public InvalidRequestServiceIdentityException() {
        super(HttpStatus.BAD_REQUEST, ErrorCode.ERROR_OCCURRED_DURING_REQUEST_HANDLING,
                "Request is missing 'X-ATC-ServiceIdentity' header, which is required for ALL requests.");
    }
}
