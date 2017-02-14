package com.meiqia.core;

import com.meiqia.core.callback.SimpleCallback;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.File;
import java.io.IOException;

import okio.BufferedSink;
import okio.Okio;

class ce implements Callback {
    final /* synthetic */ SimpleCallback a;
    final /* synthetic */ File           b;
    final /* synthetic */ bu             c;

    ce(bu buVar, SimpleCallback simpleCallback, File file) {
        this.c = buVar;
        this.a = simpleCallback;
        this.b = file;
    }

    public void onFailure(Request request, IOException iOException) {
        this.a.onFailure(0, "download failed");
    }

    public void onResponse(Response response) {
        if (response.isSuccessful()) {
            BufferedSink buffer = Okio.buffer(Okio.sink(this.b));
            buffer.writeAll(response.body().source());
            buffer.close();
            this.a.onSuccess();
            return;
        }
        this.a.onFailure(0, "download failed");
    }
}
