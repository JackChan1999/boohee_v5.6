package com.zxinsight;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build.VERSION;
import android.text.TextUtils;

import com.alipay.sdk.sys.a;
import com.umeng.socialize.net.utils.SocializeProtocolConstants;
import com.zxinsight.analytics.a.b;
import com.zxinsight.analytics.domain.response.Marketing;
import com.zxinsight.analytics.domain.response.MarketingResponse;
import com.zxinsight.common.base.MWActivity;
import com.zxinsight.common.http.Request;
import com.zxinsight.common.http.Request.HttpMethod;
import com.zxinsight.common.http.ae;
import com.zxinsight.common.util.DeviceInfoUtils;
import com.zxinsight.common.util.c;
import com.zxinsight.common.util.h;
import com.zxinsight.common.util.l;
import com.zxinsight.common.util.m;
import com.zxinsight.common.util.o;
import com.zxinsight.mlink.MLinkIntentBuilder;
import com.zxinsight.mlink.d;
import com.zxinsight.mlink.domain.DPLsResponse;
import com.zxinsight.mlink.domain.DPLsResponse.DPLsData;
import com.zxinsight.share.activity.ActivityFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

public class MarketingHelper {
    private static MarketingHelper a = null;
    private MarketingResponse b;
    private Map<String, Marketing> c = new HashMap();

    private MarketingHelper(Context context) {
        MWConfiguration.initContext(context);
    }

    public static MarketingHelper currentMarketing(Context context) {
        if (a == null) {
            a = new MarketingHelper(context.getApplicationContext());
        }
        return a;
    }

    public String checkWindowKey(String str) {
        return getMarketing(str).k;
    }

    @Deprecated
    public String getWindowKey(String str) {
        return getMarketing(str).k;
    }

    private boolean isMLinkCallbackEnable(String str) {
        return "1".equals(getMarketing(str).mlcb);
    }

    private Map<String, String> getDefaultMLink(String str) {
        return getMarketing(str).mp;
    }

    String getMLinkKey(String str) {
        return getMarketing(str).mk;
    }

    private Map<String, String> getDefaultLandingPage(String str) {
        return getMarketing(str).lp;
    }

    boolean checkShare(String str) {
        return !"0".equals(getMarketing(str).ss);
    }

    private String getDisplayType(String str) {
        return getMarketing(str).dt;
    }

    public String getActivityKey(String str) {
        return getMarketing(str).ak;
    }

    public String getImageURL(String str) {
        if (VERSION.SDK_INT < 14 || !l.b(getMarketing(str).getIw())) {
            return getMarketing(str).getIu();
        }
        return getMarketing(str).getIw();
    }

    String getThumbURL(String str) {
        return getMarketing(str).getTu();
    }

    public String getTitle(String str) {
        return getMarketing(str).t;
    }

    public String getDescription(String str) {
        return getMarketing(str).dc;
    }

    public String getShareTitle(String str) {
        return getMarketing(str).sh;
    }

    public String getShareText(String str) {
        return getMarketing(str).sc;
    }

    public String getWebviewURL(String str) {
        return getMarketing(str).getAu();
    }

    public boolean needLogin(String str) {
        return "1".equals(getLogin(str));
    }

    String getLogin(String str) {
        return getMarketing(str).rl;
    }

    String getWebviewTitle(String str) {
        return getMarketing(str).vt;
    }

    String getFloatWebURL(String str) {
        return getMarketing(str).getFu();
    }

    String getFloatWebPosition(String str) {
        return getMarketing(str).fp;
    }

    String getSharedURL(String str) {
        return getMarketing(str).getSu();
    }

    public void click(Context context, String str) {
        clickWithMLink(context, str, new JSONObject(), new JSONObject());
    }

    public void clickWithMLink(Context context, String str, Map<String, String> map, Map<String,
            String> map2) {
        if (map == null) {
            map = new HashMap();
        }
        if (map2 == null) {
            map2 = new HashMap();
        }
        clickWithMLink(context, str, new JSONObject(map), new JSONObject(map2));
    }

    public void clickWithMLink(Context context, String str, JSONObject jSONObject, JSONObject
            jSONObject2) {
        clickWithMLinkCallbackUri(context, str, null, null, jSONObject, jSONObject2);
    }

    public void clickWithMLinkCallbackUri(Context context, String str, String str2) {
        clickWithMLinkCallbackUri(context, str, str2, null, null, null);
    }

    public void clickWithMLinkCallbackUri(Context context, String str, String str2, JSONObject
            jSONObject) {
        clickWithMLinkCallbackUri(context, str, str2, jSONObject, null, null);
    }

    public void clickWithMLinkCallbackUri(Context context, String str, String str2, JSONObject
            jSONObject, JSONObject jSONObject2, JSONObject jSONObject3) {
        if (isActive(str)) {
            JSONObject jSONObject4;
            JSONObject jSONObject5;
            if (jSONObject2 == null) {
                jSONObject4 = new JSONObject();
            } else {
                jSONObject4 = jSONObject2;
            }
            if (jSONObject3 == null) {
                jSONObject5 = new JSONObject();
            } else {
                jSONObject5 = jSONObject3;
            }
            String displayType = getDisplayType(str);
            TrackAgent.currentEvent().onActionClick(str);
            if ("0".equalsIgnoreCase(displayType)) {
                toNativeWebView(context, str);
                return;
            } else if ("1".equalsIgnoreCase(displayType)) {
                share(context, str);
                return;
            } else if ("2".equalsIgnoreCase(displayType)) {
                try {
                    MLinkIntentBuilder.buildIntent(context, Uri.parse(getWebviewURL(str)));
                    return;
                } catch (Exception e) {
                    return;
                }
            } else if ("4".equalsIgnoreCase(displayType)) {
                goMLink(context, str, str2, jSONObject, jSONObject4, jSONObject5);
                return;
            } else {
                return;
            }
        }
        c.d("click failed,the windowKey:" + str + " is closed");
    }

    private void goMLink(Context context, String str, String str2, JSONObject jSONObject,
                         JSONObject jSONObject2, JSONObject jSONObject3) {
        if (jSONObject == null) {
            jSONObject = new JSONObject();
        }
        if (l.a(jSONObject2) && l.b(getDefaultMLink(str))) {
            jSONObject2 = new JSONObject(getDefaultMLink(str));
        }
        TrackAgent.currentEvent().onMLinkClick(str, jSONObject2);
        try {
            jSONObject2.put("mw_mlink_appid", o.c());
            jSONObject2.put("mw_mlink_k", str);
            jSONObject2.put("mw_mlink_ak", getActivityKey(str));
            if (!TextUtils.isEmpty(getMLinkKey(str))) {
                jSONObject2.put("mw_mk", getMLinkKey(str));
            }
        } catch (JSONException e) {
        }
        String webviewURL = getWebviewURL(str);
        String a = d.a(webviewURL, jSONObject2);
        if (isMLinkCallbackEnable(str) && !TextUtils.isEmpty(str2)) {
            a = joinURL(a, getMLinkCallback(str2, jSONObject));
        }
        if (l.a(a)) {
            a = webviewURL;
        }
        try {
            MLinkIntentBuilder.buildIntent(context, Uri.parse(a));
        } catch (Exception e2) {
            sendEvent(str, jSONObject2, a);
            if (l.a(jSONObject3) && l.b(getDefaultLandingPage(str))) {
                jSONObject3 = new JSONObject(getDefaultLandingPage(str));
            }
            a = d.a(getSharedURL(str), jSONObject3);
            if (l.b(a)) {
                try {
                    MLinkIntentBuilder.buildIntent(context, Uri.parse(a));
                } catch (Exception e3) {
                }
            }
        }
    }

    private String joinURL(String str, String str2) {
        if (TextUtils.isEmpty(str2)) {
            return str;
        }
        if (str.contains("?")) {
            return str + a.b + "mw_mlink_cb" + "=" + str2;
        }
        return str + "?" + "mw_mlink_cb" + "=" + str2;
    }

    private String getMLinkCallback(String str, JSONObject jSONObject) {
        if (str.contains("://")) {
            return Uri.encode(d.a(str, jSONObject, 1));
        }
        DPLsResponse a = b.a();
        if (a != null) {
            for (DPLsData dPLsData : a.getData().getDPLs()) {
                if (str.equals(dPLsData.k)) {
                    return Uri.encode(d.a(dPLsData.dp, jSONObject, 1));
                }
            }
        }
        return null;
    }

    private void sendEvent(String str, JSONObject jSONObject, String str2) {
        String str3 = com.zxinsight.analytics.a.a.f;
        JSONObject jSONObject2 = new JSONObject();
        m a = m.a();
        try {
            jSONObject2.put(SocializeProtocolConstants.PROTOCOL_KEY_AK, o.c());
            jSONObject2.put(a.j, DeviceInfoUtils.f(MWConfiguration.getContext()));
            jSONObject2.put(a.h, "3.9.160727");
            jSONObject2.put("k", str);
            jSONObject2.put("ack", getActivityKey(str));
            jSONObject2.put("dp", str2);
            if (!TextUtils.isEmpty(a.d())) {
                jSONObject2.put(SocializeProtocolConstants.PROTOCOL_KEY_UID, a.d());
            }
            if (jSONObject != null) {
                jSONObject2.put(SocializeProtocolConstants.PROTOCOL_KEY_DT, jSONObject);
            }
            jSONObject2.put("m", DeviceInfoUtils.j());
            jSONObject2.put("mf", DeviceInfoUtils.i());
            jSONObject2.put("fp", a.c("fp"));
            jSONObject2.put("d", DeviceInfoUtils.c(MWConfiguration.getContext()));
            jSONObject2.put(SocializeProtocolConstants.PROTOCOL_KEY_OS, DeviceInfoUtils.b());
            jSONObject2.put("osv", DeviceInfoUtils.g());
            jSONObject2.put("sr", DeviceInfoUtils.d(MWConfiguration.getContext()));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Request aeVar = new ae(HttpMethod.POST, str3, new j(this));
        aeVar.a(jSONObject2);
        com.zxinsight.common.http.d.a(MWConfiguration.getContext()).a(aeVar);
    }

    private void share(Context context, String str) {
        if (isActive(str)) {
            ShareHelper.share((Activity) context, str);
        } else {
            c.d("share failed,the windowKey:" + str + " is closed");
        }
    }

    @Deprecated
    public void toNativeWebView(Context context, String str) {
        if (isActive(str)) {
            c.b("MW toNativeWebView = " + str);
            new ActivityFactory(context, MWActivity.ACTIVITY_TYPE_WEBVIEW).toNativeBrowser(str);
            return;
        }
        c.d("to native webview failed,the windowKey:" + str + " is closed");
    }

    private MarketingResponse getMarketingResponse() {
        if (this.b != null) {
            return this.b;
        }
        Object c = m.a().c("marketing_sp");
        if (!TextUtils.isEmpty(c)) {
            try {
                this.b = (MarketingResponse) h.a(new JSONObject(c), MarketingResponse.class);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (this.b != null) {
            return this.b;
        }
        return new MarketingResponse();
    }

    private Marketing getMarketing(String str) {
        if (l.a(str)) {
            return new Marketing();
        }
        if (!l.b(this.c)) {
            this.b = getMarketingResponse();
            if (this.b != null && l.b(this.b.getData())) {
                for (Marketing marketing : this.b.getData()) {
                    if (marketing != null && str.equals(marketing.k)) {
                        return marketing;
                    }
                }
            }
            return new Marketing();
        } else if (this.c.containsKey(str)) {
            return (Marketing) this.c.get(str);
        } else {
            return new Marketing();
        }
    }

    public ArrayList<String> checkAll() {
        ArrayList<String> arrayList = new ArrayList();
        if (l.b(this.c)) {
            for (String add : this.c.keySet()) {
                arrayList.add(add);
            }
        } else {
            this.b = getMarketingResponse();
            if (this.b != null && l.b(this.b.getData())) {
                for (Marketing marketing : this.b.getData()) {
                    arrayList.add(marketing.k);
                }
            }
        }
        return arrayList;
    }

    public ArrayList<String> checkAllSorted(ArrayList<String> arrayList) {
        ArrayList<String> arrayList2 = new ArrayList();
        ArrayList checkAll = checkAll();
        if (arrayList == null || checkAll == null) {
            return arrayList2;
        }
        Iterator it = arrayList.iterator();
        while (it.hasNext()) {
            String str = (String) it.next();
            if (checkAll.contains(str)) {
                arrayList2.add(str);
            }
        }
        return arrayList2;
    }

    @Deprecated
    public boolean isAllClosed() {
        if (l.b(this.c)) {
            return false;
        }
        this.b = getMarketingResponse();
        if (this.b == null || !l.b(this.b.getData())) {
            return true;
        }
        return false;
    }

    public boolean isActive(String str) {
        if (l.a(str)) {
            return false;
        }
        if (l.b(this.c)) {
            return this.c.containsKey(str);
        }
        return l.b(checkWindowKey(str));
    }

    public void renderMarketing(RenderParam renderParam) {
        renderMarketingWithMLink(renderParam);
    }

    public void renderMarketingWithMLink(RenderParam renderParam) {
        try {
            if (renderParam.getParent() == null || renderParam.getListener() == null) {
                throw new Exception();
            } else if (l.b(checkWindowKey(renderParam.getWindowKey()))) {
                renderParam.getParent().setOnClickListener(new k(this, renderParam));
                renderParam.getListener().setTitle(getTitle(renderParam.getWindowKey()));
                renderParam.getListener().setDescription(getDescription(renderParam.getWindowKey
                        ()));
                renderParam.getListener().setImage(getImageURL(renderParam.getWindowKey()));
                TrackAgent.currentEvent().onImpression(renderParam.getWindowKey());
            } else if (m.a().E()) {
                update(renderParam.getWindowKey(), new l(this, renderParam));
            }
        } catch (Exception e) {
        }
    }

    @Deprecated
    public void updateMarketing() {
        updateMarketing(null);
    }

    @Deprecated
    public void updateMarketing(String str) {
        c.e("MarketingHelper:prepare to update Marketing");
        String str2 = com.zxinsight.analytics.a.a.e;
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put(SocializeProtocolConstants.PROTOCOL_KEY_AK, o.c());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Request aeVar = new ae(HttpMethod.POST, str2, new n(this, str));
        aeVar.a(jSONObject);
        com.zxinsight.common.http.d.a(MWConfiguration.getContext()).a(aeVar);
    }

    public void update(UpdateMarketingListener updateMarketingListener) {
        update(null, updateMarketingListener);
    }

    public void update(String str, UpdateMarketingListener updateMarketingListener) {
        String str2 = com.zxinsight.analytics.a.a.c;
        JSONObject jSONObject = new JSONObject();
        m a = m.a();
        try {
            jSONObject.put(SocializeProtocolConstants.PROTOCOL_KEY_AK, o.c());
            jSONObject.put(a.j, DeviceInfoUtils.f(MWConfiguration.getContext()));
            jSONObject.put(a.h, "3.9.160727");
            if (!TextUtils.isEmpty(str)) {
                jSONObject.put("k", str);
            }
            if (!TextUtils.isEmpty(a.d())) {
                jSONObject.put(SocializeProtocolConstants.PROTOCOL_KEY_UID, a.d());
            }
            if (u.a().b()) {
                jSONObject.put("lat", a.c(com.baidu.location.a.a.int));
                jSONObject.put("lng", a.c(com.baidu.location.a.a.char));
            }
            jSONObject.put("fp", a.c("fp"));
            jSONObject.put("d", DeviceInfoUtils.c(MWConfiguration.getContext()));
            jSONObject.put(SocializeProtocolConstants.PROTOCOL_KEY_OS, DeviceInfoUtils.b());
            jSONObject.put("sr", DeviceInfoUtils.d(MWConfiguration.getContext()));
            jSONObject.put("re", a.z());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Request aeVar = new ae(HttpMethod.POST, str2, new o(this, str, updateMarketingListener));
        aeVar.a(jSONObject);
        aeVar.d(1000);
        aeVar.c(1000);
        aeVar.b(0);
        com.zxinsight.common.http.d.a(MWConfiguration.getContext()).a(aeVar);
    }

    private void saveMarketing(MarketingResponse marketingResponse) {
        MWConfiguration.getContext().sendBroadcast(new Intent("com.magicwindow.marketing.update" +
                ".MW_MESSAGE"));
        this.c.clear();
        this.b = marketingResponse;
        m.a().a("marketing_sp", h.a((Object) marketingResponse));
        if (l.b(marketingResponse.getData())) {
            for (Marketing marketing : marketingResponse.getData()) {
                CustomStyle.setStyle(marketing.k, marketing.getSy());
                this.c.put(marketing.k, marketing);
            }
        }
    }
}
