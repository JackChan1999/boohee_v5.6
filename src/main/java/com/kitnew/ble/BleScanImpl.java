package com.kitnew.ble;

import android.annotation.TargetApi;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothAdapter.LeScanCallback;
import android.bluetooth.BluetoothDevice;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.kitnew.ble.utils.QNLog;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@TargetApi(18)
class BleScanImpl implements LeScanCallback {
    BluetoothAdapter bluetoothAdapter;
    final Handler handler = new Handler(Looper.getMainLooper());
    volatile boolean isScanning;
    String            mDeviceName;
    QNBleScanCallback mScanCallback;
    String            mTargetScanMac;
    Set<String> matchedNames = new HashSet(Arrays.asList(new String[]{"Yolanda-CS10C1",
            "Yolanda-CS11", "Yolanda-CS20E1", "Yolanda-CS20E2", "Yolanda-CS20F1",
            "Yolanda-CS20F2", "Yolanda-CS20G1", "Yolanda-CS20G2", "Yolanda-CS20H",
            "Yolanda-CS20I", "QN-Scale", "Beryl-CS40A"}));
    final Map<String, QNBleDevice> scanCache = new HashMap();

    BleScanImpl() {
    }

    void start(BluetoothAdapter bluetoothAdapter, String deviceName, String mac,
               QNBleScanCallback callback) {
        if (deviceName == null || this.matchedNames.contains(deviceName)) {
            this.bluetoothAdapter = bluetoothAdapter;
            this.mDeviceName = deviceName;
            this.mTargetScanMac = mac;
            this.mScanCallback = callback;
            this.scanCache.clear();
            QNLog.log("成功调用了启动,清空扫描缓存");
            if (this.isScanning) {
                QNLog.log("此时正在扫描，没有再做其它操作");
                return;
            }
            QNLog.log("此时没有扫描，启动扫描！");
            doStartScan(bluetoothAdapter);
            return;
        }
        QNLog.error("指定的蓝牙名错误");
        callback.onCompete(5);
    }

    void stop(BluetoothAdapter bluetoothAdapter) {
        this.mDeviceName = null;
        this.mTargetScanMac = null;
        this.mScanCallback = null;
        doStopScan(bluetoothAdapter);
    }

    void doStartScan(BluetoothAdapter bluetoothAdapter) {
        if (bluetoothAdapter != null) {
            this.isScanning = true;
            bluetoothAdapter.startLeScan(this);
        }
    }

    void onBleClosed(BluetoothAdapter bluetoothAdapter) {
        if (this.isScanning) {
            doStopScan(bluetoothAdapter);
        }
    }

    void clearCache() {
        this.scanCache.clear();
    }

    void doStopScan(BluetoothAdapter bluetoothAdapter) {
        if (bluetoothAdapter != null) {
            this.isScanning = false;
            bluetoothAdapter.stopLeScan(this);
            this.mScanCallback = null;
        }
    }

    public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
        QNLog.log("扫描到设备:", device.getName(), device.getAddress());
        if (this.mScanCallback == null) {
            QNLog.log("扫描中，但是没有回调接口");
            this.bluetoothAdapter.stopLeScan(this);
            return;
        }
        String mac = device.getAddress();
        if (((QNBleDevice) this.scanCache.get(mac)) != null) {
            QNLog.log("重复设备:", ((QNBleDevice) this.scanCache.get(mac)).getDeviceName(), (
                    (QNBleDevice) this.scanCache.get(mac)).mac);
            return;
        }
        BleScannerDataItem item;
        BleScannerData scannerData = new BleScannerData(scanRecord);
        String deviceName = device.getName();
        if (deviceName == null) {
            item = scannerData.getByType((byte) 9);
            if (item != null) {
                deviceName = new String(item.content);
            } else {
                return;
            }
        }
        if (!this.matchedNames.contains(deviceName)) {
            return;
        }
        if (this.mTargetScanMac != null && !this.mTargetScanMac.equals(mac)) {
            return;
        }
        if (this.mDeviceName == null || this.mDeviceName.equals(deviceName)) {
            QNLog.log("扫描到新设备:", deviceName, mac);
            final QNBleDevice qnBleDevice = new QNBleDevice();
            qnBleDevice.mac = mac;
            qnBleDevice.deviceName = deviceName;
            qnBleDevice.device = device;
            qnBleDevice.record = scanRecord;
            qnBleDevice.rssi = rssi;
            String internalModel = "0000";
            if ("QN-Scale".equals(deviceName)) {
                item = scannerData.getByType((byte) -1);
                if (item != null && item.length >= (byte) 12 && item.content[0] == item
                        .content[1] && item.content[0] == (byte) -1) {
                    internalModel = String.format("%02X%02X", new Object[]{Byte.valueOf(item
                            .content[2]), Byte.valueOf(item.content[3])});
                    Log.i("bind", "广播中识别内部型号:" + internalModel);
                } else if (item != null && item.content[1] == (byte) 1 && item.content[0] ==
                        (byte) -88) {
                    internalModel = String.format("%02X%02X", new Object[]{Byte.valueOf(item
                            .content[3]), Byte.valueOf(item.content[4])});
                    Log.i("bind", "广播中识别内部型号:" + internalModel);
                    QNBleDevice temp = getInternalModel(deviceName, internalModel);
                    qnBleDevice.model = temp.model;
                    qnBleDevice.method = temp.method;
                    if (qnBleDevice.getModel() == null) {
                        return;
                    }
                } else {
                    Log.i("bind", "没有识别出内部型号:" + mac);
                    return;
                }
            }
            for (DeviceData aDataList : QNCalc.getDeviceDataList()) {
                if (aDataList.internalModel.equals(internalModel) && aDataList.scaleName.equals
                        (deviceName)) {
                    qnBleDevice.model = aDataList.model;
                    qnBleDevice.method = aDataList.method;
                    break;
                }
            }
            this.scanCache.put(mac, qnBleDevice);
            this.handler.post(new Runnable() {
                public void run() {
                    if (BleScanImpl.this.mScanCallback != null) {
                        BleScanImpl.this.mScanCallback.onScan(qnBleDevice);
                    }
                }
            });
        }
    }

    private QNBleDevice getInternalModel(String deviceName, String internalModel) {
        QNBleDevice temp = new QNBleDevice();
        for (DeviceData aDataList : QNCalc.getDeviceDataList()) {
            if (aDataList.internalModel.equals(internalModel) && aDataList.scaleName.equals
                    (deviceName)) {
                temp.model = aDataList.model;
                temp.method = aDataList.method;
                break;
            }
        }
        if (temp.model == null) {
            temp.model = "YOLANDA";
            temp.method = 3;
        }
        return temp;
    }
}
