package com.zxinsight.analytics.domain.trackEvent;

public class ErrorEvent extends EventPojo {
    public String ts;

    protected void init() {
        this.a = "e";
    }
}
