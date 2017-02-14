package com.alipay.security.mobile.module.a;

import com.alipay.security.mobile.module.commonutils.Dbg;
import com.alipay.security.mobile.module.commonutils.LOG;
import com.alipay.tscenter.biz.rpc.vkeydfp.request.DeviceDataReportRequest;
import com.alipay.tscenter.biz.rpc.vkeydfp.result.DeviceDataReportResult;

final class c implements Runnable {
    final /* synthetic */ DeviceDataReportRequest a;
    final /* synthetic */ b b;

    c(b bVar, DeviceDataReportRequest deviceDataReportRequest) {
        this.b = bVar;
        this.a = deviceDataReportRequest;
    }

    public final void run() {
        try {
            b.g = this.b.d.reportStaticData(this.a);
            Dbg.log("Rpc success.");
        } catch (Throwable th) {
            b.g = new DeviceDataReportResult();
            b.g.success = false;
            b.g.resultCode = "static data rpc upload error, " + LOG.getStackString(th);
            Dbg.log("Rpc failed.");
            Dbg.log(LOG.getStackString(th));
        }
    }
}
