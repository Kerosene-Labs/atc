package com.smokeslabs.requestdirector.server;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;

import lombok.Getter;
import lombok.NonNull;

/**
 * A wrapper for HTTP responses
 */
public class HttpResponse {
    @Getter
    private int status;

    @Getter
    private String statusText;

    @Getter
    @NonNull
    private HashMap<String, String> headers = new HashMap<>();

    @Getter
    @NonNull
    private String responseContent;

    public HttpResponse(int status, String statusText, HashMap<String, String> headers, String responseContent) {
        this.status = status;
        this.statusText = statusText;
        this.headers = headers;
        this.responseContent = responseContent;
    }

    public String toString() {
        StringBuilder response = new StringBuilder();

        // add our response line
        response.append(String.format("HTTP/1.1 %s %s\r\n", status, statusText));

        // add our Content-Length header
        headers.put("Content-Length", Integer.toString(responseContent.toCharArray().length));

        // add our headers
        for (String key : headers.keySet()) {
            response.append(String.format("%s: %s\r\n", key, headers.get(key)));
        }

        // add the break point between headers and content
        response.append("\r\n");

        // add content
        response.append(responseContent);
        return response.toString();
    }

    public byte[] getBytes(String charsetName) throws UnsupportedEncodingException {
        return toString().getBytes(charsetName);
    }

}
