package io.kerosenelabs.atc;

import java.nio.file.Path;

import io.kerosenelabs.kindling.HttpRequest;
import io.kerosenelabs.kindling.HttpResponse;
import io.kerosenelabs.kindling.KindlingServer;
import io.kerosenelabs.kindling.constant.HttpMethod;
import io.kerosenelabs.kindling.exception.KindlingException;
import io.kerosenelabs.kindling.handler.RequestHandler;

public class Main {
    public static void main(String[] args) throws KindlingException {
        // 
        KindlingServer server = KindlingServer.getInstance();


        server.installRequestHandler(new RequestHandler() {

            @Override
            public boolean accepts(HttpMethod arg0, String arg1) throws KindlingException {
                // TODO Auto-generated method stub
                throw new UnsupportedOperationException("Unimplemented method 'accepts'");
            }

            @Override
            public HttpResponse handle(HttpRequest arg0) throws KindlingException {
                // TODO Auto-generated method stub
                throw new UnsupportedOperationException("Unimplemented method 'handle'");
            }

        });
        server.serve(8443, Path.of("mykeystore.p12"), "password");
    }
}
