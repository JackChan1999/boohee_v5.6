package org.java_websocket;

import java.net.Socket;
import java.util.List;
import org.java_websocket.drafts.Draft;

public interface WebSocketFactory {
    WebSocket createWebSocket(WebSocketAdapter webSocketAdapter, List<Draft> list, Socket socket);

    WebSocket createWebSocket(WebSocketAdapter webSocketAdapter, Draft draft, Socket socket);
}
