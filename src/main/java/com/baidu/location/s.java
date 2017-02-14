package com.baidu.location;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Proxy;
import android.util.Log;
import com.qiniu.android.common.Constants;
import com.xiaomi.account.openauth.utils.Network;
import java.io.File;
import java.util.List;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.FileEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpProtocolParams;

abstract class s implements ax, n {
    private static int cU = 4;
    private static String cX = Network.CMWAP_GATEWAY;
    protected static int cY = 0;
    private static int cZ = 80;
    public int c0 = 3;
    public String cR = null;
    public HttpEntity cS = null;
    public List cT = null;
    private boolean cV = false;
    public String cW = null;

    s() {
    }

    private void L() {
        cU = P();
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private int P() {
        /*
        r10 = this;
        r7 = 1;
        r6 = 4;
        r1 = 0;
        r9 = com.baidu.location.f.getServiceContext();
        r0 = "connectivity";
        r0 = r9.getSystemService(r0);	 Catch:{ SecurityException -> 0x00c9, Exception -> 0x00d5 }
        r0 = (android.net.ConnectivityManager) r0;	 Catch:{ SecurityException -> 0x00c9, Exception -> 0x00d5 }
        if (r0 != 0) goto L_0x0014;
    L_0x0012:
        r0 = r6;
    L_0x0013:
        return r0;
    L_0x0014:
        r8 = r0.getActiveNetworkInfo();	 Catch:{ SecurityException -> 0x00c9, Exception -> 0x00d5 }
        if (r8 == 0) goto L_0x0020;
    L_0x001a:
        r0 = r8.isAvailable();	 Catch:{ SecurityException -> 0x00d9, Exception -> 0x00d5 }
        if (r0 != 0) goto L_0x0022;
    L_0x0020:
        r0 = r6;
        goto L_0x0013;
    L_0x0022:
        r0 = r8.getType();	 Catch:{ SecurityException -> 0x00d9, Exception -> 0x00d5 }
        if (r0 != r7) goto L_0x002a;
    L_0x0028:
        r0 = 3;
        goto L_0x0013;
    L_0x002a:
        r0 = "content://telephony/carriers/preferapn";
        r1 = android.net.Uri.parse(r0);	 Catch:{ SecurityException -> 0x00d9, Exception -> 0x00d5 }
        r0 = r9.getContentResolver();	 Catch:{ SecurityException -> 0x00d9, Exception -> 0x00d5 }
        r2 = 0;
        r3 = 0;
        r4 = 0;
        r5 = 0;
        r1 = r0.query(r1, r2, r3, r4, r5);	 Catch:{ SecurityException -> 0x00d9, Exception -> 0x00d5 }
        if (r1 == 0) goto L_0x00c1;
    L_0x003f:
        r0 = r1.moveToFirst();	 Catch:{ SecurityException -> 0x00d9, Exception -> 0x00d5 }
        if (r0 == 0) goto L_0x00c1;
    L_0x0045:
        r0 = "apn";
        r0 = r1.getColumnIndex(r0);	 Catch:{ SecurityException -> 0x00d9, Exception -> 0x00d5 }
        r0 = r1.getString(r0);	 Catch:{ SecurityException -> 0x00d9, Exception -> 0x00d5 }
        if (r0 == 0) goto L_0x0088;
    L_0x0052:
        r2 = r0.toLowerCase();	 Catch:{ SecurityException -> 0x00d9, Exception -> 0x00d5 }
        r3 = "ctwap";
        r2 = r2.contains(r3);	 Catch:{ SecurityException -> 0x00d9, Exception -> 0x00d5 }
        if (r2 == 0) goto L_0x0088;
    L_0x005f:
        r0 = android.net.Proxy.getDefaultHost();	 Catch:{ SecurityException -> 0x00d9, Exception -> 0x00d5 }
        if (r0 == 0) goto L_0x0084;
    L_0x0065:
        r2 = "";
        r2 = r0.equals(r2);	 Catch:{ SecurityException -> 0x00d9, Exception -> 0x00d5 }
        if (r2 != 0) goto L_0x0084;
    L_0x006e:
        r2 = "null";
        r2 = r0.equals(r2);	 Catch:{ SecurityException -> 0x00d9, Exception -> 0x00d5 }
        if (r2 != 0) goto L_0x0084;
    L_0x0077:
        cX = r0;	 Catch:{ SecurityException -> 0x00d9, Exception -> 0x00d5 }
        r0 = 80;
        cZ = r0;	 Catch:{ SecurityException -> 0x00d9, Exception -> 0x00d5 }
        if (r1 == 0) goto L_0x0082;
    L_0x007f:
        r1.close();	 Catch:{ SecurityException -> 0x00d9, Exception -> 0x00d5 }
    L_0x0082:
        r0 = r7;
        goto L_0x0013;
    L_0x0084:
        r0 = "10.0.0.200";
        goto L_0x0077;
    L_0x0088:
        if (r0 == 0) goto L_0x00c1;
    L_0x008a:
        r0 = r0.toLowerCase();	 Catch:{ SecurityException -> 0x00d9, Exception -> 0x00d5 }
        r2 = "wap";
        r0 = r0.contains(r2);	 Catch:{ SecurityException -> 0x00d9, Exception -> 0x00d5 }
        if (r0 == 0) goto L_0x00c1;
    L_0x0097:
        r0 = android.net.Proxy.getDefaultHost();	 Catch:{ SecurityException -> 0x00d9, Exception -> 0x00d5 }
        if (r0 == 0) goto L_0x00bd;
    L_0x009d:
        r2 = "";
        r2 = r0.equals(r2);	 Catch:{ SecurityException -> 0x00d9, Exception -> 0x00d5 }
        if (r2 != 0) goto L_0x00bd;
    L_0x00a6:
        r2 = "null";
        r2 = r0.equals(r2);	 Catch:{ SecurityException -> 0x00d9, Exception -> 0x00d5 }
        if (r2 != 0) goto L_0x00bd;
    L_0x00af:
        cX = r0;	 Catch:{ SecurityException -> 0x00d9, Exception -> 0x00d5 }
        r0 = 80;
        cZ = r0;	 Catch:{ SecurityException -> 0x00d9, Exception -> 0x00d5 }
        if (r1 == 0) goto L_0x00ba;
    L_0x00b7:
        r1.close();	 Catch:{ SecurityException -> 0x00d9, Exception -> 0x00d5 }
    L_0x00ba:
        r0 = r7;
        goto L_0x0013;
    L_0x00bd:
        r0 = "10.0.0.172";
        goto L_0x00af;
    L_0x00c1:
        if (r1 == 0) goto L_0x00c6;
    L_0x00c3:
        r1.close();	 Catch:{ SecurityException -> 0x00d9, Exception -> 0x00d5 }
    L_0x00c6:
        r0 = 2;
        goto L_0x0013;
    L_0x00c9:
        r0 = move-exception;
        r0 = r1;
    L_0x00cb:
        r0 = if(r9, r0);	 Catch:{ Exception -> 0x00d1 }
        goto L_0x0013;
    L_0x00d1:
        r0 = move-exception;
        r0 = r6;
        goto L_0x0013;
    L_0x00d5:
        r0 = move-exception;
        r0 = r6;
        goto L_0x0013;
    L_0x00d9:
        r0 = move-exception;
        r0 = r8;
        goto L_0x00cb;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.baidu.location.s.P():int");
    }

    private static int if(Context context, NetworkInfo networkInfo) {
        String toLowerCase;
        if (!(networkInfo == null || networkInfo.getExtraInfo() == null)) {
            toLowerCase = networkInfo.getExtraInfo().toLowerCase();
            if (toLowerCase != null) {
                String defaultHost;
                if (toLowerCase.startsWith("cmwap") || toLowerCase.startsWith("uniwap") || toLowerCase.startsWith("3gwap")) {
                    defaultHost = Proxy.getDefaultHost();
                    if (defaultHost == null || defaultHost.equals("") || defaultHost.equals("null")) {
                        defaultHost = Network.CMWAP_GATEWAY;
                    }
                    cX = defaultHost;
                    return 1;
                } else if (toLowerCase.startsWith("ctwap")) {
                    defaultHost = Proxy.getDefaultHost();
                    if (defaultHost == null || defaultHost.equals("") || defaultHost.equals("null")) {
                        defaultHost = "10.0.0.200";
                    }
                    cX = defaultHost;
                    return 1;
                } else if (toLowerCase.startsWith("cmnet") || toLowerCase.startsWith("uninet") || toLowerCase.startsWith("ctnet") || toLowerCase.startsWith("3gnet")) {
                    return 2;
                }
            }
        }
        toLowerCase = Proxy.getDefaultHost();
        if (toLowerCase == null || toLowerCase.length() <= 0) {
            return 2;
        }
        if (Network.CMWAP_GATEWAY.equals(toLowerCase.trim())) {
            cX = Network.CMWAP_GATEWAY;
            return 1;
        } else if (!"10.0.0.200".equals(toLowerCase.trim())) {
            return 2;
        } else {
            cX = "10.0.0.200";
            return 1;
        }
    }

    public static boolean if(Context context) {
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService("connectivity");
            return connectivityManager.getActiveNetworkInfo() != null ? connectivityManager.getActiveNetworkInfo().isAvailable() : false;
        } catch (Exception e) {
            return false;
        }
    }

    public void N() {
        new Thread(this) {
            final /* synthetic */ s a;

            {
                this.a = r1;
            }

            public void run() {
                this.a.cR = c.for();
                this.a.T();
                int i = this.a.c0;
                this.a.L();
                int i2 = i;
                HttpEntity httpEntity = null;
                while (i2 > 0) {
                    HttpPost httpPost;
                    Object obj;
                    try {
                        httpPost = new HttpPost(this.a.cR);
                        try {
                            httpEntity = new UrlEncodedFormEntity(this.a.cT, Constants.UTF_8);
                            httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
                            httpPost.setHeader("Accept-Charset", "UTF-8;");
                            httpPost.setEntity(httpEntity);
                            HttpClient defaultHttpClient = new DefaultHttpClient();
                            defaultHttpClient.getParams().setParameter("http.connection.timeout", Integer.valueOf(ax.F));
                            defaultHttpClient.getParams().setParameter("http.socket.timeout", Integer.valueOf(ax.F));
                            HttpProtocolParams.setUseExpectContinue(defaultHttpClient.getParams(), false);
                            if ((s.cU == 1 || s.cU == 4) && (this.a.c0 - i2) % 2 == 0) {
                                defaultHttpClient.getParams().setParameter("http.route.default-proxy", new HttpHost(s.cX, s.cZ, "http"));
                            }
                            HttpResponse execute = defaultHttpClient.execute(httpPost);
                            if (execute.getStatusLine().getStatusCode() == 200) {
                                this.a.cS = execute.getEntity();
                                this.a.do(true);
                                break;
                            }
                            httpPost.abort();
                            i2--;
                            obj = httpPost;
                        } catch (Exception e) {
                        }
                    } catch (Exception e2) {
                        Object obj2 = httpEntity;
                        httpPost.abort();
                        Log.d(ax.i, "NetworkCommunicationException!");
                        i2--;
                        obj = httpPost;
                    }
                }
                if (i2 <= 0) {
                    s.cY++;
                    this.a.cS = null;
                    this.a.do(false);
                } else {
                    s.cY = 0;
                }
                this.a.cV = false;
            }
        }.start();
    }

    public void Q() {
        new Thread(this) {
            final /* synthetic */ s a;

            {
                this.a = r1;
            }

            public void run() {
                this.a.cR = c.for();
                this.a.T();
                int i = this.a.c0;
                this.a.L();
                while (i > 0) {
                    try {
                        Object httpGet = new HttpGet(this.a.cR);
                        httpGet.setHeader("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
                        httpGet.setHeader("Accept-Charset", "UTF-8;");
                        HttpClient defaultHttpClient = new DefaultHttpClient();
                        defaultHttpClient.getParams().setParameter("http.connection.timeout", Integer.valueOf(ax.F));
                        defaultHttpClient.getParams().setParameter("http.socket.timeout", Integer.valueOf(ax.F));
                        HttpProtocolParams.setUseExpectContinue(defaultHttpClient.getParams(), false);
                        if ((s.cU == 1 || s.cU == 4) && (this.a.c0 - i) % 2 == 0) {
                            defaultHttpClient.getParams().setParameter("http.route.default-proxy", new HttpHost(s.cX, s.cZ, "http"));
                        }
                        HttpResponse execute = defaultHttpClient.execute(httpGet);
                        if (execute.getStatusLine().getStatusCode() == 200) {
                            this.a.cS = execute.getEntity();
                            this.a.do(true);
                            break;
                        }
                        httpGet.abort();
                        i--;
                    } catch (Exception e) {
                        Log.d(ax.i, "NetworkCommunicationException!");
                    }
                }
                if (i <= 0) {
                    s.cY++;
                    this.a.cS = null;
                    this.a.do(false);
                } else {
                    s.cY = 0;
                }
                this.a.cV = false;
            }
        }.start();
    }

    public void R() {
        new Thread(this) {
            final /* synthetic */ s a;

            {
                this.a = r1;
            }

            public void run() {
                Object obj;
                this.a.cR = c.for();
                this.a.T();
                int i = this.a.c0;
                this.a.L();
                int i2 = i;
                HttpEntity httpEntity = null;
                while (i2 > 0) {
                    HttpPost httpPost;
                    try {
                        httpPost = new HttpPost(this.a.cR);
                        try {
                            httpEntity = new FileEntity(new File(this.a.cW), "binary/octet-stream");
                            httpPost.setHeader("Content-Type", "application/octet-stream");
                            httpPost.setHeader("Accept-Charset", "UTF-8;");
                            httpPost.setEntity(httpEntity);
                            HttpClient defaultHttpClient = new DefaultHttpClient();
                            defaultHttpClient.getParams().setParameter("http.connection.timeout", Integer.valueOf(ax.F));
                            defaultHttpClient.getParams().setParameter("http.socket.timeout", Integer.valueOf(ax.F));
                            HttpProtocolParams.setUseExpectContinue(defaultHttpClient.getParams(), false);
                            if ((s.cU == 1 || s.cU == 4) && (this.a.c0 - i2) % 2 == 0) {
                                defaultHttpClient.getParams().setParameter("http.route.default-proxy", new HttpHost(s.cX, s.cZ, "http"));
                            }
                            HttpResponse execute = defaultHttpClient.execute(httpPost);
                            if (execute.getStatusLine().getStatusCode() == 200) {
                                this.a.cS = execute.getEntity();
                                this.a.do(true);
                                break;
                            }
                            httpPost.abort();
                            i2--;
                            obj = httpPost;
                        } catch (Exception e) {
                        }
                    } catch (Exception e2) {
                        Object obj2 = httpEntity;
                        httpPost.abort();
                        Log.d(ax.i, "NetworkCommunicationException!");
                        i2--;
                        obj = httpPost;
                    }
                }
                if (i2 <= 0) {
                    s.cY++;
                    this.a.cS = null;
                    this.a.do(false);
                } else {
                    s.cY = 0;
                }
                this.a.cV = false;
            }
        }.start();
    }

    abstract void T();

    abstract void do(boolean z);
}
