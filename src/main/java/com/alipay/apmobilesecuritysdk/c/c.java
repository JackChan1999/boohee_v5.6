package com.alipay.apmobilesecuritysdk.c;

import android.content.Context;
import com.alipay.apmobilesecuritysdk.e.d;
import com.alipay.security.mobile.module.commonutils.CommonUtils;
import com.alipay.security.mobile.module.commonutils.Dbg;
import com.alipay.security.mobile.module.commonutils.LOG;
import com.alipay.security.mobile.module.deviceinfo.DeviceInfo;
import com.umeng.socialize.net.utils.SocializeProtocolConstants;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONObject;

public final class c {
    public static Map<String, String> a(Context context) {
        String str;
        DeviceInfo instance = DeviceInfo.getInstance();
        Map<String, String> hashMap = new HashMap();
        d a = com.alipay.apmobilesecuritysdk.e.c.a(context);
        String imei = instance.getIMEI(context);
        String imsi = instance.getIMSI(context);
        String mACAddress = instance.getMACAddress(context);
        String bluMac = instance.getBluMac();
        String androidID = instance.getAndroidID(context);
        if (a != null) {
            Dbg.log("Read deviceInfoStorageModel From local data:");
            if (CommonUtils.isBlank(imei)) {
                imei = a.a();
            }
            if (CommonUtils.isBlank(imsi)) {
                imsi = a.b();
            }
            if (CommonUtils.isBlank(mACAddress)) {
                mACAddress = a.c();
            }
            if (CommonUtils.isBlank(bluMac)) {
                bluMac = a.d();
            }
            if (CommonUtils.isBlank(androidID)) {
                androidID = a.e();
            }
            str = androidID;
            androidID = bluMac;
            bluMac = mACAddress;
            mACAddress = imsi;
            imsi = imei;
        } else {
            Dbg.log("Local deviceInfoStorageModel is null");
            str = androidID;
            androidID = bluMac;
            bluMac = mACAddress;
            mACAddress = imsi;
            imsi = imei;
        }
        d dVar = new d(imsi, mACAddress, bluMac, androidID, str);
        if (context != null) {
            try {
                JSONObject jSONObject = new JSONObject();
                jSONObject.put(SocializeProtocolConstants.PROTOCOL_KEY_IMEI, dVar.a());
                jSONObject.put("imsi", dVar.b());
                jSONObject.put(SocializeProtocolConstants.PROTOCOL_KEY_MAC, dVar.c());
                jSONObject.put("bluetoothmac", dVar.d());
                jSONObject.put("gsi", dVar.e());
                imei = jSONObject.toString();
                com.alipay.apmobilesecuritysdk.f.c.a("device_feature_file_name", "device_feature_file_key", imei);
                com.alipay.apmobilesecuritysdk.f.c.a(context, "device_feature_prefs_name", "device_feature_prefs_key", imei);
            } catch (Throwable e) {
                LOG.logException(e);
            }
        }
        hashMap.put("AD1", imsi);
        hashMap.put("AD2", mACAddress);
        hashMap.put("AD3", instance.getSensorDigest(context));
        hashMap.put("AD5", instance.getScreenResolution(context));
        hashMap.put("AD6", instance.getScreenWidth(context));
        hashMap.put("AD7", instance.getScreenHeight(context));
        hashMap.put("AD8", bluMac);
        hashMap.put("AD9", instance.getSIMSerial(context));
        hashMap.put("AD10", str);
        hashMap.put("AD11", instance.getCPUSerial());
        hashMap.put("AD12", instance.getCpuCount());
        hashMap.put("AD13", instance.getCpuFrequent());
        hashMap.put("AD14", instance.getMemorySize());
        hashMap.put("AD15", instance.getTotalInternalMemorySize());
        hashMap.put("AD16", instance.getSDCardSize());
        hashMap.put("AD17", "");
        hashMap.put("AD18", androidID);
        hashMap.put("AD19", instance.getNetworkType(context));
        hashMap.put("AD20", instance.getBandVer());
        hashMap.put("AD21", instance.getPhoneNumber(context));
        hashMap.put("AD22", "");
        hashMap.put("AD23", instance.getSerialNumber());
        hashMap.put("AL3", instance.getWifiBssid(context));
        return hashMap;
    }
}
