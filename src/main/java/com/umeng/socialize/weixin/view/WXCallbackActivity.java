package com.umeng.socialize.weixin.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.umeng.socialize.bean.HandlerRequestCode;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.SocializeConfig;
import com.umeng.socialize.sso.UMSsoHandler;
import com.umeng.socialize.utils.Log;
import com.umeng.socialize.weixin.controller.UMWXHandler;

public abstract class WXCallbackActivity extends Activity implements IWXAPIEventHandler {
    private final String      TAG        = WXCallbackActivity.class.getSimpleName();
    protected     UMWXHandler mWxHandler = null;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(this.TAG, "### WXCallbackActivity   onCreate");
        initWXHandler();
        handleIntent(getIntent());
    }

    protected final void onNewIntent(Intent paramIntent) {
        Log.d(this.TAG, "### WXCallbackActivity   onNewIntent");
        super.onNewIntent(paramIntent);
        setIntent(paramIntent);
        initWXHandler();
        handleIntent(paramIntent);
    }

    protected void initWXHandler() {
        SocializeConfig socializeConfig = SocializeConfig.getSocializeConfig();
        int requestCode = 10086;
        if (SocializeConfig.getSelectedPlatfrom() == SHARE_MEDIA.WEIXIN_CIRCLE) {
            requestCode = HandlerRequestCode.WX_CIRCLE_REQUEST_CODE;
        }
        UMSsoHandler ssoHandler = socializeConfig.getSsoHandler(requestCode);
        if (ssoHandler instanceof UMWXHandler) {
            this.mWxHandler = (UMWXHandler) ssoHandler;
        }
    }

    protected void handleIntent(Intent intent) {
        Log.d(this.TAG, "### WXCallbackActivity   handleIntent()");
        IWXAPI wxApi = getWXApi();
        if (wxApi != null) {
            wxApi.handleIntent(getIntent(), this);
        } else {
            Log.e(this.TAG, "### WXCallbackActivity   wxApi == null ");
        }
    }

    protected IWXAPI getWXApi() {
        if (this.mWxHandler != null) {
            return this.mWxHandler.getWXApi();
        }
        return null;
    }

    public void onResp(BaseResp resp) {
        if (this.mWxHandler != null) {
            this.mWxHandler.getWXEventHandler().onResp(resp);
        }
        finish();
    }

    public void onReq(BaseReq req) {
        if (this.mWxHandler != null) {
            this.mWxHandler.getWXEventHandler().onReq(req);
        }
        finish();
    }
}
