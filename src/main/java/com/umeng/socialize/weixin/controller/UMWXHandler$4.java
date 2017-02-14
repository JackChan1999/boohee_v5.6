package com.umeng.socialize.weixin.controller;

import android.os.Bundle;

import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.common.UMAsyncTask;
import com.umeng.socialize.weixin.net.WXAuthUtils;

class UMWXHandler$4 extends UMAsyncTask<Bundle> {
    final /* synthetic */ UMWXHandler this$0;
    final /* synthetic */ String      val$url;

    UMWXHandler$4(UMWXHandler this$0, String str) {
        this.this$0 = this$0;
        this.val$url = str;
    }

    protected Bundle doInBackground() {
        return UMWXHandler.access$3100(this.this$0, WXAuthUtils.request(this.val$url));
    }

    protected void onPostExecute(Bundle result) {
        super.onPostExecute(result);
        UMWXHandler.access$3200(this.this$0).onComplete(result, UMWXHandler.access$3000(this
                .this$0) ? SHARE_MEDIA.WEIXIN_CIRCLE : SHARE_MEDIA.WEIXIN);
    }
}
