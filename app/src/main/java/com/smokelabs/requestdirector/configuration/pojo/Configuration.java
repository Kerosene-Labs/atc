package com.smokelabs.requestdirector.configuration.pojo;

import com.smokelabs.requestdirector.configuration.pojo.server.Server;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Configuration {
    private String configuration_version;
    private Server server;
    private Object services;
}