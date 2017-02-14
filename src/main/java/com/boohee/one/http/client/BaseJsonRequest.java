package com.boohee.one.http.client;

import android.net.Uri;
import android.text.TextUtils;

import com.alipay.sdk.cons.b;
import com.alipay.sdk.cons.c;
import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonRequest;
import com.boohee.database.UserPreference;
import com.boohee.one.MyApplication;
import com.boohee.one.http.DnspodFree;
import com.boohee.one.http.JsonParams;
import com.boohee.utility.Config;
import com.boohee.utils.AppUtils;
import com.boohee.utils.DateHelper;
import com.boohee.utils.SystemUtil;
import com.xiaomi.account.openauth.utils.Network;

import java.util.HashMap;
import java.util.Map;

public class BaseJsonRequest extends JsonRequest<String> {
    private static final String VERSION_CODE = String.valueOf(Config.getVersionCode());
    private String     mAPI;
    private String     mHost;
    private JsonParams mJsonParams;

    public BaseJsonRequest(int method, String url, JsonParams jsonParams, Listener<String>
            listener, ErrorListener errorListener) {
        JsonParams jsonParams2;
        String bestUrl = DnspodFree.getBestUrl(url);
        String host = Uri.parse(url).getHost();
        if (jsonParams == null) {
            jsonParams2 = null;
        } else {
            jsonParams2 = jsonParams;
        }
        this(method, bestUrl, host, jsonParams2, listener, errorListener);
    }

    public BaseJsonRequest(int method, String url, String host, JsonParams jsonParams,
                           Listener<String> listener, ErrorListener errorListener) {
        String str;
        String bestUrl = DnspodFree.getBestUrl(url);
        if (jsonParams == null) {
            str = null;
        } else {
            str = jsonParams.toString();
        }
        super(method, bestUrl, str, listener, errorListener);
        parseUrlToApi(jsonParams, host);
    }

    private void parseUrlToApi(JsonParams jsonParams, String host) {
        this.mJsonParams = jsonParams;
        this.mHost = host;
        if (!TextUtils.isEmpty(this.mHost)) {
            this.mAPI = this.mHost.substring(0, this.mHost.indexOf("."));
        }
    }

    public Map<String, String> getHeaders() throws AuthFailureError {
        Map<String, String> headers = new HashMap();
        headers.put("App-Version", Config.getVersionName());
        headers.put("App-Device", "Android");
        headers.put("Version-Code", VERSION_CODE);
        headers.put(c.f, this.mHost);
        headers.put("Os-Version", SystemUtil.getVersionCode());
        headers.put("Phone-Model", SystemUtil.getPhoneModel());
        headers.put("Accept", "application/json");
        headers.put(Network.USER_AGENT, "Android/Volley");
        headers.put("channel", AppUtils.getChannel(MyApplication.getContext()));
        headers.put("token", UserPreference.getToken(MyApplication.getContext()));
        headers.put("User-Key", UserPreference.getUserKey(MyApplication.getContext()));
        headers.put("Utc-Offset", String.valueOf(DateHelper.getOffsetFromUtc()));
        otherSpecialHeader(this.mAPI, headers);
        return headers;
    }

    protected Response<String> parseNetworkResponse(NetworkResponse response) {
        if (response == null) {
            return null;
        }
        String parsed;
        try {
            parsed = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
        } catch (Exception e) {
            parsed = new String(response.data);
        }
        return Response.success(parsed, HttpHeaderParser.parseCacheHeaders(response));
    }

    public JsonParams getJsonParams() {
        return this.mJsonParams;
    }

    public static JsonParams addDefaultParams(String url, JsonParams jsonParams) {
        if (jsonParams == null) {
            jsonParams = new JsonParams();
        }
        jsonParams.put("token", UserPreference.getToken(MyApplication.getContext()));
        jsonParams.put("user_key", UserPreference.getUserKey(MyApplication.getContext()));
        jsonParams.put("app_version", Config.getVersionName());
        jsonParams.put("app_device", "Android");
        jsonParams.put("os_version", SystemUtil.getVersionCode());
        jsonParams.put("phone_model", SystemUtil.getPhoneModel());
        jsonParams.put("channel", AppUtils.getChannel(MyApplication.getContext()));
        otherSpecialParams(url, jsonParams);
        return jsonParams;
    }

    private static void otherSpecialParams(String url, JsonParams jsonParams) {
        if (!apiIsFoodOrIFood(url)) {
            jsonParams.put(b.h, BooheeClient.ONE);
        }
    }

    private void otherSpecialHeader(String api, Map<String, String> headers) {
        boolean isFoodOriFood = api.equalsIgnoreCase(BooheeClient.FOOD) || api.equalsIgnoreCase
                ("ifood");
        if (!isFoodOriFood) {
            headers.put("App-Key", BooheeClient.ONE);
        }
    }

    private static boolean apiIsFoodOrIFood(String api) {
        try {
            String subUrl = api.substring(7);
            if (subUrl.startsWith(BooheeClient.FOOD) || subUrl.startsWith("ifood")) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
