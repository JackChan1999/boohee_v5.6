package com.tencent.wxop.stat.a;

import org.java_websocket.framing.CloseFrame;

public enum e {
    PAGE_VIEW(1),
    SESSION_ENV(2),
    ERROR(3),
    CUSTOM(1000),
    ADDITION(1001),
    MONITOR_STAT(1002),
    MTA_GAME_USER(CloseFrame.REFUSE),
    NETWORK_MONITOR(1004),
    NETWORK_DETECTOR(CloseFrame.NOCODE);

    private int bG;

    private e(int i) {
        this.bG = i;
    }

    public final int r() {
        return this.bG;
    }
}
