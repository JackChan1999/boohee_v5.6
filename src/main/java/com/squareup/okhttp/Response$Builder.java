package com.squareup.okhttp;

import com.squareup.okhttp.Headers.Builder;

public class Response$Builder {
    private ResponseBody body;
    private Response     cacheResponse;
    private int          code;
    private Handshake    handshake;
    private Builder      headers;
    private String       message;
    private Response     networkResponse;
    private Response     priorResponse;
    private Protocol     protocol;
    private Request      request;

    public Response$Builder() {
        this.code = -1;
        this.headers = new Builder();
    }

    private Response$Builder(Response response) {
        this.code = -1;
        this.request = Response.access$1100(response);
        this.protocol = Response.access$1200(response);
        this.code = Response.access$1300(response);
        this.message = Response.access$1400(response);
        this.handshake = Response.access$1500(response);
        this.headers = Response.access$1600(response).newBuilder();
        this.body = Response.access$1700(response);
        this.networkResponse = Response.access$1800(response);
        this.cacheResponse = Response.access$1900(response);
        this.priorResponse = Response.access$2000(response);
    }

    public Response$Builder request(Request request) {
        this.request = request;
        return this;
    }

    public Response$Builder protocol(Protocol protocol) {
        this.protocol = protocol;
        return this;
    }

    public Response$Builder code(int code) {
        this.code = code;
        return this;
    }

    public Response$Builder message(String message) {
        this.message = message;
        return this;
    }

    public Response$Builder handshake(Handshake handshake) {
        this.handshake = handshake;
        return this;
    }

    public Response$Builder header(String name, String value) {
        this.headers.set(name, value);
        return this;
    }

    public Response$Builder addHeader(String name, String value) {
        this.headers.add(name, value);
        return this;
    }

    public Response$Builder removeHeader(String name) {
        this.headers.removeAll(name);
        return this;
    }

    public Response$Builder headers(Headers headers) {
        this.headers = headers.newBuilder();
        return this;
    }

    public Response$Builder body(ResponseBody body) {
        this.body = body;
        return this;
    }

    public Response$Builder networkResponse(Response networkResponse) {
        if (networkResponse != null) {
            checkSupportResponse("networkResponse", networkResponse);
        }
        this.networkResponse = networkResponse;
        return this;
    }

    public Response$Builder cacheResponse(Response cacheResponse) {
        if (cacheResponse != null) {
            checkSupportResponse("cacheResponse", cacheResponse);
        }
        this.cacheResponse = cacheResponse;
        return this;
    }

    private void checkSupportResponse(String name, Response response) {
        if (Response.access$1700(response) != null) {
            throw new IllegalArgumentException(name + ".body != null");
        } else if (Response.access$1800(response) != null) {
            throw new IllegalArgumentException(name + ".networkResponse != null");
        } else if (Response.access$1900(response) != null) {
            throw new IllegalArgumentException(name + ".cacheResponse != null");
        } else if (Response.access$2000(response) != null) {
            throw new IllegalArgumentException(name + ".priorResponse != null");
        }
    }

    public Response$Builder priorResponse(Response priorResponse) {
        if (priorResponse != null) {
            checkPriorResponse(priorResponse);
        }
        this.priorResponse = priorResponse;
        return this;
    }

    private void checkPriorResponse(Response response) {
        if (Response.access$1700(response) != null) {
            throw new IllegalArgumentException("priorResponse.body != null");
        }
    }

    public Response build() {
        if (this.request == null) {
            throw new IllegalStateException("request == null");
        } else if (this.protocol == null) {
            throw new IllegalStateException("protocol == null");
        } else if (this.code >= 0) {
            return new Response(this, null);
        } else {
            throw new IllegalStateException("code < 0: " + this.code);
        }
    }
}
