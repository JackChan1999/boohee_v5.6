package com.squareup.okhttp.internal.http;

import com.squareup.okhttp.CacheControl;
import com.squareup.okhttp.Headers;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Request.Builder;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.Response$Builder;
import com.umeng.analytics.a;

import java.util.Date;
import java.util.concurrent.TimeUnit;

public class CacheStrategy$Factory {
    private int ageSeconds = -1;
    final   Response cacheResponse;
    private String   etag;
    private Date     expires;
    private Date     lastModified;
    private String   lastModifiedString;
    final   long     nowMillis;
    private long     receivedResponseMillis;
    final   Request  request;
    private long     sentRequestMillis;
    private Date     servedDate;
    private String   servedDateString;

    public CacheStrategy$Factory(long nowMillis, Request request, Response cacheResponse) {
        this.nowMillis = nowMillis;
        this.request = request;
        this.cacheResponse = cacheResponse;
        if (cacheResponse != null) {
            Headers headers = cacheResponse.headers();
            int size = headers.size();
            for (int i = 0; i < size; i++) {
                String fieldName = headers.name(i);
                String value = headers.value(i);
                if ("Date".equalsIgnoreCase(fieldName)) {
                    this.servedDate = HttpDate.parse(value);
                    this.servedDateString = value;
                } else if ("Expires".equalsIgnoreCase(fieldName)) {
                    this.expires = HttpDate.parse(value);
                } else if ("Last-Modified".equalsIgnoreCase(fieldName)) {
                    this.lastModified = HttpDate.parse(value);
                    this.lastModifiedString = value;
                } else if ("ETag".equalsIgnoreCase(fieldName)) {
                    this.etag = value;
                } else if ("Age".equalsIgnoreCase(fieldName)) {
                    this.ageSeconds = HeaderParser.parseSeconds(value, -1);
                } else if (OkHeaders.SENT_MILLIS.equalsIgnoreCase(fieldName)) {
                    this.sentRequestMillis = Long.parseLong(value);
                } else if (OkHeaders.RECEIVED_MILLIS.equalsIgnoreCase(fieldName)) {
                    this.receivedResponseMillis = Long.parseLong(value);
                }
            }
        }
    }

    public CacheStrategy get() {
        CacheStrategy candidate = getCandidate();
        if (candidate.networkRequest == null || !this.request.cacheControl().onlyIfCached()) {
            return candidate;
        }
        return new CacheStrategy(null, null, null);
    }

    private CacheStrategy getCandidate() {
        if (this.cacheResponse == null) {
            return new CacheStrategy(this.request, null, null);
        }
        if (this.request.isHttps() && this.cacheResponse.handshake() == null) {
            return new CacheStrategy(this.request, null, null);
        }
        if (!CacheStrategy.isCacheable(this.cacheResponse, this.request)) {
            return new CacheStrategy(this.request, null, null);
        }
        CacheControl requestCaching = this.request.cacheControl();
        if (requestCaching.noCache() || hasConditions(this.request)) {
            return new CacheStrategy(this.request, null, null);
        }
        long ageMillis = cacheResponseAge();
        long freshMillis = computeFreshnessLifetime();
        if (requestCaching.maxAgeSeconds() != -1) {
            freshMillis = Math.min(freshMillis, TimeUnit.SECONDS.toMillis((long) requestCaching
                    .maxAgeSeconds()));
        }
        long minFreshMillis = 0;
        if (requestCaching.minFreshSeconds() != -1) {
            minFreshMillis = TimeUnit.SECONDS.toMillis((long) requestCaching.minFreshSeconds());
        }
        long maxStaleMillis = 0;
        CacheControl responseCaching = this.cacheResponse.cacheControl();
        if (!(responseCaching.mustRevalidate() || requestCaching.maxStaleSeconds() == -1)) {
            maxStaleMillis = TimeUnit.SECONDS.toMillis((long) requestCaching.maxStaleSeconds());
        }
        if (responseCaching.noCache() || ageMillis + minFreshMillis >= freshMillis +
                maxStaleMillis) {
            Builder conditionalRequestBuilder = this.request.newBuilder();
            if (this.etag != null) {
                conditionalRequestBuilder.header("If-None-Match", this.etag);
            } else if (this.lastModified != null) {
                conditionalRequestBuilder.header("If-Modified-Since", this.lastModifiedString);
            } else if (this.servedDate != null) {
                conditionalRequestBuilder.header("If-Modified-Since", this.servedDateString);
            }
            Request conditionalRequest = conditionalRequestBuilder.build();
            if (hasConditions(conditionalRequest)) {
                return new CacheStrategy(conditionalRequest, this.cacheResponse, null);
            }
            return new CacheStrategy(conditionalRequest, null, null);
        }
        Response$Builder builder = this.cacheResponse.newBuilder();
        if (ageMillis + minFreshMillis >= freshMillis) {
            builder.addHeader("Warning", "110 HttpURLConnection \"Response is stale\"");
        }
        if (ageMillis > a.h && isFreshnessLifetimeHeuristic()) {
            builder.addHeader("Warning", "113 HttpURLConnection \"Heuristic expiration\"");
        }
        return new CacheStrategy(null, builder.build(), null);
    }

    private long computeFreshnessLifetime() {
        CacheControl responseCaching = this.cacheResponse.cacheControl();
        if (responseCaching.maxAgeSeconds() != -1) {
            return TimeUnit.SECONDS.toMillis((long) responseCaching.maxAgeSeconds());
        }
        long delta;
        if (this.expires != null) {
            delta = this.expires.getTime() - (this.servedDate != null ? this.servedDate.getTime()
                    : this.receivedResponseMillis);
            if (delta <= 0) {
                delta = 0;
            }
            return delta;
        } else if (this.lastModified == null || this.cacheResponse.request().httpUrl().query() !=
                null) {
            return 0;
        } else {
            delta = (this.servedDate != null ? this.servedDate.getTime() : this
                    .sentRequestMillis) - this.lastModified.getTime();
            if (delta > 0) {
                return delta / 10;
            }
            return 0;
        }
    }

    private long cacheResponseAge() {
        long receivedAge;
        long apparentReceivedAge = 0;
        if (this.servedDate != null) {
            apparentReceivedAge = Math.max(0, this.receivedResponseMillis - this.servedDate
                    .getTime());
        }
        if (this.ageSeconds != -1) {
            receivedAge = Math.max(apparentReceivedAge, TimeUnit.SECONDS.toMillis((long) this
                    .ageSeconds));
        } else {
            receivedAge = apparentReceivedAge;
        }
        return (receivedAge + (this.receivedResponseMillis - this.sentRequestMillis)) + (this
                .nowMillis - this.receivedResponseMillis);
    }

    private boolean isFreshnessLifetimeHeuristic() {
        return this.cacheResponse.cacheControl().maxAgeSeconds() == -1 && this.expires == null;
    }

    private static boolean hasConditions(Request request) {
        return (request.header("If-Modified-Since") == null && request.header("If-None-Match") == null) ? false : true;
    }
}
