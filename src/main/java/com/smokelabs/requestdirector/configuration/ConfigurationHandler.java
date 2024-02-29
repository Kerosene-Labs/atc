package com.smokelabs.requestdirector.configuration;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import com.smokelabs.requestdirector.configuration.pojo.Configuration;

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
            try (FileInputStream inputStream = new FileInputStream("configuration.example.yml")) {
                Yaml yaml = new Yaml(new Constructor(Configuration.class, new LoaderOptions()));
                loadedConfiguration = yaml.load(inputStream);
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
