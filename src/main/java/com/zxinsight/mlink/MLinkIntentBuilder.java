package com.zxinsight.mlink;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.zxinsight.common.util.l;

import java.util.Map;
import java.util.Map.Entry;

public class MLinkIntentBuilder {
    public static void buildIntent(Context context, Uri uri) {
        Intent intent = new Intent("android.intent.action.VIEW", uri);
        intent.addFlags(335544320);
        context.startActivity(intent);
    }

    public static void buildIntent(Context context, Class cls) {
        buildIntent(null, context, cls);
    }

    public static void buildIntent(Map<String, String> map, Context context, Class cls) {
        if (context == null) {
            throw new IllegalArgumentException("context must not be null");
        } else if (!context.getClass().equals(cls)) {
            Intent intent = new Intent(context, cls);
            intent.addFlags(335544320);
            if (l.b(map)) {
                for (Entry entry : map.entrySet()) {
                    intent.putExtra((String) entry.getKey(), (String) entry.getValue());
                }
            }
            context.startActivity(intent);
        }
    }
}
