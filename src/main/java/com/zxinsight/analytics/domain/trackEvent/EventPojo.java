package com.zxinsight.analytics.domain.trackEvent;

import com.zxinsight.MWConfiguration;
import com.zxinsight.common.util.DeviceInfoUtils;

public abstract class EventPojo {
    public String a;
    public String et;
    public String l;
    public String nw;
    public final String sn = "2";
    public String st;

    protected abstract void init();

    public EventPojo() {
        init();
        this.nw = DeviceInfoUtils.b(MWConfiguration.getContext());
    }

    public void save() {
        EventsProxy.create().addEvent(this);
    }
}
