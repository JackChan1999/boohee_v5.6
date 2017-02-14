package com.zxinsight;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.text.TextUtils;

import com.umeng.socialize.net.utils.SocializeProtocolConstants;
import com.zxinsight.analytics.a.b;
import com.zxinsight.common.http.Request;
import com.zxinsight.common.http.Request.HttpMethod;
import com.zxinsight.common.http.ae;
import com.zxinsight.common.util.DeviceInfoUtils;
import com.zxinsight.common.util.NoEmptyHashMap;
import com.zxinsight.common.util.c;
import com.zxinsight.common.util.f;
import com.zxinsight.common.util.h;
import com.zxinsight.common.util.l;
import com.zxinsight.common.util.m;
import com.zxinsight.common.util.o;
import com.zxinsight.mlink.MLinkCallback;
import com.zxinsight.mlink.MLinkException;
import com.zxinsight.mlink.MLinkIntentBuilder;
import com.zxinsight.mlink.a;
import com.zxinsight.mlink.d;
import com.zxinsight.mlink.domain.DPLsResponse;
import com.zxinsight.mlink.domain.DPLsResponse.DPLsData;
import com.zxinsight.mlink.domain.MLinkIntent;
import com.zxinsight.mlink.domain.MLinkResult;
import com.zxinsight.mlink.domain.YYBResponse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

public class MLink {
    private static volatile MLink         link;
    private                 MLinkCallback defaultMLinkCallback;
    private NoEmptyHashMap<String, DPLsData> dplMap = new NoEmptyHashMap();
    private String      mLinkKeyForCallback;
    private MLinkResult mLinkResult;
    private NoEmptyHashMap<String, MLinkIntent> routerMap       = new NoEmptyHashMap();
    private Map<String, MLinkIntent>            tempRegisterMap = new HashMap();
    public String urlCB;

    private MLink(Context context) {
        MWConfiguration.initContext(context);
        onReferral();
    }

    public static MLink getInstance(Context context) {
        if (link == null) {
            link = new MLink(context.getApplicationContext());
        }
        return link;
    }

    public void registerWithAnnotation(Context context) {
        a.a(context);
    }

    public String parseCallBackUri(String str) {
        if (l.a(this.urlCB)) {
            return null;
        }
        JSONObject jSONObject = new JSONObject();
        Object c = d.c(this.urlCB);
        if (!TextUtils.isEmpty(c)) {
            try {
                jSONObject.put(c, str);
            } catch (JSONException e) {
                return null;
            }
        }
        return joinURL(d.a(this.urlCB, jSONObject, 2));
    }

    public boolean callbackEnable() {
        c.e("callbackEnable uri = " + this.urlCB);
        return l.b(this.urlCB) && this.urlCB.contains("://");
    }

    private String joinURL(String str) {
        c.e("CallBackUri uri = " + str);
        if (l.a(str)) {
            return "";
        }
        if (str.contains("?")) {
            return str + com.alipay.sdk.sys.a.b + "mw_mlink_appid" + "=" + o.c() + com.alipay.sdk
                    .sys.a.b + "mw_mlink_k" + "=" + "mw_dump_key" + com.alipay.sdk.sys.a.b +
                    "mw_mlink_ak" + "=" + "mw_dump_key" + com.alipay.sdk.sys.a.b + "mw_mk" + "="
                    + getMLinkCallbackGMK(this.mLinkKeyForCallback);
        }
        return str + "?" + "mw_mlink_appid" + "=" + o.c() + com.alipay.sdk.sys.a.b + "mw_mlink_k"
                + "=" + "mw_dump_key" + com.alipay.sdk.sys.a.b + "mw_mlink_ak" + "=" +
                "mw_dump_key" + com.alipay.sdk.sys.a.b + "mw_mk" + "=" + getMLinkCallbackGMK(this
                .mLinkKeyForCallback);
    }

    public String parseCallBackUri(JSONObject jSONObject) {
        if (l.a(this.urlCB)) {
            return null;
        }
        return joinURL(d.a(this.urlCB, jSONObject, 2));
    }

    public void returnOriginApp(Activity activity, String str) {
        String parseCallBackUri = parseCallBackUri(str);
        if (l.a(parseCallBackUri)) {
            c.a("The scheme uri is null!");
            return;
        }
        try {
            MLinkIntentBuilder.buildIntent((Context) activity, Uri.parse(parseCallBackUri));
            activity.finish();
        } catch (Exception e) {
            c.a("The scheme uri [" + parseCallBackUri + "]is invalid!");
        }
    }

    public void returnOriginApp(Activity activity, JSONObject jSONObject) {
        String parseCallBackUri = parseCallBackUri(jSONObject);
        if (l.a(parseCallBackUri)) {
            c.a("The scheme uri is null!");
            return;
        }
        try {
            MLinkIntentBuilder.buildIntent((Context) activity, Uri.parse(parseCallBackUri));
            activity.finish();
        } catch (Exception e) {
            c.a("The scheme uri [" + parseCallBackUri + "]is invalid!");
        }
    }

    public String getLastChannelForMLink() {
        return m.a().K();
    }

    private String getMLinkCallbackGMK(String str) {
        DPLsResponse a = b.a();
        if (a != null) {
            for (DPLsData dPLsData : a.getData().getDPLs()) {
                if (str.equals(dPLsData.k)) {
                    return dPLsData.gmk;
                }
            }
        }
        return null;
    }

    private void onReferral() {
        if (needGetDPLs()) {
            getDPLs();
        }
    }

    private boolean needCheckYYB() {
        return !isFirstLaunch() && m.a().C();
    }

    private boolean needGetDPLs() {
        long D = m.a().D();
        return D <= 0 || D > m.a().B();
    }

    public void router(Uri uri) {
        if (uri == null) {
            routeToDefault(null);
        } else {
            doRouter(uri);
        }
    }

    @Deprecated
    public void router(Context context, Uri uri) {
        router(uri);
    }

    private void doRouter(Uri uri) {
        if (this.defaultMLinkCallback == null) {
            throw new MLinkException("The method of register and registerDefault must be called " +
                    "before the router method");
        }
        c.e("doRouter uri = " + uri);
        Uri a = d.a(uri);
        for (String str : this.dplMap.keySet()) {
            this.mLinkResult = d.a(a, Uri.parse(((DPLsData) this.dplMap.get(str)).dp));
            MLinkIntent mLinkIntent = (MLinkIntent) this.routerMap.get(str);
            if (this.mLinkResult.flag && mLinkIntent != null) {
                if (l.b(mLinkIntent.extraData)) {
                    this.mLinkResult.params.putAll(mLinkIntent.extraData);
                }
                saveTrackingData(uri);
                MLinkCallback mLinkCallback = mLinkIntent.callback;
                if (mLinkCallback != null) {
                    this.mLinkKeyForCallback = str;
                    mLinkCallback.execute(this.mLinkResult.params, a, MWConfiguration.getContext());
                    return;
                }
            }
        }
        routeToDefault(a);
    }

    private void saveTrackingData(Uri uri) {
        m.a().m(uri.getQueryParameter("mw_mlink_appid"));
        m.a().n(uri.getQueryParameter("mw_mlink_k"));
        m.a().o(uri.getQueryParameter("mw_mlink_ak"));
        m.a().p(uri.getQueryParameter("mw_tags"));
        if (l.b(uri.getQueryParameter("mw_ck"))) {
            m.a().a(uri.getQueryParameter("mw_mk"), uri.getQueryParameter("mw_slk"), uri
                    .getQueryParameter("mw_ck"), uri.getQueryParameter("mw_tk"));
        } else {
            m.a().a(uri.getQueryParameter("mw_mk"), uri.getQueryParameter("mw_slk"), uri
                    .getQueryParameter("mw_mlink_appid"), uri.getQueryParameter("mw_tk"));
        }
        TrackAgent.currentEvent().onMLinkViewOrInstall(this.mLinkResult.params, "mv");
    }

    private void routeToDefault(Uri uri) {
        if (uri != null) {
            Map a = d.a(uri.getEncodedQuery());
            if (this.defaultMLinkCallback != null) {
                this.defaultMLinkCallback.execute(a, uri, MWConfiguration.getContext());
            }
        } else if (this.defaultMLinkCallback != null) {
            this.defaultMLinkCallback.execute(new HashMap(), null, MWConfiguration.getContext());
        } else {
            throw new MLinkException("registerDefault must be called");
        }
    }

    public void registerDefault(MLinkCallback mLinkCallback) {
        if (mLinkCallback == null) {
            throw new MLinkException("MLinkCallback must not be null");
        }
        this.defaultMLinkCallback = mLinkCallback;
    }

    public void register(String str, MLinkCallback mLinkCallback) {
        if (str == null || mLinkCallback == null) {
            throw new MLinkException("key and MLinkCallback must not be null");
        }
        if (l.a(this.dplMap)) {
            com.zxinsight.common.b.a a = b.a();
            if (a == null) {
                this.tempRegisterMap.put(str, new MLinkIntent(str, mLinkCallback));
                return;
            } else if (f.a(a)) {
                List<DPLsData> dPLs = a.getData().getDPLs();
                if (l.b(dPLs)) {
                    for (DPLsData dPLsData : dPLs) {
                        this.dplMap.put(dPLsData.k, dPLsData);
                    }
                }
            }
        }
        if (this.dplMap.containsKey(str)) {
            this.routerMap.put(str, new MLinkIntent(str, mLinkCallback));
        }
    }

    private void getDPLs() {
        JSONObject jSONObject = new JSONObject();
        m a = m.a();
        try {
            jSONObject.put(SocializeProtocolConstants.PROTOCOL_KEY_AK, o.c());
            jSONObject.put(com.alipay.sdk.sys.a.j, DeviceInfoUtils.f(MWConfiguration.getContext()));
            jSONObject.put(com.alipay.sdk.sys.a.h, "3.9.160727");
            if (!TextUtils.isEmpty(a.d())) {
                jSONObject.put(SocializeProtocolConstants.PROTOCOL_KEY_UID, a.d());
            }
            if (isFirstLaunch()) {
                jSONObject.put("ddl", "1");
            } else {
                jSONObject.put("ddl", "0");
            }
            jSONObject.put("fp", a.c("fp"));
            jSONObject.put("d", DeviceInfoUtils.c(MWConfiguration.getContext()));
            jSONObject.put(SocializeProtocolConstants.PROTOCOL_KEY_OS, DeviceInfoUtils.b());
            jSONObject.put("osv", DeviceInfoUtils.g());
            jSONObject.put("m", DeviceInfoUtils.j());
            jSONObject.put("mf", DeviceInfoUtils.i());
            jSONObject.put("sr", DeviceInfoUtils.d(MWConfiguration.getContext()));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Request aeVar = new ae(HttpMethod.POST, com.zxinsight.analytics.a.a.h, new e(this));
        aeVar.a(jSONObject);
        com.zxinsight.common.http.d.a(MWConfiguration.getContext()).a(aeVar);
    }

    private boolean isFirstLaunch() {
        try {
            PackageInfo packageInfo = MWConfiguration.getContext().getPackageManager()
                    .getPackageInfo(MWConfiguration.getContext().getPackageName(), 0);
            int i = packageInfo.versionCode;
            String str = packageInfo.versionName;
            c.e("currentVersionCode = " + i);
            c.e("currentVersionName = " + str);
            int b = m.a().b("sp_versionCode", 0);
            String d = m.a().d("sp_versionName");
            c.e("lastVersionCode = " + b);
            c.e("lastVersionName = " + d);
            if (i == b && d.equals(str)) {
                c.e("it is first launch false");
                return false;
            }
            m.a().a("sp_versionCode", i);
            m.a().c("sp_versionName", str);
            c.e("it is first launch");
            return true;
        } catch (NameNotFoundException e) {
            c.e("it is package catch");
            return false;
        }
    }

    private void saveMLinks(String str) {
        try {
            JSONObject jSONObject = new JSONObject(str);
            if (h.c(jSONObject)) {
                DPLsResponse dPLsResponse = (DPLsResponse) h.a(jSONObject, DPLsResponse.class);
                if (f.a(dPLsResponse)) {
                    if (l.b(this.tempRegisterMap)) {
                        this.routerMap.putAll(this.tempRegisterMap);
                    }
                    List<DPLsData> dPLs = dPLsResponse.getData().getDPLs();
                    if (l.b(dPLs)) {
                        m.a().l(str);
                        m.a().d(m.a().D());
                        for (DPLsData dPLsData : dPLs) {
                            this.dplMap.put(dPLsData.k, dPLsData);
                        }
                    }
                    if (dPLsResponse.getData().ddl != null) {
                        router(Uri.parse(dPLsResponse.getData().ddl.dp));
                        if (this.mLinkResult != null && this.mLinkResult.flag) {
                            TrackAgent.currentEvent().onMLinkViewOrInstall(this.mLinkResult
                                    .params, "mi");
                        }
                    }
                    if ("1".equals(dPLsResponse.getData().yyb)) {
                        m.a().g(true);
                        return;
                    }
                    return;
                }
                return;
            }
            c.e("get MLinks error!");
        } catch (Exception e) {
            e.printStackTrace();
            c.e(e.getMessage());
        }
    }

    public void checkYYB() {
        if (needCheckYYB()) {
            m a = m.a();
            JSONObject jSONObject = new JSONObject();
            try {
                jSONObject.put(SocializeProtocolConstants.PROTOCOL_KEY_AK, o.c());
                jSONObject.put(com.alipay.sdk.sys.a.j, DeviceInfoUtils.f(MWConfiguration
                        .getContext()));
                jSONObject.put(com.alipay.sdk.sys.a.h, "3.9.160727");
                if (!TextUtils.isEmpty(a.d())) {
                    jSONObject.put(SocializeProtocolConstants.PROTOCOL_KEY_UID, a.d());
                }
                jSONObject.put("fp", a.c("fp"));
                jSONObject.put("d", DeviceInfoUtils.c(MWConfiguration.getContext()));
                jSONObject.put(SocializeProtocolConstants.PROTOCOL_KEY_OS, DeviceInfoUtils.b());
                jSONObject.put("osv", DeviceInfoUtils.g());
                jSONObject.put("m", DeviceInfoUtils.j());
                jSONObject.put("mf", DeviceInfoUtils.i());
                jSONObject.put("sr", DeviceInfoUtils.d(MWConfiguration.getContext()));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Request aeVar = new ae(HttpMethod.POST, com.zxinsight.analytics.a.a.g, new f(this));
            aeVar.c(1000);
            aeVar.d(1000);
            aeVar.b(1);
            aeVar.a(jSONObject);
            com.zxinsight.common.http.d.a(MWConfiguration.getContext()).a(aeVar);
            return;
        }
        c.e("do not need check YYB!");
    }

    private void saveYYB(JSONObject jSONObject) {
        try {
            if (h.c(jSONObject)) {
                YYBResponse yYBResponse = (YYBResponse) h.a(jSONObject, YYBResponse.class);
                if (f.a(yYBResponse)) {
                    String str = yYBResponse.getData().dp;
                    if (str != null) {
                        doRouter(Uri.parse(str));
                        return;
                    }
                    return;
                }
                return;
            }
            c.e("get MLink error!");
        } catch (Exception e) {
            c.e(e.getMessage());
        }
    }
}
