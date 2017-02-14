package com.kitnew.ble;

import android.annotation.TargetApi;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build.VERSION;

import com.kitnew.ble.utils.EncryptUtils;
import com.kitnew.ble.utils.FileUtils;
import com.kitnew.ble.utils.FileUtils.NetCallback;
import com.kitnew.ble.utils.QNLog;
import com.umeng.socialize.net.utils.SocializeProtocolConstants;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

class QNApiImpl implements QNBleApi, QNApiCallback {
    static final int APP_ID_RESULT_ERROR     = 1;
    static final int APP_ID_RESULT_LOWER_SDK = 2;
    static final int APP_ID_RESULT_NOT_CHECK = 0;
    static final int APP_ID_RESULT_OVER_DUE  = 3;
    static final int APP_ID_RESULT_SUCCESS   = 4;
    final BleScanImpl bleScan;
    BroadcastReceiver bleStateListener = new BroadcastReceiver() {
        @TargetApi(18)
        public void onReceive(Context context, Intent intent) {
            if (VERSION.SDK_INT >= 18) {
                if (QNApiImpl.this.mBluetoothAdapter == null) {
                    BluetoothManager bluetoothManager = (BluetoothManager) QNApiImpl.this
                            .mContext.getSystemService("bluetooth");
                    QNApiImpl.this.mBluetoothAdapter = bluetoothManager.getAdapter();
                }
                if (QNApiImpl.this.mBluetoothAdapter != null && !QNApiImpl.this.mBluetoothAdapter
                        .isEnabled()) {
                    QNApiImpl.this.bleScan.onBleClosed(QNApiImpl.this.mBluetoothAdapter);
                    for (QNBleHelper bleHelper : QNApiImpl.this.helperMap.values()) {
                        if (!(bleHelper == null || bleHelper.bleCallback == null)) {
                            bleHelper.bleCallback.onCompete(7);
                        }
                    }
                }
            }
        }
    };
    volatile boolean callInit = false;
    String checkFilename;
    final Map<String, QNBleHelper> helperMap = new HashMap();
    BluetoothAdapter mBluetoothAdapter;
    Context          mContext;
    QNUserDao        userDao;

    public QNApiImpl(Context context) {
        this.mContext = context;
        this.bleScan = new BleScanImpl();
        this.userDao = new QNUserDao(context);
        this.userDao.getAllUser();
    }

    boolean checkNetwork() {
        NetworkInfo ni = ((ConnectivityManager) this.mContext.getSystemService("connectivity"))
                .getActiveNetworkInfo();
        return ni != null && ni.isConnectedOrConnecting();
    }

    void doInitSDK(String appId, QNResultCallback callback) {
        String checkedAppString = FileUtils.getStringFromFileWithDecrypt(getCheckFilename());
        int originResult = 0;
        long originOverDue = 0;
        if (checkedAppString == null) {
            saveAppIdCheckResult(appId, 0, 0);
        } else {
            try {
                JSONObject jsonObject = new JSONObject(checkedAppString);
                try {
                    String originAppId = jsonObject.getString(SocializeProtocolConstants
                            .PROTOCOL_KEY_APP_ID);
                    if (originAppId == null || !originAppId.equals(appId)) {
                        saveAppIdCheckResult(appId, 0, 0);
                    } else {
                        originResult = jsonObject.getInt("result");
                        originOverDue = jsonObject.getLong("over_due");
                    }
                } catch (JSONException e) {
                    JSONObject jSONObject = jsonObject;
                }
            } catch (JSONException e2) {
            }
        }
        if (checkNetwork()) {
            final int _originResult = originResult;
            final long _overDue = originOverDue;
            final String str = appId;
            final QNResultCallback qNResultCallback = callback;
            FileUtils.checkAppId(appId, new NetCallback() {
                /* JADX WARNING: inconsistent code. */
                /* Code decompiled incorrectly, please refer to instructions dump. */
                public void onSuccess(java.lang.String r13) {
                    /*
                    r12 = this;
                    r9 = 1;
                    r7 = 0;
                    r13 = com.kitnew.ble.utils.EncryptUtils.decrypt(r13);	 Catch:{ Exception ->
                    0x00ac }
                    r3 = new org.json.JSONObject;	 Catch:{ Exception -> 0x00ac }
                    r3.<init>(r13);	 Catch:{ Exception -> 0x00ac }
                    r8 = 2;
                    r8 = new java.lang.Object[r8];	 Catch:{ Exception -> 0x00ac }
                    r10 = 0;
                    r11 = "请求结果:";
                    r8[r10] = r11;	 Catch:{ Exception -> 0x00ac }
                    r10 = 1;
                    r8[r10] = r13;	 Catch:{ Exception -> 0x00ac }
                    com.kitnew.ble.utils.QNLog.log(r8);	 Catch:{ Exception -> 0x00ac }
                    r8 = "status_code";
                    r1 = r3.getString(r8);	 Catch:{ Exception -> 0x00ac }
                    r4 = 0;
                    r8 = -1;
                    r10 = r1.hashCode();	 Catch:{ Exception -> 0x00ac }
                    switch(r10) {
                        case 47653682: goto L_0x0042;
                        case 51347766: goto L_0x004c;
                        default: goto L_0x002b;
                    };	 Catch:{ Exception -> 0x00ac }
                L_0x002b:
                    r7 = r8;
                L_0x002c:
                    switch(r7) {
                        case 0: goto L_0x0057;
                        case 1: goto L_0x00ae;
                        default: goto L_0x002f;
                    };	 Catch:{ Exception -> 0x00ac }
                L_0x002f:
                    r0 = 1;
                    r6 = 1;
                L_0x0031:
                    r7 = com.kitnew.ble.QNApiImpl.this;	 Catch:{ Exception -> 0x00ac }
                    r8 = r5;	 Catch:{ Exception -> 0x00ac }
                    r7.saveAppIdCheckResult(r8, r6, r4);	 Catch:{ Exception -> 0x00ac }
                    r7 = r6;	 Catch:{ Exception -> 0x00ac }
                    if (r7 == 0) goto L_0x0041;
                L_0x003c:
                    r7 = r6;	 Catch:{ Exception -> 0x00ac }
                    r7.onCompete(r0);	 Catch:{ Exception -> 0x00ac }
                L_0x0041:
                    return;
                L_0x0042:
                    r9 = "20000";
                    r9 = r1.equals(r9);	 Catch:{ Exception -> 0x00ac }
                    if (r9 == 0) goto L_0x002b;
                L_0x004b:
                    goto L_0x002c;
                L_0x004c:
                    r7 = "60000";
                    r7 = r1.equals(r7);	 Catch:{ Exception -> 0x00ac }
                    if (r7 == 0) goto L_0x002b;
                L_0x0055:
                    r7 = r9;
                    goto L_0x002c;
                L_0x0057:
                    r6 = 4;
                    r7 = "validate_day";
                    r2 = r3.getInt(r7);	 Catch:{ Exception -> 0x00ac }
                    r8 = java.lang.System.currentTimeMillis();	 Catch:{ Exception -> 0x00ac }
                    r7 = r2 * 24;
                    r7 = r7 * 60;
                    r7 = r7 * 60;
                    r7 = r7 * 1000;
                    r10 = (long) r7;	 Catch:{ Exception -> 0x00ac }
                    r4 = r8 + r10;
                    r0 = 0;
                    r7 = "bodyage";
                    r7 = r3.getInt(r7);	 Catch:{ Exception -> 0x00ac }
                    com.kitnew.ble.QNData.setBodyage(r7);	 Catch:{ Exception -> 0x00ac }
                    r7 = "sinew";
                    r7 = r3.getInt(r7);	 Catch:{ Exception -> 0x00ac }
                    com.kitnew.ble.QNData.setSinew(r7);	 Catch:{ Exception -> 0x00ac }
                    r7 = "body_shape";
                    r7 = r3.getInt(r7);	 Catch:{ Exception -> 0x00ac }
                    com.kitnew.ble.QNData.setBodyShape(r7);	 Catch:{ Exception -> 0x00ac }
                    r7 = "fat_free_weight";
                    r7 = r3.getInt(r7);	 Catch:{ Exception -> 0x00ac }
                    com.kitnew.ble.QNData.setFatFreeWeight(r7);	 Catch:{ Exception -> 0x00ac }
                    r7 = "whr";
                    r7 = r3.getInt(r7);	 Catch:{ Exception -> 0x00ac }
                    com.kitnew.ble.QNData.setWhr(r7);	 Catch:{ Exception -> 0x00ac }
                    r7 = "fatty_liver_risk";
                    r7 = r3.getInt(r7);	 Catch:{ Exception -> 0x00ac }
                    com.kitnew.ble.QNData.setFattyLiverRisk(r7);	 Catch:{ Exception -> 0x00ac }
                    goto L_0x0031;
                L_0x00ac:
                    r7 = move-exception;
                    goto L_0x0041;
                L_0x00ae:
                    r6 = 2;
                    r0 = 6;
                    goto L_0x0031;
                    */
                    throw new UnsupportedOperationException("Method not decompiled: com.kitnew" +
                            ".ble.QNApiImpl.2.onSuccess(java.lang.String):void");
                }

                public void onFailure(Throwable throwable) {
                    int apiResult = 0;
                    if (_originResult != 4 || _overDue < System.currentTimeMillis()) {
                        QNApiImpl.this.saveAppIdCheckResult(str, 0, 0);
                    } else {
                        apiResult = 0;
                    }
                    if (qNResultCallback != null) {
                        qNResultCallback.onCompete(apiResult);
                    }
                }
            });
        } else if (callback != null && !isValid(originResult, originOverDue)) {
            callback.onCompete(2);
        }
    }

    boolean isValid(int result, long overDue) {
        return result == 4 && overDue >= System.currentTimeMillis();
    }

    public void initSDK(String appId, boolean isRelease, QNResultCallback callback) {
        FileUtils.isRelease = isRelease;
        if (!this.callInit) {
            this.callInit = true;
            this.mContext.registerReceiver(new NetworkListener(this, appId, callback), new
                    IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"));
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction("android.bluetooth.adapter.action.STATE_CHANGED");
            this.mContext.registerReceiver(this.bleStateListener, intentFilter);
        }
    }

    public void deleteUser(String userId) {
        this.userDao.deleteUser(userId);
    }

    void saveAppIdCheckResult(String appId, int result, long overDue) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(SocializeProtocolConstants.PROTOCOL_KEY_APP_ID, appId);
            jsonObject.put("result", result);
            jsonObject.put("over_due", overDue);
            FileUtils.writeStringToFileWithEncrypt(jsonObject.toString(), getCheckFilename());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public boolean isAppIdReady(QNResultCallback callback) {
        String checkedAppString = FileUtils.getStringFromFileWithDecrypt(getCheckFilename());
        int result = 0;
        if (checkedAppString != null) {
            try {
                result = new JSONObject(checkedAppString).getInt("result");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        int apiResult = 0;
        if (result == 0) {
            if (callback != null) {
                QNLog.error("appId 还未校验");
            }
            apiResult = 1;
        } else if (result == 1) {
            if (callback != null) {
                QNLog.error("appId 校验失败，请检查您的appId");
            }
            apiResult = 1;
        } else if (result == 2) {
            if (callback != null) {
                QNLog.error("SDK版本过低，请升级SDK");
            }
            apiResult = 8;
        }
        if (!(result == 4 || callback == null)) {
            callback.onCompete(apiResult);
        }
        if (result == 4) {
            return true;
        }
        return false;
    }

    String getStoreAppId() {
        String str = null;
        String checkedAppString = FileUtils.getStringFromFileWithDecrypt(getCheckFilename());
        if (checkedAppString != null) {
            try {
                str = new JSONObject(checkedAppString).getString(SocializeProtocolConstants
                        .PROTOCOL_KEY_APP_ID);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return str;
    }

    String getCheckFilename() {
        if (this.checkFilename == null) {
            this.checkFilename = FileUtils.getDirectPath(this.mContext) + EncryptUtils.encrypt
                    (this.mContext.getPackageName());
        }
        return this.checkFilename;
    }

    public void startLeScan(String deviceName, String mac, QNBleScanCallback callback) {
        if (isAppIdReady(callback)) {
            if (this.mBluetoothAdapter == null) {
                this.mBluetoothAdapter = ((BluetoothManager) this.mContext.getSystemService
                        ("bluetooth")).getAdapter();
            }
            if (this.mBluetoothAdapter == null) {
                callback.onCompete(4);
            } else if (!this.mContext.getPackageManager().hasSystemFeature("android.hardware" +
                    ".bluetooth_le")) {
                callback.onCompete(6);
            } else if (this.mBluetoothAdapter.isEnabled()) {
                this.bleScan.start(this.mBluetoothAdapter, deviceName, mac, callback);
            } else {
                callback.onCompete(7);
            }
        }
    }

    public void stopScan() {
        this.bleScan.stop(this.mBluetoothAdapter);
    }

    public boolean isScanning() {
        return this.bleScan.isScanning;
    }

    public void connectDevice(QNBleDevice bleDevice, String userId, int height, int gender, Date
            birthday, QNBleCallback callback) {
        if (isAppIdReady(callback)) {
            if (this.mBluetoothAdapter == null) {
                this.mBluetoothAdapter = ((BluetoothManager) this.mContext.getSystemService
                        ("bluetooth")).getAdapter();
            }
            if (this.mBluetoothAdapter == null) {
                callback.onCompete(4);
            } else if (!this.mContext.getPackageManager().hasSystemFeature("android.hardware" +
                    ".bluetooth_le")) {
                callback.onCompete(6);
            } else if (this.mBluetoothAdapter.isEnabled()) {
                QNLog.log("成功进入到连接设备的方法:", bleDevice.deviceName, bleDevice.mac);
                QNUser user = synUser(userId, height, gender, birthday);
                synchronized (this.helperMap) {
                    QNBleHelper bleHelper = (QNBleHelper) this.helperMap.get(bleDevice.mac);
                    if (bleHelper == null) {
                        QNLog.log("没有连接过这个设备,重新创建蓝牙辅助类");
                        bleHelper = new QNBleHelper(bleDevice, user, this.mContext, callback, this);
                        this.helperMap.put(bleDevice.mac, bleHelper);
                    } else {
                        QNLog.log("连接过这个设备,复用蓝牙辅助类");
                        bleHelper.update(user, callback);
                    }
                    callback.onConnectStart(bleDevice);
                    bleHelper.doConnect();
                }
            } else {
                callback.onCompete(7);
            }
        }
    }

    QNUser synUser(String userId, int height, int gender, Date birthday) {
        QNUser storedUser = this.userDao.getById(userId);
        QNUser user = new QNUser(userId, height, gender, birthday);
        if (storedUser != null) {
            user.weight = storedUser.weight;
            user.resistance = storedUser.resistance;
        }
        if (!user.allEqual(storedUser)) {
            this.userDao.saveUser(user);
        }
        return user;
    }

    public void connectDevice(String mac, String userId, int height, int gender, Date birthday,
                              QNBleCallback callback) {
        QNUser user = synUser(userId, height, gender, birthday);
        synchronized (this.helperMap) {
            QNBleHelper bleHelper = (QNBleHelper) this.helperMap.get(mac);
            if (bleHelper != null) {
                bleHelper.update(user, callback);
                callback.onConnectStart(bleHelper.qnBleDevice);
                bleHelper.doConnect();
                return;
            }
            final QNBleCallback qNBleCallback = callback;
            final String str = userId;
            final int i = height;
            final int i2 = gender;
            final Date date = birthday;
            startLeScan(null, mac, new QNBleScanCallback() {
                public void onCompete(int errorCode) {
                    qNBleCallback.onCompete(errorCode);
                }

                public void onScan(QNBleDevice bleDevice) {
                    QNApiImpl.this.connectDevice(bleDevice, str, i, i2, date, qNBleCallback);
                }
            });
        }
    }

    public void disconnectDevice(String mac) {
        synchronized (this.helperMap) {
            QNBleHelper bleHelper = (QNBleHelper) this.helperMap.get(mac);
            if (bleHelper == null) {
                return;
            }
            bleHelper.doDisconnect();
        }
    }

    public void disconnectAll() {
        synchronized (this.helperMap) {
            if (this.bleScan.isScanning) {
                this.bleScan.doStopScan(this.mBluetoothAdapter);
            }
            this.bleScan.clearCache();
            for (QNBleHelper bleHelper : this.helperMap.values()) {
                bleHelper.close();
            }
            this.helperMap.clear();
        }
    }

    public void autoConnect(String userId, int height, int gender, Date birthday, QNBleCallback
            callback) {
        final QNBleCallback qNBleCallback = callback;
        final String str = userId;
        final int i = height;
        final int i2 = gender;
        final Date date = birthday;
        startLeScan(null, null, new QNBleScanCallback() {
            public void onCompete(int errorCode) {
                qNBleCallback.onCompete(errorCode);
            }

            public void onScan(QNBleDevice bleDevice) {
                QNApiImpl.this.stopScan();
                QNApiImpl.this.connectDevice(bleDevice, str, i, i2, date, qNBleCallback);
            }
        });
    }

    public void onReceivedData(QNData data) {
        this.userDao.saveUser(data.getUser());
        FileUtils.postData(getStoreAppId(), data.toJson(), new NetCallback() {
            public void onSuccess(String jsonString) {
            }

            public void onFailure(Throwable throwable) {
            }
        });
    }

    public void onReceiveStoreData(List<QNData> datas) {
        List jsons = new ArrayList(datas.size());
        for (QNData data : datas) {
            jsons.add(data.toJson());
        }
        FileUtils.postData(getStoreAppId(), jsons, new NetCallback() {
            public void onSuccess(String jsonString) {
            }

            public void onFailure(Throwable throwable) {
            }
        });
    }

    public List<QNUser> getAllUser() {
        return this.userDao.getAllUser();
    }
}
