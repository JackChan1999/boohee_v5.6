package com.zxinsight.share.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.zxinsight.common.util.c;
import com.zxinsight.common.util.m;
import com.zxinsight.common.util.o;
import com.zxinsight.share.b.a;
import com.zxinsight.share.domain.BMPlatform;
import com.zxinsight.share.e;

public class MWWXHandlerActivity extends Activity implements IWXAPIEventHandler {
    private static a          listener;
    private        IWXAPI     mIWXAPI;
    private        BMPlatform platform;

    public static a getListener() {
        return listener;
    }

    public static void setListener(a aVar) {
        listener = aVar;
    }

    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        String r = m.a().r();
        c.a("MWWXEntryActivity is right， WeChatAppId = " + r);
        this.mIWXAPI = WXAPIFactory.createWXAPI(this, r, false);
        this.mIWXAPI.registerApp(r);
        if (this.mIWXAPI.isWXAppInstalled() && this.mIWXAPI.isWXAppSupportAPI()) {
            this.mIWXAPI.handleIntent(getIntent(), this);
            return;
        }
        c.d(o.a("未安装微信或者微信版本过低。", "There is no WeChat or the version is too low."));
        finish();
    }

    public void onNewIntent(Intent intent) {
        handIntent(intent);
        super.onNewIntent(intent);
    }

    public void onActivityResult(int i, int i2, Intent intent) {
    }

    private void handIntent(Intent intent) {
        this.mIWXAPI.handleIntent(intent, this);
    }

    protected String buildTransaction(String str) {
        return str == null ? String.valueOf(System.currentTimeMillis()) : str;
    }

    public void onReq(BaseReq baseReq) {
        finish();
    }

    public void onRestart() {
        super.onRestart();
        o.d();
        finish();
    }

    protected void onDestroy() {
        super.onDestroy();
        listener = null;
    }

    public void onResp(BaseResp baseResp) {
        e eVar;
        switch (baseResp.errCode) {
            case -3:
                if (listener != null) {
                    eVar = new e();
                    eVar.a(baseResp.errStr);
                    listener.a(eVar);
                    break;
                }
                break;
            case -2:
                if (listener != null) {
                    listener.a();
                    break;
                }
                break;
            case -1:
                if (listener != null) {
                    eVar = new e();
                    eVar.a(baseResp.errStr);
                    listener.a(eVar);
                    break;
                }
                break;
            case 0:
                if (this.platform != BMPlatform.PLATFORM_WXTIMELINE) {
                    if (listener != null) {
                        new e().a(baseResp.errStr);
                        listener.a(BMPlatform.getPlatformName(BMPlatform.PLATFORM_WXSESSION));
                        break;
                    }
                } else if (listener != null) {
                    new e().a(baseResp.errStr);
                    listener.a(BMPlatform.getPlatformName(BMPlatform.PLATFORM_WXTIMELINE));
                    break;
                }
                break;
        }
        finish();
    }
}
