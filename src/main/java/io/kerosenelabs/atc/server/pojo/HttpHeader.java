package io.kerosenelabs.atc.server.pojo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class HttpHeader {
    private String name;
    private String value;
}
