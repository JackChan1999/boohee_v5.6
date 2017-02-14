package com.zxinsight.analytics.domain.trackEvent;

import com.umeng.socialize.net.utils.SocializeProtocolConstants;

public class AppLaunchEvent extends EventPojo {
    protected void init() {
        this.a = SocializeProtocolConstants.PROTOCOL_KEY_ST;
    }
}
