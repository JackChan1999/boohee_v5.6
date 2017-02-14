package com.alipay.apmobilesecuritysdk.f;

import android.content.Context;
import com.alipay.security.mobile.module.commonutils.CommonUtils;
import com.alipay.security.mobile.module.commonutils.crypto.DigestUtil;
import com.alipay.security.mobile.module.localstorage.SharePreferenceStorage;
import java.util.UUID;

public final class b {
    private static String a = "";

    public static synchronized String a(Context context) {
        String dataFromSharePreference;
        synchronized (b.class) {
            if (CommonUtils.isBlank(a)) {
                dataFromSharePreference = SharePreferenceStorage.getDataFromSharePreference(context, "alipay_vkey_random", "random", "");
                a = dataFromSharePreference;
                if (CommonUtils.isBlank(dataFromSharePreference)) {
                    a = DigestUtil.sha1ByString(UUID.randomUUID().toString());
                    SharePreferenceStorage.writeDataToSharePreference(context, "alipay_vkey_random", "random", a);
                }
            }
            dataFromSharePreference = a;
        }
        return dataFromSharePreference;
    }
}
