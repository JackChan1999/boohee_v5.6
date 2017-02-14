package com.loopj.android.http;

import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpResponseInterceptor;
import org.apache.http.protocol.HttpContext;

class AsyncHttpClient$2 implements HttpResponseInterceptor {
    final /* synthetic */ AsyncHttpClient this$0;

    AsyncHttpClient$2(AsyncHttpClient this$0) {
        this.this$0 = this$0;
    }

    public void process(HttpResponse response, HttpContext context) {
        HttpEntity entity = response.getEntity();
        if (entity != null) {
            Header encoding = entity.getContentEncoding();
            if (encoding != null) {
                for (HeaderElement element : encoding.getElements()) {
                    if (element.getName().equalsIgnoreCase(AsyncHttpClient.ENCODING_GZIP)) {
                        response.setEntity(new AsyncHttpClient$InflatingEntity(entity));
                        return;
                    }
                }
            }
        }
    }
}
