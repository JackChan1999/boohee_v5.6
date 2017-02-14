package com.boohee.uchoice;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.webkit.WebView;

import com.boohee.api.ApiUrls;
import com.boohee.database.UserPreference;
import com.boohee.main.GestureActivity;
import com.boohee.one.R;
import com.boohee.one.event.OrderEvent;

import de.greenrobot.event.EventBus;

public class PaySuccessActivity extends GestureActivity {
    private static final String KEY_URL = "key_url";
    private String  mUrl;
    private WebView webView;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.e7);
        setTitle(R.string.z2);
        handleUrl(getIntent());
        initView();
    }

    private void initView() {
        this.webView = (WebView) findViewById(R.id.wv_content);
        this.webView.getSettings().setDefaultTextEncodingName("UTF-8");
        if (TextUtils.isEmpty(this.mUrl)) {
            this.webView.loadUrl(ApiUrls.URL_PAY_SUCCESS);
        } else {
            this.webView.loadUrl(this.mUrl);
        }
    }

    private void handleUrl(Intent intent) {
        if (intent != null) {
            this.mUrl = intent.getStringExtra(KEY_URL);
            String token = UserPreference.getToken(this.ctx);
            String userKey = UserPreference.getUserKey(this.ctx);
            this.mUrl = String.format("%s?token=%s&user_key=%s", new Object[]{this.mUrl, token,
                    userKey});
        }
    }

    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this.ctx, OrderListActivity.class);
        intent.putExtra(OrderListActivity.EXTRA_INDEX, 1);
        startActivity(intent);
        EventBus.getDefault().post(new OrderEvent());
    }

    public static void comeOnBaby(Context context, String url) {
        if (context != null) {
            Intent intent = new Intent(context, PaySuccessActivity.class);
            if (!TextUtils.isEmpty(url)) {
                intent.putExtra(KEY_URL, url);
            }
            context.startActivity(intent);
        }
    }
}
