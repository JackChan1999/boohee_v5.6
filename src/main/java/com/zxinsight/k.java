package com.zxinsight;

import android.view.View;
import android.view.View.OnClickListener;

class k implements OnClickListener {
    final /* synthetic */ RenderParam     a;
    final /* synthetic */ MarketingHelper b;

    k(MarketingHelper marketingHelper, RenderParam renderParam) {
        this.b = marketingHelper;
        this.a = renderParam;
    }

    public void onClick(View view) {
        this.b.clickWithMLink(view.getContext(), this.a.getWindowKey(), this.a.getDt(), this.a
                .getLp());
    }
}
