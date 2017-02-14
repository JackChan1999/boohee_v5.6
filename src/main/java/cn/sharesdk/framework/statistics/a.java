package cn.sharesdk.framework.statistics;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.text.TextUtils;
import android.util.Base64;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.framework.statistics.a.c;
import cn.sharesdk.framework.statistics.a.d;
import cn.sharesdk.framework.statistics.a.e;
import cn.sharesdk.framework.statistics.b.b;
import cn.sharesdk.framework.statistics.b.f;
import cn.sharesdk.framework.statistics.b.g;
import com.boohee.utility.TimeLinePatterns;
import com.mob.tools.network.KVPair;
import com.mob.tools.network.NetworkHelper;
import com.mob.tools.utils.BitmapHelper;
import com.mob.tools.utils.Data;
import com.mob.tools.utils.DeviceHelper;
import com.mob.tools.utils.Hashon;
import com.mob.tools.utils.Ln;
import com.mob.tools.utils.R;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.qiniu.android.common.Constants;
import com.tencent.open.SocialConstants;
import com.tencent.tinker.loader.shareutil.ShareConstants;
import com.umeng.socialize.net.utils.SocializeProtocolConstants;
import com.xiaomi.account.openauth.utils.Network;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.GZIPOutputStream;
import org.json.JSONObject;

public class a {
    public static String b = "http://api.share.mob.com:80";
    static String c;
    static boolean d = false;
    private static a e;
    private static c f;
    private static String g = "http://l.mob.com/url/ShareSdkMapping.do";
    private static String h = "http://up.sharesdk.cn/upload/image";
    Context a;
    private boolean i;
    private boolean j = true;
    private NetworkHelper k = new NetworkHelper();

    public enum a {
        FINISH_SHARE,
        BEFORE_SHARE
    }

    private a() {
    }

    public static a a(Context context) {
        if (e == null) {
            e = new a();
            e.a = context.getApplicationContext();
            f = c.a(e.a);
        }
        return e;
    }

    private String a(Bitmap bitmap, a aVar) {
        try {
            File createTempFile = File.createTempFile("bm_tmp", ".png");
            OutputStream fileOutputStream = new FileOutputStream(createTempFile);
            bitmap.compress(CompressFormat.PNG, 100, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
            return a(createTempFile.getAbsolutePath(), aVar);
        } catch (Throwable th) {
            Ln.e(th);
            return null;
        }
    }

    private String a(String str, a aVar) {
        if (TextUtils.isEmpty(str) || !new File(str).exists()) {
            return null;
        }
        int ceil;
        CompressFormat bmpFormat = BitmapHelper.getBmpFormat(str);
        float f = 200.0f;
        if (aVar == a.BEFORE_SHARE) {
            f = 600.0f;
        }
        Options options = new Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(str, options);
        options.inJustDecodeBounds = false;
        int i = options.outWidth;
        int i2 = options.outHeight;
        if (i >= i2 && ((float) i2) > f) {
            ceil = (int) Math.ceil((double) (((float) options.outHeight) / f));
        } else if (i >= i2 || ((float) i) <= f) {
            return h(str);
        } else {
            ceil = (int) Math.ceil((double) (((float) options.outWidth) / f));
        }
        if (ceil <= 0) {
            ceil = 1;
        }
        options.inSampleSize = ceil;
        options.inPurgeable = true;
        options.inInputShareable = true;
        try {
            Bitmap decodeFile = BitmapFactory.decodeFile(str, options);
            decodeFile.getHeight();
            decodeFile.getWidth();
            File createTempFile = File.createTempFile("bm_tmp2", "." + bmpFormat.name().toLowerCase());
            OutputStream fileOutputStream = new FileOutputStream(createTempFile);
            decodeFile.compress(bmpFormat, 80, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
            return h(createTempFile.getAbsolutePath());
        } catch (Throwable th) {
            Ln.e(th);
            return null;
        }
    }

    private String a(String str, String str2, String str3, int i, String str4) {
        if (TextUtils.isEmpty(str) || !this.j) {
            return str;
        }
        ArrayList arrayList = new ArrayList();
        Pattern compile = Pattern.compile(str3);
        Matcher matcher = compile.matcher(str);
        while (matcher.find()) {
            String group = matcher.group();
            if (group != null && group.length() > 0) {
                arrayList.add(group);
            }
        }
        if (arrayList.size() == 0) {
            return str;
        }
        ArrayList arrayList2 = new ArrayList();
        arrayList2.add(new KVPair("key", str2));
        for (int i2 = 0; i2 < arrayList.size(); i2++) {
            arrayList2.add(new KVPair("urls", ((String) arrayList.get(i2)).toString()));
        }
        arrayList2.add(new KVPair("deviceid", DeviceHelper.getInstance(this.a).getDeviceKey()));
        arrayList2.add(new KVPair("snsplat", String.valueOf(i)));
        CharSequence d = d(str2, str4);
        if (TextUtils.isEmpty(d)) {
            return str;
        }
        String httpPost;
        arrayList2.add(new KVPair("m", d));
        ArrayList arrayList3 = new ArrayList();
        arrayList3.add(new KVPair(Network.USER_AGENT, c));
        ArrayList arrayList4 = new ArrayList();
        arrayList4.add(new KVPair("http.socket.timeout", Integer.valueOf(BaseImageDownloader.DEFAULT_HTTP_CONNECT_TIMEOUT)));
        arrayList4.add(new KVPair("http.connection.timeout", Integer.valueOf(BaseImageDownloader.DEFAULT_HTTP_CONNECT_TIMEOUT)));
        try {
            httpPost = this.k.httpPost(g, arrayList2, null, arrayList3, arrayList4);
        } catch (Throwable th) {
            Ln.e(th);
            httpPost = null;
        }
        if (TextUtils.isEmpty(httpPost)) {
            this.j = false;
            return str;
        }
        int intValue;
        HashMap fromJson = new Hashon().fromJson(httpPost);
        try {
            intValue = ((Integer) fromJson.get("status")).intValue();
        } catch (Throwable th2) {
            Ln.e(th2);
            intValue = -1;
        }
        if (intValue != 200) {
            return str;
        }
        ArrayList arrayList5 = (ArrayList) fromJson.get("data");
        fromJson = new HashMap();
        Iterator it = arrayList5.iterator();
        while (it.hasNext()) {
            HashMap hashMap = (HashMap) it.next();
            fromJson.put(String.valueOf(hashMap.get(SocialConstants.PARAM_SOURCE)), String.valueOf(hashMap.get("surl")));
        }
        Matcher matcher2 = compile.matcher(str);
        StringBuilder stringBuilder = new StringBuilder();
        intValue = 0;
        while (matcher2.find()) {
            stringBuilder.append(str.substring(intValue, matcher2.start()));
            stringBuilder.append((String) fromJson.get(matcher2.group()));
            intValue = matcher2.end();
        }
        stringBuilder.append(str.substring(intValue, str.length()));
        Ln.w("> SERVER_SHORT_LINK_URL content after replace link ===  %s", stringBuilder.toString());
        return stringBuilder.toString();
    }

    private String a(String str, byte[] bArr) {
        String encodeToString;
        Throwable th;
        try {
            encodeToString = Base64.encodeToString(Data.AES128Encode(bArr, str), 0);
            try {
                if (encodeToString.contains("\n")) {
                    encodeToString = encodeToString.replace("\n", "");
                }
            } catch (Throwable th2) {
                th = th2;
                Ln.e(th);
                return encodeToString;
            }
        } catch (Throwable th3) {
            Throwable th4 = th3;
            encodeToString = null;
            th = th4;
            Ln.e(th);
            return encodeToString;
        }
        return encodeToString;
    }

    private String b() {
        return b + "/date";
    }

    private String c() {
        return b + "/log4";
    }

    private boolean c(String str, String str2) {
        try {
            ArrayList arrayList = new ArrayList();
            arrayList.add(new KVPair("m", str));
            arrayList.add(new KVPair("t", str2));
            ArrayList arrayList2 = new ArrayList();
            arrayList2.add(new KVPair(Network.USER_AGENT, c));
            ArrayList arrayList3 = new ArrayList();
            arrayList3.add(new KVPair("http.socket.timeout", Integer.valueOf(30000)));
            arrayList3.add(new KVPair("http.connection.timeout", Integer.valueOf(30000)));
            Ln.i("> %s  resp: %s", c(), this.k.httpPost(c(), arrayList, null, arrayList2, arrayList3));
            return this.k.httpPost(c(), arrayList, null, arrayList2, arrayList3) != null;
        } catch (Throwable th) {
            Ln.e(th);
            return false;
        }
    }

    private String d() {
        return b + "/data2";
    }

    private String d(String str, String str2) {
        DeviceHelper instance = DeviceHelper.getInstance(this.a);
        boolean b = f.b();
        boolean c = f.c();
        StringBuilder stringBuilder = new StringBuilder();
        try {
            stringBuilder.append(Data.urlEncode(instance.getPackageName(), Constants.UTF_8)).append("|");
            stringBuilder.append(Data.urlEncode(instance.getAppVersionName(), Constants.UTF_8)).append("|");
            stringBuilder.append(Data.urlEncode(String.valueOf(50000 + ShareSDK.getSDKVersionCode()), Constants.UTF_8)).append("|");
            stringBuilder.append(Data.urlEncode(String.valueOf(instance.getPlatformCode()), Constants.UTF_8)).append("|");
            stringBuilder.append(Data.urlEncode(instance.getDetailNetworkTypeForStatic(), Constants.UTF_8)).append("|");
            if (b) {
                stringBuilder.append(Data.urlEncode(instance.getOSVersion(), Constants.UTF_8)).append("|");
                stringBuilder.append(Data.urlEncode(instance.getScreenSize(), Constants.UTF_8)).append("|");
                stringBuilder.append(Data.urlEncode(instance.getManufacturer(), Constants.UTF_8)).append("|");
                stringBuilder.append(Data.urlEncode(instance.getModel(), Constants.UTF_8)).append("|");
                stringBuilder.append(Data.urlEncode(instance.getCarrier(), Constants.UTF_8)).append("|");
            } else {
                stringBuilder.append("|||||");
            }
            if (c) {
                stringBuilder.append(str2);
            } else {
                stringBuilder.append(str2.split("\\|")[0]);
                stringBuilder.append("|||||");
            }
            return a(stringBuilder.toString(), Data.rawMD5(String.format("%s:%s", new Object[]{instance.getDeviceKey(), str})));
        } catch (Throwable th) {
            Ln.e(th);
            return "";
        }
    }

    private String e() {
        return b + "/snsconf";
    }

    private String f() {
        return b + "/conf4";
    }

    private String g() {
        return b + "/conn";
    }

    private long h() {
        if (!f.h()) {
            return 0;
        }
        String str = "{}";
        try {
            str = this.k.httpGet(b(), null, null, null);
        } catch (Throwable th) {
            Ln.e(th);
        }
        HashMap fromJson = new Hashon().fromJson(str);
        if (!fromJson.containsKey("timestamp")) {
            return f.a();
        }
        try {
            long currentTimeMillis = System.currentTimeMillis() - R.parseLong(String.valueOf(fromJson.get("timestamp")));
            f.a("service_time", Long.valueOf(currentTimeMillis));
            return currentTimeMillis;
        } catch (Throwable th2) {
            Ln.w(th2);
            return f.a();
        }
    }

    private String h(String str) {
        Ln.i(" upload file , server url = %s, file path = %s", h, str);
        try {
            KVPair kVPair = new KVPair("file", str);
            ArrayList arrayList = new ArrayList();
            arrayList.add(new KVPair(Network.USER_AGENT, c));
            String httpPost = this.k.httpPost(r1, null, kVPair, arrayList, null);
            Ln.i("upload file response == %s", httpPost);
            if (httpPost == null || httpPost.length() <= 0) {
                return null;
            }
            int parseInt;
            HashMap fromJson = new Hashon().fromJson(httpPost);
            if (fromJson.containsKey("status")) {
                try {
                    parseInt = R.parseInt(String.valueOf(fromJson.get("status")));
                } catch (Throwable th) {
                    parseInt = -1;
                }
            } else {
                parseInt = -1;
            }
            if (parseInt != 200) {
                return null;
            }
            return fromJson.containsKey("url") ? fromJson.get("url").toString() : null;
        } catch (Throwable th2) {
            Ln.e(th2);
            return null;
        }
    }

    private String i(String str) {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(str.getBytes());
        OutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        String str2 = null;
        try {
            GZIPOutputStream gZIPOutputStream = new GZIPOutputStream(byteArrayOutputStream);
            byte[] bArr = new byte[1024];
            while (true) {
                int read = byteArrayInputStream.read(bArr, 0, 1024);
                if (read == -1) {
                    break;
                }
                gZIPOutputStream.write(bArr, 0, read);
            }
            gZIPOutputStream.flush();
            gZIPOutputStream.close();
            byte[] toByteArray = byteArrayOutputStream.toByteArray();
            byteArrayOutputStream.flush();
            byteArrayOutputStream.close();
            byteArrayInputStream.close();
            str2 = Base64.encodeToString(toByteArray, 2);
        } catch (Throwable e) {
            Ln.e(e);
        }
        return str2;
    }

    private JSONObject i() {
        JSONObject jSONObject = new JSONObject();
        DeviceHelper instance = DeviceHelper.getInstance(this.a);
        try {
            jSONObject.put(SocializeProtocolConstants.PROTOCOL_KEY_MAC, instance.getMacAddress());
            jSONObject.put(SocializeProtocolConstants.PROTOCOL_KEY_UDID, instance.getDeviceId());
            jSONObject.put("model", instance.getModel());
            jSONObject.put("factory", instance.getManufacturer());
            jSONObject.put("plat", instance.getPlatformCode());
            jSONObject.put("sysver", instance.getOSVersion());
            jSONObject.put("breaked", false);
            jSONObject.put("screensize", instance.getScreenSize());
            jSONObject.put("androidid", instance.getAndroidID());
            CharSequence advertisingID = instance.getAdvertisingID();
            if (!TextUtils.isEmpty(advertisingID)) {
                jSONObject.put("adsid", advertisingID);
            }
        } catch (Throwable e) {
            Ln.e(e);
        }
        return jSONObject;
    }

    private String j(String str) {
        JSONObject i = i();
        DeviceHelper instance = DeviceHelper.getInstance(this.a);
        try {
            i.put("type", "DEVICE");
            i.put("key", instance.getDeviceKey());
            i.put("carrier", instance.getCarrier());
            i.put("appkey", str);
            i.put("apppkg", instance.getPackageName());
            i.put("appver", String.valueOf(instance.getAppVersion()));
            i.put("sdkver", 50000 + ShareSDK.getSDKVersionCode());
            i.put("networktype", instance.getDetailNetworkTypeForStatic());
        } catch (Throwable e) {
            Ln.e(e);
        }
        return i.toString();
    }

    private JSONObject j() {
        JSONObject jSONObject = new JSONObject();
        DeviceHelper instance = DeviceHelper.getInstance(this.a);
        try {
            jSONObject.put("type", "DEVEXT");
            jSONObject.put("plat", instance.getPlatformCode());
            jSONObject.put("device", instance.getDeviceKey());
            jSONObject.put("phonename", instance.getBluetoothName());
            jSONObject.put("signmd5", instance.getSignMD5());
            if (instance.getDetailNetworkTypeForStatic().equals("wifi")) {
                jSONObject.put("ssid", instance.getSSID());
                jSONObject.put("bssid", instance.getBssid());
            }
        } catch (Throwable e) {
            Ln.e(e);
        }
        Ln.d(" networkType == getDeviceExtInfo == %s", jSONObject.toString());
        return jSONObject;
    }

    public String a(Bitmap bitmap) {
        return a(bitmap, a.BEFORE_SHARE);
    }

    public String a(String str, String str2, int i, boolean z, String str3) {
        if (!f.h()) {
            return str;
        }
        String a;
        Ln.w("> SERVER_SHORT_LINK_URL content before replace link ===  %s", str);
        if (z) {
            a = a(str, str2, "<a[^>]*?href[\\s]*=[\\s]*[\"']?([^'\">]+?)['\"]?>", i, str3);
            if (!(a == null || a.equals(str))) {
                return a;
            }
        }
        a = a(str, str2, "(http://|https://){1}[\\w\\.\\-/:\\?&%=,;\\[\\]\\{\\}`~!@#\\$\\^\\*\\(\\)_\\+\\\\\\|]+", i, str3);
        return (a == null || a.equals(str)) ? str : a;
    }

    public void a() {
        if (f.h()) {
            if (!"none".equals(DeviceHelper.getInstance(this.a).getDetailNetworkTypeForStatic())) {
                ArrayList a = e.a(this.a);
                for (int i = 0; i < a.size(); i++) {
                    d dVar = (d) a.get(i);
                    if (dVar.b.size() == 1 ? c(dVar.a, "0") : c(i(dVar.a), "1")) {
                        e.a(this.a, dVar.b);
                    }
                }
            }
        }
    }

    public void a(cn.sharesdk.framework.statistics.b.c cVar) {
        if (f.h()) {
            DeviceHelper instance = DeviceHelper.getInstance(this.a);
            String detailNetworkTypeForStatic = instance.getDetailNetworkTypeForStatic();
            if ((cVar instanceof g) && !detailNetworkTypeForStatic.endsWith("none")) {
                c = (instance.getPackageName() + "/" + instance.getAppVersionName()) + " " + ("ShareSDK/" + ShareSDK.getSDKVersionName()) + " " + ("Android/" + instance.getOSVersion());
            } else if (cVar instanceof b) {
                boolean c = f.c();
                Object obj = ((b) cVar).c;
                if (!c || TextUtils.isEmpty(obj)) {
                    ((b) cVar).d = null;
                    ((b) cVar).c = null;
                } else {
                    ((b) cVar).c = Data.Base64AES(obj, cVar.f.substring(0, 16));
                }
            } else if (cVar instanceof f) {
                f fVar = (f) cVar;
                int d = f.d();
                boolean c2 = f.c();
                cn.sharesdk.framework.statistics.b.f.a aVar = fVar.d;
                if (d == 1 || (d == 0 && this.i)) {
                    int i;
                    int size = (aVar == null || aVar.e == null) ? 0 : aVar.e.size();
                    for (i = 0; i < size; i++) {
                        detailNetworkTypeForStatic = a((String) aVar.e.get(i), a.FINISH_SHARE);
                        if (detailNetworkTypeForStatic != null) {
                            aVar.d.add(detailNetworkTypeForStatic);
                        }
                    }
                    size = (aVar == null || aVar.f == null) ? 0 : aVar.f.size();
                    for (i = 0; i < size; i++) {
                        detailNetworkTypeForStatic = a((Bitmap) aVar.f.get(i), a.FINISH_SHARE);
                        if (detailNetworkTypeForStatic != null) {
                            aVar.d.add(detailNetworkTypeForStatic);
                        }
                    }
                } else {
                    fVar.d = null;
                }
                if (!c2) {
                    fVar.n = null;
                }
            }
            if (!f.b()) {
                cVar.m = null;
            }
            long a = f.a();
            if (a == 0) {
                a = h();
            }
            cVar.e = System.currentTimeMillis() - a;
            Ln.i(" insert event in DB == %s", cVar.toString());
            e.a(this.a, cVar.toString(), cVar.e);
        }
    }

    public void a(String str, String str2) {
        f.b(str, str2);
    }

    public void a(String str, ArrayList<HashMap<String, String>> arrayList) {
        if (f.h()) {
            HashMap hashMap = new HashMap();
            hashMap.put("type", str);
            DeviceHelper instance = DeviceHelper.getInstance(this.a);
            hashMap.put("plat", Integer.valueOf(instance.getPlatformCode()));
            hashMap.put("device", instance.getDeviceKey());
            hashMap.put("list", arrayList);
            Ln.d(" upload apps info == %s", new Hashon().fromHashMap(hashMap));
            ArrayList arrayList2 = new ArrayList();
            arrayList2.add(new KVPair("m", Data.Base64AES(r0, "sdk.sharesdk.sdk")));
            ArrayList arrayList3 = new ArrayList();
            arrayList3.add(new KVPair(Network.USER_AGENT, c));
            ArrayList arrayList4 = new ArrayList();
            arrayList4.add(new KVPair("http.socket.timeout", Integer.valueOf(30000)));
            arrayList4.add(new KVPair("http.connection.timeout", Integer.valueOf(30000)));
            String httpPost = this.k.httpPost(d(), arrayList2, null, arrayList3, arrayList4);
            Ln.i("> APPS_UNFINISHED  resp: %s", httpPost);
        }
    }

    public void a(boolean z) {
        this.i = z;
    }

    public boolean a(String str) {
        Calendar instance = Calendar.getInstance();
        instance.setTimeInMillis(f.i().longValue());
        int i = instance.get(1);
        int i2 = instance.get(2);
        int i3 = instance.get(5);
        instance.setTimeInMillis(System.currentTimeMillis());
        int i4 = instance.get(1);
        int i5 = instance.get(2);
        int i6 = instance.get(5);
        if (i == i4 || i2 == i5 || i3 == i6) {
            return f.h();
        }
        String httpPost;
        try {
            ArrayList arrayList = new ArrayList();
            arrayList.add(new KVPair("appkey", str));
            ArrayList arrayList2 = new ArrayList();
            arrayList2.add(new KVPair(Network.USER_AGENT, c));
            ArrayList arrayList3 = new ArrayList();
            arrayList3.add(new KVPair("http.socket.timeout", Integer.valueOf(30000)));
            arrayList3.add(new KVPair("http.connection.timeout", Integer.valueOf(30000)));
            httpPost = this.k.httpPost(g(), arrayList, null, arrayList2, arrayList3);
            if (TextUtils.isEmpty(httpPost)) {
                httpPost = "{}";
            }
            Ln.i(" get server connection response == %s", httpPost);
        } catch (Throwable th) {
            Ln.e(th);
            httpPost = "{}";
        }
        HashMap fromJson = new Hashon().fromJson(httpPost);
        boolean parseBoolean = fromJson.containsKey(ShareConstants.RES_PATH) ? Boolean.parseBoolean(fromJson.get(ShareConstants.RES_PATH).toString()) : true;
        f.a(parseBoolean);
        if ("{}".equals(httpPost)) {
            return parseBoolean;
        }
        f.b(System.currentTimeMillis());
        return parseBoolean;
    }

    public long b(String str) {
        String httpPost;
        long currentTimeMillis;
        long j;
        Throwable th;
        HashMap hashMap;
        String obj;
        String obj2;
        Object valueOf;
        Object valueOf2;
        long j2 = 0;
        if (!f.h()) {
            return 0;
        }
        DeviceHelper instance = DeviceHelper.getInstance(this.a);
        f.a(System.currentTimeMillis());
        try {
            ArrayList arrayList = new ArrayList();
            arrayList.add(new KVPair("appkey", str));
            arrayList.add(new KVPair("device", instance.getDeviceKey()));
            arrayList.add(new KVPair("plat", String.valueOf(instance.getPlatformCode())));
            arrayList.add(new KVPair("apppkg", instance.getPackageName()));
            arrayList.add(new KVPair("appver", String.valueOf(instance.getAppVersion())));
            arrayList.add(new KVPair("sdkver", String.valueOf(50000 + ShareSDK.getSDKVersionCode())));
            arrayList.add(new KVPair("networktype", instance.getDetailNetworkTypeForStatic()));
            ArrayList arrayList2 = new ArrayList();
            arrayList2.add(new KVPair(Network.USER_AGENT, c));
            ArrayList arrayList3 = new ArrayList();
            arrayList3.add(new KVPair("http.socket.timeout", Integer.valueOf(10000)));
            arrayList3.add(new KVPair("http.connection.timeout", Integer.valueOf(10000)));
            httpPost = this.k.httpPost(f(), arrayList, null, arrayList2, arrayList3);
            if (TextUtils.isEmpty(httpPost)) {
                httpPost = "{}";
            }
            Ln.i(" get server config response == %s", httpPost);
        } catch (Throwable th2) {
            Ln.e(th2);
            httpPost = "{}";
        }
        HashMap fromJson = new Hashon().fromJson(httpPost);
        if (fromJson.containsKey("status")) {
            int parseInt;
            try {
                parseInt = R.parseInt(String.valueOf(fromJson.get("status")));
            } catch (Throwable th22) {
                Ln.w(th22);
                parseInt = -200;
            }
            if (parseInt == -200) {
                if (!ShareSDK.isDebug()) {
                    return 0;
                }
                System.err.print(String.valueOf(fromJson.get("error")));
                return 0;
            }
        }
        if (fromJson.containsKey("timestamp")) {
            try {
                j2 = R.parseLong(fromJson.get("timestamp").toString());
                currentTimeMillis = System.currentTimeMillis() - j2;
                try {
                    f.a("service_time", Long.valueOf(currentTimeMillis));
                    j = currentTimeMillis;
                } catch (Throwable th3) {
                    th = th3;
                    Ln.w(th);
                    j = currentTimeMillis;
                    if (fromJson.containsKey("switchs")) {
                        hashMap = new HashMap();
                        hashMap = (HashMap) fromJson.get("switchs");
                        obj = hashMap.get("device").toString();
                        obj2 = hashMap.get("share").toString();
                        httpPost = hashMap.get("auth").toString();
                        f.d(obj);
                        f.f(obj2);
                        f.e(httpPost);
                    }
                    valueOf = String.valueOf(fromJson.get("requesthost"));
                    valueOf2 = String.valueOf(fromJson.get("requestport"));
                    b = TimeLinePatterns.WEB_SCHEME + valueOf + ":" + valueOf2;
                    d(str);
                    c(str);
                    return j;
                }
            } catch (Throwable th222) {
                th = th222;
                currentTimeMillis = j2;
                Ln.w(th);
                j = currentTimeMillis;
                if (fromJson.containsKey("switchs")) {
                    hashMap = new HashMap();
                    hashMap = (HashMap) fromJson.get("switchs");
                    obj = hashMap.get("device").toString();
                    obj2 = hashMap.get("share").toString();
                    httpPost = hashMap.get("auth").toString();
                    f.d(obj);
                    f.f(obj2);
                    f.e(httpPost);
                }
                valueOf = String.valueOf(fromJson.get("requesthost"));
                valueOf2 = String.valueOf(fromJson.get("requestport"));
                b = TimeLinePatterns.WEB_SCHEME + valueOf + ":" + valueOf2;
                d(str);
                c(str);
                return j;
            }
        }
        j = 0;
        if (fromJson.containsKey("switchs")) {
            hashMap = new HashMap();
            hashMap = (HashMap) fromJson.get("switchs");
            obj = hashMap.get("device").toString();
            obj2 = hashMap.get("share").toString();
            httpPost = hashMap.get("auth").toString();
            f.d(obj);
            f.f(obj2);
            f.e(httpPost);
        }
        if (fromJson.containsKey("requesthost") && fromJson.containsKey("requestport")) {
            valueOf = String.valueOf(fromJson.get("requesthost"));
            valueOf2 = String.valueOf(fromJson.get("requestport"));
            if (!(TextUtils.isEmpty(valueOf) || TextUtils.isEmpty(valueOf2))) {
                b = TimeLinePatterns.WEB_SCHEME + valueOf + ":" + valueOf2;
            }
        }
        d(str);
        c(str);
        return j;
    }

    public String b(String str, String str2) {
        return new String(Data.AES128Decode(Data.rawMD5(str + ":" + DeviceHelper.getInstance(this.a).getDeviceKey()), Base64.decode(str2, 0)), "UTF-8");
    }

    public void c(String str) {
        if (f.h()) {
            try {
                String f = f.f();
                String Base64AES = Data.Base64AES(j().toString(), "sdk.sharesdk.sdk");
                if (!Base64AES.equals(f)) {
                    f.i(Base64AES);
                    ArrayList arrayList = new ArrayList();
                    arrayList.add(new KVPair("m", Base64AES));
                    ArrayList arrayList2 = new ArrayList();
                    arrayList2.add(new KVPair(Network.USER_AGENT, c));
                    ArrayList arrayList3 = new ArrayList();
                    arrayList3.add(new KVPair("http.socket.timeout", Integer.valueOf(10000)));
                    arrayList3.add(new KVPair("http.connection.timeout", Integer.valueOf(10000)));
                    f = this.k.httpPost(d(), arrayList, null, arrayList2, arrayList3);
                    Ln.i("> DEVICE_EXT_DATA_UNFINISHED  resp: %s", f);
                }
            } catch (Throwable th) {
                Ln.e(th);
            }
        }
    }

    public void d(String str) {
        if (f.h()) {
            try {
                String trim = f.e().trim();
                String trim2 = i().toString().trim();
                if (!trim2.equals(trim)) {
                    f.h(trim2);
                    Ln.d(" curBaseDeviceInfo == %s", trim2);
                    ArrayList arrayList = new ArrayList();
                    arrayList.add(new KVPair("m", Data.Base64AES(j(str), "sdk.sharesdk.sdk")));
                    ArrayList arrayList2 = new ArrayList();
                    arrayList2.add(new KVPair(Network.USER_AGENT, c));
                    ArrayList arrayList3 = new ArrayList();
                    arrayList3.add(new KVPair("http.socket.timeout", Integer.valueOf(30000)));
                    arrayList3.add(new KVPair("http.connection.timeout", Integer.valueOf(30000)));
                    trim = this.k.httpPost(d(), arrayList, null, arrayList2, arrayList3);
                    Ln.i("> DEVICE_UNFINISHED  resp: %s", trim);
                }
            } catch (Throwable th) {
                Ln.e(th);
            }
        }
    }

    public String e(String str) {
        return a(str, a.BEFORE_SHARE);
    }

    public HashMap<String, Object> f(String str) {
        try {
            HashMap<String, Object> hashMap = new HashMap();
            String g = f.g(str);
            return !TextUtils.isEmpty(g) ? new Hashon().fromJson(g) : hashMap;
        } catch (Throwable th) {
            Ln.w(th);
            return new HashMap();
        }
    }

    public String g(String str) {
        ArrayList arrayList = new ArrayList();
        arrayList.add(new KVPair("appkey", str));
        arrayList.add(new KVPair("device", DeviceHelper.getInstance(this.a).getDeviceKey()));
        ArrayList arrayList2 = new ArrayList();
        arrayList2.add(new KVPair(Network.USER_AGENT, c));
        ArrayList arrayList3 = new ArrayList();
        arrayList3.add(new KVPair("http.socket.timeout", Integer.valueOf(10000)));
        arrayList3.add(new KVPair("http.connection.timeout", Integer.valueOf(10000)));
        return this.k.httpPost(e(), arrayList, null, arrayList2, arrayList3);
    }
}
