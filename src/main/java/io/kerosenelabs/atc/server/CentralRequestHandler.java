package io.kerosenelabs.atc.server;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.kerosenelabs.atc.client.SimpleHttpClient;
import io.kerosenelabs.atc.configuration.ConfigurationHandler;
import io.kerosenelabs.atc.configuration.model.Configuration;
import io.kerosenelabs.atc.model.ExceptionErrorResponse;
import io.kerosenelabs.kindling.HttpRequest;
import io.kerosenelabs.kindling.HttpResponse;
import io.kerosenelabs.kindling.constant.HttpStatus;
import io.kerosenelabs.kindling.exception.KindlingException;
import io.kerosenelabs.kindling.handler.RequestHandler;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.URISyntaxException;
import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

@Slf4j
public class CentralRequestHandler extends RequestHandler {
    private final ConfigurationHandler configurationHandler;
    private final List<Configuration.Service> services;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public CentralRequestHandler() throws IOException {
        configurationHandler = ConfigurationHandler.getInstance();
        services = configurationHandler.getConfiguration().getServices();
        ;
        log.info("Central request handler initialized");
    }

    @Override
    public HttpResponse handle(HttpRequest httpRequest) throws KindlingException {
        // record when we started working this request
        Instant before = Instant.now();

        // get our identity token
        String identityToken = httpRequest.getHeaders().get("X-ATC-IdentityToken");
        if (identityToken == null) {
            throw new KindlingException("X-ATC-IdentityToken header is missing");
        }

        // iterate over services, find a service with this identity token
        Configuration.Service requestingService = null;
        for (Configuration.Service service : services) {
            if (service.getIdentityToken().equals(identityToken)) {
                requestingService = service;
            }
        }
        if (requestingService == null) {
            throw new KindlingException("A service could not be found with the specified identity token");
        }

        // iterate over services, ensure requestingService consumes a provided endpoint
        Configuration.Service.ConsumingEndpoint consumedEndpoint = null;
        for (Configuration.Service service : services) {
            for (Configuration.Service.ConsumingEndpoint consumes : service.getConsumes()) {
                if (consumes.getEndpoint().equals(httpRequest.getResource()) && consumes.getMethods().contains(httpRequest.getHttpMethod())) {
                    consumedEndpoint = consumes;
                    break;
                }
            }
        }
        if (consumedEndpoint == null) {
            throw new KindlingException("Disallowed request, consumer does not declare the provider as consumed");
        }

        // get a service by our host
        Configuration.Service providingService = null;
        for (Configuration.Service service : services) {
            if (service.getHosts().contains(httpRequest.getHeaders().get("Host"))) {
                providingService = service;
                break;
            }
        }
        if (providingService == null) {
            throw new KindlingException("A provider with the specified host could not be found");
        }

        // iterate over services, ensure that we have a service providing this endpoint
        Configuration.Service.ProvidingEndpoint providedEndpoint = null;
        for (Configuration.Service.ProvidingEndpoint provides : providingService.getProvides()) {
            if (provides.getEndpoint().equals(httpRequest.getResource()) && provides.getMethods().contains(httpRequest.getHttpMethod())) {
                providedEndpoint = provides;
                break;
            }
        }
        if (providedEndpoint == null) {
            throw new KindlingException("Disallowed request, provider does not provide this endpoint");
        }

        // forward the request, get our response
        HttpResponse httpResponse;
        try {
            httpResponse = SimpleHttpClient.doCall(httpRequest);
        } catch (URISyntaxException | IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }

        // log our request time in the appropriate format
        Instant after = Instant.now();
        long duration = Duration.between(before, after).toMillis();
        String durationUnit = "ms";
        if (duration == 0) {
            duration = Duration.between(before, after).toNanos() / 1_000;
            durationUnit = "us";
        }
        log.debug("Request took: {}{}", duration, durationUnit);
        return httpResponse;
    }

    @Override
    public boolean accepts(HttpRequest httpRequest) throws KindlingException {
        return true;
    }

    @SneakyThrows
    @Override
    public HttpResponse handleError(Throwable t) {
        log.error("An error occurred while processing a request", t);

        // if a kindling exception was thrown OR we're in allowExceptionsInErrorResponse mode
        String stackTraceInErrorResponse = System.getProperty("atc.http.stackTraceInErrorResponse");
        if (stackTraceInErrorResponse != null && (stackTraceInErrorResponse.equals("true"))) {
            return new HttpResponse.Builder()
                    .status(HttpStatus.BAD_REQUEST)
                    .headers(new HashMap<>() {
                        {
                            put("Content-Type", "application/json");
                        }
                    })
                    .content(objectMapper.writeValueAsString(
                                    new ExceptionErrorResponse(
                                            t.getMessage(),
                                            Arrays.stream(t.getStackTrace())
                                                    .map(Object::toString)
                                                    .toList())
                            )
                    )
                    .build();
        }
        return new HttpResponse.Builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .headers(new HashMap<>() {
                    {
                        put("Content-Type", "application/json");
                    }
                })
                .content("{\"message\": \"Gateway error. Please contact the system administrator.\"}")
                .build();
    }
}
