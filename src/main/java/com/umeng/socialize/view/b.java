package com.umeng.socialize.view;

import android.view.View;
import android.view.View.OnClickListener;

import com.umeng.socialize.utils.SocializeUtils;

/* compiled from: LoginAgent */
class b implements OnClickListener {
    final /* synthetic */ a a;

    b(a aVar) {
        this.a = aVar;
    }

    public void onClick(View view) {
        SocializeUtils.safeCloseDialog(this.a.b.a);
    }
}
