package com.umeng.socialize.utils;

import android.app.ProgressDialog;
import android.content.Context;

import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.common.ResContainer;
import com.umeng.socialize.common.ResContainer.ResType;
import com.umeng.socialize.common.SocialSNSHelper;

public class LoadingDialog {
    private static int theme           = -1;
    private static int waitingMessage  = -1;
    private static int waitingRedirect = -1;

    public static ProgressDialog createProgressDialog(Context context, SHARE_MEDIA share_media,
                                                      String str, boolean z) {
        if (theme == -1) {
            theme = ResContainer.getResourceId(context, ResType.STYLE, "Theme.UMDialog");
        }
        if (!z && waitingRedirect == -1) {
            waitingRedirect = ResContainer.getResourceId(context, ResType.STRING,
                    "umeng_socialize_text_waitting_redirect");
        }
        if (!z && waitingMessage == -1) {
            waitingMessage = ResContainer.getResourceId(context, ResType.STRING,
                    "umeng_socialize_text_waitting_message");
        }
        ProgressDialog progressDialog = new ProgressDialog(context, theme);
        if (!z) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(context.getString(waitingRedirect));
            stringBuilder.append(SocialSNSHelper.getShowWord(context, share_media));
            stringBuilder.append(context.getString(waitingMessage));
            str = stringBuilder.toString();
        }
        progressDialog.setMessage(str);
        return progressDialog;
    }
}
