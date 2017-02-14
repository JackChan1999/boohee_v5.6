package com.zxinsight.analytics.domain.trackEvent;

import java.util.Map;

public class MLinkEvent extends EventPojo {
    public String ak;
    public String p;
    public String pp;
    public Map    ps;
    public String st;
    public String ts;

    protected void init() {
        this.a = "mc";
    }
}
