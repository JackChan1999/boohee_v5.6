package com.umeng.socialize.sso;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.TextView;
import android.widget.Toast;

import com.boohee.one.BuildConfig;
import com.boohee.status.UserTimelineActivity;
import com.tencent.connect.UserInfo;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
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
import com.umeng.socialize.common.SocialSNSHelper;
import com.umeng.socialize.common.SocializeConstants;
import com.umeng.socialize.controller.listener.SocializeListeners.SnsPostListener;
import com.umeng.socialize.controller.listener.SocializeListeners.UMAuthListener;
import com.umeng.socialize.controller.listener.SocializeListeners.UMDataListener;
import com.umeng.socialize.exception.SocializeException;
import com.umeng.socialize.media.QQShareContent;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMVideo;
import com.umeng.socialize.media.UMusic;
import com.umeng.socialize.net.utils.SocializeProtocolConstants;
import com.umeng.socialize.sso.UMTencentSsoHandler.ObtainImageUrlListener;
import com.umeng.socialize.utils.BitmapUtils;
import com.umeng.socialize.utils.Log;
import com.umeng.socialize.utils.OauthHelper;
import com.umeng.socialize.utils.SocializeUtils;
import com.umeng.socialize.utils.StatisticsDataUtils;
import com.umeng.socialize.view.ShareActivity;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

public class UMQQSsoHandler extends UMTencentSsoHandler {
    private static final String  TAG                 = "UMQQSsoHandler";
    private              boolean GOTO_SHARE_ACTIVITY = false;
    private Activity mActivity;
    private Bundle   mParams;
    private int mShareType = 1;

    public UMQQSsoHandler(Activity activity, String appId, String appKey) {
        super(activity, appId, appKey);
        this.mActivity = activity;
    }

    protected void initResource() {
        this.mKeyWord = SocialSNSHelper.SOCIALIZE_QQ_KEY;
        this.mShowWord = ResContainer.getString(this.mContext, "umeng_socialize_text_qq_key");
        this.mIcon = ResContainer.getResourceId(this.mContext, ResType.DRAWABLE,
                "umeng_socialize_qq_on");
        this.mGrayIcon = ResContainer.getResourceId(this.mContext, ResType.DRAWABLE,
                "umeng_socialize_qq_off");
    }

    protected void handleOnClick(CustomPlatform customPlatform, SocializeEntity entity,
                                 SnsPostListener listener) {
        if (SocializeConstants.DEFAULTID.equals(this.mAppID)) {
            showDialog();
            return;
        }
        this.mSocializeConfig.registerListener(listener);
        this.isShareAfterAuthorize = true;
        SocializeConfig.setSelectedPlatfrom(SHARE_MEDIA.QQ);
        this.mShareType = 1;
        if (entity != null) {
            mEntity = entity;
            UMShareMsg shareMsg = mEntity.getShareMsg();
            if (shareMsg == null || mEntity.getShareType() != ShareType.SHAKE) {
                this.mShareContent = entity.getShareContent();
                this.mShareMedia = entity.getMedia();
            } else {
                this.mShareContent = shareMsg.mText;
                this.mShareMedia = shareMsg.getMedia();
            }
        }
        setShareContent();
        String[] authData = OauthHelper.getAccessTokenForQQ(this.mContext);
        ObtainAppIdListener obtainAppIdListener = new ObtainAppIdListener() {
            public void onComplete() {
                if (UMQQSsoHandler.this.initTencent()) {
                    UMQQSsoHandler.this.gotoShare();
                }
            }
        };
        if (authData != null) {
            if (TextUtils.isEmpty(this.mAppID)) {
                this.mAppID = (String) OauthHelper.getAppIdAndAppkey(this.mContext).get("appid");
                this.mAppKey = (String) OauthHelper.getAppIdAndAppkey(this.mContext).get("appkey");
            }
            if (TextUtils.isEmpty(this.mAppID)) {
                getAppIdFromServer(obtainAppIdListener);
                return;
            }
            this.mTencent = Tencent.createInstance(this.mAppID, this.mContext);
            this.mTencent.setOpenId(authData[1]);
            this.mTencent.setAccessToken(authData[0], authData[2]);
            gotoShare();
        } else if (TextUtils.isEmpty(this.mAppID)) {
            getAppIdFromServer(obtainAppIdListener);
        } else if (initTencent()) {
            gotoShare();
        }
    }

    private void showDialog() {
        Builder builder = new Builder(this.mContext);
        TextView title = new TextView(this.mContext);
        title.setText("分享失败原因");
        title.setPadding(0, 20, 0, 20);
        title.setTextColor(-1);
        title.setGravity(17);
        title.setTextSize(16.0f);
        builder.setCustomTitle(title);
        TextView textView = new TextView(this.mContext);
        textView.setText("请添加QQ平台到SDK \n添加方式：\nUMQQSsoHandler qqSsoHandler = new UMQQSsoHandler" +
                "(getActivity(), \"你的APP ID\",\"你的APP KEY\");\nqqSsoHandler.addToSocialSDK(); " +
                "\n参考文档：\nhttp://dev.umeng" +
                ".com/social/android/share/quick-integration#social_qq_sso");
        textView.setTextColor(-1);
        textView.setTextSize(16.0f);
        textView.setAutoLinkMask(1);
        builder.setView(textView);
        builder.show().show();
    }

    public boolean shareTo() {
        shareToQQ();
        return true;
    }

    public void authorize(Activity act, UMAuthListener listener) {
        this.mAuthListener = listener;
        setActivity(act);
        if (TextUtils.isEmpty(this.mAppID)) {
            this.mAppID = (String) OauthHelper.getAppIdAndAppkey(this.mContext).get("appid");
            this.mAppKey = (String) OauthHelper.getAppIdAndAppkey(this.mContext).get("appkey");
        }
        if (TextUtils.isEmpty(this.mAppID)) {
            getAppIdFromServer(new ObtainAppIdListener() {
                public void onComplete() {
                    UMQQSsoHandler.this.loginDeal();
                }
            });
        } else {
            loginDeal();
        }
    }

    private void loginDeal() {
        if (validTencent()) {
            this.mTencent.logout(this.mContext);
        } else if ((this.mTencent == null || TextUtils.isEmpty(this.mTencent.getAppId())) &&
                !initTencent()) {
            return;
        }
        if (mEntity != null) {
            mEntity.addStatisticsData(this.mContext, SHARE_MEDIA.QQ, 3);
        }
        Log.i(TAG, "QQ oauth login...");
        this.mTencent.login(this.mActivity, BuildConfig.PLATFORM, new IUiListener() {
            public void onError(UiError e) {
                if (e != null) {
                    Log.d(UMQQSsoHandler.TAG, "授权失败! ==> errorCode = " + e.errorCode + ", " +
                            "errorMsg = " + e.errorMessage + ", detail = " + e.errorDetail);
                }
                SocializeUtils.safeCloseDialog(UMQQSsoHandler.this.mProgressDialog);
                UMQQSsoHandler.this.mAuthListener.onError(new SocializeException(e.errorCode, e
                        .errorDetail), SHARE_MEDIA.QQ);
                if (UMSsoHandler.mEntity != null) {
                    UMSsoHandler.mEntity.addOauthData(UMQQSsoHandler.this.mContext, SHARE_MEDIA
                            .QQ, 0);
                }
            }

            public void onCancel() {
                Log.d(UMQQSsoHandler.TAG, "cancel");
                SocializeUtils.safeCloseDialog(UMQQSsoHandler.this.mProgressDialog);
                UMQQSsoHandler.this.mAuthListener.onCancel(SHARE_MEDIA.QQ);
                if (UMSsoHandler.mEntity != null) {
                    UMSsoHandler.mEntity.addOauthData(UMQQSsoHandler.this.mContext, SHARE_MEDIA
                            .QQ, 0);
                }
            }

            public void onComplete(Object response) {
                SocializeUtils.safeCloseDialog(UMQQSsoHandler.this.mProgressDialog);
                Bundle values = UMQQSsoHandler.this.parseOauthData(response);
                if (values == null || values.getInt("ret") != 0) {
                    if (UMSsoHandler.mEntity != null) {
                        UMSsoHandler.mEntity.addOauthData(UMQQSsoHandler.this.mContext,
                                SHARE_MEDIA.QQ, 0);
                    }
                    UMQQSsoHandler.this.mAuthListener.onComplete(null, SHARE_MEDIA.QQ);
                    return;
                }
                if (UMSsoHandler.mEntity != null) {
                    UMSsoHandler.mEntity.addOauthData(UMQQSsoHandler.this.mContext, SHARE_MEDIA
                            .QQ, 1);
                }
                UMQQSsoHandler.this.uploadToken(UMQQSsoHandler.this.mContext, response,
                        UMQQSsoHandler.this.mAuthListener);
            }
        });
    }

    private void setShareContent() {
        if (this.mShareMedia instanceof QQShareContent) {
            QQShareContent qqShareContent = this.mShareMedia;
            this.mShareContent = qqShareContent.getShareContent();
            this.mTargetUrl = qqShareContent.getTargetUrl();
            this.mTitle = qqShareContent.getTitle();
            this.mShareMedia = qqShareContent.getShareMedia();
        }
    }

    public void canOpenShareActivity(boolean val) {
        this.GOTO_SHARE_ACTIVITY = val;
    }

    private void gotoShare() {
        if (this.GOTO_SHARE_ACTIVITY) {
            Activity activity = this.mActivity;
            if (activity == null) {
                Log.e(TAG, "Activity is null");
                return;
            }
            SocializeUtils.safeCloseDialog(this.mProgressDialog);
            Intent intent = new Intent(activity, ShareActivity.class);
            intent.putExtra("QQ-SSO", true);
            intent.putExtra("sns", SHARE_MEDIA.QQ.toString());
            if (!(mEntity == null || TextUtils.isEmpty(mEntity.mEntityKey))) {
                intent.putExtra(SocializeProtocolConstants.PROTOCOL_KEY_DESCRIPTOR, mEntity
                        .mEntityKey);
            }
            activity.startActivity(intent);
            return;
        }
        shareTo();
    }

    public void shareToQQ() {
        if (validTencent()) {
            this.mSocializeConfig.fireAllListenersOnStart(SnsPostListener.class);
            String path = (String) this.mExtraData.get("image_path_local");
            if (isLoadImageAsync()) {
                loadImage(this.mContext, (String) this.mExtraData.get("image_path_url"));
                return;
            } else if (isUploadImageAsync(path, this.mShareType)) {
                UMImage image = new UMImage(this.mContext, new File(path));
                Log.w(TAG, "未安装QQ客户端的情况下，QQ不支持音频，图文是为本地图片的分享。此时将上传本地图片到相册，请确保在QQ互联申请了upload_pic权限" +
                        ".");
                authorize(this.mActivity, createUploadAuthListener(image));
                return;
            } else {
                defaultShareToQQ();
                return;
            }
        }
        Log.d(TAG, "QQ平台还没有授权");
        createAuthListener();
        authorize(this.mActivity, this.mAuthListener);
    }

    public boolean isClientInstalled() {
        return this.mTencent.isSupportSSOLogin(this.mActivity);
    }

    private void defaultShareToQQ() {
        SocializeUtils.safeCloseDialog(this.mProgressDialog);
        buildParams();
        Log.d(TAG, "invoke Tencent.shareToQQ method...");
        this.mTencent.shareToQQ(this.mActivity, this.mParams, new IUiListener() {
            public void onError(UiError e) {
                Log.e(UMQQSsoHandler.TAG, "分享失败! ==> errorCode = " + e.errorCode + ", errorMsg = " +
                        "" + e.errorMessage + ", detail = " + e.errorDetail);
                UMQQSsoHandler.this.mSocializeConfig.fireAllListenersOnComplete(SnsPostListener
                        .class, SHARE_MEDIA.QQ, StatusCode.ST_CODE_ERROR, UMSsoHandler.mEntity);
            }

            public void onCancel() {
                UMQQSsoHandler.this.mSocializeConfig.fireAllListenersOnComplete(SnsPostListener
                        .class, SHARE_MEDIA.QQ, StatusCode.ST_CODE_ERROR_CANCEL, UMSsoHandler
                        .mEntity);
            }

            public void onComplete(Object response) {
                int status = StatusCode.ST_CODE_ERROR;
                if (UMQQSsoHandler.this.getResponseCode(response) == 0) {
                    status = 200;
                }
                UMQQSsoHandler.this.mSocializeConfig.fireAllListenersOnComplete(SnsPostListener
                        .class, SHARE_MEDIA.QQ, status, UMSsoHandler.mEntity);
                UMQQSsoHandler.this.sendReport(true);
            }
        });
        this.mParams = null;
        mEntity.setShareType(ShareType.NORMAL);
    }

    private boolean isLoadImageAsync() {
        String urlPath = (String) this.mExtraData.get("image_path_url");
        String localPath = (String) this.mExtraData.get("image_path_local");
        if (this.mShareType == 5 && isClientInstalled() && !TextUtils.isEmpty(urlPath) &&
                TextUtils.isEmpty(localPath)) {
            return true;
        }
        return false;
    }

    protected void sendReport(boolean result) {
        if (mEntity.mDescriptor != null) {
            SocializeUtils.sendAnalytic(this.mContext, mEntity.mDescriptor, this.mShareContent,
                    this.mShareMedia, SocialSNSHelper.SOCIALIZE_QQ_KEY);
        }
        try {
            StatisticsDataUtils.addStatisticsData(this.mContext, SHARE_MEDIA.QQ, 16);
        } catch (Exception e) {
        }
    }

    public void shareToQQ(String summary) {
        this.mShareContent = summary;
        shareTo();
    }

    private void buildParams() {
        this.mParams = new Bundle();
        this.mParams.putString("summary", this.mShareContent);
        if ((this.mShareMedia instanceof UMImage) && TextUtils.isEmpty(this.mShareContent)) {
            this.mShareType = 5;
            buildImageParams(this.mParams);
        } else if ((this.mShareMedia instanceof UMusic) || (this.mShareMedia instanceof UMVideo)) {
            this.mShareType = 2;
            buildAudioParams(this.mParams);
        } else {
            buildTextImageParams(this.mParams);
        }
        this.mParams.putInt("req_type", this.mShareType);
        if (TextUtils.isEmpty(this.mTitle)) {
            this.mTitle = "分享到QQ";
        }
        if (TextUtils.isEmpty(this.mTargetUrl)) {
            this.mTargetUrl = SocializeConstants.SOCIAL_LINK;
        }
        this.mParams.putString("targetUrl", this.mTargetUrl);
        this.mParams.putString("title", this.mTitle);
        this.mParams.putString("appName", getAppName());
    }

    private void buildImageParams(Bundle bundle) {
        parseImage(this.mShareMedia);
        String path = (String) this.mExtraData.get("image_path_local");
        String urlPath = (String) this.mExtraData.get("image_path_url");
        if (!TextUtils.isEmpty(path) && BitmapUtils.isFileExist(path)) {
            bundle.putString("imageLocalUrl", path);
        } else if (!TextUtils.isEmpty(urlPath)) {
            bundle.putString("imageUrl", urlPath);
        }
        if (!isClientInstalled()) {
            Log.w(TAG, "QQ不支持无客户端情况下纯图片分享...");
        }
    }

    private void buildTextImageParams(Bundle bundle) {
        buildImageParams(bundle);
    }

    private void buildAudioParams(Bundle bundle) {
        if (this.mShareMedia instanceof UMusic) {
            parseMusic(this.mShareMedia);
        } else if (this.mShareMedia instanceof UMVideo) {
            parseVideo(this.mShareMedia);
        }
        String path = (String) this.mExtraData.get("image_path_local");
        String urlPath = (String) this.mExtraData.get("image_path_url");
        if (!TextUtils.isEmpty(path) && BitmapUtils.isFileExist(path)) {
            bundle.putString("imageLocalUrl", path);
        } else if (!TextUtils.isEmpty(urlPath)) {
            bundle.putString("imageUrl", urlPath);
        }
        bundle.putString("audio_url", this.mShareMedia.toUrl());
    }

    public int getRequstCode() {
        return HandlerRequestCode.QQ_REQUEST_CODE;
    }

    public void setTitle(String title) {
        this.mTitle = title;
    }

    public void createAuthListener() {
        this.mAuthListener = new UMAuthListener() {
            public void onStart(SHARE_MEDIA platform) {
            }

            public void onError(SocializeException e, SHARE_MEDIA platform) {
            }

            public void onComplete(Bundle value, SHARE_MEDIA platform) {
                if (TextUtils.isEmpty(value.getString(SocializeProtocolConstants
                        .PROTOCOL_KEY_UID))) {
                    Toast.makeText(UMQQSsoHandler.this.mContext, "授权失败", 0).show();
                } else {
                    UMQQSsoHandler.this.gotoShare();
                }
            }

            public void onCancel(SHARE_MEDIA platform) {
            }
        };
    }

    private void loadImage(Context context, final String imageUrlPath) {
        new DialogAsyncTask<Void>(context, "") {
            protected Void doInBackground() {
                BitmapUtils.getBitmapFromFile(imageUrlPath);
                return null;
            }

            protected void onPostExecute(Void result) {
                super.onPostExecute(result);
                SocializeUtils.safeCloseDialog(UMQQSsoHandler.this.mProgressDialog);
                UMQQSsoHandler.this.mParams.putString("imageLocalUrl", BitmapUtils.getFileName
                        (imageUrlPath));
                UMQQSsoHandler.this.mParams.remove("imageUrl");
                UMQQSsoHandler.this.defaultShareToQQ();
            }
        }.execute();
    }

    private UMAuthListener createUploadAuthListener(final UMImage image) {
        return new UMAuthListener() {
            public void onStart(SHARE_MEDIA platform) {
            }

            public void onError(SocializeException e, SHARE_MEDIA platform) {
            }

            public void onComplete(Bundle value, SHARE_MEDIA platform) {
                if (value != null && value.containsKey(SocializeProtocolConstants
                        .PROTOCOL_KEY_UID)) {
                    UMQQSsoHandler.this.getBitmapUrl(image, value.getString
                            (SocializeProtocolConstants.PROTOCOL_KEY_UID), new
                            ObtainImageUrlListener() {
                        public void onComplete(String path) {
                            UMQQSsoHandler.this.mParams.putString("imageUrl", path);
                            UMQQSsoHandler.this.mParams.remove("imageLocalUrl");
                            UMQQSsoHandler.this.defaultShareToQQ();
                        }
                    });
                }
            }

            public void onCancel(SHARE_MEDIA platform) {
            }
        };
    }

    public void getUserInfo(final UMDataListener listener) {
        listener.onStart();
        if (this.mTencent == null) {
            listener.onComplete(StatusCode.ST_CODE_SDK_NO_OAUTH, null);
        } else {
            new UserInfo(this.mContext, this.mTencent.getQQToken()).getUserInfo(new IUiListener() {
                public void onCancel() {
                    listener.onComplete(StatusCode.ST_CODE_ERROR_CANCEL, null);
                }

                public void onComplete(Object arg) {
                    if (arg == null) {
                        listener.onComplete(StatusCode.ST_CODE_ERROR, null);
                        return;
                    }
                    try {
                        JSONObject jsonObject = new JSONObject(arg.toString());
                        Map<String, Object> infos = new HashMap();
                        infos.put("screen_name", jsonObject.optString(UserTimelineActivity
                                .NICK_NAME));
                        infos.put(SocializeProtocolConstants.PROTOCOL_KEY_GENDER, jsonObject
                                .optString(SocializeProtocolConstants.PROTOCOL_KEY_GENDER));
                        infos.put(SocializeProtocolConstants.PROTOCOL_KEY_FRIENDS_ICON,
                                jsonObject.optString("figureurl_qq_2"));
                        infos.put("is_yellow_year_vip", jsonObject.optString("is_yellow_year_vip"));
                        infos.put("yellow_vip_level", jsonObject.optString("yellow_vip_level"));
                        infos.put("msg", jsonObject.optString("msg"));
                        infos.put("city", jsonObject.optString("city"));
                        infos.put("vip", jsonObject.optString("vip"));
                        infos.put("level", jsonObject.optString("level"));
                        infos.put("province", jsonObject.optString("province"));
                        infos.put("is_yellow_vip", jsonObject.optString("is_yellow_vip"));
                        infos.put("openid", OauthHelper.getUsid(UMQQSsoHandler.this.mContext,
                                SHARE_MEDIA.QQ));
                        listener.onComplete(200, infos);
                    } catch (JSONException e) {
                        listener.onComplete(StatusCode.ST_CODE_ERROR, null);
                    }
                }

                public void onError(UiError arg0) {
                    listener.onComplete(StatusCode.ST_CODE_ERROR, null);
                }
            });
        }
    }
}
