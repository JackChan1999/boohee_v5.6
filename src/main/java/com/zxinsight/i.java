package com.zxinsight;

import com.zxinsight.analytics.domain.trackEvent.AppLaunchEvent;
import com.zxinsight.common.util.DeviceInfoUtils;
import com.zxinsight.common.util.m;
import com.zxinsight.common.util.o;

final class i implements Runnable {
    i() {
    }

    public void run() {
        DeviceInfoUtils.a(MWConfiguration.getContext());
        u.a().c();
        a.a().c();
        if (m.a().b()) {
            AppLaunchEvent appLaunchEvent = new AppLaunchEvent();
            appLaunchEvent.st = o.b();
            appLaunchEvent.et = o.b();
            appLaunchEvent.save();
        }
        a.a().b();
    }
}
