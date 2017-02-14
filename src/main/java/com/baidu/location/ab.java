package com.baidu.location;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Messenger;
import android.os.Process;
import android.util.Log;

public class ab extends Service implements ax, n, LLSInterface {
    private static long gC = 0;
    static a gD = null;
    public static boolean gE;
    public static boolean gv = false;
    public static long gw;
    private static Context gy = null;
    private HandlerThread gA;
    private Looper gB;
    private boolean gx = false;
    Messenger gz = null;

    public class a extends Handler {
        final /* synthetic */ ab a;

        public a(ab abVar, Looper looper) {
            this.a = abVar;
            super(looper);
        }

        public void handleMessage(Message message) {
            if (ab.gv) {
                switch (message.what) {
                    case 11:
                        this.a.h(message);
                        break;
                    case 12:
                        this.a.e(message);
                        break;
                    case 15:
                        this.a.i(message);
                        break;
                    case 22:
                        ah.ay().case(message);
                        break;
                    case 25:
                        u.aG().long(message);
                        break;
                    case 28:
                        ay.cd().j(message);
                        break;
                    case 41:
                        ah.ay().aw();
                        break;
                    case 57:
                        this.a.d(message);
                        break;
                    case 110:
                        ae.bp().bt();
                        break;
                    case 111:
                        ae.bp().br();
                        break;
                    case 201:
                        ak.a().do();
                        break;
                    case 202:
                        ak.a().if();
                        break;
                    case 203:
                        ak.a().a(message);
                        break;
                    case 206:
                        a0.cq().if(f.getServiceContext(), message);
                        break;
                    case 207:
                        au.int(f.getServiceContext());
                        break;
                }
            }
            if (message.what == 0) {
                this.a.bl();
            }
            if (message.what == 1) {
                this.a.bk();
            }
            super.handleMessage(message);
        }
    }

    public static long bi() {
        return gC;
    }

    public static Handler bj() {
        return gD;
    }

    private void bk() {
        t.an().as();
        ah.ay().ax();
        ae.bp().br();
        q.w();
        k.p().n();
        if (!this.gx) {
            Process.killProcess(Process.myPid());
        }
    }

    private void bl() {
        gv = true;
        t.an().am();
        ar.bW().b2();
        az.cn();
        x.a4().aU();
        r.H().K();
        ah.ay().aC();
        ay.cd().ci();
        y.a7().a8();
        aw.do().for();
        aa.bg().bh();
    }

    private void d(Message message) {
        if (message != null && message.obj == null) {
        }
    }

    private void e(Message message) {
        k.p().do(message);
    }

    private void h(Message message) {
        k.p().new(message);
        y.a7().a9();
        d.X().Z();
    }

    private void i(Message message) {
        k.p().int(message);
    }

    public double getVersion() {
        return 4.199999809265137d;
    }

    public IBinder onBind(Intent intent) {
        Bundle extras = intent.getExtras();
        boolean z = false;
        if (extras != null) {
            z = extras.getBoolean("cache_exception");
            this.gx = extras.getBoolean("kill_process");
            gE = extras.getBoolean("debug_dev");
            gw = extras.getLong("interval");
        }
        if (!z) {
            Thread.setDefaultUncaughtExceptionHandler(aa.bg());
        }
        return this.gz.getBinder();
    }

    public void onCreate(Context context) {
        gC = System.currentTimeMillis();
        gy = context;
        this.gA = ao.a();
        this.gB = this.gA.getLooper();
        gD = new a(this, this.gB);
        this.gz = new Messenger(gD);
        gD.sendEmptyMessage(0);
        Log.d(ax.i, "baidu location service start1 ..." + Process.myPid());
    }

    public void onDestroy() {
        gv = false;
        ar.bW().bT();
        h.for().do();
        r.H().G();
        x.a4().aW();
        aw.do().if();
        gD.sendEmptyMessage(1);
        Log.d(ax.i, "baidu location service stop ...");
    }

    public int onStartCommand(Intent intent, int i, int i2) {
        return 1;
    }

    public boolean onUnBind(Intent intent) {
        return false;
    }
}
