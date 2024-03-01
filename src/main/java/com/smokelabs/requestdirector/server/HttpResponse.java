package com.smokelabs.requestdirector.server;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;

import com.smokelabs.requestdirector.util.HttpStatus;

import lombok.Getter;
import lombok.NonNull;

/**
 * A wrapper for HTTP responses
 * 
 * @author hlafaille
 */
public class HttpResponse {
    @Getter
    private HttpStatus httpStatus;

    @Getter
    @NonNull
    private HashMap<String, String> headers = new HashMap<>();

    @Getter
    @NonNull
    private String responseContent;

    public HttpResponse(HttpStatus httpStatus, HashMap<String, String> headers, String responseContent) {
        this.httpStatus = httpStatus;

        if (headers == null) {
            headers = new HashMap<>();
        }
        this.headers = headers;

        this.responseContent = responseContent;
    }

    public String toString() {
        StringBuilder response = new StringBuilder();

        // add our response line
        response.append(String.format("HTTP/1.1 %s %s\r\n", httpStatus.getCode(), httpStatus.getText()));

        // add our Content-Length header
        if (responseContent == null) {
            headers.put("Content-Length", "0");
        } else {
            headers.put("Content-Length", Integer.toString(responseContent.toCharArray().length));
        }

        // add our headers
        for (String key : headers.keySet()) {
            response.append(String.format("%s: %s\r\n", key, headers.get(key)));
        }

        // add the break point between headers and content
        response.append("\r\n");

        // add content
        if (responseContent != null) {
            response.append(responseContent);
        }
        return response.toString();
    }

    public byte[] getBytes(String charsetName) throws UnsupportedEncodingException {
        return toString().getBytes(charsetName);
    }

}
