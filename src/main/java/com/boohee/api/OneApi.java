package com.boohee.api;

import android.content.Context;
import android.text.TextUtils;

import com.boohee.one.http.JsonCallback;
import com.boohee.one.http.JsonParams;
import com.boohee.one.http.client.BooheeClient;
import com.boohee.utility.Config;

public class OneApi {
    public static final String URL_ADVERTISEMENT          = "/api/v1/advertisements";
    public static final String URL_ANDROID_RECOMMENDS     = "/api/v1/android_recommends";
    public static final String URL_PATCH                  = "/api/v1/patch";
    public static final String URL_TABBAR_SETTINGS        = "/api/v1/tabbar_settings";
    public static final String URL_USER_BEHAVIOR_APPRAISE =
            "/api/v1/user_behavior/inc_envious_after_appraise";
    public static final String URL_USER_BEHAVIOR_SHARE    =
            "/api/v1/user_behavior/inc_envious_after_share";

    public static void getAndroidRecommends(Context context, JsonCallback callback) {
        JsonParams jsonParams = new JsonParams();
        jsonParams.put("version", Config.getVersionName());
        jsonParams.put("channel", Config.getChannel(context));
        BooheeClient.build(BooheeClient.ONE).get(URL_ANDROID_RECOMMENDS, jsonParams, callback,
                context);
    }

    public static void getAdvertisement(Context context, String position, JsonCallback callback) {
        JsonParams jsonParams = new JsonParams();
        jsonParams.put("positions[]", position);
        BooheeClient.build(BooheeClient.ONE).get("/api/v1/advertisements", jsonParams, callback,
                context);
    }

    public static void getTabbarSettings(Context context, JsonCallback callback) {
        BooheeClient.build(BooheeClient.ONE).get(URL_TABBAR_SETTINGS, callback, context);
    }

    public static void getUserBehaviorAppraise(Context context, JsonCallback callback) {
        BooheeClient.build(BooheeClient.ONE).get(URL_USER_BEHAVIOR_APPRAISE, callback, context);
    }

    public static void putUserBehaviorShare(Context context, JsonCallback callback) {
        BooheeClient.build(BooheeClient.ONE).put(URL_USER_BEHAVIOR_SHARE, null, callback, context);
    }

    public static void getPatch(Context context, JsonCallback callback) {
        if (!TextUtils.isEmpty(Config.getVersionName())) {
            BooheeClient.build(BooheeClient.ONE).get(URL_PATCH, null, callback, context);
        }
    }
}
