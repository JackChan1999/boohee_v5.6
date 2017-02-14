package com.meiqia.core;

import android.text.TextUtils;

import com.meiqia.core.b.a;
import com.meiqia.core.b.c;
import com.meiqia.core.callback.OnFailureCallBack;
import com.meiqia.meiqiasdk.util.ErrorCode;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

import org.json.JSONObject;

class cj implements Callback {
    final /* synthetic */ OnFailureCallBack a;
    final /* synthetic */ cw                b;
    final /* synthetic */ bu                c;

    cj(bu buVar, OnFailureCallBack onFailureCallBack, cw cwVar) {
        this.c = buVar;
        this.a = onFailureCallBack;
        this.b = cwVar;
    }

    public void onFailure(Request request, IOException iOException) {
        this.c.d.post(new ck(this, iOException));
    }

    public void onResponse(Response response) {
        if (this.b != null) {
            if (this.b instanceof cx) {
                this.b.a(null, response);
                return;
            }
            JSONObject a = c.a(response);
            Object optString = a.optString("ret");
            if (!TextUtils.isEmpty(optString)) {
                try {
                    a = new JSONObject(a.b(b.a.getAESKey(), optString));
                } catch (Exception e) {
                    this.c.d.post(new cl(this));
                    return;
                }
            }
            if (response.isSuccessful()) {
                if (a.has("msg")) {
                    if ("conversation not found".equals(a.optString("msg"))) {
                        this.c.d.post(new cm(this));
                        return;
                    }
                }
                if (!a.has("success")) {
                    this.b.a(a, response);
                    return;
                } else if (a.optBoolean("success")) {
                    this.b.a(a, response);
                    return;
                } else {
                    this.c.d.post(new cn(this, a.optBoolean("black") ? ErrorCode.BLACKLIST :
                            response.code()));
                    return;
                }
            }
            this.c.d.post(new co(this, a, response));
        }
    }
}
