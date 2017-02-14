package com.zxinsight;

import com.zxinsight.analytics.domain.response.TimestampResponse;
import com.zxinsight.common.http.u;
import com.zxinsight.common.util.c;
import com.zxinsight.common.util.h;
import com.zxinsight.common.util.l;
import com.zxinsight.common.util.m;

import org.json.JSONException;
import org.json.JSONObject;

class n implements u<String> {
    final /* synthetic */ String          a;
    final /* synthetic */ MarketingHelper b;

    n(MarketingHelper marketingHelper, String str) {
        this.b = marketingHelper;
        this.a = str;
    }

    public void a(String str) {
        if (l.b(str)) {
            JSONObject jSONObject;
            try {
                jSONObject = new JSONObject(str);
            } catch (JSONException e) {
                e.printStackTrace();
                jSONObject = null;
            }
            try {
                if (h.c(jSONObject)) {
                    long j = ((TimestampResponse) h.a(jSONObject, TimestampResponse.class))
                            .getData().ts;
                    if (Math.abs(j) >= m.a().i()) {
                        this.b.update(this.a, null);
                        m.a().a(j);
                        return;
                    }
                    return;
                }
                c.e("get Timestamp error!");
            } catch (Exception e2) {
                e2.printStackTrace();
                c.e(e2.getMessage());
            }
        }
    }

    public void a(Exception exception) {
        this.b.update(this.a, null);
    }
}
