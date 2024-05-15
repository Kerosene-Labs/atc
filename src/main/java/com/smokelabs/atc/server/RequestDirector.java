package com.smokelabs.atc.server;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;

import com.smokelabs.atc.client.HttpForwarder;
import com.smokelabs.atc.configuration.ConfigurationHandler;
import com.smokelabs.atc.configuration.pojo.service.Service;
import com.smokelabs.atc.exception.AtcException;
import com.smokelabs.atc.exception.HeaderNotFoundException;
import com.smokelabs.atc.exception.InvalidHttpRequestException;
import com.smokelabs.atc.exception.ConsumingServiceInvalidScopeException;
import com.smokelabs.atc.exception.ConsumingServiceNotFoundException;
import com.smokelabs.atc.exception.ServiceNotFoundException;
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
    }

    /**
     * Get the base headers (changes depending on the server configuration)
     */
    private void generateBaseResponseHeaders() {
        this.headers = new HashMap<>();
        headers.put("X-RD-Trace", traceId);
    }

    /**
     * If the {@code consuming} service should have access the
     * {@code providing} service.
     * The outcome of this is determined by the declared {@code provides} and
     * {@code consumes} blocks under each respective service in
     * {@code configuration.json}.
     * 
     * @param consuming The {@link Service} making the request
     * @param providing The {@link Service} receiving the request
     * @throws ConsumingServiceInvalidScopeException If the consumnig service
     */
    private boolean isConsumingServiceScopedForProvidingService(Service consuming, Service providing) {
        return true;
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

            // get our consuming service
            Service consuming;
            try {
                consuming = ConfigurationHandler.getByName(httpRequest.getConsumingServiceIdentity());
            } catch (ServiceNotFoundException e) {
                throw new ConsumingServiceNotFoundException(httpRequest.getConsumingServiceIdentity());
            }

            // get our destination service
            Service providingService = ConfigurationHandler.getByHost(requestHost);

            // ensure we have access
            boolean isScoped = isConsumingServiceScopedForProvidingService(consuming, providingService);
            if (!isScoped) {
                throw new ConsumingServiceInvalidScopeException();
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
