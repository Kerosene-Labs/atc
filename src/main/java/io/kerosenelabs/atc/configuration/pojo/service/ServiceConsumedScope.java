package io.kerosenelabs.atc.configuration.pojo.service;

import java.util.List;

import io.kerosenelabs.atc.server.HttpMethod;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ServiceConsumedScope {
    private List<HttpMethod> methods;
    private String service;
    private String endpoint;
}
