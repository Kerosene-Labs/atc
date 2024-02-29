package com.smokelabs.requestdirector.exception;

/**
 * Represent a scenario in which an HTTP message could not be parsed
 */
public class MalformedHttpMessage extends Exception {
    public MalformedHttpMessage(String message) {
        super(message);
    }
}
