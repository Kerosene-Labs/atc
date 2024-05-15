package io.kerosenelabs.atc.server.pojo;

import io.kerosenelabs.atc.server.HttpMethod;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class HttpRequestLine {
    private HttpMethod method;
    private String resource;
    private String protocol;
}
