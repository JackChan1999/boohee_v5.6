package com.zxinsight.analytics.domain.response;

import com.zxinsight.common.b.a;

import java.util.ArrayList;
import java.util.List;

public class MarketingResponse extends a {
    private List<Marketing> data = new ArrayList();

    public List<Marketing> getData() {
        return this.data != null ? this.data : new ArrayList();
    }

    public void setData(List<Marketing> list) {
        this.data = list;
    }
}
