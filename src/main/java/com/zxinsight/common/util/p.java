package com.zxinsight.common.util;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;

final class p implements OnCancelListener {
    final /* synthetic */ boolean  a;
    final /* synthetic */ Activity b;

    p(boolean z, Activity activity) {
        this.a = z;
        this.b = activity;
    }

    public void onCancel(DialogInterface dialogInterface) {
        if (this.a) {
            this.b.finish();
        }
    }
}
