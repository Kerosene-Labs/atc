package com.smokelabs.requestdirector.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.errorprone.annotations.Immutable;
import com.smokelabs.requestdirector.exception.HeaderNotFoundException;
import com.smokelabs.requestdirector.exception.MalformedHttpMessage;
import com.smokelabs.requestdirector.server.pojo.HttpHeader;
import com.smokelabs.requestdirector.server.pojo.HttpHeaderContainer;
import com.smokelabs.requestdirector.server.pojo.HttpRequestLine;

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
    private HttpHeaderContainer headers = new HttpHeaderContainer();

    @Getter
    private String content;

    /**
     * Read the raw HTTP Request message head as a list of Strings. The "HTTP
     * Request Message
     * Head" is known colloquially in this project as the literal text before the
     * content separator.
     * 
     * @param bufferedReader Buffered reader from the socket
     * @return Raw HTTP request message head as a list of Strings
     * @throws IOException
     */
    private static List<String> readRawMessageHead(BufferedReader bufferedReader) throws IOException {
        List<String> messageHead = new ArrayList<>();
        String line;
        while (!(line = bufferedReader.readLine()).equals("\r\n\r\n")) {
            messageHead.add(line);
            if (line.isEmpty()) {
                break;
            }
        }
        return messageHead;
    }

    /**
     * Parse the request line. An HTTP request line is always the first line in an
     * HTTP request message.
     * 
     * @param messageHead
     * @return HttpRequestLine object
     */
    private static HttpRequestLine parseRequestLine(List<String> messageHead) {
        String requestLine = messageHead.get(0);
        String[] splitRequestLine = requestLine.split(" ");
        return new HttpRequestLine(HttpMethod.valueOf(splitRequestLine[0]), splitRequestLine[1], splitRequestLine[2]);
    }

    /**
     * Parse HTTP headers from the message head
     * 
     * @param messageHead
     * @return List of ImmutableHttpHeader(s)
     */
    private static List<HttpHeader> parseHttpHeaders(List<String> messageHead) {
        List<HttpHeader> headers = new ArrayList<>();

        // note: n=1 here because we are only parsing headers
        int n = 1;
        for (n = 1; n < messageHead.size(); n++) {
            String currentLine = messageHead.get(n);

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
            headers.add(new HttpHeader(headerName.toString(), headerValue.toString()));
        }
        return headers;
    }

    /**
     * Parse the HTTP Content (after the content separator).
     * 
     * @param contentLength  Int value of the Content-Length header
     * @param bufferedReader Buffered Reader from the socket
     * @return String containing the content
     * @throws IOException
     */
    private static String parseContent(int contentLength, BufferedReader bufferedReader) throws IOException {
        char[] content = new char[contentLength];
        bufferedReader.read(content, 0, contentLength);
        return new String(content);
    }

    /**
     * Get the HTTP Method (verb) from an input string
     * 
     * @param beginningText
     * @return HTTP Method (verb)
     * @throws MalformedHttpMessage
     * @throws IOException
     */
    public HttpRequest(BufferedReader bufferedReader) throws MalformedHttpMessage, IOException {
        List<String> messageHead = readRawMessageHead(bufferedReader);

        // do http request line
        HttpRequestLine httpRequestLine = parseRequestLine(messageHead);
        method = httpRequestLine.getMethod();
        resource = httpRequestLine.getResource();
        protocolVersion = httpRequestLine.getProtocol();

        // do headers
        headers.addList(parseHttpHeaders(messageHead));

        // do the content (if there's no content-length, we assume there's no body)
        try {
            int contentLength = Integer.parseInt(headers.getByName("Content-Length").getValue());
            if (contentLength > 0) {
                content = parseContent(contentLength, bufferedReader);
            }
        } catch (HeaderNotFoundException e) {
        }
    }

}
