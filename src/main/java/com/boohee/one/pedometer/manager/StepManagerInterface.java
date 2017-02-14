package com.boohee.one.pedometer.manager;

import com.boohee.one.pedometer.StepModel;

public interface StepManagerInterface {
    StepModel getCurrentStep();

    void getCurrentStepAsyncs();

    void postStepsToServer();

    void saveSteps();
}
