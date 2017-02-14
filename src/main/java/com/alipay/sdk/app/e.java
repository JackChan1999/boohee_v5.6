package com.alipay.sdk.app;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;

final class e implements OnClickListener {
    final /* synthetic */ d a;

    e(d dVar) {
        this.a = dVar;
    }

    public final void onClick(DialogInterface dialogInterface, int i) {
        this.a.b.a.d = true;
        this.a.a.proceed();
        dialogInterface.dismiss();
    }
}
