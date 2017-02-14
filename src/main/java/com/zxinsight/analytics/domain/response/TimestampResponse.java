package com.zxinsight.analytics.domain.response;

import com.zxinsight.common.b.a;

public class TimestampResponse extends a {
    private Timestamp data;

    public Timestamp getData() {
        return this.data != null ? this.data : new Timestamp();
    }

    public void setData(Timestamp timestamp) {
        this.data = timestamp;
    }
}
