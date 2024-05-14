package com.smokelabs.atc.server;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;

import com.smokelabs.atc.client.HttpForwarder;
import com.smokelabs.atc.configuration.ConfigurationHandler;
import com.smokelabs.atc.configuration.pojo.Configuration;
import com.smokelabs.atc.configuration.pojo.service.Service;
import com.smokelabs.atc.configuration.pojo.service.ServiceScope;
import com.smokelabs.atc.exception.AtcException;
import com.smokelabs.atc.exception.HeaderNotFoundException;
import com.smokelabs.atc.exception.InvalidHttpRequestException;
import com.smokelabs.atc.exception.InvalidScopeException;
import com.smokelabs.atc.exception.RequestServiceNotFoundException;
import com.smokelabs.atc.exception.ServiceNotFoundException;
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
     * If the {@code requestingService} should have access the
     * {@code destinationService}.
     * The outcome of this is determined by the declared {@code scopes} and
     * {@code consumes} blocks under each respective service in
     * {@code configuration.json}.
     * 
     * @param requestingService  The {@link Service} making the request
     * @param destinationService The {@link Service} receiving the request
     */
    private boolean isRequestingScopedForDestination(Service requestingService, Service destinationService) {
        // find a matching scope on the destination
        ServiceScope matchedScope = null;
        for (ServiceScope scope : destinationService.getScopes()) {
            if (httpRequest.getResource().equals(scope)) {
                matchedScope = scope;
            }
        }

        // if we didn't find a matched scope, we're not scoped for this destination
        if (matchedScope == null) {
            return false;
        }
        return true;

        // ensure we have the methods on this scope
        // httpRequest.getMethod().toString()
        // if (requestingService.getConsumes().get(destinationService)) {

        // }
    }

    /**
     * Determine what to do with this request
     * 
     * @return HttpResponse object to be sent over the socket
     * @throws InterruptedException
     * @throws IOException
     * @throws URISyntaxException
     */
    public AtcHttpResponse directRequest()
            throws IOException, InterruptedException, AtcException, URISyntaxException {
        // generate our response headers
        generateBaseResponseHeaders();

        // determine our request host
        try {
            String requestHost = httpRequest.getHeaders().getByName("host").getValue();

            // set a base response
            AtcHttpResponse httpResponse = new AtcHttpResponse(HttpStatus.OK, headers, null);

            // get our requesting service
            Service requestingService;
            try {
                requestingService = ConfigurationHandler.getByName(httpRequest.getRequestingServiceIdentity());
            } catch (ServiceNotFoundException e) {
                throw new RequestServiceNotFoundException(httpRequest.getRequestingServiceIdentity());
            }

            // get our destination service
            Service destinationService = ConfigurationHandler.getByHost(requestHost);

            // ensure we have access
            boolean isScoped = isRequestingScopedForDestination(requestingService, destinationService);
            if (!isScoped) {
                throw new InvalidScopeException();
            }

            log.info(String.format("sending request to upstream (%s)",
                    httpRequest.getHeaders().getByName("host").getValue()));
            httpResponse = HttpForwarder.getResponseFromUpstream(httpRequest);

            // return our response back for transport over the socket
            return httpResponse;
        } catch (HeaderNotFoundException e) {
            throw new InvalidHttpRequestException();
        }
    }

}
