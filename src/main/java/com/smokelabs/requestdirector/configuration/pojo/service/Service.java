package com.smokelabs.requestdirector.configuration.pojo.service;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Service {
    private String description;
    private String address;
    private String address_postfix;
    private String maintainer;
    private Map<String, ServiceScope> scopes;
}
