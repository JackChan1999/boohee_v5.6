package com.xiaomi.push.service;

import com.alipay.security.mobile.module.deviceinfo.constant.DeviceInfoConstant;
import com.baidu.location.a0;
import com.xiaomi.channel.commonutils.logger.b;
import com.xiaomi.push.service.XMPushService.c;

class ad {
    private static int e = 300;
    private XMPushService a;
    private int           b;
    private long          c;
    private int d = 0;

    public ad(XMPushService xMPushService) {
        this.a = xMPushService;
        this.b = 10;
        this.c = 0;
    }

    private int b() {
        int i = 40;
        if (this.d > 8) {
            return 300;
        }
        if (this.d > 4) {
            return 60;
        }
        if (this.d >= 1) {
            return 10;
        }
        if (this.c == 0) {
            return 0;
        }
        long currentTimeMillis = System.currentTimeMillis() - this.c;
        if (currentTimeMillis < DeviceInfoConstant.REQUEST_LOCATE_INTERVAL) {
            if (this.b >= e) {
                return this.b;
            }
            i = this.b;
            this.b = (int) (((double) this.b) * 1.5d);
            return i;
        } else if (currentTimeMillis < 900000) {
            if (this.b < 40) {
                i = this.b;
            }
            this.b = i;
            return this.b;
        } else if (currentTimeMillis < a0.i2) {
            this.b = this.b < 20 ? this.b : 20;
            return this.b;
        } else {
            this.b = 10;
            return this.b;
        }
    }

    public void a() {
        this.c = System.currentTimeMillis();
        this.a.a(1);
        this.d = 0;
    }

    public void a(boolean z) {
        if (!this.a.a()) {
            b.c("should not reconnect as no client or network.");
        } else if (z) {
            this.a.a(1);
            XMPushService xMPushService = this.a;
            XMPushService xMPushService2 = this.a;
            xMPushService2.getClass();
            xMPushService.a(new c(xMPushService2));
            this.d++;
        } else if (!this.a.b(1)) {
            int b = b();
            b.a("schedule reconnect in " + b + "s");
            XMPushService xMPushService3 = this.a;
            XMPushService xMPushService4 = this.a;
            xMPushService4.getClass();
            xMPushService3.a(new c(xMPushService4), (long) (b * 1000));
            this.d++;
            if (this.d == 3) {
                u.a();
            }
        }
    }
}
