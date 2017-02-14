package com.umeng.socialize.sso;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import com.boohee.utility.TimeLinePatterns;
import com.tencent.connect.common.Constants;
import com.tencent.tauth.Tencent;
import com.umeng.socialize.bean.CustomPlatform;
import com.umeng.socialize.bean.RequestType;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.SNSPair;
import com.umeng.socialize.bean.SocializeConfig;
import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.bean.UMToken;
import com.umeng.socialize.common.ResContainer;
import com.umeng.socialize.common.ResContainer.ResType;
import com.umeng.socialize.common.SocialSNSHelper;
import com.umeng.socialize.common.SocializeConstants;
import com.umeng.socialize.common.UMAsyncTask;
import com.umeng.socialize.controller.impl.BaseController;
import com.umeng.socialize.controller.impl.InitializeController;
import com.umeng.socialize.controller.listener.SocializeListeners.OnSnsPlatformClickListener;
import com.umeng.socialize.controller.listener.SocializeListeners.SnsPostListener;
import com.umeng.socialize.controller.listener.SocializeListeners.UMAuthListener;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMediaObject;
import com.umeng.socialize.net.GetPlatformKeyResponse;
import com.umeng.socialize.net.utils.SocializeProtocolConstants;
import com.umeng.socialize.utils.DeviceConfig;
import com.umeng.socialize.utils.Log;
import com.umeng.socialize.utils.OauthHelper;
import com.umeng.socialize.utils.SocializeUtils;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

public abstract class UMTencentSsoHandler extends UMSsoHandler {
    private static final String              PUBLIC_ACCOUNT = "100424468";
    private static final String              TAG            = UMTencentSsoHandler.class.getName();
    protected static     Map<String, String> mImageCache    = new HashMap();
    protected String         mAppID;
    protected String         mAppKey;
    protected UMAuthListener mAuthListener;
    protected int            mGrayIcon;
    protected int            mIcon;
    protected String mImageUrl = null;
    protected String mKeyWord;
    protected ProgressDialog mProgressDialog = null;
    protected String mShowWord;
    protected SocializeConfig mSocializeConfig = SocializeConfig.getSocializeConfig();
    protected Tencent mTencent;

    protected interface ObtainAppIdListener {
        void onComplete();
    }

    public interface ObtainImageUrlListener {
        void onComplete(String str);
    }

    static abstract class DialogAsyncTask<T> extends UMAsyncTask<T> {
        private ProgressDialog dialog = null;
        private String message;

        public DialogAsyncTask(Context context, String message) {
            setShowMessage(message);
            if (context instanceof Activity) {
                this.dialog = buildDialog((Activity) context, this.message);
            }
        }

        protected void onPreExecute() {
            super.onPreExecute();
            SocializeUtils.safeShowDialog(this.dialog);
        }

        protected void onPostExecute(T response) {
            super.onPostExecute(response);
            SocializeUtils.safeCloseDialog(this.dialog);
        }

        public void setShowMessage(String message) {
            this.message = message;
        }

        private ProgressDialog buildDialog(Activity activity, String message) {
            Context appCtx = activity.getApplicationContext();
            int theme = ResContainer.getResourceId(appCtx, ResType.STYLE, "Theme.UMDialog");
            if (TextUtils.isEmpty(message)) {
                message = appCtx.getString(ResContainer.getResourceId(appCtx, ResType.STRING,
                        SocializeConfig.getSelectedPlatfrom() == SHARE_MEDIA.QZONE ?
                                "umeng_socialize_text_waitting_qzone" :
                                "umeng_socialize_text_waitting_qq"));
            }
            this.dialog = new ProgressDialog(activity, theme);
            this.dialog.setMessage(message);
            return this.dialog;
        }
    }

    protected abstract void initResource();

    public UMTencentSsoHandler(Activity activity, String appId, String appKey) {
        super(activity);
        if (activity == null) {
            Log.e(TAG, "传入的activity为null，请传递一个非空Activity对象");
            return;
        }
        if (TextUtils.isEmpty(appKey)) {
            Log.e(TAG, "传递的APP KEY无效，请传一个有效的APP KEY");
        }
        if (TextUtils.isEmpty(appId)) {
            Log.e(TAG, "传递的APP ID无效，请传一个有效的APP ID");
        }
        this.mContext = activity.getApplicationContext();
        this.mAppID = appId;
        this.mAppKey = appKey;
        saveAppIDAndAppKey();
        this.mExtraData.put(SocializeConstants.FIELD_QZONE_ID, appId);
        this.mExtraData.put("qzone_secret", appKey);
    }

    private void saveAppIDAndAppKey() {
        if (!TextUtils.isEmpty(this.mAppID) && !TextUtils.isEmpty(this.mAppKey)) {
            OauthHelper.saveAppidAndAppkey(this.mContext, this.mAppID, this.mAppKey);
        }
    }

    protected CustomPlatform createNewPlatform() {
        initResource();
        this.mCustomPlatform = new CustomPlatform(this.mKeyWord, this.mShowWord, this.mIcon);
        this.mCustomPlatform.mGrayIcon = this.mGrayIcon;
        this.mCustomPlatform.mClickListener = new OnSnsPlatformClickListener() {
            public void onClick(Context context, SocializeEntity entity, SnsPostListener listener) {
                UMTencentSsoHandler.this.handleOnClick(UMTencentSsoHandler.this.mCustomPlatform,
                        entity, listener);
            }
        };
        return this.mCustomPlatform;
    }

    public void setAppId(String appid) {
        if (TextUtils.isEmpty(appid)) {
            Log.w(TAG, "your appid is null...");
        } else {
            this.mAppID = appid;
        }
    }

    protected void getAppIdFromServer(final ObtainAppIdListener listener) {
        if (DeviceConfig.isNetworkAvailable(this.mContext)) {
            new DialogAsyncTask<GetPlatformKeyResponse>(this.mContext, "获取AppID中...") {
                protected GetPlatformKeyResponse doInBackground() {
                    return new BaseController(new SocializeEntity("com.umeng.qq.sso", RequestType
                            .SOCIAL)).getPlatformKeys(UMTencentSsoHandler.this.mContext);
                }

                protected void onPostExecute(GetPlatformKeyResponse response) {
                    super.onPostExecute(response);
                    if (response == null || response.mData == null) {
                        Log.e(UMTencentSsoHandler.TAG, "obtain appId failed,public account " +
                                "share...");
                        UMTencentSsoHandler.this.mAppID = UMTencentSsoHandler.PUBLIC_ACCOUNT;
                        listener.onComplete();
                        return;
                    }
                    UMTencentSsoHandler.this.mAppID = (String) response.mData.get("qzone");
                    if (response.mSecrets != null) {
                        UMTencentSsoHandler.this.mAppKey = (String) response.mSecrets.get("qzone");
                    }
                    SocializeUtils.savePlatformKey(UMTencentSsoHandler.this.mContext, response
                            .mData);
                    OauthHelper.saveAppidAndAppkey(UMTencentSsoHandler.this.mContext,
                            UMTencentSsoHandler.this.mAppID, UMTencentSsoHandler.this.mAppKey);
                    if (listener != null) {
                        listener.onComplete();
                    }
                }
            }.execute();
        } else {
            Toast.makeText(this.mContext, "您的网络不可用,请检查网络连接...", 0).show();
        }
    }

    protected Bundle parseOauthData(Object response) {
        Bundle bundle = new Bundle();
        if (response != null) {
            String jsonStr = response.toString().trim();
            if (!TextUtils.isEmpty(jsonStr)) {
                JSONObject json = null;
                try {
                    json = new JSONObject(jsonStr);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (json != null) {
                    bundle.putString("auth_time", json.optString("auth_time", ""));
                    bundle.putString("pay_token", json.optString("pay_token", ""));
                    bundle.putString(Constants.PARAM_PLATFORM_ID, json.optString(Constants
                            .PARAM_PLATFORM_ID, ""));
                    bundle.putInt("ret", json.optInt("ret", -1));
                    bundle.putString("sendinstall", json.optString("sendinstall", ""));
                    bundle.putString("page_type", json.optString("page_type", ""));
                    bundle.putString("appid", json.optString("appid", ""));
                    bundle.putString("openid", json.optString("openid", ""));
                    bundle.putString(SocializeProtocolConstants.PROTOCOL_KEY_UID, json.optString
                            ("openid", ""));
                    bundle.putString("expires_in", json.optString("expires_in", ""));
                    bundle.putString("pfkey", json.optString("pfkey", ""));
                    bundle.putString("access_token", json.optString("access_token", ""));
                }
            }
        }
        return bundle;
    }

    protected UMToken buildUmToken(Object response) {
        Bundle bundle = parseOauthData(response);
        if (bundle == null) {
            return null;
        }
        String token = bundle.getString("access_token");
        String openid = bundle.getString("openid");
        String usid = openid;
        String expire_in = bundle.getString("expires_in");
        UMToken mToken = UMToken.buildToken(new SNSPair(SocializeConfig.getSelectedPlatfrom()
                .toString(), usid), token, openid);
        mToken.setAppKey(this.mAppKey);
        mToken.setAppId(this.mAppID);
        mToken.setExpireIn(expire_in);
        return mToken;
    }

    protected boolean initTencent() {
        Log.d("", "#### qzone app id  = " + this.mAppID);
        this.mTencent = Tencent.createInstance(this.mAppID, this.mContext);
        if (this.mTencent != null) {
            return true;
        }
        Log.e(TAG, "Tencent变量初始化失败，请检查你的app id跟AndroidManifest.xml文件中AuthActivity的scheme是否填写正确");
        return false;
    }

    protected boolean validTencent() {
        return this.mTencent != null && this.mTencent.getAppId().equals(this.mAppID);
    }

    protected void uploadToken(Context context, Object response, UMAuthListener listener) {
        final Bundle value = parseOauthData(response);
        final UMToken token = buildUmToken(response);
        if (token != null) {
            if (!DeviceConfig.isNetworkAvailable(this.mContext)) {
                Toast.makeText(context, "您的网络不可用,请检查网络连接...", 0).show();
            }
            final UMAuthListener uMAuthListener = listener;
            final Context context2 = context;
            new UMAsyncTask<Integer>() {
                protected void onPreExecute() {
                    super.onPreExecute();
                    if (uMAuthListener != null) {
                        UMAuthListener uMAuthListener = uMAuthListener;
                        SocializeConfig socializeConfig = UMTencentSsoHandler.this.mSocializeConfig;
                        uMAuthListener.onStart(SocializeConfig.getSelectedPlatfrom());
                    }
                }

                protected Integer doInBackground() {
                    return Integer.valueOf(new InitializeController(new SocializeEntity
                            (SocialSNSHelper.SOCIALIZE_QQ_KEY, RequestType.SOCIAL))
                            .uploadPlatformToken(context2, token));
                }

                protected void onPostExecute(Integer result) {
                    super.onPostExecute(result);
                    if (200 != result.intValue()) {
                        Log.d(UMTencentSsoHandler.TAG, "##### Token 授权失败");
                    } else {
                        Log.d(UMTencentSsoHandler.TAG, "##### Token 授权成功");
                        String mtk = token.getToken();
                        SHARE_MEDIA platform = SHARE_MEDIA.convertToEmun(token.mPaltform);
                        if (!(platform == null || TextUtils.isEmpty(mtk))) {
                            OauthHelper.saveAccessToken(context2, platform, mtk, "null");
                            OauthHelper.setUsid(context2, platform, token.mUsid);
                        }
                    }
                    if (uMAuthListener != null) {
                        UMAuthListener uMAuthListener = uMAuthListener;
                        Bundle bundle = value;
                        SocializeConfig socializeConfig = UMTencentSsoHandler.this.mSocializeConfig;
                        uMAuthListener.onComplete(bundle, SocializeConfig.getSelectedPlatfrom());
                    }
                    Log.d(UMTencentSsoHandler.TAG, "RESULT : CODE = " + result);
                }
            }.execute();
        }
    }

    public int getResponseCode(Object response) {
        if (response == null) {
            return -1;
        }
        String jsonStr = response.toString().trim();
        if (TextUtils.isEmpty(jsonStr)) {
            return -1;
        }
        JSONObject json = null;
        try {
            json = new JSONObject(jsonStr);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (json == null || !json.has("ret")) {
            return -1;
        }
        return json.optInt("ret");
    }

    public void setActivity(Activity activity) {
        this.mContext = activity.getApplicationContext();
    }

    public void authorizeCallBack(int requestCode, int resultCode, Intent data) {
    }

    public boolean isClientInstalled() {
        return this.mTencent.isSupportSSOLogin((Activity) this.mContext);
    }

    protected String getAppName() {
        String appName = "";
        if (!TextUtils.isEmpty(SocializeEntity.mAppName) || this.mContext == null) {
            return SocializeEntity.mAppName;
        }
        CharSequence sequence = this.mContext.getApplicationInfo().loadLabel(this.mContext
                .getPackageManager());
        if (TextUtils.isEmpty(sequence)) {
            return appName;
        }
        appName = sequence.toString();
        SocializeEntity.mAppName = appName;
        return appName;
    }

    public void getBitmapUrl(UMediaObject uMediaObjects, String usid, ObtainImageUrlListener
            listener) {
        final UMediaObject media = uMediaObjects;
        final InitializeController controller = new InitializeController(new SocializeEntity("com" +
                ".umeng.share.uploadImage", RequestType.SOCIAL));
        final long startTime = System.currentTimeMillis();
        final String str = usid;
        final ObtainImageUrlListener obtainImageUrlListener = listener;
        new DialogAsyncTask<String>(this.mContext, "") {
            protected String doInBackground() {
                UMImage image = null;
                if (media instanceof UMImage) {
                    image = media;
                }
                if (!image.isSerialized()) {
                    image.waitImageToSerialize();
                }
                if (image != null) {
                    String imageLocalPath = image.getImageCachePath();
                    String imageCachePath = (String) UMTencentSsoHandler.mImageCache.get
                            (imageLocalPath);
                    if (TextUtils.isEmpty(imageCachePath)) {
                        Log.i(UMTencentSsoHandler.TAG, "obtain image url form server...");
                        String imageUrl = controller.uploadImage(UMTencentSsoHandler.this
                                .mContext, image, str);
                        UMTencentSsoHandler.this.setImageUrl(imageLocalPath, imageUrl);
                        if (UMTencentSsoHandler.this.mContext != null && TextUtils.isEmpty
                                (imageUrl)) {
                            Toast.makeText(UMTencentSsoHandler.this.mContext, "上传图片失败", 0).show();
                        }
                        Log.i(UMTencentSsoHandler.TAG, "obtain image url form server..." +
                                UMTencentSsoHandler.this.mImageUrl);
                    } else {
                        UMTencentSsoHandler.this.mImageUrl = imageCachePath;
                        Log.i(UMTencentSsoHandler.TAG, "obtain image url form cache..." +
                                UMTencentSsoHandler.this.mImageUrl);
                    }
                }
                Log.i(UMTencentSsoHandler.TAG, "doInBackground end...");
                return "";
            }

            protected void onPostExecute(String imageUrl) {
                super.onPostExecute(imageUrl);
                Log.i(UMTencentSsoHandler.TAG, "upload image kill time: " + (System
                        .currentTimeMillis() - startTime));
                SocializeUtils.safeCloseDialog(UMTencentSsoHandler.this.mProgressDialog);
                obtainImageUrlListener.onComplete(UMTencentSsoHandler.this.mImageUrl);
            }
        }.execute();
    }

    private void setImageUrl(String localPath, String urlPath) {
        if (!TextUtils.isEmpty(urlPath)) {
            mImageCache.put(localPath, urlPath);
            this.mImageUrl = urlPath;
        }
    }

    protected boolean isUploadImageAsync(String imagePath, int type) {
        if (TextUtils.isEmpty(imagePath)) {
            return false;
        }
        SHARE_MEDIA platform = SocializeConfig.getSelectedPlatfrom();
        boolean hasClient = isClientInstalled();
        boolean isLocalImage;
        if (imagePath.startsWith(TimeLinePatterns.WEB_SCHEME) || imagePath.startsWith("https://")) {
            isLocalImage = false;
        } else {
            isLocalImage = true;
        }
        if (hasClient || !isLocalImage) {
            return false;
        }
        if (platform == SHARE_MEDIA.QQ && (type == 2 || type == 1)) {
            return true;
        }
        if (platform != SHARE_MEDIA.QZONE) {
            return false;
        }
        if (type == 1 || type == 2) {
            return true;
        }
        return false;
    }

    public boolean shareTo() {
        return true;
    }

    public void cleanQQCache() {
    }
}
