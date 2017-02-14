package com.zxinsight.analytics.domain.trackEvent;

public class SocialEvent extends EventPojo {
    public String ak;
    public String sa;
    public int sn = 2;
    public String ts;

    protected void init() {
        this.a = "sh";
    }
}
