package com.loopj.android.http;

import java.util.List;

class AsyncHttpClient$4 implements Runnable {
    final /* synthetic */ AsyncHttpClient this$0;
    final /* synthetic */ boolean         val$mayInterruptIfRunning;
    final /* synthetic */ List            val$requestList;

    AsyncHttpClient$4(AsyncHttpClient this$0, List list, boolean z) {
        this.this$0 = this$0;
        this.val$requestList = list;
        this.val$mayInterruptIfRunning = z;
    }

    public void run() {
        AsyncHttpClient.access$100(this.this$0, this.val$requestList, this.val$mayInterruptIfRunning);
    }
}
