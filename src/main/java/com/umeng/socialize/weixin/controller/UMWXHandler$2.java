package com.umeng.socialize.weixin.controller;

import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

class UMWXHandler$2 extends Handler {
    final /* synthetic */ UMWXHandler this$0;

    UMWXHandler$2(UMWXHandler this$0) {
        this.this$0 = this$0;
    }

    public void handleMessage(Message msg) {
        if (msg.what == 1 && UMWXHandler.access$2200(this.this$0)) {
            Toast.makeText(UMWXHandler.access$2300(this.this$0), "图片大小超过32KB，正在对图片进行压缩...", 0)
                    .show();
        } else if (msg.what == 2) {
            Toast.makeText(UMWXHandler.access$2400(this.this$0), "标题长度超过512 Bytes...", 0).show();
        }
    }
}
