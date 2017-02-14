package com.zxinsight.analytics.domain.trackEvent;

import com.baidu.location.a.a;
import com.zxinsight.MWConfiguration;
import com.zxinsight.analytics.domain.Device;
import com.zxinsight.common.util.DeviceInfoUtils;
import com.zxinsight.common.util.l;
import com.zxinsight.common.util.m;
import com.zxinsight.common.util.o;
import com.zxinsight.u;

import java.util.ArrayList;
import java.util.List;

public class CompositeEvent {
    public String a;
    public String ak = o.c();
    public String av = DeviceInfoUtils.f(MWConfiguration.getContext());
    public String          ck;
    public Device          d;
    public List<EventPojo> es;
    public String          o;
    public String          sid;
    public String sv = "3.9.160727";
    public String uid;

    public CompositeEvent() {
        Device device = new Device();
        this.d = device;
        this.sid = m.a().f();
        this.es = new ArrayList();
        this.ck = m.a().s();
        if (l.b(m.a().d())) {
            this.uid = m.a().d();
        }
        if (u.a().b()) {
            this.a = m.a().c(a.int);
            this.o = m.a().c(a.char);
        }
    }

    public void addEvent(EventPojo eventPojo) {
        if (l.a(this.ak)) {
            this.ak = o.c();
        }
        this.es.add(eventPojo);
    }

    public void clearEvent() {
        this.es.clear();
    }
}
