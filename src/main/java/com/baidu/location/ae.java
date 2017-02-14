package com.baidu.location;

import android.os.Handler;

class ae implements n, ax {
    private static ae g2;
    public Handler g1;

    private ae() {
        this.g1 = null;
        this.g1 = new Handler();
    }

    public static ae bp() {
        if (g2 == null) {
            g2 = new ae();
        }
        return g2;
    }

    public boolean bq() {
        return false;
    }

    public synchronized void br() {
    }

    public boolean bs() {
        return false;
    }

    public synchronized void bt() {
    }
}
