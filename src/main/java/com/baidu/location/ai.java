package com.baidu.location;

import android.os.Handler;

class ai implements ax, n {
    private static ai hb = null;
    private boolean ha;
    private Handler hc;
    private boolean hd;

    private ai() {
        this.hc = null;
        this.ha = false;
        this.hd = false;
        this.hc = new Handler();
    }

    public static ai bA() {
        if (hb == null) {
            hb = new ai();
        }
        return hb;
    }

    private void bC() {
    }

    public void bB() {
        this.ha = false;
    }

    public void bz() {
        this.hc.post(new Runnable(this) {
            final /* synthetic */ ai a;

            {
                this.a = r1;
            }

            public void run() {
                this.a.bC();
            }
        });
    }
}
