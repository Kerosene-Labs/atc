package com.smokelabs.atc.server.pojo;

import com.smokelabs.atc.server.HttpMethod;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class HttpRequestLine {
    private HttpMethod method;
    private String resource;
    private String protocol;
}
