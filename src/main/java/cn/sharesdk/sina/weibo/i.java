package cn.sharesdk.sina.weibo;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.framework.a.a;
import cn.sharesdk.framework.authorize.AuthorizeListener;
import cn.sharesdk.framework.authorize.b;
import cn.sharesdk.framework.authorize.f;
import cn.sharesdk.framework.authorize.g;
import cn.sharesdk.framework.e;
import com.mob.tools.network.KVPair;
import com.mob.tools.network.NetworkHelper;
import com.mob.tools.utils.Hashon;
import com.mob.tools.utils.Ln;
import com.mob.tools.utils.R;
import com.tencent.connect.common.Constants;
import com.tencent.open.SocialConstants;
import com.umeng.socialize.net.utils.SocializeProtocolConstants;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

public class i extends e {
    private static i b;
    private String c;
    private String d;
    private String e;
    private String f;
    private String[] g;
    private a h = a.a();

    private i(Platform platform) {
        super(platform);
    }

    public static synchronized i a(Platform platform) {
        i iVar;
        synchronized (i.class) {
            if (b == null) {
                b = new i(platform);
            }
            iVar = b;
        }
        return iVar;
    }

    private HashMap<String, Object> a(String str, float f, float f2) {
        ArrayList arrayList = new ArrayList();
        arrayList.add(new KVPair(SocialConstants.PARAM_SOURCE, this.c));
        arrayList.add(new KVPair("access_token", this.f));
        arrayList.add(new KVPair("status", str));
        arrayList.add(new KVPair("long", String.valueOf(f)));
        arrayList.add(new KVPair("lat", String.valueOf(f2)));
        String b = this.h.b("https://api.weibo.com/2/statuses/update.json", arrayList, "/2/statuses/update.json", c());
        return b != null ? new Hashon().fromJson(b) : null;
    }

    private HashMap<String, Object> a(String str, String str2, float f, float f2) {
        ArrayList arrayList = new ArrayList();
        arrayList.add(new KVPair(SocialConstants.PARAM_SOURCE, this.c));
        arrayList.add(new KVPair("access_token", this.f));
        arrayList.add(new KVPair("status", str));
        arrayList.add(new KVPair("long", String.valueOf(f)));
        arrayList.add(new KVPair("lat", String.valueOf(f2)));
        arrayList.add(new KVPair("url", str2));
        String b = this.h.b("https://api.weibo.com/2/statuses/upload_url_text.json", arrayList, "/2/statuses/upload_url_text.json", c());
        return b != null ? new Hashon().fromJson(b) : null;
    }

    private HashMap<String, Object> b(String str, String str2, float f, float f2) {
        ArrayList arrayList = new ArrayList();
        arrayList.add(new KVPair(SocialConstants.PARAM_SOURCE, this.c));
        arrayList.add(new KVPair("access_token", this.f));
        arrayList.add(new KVPair("status", str2));
        arrayList.add(new KVPair("long", String.valueOf(f)));
        arrayList.add(new KVPair("lat", String.valueOf(f2)));
        String a = this.h.a("https://api.weibo.com/2/statuses/upload.json", arrayList, new KVPair("pic", str), "/2/statuses/upload.json", c());
        return a != null ? new Hashon().fromJson(a) : null;
    }

    public String a(Context context, String str) {
        ArrayList arrayList = new ArrayList();
        arrayList.add(new KVPair(Constants.PARAM_CLIENT_ID, this.c));
        arrayList.add(new KVPair("client_secret", this.d));
        arrayList.add(new KVPair("redirect_uri", this.e));
        arrayList.add(new KVPair("grant_type", "authorization_code"));
        arrayList.add(new KVPair("code", str));
        String b = this.h.b("https://api.weibo.com/oauth2/access_token", arrayList, "/oauth2/access_token", c());
        ShareSDK.logApiEvent("/oauth2/access_token", c());
        return b;
    }

    public HashMap<String, Object> a(int i, int i2, String str) {
        ArrayList arrayList = new ArrayList();
        arrayList.add(new KVPair(SocialConstants.PARAM_SOURCE, this.c));
        Object obj = 1;
        try {
            R.parseLong(str);
        } catch (Throwable th) {
            obj = null;
        }
        if (obj != null) {
            arrayList.add(new KVPair(SocializeProtocolConstants.PROTOCOL_KEY_UID, str));
        } else {
            arrayList.add(new KVPair("screen_name", str));
        }
        arrayList.add(new KVPair("count", String.valueOf(i)));
        arrayList.add(new KVPair("page", String.valueOf(i2)));
        String a = this.h.a("https://api.weibo.com/2/statuses/user_timeline.json", arrayList, "/2/statuses/user_timeline.json", c());
        return a != null ? new Hashon().fromJson(a) : null;
    }

    public HashMap<String, Object> a(String str, String str2, String str3, float f, float f2) {
        if (!TextUtils.isEmpty(str) || !TextUtils.isEmpty(str2) || !TextUtils.isEmpty(str3)) {
            return !TextUtils.isEmpty(str3) ? b(str3, str, f, f2) : !TextUtils.isEmpty(str2) ? a(str, str2, f, f2) : a(str, f, f2);
        } else {
            throw new Throwable("weibo content can not be null!");
        }
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
        arrayList.add(new KVPair(SocialConstants.PARAM_SOURCE, this.c));
        if (this.f != null) {
            arrayList.add(new KVPair("access_token", this.f));
        }
        if (hashMap2 == null || hashMap2.size() <= 0) {
            kVPair = null;
        } else {
            HashMap<String, Object> hashMap3 = null;
            for (Entry entry2 : hashMap2.entrySet()) {
                Object kVPair2 = new KVPair((String) entry2.getKey(), entry2.getValue());
            }
            kVPair = hashMap3;
        }
        try {
            if ("GET".equals(str2.toUpperCase())) {
                httpGet = new NetworkHelper().httpGet(str, arrayList, null, null);
            } else {
                if (Constants.HTTP_POST.equals(str2.toUpperCase())) {
                    httpGet = new NetworkHelper().httpPost(str, arrayList, kVPair, null, null);
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
            a(new j(this, authorizeListener));
        }
    }

    public void a(String str) {
        this.e = str;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void a(java.lang.String r11, cn.sharesdk.framework.Platform.ShareParams r12, cn.sharesdk.framework.PlatformActionListener r13) {
        /*
        r10 = this;
        r0 = r12.getImagePath();
        r1 = r12.getImageUrl();
        r2 = android.text.TextUtils.isEmpty(r0);
        if (r2 == 0) goto L_0x002d;
    L_0x000e:
        r2 = android.text.TextUtils.isEmpty(r1);
        if (r2 != 0) goto L_0x002d;
    L_0x0014:
        r2 = r10.a;
        r2 = r2.getContext();
        r1 = com.mob.tools.utils.BitmapHelper.downloadBitmap(r2, r1);
        r2 = new java.io.File;
        r2.<init>(r1);
        r1 = r2.exists();
        if (r1 == 0) goto L_0x002d;
    L_0x0029:
        r0 = r2.getAbsolutePath();
    L_0x002d:
        r3 = new android.content.Intent;
        r1 = "android.intent.action.SEND";
        r3.<init>(r1);
        r1 = "android.intent.extra.TEXT";
        r3.putExtra(r1, r11);
        r1 = android.text.TextUtils.isEmpty(r0);
        if (r1 != 0) goto L_0x00fc;
    L_0x0041:
        r2 = new java.io.File;
        r2.<init>(r0);
        r1 = r2.exists();
        if (r1 == 0) goto L_0x00ad;
    L_0x004c:
        r1 = "/data/";
        r1 = r0.startsWith(r1);
        if (r1 == 0) goto L_0x011b;
    L_0x0055:
        r1 = r10.a;
        r1 = r1.getContext();
        r4 = "images";
        r4 = com.mob.tools.utils.R.getCachePath(r1, r4);
        r1 = new java.io.File;
        r5 = new java.lang.StringBuilder;
        r5.<init>();
        r6 = java.lang.System.currentTimeMillis();
        r5 = r5.append(r6);
        r6 = r2.getName();
        r5 = r5.append(r6);
        r5 = r5.toString();
        r1.<init>(r4, r5);
        r4 = r1.getAbsolutePath();
        r1.createNewFile();
        r4 = com.mob.tools.utils.R.copyFile(r0, r4);
        if (r4 == 0) goto L_0x011b;
    L_0x008d:
        r2 = "android.intent.extra.STREAM";
        r1 = android.net.Uri.fromFile(r1);
        r3.putExtra(r2, r1);
        r1 = java.net.URLConnection.getFileNameMap();
        r0 = r1.getContentTypeFor(r0);
        if (r0 == 0) goto L_0x00a7;
    L_0x00a1:
        r1 = r0.length();
        if (r1 > 0) goto L_0x00aa;
    L_0x00a7:
        r0 = "image/*";
    L_0x00aa:
        r3.setType(r0);
    L_0x00ad:
        r0 = r10.b();
        if (r0 == 0) goto L_0x0103;
    L_0x00b3:
        r0 = "com.sina.weibo";
        r3.setPackage(r0);
    L_0x00b9:
        r0 = 268435456; // 0x10000000 float:2.5243549E-29 double:1.32624737E-315;
        r3.addFlags(r0);
        r0 = r10.a;
        r0 = r0.getContext();
        r0.startActivity(r3);
        r0 = r10.a;
        r0 = r0.getContext();
        r2 = com.mob.tools.utils.DeviceHelper.getInstance(r0);
        r0 = r10.a;
        r0 = r0.getContext();
        r3 = r0.getPackageName();
        r5 = new java.util.HashMap;
        r5.<init>();
        r0 = "ShareParams";
        r5.put(r0, r12);
        r0 = r2.getTopTaskPackageName();
        r0 = android.text.TextUtils.isEmpty(r0);
        if (r0 == 0) goto L_0x010d;
    L_0x00f0:
        if (r13 == 0) goto L_0x00fb;
    L_0x00f2:
        if (r13 == 0) goto L_0x00fb;
    L_0x00f4:
        r0 = r10.a;
        r1 = 9;
        r13.onComplete(r0, r1, r5);
    L_0x00fb:
        return;
    L_0x00fc:
        r0 = "text/plain";
        r3.setType(r0);
        goto L_0x00ad;
    L_0x0103:
        r0 = "com.sina.weibo";
        r1 = "com.sina.weibo.EditActivity";
        r3.setClassName(r0, r1);
        goto L_0x00b9;
    L_0x010d:
        r6 = 0;
        r8 = 2000; // 0x7d0 float:2.803E-42 double:9.88E-321;
        r0 = new cn.sharesdk.sina.weibo.k;
        r1 = r10;
        r4 = r13;
        r0.<init>(r1, r2, r3, r4, r5);
        com.mob.tools.utils.UIHandler.sendEmptyMessageDelayed(r6, r8, r0);
        goto L_0x00fb;
    L_0x011b:
        r1 = r2;
        goto L_0x008d;
        */
        throw new UnsupportedOperationException("Method not decompiled: cn.sharesdk.sina.weibo.i.a(java.lang.String, cn.sharesdk.framework.Platform$ShareParams, cn.sharesdk.framework.PlatformActionListener):void");
    }

    public void a(String str, String str2) {
        this.c = str;
        this.d = str2;
    }

    public void a(String[] strArr) {
        this.g = strArr;
    }

    public boolean a() {
        Intent intent = new Intent("android.intent.action.SEND");
        intent.setPackage("com.sina.weibo");
        intent.setType("image/*");
        return this.a.getContext().getPackageManager().resolveActivity(intent, 0) != null;
    }

    public HashMap<String, Object> b(int i, int i2, String str) {
        ArrayList arrayList = new ArrayList();
        arrayList.add(new KVPair(SocialConstants.PARAM_SOURCE, this.c));
        if (this.f != null) {
            arrayList.add(new KVPair("access_token", this.f));
        }
        Object obj = 1;
        try {
            R.parseLong(str);
        } catch (Throwable th) {
            obj = null;
        }
        if (obj != null) {
            arrayList.add(new KVPair(SocializeProtocolConstants.PROTOCOL_KEY_UID, str));
        } else {
            arrayList.add(new KVPair("screen_name", str));
        }
        arrayList.add(new KVPair("count", String.valueOf(i)));
        arrayList.add(new KVPair("cursor", String.valueOf(i2 * i)));
        String a = this.h.a("https://api.weibo.com/2/friendships/friends.json", arrayList, "/2/friendships/friends.json", c());
        return a != null ? new Hashon().fromJson(a) : null;
    }

    public void b(String str) {
        this.f = str;
    }

    public boolean b() {
        int i;
        Throwable th;
        try {
            i = this.a.getContext().getPackageManager().getPackageInfo("com.sina.weibo", 0).versionCode;
            try {
                Ln.i("sinaweibo client versionCode ==>> " + i, new Object[0]);
            } catch (Throwable th2) {
                th = th2;
                Ln.e(th);
                return i >= 1914;
            }
        } catch (Throwable th3) {
            th = th3;
            i = 0;
            Ln.e(th);
            if (i >= 1914) {
            }
        }
        if (i >= 1914) {
        }
    }

    public HashMap<String, Object> c(String str) {
        ArrayList arrayList = new ArrayList();
        arrayList.add(new KVPair(SocialConstants.PARAM_SOURCE, this.c));
        if (this.f != null) {
            arrayList.add(new KVPair("access_token", this.f));
        }
        Object obj = 1;
        try {
            R.parseLong(str);
        } catch (Throwable th) {
            obj = null;
        }
        if (obj != null) {
            arrayList.add(new KVPair(SocializeProtocolConstants.PROTOCOL_KEY_UID, str));
        } else {
            arrayList.add(new KVPair("screen_name", str));
        }
        String a = this.h.a("https://api.weibo.com/2/users/show.json", arrayList, "/2/users/show.json", c());
        return a != null ? new Hashon().fromJson(a) : null;
    }

    public HashMap<String, Object> d(String str) {
        ArrayList arrayList = new ArrayList();
        arrayList.add(new KVPair(SocialConstants.PARAM_SOURCE, this.c));
        arrayList.add(new KVPair("access_token", this.f));
        Object obj = 1;
        try {
            R.parseLong(str);
        } catch (Throwable th) {
            obj = null;
        }
        if (obj != null) {
            arrayList.add(new KVPair(SocializeProtocolConstants.PROTOCOL_KEY_UID, str));
        } else {
            arrayList.add(new KVPair("screen_name", str));
        }
        String b = this.h.b("https://api.weibo.com/2/friendships/create.json", arrayList, "/2/friendships/create.json", c());
        return b != null ? new Hashon().fromJson(b) : null;
    }

    public String getAuthorizeUrl() {
        ArrayList arrayList = new ArrayList();
        arrayList.add(new KVPair(Constants.PARAM_CLIENT_ID, this.c));
        arrayList.add(new KVPair("response_type", "code"));
        arrayList.add(new KVPair("redirect_uri", this.e));
        if (this.g != null && this.g.length > 0) {
            arrayList.add(new KVPair("scope", TextUtils.join(",", this.g)));
        }
        arrayList.add(new KVPair("display", "mobile"));
        String str = "https://api.weibo.com/oauth2/authorize?" + R.encodeUrl(arrayList);
        ShareSDK.logApiEvent("/oauth2/authorize", c());
        return str;
    }

    public b getAuthorizeWebviewClient(g gVar) {
        return new f(gVar);
    }

    public String getRedirectUri() {
        return TextUtils.isEmpty(this.e) ? "https://api.weibo.com/oauth2/default.html" : this.e;
    }

    public f getSSOProcessor(cn.sharesdk.framework.authorize.e eVar) {
        f hVar = new h(eVar);
        hVar.a(32973);
        hVar.a(this.c, this.e, new String[0]);
        return hVar;
    }
}
