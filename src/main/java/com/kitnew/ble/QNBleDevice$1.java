package com.kitnew.ble;

import android.os.Parcel;
import android.os.Parcelable.Creator;

class QNBleDevice$1 implements Creator<QNBleDevice> {
    QNBleDevice$1() {
    }

    public QNBleDevice createFromParcel(Parcel source) {
        return new QNBleDevice(source);
    }

    public QNBleDevice[] newArray(int size) {
        return new QNBleDevice[size];
    }
}
