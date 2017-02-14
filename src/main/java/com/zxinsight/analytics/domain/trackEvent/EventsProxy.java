package com.zxinsight.analytics.domain.trackEvent;

import com.zxinsight.a;
import com.zxinsight.common.util.h;
import com.zxinsight.common.util.l;
import com.zxinsight.common.util.m;
import com.zxinsight.common.util.o;

public class EventsProxy {
    private static volatile EventsProxy defaultInstance;
    private CompositeEvent compositeEvent = new CompositeEvent();

    private EventsProxy() {
    }

    public static EventsProxy create() {
        if (defaultInstance == null) {
            synchronized (EventsProxy.class) {
                if (defaultInstance == null) {
                    defaultInstance = new EventsProxy();
                }
            }
        }
        return defaultInstance;
    }

    void addEvent(EventPojo eventPojo) {
        this.compositeEvent = getEvents();
        this.compositeEvent.addEvent(eventPojo);
        if (l.a(this.compositeEvent.ak)) {
            this.compositeEvent.ak = o.c();
        }
        if (this.compositeEvent.es.size() >= m.a().k()) {
            a.a().b();
        }
    }

    public void addSession(String str) {
        this.compositeEvent = getEvents();
        this.compositeEvent.sid = str;
    }

    public void addUserMd5(String str) {
        this.compositeEvent = getEvents();
        this.compositeEvent.uid = str;
    }

    public void clearEvents() {
        if (this.compositeEvent != null) {
            this.compositeEvent.clearEvent();
        }
    }

    private CompositeEvent getEvents() {
        return this.compositeEvent != null ? this.compositeEvent : new CompositeEvent();
    }

    public String getJsonString() {
        return getEvents().es.size() > 0 ? h.a(getEvents()) : "";
    }
}
