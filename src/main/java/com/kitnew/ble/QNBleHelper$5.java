package com.kitnew.ble;

class QNBleHelper$5 implements Runnable {
    final /* synthetic */ QNBleHelper this$0;

    QNBleHelper$5(QNBleHelper qNBleHelper) {
        this.this$0 = qNBleHelper;
    }

    public void run() {
        if (this.this$0.bleCallback != null) {
            this.this$0.bleCallback.onConnected(this.this$0.qnBleDevice);
        }
    }
}
