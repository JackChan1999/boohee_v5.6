package com.zxinsight;

class l implements UpdateMarketingListener {
    final /* synthetic */ RenderParam     a;
    final /* synthetic */ MarketingHelper b;

    l(MarketingHelper marketingHelper, RenderParam renderParam) {
        this.b = marketingHelper;
        this.a = renderParam;
    }

    public void success() {
        this.a.getParent().setOnClickListener(new m(this));
        this.a.getListener().setTitle(this.b.getTitle(this.a.getWindowKey()));
        this.a.getListener().setDescription(this.b.getDescription(this.a.getWindowKey()));
        this.a.getListener().setImage(this.b.getImageURL(this.a.getWindowKey()));
        TrackAgent.currentEvent().onImpression(this.a.getWindowKey());
    }

    public void failed(String str) {
    }
}
