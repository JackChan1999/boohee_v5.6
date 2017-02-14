package com.xiaomi.push.log;

import android.content.Context;
import android.content.SharedPreferences;

import com.umeng.socialize.net.utils.SocializeProtocolConstants;
import com.xiaomi.channel.commonutils.network.d;
import com.xiaomi.smack.util.h;
import com.xiaomi.smack.util.i;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.json.JSONException;
import org.json.JSONObject;

public class b {
    private static b                        c = null;
    private final  ConcurrentLinkedQueue<b> a = new ConcurrentLinkedQueue();
    private Context b;

    class b extends com.xiaomi.channel.commonutils.misc.b.b {
        long i = System.currentTimeMillis();
        final /* synthetic */ b j;

        b(b bVar) {
            this.j = bVar;
        }

        public void b() {
        }

        public boolean d() {
            return true;
        }

        final boolean e() {
            return System.currentTimeMillis() - this.i > 172800000;
        }
    }

    class a extends b {
        final /* synthetic */ b a;

        a(b bVar) {
            this.a = bVar;
            super(bVar);
        }

        public void b() {
            this.a.b();
        }
    }

    class c extends b {
        String  a;
        String  b;
        File    c;
        int     d;
        boolean e;
        boolean f;
        final /* synthetic */ b g;

        c(b bVar, String str, String str2, File file, boolean z) {
            this.g = bVar;
            super(bVar);
            this.a = str;
            this.b = str2;
            this.c = file;
            this.f = z;
        }

        private boolean f() {
            int i;
            SharedPreferences sharedPreferences = this.g.b.getSharedPreferences("log.timestamp", 0);
            String string = sharedPreferences.getString("log.requst", "");
            long currentTimeMillis = System.currentTimeMillis();
            try {
                JSONObject jSONObject = new JSONObject(string);
                currentTimeMillis = jSONObject.getLong("time");
                i = jSONObject.getInt("times");
            } catch (JSONException e) {
                i = 0;
            }
            if (System.currentTimeMillis() - currentTimeMillis >= com.umeng.analytics.a.h) {
                currentTimeMillis = System.currentTimeMillis();
                i = 0;
            } else if (i > 10) {
                return false;
            }
            JSONObject jSONObject2 = new JSONObject();
            try {
                jSONObject2.put("time", currentTimeMillis);
                jSONObject2.put("times", i + 1);
                sharedPreferences.edit().putString("log.requst", jSONObject2.toString()).commit();
            } catch (JSONException e2) {
                com.xiaomi.channel.commonutils.logger.b.c("JSONException on put " + e2.getMessage
                        ());
            }
            return true;
        }

        public void b() {
            try {
                if (f()) {
                    Map hashMap = new HashMap();
                    hashMap.put(SocializeProtocolConstants.PROTOCOL_KEY_UID, h.b());
                    hashMap.put("token", this.b);
                    hashMap.put(com.alipay.sdk.app.statistic.c.a, d.f(this.g.b));
                    d.a(this.a, hashMap, this.c, "file");
                }
                this.e = true;
            } catch (IOException e) {
            }
        }

        public void c() {
            if (!this.e) {
                this.d++;
                if (this.d < 3) {
                    this.g.a.add(this);
                }
            }
            if (this.e || this.d >= 3) {
                this.c.delete();
            }
            this.g.a((long) ((1 << this.d) * 1000));
        }

        public boolean d() {
            return d.e(this.g.b) || (this.f && d.d(this.g.b));
        }
    }

    private b(Context context) {
        this.b = context;
        this.a.add(new a(this));
        b(0);
    }

    public static b a(Context context) {
        if (c == null) {
            synchronized (b.class) {
                if (c == null) {
                    c = new b(context);
                }
            }
        }
        c.b = context;
        return c;
    }

    private void a(long j) {
        b bVar = (b) this.a.peek();
        if (bVar != null && bVar.d()) {
            b(j);
        }
    }

    private void b() {
        if (!com.xiaomi.channel.commonutils.file.c.b() && !com.xiaomi.channel.commonutils.file.c
                .a()) {
            try {
                File file = new File(this.b.getExternalFilesDir(null) + "/.logcache");
                if (file.exists() && file.isDirectory()) {
                    for (File delete : file.listFiles()) {
                        delete.delete();
                    }
                }
            } catch (NullPointerException e) {
            }
        }
    }

    private void b(long j) {
        if (!this.a.isEmpty()) {
            i.a(new d(this), j);
        }
    }

    private void c() {
        while (!this.a.isEmpty()) {
            if (((b) this.a.peek()).e() || this.a.size() > 6) {
                com.xiaomi.channel.commonutils.logger.b.c("remove Expired task");
                this.a.remove();
            } else {
                return;
            }
        }
    }

    public void a() {
        c();
        a(0);
    }

    public void a(String str, String str2, Date date, Date date2, int i, boolean z) {
        this.a.add(new c(this, i, date, date2, str, str2, z));
        b(0);
    }
}
