package com.zxinsight.analytics.a;

import com.zxinsight.common.util.h;
import com.zxinsight.common.util.l;
import com.zxinsight.common.util.m;
import com.zxinsight.mlink.domain.DPLsResponse;

import org.json.JSONObject;

public final class b {
    private static DPLsResponse a = null;

    public static DPLsResponse a() {
        if (a != null) {
            return a;
        }
        String A = m.a().A();
        if (l.b(A)) {
            try {
                a = (DPLsResponse) h.a(new JSONObject(A), DPLsResponse.class);
                return a;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
