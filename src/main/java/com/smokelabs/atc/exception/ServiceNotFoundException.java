package com.smokelabs.atc.exception;

import com.smokelabs.atc.util.ErrorCode;
import com.smokelabs.atc.util.HttpStatus;

public class ServiceNotFoundException extends AtcException {
    public ServiceNotFoundException() {
        super(HttpStatus.NOT_IMPLEMENTED, ErrorCode.SERVICE_NOT_FOUND);
    }
}
