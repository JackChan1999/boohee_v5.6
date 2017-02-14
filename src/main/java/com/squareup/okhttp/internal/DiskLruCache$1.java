package com.squareup.okhttp.internal;

import java.io.IOException;

class DiskLruCache$1 implements Runnable {
    final /* synthetic */ DiskLruCache this$0;

    DiskLruCache$1(DiskLruCache this$0) {
        this.this$0 = this$0;
    }

    public void run() {
        int i = 0;
        synchronized (this.this$0) {
            if (!DiskLruCache.access$000(this.this$0)) {
                i = 1;
            }
            if ((i | DiskLruCache.access$100(this.this$0)) != 0) {
                return;
            }
            try {
                DiskLruCache.access$200(this.this$0);
                if (DiskLruCache.access$300(this.this$0)) {
                    DiskLruCache.access$400(this.this$0);
                    DiskLruCache.access$502(this.this$0, 0);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
