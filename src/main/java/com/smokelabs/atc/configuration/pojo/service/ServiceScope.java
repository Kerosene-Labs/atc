package com.smokelabs.atc.configuration.pojo.service;

import java.util.List;

import com.smokelabs.atc.server.HttpMethod;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ServiceScope {
    private String endpoint;
    private List<HttpMethod> methods;
}
