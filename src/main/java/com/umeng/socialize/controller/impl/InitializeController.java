package com.umeng.socialize.controller.impl;

import android.content.Context;

import com.umeng.socialize.bean.MultiStatus;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.SNSPair;
import com.umeng.socialize.bean.SnsAccount;
import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.bean.StatusCode;
import com.umeng.socialize.bean.UMComment;
import com.umeng.socialize.bean.UMToken;
import com.umeng.socialize.controller.listener.SocializeListeners.FetchCommetsListener;
import com.umeng.socialize.controller.listener.SocializeListeners.MulStatusListener;
import com.umeng.socialize.controller.listener.SocializeListeners.SocializeClientListener;
import com.umeng.socialize.controller.listener.SocializeListeners.UMDataListener;
import com.umeng.socialize.exception.SocializeException;
import com.umeng.socialize.media.UMediaObject;
import com.umeng.socialize.net.i;
import com.umeng.socialize.net.l;
import com.umeng.socialize.net.y;

import java.util.HashMap;

public class InitializeController extends BaseController {
    public InitializeController(SocializeEntity socializeEntity) {
        super(socializeEntity);
    }

    protected void likeChange(Context context, SocializeClientListener socializeClientListener) {
        this.d.likeChange(context, socializeClientListener);
    }

    protected void postLike(Context context, SocializeClientListener socializeClientListener) {
        this.d.postLike(context, socializeClientListener);
    }

    protected void postUnLike(Context context, SocializeClientListener socializeClientListener) {
        this.d.postUnLike(context, socializeClientListener);
    }

    protected void a(Context context, SHARE_MEDIA share_media, SocializeClientListener
            socializeClientListener) {
        if (a(context)) {
            this.f.login(context, share_media, socializeClientListener);
        } else {
            socializeClientListener.onComplete(StatusCode.ST_CODE_SDK_INITQUEUE_FAILED, this.a);
        }
    }

    protected void a(Context context, SocializeClientListener socializeClientListener) {
        if (a(context)) {
            this.f.loginout(context, socializeClientListener);
        } else {
            socializeClientListener.onComplete(StatusCode.ST_CODE_SDK_INITQUEUE_FAILED, this.a);
        }
    }

    public void postComment(Context context, UMComment uMComment, MulStatusListener
            mulStatusListener, SHARE_MEDIA... share_mediaArr) {
        this.c.postComment(context, uMComment, mulStatusListener, share_mediaArr);
    }

    protected void openComment(Context context, boolean z) {
        this.c.openComment(context, z);
    }

    protected void a(Context context, SnsAccount snsAccount, SocializeClientListener
            socializeClientListener) {
        if (a(context)) {
            this.f.login(context, snsAccount, socializeClientListener);
        } else {
            socializeClientListener.onComplete(StatusCode.ST_CODE_SDK_INITQUEUE_FAILED, this.a);
        }
    }

    public MultiStatus follow(Context context, SNSPair sNSPair, String... strArr) {
        if (!a(context)) {
            return new MultiStatus(StatusCode.ST_CODE_SDK_INITQUEUE_FAILED);
        }
        MultiStatus follow = super.follow(context, sNSPair, strArr);
        if (follow == null) {
            return new MultiStatus(StatusCode.ST_CODE_SDK_UNKNOW);
        }
        return follow;
    }

    protected void deleteOauth(Context context, SHARE_MEDIA share_media, SocializeClientListener
            socializeClientListener) {
        if (a(context)) {
            this.e.deleteOauth(context, share_media, socializeClientListener);
        } else if (socializeClientListener != null) {
            socializeClientListener.onComplete(StatusCode.ST_CODE_SDK_INITQUEUE_FAILED, this.a);
        }
    }

    public i getUserInfo(Context context) throws SocializeException {
        if (a(context)) {
            return super.getUserInfo(context);
        }
        return null;
    }

    protected void a(Context context, long j, FetchCommetsListener fetchCommetsListener) throws
            SocializeException {
        this.c.getComments(context, fetchCommetsListener, j);
    }

    public l getFriends(Context context, SHARE_MEDIA share_media, String str) throws
            SocializeException {
        if (a(context)) {
            return super.getFriends(context, share_media, str);
        }
        return null;
    }

    public y getPlatformInfo(Context context, SNSPair sNSPair) {
        if (a(context)) {
            return super.getPlatformInfo(context, sNSPair);
        }
        return null;
    }

    public void getPlatformKeys(Context context, UMDataListener uMDataListener) {
        if (a(context)) {
            this.e.getPlatformKeys(context, uMDataListener);
        } else {
            uMDataListener.onComplete(StatusCode.ST_CODE_SDK_INITQUEUE_FAILED, new HashMap());
        }
    }

    public void uploadPlatformToken(Context context, UMToken uMToken, SocializeClientListener
            socializeClientListener) {
        if (a(context)) {
            this.e.uploadToken(context, uMToken, socializeClientListener);
        } else {
            socializeClientListener.onComplete(StatusCode.ST_CODE_SDK_INITQUEUE_FAILED, this.a);
        }
    }

    public String uploadImage(Context context, UMediaObject uMediaObject, String str) {
        if (a(context)) {
            return super.uploadImage(context, uMediaObject, str);
        }
        return "";
    }

    public int uploadStatisticsData(Context context) {
        if (a(context)) {
            return super.uploadStatisticsData(context);
        }
        return StatusCode.ST_CODE_SDK_UNKNOW;
    }

    public void checkTokenExpired(Context context, SHARE_MEDIA[] share_mediaArr, UMDataListener
            uMDataListener) {
        if (a(context)) {
            this.e.checkTokenExpired(context, share_mediaArr, uMDataListener);
        }
    }

    public int uploadKeySecret(Context context) {
        if (a(context)) {
            return super.uploadKeySecret(context);
        }
        return StatusCode.ST_CODE_SDK_UNKNOW;
    }
}
