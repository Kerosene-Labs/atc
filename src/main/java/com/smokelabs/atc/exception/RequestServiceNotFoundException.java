package com.smokelabs.atc.exception;

import com.smokelabs.atc.util.ErrorCode;
import com.smokelabs.atc.util.HttpStatus;

public class RequestServiceNotFoundException extends AtcException {
    public RequestServiceNotFoundException(String name) {
        super(HttpStatus.BAD_REQUEST, ErrorCode.REQUEST_SERVICE_NOT_FOUND,
                String.format("'%s' is not a valid service", name));
    }
}
