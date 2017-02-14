package com.meiqia.meiqiasdk.dialog;

import android.app.Activity;
import android.app.Dialog;

import com.meiqia.meiqiasdk.R;

public class MQLoadingDialog extends Dialog {
    public MQLoadingDialog(Activity activity) {
        super(activity, R.style.MQDialog);
        setContentView(R.layout.mq_dialog_loading);
    }
}
