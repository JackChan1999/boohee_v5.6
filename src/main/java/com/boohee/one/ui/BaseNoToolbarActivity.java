package com.boohee.one.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager.LayoutParams;

import com.boohee.one.MyApplication;
import com.boohee.one.http.RequestManager;
import com.boohee.utils.ViewFinder;
import com.nostra13.universalimageloader.core.ImageLoader;

public class BaseNoToolbarActivity extends AppCompatActivity {
    protected Context    ctx;
    protected ViewFinder finder;
    protected ImageLoader imageLoader = ImageLoader.getInstance();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.ctx = this;
        this.finder = new ViewFinder((Activity) this);
        if (VERSION.SDK_INT >= 19) {
            LayoutParams localLayoutParams = getWindow().getAttributes();
            localLayoutParams.flags = 67108864 | localLayoutParams.flags;
        }
    }

    public void onBackPressed() {
        if (MyApplication.getIsMainOpened()) {
            super.onBackPressed();
            return;
        }
        startActivity(new Intent(this.ctx, MainActivity.class));
        finish();
    }

    protected void onDestroy() {
        super.onDestroy();
        RequestManager.cancelAll(this);
    }
}
