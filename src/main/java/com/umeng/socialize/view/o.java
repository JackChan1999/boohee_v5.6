package com.umeng.socialize.view;

import android.view.View;

/* compiled from: OauthDialog */
class o implements Runnable {
    final /* synthetic */ View a;
    final /* synthetic */ View b;
    final /* synthetic */ m    c;

    o(m mVar, View view, View view2) {
        this.c = mVar;
        this.a = view;
        this.b = view2;
    }

    public void run() {
        this.a.setVisibility(0);
        if (!(this.b.getVisibility() == 0 || this.c.d.n == null || this.c.d.n.size() <= 0)) {
            this.b.setVisibility(0);
        }
        this.c.requestLayout();
    }
}
