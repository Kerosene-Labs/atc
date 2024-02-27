package com.smokelabs.requestdirector.client;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.HashMap;

import com.smokelabs.requestdirector.server.HttpRequest;
import com.smokelabs.requestdirector.server.HttpResponse;
import com.smokelabs.requestdirector.util.HttpStatus;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HttpForwarder {
    private static HttpResponse convertStdHttpResponseToCustomHttpResponse(
            java.net.http.HttpResponse<String> httpResponse) {
        HashMap<String, String> responseHeaders = new HashMap<>();

        // convert the HttpHeaders object to a HashMap<String, String>
        for (String headerName : httpResponse.headers().map().keySet()) {
            if (headerName.equals(":status")) {
                continue;
            }
            responseHeaders.put(headerName, httpResponse.headers().firstValue(headerName).get().toString());
        }

        // convert a stdlib HttpResponse to our custom HttpResponse
        return new HttpResponse(HttpStatus.getFromCode(httpResponse.statusCode()), responseHeaders,
                httpResponse.body());
    }

    public static HttpResponse getResponseFromUpstream(HttpRequest httpRequest)
            throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        java.net.http.HttpRequest stdHttpRequest = java.net.http.HttpRequest.newBuilder()
                .uri(URI.create("https://" + httpRequest.getHeaders().get("host") + httpRequest.getResource()))
                .GET()
                .build();
        java.net.http.HttpResponse<String> upstreamResponse = client.send(stdHttpRequest, BodyHandlers.ofString());
        return convertStdHttpResponseToCustomHttpResponse(upstreamResponse);
    }
}
