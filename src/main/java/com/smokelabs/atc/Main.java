package com.smokelabs.atc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.UUID;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;
import java.util.concurrent.ThreadLocalRandom;
import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import com.smokelabs.atc.configuration.ConfigurationHandler;
import com.smokelabs.atc.configuration.pojo.Configuration;
import com.smokelabs.atc.exception.InvalidHttpRequestException;
import com.smokelabs.atc.exception.MalformedHttpMessage;
import com.smokelabs.atc.server.AtcHttpRequest;
import com.smokelabs.atc.server.AtcHttpResponse;
import com.smokelabs.atc.server.RequestDirector;
import com.smokelabs.atc.util.AnsiCodes;
import com.smokelabs.atc.util.ErrorCode;
import com.smokelabs.atc.util.HttpStatus;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Main {
    private static Configuration loadedConfiguration;
    private static SSLSocket socket;

    private static String getSplashArt() {
        URL resourceUrl = Resources.getResource("art.txt");
        if (resourceUrl == null) {
            return "(no art)";
        }
        try {
            return AnsiCodes.COLOR_CYAN + Resources.toString(resourceUrl, Charsets.UTF_8) + AnsiCodes.RESET;
        } catch (IOException e) {
            return "(failed to laod art)";
        }
    }

    private static String getRandomSplashText() {
        URL resourceUrl = Resources.getResource("splash.txt");
        if (resourceUrl == null) {
            return "(no art)";
        }
        String splashTextContent;
        try {
            splashTextContent = Resources.toString(resourceUrl, Charsets.UTF_8);
        } catch (IOException e) {
            return "(failed to load splash text)";
        }
        String[] splashTextMessages = splashTextContent.split("\n");
        return AnsiCodes.COLOR_BLUE
                + splashTextMessages[ThreadLocalRandom.current().nextInt(0, splashTextMessages.length)]
                + AnsiCodes.RESET + "\n\n";
    }

    public static void main(String[] args) throws MalformedHttpMessage, InterruptedException {
        // print art and splash text
        System.out.println(getSplashArt());
        System.out.println(getRandomSplashText());

        // load our configuration
        Thread configurationThread = Thread.ofVirtual().name("configuration").start(() -> {
            loadedConfiguration = ConfigurationHandler.getInstance().getLoadedConfiguration();
        });
        configurationThread.join();

        // set where our keystore lives
        System.setProperty("javax.net.ssl.keyStore", "keystore.p12");
        System.setProperty("javax.net.ssl.keyStorePassword", "my-secure-pw");

        // create the tls socket
        Thread serverThread = Thread.ofVirtual().name("https").start(() -> {
            SSLServerSocketFactory factory = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();
            try (SSLServerSocket sslServerSocket = (SSLServerSocket) factory.createServerSocket(8443)) {
                log.info("tls socket created: awaiting clients");
                while (true) {
                    socket = (SSLSocket) sslServerSocket.accept();
                    try {
                        dispatchThread(socket);
                    } catch (Exception e) {
                        log.error("an error occurred during the request lifecycle", e);
                    }
                }
            } catch (IOException e) {
                log.error("io error on socket", e);
            }
        });
        serverThread.join();

    }

    public static void dispatchThread(SSLSocket socket) {
        Thread.startVirtualThread(() -> {
            try (
                    InputStream inputStream = socket.getInputStream();
                    InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                    OutputStream outputStream = socket.getOutputStream();) {
                if (socket.isClosed()) {
                    throw new RuntimeException("received closed socket at beginning of dispatch");
                }

                AtcHttpResponse httpResponse;

                // do our request
                try {
                    String traceId = "req:" + UUID.randomUUID().toString().replace("-", "");
                    Thread.currentThread().setName(traceId);
                    httpResponse = handleClient(inputStreamReader, outputStream, traceId);
                } catch (Exception e) {
                    // if any error during request/response lifecycle happened
                    log.error("exception occurred while handling client", e);
                    HashMap<String, String> headers = new HashMap<>();
                    headers.put("X-RD-Error", ErrorCode.ERROR_OCCURRED_DURING_REQUEST_HANDLING.getCode());
                    httpResponse = new AtcHttpResponse(HttpStatus.INTERNAL_SERVER_ERROR, headers, null);
                }

                if (socket.isClosed()) {
                    throw new RuntimeException("socket closed before writing could occurr");
                }

                try {
                    outputStream.write(httpResponse.getBytes("UTF-8"));
                } catch (IOException e) {
                    log.error("failed to write response to stream", e);
                }

                socket.close();
            } catch (IOException e) {
                log.error("socket or stream error", e);
            }
        });
    }

    public static AtcHttpResponse handleClient(InputStreamReader inputStreamReader, OutputStream output, String traceId)
            throws IOException, MalformedHttpMessage, InterruptedException, InvalidHttpRequestException {
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

        // parse our http request
        AtcHttpRequest httpRequest = new AtcHttpRequest(bufferedReader);

        // direct our request & get a response
        RequestDirector requestDirector = new RequestDirector(httpRequest, traceId);
        AtcHttpResponse httpResponse = requestDirector.directRequest();
        return httpResponse;
    }
}
