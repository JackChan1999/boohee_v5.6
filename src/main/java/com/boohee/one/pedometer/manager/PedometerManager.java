package com.boohee.one.pedometer.manager;

import android.content.Context;
import android.content.Intent;

import com.boohee.one.pedometer.StepModel;
import com.boohee.one.pedometer.StepServiceConnection;
import com.boohee.one.pedometer.StepServiceConnection.OnGetSensorStepAdapter;
import com.boohee.one.service.StepCounterService;
import com.boohee.utils.DateHelper;
import com.boohee.utils.Helper;

public class PedometerManager extends AbstractStepManager {
    public static final String TAG = PedometerManager.class.getSimpleName();
    private StepServiceConnection connection;

    public PedometerManager(Context context) {
        super(context);
    }

    public void init() {
        super.init();
        setupService();
    }

    private void setupService() {
        this.connection = new StepServiceConnection(this.context, new OnGetSensorStepAdapter() {
            public void onConnect() {
                Helper.showLog(PedometerManager.TAG, "onConnectService : " + PedometerManager
                        .this.toString());
            }

            public void onServiceDisConnected() {
                Helper.showLog(PedometerManager.TAG, "onDisconnectService");
            }

            public void onGetSensorStep(StepModel step, boolean Error) {
                Helper.showLog(PedometerManager.TAG, "onGetSensorStep : " + PedometerManager.this
                        .toString());
                PedometerManager.this.stepModel = step;
                PedometerManager.this.isError = Error;
                PedometerManager.this.notifyDataChanged();
            }
        });
        Intent intent = new Intent(this.context, StepCounterService.class);
        this.context.startService(intent);
        this.context.bindService(intent, this.connection, 1);
    }

    public StepModel getCurrentStep() {
        this.stepModel = this.dao.queryStep(DateHelper.today());
        return this.stepModel;
    }

    public void getCurrentStepAsyncs() {
        getCurrentStep();
        notifyDataChanged();
    }

    public boolean isPedometer() {
        return true;
    }

    public void onDestroy() {
        super.onDestroy();
        this.context.unbindService(this.connection);
        this.connection.release();
    }

    public void closePedometer() {
        onDestroy();
        this.context.stopService(new Intent(this.context, StepCounterService.class));
    }
}
