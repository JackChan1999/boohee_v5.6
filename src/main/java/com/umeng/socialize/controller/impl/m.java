package com.umeng.socialize.controller.impl;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.text.TextUtils;
import android.view.ViewGroup.LayoutParams;
import android.widget.Toast;

import com.umeng.socialize.bean.MultiStatus;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.SNSPair;
import com.umeng.socialize.bean.ShareType;
import com.umeng.socialize.bean.SnsPlatform;
import com.umeng.socialize.bean.SocializeConfig;
import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.bean.StatusCode;
import com.umeng.socialize.bean.UMShareMsg;
import com.umeng.socialize.common.SocializeConstants;
import com.umeng.socialize.controller.ShareService;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.listener.SocializeListeners.MulStatusListener;
import com.umeng.socialize.controller.listener.SocializeListeners.SnsPostListener;
import com.umeng.socialize.controller.listener.SocializeListeners.UMAuthListener;
import com.umeng.socialize.controller.listener.SocializeListeners.UMShareBoardListener;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.net.base.SocializeClient;
import com.umeng.socialize.net.base.SocializeReseponse;
import com.umeng.socialize.net.o;
import com.umeng.socialize.net.p;
import com.umeng.socialize.net.q;
import com.umeng.socialize.net.utils.SocializeProtocolConstants;
import com.umeng.socialize.utils.BitmapUtils;
import com.umeng.socialize.utils.ListenerUtils;
import com.umeng.socialize.utils.LoadingDialog;
import com.umeng.socialize.utils.Log;
import com.umeng.socialize.utils.OauthHelper;
import com.umeng.socialize.utils.SocializeUtils;
import com.umeng.socialize.view.ShareActivity;
import com.umeng.socialize.view.abs.b;
import com.umeng.socialize.view.aj;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/* compiled from: ShareServiceImpl */
public final class m implements ShareService {
    public static SnsPostListener d = null;
    SocializeEntity a;
    UMSocialService b;
    SocializeConfig c = SocializeConfig.getSocializeConfig();
    private       boolean e = false;
    private       aj      f = null;
    private       boolean g = false;
    private final String  h = m.class.getSimpleName();
    private UMShareBoardListener i;

    public m(SocializeEntity socializeEntity) {
        this.a = socializeEntity;
    }

    private final void a() {
        if (this.b == null && this.a != null) {
            this.b = UMServiceFactory.getUMSocialService(this.a.mDescriptor);
        }
    }

    @Deprecated
    public void shareTo(Activity activity, SHARE_MEDIA share_media, String str, byte[] bArr) {
        a();
        a(activity, str, bArr);
        postShare(activity, share_media, null);
    }

    @Deprecated
    public void shareTo(Activity activity, String str, byte[] bArr) {
        a();
        a(activity, str, bArr);
        openShare(activity, false);
    }

    private void a(Activity activity, String str, byte[] bArr) {
        if (!TextUtils.isEmpty(str)) {
            this.b.setShareContent(str);
        }
        if (bArr != null) {
            this.b.setShareMedia(new UMImage(activity, bArr));
        } else {
            this.b.setShareMedia(null);
        }
    }

    public void openShare(Activity activity, boolean z) {
        this.g = z;
        openShare(activity, null);
    }

    public void openShare(Activity activity, SnsPostListener snsPostListener) {
        a();
        if (e(activity)) {
            a(activity);
            this.a.addStatisticsData(activity, SHARE_MEDIA.GENERIC, 1);
            this.f = null;
            b bVar = new b(activity);
            bVar.setLayoutParams(new LayoutParams(-1, -1));
            this.f = new aj(activity, bVar, UMServiceFactory.getUMSocialService(this.a
                    .mDescriptor));
            this.f.setFocusable(true);
            this.f.setBackgroundDrawable(new BitmapDrawable());
            this.f.a(this.i);
            bVar.a(new n(this));
            if (snsPostListener != null) {
                this.c.registerListener(snsPostListener);
            }
            if (this.g) {
                d(activity);
            } else {
                this.f.showAtLocation(activity.getWindow().getDecorView(), 80, 0, 0);
            }
        }
    }

    private void a(Activity activity) {
        b(activity);
        c(activity);
        SocializeConfig.getSocializeConfig().removePlatform(new SHARE_MEDIA[]{SHARE_MEDIA.DOUBAN,
                SHARE_MEDIA.RENREN});
    }

    private void b(Activity activity) {
        String str = SocializeConstants.DEFAULTID;
        Class[] clsArr = new Class[]{Context.class, String.class, String.class};
        Object[] objArr = new Object[]{activity, str, str};
        Class[] clsArr2 = new Class[]{Boolean.TYPE};
        Object[] objArr2 = new Object[]{Boolean.valueOf(true)};
        String str2 = "com.umeng.socialize.weixin.controller.UMWXHandler";
        String str3 = "addToSocialSDK";
        String str4 = "setToCircle";
        if (this.c.getSsoHandler(SHARE_MEDIA.WEIXIN.getReqCode()) == null) {
            Object a = a(str2, clsArr, objArr);
            if (a != null) {
                a(a, str3, null, null);
            }
        }
        if (this.c.getSsoHandler(SHARE_MEDIA.WEIXIN_CIRCLE.getReqCode()) == null) {
            Object a2 = a(str2, clsArr, objArr);
            if (a2 != null) {
                a(a2, str4, clsArr2, objArr2);
                a(a2, str3, null, null);
            }
        }
    }

    private void c(Activity activity) {
        Object a;
        String str = SocializeConstants.DEFAULTID;
        String str2 = "com.umeng.socialize.sso.QZoneSsoHandler";
        String str3 = "com.umeng.socialize.sso.UMQQSsoHandler";
        String str4 = "addToSocialSDK";
        Class[] clsArr = new Class[]{Activity.class, String.class, String.class};
        Object[] objArr = new Object[]{activity, str, str};
        if (this.c.getSsoHandler(SHARE_MEDIA.QZONE.getReqCode()) == null) {
            a = a(str2, clsArr, objArr);
            if (a != null) {
                a(a, str4, null, null);
            }
        }
        if (this.c.getSsoHandler(SHARE_MEDIA.QQ.getReqCode()) == null) {
            a = a(str3, clsArr, objArr);
            if (a != null) {
                a(a, str4, null, null);
            }
        }
    }

    private void a(Object obj, String str, Class<?>[] clsArr, Object[] objArr) {
        try {
            obj.getClass().getMethod(str, clsArr).invoke(obj, objArr);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e2) {
            e2.printStackTrace();
        } catch (IllegalArgumentException e3) {
            e3.printStackTrace();
        } catch (InvocationTargetException e4) {
            e4.printStackTrace();
        }
    }

    private Object a(String str, Class<?>[] clsArr, Object[] objArr) {
        try {
            return Class.forName(str).getConstructor(clsArr).newInstance(objArr);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            return null;
        } catch (InstantiationException e2) {
            e2.printStackTrace();
            return null;
        } catch (IllegalAccessException e3) {
            e3.printStackTrace();
            return null;
        } catch (IllegalArgumentException e4) {
            e4.printStackTrace();
            return null;
        } catch (InvocationTargetException e5) {
            e5.printStackTrace();
            return null;
        } catch (ClassNotFoundException e6) {
            e6.printStackTrace();
            return null;
        }
    }

    private void d(Activity activity) {
        this.b.showLoginDialog(activity, new o(this, activity));
    }

    private boolean e(Activity activity) {
        if (activity == null || activity.isFinishing()) {
            Log.e(this.h, "### activity == null");
            return false;
        } else if (this.f != null && this.f.isShowing()) {
            Toast.makeText(activity, "分享面板已打开", 0).show();
            return false;
        } else if (this.c.getAllPlatforms(activity, this.b).size() != 0) {
            return true;
        } else {
            Log.e(this.h, "### 平台数量为0");
            Toast.makeText(activity, "平台数量为0", 0).show();
            return false;
        }
    }

    public void shareSms(Context context) {
        ((SnsPlatform) this.c.getPlatformMap().get(SHARE_MEDIA.SMS.toString())).performClick
                (context, this.a, null);
    }

    public void shareEmail(Context context) {
        ((SnsPlatform) this.c.getPlatformMap().get(SHARE_MEDIA.EMAIL.toString())).performClick
                (context, this.a, null);
    }

    public void postShare(Context context, SHARE_MEDIA share_media, SnsPostListener
            snsPostListener) {
        if (SocializeUtils.platformCheck(context, share_media)) {
            if (snsPostListener == null) {
                snsPostListener = ListenerUtils.createSnsPostListener();
            }
            a();
            this.a.addStatisticsData(context, share_media, 2);
            SocializeConfig.setSelectedPlatfrom(share_media);
            b(context.getApplicationContext());
            this.c.registerListener(d);
            if (share_media.isCustomPlatform()) {
                c(context, share_media, snsPostListener);
            } else {
                a(context, share_media, snsPostListener);
            }
        }
    }

    public void postShare(Context context, String str, SHARE_MEDIA share_media, SnsPostListener
            snsPostListener) {
        if (SocializeUtils.isValidPlatform(share_media)) {
            UMShareMsg shareMsg;
            a();
            if (this.a.getShareMsg() != null) {
                shareMsg = this.a.getShareMsg();
                this.a.setShareMsg(null);
            } else {
                shareMsg = new UMShareMsg();
                shareMsg.mText = this.a.getShareContent();
                shareMsg.setMediaData(this.a.getMedia());
            }
            this.a.setFireCallback(true);
            postShareByCustomPlatform(context, str, share_media.toString(), shareMsg,
                    snsPostListener);
        }
    }

    private void a(Context context, SHARE_MEDIA share_media, SnsPostListener snsPostListener) {
        Intent intent = new Intent(context, ShareActivity.class);
        intent.putExtra(SocializeProtocolConstants.PROTOCOL_KEY_DESCRIPTOR, this.a.mDescriptor);
        intent.putExtra("sns", share_media.toString());
        if (!OauthHelper.isAuthenticatedAndTokenNotExpired(context, share_media)) {
            Dialog createProgressDialog = LoadingDialog.createProgressDialog(context,
                    share_media, "", false);
            UMAuthListener pVar = new p(this, createProgressDialog, context, snsPostListener,
                    intent);
            SocializeUtils.safeShowDialog(createProgressDialog);
            this.b.doOauthVerify(context, share_media, pVar);
        } else if (this.e) {
            String usid = OauthHelper.getUsid(context, share_media);
            this.e = false;
            postShare(context, usid, share_media, snsPostListener);
        } else {
            if (this.c.contains(snsPostListener) <= 0) {
                this.c.registerListener(snsPostListener);
            }
            context.startActivity(intent);
        }
    }

    public void postShareByCustomPlatform(Context context, String str, String str2, UMShareMsg
            uMShareMsg, SnsPostListener snsPostListener) {
        SHARE_MEDIA convertToEmun = SHARE_MEDIA.convertToEmun(str2);
        a();
        new q(this, snsPostListener, str2, str, context, uMShareMsg, convertToEmun).execute();
    }

    private boolean a(Context context) {
        boolean a = this.b instanceof InitializeController ? ((InitializeController) this.b).a
                (context) : false;
        if (a && "-1".equals(this.a.mEntityKey)) {
            this.a.mEntityKey = this.b.getEntity().mEntityKey;
        }
        return a;
    }

    private MultiStatus a(Context context, SNSPair[] sNSPairArr, UMShareMsg uMShareMsg) {
        if (uMShareMsg == null) {
            return new MultiStatus(StatusCode.ST_CODE_SDK_UNKNOW);
        }
        CharSequence charSequence = uMShareMsg.mWeiBoId;
        if (sNSPairArr == null || sNSPairArr.length < 1) {
            return new MultiStatus(StatusCode.ST_CODE_SDK_SHARE_PARAMS_ERROR);
        }
        if (TextUtils.isEmpty(charSequence)) {
            p pVar = (p) new SocializeClient().execute(new o(context, this.a, sNSPairArr,
                    uMShareMsg));
            if (pVar == null) {
                return new MultiStatus(StatusCode.ST_CODE_SDK_NORESPONSE);
            }
            Log.d("", "#### ShareMultiResponse toString : " + pVar.toString());
            if (pVar.c != null) {
                this.a.putExtra(pVar.c.toString(), pVar.b);
            }
            MultiStatus multiStatus = new MultiStatus(pVar.mStCode, pVar.mMsg);
            multiStatus.setPlatformCode(pVar.a);
            return multiStatus;
        }
        SocializeReseponse execute = new SocializeClient().execute(new q(context, this.a,
                sNSPairArr[0].mPaltform, sNSPairArr[0].mUsid, uMShareMsg));
        Log.e("xxxx sns[0].mUsid=" + sNSPairArr[0].mUsid);
        if (execute == null) {
            return new MultiStatus(StatusCode.ST_CODE_SDK_NORESPONSE);
        }
        return new MultiStatus(execute.mStCode, execute.mMsg);
    }

    private void a(Context context, InitializeController initializeController) {
        new r(this, initializeController, context).execute();
    }

    private void b() {
        if (SocializeUtils.deleteUris != null && SocializeUtils.deleteUris.size() > 0) {
            Set hashSet = new HashSet();
            for (Uri uri : SocializeUtils.deleteUris) {
                hashSet.add(uri.getScheme() + "://" + uri.getAuthority() + uri.getPath());
            }
            SocializeUtils.saveObject(hashSet, BitmapUtils.PATH + SocializeConstants.FILE_URI_NAME);
        }
    }

    public void directShare(Context context, SHARE_MEDIA share_media, SnsPostListener
            snsPostListener) {
        if (SocializeUtils.platformCheck(context, share_media)) {
            if (snsPostListener == null) {
                snsPostListener = ListenerUtils.createSnsPostListener();
            }
            a();
            this.a.addStatisticsData(context, share_media, 8);
            if (this.a.getShareType() == ShareType.NORMAL) {
                SocializeConfig.setSelectedPlatfrom(share_media);
            } else {
                SocializeConfig.setSelectedPlatfrom(SHARE_MEDIA.GENERIC);
            }
            b(context.getApplicationContext());
            this.c.registerListener(d);
            if (share_media.isCustomPlatform()) {
                c(context, share_media, snsPostListener);
            } else {
                b(context, share_media, snsPostListener);
            }
        }
    }

    private void b(Context context, SHARE_MEDIA share_media, SnsPostListener snsPostListener) {
        SHARE_MEDIA convertToEmun = SHARE_MEDIA.convertToEmun(share_media.toString());
        String usid = OauthHelper.getUsid(context, convertToEmun);
        if (OauthHelper.isAuthenticatedAndTokenNotExpired(context, convertToEmun)) {
            postShare(context, usid, share_media, snsPostListener);
            return;
        }
        this.e = true;
        postShare(context, share_media, snsPostListener);
    }

    private void c(Context context, SHARE_MEDIA share_media, SnsPostListener snsPostListener) {
        SnsPlatform snsPlatform = (SnsPlatform) this.c.getPlatformMap().get(share_media.toString());
        if (snsPlatform != null) {
            snsPlatform.performClick(context, this.a, snsPostListener);
        }
    }

    public void postShareMulti(Context context, MulStatusListener mulStatusListener,
                               SHARE_MEDIA... share_mediaArr) {
        if (context == null) {
            Log.e(this.h, "请传递一个有效的Context对象");
        } else if (share_mediaArr == null || share_mediaArr.length == 0) {
            Log.e(this.h, "分享的平台为空，请传递有效的分享平台");
        } else {
            SHARE_MEDIA[] share_mediaArr2;
            a();
            Map hashMap = new HashMap();
            List asList = Arrays.asList(SHARE_MEDIA.getShareMultiPlatforms());
            List arrayList = new ArrayList();
            if (share_mediaArr != null) {
                for (SHARE_MEDIA share_media : share_mediaArr) {
                    if (asList.contains(share_media)) {
                        arrayList.add(share_media);
                    } else {
                        Log.w(this.h, share_media.toString() + "不支持一键分享到多个平台");
                    }
                }
                share_mediaArr2 = (SHARE_MEDIA[]) arrayList.toArray(new SHARE_MEDIA[arrayList
                        .size()]);
            } else {
                share_mediaArr2 = share_mediaArr;
            }
            SNSPair[] oauthedPlatforms = SocializeUtils.getOauthedPlatforms(context, hashMap,
                    share_mediaArr2);
            UMShareMsg uMShareMsg = new UMShareMsg();
            uMShareMsg.mText = this.a.getShareContent();
            uMShareMsg.setMediaData(this.a.getMedia());
            if (oauthedPlatforms != null && oauthedPlatforms.length > 0) {
                new s(this, mulStatusListener, context, oauthedPlatforms, uMShareMsg, hashMap)
                        .execute();
            } else if (mulStatusListener != null) {
                mulStatusListener.onStart();
                MultiStatus multiStatus = new MultiStatus(StatusCode
                        .ST_CODE_SDK_SHARE_PARAMS_ERROR);
                multiStatus.setPlatformCode(hashMap);
                mulStatusListener.onComplete(multiStatus, StatusCode
                        .ST_CODE_SDK_SHARE_PARAMS_ERROR, this.a);
            }
        }
    }

    public void postShareByID(Context context, String str, String str2, SHARE_MEDIA share_media,
                              SnsPostListener snsPostListener) {
        a();
        new t(this, snsPostListener, share_media, str2, str, context).execute();
    }

    private void b(Context context) {
        if (d == null) {
            d = new u(this, context);
        }
    }

    public void setShareBoardListener(UMShareBoardListener uMShareBoardListener) {
        this.i = uMShareBoardListener;
    }

    public void dismissShareBoard() {
        if (this.f != null && this.f.isShowing()) {
            this.f.dismiss();
            this.f = null;
        }
    }

    public boolean isOpenShareBoard() {
        return this.f != null && this.f.isShowing();
    }
}
