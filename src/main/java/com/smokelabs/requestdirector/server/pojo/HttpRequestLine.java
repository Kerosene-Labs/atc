package com.smokelabs.requestdirector.server.pojo;

import com.smokelabs.requestdirector.server.HttpMethod;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class HttpRequestLine {
    private HttpMethod method;
    private String resource;
    private String protocol;
}
