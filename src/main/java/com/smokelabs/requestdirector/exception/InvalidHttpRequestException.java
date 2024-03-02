package com.smokelabs.requestdirector.exception;

/**
 * Represents a scenario where we get an invalid HTTP request
 * 
 * ex: missing host header
 */
public class InvalidHttpRequestException extends Exception {
    public InvalidHttpRequestException(String message) {
        super(message);
    }
}
