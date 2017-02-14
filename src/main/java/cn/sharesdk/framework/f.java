package cn.sharesdk.framework;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import cn.sharesdk.framework.Platform.ShareParams;
import com.mob.tools.utils.Data;
import com.mob.tools.utils.Hashon;
import com.mob.tools.utils.Ln;
import com.mob.tools.utils.R;
import com.qiniu.android.common.Constants;
import com.umeng.socialize.net.utils.SocializeProtocolConstants;
import com.zxinsight.share.domain.BMPlatform;
import java.lang.reflect.Field;
import java.util.HashMap;

public class f {
    private Platform a;
    private Context b;
    private PlatformDb c;
    private a d;
    private int e;
    private int f;
    private boolean g;
    private boolean h = true;
    private boolean i;

    public f(Platform platform, Context context) {
        this.a = platform;
        this.b = context;
        String name = platform.getName();
        this.c = new PlatformDb(context, name, platform.getVersion());
        a(name);
        this.d = new a();
    }

    private boolean j() {
        boolean z = false;
        if (!ShareSDK.a()) {
            return true;
        }
        String a;
        if (ShareSDK.b()) {
            a = a(this.a.getPlatformId(), "covert_url", null);
            if (a != null) {
                a.trim();
            }
            if (!"false".equals(a)) {
                z = true;
            }
            this.h = z;
            this.a.setNetworkDevinfo();
            return true;
        }
        try {
            HashMap hashMap = new HashMap();
            if (!ShareSDK.a(hashMap)) {
                return false;
            }
            if (ShareSDK.b(hashMap)) {
                a = a(this.a.getPlatformId(), "covert_url", null);
                if (a != null) {
                    a.trim();
                }
                this.h = !"false".equals(a);
                this.a.setNetworkDevinfo();
                return true;
            }
            String fromHashMap = new Hashon().fromHashMap(hashMap);
            if (ShareSDK.isDebug()) {
                System.err.println("Failed to parse network dev-info: " + fromHashMap);
            }
            return false;
        } catch (Throwable th) {
            Ln.w(th);
            return false;
        }
    }

    private String k() {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            if (BMPlatform.NAME_TENCENTWEIBO.equals(this.a.getName())) {
                Ln.e("user id %s ==>>", g().getUserName());
                stringBuilder.append(Data.urlEncode(g().getUserName(), Constants.UTF_8));
            } else {
                stringBuilder.append(Data.urlEncode(g().getUserId(), Constants.UTF_8));
            }
            stringBuilder.append("|").append(Data.urlEncode(g().get("secretType"), Constants.UTF_8));
            stringBuilder.append("|").append(Data.urlEncode(g().get(SocializeProtocolConstants.PROTOCOL_KEY_GENDER), Constants.UTF_8));
            stringBuilder.append("|").append(Data.urlEncode(g().get("birthday"), Constants.UTF_8));
            stringBuilder.append("|").append(Data.urlEncode(g().get("educationJSONArrayStr"), Constants.UTF_8));
            stringBuilder.append("|").append(Data.urlEncode(g().get("workJSONArrayStr"), Constants.UTF_8));
        } catch (Throwable th) {
            Ln.w(th);
        }
        return stringBuilder.toString();
    }

    public int a() {
        return this.e;
    }

    public String a(int i, String str, String str2) {
        Object a = ShareSDK.a(i, str);
        return (TextUtils.isEmpty(a) || "null".equals(a)) ? this.a.getDevinfo(this.a.getName(), str2) : a;
    }

    public String a(Bitmap bitmap) {
        return ShareSDK.a(bitmap);
    }

    public String a(String str, boolean z) {
        long currentTimeMillis = System.currentTimeMillis();
        if (!this.h) {
            Ln.e("getShortLintk use time: " + (System.currentTimeMillis() - currentTimeMillis), new Object[0]);
            return str;
        } else if (TextUtils.isEmpty(str)) {
            Ln.e("getShortLintk use time: " + (System.currentTimeMillis() - currentTimeMillis), new Object[0]);
            return str;
        } else {
            str = ShareSDK.a(str, z, this.a.getPlatformId(), k());
            Ln.e("getShortLintk use time: " + (System.currentTimeMillis() - currentTimeMillis), new Object[0]);
            return str;
        }
    }

    public void a(int i, int i2, String str) {
        c(2, new Object[]{Integer.valueOf(i), Integer.valueOf(i2), str});
    }

    public void a(int i, Object obj) {
        this.d.a(this.a, i, obj);
    }

    public void a(ShareParams shareParams) {
        if (shareParams != null) {
            c(9, shareParams);
        } else if (this.d != null) {
            this.d.onError(this.a, 9, new NullPointerException());
        }
    }

    public void a(PlatformActionListener platformActionListener) {
        this.d.a(platformActionListener);
    }

    public void a(String str) {
        try {
            this.e = R.parseInt(String.valueOf(ShareSDK.b(str, "Id")).trim());
        } catch (Throwable th) {
            if (!(this.a instanceof CustomPlatform) && ShareSDK.isDebug()) {
                System.err.println(this.a.getName() + " failed to parse Id, this will cause method getId() always returens 0");
            }
        }
        try {
            this.f = R.parseInt(String.valueOf(ShareSDK.b(str, "SortId")).trim());
        } catch (Throwable th2) {
            if (!(this.a instanceof CustomPlatform) && ShareSDK.isDebug()) {
                System.err.println(this.a.getName() + " failed to parse SortId, this won't cause any problem, don't worry");
            }
        }
        String b = ShareSDK.b(str, "Enable");
        if (b == null) {
            this.i = true;
            if (!(this.a instanceof CustomPlatform) && ShareSDK.isDebug()) {
                System.err.println(this.a.getName() + " failed to parse Enable, this will cause platform always be enable");
            }
        } else {
            this.i = "true".equals(b.trim());
        }
        this.a.initDevInfo(str);
    }

    public void a(String str, int i, int i2) {
        c(7, new Object[]{Integer.valueOf(i), Integer.valueOf(i2), str});
    }

    public void a(String str, String str2, short s, HashMap<String, Object> hashMap, HashMap<String, String> hashMap2) {
        c(655360 | s, new Object[]{str, str2, hashMap, hashMap2});
    }

    public void a(boolean z) {
        this.g = z;
    }

    public void a(String[] strArr) {
        new h(this, strArr).start();
    }

    public int b() {
        return this.f;
    }

    protected void b(int i, Object obj) {
        Object[] objArr;
        switch (i) {
            case 1:
                if (this.d != null) {
                    this.d.onComplete(this.a, 1, null);
                    return;
                }
                return;
            case 2:
                objArr = (Object[]) obj;
                this.a.getFriendList(((Integer) objArr[0]).intValue(), ((Integer) objArr[1]).intValue(), (String) objArr[2]);
                return;
            case 6:
                this.a.follow((String) obj);
                return;
            case 7:
                objArr = (Object[]) obj;
                this.a.timeline(((Integer) objArr[0]).intValue(), ((Integer) objArr[1]).intValue(), (String) objArr[2]);
                return;
            case 8:
                this.a.userInfor(obj == null ? null : (String) obj);
                return;
            case 9:
                ShareParams shareParams = (ShareParams) obj;
                HashMap toMap = shareParams.toMap();
                for (Field field : shareParams.getClass().getFields()) {
                    if (toMap.get(field.getName()) == null) {
                        Object obj2;
                        field.setAccessible(true);
                        try {
                            obj2 = field.get(shareParams);
                        } catch (Throwable th) {
                            Ln.w(th);
                            obj2 = null;
                        }
                        if (obj2 != null) {
                            toMap.put(field.getName(), obj2);
                        }
                    }
                }
                this.a.doShare(shareParams);
                return;
            default:
                objArr = (Object[]) obj;
                this.a.doCustomerProtocol(String.valueOf(objArr[0]), String.valueOf(objArr[1]), i, (HashMap) objArr[2], (HashMap) objArr[3]);
                return;
        }
    }

    public void b(String str) {
        c(6, str);
    }

    public PlatformActionListener c() {
        return this.d.a();
    }

    protected void c(int i, Object obj) {
        new g(this, i, obj).start();
    }

    public void c(String str) {
        c(8, str);
    }

    public String d(String str) {
        return ShareSDK.a(str);
    }

    public boolean d() {
        return this.c.isValid();
    }

    public boolean e() {
        return this.g;
    }

    public boolean f() {
        return this.i;
    }

    public PlatformDb g() {
        return this.c;
    }

    public void h() {
        this.c.removeAccount();
    }

    protected PlatformActionListener i() {
        return this.d;
    }
}
