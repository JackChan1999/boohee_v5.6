package com.meiqia.core;

import com.squareup.okhttp.Response;

import org.json.JSONObject;

class co implements Runnable {
    final /* synthetic */ JSONObject a;
    final /* synthetic */ Response   b;
    final /* synthetic */ cj         c;

    co(cj cjVar, JSONObject jSONObject, Response response) {
        this.c = cjVar;
        this.a = jSONObject;
        this.b = response;
    }

    public void run() {
        String jSONObject = this.a.toString();
        if (this.c.a != null) {
            this.c.a.onFailure(20000, "code = " + this.b.code() + " msg = " + this.b.message() +
                    " details = " + jSONObject);
        }
    }
}
