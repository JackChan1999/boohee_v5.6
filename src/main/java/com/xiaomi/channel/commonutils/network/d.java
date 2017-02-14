package com.xiaomi.channel.commonutils.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;

import com.tencent.connect.common.Constants;
import com.umeng.socialize.common.SocializeConstants;
import com.xiaomi.account.openauth.utils.Network;
import com.xiaomi.channel.commonutils.string.c;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Proxy.Type;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Pattern;

public class d {
    public static final Pattern a = Pattern.compile("([^\\s;]+)(.*)");
    public static final Pattern b = Pattern.compile("(.*?charset\\s*=[^a-zA-Z0-9]*)" +
            "([-a-zA-Z0-9]+)(.*)", 2);
    public static final Pattern c = Pattern.compile("(\\<\\?xml\\s+.*?encoding\\s*=[^a-zA-Z0-9]*)" +
            "([-a-zA-Z0-9]+)(.*)", 2);

    public static final class a extends FilterInputStream {
        private boolean a;

        public a(InputStream inputStream) {
            super(inputStream);
        }

        public int read(byte[] bArr, int i, int i2) {
            if (!this.a) {
                int read = super.read(bArr, i, i2);
                if (read != -1) {
                    return read;
                }
            }
            this.a = true;
            return -1;
        }
    }

    public static class b {
        public int                 a;
        public Map<String, String> b;

        public String toString() {
            return String.format("resCode = %1$d, headers = %2$s", new Object[]{Integer.valueOf
                    (this.a), this.b.toString()});
        }
    }

    public static int a(Context context) {
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) context
                    .getSystemService("connectivity");
            if (connectivityManager == null) {
                return -1;
            }
            try {
                NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
                return activeNetworkInfo == null ? -1 : activeNetworkInfo.getType();
            } catch (Exception e) {
                return -1;
            }
        } catch (Exception e2) {
            return -1;
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static com.xiaomi.channel.commonutils.network.b a(android.content.Context r9, java
            .lang.String r10, java.lang.String r11, java.util.Map<java.lang.String, java.lang
            .String> r12, java.lang.String r13) {
        /*
        r3 = 0;
        r2 = 0;
        r5 = new com.xiaomi.channel.commonutils.network.b;
        r5.<init>();
        r0 = b(r10);	 Catch:{ IOException -> 0x0041, Throwable -> 0x00ef, all -> 0x012a }
        r6 = b(r9, r0);	 Catch:{ IOException -> 0x0041, Throwable -> 0x00ef, all -> 0x012a }
        r0 = 10000; // 0x2710 float:1.4013E-41 double:4.9407E-320;
        r6.setConnectTimeout(r0);	 Catch:{ IOException -> 0x0041, Throwable -> 0x00ef, all ->
        0x012a }
        r0 = 15000; // 0x3a98 float:2.102E-41 double:7.411E-320;
        r6.setReadTimeout(r0);	 Catch:{ IOException -> 0x0041, Throwable -> 0x00ef, all ->
        0x012a }
        if (r11 != 0) goto L_0x001e;
    L_0x001b:
        r11 = "GET";
    L_0x001e:
        r6.setRequestMethod(r11);	 Catch:{ IOException -> 0x0041, Throwable -> 0x00ef, all ->
        0x012a }
        if (r12 == 0) goto L_0x0053;
    L_0x0023:
        r0 = r12.keySet();	 Catch:{ IOException -> 0x0041, Throwable -> 0x00ef, all -> 0x012a }
        r4 = r0.iterator();	 Catch:{ IOException -> 0x0041, Throwable -> 0x00ef, all -> 0x012a }
    L_0x002b:
        r0 = r4.hasNext();	 Catch:{ IOException -> 0x0041, Throwable -> 0x00ef, all -> 0x012a }
        if (r0 == 0) goto L_0x0053;
    L_0x0031:
        r0 = r4.next();	 Catch:{ IOException -> 0x0041, Throwable -> 0x00ef, all -> 0x012a }
        r0 = (java.lang.String) r0;	 Catch:{ IOException -> 0x0041, Throwable -> 0x00ef, all ->
        0x012a }
        r1 = r12.get(r0);	 Catch:{ IOException -> 0x0041, Throwable -> 0x00ef, all -> 0x012a }
        r1 = (java.lang.String) r1;	 Catch:{ IOException -> 0x0041, Throwable -> 0x00ef, all ->
        0x012a }
        r6.setRequestProperty(r0, r1);	 Catch:{ IOException -> 0x0041, Throwable -> 0x00ef, all
        -> 0x012a }
        goto L_0x002b;
    L_0x0041:
        r0 = move-exception;
        r1 = r2;
    L_0x0043:
        throw r0;	 Catch:{ all -> 0x0044 }
    L_0x0044:
        r0 = move-exception;
        r8 = r1;
        r1 = r2;
        r2 = r8;
    L_0x0048:
        if (r1 == 0) goto L_0x004d;
    L_0x004a:
        r1.close();	 Catch:{ IOException -> 0x011e }
    L_0x004d:
        if (r2 == 0) goto L_0x0052;
    L_0x004f:
        r2.close();	 Catch:{ IOException -> 0x011e }
    L_0x0052:
        throw r0;
    L_0x0053:
        r0 = android.text.TextUtils.isEmpty(r13);	 Catch:{ IOException -> 0x0041, Throwable ->
        0x00ef, all -> 0x012a }
        if (r0 != 0) goto L_0x0144;
    L_0x0059:
        r0 = 1;
        r6.setDoOutput(r0);	 Catch:{ IOException -> 0x0041, Throwable -> 0x00ef, all -> 0x012a }
        r0 = r13.getBytes();	 Catch:{ IOException -> 0x0041, Throwable -> 0x00ef, all ->
        0x012a }
        r1 = r6.getOutputStream();	 Catch:{ IOException -> 0x0041, Throwable -> 0x00ef, all ->
        0x012a }
        r4 = 0;
        r7 = r0.length;	 Catch:{ IOException -> 0x013b, Throwable -> 0x0134 }
        r1.write(r0, r4, r7);	 Catch:{ IOException -> 0x013b, Throwable -> 0x0134 }
        r1.flush();	 Catch:{ IOException -> 0x013b, Throwable -> 0x0134 }
        r1.close();	 Catch:{ IOException -> 0x013b, Throwable -> 0x0134 }
        r4 = r2;
    L_0x0071:
        r0 = r6.getResponseCode();	 Catch:{ IOException -> 0x0041, Throwable -> 0x00ef, all ->
        0x012a }
        r5.a = r0;	 Catch:{ IOException -> 0x0041, Throwable -> 0x00ef, all -> 0x012a }
        r0 = "com.xiaomi.common.Network";
        r1 = new java.lang.StringBuilder;	 Catch:{ IOException -> 0x0041, Throwable -> 0x00ef,
        all -> 0x012a }
        r1.<init>();	 Catch:{ IOException -> 0x0041, Throwable -> 0x00ef, all -> 0x012a }
        r7 = "Http POST Response Code: ";
        r1 = r1.append(r7);	 Catch:{ IOException -> 0x0041, Throwable -> 0x00ef, all -> 0x012a }
        r7 = r5.a;	 Catch:{ IOException -> 0x0041, Throwable -> 0x00ef, all -> 0x012a }
        r1 = r1.append(r7);	 Catch:{ IOException -> 0x0041, Throwable -> 0x00ef, all -> 0x012a }
        r1 = r1.toString();	 Catch:{ IOException -> 0x0041, Throwable -> 0x00ef, all -> 0x012a }
        android.util.Log.d(r0, r1);	 Catch:{ IOException -> 0x0041, Throwable -> 0x00ef, all ->
        0x012a }
        r0 = r3;
    L_0x0094:
        r1 = r6.getHeaderFieldKey(r0);	 Catch:{ IOException -> 0x0041, Throwable -> 0x00ef, all
        -> 0x012a }
        r3 = r6.getHeaderField(r0);	 Catch:{ IOException -> 0x0041, Throwable -> 0x00ef, all ->
        0x012a }
        if (r1 != 0) goto L_0x00d0;
    L_0x009e:
        if (r3 != 0) goto L_0x00d0;
    L_0x00a0:
        r1 = new java.io.BufferedReader;	 Catch:{ IOException -> 0x00da, Throwable -> 0x00ef,
        all -> 0x012a }
        r0 = new java.io.InputStreamReader;	 Catch:{ IOException -> 0x00da, Throwable -> 0x00ef,
        all -> 0x012a }
        r3 = new com.xiaomi.channel.commonutils.network.d$a;	 Catch:{ IOException -> 0x00da,
        Throwable -> 0x00ef, all -> 0x012a }
        r7 = r6.getInputStream();	 Catch:{ IOException -> 0x00da, Throwable -> 0x00ef, all ->
        0x012a }
        r3.<init>(r7);	 Catch:{ IOException -> 0x00da, Throwable -> 0x00ef, all -> 0x012a }
        r0.<init>(r3);	 Catch:{ IOException -> 0x00da, Throwable -> 0x00ef, all -> 0x012a }
        r1.<init>(r0);	 Catch:{ IOException -> 0x00da, Throwable -> 0x00ef, all -> 0x012a }
    L_0x00b3:
        r0 = r1.readLine();	 Catch:{ IOException -> 0x0141, Throwable -> 0x0136, all -> 0x012e }
        r3 = new java.lang.StringBuffer;	 Catch:{ IOException -> 0x0141, Throwable -> 0x0136,
        all -> 0x012e }
        r3.<init>();	 Catch:{ IOException -> 0x0141, Throwable -> 0x0136, all -> 0x012e }
        r6 = "line.separator";
        r6 = java.lang.System.getProperty(r6);	 Catch:{ IOException -> 0x0141, Throwable ->
        0x0136, all -> 0x012e }
    L_0x00c3:
        if (r0 == 0) goto L_0x00fe;
    L_0x00c5:
        r3.append(r0);	 Catch:{ IOException -> 0x0141, Throwable -> 0x0136, all -> 0x012e }
        r3.append(r6);	 Catch:{ IOException -> 0x0141, Throwable -> 0x0136, all -> 0x012e }
        r0 = r1.readLine();	 Catch:{ IOException -> 0x0141, Throwable -> 0x0136, all -> 0x012e }
        goto L_0x00c3;
    L_0x00d0:
        r7 = r5.b;	 Catch:{ IOException -> 0x0041, Throwable -> 0x00ef, all -> 0x012a }
        r7.put(r1, r3);	 Catch:{ IOException -> 0x0041, Throwable -> 0x00ef, all -> 0x012a }
        r0 = r0 + 1;
        r0 = r0 + 1;
        goto L_0x0094;
    L_0x00da:
        r0 = move-exception;
        r1 = new java.io.BufferedReader;	 Catch:{ IOException -> 0x0041, Throwable -> 0x00ef,
        all -> 0x012a }
        r0 = new java.io.InputStreamReader;	 Catch:{ IOException -> 0x0041, Throwable -> 0x00ef,
        all -> 0x012a }
        r3 = new com.xiaomi.channel.commonutils.network.d$a;	 Catch:{ IOException -> 0x0041,
        Throwable -> 0x00ef, all -> 0x012a }
        r6 = r6.getErrorStream();	 Catch:{ IOException -> 0x0041, Throwable -> 0x00ef, all ->
        0x012a }
        r3.<init>(r6);	 Catch:{ IOException -> 0x0041, Throwable -> 0x00ef, all -> 0x012a }
        r0.<init>(r3);	 Catch:{ IOException -> 0x0041, Throwable -> 0x00ef, all -> 0x012a }
        r1.<init>(r0);	 Catch:{ IOException -> 0x0041, Throwable -> 0x00ef, all -> 0x012a }
        goto L_0x00b3;
    L_0x00ef:
        r0 = move-exception;
        r1 = r2;
    L_0x00f1:
        r3 = new java.io.IOException;	 Catch:{ all -> 0x00fb }
        r0 = r0.getMessage();	 Catch:{ all -> 0x00fb }
        r3.<init>(r0);	 Catch:{ all -> 0x00fb }
        throw r3;	 Catch:{ all -> 0x00fb }
    L_0x00fb:
        r0 = move-exception;
        goto L_0x0048;
    L_0x00fe:
        r0 = r3.toString();	 Catch:{ IOException -> 0x0141, Throwable -> 0x0136, all -> 0x012e }
        r5.c = r0;	 Catch:{ IOException -> 0x0141, Throwable -> 0x0136, all -> 0x012e }
        r1.close();	 Catch:{ IOException -> 0x0141, Throwable -> 0x0136, all -> 0x012e }
        r0 = 0;
        if (r2 == 0) goto L_0x010d;
    L_0x010a:
        r4.close();	 Catch:{ IOException -> 0x0113 }
    L_0x010d:
        if (r2 == 0) goto L_0x0112;
    L_0x010f:
        r0.close();	 Catch:{ IOException -> 0x0113 }
    L_0x0112:
        return r5;
    L_0x0113:
        r0 = move-exception;
        r1 = "com.xiaomi.common.Network";
        r2 = "error while closing strean";
        android.util.Log.e(r1, r2, r0);
        goto L_0x0112;
    L_0x011e:
        r1 = move-exception;
        r2 = "com.xiaomi.common.Network";
        r3 = "error while closing strean";
        android.util.Log.e(r2, r3, r1);
        goto L_0x0052;
    L_0x012a:
        r0 = move-exception;
        r1 = r2;
        goto L_0x0048;
    L_0x012e:
        r0 = move-exception;
        r8 = r1;
        r1 = r2;
        r2 = r8;
        goto L_0x0048;
    L_0x0134:
        r0 = move-exception;
        goto L_0x00f1;
    L_0x0136:
        r0 = move-exception;
        r8 = r1;
        r1 = r2;
        r2 = r8;
        goto L_0x00f1;
    L_0x013b:
        r0 = move-exception;
        r8 = r2;
        r2 = r1;
        r1 = r8;
        goto L_0x0043;
    L_0x0141:
        r0 = move-exception;
        goto L_0x0043;
    L_0x0144:
        r4 = r2;
        goto L_0x0071;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.xiaomi.channel" +
                ".commonutils.network.d.a(android.content.Context, java.lang.String, java.lang" +
                ".String, java.util.Map, java.lang.String):com.xiaomi.channel.commonutils.network" +
                ".b");
    }

    public static b a(Context context, String str, Map<String, String> map) {
        return a(context, str, Constants.HTTP_POST, null, a((Map) map));
    }

    public static InputStream a(Context context, URL url, boolean z, String str, String str2) {
        return a(context, url, z, str, str2, null, null);
    }

    public static InputStream a(Context context, URL url, boolean z, String str, String str2,
                                Map<String, String> map, b bVar) {
        if (context == null) {
            throw new IllegalArgumentException("context");
        } else if (url == null) {
            throw new IllegalArgumentException("url");
        } else {
            URL url2 = !z ? new URL(a(url.toString())) : url;
            try {
                HttpURLConnection.setFollowRedirects(true);
                HttpURLConnection b = b(context, url2);
                b.setConnectTimeout(10000);
                b.setReadTimeout(15000);
                if (!TextUtils.isEmpty(str)) {
                    b.setRequestProperty(Network.USER_AGENT, str);
                }
                if (str2 != null) {
                    b.setRequestProperty("Cookie", str2);
                }
                if (map != null) {
                    for (String str3 : map.keySet()) {
                        b.setRequestProperty(str3, (String) map.get(str3));
                    }
                }
                if (bVar != null) {
                    if (url.getProtocol().equals("http") || url.getProtocol().equals(com.alipay
                            .sdk.cons.b.a)) {
                        bVar.a = b.getResponseCode();
                        if (bVar.b == null) {
                            bVar.b = new HashMap();
                        }
                        int i = 0;
                        while (true) {
                            CharSequence headerFieldKey = b.getHeaderFieldKey(i);
                            CharSequence headerField = b.getHeaderField(i);
                            if (headerFieldKey == null && headerField == null) {
                                break;
                            }
                            if (!(TextUtils.isEmpty(headerFieldKey) || TextUtils.isEmpty
                                    (headerField))) {
                                bVar.b.put(headerFieldKey, headerField);
                            }
                            i++;
                        }
                    }
                }
                return new a(b.getInputStream());
            } catch (IOException e) {
                throw e;
            } catch (Throwable th) {
                IOException iOException = new IOException(th.getMessage());
            }
        }
    }

    public static String a(Context context, URL url) {
        return a(context, url, false, null, "UTF-8", null);
    }

    public static String a(Context context, URL url, boolean z, String str, String str2, String
            str3) {
        InputStream inputStream = null;
        try {
            inputStream = a(context, url, z, str, str3);
            StringBuilder stringBuilder = new StringBuilder(1024);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,
                    str2));
            char[] cArr = new char[4096];
            while (true) {
                int read = bufferedReader.read(cArr);
                if (-1 == read) {
                    break;
                }
                stringBuilder.append(cArr, 0, read);
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    Log.e("com.xiaomi.common.Network", "Failed to close responseStream" + e
                            .toString());
                }
            }
            return stringBuilder.toString();
        } catch (Throwable th) {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e2) {
                    Log.e("com.xiaomi.common.Network", "Failed to close responseStream" + e2
                            .toString());
                }
            }
        }
    }

    public static String a(String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        String str2 = new String();
        str2 = String.format("%sbe988a6134bc8254465424e5a70ef037", new Object[]{str});
        return String.format("%s&key=%s", new Object[]{str, c.a(str2)});
    }

    public static String a(String str, Map<String, String> map, File file, String str2) {
        DataOutputStream dataOutputStream;
        IOException e;
        Object obj;
        BufferedReader bufferedReader;
        Throwable th;
        Object obj2;
        BufferedReader bufferedReader2 = null;
        if (!file.exists()) {
            return null;
        }
        String name = file.getName();
        String str3 = "\r\n";
        str3 = "--";
        str3 = "*****";
        FileInputStream fileInputStream;
        BufferedReader bufferedReader3;
        try {
            HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(str).openConnection();
            httpURLConnection.setReadTimeout(15000);
            httpURLConnection.setConnectTimeout(10000);
            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setUseCaches(false);
            httpURLConnection.setRequestMethod(Constants.HTTP_POST);
            httpURLConnection.setRequestProperty("Connection", "Keep-Alive");
            httpURLConnection.setRequestProperty("Content-Type", "multipart/form-data;" +
                    "boundary=*****");
            if (map != null) {
                for (Entry entry : map.entrySet()) {
                    httpURLConnection.setRequestProperty((String) entry.getKey(), (String) entry
                            .getValue());
                }
            }
            httpURLConnection.setFixedLengthStreamingMode(((name.length() + 77) + ((int) file
                    .length())) + str2.length());
            dataOutputStream = new DataOutputStream(httpURLConnection.getOutputStream());
            try {
                dataOutputStream.writeBytes("--*****\r\n");
                dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"" + str2 +
                        "\";filename=\"" + file.getName() + com.alipay.sdk.sys.a.e + "\r\n");
                dataOutputStream.writeBytes("\r\n");
                fileInputStream = new FileInputStream(file);
            } catch (IOException e2) {
                e = e2;
                obj = dataOutputStream;
                bufferedReader = null;
                try {
                    throw e;
                } catch (Throwable th2) {
                    th = th2;
                    fileInputStream = bufferedReader;
                    dataOutputStream = bufferedReader3;
                }
            } catch (Throwable th3) {
                th = th3;
                fileInputStream = null;
                if (fileInputStream != null) {
                    fileInputStream.close();
                }
                if (dataOutputStream != null) {
                    dataOutputStream.close();
                }
                if (bufferedReader2 != null) {
                    bufferedReader2.close();
                }
                throw th;
            }
            try {
                byte[] bArr = new byte[1024];
                while (true) {
                    int read = fileInputStream.read(bArr);
                    if (read == -1) {
                        break;
                    }
                    dataOutputStream.write(bArr, 0, read);
                    dataOutputStream.flush();
                }
                dataOutputStream.writeBytes("\r\n");
                dataOutputStream.writeBytes("--");
                dataOutputStream.writeBytes("*****");
                dataOutputStream.writeBytes("--");
                dataOutputStream.writeBytes("\r\n");
                dataOutputStream.flush();
                StringBuffer stringBuffer = new StringBuffer();
                bufferedReader3 = new BufferedReader(new InputStreamReader(new a
                        (httpURLConnection.getInputStream())));
                while (true) {
                    try {
                        str3 = bufferedReader3.readLine();
                        if (str3 == null) {
                            break;
                        }
                        stringBuffer.append(str3);
                    } catch (IOException e3) {
                        e = e3;
                        bufferedReader2 = bufferedReader3;
                        obj = dataOutputStream;
                        obj2 = fileInputStream;
                    } catch (Throwable th4) {
                        th = th4;
                        bufferedReader2 = bufferedReader3;
                    }
                }
                str3 = stringBuffer.toString();
                if (fileInputStream != null) {
                    try {
                        fileInputStream.close();
                    } catch (Throwable e4) {
                        Log.e("com.xiaomi.common.Network", "error while closing strean", e4);
                        return str3;
                    }
                }
                if (dataOutputStream != null) {
                    dataOutputStream.close();
                }
                if (bufferedReader3 == null) {
                    return str3;
                }
                bufferedReader3.close();
                return str3;
            } catch (IOException e5) {
                e = e5;
                obj = dataOutputStream;
                obj2 = fileInputStream;
                throw e;
            } catch (Throwable th5) {
                th = th5;
                throw new IOException(th.getMessage());
            }
        } catch (IOException e6) {
            e = e6;
            bufferedReader3 = null;
            bufferedReader = null;
            throw e;
        } catch (Throwable th6) {
            th = th6;
            dataOutputStream = null;
            fileInputStream = null;
            if (fileInputStream != null) {
                fileInputStream.close();
            }
            if (dataOutputStream != null) {
                dataOutputStream.close();
            }
            if (bufferedReader2 != null) {
                bufferedReader2.close();
            }
            throw th;
        }
    }

    public static String a(URL url) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(url.getProtocol()).append("://").append(Network.CMWAP_GATEWAY)
                .append(url.getPath());
        if (!TextUtils.isEmpty(url.getQuery())) {
            stringBuilder.append("?").append(url.getQuery());
        }
        return stringBuilder.toString();
    }

    public static String a(Map<String, String> map) {
        if (map == null || map.size() <= 0) {
            return null;
        }
        StringBuffer stringBuffer = new StringBuffer();
        for (Entry entry : map.entrySet()) {
            if (!(entry.getKey() == null || entry.getValue() == null)) {
                try {
                    stringBuffer.append(URLEncoder.encode((String) entry.getKey(), "UTF-8"));
                    stringBuffer.append("=");
                    stringBuffer.append(URLEncoder.encode((String) entry.getValue(), "UTF-8"));
                    stringBuffer.append(com.alipay.sdk.sys.a.b);
                } catch (UnsupportedEncodingException e) {
                    Log.d("com.xiaomi.common.Network", "Failed to convert from params map to " +
                            "string: " + e.toString());
                    Log.d("com.xiaomi.common.Network", "map: " + map.toString());
                    return null;
                }
            }
        }
        return (stringBuffer.length() > 0 ? stringBuffer.deleteCharAt(stringBuffer.length() - 1)
                : stringBuffer).toString();
    }

    public static HttpURLConnection b(Context context, URL url) {
        if (!"http".equals(url.getProtocol())) {
            return (HttpURLConnection) url.openConnection();
        }
        if (c(context)) {
            return (HttpURLConnection) url.openConnection(new Proxy(Type.HTTP, new
                    InetSocketAddress("10.0.0.200", 80)));
        }
        if (!b(context)) {
            return (HttpURLConnection) url.openConnection();
        }
        HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(a(url)).openConnection();
        httpURLConnection.addRequestProperty(Network.CMWAP_HEADER_HOST_KEY, url.getHost());
        return httpURLConnection;
    }

    private static URL b(String str) {
        return new URL(str);
    }

    public static boolean b(Context context) {
        if (!"CN".equalsIgnoreCase(((TelephonyManager) context.getSystemService("phone"))
                .getSimCountryIso())) {
            return false;
        }
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) context
                    .getSystemService("connectivity");
            if (connectivityManager == null) {
                return false;
            }
            try {
                NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
                if (activeNetworkInfo == null) {
                    return false;
                }
                String extraInfo = activeNetworkInfo.getExtraInfo();
                return (TextUtils.isEmpty(extraInfo) || extraInfo.length() < 3 || extraInfo
                        .contains("ctwap")) ? false : extraInfo.regionMatches(true, extraInfo
                        .length() - 3, "wap", 0, 3);
            } catch (Exception e) {
                return false;
            }
        } catch (Exception e2) {
            return false;
        }
    }

    public static boolean c(Context context) {
        if (!"CN".equalsIgnoreCase(((TelephonyManager) context.getSystemService("phone"))
                .getSimCountryIso())) {
            return false;
        }
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) context
                    .getSystemService("connectivity");
            if (connectivityManager == null) {
                return false;
            }
            try {
                NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
                if (activeNetworkInfo == null) {
                    return false;
                }
                String extraInfo = activeNetworkInfo.getExtraInfo();
                return (TextUtils.isEmpty(extraInfo) || extraInfo.length() < 3) ? false :
                        extraInfo.contains("ctwap");
            } catch (Exception e) {
                return false;
            }
        } catch (Exception e2) {
            return false;
        }
    }

    public static boolean d(Context context) {
        return a(context) >= 0;
    }

    public static boolean e(Context context) {
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) context
                    .getSystemService("connectivity");
            if (connectivityManager == null) {
                return false;
            }
            try {
                NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
                if (activeNetworkInfo == null) {
                    return false;
                }
                return 1 == activeNetworkInfo.getType();
            } catch (Exception e) {
                return false;
            }
        } catch (Exception e2) {
            return false;
        }
    }

    public static String f(Context context) {
        if (e(context)) {
            return "wifi";
        }
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) context
                    .getSystemService("connectivity");
            if (connectivityManager == null) {
                return "";
            }
            try {
                NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
                return activeNetworkInfo == null ? "" : (activeNetworkInfo.getTypeName() +
                        SocializeConstants.OP_DIVIDER_MINUS + activeNetworkInfo.getSubtypeName()
                        + SocializeConstants.OP_DIVIDER_MINUS + activeNetworkInfo.getExtraInfo())
                        .toLowerCase();
            } catch (Exception e) {
                return "";
            }
        } catch (Exception e2) {
            return "";
        }
    }
}
