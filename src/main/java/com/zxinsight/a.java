package com.zxinsight;

import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.HandlerThread;
import android.os.Message;
import android.text.TextUtils;

import com.umeng.socialize.net.utils.SocializeProtocolConstants;
import com.zxinsight.analytics.domain.trackEvent.EventsProxy;
import com.zxinsight.common.a.c;
import com.zxinsight.common.http.Request;
import com.zxinsight.common.http.Request.HttpMethod;
import com.zxinsight.common.http.ae;
import com.zxinsight.common.http.d;
import com.zxinsight.common.util.DeviceInfoUtils;
import com.zxinsight.common.util.b;
import com.zxinsight.common.util.l;
import com.zxinsight.common.util.m;

import org.json.JSONException;
import org.json.JSONObject;

public class a extends HandlerThread implements Callback {
    private static volatile a           a;
    private static          EventsProxy b;
    private                 m           c;
    private Handler d = new Handler(getLooper(), this);

    private a() {
        super("MW EventManager");
        start();
        b = EventsProxy.create();
        this.c = m.a();
    }

    public static a a() {
        synchronized (a.class) {
            if (a == null) {
                a = new a();
            }
        }
        return a;
    }

    public synchronized void b() {
        Message message = new Message();
        message.what = 1;
        this.d.sendMessage(message);
    }

    public void a(String str) {
        Message message = new Message();
        message.what = 4;
        Bundle bundle = new Bundle();
        bundle.putString("msg_user_profile_bundle", str);
        message.setData(bundle);
        this.d.sendMessage(message);
    }

    private void a(long j) {
        if (this.c.a(1)) {
            Message message = new Message();
            message.what = 2;
            this.d.sendMessageDelayed(message, j);
        }
    }

    public boolean handleMessage(Message message) {
        if (!Thread.currentThread().isInterrupted()) {
            switch (message.what) {
                case 1:
                    if (b != null && l.b(b.getJsonString())) {
                        c(b.getJsonString());
                    }
                    f();
                    a((long) this.c.l());
                    break;
                case 2:
                    if (this.c.a(1)) {
                        int l = this.c.l();
                        if (System.currentTimeMillis() - this.c.m() >= ((long) l) && b != null &&
                                l.b(b.getJsonString())) {
                            c(b.getJsonString());
                        }
                        a((long) l);
                        break;
                    }
                    break;
                case 4:
                    d(message.getData().getString("msg_user_profile_bundle"));
                    break;
            }
        }
        return false;
    }

    void c() {
        if (m.a().u()) {
            b.a().b();
        }
    }

    public void d() {
        TrackAgent.currentEvent().launchEvent();
        this.c.g();
        if (this.c.u()) {
            e();
        }
    }

    void e() {
        b(b.getJsonString());
        b.clearEvents();
    }

    private void b(String str) {
        c.a(MWConfiguration.getContext(), str);
    }

    private synchronized void c(String str) {
        this.c.b(System.currentTimeMillis());
        b.clearEvents();
        DeviceInfoUtils.f();
        if (!TextUtils.isEmpty(str)) {
            Request aeVar = new ae(HttpMethod.POST, com.zxinsight.analytics.a.a.b, new b(this,
                    str));
            try {
                aeVar.a(new JSONObject(str));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            d.a(MWConfiguration.getContext()).a(aeVar);
        }
    }

    private synchronized void f() {
        long currentTimeMillis = System.currentTimeMillis();
        if (currentTimeMillis - this.c.n() >= 100) {
            this.c.b(currentTimeMillis);
            this.c.c(currentTimeMillis);
            com.zxinsight.common.a.d a = c.a(MWConfiguration.getContext(), -1);
            if (a != null && l.b(a.b())) {
                int size = a.a().size();
                for (int i = 0; i < size; i++) {
                    String str = (String) a.b().get(i);
                    String str2 = (String) a.a().get(i);
                    if (l.b(str)) {
                        Request aeVar = new ae(HttpMethod.POST, com.zxinsight.analytics.a.a.b,
                                new c(this, str2));
                        try {
                            aeVar.a(new JSONObject(str));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        d.a(MWConfiguration.getContext()).a(aeVar);
                    }
                }
            }
        }
    }

    private void d(String str) {
        if (!l.a(str)) {
            Request aeVar = new ae(HttpMethod.POST, com.zxinsight.analytics.a.a.d, new d(this));
            try {
                aeVar.a(new JSONObject(str));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            aeVar.a(SocializeProtocolConstants.PROTOCOL_KEY_UID, m.a().d());
            d.a(MWConfiguration.getContext()).a(aeVar);
        }
    }
}
