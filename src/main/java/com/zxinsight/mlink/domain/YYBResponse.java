package com.zxinsight.mlink.domain;

import com.zxinsight.common.b.a;

public class YYBResponse extends a {
    private MLinkData data;

    public class MLinkData {
        public String dp;
        public String k;
    }

    public MLinkData getData() {
        return this.data != null ? this.data : new MLinkData();
    }

    public void setData(MLinkData mLinkData) {
        this.data = mLinkData;
    }
}
