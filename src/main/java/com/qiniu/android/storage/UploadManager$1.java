package com.qiniu.android.storage;

import com.qiniu.android.http.ResponseInfo;

class UploadManager$1 implements Runnable {
    final /* synthetic */ UpCompletionHandler val$completionHandler;
    final /* synthetic */ ResponseInfo        val$info2;
    final /* synthetic */ String              val$key;

    UploadManager$1(UpCompletionHandler upCompletionHandler, String str, ResponseInfo
            responseInfo) {
        this.val$completionHandler = upCompletionHandler;
        this.val$key = str;
        this.val$info2 = responseInfo;
    }

    public void run() {
        this.val$completionHandler.complete(this.val$key, this.val$info2, null);
    }
}
