package com.umeng.socialize.controller;

import android.content.Context;

import com.umeng.socialize.controller.listener.SocializeListeners.SocializeClientListener;

/* compiled from: UMSubServiceFactory */
class k implements LikeService {
    final /* synthetic */ j a;

    k(j jVar) {
        this.a = jVar;
    }

    public void postUnLike(Context context, SocializeClientListener socializeClientListener) {
        this.a.a("init LikeService failed,please add SocialSDK_like.jar file");
    }

    public void postLike(Context context, SocializeClientListener socializeClientListener) {
        this.a.a("init LikeService failed,please add SocialSDK_like.jar file");
    }

    public void likeChange(Context context, SocializeClientListener socializeClientListener) {
        this.a.a("init LikeService failed,please add SocialSDK_like.jar file");
    }
}
