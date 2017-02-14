package com.qiniu.android.storage;

class UploadManager$3 implements Runnable {
    final /* synthetic */ UploadManager       this$0;
    final /* synthetic */ UpCompletionHandler val$completionHandler;
    final /* synthetic */ byte[]              val$data;
    final /* synthetic */ UpToken             val$decodedToken;
    final /* synthetic */ String              val$key;
    final /* synthetic */ UploadOptions       val$options;

    UploadManager$3(UploadManager this$0, byte[] bArr, String str, UpToken upToken,
                    UpCompletionHandler upCompletionHandler, UploadOptions uploadOptions) {
        this.this$0 = this$0;
        this.val$data = bArr;
        this.val$key = str;
        this.val$decodedToken = upToken;
        this.val$completionHandler = upCompletionHandler;
        this.val$options = uploadOptions;
    }

    public void run() {
        FormUploader.upload(UploadManager.access$000(this.this$0), UploadManager.access$100(this
                .this$0), this.val$data, this.val$key, this.val$decodedToken, this.val$completionHandler, this.val$options);
    }
}
