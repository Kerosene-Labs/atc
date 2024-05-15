package io.kerosenelabs.atc.client;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.HashMap;

import io.kerosenelabs.atc.exception.HeaderNotFoundException;
import io.kerosenelabs.atc.exception.InvalidHttpRequestException;
import io.kerosenelabs.atc.server.AtcHttpRequest;
import io.kerosenelabs.atc.server.AtcHttpResponse;
import io.kerosenelabs.atc.server.HttpMethod;
import io.kerosenelabs.atc.util.HttpStatus;

public class HttpForwarder {
    /**
     * 
     * @param httpResponse
     * @return
     */
    private static AtcHttpResponse convertStdHttpResponseToAtcHttpResponse(HttpResponse<String> httpResponse) {
        HashMap<String, String> responseHeaders = new HashMap<>();

        // convert the HttpHeaders object to a HashMap<String, String>
        for (String headerName : httpResponse.headers().map().keySet()) {
            if (headerName.equals(":status")) {
                continue;
            }
            responseHeaders.put(headerName, httpResponse.headers().firstValue(headerName).get().toString());
        }

        // add our header to let the client know we're involved
        responseHeaders.put("X-ATC-InLine", "1");

        // convert a stdlib HttpResponse to our custom HttpResponse
        return new AtcHttpResponse(HttpStatus.getFromCode(httpResponse.statusCode()), responseHeaders,
                httpResponse.body());
    }

    /**
     * Converts an AtcHttpRequest to a stdlib HttpRequest
     * 
     * @param atcHttpRequest AtcHttpRequest object
     * @return HttpRequest object
     * @throws HeaderNotFoundException
     * @throws URISyntaxException
     */
    private static HttpRequest convertAtcRequestToStdRequest(AtcHttpRequest atcHttpRequest)
            throws URISyntaxException, HeaderNotFoundException {
        URI hostUri = new URI(
                "https://" + atcHttpRequest.getHeaders().getByName("Host").getValue() + atcHttpRequest.getResource());
        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder(hostUri);

        HttpRequest.BodyPublisher bodyPublisher = BodyPublishers.noBody();
        if (atcHttpRequest.getContent() != null) {
            bodyPublisher = HttpRequest.BodyPublishers.ofString(atcHttpRequest.getContent());
        }

        switch (atcHttpRequest.getMethod()) {
            case HttpMethod.GET:
                requestBuilder.GET();
                break;
            case HttpMethod.POST:
                requestBuilder.POST(bodyPublisher);
                break;
            case HttpMethod.DELETE:
                requestBuilder.DELETE();
                break;
            case HttpMethod.PUT:
                requestBuilder.PUT(bodyPublisher);
                break;
        }

        return requestBuilder.build();
    }

    /**
     * Get an AtcHttpResponse from the upstream by sending the request
     * 
     * @param httpRequest AtcHttpRequest object
     * @return AtcHttpResponse object
     * @throws URISyntaxException
     * @throws IOException
     * @throws InterruptedException
     * @throws InvalidHttpRequestException
     * @throws HeaderNotFoundException
     */
    public static AtcHttpResponse getResponseFromUpstream(AtcHttpRequest atcHttpRequest) throws URISyntaxException,
            IOException, InterruptedException, InvalidHttpRequestException, HeaderNotFoundException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest httpRequest = convertAtcRequestToStdRequest(atcHttpRequest);
        HttpResponse<String> upstreamResponse = client.send(httpRequest, BodyHandlers.ofString());
        return convertStdHttpResponseToAtcHttpResponse(upstreamResponse);
    }
}
