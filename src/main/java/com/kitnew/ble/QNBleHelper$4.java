package com.kitnew.ble;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattDescriptor;

class QNBleHelper$4 implements Runnable {
    final /* synthetic */ QNBleHelper             this$0;
    final /* synthetic */ BluetoothGattDescriptor val$bleReadBgcDes;
    final /* synthetic */ BluetoothGatt           val$gatt;

    QNBleHelper$4(QNBleHelper qNBleHelper, BluetoothGatt bluetoothGatt, BluetoothGattDescriptor
            bluetoothGattDescriptor) {
        this.this$0 = qNBleHelper;
        this.val$gatt = bluetoothGatt;
        this.val$bleReadBgcDes = bluetoothGattDescriptor;
    }

    public void run() {
        this.val$gatt.writeDescriptor(this.val$bleReadBgcDes);
    }
}
