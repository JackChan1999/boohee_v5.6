package com.umeng.socialize.controller;

import android.content.Context;

import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.SnsAccount;
import com.umeng.socialize.controller.listener.SocializeListeners.LoginListener;
import com.umeng.socialize.controller.listener.SocializeListeners.SocializeClientListener;

public interface UserCenterService {
    void login(Context context, SHARE_MEDIA share_media, SocializeClientListener
            socializeClientListener);

    void login(Context context, SnsAccount snsAccount, SocializeClientListener
            socializeClientListener);

    void loginout(Context context, SocializeClientListener socializeClientListener);

    void openUserCenter(Context context, int... iArr);

    void showLoginDialog(Context context, LoginListener loginListener);
}
