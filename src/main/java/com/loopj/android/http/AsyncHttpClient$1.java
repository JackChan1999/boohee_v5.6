package com.loopj.android.http;

import android.util.Log;

import org.apache.http.Header;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.protocol.HttpContext;

class AsyncHttpClient$1 implements HttpRequestInterceptor {
    final /* synthetic */ AsyncHttpClient this$0;

    AsyncHttpClient$1(AsyncHttpClient this$0) {
        this.this$0 = this$0;
    }

    public void process(HttpRequest request, HttpContext context) {
        if (!request.containsHeader(AsyncHttpClient.HEADER_ACCEPT_ENCODING)) {
            request.addHeader(AsyncHttpClient.HEADER_ACCEPT_ENCODING, AsyncHttpClient
                    .ENCODING_GZIP);
        }
        for (String header : AsyncHttpClient.access$000(this.this$0).keySet()) {
            if (request.containsHeader(header)) {
                Header overwritten = request.getFirstHeader(header);
                Log.d(AsyncHttpClient.LOG_TAG, String.format("Headers were overwritten! (%s | %s)" +
                        " overwrites (%s | %s)", new Object[]{header, AsyncHttpClient.access$000
                        (this.this$0).get(header), overwritten.getName(), overwritten.getValue()}));
                request.removeHeader(overwritten);
            }
            request.addHeader(header, (String) AsyncHttpClient.access$000(this.this$0).get(header));
        }
    }
}
