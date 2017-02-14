package com.kitnew.ble;

import android.bluetooth.BluetoothGattCharacteristic;

class QNBleHelper$3 implements Runnable {
    final /* synthetic */ QNBleHelper                 this$0;
    final /* synthetic */ BluetoothGattCharacteristic val$characteristic;

    QNBleHelper$3(QNBleHelper qNBleHelper, BluetoothGattCharacteristic
            bluetoothGattCharacteristic) {
        this.this$0 = qNBleHelper;
        this.val$characteristic = bluetoothGattCharacteristic;
    }

    public void run() {
        if (this.this$0.qnDecoder != null) {
            this.this$0.qnDecoder.decode(this.val$characteristic.getValue());
        }
    }
}
