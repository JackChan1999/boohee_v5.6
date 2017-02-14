package com.boohee.utils;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;

import com.boohee.one.BuildConfig;
import com.boohee.one.MyApplication;
import com.boohee.one.R;
import com.boohee.one.http.JsonCallback;
import com.boohee.one.http.JsonParams;
import com.boohee.one.http.client.PassportClient;
import com.boohee.one.ui.BaseActivity;
import com.boohee.status.UserTimelineActivity;
import com.boohee.utility.ShareHelper;
import com.tencent.connect.UserInfo;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.listener.SocializeListeners.UMAuthListener;
import com.umeng.socialize.controller.listener.SocializeListeners.UMDataListener;
import com.umeng.socialize.exception.SocializeException;
import com.umeng.socialize.net.utils.SocializeProtocolConstants;
import com.umeng.socialize.sso.QZoneSsoHandler;
import com.umeng.socialize.sso.SinaSsoHandler;
import com.umeng.socialize.weixin.controller.UMWXHandler;

import java.util.Date;
import java.util.Map;

import org.json.JSONObject;

public class SNSLogin {
    public static final String  KEY_QQ_ZONE               = "qq_zone";
    public static final String  KEY_SINA_WEIBO            = "sina_weibo";
    public static final String  KEY_WEIXIN                = "weixin";
    public static final String  QQ_APPID                  = "100530867";
    public static final String  QQ_APPKEY                 = "d32ea174315e9c42bfbd481ac3b3fef6";
    static final        String  TAG                       = SNSLogin.class.getSimpleName();
    public static final String  URL_BIND_SNS_ACCOUNT      = "/api/v1/user_connections.json";
    public static final String  URL_SNS_LOGIN_INFO_UPLOAD =
            "/api/v1/user_connections/authenticate_sns.json";
    private static      Tencent mTencent                  = Tencent.createInstance("100530867",
            MyApplication.getContext());
    private BaseActivity    mActivity;
    private Bundle          mBundle;
    private UMSocialService mController;
    private JsonCallback    mListener;
    private String          mUrl;

    static /* synthetic */ class AnonymousClass4 {
        static final /* synthetic */ int[] $SwitchMap$com$umeng$socialize$bean$SHARE_MEDIA = new
                int[SHARE_MEDIA.values().length];

        static {
            try {
                $SwitchMap$com$umeng$socialize$bean$SHARE_MEDIA[SHARE_MEDIA.SINA.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$com$umeng$socialize$bean$SHARE_MEDIA[SHARE_MEDIA.QZONE.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$com$umeng$socialize$bean$SHARE_MEDIA[SHARE_MEDIA.WEIXIN.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
        }
    }

    public SNSLogin(BaseActivity activity, UMSocialService controller, JsonCallback listener) {
        this.mActivity = activity;
        this.mController = controller;
        this.mListener = listener;
    }

    public void doRequest(SHARE_MEDIA args, String url) {
        if (this.mController != null && this.mListener != null && url != null) {
            this.mUrl = url;
            onClick(args);
        }
    }

    public void onClick(SHARE_MEDIA args) {
        switch (AnonymousClass4.$SwitchMap$com$umeng$socialize$bean$SHARE_MEDIA[args.ordinal()]) {
            case 1:
                this.mController.getConfig().setSsoHandler(new SinaSsoHandler());
                doSNSLogin(SHARE_MEDIA.SINA);
                return;
            case 2:
                this.mController.getConfig().setSsoHandler(new QZoneSsoHandler(this.mActivity,
                        "100530867", "d32ea174315e9c42bfbd481ac3b3fef6"));
                doSNSLogin(SHARE_MEDIA.QZONE);
                return;
            case 3:
                UMWXHandler wxHandler = new UMWXHandler(this.mActivity, ShareHelper.WX_APPID,
                        ShareHelper.WX_APPKEY);
                wxHandler.setRefreshTokenAvailable(false);
                this.mController.getConfig().setSsoHandler(wxHandler);
                doSNSLogin(SHARE_MEDIA.WEIXIN);
                return;
            default:
                return;
        }
    }

    private void doSNSLogin(SHARE_MEDIA args) {
        this.mController.doOauthVerify(this.mActivity, args, new UMAuthListener() {
            public void onError(SocializeException e, SHARE_MEDIA platform) {
                Helper.showToast((int) R.string.ca);
                if (SNSLogin.this.mActivity != null) {
                    SNSLogin.this.mActivity.dismissLoading();
                }
            }

            public void onComplete(Bundle value, SHARE_MEDIA platform) {
                SNSLogin.this.mBundle = SHARE_MEDIA.WEIXIN == platform ? value : null;
                if (value == null || TextUtils.isEmpty(value.getString(SocializeProtocolConstants
                        .PROTOCOL_KEY_UID))) {
                    Helper.showToast((int) R.string.ca);
                    return;
                }
                Helper.showToast((int) R.string.cd);
                SNSLogin.this.getPlatformAndLogin(platform);
            }

            public void onCancel(SHARE_MEDIA arg0) {
                Helper.showToast((int) R.string.c_);
                if (SNSLogin.this.mActivity != null) {
                    SNSLogin.this.mActivity.dismissLoading();
                }
            }

            public void onStart(SHARE_MEDIA arg0) {
            }
        });
    }

    private void getPlatformAndLogin(final SHARE_MEDIA args) {
        if (this.mActivity != null) {
            this.mActivity.showLoading();
        }
        this.mController.getPlatformInfo(this.mActivity, args, new UMDataListener() {
            public void onStart() {
                Helper.showLog(SNSLogin.TAG, "获取平台数据开始...");
            }

            public void onComplete(int status, Map<String, Object> info) {
                if (status != 200 || info == null) {
                    Helper.showLog("TestData", "发生错误：" + status);
                    Helper.showToast((int) R.string.ca);
                    if (SNSLogin.this.mActivity != null) {
                        SNSLogin.this.mActivity.dismissLoading();
                        return;
                    }
                    return;
                }
                SNSLogin.this.snsLogin(args, info);
            }
        });
    }

    private void snsLogin(SHARE_MEDIA args, Map<String, Object> info) {
        JsonParams userParams = getUserConnectionJsonParams(args, info);
        if (userParams == null) {
            Helper.showToast((int) R.string.ca);
        } else {
            PassportClient.post(this.mUrl, userParams, this.mActivity, this.mListener);
        }
    }

    private JsonParams getUserConnectionJsonParams(SHARE_MEDIA args, Map<String, Object> info) {
        JsonParams userConnection = new JsonParams();
        try {
            if (SHARE_MEDIA.WEIXIN != args) {
                userConnection.put("identity", info.get(SocializeProtocolConstants
                        .PROTOCOL_KEY_UID).toString());
                userConnection.put("access_token", info.get("access_token").toString());
                userConnection.put("avatar_url", info.get(SocializeProtocolConstants
                        .PROTOCOL_KEY_FRIENDS_ICON).toString());
                userConnection.put(UserTimelineActivity.NICK_NAME, info.get("screen_name")
                        .toString());
            } else if (this.mBundle == null) {
                return null;
            } else {
                userConnection.put("identity", info.get("unionid").toString());
                userConnection.put("access_token", this.mBundle.getString("access_token"));
                userConnection.put("avatar_url", info.get("headimgurl").toString());
                userConnection.put(UserTimelineActivity.NICK_NAME, info.get(UserTimelineActivity
                        .NICK_NAME).toString());
            }
            userConnection.put("provider", shareMedia2String(args));
            userConnection.put("sync_to_qq", "1");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return userConnection.with("user_connection");
    }

    private String shareMedia2String(SHARE_MEDIA args) {
        String result = "";
        switch (AnonymousClass4.$SwitchMap$com$umeng$socialize$bean$SHARE_MEDIA[args.ordinal()]) {
            case 1:
                return KEY_SINA_WEIBO;
            case 2:
                return KEY_QQ_ZONE;
            case 3:
                return "weixin";
            default:
                return result;
        }
    }

    public static void qqLogin(final BaseActivity activity, final String url, final JsonCallback
            callback) {
        mTencent.login((Activity) activity, BuildConfig.PLATFORM, new IUiListener() {
            public void onComplete(Object response) {
                if (response == null) {
                    Helper.showToast((int) R.string.cb);
                    return;
                }
                JSONObject jsonResponse = (JSONObject) response;
                if (jsonResponse == null || jsonResponse.length() != 0) {
                    new UserInfo(activity, SNSLogin.mTencent.getQQToken()).getUserInfo(new
                            BaseUIListener(url, activity, jsonResponse, callback));
                } else {
                    Helper.showToast((int) R.string.cb);
                }
            }

            public void onError(UiError e) {
                Helper.showToast((int) R.string.ca);
            }

            public void onCancel() {
                Helper.showToast((int) R.string.c_);
            }
        });
    }

    public static void qqLogout() {
        if (mTencent != null) {
            mTencent.logout(MyApplication.getContext());
        }
    }

    public static void snsLogin(String url, String openID, String accessToken, String nickName,
                                String avatarUrl, long expires_at, Context context, JsonCallback
                                        listener) {
        JsonParams userConnection = new JsonParams();
        userConnection.put("identity", openID);
        userConnection.put("access_token", accessToken);
        userConnection.put("avatar_url", avatarUrl);
        userConnection.put(UserTimelineActivity.NICK_NAME, nickName);
        userConnection.put("expires_at", new Date(System.currentTimeMillis() + (1000 *
                expires_at)).toString());
        userConnection.put("provider", KEY_QQ_ZONE);
        userConnection.put("sync_to_qq", "1");
        PassportClient.post(url, userConnection.with("user_connection"), context, listener);
        Helper.showToast((int) R.string.cc);
    }
}
