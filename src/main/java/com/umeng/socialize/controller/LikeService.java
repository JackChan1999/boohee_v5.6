package com.umeng.socialize.controller;

import android.content.Context;

import com.umeng.socialize.controller.listener.SocializeListeners.SocializeClientListener;

public interface LikeService {
    void likeChange(Context context, SocializeClientListener socializeClientListener);

    void postLike(Context context, SocializeClientListener socializeClientListener);

    void postUnLike(Context context, SocializeClientListener socializeClientListener);
}
