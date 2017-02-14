package com.boohee.utils;

import android.text.TextUtils;

import com.boohee.database.UserPreference;
import com.boohee.one.MyApplication;
import com.boohee.one.http.client.BooheeClient;
import com.boohee.utility.Config;

public class UrlUtils {
    public static String handleUrl(String url) {
        if (TextUtils.isEmpty(url)) {
            return null;
        }
        StringBuilder sb = new StringBuilder(url);
        appendBaseParams(sb);
        appendToken(sb);
        return sb.toString();
    }

    private static StringBuilder appendBaseParams(StringBuilder sb) {
        if (sb.toString().contains("?")) {
            sb.append("&app_device=Android");
        } else {
            sb.append("?app_device=Android");
        }
        sb.append("&os_version=").append(SystemUtil.getVersionCode());
        sb.append("&app_version=").append(Config.getVersionName());
        sb.append("&version_code=").append(Config.getVersionCode());
        sb.append("&channel=").append(AppUtils.getChannel(MyApplication.getContext()));
        sb.append("&app_key=").append(BooheeClient.ONE);
        return sb;
    }

    private static StringBuilder appendToken(StringBuilder sb) {
        String url = sb.toString();
        if (url.contains("boohee.com") || url.contains("boohee.cn") || url.contains("iboohee.cn")) {
            if (url.contains("?")) {
                sb.append("&token=");
            } else {
                sb.append("?token=");
            }
            sb.append(UserPreference.getToken(MyApplication.getContext()));
        }
        return sb;
    }
}
