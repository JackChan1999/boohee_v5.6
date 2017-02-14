package com.boohee.record;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.support.annotation.Nullable;

import com.boohee.database.OnePreference;
import com.boohee.database.UserPreference;
import com.boohee.model.User;
import com.boohee.model.WeightScale;
import com.boohee.modeldao.UserDao;
import com.boohee.utils.BleUtil;
import com.boohee.utils.DateFormatUtils;
import com.boohee.utils.Helper;
import com.boohee.utils.NumberUtils;
import com.kitnew.ble.QNApiManager;
import com.kitnew.ble.QNBleApi;
import com.kitnew.ble.QNBleCallback;
import com.kitnew.ble.QNBleDevice;
import com.kitnew.ble.QNBleScanCallback;
import com.kitnew.ble.QNData;

import java.util.List;

public class ScaleConnHelper {
    private static ScaleConnHelper INSTANCE;
    private        QNBleApi        bleApi;
    private          Runnable          gapScan          = new Runnable() {
        public void run() {
            if (ScaleConnHelper.this.bleApi.isScanning()) {
                ScaleConnHelper.this.bleApi.stopScan();
                Helper.simpleLog("BLE", "gap scan");
                ScaleConnHelper.this.mHandler.postDelayed(new Runnable() {
                    public void run() {
                        WeightScale scale = OnePreference.getWeightScale();
                        if (scale != null) {
                            ScaleConnHelper.this.startScan(scale);
                        }
                    }
                }, 500);
            }
        }
    };
    private          boolean           hasShowScanError = false;
    private volatile boolean           isConnecting     = false;
    private          BroadcastReceiver mBleReceiver     = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            switch (intent.getIntExtra("android.bluetooth.adapter.extra.STATE", 0)) {
                case 12:
                    ScaleConnHelper.this.mHandler.postDelayed(new Runnable() {
                        public void run() {
                            WeightScale scale = OnePreference.getWeightScale();
                            if (scale != null) {
                                ScaleConnHelper.this.startScan(scale);
                            }
                        }
                    }, 1000);
                    return;
                default:
                    return;
            }
        }
    };
    private WeightRecordActivity mContext;
    private Handler              mHandler;
    private QNBleCallback mScaleCallback = new QNBleCallback() {
        public void onConnectStart(QNBleDevice qnBleDevice) {
            Helper.simpleLog("BLE", "onConnectStart");
            ScaleConnHelper.this.isConnecting = true;
        }

        public void onConnected(QNBleDevice qnBleDevice) {
            ScaleConnHelper.this.showIfDismiss = true;
            Helper.simpleLog("BLE", "onConnected");
            ScaleConnHelper.this.mContext.showScaleConnect(0.0f, ScaleConnHelper.this
                    .showIfDismiss);
            ScaleConnHelper.this.isConnecting = true;
        }

        public void onDisconnected(QNBleDevice qnBleDevice) {
            ScaleConnHelper.this.showIfDismiss = true;
            Helper.simpleLog("BLE", "onDisconnected");
            ScaleConnHelper.this.mContext.disconnect();
            ScaleConnHelper.this.mHandler.post(new Runnable() {
                public void run() {
                    ScaleConnHelper.this.startScan(OnePreference.getWeightScale());
                }
            });
            ScaleConnHelper.this.isConnecting = false;
        }

        public void onUnsteadyWeight(QNBleDevice qnBleDevice, float v) {
            Helper.simpleLog("BLE", "onUnsteadyWeight");
            ScaleConnHelper.this.mContext.showScaleConnect(0.001f + v, ScaleConnHelper.this
                    .showIfDismiss);
        }

        public void onReceivedData(QNBleDevice qnBleDevice, QNData qnData) {
            Helper.simpleLog("BLE", "onReceivedData");
            ScaleConnHelper.this.mContext.showScaleResult(qnData);
        }

        public void onReceivedStoreData(QNBleDevice qnBleDevice, List<QNData> list) {
        }

        public void onCompete(final int i) {
            Helper.simpleLog("BLE", "onCompete");
            if (i != 0) {
                ScaleConnHelper.this.mHandler.post(new Runnable() {
                    public void run() {
                        Helper.showToast(BleUtil.getErrorMsg(i));
                    }
                });
            }
            ScaleConnHelper.this.isConnecting = false;
        }
    };
    private User mUser;
    private boolean showIfDismiss = true;

    @Nullable
    public static ScaleConnHelper getInstance() {
        return INSTANCE;
    }

    public ScaleConnHelper(WeightRecordActivity activity) {
        this.mContext = activity;
        this.bleApi = QNApiManager.getApi(activity);
        this.mUser = new UserDao(activity).queryWithToken(UserPreference.getToken(activity));
        this.mHandler = new Handler();
        INSTANCE = this;
    }

    public void stopAll() {
        this.mHandler.removeCallbacksAndMessages(null);
        this.bleApi.stopScan();
        this.bleApi.disconnectAll();
    }

    public void registerBle() {
        this.mContext.registerReceiver(this.mBleReceiver, new IntentFilter("android.bluetooth" +
                ".adapter.action.STATE_CHANGED"));
    }

    public void unRegisterAndPause() {
        stopAll();
        this.mContext.unregisterReceiver(this.mBleReceiver);
    }

    public void startScan(final WeightScale scale) {
        if (scale != null && !this.isConnecting) {
            this.bleApi.startLeScan(null, null, new QNBleScanCallback() {
                public void onScan(QNBleDevice qnBleDevice) {
                    if (qnBleDevice.getMac().equals(scale.mac)) {
                        ScaleConnHelper.this.bleApi.stopScan();
                        ScaleConnHelper.this.connectDevice(qnBleDevice);
                    }
                }

                public void onCompete(int i) {
                    if (i != 0 && i != 7) {
                        if (!ScaleConnHelper.this.hasShowScanError) {
                            Helper.showToast(BleUtil.getErrorMsg(i));
                        }
                        ScaleConnHelper.this.hasShowScanError = true;
                    }
                }
            });
            this.mHandler.postDelayed(this.gapScan, 5000);
        }
    }

    public void connectDevice(QNBleDevice device) {
        int sex = NumberUtils.safeParseInt(this.mUser.sex_type);
        if (sex == 2) {
            sex = 0;
        }
        this.bleApi.connectDevice(device, this.mUser.user_key, (int) this.mUser.height, sex,
                DateFormatUtils.string2date(this.mUser.birthday, "yyyy-MM-dd"), this
                        .mScaleCallback);
    }

    public void notShowWithUnsteadyWeight() {
        this.showIfDismiss = false;
        this.mHandler.postDelayed(new Runnable() {
            public void run() {
                ScaleConnHelper.this.showIfDismiss = true;
            }
        }, 5000);
    }
}
