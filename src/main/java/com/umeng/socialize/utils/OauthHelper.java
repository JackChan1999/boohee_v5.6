package com.umeng.socialize.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.text.TextUtils;

import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.common.SocialSNSHelper;
import com.umeng.socialize.common.SocializeConstants;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

public class OauthHelper {
    public static final  String APP_ID  = "appid";
    public static final  String APP_KEY = "appkey";
    private static final String QQ_KEY  = "qq_sso";
    private static final String TAG     = OauthHelper.class.getName();

    public static boolean isAuthenticated(Context context, SHARE_MEDIA share_media) {
        if (context == null) {
            Log.w(TAG, "context对象为空，请传递一个非空Context对象");
            return false;
        } else if (share_media == null || share_media == SHARE_MEDIA.GENERIC) {
            Log.w(TAG, "传递的检测授权平台无效");
            return false;
        } else if (TextUtils.isEmpty(getUsid(context, share_media))) {
            return false;
        } else {
            return true;
        }
    }

    public static boolean isAuthenticatedAndTokenNotExpired(Context context, SHARE_MEDIA
            share_media) {
        if (!isAuthenticated(context, share_media)) {
            return false;
        }
        if (System.currentTimeMillis() / 1000 < getTokenExpiresIn(context, share_media)) {
            return true;
        }
        return false;
    }

    public static Map<SHARE_MEDIA, String> getAuthenticatedPlatform(Context context) {
        Map<SHARE_MEDIA, String> hashMap = new HashMap();
        try {
            for (SHARE_MEDIA share_media : SHARE_MEDIA.getDefaultPlatform()) {
                if (isAuthenticated(context, share_media)) {
                    String usid = getUsid(context, share_media);
                    hashMap.put(share_media, usid);
                    Log.i(TAG, "found platform " + share_media.toString() + " usid=" + usid);
                } else {
                    Log.i(TAG, "No found oauthed platform " + share_media.toString());
                }
            }
            Log.i(TAG, "found platform count " + hashMap.size());
            return hashMap;
        } catch (NullPointerException e) {
            Log.i(TAG, "no authenticated platform.......");
            return null;
        }
    }

    public static void saveTokenExpiresIn(Context context, SHARE_MEDIA share_media, String str) {
        if (context == null) {
            Log.w(TAG, "context is null when save expires in");
            return;
        }
        String keywordByPlatform = SocialSNSHelper.getKeywordByPlatform(share_media);
        if (TextUtils.isEmpty(keywordByPlatform)) {
            Log.w(TAG, "platform is null when save expires in ");
        } else if (!TextUtils.isEmpty(str)) {
            long longValue;
            try {
                longValue = Long.valueOf(str).longValue();
            } catch (Exception e) {
                longValue = 0;
            }
            if (longValue <= 0) {
                Log.w(TAG, keywordByPlatform + " expires in is " + longValue);
                return;
            }
            longValue += System.currentTimeMillis() / 1000;
            Editor edit = context.getSharedPreferences(SocializeConstants
                    .SOCIAL_PREFERENCE_EXPIRE_IN, 0).edit();
            edit.putLong(keywordByPlatform, longValue);
            edit.commit();
        }
    }

    public static long getTokenExpiresIn(Context context, SHARE_MEDIA share_media) {
        if (context == null) {
            Log.w(TAG, "context is null when obtain expires in");
            return 0;
        }
        Object keywordByPlatform = SocialSNSHelper.getKeywordByPlatform(share_media);
        if (!TextUtils.isEmpty(keywordByPlatform)) {
            return context.getSharedPreferences(SocializeConstants.SOCIAL_PREFERENCE_EXPIRE_IN,
                    0).getLong(keywordByPlatform, 0);
        }
        Log.w(TAG, "platform is null when save expires in ");
        return 0;
    }

    public static void removeTokenExpiresIn(Context context, SHARE_MEDIA share_media) {
        if (context == null) {
            Log.w(TAG, "context is null when removing expires in");
            return;
        }
        Object keywordByPlatform = SocialSNSHelper.getKeywordByPlatform(share_media);
        if (TextUtils.isEmpty(keywordByPlatform)) {
            Log.w(TAG, "platform is null when save expires in ");
            return;
        }
        Editor edit = context.getSharedPreferences(SocializeConstants
                .SOCIAL_PREFERENCE_EXPIRE_IN, 0).edit();
        edit.remove(keywordByPlatform);
        edit.commit();
    }

    public static String getUsid(Context context, SHARE_MEDIA share_media) {
        if (share_media == null) {
            return "";
        }
        SharedPreferences sharedPreferences = context.getSharedPreferences(SocializeConstants
                .SOCIAL_PREFERENCE_NAME, 0);
        Object keywordByPlatform = SocialSNSHelper.getKeywordByPlatform(share_media);
        String str = "";
        if (TextUtils.isEmpty(keywordByPlatform)) {
            return str;
        }
        return sharedPreferences.getString(keywordByPlatform, "");
    }

    public static void setUsid(Context context, SHARE_MEDIA share_media, String str) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SocializeConstants
                .SOCIAL_PREFERENCE_NAME, 0);
        synchronized (sharedPreferences) {
            Editor edit = sharedPreferences.edit();
            String keywordByPlatform = SocialSNSHelper.getKeywordByPlatform(share_media);
            if (!TextUtils.isEmpty(keywordByPlatform)) {
                edit.putString(keywordByPlatform, str);
                edit.commit();
                Log.d(TAG, "Save platform " + keywordByPlatform + "   " + str);
            }
        }
    }

    public static void saveAccessToken(Context context, SHARE_MEDIA share_media, String str,
                                       String str2) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SocializeConstants
                .SOCIAL_PREFERENCE_NAME, 0);
        synchronized (sharedPreferences) {
            Editor edit = sharedPreferences.edit();
            String keywordByPlatform = SocialSNSHelper.getKeywordByPlatform(share_media);
            if (!TextUtils.isEmpty(keywordByPlatform)) {
                edit.putString(keywordByPlatform + "_ak", str);
                edit.putString(keywordByPlatform + "_as", str2);
                edit.commit();
            }
        }
    }

    public static String[] getAccessToken(Context context, SHARE_MEDIA share_media) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SocializeConstants
                .SOCIAL_PREFERENCE_NAME, 0);
        String keywordByPlatform = SocialSNSHelper.getKeywordByPlatform(share_media);
        String str = keywordByPlatform + "_ak";
        keywordByPlatform = keywordByPlatform + "_as";
        if (!sharedPreferences.contains(str) || !sharedPreferences.contains(keywordByPlatform)) {
            return new String[0];
        }
        str = sharedPreferences.getString(str, "");
        keywordByPlatform = sharedPreferences.getString(keywordByPlatform, "");
        return new String[]{str, keywordByPlatform};
    }

    public static void saveQQAccessToken(Context context, JSONObject jSONObject) {
        saveQQAccessToken(context, jSONObject.optString("access_token", ""), jSONObject.optString
                ("openid", ""), jSONObject.optString("expires_in", ""));
    }

    public static void saveQQAccessToken(Context context, Bundle bundle) {
        saveQQAccessToken(context, bundle.getString("access_token"), bundle.getString("openid"),
                bundle.getString("expires_in"));
    }

    private static void saveQQAccessToken(Context context, String str, String str2, String str3) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SocializeConstants
                .SOCIAL_PREFERENCE_QQ, 0);
        synchronized (sharedPreferences) {
            Editor edit = sharedPreferences.edit();
            if (!(TextUtils.isEmpty(str) && TextUtils.isEmpty(str2))) {
                edit.putString("qq_sso_token", str);
                edit.putString("qq_sso_openid", str2);
                edit.putString("qq_sso_expires_in", calExpireTime(str3));
                edit.commit();
                Log.d(TAG, "### Saved QQ Token.");
            }
        }
    }

    public static String[] getAccessTokenForQQ(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SocializeConstants
                .SOCIAL_PREFERENCE_QQ, 0);
        String str = "qq_sso_token";
        String str2 = "qq_sso_openid";
        String str3 = "qq_sso_expires_in";
        if (!sharedPreferences.contains(str) || !sharedPreferences.contains(str2)) {
            return null;
        }
        str = sharedPreferences.getString(str, "");
        str2 = sharedPreferences.getString(str2, "");
        String string = sharedPreferences.getString(str3, "");
        Log.d(TAG, "get QQ Token." + string);
        if (isQQAuthExpired(string)) {
            return null;
        }
        return new String[]{str, str2, string};
    }

    private static String calExpireTime(String str) {
        return String.valueOf((System.currentTimeMillis() / 1000) + Long.parseLong(str));
    }

    public static boolean isQQAuthExpired(String str) {
        return System.currentTimeMillis() / 1000 >= Long.parseLong(str);
    }

    public static void remove(Context context, SHARE_MEDIA share_media) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SocializeConstants
                .SOCIAL_PREFERENCE_NAME, 0);
        synchronized (sharedPreferences) {
            Editor edit = sharedPreferences.edit();
            edit.remove(share_media.toString());
            edit.remove(share_media.toString() + "_ak");
            edit.remove(share_media.toString() + "_as");
            edit.commit();
            Log.d(TAG, "Remove platform " + share_media.toString());
        }
        deleteRefreshToken(context, share_media);
    }

    public static void saveAppidAndAppkey(Context context, String str, String str2) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SocializeConstants
                .SOCIAL_PREFERENCE_QQ, 0);
        synchronized (sharedPreferences) {
            Editor edit = sharedPreferences.edit();
            edit.putString("appid", str);
            edit.putString(str2, str2);
            edit.commit();
        }
    }

    public static Map<String, String> getAppIdAndAppkey(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SocializeConstants
                .SOCIAL_PREFERENCE_QQ, 0);
        String string = sharedPreferences.getString("appid", "");
        String string2 = sharedPreferences.getString("appkey", "");
        Map<String, String> hashMap = new HashMap();
        hashMap.put("appid", string);
        hashMap.put("appkey", string2);
        return hashMap;
    }

    public static void saveRefreshToken(Context context, SHARE_MEDIA share_media, String str) {
        Editor edit = context.getSharedPreferences(SocializeConstants
                .SOCIAL_PREFERENCE_REFRESH_TOKEN, 0).edit();
        edit.putString(share_media.toString(), str);
        edit.commit();
    }

    public static void deleteRefreshToken(Context context, SHARE_MEDIA share_media) {
        Editor edit = context.getSharedPreferences(SocializeConstants
                .SOCIAL_PREFERENCE_REFRESH_TOKEN, 0).edit();
        edit.remove(share_media.toString());
        edit.commit();
        edit = context.getSharedPreferences(SocializeConstants
                .SOCIAL_PREFERENCE_REFRESH_TOKEN_EXPIRES, 0).edit();
        edit.remove(share_media.toString());
        edit.commit();
    }

    public static String getRefreshToken(Context context, SHARE_MEDIA share_media) {
        return context.getSharedPreferences(SocializeConstants.SOCIAL_PREFERENCE_REFRESH_TOKEN,
                0).getString(share_media.toString(), "");
    }

    public static void saveRefreshTokenExpires(Context context, SHARE_MEDIA share_media, long j) {
        long currentTimeMillis = (System.currentTimeMillis() / 1000) + j;
        Editor edit = context.getSharedPreferences(SocializeConstants
                .SOCIAL_PREFERENCE_REFRESH_TOKEN_EXPIRES, 0).edit();
        edit.putLong(share_media.toString(), currentTimeMillis);
        edit.commit();
    }

    public static boolean isRefreshTokenNotExpired(Context context, SHARE_MEDIA share_media) {
        if (context.getSharedPreferences(SocializeConstants
                .SOCIAL_PREFERENCE_REFRESH_TOKEN_EXPIRES, 0).getLong(share_media.toString(), 0) >
                System.currentTimeMillis() / 1000) {
            return true;
        }
        return false;
    }
}
