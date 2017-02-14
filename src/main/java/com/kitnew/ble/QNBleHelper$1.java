package com.kitnew.ble;

class QNBleHelper$1 implements Runnable {
    final /* synthetic */ QNBleHelper this$0;

    QNBleHelper$1(QNBleHelper qNBleHelper) {
        this.this$0 = qNBleHelper;
    }

    public void run() {
        if (this.this$0.bleCallback != null) {
            this.this$0.bleCallback.onDisconnected(this.this$0.qnBleDevice);
            this.this$0.bleCallback = null;
        }
    }
}
