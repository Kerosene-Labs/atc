package com.smokeslabs.requestdirector;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;

import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;

import com.smokeslabs.requestdirector.exception.MalformedHttpMessage;
import com.smokeslabs.requestdirector.server.HttpRequest;
import com.smokeslabs.requestdirector.server.HttpResponse;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class App {
    public static void main(String[] args) throws IOException, MalformedHttpMessage {
        System.out.println(
                "  _____  ______ ____  _    _ ______  _____ _______     \n" + //
                        " |  __ \\|  ____/ __ \\| |  | |  ____|/ ____|__   __|    \n" + //
                        " | |__) | |__ | |  | | |  | | |__  | (___    | |       \n" + //
                        " |  _  /|  __|| |  | | |  | |  __|  \\___ \\   | |       \n" + //
                        " | | \\ \\| |___| |__| | |__| | |____ ____) |  | |       \n" + //
                        " |_|__\\_\\______\\___\\_\\\\____/|______|_____/___|_|_____  \n" + //
                        " |  __ \\_   _|  __ \\|  ____/ ____|__   __/ __ \\|  __ \\ \n" + //
                        " | |  | || | | |__) | |__ | |       | | | |  | | |__) |\n" + //
                        " | |  | || | |  _  /|  __|| |       | | | |  | |  _  / \n" + //
                        " | |__| || |_| | \\ \\| |___| |____   | | | |__| | | \\ \\ \n" + //
                        " |_____/_____|_|  \\_\\______\\_____|  |_|  \\____/|_|  \\_\\\n" + //
                        "");

        // set where our keystore lives
        System.setProperty("javax.net.ssl.keyStore", "keystore.p12");
        System.setProperty("javax.net.ssl.keyStorePassword", "my-secure-pw");

        // create the tls socket
        SSLServerSocketFactory factory = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();
        SSLServerSocket sslServerSocket = (SSLServerSocket) factory.createServerSocket(8443);
        log.info("tls socket created: awaiting clients");

        while (true) {
            try (SSLSocket socket = (SSLSocket) sslServerSocket.accept()) {
                try {
                    handleClient(socket);
                } catch (Exception e) {
                    log.error("exception occurred while handling client", e);
                }
            }
        }
    }

    public static void handleClient(SSLSocket socket) throws IOException, MalformedHttpMessage {
        InputStream input = socket.getInputStream();
        OutputStream output = socket.getOutputStream();

        // handle the input
        try (InputStreamReader inputStreamReader = new InputStreamReader(input)) {
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            // parse our http request
            HttpRequest httpRequest = new HttpRequest(bufferedReader);

            // check if the socket is closed before we write any responses
            if (socket.isClosed()) {
                log.info("unclean socket closure: client disconnected: this may indicate buggy code");
                return;
            }

            // build our response, send it
            HttpResponse httpResponse = new HttpResponse(200, "OK", new HashMap<>(), "<p>RequestDirector v1.0.0</p>");
            output.write(httpResponse.getBytes("UTF-8"));
        }
        socket.close();
    }
}
