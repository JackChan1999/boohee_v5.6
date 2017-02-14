package com.pingplusplus.android;

import android.os.Message;

import com.alipay.sdk.app.PayTask;

class a extends Thread {
    final /* synthetic */ String          a;
    final /* synthetic */ PaymentActivity b;

    a(PaymentActivity paymentActivity, String str) {
        this.b = paymentActivity;
        this.a = str;
    }

    public void run() {
        String pay;
        try {
            PayTask payTask = new PayTask(this.b);
            PingppLog.d("alipaysdk version: " + payTask.getVersion());
            pay = payTask.pay(this.a, true);
            PingppLog.d("PaymentActivity alipay result: " + pay);
            Message message = new Message();
            message.what = 1;
            message.obj = pay;
            this.b.m.sendMessage(message);
        } catch (NoClassDefFoundError e) {
            e.printStackTrace();
            pay = PingppObject.getInstance().currentChannel;
            this.b.a("fail", "channel_sdk_not_included:" + pay, "不支持该渠道: " + pay + "。缺少支付宝的 SDK。");
        }
    }
}
