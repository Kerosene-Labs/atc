package io.kerosenelabs.atc.client;

import io.kerosenelabs.kindling.HttpRequest;
import io.kerosenelabs.kindling.HttpResponse;
import io.kerosenelabs.kindling.constant.HttpStatus;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SimpleHttpClient {
    public static HttpResponse convertStdHttpResponseToKindlingHttpResponse(java.net.http.HttpResponse<String> stdHttpResponse) {
        // convert std httpresponse to a kindling httpresponse
        HashMap<String, String> headers = new HashMap<>();
        for (Map.Entry<String, List<String>> entry : stdHttpResponse.headers().map().entrySet()) {
            if (entry.getKey().contains(":status")) {
                continue;
            }
            headers.put(entry.getKey(), entry.getValue().getFirst());
        }
        return new HttpResponse.Builder()
                .status(HttpStatus.OK) // <-- TODO: change this to a method that gets an enum member by number value
                .headers(headers)
                .content(stdHttpResponse.body())
                .build();
    }

    /**
     * Convert the Kindling HttpRequest to a stdlib HttpRequest
     * @param httpRequest
     * @return
     * @throws URISyntaxException
     */
    public static java.net.http.HttpRequest convertKindlingHttpRequestToStdHttpRequest(HttpRequest httpRequest) throws URISyntaxException {
        // create the request
        URI hostUri = new URI("https://" + httpRequest.getHeaders().get("Host") + httpRequest.getResource());
        java.net.http.HttpRequest.Builder requestBuilder = java.net.http.HttpRequest.newBuilder().uri(hostUri);

        // do content
        java.net.http.HttpRequest.BodyPublisher bodyPublisher;
        if (httpRequest.getContent() == null) {
            bodyPublisher = java.net.http.HttpRequest.BodyPublishers.noBody();
        } else {
            bodyPublisher = java.net.http.HttpRequest.BodyPublishers.ofString(httpRequest.getContent());
        }

        // set method
        switch (httpRequest.getHttpMethod()) {
            case GET:
                requestBuilder.GET();
                break;
            case POST:
                requestBuilder.POST(bodyPublisher);
                break;
            case DELETE:
                requestBuilder.DELETE();
                break;
            case PUT:
                requestBuilder.PUT(bodyPublisher);
                break;
        }
        return requestBuilder.build();
    }

    /**
     * Get the response from the provider
     * @param request The request from the consumer
     * @return
     */
    public static HttpResponse doCall(HttpRequest request) throws URISyntaxException, IOException, InterruptedException {
        try (HttpClient httpClient = HttpClient.newHttpClient()){
            java.net.http.HttpRequest stdHttpRequest = convertKindlingHttpRequestToStdHttpRequest(request);
            java.net.http.HttpResponse<String> providerResponse = httpClient.send(stdHttpRequest, java.net.http.HttpResponse.BodyHandlers.ofString());
            return convertStdHttpResponseToKindlingHttpResponse(providerResponse);
        }
    }
}
