package com.meiqia.core;

import com.meiqia.core.callback.OnProgressCallback;
import com.squareup.okhttp.Response;

import java.io.File;

import org.json.JSONObject;

class cu extends cx {
    final /* synthetic */ File               a;
    final /* synthetic */ OnProgressCallback b;
    final /* synthetic */ bu                 c;

    cu(bu buVar, File file, OnProgressCallback onProgressCallback) {
        this.c = buVar;
        this.a = file;
        this.b = onProgressCallback;
        super(buVar);
    }

    public void a(JSONObject jSONObject, Response response) {
        this.c.a(this.a, response, this.b);
    }
}
