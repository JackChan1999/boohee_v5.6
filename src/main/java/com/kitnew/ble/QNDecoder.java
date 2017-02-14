package com.kitnew.ble;

import android.os.Handler;

import com.baidu.location.l.b;
import com.kitnew.ble.utils.QNLog;
import com.umeng.analytics.a;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import uk.co.senab.photoview.IPhotoView;

public class QNDecoder {
    static long MILLIS_2000_YEAR = 949334400000L;
    QNApiCallback apiCallback;
    QNBleWriter   bleWriter;
    QNBleCallback callback;
    boolean       hasReceiveStoreData;
    boolean       hasReceived;
    volatile boolean hasWork = false;
    int          protocolType;
    QNBleDevice  qnBleDevice;
    QNUser       qnUser;
    List<QNData> storageData;
    List<QNUser> users;
    float        weightScale;

    public QNDecoder(QNUser qnUser, QNBleDevice qnBleDevice, QNBleWriter bleWriter, QNBleCallback
            callback, QNApiCallback apiCallback) {
        this.qnUser = qnUser;
        this.qnBleDevice = qnBleDevice;
        this.bleWriter = bleWriter;
        this.callback = callback;
        this.apiCallback = apiCallback;
    }

    void decode(byte[] data) {
        StringBuilder sb = new StringBuilder();
        int len$ = data.length;
        for (int i$ = 0; i$ < len$; i$++) {
            sb.append(String.format("%02X ", new Object[]{Byte.valueOf(arr$[i$])}));
        }
        QNLog.log(new Object[]{sb.toString()});
        this.hasWork = true;
        float weight;
        switch (data[0]) {
            case (byte) 16:
                if (data[5] == (byte) 0) {
                    this.hasReceived = false;
                    this.callback.onUnsteadyWeight(this.qnBleDevice, decodeWeight(data[3],
                            data[4]));
                    return;
                } else if (data[5] == (byte) 1) {
                    writeData(CmdBuilder.buildOverCmd(this.protocolType, 16));
                    if (!this.hasReceived) {
                        this.hasReceived = true;
                        weight = decodeWeight(data[3], data[4]);
                        if (weight > 0.0f) {
                            QNData md = buildMeasuredData(this.qnUser, weight, decodeIntegerValue
                                    (data[6], data[7]), decodeIntegerValue(data[8], data[9]), new
                                    Date(), data);
                            this.apiCallback.onReceivedData(md);
                            this.callback.onReceivedData(this.qnBleDevice, md);
                            return;
                        }
                        return;
                    }
                    return;
                } else {
                    return;
                }
            case (byte) 18:
                this.protocolType = data[2];
                this.weightScale = data[10] == (byte) 1 ? 100.0f : 10.0f;
                int[] iArr = new int[5];
                writeData(CmdBuilder.buildCmd(19, this.protocolType, 1, 16, 0, 0, 0));
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        try {
                            QNDecoder.this.writeBleData(CmdBuilder.builderTimeCmd(QNDecoder.this
                                    .protocolType));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                }, 300);
                return;
            case (byte) 33:
                writeBleData(CmdBuilder.buildCmd(34, this.protocolType, new int[0]));
                return;
            case (byte) 35:
                weight = decodeWeight(data[9], data[10]);
                if (weight > 0.0f) {
                    int resistance = decodeIntegerValue(data[11], data[12]);
                    QNUser user = matchUser(weight, resistance);
                    if (user != null) {
                        int resistance500 = decodeIntegerValue(data[13], data[14]);
                        long differTime = 0;
                        for (int i = 0; i < 4; i++) {
                            differTime |= (((long) data[i + 5]) & 255) << (i * 8);
                        }
                        Date date = new Date(MILLIS_2000_YEAR + (1000 * differTime));
                        if ((System.currentTimeMillis() - date.getTime()) / a.h > 365) {
                            date = new Date();
                        }
                        QNData qnData = buildMeasuredData(user, weight, resistance,
                                resistance500, date, data);
                        if (this.storageData == null) {
                            this.storageData = new ArrayList();
                        }
                        this.storageData.add(qnData);
                    } else {
                        QNLog.log(new Object[]{"没有匹配到用户,体重:", Float.valueOf(weight), " 电阻:",
                                Integer.valueOf(resistance)});
                    }
                }
                if (data[3] == data[4] && this.storageData != null && this.storageData.size() > 0) {
                    this.callback.onReceivedStoreData(this.qnBleDevice, this.storageData);
                    this.apiCallback.onReceiveStoreData(this.storageData);
                    this.hasReceiveStoreData = true;
                    return;
                }
                return;
            default:
                return;
        }
    }

    QNUser matchUser(float weight, int resistance) {
        if (this.users == null) {
            this.users = this.apiCallback.getAllUser();
        }
        QNUser perfectUser = null;
        for (QNUser user : this.users) {
            if (Math.abs(user.weight - weight) <= IPhotoView.DEFAULT_MAX_SCALE && (perfectUser ==
                    null || Math.abs(user.resistance - resistance) < Math.abs(perfectUser
                    .resistance - resistance))) {
                perfectUser = user;
            }
        }
        return perfectUser;
    }

    QNData buildMeasuredData(QNUser user, float weight, int resistance, int resistance500, Date
            date, byte[] data) {
        QNData qnData = new QNData();
        qnData.setUserData(user);
        qnData.setDeviceData(this.qnBleDevice);
        qnData.setResistance(resistance);
        qnData.setResistance500(resistance500);
        qnData.setCreateTime(date);
        qnData.addItemData(new QNItemData(2, weight));
        float height = ((float) this.qnUser.height) / 100.0f;
        qnData.addItemData(new QNItemData(3, weight / (height * height)));
        QNCalc calc = new QNCalc();
        if (this.qnBleDevice.isTwoPole()) {
            calc.initDC(this.qnUser.height, this.qnUser.calcAge(), this.qnUser.gender, 0, 0, 0,
                    (double) this.weightScale, data);
        } else {
            calc.init(this.qnUser.height, this.qnUser.calcAge(), this.qnUser.gender, 0, 0, 0,
                    (double) this.weightScale, data);
        }
        QNItemData qNItemData = new QNItemData(4, calc.getBodyfat());
        qnData.addItemData(qNItemData);
        boolean valid = qNItemData.value > 5.0f && qNItemData.value < 75.0f;
        qnData.addItemData(new QNItemData(7, valid ? calc.getWater() : 0.0d));
        qnData.addItemData(new QNItemData(12, valid ? calc.getBmr() : 0.0d));
        qnData.addItemData(new QNItemData(5, valid ? calc.getSubfat() : 0.0d));
        qnData.addItemData(new QNItemData(6, valid ? (float) calc.getVisfat() : 0.0f));
        qnData.addItemData(new QNItemData(9, valid ? calc.getMuscle() : 0.0d));
        qnData.addItemData(new QNItemData(10, valid ? calc.getBone() : 0.0d));
        qnData.addItemData(new QNItemData(11, valid ? calc.getEgg() : 0.0d));
        if (QNData.isShowBodyShape()) {
            qnData.addItemData(new QNItemData(13, valid ? calc.calcBodyShapeType(user.gender) :
                    ""));
        }
        if (QNData.isShowBodyAge()) {
            qnData.addItemData(new QNItemData(17, valid ? (float) calc.calcBodyAge(user, user
                    .getBirthday(), qNItemData.value, weight) : 0.0f));
        }
        if (QNData.isShowFattyLiveRisk()) {
            qnData.addItemData(new QNItemData(16, valid ? "I级" : ""));
        }
        if (QNData.isShowFfw()) {
            qnData.addItemData(new QNItemData(14, valid ? calc.getLbm() : 0.0d));
        }
        if (QNData.isShowSinew()) {
            qnData.addItemData(new QNItemData(18, valid ? calc.getSkeletalMuscle() : 0.0d));
        }
        if (QNData.isShowWhr()) {
            qnData.addItemData(new QNItemData(15, valid ? b. do:0.0d));
        }
        return qnData;
    }

    void writeData(byte[] data) {
        this.bleWriter.writeData(data);
    }

    void writeBleData(byte[] data) {
        this.bleWriter.writeBleData(data);
    }

    float decodeWeight(byte a, byte b) {
        return ((float) (((a & 255) << 8) + (b & 255))) / this.weightScale;
    }

    public static int decodeIntegerValue(byte a, byte b) {
        return ((a & 255) << 8) + (b & 255);
    }
}
