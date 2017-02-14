package com.alipay.security.mobile.module.a;

import android.content.Context;
import com.alipay.android.phone.mrpc.core.aa;
import com.alipay.android.phone.mrpc.core.h;
import com.alipay.android.phone.mrpc.core.w;
import com.alipay.security.mobile.module.a.a.a;
import com.alipay.security.mobile.module.commonutils.CommonUtils;
import com.alipay.security.mobile.module.commonutils.LOG;
import com.alipay.tscenter.biz.rpc.deviceFp.BugTrackMessageService;
import com.alipay.tscenter.biz.rpc.vkeydfp.AppListCmdService;
import com.alipay.tscenter.biz.rpc.vkeydfp.DeviceDataReportService;
import com.alipay.tscenter.biz.rpc.vkeydfp.request.AppListCmdRequest;
import com.alipay.tscenter.biz.rpc.vkeydfp.request.DeviceDataReportRequest;
import com.alipay.tscenter.biz.rpc.vkeydfp.result.AppListResult;
import com.alipay.tscenter.biz.rpc.vkeydfp.result.DeviceDataReportResult;
import org.json.JSONObject;

public final class b implements a {
    private static b f = null;
    private static DeviceDataReportResult g;
    private Context a = null;
    private w b = null;
    private BugTrackMessageService c = null;
    private DeviceDataReportService d = null;
    private AppListCmdService e = null;

    private b(Context context) {
        this.a = context;
        try {
            aa aaVar = new aa();
            aaVar.a(a.a());
            this.b = new h(context);
            this.c = (BugTrackMessageService) this.b.a(BugTrackMessageService.class, aaVar);
            this.d = (DeviceDataReportService) this.b.a(DeviceDataReportService.class, aaVar);
            this.e = (AppListCmdService) this.b.a(AppListCmdService.class, aaVar);
        } catch (Throwable e) {
            LOG.logException(e);
        }
    }

    public static synchronized b a(Context context) {
        b bVar;
        synchronized (b.class) {
            if (f == null) {
                f = new b(context);
            }
            bVar = f;
        }
        return bVar;
    }

    public final AppListResult a(String str, String str2, String str3, String str4) {
        AppListResult appListResult = null;
        try {
            AppListCmdRequest appListCmdRequest = new AppListCmdRequest();
            appListCmdRequest.os = str;
            appListCmdRequest.apdid = str4;
            appListCmdRequest.userId = str2;
            appListCmdRequest.token = str3;
            appListResult = this.e.getAppListCmd(appListCmdRequest);
        } catch (Exception e) {
        }
        return appListResult;
    }

    public final DeviceDataReportResult a(DeviceDataReportRequest deviceDataReportRequest) {
        if (this.d != null) {
            try {
                g = null;
                new Thread(new c(this, deviceDataReportRequest)).start();
                int i = 300000;
                while (g == null && i >= 0) {
                    Thread.sleep(50);
                    i -= 50;
                }
            } catch (Throwable e) {
                LOG.logException(e);
            }
        }
        return g;
    }

    public final boolean a(String str) {
        if (CommonUtils.isBlank(str)) {
            return false;
        }
        boolean booleanValue;
        if (this.c != null) {
            String str2 = null;
            try {
                str2 = this.c.logCollect(CommonUtils.textCompress(str));
            } catch (Exception e) {
            }
            if (!CommonUtils.isBlank(str2)) {
                try {
                    booleanValue = ((Boolean) new JSONObject(str2).get("success")).booleanValue();
                } catch (Throwable e2) {
                    LOG.logException(e2);
                }
                return booleanValue;
            }
        }
        booleanValue = false;
        return booleanValue;
    }
}
