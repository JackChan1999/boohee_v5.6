package com.pingplusplus.android;

import com.baidu.android.pay.PayCallBack;

class b implements PayCallBack {
    final /* synthetic */ PaymentActivity a;

    b(PaymentActivity paymentActivity) {
        this.a = paymentActivity;
    }

    public boolean isHideLoadingDialog() {
        return false;
    }

    public void onPayResult(int i, String str) {
        this.a.f = 0;
        this.a.a(i, str);
    }
}
