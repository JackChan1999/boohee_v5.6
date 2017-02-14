package com.boohee.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.text.TextUtils;

import com.boohee.api.PassportApi;
import com.boohee.database.OnePreference;
import com.boohee.database.UserPreference;
import com.boohee.model.User;
import com.boohee.model.status.UserConnection;
import com.boohee.one.MyApplication;
import com.boohee.one.R;
import com.boohee.one.http.JsonCallback;
import com.boohee.one.http.JsonParams;
import com.boohee.one.http.client.BooheeClient;
import com.boohee.one.ui.BaseActivity;
import com.boohee.one.ui.CheckPhoneActivity;
import com.boohee.one.ui.MainActivity;
import com.boohee.status.UserTimelineActivity;
import com.boohee.utility.Const;
import com.boohee.widgets.LightAlertDialog;
import com.hanyou.leyusdk.LEYUApplication;
import com.hanyou.leyusdk.LEYUApplication.ICallBack;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.net.utils.SocializeProtocolConstants;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

public class ThirdLoginHelper {
    private static User mUser;

    public interface OnQQAuthFinishListener {
        void onQQAuthFinish(boolean z);
    }

    public static void leyuLogin(final BaseActivity activity, final String userName, final String
            passsword, final JsonCallback callback) {
        if (activity != null) {
            activity.showLoading();
        }
        LEYUApplication leyuapp = LEYUApplication.getAppConfig(activity);
        leyuapp._callback = new ICallBack() {
            public void ReturnAccessToken(String content) {
                ThirdLoginHelper.getLeyuUserInfo(activity, userName, passsword, content, callback);
            }

            public void OnCompleted(String content) {
            }

            public void onFailed(final String content) {
                if (activity != null) {
                    activity.runOnUiThread(new Runnable() {
                        public void run() {
                            Helper.showLong(content);
                            activity.dismissLoading();
                        }
                    });
                }
            }
        };
        leyuapp.GetDeveloperAccessToken();
    }

    private static void getLeyuUserInfo(final BaseActivity activity, String userName, String
            password, String accessToken, final JsonCallback callback) {
        JsonParams params = new JsonParams();
        params.put("action", "login");
        params.put("name", userName);
        params.put(Const.PASSWORD, password);
        params.put("access_token", accessToken);
        BooheeClient.defaultSendRequest(0, "http://m.miao.cn/action/devapi/login.jsp", params,
                new JsonCallback(activity) {
            public void ok(JSONObject response) {
                super.ok(response);
                Map<String, String> param = new HashMap();
                param.put("provider", "leyu");
                param.put("identity", response.optString(SocializeProtocolConstants
                        .PROTOCOL_KEY_UID));
                param.put("access_token", response.optString("access_token"));
                param.put("avatar_url", "http://m.miao.cn" + response.optString("uheadpic"));
                param.put(UserTimelineActivity.NICK_NAME, response.optString
                        (SocializeProtocolConstants.PROTOCOL_KEY_USER_NAME));
                ThirdLoginHelper.uploadSNSInfo(param, activity, callback);
            }

            public void onFinish() {
                super.onFinish();
                if (activity != null) {
                    activity.dismissLoading();
                }
            }
        }, activity);
    }

    public static void uploadSNSInfo(Map<String, String> param, Context context, JsonCallback
            callback) {
        if (param.size() != 0) {
            BooheeClient.build(BooheeClient.PASSPORT).post
                    ("/api/v1/user_connections/authenticate_sns.json", createUploadJSONParam
                            (param), callback, context);
        }
    }

    public static void weixinLogin(SNSLogin snsLogin) {
        if (snsLogin != null) {
            snsLogin.doRequest(SHARE_MEDIA.WEIXIN, "/api/v1/user_connections/authenticate_sns" +
                    ".json");
        }
    }

    public static void weiboLogin(SNSLogin snsLogin) {
        if (snsLogin != null) {
            snsLogin.doRequest(SHARE_MEDIA.SINA, "/api/v1/user_connections/authenticate_sns.json");
        }
    }

    public static void qqLogin(SNSLogin snsLogin) {
        if (snsLogin != null) {
            snsLogin.doRequest(SHARE_MEDIA.QZONE, "/api/v1/user_connections/authenticate_sns.json");
        }
    }

    public static void doLogin(final BaseActivity activity, String accountStr, String pwdStr) {
        if (activity != null && couldLogin(accountStr, pwdStr)) {
            activity.showLoading();
            PassportApi.login(accountStr, pwdStr, activity, new JsonCallback(activity) {
                public void ok(JSONObject object) {
                    super.ok(object);
                    ThirdLoginHelper.mUser = User.parsePassportUser(object);
                    AccountUtils.saveTokenAndUserKey(MyApplication.getContext(), ThirdLoginHelper
                            .mUser);
                    ThirdLoginHelper.getUserConnections(activity, true);
                }

                public void onFinish() {
                    super.onFinish();
                    if (activity != null) {
                        activity.dismissLoading();
                    }
                }
            });
        }
    }

    public static void doRegister(BaseActivity activity, String account, String phoneEmail,
                                  String pwdStr, String friendsPhoneNumber) {
        if (activity != null && couldRegister(account, phoneEmail, pwdStr)) {
            activity.showLoading();
            if (AccountUtils.hasUserKeyAndToken(MyApplication.getContext())) {
                register(activity, account, phoneEmail, pwdStr, friendsPhoneNumber);
                return;
            }
            final BaseActivity baseActivity = activity;
            final String str = account;
            final String str2 = phoneEmail;
            final String str3 = pwdStr;
            final String str4 = friendsPhoneNumber;
            PassportApi.authenticateGuest(activity, new JsonCallback(activity) {
                public void ok(JSONObject object) {
                    super.ok(object);
                    ThirdLoginHelper.mUser = User.parsePassportUser(object);
                    AccountUtils.saveTokenAndUserKey(MyApplication.getContext(), ThirdLoginHelper
                            .mUser);
                    ThirdLoginHelper.register(baseActivity, str, str2, str3, str4);
                }

                public void onFinish() {
                    super.onFinish();
                    if (baseActivity != null) {
                        baseActivity.dismissLoading();
                    }
                }
            });
        }
    }

    private static void register(final BaseActivity activity, String account, String phoneEmail,
                                 String pwdStr, final String friendsNumber) {
        PassportApi.upgradeToBoohee(account, phoneEmail, pwdStr, activity, new JsonCallback
                (activity) {
            public void ok(JSONObject object) {
                super.ok(object);
                AccountUtils.setUserTypeBoohee(MyApplication.getContext());
                if (TextUtils.isEmpty(friendsNumber)) {
                    AccountUtils.login(activity);
                    return;
                }
                Intent intent = new Intent(activity, CheckPhoneActivity.class);
                intent.putExtra(CheckPhoneActivity.KEY, 2);
                intent.putExtra(CheckPhoneActivity.KEY_PHONE, friendsNumber);
                activity.startActivityForResult(intent, 11);
            }

            public void onFinish() {
                super.onFinish();
                if (activity != null) {
                    activity.dismissLoading();
                }
            }
        });
    }

    private static boolean couldLogin(String accountStr, String pwdStr) {
        if (TextUtil.isEmpty(accountStr)) {
            Helper.showToast((int) R.string.r_);
            return false;
        }
        if (!TextUtil.isEmpty(pwdStr)) {
            return true;
        }
        Helper.showToast((int) R.string.ys);
        return false;
    }

    private static JsonParams createUploadJSONParam(Map<String, String> param) {
        JsonParams obj = new JsonParams();
        for (String key : param.keySet()) {
            obj.put(key, (String) param.get(key));
        }
        return obj.with("user_connection");
    }

    private static boolean couldRegister(String account, String phoneEmail, String pwdStr) {
        if (TextUtils.isEmpty(account)) {
            Helper.showToast((int) R.string.abs);
            return false;
        } else if (TextUtil.isEmail(phoneEmail) || TextUtil.isPhoneNumber(phoneEmail)) {
            if (!TextUtil.isEmpty(pwdStr)) {
                return true;
            }
            Helper.showToast((int) R.string.ys);
            return false;
        } else {
            Helper.showToast((CharSequence) "请输入正确的电话号码或邮箱~~");
            return false;
        }
    }

    public static boolean handleQQHealthRequest(final MainActivity activity, Intent intent, final
    OnQQAuthFinishListener listener) {
        if (intent == null) {
            return false;
        }
        String openId = intent.getStringExtra("openid");
        String accessToken = intent.getStringExtra("accesstoken");
        if (TextUtils.isEmpty(openId)) {
            return false;
        }
        LightAlertDialog qqAlert = null;
        String qqOpenId = UserPreference.getQQOpenID();
        String qqAccessToken = UserPreference.getQQAccessToken();
        if (TextUtils.isEmpty(qqOpenId)) {
            if (AccountUtils.isVisitorAccount(activity)) {
                doQQAuth(false, false, activity, listener);
            } else {
                qqAlert = LightAlertDialog.create((Context) activity, (int) R.string.a0z)
                        .setNegativeButton((int) R.string.eq, null).setPositiveButton((int) R
                                .string.a0w, new OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        ThirdLoginHelper.doQQAuth(true, false, activity, listener);
                    }
                });
            }
        } else if (!openId.equals(qqOpenId)) {
            qqAlert = LightAlertDialog.create((Context) activity, (int) R.string.a0u)
                    .setNegativeButton((int) R.string.eq, null).setPositiveButton((int) R.string
                            .a0x, new OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    ThirdLoginHelper.doQQAuth(false, true, activity, listener);
                }
            });
        } else if (!accessToken.equals(qqAccessToken)) {
            qqAlert = LightAlertDialog.create((Context) activity, (int) R.string.a10)
                    .setNegativeButton((int) R.string.eq, null).setPositiveButton((int) R.string
                            .a0y, new OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    ThirdLoginHelper.doQQAuth(false, true, activity, listener);
                }
            });
        }
        if (qqAlert == null) {
            return false;
        }
        qqAlert.setCancelable(false);
        if (qqAlert.isShowing()) {
            qqAlert.dismiss();
        }
        qqAlert.show();
        return true;
    }

    public static void getUserConnections(final BaseActivity activity, boolean isFinish) {
        if (activity != null) {
            activity.showLoading();
            PassportApi.getUserConnections(activity, new JsonCallback(activity) {
                public void ok(JSONObject object) {
                    super.ok(object);
                    AccountUtils.saveQQOpenIDAndAccessToken(activity, UserConnection
                            .parseUserConnections(object));
                    AccountUtils.getUserProfileAndCheck(activity);
                }

                public void onFinish() {
                    super.onFinish();
                    activity.dismissLoading();
                }
            });
        }
    }

    public static void requestSNSInfoWhenFirstStartAfterUpdate(BaseActivity activity) {
        OnePreference op = OnePreference.getInstance(activity);
        int versionCode = 0;
        try {
            versionCode = activity.getPackageManager().getPackageInfo(activity.getPackageName(),
                    0).versionCode;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        if (versionCode > op.getInt(BooheeAlert.CURRENT_VER)) {
            getUserConnections(activity, false);
        }
    }

    public static void doQQAuth(boolean isBind, final boolean isNeedLogout, final BaseActivity
            activity, final OnQQAuthFinishListener listener) {
        activity.showLoading();
        SNSLogin.qqLogin(activity, isBind ? "/api/v1/user_connections.json" :
                "/api/v1/user_connections/authenticate_sns.json", new JsonCallback(activity) {
            public void ok(JSONObject object) {
                super.ok(object);
                if (isNeedLogout) {
                    AccountUtils.logout();
                }
                AccountUtils.saveTokenAndUserKey(activity, User.parsePassportUser(object));
                AccountUtils.saveQQOpenIDAndAccessToken(activity, UserConnection
                        .parseUserConnections(object));
                if (listener != null) {
                    listener.onQQAuthFinish(true);
                }
            }

            public void fail(String message) {
                super.fail(message);
                Helper.showToast((int) R.string.cb);
                if (listener != null) {
                    listener.onQQAuthFinish(false);
                }
            }

            public void onFinish() {
                super.onFinish();
                if (activity != null) {
                    activity.dismissLoading();
                }
            }
        });
    }
}
