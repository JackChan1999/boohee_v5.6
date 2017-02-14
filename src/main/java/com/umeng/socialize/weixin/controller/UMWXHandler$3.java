package com.umeng.socialize.weixin.controller;

import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.SocializeConfig;
import com.umeng.socialize.bean.StatusCode;
import com.umeng.socialize.controller.listener.SocializeListeners.SnsPostListener;
import com.umeng.socialize.utils.Log;
import com.umeng.socialize.utils.SocializeUtils;

class UMWXHandler$3 implements IWXAPIEventHandler {
    final /* synthetic */ UMWXHandler this$0;

    UMWXHandler$3(UMWXHandler this$0) {
        this.this$0 = this$0;
    }

    public void onResp(BaseResp resp) {
        if (UMWXHandler.access$2600(this.this$0)) {
            UMWXHandler.access$2700(this.this$0, resp);
            return;
        }
        int statusCode = 0;
        SHARE_MEDIA platform = SocializeConfig.getSelectedPlatfrom();
        switch (resp.errCode) {
            case -4:
                statusCode = -4;
                Log.d("UMWXHandler", "### 微信发送被拒绝");
                break;
            case -3:
                statusCode = -3;
                break;
            case -2:
                statusCode = StatusCode.ST_CODE_ERROR_CANCEL;
                Log.d("UMWXHandler", "### 微信分享取消");
                break;
            case -1:
                statusCode = -1;
                break;
            case 0:
                statusCode = 200;
                Log.d("UMWXHandler", "### 微信分享成功.");
                break;
            default:
                Log.d("UMWXHandler", "### 微信发送 -- 未知错误.");
                break;
        }
        if (!UMWXHandler.access$2600(this.this$0)) {
            UMWXHandler.access$2800(this.this$0).fireAllListenersOnComplete(SnsPostListener
                    .class, platform, statusCode, UMWXHandler.access$1200());
            if (statusCode == 200 && UMWXHandler.access$1200() != null) {
                SocializeUtils.sendAnalytic(UMWXHandler.access$2900(this.this$0), UMWXHandler
                        .access$1200().mDescriptor, this.this$0.mShareContent, this.this$0
                        .mShareMedia, UMWXHandler.access$3000(this.this$0) ? "wxtimeline" :
                        "wxsession");
            }
        }
    }

    public void onReq(BaseReq req) {
    }
}
