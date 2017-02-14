package com.umeng.socialize.weixin.controller;

import android.text.TextUtils;

import com.boohee.utils.Utils;
import com.umeng.socialize.common.UMAsyncTask;
import com.umeng.socialize.controller.listener.SocializeListeners.UMDataListener;
import com.umeng.socialize.weixin.net.WXAuthUtils;

import java.util.Map;

class UMWXHandler$5 extends UMAsyncTask<Map<String, Object>> {
    final /* synthetic */ UMWXHandler    this$0;
    final /* synthetic */ StringBuilder  val$builder;
    final /* synthetic */ UMDataListener val$listener;

    UMWXHandler$5(UMWXHandler this$0, StringBuilder stringBuilder, UMDataListener uMDataListener) {
        this.this$0 = this$0;
        this.val$builder = stringBuilder;
        this.val$listener = uMDataListener;
    }

    protected Map<String, Object> doInBackground() {
        return UMWXHandler.access$3300(this.this$0, WXAuthUtils.request(this.val$builder.toString
                ()));
    }

    protected void onPostExecute(Map<String, Object> result) {
        super.onPostExecute(result);
        int code = 200;
        Object errcode = result.get(Utils.RESPONSE_ERRCODE);
        if (!(errcode == null || TextUtils.isEmpty(errcode.toString()))) {
            code = Integer.parseInt(errcode.toString());
        }
        this.val$listener.onComplete(code, result);
    }
}
