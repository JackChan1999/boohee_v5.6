package com.umeng.socialize.bean;

import android.content.Context;
import android.text.TextUtils;
import android.util.SparseArray;

import com.umeng.socialize.common.SocialSNSHelper;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.a;
import com.umeng.socialize.controller.listener.SocializeListeners.MulStatusListener;
import com.umeng.socialize.controller.listener.SocializeListeners.SnsPostListener;
import com.umeng.socialize.sso.UMSsoHandler;
import com.umeng.socialize.utils.DeviceConfig;
import com.umeng.socialize.utils.Log;
import com.umeng.socialize.utils.SocializeUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SocializeConfig extends CallbackConfig {
    private static final String                    b = SocializeConfig.class.getName();
    private static       SparseArray<UMSsoHandler> f = new SparseArray();
    private static       SHARE_MEDIA               g = SHARE_MEDIA.GENERIC;
    private static       SocializeConfig           h = new SocializeConfig();
    private static CustomPlatform o;
    private static CustomPlatform p;
    private static Map<String, SnsPlatform> t = new HashMap();
    private static List<SnsPlatform>        u = Collections.synchronizedList(new ArrayList());
    private static String[]                 x = null;
    private static List<SHARE_MEDIA>        y = new ArrayList();
    private        boolean                  c = true;
    private        boolean                  d = true;
    private        boolean                  e = true;
    private Map<SHARE_MEDIA, HashSet<String>> i;
    private boolean j = true;
    private boolean k = true;
    private boolean l = true;
    private String  m = "Sharing Socialize";
    private String  n = "";
    private boolean q = true;
    private MulStatusListener r;
    private Language             s = Language.ZH;
    private List<SHARE_MEDIA>    v = new ArrayList();
    private List<CustomPlatform> w = new ArrayList();

    static {
        a();
    }

    private SocializeConfig() {
    }

    public static SocializeConfig getSocializeConfig() {
        return h;
    }

    private static void a() {
        a(new d("sina"));
        a(new d(SHARE_MEDIA.DOUBAN.toString()));
        a(new d(SHARE_MEDIA.RENREN.toString()));
        a(new d(SHARE_MEDIA.TENCENT.toString()));
        x = new String[]{SHARE_MEDIA.WEIXIN.toString(), SHARE_MEDIA.WEIXIN_CIRCLE.toString(),
                SHARE_MEDIA.QZONE.toString(), SHARE_MEDIA.SINA.toString(), SHARE_MEDIA.QQ
                .toString(), SHARE_MEDIA.TENCENT.toString()};
    }

    private static void a(SnsPlatform snsPlatform) {
        if (snsPlatform != null && !TextUtils.isEmpty(snsPlatform.mKeyword)) {
            String str = snsPlatform.mKeyword;
            if (t.containsKey(str)) {
                t.remove(str);
            }
            t.put(str, snsPlatform);
        }
    }

    public Map<String, SnsPlatform> getPlatformMap() {
        return t;
    }

    public boolean isDefaultShareLocation() {
        return this.c;
    }

    public void setWechatUserInfoLanguage(Language language) {
        this.s = language;
    }

    public Language getWechatUserInfoLanguage() {
        return this.s;
    }

    public boolean isDefaultShareComment() {
        return this.d;
    }

    public String getMailSubject() {
        return this.m;
    }

    public void setMailSubject(String str) {
        this.m = str;
    }

    public void addFollow(SHARE_MEDIA share_media, String str) {
        if (!TextUtils.isEmpty(str) && share_media != null) {
            if (this.i == null) {
                this.i = new HashMap();
            }
            if (this.i.containsKey(share_media)) {
                ((HashSet) this.i.get(share_media)).add(str);
                return;
            }
            HashSet hashSet = new HashSet();
            hashSet.add(str);
            this.i.put(share_media, hashSet);
        }
    }

    public void addFollow(SHARE_MEDIA share_media, String[] strArr) {
        if (strArr != null && strArr.length != 0) {
            for (String addFollow : strArr) {
                addFollow(share_media, addFollow);
            }
        }
    }

    public MulStatusListener getOauthDialogFollowListener() {
        return this.r;
    }

    public void setOauthDialogFollowListener(MulStatusListener mulStatusListener) {
        this.r = mulStatusListener;
    }

    public Set<String> getFollowFids(SHARE_MEDIA share_media) {
        if (this.i == null || !this.i.containsKey(share_media)) {
            return null;
        }
        return new HashSet((Collection) this.i.get(share_media));
    }

    public boolean isShareSms() {
        return this.j;
    }

    public void setShareSms(boolean z) {
        this.j = z;
        String share_media = SHARE_MEDIA.SMS.toString();
        if (z && !t.containsKey(share_media)) {
            a(o);
        } else if (!z && t.containsKey(share_media)) {
            t.remove(share_media);
        }
    }

    public boolean isShareMail() {
        return this.k;
    }

    public void setShareMail(boolean z) {
        this.k = z;
        String share_media = SHARE_MEDIA.EMAIL.toString();
        if (z && !t.containsKey(share_media)) {
            a(p);
        } else if (!z && t.containsKey(share_media)) {
            t.remove(share_media);
        }
    }

    public void setDefaultShareLocation(boolean z) {
        this.c = z;
    }

    public void setDefaultShareComment(boolean z) {
        this.d = z;
    }

    public boolean containsDeletePlatform(SHARE_MEDIA share_media) {
        return y.contains(share_media);
    }

    public void supportAppPlatform(Context context, SHARE_MEDIA share_media, String str, boolean
            z) {
        if (z) {
            SnsPlatform a = a.a(context, share_media, str);
            if (!this.w.contains(a)) {
                a(a);
                return;
            }
            return;
        }
        CustomPlatform a2 = a.a(context, share_media, str);
        if (a2 != null && t.containsKey(a2.mKeyword)) {
            t.remove(a2.mKeyword);
        }
    }

    public boolean isSyncUserInfo() {
        return this.q;
    }

    public List<SHARE_MEDIA> getPlatforms() {
        Set<String> keySet = t.keySet();
        List<SHARE_MEDIA> arrayList = new ArrayList();
        for (String convertToEmun : keySet) {
            SHARE_MEDIA convertToEmun2 = SHARE_MEDIA.convertToEmun(convertToEmun);
            if (!(convertToEmun2 == null || convertToEmun2.isCustomPlatform())) {
                arrayList.add(convertToEmun2);
            }
        }
        return arrayList;
    }

    private SnsPlatform a(List<SnsPlatform> list, String str) {
        for (int i = 0; i < list.size(); i++) {
            SnsPlatform snsPlatform = (SnsPlatform) list.get(i);
            if (str.equals(snsPlatform.mKeyword)) {
                list.remove(snsPlatform);
                return snsPlatform;
            }
        }
        return null;
    }

    public List<SnsPlatform> getAllPlatforms(Context context, UMSocialService uMSocialService) {
        u.clear();
        SocialSNSHelper.getSupprotCloudPlatforms(context, this);
        u.addAll(t.values());
        for (SnsPlatform entityDescriptor : u) {
            entityDescriptor.setEntityDescriptor(uMSocialService.getEntity().mDescriptor);
        }
        b();
        c();
        return u;
    }

    private void b() {
        if (this.v.size() == 0) {
            a(y);
        } else {
            u = a(this.v);
        }
        HashMap hashMap = new HashMap();
        for (int i = 0; i < u.size(); i++) {
            hashMap.put(((SnsPlatform) u.get(i)).mKeyword, u.get(i));
        }
        u.clear();
        for (String str : hashMap.keySet()) {
            u.add(hashMap.get(str));
        }
    }

    private List<SnsPlatform> a(List<SHARE_MEDIA> list) {
        List<SnsPlatform> arrayList = new ArrayList();
        for (int i = 0; i < list.size(); i++) {
            SnsPlatform a = a(u, ((SHARE_MEDIA) list.get(i)).toString());
            if (a != null) {
                arrayList.add(a);
            }
        }
        return arrayList;
    }

    private void c() {
        if (x != null && x.length != 0) {
            d();
            int i = 0;
            for (String str : x) {
                Iterator it = u.iterator();
                while (it.hasNext()) {
                    SnsPlatform snsPlatform = (SnsPlatform) it.next();
                    if (SocializeUtils.isValidPlatform(str, u) && snsPlatform.mKeyword.equals(str
                            .toString())) {
                        it.remove();
                        u.add(i, snsPlatform);
                        i++;
                        break;
                    }
                }
            }
        }
    }

    private void d() {
        Set hashSet = new HashSet();
        List arrayList = new ArrayList();
        for (String str : x) {
            if (SocializeUtils.isValidPlatform(str, u) && hashSet.add(str)) {
                arrayList.add(str);
            }
        }
        x = (String[]) arrayList.toArray(new String[arrayList.size()]);
    }

    public void setPlatforms(SHARE_MEDIA... share_mediaArr) {
        this.v.clear();
        if (share_mediaArr != null && share_mediaArr.length > 0) {
            for (SHARE_MEDIA share_media : share_mediaArr) {
                if (SocializeUtils.isValidPlatform(share_media)) {
                    this.v.add(share_media);
                }
            }
        }
    }

    public void removePlatform(SHARE_MEDIA... share_mediaArr) {
        if (share_mediaArr != null && share_mediaArr.length > 0) {
            for (Object obj : share_mediaArr) {
                if (!y.contains(obj)) {
                    y.add(obj);
                }
            }
        }
    }

    public void setPlatformOrder(SHARE_MEDIA... share_mediaArr) {
        int i = 0;
        if (share_mediaArr == null || share_mediaArr.length == 0) {
            x = new String[0];
            return;
        }
        x = new String[share_mediaArr.length];
        int length = share_mediaArr.length;
        int i2 = 0;
        while (i < length) {
            int i3 = i2 + 1;
            x[i2] = share_mediaArr[i].toString();
            i++;
            i2 = i3;
        }
    }

    public void setPlatformOrder(String... strArr) {
        x = strArr;
    }

    public List<CustomPlatform> getCustomPlatforms() {
        for (SnsPlatform snsPlatform : t.values()) {
            if (snsPlatform instanceof CustomPlatform) {
                this.w.add((CustomPlatform) snsPlatform);
            }
        }
        return this.w;
    }

    public boolean isConfigedInSDK(SHARE_MEDIA share_media) {
        if (share_media == null) {
            return false;
        }
        return t.containsKey(share_media.toString());
    }

    public void setSsoHandler(UMSsoHandler uMSsoHandler) {
        if (uMSsoHandler == null) {
            Log.w(b, "ssoHander is null");
            return;
        }
        int requstCode = uMSsoHandler.getRequstCode();
        Log.d("", "#### set sso handler, code = " + requstCode);
        f.put(requstCode, uMSsoHandler);
        CustomPlatform build = uMSsoHandler.build();
        if (build != null) {
            addCustomPlatform(build);
        }
    }

    public void removeSsoHandler(SHARE_MEDIA share_media) {
        if (SocializeUtils.isValidPlatform(share_media)) {
            f.remove(share_media.getReqCode());
        }
    }

    public UMSsoHandler getSsoHandler(int i) {
        Log.d("", "## get sso Handler, requestCode = " + i);
        UMSsoHandler uMSsoHandler = (UMSsoHandler) f.get(i);
        if (uMSsoHandler == null && i == HandlerRequestCode.FACEBOOK_REQUEST_SHARE_CODE) {
            return (UMSsoHandler) f.get(SHARE_MEDIA.FACEBOOK.getReqCode());
        }
        return uMSsoHandler;
    }

    public void addCustomPlatform(CustomPlatform customPlatform) {
        a((SnsPlatform) customPlatform);
    }

    public SparseArray<UMSsoHandler> getSsoHandlersMap() {
        return f;
    }

    public static void setSelectedPlatfrom(SHARE_MEDIA share_media) {
        g = share_media;
        if (g == null) {
            g = SHARE_MEDIA.GENERIC;
        }
    }

    public static SHARE_MEDIA getSelectedPlatfrom() {
        return g;
    }

    public void closeToast() {
        this.a = false;
    }

    public void openToast() {
        this.a = true;
    }

    public boolean isShowToast() {
        return this.a;
    }

    public void enableSIMCheck(boolean z) {
        this.l = z;
    }

    public boolean isCheckSIM() {
        return this.l;
    }

    public void setSinaCallbackUrl(String str) {
        this.n = str;
    }

    public String getSinaCallbackUrl() {
        return this.n;
    }

    public static boolean isSupportSinaSSO(Context context) {
        String str = "com.sina.weibo";
        boolean isAppInstalled = DeviceConfig.isAppInstalled(str, context);
        if (getSocializeConfig().getSsoHandler(SHARE_MEDIA.SINA.getReqCode()) == null ||
                !isAppInstalled) {
            return false;
        }
        str = DeviceConfig.getAppVersion(str, context);
        if (str == null || str.compareTo("3.0.0") <= 0) {
            return false;
        }
        return true;
    }

    public static boolean isSupportQQZoneSSO(Context context) {
        String str = "com.tencent.mobileqq";
        if (!DeviceConfig.isAppInstalled(str, context)) {
            return false;
        }
        str = DeviceConfig.getAppVersion(str, context);
        if (str == null || str.compareTo("4.1") <= 0) {
            return false;
        }
        return true;
    }

    public static boolean isSupportTencentWBSSO(Context context) {
        String str = "com.tencent.WBlog";
        if (!DeviceConfig.isAppInstalled(str, context)) {
            return false;
        }
        str = DeviceConfig.getAppVersion(str, context);
        if (str == null || str.compareTo("3.8.1") <= 0) {
            return false;
        }
        return true;
    }

    public void setCacheValidStatus(boolean z) {
        this.e = z;
    }

    public boolean getCacheValidStatus() {
        return this.e;
    }

    public void fireAllListenersOnStart(Class<SnsPostListener> cls) {
        SnsPostListener[] snsPostListenerArr = (SnsPostListener[]) getListener(cls);
        if (snsPostListenerArr != null && snsPostListenerArr.length != 0) {
            for (SnsPostListener snsPostListener : snsPostListenerArr) {
                if (snsPostListener != null) {
                    snsPostListener.onStart();
                }
            }
        }
    }

    public void fireAllListenersOnComplete(Class<SnsPostListener> cls, SHARE_MEDIA share_media,
                                           int i, SocializeEntity socializeEntity) {
        SnsPostListener[] snsPostListenerArr = (SnsPostListener[]) getListener(cls);
        if (snsPostListenerArr != null && snsPostListenerArr.length != 0) {
            for (SnsPostListener snsPostListener : snsPostListenerArr) {
                if (snsPostListener != null) {
                    snsPostListener.onComplete(share_media, i, socializeEntity);
                }
            }
        }
    }
}
