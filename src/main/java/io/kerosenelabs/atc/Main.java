package io.kerosenelabs.atc;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;

import io.kerosenelabs.atc.configuration.ConfigurationHandler;
import io.kerosenelabs.atc.configuration.model.Configuration;
import io.kerosenelabs.atc.constant.AnsiCodes;
import io.kerosenelabs.atc.server.CentralRequestHandler;
import io.kerosenelabs.kindling.KindlingServer;
import io.kerosenelabs.kindling.exception.KindlingException;
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

    public static void main(String[] args) throws KindlingException, IOException {
        System.out.println(getSplashArt());
        System.out.println(getRandomSplashText());

        // get our configuration
        log.info("Parsing configuration");
        ConfigurationHandler configurationHandler = ConfigurationHandler.getInstance();
        log.info("Configuration parsed, found {} service(s)", configurationHandler.getConfiguration().getServices().size());

        // build our consumes list
        List<Configuration.Service.ConsumingEndpoint> allConsumes = configurationHandler.getAllConsumes();

        // setup kindling
        log.info("Beginning Kindling initialization");
        KindlingServer server = KindlingServer.getInstance();
        server.installRequestHandler(new CentralRequestHandler());

        // serve our server
        log.info("Initialized, starting Kindling server");
        server.serve(8443, Path.of("mykeystore.p12"), "password");
    }
}
