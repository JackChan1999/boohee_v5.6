package com.boohee.one.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import com.boohee.main.GestureActivity;
import com.boohee.more.DescriptionActivity;
import com.boohee.one.R;
import com.boohee.one.http.client.BooheeClient;
import com.boohee.utils.AppUtils;

public class AboutBooheeActivity extends GestureActivity {
    @InjectView(2131427410)
    TextView tvApp;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a5);
        ButterKnife.inject((Activity) this);
        initView();
    }

    private void initView() {
        try {
            this.tvApp.setText("薄荷 v" + this.activity.getPackageManager().getPackageInfo(this
                    .activity.getPackageName(), 16384).versionName);
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    @OnClick({2131427412, 2131427413, 2131427415, 2131427414, 2131427411, 2131427417, 2131427416})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.clv_job:
                BrowserActivity.comeOnBaby(this.activity, "加入薄荷", "http://shop.boohee" +
                        ".com/store/pages/zhaopin");
                return;
            case R.id.clv_business:
                startActivity(new Intent(this.activity, ContactUsActivity.class));
                return;
            case R.id.clv_article:
                BrowserActivity.comeOnBaby(this.activity, getString(R.string.abp), BooheeClient
                        .build(BooheeClient.ONE).getDefaultURL("/api/v1/articles/partner_rules" +
                                ".html"));
                return;
            case R.id.clv_shop:
                BrowserActivity.comeOnBaby(this.activity, "关于商店", "http://shop.boohee" +
                        ".com/store/pages/about_shop");
                return;
            case R.id.clv_boohee:
                Intent aboutIntent = new Intent(this.activity, DescriptionActivity.class);
                aboutIntent.putExtra("index", 1);
                startActivity(aboutIntent);
                return;
            case R.id.clv_top_model:
                AppUtils.launchApp(this, AppUtils.MODEL_PACKAGE_NAME);
                return;
            case R.id.clv_food_library:
                AppUtils.launchApp(this, AppUtils.FOOD_PACKAGE_NAME);
                return;
            default:
                return;
        }
    }

    public static void comeOnBaby(Context context) {
        if (context != null) {
            context.startActivity(new Intent(context, AboutBooheeActivity.class));
        }
    }
}
