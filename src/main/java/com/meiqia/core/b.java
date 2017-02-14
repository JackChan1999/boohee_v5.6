package com.meiqia.core;

import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.os.Handler;
import android.text.TextUtils;

import com.boohee.modeldao.UserDao;
import com.boohee.utils.Utils;
import com.meiqia.core.b.c;
import com.meiqia.core.b.e;
import com.meiqia.core.b.h;
import com.meiqia.core.b.i;
import com.meiqia.core.b.j;
import com.meiqia.core.bean.MQAgent;
import com.meiqia.core.bean.MQClient;
import com.meiqia.core.bean.MQConversation;
import com.meiqia.core.bean.MQMessage;
import com.meiqia.core.callback.OnClientInfoCallback;
import com.meiqia.core.callback.OnEndConversationCallback;
import com.meiqia.core.callback.OnFailureCallBack;
import com.meiqia.core.callback.OnGetMQClientIdCallBackOn;
import com.meiqia.core.callback.OnGetMessageListCallback;
import com.meiqia.core.callback.OnInitCallback;
import com.meiqia.core.callback.OnMessageSendCallback;
import com.meiqia.core.callback.OnProgressCallback;
import com.meiqia.core.callback.OnRegisterDeviceTokenCallback;
import com.meiqia.core.callback.SimpleCallback;
import com.meiqia.meiqiasdk.util.ErrorCode;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

class b {
    protected static MQClient       a;
    private final    h              b;
    private final    Handler        c;
    private final    bc             d;
    private final    Context        e;
    private          MQAgent        f;
    private          MQConversation g;
    private          bu             h;
    private          String         i;
    private          String         j;
    private MQScheduleRule k = MQScheduleRule.REDIRECT_ENTERPRISE;

    public b(Context context, h hVar, bc bcVar, Handler handler) {
        this.e = context;
        this.b = hVar;
        this.c = handler;
        this.h = bu.a();
        this.d = bcVar;
    }

    private void a(MQConversation mQConversation) {
        this.g = mQConversation;
    }

    private void a(MQMessage mQMessage) {
        mQMessage.setAvatar(this.b.n());
        mQMessage.setFrom_type("client");
        mQMessage.setType("message");
        Object trackId = a.getTrackId();
        if (!TextUtils.isEmpty(trackId)) {
            mQMessage.setTrack_id(trackId);
        }
        if (this.g != null && this.f != null) {
            mQMessage.setAgent_nickname(this.f.getNickname());
            mQMessage.setConversation_id(this.g.getId());
            mQMessage.setAgent_id(this.g.getAgent_id());
            mQMessage.setEnterprise_id(this.g.getEnterprise_id());
        }
    }

    private void a(MQMessage mQMessage, OnMessageSendCallback onMessageSendCallback) {
        if (this.f != null) {
            Map hashMap = new HashMap();
            hashMap.put("browser_id", a.getBrowserId());
            hashMap.put("track_id", a.getTrackId());
            hashMap.put("ent_id", a.getEnterpriseId());
            hashMap.put("type", mQMessage.getContent_type());
            hashMap.put(Utils.RESPONSE_CONTENT, mQMessage.getContent());
            this.h.a("https://eco-api.meiqia.com/client/send_msg", hashMap, new aq(this,
                    mQMessage, onMessageSendCallback));
            return;
        }
        b(mQMessage, onMessageSendCallback);
    }

    private void a(cv cvVar) {
        a(new an(this, cvVar));
    }

    private void a(Runnable runnable) {
        this.c.post(runnable);
    }

    private void a(String str, String str2, dc dcVar) {
        try {
            File file = new File(str2);
            Object obj = -1;
            switch (str.hashCode()) {
                case 3143036:
                    if (str.equals("file")) {
                        obj = 2;
                        break;
                    }
                    break;
                case 93166550:
                    if (str.equals("audio")) {
                        obj = 1;
                        break;
                    }
                    break;
                case 106642994:
                    if (str.equals("photo")) {
                        obj = null;
                        break;
                    }
                    break;
            }
            switch (obj) {
                case null:
                    File file2 = new File(j.a(this.e), System.currentTimeMillis() + "");
                    com.meiqia.core.b.b.a(file, file2);
                    this.h.a(file2, new ax(this, dcVar), (OnFailureCallBack) dcVar);
                    return;
                case 1:
                    this.h.b(file, new d(this, dcVar), dcVar);
                    return;
                case 2:
                    return;
                default:
                    dcVar.onFailure(ErrorCode.PARAMETER_ERROR, "unknown contentType");
                    return;
            }
        } catch (Exception e) {
            dcVar.onFailure(ErrorCode.FILE_NOT_FOUND, "file not found");
        }
    }

    private void a(List<MQMessage> list, long j) {
        Iterator it = list.iterator();
        while (it.hasNext()) {
            MQMessage mQMessage = (MQMessage) it.next();
            if ("ending".equals(mQMessage.getType()) || mQMessage.getCreated_on() <= j) {
                it.remove();
            }
        }
    }

    private void a(List<MQMessage> list, List<String> list2, Map<String, String> map,
                   SimpleCallback simpleCallback) {
        int[] iArr = new int[]{0, 0};
        for (String a : list2) {
            a("photo", a, new z(this, iArr, list, list2, map, simpleCallback));
        }
    }

    private void a(List<MQMessage> list, Map<String, String> map, SimpleCallback simpleCallback) {
        Map hashMap = new HashMap();
        hashMap.put("browser_id", a.getBrowserId());
        hashMap.put("track_id", a.getTrackId());
        hashMap.put("enterprise_id", a.getEnterpriseId());
        hashMap.put("visit_id", a.getVisitId());
        List arrayList = new ArrayList();
        for (MQMessage mQMessage : list) {
            Map hashMap2 = new HashMap();
            hashMap2.put("content_type", mQMessage.getContent_type());
            hashMap2.put(Utils.RESPONSE_CONTENT, mQMessage.getContent());
            arrayList.add(hashMap2);
        }
        hashMap.put("replies", arrayList);
        this.h.a(hashMap, new aa(this, list, map, simpleCallback));
    }

    private void a(Map<String, String> map, SimpleCallback simpleCallback) {
        a((Map) map, new ac(this, simpleCallback));
    }

    private void a(Map<String, Object> map, List<MQMessage> list, cv cvVar) {
        this.h.a((Map) map, new am(this, list, cvVar));
    }

    private void b(MQMessage mQMessage, OnMessageSendCallback onMessageSendCallback) {
        a(this.d, this.i, this.j, this.k, new as(this, mQMessage, onMessageSendCallback));
    }

    private void c(String str) {
        try {
            Object k = this.b.k();
            String m = this.b.m();
            this.b.k(str);
            Object k2 = this.b.k();
            this.b.k(m);
            Map e = j.e(this.e);
            String jSONObject = c.a(e).toString();
            if (TextUtils.isEmpty(k2) || TextUtils.isEmpty(k) || !(TextUtils.isEmpty(k) || k
                    .equals(jSONObject))) {
                this.h.a(str, e, new p(this, k2, str, jSONObject));
            }
        } catch (Exception e2) {
        }
    }

    private boolean d(String str) {
        return j.a(str, this.b) != null;
    }

    public void a() {
        a(new ap(this));
    }

    public void a(int i, int i2, long j, int i3, OnGetMessageListCallback
            onGetMessageListCallback) {
        int parseInt = Integer.parseInt(a.getEnterpriseId());
        String a = i.a(j);
        String trackId = a.getTrackId();
        this.h.a(trackId, i, i2, parseInt, a, i3, new f(this, j, i, onGetMessageListCallback));
    }

    public void a(long j) {
        this.d.a(j);
    }

    public void a(long j, boolean z) {
        MQMessage b = this.d.b(j);
        if (b != null) {
            b.setIs_read(z);
            this.d.a(b);
        }
    }

    public void a(Context context) {
        Intent intent = new Intent(context, MeiQiaService.class);
        intent.setAction("ACTION_OPEN_SOCKET");
        context.startService(intent);
        a();
    }

    public void a(bc bcVar, String str, String str2, MQScheduleRule mQScheduleRule, cv cvVar) {
        if (!MeiQiaService.c || this.f == null || cvVar == null || !this.b.c()) {
            String trackId = a.getTrackId();
            String visitId = a.getVisitId();
            String enterpriseId = a.getEnterpriseId();
            Map hashMap = new HashMap();
            if (!TextUtils.isEmpty(str)) {
                hashMap.put("group_token", str);
            }
            if (!TextUtils.isEmpty(str2)) {
                hashMap.put("agent_token", str2);
            }
            hashMap.put("fallback", Integer.valueOf(mQScheduleRule.getValue()));
            hashMap.put("visit_id", visitId);
            hashMap.put("track_id", trackId);
            hashMap.put("ent_id", Long.valueOf(enterpriseId));
            a(new y(this, bcVar, hashMap, cvVar));
            return;
        }
        a(cvVar);
    }

    public void a(MQAgent mQAgent) {
        this.f = mQAgent;
        MQMessageManager.getInstance(this.e).setCurrentAgent(mQAgent);
    }

    public void a(MQClient mQClient) {
        if (mQClient != null) {
            a = mQClient;
            this.b.k(a.getTrackId());
            e.b((("current info: t = " + mQClient.getTrackId()) + " b " + mQClient.getBrowserId()
            ) + " e " + mQClient.getEnterpriseId());
        }
    }

    public void a(MQMessage mQMessage, OnProgressCallback onProgressCallback) {
        if (j.a()) {
            this.h.a(mQMessage.getConversation_id(), mQMessage.getId(), a.getTrackId(), Long
                    .parseLong(a.getEnterpriseId()), null);
            String absolutePath = Environment.getExternalStoragePublicDirectory(Environment
                    .DIRECTORY_DOWNLOADS).getAbsolutePath();
            try {
                String optString = new JSONObject(mQMessage.getExtra()).optString("filename");
                int lastIndexOf = optString.lastIndexOf(".");
                String substring = optString.substring(0, lastIndexOf);
                optString = substring + mQMessage.getId() + optString.substring(lastIndexOf,
                        optString.length());
                File file = new File(absolutePath);
                if (!file.exists()) {
                    file.mkdirs();
                }
                this.h.a(mQMessage, new File(file, optString), new ah(this, onProgressCallback));
                return;
            } catch (Exception e) {
                a(new al(this, onProgressCallback));
                return;
            }
        }
        a(new ag(this, onProgressCallback));
    }

    public void a(OnEndConversationCallback onEndConversationCallback) {
        this.h.a(new u(this, onEndConversationCallback));
    }

    public void a(OnGetMQClientIdCallBackOn onGetMQClientIdCallBackOn) {
        this.h.a(new az(this, onGetMQClientIdCallBackOn));
    }

    public void a(OnGetMessageListCallback onGetMessageListCallback) {
        long b = this.b.b();
        int parseInt = Integer.parseInt(a.getEnterpriseId());
        String a = i.a(b);
        this.h.a(a.getTrackId(), ActivityChooserViewAdapter.MAX_ACTIVITY_COUNT_UNLIMITED, 0,
                parseInt, a, 1, new s(this, onGetMessageListCallback));
    }

    public void a(OnInitCallback onInitCallback) {
        MQClient d = d();
        if ((d != null ? 1 : null) == null) {
            a(new c(this, onInitCallback));
        } else if (onInitCallback != null) {
            onInitCallback.onSuccess(d.getTrackId());
        }
    }

    public void a(SimpleCallback simpleCallback) {
        Map hashMap = new HashMap();
        hashMap.put("enterprise_id", a.getEnterpriseId());
        this.h.a(hashMap, new ae(this, simpleCallback));
    }

    public void a(String str) {
        this.h.b(str);
    }

    public void a(String str, int i, String str2, SimpleCallback simpleCallback) {
        this.h.a(str, i, str2, new w(this, simpleCallback));
    }

    public void a(String str, OnGetMQClientIdCallBackOn onGetMQClientIdCallBackOn) {
        this.h.a(str, new az(this, onGetMQClientIdCallBackOn));
    }

    public void a(String str, OnGetMessageListCallback onGetMessageListCallback) {
        int parseInt;
        String a;
        String trackId;
        long j;
        long o;
        if (TextUtils.isEmpty(str)) {
            o = this.b.o();
            parseInt = Integer.parseInt(a.getEnterpriseId());
            a = i.a(o);
            trackId = a.getTrackId();
            j = o;
        } else {
            CharSequence n = this.b.n(str);
            if (!TextUtils.isEmpty(n)) {
                CharSequence charSequence = n;
            }
            if (j.a(str, this.b) == null) {
                a(null, onGetMessageListCallback);
                return;
            }
            String m = this.b.m();
            this.b.k(str);
            o = this.b.o();
            parseInt = Integer.parseInt(this.b.h());
            a = i.a(o);
            trackId = this.b.d();
            this.b.k(m);
            j = o;
        }
        this.h.a(trackId, ActivityChooserViewAdapter.MAX_ACTIVITY_COUNT_UNLIMITED, 0, parseInt,
                a, 1, new q(this, j, onGetMessageListCallback));
    }

    public void a(String str, OnInitCallback onInitCallback) {
        if (!TextUtils.isEmpty(str)) {
            String n = this.b.n(str);
            Object obj = (TextUtils.isEmpty(n) || !d(n)) ? null : 1;
            if (obj == null) {
                this.h.b(str, new n(this, str, onInitCallback));
                return;
            }
            this.b.k(n);
            if (onInitCallback != null) {
                onInitCallback.onSuccess(n);
            }
        } else if (onInitCallback != null) {
            onInitCallback.onFailure(ErrorCode.PARAMETER_ERROR, "customizedId can't be empty");
        }
    }

    public void a(String str, OnRegisterDeviceTokenCallback onRegisterDeviceTokenCallback) {
        this.h.a(str, new j(this, onRegisterDeviceTokenCallback));
    }

    public void a(String str, String str2, MQScheduleRule mQScheduleRule) {
        this.j = str;
        this.i = str2;
        this.k = mQScheduleRule;
    }

    public void a(String str, String str2, String str3, OnMessageSendCallback
            onMessageSendCallback) {
        MQMessage mQMessage = new MQMessage(str2);
        mQMessage.setContent(str);
        mQMessage.setMedia_url(str3);
        mQMessage.setFrom_type("client");
        a(mQMessage);
        this.d.a(mQMessage);
        if ("text".equals(str2)) {
            a(mQMessage, onMessageSendCallback);
        } else {
            a(str2, str3, new aw(this, mQMessage, str2, onMessageSendCallback));
        }
    }

    public void a(String str, List<String> list, Map<String, String> map, SimpleCallback
            simpleCallback) {
        MQMessage mQMessage = new MQMessage("text");
        mQMessage.setContent(str);
        mQMessage.setMedia_url("");
        List arrayList = new ArrayList();
        arrayList.add(mQMessage);
        if (list == null || list.size() <= 0) {
            a(arrayList, (Map) map, simpleCallback);
        } else {
            a(arrayList, (List) list, (Map) map, simpleCallback);
        }
    }

    public void a(Map<String, String> map, OnClientInfoCallback onClientInfoCallback) {
        try {
            String jSONObject = c.a((Map) map).toString();
            if (jSONObject.equals(this.b.l())) {
                onClientInfoCallback.onSuccess();
                return;
            }
            String trackId = a.getTrackId();
            String enterpriseId = a.getEnterpriseId();
            JSONObject a = c.a(new HashMap(map));
            Map hashMap = new HashMap();
            hashMap.put("attrs", a);
            hashMap.put("track_id", trackId);
            hashMap.put("ent_id", enterpriseId);
            if (map.containsKey(UserDao.AVATAR)) {
                this.b.l((String) map.get(UserDao.AVATAR));
            }
            this.h.a(hashMap, new l(this, jSONObject, onClientInfoCallback));
        } catch (Exception e) {
            if (onClientInfoCallback != null) {
                onClientInfoCallback.onFailure(ErrorCode.PARAMETER_ERROR, "parameter error");
            }
        }
    }

    public void b() {
        this.d.a();
    }

    public void b(String str) {
        this.h.a(str);
    }

    public String c() {
        return a.getTrackId();
    }

    public MQClient d() {
        return j.a(this.b.d(), this.b);
    }

    public MQAgent e() {
        return this.f;
    }
}
