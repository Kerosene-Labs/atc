package com.smokelabs.atc.configuration.pojo.service;

import java.util.List;

import com.smokelabs.atc.server.HttpMethod;

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
