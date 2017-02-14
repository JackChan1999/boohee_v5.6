package com.boohee.one.pedometer.manager;

import android.content.Context;
import android.text.TextUtils;

import com.boohee.database.StepsPreference;
import com.boohee.one.event.BandTypeEvent;
import com.boohee.one.pedometer.StepCounterUtil;

public class StepManagerFactory {
    public static final String STEP_TYPE_CLING     = "cling";
    public static final String STEP_TYPE_DEFAULT   = "default";
    public static final String STEP_TYPE_PEDOMETER = "pedometer";
    private String type;

    private static class INSTANCE {
        private static StepManagerFactory managerFactory = new StepManagerFactory();

        private INSTANCE() {
        }
    }

    public @interface StepType {
    }

    public StepManagerFactory setType(@StepType String type) {
        this.type = type;
        return this;
    }

    public String getType() {
        return this.type;
    }

    public static StepManagerFactory getInstance() {
        return INSTANCE.managerFactory;
    }

    private StepManagerFactory() {
        this.type = "default";
    }

    public AbstractStepManager createStepManager(Context context) {
        if (TextUtils.equals(this.type, STEP_TYPE_CLING)) {
            return new BandStepManager(context);
        }
        if (TextUtils.equals(this.type, STEP_TYPE_PEDOMETER) && StepCounterUtil
                .isKitkatWithStepSensor(context) && StepsPreference.isStepOpen()) {
            return new PedometerManager(context);
        }
        this.type = "default";
        return new DefaultStepManager(context);
    }

    public AbstractStepManager changeStepManager(Context context, BandTypeEvent event,
                                                 AbstractStepManager manager) {
        if (TextUtils.equals(event.bandType, STEP_TYPE_CLING)) {
            if (manager.isPedometer()) {
                ((PedometerManager) manager).closePedometer();
            } else if (TextUtils.equals(getType(), "default")) {
                manager.onDestroy();
            }
            setType(STEP_TYPE_CLING);
        } else if (!TextUtils.equals(event.bandType, STEP_TYPE_PEDOMETER)) {
            if (manager.isPedometer()) {
                ((PedometerManager) manager).closePedometer();
            } else if (TextUtils.equals(getType(), STEP_TYPE_CLING)) {
                manager.onDestroy();
            }
            setType("default");
        } else if (!manager.isPedometer()) {
            setType(STEP_TYPE_PEDOMETER);
            manager.onDestroy();
        }
        return createStepManager(context);
    }
}
