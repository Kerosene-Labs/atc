package io.kerosenelabs.atc.scope;

import java.util.List;

import io.kerosenelabs.atc.server.HttpMethod;

/**
 * A {@code ScopeGraphRelationship} represents the relationship between a
 * requesting services {@code consumes} declaration with a destination services
 * {@code scopes} declaration. A {@code ScopeGraphRelationship} is
 * shared per-endpoint, perservice, and details all of the allowed HTTP methods
 * that are under both the {@code consumes} and {@code scopes}.
 * 
 * Many {@code ScopeGraphRelationship}s make up a {@link ScopeGraph}, which can
 * be queried, for example if a {@link Service} should have access to another.
 */
public class ScopeGraphRelationship {
    private final String commonEndpoint;
    private final List<HttpMethod> commonMethods;

    private ScopeGraphRelationship(Builder builder) {
        this.commonEndpoint = builder.endpoint;
        this.commonMethods = builder.methods;
    }

    public String getCommonEndpoint() {
        return this.commonEndpoint;
    }

    public List<HttpMethod> getCommonMethods() {
        return this.commonMethods;
    }

    public static class Builder {
        private String endpoint;
        private List<HttpMethod> methods;

        public Builder commonEndpoint(String endpoint) {
            this.endpoint = endpoint;
            return this;
        }

        public Builder commonMethods(List<HttpMethod> httpMethods) {
            this.methods = httpMethods;
            return this;
        }

        public Builder addCommonMethod(HttpMethod httpMethod) {
            this.methods.add(httpMethod);
            return this;
        }
    }
}
