package com.umeng.socialize.controller;

import android.content.Context;

import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.UMToken;
import com.umeng.socialize.controller.listener.SocializeListeners.SocializeClientListener;
import com.umeng.socialize.controller.listener.SocializeListeners.UMAuthListener;
import com.umeng.socialize.controller.listener.SocializeListeners.UMDataListener;

public interface AuthService {
    void checkTokenExpired(Context context, SHARE_MEDIA[] share_mediaArr, UMDataListener
            uMDataListener);

    void deleteOauth(Context context, SHARE_MEDIA share_media, SocializeClientListener
            socializeClientListener);

    void doOauthVerify(Context context, SHARE_MEDIA share_media, UMAuthListener uMAuthListener);

    void getPlatformKeys(Context context, UMDataListener uMDataListener);

    void uploadToken(Context context, UMToken uMToken, SocializeClientListener
            socializeClientListener);
}
