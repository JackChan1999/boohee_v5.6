package com.umeng.socialize.view;

import android.view.View;

/* compiled from: OauthDialog */
class n implements Runnable {
    final /* synthetic */ View a;
    final /* synthetic */ View b;
    final /* synthetic */ m    c;

    n(m mVar, View view, View view2) {
        this.c = mVar;
        this.a = view;
        this.b = view2;
    }

    public void run() {
        this.a.setVisibility(8);
        if (this.b.getVisibility() == 0) {
            this.b.setVisibility(8);
        }
        this.c.requestLayout();
    }
}
