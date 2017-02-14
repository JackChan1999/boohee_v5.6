package com.kitnew.ble;

import android.annotation.TargetApi;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.kitnew.ble.utils.QNLog;

import java.util.UUID;

@TargetApi(18)
public class QNBleHelper extends BluetoothGattCallback implements QNBleWriter {
    UUID CLIENT_CHARACTERISTIC_CONFIG = UUID.fromString("00002902-0000-1000-8000-00805f9b34fb");
    UUID UUID_IBT_BLE_READER          = UUID.fromString("0000ffe2-0000-1000-8000-00805f9b34fb");
    UUID UUID_IBT_BLE_WRITER          = UUID.fromString("0000ffe4-0000-1000-8000-00805f9b34fb");
    UUID UUID_IBT_READ                = UUID.fromString("0000ffe1-0000-1000-8000-00805f9b34fb");
    UUID UUID_IBT_SERVICES            = UUID.fromString("0000ffe0-0000-1000-8000-00805f9b34fb");
    UUID UUID_IBT_WRITE               = UUID.fromString("0000ffe3-0000-1000-8000-00805f9b34fb");
    QNApiCallback               apiCallback;
    QNBleCallback               bleCallback;
    BluetoothGattCharacteristic bleWriteBgc;
    Context                     mContext;
    BluetoothGatt               mGatt;
    final QNBleDevice qnBleDevice;
    QNDecoder qnDecoder;
    QNUser    qnUser;
    Handler uiHandler = new Handler(Looper.getMainLooper());
    BluetoothGattCharacteristic writeBgc;

    public QNBleHelper(QNBleDevice qnBleDevice, QNUser qnUser, Context mContext, QNBleCallback
            bleCallback, QNApiCallback apiCallback) {
        this.qnBleDevice = qnBleDevice;
        this.qnUser = qnUser;
        this.mContext = mContext;
        this.bleCallback = bleCallback;
        this.apiCallback = apiCallback;
    }

    void update(QNUser user, QNBleCallback callback) {
        this.qnUser = user;
        this.bleCallback = callback;
        if (this.qnDecoder != null) {
            this.qnDecoder.callback = callback;
        }
    }

    void doConnect() {
        if (isConnected()) {
            QNLog.log("当前蓝牙已连接，不做操作");
            return;
        }
        if (this.mGatt != null) {
            this.mGatt.close();
        }
        this.mGatt = this.qnBleDevice.device.connectGatt(this.mContext, false, this);
    }

    void doDisconnect() {
        if (this.mGatt == null || isDisconnected()) {
            QNLog.log("当前蓝牙已断开连接，不做操作");
            return;
        }
        this.mGatt.disconnect();
    }

    void close() {
        doDisconnect();
        if (this.mGatt != null) {
            this.mGatt.close();
        }
        this.bleCallback = null;
        this.qnDecoder = null;
    }

    boolean isConnected() {
        int state = getConnectionState();
        QNLog.log("isConnected.state:", Integer.valueOf(state));
        if (state == 2 || state == 1) {
            return true;
        }
        return false;
    }

    boolean isDisconnected() {
        int state = getConnectionState();
        QNLog.log("isDisconnected.state:", Integer.valueOf(state));
        if (state == 0 || state == 3) {
            return true;
        }
        return false;
    }

    int getConnectionState() {
        return ((BluetoothManager) this.mContext.getSystemService("bluetooth"))
                .getConnectionState(this.qnBleDevice.device, 7);
    }

    public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
        if (this.mGatt != gatt) {
            this.mGatt = gatt;
        }
        if (newState == 2) {
            QNLog.log("连接设备成功");
            this.qnDecoder = null;
            this.mGatt.discoverServices();
        } else if (newState == 0) {
            this.mGatt.close();
            this.uiHandler.post(new 1 (this));
            this.qnDecoder = null;
        } else {
            gatt.disconnect();
            QNLog.error("连接状态异常:", Integer.valueOf(status));
            this.uiHandler.post(new 2 (this));
            if (this.bleCallback != null) {
                this.bleCallback.onCompete(5);
            }
        }
    }

    public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic
            characteristic) {
        this.uiHandler.post(new 3 (this, characteristic));
    }

    public void onServicesDiscovered(BluetoothGatt gatt, int status) {
        if (this.bleCallback == null) {
            gatt.disconnect();
        } else if (status != 0) {
            QNLog.error("onServicesDiscovered 中状态错误");
            this.bleCallback.onCompete(5);
        } else {
            BluetoothGattService service = gatt.getService(this.UUID_IBT_SERVICES);
            if (service == null) {
                gatt.disconnect();
                QNLog.error("onServicesDiscovered 没有关键的 service");
                this.bleCallback.onCompete(5);
                return;
            }
            BluetoothGattCharacteristic readBgc = service.getCharacteristic(this.UUID_IBT_READ);
            if (readBgc == null) {
                gatt.disconnect();
                QNLog.error("onServicesDiscovered 没有关键的 readBgc");
                this.bleCallback.onCompete(5);
                return;
            }
            gatt.setCharacteristicNotification(readBgc, true);
            BluetoothGattDescriptor readBgcDes = readBgc.getDescriptor(this
                    .CLIENT_CHARACTERISTIC_CONFIG);
            readBgcDes.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
            gatt.writeDescriptor(readBgcDes);
            BluetoothGattCharacteristic bleReadBgc = service.getCharacteristic(this
                    .UUID_IBT_BLE_READER);
            if (bleReadBgc == null) {
                gatt.disconnect();
                QNLog.error("onServicesDiscovered 没有关键的 bleReadBgc");
                this.bleCallback.onCompete(5);
                return;
            }
            gatt.setCharacteristicNotification(bleReadBgc, true);
            BluetoothGattDescriptor bleReadBgcDes = bleReadBgc.getDescriptor(this
                    .CLIENT_CHARACTERISTIC_CONFIG);
            if (bleReadBgcDes == null) {
                gatt.disconnect();
                QNLog.error("onServicesDiscovered 没有关键的 bleReadBgcDes");
                this.bleCallback.onCompete(5);
                return;
            }
            bleReadBgcDes.setValue(BluetoothGattDescriptor.ENABLE_INDICATION_VALUE);
            this.uiHandler.postDelayed(new 4 (this, gatt, bleReadBgcDes),300);
            this.writeBgc = service.getCharacteristic(this.UUID_IBT_WRITE);
            if (this.writeBgc == null) {
                gatt.disconnect();
                QNLog.error("onServicesDiscovered 没有关键的 writeBgc");
                this.bleCallback.onCompete(5);
                return;
            }
            this.bleWriteBgc = service.getCharacteristic(this.UUID_IBT_BLE_WRITER);
            if (this.bleWriteBgc == null) {
                gatt.disconnect();
                QNLog.error("onServicesDiscovered 没有关键的 bleWriteBgc");
                this.bleCallback.onCompete(5);
            }
            this.qnDecoder = new QNDecoder(this.qnUser, this.qnBleDevice, this, this.bleCallback,
                    this.apiCallback);
            QNLog.log("discoverServices成功");
            this.uiHandler.post(new 5 (this));
        }
    }

    public void writeData(byte[] data) {
        this.writeBgc.setValue(data);
        this.mGatt.writeCharacteristic(this.writeBgc);
    }

    public void writeBleData(byte[] data) {
        if (this.bleWriteBgc != null && this.mGatt != null) {
            this.bleWriteBgc.setValue(data);
            this.mGatt.writeCharacteristic(this.bleWriteBgc);
        }
    }
}
