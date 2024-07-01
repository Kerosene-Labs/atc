package io.kerosenelabs.atc.exception;

import io.kerosenelabs.atc.util.ErrorCode;
import io.kerosenelabs.atc.util.HttpStatus;

public class ConsumingServiceNotFoundException extends AtcException {
    public ConsumingServiceNotFoundException(String name) {
        super(HttpStatus.BAD_REQUEST, ErrorCode.REQUEST_SERVICE_NOT_FOUND,
                String.format("'%s' is not a valid service", name));
    }
}
