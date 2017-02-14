package com.alipay.sdk.app;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;

final class f implements OnClickListener {
    final /* synthetic */ d a;

    f(d dVar) {
        this.a = dVar;
    }

    public final void onClick(DialogInterface dialogInterface, int i) {
        this.a.a.cancel();
        this.a.b.a.d = false;
        n.a = n.a();
        this.a.b.a.finish();
    }
}
