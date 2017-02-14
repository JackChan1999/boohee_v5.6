package com.umeng.socialize.weixin.controller;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.widget.Toast;

import com.boohee.status.UserTimelineActivity;
import com.boohee.utils.Utils;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.modelmsg.SendAuth.Resp;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX.Req;
import com.tencent.mm.sdk.modelmsg.WXEmojiObject;
import com.tencent.mm.sdk.modelmsg.WXImageObject;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXMusicObject;
import com.tencent.mm.sdk.modelmsg.WXTextObject;
import com.tencent.mm.sdk.modelmsg.WXVideoObject;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.umeng.socialize.bean.CustomPlatform;
import com.umeng.socialize.bean.HandlerRequestCode;
import com.umeng.socialize.bean.Language;
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
import com.umeng.socialize.controller.listener.SocializeListeners.SocializeClientListener;
import com.umeng.socialize.controller.listener.SocializeListeners.UMAuthListener;
import com.umeng.socialize.controller.listener.SocializeListeners.UMDataListener;
import com.umeng.socialize.exception.SocializeException;
import com.umeng.socialize.media.BaseShareContent;
import com.umeng.socialize.media.UMEmoji;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMVideo;
import com.umeng.socialize.media.UMusic;
import com.umeng.socialize.net.utils.SocializeProtocolConstants;
import com.umeng.socialize.sso.UMSsoHandler;
import com.umeng.socialize.utils.BitmapUtils;
import com.umeng.socialize.utils.DeviceConfig;
import com.umeng.socialize.utils.Log;
import com.umeng.socialize.utils.OauthHelper;
import com.umeng.socialize.utils.SocializeUtils;
import com.umeng.socialize.utils.StatisticsDataUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class UMWXHandler extends UMSsoHandler {
    private static final String DEFAULT_TITLE   = "分享到";
    private static final String TYPE_EMOJI      = "emoji";
    private static final String TYPE_IMAGE      = "image";
    private static final String TYPE_MUSIC      = "music";
    private static final String TYPE_TEXT       = "text";
    private static final String TYPE_TEXT_IMAGE = "text_image";
    private static final String TYPE_VIDEO      = "video";
    private static SocializeEntity    mEntity;
    private final  int                DESCRIPTION_LIMIT;
    private final  int                REFRESH_TOKEN_EXPIRES;
    private final  int                SHOW_COMPRESS_TOAST;
    private final  int                SHOW_TITLE_TOAST;
    private final  String             TAG;
    private final  int                THUMB_LIMIT;
    private final  int                THUMB_SIZE;
    private final  int                TITLE_LIMIT;
    private        int                WXCIRCLE_DEFAULT_ICON;
    private        String             WX_APPID;
    private        int                WX_DEFAULT_ICON;
    private        String             WX_SECRET;
    private final  Handler            handler;
    private        boolean            isFromAuth;
    private        boolean            isShowCompressToast;
    private        boolean            isToCircle;
    private        boolean            isWXSceneFavorite;
    private        UMAuthListener     mAuthListener;
    private        IWXAPIEventHandler mEventHandler;
    private        boolean            mRefreshTokenAvailable;
    private        String             mReportDesc;
    private        String             mShareType;
    private        IWXAPI             mWXApi;
    private        WXMediaMessage     mWxMediaMessage;

    @Deprecated
    public UMWXHandler(Context context, String appid) {
        this(context, appid, null);
        Log.w("UMWXHandler", "为了保证微信授权正常，请传递应用的secret");
    }

    public UMWXHandler(Context context, String appid, String secret) {
        this.THUMB_SIZE = 150;
        this.THUMB_LIMIT = 32768;
        this.TITLE_LIMIT = 512;
        this.DESCRIPTION_LIMIT = 1024;
        this.REFRESH_TOKEN_EXPIRES = 604800;
        this.WX_SECRET = "";
        this.WX_DEFAULT_ICON = 0;
        this.WXCIRCLE_DEFAULT_ICON = 0;
        this.isToCircle = false;
        this.mRefreshTokenAvailable = true;
        this.TAG = "UMWXHandler";
        this.SHOW_COMPRESS_TOAST = 1;
        this.SHOW_TITLE_TOAST = 2;
        this.isShowCompressToast = true;
        this.mReportDesc = "";
        this.mWxMediaMessage = null;
        this.isFromAuth = false;
        this.isWXSceneFavorite = false;
        this.handler = new 2 (this);
        this.mEventHandler = new 3 (this);
        this.mContext = context.getApplicationContext();
        this.WX_APPID = appid;
        if (TextUtils.isEmpty(this.WX_APPID)) {
            throw new NullPointerException("the weixin appid is null");
        }
        this.mWXApi = WXAPIFactory.createWXAPI(this.mContext, this.WX_APPID);
        this.mWXApi.registerApp(this.WX_APPID);
        this.WX_SECRET = secret;
        this.mExtraData.put(SocializeConstants.FIELD_WX_APPID, this.WX_APPID);
        if (TextUtils.isEmpty(this.WX_SECRET)) {
            this.WX_SECRET = "";
        }
        this.mExtraData.put(SocializeConstants.FIELD_WX_SECRET, this.WX_SECRET);
        if (this.WX_DEFAULT_ICON == 0 || this.WXCIRCLE_DEFAULT_ICON == 0) {
            this.WX_DEFAULT_ICON = ResContainer.getResourceId(context, ResType.DRAWABLE,
                    "umeng_socialize_wechat");
            this.WXCIRCLE_DEFAULT_ICON = ResContainer.getResourceId(context, ResType.DRAWABLE,
                    "umeng_socialize_wxcircle");
        }
    }

    public final CustomPlatform createNewPlatform() {
        int grayIcon;
        this.mCustomPlatform = new CustomPlatform(this.isToCircle ? SocialSNSHelper
                .SOCIALIZE_WEIXIN_CIRCLE_KEY : "weixin", this.isToCircle ? "朋友圈" : "微信", this
                .isToCircle ? this.WXCIRCLE_DEFAULT_ICON : this.WX_DEFAULT_ICON);
        if (this.isToCircle) {
            grayIcon = ResContainer.getResourceId(this.mContext, ResType.DRAWABLE,
                    "umeng_socialize_wxcircle_gray");
        } else {
            grayIcon = ResContainer.getResourceId(this.mContext, ResType.DRAWABLE,
                    "umeng_socialize_wechat_gray");
        }
        this.mCustomPlatform.mGrayIcon = grayIcon;
        this.mCustomPlatform.mClickListener = new 1 (this);
        return this.mCustomPlatform;
    }

    private void simplePaseData() {
        if (this.mShareMedia instanceof BaseShareContent) {
            BaseShareContent baseShareContent = this.mShareMedia;
            this.mShareContent = baseShareContent.getShareContent();
            this.mShareMedia = baseShareContent.getShareMedia();
            this.mTargetUrl = baseShareContent.getTargetUrl();
            this.mTitle = baseShareContent.getTitle();
        }
    }

    private void parseMediaType() {
        if (!TextUtils.isEmpty(this.mShareContent) && this.mShareMedia == null) {
            this.mShareType = "text";
        } else if (this.mShareMedia != null && (this.mShareMedia instanceof UMEmoji)) {
            this.mShareType = TYPE_EMOJI;
        } else if (TextUtils.isEmpty(this.mShareContent) && this.mShareMedia != null && (this
                .mShareMedia instanceof UMImage)) {
            this.mShareType = "image";
        } else if (this.mShareMedia != null && (this.mShareMedia instanceof UMusic)) {
            this.mShareType = TYPE_MUSIC;
        } else if (this.mShareMedia != null && (this.mShareMedia instanceof UMVideo)) {
            this.mShareType = TYPE_VIDEO;
        } else if (!TextUtils.isEmpty(this.mShareContent) && this.mShareMedia != null && (this
                .mShareMedia instanceof UMImage)) {
            this.mShareType = TYPE_TEXT_IMAGE;
        }
    }

    private WXMediaMessage buildEmojiParams() {
        UMEmoji emoji = this.mShareMedia;
        UMImage image = emoji.mSrcImage;
        String path = image.getImageCachePath();
        WXEmojiObject wxEmojiObject = new WXEmojiObject();
        if (emoji.mSrcImage.isUrlMedia()) {
            path = BitmapUtils.getFileName(image.toUrl());
            if (!new File(path).exists()) {
                BitmapUtils.loadImage(image.toUrl(), 150, 150);
            }
        }
        wxEmojiObject.emojiPath = path;
        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = wxEmojiObject;
        if (emoji.getThumbImage() != null) {
            msg.thumbData = emoji.mThumb.toByte();
        } else if (TextUtils.isEmpty(emoji.getThumb())) {
            msg.thumbData = emoji.mSrcImage.toByte();
        } else {
            Bitmap bitmap = BitmapUtils.loadImage(emoji.getThumb(), 150, 150);
            msg.thumbData = BitmapUtils.bitmap2Bytes(bitmap);
            bitmap.recycle();
        }
        msg.title = this.mTitle;
        msg.description = this.mShareContent;
        return msg;
    }

    private WXMediaMessage buildMusicParams() {
        parseMusic(this.mShareMedia);
        String musicUrl = (String) this.mExtraData.get("audio_url");
        WXMusicObject music = new WXMusicObject();
        music.musicUrl = this.mTargetUrl;
        music.musicDataUrl = musicUrl;
        WXMediaMessage msg = buildMediaMessage();
        msg.mediaObject = music;
        msg.title = this.mTitle;
        msg.description = this.mShareContent;
        msg.mediaObject = music;
        return msg;
    }

    private WXMediaMessage buildTextParams() {
        WXTextObject textObj = new WXTextObject();
        textObj.text = this.mShareContent;
        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = textObj;
        msg.description = this.mShareContent;
        msg.title = this.mTitle;
        return msg;
    }

    private WXMediaMessage buildImageParams() {
        parseImage(this.mShareMedia);
        WXImageObject imgObj = new WXImageObject();
        WXMediaMessage msg = buildMediaMessage();
        if (TextUtils.isEmpty((CharSequence) this.mExtraData.get("image_path_local"))) {
            imgObj.imageUrl = (String) this.mExtraData.get("image_path_url");
        } else {
            imgObj.imagePath = (String) this.mExtraData.get("image_path_local");
        }
        msg.mediaObject = imgObj;
        return msg;
    }

    private WXMediaMessage buildVideoParams() {
        parseVideo(this.mShareMedia);
        String videoUrl = (String) this.mExtraData.get("audio_url");
        WXVideoObject video = new WXVideoObject();
        video.videoUrl = videoUrl;
        WXMediaMessage msg = buildMediaMessage();
        msg.mediaObject = video;
        msg.title = this.mTitle;
        msg.description = this.mShareContent;
        return msg;
    }

    private WXMediaMessage buildTextImageParams() {
        parseImage(this.mShareMedia);
        if (TextUtils.isEmpty(this.mTargetUrl)) {
            this.mTargetUrl = SocializeConstants.SOCIAL_LINK;
        }
        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = this.mTargetUrl;
        WXMediaMessage msg = buildMediaMessage();
        msg.title = this.mTitle;
        msg.description = this.mShareContent;
        msg.mediaObject = webpage;
        return msg;
    }

    private WXMediaMessage buildMediaMessage() {
        String localPath = (String) this.mExtraData.get("image_path_local");
        String urlPath = (String) this.mExtraData.get("image_path_url");
        WXMediaMessage msg = new WXMediaMessage();
        if (!TextUtils.isEmpty(urlPath)) {
            msg.thumbData = BitmapUtils.bitmap2Bytes(BitmapUtils.loadImage(urlPath, 150, 150));
        } else if (!TextUtils.isEmpty(localPath)) {
            Bitmap thumb = getThumbFromCache(localPath);
            msg.thumbData = BitmapUtils.bitmap2Bytes(thumb);
            if (!(thumb == null || thumb.isRecycled())) {
                thumb.recycle();
            }
        }
        return msg;
    }

    private Bitmap getThumbFromCache(String imgPath) {
        if (!BitmapUtils.isFileExist(imgPath)) {
            return null;
        }
        if (BitmapUtils.isNeedScale(imgPath, 32768)) {
            return BitmapUtils.getBitmapFromFile(imgPath, 150, 150);
        }
        return BitmapUtils.getBitmapFromFile(imgPath);
    }

    private byte[] compressBitmap(byte[] datas, int byteCount) {
        boolean isFinish = false;
        if (datas != null && datas.length >= byteCount) {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            Bitmap tmpBitmap = BitmapFactory.decodeByteArray(datas, 0, datas.length);
            int times = 1;
            while (!isFinish && times <= 10) {
                int quality = (int) (100.0d * Math.pow(b. do,(double) times));
                Log.d("UMWXHandler", "quality = " + quality);
                tmpBitmap.compress(CompressFormat.JPEG, quality, outputStream);
                Log.d("UMWXHandler", "WeiXin Thumb Size = " + (outputStream.toByteArray().length
                        / 1024) + " KB");
                if (outputStream == null || outputStream.size() >= byteCount) {
                    outputStream.reset();
                    times++;
                } else {
                    isFinish = true;
                }
            }
            if (outputStream != null) {
                byte[] compress_datas = outputStream.toByteArray();
                if (!tmpBitmap.isRecycled()) {
                    tmpBitmap.recycle();
                }
                if (compress_datas == null || compress_datas.length > 0) {
                    return compress_datas;
                }
                Log.e("UMWXHandler", "### 您的原始图片太大,导致缩略图压缩过后还大于32KB,请将分享到微信的图片进行适当缩小.");
                return compress_datas;
            }
        }
        return datas;
    }

    protected boolean haveCallback(Context context) {
        if (this.mContext == null) {
            return false;
        }
        String callbackClassName = this.mContext.getPackageName() + ".wxapi.WXEntryActivity";
        try {
            Class.forName(callbackClassName);
            Log.d("UMWXHandler", "### 微信或微信朋友圈回调类地址 : " + callbackClassName);
            return true;
        } catch (ClassNotFoundException e) {
            Log.e("UMWXHandler", callbackClassName + " 类没有找到. 请把weixin文件夹中的wxapi目录拷贝到您的包目录下.");
            return false;
        }
    }

    public void setTitle(String title) {
        this.mTitle = title;
    }

    public void showCompressToast(boolean show) {
        this.isShowCompressToast = show;
    }

    protected void handleOnClick(CustomPlatform customPlatform, SocializeEntity entity,
                                 SnsPostListener listener) {
    }

    protected void handleOnClick(Context context, CustomPlatform customPlatform, SocializeEntity
            entity, SnsPostListener listener) {
        if (SocializeConstants.DEFAULTID.equals(this.WX_APPID)) {
            showDialog();
            return;
        }
        if (entity != null) {
            mEntity = entity;
            setSelectedPlatform();
            UMShareMsg shareMsg = entity.getShareMsg();
            if (shareMsg == null || mEntity.getShareType() != ShareType.SHAKE) {
                this.mShareContent = entity.getShareContent();
                this.mShareMedia = entity.getMedia();
            } else {
                this.mShareContent = shareMsg.mText;
                this.mShareMedia = shareMsg.getMedia();
            }
        }
        mEntity.setShareType(ShareType.NORMAL);
        this.mSocializeConfig.registerListener(listener);
        if (!isClientInstalled()) {
            Toast.makeText(this.mContext, "你还没有安装微信", 0).show();
        } else if (!this.mWXApi.isWXAppSupportAPI()) {
            Toast.makeText(this.mContext, "你安装的微信版本不支持当前API", 0).show();
        } else if (this.mShareMedia == null && TextUtils.isEmpty(this.mShareContent)) {
            Toast.makeText(this.mContext, "请设置分享内容...", 0).show();
        } else {
            simplePaseData();
            parseMediaType();
            if (this.mShareType == TYPE_EMOJI && this.isToCircle) {
                Toast.makeText(this.mContext, "微信朋友圈不支持表情分享...", 0).show();
            } else if (!TextUtils.isEmpty(this.mShareType)) {
                new LoadResourceTask(this, context, null).execute();
            }
        }
    }

    private void showDialog() {
        Toast.makeText(this.mContext, "分享失败，详情见Logcat", 0).show();
        StringBuilder log = new StringBuilder();
        if (this.isToCircle) {
            log.append("请添加朋友圈平台到SDK \n");
            log.append("添加方式：\nUMWXHandler wxCircleHandler = new UMWXHandler(getActivity()," +
                    "\"你的AppID\",\"你的AppSecret\");\n");
            log.append("wxCircleHandler.setToCircle(true);\n");
            log.append("wxCircleHandler.addToSocialSDK();\n");
            log.append("参考文档：\nhttp://dev.umeng" +
                    ".com/social/android/share/quick-integration#social_weixin");
        } else {
            log.append("请添加微信平台到SDK \n");
            log.append("添加方式：\nUMWXHandler wxHandler = new UMWXHandler(getActivity(),\"你的AppID\"," +
                    "\"你的AppSecret\");\n");
            log.append("wxHandler.addToSocialSDK();\n");
            log.append("参考文档：\nhttp://dev.umeng" +
                    ".com/social/android/share/quick-integration#social_weixin");
        }
        Log.e("UMWXHandler", log.toString());
    }

    public boolean isClientInstalled() {
        return this.mWXApi.isWXAppInstalled();
    }

    public boolean shareTo() {
        int i = 0;
        this.isFromAuth = false;
        this.mSocializeConfig.fireAllListenersOnStart(SnsPostListener.class);
        Req req = new Req();
        req.transaction = buildTransaction(this.mShareType);
        req.message = this.mWxMediaMessage;
        if (this.isWXSceneFavorite) {
            req.scene = 2;
        } else {
            if (this.isToCircle) {
                i = 1;
            }
            req.scene = i;
        }
        return this.mWXApi.sendReq(req);
    }

    protected void sendReport(boolean result) {
        int statusCode = StatusCode.ST_CODE_ERROR_WEIXIN;
        SHARE_MEDIA platform = this.isToCircle ? SHARE_MEDIA.WEIXIN_CIRCLE : SHARE_MEDIA.WEIXIN;
        if (result) {
            try {
                StatisticsDataUtils.addStatisticsData(this.mContext, platform, 17);
            } catch (Exception e) {
            }
            statusCode = 200;
        }
        if (!haveCallback(this.mContext) || isBlackList(this.mContext)) {
            this.mSocializeConfig.fireAllListenersOnComplete(SnsPostListener.class, platform,
                    statusCode, mEntity);
            if (result && mEntity != null) {
                SocializeUtils.sendAnalytic(this.mContext, mEntity.mDescriptor, this.mReportDesc,
                        this.mShareMedia, this.isToCircle ? "wxtimeline" : "wxsession");
            }
        }
    }

    private boolean isBlackList(Context context) {
        if (!this.isToCircle) {
            return false;
        }
        String packageName = "com.tencent.mm";
        String errorWechatVersion = "6.0.2.56";
        String versionCode = DeviceConfig.getAppVersion("com.tencent.mm", context);
        if (TextUtils.isEmpty(versionCode)) {
            return false;
        }
        return versionCode.startsWith("6.0.2.56");
    }

    private void dealOAuth(BaseResp resp) {
        if (resp.errCode == 0) {
            loadOauthData("https://api.weixin.qq.com/sns/oauth2/access_token?appid=" + this
                    .WX_APPID + "&secret=" + this.WX_SECRET + "&code=" + ((Resp) resp).code +
                    "&grant_type=authorization_code");
        } else if (resp.errCode == -2) {
            SHARE_MEDIA share_media;
            UMAuthListener uMAuthListener = this.mAuthListener;
            if (this.isToCircle) {
                share_media = SHARE_MEDIA.WEIXIN_CIRCLE;
            } else {
                share_media = SHARE_MEDIA.WEIXIN;
            }
            uMAuthListener.onCancel(share_media);
        } else {
            this.mAuthListener.onError(new SocializeException("aouth error! error code :" + resp
                    .errCode), this.isToCircle ? SHARE_MEDIA.WEIXIN_CIRCLE : SHARE_MEDIA.WEIXIN);
        }
    }

    public IWXAPIEventHandler getWXEventHandler() {
        return this.mEventHandler;
    }

    private void setSelectedPlatform() {
        if (this.isToCircle) {
            SocializeConfig.setSelectedPlatfrom(SHARE_MEDIA.WEIXIN_CIRCLE);
        } else {
            SocializeConfig.setSelectedPlatfrom(SHARE_MEDIA.WEIXIN);
        }
    }

    public IWXAPI getWXApi() {
        return this.mWXApi;
    }

    private String buildTransaction(String type) {
        if (type == null) {
            return String.valueOf(System.currentTimeMillis());
        }
        return type + System.currentTimeMillis();
    }

    public UMWXHandler setToCircle(boolean toCircle) {
        this.isToCircle = toCircle;
        return this;
    }

    public int getRequstCode() {
        return this.isToCircle ? HandlerRequestCode.WX_CIRCLE_REQUEST_CODE : 10086;
    }

    public void authorize(Activity act, UMAuthListener listener) {
        if (TextUtils.isEmpty(this.WX_APPID) || TextUtils.isEmpty(this.WX_SECRET)) {
            Log.e("UMWXHandler", "Appid或者App secret为空，不能授权。请设置正确地Appid跟App Secret");
            return;
        }
        this.mAuthListener = listener;
        this.mAuthListener.onStart(this.isToCircle ? SHARE_MEDIA.WEIXIN_CIRCLE : SHARE_MEDIA
                .WEIXIN);
        this.isFromAuth = true;
        boolean isNotExpired = OauthHelper.isRefreshTokenNotExpired(act, SHARE_MEDIA.WEIXIN);
        if (!isNotExpired) {
            isNotExpired = OauthHelper.isRefreshTokenNotExpired(act, SHARE_MEDIA.WEIXIN_CIRCLE);
        }
        if (isNotExpired && this.mRefreshTokenAvailable) {
            String refreshToken = OauthHelper.getRefreshToken(act, SHARE_MEDIA.WEIXIN);
            if (TextUtils.isEmpty(refreshToken)) {
                refreshToken = OauthHelper.getRefreshToken(act, SHARE_MEDIA.WEIXIN_CIRCLE);
            }
            loadOauthData("https://api.weixin.qq.com/sns/oauth2/refresh_token?appid=" + this
                    .WX_APPID + "&grant_type=refresh_token&refresh_token=" + refreshToken);
            return;
        }
        SendAuth.Req req = new SendAuth.Req();
        req.scope = "snsapi_userinfo,snsapi_friend,snsapi_message";
        req.state = "none";
        this.mWXApi.sendReq(req);
    }

    public void authorizeCallBack(int requestCode, int resultCode, Intent data) {
    }

    private void loadOauthData(String url) {
        new 4 (this, url).execute();
    }

    private Bundle parseAuthData(String response) {
        Bundle bundle = new Bundle();
        if (!TextUtils.isEmpty(response)) {
            try {
                JSONObject jsonObject = new JSONObject(response);
                Iterator<String> iterator = jsonObject.keys();
                String key = "";
                while (iterator.hasNext()) {
                    key = (String) iterator.next();
                    bundle.putString(key, jsonObject.optString(key));
                }
                bundle.putString(SocializeProtocolConstants.PROTOCOL_KEY_UID, bundle.getString
                        ("openid"));
                bundle.putLong("refresh_token_expires", 604800);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return bundle;
    }

    public void setRefreshTokenAvailable(boolean available) {
        this.mRefreshTokenAvailable = available;
    }

    public void setSecret(String secret) {
        this.WX_SECRET = secret;
    }

    public void getUserInfo(UMDataListener listener) {
        String uid = OauthHelper.getUsid(this.mContext, SHARE_MEDIA.WEIXIN);
        if (TextUtils.isEmpty(uid)) {
            uid = OauthHelper.getUsid(this.mContext, SHARE_MEDIA.WEIXIN_CIRCLE);
        }
        String accessToken = OauthHelper.getAccessToken(this.mContext, SHARE_MEDIA.WEIXIN)[0];
        if (TextUtils.isEmpty(accessToken)) {
            accessToken = OauthHelper.getAccessToken(this.mContext, SHARE_MEDIA.WEIXIN_CIRCLE)[0];
        }
        if (TextUtils.isEmpty(uid) || TextUtils.isEmpty(accessToken)) {
            Log.e("UMWXHandler", "please check had authed...");
            listener.onComplete(StatusCode.ST_CODE_ERROR, null);
            return;
        }
        StringBuilder builder = new StringBuilder();
        builder.append("https://api.weixin.qq.com/sns/userinfo?access_token=");
        builder.append(accessToken).append("&openid=").append(uid);
        if (SocializeConfig.getSocializeConfig().getWechatUserInfoLanguage() == Language.ZH) {
            builder.append("&lang=zh_CN");
        }
        new 5 (this, builder, listener).execute();
    }

    private Map<String, Object> parseUserInfo(String result) {
        Map<String, Object> map = new HashMap();
        try {
            JSONObject jsonObject = new JSONObject(result);
            if (jsonObject.has(Utils.RESPONSE_ERRCODE)) {
                Log.e("UMWXHandler", result + "");
                map.put(Utils.RESPONSE_ERRCODE, jsonObject.getString(Utils.RESPONSE_ERRCODE));
                return map;
            }
            map.put("openid", jsonObject.opt("openid"));
            map.put(UserTimelineActivity.NICK_NAME, jsonObject.opt(UserTimelineActivity.NICK_NAME));
            map.put("language", jsonObject.opt("language"));
            map.put("city", jsonObject.opt("city"));
            map.put("province", jsonObject.opt("province"));
            map.put("country", jsonObject.opt("country"));
            map.put("headimgurl", jsonObject.opt("headimgurl"));
            map.put("unionid", jsonObject.opt("unionid"));
            map.put("sex", jsonObject.opt("sex"));
            JSONArray jsonArray = jsonObject.getJSONArray("privilege");
            int len = jsonArray.length();
            if (len <= 0) {
                return map;
            }
            String[] privileges = new String[len];
            for (int i = 0; i < len; i++) {
                privileges[i] = jsonArray.get(i).toString();
            }
            map.put("privilege", privileges);
            return map;
        } catch (JSONException e) {
            e.printStackTrace();
            return Collections.emptyMap();
        }
    }

    public void setWXSceneFavorite(boolean flag) {
        this.isWXSceneFavorite = flag;
    }

    public void deleteAuthorization(SocializeEntity entity, SHARE_MEDIA platform, SocializeClientListener listener) {
        OauthHelper.remove(this.mContext, platform);
        if (listener != null) {
            listener.onStart();
            listener.onComplete(200, entity);
        }
    }
}
