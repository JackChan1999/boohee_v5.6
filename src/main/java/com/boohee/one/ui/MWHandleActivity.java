package com.boohee.one.ui;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.alipay.sdk.sys.a;
import com.boohee.utility.BooheeScheme;
import com.boohee.utils.Helper;
import com.zxinsight.MWConfiguration;
import com.zxinsight.MagicWindowSDK;
import com.zxinsight.TrackAgent;
import com.zxinsight.mlink.MLinkCallback;

import java.util.Map;

public class MWHandleActivity extends BaseActivity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TextView tvContent = new TextView(this);
        tvContent.setText("MagicWindow");
        tvContent.setTextSize(86.0f);
        setContentView((View) tvContent);
        initMagicWindow();
        handleIntent();
        TrackAgent.currentEvent().customEvent("Handle_MLink");
    }

    protected void onNewIntent(Intent intent) {
        handleIntent();
    }

    private void handleIntent() {
        if (MagicWindowSDK.getMLink() != null) {
            MagicWindowSDK.getMLink().router(getIntent().getData());
        }
        finish();
    }

    private void initMagicWindow() {
        MWConfiguration config = new MWConfiguration(this);
        config.setDebugModel(false);
        MagicWindowSDK.initSDK(config);
        MagicWindowSDK.getMLink().registerDefault(new MLinkCallback() {
            public void execute(Map paramMap, Uri uri, Context context) {
                try {
                    String channel = MagicWindowSDK.getMLink().getLastChannelForMLink();
                    Helper.showLog(String.format("MW Channel Register: %s", new Object[]{channel}));
                    if (uri != null) {
                        Helper.showLog(String.format("MW Scheme: %s ", new Object[]{uri.toString
                                ()}));
                        String url = uri.toString();
                        if (url.endsWith("?") || url.endsWith(a.b) || url.endsWith("/")) {
                            url = url.substring(0, url.length() - 1);
                        }
                        Context ctx = MWHandleActivity.this;
                        if (!BooheeScheme.handleUrl(ctx, url)) {
                            Intent intent = new Intent(ctx, MainActivity.class);
                            intent.addFlags(268435456);
                            ctx.startActivity(intent);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
