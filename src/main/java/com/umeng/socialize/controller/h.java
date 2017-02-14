package com.umeng.socialize.controller;

import android.content.Context;

import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.UMComment;
import com.umeng.socialize.controller.listener.SocializeListeners.FetchCommetsListener;
import com.umeng.socialize.controller.listener.SocializeListeners.MulStatusListener;

/* compiled from: UMSubServiceFactory */
class h implements CommentService {
    final String a = "init CommentService failed,please add SocialSDK_comment.jar file";
    final /* synthetic */ g b;

    h(g gVar) {
        this.b = gVar;
    }

    public void postComment(Context context, UMComment uMComment, MulStatusListener
            mulStatusListener, SHARE_MEDIA... share_mediaArr) {
        this.b.a("init CommentService failed,please add SocialSDK_comment.jar file");
    }

    public void openComment(Context context, boolean z) {
        this.b.a("init CommentService failed,please add SocialSDK_comment.jar file");
    }

    public void getComments(Context context, FetchCommetsListener fetchCommetsListener, long j) {
        this.b.a("init CommentService failed,please add SocialSDK_comment.jar file");
    }
}
