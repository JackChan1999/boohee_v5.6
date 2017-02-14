package com.squareup.okhttp.internal;

import com.squareup.okhttp.internal.DiskLruCache.Editor;

import java.io.IOException;

import okio.Sink;

class DiskLruCache$Editor$1 extends FaultHidingSink {
    final /* synthetic */ Editor this$1;

    DiskLruCache$Editor$1(Editor this$1, Sink delegate) {
        this.this$1 = this$1;
        super(delegate);
    }

    protected void onException(IOException e) {
        synchronized (this.this$1.this$0) {
            Editor.access$1902(this.this$1, true);
        }
    }
}
