package com.umeng.socialize.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ApplicationInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore.Images.Media;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;
import android.widget.Toast;

import com.alipay.sdk.sys.a;
import com.umeng.socialize.bean.RequestType;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.SNSPair;
import com.umeng.socialize.bean.SnsPlatform;
import com.umeng.socialize.bean.SocializeConfig;
import com.umeng.socialize.bean.StatusCode;
import com.umeng.socialize.bean.UMShareMsg;
import com.umeng.socialize.common.ResContainer;
import com.umeng.socialize.common.ResContainer.ResType;
import com.umeng.socialize.common.SocializeConstants;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.media.BaseMediaObject;
import com.umeng.socialize.media.BaseShareContent;
import com.umeng.socialize.media.UMediaObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

public class SocializeUtils {
    protected static final String   TAG        = SocializeUtils.class.getName();
    public static          Set<Uri> deleteUris = new HashSet();
    private static String horStr;
    private static Pattern mDoubleByte_Pattern = null;
    private static String minStr;
    private static String secStr;
    private static int smDip = 0;

    public static String getAppkey(Context context) {
        String str = SocializeConstants.APPKEY;
        if (!TextUtils.isEmpty(str)) {
            return str;
        }
        try {
            ApplicationInfo applicationInfo = context.getPackageManager().getApplicationInfo
                    (context.getPackageName(), 128);
            if (applicationInfo == null) {
                return str;
            }
            Object obj = applicationInfo.metaData.get("UMENG_APPKEY");
            if (obj != null) {
                return obj.toString();
            }
            Log.i("com.umeng.socialize", "Could not read UMENG_APPKEY meta-data from " +
                    "AndroidManifest.xml.");
            return str;
        } catch (Exception e) {
            Log.i("com.umeng.socialize", "Could not read UMENG_APPKEY meta-data from " +
                    "AndroidManifest.xml.", e);
            return str;
        }
    }

    public static void safeCloseDialog(Dialog dialog) {
        if (dialog != null) {
            try {
                if (dialog.isShowing()) {
                    Activity ownerActivity = dialog.getOwnerActivity();
                    if (ownerActivity == null || !ownerActivity.isFinishing()) {
                        dialog.dismiss();
                    }
                }
            } catch (Exception e) {
                Log.e(TAG, "dialog dismiss error", e);
            }
        }
    }

    public static void safeShowDialog(Dialog dialog) {
        if (dialog != null) {
            try {
                if (!dialog.isShowing()) {
                    Activity ownerActivity = dialog.getOwnerActivity();
                    if (ownerActivity == null || !ownerActivity.isFinishing()) {
                        dialog.show();
                    }
                }
            } catch (Exception e) {
                Log.e(TAG, "dialog show error", e);
            }
        }
    }

    public static boolean isValidPlatform(SHARE_MEDIA share_media) {
        return (share_media == null || share_media == SHARE_MEDIA.GENERIC) ? false : true;
    }

    public static boolean isValidPlatform(String str, List<SnsPlatform> list) {
        SHARE_MEDIA convertToEmun = SHARE_MEDIA.convertToEmun(str);
        if (convertToEmun == null) {
            for (SnsPlatform snsPlatform : list) {
                if (snsPlatform.mKeyword.equals(str)) {
                    return true;
                }
            }
        }
        return isValidPlatform(convertToEmun);
    }

    public static String beforeData(Context context, long j) {
        long currentTimeMillis = (System.currentTimeMillis() - j) / 1000;
        if (currentTimeMillis < 0) {
            currentTimeMillis = 0;
        }
        if (TextUtils.isEmpty(secStr) || TextUtils.isEmpty(minStr) || TextUtils.isEmpty(horStr)) {
            secStr = context.getResources().getString(ResContainer.getResourceId(context, ResType
                    .STRING, "umeng_socialize_msg_sec"));
            minStr = context.getResources().getString(ResContainer.getResourceId(context, ResType
                    .STRING, "umeng_socialize_msg_min"));
            horStr = context.getResources().getString(ResContainer.getResourceId(context, ResType
                    .STRING, "umeng_socialize_msg_hor"));
        }
        r0 = currentTimeMillis / 60 == 0 ? currentTimeMillis + secStr : currentTimeMillis / 3600
                == 0 ? (currentTimeMillis / 60) + minStr : currentTimeMillis / 86400 == 0 ?
                (currentTimeMillis / 3600) + horStr : null;
        if (r0 == null) {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm").format(Long.valueOf(j));
        }
        return r0;
    }

    public static Bundle parseUrl(String str) {
        try {
            URL url = new URL(str);
            Bundle decodeUrl = decodeUrl(url.getQuery());
            decodeUrl.putAll(decodeUrl(url.getRef()));
            return decodeUrl;
        } catch (MalformedURLException e) {
            return new Bundle();
        }
    }

    public static Bundle decodeUrl(String str) {
        Bundle bundle = new Bundle();
        if (str != null) {
            for (String split : str.split(a.b)) {
                String[] split2 = split.split("=");
                bundle.putString(URLDecoder.decode(split2[0]), URLDecoder.decode(split2[1]));
            }
        }
        return bundle;
    }

    public static int countContentLength(String str) {
        Object trim = str.trim();
        int i = 0;
        while (getDoubleBytePattern().matcher(trim).find()) {
            i++;
        }
        int length = trim.length() - i;
        if (length % 2 != 0) {
            return i + ((length + 1) / 2);
        }
        return i + (length / 2);
    }

    private static Pattern getDoubleBytePattern() {
        if (mDoubleByte_Pattern == null) {
            mDoubleByte_Pattern = Pattern.compile("[^\\x00-\\xff]");
        }
        return mDoubleByte_Pattern;
    }

    public static Object[] readSIMCard(Context context) {
        try {
            Object[] objArr = new Object[3];
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService
                    ("phone");
            objArr[0] = Boolean.valueOf(false);
            switch (telephonyManager.getSimState()) {
                case 0:
                    objArr[0] = Boolean.valueOf(true);
                    objArr[1] = "未知状态";
                    break;
                case 1:
                    objArr[1] = "无卡";
                    break;
                case 2:
                    objArr[1] = "需要PIN解锁";
                    break;
                case 3:
                    objArr[1] = "需要PUK解锁";
                    break;
                case 4:
                    objArr[1] = "需要NetworkPIN解锁";
                    break;
                case 5:
                    objArr[0] = Boolean.valueOf(true);
                    objArr[1] = "良好";
                    break;
            }
            return objArr;
        } catch (Exception e) {
            Log.e(TAG, "cannot read SIM card. [" + e.toString() + "]");
            return null;
        }
    }

    public static boolean isGoogleMapExist() {
        try {
            Class.forName("com.google.android.maps.MapActivity");
            return true;
        } catch (Exception e) {
            Log.w(TAG, "The device has no google map lib!");
            return false;
        }
    }

    public static int[] getFloatWindowSize(Context context) {
        return new int[]{(int) context.getResources().getDimension(ResContainer.getResourceId
                (context, ResType.DIMEN, "umeng_socialize_pad_window_width")), (int) context
                .getResources().getDimension(ResContainer.getResourceId(context, ResType.DIMEN,
                        "umeng_socialize_pad_window_height"))};
    }

    public static boolean isFloatWindowStyle(Context context) {
        if (SocializeConstants.SUPPORT_PAD) {
            if (smDip == 0) {
                WindowManager windowManager = (WindowManager) context.getSystemService("window");
                Display defaultDisplay = windowManager.getDefaultDisplay();
                int width = defaultDisplay.getWidth();
                int height = defaultDisplay.getHeight();
                if (width <= height) {
                    height = width;
                }
                DisplayMetrics displayMetrics = new DisplayMetrics();
                windowManager.getDefaultDisplay().getMetrics(displayMetrics);
                smDip = (int) ((((float) height) / displayMetrics.density) + 0.5f);
            }
            if ((context.getResources().getConfiguration().screenLayout & 15) >= 3 && smDip >=
                    550) {
                return true;
            }
        }
        return false;
    }

    public static Uri insertImage(Context context, String str) {
        Uri uri = null;
        if (!TextUtils.isEmpty(str) && new File(str).exists()) {
            try {
                Object insertImage = Media.insertImage(context.getContentResolver(), str,
                        "umeng_social_shareimg", null);
                if (!TextUtils.isEmpty(insertImage)) {
                    uri = Uri.parse(insertImage);
                }
            } catch (Exception e) {
                Log.e("com.umeng.socialize", "", e);
            } catch (Exception e2) {
                Log.e("com.umeng.socialize", "", e2);
            }
        }
        return uri;
    }

    public static void deleteUriImage(Context context, Set<Uri> set) {
        Set<String> set2 = (Set) getObject(BitmapUtils.PATH + SocializeConstants.FILE_URI_NAME);
        if (set2 != null && set2.size() > 0) {
            for (String parse : set2) {
                set.add(Uri.parse(parse));
            }
        }
        if (set != null && set.size() > 0) {
            for (Uri delete : set) {
                context.getContentResolver().delete(delete, null, null);
            }
            set.clear();
        } else if (set == null) {
            HashSet hashSet = new HashSet();
        }
    }

    public static void sendAnalytic(Context context, String str, String str2, UMediaObject
            uMediaObject, String str3) {
        UMSocialService uMSocialService = UMServiceFactory.getUMSocialService(str, RequestType
                .ANALYTICS);
        UMShareMsg uMShareMsg = new UMShareMsg();
        Log.i(TAG, "send analytic report , the entity name is " + uMSocialService.getEntity()
                .mDescriptor);
        if (uMediaObject instanceof BaseMediaObject) {
            uMShareMsg.setMediaData(uMediaObject);
        } else if (uMediaObject instanceof BaseShareContent) {
            uMShareMsg.setMediaData(((BaseShareContent) uMediaObject).getShareMedia());
        }
        uMSocialService.getEntity().setFireCallback(false);
        uMShareMsg.mText = str2;
        uMSocialService.postShareByCustomPlatform(context, null, str3, uMShareMsg, null);
    }

    public static void saveObject(Object obj, String str) {
        try {
            File file = new File(str);
            if (file.exists()) {
                file.delete();
            }
            file.createNewFile();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream
                    (file));
            objectOutputStream.writeObject(obj);
            objectOutputStream.flush();
            objectOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static <T> T getObject(String str) {
        Exception e;
        Throwable th;
        ObjectInputStream objectInputStream = null;
        ObjectInputStream objectInputStream2;
        try {
            File file = new File(str);
            if (file.exists()) {
                objectInputStream2 = new ObjectInputStream(new FileInputStream(file));
                try {
                    T readObject = objectInputStream2.readObject();
                    if (readObject != null) {
                        if (objectInputStream2 != null) {
                            try {
                                objectInputStream2.close();
                            } catch (IOException e2) {
                                e2.printStackTrace();
                            }
                        }
                        return readObject;
                    } else if (objectInputStream2 == null) {
                        return null;
                    } else {
                        try {
                            objectInputStream2.close();
                            return null;
                        } catch (IOException e3) {
                            e3.printStackTrace();
                            return null;
                        }
                    }
                } catch (Exception e4) {
                    e = e4;
                    try {
                        e.printStackTrace();
                        if (objectInputStream2 != null) {
                            return null;
                        }
                        try {
                            objectInputStream2.close();
                            return null;
                        } catch (IOException e32) {
                            e32.printStackTrace();
                            return null;
                        }
                    } catch (Throwable th2) {
                        th = th2;
                        if (objectInputStream2 != null) {
                            try {
                                objectInputStream2.close();
                            } catch (IOException e322) {
                                e322.printStackTrace();
                            }
                        }
                        throw th;
                    }
                }
            } else if (null == null) {
                return null;
            } else {
                try {
                    objectInputStream.close();
                    return null;
                } catch (IOException e3222) {
                    e3222.printStackTrace();
                    return null;
                }
            }
        } catch (Exception e5) {
            e = e5;
            objectInputStream2 = null;
            e.printStackTrace();
            if (objectInputStream2 != null) {
                return null;
            }
            objectInputStream2.close();
            return null;
        } catch (Throwable th3) {
            objectInputStream2 = null;
            th = th3;
            if (objectInputStream2 != null) {
                objectInputStream2.close();
            }
            throw th;
        }
    }

    public static Map<String, Object> getPlatformKey(Context context) {
        int i = 0;
        if (context == null) {
            return null;
        }
        SharedPreferences sharedPreferences = context.getSharedPreferences(SocializeConstants
                .FILE_KEY, 0);
        Map<String, Object> hashMap = new HashMap();
        SHARE_MEDIA[] values = SHARE_MEDIA.values();
        int length = values.length;
        while (i < length) {
            String share_media = values[i].toString();
            if (sharedPreferences.contains(share_media)) {
                hashMap.put(share_media, sharedPreferences.getString(share_media, ""));
            }
            i++;
        }
        return hashMap;
    }

    public static void savePlatformKey(Context context, Map<String, Object> map) {
        if (context != null && map != null && map.size() != 0) {
            Editor edit = context.getSharedPreferences(SocializeConstants.FILE_KEY, 0).edit();
            for (String str : map.keySet()) {
                if (map.get(str) != null) {
                    edit.putString(str, map.get(str).toString());
                }
            }
            edit.commit();
        }
    }

    public static Map<String, String> getPlatformSecret(Context context) {
        int i = 0;
        if (context == null) {
            return null;
        }
        SharedPreferences sharedPreferences = context.getSharedPreferences(SocializeConstants
                .FILE_SECRET, 0);
        Map<String, String> hashMap = new HashMap();
        SHARE_MEDIA[] values = SHARE_MEDIA.values();
        int length = values.length;
        while (i < length) {
            String share_media = values[i].toString();
            if (sharedPreferences.contains(share_media)) {
                hashMap.put(share_media, sharedPreferences.getString(share_media, ""));
            }
            i++;
        }
        return hashMap;
    }

    public static void savePlatformSecret(Context context, Map<String, String> map) {
        if (context != null && map != null && map.size() != 0) {
            Editor edit = context.getSharedPreferences(SocializeConstants.FILE_SECRET, 0).edit();
            for (String str : map.keySet()) {
                if (map.get(str) != null) {
                    edit.putString(str, ((String) map.get(str)).toString());
                }
            }
            edit.commit();
        }
    }

    public static SNSPair[] getOauthedPlatforms(Context context, Map<SHARE_MEDIA, Integer> map,
                                                SHARE_MEDIA... share_mediaArr) {
        List arrayList = new ArrayList();
        if (share_mediaArr != null) {
            for (SHARE_MEDIA share_media : share_mediaArr) {
                if (OauthHelper.isAuthenticatedAndTokenNotExpired(context, share_media)) {
                    arrayList.add(new SNSPair(share_media.toString(), OauthHelper.getUsid
                            (context, share_media)));
                } else if (map != null) {
                    map.put(share_media, Integer.valueOf(StatusCode.ST_CODE_SDK_NO_OAUTH));
                }
            }
        }
        return (SNSPair[]) arrayList.toArray(new SNSPair[arrayList.size()]);
    }

    public static boolean platformCheck(Context context, SHARE_MEDIA share_media) {
        if (!isValidPlatform(share_media) || context == null) {
            return false;
        }
        if (SocializeConfig.getSocializeConfig().isConfigedInSDK(share_media)) {
            return true;
        }
        if (context == null) {
            return false;
        }
        Toast.makeText(context, share_media + "没有在SDK中配置", 0).show();
        return false;
    }

    public static void errorHanding(Context context, SHARE_MEDIA share_media, Integer num) {
        if (num.intValue() == StatusCode.ST_CODE_ACCESS_EXPIRED || num.intValue() == StatusCode
                .ST_CODE_ACCESS_EXPIRED2 || num.intValue() == StatusCode.ST_CODE_NO_AUTH || num
                .intValue() == StatusCode.ST_CODE_RESERVE_CODE) {
            OauthHelper.remove(context, share_media);
            OauthHelper.removeTokenExpiresIn(context, share_media);
        }
    }

    public static int dip2Px(Context context, float f) {
        return (int) ((context.getResources().getDisplayMetrics().density * f) + 0.5f);
    }

    public static String reverse(String str) {
        if (TextUtils.isEmpty(str)) {
            return "";
        }
        StringBuilder stringBuilder = new StringBuilder();
        char[] toCharArray = str.toCharArray();
        for (int length = toCharArray.length - 1; length >= 0; length--) {
            stringBuilder.append(toCharArray[length]);
        }
        return stringBuilder.toString();
    }
}
