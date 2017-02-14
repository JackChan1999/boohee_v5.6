package com.android.volley.toolbox;

import android.os.SystemClock;
import com.android.volley.Cache.Entry;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RetryPolicy;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.baidu.location.LocationClientOption;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.StatusLine;
import org.apache.http.impl.cookie.DateUtils;

public class BasicNetwork implements Network {
    protected static final boolean DEBUG = VolleyLog.DEBUG;
    private static int DEFAULT_POOL_SIZE = 4096;
    private static int SLOW_REQUEST_THRESHOLD_MS = LocationClientOption.MIN_SCAN_SPAN_NETWORK;
    protected final HttpStack mHttpStack;
    protected final ByteArrayPool mPool;

    public BasicNetwork(HttpStack httpStack) {
        this(httpStack, new ByteArrayPool(DEFAULT_POOL_SIZE));
    }

    public BasicNetwork(HttpStack httpStack, ByteArrayPool pool) {
        this.mHttpStack = httpStack;
        this.mPool = pool;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public com.android.volley.NetworkResponse performRequest(com.android.volley.Request<?> r28) throws com.android.volley.VolleyError {
        /*
        r27 = this;
        r24 = android.os.SystemClock.elapsedRealtime();
    L_0x0004:
        r22 = 0;
        r26 = 0;
        r6 = java.util.Collections.emptyMap();
        r21 = new java.util.HashMap;	 Catch:{ SocketTimeoutException -> 0x01b3, ConnectTimeoutException -> 0x00df, MalformedURLException -> 0x00f1, IOException -> 0x0112 }
        r21.<init>();	 Catch:{ SocketTimeoutException -> 0x01b3, ConnectTimeoutException -> 0x00df, MalformedURLException -> 0x00f1, IOException -> 0x0112 }
        r3 = r28.getCacheEntry();	 Catch:{ SocketTimeoutException -> 0x01b3, ConnectTimeoutException -> 0x00df, MalformedURLException -> 0x00f1, IOException -> 0x0112 }
        r0 = r27;
        r1 = r21;
        r0.addCacheHeaders(r1, r3);	 Catch:{ SocketTimeoutException -> 0x01b3, ConnectTimeoutException -> 0x00df, MalformedURLException -> 0x00f1, IOException -> 0x0112 }
        r0 = r27;
        r3 = r0.mHttpStack;	 Catch:{ SocketTimeoutException -> 0x01b3, ConnectTimeoutException -> 0x00df, MalformedURLException -> 0x00f1, IOException -> 0x0112 }
        r0 = r28;
        r1 = r21;
        r22 = r3.performRequest(r0, r1);	 Catch:{ SocketTimeoutException -> 0x01b3, ConnectTimeoutException -> 0x00df, MalformedURLException -> 0x00f1, IOException -> 0x0112 }
        r12 = r22.getStatusLine();	 Catch:{ SocketTimeoutException -> 0x01b3, ConnectTimeoutException -> 0x00df, MalformedURLException -> 0x00f1, IOException -> 0x0112 }
        r14 = r12.getStatusCode();	 Catch:{ SocketTimeoutException -> 0x01b3, ConnectTimeoutException -> 0x00df, MalformedURLException -> 0x00f1, IOException -> 0x0112 }
        r3 = r22.getAllHeaders();	 Catch:{ SocketTimeoutException -> 0x01b3, ConnectTimeoutException -> 0x00df, MalformedURLException -> 0x00f1, IOException -> 0x0112 }
        r6 = convertHeaders(r3);	 Catch:{ SocketTimeoutException -> 0x01b3, ConnectTimeoutException -> 0x00df, MalformedURLException -> 0x00f1, IOException -> 0x0112 }
        r3 = 304; // 0x130 float:4.26E-43 double:1.5E-321;
        if (r14 != r3) goto L_0x0075;
    L_0x003c:
        r20 = r28.getCacheEntry();	 Catch:{ SocketTimeoutException -> 0x01b3, ConnectTimeoutException -> 0x00df, MalformedURLException -> 0x00f1, IOException -> 0x0112 }
        if (r20 != 0) goto L_0x0054;
    L_0x0042:
        r3 = new com.android.volley.NetworkResponse;	 Catch:{ SocketTimeoutException -> 0x01b3, ConnectTimeoutException -> 0x00df, MalformedURLException -> 0x00f1, IOException -> 0x0112 }
        r4 = 304; // 0x130 float:4.26E-43 double:1.5E-321;
        r5 = 0;
        r7 = 1;
        r16 = android.os.SystemClock.elapsedRealtime();	 Catch:{ SocketTimeoutException -> 0x01b3, ConnectTimeoutException -> 0x00df, MalformedURLException -> 0x00f1, IOException -> 0x0112 }
        r8 = r16 - r24;
        r3.<init>(r4, r5, r6, r7, r8);	 Catch:{ SocketTimeoutException -> 0x01b3, ConnectTimeoutException -> 0x00df, MalformedURLException -> 0x00f1, IOException -> 0x0112 }
        r11 = r26;
    L_0x0053:
        return r3;
    L_0x0054:
        r0 = r20;
        r3 = r0.responseHeaders;	 Catch:{ SocketTimeoutException -> 0x01b3, ConnectTimeoutException -> 0x00df, MalformedURLException -> 0x00f1, IOException -> 0x0112 }
        r3.putAll(r6);	 Catch:{ SocketTimeoutException -> 0x01b3, ConnectTimeoutException -> 0x00df, MalformedURLException -> 0x00f1, IOException -> 0x0112 }
        r7 = new com.android.volley.NetworkResponse;	 Catch:{ SocketTimeoutException -> 0x01b3, ConnectTimeoutException -> 0x00df, MalformedURLException -> 0x00f1, IOException -> 0x0112 }
        r8 = 304; // 0x130 float:4.26E-43 double:1.5E-321;
        r0 = r20;
        r9 = r0.data;	 Catch:{ SocketTimeoutException -> 0x01b3, ConnectTimeoutException -> 0x00df, MalformedURLException -> 0x00f1, IOException -> 0x0112 }
        r0 = r20;
        r10 = r0.responseHeaders;	 Catch:{ SocketTimeoutException -> 0x01b3, ConnectTimeoutException -> 0x00df, MalformedURLException -> 0x00f1, IOException -> 0x0112 }
        r11 = 1;
        r4 = android.os.SystemClock.elapsedRealtime();	 Catch:{ SocketTimeoutException -> 0x01b3, ConnectTimeoutException -> 0x00df, MalformedURLException -> 0x00f1, IOException -> 0x0112 }
        r12 = r4 - r24;
        r7.<init>(r8, r9, r10, r11, r12);	 Catch:{ SocketTimeoutException -> 0x01b3, ConnectTimeoutException -> 0x00df, MalformedURLException -> 0x00f1, IOException -> 0x0112 }
        r11 = r26;
        r3 = r7;
        goto L_0x0053;
    L_0x0075:
        r3 = 301; // 0x12d float:4.22E-43 double:1.487E-321;
        if (r14 == r3) goto L_0x007d;
    L_0x0079:
        r3 = 302; // 0x12e float:4.23E-43 double:1.49E-321;
        if (r14 != r3) goto L_0x008d;
    L_0x007d:
        r3 = "Location";
        r23 = r6.get(r3);	 Catch:{ SocketTimeoutException -> 0x01b3, ConnectTimeoutException -> 0x00df, MalformedURLException -> 0x00f1, IOException -> 0x0112 }
        r23 = (java.lang.String) r23;	 Catch:{ SocketTimeoutException -> 0x01b3, ConnectTimeoutException -> 0x00df, MalformedURLException -> 0x00f1, IOException -> 0x0112 }
        r0 = r28;
        r1 = r23;
        r0.setRedirectUrl(r1);	 Catch:{ SocketTimeoutException -> 0x01b3, ConnectTimeoutException -> 0x00df, MalformedURLException -> 0x00f1, IOException -> 0x0112 }
    L_0x008d:
        r3 = r22.getEntity();	 Catch:{ SocketTimeoutException -> 0x01b3, ConnectTimeoutException -> 0x00df, MalformedURLException -> 0x00f1, IOException -> 0x0112 }
        if (r3 == 0) goto L_0x00c8;
    L_0x0093:
        r3 = r22.getEntity();	 Catch:{ SocketTimeoutException -> 0x01b3, ConnectTimeoutException -> 0x00df, MalformedURLException -> 0x00f1, IOException -> 0x0112 }
        r0 = r27;
        r11 = r0.entityToBytes(r3);	 Catch:{ SocketTimeoutException -> 0x01b3, ConnectTimeoutException -> 0x00df, MalformedURLException -> 0x00f1, IOException -> 0x0112 }
    L_0x009d:
        r4 = android.os.SystemClock.elapsedRealtime();	 Catch:{ SocketTimeoutException -> 0x00b8, ConnectTimeoutException -> 0x01b0, MalformedURLException -> 0x01ad, IOException -> 0x01aa }
        r8 = r4 - r24;
        r7 = r27;
        r10 = r28;
        r7.logSlowRequests(r8, r10, r11, r12);	 Catch:{ SocketTimeoutException -> 0x00b8, ConnectTimeoutException -> 0x01b0, MalformedURLException -> 0x01ad, IOException -> 0x01aa }
        r3 = 200; // 0xc8 float:2.8E-43 double:9.9E-322;
        if (r14 < r3) goto L_0x00b2;
    L_0x00ae:
        r3 = 299; // 0x12b float:4.19E-43 double:1.477E-321;
        if (r14 <= r3) goto L_0x00cc;
    L_0x00b2:
        r3 = new java.io.IOException;	 Catch:{ SocketTimeoutException -> 0x00b8, ConnectTimeoutException -> 0x01b0, MalformedURLException -> 0x01ad, IOException -> 0x01aa }
        r3.<init>();	 Catch:{ SocketTimeoutException -> 0x00b8, ConnectTimeoutException -> 0x01b0, MalformedURLException -> 0x01ad, IOException -> 0x01aa }
        throw r3;	 Catch:{ SocketTimeoutException -> 0x00b8, ConnectTimeoutException -> 0x01b0, MalformedURLException -> 0x01ad, IOException -> 0x01aa }
    L_0x00b8:
        r2 = move-exception;
    L_0x00b9:
        r3 = "socket";
        r4 = new com.android.volley.TimeoutError;
        r4.<init>();
        r0 = r28;
        attemptRetryOnException(r3, r0, r4);
        goto L_0x0004;
    L_0x00c8:
        r3 = 0;
        r11 = new byte[r3];	 Catch:{ SocketTimeoutException -> 0x01b3, ConnectTimeoutException -> 0x00df, MalformedURLException -> 0x00f1, IOException -> 0x0112 }
        goto L_0x009d;
    L_0x00cc:
        r13 = new com.android.volley.NetworkResponse;	 Catch:{ SocketTimeoutException -> 0x00b8, ConnectTimeoutException -> 0x01b0, MalformedURLException -> 0x01ad, IOException -> 0x01aa }
        r17 = 0;
        r4 = android.os.SystemClock.elapsedRealtime();	 Catch:{ SocketTimeoutException -> 0x00b8, ConnectTimeoutException -> 0x01b0, MalformedURLException -> 0x01ad, IOException -> 0x01aa }
        r18 = r4 - r24;
        r15 = r11;
        r16 = r6;
        r13.<init>(r14, r15, r16, r17, r18);	 Catch:{ SocketTimeoutException -> 0x00b8, ConnectTimeoutException -> 0x01b0, MalformedURLException -> 0x01ad, IOException -> 0x01aa }
        r3 = r13;
        goto L_0x0053;
    L_0x00df:
        r2 = move-exception;
        r11 = r26;
    L_0x00e2:
        r3 = "connection";
        r4 = new com.android.volley.TimeoutError;
        r4.<init>();
        r0 = r28;
        attemptRetryOnException(r3, r0, r4);
        goto L_0x0004;
    L_0x00f1:
        r2 = move-exception;
        r11 = r26;
    L_0x00f4:
        r3 = new java.lang.RuntimeException;
        r4 = new java.lang.StringBuilder;
        r4.<init>();
        r5 = "Bad URL ";
        r4 = r4.append(r5);
        r5 = r28.getUrl();
        r4 = r4.append(r5);
        r4 = r4.toString();
        r3.<init>(r4, r2);
        throw r3;
    L_0x0112:
        r2 = move-exception;
        r11 = r26;
    L_0x0115:
        r14 = 0;
        r13 = 0;
        if (r22 == 0) goto L_0x0169;
    L_0x0119:
        r3 = r22.getStatusLine();
        r14 = r3.getStatusCode();
        r3 = 301; // 0x12d float:4.22E-43 double:1.487E-321;
        if (r14 == r3) goto L_0x0129;
    L_0x0125:
        r3 = 302; // 0x12e float:4.23E-43 double:1.49E-321;
        if (r14 != r3) goto L_0x016f;
    L_0x0129:
        r3 = "Request at %s has been redirected to %s";
        r4 = 2;
        r4 = new java.lang.Object[r4];
        r5 = 0;
        r7 = r28.getOriginUrl();
        r4[r5] = r7;
        r5 = 1;
        r7 = r28.getUrl();
        r4[r5] = r7;
        com.android.volley.VolleyLog.e(r3, r4);
    L_0x0140:
        if (r11 == 0) goto L_0x01a4;
    L_0x0142:
        r13 = new com.android.volley.NetworkResponse;
        r17 = 0;
        r4 = android.os.SystemClock.elapsedRealtime();
        r18 = r4 - r24;
        r15 = r11;
        r16 = r6;
        r13.<init>(r14, r15, r16, r17, r18);
        r3 = 401; // 0x191 float:5.62E-43 double:1.98E-321;
        if (r14 == r3) goto L_0x015a;
    L_0x0156:
        r3 = 403; // 0x193 float:5.65E-43 double:1.99E-321;
        if (r14 != r3) goto L_0x0187;
    L_0x015a:
        r3 = "auth";
        r4 = new com.android.volley.AuthFailureError;
        r4.<init>(r13);
        r0 = r28;
        attemptRetryOnException(r3, r0, r4);
        goto L_0x0004;
    L_0x0169:
        r3 = new com.android.volley.NoConnectionError;
        r3.<init>(r2);
        throw r3;
    L_0x016f:
        r3 = "Unexpected response code %d for %s";
        r4 = 2;
        r4 = new java.lang.Object[r4];
        r5 = 0;
        r7 = java.lang.Integer.valueOf(r14);
        r4[r5] = r7;
        r5 = 1;
        r7 = r28.getUrl();
        r4[r5] = r7;
        com.android.volley.VolleyLog.e(r3, r4);
        goto L_0x0140;
    L_0x0187:
        r3 = 301; // 0x12d float:4.22E-43 double:1.487E-321;
        if (r14 == r3) goto L_0x018f;
    L_0x018b:
        r3 = 302; // 0x12e float:4.23E-43 double:1.49E-321;
        if (r14 != r3) goto L_0x019e;
    L_0x018f:
        r3 = "redirect";
        r4 = new com.android.volley.RedirectError;
        r4.<init>(r13);
        r0 = r28;
        attemptRetryOnException(r3, r0, r4);
        goto L_0x0004;
    L_0x019e:
        r3 = new com.android.volley.ServerError;
        r3.<init>(r13);
        throw r3;
    L_0x01a4:
        r3 = new com.android.volley.NetworkError;
        r3.<init>(r2);
        throw r3;
    L_0x01aa:
        r2 = move-exception;
        goto L_0x0115;
    L_0x01ad:
        r2 = move-exception;
        goto L_0x00f4;
    L_0x01b0:
        r2 = move-exception;
        goto L_0x00e2;
    L_0x01b3:
        r2 = move-exception;
        r11 = r26;
        goto L_0x00b9;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.volley.toolbox.BasicNetwork.performRequest(com.android.volley.Request):com.android.volley.NetworkResponse");
    }

    private void logSlowRequests(long requestLifetime, Request<?> request, byte[] responseContents, StatusLine statusLine) {
        if (DEBUG || requestLifetime > ((long) SLOW_REQUEST_THRESHOLD_MS)) {
            String str = "HTTP response for request=<%s> [lifetime=%d], [size=%s], [rc=%d], [retryCount=%s]";
            Object[] objArr = new Object[5];
            objArr[0] = request;
            objArr[1] = Long.valueOf(requestLifetime);
            objArr[2] = responseContents != null ? Integer.valueOf(responseContents.length) : "null";
            objArr[3] = Integer.valueOf(statusLine.getStatusCode());
            objArr[4] = Integer.valueOf(request.getRetryPolicy().getCurrentRetryCount());
            VolleyLog.d(str, objArr);
        }
    }

    private static void attemptRetryOnException(String logPrefix, Request<?> request, VolleyError exception) throws VolleyError {
        RetryPolicy retryPolicy = request.getRetryPolicy();
        int oldTimeout = request.getTimeoutMs();
        try {
            retryPolicy.retry(exception);
            request.addMarker(String.format("%s-retry [timeout=%s]", new Object[]{logPrefix, Integer.valueOf(oldTimeout)}));
        } catch (VolleyError e) {
            request.addMarker(String.format("%s-timeout-giveup [timeout=%s]", new Object[]{logPrefix, Integer.valueOf(oldTimeout)}));
            throw e;
        }
    }

    private void addCacheHeaders(Map<String, String> headers, Entry entry) {
        if (entry != null) {
            if (entry.etag != null) {
                headers.put("If-None-Match", entry.etag);
            }
            if (entry.lastModified > 0) {
                headers.put("If-Modified-Since", DateUtils.formatDate(new Date(entry.lastModified)));
            }
        }
    }

    protected void logError(String what, String url, long start) {
        long now = SystemClock.elapsedRealtime();
        VolleyLog.v("HTTP ERROR(%s) %d ms to fetch %s", what, Long.valueOf(now - start), url);
    }

    private byte[] entityToBytes(HttpEntity entity) throws IOException, ServerError {
        PoolingByteArrayOutputStream bytes = new PoolingByteArrayOutputStream(this.mPool, (int) entity.getContentLength());
        byte[] buffer = null;
        try {
            InputStream in = entity.getContent();
            if (in == null) {
                throw new ServerError();
            }
            buffer = this.mPool.getBuf(1024);
            while (true) {
                int count = in.read(buffer);
                if (count == -1) {
                    break;
                }
                bytes.write(buffer, 0, count);
            }
            byte[] toByteArray = bytes.toByteArray();
            return toByteArray;
        } finally {
            try {
                entity.consumeContent();
            } catch (IOException e) {
                VolleyLog.v("Error occured when calling consumingContent", new Object[0]);
            }
            this.mPool.returnBuf(buffer);
            bytes.close();
        }
    }

    protected static Map<String, String> convertHeaders(Header[] headers) {
        Map<String, String> result = new TreeMap(String.CASE_INSENSITIVE_ORDER);
        for (int i = 0; i < headers.length; i++) {
            result.put(headers[i].getName(), headers[i].getValue());
        }
        return result;
    }
}
