package com.umeng.socialize.controller.impl;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;

import com.umeng.socialize.bean.CallbackConfig.ICallbackListener;
import com.umeng.socialize.bean.MultiStatus;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.SNSPair;
import com.umeng.socialize.bean.ShareType;
import com.umeng.socialize.bean.SnsAccount;
import com.umeng.socialize.bean.SocializeConfig;
import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.bean.StatusCode;
import com.umeng.socialize.bean.UMComment;
import com.umeng.socialize.bean.UMShareMsg;
import com.umeng.socialize.bean.UMToken;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.listener.SocializeListeners.FetchCommetsListener;
import com.umeng.socialize.controller.listener.SocializeListeners.FetchFriendsListener;
import com.umeng.socialize.controller.listener.SocializeListeners.FetchUserListener;
import com.umeng.socialize.controller.listener.SocializeListeners.LoginListener;
import com.umeng.socialize.controller.listener.SocializeListeners.MulStatusListener;
import com.umeng.socialize.controller.listener.SocializeListeners.SnsPostListener;
import com.umeng.socialize.controller.listener.SocializeListeners.SocializeClientListener;
import com.umeng.socialize.controller.listener.SocializeListeners.UMAuthListener;
import com.umeng.socialize.controller.listener.SocializeListeners.UMDataListener;
import com.umeng.socialize.controller.listener.SocializeListeners.UMShareBoardListener;
import com.umeng.socialize.exception.SocializeException;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMediaObject;
import com.umeng.socialize.net.utils.AesHelper;
import com.umeng.socialize.sso.UMSsoHandler;
import com.umeng.socialize.utils.ListenerUtils;
import com.umeng.socialize.utils.Log;
import com.umeng.socialize.utils.OauthHelper;
import com.umeng.socialize.utils.SocializeUtils;

import java.util.HashMap;
import java.util.Map;

/* compiled from: SocialServiceImpl */
public final class v extends InitializeController implements UMSocialService {
    public static volatile Map<String, SocializeEntity> g = new HashMap();
    private static final   String                       h = v.class.getName();
    private static         SocializeConfig              i = SocializeConfig.getSocializeConfig();

    public v(SocializeEntity socializeEntity) {
        super(socializeEntity);
    }

    public void initEntity(Context context, SocializeClientListener socializeClientListener) {
        AesHelper.setPassword(SocializeUtils.getAppkey(context));
        new w(this, socializeClientListener, this, context).execute();
    }

    public void likeChange(Context context, SocializeClientListener socializeClientListener) {
        super.likeChange(context, socializeClientListener);
    }

    public void postLike(Context context, SocializeClientListener socializeClientListener) {
        super.postLike(context, socializeClientListener);
    }

    public void uploadToken(Context context, UMToken uMToken, SocializeClientListener
            socializeClientListener) {
        super.uploadPlatformToken(context, uMToken, socializeClientListener);
    }

    public void postUnLike(Context context, SocializeClientListener socializeClientListener) {
        super.postUnLike(context, socializeClientListener);
    }

    public void getComments(Context context, FetchCommetsListener fetchCommetsListener, long j) {
        super.a(context, j, fetchCommetsListener);
    }

    public void getPlatformKeys(Context context, UMDataListener uMDataListener) {
        super.getPlatformKeys(context, uMDataListener);
    }

    public void login(Context context, SHARE_MEDIA share_media, SocializeClientListener
            socializeClientListener) {
        super.a(context, share_media, socializeClientListener);
    }

    public void login(Context context, SnsAccount snsAccount, SocializeClientListener
            socializeClientListener) {
        super.a(context, snsAccount, socializeClientListener);
    }

    public void loginout(Context context, SocializeClientListener socializeClientListener) {
        super.a(context, socializeClientListener);
    }

    public void postComment(Context context, UMComment uMComment, MulStatusListener
            mulStatusListener, SHARE_MEDIA... share_mediaArr) {
        super.postComment(context, uMComment, mulStatusListener, share_mediaArr);
    }

    public void getUserInfo(Context context, FetchUserListener fetchUserListener) {
        new x(this, fetchUserListener, this, context).execute();
    }

    public void shareSms(Context context) {
        this.b.shareSms(context);
    }

    public void shareEmail(Context context) {
        this.b.shareEmail(context);
    }

    public void directShare(Context context, SHARE_MEDIA share_media, SnsPostListener
            snsPostListener) {
        this.b.directShare(context, share_media, snsPostListener);
    }

    public void postShare(Context context, SHARE_MEDIA share_media, SnsPostListener
            snsPostListener) {
        this.b.postShare(context, share_media, snsPostListener);
    }

    public void postShare(Context context, String str, SHARE_MEDIA share_media, SnsPostListener
            snsPostListener) {
        this.b.postShare(context, str, share_media, snsPostListener);
    }

    public void postShareByCustomPlatform(Context context, String str, String str2, UMShareMsg
            uMShareMsg, SnsPostListener snsPostListener) {
        this.b.postShareByCustomPlatform(context, str, str2, uMShareMsg, snsPostListener);
    }

    public void postShareMulti(Context context, MulStatusListener mulStatusListener,
                               SHARE_MEDIA... share_mediaArr) {
        this.b.postShareMulti(context, mulStatusListener, share_mediaArr);
    }

    public void postShareByID(Context context, String str, String str2, SHARE_MEDIA share_media,
                              SnsPostListener snsPostListener) {
        this.b.postShareByID(context, str, str2, share_media, snsPostListener);
    }

    public void getFriends(Context context, FetchFriendsListener fetchFriendsListener,
                           SHARE_MEDIA share_media) {
        Object usid = OauthHelper.getUsid(context, share_media);
        if (TextUtils.isEmpty(usid)) {
            if (fetchFriendsListener != null) {
                fetchFriendsListener.onStart();
            }
            if (fetchFriendsListener != null) {
                fetchFriendsListener.onComplete(StatusCode.ST_CODE_SDK_NO_OAUTH, null);
            }
        }
        new y(this, fetchFriendsListener, this, context, share_media, usid).execute();
    }

    public void deleteOauth(Context context, SHARE_MEDIA share_media, SocializeClientListener
            socializeClientListener) {
        super.deleteOauth(context, share_media, socializeClientListener);
    }

    public void setShareType(ShareType shareType) {
        this.a.setShareType(shareType);
    }

    public void setShareContent(String str) {
        this.a.setShareContent(str);
    }

    public void setShareImage(UMImage uMImage) {
        this.a.setMedia(uMImage);
    }

    public boolean setShareMedia(UMediaObject uMediaObject) {
        if (uMediaObject == null) {
            this.a.setMedia(null);
            return true;
        } else if (uMediaObject.isMultiMedia()) {
            this.a.setMedia(uMediaObject);
            return true;
        } else {
            Log.w(h, "unable set share media.type is no support.");
            return false;
        }
    }

    public void setConfig(SocializeConfig socializeConfig) {
        if (socializeConfig != null) {
            this.a.setConfig(socializeConfig);
        }
    }

    public void setCustomId(String str) {
        this.a.mCustomID = str;
    }

    public void setGlobalConfig(SocializeConfig socializeConfig) {
        if (socializeConfig != null) {
            i = socializeConfig;
        }
    }

    public SocializeConfig getConfig() {
        if (this.a.getEntityConfig() != null) {
            return this.a.getEntityConfig();
        }
        if (i == null) {
            i = SocializeConfig.getSocializeConfig();
        }
        return i;
    }

    public boolean hasShareImage() {
        if (this.a == null || this.a.getMedia() == null) {
            return false;
        }
        return true;
    }

    public boolean hasShareContent() {
        if (this.a == null || TextUtils.isEmpty(this.a.getShareContent())) {
            return false;
        }
        return true;
    }

    public void openShare(Activity activity, boolean z) {
        this.b.openShare(activity, z);
    }

    public void openShare(Activity activity, SnsPostListener snsPostListener) {
        this.b.openShare(activity, snsPostListener);
    }

    public void openComment(Context context, boolean z) {
        super.openComment(context, z);
    }

    public void openUserCenter(Context context, int... iArr) {
        this.f.openUserCenter(context, iArr);
    }

    public void showLoginDialog(Context context, LoginListener loginListener) {
        this.f.showLoginDialog(context, loginListener);
    }

    public void doOauthVerify(Context context, SHARE_MEDIA share_media, UMAuthListener
            uMAuthListener) {
        this.e.doOauthVerify(context, share_media, uMAuthListener);
    }

    public void follow(Context context, SHARE_MEDIA share_media, MulStatusListener
            mulStatusListener, String... strArr) {
        if (OauthHelper.isAuthenticatedAndTokenNotExpired(context, share_media)) {
            new z(this, mulStatusListener, this, context, new SNSPair(share_media.toString(),
                    OauthHelper.getUsid(context, share_media)), strArr, share_media).execute();
        } else if (mulStatusListener != null) {
            mulStatusListener.onStart();
            mulStatusListener.onComplete(new MultiStatus(StatusCode.ST_CODE_SDK_NO_OAUTH),
                    StatusCode.ST_CODE_SDK_NO_OAUTH, this.a);
        }
    }

    public void setEntityName(String str) {
        this.a.setNickName(str);
    }

    public void getPlatformInfo(Context context, SHARE_MEDIA share_media, UMDataListener
            uMDataListener) {
        UMDataListener createDataListener;
        if (uMDataListener == null) {
            createDataListener = ListenerUtils.createDataListener();
        } else {
            createDataListener = uMDataListener;
        }
        if (!OauthHelper.isAuthenticated(context, share_media) && share_media != SHARE_MEDIA
                .FACEBOOK) {
            createDataListener.onStart();
            createDataListener.onComplete(StatusCode.ST_CODE_SDK_NO_OAUTH, null);
        } else if (share_media == SHARE_MEDIA.WEIXIN || share_media == SHARE_MEDIA.WEIXIN_CIRCLE
                || share_media == SHARE_MEDIA.QQ || share_media == SHARE_MEDIA.FACEBOOK) {
            UMSsoHandler ssoHandler = getConfig().getSsoHandler(share_media.getReqCode());
            if (ssoHandler == null) {
                Log.w("", "请添加" + share_media.toString() + "平台");
            } else {
                ssoHandler.getUserInfo(createDataListener);
            }
        } else {
            new aa(this, createDataListener, this, context, new SNSPair(share_media.toString(),
                    OauthHelper.getUsid(context, share_media))).execute();
        }
    }

    public boolean registerListener(ICallbackListener iCallbackListener, int i) throws
            SocializeException {
        return getConfig().registerListener(iCallbackListener, i);
    }

    public boolean registerListener(ICallbackListener iCallbackListener) throws SocializeException {
        return getConfig().registerListener(iCallbackListener);
    }

    public boolean unregisterListener(ICallbackListener iCallbackListener) {
        return getConfig().unregisterListener(iCallbackListener);
    }

    @Deprecated
    public void shareTo(Activity activity, SHARE_MEDIA share_media, String str, byte[] bArr) {
        this.b.shareTo(activity, share_media, str, bArr);
    }

    @Deprecated
    public void shareTo(Activity activity, String str, byte[] bArr) {
        this.b.shareTo(activity, str, bArr);
    }

    public void setAppWebSite(SHARE_MEDIA share_media, String str) {
        SocializeEntity.setAppWebSite(share_media, str);
    }

    public void setAppWebSite(String str) {
        setAppWebSite(SHARE_MEDIA.GENERIC, str);
    }

    public void checkTokenExpired(Context context, SHARE_MEDIA[] share_mediaArr, UMDataListener
            uMDataListener) {
        super.checkTokenExpired(context, share_mediaArr, uMDataListener);
    }

    public void setShareBoardListener(UMShareBoardListener uMShareBoardListener) {
        this.b.setShareBoardListener(uMShareBoardListener);
    }

    public void dismissShareBoard() {
        this.b.dismissShareBoard();
    }

    public boolean isOpenShareBoard() {
        return this.b.isOpenShareBoard();
    }
}
