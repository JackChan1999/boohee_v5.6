package com.umeng.socialize.controller;

import android.app.Activity;
import android.content.Context;

import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.UMShareMsg;
import com.umeng.socialize.controller.listener.SocializeListeners.MulStatusListener;
import com.umeng.socialize.controller.listener.SocializeListeners.SnsPostListener;
import com.umeng.socialize.controller.listener.SocializeListeners.UMShareBoardListener;

public interface ShareService {
    void directShare(Context context, SHARE_MEDIA share_media, SnsPostListener snsPostListener);

    void dismissShareBoard();

    boolean isOpenShareBoard();

    void openShare(Activity activity, SnsPostListener snsPostListener);

    void openShare(Activity activity, boolean z);

    void postShare(Context context, SHARE_MEDIA share_media, SnsPostListener snsPostListener);

    void postShare(Context context, String str, SHARE_MEDIA share_media, SnsPostListener
            snsPostListener);

    void postShareByCustomPlatform(Context context, String str, String str2, UMShareMsg
            uMShareMsg, SnsPostListener snsPostListener);

    void postShareByID(Context context, String str, String str2, SHARE_MEDIA share_media,
                       SnsPostListener snsPostListener);

    void postShareMulti(Context context, MulStatusListener mulStatusListener, SHARE_MEDIA...
            share_mediaArr);

    void setShareBoardListener(UMShareBoardListener uMShareBoardListener);

    void shareEmail(Context context);

    void shareSms(Context context);

    @Deprecated
    void shareTo(Activity activity, SHARE_MEDIA share_media, String str, byte[] bArr);

    @Deprecated
    void shareTo(Activity activity, String str, byte[] bArr);
}
