package io.kerosenelabs.atc.configuration.pojo.service;

import java.util.List;

import io.kerosenelabs.atc.server.HttpMethod;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ServiceProvidedScope {
    private String endpoint;
    private List<HttpMethod> methods;
}
