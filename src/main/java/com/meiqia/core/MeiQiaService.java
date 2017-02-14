package com.meiqia.core;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.IBinder;
import android.text.TextUtils;

import com.meiqia.core.a.a.a.a;
import com.meiqia.core.b.c;
import com.meiqia.core.b.e;
import com.meiqia.core.b.h;
import com.meiqia.core.b.j;
import com.meiqia.core.bean.MQAgent;
import com.meiqia.core.bean.MQMessage;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;

import org.json.JSONObject;

public class MeiQiaService extends Service {
    public static    boolean a = false;
    public static    boolean b = false;
    protected static boolean c = false;
    private          boolean d = false;
    private h       e;
    private bt      f;
    private a       g;
    private Handler h;
    private a       i;
    private String  j;
    private AtomicBoolean k = new AtomicBoolean(false);

    private void a() {
        if ((this.i == null || !this.i.e().b()) && b.a != null) {
            this.j = b.a.getTrackId();
            e.b("socket init");
            b.a.setBrowserId(d());
            j.a(b.a, this.e);
            String browserId = b.a.getBrowserId();
            String trackId = b.a.getTrackId();
            String str = b.a.getEnterpriseId() + "";
            String visitId = b.a.getVisitId();
            String visitPageId = b.a.getVisitPageId();
            try {
                this.i = new bq(this, new URI("ws://eco-push-api-client.meiqia" +
                        ".com/pusher/websocket" + ("?browser_id=" + browserId + com.alipay.sdk
                        .sys.a.b + "ent_id=" + str + com.alipay.sdk.sys.a.b + "visit_id=" +
                        visitId + com.alipay.sdk.sys.a.b + "visit_page_id=" + visitPageId + com
                        .alipay.sdk.sys.a.b + "track_id=" + trackId + "&time=" + (System
                        .currentTimeMillis() + ""))));
                this.i.c();
            } catch (AssertionError e) {
                c = true;
                e.a("socket AssertionError");
            } catch (URISyntaxException e2) {
                c = true;
                e.a("socket URISyntaxException");
            }
        }
    }

    private void a(MQAgent mQAgent) {
        MQManager.getInstance(this).a(mQAgent);
        Intent intent = new Intent("agent_change_action");
        intent.putExtra("client_is_redirected", true);
        j.a((Context) this, intent);
        if (a) {
            e.b("action directAgent : agentName = " + mQAgent.getNickname());
        }
    }

    private void a(MQMessage mQMessage) {
        if ("ending".equals(mQMessage.getType())) {
            MQManager.getInstance(this).a(null);
        }
        if ("audio".equals(mQMessage.getType())) {
            mQMessage.setIs_read(false);
            b(mQMessage);
            return;
        }
        this.g.a(mQMessage);
    }

    private void a(JSONObject jSONObject) {
        JSONObject optJSONObject = jSONObject.optJSONObject("body");
        if (optJSONObject != null) {
            a(c.a(optJSONObject));
        }
    }

    private void b() {
        if (this.i != null) {
            this.i.d();
            this.i = null;
        }
    }

    private void b(MQMessage mQMessage) {
        File externalCacheDir = getExternalCacheDir();
        String media_url = mQMessage.getMedia_url();
        if (externalCacheDir == null || !j.a()) {
            a(mQMessage);
            return;
        }
        bu.a().a(media_url, externalCacheDir.getAbsolutePath(), mQMessage.getId() + "", new br
                (this, mQMessage));
    }

    private void b(JSONObject jSONObject) {
        Object optString = jSONObject.optString("target_id");
        if (!TextUtils.isEmpty(optString)) {
            Intent intent = new Intent("invite_evaluation");
            intent.putExtra("conversation_id", optString);
            j.a((Context) this, intent);
        }
    }

    private void c() {
        if (!c && !this.k.get() && !b && j.f(this) && b.a != null) {
            this.k.set(true);
            this.d = true;
            this.h.sendEmptyMessageDelayed(1, 5000);
        }
    }

    private void c(JSONObject jSONObject) {
        JSONObject optJSONObject = jSONObject.optJSONObject("body");
        if (optJSONObject != null) {
            MQAgent b = c.b(optJSONObject);
            MQAgent currentAgent = MQManager.getInstance(this).getCurrentAgent();
            if (currentAgent != null) {
                b.setNickname(currentAgent.getNickname());
                MQManager.getInstance(this).a(b);
                j.a((Context) this, new Intent("action_agent_status_update_event"));
            }
        }
    }

    private String d() {
        String str = "";
        String str2 = System.currentTimeMillis() + "";
        Random random = new Random();
        for (int i = 0; i < 5; i++) {
            str = str + random.nextInt(10);
        }
        return str + str2;
    }

    private void d(JSONObject jSONObject) {
        if (TextUtils.equals(jSONObject.optString("track_id"), this.j)) {
            MQManager.getInstance(this).a(null);
            j.a((Context) this, new Intent("action_black_add"));
        }
    }

    private void e() {
        MQManager.getInstance(this).a(new bs(this));
    }

    public IBinder onBind(Intent intent) {
        return null;
    }

    public void onCreate() {
        super.onCreate();
        this.h = new Handler();
        this.f = new bt();
        this.e = new h(this);
        this.g = a.a((Context) this);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        registerReceiver(this.f, intentFilter);
        this.h = new Handler(new bp(this));
    }

    public void onDestroy() {
        super.onDestroy();
        try {
            unregisterReceiver(this.f);
            b();
        } catch (Exception e) {
        }
    }

    public int onStartCommand(Intent intent, int i, int i2) {
        if (b.a == null) {
            return super.onStartCommand(intent, i, i2);
        }
        if ("ACTION_OPEN_SOCKET".equals(intent != null ? intent.getAction() :
                "ACTION_OPEN_SOCKET")) {
            b = false;
            this.d = false;
            if (!(TextUtils.isEmpty(this.j) || TextUtils.isEmpty(b.a.getTrackId()) || b.a
                    .getTrackId().equals(this.j))) {
                b();
            }
            a();
        } else {
            b();
        }
        return super.onStartCommand(intent, i, i2);
    }
}
