package com.boohee.one.pedometer.manager;

import android.content.Context;

import com.boohee.one.pedometer.StepModel;

public class DefaultStepManager extends AbstractStepManager {
    public DefaultStepManager(Context context) {
        super(context);
    }

    public boolean isSurpportStepCount() {
        return false;
    }

    public StepModel getCurrentStep() {
        return null;
    }

    public void getCurrentStepAsyncs() {
    }
}
