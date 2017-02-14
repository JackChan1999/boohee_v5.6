package com.boohee.one.pedometer;

import android.content.Context;
import android.content.Intent;
import android.os.Build.VERSION;
import android.provider.Settings.Secure;
import android.text.TextUtils;

import com.boohee.database.OnePreference;
import com.boohee.database.StepsPreference;
import com.boohee.model.VideoSportRecord;
import com.boohee.modeldao.StepCounterDao;
import com.boohee.one.pedometer.manager.StepManagerFactory;
import com.boohee.one.service.StepCounterService;
import com.boohee.utils.ArithmeticUtil;
import com.boohee.utils.DateHelper;

public class StepCounterUtil {
    public static final float caloryModulus = 0.52f;

    public static boolean isKitkatWithStepSensor(Context context) {
        return isKitkat() && (isStepCounterSensor(context) || isStepDetectorSensor(context));
    }

    public static boolean isStepCounterSensor(Context context) {
        return context.getPackageManager().hasSystemFeature("android.hardware.sensor.stepcounter");
    }

    public static boolean isStepDetectorSensor(Context context) {
        return context.getPackageManager().hasSystemFeature("android.hardware.sensor.stepdetector");
    }

    public static boolean isKitkat() {
        return VERSION.SDK_INT >= 19;
    }

    public static void startStepService(Context context) {
        if (isKitkatWithStepSensor(context) && StepsPreference.isStepOpen() && isPedometer()) {
            context.startService(new Intent(context, StepCounterService.class));
        }
    }

    public static boolean isSameDay() {
        return TextUtils.equals(StepsPreference.getStepsCurrentDay(), DateHelper.today());
    }

    public static int getCalory(int steps) {
        if (steps <= 0) {
            return 0;
        }
        return (int) Math.ceil(((((double) steps) * 0.52d) * ((double) OnePreference
                .getLatestWeight())) / 1000.0d);
    }

    public static float getFat(int calory) {
        if (calory <= 0) {
            return 0.0f;
        }
        return ArithmeticUtil.round((float) (calory / 7), 1);
    }

    public static void goNLPermission(Context context) {
        try {
            context.startActivity(new Intent("android.settings" +
                    ".ACTION_NOTIFICATION_LISTENER_SETTINGS"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean checkNotificationPermission(Context context) {
        String pkg = context.getPackageName();
        String flat = Secure.getString(context.getContentResolver(),
                "enabled_notification_listeners");
        return flat != null && flat.contains(pkg);
    }

    public static boolean isBindCling() {
        return TextUtils.equals(StepManagerFactory.getInstance().getType(), StepManagerFactory
                .STEP_TYPE_CLING);
    }

    public static boolean isPedometer() {
        return TextUtils.equals(StepManagerFactory.getInstance().getType(), StepManagerFactory
                .STEP_TYPE_PEDOMETER);
    }

    public static VideoSportRecord getStepToSportRecordToday(Context context) {
        return getStepToSportRecord(context, DateHelper.today());
    }

    public static VideoSportRecord getStepToSportRecord(Context context, String record_on) {
        if (TextUtils.isEmpty(record_on)) {
            return null;
        }
        StepModel stepModel = new StepCounterDao(context).queryStep(record_on);
        if (stepModel == null || stepModel.step <= 0) {
            return null;
        }
        int calory = getCalory(stepModel.step);
        VideoSportRecord record = new VideoSportRecord();
        record.calory = calory;
        record.activity_name = "走路";
        record.img_url = "http://up.boohee.cn/house/u/one/sport_course/ic_walk_sportv2.png";
        record.amount = stepModel.step;
        record.unit_name = "步";
        return record;
    }
}
