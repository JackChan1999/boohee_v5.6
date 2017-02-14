package com.zxinsight;

import android.view.View;
import android.view.View.OnClickListener;

class m implements OnClickListener {
    final /* synthetic */ l a;

    m(l lVar) {
        this.a = lVar;
    }

    public void onClick(View view) {
        this.a.b.clickWithMLink(view.getContext(), this.a.a.getWindowKey(), this.a.a.getDt(),
                this.a.a.getLp());
    }
}
