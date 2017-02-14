package com.umeng.socialize.view;

import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;

import com.umeng.socialize.utils.SocializeUtils;

/* compiled from: ShareActivity */
class ac implements OnClickListener {
    final /* synthetic */ ShareActivity a;

    ac(ShareActivity shareActivity) {
        this.a = shareActivity;
    }

    public void onClick(View view) {
        if (this.a.B && this.a.k != null) {
            if (!this.a.t()) {
                this.a.a(this.a.k, "init");
            }
            this.a.a(this.a.k, "onShow");
            this.a.k.setVisibility(0);
        } else if (this.a.C != null && !this.a.C.isShowing()) {
            ((InputMethodManager) this.a.getSystemService("input_method"))
                    .hideSoftInputFromWindow(this.a.f.getWindowToken(), 0);
            SocializeUtils.safeShowDialog(this.a.C);
        }
    }
}
