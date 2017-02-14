package com.boohee.one.wxapi;

import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;

import com.boohee.api.ApiUrls;
import com.boohee.main.GestureActivity;
import com.boohee.one.R;
import com.boohee.one.event.OrderEvent;
import com.boohee.uchoice.OrderListActivity;
import com.boohee.utility.ShareHelper;
import com.boohee.utils.Helper;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import de.greenrobot.event.EventBus;

public class WXPayEntryActivity extends GestureActivity implements IWXAPIEventHandler {
    private IWXAPI  api;
    private WebView webView;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.e7);
        setTitle(R.string.z2);
        this.api = WXAPIFactory.createWXAPI(this, ShareHelper.WX_APPID);
        this.api.handleIntent(getIntent(), this);
        initView();
    }

    private void initView() {
        this.webView = (WebView) findViewById(R.id.wv_content);
        this.webView.getSettings().setDefaultTextEncodingName("UTF-8");
        this.webView.loadUrl(ApiUrls.URL_PAY_SUCCESS);
    }

    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        this.api.handleIntent(intent, this);
    }

    public void onReq(BaseReq req) {
    }

    public void onResp(BaseResp resp) {
        if (resp.getType() == 5 && resp.errCode < 0) {
            Helper.showToast((int) R.string.z1);
            finish();
        }
    }

    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this.ctx, OrderListActivity.class);
        intent.putExtra(OrderListActivity.EXTRA_INDEX, 1);
        startActivity(intent);
        EventBus.getDefault().post(new OrderEvent());
    }
}
