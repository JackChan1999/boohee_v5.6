package com.baidu.location;

import android.os.Handler;
import android.os.Message;

class r implements ax, n {
    private static r cP = null;
    private l cO;
    private Handler cQ;

    public class a extends Handler {
        final /* synthetic */ r a;

        public a(r rVar) {
            this.a = rVar;
        }

        public void handleMessage(Message message) {
            if (ab.gv) {
                switch (message.what) {
                    case 92:
                        this.a.J();
                        break;
                }
                super.handleMessage(message);
            }
        }
    }

    private r() {
        this.cO = null;
        this.cQ = null;
        this.cQ = new a(this);
    }

    public static r H() {
        if (cP == null) {
            cP = new r();
        }
        return cP;
    }

    private void J() {
        try {
            if (y.gb && !c.a7) {
            }
        } catch (Exception e) {
        }
    }

    public void G() {
        if (this.cO != null) {
            this.cO.u();
        }
        this.cO = null;
    }

    public Handler I() {
        return this.cQ;
    }

    public void K() {
    }
}
