package io.kerosenelabs.atc.configuration.model;

import java.util.List;

import io.kerosenelabs.kindling.constant.HttpMethod;
import lombok.*;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
@ToString
public class Configuration {
    private List<Service> services;
    private Api api;

    @Data
    @AllArgsConstructor
    @Builder
    @NoArgsConstructor
    @ToString
    public static class Service {
        private String name;
        private String description;
        private String maintainer;
        private String identityToken;
        private List<String> hosts;
        private List<ProvidingEndpoint> provides;
        private List<ConsumingEndpoint> consumes;

        @Data
        @AllArgsConstructor
        @Builder
        @NoArgsConstructor
        @ToString
        public static class ProvidingEndpoint {
            private String endpoint;
            private List<HttpMethod> methods;
        }

        @Data
        @AllArgsConstructor
        @Builder
        @NoArgsConstructor
        @ToString
        public static class ConsumingEndpoint {
            private String service;
            private String endpoint;
            private List<HttpMethod> methods;
        }
    }
}