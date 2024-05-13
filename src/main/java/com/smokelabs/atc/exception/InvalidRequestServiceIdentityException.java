package com.smokelabs.atc.exception;

public class InvalidRequestServiceIdentityException extends Exception {
    public InvalidRequestServiceIdentityException() {
        super("Request is missing 'X-RD-ServiceIdentity' header, which is required for ALL requests.");
    }
}
