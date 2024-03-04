package com.smokelabs.atc.server;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.smokelabs.atc.client.HttpForwarder;
import com.smokelabs.atc.configuration.ConfigurationHandler;
import com.smokelabs.atc.configuration.pojo.Configuration;
import com.smokelabs.atc.configuration.pojo.service.Service;
import com.smokelabs.atc.exception.HeaderNotFoundException;
import com.smokelabs.atc.exception.InvalidHttpRequestException;
import com.smokelabs.atc.util.ErrorCode;
import com.smokelabs.atc.util.HttpStatus;

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
    private AtcHttpRequest httpRequest;

    @NonNull
    private String traceId;

    @NonNull
    private HashMap<String, String> headers;

    /**
     * Constructor
     */
    public RequestDirector(AtcHttpRequest httpRequest, String traceId) {
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
    public AtcHttpResponse directRequest() throws IOException, InterruptedException, InvalidHttpRequestException {
        // generate our response headers
        generateBaseResponseHeaders();

        // determine our request host
        try {
            String requestHost = httpRequest.getHeaders().getByName("host").getValue();

            // set a base response
            AtcHttpResponse httpResponse = new AtcHttpResponse(HttpStatus.OK, headers, null);

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
                httpResponse = new AtcHttpResponse(HttpStatus.BAD_REQUEST, headers, null);
            }

            // return our response back for transport over the socket
            return httpResponse;
        } catch (HeaderNotFoundException e) {
            throw new InvalidHttpRequestException("host header is missing");
        }
    }

}
