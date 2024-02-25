package com.smokelabs.requestdirector.configuration.pojo.server;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * SERVER
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Server {
    private ServerResponses responses;
    private ServerRequestForking request_forking;
}