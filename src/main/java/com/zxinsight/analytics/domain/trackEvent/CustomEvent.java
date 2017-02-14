package com.zxinsight.analytics.domain.trackEvent;

import com.zxinsight.common.util.l;
import com.zxinsight.common.util.m;

import java.util.Map;

public class CustomEvent extends EventPojo {
    public String              p;
    public String              pp;
    public Map<String, String> ps;
    public String              t;

    protected void init() {
        this.a = "u";
        if (l.b(m.a().L())) {
            this.p = m.a().L();
        }
        if (l.b(m.a().G())) {
            this.pp = m.a().G();
        }
        this.t = m.a().F();
    }
}
