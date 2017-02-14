package com.umeng.socialize.view;

import android.view.View;
import android.view.View.OnClickListener;

import com.umeng.socialize.bean.StatusCode;
import com.umeng.socialize.controller.impl.m;
import com.umeng.socialize.controller.listener.SocializeListeners.SnsPostListener;

/* compiled from: ShareActivity */
class ad implements OnClickListener {
    final /* synthetic */ ShareActivity a;

    ad(ShareActivity shareActivity) {
        this.a = shareActivity;
    }

    public void onClick(View view) {
        this.a.d.setClickable(false);
        if (this.a.v != null) {
            this.a.v.unregisterListener(m.d);
            this.a.E.fireAllListenersOnComplete(SnsPostListener.class, this.a.x, StatusCode
                    .ST_CODE_ERROR_CANCEL, this.a.q);
            this.a.v.getConfig().cleanListeners();
            this.a.a();
        }
    }
}
