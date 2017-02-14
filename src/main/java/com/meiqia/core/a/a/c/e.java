package com.meiqia.core.a.a.c;

import org.java_websocket.framing.CloseFrame;

public class e extends b {
    public e() {
        super(CloseFrame.TOOBIG);
    }

    public e(String str) {
        super((int) CloseFrame.TOOBIG, str);
    }
}
