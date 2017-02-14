package com.umeng.socialize.controller.listener;

import android.content.Context;
import android.os.Bundle;

import com.umeng.socialize.bean.CallbackConfig.ICallbackListener;
import com.umeng.socialize.bean.MultiStatus;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.bean.SocializeUser;
import com.umeng.socialize.bean.UMComment;
import com.umeng.socialize.bean.UMFriend;
import com.umeng.socialize.exception.SocializeException;

import java.util.List;
import java.util.Map;

public abstract class SocializeListeners {

    public interface OnSnsPlatformClickListener {
        void onClick(Context context, SocializeEntity socializeEntity, SnsPostListener
                snsPostListener);
    }

    public interface UMDataListener {
        void onComplete(int i, Map<String, Object> map);

        void onStart();
    }

    public interface UMAuthListener extends ICallbackListener {
        void onCancel(SHARE_MEDIA share_media);

        void onComplete(Bundle bundle, SHARE_MEDIA share_media);

        void onError(SocializeException socializeException, SHARE_MEDIA share_media);

        void onStart(SHARE_MEDIA share_media);
    }

    public interface SocializeClientListener extends ICallbackListener {
        void onComplete(int i, SocializeEntity socializeEntity);

        void onStart();
    }

    public static abstract class LoginListener {
        public void loginSuccessed(SHARE_MEDIA share_media, boolean z) {
        }

        public void loginFailed(int i) {
        }

        public void dissmiss() {
        }
    }

    public interface SnsPostListener extends ICallbackListener {
        void onComplete(SHARE_MEDIA share_media, int i, SocializeEntity socializeEntity);

        void onStart();
    }

    public interface FetchCommetsListener {
        void onComplete(int i, List<UMComment> list, SocializeEntity socializeEntity);

        void onStart();
    }

    public interface FetchFriendsListener {
        void onComplete(int i, List<UMFriend> list);

        void onStart();
    }

    public interface FetchUserListener {
        void onComplete(int i, SocializeUser socializeUser);

        void onStart();
    }

    public interface MulStatusListener extends ICallbackListener {
        void onComplete(MultiStatus multiStatus, int i, SocializeEntity socializeEntity);

        void onStart();
    }

    public interface UMShareBoardListener {
        void onDismiss();

        void onShow();
    }

    private SocializeListeners() {
    }
}
