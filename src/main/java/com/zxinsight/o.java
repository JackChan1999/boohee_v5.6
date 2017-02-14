package com.zxinsight;

import android.text.TextUtils;
import android.util.Log;

import com.zxinsight.analytics.domain.response.MarketingResponse;
import com.zxinsight.common.http.u;
import com.zxinsight.common.util.c;
import com.zxinsight.common.util.f;
import com.zxinsight.common.util.h;
import com.zxinsight.common.util.l;

import org.json.JSONException;
import org.json.JSONObject;

class o implements u<String> {
    final /* synthetic */ String                  a;
    final /* synthetic */ UpdateMarketingListener b;
    final /* synthetic */ MarketingHelper         c;

    o(MarketingHelper marketingHelper, String str, UpdateMarketingListener
            updateMarketingListener) {
        this.c = marketingHelper;
        this.a = str;
        this.b = updateMarketingListener;
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
                    MarketingResponse marketingResponse = (MarketingResponse) h.a(jSONObject,
                            MarketingResponse.class);
                    if (TextUtils.isEmpty(this.a)) {
                        if (f.a(marketingResponse)) {
                            MarketingHelper.access$000(this.c, marketingResponse);
                        } else {
                            MarketingHelper.access$000(this.c, new MarketingResponse());
                        }
                        if (this.b != null) {
                            this.b.success();
                        }
                    } else if (f.a(marketingResponse)) {
                        MarketingHelper.access$100(this.c).remove(this.a);
                        if (l.b(marketingResponse.getData())) {
                            MarketingHelper.access$100(this.c).put(this.a, marketingResponse
                                    .getData().get(0));
                        }
                        if (this.b != null) {
                            this.b.success();
                        }
                    } else {
                        c.e("get Marketing error! please make sure that you has added active on " +
                                "website !");
                        if (this.b != null) {
                            this.b.failed("get Marketing error! please make sure that you has " +
                                    "added active on website !");
                        }
                    }
                } else {
                    c.e("get Marketing error!");
                    if (this.b != null) {
                        this.b.failed("get Marketing error !");
                    }
                }
            } catch (Exception e2) {
                e2.printStackTrace();
                c.e(e2.getMessage());
                if (this.b != null) {
                    this.b.failed(e2.getMessage());
                }
            }
            Log.e("marketing", "success = ");
        }
    }

    public void a(Exception exception) {
        Log.e("marketing", "e = " + exception.getMessage());
    }
}
