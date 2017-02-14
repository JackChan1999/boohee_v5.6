package com.umeng.socialize.common;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;

import com.umeng.socialize.controller.impl.v;

public abstract class UMAsyncTask<Result> {
    private static final HandlerThread HT = new HandlerThread(v.class.getName(), 10);

    protected abstract Result doInBackground();

    static {
        HT.start();
    }

    protected void onPreExecute() {
    }

    protected void onPostExecute(Result result) {
    }

    public final UMAsyncTask<Result> execute() {
        final Handler handler = new Handler(Looper.getMainLooper());
        Handler handler2 = new Handler(HT.getLooper());
        onPreExecute();
        handler2.post(new Runnable() {
            public void run() {
                final Object doInBackground = UMAsyncTask.this.doInBackground();
                handler.post(new Runnable() {
                    public void run() {
                        UMAsyncTask.this.onPostExecute(doInBackground);
                    }
                });
            }
        });
        return this;
    }
}
