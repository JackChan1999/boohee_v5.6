package com.zxinsight;

import com.alipay.sdk.sys.a;
import com.umeng.socialize.net.utils.SocializeProtocolConstants;
import com.zxinsight.analytics.domain.response.ServiceConfig;
import com.zxinsight.analytics.domain.response.ServiceConfigResponse;
import com.zxinsight.common.http.Request;
import com.zxinsight.common.http.Request.HttpMethod;
import com.zxinsight.common.http.ae;
import com.zxinsight.common.http.d;
import com.zxinsight.common.util.DeviceInfoUtils;
import com.zxinsight.common.util.c;
import com.zxinsight.common.util.m;
import com.zxinsight.common.util.o;

import org.json.JSONException;
import org.json.JSONObject;

public class u {
    static         String a = "config_SDK_is_enable";
    static         String b = "config_update_time";
    static         String c = "config_LBS_is_enable";
    private static u      d = null;

    private u() {
    }

    public static u a() {
        if (d == null) {
            d = new u();
        }
        return d;
    }

    public boolean b() {
        return (o.a(MWConfiguration.getContext(), "android.permission.ACCESS_FINE_LOCATION") || o
                .a(MWConfiguration.getContext(), "android.permission.ACCESS_COARSE_LOCATION")) &&
                ("1".equals(m.a().c(c)) || "true".equalsIgnoreCase(m.a().c(c)));
    }

    public void c() {
        c.e("ServiceConfigHelper:prepare to init Service Config");
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put(SocializeProtocolConstants.PROTOCOL_KEY_AK, o.c());
            jSONObject.put(a.j, DeviceInfoUtils.f(MWConfiguration.getContext()));
            jSONObject.put(a.h, "3.9.160727");
            jSONObject.put("pn", DeviceInfoUtils.e(MWConfiguration.getContext()));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Request aeVar = new ae(HttpMethod.POST, com.zxinsight.analytics.a.a.a, new v(this));
        aeVar.a(jSONObject);
        d.a(MWConfiguration.getContext()).a(aeVar);
    }

    private void a(ServiceConfigResponse serviceConfigResponse) {
        m a = m.a();
        ServiceConfig data = serviceConfigResponse.getData();
        m.a().a(a, String.valueOf(data.e));
        m.a().a(b, String.valueOf(data.ss));
        m.a().a(c, String.valueOf(data.lbs));
        m.a().e(data.dpt);
        a.a(data.getSp());
        a.a(data.getSt());
    }
}
