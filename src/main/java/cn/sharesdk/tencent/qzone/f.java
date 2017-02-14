package cn.sharesdk.tencent.qzone;

import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Base64;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.framework.a.a;
import cn.sharesdk.framework.authorize.AuthorizeListener;
import cn.sharesdk.framework.authorize.b;
import cn.sharesdk.framework.authorize.g;
import cn.sharesdk.framework.e;
import com.boohee.utils.Utils;
import com.mob.tools.network.KVPair;
import com.mob.tools.network.NetworkHelper;
import com.mob.tools.utils.Data;
import com.mob.tools.utils.DeviceHelper;
import com.mob.tools.utils.Hashon;
import com.mob.tools.utils.Ln;
import com.mob.tools.utils.R;
import com.qiniu.android.dns.Record;
import com.tencent.connect.common.Constants;
import com.tencent.open.SocialConstants;
import com.tencent.open.utils.ServerSetting;
import com.xiaomi.account.openauth.utils.Network;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

public class f extends e {
    private static final String[] b = new String[]{"get_user_info", "get_simple_userinfo", "get_user_profile", "get_app_friends", "add_share", "list_album", "upload_pic", "add_album", "set_user_face", "get_vip_info", "get_vip_rich_info", "get_intimate_friends_weibo", "match_nick_tips_weibo", "add_t", "add_pic_t"};
    private static f c;
    private String d;
    private String e;
    private String f;
    private a g = a.a();
    private String[] h;

    private f(Platform platform) {
        super(platform);
    }

    public static f a(Platform platform) {
        if (c == null) {
            c = new f(platform);
        }
        return c;
    }

    private String b() {
        int i = 0;
        String[] strArr = this.h == null ? b : this.h;
        StringBuilder stringBuilder = new StringBuilder();
        int length = strArr.length;
        int i2 = 0;
        while (i < length) {
            String str = strArr[i];
            if (i2 > 0) {
                stringBuilder.append(',');
            }
            stringBuilder.append(str);
            i2++;
            i++;
        }
        return stringBuilder.toString();
    }

    public HashMap<String, Object> a(String str, String str2) {
        String str3 = "https://graph.qq.com/photo/upload_pic";
        ArrayList arrayList = new ArrayList();
        arrayList.add(new KVPair("access_token", this.f));
        arrayList.add(new KVPair("oauth_consumer_key", this.d));
        arrayList.add(new KVPair("openid", this.e));
        arrayList.add(new KVPair("format", "json"));
        if (!TextUtils.isEmpty(str2)) {
            Object obj;
            if (str2.length() > 200) {
                obj = str2.substring(0, 199) + "…";
            }
            arrayList.add(new KVPair("photodesc", obj));
        }
        arrayList.add(new KVPair("mobile", "1"));
        KVPair kVPair = new KVPair(SocialConstants.PARAM_AVATAR_URI, str);
        ArrayList arrayList2 = new ArrayList();
        arrayList2.add(new KVPair(Network.USER_AGENT, System.getProperties().getProperty("http.agent") + " ArzenAndroidSDK"));
        String a = this.g.a(str3, arrayList, kVPair, arrayList2, "/photo/upload_pic", c());
        return (a == null || a.length() <= 0) ? null : new Hashon().fromJson(a);
    }

    public HashMap<String, Object> a(String str, String str2, HashMap<String, Object> hashMap, HashMap<String, String> hashMap2) {
        if (str2 == null) {
            return null;
        }
        KVPair kVPair;
        String httpGet;
        ArrayList arrayList = new ArrayList();
        if (hashMap != null && hashMap.size() > 0) {
            for (Entry entry : hashMap.entrySet()) {
                arrayList.add(new KVPair((String) entry.getKey(), String.valueOf(entry.getValue())));
            }
        }
        arrayList.add(new KVPair("access_token", this.f));
        arrayList.add(new KVPair("oauth_consumer_key", this.d));
        arrayList.add(new KVPair("openid", this.e));
        arrayList.add(new KVPair("format", "json"));
        if (hashMap2 == null || hashMap2.size() <= 0) {
            kVPair = null;
        } else {
            HashMap<String, Object> hashMap3 = null;
            for (Entry entry2 : hashMap2.entrySet()) {
                Object kVPair2 = new KVPair((String) entry2.getKey(), entry2.getValue());
            }
            kVPair = hashMap3;
        }
        ArrayList arrayList2 = new ArrayList();
        arrayList2.add(new KVPair(Network.USER_AGENT, System.getProperties().getProperty("http.agent") + " ArzenAndroidSDK"));
        try {
            if ("GET".equals(str2.toUpperCase())) {
                httpGet = new NetworkHelper().httpGet(str, arrayList, arrayList2, null);
            } else {
                if (Constants.HTTP_POST.equals(str2.toUpperCase())) {
                    httpGet = new NetworkHelper().httpPost(str, arrayList, kVPair, arrayList2, null);
                }
                httpGet = null;
            }
        } catch (Throwable th) {
            Ln.e(th);
        }
        return (httpGet == null || httpGet.length() <= 0) ? null : new Hashon().fromJson(httpGet);
    }

    public void a(AuthorizeListener authorizeListener, boolean z) {
        if (z) {
            b(authorizeListener);
        } else {
            a(new g(this, authorizeListener));
        }
    }

    public void a(String str) {
        this.d = str;
    }

    public void a(String str, String str2, String str3, String str4, String str5, PlatformActionListener platformActionListener) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("mqqapi://share/to_qzone?");
        stringBuilder.append("src_type=app&");
        stringBuilder.append("version=1&");
        stringBuilder.append("file_type=news&");
        if (!TextUtils.isEmpty(str4)) {
            stringBuilder.append("image_url=").append(Base64.encodeToString(str4.getBytes(com.qiniu.android.common.Constants.UTF_8), 2)).append(com.alipay.sdk.sys.a.b);
        }
        stringBuilder.append("title=").append(Base64.encodeToString(str.getBytes(com.qiniu.android.common.Constants.UTF_8), 2)).append(com.alipay.sdk.sys.a.b);
        stringBuilder.append("description=").append(Base64.encodeToString(str3.getBytes(com.qiniu.android.common.Constants.UTF_8), 2)).append(com.alipay.sdk.sys.a.b);
        stringBuilder.append("app_name=").append(Base64.encodeToString(str5.getBytes(com.qiniu.android.common.Constants.UTF_8), 2)).append(com.alipay.sdk.sys.a.b);
        stringBuilder.append("open_id=&");
        stringBuilder.append("share_id=").append(this.d).append(com.alipay.sdk.sys.a.b);
        stringBuilder.append("url=").append(Base64.encodeToString(str2.getBytes(com.qiniu.android.common.Constants.UTF_8), 2)).append(com.alipay.sdk.sys.a.b);
        stringBuilder.append("req_type=MQ==&");
        stringBuilder.append("cflag=").append(Base64.encodeToString((a() ? "1" : "0").getBytes(com.qiniu.android.common.Constants.UTF_8), 2));
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.setData(Uri.parse(stringBuilder.toString()));
        if (this.a.getContext().getPackageManager().resolveActivity(intent, 1) == null) {
            b(str, str2, str3, str4, str5, platformActionListener);
            return;
        }
        i iVar = new i();
        iVar.a(stringBuilder.toString(), true);
        iVar.a(platformActionListener);
        iVar.a(this.d);
        iVar.show(this.a.getContext(), null);
    }

    public void a(String str, String str2, String str3, String str4, String str5, boolean z, PlatformActionListener platformActionListener) {
        if (TextUtils.isEmpty(str2)) {
            throw new Throwable("titleUrl is needed");
        } else if (TextUtils.isEmpty(str3)) {
            throw new Throwable("text is needed");
        } else {
            CharSequence substring;
            String string;
            if (str.length() > 200) {
                substring = str.substring(0, 200);
            }
            String substring2 = str3.length() > Record.TTL_MIN_SECONDS ? str3.substring(0, Record.TTL_MIN_SECONDS) : str3;
            if (TextUtils.isEmpty(str5)) {
                str5 = DeviceHelper.getInstance(this.a.getContext()).getAppName();
            }
            String str6 = str5.length() > 20 ? str5.substring(0, 20) + "..." : str5;
            if (TextUtils.isEmpty(substring)) {
                int stringRes = R.getStringRes(this.a.getContext(), "share_to_qzone_default");
                string = stringRes > 0 ? this.a.getContext().getString(stringRes, new Object[]{str6}) : str6;
            } else {
                CharSequence charSequence = substring;
            }
            if (z) {
                a(string, str2, substring2, str4, str6, platformActionListener);
            } else {
                b(string, str2, substring2, str4, str6, platformActionListener);
            }
        }
    }

    public void a(String[] strArr) {
        this.h = strArr;
    }

    public boolean a() {
        String str;
        try {
            str = this.a.getContext().getPackageManager().getPackageInfo("com.tencent.mobileqq", 0).versionName;
        } catch (Throwable th) {
            Ln.w(th);
            str = "0";
        }
        String[] split = str.split("\\.");
        int[] iArr = new int[split.length];
        for (int i = 0; i < iArr.length; i++) {
            try {
                iArr[i] = R.parseInt(split[i]);
            } catch (Throwable th2) {
                Ln.e(th2);
                iArr[i] = 0;
            }
        }
        return iArr.length > 1 && iArr[1] >= 5;
    }

    public HashMap<String, Object> b(String str, String str2) {
        Object obj = !TextUtils.isEmpty(str) ? 1 : null;
        String str3 = obj != null ? "/t/add_pic_t" : "/t/add_t";
        String str4 = "https://graph.qq.com" + str3;
        ArrayList arrayList = new ArrayList();
        arrayList.add(new KVPair("oauth_consumer_key", this.d));
        arrayList.add(new KVPair("access_token", this.f));
        arrayList.add(new KVPair("openid", this.e));
        arrayList.add(new KVPair("format", "json"));
        arrayList.add(new KVPair(Utils.RESPONSE_CONTENT, str2));
        String a = obj != null ? this.g.a(str4, arrayList, new KVPair("pic", str), str3, c()) : this.g.b(str4, arrayList, str3, c());
        if (a == null || a.length() <= 0) {
            return null;
        }
        HashMap<String, Object> fromJson = new Hashon().fromJson(a);
        if (((Integer) fromJson.get("ret")).intValue() == 0) {
            return fromJson;
        }
        throw new Throwable(a);
    }

    public void b(String str) {
        this.e = str;
    }

    public void b(String str, String str2, String str3, String str4, String str5, PlatformActionListener platformActionListener) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("http://openmobile.qq.com/api/check2?");
        stringBuilder.append("page=qzshare.html&");
        stringBuilder.append("loginpage=loginindex.html&");
        stringBuilder.append("logintype=qzone&");
        stringBuilder.append("action=shareToQQ&");
        stringBuilder.append("sdkv=2.6&");
        stringBuilder.append("sdkp=a&");
        DeviceHelper instance = DeviceHelper.getInstance(this.a.getContext());
        stringBuilder.append("status_os=").append(Data.urlEncode(instance.getOSVersionName(), com.qiniu.android.common.Constants.UTF_8)).append(com.alipay.sdk.sys.a.b);
        stringBuilder.append("status_machine=").append(Data.urlEncode(instance.getModel(), com.qiniu.android.common.Constants.UTF_8)).append(com.alipay.sdk.sys.a.b);
        stringBuilder.append("status_version=").append(Data.urlEncode(instance.getOSVersion(), com.qiniu.android.common.Constants.UTF_8)).append(com.alipay.sdk.sys.a.b);
        stringBuilder.append("appId=").append(this.d).append(com.alipay.sdk.sys.a.b);
        Object appName = instance.getAppName();
        if (TextUtils.isEmpty(appName)) {
            stringBuilder.append("appName=").append(Data.urlEncode(str5, com.qiniu.android.common.Constants.UTF_8)).append(com.alipay.sdk.sys.a.b);
        } else {
            stringBuilder.append("appName=").append(Data.urlEncode(appName, com.qiniu.android.common.Constants.UTF_8)).append(com.alipay.sdk.sys.a.b);
        }
        if (str.length() > 40) {
            str = str.substring(40) + "...";
        }
        stringBuilder.append("title=").append(Data.urlEncode(str, com.qiniu.android.common.Constants.UTF_8)).append(com.alipay.sdk.sys.a.b);
        if (str.length() > 80) {
            str.substring(80) + "...";
        }
        stringBuilder.append("summary=").append(Data.urlEncode(str3, com.qiniu.android.common.Constants.UTF_8)).append(com.alipay.sdk.sys.a.b);
        stringBuilder.append("targeturl=").append(Data.urlEncode(str2, com.qiniu.android.common.Constants.UTF_8)).append(com.alipay.sdk.sys.a.b);
        if (!TextUtils.isEmpty(str4)) {
            stringBuilder.append("imageUrl=").append(Data.urlEncode(str4, com.qiniu.android.common.Constants.UTF_8)).append(com.alipay.sdk.sys.a.b);
        }
        stringBuilder.append("site=").append(Data.urlEncode(str5, com.qiniu.android.common.Constants.UTF_8)).append(com.alipay.sdk.sys.a.b);
        stringBuilder.append("type=1");
        i iVar = new i();
        iVar.a(stringBuilder.toString(), false);
        iVar.a(platformActionListener);
        iVar.a(this.d);
        iVar.show(this.a.getContext(), null);
    }

    public void c(String str) {
        this.f = str;
    }

    public HashMap<String, Object> d(String str) {
        ArrayList arrayList = new ArrayList();
        arrayList.add(new KVPair("access_token", this.f));
        arrayList.add(new KVPair("oauth_consumer_key", this.d));
        arrayList.add(new KVPair("openid", this.e));
        ArrayList arrayList2 = new ArrayList();
        arrayList2.add(new KVPair(Network.USER_AGENT, System.getProperties().getProperty("http.agent") + " ArzenAndroidSDK"));
        String a = this.g.a("https://graph.qq.com/user/get_simple_userinfo", arrayList, arrayList2, null, "/user/get_simple_userinfo", c());
        return (a == null || a.length() <= 0) ? null : new Hashon().fromJson(a);
    }

    public HashMap<String, Object> e(String str) {
        ArrayList arrayList = new ArrayList();
        arrayList.add(new KVPair("access_token", str));
        ArrayList arrayList2 = new ArrayList();
        arrayList2.add(new KVPair(Network.USER_AGENT, System.getProperties().getProperty("http.agent") + " ArzenAndroidSDK"));
        String a = this.g.a("https://graph.qq.com/oauth2.0/me", arrayList, arrayList2, null, "/oauth2.0/me", c());
        if (a.startsWith(com.alipay.sdk.authjs.a.c)) {
            while (!a.startsWith("{") && a.length() > 0) {
                a = a.substring(1);
            }
            while (!a.endsWith("}") && a.length() > 0) {
                a = a.substring(0, a.length() - 1);
            }
        }
        return (a == null || a.length() <= 0) ? null : new Hashon().fromJson(a);
    }

    public String getAuthorizeUrl() {
        String urlEncode;
        ShareSDK.logApiEvent("/oauth2.0/authorize", c());
        String b = b();
        try {
            urlEncode = Data.urlEncode(getRedirectUri(), com.qiniu.android.common.Constants.UTF_8);
        } catch (Throwable th) {
            Ln.e(th);
            urlEncode = getRedirectUri();
        }
        return "https://graph.qq.com/oauth2.0/m_authorize?response_type=token&client_id=" + this.d + com.alipay.sdk.sys.a.b + "redirect_uri=" + urlEncode + com.alipay.sdk.sys.a.b + "display=mobile&" + "scope=" + b;
    }

    public b getAuthorizeWebviewClient(g gVar) {
        return new d(gVar);
    }

    public String getRedirectUri() {
        return ServerSetting.DEFAULT_REDIRECT_URI;
    }

    public cn.sharesdk.framework.authorize.f getSSOProcessor(cn.sharesdk.framework.authorize.e eVar) {
        cn.sharesdk.framework.authorize.f hVar = new h(eVar);
        hVar.a((int) Constants.CODE_REQUEST_MIN);
        hVar.a(this.d, b());
        return hVar;
    }
}
