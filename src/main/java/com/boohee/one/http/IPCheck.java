package com.boohee.one.http;

import android.text.TextUtils;

import com.boohee.one.MyApplication;
import com.boohee.one.http.DnspodFree.OnIpGetListener;
import com.boohee.one.http.client.BaseJsonRequest;
import com.boohee.one.http.client.BooheeClient;
import com.boohee.utils.BlackTech;
import com.boohee.utils.Helper;
import com.boohee.utils.HttpUtils;

public class IPCheck {
    private static       String API_HOST       = "";
    private static final String FORMAT_IP_TEST = "http://%s/iptest.txt";
    private static final String TAG            = IPCheck.class.getSimpleName();
    private static       String ip             = BlackTech.getCurrentIp();

    public static synchronized void cacheApiIpAndCheck() {
        synchronized (IPCheck.class) {
            API_HOST = BooheeClient.getHost(BooheeClient.API);
            DnspodFree.getIpWithHost(API_HOST, new OnIpGetListener() {
                public void onIpGet() {
                    IPCheck.ipTest();
                }
            });
        }
    }

    public static void ipTest() {
        if (BlackTech.isIPConnectOpen()) {
            API_HOST = BooheeClient.getHost(BooheeClient.API);
            ip = TextUtils.isEmpty(ip) ? DnspodFree.getCachedIp(API_HOST) : ip;
            if (TextUtils.isEmpty(ip)) {
                BlackTech.setCanIPConnect(false);
                return;
            }
            String url = String.format(FORMAT_IP_TEST, new Object[]{ip});
            JsonCallback callback = new JsonCallback(MyApplication.getContext()) {
                public void ok(String response) {
                    Helper.showLog(IPCheck.TAG, response);
                    if ("ok".equalsIgnoreCase(response)) {
                        BlackTech.setCanIPConnect(true);
                        BlackTech.setCurrentIp(IPCheck.ip);
                    } else if ("no_host".equalsIgnoreCase(response)) {
                        BlackTech.setCanIPConnect(false);
                        BlackTech.setCurrentIp(DnspodFree.getNextCacheIp());
                    }
                }

                public void fail(String message) {
                    if (HttpUtils.isNetworkAvailable(MyApplication.getContext())) {
                        BlackTech.setCanIPConnect(false);
                        BlackTech.setCurrentIp(DnspodFree.getNextCacheIp());
                    }
                }
            };
            RequestManager.addRequest(new BaseJsonRequest(0, url, API_HOST, null, callback,
                    callback), MyApplication.getContext());
        }
    }
}
