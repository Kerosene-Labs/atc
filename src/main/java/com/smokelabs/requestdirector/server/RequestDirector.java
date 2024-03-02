package com.smokelabs.requestdirector.server;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.smokelabs.requestdirector.client.HttpForwarder;
import com.smokelabs.requestdirector.configuration.ConfigurationHandler;
import com.smokelabs.requestdirector.configuration.pojo.Configuration;
import com.smokelabs.requestdirector.configuration.pojo.service.Service;
import com.smokelabs.requestdirector.exception.HeaderNotFoundException;
import com.smokelabs.requestdirector.exception.InvalidHttpRequestException;
import com.smokelabs.requestdirector.util.ErrorCode;
import com.smokelabs.requestdirector.util.HttpStatus;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

/**
 * Routes a request to its destined service
 * 
 * @author hlafaille
 */
@Slf4j
public class RequestDirector {
    private Configuration loadedConfiguration;

    @NonNull
    private HttpRequest httpRequest;

    @NonNull
    private String traceId;

    @NonNull
    private HashMap<String, String> headers;

    /**
     * Constructor
     */
    public RequestDirector(HttpRequest httpRequest, String traceId) {
        this.httpRequest = httpRequest;
        this.traceId = traceId;
        this.loadedConfiguration = ConfigurationHandler.getInstance().getLoadedConfiguration();
    }

    /**
     * Get the base headers (changes depending on the server configuration)
     */
    private void generateBaseResponseHeaders() {
        this.headers = new HashMap<>();
        headers.put("X-RD-Trace", traceId);
    }

    /**
     * Determine what to do with this request
     * 
     * @return HttpResponse object to be sent over the socket
     * @throws InterruptedException
     * @throws IOException
     * @throws InvalidHttpRequestException
     */
    public HttpResponse directRequest() throws IOException, InterruptedException, InvalidHttpRequestException {
        // generate our response headers
        generateBaseResponseHeaders();

        // determine our request host
        try {
            String requestHost = httpRequest.getHeaders().getByName("host").getValue();

            // set a base response
            HttpResponse httpResponse = new HttpResponse(HttpStatus.OK, headers, null);

            // handle determining the routing of this request
            boolean matchingServiceFound = false;
            Service service;
            for (String serviceName : loadedConfiguration.getServices().keySet()) {
                service = loadedConfiguration.getServices().get(serviceName);

                // todo call this service, do all that jazz
                if (service.getAddress().equals(requestHost)) {
                    matchingServiceFound = true;
                }
            }

            // if a service was found
            if (matchingServiceFound) {
                log.info(String.format("sending request to upstream (%s)",
                        httpRequest.getHeaders().getByName("host").getValue()));
                httpResponse = HttpForwarder.getResponseFromUpstream(httpRequest);
            } else if (!matchingServiceFound) {
                headers.put("X-RD-Error", ErrorCode.SERVICE_NOT_FOUND.getCode());
                httpResponse = new HttpResponse(HttpStatus.BAD_REQUEST, headers, null);
            }

            // return our response back for transport over the socket
            return httpResponse;
        } catch (HeaderNotFoundException e) {
            throw new InvalidHttpRequestException("host header is missing");
        }
    }

}
