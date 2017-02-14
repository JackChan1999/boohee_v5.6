package com.umeng.socialize.controller;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.os.Parcelable;
import android.text.TextUtils;

import com.umeng.socialize.bean.CustomPlatform;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.common.ResContainer;
import com.umeng.socialize.common.ResContainer.ResType;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMediaObject;
import com.umeng.socialize.utils.Log;
import com.umeng.socialize.utils.SocializeUtils;

import java.util.HashMap;
import java.util.Map;

/* compiled from: AppPlatformFactory */
public class a {
    private static Map<SHARE_MEDIA, String> a = new HashMap();

    static {
        a.put(SHARE_MEDIA.FACEBOOK, "com.facebook.katana");
        a.put(SHARE_MEDIA.TWITTER, "com.twitter.android");
        a.put(SHARE_MEDIA.GOOGLEPLUS, "com.google.android.apps.plus");
    }

    public static CustomPlatform a(Context context, SHARE_MEDIA share_media, String str) {
        if (!a.keySet().contains(share_media)) {
            return null;
        }
        if (TextUtils.isEmpty(str)) {
            str = share_media.toString();
        }
        return b(context, share_media, str);
    }

    private static CustomPlatform b(Context context, SHARE_MEDIA share_media, String str) {
        String str2 = "umeng_socialize_google";
        if (share_media == SHARE_MEDIA.FACEBOOK) {
            str2 = "umeng_socialize_facebook";
        } else if (share_media == SHARE_MEDIA.TWITTER) {
            str2 = "umeng_socialize_twitter";
        }
        int resourceId = ResContainer.getResourceId(context, ResType.DRAWABLE, str2);
        CustomPlatform customPlatform = new CustomPlatform(share_media.toString(), resourceId);
        customPlatform.mGrayIcon = resourceId;
        customPlatform.mClickListener = new b(share_media, str);
        return customPlatform;
    }

    private static Intent a(Context context, SHARE_MEDIA share_media, Intent intent) {
        intent.setFlags(270532608);
        try {
            String str = (String) a.get(share_media);
            for (ResolveInfo resolveInfo : context.getPackageManager().queryIntentActivities
                    (intent, 0)) {
                if (str.equals(resolveInfo.activityInfo.packageName)) {
                    break;
                }
            }
            ResolveInfo resolveInfo2 = null;
            if (resolveInfo2 != null) {
                intent.setClassName(resolveInfo2.activityInfo.packageName, "com.google.android" +
                        ".apps.plus.phone.HomeActivity");
                return intent;
            }
        } catch (Exception e) {
            Log.w("com.umeng.socialize", "", e);
        }
        return null;
    }

    private static Intent b(Context context, String str, UMediaObject uMediaObject) {
        Intent intent = new Intent("android.intent.action.SEND");
        intent.setType("image/*;text/plain");
        if (!TextUtils.isEmpty(str)) {
            intent.putExtra("android.intent.extra.TEXT", str);
            intent.putExtra("android.intent.extra.SUBJECT", str);
        }
        if (uMediaObject instanceof UMImage) {
            String imageCachePath = ((UMImage) uMediaObject).getImageCachePath();
            if (imageCachePath != null) {
                Parcelable insertImage = SocializeUtils.insertImage(context, imageCachePath);
                if (insertImage != null) {
                    intent.putExtra("android.intent.extra.STREAM", insertImage);
                    SocializeUtils.deleteUris.add(insertImage);
                }
            }
        }
        return intent;
    }
}
