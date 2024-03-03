package com.smokelabs.atc.configuration.pojo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.smokelabs.atc.configuration.pojo.server.Server;
import com.smokelabs.atc.configuration.pojo.service.Service;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Configuration {
    private String configuration_version;
    private Server server;
    private Map<String, Service> services;
}