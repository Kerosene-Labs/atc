package io.kerosenelabs.atc.exception;

import io.kerosenelabs.atc.util.ErrorCode;
import io.kerosenelabs.atc.util.HttpStatus;

public class ServiceNotFoundException extends AtcException {
    public ServiceNotFoundException() {
        super(HttpStatus.NOT_IMPLEMENTED, ErrorCode.SERVICE_NOT_FOUND);
    }
}
