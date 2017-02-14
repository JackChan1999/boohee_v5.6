package com.baidu.location;

import android.os.Message;

class u extends ag implements ax, n {
    private static u eD = null;
    static final int eE = 3000;
    private long eF;
    private BDLocation eG;
    public a eH;

    private u() {
        this.eG = null;
        this.eF = 0;
        this.eH = null;
        this.eH = new a(this);
    }

    private void aF() {
        q.x().E();
    }

    public static u aG() {
        if (eD == null) {
            eD = new u();
        }
        return eD;
    }

    private void void(Message message) {
        if (System.currentTimeMillis() - this.eF >= 3000 || this.eG == null) {
            this.eH.d(e(k.p().if(message)));
            this.eF = System.currentTimeMillis();
            return;
        }
        k.p().if(this.eG, 26);
    }

    void at() {
        c.if(ax.i, "on network exception");
        this.eG = null;
        k.p().if(ay.cd().case(false), 26);
        aF();
    }

    void byte(Message message) {
        c.if(ax.i, "on network success");
        BDLocation bDLocation = (BDLocation) message.obj;
        k.p().if(bDLocation, 26);
        if (c.if(bDLocation)) {
            this.eG = bDLocation;
        } else {
            this.eG = null;
        }
        aF();
    }

    public void long(Message message) {
        void(message);
    }
}
