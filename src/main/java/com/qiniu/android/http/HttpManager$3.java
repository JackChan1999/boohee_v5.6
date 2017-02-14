package com.qiniu.android.http;

import org.json.JSONObject;

class HttpManager$3 implements CompletionHandler {
    final /* synthetic */ HttpManager       this$0;
    final /* synthetic */ CompletionHandler val$completionHandler;

    HttpManager$3(HttpManager this$0, CompletionHandler completionHandler) {
        this.this$0 = this$0;
        this.val$completionHandler = completionHandler;
    }

    public void complete(ResponseInfo info, JSONObject response) {
        this.val$completionHandler.complete(info, response);
        if (info.isOK()) {
            HttpManager.access$100(this.this$0).updateSpeedInfo(info);
        } else {
            HttpManager.access$100(this.this$0).updateErrorInfo(info);
        }
    }
}
