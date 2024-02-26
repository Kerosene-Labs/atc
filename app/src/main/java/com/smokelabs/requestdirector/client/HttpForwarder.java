package com.smokelabs.requestdirector.client;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpResponse.BodyHandlers;

import com.smokelabs.requestdirector.server.HttpRequest;
import com.smokelabs.requestdirector.server.HttpResponse;

public class HttpForwarder {
    public void getResponseFromUpstream(HttpRequest httpRequest) {
        HttpClient client = HttpClient.newHttpClient();
        java.net.http.HttpRequest stdHttpRequest = java.net.http.HttpRequest.newBuilder()
                .uri(URI.create("google.com"))
                .GET()
                .build();
        try {
            java.net.http.HttpResponse<String> response = client.send(stdHttpRequest, BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
