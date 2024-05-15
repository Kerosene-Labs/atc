package io.kerosenelabs.atc.configuration.pojo.service;

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
    private String providesPrefix;
    private List<ServiceProvidedScope> provides;
    private List<ServiceConsumedScope> consumes;
}
