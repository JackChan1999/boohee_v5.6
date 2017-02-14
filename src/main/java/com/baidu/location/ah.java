package com.baidu.location;

import android.os.Handler;
import android.os.Message;
import com.baidu.location.ag.b;

class ah extends ag implements ax, n {
    private static ah eo = null;
    final int eA;
    private String eB;
    final Handler eg;
    final int el;
    public a em;
    private volatile boolean en;
    private int ep;
    private long eq;
    private as er;
    private long es;
    private com.baidu.location.t.a et;
    private b eu;
    private boolean ev;
    private boolean ew;
    private boolean ex;
    private BDLocation ey;
    private BDLocation ez;

    private class a implements Runnable {
        final /* synthetic */ ah a;

        private a(ah ahVar) {
            this.a = ahVar;
        }

        public void run() {
            if (this.a.ev) {
                this.a.ev = false;
                this.a.aE();
            }
        }
    }

    private ah() {
        this.eA = 2000;
        this.el = 1000;
        this.ew = true;
        this.er = null;
        this.em = null;
        this.eB = null;
        this.ey = null;
        this.ez = null;
        this.eu = null;
        this.et = null;
        this.ex = true;
        this.en = false;
        this.ev = false;
        this.eq = 0;
        this.es = 0;
        this.eg = new b(this);
        this.er = new as();
        this.em = new a(this);
    }

    private void aB() {
        if (this.ex) {
            aE();
        } else if (!this.en) {
            if (ar.bW().bX()) {
                this.ev = true;
                this.eg.postDelayed(new a(), 2000);
                return;
            }
            aE();
        }
    }

    private void aD() {
        if (this.ey != null) {
            q.x().E();
        }
    }

    private void aE() {
        if (!this.en) {
            if (System.currentTimeMillis() - this.eq >= 1000 || this.ey == null) {
                c.if(ax.i, "start network locating ...");
                this.en = true;
                this.ew = if(this.et);
                if (if(this.eu) || this.ew || this.ey == null || this.ep != 0) {
                    String e = e(null);
                    if (e == null) {
                        BDLocation bDLocation = new BDLocation();
                        bDLocation.setLocType(62);
                        k.p().do(bDLocation);
                        av();
                        return;
                    }
                    if (this.eB != null) {
                        e = e + this.eB;
                        this.eB = null;
                    }
                    this.em.d(e);
                    this.et = this.eh;
                    this.eu = this.ek;
                    if (this.ex) {
                        this.ex = false;
                    }
                    this.eq = System.currentTimeMillis();
                    return;
                }
                if (this.ez != null && System.currentTimeMillis() - this.es > 30000) {
                    this.ey = this.ez;
                    this.ez = null;
                }
                k.p().do(this.ey);
                av();
                return;
            }
            k.p().do(this.ey);
            av();
        }
    }

    private boolean au() {
        this.ek = ar.bW().b1();
        return !this.er.do(this.ek);
    }

    private void av() {
        this.en = false;
        aD();
    }

    public static ah ay() {
        if (eo == null) {
            eo = new ah();
        }
        return eo;
    }

    private void char(Message message) {
        String aP = x.a4().aP();
        k.p().if(new BDLocation(aP), message);
        ak.a().a(null);
        ak.a().if(aP);
    }

    private void else(Message message) {
        c.if(ax.i, "on request location ...");
        if (!ae.bp().bq()) {
            int i = k.p().for(message);
            this.ep = message.arg1;
            switch (i) {
                case 1:
                    goto(message);
                    return;
                case 2:
                    aB();
                    return;
                case 3:
                    if (x.a4().aR()) {
                        char(message);
                        return;
                    }
                    return;
                default:
                    throw new IllegalArgumentException(String.format("this type %d is illegal", new Object[]{Integer.valueOf(i)}));
            }
        }
    }

    private void goto(Message message) {
        if (x.a4().aR()) {
            char(message);
        } else {
            aB();
        }
    }

    private boolean if(b bVar) {
        this.ek = ar.bW().b1();
        return bVar == this.ek ? false : this.ek == null || bVar == null || !bVar.a(this.ek);
    }

    private boolean if(com.baidu.location.t.a aVar) {
        this.eh = t.an().ak();
        return this.eh == aVar ? false : this.eh == null || aVar == null || !aVar.a(this.eh);
    }

    public boolean aA() {
        return this.ew;
    }

    void aC() {
        this.ex = true;
        this.en = false;
    }

    void at() {
        c.if(ax.i, "on network exception");
        if (this.ew || this.ey == null) {
            k.p().if(ay.cd().case(false), 21);
        } else {
            k.p().if(this.ey, 21);
        }
        this.ey = null;
        this.ez = null;
        this.er.b6();
        av();
    }

    public void aw() {
        if (this.ev) {
            aE();
            this.ev = false;
        }
    }

    void ax() {
        this.en = false;
        az();
    }

    void az() {
        this.ey = null;
        this.er.b6();
    }

    void byte(Message message) {
        c.if(ax.i, "on network success");
        BDLocation bDLocation = (BDLocation) message.obj;
        BDLocation bDLocation2 = new BDLocation(bDLocation);
        this.ez = null;
        Object obj = null;
        if (bDLocation.getLocType() == 161 && "cl".equals(bDLocation.getNetworkLocationType()) && this.ey != null && this.ey.getLocType() == 161 && "wf".equals(this.ey.getNetworkLocationType()) && System.currentTimeMillis() - this.es < 30000) {
            obj = 1;
            this.ez = bDLocation;
        }
        if (obj != null) {
            k.p().if(this.ey, 21);
        } else {
            k.p().if(bDLocation, 21);
            this.es = System.currentTimeMillis();
        }
        if (!c.if(bDLocation)) {
            this.ey = null;
            this.er.b6();
        } else if (obj == null) {
            this.ey = bDLocation;
        }
        int i = c.do(ee, "ssid\":\"", com.alipay.sdk.sys.a.e);
        if (i == Integer.MIN_VALUE || this.eu == null) {
            this.eB = null;
        } else {
            this.eB = this.eu.if(i);
        }
        ay.cd().if(ee, this.et, this.eu, bDLocation2);
        j.cZ().byte(bDLocation2);
        av();
    }

    public void case(Message message) {
        else(message);
    }

    public void for(BDLocation bDLocation) {
        az();
        this.ey = bDLocation;
    }
}
