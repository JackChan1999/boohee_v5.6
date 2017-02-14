package com.qiniu.android.storage;

import com.qiniu.android.http.ResponseInfo;

class UploadManager$2 implements Runnable {
    final /* synthetic */ UploadManager       this$0;
    final /* synthetic */ UpCompletionHandler val$completionHandler;
    final /* synthetic */ ResponseInfo        val$info;
    final /* synthetic */ String              val$key;

    UploadManager$2(UploadManager this$0, UpCompletionHandler upCompletionHandler, String str,
                    ResponseInfo responseInfo) {
        this.this$0 = this$0;
        this.val$completionHandler = upCompletionHandler;
        this.val$key = str;
        this.val$info = responseInfo;
    }

    public void run() {
        this.val$completionHandler.complete(this.val$key, this.val$info, null);
    }
}
