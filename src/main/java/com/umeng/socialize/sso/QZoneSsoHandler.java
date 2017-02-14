package com.umeng.socialize.sso;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import com.boohee.one.BuildConfig;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.UiError;
import com.umeng.socialize.bean.CustomPlatform;
import com.umeng.socialize.bean.HandlerRequestCode;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.ShareType;
import com.umeng.socialize.bean.SocializeConfig;
import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.bean.StatusCode;
import com.umeng.socialize.bean.UMShareMsg;
import com.umeng.socialize.common.ResContainer;
import com.umeng.socialize.common.ResContainer.ResType;
import com.umeng.socialize.common.SocializeConstants;
import com.umeng.socialize.controller.listener.SocializeListeners.SnsPostListener;
import com.umeng.socialize.controller.listener.SocializeListeners.UMAuthListener;
import com.umeng.socialize.exception.SocializeException;
import com.umeng.socialize.media.QZoneShareContent;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMVideo;
import com.umeng.socialize.media.UMediaObject;
import com.umeng.socialize.media.UMediaObject.MediaType;
import com.umeng.socialize.media.UMusic;
import com.umeng.socialize.net.utils.SocializeProtocolConstants;
import com.umeng.socialize.sso.UMTencentSsoHandler.ObtainImageUrlListener;
import com.umeng.socialize.utils.Log;
import com.umeng.socialize.utils.OauthHelper;
import com.umeng.socialize.utils.SocializeUtils;
import com.umeng.socialize.utils.StatisticsDataUtils;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class QZoneSsoHandler extends UMTencentSsoHandler {
    private static final String TAG = QZoneSsoHandler.class.getName();
    private WeakReference<Activity> mActivity;
    private IUiListener mUiListener = new IUiListener() {
        public void onError(UiError e) {
            Log.d("Tencent SSo Authorize --> onError:", e.toString());
            QZoneSsoHandler.this.mAuthListener.onError(new SocializeException(e.errorCode, e
                    .errorMessage), SHARE_MEDIA.QZONE);
        }

        public void onCancel() {
            Log.d("### Tencent Sso Authorize --> onCancel", "Authorize Cancel");
            QZoneSsoHandler.this.mAuthListener.onCancel(SHARE_MEDIA.QZONE);
        }

        public void onComplete(Object response) {
            Log.d(QZoneSsoHandler.TAG, "oauth complete...");
            UMSsoHandler.mEntity.addOauthData(QZoneSsoHandler.this.mContext, SHARE_MEDIA.QZONE, 1);
            QZoneSsoHandler.this.uploadToken(QZoneSsoHandler.this.mContext, response,
                    QZoneSsoHandler.this.mAuthListener);
        }
    };

    public QZoneSsoHandler(Activity activity, String appId, String appKey) {
        super(activity, appId, appKey);
        this.mActivity = new WeakReference(activity);
    }

    public void authorize(Activity activity, UMAuthListener authListener) {
        Context appCtx = activity.getApplicationContext();
        this.mActivity = new WeakReference(activity);
        if (this.mTencent != null && this.mTencent.isSessionValid()) {
            this.mTencent.logout(this.mContext);
        }
        this.mAuthListener = authListener;
        SocializeConfig.setSelectedPlatfrom(SHARE_MEDIA.QZONE);
        if (TextUtils.isEmpty(this.mAppID)) {
            this.mAppID = (String) OauthHelper.getAppIdAndAppkey(appCtx).get("appid");
            this.mAppKey = (String) OauthHelper.getAppIdAndAppkey(appCtx).get("appkey");
        }
        if (TextUtils.isEmpty(this.mAppID)) {
            getAppIdFromServer(new ObtainAppIdListener() {
                public void onComplete() {
                    QZoneSsoHandler.this.authorizeCheck();
                }
            });
        } else {
            authorizeCheck();
        }
    }

    public boolean isClientInstalled() {
        return this.mTencent.isSupportSSOLogin((Activity) this.mActivity.get());
    }

    private void authorizeCheck() {
        if (!initTencent()) {
            return;
        }
        if (validTencent()) {
            this.mTencent.reAuth((Activity) this.mActivity.get(), BuildConfig.PLATFORM, this
                    .mUiListener);
        } else if (this.mTencent != null) {
            this.mTencent.login((Activity) this.mActivity.get(), BuildConfig.PLATFORM, this
                    .mUiListener);
        }
    }

    public int getRequstCode() {
        return HandlerRequestCode.QZONE_REQUEST_CODE;
    }

    private IUiListener getShareToQZoneListener() {
        return new IUiListener() {
            public void onError(UiError error) {
                Log.e("IUiListener", "error code : " + error.errorCode + "       error message:"
                        + error.errorMessage);
                QZoneSsoHandler.this.mSocializeConfig.fireAllListenersOnComplete(SnsPostListener
                        .class, SHARE_MEDIA.QZONE, StatusCode.ST_CODE_ERROR, UMSsoHandler.mEntity);
            }

            public void onCancel() {
                QZoneSsoHandler.this.mSocializeConfig.fireAllListenersOnComplete(SnsPostListener
                        .class, SHARE_MEDIA.QZONE, StatusCode.ST_CODE_ERROR_CANCEL, UMSsoHandler
                        .mEntity);
            }

            public void onComplete(Object response) {
                int code = 200;
                if (QZoneSsoHandler.this.getResponseCode(response) != 0) {
                    code = StatusCode.ST_CODE_ERROR;
                }
                QZoneSsoHandler.this.mSocializeConfig.fireAllListenersOnComplete(SnsPostListener
                        .class, SHARE_MEDIA.QZONE, code, UMSsoHandler.mEntity);
                QZoneSsoHandler.this.sendReport(true);
            }
        };
    }

    private Bundle buildParams(UMShareMsg shareMsg) {
        Bundle bundle = new Bundle();
        String shareContent = shareMsg.mText;
        int shareType = 1;
        UMediaObject uMediaObject = shareMsg.getMedia();
        if (uMediaObject instanceof QZoneShareContent) {
            QZoneShareContent qZoneShareContent = (QZoneShareContent) uMediaObject;
            this.mTitle = qZoneShareContent.getTitle();
            shareContent = qZoneShareContent.getShareContent();
            if (!TextUtils.isEmpty(qZoneShareContent.getTargetUrl())) {
                this.mTargetUrl = qZoneShareContent.getTargetUrl();
            }
            uMediaObject = qZoneShareContent.getShareMedia();
        }
        if ((uMediaObject instanceof UMImage) && TextUtils.isEmpty(shareContent)) {
            shareType = 5;
            setShareToImage(bundle, uMediaObject);
        } else if ((uMediaObject instanceof UMVideo) || (uMediaObject instanceof UMusic)) {
            shareType = 2;
            setShareToAudio(bundle, uMediaObject);
        } else {
            setShareToTextAndImage(bundle, uMediaObject);
        }
        bundle.putString("summary", shareContent);
        ArrayList<String> paths = new ArrayList();
        String imagePath = bundle.getString("imageUrl");
        bundle.remove("imageUrl");
        if (!TextUtils.isEmpty(imagePath)) {
            paths.add(imagePath);
        }
        bundle.putStringArrayList("imageUrl", paths);
        bundle.putInt("req_type", shareType);
        if (TextUtils.isEmpty(bundle.getString("title"))) {
            bundle.putString("title", "分享到QQ空间");
        }
        if (TextUtils.isEmpty(bundle.getString("targetUrl"))) {
            bundle.putString("targetUrl", SocializeConstants.SOCIAL_LINK);
            Log.w(TAG, "没有设置QZone targetUrl，分享将采用友盟默认targetUrl");
        }
        bundle.putString("appName", getAppName());
        this.mExtraData.clear();
        this.mExtraData.put(SocializeConstants.FIELD_QZONE_ID, this.mAppID);
        this.mExtraData.put("qzone_secret", this.mAppKey);
        return bundle;
    }

    private void setShareToTextAndImage(Bundle bundle, UMediaObject uMediaObject) {
        setShareToImage(bundle, uMediaObject);
    }

    private void setShareToAudio(Bundle bundle, UMediaObject uMediaObject) {
        if (uMediaObject == null || !((uMediaObject instanceof UMusic) || (uMediaObject
                instanceof UMVideo))) {
            Log.e(TAG, "请设置分享媒体...");
            return;
        }
        if (uMediaObject instanceof UMusic) {
            parseMusic(uMediaObject);
        } else if (uMediaObject instanceof UMVideo) {
            parseVideo(uMediaObject);
        }
        String path = (String) this.mExtraData.get("image_path_local");
        if (TextUtils.isEmpty(path)) {
            path = (String) this.mExtraData.get("image_path_url");
        }
        bundle.putString("imageUrl", path);
        bundle.putString("targetUrl", this.mTargetUrl);
        bundle.putString("audio_url", uMediaObject.toUrl());
        bundle.putString("title", this.mTitle);
    }

    private void setShareToImage(Bundle bundle, UMediaObject uMediaObject) {
        parseImage(uMediaObject);
        String path = (String) this.mExtraData.get("image_path_local");
        if (TextUtils.isEmpty(path)) {
            path = (String) this.mExtraData.get("image_path_url");
        }
        bundle.putString("imageUrl", path);
        if (TextUtils.isEmpty(this.mTargetUrl)) {
            this.mTargetUrl = (String) this.mExtraData.get("image_target_url");
        }
        if (TextUtils.isEmpty(this.mTargetUrl)) {
            this.mTargetUrl = SocializeConstants.SOCIAL_LINK;
            Log.w(TAG, "没有设置QZone targetUrl，分享将采用友盟默认targetUrl");
        }
        bundle.putString("targetUrl", this.mTargetUrl);
        bundle.putString("title", this.mTitle);
        Log.w(TAG, "QZone不支持纯图片分享");
    }

    private UMShareMsg getShareMsg() {
        if (mEntity.getShareMsg() != null) {
            UMShareMsg shareMsg = mEntity.getShareMsg();
            mEntity.setShareMsg(null);
            return shareMsg;
        }
        shareMsg = new UMShareMsg();
        shareMsg.mText = mEntity.getShareContent();
        shareMsg.setMediaData(mEntity.getMedia());
        return shareMsg;
    }

    private void shareToQZone() {
        if (initTencent()) {
            Bundle bundle = buildParams(getShareMsg());
            int type = bundle.getInt("req_type");
            List<String> paths = bundle.getStringArrayList("imageUrl");
            String imagePath = null;
            if (paths != null && paths.size() > 0) {
                imagePath = (String) paths.get(0);
            }
            if (isUploadImageAsync(imagePath, type)) {
                authorize((Activity) this.mActivity.get(), createAuthListener(bundle, new UMImage
                        (this.mContext, imagePath)));
            } else {
                defaultQZoneShare(bundle);
            }
            mEntity.setShareType(ShareType.NORMAL);
        }
    }

    private void defaultQZoneShare(Bundle bundle) {
        this.mSocializeConfig.fireAllListenersOnStart(SnsPostListener.class);
        Log.d(TAG, "invoke Tencent.shareToQzone method...");
        if (this.mTencent != null) {
            this.mTencent.shareToQzone((Activity) this.mActivity.get(), bundle,
                    getShareToQZoneListener());
        }
    }

    protected void initResource() {
        this.mKeyWord = "qzone";
        this.mShowWord = ResContainer.getString(this.mContext, "umeng_socialize_text_qq_zone_key");
        this.mIcon = ResContainer.getResourceId(this.mContext, ResType.DRAWABLE,
                "umeng_socialize_qzone_on");
        this.mGrayIcon = ResContainer.getResourceId(this.mContext, ResType.DRAWABLE,
                "umeng_socialize_qzone_off");
    }

    protected void handleOnClick(CustomPlatform customPlatform, SocializeEntity entity,
                                 SnsPostListener listener) {
        if (SocializeConstants.DEFAULTID.equals(this.mAppID)) {
            showDialog();
            return;
        }
        this.mSocializeConfig.registerListener(listener);
        mEntity = entity;
        SocializeConfig.setSelectedPlatfrom(SHARE_MEDIA.QZONE);
        if (TextUtils.isEmpty(this.mAppID)) {
            this.mAppID = (String) OauthHelper.getAppIdAndAppkey(this.mContext).get("appid");
            this.mAppKey = (String) OauthHelper.getAppIdAndAppkey(this.mContext).get("appkey");
        }
        if (TextUtils.isEmpty(this.mAppID)) {
            getAppIdFromServer(new ObtainAppIdListener() {
                public void onComplete() {
                    QZoneSsoHandler.this.shareToQZone();
                }
            });
        } else {
            shareToQZone();
        }
    }

    private void showDialog() {
        Toast.makeText(this.mContext, "分享失败，详情见Logcat", 0).show();
        StringBuilder log = new StringBuilder();
        log.append("请添加QZone平台到SDK \n");
        log.append("添加方式：\nQZoneSsoHandler qZoneSsoHandler = new QZoneSsoHandler(getActivity(), " +
                "\"你的APP ID\",\"你的APP KEY\");\n");
        log.append("qZoneSsoHandler.addToSocialSDK();\n");
        log.append("参考文档：\nhttp://dev.umeng" +
                ".com/social/android/share/quick-integration#social_qzone_sso");
        Log.e(TAG, log.toString());
    }

    protected void sendReport(boolean flag) {
        UMShareMsg shareMsg = getShareMsg();
        SocializeUtils.sendAnalytic(this.mContext, mEntity.mDescriptor, shareMsg.mText, shareMsg
                .getMedia(), "qzone");
        try {
            StatisticsDataUtils.addStatisticsData(this.mContext, SHARE_MEDIA.QZONE, 25);
        } catch (Exception e) {
        }
    }

    private UMAuthListener createAuthListener(final Bundle bundle, final UMImage image) {
        return new UMAuthListener() {
            public void onStart(SHARE_MEDIA platform) {
            }

            public void onError(SocializeException e, SHARE_MEDIA platform) {
            }

            public void onComplete(Bundle value, SHARE_MEDIA platform) {
                if (value != null && value.containsKey(SocializeProtocolConstants
                        .PROTOCOL_KEY_UID)) {
                    QZoneSsoHandler.this.getBitmapUrl(image, value.getString
                            (SocializeProtocolConstants.PROTOCOL_KEY_UID), new
                            ObtainImageUrlListener() {
                        public void onComplete(String path) {
                            if (TextUtils.isEmpty(path)) {
                                QZoneSsoHandler.this.defaultQZoneShare(bundle);
                                UMediaObject mediaObject = QZoneSsoHandler.this.getShareMsg()
                                        .getMedia();
                                int type = bundle.getInt("req_type");
                                if (mediaObject == null) {
                                    return;
                                }
                                if (mediaObject.getMediaType() == MediaType.VEDIO || mediaObject
                                        .getMediaType() == MediaType.MUSIC || type == 1) {
                                    Log.e(QZoneSsoHandler.TAG,
                                            "QQ空间上传图片失败将导致无客户端分享失败，请设置缩略图为url类型或者较小的本地图片...");
                                    return;
                                }
                                return;
                            }
                            ArrayList<String> paths = new ArrayList();
                            bundle.remove("imageUrl");
                            paths.add(path);
                            bundle.putStringArrayList("imageUrl", paths);
                            QZoneSsoHandler.this.defaultQZoneShare(bundle);
                        }
                    });
                }
            }

            public void onCancel(SHARE_MEDIA platform) {
            }
        };
    }
}
