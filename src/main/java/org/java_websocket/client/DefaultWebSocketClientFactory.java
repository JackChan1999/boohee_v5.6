package org.java_websocket.client;

import java.net.Socket;
import java.nio.channels.ByteChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.List;
import org.java_websocket.WebSocket;
import org.java_websocket.WebSocketAdapter;
import org.java_websocket.WebSocketImpl;
import org.java_websocket.client.WebSocketClient.WebSocketClientFactory;
import org.java_websocket.drafts.Draft;

public class DefaultWebSocketClientFactory implements WebSocketClientFactory {
    private final WebSocketClient webSocketClient;

    public DefaultWebSocketClientFactory(WebSocketClient webSocketClient) {
        this.webSocketClient = webSocketClient;
    }

    public WebSocket createWebSocket(WebSocketAdapter a, Draft d, Socket s) {
        return new WebSocketImpl(this.webSocketClient, d);
    }

    public WebSocket createWebSocket(WebSocketAdapter a, List<Draft> d, Socket s) {
        return new WebSocketImpl(this.webSocketClient, d);
    }

    public ByteChannel wrapChannel(SocketChannel channel, SelectionKey c, String host, int port) {
        return c == null ? channel : channel;
    }
}
