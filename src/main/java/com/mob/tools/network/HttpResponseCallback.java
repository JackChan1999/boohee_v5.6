package com.mob.tools.network;

import org.apache.http.HttpResponse;

public interface HttpResponseCallback {
    void onResponse(HttpResponse httpResponse) throws Throwable;
}
