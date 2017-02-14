package com.umeng.socialize.view;

import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;

import com.umeng.socialize.utils.SocializeUtils;

/* compiled from: OauthDialog */
class m extends FrameLayout {
    final /* synthetic */ View a;
    final /* synthetic */ View b;
    final /* synthetic */ int  c;
    final /* synthetic */ j    d;

    m(j jVar, Context context, View view, View view2, int i) {
        this.d = jVar;
        this.a = view;
        this.b = view2;
        this.c = i;
        super(context);
    }

    protected void onSizeChanged(int i, int i2, int i3, int i4) {
        super.onSizeChanged(i, i2, i3, i4);
        if (!SocializeUtils.isFloatWindowStyle(this.d.k)) {
            a(this.a, this.b, this.c, i2);
        }
    }

    private void a(View view, View view2, int i, int i2) {
        if (view2.getVisibility() == 0 && i2 < i) {
            this.d.t.post(new n(this, view2, view));
        } else if (view2.getVisibility() != 0 && i2 >= i) {
            this.d.t.post(new o(this, view2, view));
        }
    }
}
