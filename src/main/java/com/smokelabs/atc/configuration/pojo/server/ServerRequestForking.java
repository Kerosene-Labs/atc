package com.smokelabs.atc.configuration.pojo.server;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ServerRequestForking {
    private boolean enable;
}