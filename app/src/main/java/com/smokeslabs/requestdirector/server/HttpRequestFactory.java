package com.smokeslabs.requestdirector.server;

import java.util.Arrays;
import java.util.List;

public class HttpRequestFactory {
    private static HttpRequestFactory instance;

    public static HttpRequestFactory getInstance() {
        if (instance == null) {
            instance = new HttpRequestFactory();
        }
        return instance;
    }

}
