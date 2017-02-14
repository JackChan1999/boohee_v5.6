package com.zxinsight.share.activity;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;

import com.zxinsight.common.base.MWActivity;
import com.zxinsight.common.util.c;

public class ActivityFactory {
    private Class<?> activity_clz = MWActivity.class;
    private Context context;
    private Intent  intent;

    public ActivityFactory(Context context, String str) {
        this.context = context;
        this.intent = new Intent(context, this.activity_clz);
        this.intent.putExtra(MWActivity.INTENT_KEY_ACTIVITY_TYPE, str);
    }

    public void startActivity() {
        try {
            this.context.startActivity(this.intent);
        } catch (ActivityNotFoundException e) {
            c.a(String.format("%s was not detected in AndroidManifest.xml!", new Object[]{this
                    .activity_clz.getName()}));
        }
    }

    public void toNativeBrowser(String str) {
        try {
            c.b("MW factory = " + str);
            this.intent.putExtra("mw_key", str);
            this.intent.addFlags(335544320);
            this.context.startActivity(this.intent);
        } catch (ActivityNotFoundException e) {
            c.a(String.format("%s was not detected in AndroidManifest.xml!", new Object[]{this
                    .activity_clz.getName()}));
        }
    }

    public Intent getIntent() {
        return this.intent;
    }
}
