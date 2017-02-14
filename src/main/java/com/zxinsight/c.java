package com.zxinsight;

import com.zxinsight.common.http.u;
import com.zxinsight.common.util.DeviceInfoUtils;

class c implements u<String> {
    final /* synthetic */ String a;
    final /* synthetic */ a      b;

    c(a aVar, String str) {
        this.b = aVar;
        this.a = str;
    }

    public void a(String str) {
        com.zxinsight.common.a.c.b(MWConfiguration.getContext(), this.a);
        DeviceInfoUtils.f();
    }

    public void a(Exception exception) {
    }
}
