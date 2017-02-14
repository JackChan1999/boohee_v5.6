package com.xiaomi.account.openauth.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;

import com.alipay.sdk.cons.b;

import java.io.BufferedReader;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Proxy.Type;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class Network {
    public static final  String           CMWAP_GATEWAY                  = "10.0.0.172";
    public static final  String           CMWAP_HEADER_HOST_KEY          = "X-Online-Host";
    public static final  int              CONNECTION_TIMEOUT             = 10000;
    private static final String           LogTag                         = "com.xiaomi.common" +
            ".Network";
    public static final  int              READ_TIMEOUT                   = 15000;
    public static final  String           USER_AGENT                     = "User-Agent";
    private static       TrustManager     ignoreCertificationTrustManger = new X509TrustManager() {
        private X509Certificate[] certificates;

        public void checkClientTrusted(X509Certificate[] certificates, String authType) throws
                CertificateException {
            if (this.certificates == null) {
                this.certificates = certificates;
                Log.v("openauth", "init at checkClientTrusted.");
            }
        }

        public void checkServerTrusted(X509Certificate[] ax509certificate, String s) throws
                CertificateException {
            if (this.certificates == null) {
                this.certificates = ax509certificate;
                Log.v("openauth", "init at checkServerTrusted");
            }
        }

        public X509Certificate[] getAcceptedIssuers() {
            return null;
        }
    };
    private static       HostnameVerifier ignoreHostnameVerifier         = new HostnameVerifier() {
        public boolean verify(String s, SSLSession sslsession) {
            Log.v("openauth", "WARNING: Hostname is not matched for cert.");
            return true;
        }
    };

    private static final class DoneHandlerInputStream extends FilterInputStream {
        private boolean done;

        public DoneHandlerInputStream(InputStream stream) {
            super(stream);
        }

        public int read(byte[] bytes, int offset, int count) throws IOException {
            if (!this.done) {
                int result = super.read(bytes, offset, count);
                if (result != -1) {
                    return result;
                }
            }
            this.done = true;
            return -1;
        }
    }

    public static class HttpHeaderInfo {
        public Map<String, String> AllHeaders;
        public String              ContentType;
        public int                 ResponseCode;
        public String              UserAgent;
        public String              realUrl;

        public String toString() {
            return String.format("resCode = %1$d, headers = %2$s", new Object[]{Integer.valueOf
                    (this.ResponseCode), this.AllHeaders.toString()});
        }
    }

    private static InputStream downloadXmlAsStream(Context context, URL url, String userAgent,
                                                   String cookie) throws IOException {
        return downloadXmlAsStream(context, url, userAgent, cookie, null, null);
    }

    private static InputStream downloadXmlAsStream(Context context, URL url, String userAgent,
                                                   String cookie, Map<String, String>
                                                           requestHdrs, HttpHeaderInfo
                                                           responseHdrs) throws IOException {
        if (context == null) {
            throw new IllegalArgumentException("context");
        } else if (url == null) {
            throw new IllegalArgumentException("url");
        } else {
            InputStream inputStream;
            URL newUrl = url;
            HttpsURLConnection.setDefaultHostnameVerifier(ignoreHostnameVerifier);
            HttpURLConnection.setFollowRedirects(true);
            HttpURLConnection conn = getHttpUrlConnection(context, newUrl);
            conn.setConnectTimeout(10000);
            conn.setReadTimeout(15000);
            TrustManager[] tm = new TrustManager[]{ignoreCertificationTrustManger};
            SSLContext sslContext = null;
            try {
                sslContext = SSLContext.getInstance("SSL");
                sslContext.init(null, tm, new SecureRandom());
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (KeyManagementException e2) {
                e2.printStackTrace();
            }
            ((HttpsURLConnection) conn).setSSLSocketFactory(sslContext.getSocketFactory());
            if (!TextUtils.isEmpty(userAgent)) {
                conn.setRequestProperty(USER_AGENT, userAgent);
            }
            if (cookie != null) {
                conn.setRequestProperty("Cookie", cookie);
            }
            if (requestHdrs != null) {
                for (String key : requestHdrs.keySet()) {
                    conn.setRequestProperty(key, (String) requestHdrs.get(key));
                }
            }
            if (responseHdrs != null && (url.getProtocol().equals("http") || url.getProtocol()
                    .equals(b.a))) {
                responseHdrs.ResponseCode = conn.getResponseCode();
                if (responseHdrs.AllHeaders == null) {
                    responseHdrs.AllHeaders = new HashMap();
                }
                int i = 0;
                while (true) {
                    String name = conn.getHeaderFieldKey(i);
                    String value = conn.getHeaderField(i);
                    if (name == null && value == null) {
                        break;
                    }
                    if (!(TextUtils.isEmpty(name) || TextUtils.isEmpty(value))) {
                        responseHdrs.AllHeaders.put(name, value);
                    }
                    i++;
                }
            }
            try {
                inputStream = conn.getInputStream();
            } catch (IOException e3) {
                inputStream = conn.getErrorStream();
            }
            return new DoneHandlerInputStream(inputStream);
        }
    }

    public static String downloadXml(Context context, URL url) throws IOException {
        return downloadXml(context, url, false, null, "UTF-8", null);
    }

    private static String downloadXml(Context context, URL url, boolean noEncryptUrl, String
            userAgent, String encoding, String cookie) throws IOException {
        Throwable th;
        InputStream responseStream = null;
        BufferedReader reader = null;
        try {
            responseStream = downloadXmlAsStream(context, url, userAgent, cookie);
            StringBuilder sbReponse = new StringBuilder(1024);
            BufferedReader reader2 = new BufferedReader(new InputStreamReader(responseStream,
                    encoding), 1024);
            responseStream = null;
            while (true) {
                String line = reader2.readLine();
                if (line == null) {
                    break;
                }
                try {
                    sbReponse.append(line);
                    sbReponse.append("\r\n");
                } catch (Throwable th2) {
                    th = th2;
                    reader = reader2;
                }
            }
            if (responseStream != null) {
                try {
                    responseStream.close();
                } catch (IOException e) {
                    Log.e(LogTag, "Failed to close responseStream" + e.toString());
                }
            }
            if (reader2 != null) {
                try {
                    reader2.close();
                } catch (IOException e2) {
                    Log.e(LogTag, "Failed to close reader" + e2.toString());
                }
            }
            return sbReponse.toString();
        } catch (Throwable th3) {
            th = th3;
            if (responseStream != null) {
                try {
                    responseStream.close();
                } catch (IOException e22) {
                    Log.e(LogTag, "Failed to close responseStream" + e22.toString());
                }
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e222) {
                    Log.e(LogTag, "Failed to close reader" + e222.toString());
                }
            }
            throw th;
        }
    }

    public static String downloadXml(Context context, URL url, String userAgent, String cookie,
                                     Map<String, String> requestHdrs, HttpHeaderInfo response)
            throws IOException {
        Throwable th;
        InputStream responseStream = null;
        BufferedReader reader = null;
        try {
            responseStream = downloadXmlAsStream(context, url, userAgent, cookie, requestHdrs,
                    response);
            StringBuilder sbReponse = new StringBuilder(1024);
            BufferedReader reader2 = new BufferedReader(new InputStreamReader(responseStream,
                    "UTF-8"), 1024);
            responseStream = null;
            while (true) {
                String line = reader2.readLine();
                if (line == null) {
                    break;
                }
                try {
                    sbReponse.append(line);
                    sbReponse.append("\r\n");
                } catch (Throwable th2) {
                    th = th2;
                    reader = reader2;
                }
            }
            if (responseStream != null) {
                try {
                    responseStream.close();
                } catch (IOException e) {
                    Log.e(LogTag, "Failed to close responseStream" + e.toString());
                }
            }
            if (reader2 != null) {
                try {
                    reader2.close();
                } catch (IOException e2) {
                    Log.e(LogTag, "Failed to close reader" + e2.toString());
                }
            }
            return sbReponse.toString();
        } catch (Throwable th3) {
            th = th3;
            if (responseStream != null) {
                try {
                    responseStream.close();
                } catch (IOException e22) {
                    Log.e(LogTag, "Failed to close responseStream" + e22.toString());
                }
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e222) {
                    Log.e(LogTag, "Failed to close reader" + e222.toString());
                }
            }
            throw th;
        }
    }

    private static boolean isCmwap(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService("phone");
        if (tm == null) {
            return false;
        }
        if (!"CN".equalsIgnoreCase(tm.getSimCountryIso())) {
            return false;
        }
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService("connectivity");
        if (cm == null) {
            return false;
        }
        NetworkInfo info = cm.getActiveNetworkInfo();
        if (info == null) {
            return false;
        }
        String extraInfo = info.getExtraInfo();
        if (TextUtils.isEmpty(extraInfo) || extraInfo.length() < 3 || extraInfo.contains("ctwap")) {
            return false;
        }
        return extraInfo.regionMatches(true, extraInfo.length() - 3, "wap", 0, 3);
    }

    private static boolean isCtwap(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService("phone");
        if (tm == null) {
            return false;
        }
        if (!"CN".equalsIgnoreCase(tm.getSimCountryIso())) {
            return false;
        }
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService("connectivity");
        if (cm == null) {
            return false;
        }
        NetworkInfo info = cm.getActiveNetworkInfo();
        if (info == null) {
            return false;
        }
        String extraInfo = info.getExtraInfo();
        if (TextUtils.isEmpty(extraInfo) || extraInfo.length() < 3) {
            return false;
        }
        return extraInfo.contains("ctwap");
    }

    private static HttpURLConnection getHttpUrlConnection(Context context, URL url) throws
            IOException {
        if (isCtwap(context)) {
            return (HttpURLConnection) url.openConnection(new Proxy(Type.HTTP, new
                    InetSocketAddress("10.0.0.200", 80)));
        }
        if (!isCmwap(context)) {
            return (HttpURLConnection) url.openConnection();
        }
        HttpURLConnection conn = (HttpURLConnection) new URL(getCMWapUrl(url)).openConnection();
        conn.addRequestProperty(CMWAP_HEADER_HOST_KEY, url.getHost());
        return conn;
    }

    private static String getCMWapUrl(URL oriUrl) {
        StringBuilder gatewayBuilder = new StringBuilder();
        gatewayBuilder.append(oriUrl.getProtocol()).append("://").append(CMWAP_GATEWAY).append(oriUrl.getPath());
        if (!TextUtils.isEmpty(oriUrl.getQuery())) {
            gatewayBuilder.append("?").append(oriUrl.getQuery());
        }
        return gatewayBuilder.toString();
    }
}
