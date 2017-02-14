package com.boohee.one.http;

import android.text.TextUtils;

import com.alipay.sdk.cons.c;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.boohee.database.OnePreference;
import com.boohee.one.MyApplication;
import com.boohee.one.http.client.BaseJsonRequest;
import com.boohee.one.http.client.BooheeClient;
import com.boohee.utility.RegularUtils;
import com.boohee.utils.Helper;

import java.util.Map;

import org.json.JSONObject;

public class RequestManager {
    public static RequestQueue mRequestQueue = Volley.newRequestQueue(MyApplication.getContext(),
            new OkHttpStack());

    private RequestManager() {
    }

    public static void addRequest(Request<?> request, Object tag) {
        Helper.showLog("Volley/URL", request.getCacheKey());
        if (tag != null) {
            request.setTag(tag);
        }
        request.setRetryPolicy(new DefaultRetryPolicy(15000, 0, 1.0f));
        mRequestQueue.add(request);
        if (request != null && (request instanceof BaseJsonRequest)) {
            cacheIp((BaseJsonRequest) request);
        }
    }

    public static boolean isCacheApiIp() {
        if (TextUtils.isEmpty(OnePreference.getInstance(MyApplication.getContext()).getString
                (BooheeClient.API))) {
            return false;
        }
        return true;
    }

    public static void cacheIp(BaseJsonRequest request) {
        try {
            Map<String, String> header = request.getHeaders();
            String host = (String) header.get(c.f);
            Helper.logJson("Volley/Header", new JSONObject(header).toString());
            if (request.getJsonParams() != null) {
                Helper.logJson("Volley/Params", request.getJsonParams().toString());
            }
            if (!TextUtils.isEmpty(host) && !RegularUtils.isIP(host)) {
                DnspodFree.getIpWithHost(host);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void cancelAll(Object tag) {
        if (tag != null) {
            mRequestQueue.cancelAll(tag);
        }
    }
}
