package com.squareup.okhttp;

import com.boohee.one.tinker.reporter.SampleTinkerReport;
import com.squareup.okhttp.internal.http.OkHeaders;

import java.util.Collections;
import java.util.List;

public final class Response {
    private final    ResponseBody body;
    private volatile CacheControl cacheControl;
    private          Response     cacheResponse;
    private final    int          code;
    private final    Handshake    handshake;
    private final    Headers      headers;
    private final    String       message;
    private          Response     networkResponse;
    private final    Response     priorResponse;
    private final    Protocol     protocol;
    private final    Request      request;

    private Response(Builder builder) {
        this.request = Builder.access$000(builder);
        this.protocol = Builder.access$100(builder);
        this.code = Builder.access$200(builder);
        this.message = Builder.access$300(builder);
        this.handshake = Builder.access$400(builder);
        this.headers = Builder.access$500(builder).build();
        this.body = Builder.access$600(builder);
        this.networkResponse = Builder.access$700(builder);
        this.cacheResponse = Builder.access$800(builder);
        this.priorResponse = Builder.access$900(builder);
    }

    public Request request() {
        return this.request;
    }

    public Protocol protocol() {
        return this.protocol;
    }

    public int code() {
        return this.code;
    }

    public boolean isSuccessful() {
        return this.code >= 200 && this.code < 300;
    }

    public String message() {
        return this.message;
    }

    public Handshake handshake() {
        return this.handshake;
    }

    public List<String> headers(String name) {
        return this.headers.values(name);
    }

    public String header(String name) {
        return header(name, null);
    }

    public String header(String name, String defaultValue) {
        String result = this.headers.get(name);
        return result != null ? result : defaultValue;
    }

    public Headers headers() {
        return this.headers;
    }

    public ResponseBody body() {
        return this.body;
    }

    public Builder newBuilder() {
        return new Builder(this, null);
    }

    public boolean isRedirect() {
        switch (this.code) {
            case 300:
            case SampleTinkerReport.KEY_LOADED_MISMATCH_LIB /*301*/:
            case SampleTinkerReport.KEY_LOADED_MISMATCH_RESOURCE /*302*/:
            case SampleTinkerReport.KEY_LOADED_MISSING_DEX /*303*/:
            case 307:
            case 308:
                return true;
            default:
                return false;
        }
    }

    public Response networkResponse() {
        return this.networkResponse;
    }

    public Response cacheResponse() {
        return this.cacheResponse;
    }

    public Response priorResponse() {
        return this.priorResponse;
    }

    public List<Challenge> challenges() {
        String responseField;
        if (this.code == SampleTinkerReport.KEY_LOADED_SUCC_COST_1000_LESS) {
            responseField = "WWW-Authenticate";
        } else if (this.code != 407) {
            return Collections.emptyList();
        } else {
            responseField = "Proxy-Authenticate";
        }
        return OkHeaders.parseChallenges(headers(), responseField);
    }

    public CacheControl cacheControl() {
        CacheControl result = this.cacheControl;
        if (result != null) {
            return result;
        }
        result = CacheControl.parse(this.headers);
        this.cacheControl = result;
        return result;
    }

    public String toString() {
        return "Response{protocol=" + this.protocol + ", code=" + this.code + ", message=" + this.message + ", url=" + this.request.urlString() + '}';
    }
}
