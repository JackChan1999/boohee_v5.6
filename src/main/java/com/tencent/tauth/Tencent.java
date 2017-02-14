package com.tencent.tauth;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.tencent.connect.auth.QQAuth;
import com.tencent.connect.auth.QQToken;
import com.tencent.connect.common.BaseApi;
import com.tencent.connect.share.QQShare;
import com.tencent.connect.share.QzoneShare;
import com.tencent.open.SocialApi;
import com.tencent.open.a.f;
import com.tencent.open.utils.Global;
import com.tencent.open.utils.HttpUtils;
import com.tencent.open.utils.HttpUtils.HttpStatusException;
import com.tencent.open.utils.HttpUtils.NetworkUnavailableException;
import com.tencent.open.utils.SystemUtils;
import com.tencent.open.utils.TemporaryStorage;

import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;

/* compiled from: ProGuard */
public class Tencent {
    private static Tencent sInstance;
    private final  QQAuth  mQQAuth;

    private Tencent(String str, Context context) {
        Global.setContext(context.getApplicationContext());
        this.mQQAuth = QQAuth.createInstance(str, context);
    }

    public static synchronized Tencent createInstance(String str, Context context) {
        Tencent tencent;
        synchronized (Tencent.class) {
            f.a(f.d, "createInstance()  -- start");
            if (sInstance == null) {
                sInstance = new Tencent(str, context);
            } else if (!str.equals(sInstance.getAppId())) {
                sInstance.logout(context);
                sInstance = new Tencent(str, context);
            }
            if (checkManifestConfig(context, str)) {
                f.a(f.d, "createInstance()  -- end");
                tencent = sInstance;
            } else {
                tencent = null;
            }
        }
        return tencent;
    }

    private static boolean checkManifestConfig(Context context, String str) {
        try {
            context.getPackageManager().getActivityInfo(new ComponentName(context.getPackageName
                    (), "com.tencent.tauth.AuthActivity"), 0);
            try {
                context.getPackageManager().getActivityInfo(new ComponentName(context
                        .getPackageName(), "com.tencent.connect.common.AssistActivity"), 0);
                return true;
            } catch (NameNotFoundException e) {
                StringBuilder stringBuilder = new StringBuilder();
                f.e("AndroidManifest.xml 没有检测到com.tencent.connect.common.AssistActivity",
                        stringBuilder.append("没有在AndroidManifest.xml中检测到com.tencent.connect" +
                                ".common.AssistActivity,请加上com.tencent.connect.common" +
                                ".AssistActivity,详细信息请查看官网文档.").append("\n配置示例如下: \n<activity\n  " +
                                "   android:name=\"com.tencent.connect.common.AssistActivity\"\n " +
                                "    android:screenOrientation=\"portrait\"\n     " +
                                "android:theme=\"@android:style/Theme.Translucent.NoTitleBar\"\n " +
                                "    " +
                                "android:configChanges=\"orientation|keyboardHidden\">\n" +
                                "</activity>").toString());
                return false;
            }
        } catch (NameNotFoundException e2) {
            String str2 = "AndroidManifest.xml 没有检测到com.tencent.tauth.AuthActivity";
            f.e(str2, ("没有在AndroidManifest.xml中检测到com.tencent.tauth.AuthActivity,请加上com.tencent" +
                    ".open.AuthActivity,并配置<data android:scheme=\"tencent" + str + "\" />," +
                    "详细信息请查看官网文档.") + "\n配置示例如下: \n<activity\n     android:name=\"com.tencent" +
                    ".connect.util.AuthActivity\"\n     android:noHistory=\"true\"\n     " +
                    "android:launchMode=\"singleTask\">\n<intent-filter>\n    " +
                    "<action android:name=\"android.intent.action.VIEW\" />\n     <category " +
                    "android:name=\"android.intent.category.DEFAULT\" />\n    <category " +
                    "android:name=\"android.intent.category.BROWSABLE\" />\n    <data " +
                    "android:scheme=\"tencent" + str + "\" />\n" + "</intent-filter>\n" +
                    "</activity>");
            return false;
        }
    }

    public int login(Activity activity, String str, IUiListener iUiListener) {
        return this.mQQAuth.login(activity, str, iUiListener);
    }

    public int login(Fragment fragment, String str, IUiListener iUiListener) {
        return this.mQQAuth.login(fragment, str, iUiListener, "");
    }

    public int loginWithOEM(Activity activity, String str, IUiListener iUiListener, String str2,
                            String str3, String str4) {
        return this.mQQAuth.loginWithOEM(activity, str, iUiListener, str2, str3, str4);
    }

    public void logout(Context context) {
        this.mQQAuth.getQQToken().setAccessToken(null, "0");
        this.mQQAuth.getQQToken().setOpenId(null);
    }

    public int reAuth(Activity activity, String str, IUiListener iUiListener) {
        return this.mQQAuth.reAuth(activity, str, iUiListener);
    }

    public void reportDAU() {
        this.mQQAuth.reportDAU();
    }

    public void checkLogin(IUiListener iUiListener) {
        this.mQQAuth.checkLogin(iUiListener);
    }

    public int invite(Activity activity, Bundle bundle, IUiListener iUiListener) {
        new SocialApi(this.mQQAuth.getQQToken()).invite(activity, bundle, iUiListener);
        return 0;
    }

    public int story(Activity activity, Bundle bundle, IUiListener iUiListener) {
        new SocialApi(this.mQQAuth.getQQToken()).story(activity, bundle, iUiListener);
        return 0;
    }

    public int gift(Activity activity, Bundle bundle, IUiListener iUiListener) {
        new SocialApi(this.mQQAuth.getQQToken()).gift(activity, bundle, iUiListener);
        return 0;
    }

    public int ask(Activity activity, Bundle bundle, IUiListener iUiListener) {
        new SocialApi(this.mQQAuth.getQQToken()).ask(activity, bundle, iUiListener);
        return 0;
    }

    public void requestAsync(String str, Bundle bundle, String str2, IRequestListener
            iRequestListener, Object obj) {
        HttpUtils.requestAsync(this.mQQAuth.getQQToken(), Global.getContext(), str, bundle, str2,
                iRequestListener);
    }

    public JSONObject request(String str, Bundle bundle, String str2) throws IOException,
            JSONException, NetworkUnavailableException, HttpStatusException {
        return HttpUtils.request(this.mQQAuth.getQQToken(), Global.getContext(), str, bundle, str2);
    }

    public void shareToQQ(Activity activity, Bundle bundle, IUiListener iUiListener) {
        new QQShare(activity, this.mQQAuth.getQQToken()).shareToQQ(activity, bundle, iUiListener);
    }

    public void shareToQzone(Activity activity, Bundle bundle, IUiListener iUiListener) {
        new QzoneShare(activity, this.mQQAuth.getQQToken()).shareToQzone(activity, bundle,
                iUiListener);
    }

    public void releaseResource() {
        TemporaryStorage.remove(SystemUtils.QQ_SHARE_CALLBACK_ACTION);
        TemporaryStorage.remove(SystemUtils.QZONE_SHARE_CALLBACK_ACTION);
        TemporaryStorage.remove(SystemUtils.QQDATALINE_CALLBACK_ACTION);
        TemporaryStorage.remove(SystemUtils.QQFAVORITES_CALLBACK_ACTION);
        TemporaryStorage.remove(SystemUtils.TROOPBAR_CALLBACK_ACTION);
    }

    public boolean onActivityResult(int i, int i2, Intent intent) {
        return false;
    }

    public boolean isSessionValid() {
        return this.mQQAuth.isSessionValid();
    }

    public String getAppId() {
        return this.mQQAuth.getQQToken().getAppId();
    }

    public String getAccessToken() {
        return this.mQQAuth.getQQToken().getAccessToken();
    }

    public long getExpiresIn() {
        return this.mQQAuth.getQQToken().getExpireTimeInSecond();
    }

    public String getOpenId() {
        return this.mQQAuth.getQQToken().getOpenId();
    }

    @Deprecated
    public void handleLoginData(Intent intent, IUiListener iUiListener) {
        BaseApi.handleDataToListener(intent, iUiListener);
    }

    public static void handleResultData(Intent intent, IUiListener iUiListener) {
        BaseApi.handleDataToListener(intent, iUiListener);
    }

    public void setAccessToken(String str, String str2) {
        f.a(f.d, "setAccessToken(), expiresIn = " + str2 + "");
        this.mQQAuth.setAccessToken(str, str2);
    }

    public void setOpenId(String str) {
        f.a(f.d, "setOpenId() --start");
        this.mQQAuth.setOpenId(Global.getContext(), str);
        f.a(f.d, "setOpenId() --end");
    }

    public boolean isReady() {
        return isSessionValid() && getOpenId() != null;
    }

    public QQToken getQQToken() {
        return this.mQQAuth.getQQToken();
    }

    public boolean isSupportSSOLogin(Activity activity) {
        if (SystemUtils.getAppVersionName(activity, "com.tencent.mobileqq") == null) {
            return false;
        }
        return SystemUtils.checkMobileQQ(activity);
    }
}
