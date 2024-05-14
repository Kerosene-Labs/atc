package com.smokelabs.atc.configuration.pojo;

import java.util.Map;

import com.smokelabs.atc.configuration.pojo.service.Service;
import com.smokelabs.atc.exception.ServiceNotFoundException;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Configuration {
    private Map<String, Service> services;
}