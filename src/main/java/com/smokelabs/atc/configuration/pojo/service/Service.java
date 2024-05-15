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
    private String name;
    private String description;
    private String maintainer;
    private List<String> hosts;
    private String scopesPrefix;
    private List<ServiceScope> provides;
    private List<ServiceConsumedScope> consumes;
}
