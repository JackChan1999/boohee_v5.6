package com.zxinsight.analytics.domain.response;

import com.zxinsight.common.b.a;

public class ServiceConfigResponse extends a {
    private ServiceConfig data;

    public ServiceConfig getData() {
        return this.data != null ? this.data : new ServiceConfig();
    }

    public void setData(ServiceConfig serviceConfig) {
        this.data = serviceConfig;
    }
}
