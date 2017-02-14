package com.zxinsight.analytics.domain;

import com.zxinsight.MWConfiguration;
import com.zxinsight.common.util.DeviceInfoUtils;

public class Device {
    public String b   = DeviceInfoUtils.h();
    public String c   = DeviceInfoUtils.g(MWConfiguration.getContext());
    public String d   = DeviceInfoUtils.c(MWConfiguration.getContext());
    public String fa  = DeviceInfoUtils.c();
    public String fp  = DeviceInfoUtils.a();
    public String m   = DeviceInfoUtils.j();
    public String mac = DeviceInfoUtils.h(MWConfiguration.getContext());
    public String mf  = DeviceInfoUtils.i();
    public String n   = DeviceInfoUtils.e();
    public String os  = DeviceInfoUtils.b();
    public String osv = DeviceInfoUtils.g();
    public String sr  = DeviceInfoUtils.d(MWConfiguration.getContext());
    public String ts;
}
