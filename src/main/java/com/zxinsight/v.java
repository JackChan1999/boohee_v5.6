package com.zxinsight;

import com.zxinsight.analytics.domain.response.ServiceConfigResponse;
import com.zxinsight.common.http.u;
import com.zxinsight.common.util.c;
import com.zxinsight.common.util.f;
import com.zxinsight.common.util.h;
import com.zxinsight.common.util.l;

import org.json.JSONObject;

class v implements u<String> {
    final /* synthetic */ u a;

    v(u uVar) {
        this.a = uVar;
    }

    public void a(String str) {
        if (l.b(str)) {
            try {
                ServiceConfigResponse serviceConfigResponse = (ServiceConfigResponse) h.a(new
                        JSONObject(str), ServiceConfigResponse.class);
                if (f.a(serviceConfigResponse)) {
                    c.b(String.valueOf(serviceConfigResponse.getData().ss));
                    this.a.a(serviceConfigResponse);
                    c.e("get MW ServiceConfig ok! ON/OFF = " + serviceConfigResponse.getData().e);
                    c.f("get MW ServiceConfig ok! ON/OFF = " + serviceConfigResponse.getData().e);
                    return;
                }
                c.a("get Service Config error! ");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void a(Exception exception) {
    }
}
