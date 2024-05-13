package com.smokelabs.atc.configuration;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smokelabs.atc.configuration.pojo.Configuration;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ConfigurationHandler {
    private static ConfigurationHandler instance = null;

    @Getter
    private Configuration loadedConfiguration;

    public static ConfigurationHandler getInstance() {
        if (instance == null) {
            log.info("loading configuration");
            instance = new ConfigurationHandler();
            log.info("configuration fully loaded");
        }
        return instance;
    }

    public ConfigurationHandler() {
        // load the configuration file
        try {
            try (FileInputStream inputStream = new FileInputStream("/etc/atc/configuration.json")) {
                loadedConfiguration = new ObjectMapper().readValue(inputStream, Configuration.class);
            }
        } catch (FileNotFoundException e) {
            log.error("configuration file not found", e);
            System.exit(1);
        } catch (IOException e) {
            log.error("unable to read configuration file", e);
            System.exit(1);
        }

        // continue handling our configuration
        log.info(String.format("%s service(s) recognized", loadedConfiguration.getServices().size()));
    }
}
