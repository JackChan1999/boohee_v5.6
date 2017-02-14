package com.pingplusplus.android;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.alipay.sdk.sys.a;
import com.baidu.paysdk.api.BaiduPay;
import com.boohee.one.pay.PayService;
import com.unionpay.UPPayAssistEx;
import com.unionpay.uppay.PayActivity;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

public class PaymentActivity extends Activity {
    public static final  String   EXTRA_CHARGE = "com.pingplusplus.android.PaymentActivity.CHARGE";
    private static final String[] l            = new String[]{"alipay", PayService
            .CHANNEL_WECHAT, PayService.CHANNEL_UPACP, "upmp", "bfb", "yeepay_wap", "jdpay_wap"};
    public               int      a            = 0;
    WebViewClient b = new c(this);
    WebViewClient c = new d(this);
    WebViewClient d = new e(this);
    private String         e = null;
    private int            f = 0;
    private ProgressDialog g = null;
    private String         h = "cancel";
    private JSONObject i;
    private q       j = null;
    private boolean k = false;
    private Handler m = new f(this);

    private void a(int i, String str) {
        if (str == null) {
            a("cancel", "user_cancelled");
        } else if (i == -15) {
            a("fail", "network is error");
        } else {
            String substring = str.substring("statecode={".length() + str.indexOf("statecode="),
                    str.indexOf("};order_no="));
            if (substring.equals("0")) {
                a("success");
            } else if (substring.equals("1")) {
                a("in_process");
            } else if (substring.equals("2") || substring.equals("7")) {
                a("cancel", "user_cancelled");
            } else if (substring.equals("3")) {
                a("fail", "bfb_not_supported_method");
            } else if (substring.equals("4")) {
                a("fail", "bfb_token_expired");
            } else {
                a("fail", "unknown_error");
            }
        }
    }

    private void a(JSONObject jSONObject) {
        JSONObject jSONObject2 = jSONObject.getJSONObject("alipay");
        PingppLog.d("PaymentActivity start alipay credential : " + jSONObject);
        new a(this, jSONObject2.getString("orderInfo")).start();
    }

    private boolean a(JSONObject jSONObject, String str, String str2) {
        return (str2 == null || str2.equals(str)) && jSONObject.has(str) && !jSONObject.getString
                (str).equals("[]") && !jSONObject.getString(str).equals("{}");
    }

    @TargetApi(11)
    private void b(String str, String str2) {
        PingppLog.d("模拟支付页面: 支付渠道 " + str2);
        View webView = new WebView(this);
        webView.setLayoutParams(new LayoutParams(-1, -1));
        setContentView(webView);
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setUserAgentString(String.format("%s; %s/%s", new Object[]{settings
                .getUserAgentString(), "PingppAndroidSDK", "2.1.0"}));
        webView.addJavascriptInterface(new g(this, this), "PingppAndroidSDK");
        webView.removeJavascriptInterface("searchBoxJavaBridge_");
        webView.setWebViewClient(this.b);
        String format = String.format("http://sissi.pingxx.com/mock.php?ch_id=%s&channel=%s", new
                Object[]{str, str2});
        PingppLog.a(format);
        webView.loadUrl(format);
        this.g = ProgressDialog.show(this, "", "Loading", true);
    }

    private void b(JSONObject jSONObject) {
        JSONObject jSONObject2 = jSONObject.getJSONObject(PayService.CHANNEL_WECHAT);
        PingppLog.d("PaymentActivity start wx credential : " + jSONObject);
        String string = jSONObject2.getString("appId");
        PingppObject.getInstance().wxAppId = string;
        try {
            this.j = new q(this, string);
            PingppObject.getInstance().pingppWxHandler = this.j;
            if (this.j.a()) {
                if ((this.j.b() >= 570425345 ? 1 : 0) != 0) {
                    this.a = 1;
                    this.j.a(jSONObject2);
                    return;
                }
                a("fail", "wx_app_not_support");
                return;
            }
            a("invalid", "wx_app_not_installed");
        } catch (NoClassDefFoundError e) {
            e.printStackTrace();
            string = PingppObject.getInstance().currentChannel;
            a("fail", "channel_sdk_not_included:" + string, "不支持该渠道: " + string + "。缺少微信的 SDK。");
        }
    }

    private void c(JSONObject jSONObject) {
        JSONObject jSONObject2;
        if (jSONObject.has(PayService.CHANNEL_UPACP)) {
            jSONObject2 = jSONObject.getJSONObject(PayService.CHANNEL_UPACP);
            PingppLog.d("PaymentActivity start upacp credential : " + jSONObject);
        } else {
            jSONObject2 = jSONObject.getJSONObject("upmp");
            PingppLog.d("PaymentActivity start upmp credential : " + jSONObject);
        }
        try {
            UPPayAssistEx.startPayByJAR(this, PayActivity.class, null, null, jSONObject2
                    .getString("tn"), jSONObject2.getString("mode"));
        } catch (NoClassDefFoundError e) {
            e.printStackTrace();
            String str = PingppObject.getInstance().currentChannel;
            a("fail", "channel_sdk_not_included:" + str, "不支持该渠道: " + str + "。缺少银联的 SDK。");
        }
    }

    private void d(JSONObject jSONObject) {
        JSONObject jSONObject2 = jSONObject.getJSONObject("bfb");
        PingppLog.d("PaymentActivity start bfb credential : " + jSONObject);
        String str;
        try {
            BaiduPay.getInstance();
            Map hashMap = new HashMap();
            hashMap.put("cashier_type", "0");
            Iterable arrayList = new ArrayList();
            Iterator keys = jSONObject2.keys();
            while (keys.hasNext()) {
                str = (String) keys.next();
                Iterable arrayList2 = new ArrayList();
                arrayList2.add(str);
                arrayList2.add(jSONObject2.getString(str));
                arrayList.add(TextUtils.join("=", arrayList2));
            }
            if (arrayList.size() == 0) {
                a("fail", "invalid_credential");
                return;
            }
            str = TextUtils.join(a.b, arrayList);
            this.f = 1;
            BaiduPay.getInstance().doPay(this, str, new b(this), hashMap);
        } catch (NoClassDefFoundError e) {
            e.printStackTrace();
            str = PingppObject.getInstance().currentChannel;
            a("fail", "channel_sdk_not_included:" + str, "不支持该渠道: " + str + "。缺少百度钱包的 SDK。");
        }
    }

    @TargetApi(11)
    private void e(JSONObject jSONObject) {
        String encode;
        UnsupportedEncodingException e;
        String string;
        View webView;
        WebSettings settings;
        JSONObject jSONObject2 = jSONObject.getJSONObject("yeepay_wap");
        PingppLog.d("PaymentActivity start yeepay_wap credential : " + jSONObject);
        String string2 = jSONObject2.getString("merchantaccount");
        String str = null;
        try {
            encode = URLEncoder.encode(jSONObject2.getString("encryptkey"), "UTF-8");
            try {
                str = URLEncoder.encode(jSONObject2.getString("data"), "UTF-8");
            } catch (UnsupportedEncodingException e2) {
                e = e2;
                e.printStackTrace();
                string = jSONObject2.getString("mode");
                webView = new WebView(this);
                webView.setLayoutParams(new LayoutParams(-1, -1));
                setContentView(webView);
                settings = webView.getSettings();
                settings.setJavaScriptEnabled(true);
                settings.setUserAgentString(String.format("%s; %s/%s", new Object[]{settings
                        .getUserAgentString(), "PingppAndroidSDK", "2.1.0"}));
                webView.addJavascriptInterface(new g(this, this), "PingppAndroidSDK");
                webView.removeJavascriptInterface("searchBoxJavaBridge_");
                webView.setWebViewClient(this.c);
                webView.loadUrl(String.format("live".equals(string) ? "http://mobiletest.yeepay" +
                        ".com/paymobile/api/pay/request?merchantaccount=%s&encryptkey=%s&data=%s"
                        : "https://ok.yeepay" +
                        ".com/paymobile/api/pay/request?merchantaccount=%s&encryptkey=%s&data=%s" +
                        "", new Object[]{string2, encode, str}));
                this.g = ProgressDialog.show(this, "", "Loading", true);
            }
        } catch (UnsupportedEncodingException e3) {
            UnsupportedEncodingException unsupportedEncodingException = e3;
            encode = null;
            e = unsupportedEncodingException;
            e.printStackTrace();
            string = jSONObject2.getString("mode");
            webView = new WebView(this);
            webView.setLayoutParams(new LayoutParams(-1, -1));
            setContentView(webView);
            settings = webView.getSettings();
            settings.setJavaScriptEnabled(true);
            settings.setUserAgentString(String.format("%s; %s/%s", new Object[]{settings
                    .getUserAgentString(), "PingppAndroidSDK", "2.1.0"}));
            webView.addJavascriptInterface(new g(this, this), "PingppAndroidSDK");
            webView.removeJavascriptInterface("searchBoxJavaBridge_");
            webView.setWebViewClient(this.c);
            if ("live".equals(string)) {
            }
            webView.loadUrl(String.format("live".equals(string) ? "http://mobiletest.yeepay" +
                    ".com/paymobile/api/pay/request?merchantaccount=%s&encryptkey=%s&data=%s" :
                    "https://ok.yeepay" +
                            ".com/paymobile/api/pay/request?merchantaccount=%s&encryptkey=%s&data" +
                            "=%s", new Object[]{string2, encode, str}));
            this.g = ProgressDialog.show(this, "", "Loading", true);
        }
        string = jSONObject2.getString("mode");
        webView = new WebView(this);
        webView.setLayoutParams(new LayoutParams(-1, -1));
        setContentView(webView);
        settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setUserAgentString(String.format("%s; %s/%s", new Object[]{settings
                .getUserAgentString(), "PingppAndroidSDK", "2.1.0"}));
        webView.addJavascriptInterface(new g(this, this), "PingppAndroidSDK");
        webView.removeJavascriptInterface("searchBoxJavaBridge_");
        webView.setWebViewClient(this.c);
        if ("live".equals(string)) {
        }
        webView.loadUrl(String.format("live".equals(string) ? "http://mobiletest.yeepay" +
                ".com/paymobile/api/pay/request?merchantaccount=%s&encryptkey=%s&data=%s" :
                "https://ok.yeepay.com/paymobile/api/pay/request?merchantaccount=%s&encryptkey=%s" +
                        "&data=%s", new Object[]{string2, encode, str}));
        this.g = ProgressDialog.show(this, "", "Loading", true);
    }

    @TargetApi(11)
    private void f(JSONObject jSONObject) {
        JSONObject jSONObject2 = jSONObject.getJSONObject("jdpay_wap");
        PingppLog.d("PaymentActivity start jdpay_wap credential : " + jSONObject);
        View webView = new WebView(this);
        webView.setLayoutParams(new LayoutParams(-1, -1));
        setContentView(webView);
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setUserAgentString(String.format("%s; %s/%s", new Object[]{settings
                .getUserAgentString(), "PingppAndroidSDK", "2.1.0"}));
        webView.addJavascriptInterface(new g(this, this), "PingppAndroidSDK");
        webView.removeJavascriptInterface("searchBoxJavaBridge_");
        webView.setWebViewClient(this.d);
        Iterable arrayList = new ArrayList();
        Iterator keys = jSONObject2.keys();
        while (keys.hasNext()) {
            String str = (String) keys.next();
            Iterable arrayList2 = new ArrayList();
            arrayList2.add(str);
            try {
                arrayList2.add(URLEncoder.encode(jSONObject2.getString(str), "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            arrayList.add(TextUtils.join("=", arrayList2));
        }
        if (arrayList.size() == 0) {
            a("fail", "invalid_credential");
            return;
        }
        str = TextUtils.join(a.b, arrayList);
        PingppLog.d("jdPay orderInfo: " + str);
        webView.postUrl("https://m.jdpay.com/wepay/web/pay", str.getBytes());
        this.g = ProgressDialog.show(this, "", "Loading", true);
    }

    public static String getVersion() {
        return "2.1.0";
    }

    public void a() {
        int i = PingppObject.getInstance().wxErrCode;
        if (i == 0) {
            a("success");
        } else if (i == -2) {
            a("cancel", "user_cancelled");
        } else {
            a("fail", "channel_returns_fail", "wx_err_code:" + i);
        }
        PingppObject.getInstance().wxErrCode = -10;
    }

    public void a(String str) {
        a(str, "");
    }

    public void a(String str, String str2) {
        a(str, str2, "");
    }

    public void a(String str, String str2, String str3) {
        PingppLog.a("setResultAndFinish result=" + str + " isWXPayEntryActivity=" + this.k);
        PingppObject.getInstance().currentChannel = null;
        PingppObject.getInstance().pingppWxHandler = null;
        Intent intent = new Intent();
        intent.putExtra("pay_result", str);
        intent.putExtra("error_msg", str2);
        intent.putExtra("extra_msg", str3);
        setResult(-1, intent);
        finish();
    }

    protected void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (PingppObject.getInstance().currentChannel != null && !PingppObject.getInstance()
                .currentChannel.equalsIgnoreCase("upmp") && !PingppObject.getInstance()
                .currentChannel.equalsIgnoreCase(PayService.CHANNEL_UPACP)) {
            return;
        }
        if (intent == null) {
            a("fail", "");
            return;
        }
        String string = intent.getExtras().getString("pay_result");
        if (string == null) {
            a("fail");
        } else if (string.equalsIgnoreCase("success")) {
            a("success");
        } else if (string.equalsIgnoreCase("fail")) {
            a("fail", "channel_returns_fail");
        } else if (string.equalsIgnoreCase("cancel")) {
            a("cancel", "user_cancelled");
        } else {
            a("fail", "unknown_error");
        }
    }

    public void onBackPressed() {
        PingppObject.getInstance().currentChannel = null;
        Intent intent = new Intent();
        intent.putExtra("pay_result", this.h);
        setResult(-1, intent);
        super.onBackPressed();
    }

    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        PingppLog.a("onCreate");
        if (bundle == null) {
            JSONObject jSONObject;
            this.i = null;
            String stringExtra = getIntent().getStringExtra(EXTRA_CHARGE);
            if (stringExtra != null) {
                PingppLog.d("PaymentActivity received charge: " + stringExtra);
                try {
                    this.i = new JSONObject(stringExtra);
                    if (PingppObject.getInstance().isOne) {
                        PingppObject.getInstance().dataCollection.a(h.ONE);
                        PingppObject.getInstance().dataCollection.a(this.i);
                    } else {
                        PingppDataCollection pingppDataCollection = new PingppDataCollection(this);
                        pingppDataCollection.a(this.i);
                        pingppDataCollection.a(h.SDK);
                        pingppDataCollection.sendToServer();
                    }
                    if (this.i.has("credential")) {
                        this.e = this.i.getString("channel");
                        if (!this.i.has("livemode") || this.i.getBoolean("livemode")) {
                            jSONObject = this.i.getJSONObject("credential");
                            if (jSONObject != null) {
                                PingppObject.getInstance().currentChannel = this.e;
                                PingppLog.d("PaymentActivity received channel: " + this.e);
                                if (Arrays.asList(l).contains(this.e)) {
                                    PingppObject.getInstance().currentChannel = null;
                                    a("fail", "invalid_charge_no_such_channel");
                                }
                                try {
                                    if (!a(jSONObject, "upmp", this.e) || a(jSONObject,
                                            PayService.CHANNEL_UPACP, this.e)) {
                                        c(jSONObject);
                                        return;
                                    } else if (a(jSONObject, PayService.CHANNEL_WECHAT, this.e)) {
                                        b(jSONObject);
                                        return;
                                    } else if (a(jSONObject, "alipay", this.e)) {
                                        a(jSONObject);
                                        return;
                                    } else if (a(jSONObject, "bfb", this.e)) {
                                        d(jSONObject);
                                        return;
                                    } else if (a(jSONObject, "yeepay_wap", this.e)) {
                                        e(jSONObject);
                                        return;
                                    } else if (a(jSONObject, "jdpay_wap", this.e)) {
                                        f(jSONObject);
                                        return;
                                    } else {
                                        PingppObject.getInstance().currentChannel = null;
                                        a("fail", "invalid_credential");
                                        return;
                                    }
                                } catch (JSONException e) {
                                    PingppObject.getInstance().currentChannel = null;
                                    a("fail", "invalid_credential");
                                    return;
                                }
                            } else if (PingppObject.getInstance().wxAppId != null) {
                                PingppLog.a("isWXPayEntryActivity");
                                this.k = true;
                                this.j = PingppObject.getInstance().pingppWxHandler;
                                if (this.j != null) {
                                    this.j.a(this);
                                    this.j.a(getIntent());
                                }
                            }
                        }
                        b(this.i.getString("id"), this.e);
                        return;
                    }
                    a("fail", "invalid_charge_no_credential");
                } catch (JSONException e2) {
                    a("fail", "invalid_charge_json_decode_fail");
                }
            }
            jSONObject = null;
            if (jSONObject != null) {
                PingppObject.getInstance().currentChannel = this.e;
                PingppLog.d("PaymentActivity received channel: " + this.e);
                if (Arrays.asList(l).contains(this.e)) {
                    if (a(jSONObject, "upmp", this.e)) {
                    }
                    c(jSONObject);
                    return;
                }
                PingppObject.getInstance().currentChannel = null;
                a("fail", "invalid_charge_no_such_channel");
            } else if (PingppObject.getInstance().wxAppId != null) {
                PingppLog.a("isWXPayEntryActivity");
                this.k = true;
                this.j = PingppObject.getInstance().pingppWxHandler;
                if (this.j != null) {
                    this.j.a(this);
                    this.j.a(getIntent());
                }
            }
        }
    }

    public void onDestroy() {
        super.onDestroy();
        PingppLog.a("onDestroy isWXPayEntryActivity=" + this.k);
    }

    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        PingppLog.a("onNewIntent isWXPayEntryActivity=" + this.k);
        if (this.j != null) {
            setIntent(intent);
            this.j.a(getIntent());
        }
    }

    protected void onPause() {
        super.onPause();
        if (this.a == 1) {
            this.a = 2;
        } else if (this.f == 1 && PingppObject.getInstance().currentChannel != null &&
                PingppObject.getInstance().currentChannel.equalsIgnoreCase("bfb")) {
            this.f = 2;
        }
    }

    protected void onResume() {
        super.onResume();
        PingppLog.a("onResume wxPayStatus=" + this.a + " isWXPayEntryActivity=" + this.k);
        if (this.a == 2 || this.f == 2) {
            a("cancel", "user_cancelled");
        } else if (this.a == 0 && PingppObject.getInstance().wxErrCode != -10 && PingppObject
                .getInstance().currentChannel.equals(PayService.CHANNEL_WECHAT)) {
            a();
        }
    }
}
