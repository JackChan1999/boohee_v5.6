package com.zxinsight;

import android.view.View;
import android.view.View.OnClickListener;

import org.json.JSONObject;

class g implements OnClickListener {
    final /* synthetic */ String      a;
    final /* synthetic */ JSONObject  b;
    final /* synthetic */ MWImageView c;

    g(MWImageView mWImageView, String str, JSONObject jSONObject) {
        this.c = mWImageView;
        this.a = str;
        this.b = jSONObject;
    }

    public void onClick(View view) {
        MarketingHelper.currentMarketing(view.getContext()).clickWithMLinkCallbackUri(view
                .getContext(), MWImageView.access$000(this.c), this.a, this.b, MWImageView
                .access$100(this.c), MWImageView.access$200(this.c));
    }
}
