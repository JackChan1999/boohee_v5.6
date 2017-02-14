package com.squareup.okhttp.internal.http;

import com.squareup.okhttp.Address;
import com.squareup.okhttp.Connection;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.Interceptor.Chain;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.net.ProtocolException;

import okio.BufferedSink;
import okio.Okio;

class HttpEngine$NetworkInterceptorChain implements Chain {
    private               int        calls;
    private final         int        index;
    private final         Request    request;
    final /* synthetic */ HttpEngine this$0;

    HttpEngine$NetworkInterceptorChain(HttpEngine this$0, int index, Request request) {
        this.this$0 = this$0;
        this.index = index;
        this.request = request;
    }

    public Connection connection() {
        return this.this$0.streamAllocation.connection();
    }

    public Request request() {
        return this.request;
    }

    public Response proceed(Request request) throws IOException {
        this.calls++;
        if (this.index > 0) {
            Interceptor caller = (Interceptor) this.this$0.client.networkInterceptors().get(this
                    .index - 1);
            Address address = connection().getRoute().getAddress();
            if (!request.httpUrl().host().equals(address.getUriHost()) || request.httpUrl().port
                    () != address.getUriPort()) {
                throw new IllegalStateException("network interceptor " + caller + " must retain " +
                        "the same host and port");
            } else if (this.calls > 1) {
                throw new IllegalStateException("network interceptor " + caller + " must call " +
                        "proceed() exactly once");
            }
        }
        if (this.index < this.this$0.client.networkInterceptors().size()) {
            HttpEngine$NetworkInterceptorChain chain = new HttpEngine$NetworkInterceptorChain
                    (this.this$0, this.index + 1, request);
            Interceptor interceptor = (Interceptor) this.this$0.client.networkInterceptors().get
                    (this.index);
            Response intercept = interceptor.intercept(chain);
            if (chain.calls != 1) {
                throw new IllegalStateException("network interceptor " + interceptor + " must " +
                        "call proceed() exactly once");
            } else if (intercept != null) {
                return intercept;
            } else {
                throw new NullPointerException("network interceptor " + interceptor + " returned " +
                        "null");
            }
        }
        HttpEngine.access$000(this.this$0).writeRequestHeaders(request);
        HttpEngine.access$102(this.this$0, request);
        if (this.this$0.permitsRequestBody(request) && request.body() != null) {
            BufferedSink bufferedRequestBody = Okio.buffer(HttpEngine.access$000(this.this$0)
                    .createRequestBody(request, request.body().contentLength()));
            request.body().writeTo(bufferedRequestBody);
            bufferedRequestBody.close();
        }
        Response response = HttpEngine.access$200(this.this$0);
        int code = response.code();
        if ((code != 204 && code != 205) || response.body().contentLength() <= 0) {
            return response;
        }
        throw new ProtocolException("HTTP " + code + " had non-zero Content-Length: " + response.body().contentLength());
    }
}
