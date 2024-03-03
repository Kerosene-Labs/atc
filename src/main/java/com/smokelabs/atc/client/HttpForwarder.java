package com.smokelabs.atc.client;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.HashMap;

import com.smokelabs.atc.exception.HeaderNotFoundException;
import com.smokelabs.atc.exception.InvalidHttpRequestException;
import com.smokelabs.atc.server.HttpRequest;
import com.smokelabs.atc.server.HttpResponse;
import com.smokelabs.atc.util.HttpStatus;

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

        // add our header to let the client know we're involved
        responseHeaders.put("X-RD-InLine", "1");

        // convert a stdlib HttpResponse to our custom HttpResponse
        return new HttpResponse(HttpStatus.getFromCode(httpResponse.statusCode()), responseHeaders,
                httpResponse.body());
    }

    public static HttpResponse getResponseFromUpstream(HttpRequest httpRequest)
            throws IOException, InterruptedException, InvalidHttpRequestException {
        HttpClient client = HttpClient.newHttpClient();

        try {
            String hostHeaderValue = httpRequest.getHeaders().getByName("Host").getValue();

            java.net.http.HttpRequest stdHttpRequest = java.net.http.HttpRequest.newBuilder()
                    .uri(URI.create(
                            "https://" + hostHeaderValue + httpRequest.getResource()))
                    .GET()
                    .build();
            java.net.http.HttpResponse<String> upstreamResponse = client.send(stdHttpRequest, BodyHandlers.ofString());
            return convertStdHttpResponseToCustomHttpResponse(upstreamResponse);
        } catch (HeaderNotFoundException e) {
            throw new InvalidHttpRequestException("host header is missing");
        }
    }
}
