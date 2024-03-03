package com.smokelabs.atc.server.pojo;

import java.util.ArrayList;
import java.util.List;

import com.smokelabs.atc.exception.HeaderNotFoundException;

/**
 * A basic container around a list of HttpHeader(s). Allows the developer to get
 * a header by its name.
 */
public class HttpHeaderContainer {
    private List<HttpHeader> headers = new ArrayList<>();

    /**
     * Get an HttpHeader object by its header name.
     * 
     * @param name Name of the header
     * @return HttpHeader object
     * @throws HeaderNotFoundException
     */
    public HttpHeader getByName(String name) throws HeaderNotFoundException {
        name = name.toLowerCase();
        for (HttpHeader header : headers) {
            if (header.getName().toLowerCase().equals(name)) {
                return header;
            }
        }
        throw new HeaderNotFoundException(name);
    }

    /**
     * Add a header to this container
     * 
     * @param header HttpHeader object
     */
    public void add(HttpHeader header) {
        headers.add(header);
    }

    /**
     * Adds a list of HttpHeader(s) to this container
     * 
     * @param headers List of HttpHeader objects
     */
    public void addList(List<HttpHeader> headers) {
        for (HttpHeader header : headers) {
            this.headers.add(header);
        }
    }
}
