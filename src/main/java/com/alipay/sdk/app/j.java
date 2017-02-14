package com.alipay.sdk.app;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;

final class j implements OnClickListener {
    final /* synthetic */ i a;

    j(i iVar) {
        this.a = iVar;
    }

    public final void onClick(DialogInterface dialogInterface, int i) {
        this.a.b.a.e = true;
        this.a.a.proceed();
        dialogInterface.dismiss();
    }
}
