package com.smokelabs.requestdirector.configuration;

public class ConfigurationHandler {
    private static ConfigurationHandler instance = null;

    public static ConfigurationHandler getInstance() {
        if (instance == null) {
            instance = new ConfigurationHandler();
        }
        return instance;
    }

}
