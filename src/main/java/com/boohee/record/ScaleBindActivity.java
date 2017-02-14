package com.boohee.record;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.view.View.OnClickListener;

import butterknife.ButterKnife;
import butterknife.InjectView;

import com.boohee.database.OnePreference;
import com.boohee.model.WeightScale;
import com.boohee.myview.swipeback.SwipeBackActivity;
import com.boohee.one.R;
import com.boohee.utils.BleUtil;
import com.boohee.utils.Helper;
import com.kitnew.ble.QNApiManager;
import com.kitnew.ble.QNBleApi;
import com.kitnew.ble.QNBleDevice;
import com.kitnew.ble.QNBleScanCallback;
import com.skyfishjy.library.RippleBackground;

public class ScaleBindActivity extends SwipeBackActivity {
    public static final String DEVICE           = "device";
    public static final int    REQUEST_BIND     = 1;
    public static final int    REQUEST_OPEN_BLE = 2;
    private QNBleApi bleApi;
    private BroadcastReceiver mBleReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            switch (intent.getIntExtra("android.bluetooth.adapter.extra.STATE", 0)) {
                case 12:
                    ScaleBindActivity.this.mHandler.postDelayed(new Runnable() {
                        public void run() {
                            ScaleBindActivity.this.startScan();
                        }
                    }, 1000);
                    return;
                default:
                    return;
            }
        }
    };
    private Handler           mHandler     = new Handler();
    @InjectView(2131427338)
    RippleBackground ripple;

    public static void startActivity(Activity context) {
        context.startActivityForResult(new Intent(context, ScaleBindActivity.class), 1);
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cq);
        ButterKnife.inject((Activity) this);
        this.bleApi = QNApiManager.getApi(this);
    }

    protected void onStart() {
        super.onStart();
        this.ripple.startRippleAnimation();
        registerReceiver(this.mBleReceiver, new IntentFilter("android.bluetooth.adapter.action" +
                ".STATE_CHANGED"));
        if (!BleUtil.isBleOpen()) {
            showOpenBleMsg();
        }
    }

    private void showOpenBleMsg() {
        Snackbar.make(this.ripple, (CharSequence) "请打开蓝牙来绑定体重秤", -2).setAction((CharSequence)
                "打开蓝牙", new OnClickListener() {
            public void onClick(View v) {
                ScaleBindActivity.this.startActivityForResult(new Intent("android.bluetooth" +
                        ".adapter.action.REQUEST_ENABLE"), 2);
            }
        }).show();
    }

    private void startScan() {
        this.bleApi.startLeScan(null, null, new QNBleScanCallback() {
            public void onScan(final QNBleDevice qnBleDevice) {
                OnePreference.setWeightScale(new WeightScale(qnBleDevice.getDeviceName(),
                        qnBleDevice.getMac(), qnBleDevice.getModel()));
                ScaleBindActivity.this.bleApi.stopScan();
                Helper.showToast((CharSequence) "绑定成功");
                Helper.simpleLog("BLE", "stopScan");
                ScaleBindActivity.this.mHandler.postDelayed(new Runnable() {
                    public void run() {
                        Intent data = new Intent();
                        data.putExtra("device", qnBleDevice);
                        ScaleBindActivity.this.setResult(-1, data);
                        ScaleBindActivity.this.finish();
                    }
                }, 1000);
            }

            public void onCompete(final int i) {
                if (i != 0 && i != 7) {
                    ScaleBindActivity.this.mHandler.post(new Runnable() {
                        public void run() {
                            Helper.showToast(BleUtil.getErrorMsg(i));
                        }
                    });
                }
            }
        });
    }

    protected void onResume() {
        super.onResume();
        startScan();
    }

    protected void onPause() {
        super.onPause();
        this.bleApi.stopScan();
    }

    protected void onStop() {
        super.onStop();
        unregisterReceiver(this.mBleReceiver);
        this.ripple.stopRippleAnimation();
    }
}
