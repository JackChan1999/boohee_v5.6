package com.squareup.okhttp.internal;

import com.squareup.okhttp.internal.DiskLruCache.Snapshot;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException;

class DiskLruCache$3 implements Iterator<Snapshot> {
    final Iterator<DiskLruCache$Entry> delegate = new ArrayList(DiskLruCache.access$2000(this
            .this$0).values()).iterator();
    Snapshot nextSnapshot;
    Snapshot removeSnapshot;
    final /* synthetic */ DiskLruCache this$0;

    DiskLruCache$3(DiskLruCache this$0) {
        this.this$0 = this$0;
    }

    public boolean hasNext() {
        if (this.nextSnapshot != null) {
            return true;
        }
        synchronized (this.this$0) {
            if (DiskLruCache.access$100(this.this$0)) {
                return false;
            }
            while (this.delegate.hasNext()) {
                Snapshot snapshot = ((DiskLruCache$Entry) this.delegate.next()).snapshot();
                if (snapshot != null) {
                    this.nextSnapshot = snapshot;
                    return true;
                }
            }
            return false;
        }
    }

    public Snapshot next() {
        if (hasNext()) {
            this.removeSnapshot = this.nextSnapshot;
            this.nextSnapshot = null;
            return this.removeSnapshot;
        }
        throw new NoSuchElementException();
    }

    public void remove() {
        if (this.removeSnapshot == null) {
            throw new IllegalStateException("remove() before next()");
        }
        try {
            this.this$0.remove(Snapshot.access$2100(this.removeSnapshot));
        } catch (IOException e) {
        } finally {
            this.removeSnapshot = null;
        }
    }
}
