package com.squareup.okhttp.internal.http;

import com.aspsine.multithreaddownload.Constants.HTTP;
import com.boohee.one.tinker.reporter.SampleTinkerReport;
import com.loopj.android.http.AsyncHttpClient;
import com.squareup.okhttp.Address;
import com.squareup.okhttp.CertificatePinner;
import com.squareup.okhttp.Connection;
import com.squareup.okhttp.Headers;
import com.squareup.okhttp.HttpUrl;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Protocol;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.Response.Builder;
import com.squareup.okhttp.ResponseBody;
import com.squareup.okhttp.internal.Internal;
import com.squareup.okhttp.internal.InternalCache;
import com.squareup.okhttp.internal.Util;
import com.squareup.okhttp.internal.Version;
import com.squareup.okhttp.internal.http.CacheStrategy.Factory;
import com.xiaomi.account.openauth.utils.Network;

import java.io.IOException;
import java.net.CookieHandler;
import java.util.Date;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSocketFactory;

import okio.BufferedSink;
import okio.GzipSource;
import okio.Okio;
import okio.Sink;

public final class HttpEngine {
    private static final ResponseBody EMPTY_BODY = new
    1();
    public static final int MAX_FOLLOW_UPS = 20;
    public final  boolean       bufferRequestBody;
    private       BufferedSink  bufferedRequestBody;
    private       Response      cacheResponse;
    private       CacheStrategy cacheStrategy;
    private final boolean       callerWritesRequestBody;
    final         OkHttpClient  client;
    private final boolean       forWebSocket;
    private       HttpStream    httpStream;
    private       Request       networkRequest;
    private final Response      priorResponse;
    private       Sink          requestBodyOut;
    long sentRequestMillis = -1;
    private       CacheRequest     storeRequest;
    public final  StreamAllocation streamAllocation;
    private       boolean          transparentGzip;
    private final Request          userRequest;
    private       Response         userResponse;

    public HttpEngine(OkHttpClient client, Request request, boolean bufferRequestBody, boolean
            callerWritesRequestBody, boolean forWebSocket, StreamAllocation streamAllocation,
                      RetryableSink requestBodyOut, Response priorResponse) {
        this.client = client;
        this.userRequest = request;
        this.bufferRequestBody = bufferRequestBody;
        this.callerWritesRequestBody = callerWritesRequestBody;
        this.forWebSocket = forWebSocket;
        if (streamAllocation == null) {
            streamAllocation = new StreamAllocation(client.getConnectionPool(), createAddress
                    (client, request));
        }
        this.streamAllocation = streamAllocation;
        this.requestBodyOut = requestBodyOut;
        this.priorResponse = priorResponse;
    }

    public void sendRequest() throws RequestException, RouteException, IOException {
        if (this.cacheStrategy == null) {
            if (this.httpStream != null) {
                throw new IllegalStateException();
            }
            Request request = networkRequest(this.userRequest);
            InternalCache responseCache = Internal.instance.internalCache(this.client);
            Response cacheCandidate = responseCache != null ? responseCache.get(request) : null;
            this.cacheStrategy = new Factory(System.currentTimeMillis(), request, cacheCandidate)
                    .get();
            this.networkRequest = this.cacheStrategy.networkRequest;
            this.cacheResponse = this.cacheStrategy.cacheResponse;
            if (responseCache != null) {
                responseCache.trackResponse(this.cacheStrategy);
            }
            if (cacheCandidate != null && this.cacheResponse == null) {
                Util.closeQuietly(cacheCandidate.body());
            }
            if (this.networkRequest != null) {
                this.httpStream = connect();
                this.httpStream.setHttpEngine(this);
                if (this.callerWritesRequestBody && permitsRequestBody(this.networkRequest) &&
                        this.requestBodyOut == null) {
                    long contentLength = OkHeaders.contentLength(request);
                    if (!this.bufferRequestBody) {
                        this.httpStream.writeRequestHeaders(this.networkRequest);
                        this.requestBodyOut = this.httpStream.createRequestBody(this
                                .networkRequest, contentLength);
                        return;
                    } else if (contentLength > 2147483647L) {
                        throw new IllegalStateException("Use setFixedLengthStreamingMode() or " +
                                "setChunkedStreamingMode() for requests larger than 2 GiB.");
                    } else if (contentLength != -1) {
                        this.httpStream.writeRequestHeaders(this.networkRequest);
                        this.requestBodyOut = new RetryableSink((int) contentLength);
                        return;
                    } else {
                        this.requestBodyOut = new RetryableSink();
                        return;
                    }
                }
                return;
            }
            if (this.cacheResponse != null) {
                this.userResponse = this.cacheResponse.newBuilder().request(this.userRequest)
                        .priorResponse(stripBody(this.priorResponse)).cacheResponse(stripBody
                                (this.cacheResponse)).build();
            } else {
                this.userResponse = new Builder().request(this.userRequest).priorResponse
                        (stripBody(this.priorResponse)).protocol(Protocol.HTTP_1_1).code(504)
                        .message("Unsatisfiable Request (only-if-cached)").body(EMPTY_BODY).build();
            }
            this.userResponse = unzip(this.userResponse);
        }
    }

    private HttpStream connect() throws RouteException, RequestException, IOException {
        return this.streamAllocation.newStream(this.client.getConnectTimeout(), this.client
                .getReadTimeout(), this.client.getWriteTimeout(), this.client
                .getRetryOnConnectionFailure(), !this.networkRequest.method().equals("GET"));
    }

    private static Response stripBody(Response response) {
        return (response == null || response.body() == null) ? response : response.newBuilder()
                .body(null).build();
    }

    public void writingRequestHeaders() {
        if (this.sentRequestMillis != -1) {
            throw new IllegalStateException();
        }
        this.sentRequestMillis = System.currentTimeMillis();
    }

    boolean permitsRequestBody(Request request) {
        return HttpMethod.permitsRequestBody(request.method());
    }

    public Sink getRequestBody() {
        if (this.cacheStrategy != null) {
            return this.requestBodyOut;
        }
        throw new IllegalStateException();
    }

    public BufferedSink getBufferedRequestBody() {
        BufferedSink result = this.bufferedRequestBody;
        if (result != null) {
            return result;
        }
        BufferedSink buffer;
        Sink requestBody = getRequestBody();
        if (requestBody != null) {
            buffer = Okio.buffer(requestBody);
            this.bufferedRequestBody = buffer;
        } else {
            buffer = null;
        }
        return buffer;
    }

    public boolean hasResponse() {
        return this.userResponse != null;
    }

    public Request getRequest() {
        return this.userRequest;
    }

    public Response getResponse() {
        if (this.userResponse != null) {
            return this.userResponse;
        }
        throw new IllegalStateException();
    }

    public Connection getConnection() {
        return this.streamAllocation.connection();
    }

    public HttpEngine recover(RouteException e) {
        if (!this.streamAllocation.recover(e) || !this.client.getRetryOnConnectionFailure()) {
            return null;
        }
        return new HttpEngine(this.client, this.userRequest, this.bufferRequestBody, this
                .callerWritesRequestBody, this.forWebSocket, close(), (RetryableSink) this
                .requestBodyOut, this.priorResponse);
    }

    public HttpEngine recover(IOException e, Sink requestBodyOut) {
        if (!this.streamAllocation.recover(e, requestBodyOut) || !this.client
                .getRetryOnConnectionFailure()) {
            return null;
        }
        return new HttpEngine(this.client, this.userRequest, this.bufferRequestBody, this
                .callerWritesRequestBody, this.forWebSocket, close(), (RetryableSink)
                requestBodyOut, this.priorResponse);
    }

    public HttpEngine recover(IOException e) {
        return recover(e, this.requestBodyOut);
    }

    private void maybeCache() throws IOException {
        InternalCache responseCache = Internal.instance.internalCache(this.client);
        if (responseCache != null) {
            if (CacheStrategy.isCacheable(this.userResponse, this.networkRequest)) {
                this.storeRequest = responseCache.put(stripBody(this.userResponse));
            } else if (HttpMethod.invalidatesCache(this.networkRequest.method())) {
                try {
                    responseCache.remove(this.networkRequest);
                } catch (IOException e) {
                }
            }
        }
    }

    public void releaseStreamAllocation() throws IOException {
        this.streamAllocation.release();
    }

    public void cancel() {
        this.streamAllocation.cancel();
    }

    public StreamAllocation close() {
        if (this.bufferedRequestBody != null) {
            Util.closeQuietly(this.bufferedRequestBody);
        } else if (this.requestBodyOut != null) {
            Util.closeQuietly(this.requestBodyOut);
        }
        if (this.userResponse != null) {
            Util.closeQuietly(this.userResponse.body());
        } else {
            this.streamAllocation.connectionFailed();
        }
        return this.streamAllocation;
    }

    private Response unzip(Response response) throws IOException {
        if (!this.transparentGzip || !AsyncHttpClient.ENCODING_GZIP.equalsIgnoreCase(this
                .userResponse.header(AsyncHttpClient.HEADER_CONTENT_ENCODING)) || response.body()
                == null) {
            return response;
        }
        GzipSource responseBody = new GzipSource(response.body().source());
        Headers strippedHeaders = response.headers().newBuilder().removeAll(AsyncHttpClient
                .HEADER_CONTENT_ENCODING).removeAll("Content-Length").build();
        return response.newBuilder().headers(strippedHeaders).body(new RealResponseBody
                (strippedHeaders, Okio.buffer(responseBody))).build();
    }

    public static boolean hasBody(Response response) {
        if (response.request().method().equals(HTTP.HEAD)) {
            return false;
        }
        int responseCode = response.code();
        if ((responseCode < 100 || responseCode >= 200) && responseCode != 204 && responseCode !=
                SampleTinkerReport.KEY_LOADED_MISSING_LIB) {
            return true;
        }
        if (OkHeaders.contentLength(response) != -1 || "chunked".equalsIgnoreCase(response.header
                ("Transfer-Encoding"))) {
            return true;
        }
        return false;
    }

    private Request networkRequest(Request request) throws IOException {
        Request.Builder result = request.newBuilder();
        if (request.header("Host") == null) {
            result.header("Host", Util.hostHeader(request.httpUrl()));
        }
        if (request.header("Connection") == null) {
            result.header("Connection", "Keep-Alive");
        }
        if (request.header(AsyncHttpClient.HEADER_ACCEPT_ENCODING) == null) {
            this.transparentGzip = true;
            result.header(AsyncHttpClient.HEADER_ACCEPT_ENCODING, AsyncHttpClient.ENCODING_GZIP);
        }
        CookieHandler cookieHandler = this.client.getCookieHandler();
        if (cookieHandler != null) {
            OkHeaders.addCookies(result, cookieHandler.get(request.uri(), OkHeaders.toMultimap
                    (result.build().headers(), null)));
        }
        if (request.header(Network.USER_AGENT) == null) {
            result.header(Network.USER_AGENT, Version.userAgent());
        }
        return result.build();
    }

    public void readResponse() throws IOException {
        if (this.userResponse == null) {
            if (this.networkRequest == null && this.cacheResponse == null) {
                throw new IllegalStateException("call sendRequest() first!");
            } else if (this.networkRequest != null) {
                Response networkResponse;
                if (this.forWebSocket) {
                    this.httpStream.writeRequestHeaders(this.networkRequest);
                    networkResponse = readNetworkResponse();
                } else if (this.callerWritesRequestBody) {
                    if (this.bufferedRequestBody != null && this.bufferedRequestBody.buffer()
                            .size() > 0) {
                        this.bufferedRequestBody.emit();
                    }
                    if (this.sentRequestMillis == -1) {
                        if (OkHeaders.contentLength(this.networkRequest) == -1 && (this
                                .requestBodyOut instanceof RetryableSink)) {
                            this.networkRequest = this.networkRequest.newBuilder().header
                                    ("Content-Length", Long.toString(((RetryableSink) this
                                            .requestBodyOut).contentLength())).build();
                        }
                        this.httpStream.writeRequestHeaders(this.networkRequest);
                    }
                    if (this.requestBodyOut != null) {
                        if (this.bufferedRequestBody != null) {
                            this.bufferedRequestBody.close();
                        } else {
                            this.requestBodyOut.close();
                        }
                        if (this.requestBodyOut instanceof RetryableSink) {
                            this.httpStream.writeRequestBody((RetryableSink) this.requestBodyOut);
                        }
                    }
                    networkResponse = readNetworkResponse();
                } else {
                    networkResponse = new NetworkInterceptorChain(this, 0, this.networkRequest)
                            .proceed(this.networkRequest);
                }
                receiveHeaders(networkResponse.headers());
                if (this.cacheResponse != null) {
                    if (validate(this.cacheResponse, networkResponse)) {
                        this.userResponse = this.cacheResponse.newBuilder().request(this
                                .userRequest).priorResponse(stripBody(this.priorResponse))
                                .headers(combine(this.cacheResponse.headers(), networkResponse
                                        .headers())).cacheResponse(stripBody(this.cacheResponse))
                                .networkResponse(stripBody(networkResponse)).build();
                        networkResponse.body().close();
                        releaseStreamAllocation();
                        InternalCache responseCache = Internal.instance.internalCache(this.client);
                        responseCache.trackConditionalCacheHit();
                        responseCache.update(this.cacheResponse, stripBody(this.userResponse));
                        this.userResponse = unzip(this.userResponse);
                        return;
                    }
                    Util.closeQuietly(this.cacheResponse.body());
                }
                this.userResponse = networkResponse.newBuilder().request(this.userRequest)
                        .priorResponse(stripBody(this.priorResponse)).cacheResponse(stripBody
                                (this.cacheResponse)).networkResponse(stripBody(networkResponse))
                        .build();
                if (hasBody(this.userResponse)) {
                    maybeCache();
                    this.userResponse = unzip(cacheWritingResponse(this.storeRequest, this
                            .userResponse));
                }
            }
        }
    }

    private Response readNetworkResponse() throws IOException {
        this.httpStream.finishRequest();
        Response networkResponse = this.httpStream.readResponseHeaders().request(this
                .networkRequest).handshake(this.streamAllocation.connection().getHandshake())
                .header(OkHeaders.SENT_MILLIS, Long.toString(this.sentRequestMillis)).header
                        (OkHeaders.RECEIVED_MILLIS, Long.toString(System.currentTimeMillis()))
                .build();
        if (!this.forWebSocket) {
            networkResponse = networkResponse.newBuilder().body(this.httpStream.openResponseBody
                    (networkResponse)).build();
        }
        if ("close".equalsIgnoreCase(networkResponse.request().header("Connection")) || "close"
                .equalsIgnoreCase(networkResponse.header("Connection"))) {
            this.streamAllocation.noNewStreams();
        }
        return networkResponse;
    }

    private Response cacheWritingResponse(CacheRequest cacheRequest, Response response) throws
            IOException {
        if (cacheRequest == null) {
            return response;
        }
        Sink cacheBodyUnbuffered = cacheRequest.body();
        if (cacheBodyUnbuffered == null) {
            return response;
        }
        return response.newBuilder().body(new RealResponseBody(response.headers(), Okio.buffer(new 2
        (this, response.body().source(), cacheRequest, Okio.buffer(cacheBodyUnbuffered))))).build();
    }

    private static boolean validate(Response cached, Response network) {
        if (network.code() == SampleTinkerReport.KEY_LOADED_MISSING_LIB) {
            return true;
        }
        Date lastModified = cached.headers().getDate("Last-Modified");
        if (lastModified != null) {
            Date networkLastModified = network.headers().getDate("Last-Modified");
            if (networkLastModified != null && networkLastModified.getTime() < lastModified
                    .getTime()) {
                return true;
            }
        }
        return false;
    }

    private static Headers combine(Headers cachedHeaders, Headers networkHeaders) throws
            IOException {
        int i;
        Headers.Builder result = new Headers.Builder();
        int size = cachedHeaders.size();
        for (i = 0; i < size; i++) {
            String fieldName = cachedHeaders.name(i);
            String value = cachedHeaders.value(i);
            if (!("Warning".equalsIgnoreCase(fieldName) && value.startsWith("1")) && (!OkHeaders
                    .isEndToEnd(fieldName) || networkHeaders.get(fieldName) == null)) {
                result.add(fieldName, value);
            }
        }
        size = networkHeaders.size();
        for (i = 0; i < size; i++) {
            fieldName = networkHeaders.name(i);
            if (!"Content-Length".equalsIgnoreCase(fieldName) && OkHeaders.isEndToEnd(fieldName)) {
                result.add(fieldName, networkHeaders.value(i));
            }
        }
        return result.build();
    }

    public void receiveHeaders(Headers headers) throws IOException {
        CookieHandler cookieHandler = this.client.getCookieHandler();
        if (cookieHandler != null) {
            cookieHandler.put(this.userRequest.uri(), OkHeaders.toMultimap(headers, null));
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public com.squareup.okhttp.Request followUpRequest() throws java.io.IOException {
        /*
        r12 = this;
        r9 = 0;
        r10 = r12.userResponse;
        if (r10 != 0) goto L_0x000b;
    L_0x0005:
        r9 = new java.lang.IllegalStateException;
        r9.<init>();
        throw r9;
    L_0x000b:
        r10 = r12.streamAllocation;
        r0 = r10.connection();
        if (r0 == 0) goto L_0x002d;
    L_0x0013:
        r5 = r0.getRoute();
    L_0x0017:
        if (r5 == 0) goto L_0x002f;
    L_0x0019:
        r7 = r5.getProxy();
    L_0x001d:
        r10 = r12.userResponse;
        r4 = r10.code();
        r10 = r12.userRequest;
        r2 = r10.method();
        switch(r4) {
            case 300: goto L_0x0066;
            case 301: goto L_0x0066;
            case 302: goto L_0x0066;
            case 303: goto L_0x0066;
            case 307: goto L_0x0054;
            case 308: goto L_0x0054;
            case 401: goto L_0x0047;
            case 407: goto L_0x0036;
            default: goto L_0x002c;
        };
    L_0x002c:
        return r9;
    L_0x002d:
        r5 = r9;
        goto L_0x0017;
    L_0x002f:
        r10 = r12.client;
        r7 = r10.getProxy();
        goto L_0x001d;
    L_0x0036:
        r9 = r7.type();
        r10 = java.net.Proxy.Type.HTTP;
        if (r9 == r10) goto L_0x0047;
    L_0x003e:
        r9 = new java.net.ProtocolException;
        r10 = "Received HTTP_PROXY_AUTH (407) code while not using proxy";
        r9.<init>(r10);
        throw r9;
    L_0x0047:
        r9 = r12.client;
        r9 = r9.getAuthenticator();
        r10 = r12.userResponse;
        r9 = com.squareup.okhttp.internal.http.OkHeaders.processAuthHeader(r9, r10, r7);
        goto L_0x002c;
    L_0x0054:
        r10 = "GET";
        r10 = r2.equals(r10);
        if (r10 != 0) goto L_0x0066;
    L_0x005d:
        r10 = "HEAD";
        r10 = r2.equals(r10);
        if (r10 == 0) goto L_0x002c;
    L_0x0066:
        r10 = r12.client;
        r10 = r10.getFollowRedirects();
        if (r10 == 0) goto L_0x002c;
    L_0x006e:
        r10 = r12.userResponse;
        r11 = "Location";
        r1 = r10.header(r11);
        if (r1 == 0) goto L_0x002c;
    L_0x0079:
        r10 = r12.userRequest;
        r10 = r10.httpUrl();
        r8 = r10.resolve(r1);
        if (r8 == 0) goto L_0x002c;
    L_0x0085:
        r10 = r8.scheme();
        r11 = r12.userRequest;
        r11 = r11.httpUrl();
        r11 = r11.scheme();
        r6 = r10.equals(r11);
        if (r6 != 0) goto L_0x00a1;
    L_0x0099:
        r10 = r12.client;
        r10 = r10.getFollowSslRedirects();
        if (r10 == 0) goto L_0x002c;
    L_0x00a1:
        r10 = r12.userRequest;
        r3 = r10.newBuilder();
        r10 = com.squareup.okhttp.internal.http.HttpMethod.permitsRequestBody(r2);
        if (r10 == 0) goto L_0x00cb;
    L_0x00ad:
        r10 = com.squareup.okhttp.internal.http.HttpMethod.redirectsToGet(r2);
        if (r10 == 0) goto L_0x00e1;
    L_0x00b3:
        r10 = "GET";
        r3.method(r10, r9);
    L_0x00b9:
        r9 = "Transfer-Encoding";
        r3.removeHeader(r9);
        r9 = "Content-Length";
        r3.removeHeader(r9);
        r9 = "Content-Type";
        r3.removeHeader(r9);
    L_0x00cb:
        r9 = r12.sameConnection(r8);
        if (r9 != 0) goto L_0x00d7;
    L_0x00d1:
        r9 = "Authorization";
        r3.removeHeader(r9);
    L_0x00d7:
        r9 = r3.url(r8);
        r9 = r9.build();
        goto L_0x002c;
    L_0x00e1:
        r3.method(r2, r9);
        goto L_0x00b9;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.squareup.okhttp" +
                ".internal.http.HttpEngine.followUpRequest():com.squareup.okhttp.Request");
    }

    public boolean sameConnection(HttpUrl followUp) {
        HttpUrl url = this.userRequest.httpUrl();
        return url.host().equals(followUp.host()) && url.port() == followUp.port() && url.scheme
                ().equals(followUp.scheme());
    }

    private static Address createAddress(OkHttpClient client, Request request) {
        SSLSocketFactory sslSocketFactory = null;
        HostnameVerifier hostnameVerifier = null;
        CertificatePinner certificatePinner = null;
        if (request.isHttps()) {
            sslSocketFactory = client.getSslSocketFactory();
            hostnameVerifier = client.getHostnameVerifier();
            certificatePinner = client.getCertificatePinner();
        }
        return new Address(request.httpUrl().host(), request.httpUrl().port(), client.getDns(),
                client.getSocketFactory(), sslSocketFactory, hostnameVerifier, certificatePinner, client.getAuthenticator(), client.getProxy(), client.getProtocols(), client.getConnectionSpecs(), client.getProxySelector());
    }
}
