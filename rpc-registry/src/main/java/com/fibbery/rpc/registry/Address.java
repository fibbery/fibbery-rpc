package com.fibbery.rpc.registry;

import java.io.Serializable;

/**
 * @author fibbery
 * @date 17/11/30
 */
public class Address implements Serializable {

    private static final long serialVersionUID = -1013152626620811457L;

    private String host;

    private int port;

    public Address(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public Address() {
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
