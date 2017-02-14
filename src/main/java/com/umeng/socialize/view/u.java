package com.umeng.socialize.view;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;

/* compiled from: ShareActivity */
class u implements OnClickListener {
    final /* synthetic */ ShareActivity a;

    u(ShareActivity shareActivity) {
        this.a = shareActivity;
    }

    public void onClick(DialogInterface dialogInterface, int i) {
        dialogInterface.cancel();
    }
}
