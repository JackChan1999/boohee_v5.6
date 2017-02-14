package com.baidu.location;

import android.os.Handler;
import android.os.Message;
import java.io.File;
import java.util.ArrayList;
import java.util.Locale;
import org.apache.http.message.BasicNameValuePair;

class o extends s {
    public static final int db = 1;
    static o dd = null;
    private int da;
    private a dc;
    boolean de;
    int df;
    long dg;
    long dh;
    ArrayList di;
    private Handler dj;

    class a extends s {
        final /* synthetic */ o c6;
        int c7 = 0;
        boolean c8 = false;
        String c9 = null;

        a(o oVar) {
            this.c6 = oVar;
        }

        void T() {
            this.cW = this.c9;
            this.cR = c.do() + "?&qt=state&trtm=" + System.currentTimeMillis();
            this.c0 = 2;
        }

        public boolean aa() {
            if (this.c8) {
                return true;
            }
            this.c9 = ad.cM().cG();
            if (this.c9 == null) {
                return false;
            }
            this.c8 = true;
            R();
            return true;
        }

        void do(boolean z) {
            if (z) {
                try {
                    new File(this.c9).delete();
                    this.c7 = 0;
                } catch (Exception e) {
                }
            } else {
                this.c7 += 2;
            }
            this.c9 = null;
            this.c8 = false;
            this.c6.dj.sendEmptyMessageDelayed(1, 1500);
        }
    }

    public o() {
        this.di = null;
        this.de = false;
        this.dg = 0;
        this.dh = 0;
        this.df = 0;
        this.dj = null;
        this.dc = null;
        this.da = 0;
        this.cT = new ArrayList();
        this.dc = new a(this);
        this.c0 = 2;
        this.dj = new Handler(this) {
            final /* synthetic */ o a;

            {
                this.a = r1;
            }

            public void handleMessage(Message message) {
                if (ab.gv) {
                    switch (message.what) {
                        case 1:
                            this.a.ad();
                            return;
                        default:
                            return;
                    }
                }
            }
        };
    }

    public static o ac() {
        if (dd == null) {
            dd = new o();
        }
        return dd;
    }

    private void ad() {
        if (cY >= 6 || this.da >= 10 || !aw.do().int()) {
            this.da = 0;
            return;
        }
        this.da++;
        if (!ab()) {
            this.da = 0;
        }
    }

    void T() {
        this.cT.add(new BasicNameValuePair("qt", "cltr"));
        try {
            this.cT.add(new BasicNameValuePair("info", Jni.i(az.cn().co())));
        } catch (Exception e) {
        }
        for (int i = 0; i < this.di.size(); i++) {
            this.cT.add(new BasicNameValuePair("cltr[" + i + "]", (String) this.di.get(i)));
        }
        this.cT.add(new BasicNameValuePair("trtm", String.format(Locale.CHINA, "%d", new Object[]{Long.valueOf(System.currentTimeMillis())})));
    }

    public boolean ab() {
        if (!ar.bU()) {
            return false;
        }
        if (this.dc.c7 <= 2) {
            return !this.dc.aa() ? for(true) : true;
        } else {
            a aVar = this.dc;
            aVar.c7--;
            return for(true);
        }
    }

    public void do(int i) {
        this.dj.sendEmptyMessageDelayed(i, 500);
    }

    void do(boolean z) {
        if (!z || this.cS == null) {
            this.dg = 0;
        } else if (this.di != null) {
            this.di.clear();
        }
        if (this.cT != null) {
            this.cT.clear();
        }
        this.de = false;
        this.dj.sendEmptyMessageDelayed(1, 1500);
    }

    public boolean for(boolean z) {
        if (this.de) {
            return true;
        }
        if (System.currentTimeMillis() - this.dg < 7200000) {
            return false;
        }
        if (System.currentTimeMillis() - this.dh > 3600000) {
            this.df = 0;
        }
        if (this.df > 5 && z) {
            return false;
        }
        if (!ar.bU()) {
            return false;
        }
        if (!aw.do().int() && z) {
            return false;
        }
        if (this.di == null || this.di.size() < 1) {
            String str = c.byte();
            if (str != null) {
                for (int i = 0; i < 2; i++) {
                    String k = Jni.k(str);
                    if (k == null) {
                        this.dg = System.currentTimeMillis();
                        break;
                    }
                    if (this.di == null) {
                        this.di = new ArrayList();
                    }
                    this.di.add(k);
                }
            } else {
                return false;
            }
        }
        if (this.di == null || this.di.size() <= 0) {
            return false;
        }
        this.df++;
        this.dh = System.currentTimeMillis();
        this.de = true;
        N();
        return true;
    }
}
