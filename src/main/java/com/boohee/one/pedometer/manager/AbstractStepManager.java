package com.boohee.one.pedometer.manager;

import android.content.Context;
import android.text.TextUtils;

import com.boohee.database.StepsPreference;
import com.boohee.modeldao.StepCounterDao;
import com.boohee.one.pedometer.StepApi;
import com.boohee.one.pedometer.StepModel;
import com.boohee.utils.DateHelper;
import com.boohee.utils.Helper;

public abstract class AbstractStepManager implements StepManagerInterface {
    protected       Context        context;
    protected final StepCounterDao dao;
    protected boolean isError = false;
    private   StepListener listener;
    protected StepModel    stepModel;

    public abstract StepModel getCurrentStep();

    public abstract void getCurrentStepAsyncs();

    public AbstractStepManager(Context context) {
        this.context = context;
        this.dao = new StepCounterDao(context);
        init();
    }

    public void setListener(StepListener listener) {
        Helper.showLog(StepManagerFactory.STEP_TYPE_PEDOMETER, "setListener : " + toString() +
                "listener : " + listener);
        this.listener = listener;
    }

    public void removeListener() {
        Helper.showLog(StepManagerFactory.STEP_TYPE_PEDOMETER, "removeListener : " + toString() +
                "listener : " + this.listener);
        this.listener = null;
    }

    public void notifyDataChanged() {
        Helper.showLog(StepManagerFactory.STEP_TYPE_PEDOMETER, "notifyDataChanged : " + toString
                () + "listener : " + this.listener);
        if (this.listener != null) {
            this.listener.onGetCurrentStep(this.stepModel, this.isError);
        }
    }

    public void init() {
        if (isSurpportStepCount() && !TextUtils.equals(StepsPreference.getPrefSyncDate(),
                DateHelper.today())) {
            postStepsToServer();
        }
    }

    public void onCreate() {
    }

    public void onResume() {
    }

    public void onStop() {
    }

    public void onPause() {
    }

    public void onDestroy() {
        saveSteps();
        removeListener();
    }

    public void postStepsToServer() {
        if (this.context != null) {
            StepApi.postSteps(this.context);
        }
    }

    public void saveSteps() {
        if (this.dao != null && this.stepModel != null) {
            this.dao.add(this.stepModel);
        }
    }

    public boolean isPedometer() {
        return false;
    }

    public boolean isSurpportStepCount() {
        return true;
    }
}
