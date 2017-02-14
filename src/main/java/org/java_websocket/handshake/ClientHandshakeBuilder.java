package org.java_websocket.handshake;

public interface ClientHandshakeBuilder extends HandshakeBuilder, ClientHandshake {
    void setResourceDescriptor(String str);
}
