package com.umeng.socialize.controller.impl;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.SnsPlatform;
import com.umeng.socialize.bean.SocializeConfig;
import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.bean.StatusCode;
import com.umeng.socialize.bean.UMToken;
import com.umeng.socialize.common.SocializeConstants;
import com.umeng.socialize.controller.AuthService;
import com.umeng.socialize.controller.listener.SocializeListeners.SocializeClientListener;
import com.umeng.socialize.controller.listener.SocializeListeners.UMAuthListener;
import com.umeng.socialize.controller.listener.SocializeListeners.UMDataListener;
import com.umeng.socialize.exception.SocializeException;
import com.umeng.socialize.net.base.SocializeClient;
import com.umeng.socialize.net.f;
import com.umeng.socialize.net.g;
import com.umeng.socialize.net.utils.SocializeProtocolConstants;
import com.umeng.socialize.sso.UMSsoHandler;
import com.umeng.socialize.utils.ListenerUtils;
import com.umeng.socialize.utils.Log;
import com.umeng.socialize.utils.OauthHelper;
import com.umeng.socialize.utils.SocializeUtils;
import com.umeng.socialize.view.j;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/* compiled from: AuthServiceImpl */
public final class a implements AuthService {
    SocializeEntity a;
    SocializeConfig b = SocializeConfig.getSocializeConfig();
    private Map<String, String> c;
    private Map<String, Object> d;
    private final String e = a.class.getSimpleName();

    /* compiled from: AuthServiceImpl */
    private class a implements UMDataListener {
        SHARE_MEDIA    a;
        UMAuthListener b;
        UMSsoHandler   c;
        Activity       d;
        Bundle         e;
        UMAuthListener f = b();
        final /* synthetic */ a g;

        public a(a aVar, Activity activity, SHARE_MEDIA share_media, UMAuthListener
                uMAuthListener, UMSsoHandler uMSsoHandler) {
            this.g = aVar;
            this.a = share_media;
            this.b = uMAuthListener;
            this.c = uMSsoHandler;
            this.d = activity;
        }

        private UMAuthListener b() {
            return new k(this);
        }

        protected SocializeClientListener a() {
            return new l(this);
        }

        public void onStart() {
            if (this.b != null) {
                this.b.onStart(this.a);
            }
        }

        public void onComplete(int i, Map<String, Object> map) {
            String share_media = this.a.toString();
            Object obj = (map == null || !map.containsKey(share_media)) ? null : 1;
            if (obj != null || this.g.c(this.a)) {
                if (obj != null) {
                    String obj2 = map.get(share_media).toString();
                    obj = this.g.c != null ? (String) this.g.c.get(share_media) : "";
                    this.c.mExtraData.put(UMSsoHandler.APPKEY, obj2);
                    this.c.mExtraData.put(UMSsoHandler.APPSECRET, obj);
                    if (UMSsoHandler.mEntity == null) {
                        UMSsoHandler.mEntity = this.g.a;
                    }
                }
                this.c.authorize(this.d, this.f);
            } else if (this.b != null) {
                this.b.onError(new SocializeException("no appkey on " + share_media), this.a);
            }
        }
    }

    public a(SocializeEntity socializeEntity) {
        this.a = socializeEntity;
    }

    public void getPlatformKeys(Context context, UMDataListener uMDataListener) {
        new b(this, uMDataListener, context).execute();
    }

    private boolean a(Context context, SHARE_MEDIA share_media) {
        SnsPlatform snsPlatform = (SnsPlatform) this.b.getPlatformMap().get(share_media.toString());
        if (share_media.isSupportAuthorization()) {
            return true;
        }
        if (snsPlatform != null) {
            Toast.makeText(context, snsPlatform.mShowWord + "不支持授权.", 0).show();
        }
        return false;
    }

    public void deleteOauth(Context context, SHARE_MEDIA share_media, SocializeClientListener
            socializeClientListener) {
        if (share_media == SHARE_MEDIA.FACEBOOK || share_media == SHARE_MEDIA.WEIXIN ||
                share_media == SHARE_MEDIA.WEIXIN_CIRCLE) {
            UMSsoHandler ssoHandler = this.b.getSsoHandler(share_media.getReqCode());
            if (ssoHandler != null) {
                ssoHandler.deleteAuthorization(this.a, share_media, socializeClientListener);
                return;
            }
        }
        new c(this, socializeClientListener, context, share_media).execute();
    }

    public void doOauthVerify(Context context, SHARE_MEDIA share_media, UMAuthListener
            uMAuthListener) {
        Context applicationContext = context.getApplicationContext();
        if (SocializeUtils.platformCheck(applicationContext, share_media)) {
            if (uMAuthListener == null) {
                uMAuthListener = ListenerUtils.creAuthListener();
            }
            this.a.addStatisticsData(applicationContext, share_media, 3);
            if (a(applicationContext, share_media)) {
                UMAuthListener a = a(applicationContext, share_media, uMAuthListener);
                UMSsoHandler ssoHandler = this.b.getSsoHandler(share_media.getReqCode());
                Log.d(this.e, "######## doOauthVerify -->  " + share_media.toString());
                if (context instanceof Activity) {
                    Activity activity = (Activity) context;
                    if (share_media == SHARE_MEDIA.FACEBOOK && ssoHandler != null) {
                        ssoHandler.authorize(activity, uMAuthListener);
                        return;
                    } else if (ssoHandler == null || !b(applicationContext, share_media)) {
                        a(activity, share_media, a);
                        return;
                    } else {
                        SocializeConfig.setSelectedPlatfrom(share_media);
                        Log.v("auth", "platform=" + share_media);
                        a(activity, share_media, a, ssoHandler);
                        return;
                    }
                }
                Log.e(this.e, "传入参数必须为Activity实例");
            }
        }
    }

    private boolean b(Context context, SHARE_MEDIA share_media) {
        if (share_media == SHARE_MEDIA.SINA) {
            return SocializeConfig.isSupportSinaSSO(context);
        }
        if (share_media == SHARE_MEDIA.TENCENT) {
            return SocializeConfig.isSupportTencentWBSSO(context);
        }
        if (share_media != SHARE_MEDIA.RENREN) {
            return (share_media == SHARE_MEDIA.WEIXIN || share_media != SHARE_MEDIA
                    .WEIXIN_CIRCLE) ? true : true;
        } else {
            UMSsoHandler ssoHandler = this.b.getSsoHandler(SHARE_MEDIA.RENREN.getReqCode());
            return ssoHandler == null ? false : ssoHandler.isClientInstalled();
        }
    }

    private UMAuthListener a(Context context, SHARE_MEDIA share_media, UMAuthListener
            uMAuthListener) {
        return new d(this, context, uMAuthListener, (UMAuthListener[]) this.b.getListener
                (UMAuthListener.class));
    }

    private void a(Activity activity, SHARE_MEDIA share_media, UMAuthListener uMAuthListener) {
        this.a.addStatisticsData(activity.getApplicationContext(), share_media, 18);
        UMAuthListener eVar = new e(this, activity, uMAuthListener);
        Dialog jVar = new j(activity, this.a, share_media, eVar);
        if (eVar != null) {
            eVar.onStart(share_media);
        }
        SocializeUtils.safeShowDialog(jVar);
    }

    private void a(Activity activity, SHARE_MEDIA share_media, UMAuthListener uMAuthListener,
                   UMSsoHandler uMSsoHandler) {
        this.a.addStatisticsData(activity, share_media, 12);
        UMDataListener aVar = new a(this, activity, share_media, new f(this, uMAuthListener,
                activity), uMSsoHandler);
        if (this.c == null || this.d == null) {
            this.c = SocializeUtils.getPlatformSecret(activity);
            this.d = SocializeUtils.getPlatformKey(activity);
        }
        if (a(share_media)) {
            UMSsoHandler ssoHandler = this.b.getSsoHandler(share_media.getReqCode());
            String str = "";
            Object obj = "";
            if (share_media == SHARE_MEDIA.WEIXIN || share_media == SHARE_MEDIA.WEIXIN_CIRCLE) {
                str = (String) ssoHandler.mExtraData.get(SocializeConstants.FIELD_WX_APPID);
                obj = (String) ssoHandler.mExtraData.get(SocializeConstants.FIELD_WX_SECRET);
                this.a.putExtra(SocializeConstants.FIELD_WX_APPID, str);
                this.a.putExtra(SocializeConstants.FIELD_WX_SECRET, obj);
            } else if (share_media == SHARE_MEDIA.QQ || share_media == SHARE_MEDIA.QZONE) {
                str = (String) ssoHandler.mExtraData.get(SocializeConstants.FIELD_QZONE_ID);
                String str2 = (String) ssoHandler.mExtraData.get("qzone_secret");
                this.a.putExtra(SocializeConstants.FIELD_QZONE_ID, str);
                this.a.putExtra("qzone_secret", str2);
            }
            Object obj2 = null;
            if (!(this.d == null || this.d.get(share_media.toString()) == null)) {
                obj2 = this.d.get(share_media.toString()).toString();
            }
            if (!str.equals(obj2)) {
                this.d.put(share_media.toString(), str);
                this.c.put(share_media.toString(), obj);
                SocializeUtils.savePlatformKey(activity, this.d);
                SocializeUtils.savePlatformSecret(activity, this.c);
                a((Context) activity, this.d, aVar);
                return;
            }
        }
        if (b(share_media)) {
            aVar.onStart();
            aVar.onComplete(200, this.d);
            getPlatformKeys(activity, ListenerUtils.createDataListener());
            return;
        }
        getPlatformKeys(activity, aVar);
    }

    private boolean a(SHARE_MEDIA share_media) {
        if (share_media == SHARE_MEDIA.WEIXIN || share_media == SHARE_MEDIA.WEIXIN_CIRCLE ||
                share_media == SHARE_MEDIA.QQ || share_media == SHARE_MEDIA.QZONE) {
            return true;
        }
        return false;
    }

    private void a(Context context, Map<String, Object> map, UMDataListener uMDataListener) {
        new g(this, context, uMDataListener, map).execute();
    }

    private boolean b(SHARE_MEDIA share_media) {
        String share_media2 = share_media.toString();
        return this.d != null && this.d.size() > 0 && this.d.containsKey(share_media2) &&
                !TextUtils.isEmpty(this.d.get(share_media2).toString()) && this.c != null && this
                .c.size() > 0 && this.c.containsKey(share_media2) && !TextUtils.isEmpty(
                (CharSequence) this.c.get(share_media2));
    }

    private boolean c(SHARE_MEDIA share_media) {
        if (share_media == SHARE_MEDIA.FACEBOOK || share_media == SHARE_MEDIA.WEIXIN ||
                share_media == SHARE_MEDIA.WEIXIN_CIRCLE) {
            return true;
        }
        return false;
    }

    public void uploadToken(Context context, UMToken uMToken, SocializeClientListener
            socializeClientListener) {
        new i(this, new h(this, socializeClientListener, uMToken, context), context, uMToken)
                .execute();
    }

    public int a(Context context, UMToken uMToken) {
        if (uMToken == null || !uMToken.isValid()) {
            return StatusCode.ST_CODE_SDK_SHARE_PARAMS_ERROR;
        }
        g gVar = (g) new SocializeClient().execute(new f(context, this.a, uMToken));
        if (gVar == null) {
            return StatusCode.ST_CODE_SDK_UNKNOW;
        }
        if (!(this.a == null || TextUtils.isEmpty(gVar.a))) {
            this.a.putExtra("user_id", gVar.a);
            this.a.putExtra(SocializeConstants.SINA_EXPIRES_IN, gVar.b);
        }
        return gVar.mStCode;
    }

    private void a(Context context, SHARE_MEDIA share_media, Bundle bundle) {
        CharSequence string = bundle.getString("usid");
        Object string2 = bundle.getString("access_key");
        Object string3 = bundle.getString("access_secret");
        Object string4 = bundle.getString("access_token");
        Object string5 = bundle.getString(SocializeProtocolConstants.PROTOCOL_KEY_UID);
        if (!TextUtils.isEmpty(string) && share_media == SHARE_MEDIA.TENCENT) {
            string5 = this.a.getExtra("user_id");
            bundle.putString(SocializeProtocolConstants.PROTOCOL_KEY_UID, string5);
        }
        if (TextUtils.isEmpty(string3)) {
            string3 = bundle.getString("openid");
        }
        String string6 = bundle.getString("expires_in");
        if (TextUtils.isEmpty(string6)) {
            string6 = this.a.getExtra("expires_in");
        }
        OauthHelper.saveTokenExpiresIn(context, share_media, string6);
        if (!(TextUtils.isEmpty(string2) || TextUtils.isEmpty(string3))) {
            OauthHelper.saveAccessToken(context, share_media, string2, string3);
        }
        if (!TextUtils.isEmpty(string5)) {
            OauthHelper.setUsid(context, share_media, string5);
        }
        if (!(share_media == null || TextUtils.isEmpty(string4))) {
            OauthHelper.saveAccessToken(context, share_media, string4, "null");
        }
        if (share_media == SHARE_MEDIA.WEIXIN || share_media == SHARE_MEDIA.WEIXIN_CIRCLE) {
            OauthHelper.saveRefreshToken(context, share_media, bundle.getString
                    (SocializeProtocolConstants.PROTOCOL_KEY_REFRESH_TOKEN));
            OauthHelper.saveRefreshTokenExpires(context, share_media, bundle.getLong
                    ("refresh_token_expires"));
        }
    }

    public void checkTokenExpired(Context context, SHARE_MEDIA[] share_mediaArr, UMDataListener
            uMDataListener) {
        new j(this, uMDataListener, context, a(share_mediaArr)).execute();
    }

    private SHARE_MEDIA[] a(SHARE_MEDIA[] share_mediaArr) {
        int i = 0;
        if (share_mediaArr == null || share_mediaArr.length == 0) {
            return new SHARE_MEDIA[0];
        }
        List arrayList = new ArrayList();
        int length = share_mediaArr.length;
        while (i < length) {
            SHARE_MEDIA share_media = share_mediaArr[i];
            if (share_media.isSupportAuthorization()) {
                arrayList.add(share_media);
            } else {
                Log.w(this.e, share_media.toString() + "does't support to Token expires check");
            }
            i++;
        }
        if (arrayList.contains(SHARE_MEDIA.FACEBOOK)) {
            arrayList.remove(SHARE_MEDIA.FACEBOOK);
            Log.w(this.e, "facebook does't support to Token expires check");
        }
        return (SHARE_MEDIA[]) arrayList.toArray(new SHARE_MEDIA[arrayList.size()]);
    }
}
