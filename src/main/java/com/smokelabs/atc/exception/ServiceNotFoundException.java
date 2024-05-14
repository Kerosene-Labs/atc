package com.smokelabs.atc.exception;

import com.smokelabs.atc.util.ErrorCode;
import com.smokelabs.atc.util.HttpStatus;

public class ServiceNotFoundException extends AtcException {
    public ServiceNotFoundException(String message) {
        super(HttpStatus.NOT_IMPLEMENTED, ErrorCode.DESTINATION_SERVICE_NOT_FOUND);
    }
}
