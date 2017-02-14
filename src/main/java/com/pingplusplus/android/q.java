package com.pingplusplus.android;

import android.content.Intent;

import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import org.json.JSONObject;

public class q implements IWXAPIEventHandler {
    private PaymentActivity a;
    private PaymentActivity b;
    private IWXAPI c = null;

    public q(PaymentActivity paymentActivity, String str) {
        this.a = paymentActivity;
        this.c = WXAPIFactory.createWXAPI(this.a, str);
    }

    public void a(Intent intent) {
        this.c.handleIntent(intent, this);
    }

    public void a(PaymentActivity paymentActivity) {
        if (!paymentActivity.equals(this.a)) {
            PingppLog.a("wxPayEnActivity not equals paymentActivity");
            this.b = paymentActivity;
        }
    }

    public void a(JSONObject jSONObject) {
        String string = jSONObject.getString("appId");
        this.c.registerApp(string);
        this.c.handleIntent(this.a.getIntent(), this);
        BaseReq payReq = new PayReq();
        payReq.appId = string;
        payReq.partnerId = jSONObject.getString("partnerId");
        payReq.prepayId = jSONObject.getString("prepayId");
        payReq.nonceStr = jSONObject.getString("nonceStr");
        if (jSONObject.get("timeStamp") instanceof String) {
            payReq.timeStamp = jSONObject.getString("timeStamp");
        } else {
            payReq.timeStamp = jSONObject.getInt("timeStamp") + "";
        }
        payReq.packageValue = jSONObject.getString("packageValue");
        payReq.sign = jSONObject.getString("sign");
        this.c.sendReq(payReq);
    }

    public boolean a() {
        return this.c.isWXAppInstalled();
    }

    public int b() {
        return this.c.getWXAppSupportAPI();
    }

    public void onReq(BaseReq baseReq) {
    }

    public void onResp(BaseResp baseResp) {
        PingppLog.a("onResp");
        if (baseResp.getType() == 5) {
            PingppLog.d("PaymentActivity wx result errCode : " + baseResp.errCode + " , errStr:"
                    + baseResp.errStr);
            this.a.a = 0;
            PingppLog.a("onResp wxPayStatus=" + this.a.a);
            PingppObject.getInstance().wxErrCode = baseResp.errCode;
            if (this.b != null) {
                this.b.finish();
                this.b = null;
                return;
            }
            this.a.a();
        }
    }
}
