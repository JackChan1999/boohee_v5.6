package com.alipay.sdk.app;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;

final class k implements OnClickListener {
    final /* synthetic */ i a;

    k(i iVar) {
        this.a = iVar;
    }

    public final void onClick(DialogInterface dialogInterface, int i) {
        this.a.a.cancel();
        this.a.b.a.e = false;
        n.a = n.a();
        this.a.b.a.finish();
    }
}
