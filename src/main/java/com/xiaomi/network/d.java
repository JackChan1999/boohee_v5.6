package com.xiaomi.network;

import com.xiaomi.channel.commonutils.logger.b;
import com.xiaomi.network.UploadHostStatHelper.HttpRecordCallback;

import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;

import org.apache.thrift.f;

class d extends TimerTask {
    final /* synthetic */ UploadHostStatHelper a;

    d(UploadHostStatHelper uploadHostStatHelper) {
        this.a = uploadHostStatHelper;
    }

    public void run() {
        List<HttpRecordCallback> arrayList = new ArrayList();
        synchronized (this.a) {
            arrayList.addAll(this.a.a);
        }
        for (HttpRecordCallback httpRecordCallback : arrayList) {
            List a = httpRecordCallback.a();
            double b = httpRecordCallback.b();
            if (a != null) {
                try {
                    if (a.size() > 0) {
                        this.a.a(a, b);
                    }
                } catch (f e) {
                    b.a("uploadHostStat exception" + e.toString());
                }
            }
        }
        this.a.d = false;
    }
}
