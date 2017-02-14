package com.boohee.one.pedometer.manager;

import com.boohee.one.pedometer.StepModel;

public interface StepListener {
    void onGetCurrentStep(StepModel stepModel, boolean z);
}
