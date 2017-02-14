package com.zxinsight;

import com.zxinsight.common.http.l;
import com.zxinsight.common.http.m;

class h implements m {
    final /* synthetic */ MWImageView a;

    h(MWImageView mWImageView) {
        this.a = mWImageView;
    }

    public void a(Exception exception) {
        if (this.a.getDrawable() != null) {
            this.a.setImageDrawable(this.a.getDrawable());
        } else if (this.a.getBackground() != null) {
            this.a.setImageDrawable(this.a.getBackground());
        }
    }

    public void a(l lVar, boolean z) {
        if (lVar.a() != null) {
            this.a.setImageBitmap(lVar.a());
            TrackAgent.currentEvent().onImpression(MWImageView.access$000(this.a));
        } else if (this.a.getDrawable() != null) {
            this.a.setImageDrawable(this.a.getDrawable());
        } else if (this.a.getBackground() != null) {
            this.a.setImageDrawable(this.a.getBackground());
        }
    }
}
