package com.boohee.one.http.client;

import android.content.Context;

import com.alipay.sdk.sys.a;
import com.boohee.one.http.JsonCallback;
import com.boohee.one.http.JsonParams;
import com.boohee.one.http.RequestManager;
import com.boohee.utility.TimeLinePatterns;
import com.boohee.utils.BlackTech;

public class BooheeClient {
    public static final  String API       = "api";
    private static final String BASE_PRO  = ".boohee.com";
    private static final String BASE_QA   = ".iboohee.cn";
    private static final String BASE_RC   = "-rc.iboohee.cn";
    public static final  String BH_ALL    = "bh_all";
    public static final  String BINGO     = "bingo";
    public static final  String FOOD      = "food";
    public static final  String IFOOD     = "ifood";
    public static final  String MESSENGER = "messenger";
    public static final  String NICE      = "nice";
    public static final  String ONE       = "one";
    public static final  String PASSPORT  = "passport";
    public static final  String RECORD    = "record";
    public static final  String STATUS    = "status";
    private String mClient;

    public @interface Client {
    }

    public BooheeClient(@Client String client) {
        this.mClient = client;
    }

    public static BooheeClient build(@Client String client) {
        return new BooheeClient(client);
    }

    public void get(String url, JsonCallback callback, Context context) {
        get(url, null, callback, context);
    }

    public void get(String url, JsonParams jsonParams, JsonCallback callback, Context context) {
        sendRequest(0, url, jsonParams, callback, context);
    }

    public void post(String url, JsonParams jsonParams, JsonCallback callback, Context context) {
        sendRequest(1, url, jsonParams, callback, context);
    }

    public void put(String url, JsonParams jsonParams, JsonCallback callback, Context context) {
        sendRequest(2, url, jsonParams, callback, context);
    }

    public void delete(String url, JsonParams jsonParams, JsonCallback callback, Context context) {
        sendRequest(3, url, jsonParams, callback, context);
    }

    public static String getHost(@Client String client) {
        String apiEnv = BlackTech.getApiEnvironment();
        if (BlackTech.API_ENV_QA.equals(apiEnv)) {
            return client + BASE_QA;
        }
        if (BlackTech.API_ENV_RC.equals(apiEnv)) {
            return client + BASE_RC;
        }
        return client + BASE_PRO;
    }

    public String getDefaultURL(String apiURL) {
        return TimeLinePatterns.WEB_SCHEME + getHost(this.mClient) + apiURL;
    }

    private void sendRequest(int method, String url, JsonParams jsonParams, JsonCallback
            callback, Context context) {
        String fullURL = getDefaultURL(url);
        JsonParams fullJsonParams = BaseJsonRequest.addDefaultParams(fullURL, jsonParams);
        if (method == 0) {
            fullURL = getUrlWithQueryString(fullURL, fullJsonParams);
        }
        RequestManager.addRequest(new BaseJsonRequest(method, fullURL, fullJsonParams, callback,
                callback), context);
    }

    public static String getUrlWithQueryString(String url, JsonParams params) {
        if (params == null) {
            return url;
        }
        String paramString = params.getEncodedParamString();
        if (url.indexOf("?") == -1) {
            return url + "?" + paramString;
        }
        return url + a.b + paramString;
    }

    public static void defaultSendRequest(int method, String url, JsonParams jsonParams,
                                          JsonCallback callback, Context context) {
        RequestManager.addRequest(new BaseJsonRequest(method, url, jsonParams, callback, callback), context);
    }
}
