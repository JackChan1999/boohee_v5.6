package com.alipay.security.mobile.module.deviceinfo;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Looper;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.telephony.cdma.CdmaCellLocation;
import android.telephony.gsm.GsmCellLocation;
import android.util.Base64;
import com.alipay.security.mobile.module.commonutils.CommonUtils;
import com.alipay.security.mobile.module.deviceinfo.constant.DeviceInfoConstant;
import com.alipay.security.mobile.module.deviceinfo.listener.SecLocationListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LocationInfo {
    private Context a;
    private String b;
    private String c;
    private String d;
    private String e;
    private String f;
    private String g;
    private String h;
    private String i;
    private String j;
    private String k;
    private volatile int l = 0;

    private LocationInfo() {
    }

    public static LocationInfo getLocationInfo(Context context) {
        TelephonyManager telephonyManager;
        String str;
        String str2;
        String str3;
        String str4;
        WifiManager wifiManager;
        LocationInfo locationInfo = new LocationInfo();
        locationInfo.a = context;
        try {
            telephonyManager = (TelephonyManager) context.getSystemService("phone");
            str = "";
            str2 = "";
            str3 = "";
            str4 = "";
            if ((telephonyManager.getPhoneType() == 2 ? 2 : 1) == 2) {
                String networkOperator;
                String valueOf;
                CdmaCellLocation cdmaCellLocation = (CdmaCellLocation) telephonyManager.getCellLocation();
                if (cdmaCellLocation != null) {
                    str4 = String.valueOf(cdmaCellLocation.getNetworkId());
                    networkOperator = telephonyManager.getNetworkOperator();
                    if (!(networkOperator == null || networkOperator.equals(""))) {
                        str = networkOperator.substring(0, 3);
                    }
                    str2 = String.valueOf(cdmaCellLocation.getSystemId());
                    valueOf = String.valueOf(cdmaCellLocation.getBaseStationId());
                    str3 = str;
                    networkOperator = str4;
                    str4 = str2;
                } else {
                    networkOperator = str4;
                    valueOf = str3;
                    str4 = str2;
                    str3 = str;
                }
                str2 = str4;
                str = str3;
                str4 = networkOperator;
                str3 = valueOf;
            } else {
                try {
                    GsmCellLocation gsmCellLocation = (GsmCellLocation) telephonyManager.getCellLocation();
                    if (gsmCellLocation != null) {
                        String networkOperator2 = telephonyManager.getNetworkOperator();
                        if (!(networkOperator2 == null || networkOperator2.equals(""))) {
                            str = telephonyManager.getNetworkOperator().substring(0, 3);
                            str2 = telephonyManager.getNetworkOperator().substring(3, 5);
                        }
                        str3 = String.valueOf(gsmCellLocation.getCid());
                        str4 = String.valueOf(gsmCellLocation.getLac());
                    }
                } catch (Exception e) {
                    e.fillInStackTrace();
                }
            }
        } catch (Exception e2) {
            e2.fillInStackTrace();
        } catch (Throwable th) {
        }
        locationInfo.setMcc(str);
        locationInfo.setMnc(str2);
        locationInfo.setCellId(str3);
        locationInfo.setLac(str4);
        try {
            telephonyManager = (TelephonyManager) context.getSystemService("phone");
            if (telephonyManager != null) {
                telephonyManager.listen(new PhoneStateListener(locationInfo) {
                    final /* synthetic */ LocationInfo a;

                    public final void onSignalStrengthsChanged(SignalStrength signalStrength) {
                        super.onSignalStrengthsChanged(signalStrength);
                        if (signalStrength != null) {
                            this.a.setCellRssi(signalStrength.getGsmSignalStrength());
                        }
                        telephonyManager.listen(this, 0);
                    }
                }, 256);
            }
        } catch (Throwable th2) {
        }
        try {
            Object obj;
            CdmaCellLocation cdmaCellLocation2;
            LocationManager locationManager = (LocationManager) context.getSystemService("location");
            if (locationManager.isProviderEnabled("network")) {
                LocationListener secLocationListener = new SecLocationListener();
                locationManager.requestLocationUpdates("network", DeviceInfoConstant.REQUEST_LOCATE_INTERVAL, 0.0f, secLocationListener, Looper.getMainLooper());
                locationManager.removeUpdates(secLocationListener);
                Location lastKnownLocation = locationManager.getLastKnownLocation("network");
                if (lastKnownLocation != null) {
                    locationInfo.setLatitude(lastKnownLocation.getLatitude());
                    locationInfo.setLongitude(lastKnownLocation.getLongitude());
                    obj = 1;
                    telephonyManager = (TelephonyManager) context.getSystemService("phone");
                    if (obj == null && telephonyManager.getPhoneType() == 2) {
                        cdmaCellLocation2 = (CdmaCellLocation) telephonyManager.getCellLocation();
                        if (cdmaCellLocation2 != null && CommonUtils.isBlank(locationInfo.getLatitude()) && CommonUtils.isBlank(locationInfo.getLongitude())) {
                            locationInfo.setLatitude((((double) cdmaCellLocation2.getBaseStationLatitude()) / 14400.0d));
                            locationInfo.setLongitude((((double) cdmaCellLocation2.getBaseStationLongitude()) / 14400.0d));
                        }
                    }
                    locationInfo.setIsWifiActive(((ConnectivityManager) context.getSystemService("connectivity")).getNetworkInfo(1).isConnected());
                    wifiManager = (WifiManager) context.getSystemService("wifi");
                    if (wifiManager.isWifiEnabled()) {
                        WifiInfo connectionInfo = wifiManager.getConnectionInfo();
                        locationInfo.setBssid(connectionInfo.getBSSID());
                        locationInfo.setSsid(Base64.encodeToString(connectionInfo.getSSID().getBytes(), 8));
                        locationInfo.setWifiStrength(connectionInfo.getRssi());
                    }
                    return locationInfo;
                }
            }
            obj = null;
            telephonyManager = (TelephonyManager) context.getSystemService("phone");
            cdmaCellLocation2 = (CdmaCellLocation) telephonyManager.getCellLocation();
            locationInfo.setLatitude((((double) cdmaCellLocation2.getBaseStationLatitude()) / 14400.0d));
            locationInfo.setLongitude((((double) cdmaCellLocation2.getBaseStationLongitude()) / 14400.0d));
        } catch (Exception e22) {
            e22.fillInStackTrace();
        }
        try {
            locationInfo.setIsWifiActive(((ConnectivityManager) context.getSystemService("connectivity")).getNetworkInfo(1).isConnected());
            wifiManager = (WifiManager) context.getSystemService("wifi");
            if (wifiManager.isWifiEnabled()) {
                WifiInfo connectionInfo2 = wifiManager.getConnectionInfo();
                locationInfo.setBssid(connectionInfo2.getBSSID());
                locationInfo.setSsid(Base64.encodeToString(connectionInfo2.getSSID().getBytes(), 8));
                locationInfo.setWifiStrength(connectionInfo2.getRssi());
            }
        } catch (Exception e222) {
            e222.fillInStackTrace();
        }
        return locationInfo;
    }

    public String getBssid() {
        return this.d;
    }

    public boolean getCellConnectivity() {
        return this.l != 0;
    }

    public String getCellId() {
        return this.j;
    }

    public double getCellRssi() {
        return (double) this.l;
    }

    public String getIsWifiActive() {
        return this.f;
    }

    public String getLac() {
        return this.k;
    }

    public String getLatitude() {
        return this.c;
    }

    public String getLongitude() {
        return this.b;
    }

    public String getMcc() {
        return this.h;
    }

    public String getMnc() {
        return this.i;
    }

    public String getSsid() {
        return this.e;
    }

    public List<Map<String, String>> getWifiListNearby() {
        List<Map<String, String>> arrayList = new ArrayList();
        if (this.a == null) {
            return arrayList;
        }
        WifiManager wifiManager = (WifiManager) this.a.getSystemService("wifi");
        if (wifiManager == null) {
            return arrayList;
        }
        List<ScanResult> scanResults = wifiManager.getScanResults();
        if (scanResults == null) {
            return arrayList;
        }
        for (ScanResult scanResult : scanResults) {
            Map hashMap = new HashMap();
            hashMap.put("wifiMac", scanResult.BSSID == null ? "" : scanResult.BSSID);
            hashMap.put("ssid", scanResult.SSID);
            hashMap.put("rssi", scanResult.level);
            arrayList.add(hashMap);
        }
        return arrayList;
    }

    public String getWifiStrength() {
        return this.g;
    }

    public boolean isGPSOpen() {
        if (this.a == null) {
            return false;
        }
        LocationManager locationManager = (LocationManager) this.a.getSystemService("location");
        return locationManager == null ? false : locationManager.isProviderEnabled("gps");
    }

    public boolean isWifiEncrypted() {
        if (this.a == null) {
            return false;
        }
        WifiManager wifiManager = (WifiManager) this.a.getSystemService("wifi");
        if (wifiManager == null) {
            return false;
        }
        WifiInfo connectionInfo = wifiManager.getConnectionInfo();
        if (connectionInfo == null) {
            return false;
        }
        WifiConfiguration wifiConfiguration;
        Context context = this.a;
        String ssid = connectionInfo.getSSID();
        if (context == null || ssid == null) {
            wifiConfiguration = null;
        } else {
            wifiManager = (WifiManager) context.getSystemService("wifi");
            if (wifiManager != null) {
                List<WifiConfiguration> configuredNetworks = wifiManager.getConfiguredNetworks();
                if (configuredNetworks != null) {
                    for (WifiConfiguration wifiConfiguration2 : configuredNetworks) {
                        if (wifiConfiguration2.SSID.equals(ssid)) {
                            break;
                        }
                    }
                }
            }
            wifiConfiguration2 = null;
        }
        if (wifiConfiguration2 == null) {
            return false;
        }
        int i = wifiConfiguration2.allowedKeyManagement.get(1) ? 2 : (wifiConfiguration2.allowedKeyManagement.get(2) || wifiConfiguration2.allowedKeyManagement.get(3)) ? 3 : wifiConfiguration2.wepKeys[0] != null ? 1 : 0;
        return i != 0;
    }

    public void setBssid(String str) {
        this.d = str;
    }

    public void setCellId(String str) {
        this.j = str;
    }

    public void setCellRssi(int i) {
        this.l = i;
    }

    public void setIsWifiActive(String str) {
        this.f = str;
    }

    public void setLac(String str) {
        this.k = str;
    }

    public void setLatitude(String str) {
        this.c = str;
    }

    public void setLongitude(String str) {
        this.b = str;
    }

    public void setMcc(String str) {
        this.h = str;
    }

    public void setMnc(String str) {
        this.i = str;
    }

    public void setSsid(String str) {
        this.e = str;
    }

    public void setWifiStrength(String str) {
        this.g = str;
    }
}
