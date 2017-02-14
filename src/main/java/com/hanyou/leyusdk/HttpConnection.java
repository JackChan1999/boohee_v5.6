package com.hanyou.leyusdk;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

public class HttpConnection implements Runnable {
    private static final int     BITMAP      = 4;
    private static final int     DELETE      = 3;
    public static final  int     DID_ERROR   = 1;
    public static final  int     DID_START   = 0;
    public static final  int     DID_SUCCEED = 2;
    private static final int     GET         = 0;
    private static final int     POST        = 1;
    private static final int     PUT         = 2;
    private static final Handler handler     = new Handler() {
        public void handleMessage(Message message) {
            switch (message.what) {
                case 2:
                    CallbackListener listener = message.obj;
                    Bundle data = message.getData();
                    if (listener != null && data != null) {
                        listener.callBack(data.getString("callbackkey"));
                        return;
                    }
                    return;
                default:
                    return;
            }
        }
    };
    private String           data;
    private HttpClient       httpClient;
    private CallbackListener listener;
    private int              method;
    private String           url;

    public interface CallbackListener {
        void callBack(String str);
    }

    public void create(int method, String url, String data, CallbackListener listener) {
        this.method = method;
        this.url = url;
        this.data = data;
        this.listener = listener;
        ConnectionManager.getInstance().push(this);
    }

    public void get(String url) {
        create(0, url, null, this.listener);
    }

    public void post(String url, String data, CallbackListener listener) {
        create(1, url, data, listener);
    }

    public void put(String url, String data) {
        create(2, url, data, this.listener);
    }

    public void delete(String url) {
        create(3, url, null, this.listener);
    }

    public void bitmap(String url) {
        create(4, url, null, this.listener);
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void run() {
        /*
        r7 = this;
        r4 = getHttpClient();
        r7.httpClient = r4;
        r2 = 0;
        r4 = r7.method;	 Catch:{ Exception -> 0x0041 }
        switch(r4) {
            case 0: goto L_0x0014;
            case 1: goto L_0x0022;
            default: goto L_0x000c;
        };
    L_0x000c:
        r4 = com.hanyou.leyusdk.ConnectionManager.getInstance();
        r4.didComplete(r7);
        return;
    L_0x0014:
        r4 = r7.httpClient;	 Catch:{ Exception -> 0x0041 }
        r5 = new org.apache.http.client.methods.HttpGet;	 Catch:{ Exception -> 0x0041 }
        r6 = r7.url;	 Catch:{ Exception -> 0x0041 }
        r5.<init>(r6);	 Catch:{ Exception -> 0x0041 }
        r2 = r4.execute(r5);	 Catch:{ Exception -> 0x0041 }
        goto L_0x000c;
    L_0x0022:
        r1 = new org.apache.http.client.methods.HttpPost;	 Catch:{ Exception -> 0x0041 }
        r4 = r7.url;	 Catch:{ Exception -> 0x0041 }
        r1.<init>(r4);	 Catch:{ Exception -> 0x0041 }
        r4 = r7.httpClient;	 Catch:{ Exception -> 0x0041 }
        r2 = r4.execute(r1);	 Catch:{ Exception -> 0x0041 }
        r4 = isHttpSuccessExecuted(r2);	 Catch:{ Exception -> 0x0041 }
        if (r4 == 0) goto L_0x0049;
    L_0x0035:
        r4 = r2.getEntity();	 Catch:{ Exception -> 0x0041 }
        r3 = org.apache.http.util.EntityUtils.toString(r4);	 Catch:{ Exception -> 0x0041 }
        r7.sendMessage(r3);	 Catch:{ Exception -> 0x0041 }
        goto L_0x000c;
    L_0x0041:
        r0 = move-exception;
        r4 = "fail";
        r7.sendMessage(r4);
        goto L_0x000c;
    L_0x0049:
        r4 = "fail";
        r7.sendMessage(r4);	 Catch:{ Exception -> 0x0041 }
        goto L_0x000c;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.hanyou.leyusdk" +
                ".HttpConnection.run():void");
    }

    private void sendMessage(String result) {
        Message message = Message.obtain(handler, 2, this.listener);
        Bundle data = new Bundle();
        data.putString("callbackkey", result);
        message.setData(data);
        handler.sendMessage(message);
    }

    public static DefaultHttpClient getHttpClient() {
        HttpParams httpParams = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(httpParams, 20000);
        HttpConnectionParams.setSoTimeout(httpParams, 20000);
        return new DefaultHttpClient(httpParams);
    }

    public static boolean isHttpSuccessExecuted(HttpResponse response) {
        int statusCode = response.getStatusLine().getStatusCode();
        System.out.println("statusCode:" + statusCode);
        return statusCode > 199 && statusCode < 400;
    }
}
