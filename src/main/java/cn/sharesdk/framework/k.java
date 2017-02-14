package cn.sharesdk.framework;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Message;
import android.util.Log;
import com.mob.tools.SSDKHandlerThread;
import com.mob.tools.network.NetworkHelper;
import com.mob.tools.utils.Hashon;
import com.mob.tools.utils.Ln;
import com.mob.tools.utils.R;
import com.qiniu.android.common.Constants;
import com.tencent.tinker.loader.shareutil.ShareConstants;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

public class k extends SSDKHandlerThread {
    private a a = a.IDLE;
    private Context b;
    private HashMap<String, HashMap<String, String>> c;
    private ArrayList<Platform> d;
    private HashMap<String, Integer> e;
    private HashMap<Integer, String> f;
    private HashMap<Integer, CustomPlatform> g;
    private HashMap<Integer, HashMap<String, Object>> h;
    private HashMap<Integer, Service> i;
    private String j;
    private boolean k;
    private String l;
    private boolean m;
    private boolean n;

    private enum a {
        IDLE,
        INITIALIZING,
        READY
    }

    public k(Context context) {
        super("Thread-" + Math.abs(550));
        this.b = context.getApplicationContext();
        Ln.setContext(this.b);
        this.c = new HashMap();
        this.d = new ArrayList();
        this.e = new HashMap();
        this.f = new HashMap();
        this.g = new HashMap();
        this.h = new HashMap();
        this.i = new HashMap();
    }

    private boolean a(cn.sharesdk.framework.statistics.a aVar, HashMap<String, Object> hashMap, HashMap<String, Object> hashMap2) {
        try {
            if (hashMap.containsKey("error")) {
                if (!ShareSDK.isDebug()) {
                    return false;
                }
                Log.e("ShareSDK request platform config ==>>", new Hashon().fromHashMap(hashMap));
                return false;
            } else if (hashMap.containsKey(ShareConstants.RES_PATH)) {
                Ln.i("snsconf returns ===> %s", aVar.b(this.j, String.valueOf(hashMap.get(ShareConstants.RES_PATH)).replace("\n", "")).trim());
                hashMap2.putAll(new Hashon().fromJson(r2));
                return true;
            } else if (!ShareSDK.isDebug()) {
                return false;
            } else {
                Log.e("ShareSDK platform config result ==>>", "SNS configuration is empty");
                return false;
            }
        } catch (Throwable th) {
            if (!ShareSDK.isDebug()) {
                return false;
            }
            th.printStackTrace();
            return false;
        }
    }

    private void h() {
        synchronized (this.c) {
            this.c.clear();
            i();
            if (this.c.containsKey("ShareSDK")) {
                HashMap hashMap = (HashMap) this.c.remove("ShareSDK");
                if (hashMap != null) {
                    if (this.j == null) {
                        this.j = (String) hashMap.get("AppKey");
                    }
                    this.l = hashMap.containsKey("UseVersion") ? (String) hashMap.get("UseVersion") : "latest";
                }
            }
        }
    }

    private void i() {
        XmlPullParser newPullParser;
        InputStream open;
        try {
            XmlPullParserFactory newInstance = XmlPullParserFactory.newInstance();
            newInstance.setNamespaceAware(true);
            newPullParser = newInstance.newPullParser();
            open = this.b.getAssets().open("ShareSDK.xml");
        } catch (Throwable th) {
            Ln.e(th);
            return;
        }
        newPullParser.setInput(open, Constants.UTF_8);
        for (int eventType = newPullParser.getEventType(); eventType != 1; eventType = newPullParser.next()) {
            if (eventType == 2) {
                String name = newPullParser.getName();
                HashMap hashMap = new HashMap();
                int attributeCount = newPullParser.getAttributeCount();
                for (eventType = 0; eventType < attributeCount; eventType++) {
                    hashMap.put(newPullParser.getAttributeName(eventType), newPullParser.getAttributeValue(eventType).trim());
                }
                this.c.put(name, hashMap);
            }
        }
        open.close();
    }

    private void j() {
        new l(this).start();
    }

    private void k() {
        this.d.clear();
        Collection a = new i().a(this.b);
        if (a != null) {
            this.d.addAll(a);
        }
        synchronized (this.e) {
            synchronized (this.f) {
                Iterator it = this.d.iterator();
                while (it.hasNext()) {
                    Platform platform = (Platform) it.next();
                    this.f.put(Integer.valueOf(platform.getPlatformId()), platform.getName());
                    this.e.put(platform.getName(), Integer.valueOf(platform.getPlatformId()));
                }
            }
        }
    }

    private void l() {
        new i().a(this.b, this.j, this.handler, this.k, c());
    }

    public String a(int i, String str) {
        String a;
        synchronized (this.h) {
            a = new i().a(i, str, this.h);
        }
        return a;
    }

    public String a(Bitmap bitmap) {
        return a.READY != this.a ? null : new i().a(this.b, bitmap);
    }

    public String a(String str, boolean z, int i, String str2) {
        return a.READY != this.a ? str : new i().a(this.b, this.j, str, z, i, str2);
    }

    public void a(int i) {
        NetworkHelper.connectionTimeout = i;
    }

    public void a(int i, int i2) {
        synchronized (this.h) {
            new i().a(i, i2, this.h);
        }
    }

    public void a(int i, Platform platform) {
        new i().a(i, platform);
    }

    public void a(Class<? extends Service> cls) {
        synchronized (this.i) {
            if (this.i.containsKey(Integer.valueOf(cls.hashCode()))) {
                return;
            }
            try {
                Service service = (Service) cls.newInstance();
                this.i.put(Integer.valueOf(cls.hashCode()), service);
                service.a(this.b);
                service.a(this.j);
                service.onBind();
            } catch (Throwable th) {
                Ln.e(th);
            }
        }
    }

    public void a(String str) {
        this.j = str;
    }

    public void a(String str, int i) {
        new i().a(str, i);
    }

    public void a(String str, String str2) {
        synchronized (this.c) {
            this.c.put(str2, (HashMap) this.c.get(str));
        }
    }

    public void a(String str, HashMap<String, Object> hashMap) {
        synchronized (this.c) {
            HashMap hashMap2 = (HashMap) this.c.get(str);
            HashMap hashMap3 = hashMap2 == null ? new HashMap() : hashMap2;
            synchronized (hashMap3) {
                for (Entry entry : hashMap.entrySet()) {
                    String str2 = (String) entry.getKey();
                    Object value = entry.getValue();
                    if (value != null) {
                        hashMap3.put(str2, String.valueOf(value));
                    }
                }
            }
            this.c.put(str, hashMap3);
        }
    }

    public void a(boolean z) {
        this.k = z;
    }

    public boolean a(HashMap<String, Object> hashMap) {
        if (a.READY != this.a) {
            if (ShareSDK.isDebug()) {
                System.err.println("Statistics module unopened");
            }
            return false;
        }
        cn.sharesdk.framework.statistics.a a = cn.sharesdk.framework.statistics.a.a(this.b);
        boolean a2 = a(a, a.f(this.j), hashMap);
        if (a2) {
            this.n = true;
            new m(this, a).start();
            return a2;
        }
        try {
            String g = a.g(this.j);
            a.a(this.j, g);
            a2 = a(a, new Hashon().fromJson(g), hashMap);
            this.n = true;
            return a2;
        } catch (Throwable th) {
            if (ShareSDK.isDebug()) {
                th.printStackTrace();
            }
            this.n = false;
            return a2;
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public cn.sharesdk.framework.Platform[] a() {
        /*
        r8 = this;
        r1 = 0;
        r2 = 0;
        r4 = java.lang.System.currentTimeMillis();
        r3 = r8.d;
        monitor-enter(r3);
        r0 = r8.a;	 Catch:{ all -> 0x0049 }
        r6 = cn.sharesdk.framework.k.a.IDLE;	 Catch:{ all -> 0x0049 }
        if (r0 != r6) goto L_0x0012;
    L_0x000f:
        monitor-exit(r3);	 Catch:{ all -> 0x0049 }
        r0 = r1;
    L_0x0011:
        return r0;
    L_0x0012:
        r0 = r8.a;	 Catch:{ all -> 0x0049 }
        r6 = cn.sharesdk.framework.k.a.INITIALIZING;	 Catch:{ all -> 0x0049 }
        if (r0 != r6) goto L_0x001d;
    L_0x0018:
        r0 = r8.d;	 Catch:{ Throwable -> 0x0044 }
        r0.wait();	 Catch:{ Throwable -> 0x0044 }
    L_0x001d:
        monitor-exit(r3);	 Catch:{ all -> 0x0049 }
        r6 = new java.util.ArrayList;
        r6.<init>();
        r0 = r8.d;
        r3 = r0.iterator();
    L_0x0029:
        r0 = r3.hasNext();
        if (r0 == 0) goto L_0x004c;
    L_0x002f:
        r0 = r3.next();
        r0 = (cn.sharesdk.framework.Platform) r0;
        if (r0 == 0) goto L_0x0029;
    L_0x0037:
        r7 = r0.b();
        if (r7 == 0) goto L_0x0029;
    L_0x003d:
        r0.a();
        r6.add(r0);
        goto L_0x0029;
    L_0x0044:
        r0 = move-exception;
        com.mob.tools.utils.Ln.e(r0);	 Catch:{ all -> 0x0049 }
        goto L_0x001d;
    L_0x0049:
        r0 = move-exception;
        monitor-exit(r3);	 Catch:{ all -> 0x0049 }
        throw r0;
    L_0x004c:
        r0 = r8.g;
        r0 = r0.entrySet();
        r3 = r0.iterator();
    L_0x0056:
        r0 = r3.hasNext();
        if (r0 == 0) goto L_0x0074;
    L_0x005c:
        r0 = r3.next();
        r0 = (java.util.Map.Entry) r0;
        r0 = r0.getValue();
        r0 = (cn.sharesdk.framework.Platform) r0;
        if (r0 == 0) goto L_0x0056;
    L_0x006a:
        r7 = r0.b();
        if (r7 == 0) goto L_0x0056;
    L_0x0070:
        r6.add(r0);
        goto L_0x0056;
    L_0x0074:
        r0 = r6.size();
        if (r0 > 0) goto L_0x007c;
    L_0x007a:
        r0 = r1;
        goto L_0x0011;
    L_0x007c:
        r0 = r6.size();
        r3 = new cn.sharesdk.framework.Platform[r0];
        r1 = r2;
    L_0x0083:
        r0 = r3.length;
        if (r1 >= r0) goto L_0x0092;
    L_0x0086:
        r0 = r6.get(r1);
        r0 = (cn.sharesdk.framework.Platform) r0;
        r3[r1] = r0;
        r0 = r1 + 1;
        r1 = r0;
        goto L_0x0083;
    L_0x0092:
        r0 = "sort list use time: %s";
        r1 = 1;
        r1 = new java.lang.Object[r1];
        r6 = java.lang.System.currentTimeMillis();
        r4 = r6 - r4;
        r4 = java.lang.Long.valueOf(r4);
        r1[r2] = r4;
        com.mob.tools.utils.Ln.i(r0, r1);
        r0 = r3;
        goto L_0x0011;
        */
        throw new UnsupportedOperationException("Method not decompiled: cn.sharesdk.framework.k.a():cn.sharesdk.framework.Platform[]");
    }

    public Platform b(String str) {
        if (str == null) {
            return null;
        }
        Platform[] a = a();
        if (a == null) {
            return null;
        }
        for (Platform platform : a) {
            if (str.equals(platform.getName())) {
                return platform;
            }
        }
        return null;
    }

    public String b() {
        try {
            return new i().a();
        } catch (Throwable th) {
            Ln.e(th);
            return "2.6.0";
        }
    }

    public String b(String str, String str2) {
        String str3;
        synchronized (this.c) {
            HashMap hashMap = (HashMap) this.c.get(str);
            if (hashMap == null) {
                str3 = null;
            } else {
                str3 = (String) hashMap.get(str2);
            }
        }
        return str3;
    }

    public void b(int i) {
        NetworkHelper.readTimout = i;
    }

    public void b(Class<? extends Service> cls) {
        synchronized (this.i) {
            int hashCode = cls.hashCode();
            if (this.i.containsKey(Integer.valueOf(hashCode))) {
                ((Service) this.i.get(Integer.valueOf(hashCode))).onUnbind();
                this.i.remove(Integer.valueOf(hashCode));
            }
        }
    }

    public void b(boolean z) {
        this.m = z;
    }

    public boolean b(HashMap<String, Object> hashMap) {
        boolean a;
        synchronized (this.h) {
            this.h.clear();
            a = new i().a((HashMap) hashMap, this.h);
        }
        return a;
    }

    public int c() {
        try {
            return new i().b();
        } catch (Throwable th) {
            Ln.e(th);
            return 51;
        }
    }

    public int c(String str) {
        int intValue;
        synchronized (this.e) {
            if (this.e.containsKey(str)) {
                intValue = ((Integer) this.e.get(str)).intValue();
            } else {
                intValue = 0;
            }
        }
        return intValue;
    }

    public <T extends Service> T c(Class<T> cls) {
        T t;
        synchronized (this.i) {
            try {
                t = (Service) cls.cast(this.i.get(Integer.valueOf(cls.hashCode())));
            } catch (Throwable th) {
                if (ShareSDK.isDebug()) {
                    System.err.println(cls.getName() + " has not registered");
                }
                Ln.e(th);
                t = null;
            }
        }
        return t;
    }

    public String c(int i) {
        String str;
        synchronized (this.f) {
            str = (String) this.f.get(Integer.valueOf(i));
        }
        return str;
    }

    public String d(String str) {
        return a.READY != this.a ? null : new i().a(this.b, str);
    }

    public void d(Class<? extends CustomPlatform> cls) {
        synchronized (this.g) {
            if (this.g.containsKey(Integer.valueOf(cls.hashCode()))) {
                return;
            }
            try {
                CustomPlatform customPlatform = (CustomPlatform) cls.getConstructor(new Class[]{Context.class}).newInstance(new Object[]{this.b});
                this.g.put(Integer.valueOf(cls.hashCode()), customPlatform);
                synchronized (this.e) {
                    synchronized (this.f) {
                        if (customPlatform != null) {
                            if (customPlatform.b()) {
                                this.f.put(Integer.valueOf(customPlatform.getPlatformId()), customPlatform.getName());
                                this.e.put(customPlatform.getName(), Integer.valueOf(customPlatform.getPlatformId()));
                            }
                        }
                    }
                }
            } catch (Throwable th) {
                Ln.e(th);
            }
        }
    }

    public boolean d() {
        return this.m;
    }

    public void e(Class<? extends CustomPlatform> cls) {
        int hashCode = cls.hashCode();
        synchronized (this.g) {
            this.g.remove(Integer.valueOf(hashCode));
        }
    }

    public boolean e() {
        return this.k;
    }

    public boolean f() {
        boolean z;
        synchronized (this.h) {
            if (this.h == null || this.h.size() <= 0) {
                z = this.n;
            } else {
                z = true;
            }
        }
        return z;
    }

    public void g() {
        try {
            R.clearCache(this.b);
        } catch (Throwable th) {
            Ln.e(th);
        }
    }

    protected void onMessage(Message message) {
        switch (message.what) {
            case 1:
                this.a = a.IDLE;
                this.handler.getLooper().quit();
                return;
            default:
                return;
        }
    }

    protected void onStart(Message message) {
        synchronized (this.d) {
            try {
                k();
                l();
                this.a = a.READY;
                this.d.notify();
                j();
            } catch (Throwable th) {
                Ln.e(th);
            }
        }
    }

    protected void onStop(Message message) {
        synchronized (this.i) {
            for (Entry value : this.i.entrySet()) {
                ((Service) value.getValue()).onUnbind();
            }
            this.i.clear();
        }
        synchronized (this.g) {
            this.g.clear();
        }
        try {
            new i().b(this.b);
        } catch (Throwable th) {
            Ln.e(th);
            this.handler.getLooper().quit();
            this.a = a.IDLE;
        }
    }

    public void startThread() {
        this.a = a.INITIALIZING;
        h();
        super.startThread();
    }
}
