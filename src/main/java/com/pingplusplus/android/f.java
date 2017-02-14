package com.pingplusplus.android;

import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.webkit.WebView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

class f extends Handler {
    WeakReference a;

    f(PaymentActivity paymentActivity) {
        this.a = new WeakReference(paymentActivity);
    }

    private int a(String str) {
        return Integer.parseInt(a(str.replace("{", "").replace("}", ""), "resultStatus=", ";memo"));
    }

    private String a(String str, String str2, String str3) {
        int indexOf = str.indexOf(str2) + str2.length();
        return str3 != null ? str.substring(indexOf, str.indexOf(str3)) : str.substring(indexOf);
    }

    public void handleMessage(Message message) {
        PaymentActivity paymentActivity = (PaymentActivity) this.a.get();
        switch (message.what) {
            case 1:
                String str = (String) message.obj;
                try {
                    int a = a(str);
                    if (a == 9000) {
                        paymentActivity.a("success");
                        return;
                    } else if (a == 6001) {
                        paymentActivity.a("cancel", "user_cancelled");
                        return;
                    } else {
                        Iterable arrayList = new ArrayList();
                        arrayList.add("resultStatus");
                        arrayList.add(String.valueOf(a));
                        paymentActivity.a("fail", "channel_returns_fail", TextUtils.join("=",
                                arrayList));
                        return;
                    }
                } catch (Exception e) {
                    paymentActivity.a("fail", "channel_returns_fail", str);
                    return;
                }
            case 2:
                paymentActivity.a("success");
                return;
            case 4:
                WebView webView = (WebView) message.obj;
                if (webView.getProgress() < 100) {
                    webView.stopLoading();
                    webView.destroy();
                    removeMessages(4);
                    if (paymentActivity.g != null && paymentActivity.g.isShowing()) {
                        paymentActivity.g.dismiss();
                    }
                    paymentActivity.a("fail", "network is error");
                    return;
                }
                return;
            default:
                paymentActivity.a("fail", "testmode_notify_failed");
                return;
        }
    }
}
