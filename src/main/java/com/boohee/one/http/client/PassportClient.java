package com.boohee.one.http.client;

import android.content.Context;

import com.alipay.sdk.cons.b;
import com.alipay.sdk.cons.c;
import com.android.volley.AuthFailureError;
import com.boohee.database.UserPreference;
import com.boohee.one.MyApplication;
import com.boohee.one.http.JsonCallback;
import com.boohee.one.http.JsonParams;
import com.boohee.one.http.RequestManager;
import com.boohee.utils.BlackTech;
import com.boohee.utils.Coder;

import java.util.Map;

public class PassportClient extends BaseJsonRequest {
    public static final String KEY       = "ifood";
    public static final String QA_KEY    = "ifood";
    public static final String QA_SECRET = "boohee-test-test-test";
    public static final String SECRET    = "0d1efea355c74c17deaf5fb10c6ca68f57af7ecc";

    public PassportClient(int method, String url, JsonParams jsonParams, JsonCallback callback) {
        super(method, url, jsonParams, callback, callback);
    }

    public Map<String, String> getHeaders() throws AuthFailureError {
        Map<String, String> headers = super.getHeaders();
        if (headers != null) {
            headers.put(c.f, BooheeClient.getHost(BooheeClient.PASSPORT));
        }
        return headers;
    }

    public static void get(String url, Context context, JsonCallback callback) {
        get(url, null, context, callback);
    }

    public static void get(String url, JsonParams jsonParams, Context context, JsonCallback
            callback) {
        RequestManager.addRequest(new PassportClient(0, BooheeClient.getUrlWithQueryString
                (BooheeClient.build(BooheeClient.PASSPORT).getDefaultURL(url), signParams
                        (jsonParams)), null, callback), context);
    }

    public static void post(String url, JsonParams jsonParams, Context context, JsonCallback
            callback) {
        RequestManager.addRequest(new PassportClient(1, BooheeClient.build(BooheeClient.PASSPORT)
                .getDefaultURL(url), signParams(jsonParams), callback), context);
    }

    public static void put(String url, JsonParams jsonParams, Context context, JsonCallback
            callback) {
        RequestManager.addRequest(new PassportClient(2, BooheeClient.build(BooheeClient.PASSPORT)
                .getDefaultURL(url), signParams(jsonParams), callback), context);
    }

    public static void delete(String url, JsonParams jsonParams, Context context, JsonCallback
            callback) {
        RequestManager.addRequest(new PassportClient(3, BooheeClient.build(BooheeClient.PASSPORT)
                .getDefaultURL(url), signParams(jsonParams), callback), context);
    }

    private static JsonParams signParams(JsonParams jsonParams) {
        String key = BlackTech.isApiProduction().booleanValue() ? "ifood" : "ifood";
        if (jsonParams == null) {
            jsonParams = new JsonParams();
        }
        jsonParams.put("token", UserPreference.getToken(MyApplication.getContext()));
        jsonParams.put("user_key", UserPreference.getUserKey(MyApplication.getContext()));
        JsonParams params = new JsonParams();
        params.put(b.h, key);
        params.put("context_params", contextParams(jsonParams.toString()));
        params.put("sign", signature(jsonParams.toString()));
        return params;
    }

    private static String contextParams(String json) {
        try {
            return new String(Coder.encryptBASE64(json.getBytes()));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static String signature(String json) {
        try {
            return new String(Coder.encryptBASE64(Coder.encryptHMAC(((BlackTech.isApiProduction()
                    .booleanValue() ? "ifood" : "ifood") + contextParams(json)).getBytes(),
                    BlackTech.isApiProduction().booleanValue() ? SECRET : QA_SECRET)));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
