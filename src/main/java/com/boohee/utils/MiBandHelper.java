package com.boohee.utils;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;

import com.alipay.sdk.sys.a;
import com.android.volley.Response.ErrorListener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.boohee.model.XiaoMiSportData;
import com.boohee.model.XiaoMiSportData.SportData;
import com.boohee.one.http.OkListener;
import com.boohee.one.http.RequestManager;
import com.boohee.push.XMPush;
import com.boohee.utility.SportDataBindHelper;
import com.xiaomi.account.openauth.XiaomiOAuthorize;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

public class MiBandHelper {
    public static final String KEY_DATA_SOURCE  = "xiaomi";
    public static final String KEY_MI_AUTH_DATE = "KEY_MI_AUTH_DATE";
    public static final String KEY_MI_BAND      = "KEY_MI_BAND";
    public static final String KEY_MI_BAND_AUTH = "KEY_MI_BAND_AUTH";
    public static final long   MI_APPID         = 2882303761517135649L;
    public static final String MI_REDIRECT_URI  = "http://www.boohee.com";
    public static final String THIRD_APP_ID     = "2833961550";
    public static final String THIRD_APP_SECRET = "d9b9bc7b31f4ff7f41bee21a9e95859b";
    public static final String XMDataApi        = "https://hm.xiaomi.com/huami.api" +
            ".getUserSummaryData.json?";
    private static long lastUpdateTime;

    public interface MiBandCallBack {
        void onFailed(String str);

        void onSuccess(int i);
    }

    public static void startAuthenticate(Activity activity, Bundle params, int requestCode) {
        XiaomiOAuthorize.startGetAccessToken(activity, MI_APPID, "http://www.boohee.com", params,
                requestCode);
    }

    public static void setXMToken(String xmToken) {
        SportDataBindHelper.putString(KEY_MI_BAND_AUTH, xmToken);
        SportDataBindHelper.putString(KEY_MI_AUTH_DATE, TimeUtils.getNow());
    }

    public static String getXMToken() {
        return SportDataBindHelper.getString(KEY_MI_BAND_AUTH);
    }

    public static boolean shouldAuth() {
        String xiaomiToken = SportDataBindHelper.getString(KEY_MI_BAND_AUTH);
        String authDateStr = SportDataBindHelper.getString(KEY_MI_AUTH_DATE);
        if (TextUtils.isEmpty(xiaomiToken) || TextUtils.isEmpty(authDateStr)) {
            return true;
        }
        Date authDate = DateHelper.parseString(authDateStr);
        if (authDate == null || DateHelper.between(authDate) > 90) {
            return true;
        }
        return false;
    }

    public static void loadMIBndData(final Context context, final MiBandCallBack callBack) {
        String currentDate = DateHelper.today();
        Map<String, String> params = new HashMap();
        params.put("appid", XMPush.APP_ID);
        params.put("third_appid", THIRD_APP_ID);
        params.put("third_appsecret", THIRD_APP_SECRET);
        params.put("call_id", System.currentTimeMillis() + "");
        params.put("access_token", getXMToken());
        params.put("fromdate", currentDate);
        params.put("todate", currentDate);
        params.put("v", "1.0");
        params.put("l", "english");
        RequestManager.addRequest(new JsonObjectRequest(1, buildUrl(XMDataApi, params), new
                OkListener((Activity) context) {
            public void ok(JSONObject object) {
                XiaoMiSportData sportData = (XiaoMiSportData) FastJsonUtils.fromJson(object,
                        XiaoMiSportData.class);
                if (context != null && sportData != null) {
                    if (!XiaoMiSportData.KEY_SUCCESS.equals(sportData.message)) {
                        MiBandHelper.lastUpdateTime = System.currentTimeMillis() - 180000;
                        callBack.onFailed("获取小米手环数据失败，请绑定小米手环并同步数据~~");
                    } else if (sportData.data == null || sportData.data.size() == 0) {
                        callBack.onFailed("今日暂无数据，先同步手环数据~~");
                    } else {
                        callBack.onSuccess(((SportData) sportData.data.get(0)).step);
                    }
                }
            }
        }, new ErrorListener() {
            public void onErrorResponse(VolleyError error) {
                MiBandHelper.lastUpdateTime = System.currentTimeMillis() - 180000;
                callBack.onFailed("获取小米手环数据失败，请重试~~");
            }
        }), context);
    }

    public static String buildUrl(String url, Map<String, String> params) {
        StringBuilder urlStr = new StringBuilder(url);
        if (url.indexOf(63) < 0) {
            urlStr.append('?');
        }
        try {
            for (String name : params.keySet()) {
                urlStr.append(a.b);
                urlStr.append(name);
                urlStr.append("=");
                urlStr.append(URLEncoder.encode(String.valueOf(params.get(name)), "UTF-8"));
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return urlStr.toString().replace("?&", "?");
    }
}
