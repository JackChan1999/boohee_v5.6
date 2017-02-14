package com.umeng.socialize.controller;

import android.content.Context;

import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.SnsAccount;
import com.umeng.socialize.controller.listener.SocializeListeners.LoginListener;
import com.umeng.socialize.controller.listener.SocializeListeners.SocializeClientListener;

/* compiled from: UMSubServiceFactory */
class m implements UserCenterService {
    final /* synthetic */ l a;

    m(l lVar) {
        this.a = lVar;
    }

    public void showLoginDialog(Context context, LoginListener loginListener) {
        this.a.a("init LikeService failed,please add SocialSDK_ucenter.jar file");
    }

    public void openUserCenter(Context context, int... iArr) {
        this.a.a("init LikeService failed,please add SocialSDK_ucenter.jar file");
    }

    public void loginout(Context context, SocializeClientListener socializeClientListener) {
        this.a.a("init LikeService failed,please add SocialSDK_ucenter.jar file");
    }

    public void login(Context context, SHARE_MEDIA share_media, SocializeClientListener
            socializeClientListener) {
        this.a.a("init LikeService failed,please add SocialSDK_ucenter.jar file");
    }

    public void login(Context context, SnsAccount snsAccount, SocializeClientListener
            socializeClientListener) {
        this.a.a("init LikeService failed,please add SocialSDK_ucenter.jar file");
    }
}
