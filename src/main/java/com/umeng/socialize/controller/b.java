package com.umeng.socialize.controller;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.SocializeConfig;
import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.bean.StatusCode;
import com.umeng.socialize.controller.listener.SocializeListeners.OnSnsPlatformClickListener;
import com.umeng.socialize.controller.listener.SocializeListeners.SnsPostListener;
import com.umeng.socialize.media.BaseShareContent;
import com.umeng.socialize.media.UMediaObject;
import com.umeng.socialize.utils.DeviceConfig;
import com.umeng.socialize.utils.SocializeUtils;

/* compiled from: AppPlatformFactory */
final class b implements OnSnsPlatformClickListener {
    final /* synthetic */ SHARE_MEDIA a;
    final /* synthetic */ String      b;

    b(SHARE_MEDIA share_media, String str) {
        this.a = share_media;
        this.b = str;
    }

    public void onClick(Context context, SocializeEntity socializeEntity, SnsPostListener
            snsPostListener) {
        String shareContent;
        UMediaObject media;
        String str;
        String str2;
        int i;
        if (socializeEntity != null) {
            shareContent = socializeEntity.getShareContent();
            media = socializeEntity.getMedia();
        } else {
            media = null;
            shareContent = null;
        }
        if (media != null && (media instanceof BaseShareContent)) {
            BaseShareContent baseShareContent = (BaseShareContent) media;
            shareContent = baseShareContent.getShareContent();
            media = baseShareContent.getShareMedia();
        }
        switch (c.a[this.a.ordinal()]) {
            case 1:
                str = "com.twitter.android";
                str2 = "com.twitter.android.composer.ComposerActivity";
                break;
            case 2:
                str = "com.google.android.apps.plus";
                str2 = "com.google.android.libraries.social.gateway.GatewayActivity";
                break;
            case 3:
                str = "com.facebook.katana";
                str2 = "com.facebook.composer.shareintent.ImplicitShareIntentHandlerDefaultAlias";
                break;
            default:
                str2 = null;
                str = null;
                break;
        }
        if (str == null || str2 == null || !DeviceConfig.isAppInstalled(str, context)) {
            Toast.makeText(context, "sorry, you haven't installed " + this.a + "yet.", 0).show();
            i = StatusCode.ST_CODE_ERROR_CANCEL;
        } else {
            SocializeConfig.setSelectedPlatfrom(this.a);
            Intent a = a.b(context, shareContent, media);
            a.setClassName(str, str2);
            try {
                context.startActivity(a);
                SocializeUtils.sendAnalytic(context, this.b, shareContent, media, this.a.toString
                        ());
                i = 200;
            } catch (Exception e) {
                Toast.makeText(context, "" + this.a + " is disabled", 0).show();
                i = StatusCode.ST_CODE_ERROR_CANCEL;
            }
        }
        if (snsPostListener != null) {
            snsPostListener.onComplete(this.a, i, socializeEntity);
        }
    }
}
