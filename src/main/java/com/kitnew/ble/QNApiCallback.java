package com.kitnew.ble;

import java.util.List;

public interface QNApiCallback {
    List<QNUser> getAllUser();

    void onReceiveStoreData(List<QNData> list);

    void onReceivedData(QNData qNData);
}
