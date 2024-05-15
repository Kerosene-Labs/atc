package io.kerosenelabs.atc.server;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;

import io.kerosenelabs.atc.client.HttpForwarder;
import io.kerosenelabs.atc.configuration.ConfigurationHandler;
import io.kerosenelabs.atc.configuration.pojo.service.Service;
import io.kerosenelabs.atc.configuration.pojo.service.ServiceConsumedScope;
import io.kerosenelabs.atc.configuration.pojo.service.ServiceProvidedScope;
import io.kerosenelabs.atc.exception.AtcException;
import io.kerosenelabs.atc.exception.ConsumingServiceInvalidScopeException;
import io.kerosenelabs.atc.exception.ConsumingServiceNotFoundException;
import io.kerosenelabs.atc.exception.HeaderNotFoundException;
import io.kerosenelabs.atc.exception.InvalidHttpRequestException;
import io.kerosenelabs.atc.exception.ProvidingServiceInvalidScopeException;
import io.kerosenelabs.atc.exception.ServiceNotFoundException;
import io.kerosenelabs.atc.util.HttpStatus;

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
     * @throws ProvidingServiceInvalidScopeException If the providing service does
     *                                               not have this resource declared
     *                                               under its {@code provides}
     *                                               block.
     * @throws ConsumingServiceInvalidScopeException If the consumnig service does
     *                                               not have this resource from the
     *                                               provider declared under its
     *                                               {@code consumes} block.
     */
    private void ensureRequestScopedForProvidingService(Service consuming, Service providing)
            throws ProvidingServiceInvalidScopeException, ConsumingServiceInvalidScopeException {
        // determine our pre-requisites (method & resource)
        HttpMethod method = httpRequest.getMethod();
        String resource = httpRequest.getResource();

        // iter over the consuming services consumed scopes, ensure we should be able to
        // consume it. being able to consume it means that the consumed service name
        // matches the provider, the endpoint/resource match and the HTTP method match.
        ServiceConsumedScope consumedScope = null;
        for (ServiceConsumedScope iterConsumedScope : consuming.getConsumes()) {
            if (iterConsumedScope.getService().equals(providing.getName())
                    && iterConsumedScope.getEndpoint().equals(resource)
                    && iterConsumedScope.getMethods().contains(method)) {
                consumedScope = iterConsumedScope;
            }
        }
        if (consumedScope == null) {
            throw new ConsumingServiceInvalidScopeException();
        }

        // iter over the providing services provided scopes, ensure it fits our reqs
        ServiceProvidedScope providedScope = null;
        for (ServiceProvidedScope iterProvidedScope : providing.getProvides()) {
            if (iterProvidedScope.getEndpoint().equals(resource) && iterProvidedScope.getMethods().contains(method)) {
                providedScope = iterProvidedScope;
            }
        }
        if (providedScope == null) {
            throw new ProvidingServiceInvalidScopeException();
        }
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

            // ensure the requesting service has scope to the providing service
            ensureRequestScopedForProvidingService(consuming, providingService);

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
