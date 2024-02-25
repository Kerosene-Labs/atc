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
            instance = new ConfigurationHandler();
            log.info("configuration handler initialized");
        }
        return instance;
    }

    public ConfigurationHandler() {
        try {
            try (FileInputStream inputStream = new FileInputStream("configuration.example.yml")) {
                Yaml yaml = new Yaml(new Constructor(Configuration.class, new LoaderOptions()));
                loadedConfiguration = yaml.load(inputStream);
            }
        } catch (FileNotFoundException e) {

        } catch (IOException e) {

        }
    }
}
