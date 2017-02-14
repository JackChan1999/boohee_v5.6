package com.zxinsight.analytics.domain.trackEvent;

import com.umeng.socialize.net.utils.SocializeProtocolConstants;

public class PageTrackingEvent extends EventPojo {
    public String p;
    public String pp;

    protected void init() {
        this.a = SocializeProtocolConstants.PROTOCOL_KEY_PV;
    }
}
