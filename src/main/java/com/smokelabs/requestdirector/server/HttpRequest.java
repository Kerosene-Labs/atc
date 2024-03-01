package com.smokelabs.requestdirector.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import com.smokelabs.requestdirector.exception.MalformedHttpMessage;

import lombok.Getter;

/**
 * A parser for raw HTTP Request messages
 * 
 * @author hlafaille
 */
public class HttpRequest {

    @Getter
    private HttpMethod method;

    @Getter
    private String resource;

    @Getter
    private String protocolVersion;

    @Getter
    private HashMap<String, String> headers = new HashMap<>();

    @Getter
    private String content;

    /**
     * Get the HTTP Method (verb) from an input string
     * 
     * @param beginningText
     * @return HTTP Method (verb)
     * @throws MalformedHttpMessage
     * @throws IOException
     */
    public HttpRequest(BufferedReader bufferedReader) throws MalformedHttpMessage, IOException {
        StringBuilder httpRequestBeginning = new StringBuilder();

        // handle reading the content, putting it into a string
        String line;
        while (!(line = bufferedReader.readLine()).equals("\r\n\r\n")) {
            httpRequestBeginning.append(line + "\r\n");
            if (line.isEmpty()) {
                break;
            }
        }

        // split each line in the HTTP request out into its own element in an array
        List<String> lines = Arrays.asList(httpRequestBeginning.toString().split("\r\n"));

        // begin parsing the request line (ex: GET /resource/blah HTTP/1.1) & headers
        // (ex: Content-Type: application/json)
        int n;
        for (n = 0; n < lines.size(); n++) {
            String currentLine = lines.get(n);

            // request line
            if (n == 0) {
                String[] parts = currentLine.split(" ");
                method = HttpMethod.valueOf(parts[0]);
                resource = parts[1];
                protocolVersion = parts[2];
            }

            // headers
            if (n > 0) {
                // determine our key/value pair for this header string
                StringBuilder headerName = new StringBuilder();
                StringBuilder headerValue = new StringBuilder();

                // iterate over each character in the string
                char[] currentLineChars = currentLine.toCharArray();
                boolean passedSeparator = false;
                int headerCharN;
                for (headerCharN = 0; headerCharN < currentLineChars.length; headerCharN++) {
                    // if this is the first colon
                    if (currentLineChars[headerCharN] == ':' && !passedSeparator) {
                        passedSeparator = true;
                        headerCharN += 1;
                        continue;
                    }

                    // if we're not past the first colon, this must be the header name
                    if (!passedSeparator) {
                        headerName.append(currentLineChars[headerCharN]);
                    } else {
                        headerValue.append(currentLineChars[headerCharN]);
                    }
                }
                headers.put(headerName.toString().toLowerCase(), headerValue.toString().toLowerCase());
            }
        }

        // handle reading the actual content
        String contentLengthString = headers.get("content-length");
        if (contentLengthString != null) {
            int contentLength = Integer.parseInt(headers.get("content-length"));
            char[] remainingContent = new char[contentLength];
            int bytesRead = bufferedReader.read(remainingContent, 0, contentLength);
            content = new String(remainingContent);
        }
    }

}
