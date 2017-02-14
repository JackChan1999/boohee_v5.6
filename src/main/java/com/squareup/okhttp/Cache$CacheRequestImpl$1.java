package com.squareup.okhttp;

import com.squareup.okhttp.Cache.CacheRequestImpl;
import com.squareup.okhttp.internal.DiskLruCache.Editor;

import java.io.IOException;

import okio.ForwardingSink;
import okio.Sink;

class Cache$CacheRequestImpl$1 extends ForwardingSink {
    final /* synthetic */ CacheRequestImpl this$1;
    final /* synthetic */ Editor           val$editor;
    final /* synthetic */ Cache            val$this$0;

    Cache$CacheRequestImpl$1(CacheRequestImpl this$1, Sink x0, Cache cache, Editor editor) {
        this.this$1 = this$1;
        this.val$this$0 = cache;
        this.val$editor = editor;
        super(x0);
    }

    public void close() throws IOException {
        synchronized (this.this$1.this$0) {
            if (CacheRequestImpl.access$700(this.this$1)) {
                return;
            }
            CacheRequestImpl.access$702(this.this$1, true);
            Cache.access$808(this.this$1.this$0);
            super.close();
            this.val$editor.commit();
        }
    }
}
