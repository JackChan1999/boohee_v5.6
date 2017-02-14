package com.umeng.socialize.view;

import android.text.Editable;
import android.text.TextWatcher;

/* compiled from: ShareActivity */
class af implements TextWatcher {
    final /* synthetic */ ShareActivity a;

    af(ShareActivity shareActivity) {
        this.a = shareActivity;
    }

    public void afterTextChanged(Editable editable) {
    }

    public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
    }

    public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        this.a.u = this.a.h();
    }
}
