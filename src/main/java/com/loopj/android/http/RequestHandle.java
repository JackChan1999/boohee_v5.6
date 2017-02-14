package com.loopj.android.http;

import android.os.Looper;

import java.lang.ref.WeakReference;

public class RequestHandle {
    private final WeakReference<AsyncHttpRequest> request;

    public RequestHandle(AsyncHttpRequest request) {
        this.request = new WeakReference(request);
    }

    public boolean cancel(final boolean mayInterruptIfRunning) {
        final AsyncHttpRequest _request = (AsyncHttpRequest) this.request.get();
        if (_request != null) {
            if (Looper.myLooper() == Looper.getMainLooper()) {
                new Thread(new Runnable() {
                    public void run() {
                        _request.cancel(mayInterruptIfRunning);
                    }
                }).start();
            } else {
                _request.cancel(mayInterruptIfRunning);
            }
        }
        return false;
    }

    public boolean isFinished() {
        AsyncHttpRequest _request = (AsyncHttpRequest) this.request.get();
        return _request == null || _request.isDone();
    }

    public boolean isCancelled() {
        AsyncHttpRequest _request = (AsyncHttpRequest) this.request.get();
        return _request == null || _request.isCancelled();
    }

    public boolean shouldBeGarbageCollected() {
        boolean should = isCancelled() || isFinished();
        if (should) {
            this.request.clear();
        }
        return should;
    }
}
