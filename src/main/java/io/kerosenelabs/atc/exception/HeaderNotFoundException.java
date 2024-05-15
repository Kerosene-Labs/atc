package io.kerosenelabs.atc.exception;

/**
 * Represents when the programmer tries to get an HttpHeader object from an
 * HttpHeaderContainer that could not be found.
 */
public class HeaderNotFoundException extends Exception {
    public HeaderNotFoundException(String name) {
        super(String.format("header with name='%s' not found", name));
    }
}
