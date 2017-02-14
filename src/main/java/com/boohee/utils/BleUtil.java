package com.boohee.utils;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.os.Build.VERSION;

public class BleUtil {
    public static final int QN_BLE_CLOSED          = 7;
    public static final int QN_BLE_ERROR           = 5;
    public static final int QN_BLE_LOW_SDK_VERSION = 8;
    public static final int QN_BLE_LOW_VERSION     = 6;
    public static final int QN_NETWORK_CLOSED      = 2;
    public static final int QN_NETWORK_TIMEOUT     = 3;
    public static final int QN_NO_BLE              = 4;
    public static final int QN_SUCCESS             = 0;
    public static final int QN_UNAVAILABLE_APP_ID  = 1;

    public static boolean hasBleFeature(Context context) {
        return VERSION.SDK_INT >= 18 && context.getPackageManager().hasSystemFeature("android" +
                ".hardware.bluetooth_le");
    }

    public static String getErrorMsg(int code) {
        switch (code) {
            case 1:
                return "App id 失效";
            case 2:
                return "没有网络连接";
            case 3:
                return "网络连接超时";
            case 4:
                return "没有蓝牙4.0功能";
            case 5:
                return "蓝牙错误";
            case 6:
                return "蓝牙版本过低";
            case 7:
                return "蓝牙未开启";
            case 8:
                return "轻牛SDK过低";
            default:
                return "未知错误";
        }
    }

    public static boolean isBleOpen() {
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            return false;
        }
        return mBluetoothAdapter.isEnabled();
    }
}
