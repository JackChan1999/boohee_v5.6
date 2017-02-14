package com.alipay.apmobilesecuritysdk.e;

import android.content.Context;
import com.alipay.security.mobile.module.commonutils.CommonUtils;
import com.alipay.security.mobile.module.commonutils.LOG;
import com.umeng.socialize.net.utils.SocializeProtocolConstants;
import org.json.JSONObject;

public final class c {
    public static d a(Context context) {
        if (context == null) {
            return null;
        }
        String a = com.alipay.apmobilesecuritysdk.f.c.a(context, "device_feature_prefs_name", "device_feature_prefs_key");
        if (CommonUtils.isBlank(a)) {
            a = com.alipay.apmobilesecuritysdk.f.c.a("device_feature_file_name", "device_feature_file_key");
        }
        if (CommonUtils.isBlank(a)) {
            return null;
        }
        try {
            JSONObject jSONObject = new JSONObject(a);
            d dVar = new d();
            dVar.a(jSONObject.getString(SocializeProtocolConstants.PROTOCOL_KEY_IMEI));
            dVar.b(jSONObject.getString("imsi"));
            dVar.c(jSONObject.getString(SocializeProtocolConstants.PROTOCOL_KEY_MAC));
            dVar.d(jSONObject.getString("bluetoothmac"));
            dVar.e(jSONObject.getString("gsi"));
            return dVar;
        } catch (Throwable e) {
            LOG.logException(e);
            return null;
        }
    }
}
