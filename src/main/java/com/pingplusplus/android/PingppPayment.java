package com.pingplusplus.android;

import android.app.Activity;
import android.content.Intent;

public class PingppPayment {
    private static final int REQUEST_CODE_PAYMENT = 1;

    public static void createPayment(Activity activity, String str) {
        Intent intent = new Intent(activity, PaymentActivity.class);
        intent.putExtra(PaymentActivity.EXTRA_CHARGE, str);
        activity.startActivityForResult(intent, 1);
    }
}
