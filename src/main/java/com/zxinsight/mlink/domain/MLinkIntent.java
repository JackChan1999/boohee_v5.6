package com.zxinsight.mlink.domain;

import com.zxinsight.mlink.MLinkCallback;

import java.util.Map;

public class MLinkIntent {
    public MLinkCallback       callback;
    public Map<String, String> extraData;
    public String              k;

    public MLinkIntent(String str, MLinkCallback mLinkCallback) {
        this.k = str;
        this.callback = mLinkCallback;
    }
}
