package com.kitnew.ble;

import java.util.List;

public interface QNBleCallback extends QNResultCallback {
    void onConnectStart(QNBleDevice qNBleDevice);

    void onConnected(QNBleDevice qNBleDevice);

    void onDisconnected(QNBleDevice qNBleDevice);

    void onReceivedData(QNBleDevice qNBleDevice, QNData qNData);

    void onReceivedStoreData(QNBleDevice qNBleDevice, List<QNData> list);

    void onUnsteadyWeight(QNBleDevice qNBleDevice, float f);
}
