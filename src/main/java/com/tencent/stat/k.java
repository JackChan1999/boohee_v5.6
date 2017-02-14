package com.tencent.stat;

import android.content.Context;

import com.tencent.stat.a.e;
import com.tencent.stat.a.f;
import com.tencent.stat.common.p;

class k implements Runnable {
    private e a;
    private StatReportStrategy b = null;
    private c                  c = new l(this);

    public k(e eVar) {
        this.a = eVar;
        this.b = StatConfig.getStatSendStrategy();
    }

    private void a() {
        if (n.b().a() > 0) {
            n.b().a(this.a, null);
            n.b().a(-1);
            return;
        }
        a(true);
    }

    private void a(boolean z) {
        d.b().a(this.a, this.c);
    }

    public void run() {
        try {
            if (!StatConfig.isEnableStatService()) {
                return;
            }
            if (this.a.a() == f.ERROR || this.a.d().length() <= StatConfig
                    .getMaxReportEventLength()) {
                if (StatConfig.getMaxSessionStatReportCount() > 0) {
                    if (StatConfig.getCurSessionStatReportCount() >= StatConfig
                            .getMaxSessionStatReportCount()) {
                        StatService.i.e((Object) "Times for reporting events has reached the " +
                                "limit of StatConfig.getMaxSessionStatReportCount() in current " +
                                "session.");
                        return;
                    }
                    StatConfig.c();
                }
                StatService.i.i("Lauch stat task in thread:" + Thread.currentThread().getName());
                Context c = this.a.c();
                if (com.tencent.stat.common.k.h(c)) {
                    if (StatConfig.isEnableSmartReporting() && this.b != StatReportStrategy
                            .ONLY_WIFI_NO_CACHE && com.tencent.stat.common.k.g(c)) {
                        this.b = StatReportStrategy.INSTANT;
                    }
                    switch (h.a[this.b.ordinal()]) {
                        case 1:
                            a();
                            return;
                        case 2:
                            if (com.tencent.stat.common.k.e(c)) {
                                a();
                                return;
                            } else {
                                n.a(c).a(this.a, null);
                                return;
                            }
                        case 3:
                        case 4:
                            n.a(c).a(this.a, null);
                            return;
                        case 5:
                            if (n.a(this.a.c()) != null) {
                                n.a(c).a(this.a, new m(this));
                                return;
                            }
                            return;
                        case 6:
                            n.a(c).a(this.a, null);
                            String str = "last_period_ts";
                            Long valueOf = Long.valueOf(p.a(c, str, 0));
                            Long valueOf2 = Long.valueOf(System.currentTimeMillis());
                            if (Long.valueOf(Long.valueOf(valueOf2.longValue() - valueOf
                                    .longValue()).longValue() / 60000).longValue() > ((long)
                                    StatConfig.getSendPeriodMinutes())) {
                                n.a(c).a(-1);
                                p.b(c, str, valueOf2.longValue());
                                return;
                            }
                            return;
                        case 7:
                            if (com.tencent.stat.common.k.e(c)) {
                                a(false);
                                return;
                            }
                            return;
                        default:
                            StatService.i.error("Invalid stat strategy:" + StatConfig
                                    .getStatSendStrategy());
                            return;
                    }
                    StatService.i.e(th);
                }
                n.a(c).a(this.a, null);
                return;
            }
            StatService.i.e("Event length exceed StatConfig.getMaxReportEventLength(): " +
                    StatConfig.getMaxReportEventLength());
        } catch (Exception e) {
            StatService.i.e(e);
        } catch (Object th) {
            StatService.i.e(th);
        }
    }
}
