package com.smokelabs.requestdirector.util;

public enum HttpStatus {
    OK(200, "OK"),
    CREATED(201, "Created"),
    ACCEPTED(202, "Accepted"),
    NO_CONTENT(204, "No Content"),
    REDIRECT(301, "Redirect"),
    BAD_REQUEST(400, "Bad Request"),
    UNAUTHORIZED(401, "Unauthorized"),
    FORBIDDEN(403, "Forbidden"),
    NOT_FOUND(404, "Not Found"),
    METHOD_NOT_ALLOWED(405, "Method Not Allowed"),
    INTERNAL_SERVER_ERROR(500, "Internal Server Error");

    private final int code;
    private String text;

    HttpStatus(int code, String text) {
        this.code = code;
        this.text = text;
    }

    public static HttpStatus getFromCode(int code) {
        for (HttpStatus x : HttpStatus.values()) {
            if (x.getCode() == code) {
                return x;
            }
        }
        throw new RuntimeException(String.format("http status code with int '%s' does not exist", code));
    }

    public int getCode() {
        return this.code;
    }

    public String getText() {
        return this.text;
    }
}
