package com.umeng.socialize.controller.impl;

import android.content.Context;
import android.text.TextUtils;

import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.SnsAccount;
import com.umeng.socialize.bean.SocializeUser;
import com.umeng.socialize.bean.StatusCode;
import com.umeng.socialize.common.UMAsyncTask;
import com.umeng.socialize.controller.listener.SocializeListeners.FetchUserListener;
import com.umeng.socialize.exception.SocializeException;
import com.umeng.socialize.net.i;
import com.umeng.socialize.utils.Log;
import com.umeng.socialize.utils.LoginInfoHelp;
import com.umeng.socialize.utils.OauthHelper;

import java.util.Map;

/* compiled from: SocialServiceImpl */
class x extends UMAsyncTask<i> {
    final /* synthetic */ FetchUserListener    a;
    final /* synthetic */ InitializeController b;
    final /* synthetic */ Context              c;
    final /* synthetic */ v                    d;

    x(v vVar, FetchUserListener fetchUserListener, InitializeController initializeController,
      Context context) {
        this.d = vVar;
        this.a = fetchUserListener;
        this.b = initializeController;
        this.c = context;
    }

    protected /* synthetic */ Object doInBackground() {
        return a();
    }

    protected /* synthetic */ void onPostExecute(Object obj) {
        a((i) obj);
    }

    protected void onPreExecute() {
        super.onPreExecute();
        if (this.a != null) {
            this.a.onStart();
        }
    }

    protected i a() {
        try {
            i userInfo = this.b.getUserInfo(this.c);
            if (userInfo == null) {
                return userInfo;
            }
            try {
                if (userInfo.a == null || !this.d.getConfig().isSyncUserInfo()) {
                    return userInfo;
                }
                a(this.c, userInfo.a);
                return userInfo;
            } catch (Exception e) {
                Log.w(v.a(), "Sync user center failed..", e);
                return userInfo;
            }
        } catch (SocializeException e2) {
            Log.e(v.a(), e2.toString());
            return null;
        }
    }

    private void a(Context context, SocializeUser socializeUser) {
        if (socializeUser.mAccounts != null) {
            Map authenticatedPlatform = OauthHelper.getAuthenticatedPlatform(context);
            for (SnsAccount snsAccount : socializeUser.mAccounts) {
                try {
                    if (!TextUtils.isEmpty(snsAccount.getUsid())) {
                        SHARE_MEDIA convertToEmun = SHARE_MEDIA.convertToEmun(snsAccount
                                .getPlatform());
                        if (!(convertToEmun == null || OauthHelper.isAuthenticated(context,
                                convertToEmun))) {
                            OauthHelper.setUsid(context, convertToEmun, snsAccount.getUsid());
                        }
                        if (convertToEmun != null && authenticatedPlatform.containsKey
                                (convertToEmun)) {
                            authenticatedPlatform.remove(convertToEmun);
                        }
                    }
                } catch (Exception e) {
                    Log.w(v.a(), "Sync user center failed..", e);
                }
            }
            if (authenticatedPlatform.size() > 0) {
                for (SHARE_MEDIA share_media : authenticatedPlatform.keySet()) {
                    OauthHelper.remove(context, share_media);
                    OauthHelper.removeTokenExpiresIn(context, share_media);
                }
            }
        }
        if (socializeUser.mLoginAccount != null) {
            SHARE_MEDIA convertToEmun2 = SHARE_MEDIA.convertToEmun(socializeUser.mLoginAccount
                    .getPlatform());
            Object obj = null;
            if (LoginInfoHelp.isPlatformLogin(context)) {
                SHARE_MEDIA loginInfo = LoginInfoHelp.getLoginInfo(context);
                if (!(convertToEmun2 == null || convertToEmun2 == loginInfo)) {
                    obj = 1;
                }
            } else {
                int i = 1;
            }
            if (obj != null) {
                LoginInfoHelp.saveLoginInfo(context, convertToEmun2.toString());
            }
        }
    }

    protected void a(i iVar) {
        super.onPostExecute(iVar);
        if (this.a == null) {
            return;
        }
        if (iVar != null) {
            this.a.onComplete(iVar.mStCode, iVar.a);
        } else {
            this.a.onComplete(StatusCode.ST_CODE_SDK_UNKNOW, null);
        }
    }
}
