package com.zxinsight.mlink;

import android.content.Context;
import android.net.Uri;

import java.util.Map;

final class c implements MLinkCallback {
    final /* synthetic */ Class a;

    c(Class cls) {
        this.a = cls;
    }

    public void execute(Map<String, String> map, Uri uri, Context context) {
        MLinkIntentBuilder.buildIntent(map, context, this.a);
    }
}
