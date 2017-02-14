package com.squareup.okhttp;

import com.squareup.okhttp.Cache.CacheResponseBody;
import com.squareup.okhttp.internal.DiskLruCache.Snapshot;

import java.io.IOException;

import okio.ForwardingSource;
import okio.Source;

class Cache$CacheResponseBody$1 extends ForwardingSource {
    final /* synthetic */ CacheResponseBody this$0;
    final /* synthetic */ Snapshot          val$snapshot;

    Cache$CacheResponseBody$1(CacheResponseBody this$0, Source x0, Snapshot snapshot) {
        this.this$0 = this$0;
        this.val$snapshot = snapshot;
        super(x0);
    }

    public void close() throws IOException {
        this.val$snapshot.close();
        super.close();
    }
}
