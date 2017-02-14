package com.umeng.socialize.controller;

import android.content.Context;

import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.UMComment;
import com.umeng.socialize.controller.listener.SocializeListeners.FetchCommetsListener;
import com.umeng.socialize.controller.listener.SocializeListeners.MulStatusListener;

public interface CommentService {
    void getComments(Context context, FetchCommetsListener fetchCommetsListener, long j);

    void openComment(Context context, boolean z);

    void postComment(Context context, UMComment uMComment, MulStatusListener mulStatusListener,
                     SHARE_MEDIA... share_mediaArr);
}
