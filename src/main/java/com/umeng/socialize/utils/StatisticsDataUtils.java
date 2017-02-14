package com.umeng.socialize.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.text.TextUtils;

import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.common.SocializeConstants;

import java.util.HashMap;
import java.util.Map;

public class StatisticsDataUtils {
    private static boolean                         isReplace           = false;
    private static Map<String, Integer>            mOauthMap           = new HashMap();
    private static Map<SHARE_MEDIA, Integer>       mPlatformsOperation = new HashMap();
    private static Map<SHARE_MEDIA, StringBuilder> mStatisticsMap      = new HashMap();
    private static Map<String, Integer>            sharkMap            = new HashMap();

    static {
        int i = 0;
        mPlatformsOperation.put(SHARE_MEDIA.QZONE, Integer.valueOf(1));
        mPlatformsOperation.put(SHARE_MEDIA.SINA, Integer.valueOf(2));
        mPlatformsOperation.put(SHARE_MEDIA.TENCENT, Integer.valueOf(3));
        mPlatformsOperation.put(SHARE_MEDIA.RENREN, Integer.valueOf(4));
        mPlatformsOperation.put(SHARE_MEDIA.DOUBAN, Integer.valueOf(5));
        mPlatformsOperation.put(SHARE_MEDIA.EMAIL, Integer.valueOf(6));
        mPlatformsOperation.put(SHARE_MEDIA.SMS, Integer.valueOf(7));
        mPlatformsOperation.put(SHARE_MEDIA.WEIXIN, Integer.valueOf(8));
        mPlatformsOperation.put(SHARE_MEDIA.WEIXIN_CIRCLE, Integer.valueOf(9));
        mPlatformsOperation.put(SHARE_MEDIA.QQ, Integer.valueOf(10));
        mPlatformsOperation.put(SHARE_MEDIA.FACEBOOK, Integer.valueOf(11));
        mPlatformsOperation.put(SHARE_MEDIA.TWITTER, Integer.valueOf(12));
        mPlatformsOperation.put(SHARE_MEDIA.GOOGLEPLUS, Integer.valueOf(13));
        mPlatformsOperation.put(SHARE_MEDIA.YIXIN, Integer.valueOf(14));
        mPlatformsOperation.put(SHARE_MEDIA.YIXIN_CIRCLE, Integer.valueOf(15));
        mPlatformsOperation.put(SHARE_MEDIA.LAIWANG, Integer.valueOf(16));
        mPlatformsOperation.put(SHARE_MEDIA.LAIWANG_DYNAMIC, Integer.valueOf(17));
        mPlatformsOperation.put(SHARE_MEDIA.INSTAGRAM, Integer.valueOf(18));
        SHARE_MEDIA[] values = SHARE_MEDIA.values();
        int length = values.length;
        while (i < length) {
            mStatisticsMap.put(values[i], new StringBuilder());
            i++;
        }
    }

    public static void addStatisticsData(Context context, SHARE_MEDIA share_media, int i) {
        if (i >= 1 && i <= 25) {
            getStatisticsData(context, share_media, false);
            if (isReplace && share_media != SHARE_MEDIA.GENERIC) {
                platformReplace(share_media, mStatisticsMap);
                isReplace = false;
            }
            if (share_media == SHARE_MEDIA.GENERIC) {
                isReplace = true;
            }
            StringBuilder stringBuilder = (StringBuilder) mStatisticsMap.get(share_media);
            String preMethodId = getPreMethodId(stringBuilder);
            if (stringBuilder.length() <= 0) {
                stringBuilder.append(String.valueOf(i)).append("(0-1)+");
            } else if (containCurrentMethod(stringBuilder, i)) {
                increaseNum(stringBuilder, i);
            } else {
                stringBuilder.append(String.valueOf(i)).append(SocializeConstants.OP_OPEN_PAREN)
                        .append(String.valueOf(preMethodId)).append(SocializeConstants
                        .OP_DIVIDER_MINUS).append("1").append(SocializeConstants.OP_CLOSE_PAREN)
                        .append(SocializeConstants.OP_DIVIDER_PLUS);
            }
            saveStatisticsData(context, false);
        }
    }

    private static boolean containCurrentMethod(StringBuilder stringBuilder, int i) {
        if (stringBuilder == null || TextUtils.isEmpty(stringBuilder.toString()) || !
                (SocializeConstants.OP_DIVIDER_PLUS + stringBuilder.toString()).contains
                        (SocializeConstants.OP_DIVIDER_PLUS + i + SocializeConstants
                                .OP_OPEN_PAREN)) {
            return false;
        }
        return true;
    }

    private static String getPreMethodId(StringBuilder stringBuilder) {
        if (stringBuilder == null || stringBuilder.length() <= 0) {
            return "0";
        }
        String stringBuilder2 = stringBuilder.toString();
        stringBuilder2 = stringBuilder2.substring(0, stringBuilder2.lastIndexOf
                (SocializeConstants.OP_OPEN_PAREN));
        return stringBuilder2.substring(stringBuilder2.lastIndexOf(SocializeConstants
                .OP_DIVIDER_PLUS) + 1, stringBuilder2.length());
    }

    private static void platformReplace(SHARE_MEDIA share_media, Map<SHARE_MEDIA, StringBuilder>
            map) {
        StringBuilder stringBuilder = (StringBuilder) map.get(share_media);
        if (TextUtils.isEmpty(stringBuilder.toString())) {
            map.put(share_media, new StringBuilder("1(0-1)+"));
        } else if (containCurrentMethod(stringBuilder, 1)) {
            increaseNum(stringBuilder, 1);
        }
        map.put(SHARE_MEDIA.GENERIC, new StringBuilder());
    }

    public static void addOauthData(Context context, SHARE_MEDIA share_media, int i) {
        getStatisticsData(context, share_media, true);
        int platformOperation = getPlatformOperation(share_media);
        String str = "";
        if (i == 1) {
            str = platformOperation + SocializeConstants.KEY_OAUTH_SUCCESS;
            mOauthMap.put(str, Integer.valueOf(((Integer) mOauthMap.get(str)).intValue() + 1));
        } else if (i == 0) {
            str = platformOperation + SocializeConstants.KEY_OAUTH_FAIL;
            mOauthMap.put(str, Integer.valueOf(((Integer) mOauthMap.get(str)).intValue() + 1));
        }
        saveStatisticsData(context, true);
    }

    public static int getPlatformOperation(SHARE_MEDIA share_media) {
        if (share_media != null) {
            try {
                if (share_media != SHARE_MEDIA.GENERIC) {
                    return ((Integer) mPlatformsOperation.get(share_media)).intValue();
                }
            } catch (Exception e) {
                return -1;
            }
        }
        return -1;
    }

    public static Map<SHARE_MEDIA, StringBuilder> getStatisticsData() {
        return mStatisticsMap;
    }

    public static void increaseNum(StringBuilder stringBuilder, int i) {
        String stringBuilder2 = stringBuilder.toString();
        int indexOf = stringBuilder2.indexOf(i + SocializeConstants.OP_OPEN_PAREN);
        stringBuilder2 = stringBuilder2.substring(indexOf);
        if (stringBuilder2.contains(SocializeConstants.OP_DIVIDER_PLUS)) {
            stringBuilder2 = stringBuilder2.substring(0, stringBuilder2.indexOf
                    (SocializeConstants.OP_DIVIDER_PLUS));
            if (stringBuilder2.contains(SocializeConstants.OP_DIVIDER_MINUS) && stringBuilder2
                    .contains(SocializeConstants.OP_CLOSE_PAREN)) {
                int indexOf2 = stringBuilder2.indexOf(SocializeConstants.OP_DIVIDER_MINUS);
                int lastIndexOf = stringBuilder2.lastIndexOf(SocializeConstants.OP_CLOSE_PAREN);
                Object substring = stringBuilder2.substring(indexOf2 + 1, lastIndexOf);
                if (!TextUtils.isEmpty(substring)) {
                    int intValue = Integer.valueOf(substring).intValue() + 1;
                    stringBuilder.delete((indexOf + indexOf2) + 1, lastIndexOf + indexOf);
                    stringBuilder.insert((indexOf + indexOf2) + 1, String.valueOf(intValue));
                }
            }
        }
    }

    public static Map<String, Integer> getOauthStatisticsData() {
        return mOauthMap;
    }

    private static void saveStatisticsData(Context context, boolean z) {
        String str = "";
        if (z) {
            str = SocializeConstants.OP_FILE_NAME_OAUTH;
        } else {
            str = SocializeConstants.OP_FILE_NAME_METHOD;
        }
        Editor edit = context.getSharedPreferences(str, 0).edit();
        if (z) {
            for (String str2 : mOauthMap.keySet()) {
                edit.putInt(str2, ((Integer) mOauthMap.get(str2)).intValue());
            }
        } else {
            for (SHARE_MEDIA share_media : mStatisticsMap.keySet()) {
                Editor editor;
                StringBuilder stringBuilder = (StringBuilder) mStatisticsMap.get(share_media);
                if (stringBuilder == null || stringBuilder.length() <= 0) {
                    editor = edit;
                } else {
                    editor = edit.putString(share_media.toString(), stringBuilder.toString());
                }
                edit = editor;
            }
        }
        edit.commit();
    }

    public static void getStatisticsData(Context context, SHARE_MEDIA share_media, boolean z) {
        String str = "";
        if (z) {
            str = SocializeConstants.OP_FILE_NAME_OAUTH;
        } else {
            str = SocializeConstants.OP_FILE_NAME_METHOD;
        }
        SharedPreferences sharedPreferences = context.getSharedPreferences(str, 0);
        if (z) {
            int platformOperation = getPlatformOperation(share_media);
            int i = sharedPreferences.getInt(platformOperation + SocializeConstants
                    .KEY_OAUTH_SUCCESS, 0);
            int i2 = sharedPreferences.getInt(platformOperation + SocializeConstants
                    .KEY_OAUTH_FAIL, 0);
            mOauthMap.put(platformOperation + SocializeConstants.KEY_OAUTH_SUCCESS, Integer
                    .valueOf(i));
            mOauthMap.put(platformOperation + SocializeConstants.KEY_OAUTH_FAIL, Integer.valueOf
                    (i2));
            return;
        }
        for (SHARE_MEDIA share_media2 : SHARE_MEDIA.values()) {
            StringBuilder stringBuilder = new StringBuilder(sharedPreferences.getString
                    (share_media2.toString(), ""));
            mStatisticsMap.remove(share_media2.toString());
            mStatisticsMap.put(share_media2, stringBuilder);
        }
    }

    public static void saveSharkStatisticsData(Context context) {
        getSharkStatisticsData(context);
        if (!sharkMap.containsKey("shake")) {
            sharkMap.put("shake", Integer.valueOf(0));
        }
        Editor edit = context.getSharedPreferences(SocializeConstants.OP_FILE_NAME_SHAKE, 0).edit();
        edit.putInt("shake", ((Integer) sharkMap.get("shake")).intValue() + 1);
        edit.commit();
    }

    public static Map<String, Integer> getSharkStatisticsData(Context context) {
        sharkMap.put("shake", Integer.valueOf(context.getSharedPreferences(SocializeConstants
                .OP_FILE_NAME_SHAKE, 0).getInt("shake", 0)));
        return sharkMap;
    }

    public static void getStatisticsData(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SocializeConstants
                .OP_FILE_NAME_OAUTH, 0);
        for (SHARE_MEDIA platformOperation : SHARE_MEDIA.values()) {
            int platformOperation2 = getPlatformOperation(platformOperation);
            if (platformOperation2 != -1) {
                int i = sharedPreferences.getInt(platformOperation2 + SocializeConstants
                        .KEY_OAUTH_SUCCESS, 0);
                int i2 = sharedPreferences.getInt(platformOperation2 + SocializeConstants
                        .KEY_OAUTH_FAIL, 0);
                mOauthMap.put(platformOperation2 + SocializeConstants.KEY_OAUTH_SUCCESS, Integer
                        .valueOf(i));
                mOauthMap.put(platformOperation2 + SocializeConstants.KEY_OAUTH_FAIL, Integer
                        .valueOf(i2));
            }
        }
    }

    public static void cleanStatisticsData(Context context, boolean z) {
        context.getSharedPreferences(SocializeConstants.OP_FILE_NAME_OAUTH, 0).edit().clear()
                .commit();
        mOauthMap.clear();
        if (z) {
            context.getSharedPreferences(SocializeConstants.OP_FILE_NAME_METHOD, 0).edit().clear
                    ().commit();
            mStatisticsMap.clear();
            context.getSharedPreferences(SocializeConstants.OP_FILE_NAME_SHAKE, 0).edit().clear()
                    .commit();
            sharkMap.clear();
        }
    }

    public static Map<String, Object> convertStatisticsData(String str, String str2, Map<String,
            Object> map) {
        if (!TextUtils.isEmpty(str2)) {
            try {
                String str3 = "";
                str3 = "";
                str3 = "";
                for (String str32 : str2.split("\\+")) {
                    Object obj;
                    String substring = str32.substring(0, str32.indexOf(SocializeConstants
                            .OP_OPEN_PAREN));
                    String substring2 = str32.substring(str32.indexOf(SocializeConstants
                            .OP_OPEN_PAREN) + 1, str32.indexOf(SocializeConstants
                            .OP_DIVIDER_MINUS));
                    String substring3 = str32.substring(str32.indexOf(SocializeConstants
                            .OP_DIVIDER_MINUS) + 1, str32.indexOf(SocializeConstants
                            .OP_CLOSE_PAREN));
                    substring2 = str + SocializeConstants.OP_DIVIDER_MINUS + substring2 +
                            SocializeConstants.OP_DIVIDER_MINUS + substring;
                    str32 = "";
                    if (map.get(substring2) != null) {
                        obj = map.get(substring2).toString();
                    } else {
                        substring = str32;
                    }
                    int parseInt = Integer.parseInt(substring3);
                    if (!TextUtils.isEmpty(obj)) {
                        parseInt += Integer.parseInt(obj);
                    }
                    map.put(substring2, String.valueOf(parseInt));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return map;
    }
}
