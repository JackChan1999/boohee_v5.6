package com.alipay.android.phone.mrpc.core;

import com.boohee.one.tinker.reporter.SampleTinkerReport;
import org.apache.http.HttpResponse;
import org.apache.http.impl.client.DefaultRedirectHandler;
import org.apache.http.protocol.HttpContext;

final class e extends DefaultRedirectHandler {
    int a;
    final /* synthetic */ d b;

    e(d dVar) {
        this.b = dVar;
    }

    public final boolean isRedirectRequested(HttpResponse httpResponse, HttpContext httpContext) {
        this.a++;
        boolean isRedirectRequested = super.isRedirectRequested(httpResponse, httpContext);
        if (isRedirectRequested || this.a >= 5) {
            return isRedirectRequested;
        }
        int statusCode = httpResponse.getStatusLine().getStatusCode();
        return (statusCode == SampleTinkerReport.KEY_LOADED_MISMATCH_LIB || statusCode == SampleTinkerReport.KEY_LOADED_MISMATCH_RESOURCE) ? true : isRedirectRequested;
    }
}
