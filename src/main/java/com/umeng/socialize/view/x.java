package com.umeng.socialize.view;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;

import com.umeng.socialize.bean.SHARE_MEDIA;

/* compiled from: ShareActivity */
class x implements OnClickListener {
    final /* synthetic */ ShareActivity a;

    x(ShareActivity shareActivity) {
        this.a = shareActivity;
    }

    public void onClick(DialogInterface dialogInterface, int i) {
        this.a.a.setImageDrawable(null);
        this.a.a.setVisibility(8);
        if (this.a.x == SHARE_MEDIA.QQ) {
            this.a.k();
        }
        this.a.j.setVisibility(8);
        this.a.D = null;
    }
}
