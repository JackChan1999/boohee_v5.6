package com.zxinsight.mlink;

import android.content.Context;
import android.net.Uri;

import java.util.Map;

public interface MLinkCallback {
    void execute(Map<String, String> map, Uri uri, Context context);
}
