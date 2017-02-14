package com.meiqia.core;

import android.content.Intent;
import android.text.TextUtils;

import com.meiqia.core.a.a.a.a;
import com.meiqia.core.a.a.e.h;
import com.meiqia.core.b.c;
import com.meiqia.core.b.e;
import com.meiqia.core.b.j;
import com.umeng.socialize.net.utils.SocializeProtocolConstants;

import java.net.URI;

import org.json.JSONException;
import org.json.JSONObject;

class bq extends a {
    final /* synthetic */ MeiQiaService a;

    bq(MeiQiaService meiQiaService, URI uri) {
        this.a = meiQiaService;
        super(uri);
    }

    public void a(int i, String str, boolean z) {
        e.b("socket close: i = " + i + " s = " + str);
        MeiQiaService.c = false;
        this.a.c();
    }

    public void a(h hVar) {
        e.b("socket open");
        MeiQiaService.c = true;
        this.a.k.set(false);
        if (this.a.d) {
            this.a.e();
        }
        this.a.h.removeMessages(1);
    }

    public void a(Exception exception) {
        MeiQiaService.c = false;
        this.a.c();
        e.b("socket error: message = " + exception.getMessage() + " class = " + exception
                .getClass().getSimpleName());
    }

    public void a(String str) {
        if (!TextUtils.isEmpty(str) && !MeiQiaService.b) {
            try {
                JSONObject jSONObject = new JSONObject(str);
                String optString = jSONObject.optString("action");
                this.a.i.b(jSONObject.optString("id"));
                if ("message".equals(optString)) {
                    this.a.a(c.a(jSONObject));
                } else if ("ticket_reply".equals(optString)) {
                    this.a.a(jSONObject);
                } else if ("agent_redirect".equals(optString) || "timeout_redirect".equals
                        (optString)) {
                    this.a.a(c.b(jSONObject.optJSONObject("body").optJSONObject
                            (SocializeProtocolConstants.PROTOCOL_KEY_SHARE_TO)));
                } else if ("agent_inputting".equals(optString)) {
                    j.a(this.a, new Intent("agent_inputting_action"));
                } else if ("invite_evaluation".equals(optString)) {
                    this.a.b(jSONObject);
                } else if ("end_conv_agent".equals(optString)) {
                    JSONObject optJSONObject = jSONObject.optJSONObject("body");
                    if (optJSONObject != null && optJSONObject.optBoolean("evaluation")) {
                        this.a.b(jSONObject);
                    }
                } else if ("agent_update".equals(optString)) {
                    this.a.c(jSONObject);
                } else if ("visit_black_add".equals(optString)) {
                    this.a.d(jSONObject);
                }
            } catch (JSONException e) {
            }
        }
    }
}
