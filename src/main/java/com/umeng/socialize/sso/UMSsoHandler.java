package com.umeng.socialize.sso;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.umeng.socialize.bean.CustomPlatform;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.SocializeConfig;
import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.controller.listener.SocializeListeners.SnsPostListener;
import com.umeng.socialize.controller.listener.SocializeListeners.SocializeClientListener;
import com.umeng.socialize.controller.listener.SocializeListeners.UMAuthListener;
import com.umeng.socialize.controller.listener.SocializeListeners.UMDataListener;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMVideo;
import com.umeng.socialize.media.UMediaObject;
import com.umeng.socialize.media.UMusic;
import com.umeng.socialize.net.utils.AesHelper;
import com.umeng.socialize.utils.BitmapUtils;
import com.umeng.socialize.utils.Log;
import com.umeng.socialize.utils.SocializeUtils;

import java.util.HashMap;
import java.util.Map;

public abstract class UMSsoHandler {
    public static final    String              APPKEY                = "appKey";
    public static final    String              APPSECRET             = "appSecret";
    protected static final String              AUDIO_URL             = "audio_url";
    protected static final String              DEFAULT_TARGET_URL    = "http://www.umeng" +
            ".com/social";
    protected static final String              IMAGE_PATH_LOCAL      = "image_path_local";
    protected static final String              IMAGE_PATH_URL        = "image_path_url";
    protected static final String              IMAGE_TARGETURL       = "image_target_url";
    public static final    String              SECRET_KEY            = "secretKey";
    private static final   String              TAG                   = UMSsoHandler.class.getName();
    public static          SocializeEntity     mEntity               = null;
    protected              boolean             isShareAfterAuthorize = true;
    protected              Context             mContext              = null;
    public                 CustomPlatform      mCustomPlatform       = null;
    public                 Map<String, String> mExtraData            = new HashMap();
    public                 String              mShareContent         = "";
    public                 UMediaObject        mShareMedia           = null;
    protected              SocializeConfig     mSocializeConfig      = SocializeConfig
            .getSocializeConfig();
    protected String mTargetUrl;
    protected String mTitle;

    public abstract void authorize(Activity activity, UMAuthListener uMAuthListener);

    public abstract void authorizeCallBack(int i, int i2, Intent intent);

    protected abstract CustomPlatform createNewPlatform();

    public abstract int getRequstCode();

    protected abstract void handleOnClick(CustomPlatform customPlatform, SocializeEntity
            socializeEntity, SnsPostListener snsPostListener);

    public abstract boolean isClientInstalled();

    protected abstract void sendReport(boolean z);

    public abstract boolean shareTo();

    public UMSsoHandler(Context context) {
        if (context != null) {
            this.mContext = context.getApplicationContext();
            AesHelper.setPassword(SocializeUtils.getAppkey(this.mContext));
        }
    }

    public final CustomPlatform build() {
        if (this.mCustomPlatform != null) {
            return this.mCustomPlatform;
        }
        return createNewPlatform();
    }

    public void addToSocialSDK() {
        this.mSocializeConfig.addCustomPlatform(build());
        this.mSocializeConfig.setSsoHandler(this);
    }

    public void deleteAuthorization(SocializeEntity socializeEntity, SHARE_MEDIA share_media,
                                    SocializeClientListener socializeClientListener) {
    }

    public boolean isShareAfterAuthorize() {
        return this.isShareAfterAuthorize;
    }

    public void setShareAfterAuthorize(boolean z) {
        this.isShareAfterAuthorize = z;
    }

    protected void parseImage(UMediaObject uMediaObject) {
        if (uMediaObject == null || !(uMediaObject instanceof UMImage)) {
            Log.w(TAG, "parse image params error , uMediaObject is null or isn't instance of " +
                    "UMImage");
            return;
        }
        UMImage uMImage = (UMImage) uMediaObject;
        if (!uMImage.isSerialized()) {
            uMImage.waitImageToSerialize();
        }
        String str = "";
        str = "";
        Log.v("10.12", "image=" + uMImage);
        if (TextUtils.isEmpty(this.mTargetUrl)) {
            if (TextUtils.isEmpty(uMImage.getTargetUrl())) {
                this.mTargetUrl = uMImage.toUrl();
            } else {
                this.mTargetUrl = uMImage.getTargetUrl();
            }
        }
        String toUrl = uMImage.toUrl();
        Object imageCachePath = uMImage.getImageCachePath();
        Log.v("10.12", "image path =" + imageCachePath);
        if (!BitmapUtils.isFileExist(imageCachePath)) {
            imageCachePath = "";
        }
        this.mExtraData.put(IMAGE_PATH_LOCAL, imageCachePath);
        this.mExtraData.put(IMAGE_PATH_URL, toUrl);
    }

    protected void parseMusic(UMediaObject uMediaObject) {
        UMusic uMusic = (UMusic) uMediaObject;
        this.mExtraData.put("audio_url", uMusic.toUrl());
        boolean isEmpty = TextUtils.isEmpty(this.mTargetUrl);
        if (TextUtils.isEmpty(uMusic.getThumb())) {
            parseImage(uMusic.getThumbImage());
        } else {
            this.mExtraData.put(IMAGE_PATH_URL, uMusic.getThumb());
        }
        if (!TextUtils.isEmpty(uMusic.getTitle())) {
            this.mTitle = uMusic.getTitle();
        }
        if (!isEmpty) {
            return;
        }
        if (TextUtils.isEmpty(uMusic.getTargetUrl())) {
            this.mTargetUrl = uMusic.toUrl();
        } else {
            this.mTargetUrl = uMusic.getTargetUrl();
        }
    }

    protected void parseVideo(UMediaObject uMediaObject) {
        UMVideo uMVideo = (UMVideo) uMediaObject;
        this.mExtraData.put("audio_url", uMVideo.toUrl());
        boolean isEmpty = TextUtils.isEmpty(this.mTargetUrl);
        if (TextUtils.isEmpty(uMVideo.getThumb())) {
            parseImage(uMVideo.getThumbImage());
        } else {
            this.mExtraData.put(IMAGE_PATH_URL, uMVideo.getThumb());
        }
        if (!TextUtils.isEmpty(uMVideo.getTitle())) {
            this.mTitle = uMVideo.getTitle();
        }
        if (!isEmpty) {
            return;
        }
        if (TextUtils.isEmpty(uMVideo.getTargetUrl())) {
            this.mTargetUrl = uMVideo.toUrl();
        } else {
            this.mTargetUrl = uMVideo.getTargetUrl();
        }
    }

    public void setTargetUrl(String str) {
        this.mTargetUrl = str;
    }

    public void getUserInfo(UMDataListener uMDataListener) {
    }

    public Context getContext() {
        return this.mContext;
    }
}
