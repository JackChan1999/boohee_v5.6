package com.qiniu.android.http;

public final class Proxy {
    public final String hostAddress;
    public final String password;
    public final int    port;
    public final String user;

    public Proxy(String hostAddress, int port, String user, String password) {
        this.hostAddress = hostAddress;
        this.port = port;
        this.user = user;
        this.password = password;
    }

    public java.net.Proxy toSystemProxy() {
        return null;
    }
}
