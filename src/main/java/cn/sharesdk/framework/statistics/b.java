package cn.sharesdk.framework.statistics;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.framework.statistics.b.c;
import cn.sharesdk.framework.statistics.b.e;
import cn.sharesdk.framework.statistics.b.g;
import com.mob.tools.SSDKHandlerThread;
import com.mob.tools.utils.DeviceHelper;
import com.mob.tools.utils.Ln;
import java.util.Calendar;

public class b extends SSDKHandlerThread {
    private static b a;
    private Context b;
    private DeviceHelper c;
    private a d;
    private String e;
    private Handler f;
    private boolean g;
    private int h;
    private boolean i;
    private long j;
    private boolean k;

    private b(Context context) {
        super("Thread-" + Math.abs(4736));
        this.b = context;
        this.c = DeviceHelper.getInstance(context);
        this.d = a.a(context);
    }

    public static synchronized b a(Context context) {
        b bVar;
        synchronized (b.class) {
            if (a == null) {
                if (context == null) {
                    bVar = null;
                } else {
                    a = new b(context.getApplicationContext());
                }
            }
            bVar = a;
        }
        return bVar;
    }

    private void a() {
        boolean b = b();
        if (b) {
            if (!this.k) {
                this.k = b;
                this.j = System.currentTimeMillis();
                a(new g());
            }
        } else if (this.k) {
            this.k = b;
            long currentTimeMillis = System.currentTimeMillis() - this.j;
            c eVar = new e();
            eVar.a = currentTimeMillis;
            a(eVar);
        }
    }

    private void b(c cVar) {
        cVar.f = this.c.getDeviceKey();
        cVar.g = this.e;
        cVar.h = this.c.getPackageName();
        cVar.i = this.c.getAppVersion();
        cVar.j = String.valueOf(50000 + this.h);
        cVar.k = this.c.getPlatformCode();
        cVar.l = this.c.getDetailNetworkTypeForStatic();
        if (!"cn.sharesdk.demo".equals(cVar.h) && "api20".equals(this.e) && ShareSDK.isDebug()) {
            System.err.println("Your application is using the appkey of ShareSDK Demo, this will cause its data won't be count!");
        }
        cVar.m = this.c.getDeviceData();
    }

    private boolean b() {
        DeviceHelper instance = DeviceHelper.getInstance(this.b);
        String topTaskPackageName = instance.getTopTaskPackageName();
        String packageName = instance.getPackageName();
        return packageName != null && packageName.equals(topTaskPackageName);
    }

    private void c() {
        try {
            a.a(this.b).a();
        } catch (Throwable th) {
            Ln.e(th);
        }
    }

    private void c(c cVar) {
        try {
            this.d.a(cVar);
            cVar.b(this.b);
        } catch (Throwable th) {
            Ln.e(th);
            Ln.e(cVar.toString(), new Object[0]);
        }
    }

    public void a(int i) {
        this.h = i;
    }

    public void a(Handler handler) {
        this.f = handler;
    }

    public void a(c cVar) {
        if (this.i) {
            b(cVar);
            if (cVar.a(this.b)) {
                Message message = new Message();
                message.what = 3;
                message.obj = cVar;
                try {
                    this.handler.sendMessage(message);
                    return;
                } catch (Throwable th) {
                    Ln.e(th);
                    return;
                }
            }
            Ln.d("Drop event: " + cVar.toString(), new Object[0]);
        }
    }

    public void a(String str) {
        this.e = str;
    }

    public void a(boolean z) {
        this.g = z;
    }

    protected void onMessage(Message message) {
        switch (message.what) {
            case 1:
                a();
                try {
                    this.handler.sendEmptyMessageDelayed(1, 100);
                    return;
                } catch (Throwable th) {
                    Ln.e(th);
                    return;
                }
            case 2:
                c();
                try {
                    this.handler.sendEmptyMessageDelayed(2, 10000);
                    return;
                } catch (Throwable th2) {
                    Ln.e(th2);
                    return;
                }
            case 3:
                if (message.obj != null) {
                    c((c) message.obj);
                    return;
                }
                return;
            case 4:
                long longValue = cn.sharesdk.framework.statistics.a.c.a(this.b).g().longValue();
                Calendar instance = Calendar.getInstance();
                instance.setTimeInMillis(longValue);
                int i = instance.get(1);
                int i2 = instance.get(2);
                int i3 = instance.get(5);
                instance.setTimeInMillis(System.currentTimeMillis());
                int i4 = instance.get(1);
                int i5 = instance.get(2);
                int i6 = instance.get(5);
                if (i == i4 && i2 == i5 && i3 == i6) {
                    this.d.d(this.e);
                } else {
                    this.d.b(this.e);
                }
                this.handler.sendEmptyMessageDelayed(4, 3600000);
                return;
            default:
                return;
        }
    }

    protected void onStart(Message message) {
        if (!this.i) {
            this.i = true;
            this.d.a(this.e);
            this.d.b(this.e);
            this.handler.sendEmptyMessageDelayed(4, 3600000);
            this.d.a(this.g);
            this.handler.sendEmptyMessage(1);
            this.handler.sendEmptyMessage(2);
            NewAppReceiver.a(this.b);
        }
    }

    protected void onStop(Message message) {
        if (this.i) {
            long currentTimeMillis = System.currentTimeMillis() - this.j;
            c eVar = new e();
            eVar.a = currentTimeMillis;
            a(eVar);
            this.i = false;
            try {
                this.f.sendEmptyMessage(1);
            } catch (Throwable th) {
                Ln.e(th);
            }
            a = null;
            this.handler.getLooper().quit();
        }
    }
}
