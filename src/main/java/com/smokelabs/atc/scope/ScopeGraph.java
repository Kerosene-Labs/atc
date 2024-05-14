package com.smokelabs.atc.scope;

import java.util.List;

import com.smokelabs.atc.server.HttpMethod;

import lombok.Builder;

/**
 * A {@code ScopeGraph} represents the relationship between a requesting
 * services {@code consumes} declaration with a destination services
 * {@code scopes} declaration. A {@code ScopeGraph} is shared per-endpoint, per
 * service, and details all of the allowed HTTP methods that are under both the
 * {@code consumes} and {@code scopes}.
 */
public class ScopeGraph {
    private final String endpoint;
    private final List<HttpMethod> methods;

    private ScopeGraph(Builder builder) {
        this.endpoint = builder.endpoint;
        this.methods = builder.methods;
    }

    public static class Builder {
        private String endpoint;
        private List<HttpMethod> methods;

        public Builder endpoint(String endpoint) {
            this.endpoint = endpoint;
            return this;
        }

        public Builder methods(List<HttpMethod> httpMethods) {
            this.methods = httpMethods;
            return this;
        }

        public Builder addMethod(HttpMethod httpMethod) {
            this.methods.add(httpMethod);
            return this;
        }
    }
}
