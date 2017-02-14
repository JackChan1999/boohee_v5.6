package com.kitnew.ble;

import android.bluetooth.BluetoothDevice;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class QNBleDevice implements Parcelable {
    public static final Creator<QNBleDevice> CREATOR = new
    1();
    BluetoothDevice device;
    String          deviceName;
    String          mac;
    int             method;
    String          model;
    byte[]          record;
    int             rssi;

    public QNBleDevice(String deviceName, BluetoothDevice device) {
        this.mac = device.getAddress();
        this.deviceName = deviceName;
        this.device = device;
    }

    boolean isTwoPole() {
        return this.method == 2;
    }

    public boolean equals(Object o) {
        if (o == null || !(o instanceof QNBleDevice)) {
            return false;
        }
        return ((QNBleDevice) o).mac.equals(this.mac);
    }

    public String getMac() {
        return this.mac;
    }

    public String getDeviceName() {
        return this.deviceName;
    }

    public int getRssi() {
        return this.rssi;
    }

    public byte[] getRecord() {
        return this.record;
    }

    public String getModel() {
        return this.model;
    }

    int getMethod() {
        return this.method;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mac);
        dest.writeString(this.deviceName);
        dest.writeInt(this.rssi);
        dest.writeByteArray(this.record);
        dest.writeParcelable(this.device, 0);
        dest.writeString(this.model);
        dest.writeInt(this.method);
    }

    protected QNBleDevice(Parcel in) {
        this.mac = in.readString();
        this.deviceName = in.readString();
        this.rssi = in.readInt();
        this.record = in.createByteArray();
        this.device = (BluetoothDevice) in.readParcelable(BluetoothDevice.class.getClassLoader());
        this.model = in.readString();
        this.method = in.readInt();
    }
}
