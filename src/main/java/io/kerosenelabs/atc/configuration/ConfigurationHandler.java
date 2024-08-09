package io.kerosenelabs.atc.configuration;

public class ConfigurationHandler {
    private static ConfigurationHandler instance;

    private ConfigurationHandler() {
    }

    public static ConfigurationHandler getInstance() {
        if (instance == null) {
            instance = new ConfigurationHandler();
        }
        return instance;
    }
}
