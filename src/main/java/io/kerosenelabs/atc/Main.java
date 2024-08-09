package io.kerosenelabs.atc;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.util.concurrent.ThreadLocalRandom;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;

import io.kerosenelabs.atc.constant.AnsiCodes;
import io.kerosenelabs.kindling.HttpRequest;
import io.kerosenelabs.kindling.HttpResponse;
import io.kerosenelabs.kindling.KindlingServer;
import io.kerosenelabs.kindling.constant.HttpMethod;
import io.kerosenelabs.kindling.exception.KindlingException;
import io.kerosenelabs.kindling.handler.RequestHandler;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Main {
    private static String getSplashArt() {
        URL resourceUrl = Resources.getResource("art.txt");
        if (resourceUrl == null) {
            return "(no art)";
        }
        try {
            return AnsiCodes.COLOR_CYAN.getCode() + Resources.toString(resourceUrl, Charsets.UTF_8)
                    + AnsiCodes.RESET.getCode();
        } catch (IOException e) {
            return "(failed to load art)";
        }
    }

    private static String getRandomSplashText() {
        URL resourceUrl = Resources.getResource("splash.txt");
        if (resourceUrl == null) {
            return "(no splash text)";
        }
        String splashTextContent;
        try {
            splashTextContent = Resources.toString(resourceUrl, Charsets.UTF_8);
        } catch (IOException e) {
            return "(failed to load splash text)";
        }
        String[] splashTextMessages = splashTextContent.split("\n");
        return AnsiCodes.COLOR_BLUE.getCode()
                + splashTextMessages[ThreadLocalRandom.current().nextInt(0, splashTextMessages.length)]
                + AnsiCodes.RESET.getCode() + "\n\n";
    }

    public static void main(String[] args) throws KindlingException {
        System.out.println(getSplashArt());
        System.out.println(getRandomSplashText());
        log.info("Starting up Kindling server");
        KindlingServer server = KindlingServer.getInstance();

        server.installRequestHandler(new RequestHandler() {

            @Override
            public boolean accepts(HttpMethod arg0, String arg1) throws KindlingException {
                throw new UnsupportedOperationException("Unimplemented method 'accepts'");
            }

            @Override
            public HttpResponse handle(HttpRequest arg0) throws KindlingException {
                throw new UnsupportedOperationException("Unimplemented method 'handle'");
            }

        });
        server.serve(8443, Path.of("mykeystore.p12"), "password");
    }
}
