package com.umeng.socialize.view;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;

/* compiled from: ShareActivity */
class v implements OnClickListener {
    final /* synthetic */ ShareActivity a;

    v(ShareActivity shareActivity) {
        this.a = shareActivity;
    }

    public void onClick(DialogInterface dialogInterface, int i) {
        this.a.z = null;
        this.a.a(false);
        dialogInterface.cancel();
    }
}
