package org.java_websocket.exceptions;

import org.java_websocket.framing.CloseFrame;

public class LimitExedeedException extends InvalidDataException {
    private static final long serialVersionUID = 6908339749836826785L;

    public LimitExedeedException() {
        super(CloseFrame.TOOBIG);
    }

    public LimitExedeedException(String s) {
        super((int) CloseFrame.TOOBIG, s);
    }
}
