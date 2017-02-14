package com.zxinsight;

import com.zxinsight.common.http.u;
import com.zxinsight.common.util.c;
import com.zxinsight.common.util.l;

import org.json.JSONException;
import org.json.JSONObject;

class f implements u<String> {
    final /* synthetic */ MLink a;

    f(MLink mLink) {
        this.a = mLink;
    }

    public void a(String str) {
        if (l.b(str)) {
            try {
                this.a.saveYYB(new JSONObject(str));
            } catch (JSONException e) {
                c.e("get MLink error!");
            }
        }
    }

    public void a(Exception exception) {
    }
}
