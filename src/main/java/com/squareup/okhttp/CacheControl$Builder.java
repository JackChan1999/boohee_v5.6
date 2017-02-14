package com.squareup.okhttp;

import android.support.v7.widget.ActivityChooserView.ActivityChooserViewAdapter;

import java.util.concurrent.TimeUnit;

public final class CacheControl$Builder {
    int maxAgeSeconds   = -1;
    int maxStaleSeconds = -1;
    int minFreshSeconds = -1;
    boolean noCache;
    boolean noStore;
    boolean noTransform;
    boolean onlyIfCached;

    public CacheControl$Builder noCache() {
        this.noCache = true;
        return this;
    }

    public CacheControl$Builder noStore() {
        this.noStore = true;
        return this;
    }

    public CacheControl$Builder maxAge(int maxAge, TimeUnit timeUnit) {
        if (maxAge < 0) {
            throw new IllegalArgumentException("maxAge < 0: " + maxAge);
        }
        long maxAgeSecondsLong = timeUnit.toSeconds((long) maxAge);
        this.maxAgeSeconds = maxAgeSecondsLong > 2147483647L ? ActivityChooserViewAdapter
                .MAX_ACTIVITY_COUNT_UNLIMITED : (int) maxAgeSecondsLong;
        return this;
    }

    public CacheControl$Builder maxStale(int maxStale, TimeUnit timeUnit) {
        if (maxStale < 0) {
            throw new IllegalArgumentException("maxStale < 0: " + maxStale);
        }
        long maxStaleSecondsLong = timeUnit.toSeconds((long) maxStale);
        this.maxStaleSeconds = maxStaleSecondsLong > 2147483647L ? ActivityChooserViewAdapter
                .MAX_ACTIVITY_COUNT_UNLIMITED : (int) maxStaleSecondsLong;
        return this;
    }

    public CacheControl$Builder minFresh(int minFresh, TimeUnit timeUnit) {
        if (minFresh < 0) {
            throw new IllegalArgumentException("minFresh < 0: " + minFresh);
        }
        long minFreshSecondsLong = timeUnit.toSeconds((long) minFresh);
        this.minFreshSeconds = minFreshSecondsLong > 2147483647L ? ActivityChooserViewAdapter
                .MAX_ACTIVITY_COUNT_UNLIMITED : (int) minFreshSecondsLong;
        return this;
    }

    public CacheControl$Builder onlyIfCached() {
        this.onlyIfCached = true;
        return this;
    }

    public CacheControl$Builder noTransform() {
        this.noTransform = true;
        return this;
    }

    public CacheControl build() {
        return new CacheControl(this, null);
    }
}
