package com.zxinsight.common.base;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;

import com.zxinsight.WebViewActivity;
import com.zxinsight.common.util.c;
import com.zxinsight.common.util.l;
import com.zxinsight.common.util.m;
import com.zxinsight.common.util.o;
import com.zxinsight.share.activity.MWWXEntryActivity;

public class MWActivity extends Activity {
    public static final String ACTIVITY_TYPE_WEBVIEW    = "WebViewActivity";
    public static final String ACTIVITY_TYPE_WX         = "MWWXEntryActivity";
    public static final String INTENT_KEY_ACTIVITY_TYPE = "ACTIVITY_TYPE";
    private a a;

    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (m.a().v()) {
            setRequestedOrientation(1);
        }
        Object obj = "";
        Intent intent = getIntent();
        if (intent != null) {
            if (intent.getData() != null) {
                obj = ACTIVITY_TYPE_WEBVIEW;
            } else {
                obj = intent.getStringExtra(INTENT_KEY_ACTIVITY_TYPE);
            }
        }
        if (l.a(obj)) {
            c.b("Activity launched with no type.");
            finish();
        } else if (ACTIVITY_TYPE_WEBVIEW.equals(obj)) {
            this.a = new WebViewActivity(this);
            this.a.onCreate();
        } else if (ACTIVITY_TYPE_WX.equals(obj)) {
            requestWindowFeature(1);
            this.a = new MWWXEntryActivity(this);
            this.a.onCreate();
        }
    }

    protected void onRestart() {
        if (this.a != null) {
            this.a.onRestart();
        }
        super.onRestart();
    }

    protected void onPause() {
        if (this.a != null) {
            this.a.onPause();
        }
        super.onPause();
    }

    protected void onResume() {
        if (this.a != null) {
            this.a.onResume();
        }
        super.onResume();
    }

    protected void onDestroy() {
        if (this.a != null) {
            this.a.onDestroy();
        }
        o.d();
        super.onDestroy();
    }

    public void onBackPressed() {
        if (this.a != null) {
            this.a.onBackPressed();
        }
        super.onBackPressed();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        return false;
    }

    protected void onActivityResult(int i, int i2, Intent intent) {
        if (this.a != null) {
            this.a.onActivityResult(i, i2, intent);
        }
        super.onActivityResult(i, i2, intent);
    }

    protected void onNewIntent(Intent intent) {
        if (this.a != null) {
            this.a.onNewIntent(intent);
        }
        super.onNewIntent(intent);
    }
}
