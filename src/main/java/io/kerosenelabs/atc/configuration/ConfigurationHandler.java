package io.kerosenelabs.atc.configuration;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import io.kerosenelabs.atc.configuration.model.Configuration;

public class ConfigurationHandler {
    private static ConfigurationHandler instance;
    private Configuration configuration;

    private ConfigurationHandler() throws FileNotFoundException, IOException {
        ObjectMapper objectMapper = new ObjectMapper(new YAMLFactory());
        configuration = objectMapper.readValue(new File("configuration.yml"), Configuration.class);
    }

    public static ConfigurationHandler getInstance() throws FileNotFoundException, IOException {
        if (instance == null) {
            instance = new ConfigurationHandler();
        }
        return instance;
    }

    /**
     * Get all services {@code consumes} blocks.
     * @return A list of {@link Configuration.Service.ConsumingEndpoint}.
     */
    public List<Configuration.Service.ConsumingEndpoint> getAllConsumes() {
        List<Configuration.Service.ConsumingEndpoint> consumes = new ArrayList<>();
        for (Configuration.Service service : configuration.getServices()) {
            consumes.addAll(service.getConsumes());
        }
        return consumes;
    }

    public Configuration getConfiguration() {
        return configuration;
    }
}
