package com.mob.tools.network;

import android.content.Context;

import com.alipay.sdk.cons.b;
import com.alipay.sdk.sys.a;
import com.boohee.one.http.DnspodFree;
import com.mob.tools.utils.Data;
import com.mob.tools.utils.Hashon;
import com.mob.tools.utils.Ln;
import com.mob.tools.utils.R;
import com.qiniu.android.common.Constants;
import com.qiniu.android.http.Client;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URLConnection;
import java.security.KeyStore;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.UUID;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.scheme.SocketFactory;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.util.EntityUtils;
import org.java_websocket.WebSocket;

public class NetworkHelper {
    public static int connectionTimeout;
    public static int readTimout;

    private HttpPost getFilePost(String str, ArrayList<KVPair<String>> arrayList,
                                 ArrayList<KVPair<String>> arrayList2) throws Throwable {
        Iterator it;
        HTTPPart stringPart;
        String uuid = UUID.randomUUID().toString();
        HttpPost httpPost = new HttpPost(str);
        httpPost.setHeader("Content-Type", "multipart/form-data; boundary=" + uuid);
        MultiPart multiPart = new MultiPart();
        HTTPPart stringPart2 = new StringPart();
        if (arrayList != null) {
            it = arrayList.iterator();
            while (it.hasNext()) {
                KVPair kVPair = (KVPair) it.next();
                stringPart2.append("--").append(uuid).append("\r\n");
                stringPart2.append("Content-Disposition: form-data; name=\"").append(kVPair.name)
                        .append("\"\r\n\r\n");
                stringPart2.append((String) kVPair.value).append("\r\n");
            }
        }
        multiPart.append(stringPart2);
        it = arrayList2.iterator();
        while (it.hasNext()) {
            kVPair = (KVPair) it.next();
            HTTPPart stringPart3 = new StringPart();
            File file = new File((String) kVPair.value);
            stringPart3.append("--").append(uuid).append("\r\n");
            stringPart3.append("Content-Disposition: form-data; name=\"").append(kVPair.name)
                    .append("\"; filename=\"").append(file.getName()).append("\"\r\n");
            String contentTypeFor = URLConnection.getFileNameMap().getContentTypeFor((String)
                    kVPair.value);
            if (contentTypeFor == null || contentTypeFor.length() <= 0) {
                if (((String) kVPair.value).toLowerCase().endsWith("jpg") || ((String) kVPair
                        .value).toLowerCase().endsWith("jpeg")) {
                    contentTypeFor = "image/jpeg";
                } else if (((String) kVPair.value).toLowerCase().endsWith("png")) {
                    contentTypeFor = "image/png";
                } else if (((String) kVPair.value).toLowerCase().endsWith("gif")) {
                    contentTypeFor = "image/gif";
                } else {
                    InputStream fileInputStream = new FileInputStream((String) kVPair.value);
                    contentTypeFor = URLConnection.guessContentTypeFromStream(fileInputStream);
                    fileInputStream.close();
                    if (contentTypeFor == null || contentTypeFor.length() <= 0) {
                        contentTypeFor = "application/octet-stream";
                    }
                }
            }
            stringPart3.append("Content-Type: ").append(contentTypeFor).append("\r\n\r\n");
            multiPart.append(stringPart3);
            stringPart2 = new FilePart();
            stringPart2.setFile((String) kVPair.value);
            multiPart.append(stringPart2);
            stringPart = new StringPart();
            stringPart.append("\r\n");
            multiPart.append(stringPart);
        }
        stringPart = new StringPart();
        stringPart.append("--").append(uuid).append("--\r\n");
        multiPart.append(stringPart);
        httpPost.setEntity(multiPart.getInputStreamEntity());
        return httpPost;
    }

    private HttpClient getSSLHttpClient() throws Throwable {
        KeyStore instance = KeyStore.getInstance(KeyStore.getDefaultType());
        instance.load(null, null);
        SocketFactory sSLSocketFactoryEx = new SSLSocketFactoryEx(instance);
        sSLSocketFactoryEx.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
        HttpParams basicHttpParams = new BasicHttpParams();
        HttpProtocolParams.setVersion(basicHttpParams, HttpVersion.HTTP_1_1);
        HttpProtocolParams.setContentCharset(basicHttpParams, "UTF-8");
        SchemeRegistry schemeRegistry = new SchemeRegistry();
        schemeRegistry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
        schemeRegistry.register(new Scheme(b.a, sSLSocketFactoryEx, WebSocket.DEFAULT_WSS_PORT));
        return new DefaultHttpClient(new ThreadSafeClientConnManager(basicHttpParams,
                schemeRegistry), basicHttpParams);
    }

    private HttpPost getTextPost(String str, ArrayList<KVPair<String>> arrayList) throws Throwable {
        HttpPost httpPost = new HttpPost(str);
        if (arrayList != null) {
            StringPart stringPart = new StringPart();
            stringPart.append(kvPairsToUrl(arrayList));
            HttpEntity inputStreamEntity = stringPart.getInputStreamEntity();
            inputStreamEntity.setContentType(Client.FormMime);
            httpPost.setEntity(inputStreamEntity);
        }
        return httpPost;
    }

    private String kvPairsToUrl(ArrayList<KVPair<String>> arrayList) throws Throwable {
        StringBuilder stringBuilder = new StringBuilder();
        Iterator it = arrayList.iterator();
        while (it.hasNext()) {
            KVPair kVPair = (KVPair) it.next();
            String urlEncode = Data.urlEncode(kVPair.name, Constants.UTF_8);
            String urlEncode2 = kVPair.value != null ? Data.urlEncode((String) kVPair.value,
                    Constants.UTF_8) : "";
            if (stringBuilder.length() > 0) {
                stringBuilder.append('&');
            }
            stringBuilder.append(urlEncode).append('=').append(urlEncode2);
        }
        return stringBuilder.toString();
    }

    public String downloadCache(Context context, String str, String str2, boolean z) throws
            Throwable {
        long currentTimeMillis = System.currentTimeMillis();
        Ln.i("downloading: " + str, new Object[0]);
        if (z) {
            File file = new File(R.getCachePath(context, str2), Data.MD5(str));
            if (z && file.exists()) {
                Ln.i("use time: " + (System.currentTimeMillis() - currentTimeMillis), new
                        Object[0]);
                return file.getAbsolutePath();
            }
        }
        HttpUriRequest httpGet = new HttpGet(str);
        HttpClient sSLHttpClient = str.startsWith("https://") ? getSSLHttpClient() : new
                DefaultHttpClient();
        HttpResponse execute = sSLHttpClient.execute(httpGet);
        int statusCode = execute.getStatusLine().getStatusCode();
        String MD5;
        if (statusCode == 200) {
            String str3 = null;
            Header[] headers = execute.getHeaders("Content-Disposition");
            if (headers != null && headers.length > 0) {
                for (String str4 : headers[0].getValue().split(DnspodFree.IP_SPLIT)) {
                    if (str4.trim().startsWith("filename")) {
                        str3 = str4.split("=")[1];
                        if (str3.startsWith(a.e) && str3.endsWith(a.e)) {
                            str3 = str3.substring(1, str3.length() - 1);
                        }
                    }
                }
            }
            if (str3 == null) {
                MD5 = Data.MD5(str);
                Header[] headers2 = execute.getHeaders("Content-Type");
                if (headers2 != null && headers2.length > 0) {
                    str3 = headers2[0].getValue().trim();
                    if (str3.startsWith("image/")) {
                        str3 = str3.substring("image/".length());
                        StringBuilder append = new StringBuilder().append(MD5).append(".");
                        if ("jpeg".equals(str3)) {
                            str3 = "jpg";
                        }
                        str3 = append.append(str3).toString();
                    } else {
                        int lastIndexOf = str.lastIndexOf(47);
                        str3 = null;
                        if (lastIndexOf > 0) {
                            str3 = str.substring(lastIndexOf + 1);
                        }
                        if (str3 != null && str3.length() > 0) {
                            lastIndexOf = str3.lastIndexOf(46);
                            if (lastIndexOf > 0 && str3.length() - lastIndexOf < 10) {
                                str3 = MD5 + str3.substring(lastIndexOf);
                            }
                        }
                    }
                }
                str3 = MD5;
            }
            File file2 = new File(R.getCachePath(context, str2), str3);
            if (z && file2.exists()) {
                sSLHttpClient.getConnectionManager().shutdown();
                Ln.i("use time: " + (System.currentTimeMillis() - currentTimeMillis), new
                        Object[0]);
                return file2.getAbsolutePath();
            }
            if (!file2.getParentFile().exists()) {
                file2.getParentFile().mkdirs();
            }
            if (file2.exists()) {
                file2.delete();
            }
            try {
                InputStream content = execute.getEntity().getContent();
                FileOutputStream fileOutputStream = new FileOutputStream(file2);
                byte[] bArr = new byte[1024];
                for (statusCode = content.read(bArr); statusCode > 0; statusCode = content.read
                        (bArr)) {
                    fileOutputStream.write(bArr, 0, statusCode);
                }
                fileOutputStream.flush();
                content.close();
                fileOutputStream.close();
                sSLHttpClient.getConnectionManager().shutdown();
                Ln.i("use time: " + (System.currentTimeMillis() - currentTimeMillis), new
                        Object[0]);
                return file2.getAbsolutePath();
            } catch (Throwable th) {
                if (file2.exists()) {
                    file2.delete();
                }
            }
        } else {
            MD5 = EntityUtils.toString(execute.getEntity(), Constants.UTF_8);
            HashMap hashMap = new HashMap();
            hashMap.put("error", MD5);
            hashMap.put("status", Integer.valueOf(statusCode));
            sSLHttpClient.getConnectionManager().shutdown();
            throw new Throwable(new Hashon().fromHashMap(hashMap));
        }
    }

    public void getHttpPostResponse(String str, ArrayList<KVPair<String>> arrayList,
                                    KVPair<String> kVPair, ArrayList<KVPair<String>> arrayList2,
                                    ArrayList<KVPair<?>> arrayList3, HttpResponseCallback
                                            httpResponseCallback) throws Throwable {
        HttpUriRequest filePost;
        long currentTimeMillis = System.currentTimeMillis();
        Ln.i("httpPost: " + str, new Object[0]);
        if (kVPair == null || kVPair.value == null || !new File((String) kVPair.value).exists()) {
            Object textPost = getTextPost(str, arrayList);
        } else {
            ArrayList arrayList4 = new ArrayList();
            arrayList4.add(kVPair);
            filePost = getFilePost(str, arrayList, arrayList4);
        }
        if (arrayList2 != null) {
            Iterator it = arrayList2.iterator();
            while (it.hasNext()) {
                KVPair kVPair2 = (KVPair) it.next();
                filePost.setHeader(kVPair2.name, (String) kVPair2.value);
            }
        }
        HttpParams basicHttpParams = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(basicHttpParams, connectionTimeout);
        HttpConnectionParams.setSoTimeout(basicHttpParams, readTimout);
        if (arrayList3 != null) {
            Iterator it2 = arrayList3.iterator();
            while (it2.hasNext()) {
                kVPair2 = (KVPair) it2.next();
                try {
                    basicHttpParams.setIntParameter(kVPair2.name, R.parseInt(String.valueOf
                            (kVPair2.value)));
                } catch (Exception e) {
                }
            }
        }
        filePost.setParams(basicHttpParams);
        HttpClient sSLHttpClient = str.startsWith("https://") ? getSSLHttpClient() : new
                DefaultHttpClient();
        HttpResponse execute = sSLHttpClient.execute(filePost);
        if (httpResponseCallback != null) {
            httpResponseCallback.onResponse(execute);
        }
        sSLHttpClient.getConnectionManager().shutdown();
        Ln.i("use time: " + (System.currentTimeMillis() - currentTimeMillis), new Object[0]);
    }

    public String httpGet(String str, ArrayList<KVPair<String>> arrayList,
                          ArrayList<KVPair<String>> arrayList2, ArrayList<KVPair<?>> arrayList3)
            throws Throwable {
        long currentTimeMillis = System.currentTimeMillis();
        Ln.i("httpGet: " + str, new Object[0]);
        if (arrayList != null) {
            String kvPairsToUrl = kvPairsToUrl(arrayList);
            if (kvPairsToUrl.length() > 0) {
                str = str + "?" + kvPairsToUrl;
            }
        }
        HttpUriRequest httpGet = new HttpGet(str);
        if (arrayList2 != null) {
            Iterator it = arrayList2.iterator();
            while (it.hasNext()) {
                KVPair kVPair = (KVPair) it.next();
                httpGet.setHeader(kVPair.name, (String) kVPair.value);
            }
        }
        HttpParams basicHttpParams = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(basicHttpParams, connectionTimeout);
        HttpConnectionParams.setSoTimeout(basicHttpParams, readTimout);
        if (arrayList3 != null) {
            Iterator it2 = arrayList3.iterator();
            while (it2.hasNext()) {
                kVPair = (KVPair) it2.next();
                try {
                    basicHttpParams.setIntParameter(kVPair.name, R.parseInt(String.valueOf(kVPair
                            .value)));
                } catch (Exception e) {
                }
            }
        }
        httpGet.setParams(basicHttpParams);
        HttpClient sSLHttpClient = str.startsWith("https://") ? getSSLHttpClient() : new
                DefaultHttpClient();
        HttpResponse execute = sSLHttpClient.execute(httpGet);
        int statusCode = execute.getStatusLine().getStatusCode();
        if (statusCode == 200) {
            String entityUtils = EntityUtils.toString(execute.getEntity(), Constants.UTF_8);
            sSLHttpClient.getConnectionManager().shutdown();
            Ln.i("use time: " + (System.currentTimeMillis() - currentTimeMillis), new Object[0]);
            return entityUtils;
        }
        entityUtils = EntityUtils.toString(execute.getEntity(), Constants.UTF_8);
        HashMap hashMap = new HashMap();
        hashMap.put("error", entityUtils);
        hashMap.put("status", Integer.valueOf(statusCode));
        sSLHttpClient.getConnectionManager().shutdown();
        throw new Throwable(new Hashon().fromHashMap(hashMap));
    }

    public ArrayList<KVPair<String>> httpHead(String str, ArrayList<KVPair<String>> arrayList,
                                              KVPair<String> kVPair, ArrayList<KVPair<String>>
                                                      arrayList2, ArrayList<KVPair<?>>
                                                      arrayList3) throws Throwable {
        long currentTimeMillis = System.currentTimeMillis();
        Ln.i("httpHead: " + str, new Object[0]);
        if (arrayList != null) {
            String kvPairsToUrl = kvPairsToUrl(arrayList);
            if (kvPairsToUrl.length() > 0) {
                str = str + "?" + kvPairsToUrl;
            }
        }
        HttpUriRequest httpHead = new HttpHead(str);
        if (arrayList2 != null) {
            Iterator it = arrayList2.iterator();
            while (it.hasNext()) {
                KVPair kVPair2 = (KVPair) it.next();
                httpHead.setHeader(kVPair2.name, (String) kVPair2.value);
            }
        }
        HttpParams basicHttpParams = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(basicHttpParams, connectionTimeout);
        HttpConnectionParams.setSoTimeout(basicHttpParams, readTimout);
        if (arrayList3 != null) {
            Iterator it2 = arrayList3.iterator();
            while (it2.hasNext()) {
                kVPair2 = (KVPair) it2.next();
                try {
                    basicHttpParams.setIntParameter(kVPair2.name, R.parseInt(String.valueOf
                            (kVPair2.value)));
                } catch (Exception e) {
                }
            }
        }
        httpHead.setParams(basicHttpParams);
        HttpClient sSLHttpClient = str.startsWith("https://") ? getSSLHttpClient() : new
                DefaultHttpClient();
        Header[] allHeaders = sSLHttpClient.execute(httpHead).getAllHeaders();
        ArrayList<KVPair<String>> arrayList4 = new ArrayList();
        if (allHeaders != null) {
            for (Header header : allHeaders) {
                arrayList4.add(new KVPair(header.getName(), header.getValue()));
            }
        }
        sSLHttpClient.getConnectionManager().shutdown();
        Ln.i("use time: " + (System.currentTimeMillis() - currentTimeMillis), new Object[0]);
        return arrayList4;
    }

    public String httpPatch(String str, ArrayList<KVPair<String>> arrayList, KVPair<String>
            kVPair, long j, ArrayList<KVPair<String>> arrayList2, ArrayList<KVPair<?>>
            arrayList3, OnReadListener onReadListener) throws Throwable {
        HashMap hashMap = new HashMap();
        httpPatch(str, arrayList, kVPair, j, arrayList2, arrayList3, onReadListener, new 2
        (this, hashMap));
        String str2 = (String) hashMap.get("resp");
        if (((Integer) hashMap.get("status")).intValue() == 200) {
            return str2;
        }
        throw new Throwable(str2);
    }

    public void httpPatch(String str, ArrayList<KVPair<String>> arrayList, KVPair<String> kVPair,
                          long j, ArrayList<KVPair<String>> arrayList2, ArrayList<KVPair<?>>
                                  arrayList3, OnReadListener onReadListener, HttpResponseCallback
                                  httpResponseCallback) throws Throwable {
        long currentTimeMillis = System.currentTimeMillis();
        Ln.i("httpPatch: " + str, new Object[0]);
        if (arrayList != null) {
            String kvPairsToUrl = kvPairsToUrl(arrayList);
            if (kvPairsToUrl.length() > 0) {
                str = str + "?" + kvPairsToUrl;
            }
        }
        HttpUriRequest httpPatch = new HttpPatch(str);
        if (arrayList2 != null) {
            Iterator it = arrayList2.iterator();
            while (it.hasNext()) {
                KVPair kVPair2 = (KVPair) it.next();
                httpPatch.setHeader(kVPair2.name, (String) kVPair2.value);
            }
        }
        FilePart filePart = new FilePart();
        filePart.setOnReadListener(onReadListener);
        filePart.setFile((String) kVPair.value);
        filePart.setOffset(j);
        HttpEntity inputStreamEntity = filePart.getInputStreamEntity();
        inputStreamEntity.setContentEncoding("application/offset+octet-stream");
        httpPatch.setEntity(inputStreamEntity);
        HttpParams basicHttpParams = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(basicHttpParams, connectionTimeout);
        HttpConnectionParams.setSoTimeout(basicHttpParams, readTimout);
        if (arrayList3 != null) {
            Iterator it2 = arrayList3.iterator();
            while (it2.hasNext()) {
                kVPair2 = (KVPair) it2.next();
                try {
                    basicHttpParams.setIntParameter(kVPair2.name, R.parseInt(String.valueOf
                            (kVPair2.value)));
                } catch (Exception e) {
                }
            }
        }
        httpPatch.setParams(basicHttpParams);
        HttpClient sSLHttpClient = str.startsWith("https://") ? getSSLHttpClient() : new
                DefaultHttpClient();
        HttpResponse execute = sSLHttpClient.execute(httpPatch);
        if (httpResponseCallback != null) {
            httpResponseCallback.onResponse(execute);
        }
        sSLHttpClient.getConnectionManager().shutdown();
        Ln.i("use time: " + (System.currentTimeMillis() - currentTimeMillis), new Object[0]);
    }

    public String httpPost(String str, ArrayList<KVPair<String>> arrayList, KVPair<String>
            kVPair, ArrayList<KVPair<String>> arrayList2, ArrayList<KVPair<?>> arrayList3) throws
            Throwable {
        ArrayList arrayList4 = new ArrayList();
        if (!(kVPair == null || kVPair.value == null || !new File((String) kVPair.value).exists()
        )) {
            arrayList4.add(kVPair);
        }
        return httpPostFiles(str, arrayList, arrayList4, arrayList2, arrayList3);
    }

    public void httpPost(String str, ArrayList<KVPair<String>> arrayList,
                         ArrayList<KVPair<String>> arrayList2, ArrayList<KVPair<String>>
                                 arrayList3, ArrayList<KVPair<?>> arrayList4,
                         HttpResponseCallback httpResponseCallback) throws Throwable {
        HttpUriRequest filePost;
        long currentTimeMillis = System.currentTimeMillis();
        Ln.i("httpPost: " + str, new Object[0]);
        if (arrayList2 == null || arrayList2.size() <= 0) {
            Object textPost = getTextPost(str, arrayList);
        } else {
            filePost = getFilePost(str, arrayList, arrayList2);
        }
        if (arrayList3 != null) {
            Iterator it = arrayList3.iterator();
            while (it.hasNext()) {
                KVPair kVPair = (KVPair) it.next();
                filePost.setHeader(kVPair.name, (String) kVPair.value);
            }
        }
        HttpParams basicHttpParams = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(basicHttpParams, connectionTimeout);
        HttpConnectionParams.setSoTimeout(basicHttpParams, readTimout);
        Ln.i("before set SO_TIMEOUT :" + basicHttpParams.getIntParameter("http.socket.timeout",
                readTimout), new Object[0]);
        Ln.i("before set CONNECTION_TIMEOUT :" + basicHttpParams.getIntParameter("http.connection" +
                ".timeout", connectionTimeout), new Object[0]);
        if (arrayList4 != null) {
            Iterator it2 = arrayList4.iterator();
            while (it2.hasNext()) {
                kVPair = (KVPair) it2.next();
                try {
                    basicHttpParams.setIntParameter(kVPair.name, R.parseInt(String.valueOf(kVPair
                            .value)));
                } catch (Exception e) {
                }
            }
        }
        Ln.i("before set SO_TIMEOUT :" + basicHttpParams.getIntParameter("http.socket.timeout",
                readTimout), new Object[0]);
        Ln.i("before set CONNECTION_TIMEOUT :" + basicHttpParams.getIntParameter("http.connection" +
                ".timeout", connectionTimeout), new Object[0]);
        filePost.setParams(basicHttpParams);
        HttpClient sSLHttpClient = str.startsWith("https://") ? getSSLHttpClient() : new
                DefaultHttpClient();
        HttpResponse execute = sSLHttpClient.execute(filePost);
        if (httpResponseCallback != null) {
            try {
                httpResponseCallback.onResponse(execute);
            } catch (Throwable th) {
                sSLHttpClient.getConnectionManager().shutdown();
            }
        }
        sSLHttpClient.getConnectionManager().shutdown();
        Ln.i("use time: " + (System.currentTimeMillis() - currentTimeMillis), new Object[0]);
    }

    public String httpPostFiles(String str, ArrayList<KVPair<String>> arrayList,
                                ArrayList<KVPair<String>> arrayList2, ArrayList<KVPair<String>>
                                        arrayList3, ArrayList<KVPair<?>> arrayList4) throws
            Throwable {
        HashMap hashMap = new HashMap();
        httpPost(str, arrayList, arrayList2, arrayList3, arrayList4, new 1 (this, hashMap));
        return (String) hashMap.get("resp");
    }

    public String httpPut(String str, ArrayList<KVPair<String>> arrayList, KVPair<String> kVPair,
                          ArrayList<KVPair<String>> arrayList2, ArrayList<KVPair<?>> arrayList3)
            throws Throwable {
        long currentTimeMillis = System.currentTimeMillis();
        Ln.i("httpPut: " + str, new Object[0]);
        if (arrayList != null) {
            String kvPairsToUrl = kvPairsToUrl(arrayList);
            if (kvPairsToUrl.length() > 0) {
                str = str + "?" + kvPairsToUrl;
            }
        }
        HttpUriRequest httpPut = new HttpPut(str);
        if (arrayList2 != null) {
            Iterator it = arrayList2.iterator();
            while (it.hasNext()) {
                KVPair kVPair2 = (KVPair) it.next();
                httpPut.setHeader(kVPair2.name, (String) kVPair2.value);
            }
        }
        FilePart filePart = new FilePart();
        filePart.setFile((String) kVPair.value);
        HttpEntity inputStreamEntity = filePart.getInputStreamEntity();
        inputStreamEntity.setContentEncoding("application/octet-stream");
        httpPut.setEntity(inputStreamEntity);
        HttpParams basicHttpParams = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(basicHttpParams, connectionTimeout);
        HttpConnectionParams.setSoTimeout(basicHttpParams, readTimout);
        if (arrayList3 != null) {
            Iterator it2 = arrayList3.iterator();
            while (it2.hasNext()) {
                kVPair2 = (KVPair) it2.next();
                try {
                    basicHttpParams.setIntParameter(kVPair2.name, R.parseInt(String.valueOf
                            (kVPair2.value)));
                } catch (Exception e) {
                }
            }
        }
        httpPut.setParams(basicHttpParams);
        HttpClient sSLHttpClient = str.startsWith("https://") ? getSSLHttpClient() : new
                DefaultHttpClient();
        HttpResponse execute = sSLHttpClient.execute(httpPut);
        int statusCode = execute.getStatusLine().getStatusCode();
        if (statusCode == 200 || statusCode == 201) {
            String entityUtils = EntityUtils.toString(execute.getEntity(), Constants.UTF_8);
            sSLHttpClient.getConnectionManager().shutdown();
            Ln.i("use time: " + (System.currentTimeMillis() - currentTimeMillis), new Object[0]);
            return entityUtils;
        }
        entityUtils = EntityUtils.toString(execute.getEntity(), Constants.UTF_8);
        HashMap hashMap = new HashMap();
        hashMap.put("error", entityUtils);
        hashMap.put("status", Integer.valueOf(statusCode));
        sSLHttpClient.getConnectionManager().shutdown();
        throw new Throwable(new Hashon().fromHashMap(hashMap));
    }

    public String jsonPost(String str, ArrayList<KVPair<String>> arrayList,
                           ArrayList<KVPair<String>> arrayList2, ArrayList<KVPair<?>> arrayList3)
            throws Throwable {
        long currentTimeMillis = System.currentTimeMillis();
        Ln.i("jsonPost: " + str, new Object[0]);
        HttpUriRequest httpPost = new HttpPost(str);
        StringPart stringPart = new StringPart();
        if (arrayList != null) {
            HashMap hashMap = new HashMap();
            Iterator it = arrayList.iterator();
            while (it.hasNext()) {
                KVPair kVPair = (KVPair) it.next();
                hashMap.put(kVPair.name, kVPair.value);
            }
            stringPart.append(new Hashon().fromHashMap(hashMap));
        }
        HttpEntity inputStreamEntity = stringPart.getInputStreamEntity();
        inputStreamEntity.setContentType("application/json");
        httpPost.setEntity(inputStreamEntity);
        if (arrayList2 != null) {
            Iterator it2 = arrayList2.iterator();
            while (it2.hasNext()) {
                kVPair = (KVPair) it2.next();
                httpPost.setHeader(kVPair.name, (String) kVPair.value);
            }
        }
        HttpParams basicHttpParams = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(basicHttpParams, connectionTimeout);
        HttpConnectionParams.setSoTimeout(basicHttpParams, readTimout);
        if (arrayList3 != null) {
            Iterator it3 = arrayList3.iterator();
            while (it3.hasNext()) {
                kVPair = (KVPair) it3.next();
                try {
                    basicHttpParams.setIntParameter(kVPair.name, R.parseInt(String.valueOf(kVPair
                            .value)));
                } catch (Exception e) {
                }
            }
        }
        httpPost.setParams(basicHttpParams);
        HttpClient sSLHttpClient = str.startsWith("https://") ? getSSLHttpClient() : new
                DefaultHttpClient();
        HttpResponse execute = sSLHttpClient.execute(httpPost);
        int statusCode = execute.getStatusLine().getStatusCode();
        if (statusCode == 200 || statusCode == 201) {
            String entityUtils = EntityUtils.toString(execute.getEntity(), Constants.UTF_8);
            sSLHttpClient.getConnectionManager().shutdown();
            Ln.i("use time: " + (System.currentTimeMillis() - currentTimeMillis), new Object[0]);
            return entityUtils;
        }
        entityUtils = EntityUtils.toString(execute.getEntity(), Constants.UTF_8);
        HashMap hashMap2 = new HashMap();
        hashMap2.put("error", entityUtils);
        hashMap2.put("status", Integer.valueOf(statusCode));
        sSLHttpClient.getConnectionManager().shutdown();
        throw new Throwable(new Hashon().fromHashMap(hashMap2));
    }

    public void rawGet(String str, RawNetworkCallback rawNetworkCallback) throws Throwable {
        HttpUriRequest httpGet = new HttpGet(str);
        HttpClient sSLHttpClient = str.startsWith("https://") ? getSSLHttpClient() : new
                DefaultHttpClient();
        HttpResponse execute = sSLHttpClient.execute(httpGet);
        int statusCode = execute.getStatusLine().getStatusCode();
        if (statusCode == 200) {
            if (rawNetworkCallback != null) {
                rawNetworkCallback.onResponse(execute.getEntity().getContent());
            }
            sSLHttpClient.getConnectionManager().shutdown();
            return;
        }
        String entityUtils = EntityUtils.toString(execute.getEntity(), Constants.UTF_8);
        HashMap hashMap = new HashMap();
        hashMap.put("error", entityUtils);
        hashMap.put("status", Integer.valueOf(statusCode));
        sSLHttpClient.getConnectionManager().shutdown();
        throw new Throwable(new Hashon().fromHashMap(hashMap));
    }

    public void rawPost(String str, ArrayList<KVPair<String>> arrayList, HTTPPart hTTPPart,
                        HttpResponseCallback httpResponseCallback) throws Throwable {
        long currentTimeMillis = System.currentTimeMillis();
        Ln.i("raw post: " + str, new Object[0]);
        HttpUriRequest httpPost = new HttpPost(str);
        if (arrayList != null) {
            Iterator it = arrayList.iterator();
            while (it.hasNext()) {
                KVPair kVPair = (KVPair) it.next();
                httpPost.setHeader(kVPair.name, (String) kVPair.value);
            }
        }
        httpPost.setEntity(hTTPPart.getInputStreamEntity());
        HttpClient sSLHttpClient = str.startsWith("https://") ? getSSLHttpClient() : new
                DefaultHttpClient();
        HttpResponse execute = sSLHttpClient.execute(httpPost);
        if (httpResponseCallback != null) {
            httpResponseCallback.onResponse(execute);
        }
        sSLHttpClient.getConnectionManager().shutdown();
        Ln.i("use time: " + (System.currentTimeMillis() - currentTimeMillis), new Object[0]);
    }

    public void rawPost(String str, ArrayList<KVPair<String>> arrayList, HTTPPart hTTPPart,
                        RawNetworkCallback rawNetworkCallback) throws Throwable {
        long currentTimeMillis = System.currentTimeMillis();
        Ln.i("raw post: " + str, new Object[0]);
        HttpUriRequest httpPost = new HttpPost(str);
        if (arrayList != null) {
            Iterator it = arrayList.iterator();
            while (it.hasNext()) {
                KVPair kVPair = (KVPair) it.next();
                httpPost.setHeader(kVPair.name, (String) kVPair.value);
            }
        }
        httpPost.setEntity(hTTPPart.getInputStreamEntity());
        HttpClient sSLHttpClient = str.startsWith("https://") ? getSSLHttpClient() : new
                DefaultHttpClient();
        HttpResponse execute = sSLHttpClient.execute(httpPost);
        int statusCode = execute.getStatusLine().getStatusCode();
        if (statusCode == 200) {
            Ln.i("use time: " + (System.currentTimeMillis() - currentTimeMillis), new Object[0]);
            if (rawNetworkCallback != null) {
                rawNetworkCallback.onResponse(execute.getEntity().getContent());
            }
            sSLHttpClient.getConnectionManager().shutdown();
            return;
        }
        String entityUtils = EntityUtils.toString(execute.getEntity(), Constants.UTF_8);
        HashMap hashMap = new HashMap();
        hashMap.put("error", entityUtils);
        hashMap.put("status", Integer.valueOf(statusCode));
        sSLHttpClient.getConnectionManager().shutdown();
        throw new Throwable(new Hashon().fromHashMap(hashMap));
    }
}
