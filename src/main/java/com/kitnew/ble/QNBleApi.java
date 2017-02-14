package com.kitnew.ble;

import java.util.Date;

public interface QNBleApi {
    public static final int    QN_BLE_CLOSED          = 7;
    public static final int    QN_BLE_ERROR           = 5;
    public static final int    QN_BLE_LOW_SDK_VERSION = 8;
    public static final int    QN_BLE_LOW_VERSION     = 6;
    public static final int    QN_NETWORK_CLOSED      = 2;
    public static final int    QN_NETWORK_TIMEOUT     = 3;
    public static final int    QN_NO_BLE              = 4;
    public static final int    QN_SUCCESS             = 0;
    public static final int    QN_UNAVAILABLE_APP_ID  = 1;
    public static final String SDK_VERSION            = "2.0";

    void autoConnect(String str, int i, int i2, Date date, QNBleCallback qNBleCallback);

    void connectDevice(QNBleDevice qNBleDevice, String str, int i, int i2, Date date,
                       QNBleCallback qNBleCallback);

    void connectDevice(String str, String str2, int i, int i2, Date date, QNBleCallback
            qNBleCallback);

    void deleteUser(String str);

    void disconnectAll();

    void disconnectDevice(String str);

    void initSDK(String str, boolean z, QNResultCallback qNResultCallback);

    boolean isAppIdReady(QNResultCallback qNResultCallback);

    boolean isScanning();

    void startLeScan(String str, String str2, QNBleScanCallback qNBleScanCallback);

    void stopScan();
}
