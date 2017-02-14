package com.kitnew.ble;

import android.content.Context;
import android.os.Build.VERSION;

import java.util.Date;

public class QNApiManager {
    static QNBleApi instance;

    static class NotsupportImpl implements QNBleApi {
        NotsupportImpl() {
        }

        public void initSDK(String appId, boolean isRelease, QNResultCallback callback) {
            callback.onCompete(6);
        }

        public void deleteUser(String userId) {
        }

        public boolean isAppIdReady(QNResultCallback callback) {
            callback.onCompete(6);
            return false;
        }

        public void startLeScan(String deviceName, String mac, QNBleScanCallback callback) {
            callback.onCompete(6);
        }

        public void stopScan() {
        }

        public boolean isScanning() {
            return false;
        }

        public void connectDevice(QNBleDevice bleDevice, String userId, int height, int gender,
                                  Date birthday, QNBleCallback callback) {
            callback.onCompete(6);
        }

        public void disconnectDevice(String mac) {
        }

        public void disconnectAll() {
        }

        public void connectDevice(String mac, String userId, int height, int gender, Date
                birthday, QNBleCallback callback) {
            callback.onCompete(6);
        }

        public void autoConnect(String userId, int height, int gender, Date birthday,
                                QNBleCallback callback) {
            callback.onCompete(6);
        }
    }

    public static QNBleApi getApi(Context context) {
        if (instance == null) {
            if (VERSION.SDK_INT < 18) {
                instance = new NotsupportImpl();
            } else {
                instance = new QNApiImpl(context);
            }
        }
        return instance;
    }
}
