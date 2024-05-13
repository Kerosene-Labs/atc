package com.smokelabs.atc.configuration.pojo.service;

import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Service {
    private String description;
    private String maintainer;
    private List<String> hosts;
    private String scopesPrefix;
    private Map<String, ServiceScope> scopes;
    private Map<String, Map<String, ServiceConsumedScope>> consumes;
}
