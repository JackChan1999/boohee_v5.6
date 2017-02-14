package com.kitnew.ble;

public interface QNBleScanCallback extends QNResultCallback {
    void onScan(QNBleDevice qNBleDevice);
}
