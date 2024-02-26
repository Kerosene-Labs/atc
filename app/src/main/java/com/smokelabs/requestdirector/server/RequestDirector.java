package com.smokelabs.requestdirector.server;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.smokelabs.requestdirector.configuration.ConfigurationHandler;
import com.smokelabs.requestdirector.configuration.pojo.Configuration;
import com.smokelabs.requestdirector.configuration.pojo.service.Service;
import com.smokelabs.requestdirector.util.ErrorCode;
import com.smokelabs.requestdirector.util.HttpStatus;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

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
    public RequestDirector(HttpRequest httpRequest) {
        this.httpRequest = httpRequest;
        this.loadedConfiguration = ConfigurationHandler.getInstance().getLoadedConfiguration();
    }

    /**
     * Get a Trace ID for this request/response
     * 
     * @return
     */
    private void generateTraceId() {
        traceId = UUID.randomUUID().toString();
    }

    /**
     * Get the base headers (changes depending on the server configuration)
     * 
     * @return
     */
    private void generateBaseResponseHeaders() {
        this.headers = new HashMap<>();
        headers.put("X-RD-Trace", traceId);
    }

    /**
     * Determine what to do with this request
     * 
     * @return HttpResponse object to be sent over the socket
     */
    public HttpResponse directRequest() {
        // generate a trace id
        generateTraceId();

        // generate our response headers
        generateBaseResponseHeaders();

        // determine our request host
        String requestHost = httpRequest.getHeaders().get("host");

        // set a base response
        HttpResponse httpResponse = new HttpResponse(HttpStatus.OK, headers, requestHost);

        // handle determining the routing of this request
        boolean matchingServiceFound = false;
        Service service;
        for (String serviceName : loadedConfiguration.getServices().keySet()) {
            service = loadedConfiguration.getServices().get(serviceName);

            // todo call this service, do all that jazz
            if (service.getAddress() == requestHost) {
                matchingServiceFound = true;
            }
        }

        // if a service couldn't be found
        if (!matchingServiceFound) {
            headers.put("X-RD-Error", ErrorCode.SERVICE_NOT_FOUND.getCode());
            httpResponse = new HttpResponse(HttpStatus.BAD_REQUEST, headers, null);
        }

        // return our response back for transport over the socket
        return httpResponse;
    }

}
