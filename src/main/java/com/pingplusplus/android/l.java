package com.pingplusplus.android;

import android.content.Context;
import android.os.Build;
import android.os.Build.VERSION;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;

import com.boohee.utils.Coder;
import com.tencent.connect.common.Constants;

import java.io.InputStream;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.UUID;

import org.json.JSONObject;

public class l {
    private static Context e;
    String a;
    String b;
    String c;
    String d;

    public l(Context context) {
        if (context != null) {
            b(context);
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static com.pingplusplus.android.k a(java.lang.String r6, java.lang.Object r7, java
            .lang.String r8, java.util.Map r9) {
        /*
        r4 = 5000; // 0x1388 float:7.006E-42 double:2.4703E-320;
        r2 = 0;
        r0 = new java.net.URL;	 Catch:{ MalformedURLException -> 0x000c }
        r0.<init>(r6);	 Catch:{ MalformedURLException -> 0x000c }
    L_0x0008:
        if (r0 != 0) goto L_0x0012;
    L_0x000a:
        r0 = r2;
    L_0x000b:
        return r0;
    L_0x000c:
        r0 = move-exception;
        com.pingplusplus.android.PingppLog.a(r0);
        r0 = r2;
        goto L_0x0008;
    L_0x0012:
        r1 = r0.getProtocol();	 Catch:{ IOException -> 0x0031 }
        r3 = "https";
        r1 = r1.equals(r3);	 Catch:{ IOException -> 0x0031 }
        if (r1 == 0) goto L_0x002a;
    L_0x001f:
        r0 = r0.openConnection();	 Catch:{ IOException -> 0x0031 }
        r0 = (javax.net.ssl.HttpsURLConnection) r0;	 Catch:{ IOException -> 0x0031 }
    L_0x0025:
        r3 = r0;
    L_0x0026:
        if (r3 != 0) goto L_0x0037;
    L_0x0028:
        r0 = r2;
        goto L_0x000b;
    L_0x002a:
        r0 = r0.openConnection();	 Catch:{ IOException -> 0x0031 }
        r0 = (java.net.HttpURLConnection) r0;	 Catch:{ IOException -> 0x0031 }
        goto L_0x0025;
    L_0x0031:
        r0 = move-exception;
        com.pingplusplus.android.PingppLog.a(r0);
        r3 = r2;
        goto L_0x0026;
    L_0x0037:
        r3.setConnectTimeout(r4);
        r3.setReadTimeout(r4);
        r0 = 0;
        r3.setUseCaches(r0);
        r0 = d();
        r0 = r0.entrySet();
        r4 = r0.iterator();
    L_0x004d:
        r0 = r4.hasNext();
        if (r0 == 0) goto L_0x0069;
    L_0x0053:
        r0 = r4.next();
        r0 = (java.util.Map.Entry) r0;
        r1 = r0.getKey();
        r1 = (java.lang.String) r1;
        r0 = r0.getValue();
        r0 = (java.lang.String) r0;
        r3.setRequestProperty(r1, r0);
        goto L_0x004d;
    L_0x0069:
        r0 = r9.entrySet();
        r4 = r0.iterator();
    L_0x0071:
        r0 = r4.hasNext();
        if (r0 == 0) goto L_0x008d;
    L_0x0077:
        r0 = r4.next();
        r0 = (java.util.Map.Entry) r0;
        r1 = r0.getKey();
        r1 = (java.lang.String) r1;
        r0 = r0.getValue();
        r0 = (java.lang.String) r0;
        r3.setRequestProperty(r1, r0);
        goto L_0x0071;
    L_0x008d:
        r3.setRequestMethod(r8);	 Catch:{ IOException -> 0x010e }
        r0 = "POST";
        r0 = r8.equals(r0);	 Catch:{ IOException -> 0x010e }
        if (r0 == 0) goto L_0x00bf;
    L_0x0099:
        r0 = 1;
        r3.setDoOutput(r0);	 Catch:{ IOException -> 0x010e }
        r0 = "Content-Type";
        r1 = "application/json; charset=utf-8";
        r3.setRequestProperty(r0, r1);	 Catch:{ IOException -> 0x010e }
        r0 = r7 instanceof java.lang.String;	 Catch:{ IOException -> 0x010e }
        if (r0 == 0) goto L_0x00e2;
    L_0x00aa:
        r7 = (java.lang.String) r7;	 Catch:{ IOException -> 0x010e }
    L_0x00ac:
        r1 = r3.getOutputStream();	 Catch:{ all -> 0x0106 }
        r0 = "UTF-8";
        r0 = r7.getBytes(r0);	 Catch:{ all -> 0x0127 }
        r1.write(r0);	 Catch:{ all -> 0x0127 }
        if (r1 == 0) goto L_0x00bf;
    L_0x00bc:
        r1.close();	 Catch:{ IOException -> 0x010e }
    L_0x00bf:
        r4 = r3.getResponseCode();	 Catch:{ IOException -> 0x010e }
        r0 = 200; // 0xc8 float:2.8E-43 double:9.9E-322;
        if (r4 < r0) goto L_0x0118;
    L_0x00c7:
        r0 = 300; // 0x12c float:4.2E-43 double:1.48E-321;
        if (r4 >= r0) goto L_0x0118;
    L_0x00cb:
        r0 = r3.getInputStream();	 Catch:{ IOException -> 0x010e }
        r0 = a(r0);	 Catch:{ IOException -> 0x010e }
        r1 = r0;
    L_0x00d4:
        r5 = r3.getHeaderFields();	 Catch:{ IOException -> 0x010e }
        r0 = new com.pingplusplus.android.k;	 Catch:{ IOException -> 0x010e }
        r0.<init>(r4, r1, r5);	 Catch:{ IOException -> 0x010e }
        r3.disconnect();
        goto L_0x000b;
    L_0x00e2:
        r0 = r7 instanceof java.util.Map;	 Catch:{ IOException -> 0x010e }
        if (r0 == 0) goto L_0x00f2;
    L_0x00e6:
        r0 = new org.json.JSONObject;	 Catch:{ IOException -> 0x010e }
        r7 = (java.util.Map) r7;	 Catch:{ IOException -> 0x010e }
        r0.<init>(r7);	 Catch:{ IOException -> 0x010e }
        r7 = r0.toString();	 Catch:{ IOException -> 0x010e }
        goto L_0x00ac;
    L_0x00f2:
        r0 = r7 instanceof java.util.List;	 Catch:{ IOException -> 0x010e }
        if (r0 == 0) goto L_0x0102;
    L_0x00f6:
        r0 = new org.json.JSONArray;	 Catch:{ IOException -> 0x010e }
        r7 = (java.util.List) r7;	 Catch:{ IOException -> 0x010e }
        r0.<init>(r7);	 Catch:{ IOException -> 0x010e }
        r7 = r0.toString();	 Catch:{ IOException -> 0x010e }
        goto L_0x00ac;
    L_0x0102:
        r7 = "{}";
        goto L_0x00ac;
    L_0x0106:
        r0 = move-exception;
        r1 = r2;
    L_0x0108:
        if (r1 == 0) goto L_0x010d;
    L_0x010a:
        r1.close();	 Catch:{ IOException -> 0x010e }
    L_0x010d:
        throw r0;	 Catch:{ IOException -> 0x010e }
    L_0x010e:
        r0 = move-exception;
        com.pingplusplus.android.PingppLog.a(r0);	 Catch:{ all -> 0x0122 }
        r3.disconnect();
        r0 = r2;
        goto L_0x000b;
    L_0x0118:
        r0 = r3.getErrorStream();	 Catch:{ IOException -> 0x010e }
        r0 = a(r0);	 Catch:{ IOException -> 0x010e }
        r1 = r0;
        goto L_0x00d4;
    L_0x0122:
        r0 = move-exception;
        r3.disconnect();
        throw r0;
    L_0x0127:
        r0 = move-exception;
        goto L_0x0108;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.pingplusplus.android" +
                ".l.a(java.lang.String, java.lang.Object, java.lang.String, java.util.Map):com" +
                ".pingplusplus.android.k");
    }

    public static l a() {
        return p.a;
    }

    public static l a(Context context) {
        l a = p.a;
        if (e == null) {
            e = context;
            a.b(e);
        }
        return a;
    }

    private static String a(InputStream inputStream) {
        String next = new Scanner(inputStream, "UTF-8").useDelimiter("\\A").next();
        inputStream.close();
        return next;
    }

    public static String a(String str) {
        int i = 1;
        if (str == null || str.equals("")) {
            return "";
        }
        StringBuilder stringBuilder = new StringBuilder();
        if (str.length() > 0) {
            stringBuilder.append(str.substring(0, 1).toLowerCase());
            while (i < str.length()) {
                char charAt = str.charAt(i);
                if (Character.isUpperCase(charAt)) {
                    stringBuilder.append("_");
                    stringBuilder.append(Character.toLowerCase(charAt));
                } else {
                    stringBuilder.append(charAt);
                }
                i++;
            }
        }
        return stringBuilder.toString();
    }

    public static String b(String str) {
        String str2 = null;
        try {
            byte[] digest = MessageDigest.getInstance(Coder.KEY_MD5).digest(str.getBytes("UTF-8"));
            StringBuilder stringBuilder = new StringBuilder();
            for (byte b : digest) {
                int i = b & 255;
                if (i < 16) {
                    stringBuilder.append("0");
                }
                stringBuilder.append(Integer.toHexString(i));
            }
            str2 = stringBuilder.toString();
        } catch (Exception e) {
            PingppLog.a(e);
        } catch (Exception e2) {
            PingppLog.a(e2);
        }
        return str2;
    }

    private static Map d() {
        Map hashMap = new HashMap();
        Map hashMap2 = new HashMap();
        hashMap2.put("bindings_version", PaymentActivity.getVersion());
        hashMap2.put("lang", "Java");
        hashMap2.put("os_version", VERSION.RELEASE);
        hashMap2.put("model", Build.MODEL);
        hashMap.put("X-Pingpp-User-Agent", new JSONObject(hashMap2).toString());
        return hashMap;
    }

    public void a(String str, Object obj, Map map) {
        o oVar = new o();
        n[] nVarArr = new n[1];
        nVarArr[0] = new n(this, str, obj, Constants.HTTP_POST, map);
        oVar.execute(nVarArr);
    }

    public String b() {
        String str = "6_";
        String str2 = "7_";
        String str3 = "8_";
        String str4 = "9_";
        StringBuilder stringBuilder = new StringBuilder();
        if (this.a != null) {
            stringBuilder.append(str).append(this.a);
        }
        if (this.b != null) {
            if (stringBuilder.length() > 0) {
                stringBuilder.append("|");
            }
            stringBuilder.append(str2).append(this.b);
        }
        if (this.c != null) {
            if (stringBuilder.length() > 0) {
                stringBuilder.append("|");
            }
            stringBuilder.append(str3).append(this.c);
        }
        if (this.d != null) {
            if (stringBuilder.length() > 0) {
                stringBuilder.append("|");
            }
            stringBuilder.append(str4).append(this.d);
        }
        return stringBuilder.toString();
    }

    public void b(Context context) {
        String simSerialNumber;
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService("phone");
        try {
            String deviceId = telephonyManager.getDeviceId();
            if (deviceId != null) {
                this.a = UUID.nameUUIDFromBytes(deviceId.getBytes("UTF-8")).toString();
            }
        } catch (Exception e) {
            PingppLog.a(e.getMessage());
        }
        try {
            simSerialNumber = telephonyManager.getSimSerialNumber();
            if (simSerialNumber != null) {
                this.c = UUID.nameUUIDFromBytes(simSerialNumber.getBytes("UTF-8")).toString();
            }
        } catch (Exception e2) {
            PingppLog.a(e2.getMessage());
        }
        try {
            simSerialNumber = Secure.getString(context.getContentResolver(), "android_id");
            if (!(simSerialNumber == null || "9774d56d682e549c".equals(simSerialNumber))) {
                this.b = UUID.nameUUIDFromBytes(simSerialNumber.getBytes("UTF-8")).toString();
            }
        } catch (Exception e22) {
            PingppLog.a(e22.getMessage());
        }
        try {
            simSerialNumber = Build.SERIAL;
            if (simSerialNumber != null) {
                this.d = UUID.nameUUIDFromBytes(simSerialNumber.getBytes("UTF-8")).toString();
            }
        } catch (Exception e222) {
            PingppLog.a(e222.getMessage());
        }
        PingppLog.a("deviceId: " + this.a + "\nandroidId: " + this.b + "\nsimSerialNum: " + this
                .c + "\nserialNum: " + this.d);
    }
}
