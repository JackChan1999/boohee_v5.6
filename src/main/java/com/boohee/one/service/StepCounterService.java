package com.boohee.one.service;

import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.BitmapFactory;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat.Builder;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;

import com.boohee.database.StepsPreference;
import com.boohee.modeldao.StepCounterDao;
import com.boohee.one.R;
import com.boohee.one.pedometer.StepCounterUtil;
import com.boohee.one.pedometer.StepModel;
import com.boohee.one.ui.MainActivity;
import com.boohee.utils.DateFormatUtils;
import com.boohee.utils.DateHelper;
import com.boohee.utils.Helper;

public class StepCounterService extends Service implements SensorEventListener {
    public static final String ACTION_CLOSE_NOTIFICATION = "com.boohee.one" +
            ".step:action_close_notification";
    public static final String ACTION_CLOSE_STEP         = "com.boohee.one.step:action_stop_step";
    public static final String ACTION_OPEN_NOTIFICATION  = "com.boohee.one" +
            ".step:action_open_notification";
    public static final String ACTION_OPEN_STEP          = "com.boohee.one.step:action_open_step";
    public static final String ACTION_POST_STEPS         = "com.boohee.one.step:action_post_steps";
    public static final String ACTION_SAVE_STEPS         = "com.boohee.one.step:action_save_steps";
    public static final int    GRAY_SERVICE_ID           = -1213;
    public static final String KEY_DATA_STEP             = "KEY_DATA_STEP";
    public static final String KEY_IS_ERROR              = "KEY_IS_ERROR";
    public static final String KEY_WAKE_LOCK             = "WAKE_LOCK";
    public static final int    MSG_GET_STEPS             = 1;
    public static final int    MSG_GET_STEPS_DELAY       = 2;
    public static final int    MSG_REPORT_STEPS          = 4;
    public static final String TAG                       = StepCounterService.class.getSimpleName();
    private             int    MAX_VALUE                 = 1073741;
    private AlarmManager am;
    private Builder      builder;
    Handler closeWakeHandler = new Handler() {
        public void handleMessage(Message message) {
            StepCounterService.this.unlock();
        }
    };
    private PendingIntent contentIntent;
    private String        currentDay;
    private int daemonSleepTime = 2000;
    private Sensor gsensor;
    private boolean isBatchMode = false;
    private boolean isError     = false;
    private boolean isRunning;
    private int     lastSensorSteps;
    private int lastSteps = 0;
    private BroadcastReceiver mBatInfoReceiver;
    private WakeLock          mWakeLock;
    private int       maxReportLatency = 10000000;
    private Messenger messenger        = new Messenger(new MessenerHandler());
    private int       newSteps         = 0;
    private PendingIntent pendingIntent;
    private volatile boolean sPower = true;
    private SensorManager sensorManager;
    private boolean       shouldAddDx;
    private Intent        showTaskIntent;
    private StepModel step = new StepModel();
    private int            stepCounter;
    private StepCounterDao stepCounterDao;
    private Sensor         stepCounterSensor;
    private Sensor         stepDetectorSensor;
    private int            stepsBeforeToday;
    private int            stepsToday;
    private int totalSteps = 0;
    private PendingIntent     wakeLockPi;
    private BroadcastReceiver wakeReceiver;

    private class MessenerHandler extends Handler {
        private MessenerHandler() {
        }

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    try {
                        Messenger messenger = msg.replyTo;
                        Message replyMsg = Message.obtain(null, 4);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable(StepCounterService.KEY_DATA_STEP,
                                StepCounterService.this.step);
                        bundle.putBoolean(StepCounterService.KEY_IS_ERROR, StepCounterService
                                .this.isError);
                        replyMsg.setData(bundle);
                        messenger.send(replyMsg);
                        return;
                    } catch (RemoteException e) {
                        e.printStackTrace();
                        return;
                    }
                default:
                    super.handleMessage(msg);
                    return;
            }
        }
    }

    private class WakeReceiver extends BroadcastReceiver {
        private WakeReceiver() {
        }

        public void onReceive(Context context, Intent intent) {
            StepCounterService.this.unlock();
            StepCounterService.this.getLock();
            StepCounterService.this.closeWakeHandler.sendEmptyMessageDelayed(0, 15000);
        }
    }

    public void onCreate() {
        super.onCreate();
        if (StepCounterUtil.isKitkatWithStepSensor(this)) {
            Helper.showLog(TAG, "onCreate");
            this.stepCounterDao = new StepCounterDao(this);
            initNotification();
            initData();
            getLock();
            initBroadcastReceiver();
            initStepCounter();
            startAlarmWakeLock();
            return;
        }
        stopService();
    }

    private void initNotification() {
        if (StepsPreference.isStepNotificationOpen()) {
            this.showTaskIntent = new Intent(getApplicationContext(), MainActivity.class);
            this.showTaskIntent.setAction("android.intent.action.MAIN");
            this.showTaskIntent.addCategory("android.intent.category.LAUNCHER");
            this.showTaskIntent.addFlags(268435456);
            this.contentIntent = PendingIntent.getActivity(getApplicationContext(), 0, this
                    .showTaskIntent, 134217728);
            this.builder = new Builder(getApplicationContext()).setContentTitle(getString(R
                    .string.a78)).setWhen(System.currentTimeMillis()).setContentIntent(this
                    .contentIntent).setLargeIcon(BitmapFactory.decodeResource(getResources(), R
                    .drawable.icon));
            if (VERSION.SDK_INT >= 21) {
                this.builder.setColor(ContextCompat.getColor(this, R.color.hb)).setSmallIcon(R
                        .drawable.qm);
            } else {
                this.builder.setSmallIcon(R.drawable.icon);
            }
        }
    }

    private void initData() {
        this.currentDay = StepsPreference.getStepsCurrentDay();
        this.stepsBeforeToday = StepsPreference.getStepsBeforeToday();
        this.totalSteps = StepsPreference.getStepsTotal();
        this.lastSensorSteps = StepsPreference.getLastSensorSteps();
        Log.d(TAG, "currentDay : " + this.currentDay + ", stepsBefore : " + this.stepsBeforeToday
                + ", totalSteps : " + this.totalSteps + ", lastSensorSteps : " + this
                .lastSensorSteps);
        this.stepsToday = this.totalSteps - this.stepsBeforeToday;
        if (this.stepsToday > 0 && this.stepsBeforeToday > 0 && !TextUtils.isEmpty(this
                .currentDay) && DateFormatUtils.isToday(this.currentDay) && this.lastSensorSteps
                > 0) {
            this.shouldAddDx = true;
        }
        checkDate();
    }

    private void initStepCounter() {
        this.sensorManager = (SensorManager) getSystemService("sensor");
        this.stepCounterSensor = this.sensorManager.getDefaultSensor(19);
        this.stepDetectorSensor = this.sensorManager.getDefaultSensor(18);
        this.gsensor = this.sensorManager.getDefaultSensor(1);
        initSensor();
    }

    private void initSensor() {
        if (StepCounterUtil.isStepCounterSensor(this)) {
            registerStepSensor(this.stepCounterSensor);
            registerStepSensor(this.gsensor);
        } else if (StepCounterUtil.isStepDetectorSensor(this)) {
            registerStepSensor(this.stepDetectorSensor);
        }
    }

    private void registerStepSensor(Sensor sensor) {
        if (sensor != null && StepCounterUtil.isKitkat()) {
            if (this.isBatchMode) {
                this.sensorManager.registerListener(this, sensor, 2, this.maxReportLatency);
            } else {
                this.sensorManager.registerListener(this, sensor, 2);
            }
        }
    }

    private void unRegisterStepSensor() {
        if (this.sensorManager != null) {
            this.sensorManager.unregisterListener(this);
        }
    }

    private void initBroadcastReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.intent.action.SCREEN_OFF");
        filter.addAction("android.intent.action.ACTION_SHUTDOWN");
        filter.addAction("android.intent.action.SCREEN_ON");
        filter.addAction("android.intent.action.USER_PRESENT");
        filter.addAction("android.intent.action.CLOSE_SYSTEM_DIALOGS");
        this.mBatInfoReceiver = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if ("android.intent.action.SCREEN_ON".equals(action)) {
                    Log.d(StepCounterService.TAG, "screen on");
                    StepCounterService.this.isBatchMode = false;
                    StepCounterService.this.unRegisterStepSensor();
                    StepCounterService.this.initSensor();
                } else if ("android.intent.action.SCREEN_OFF".equals(action)) {
                    Log.d(StepCounterService.TAG, "screen off");
                    StepCounterService.this.isBatchMode = true;
                    StepCounterService.this.maxReportLatency = 10000000;
                    StepCounterService.this.unRegisterStepSensor();
                    StepCounterService.this.initSensor();
                } else if ("android.intent.action.USER_PRESENT".equals(action)) {
                    Log.d(StepCounterService.TAG, "screen unlock");
                } else if ("android.intent.action.CLOSE_SYSTEM_DIALOGS".equals(intent.getAction()
                )) {
                    Log.d(StepCounterService.TAG, " receive Intent.ACTION_CLOSE_SYSTEM_DIALOGS");
                } else if ("android.intent.action.ACTION_SHUTDOWN".equals(intent.getAction())) {
                    Log.d(StepCounterService.TAG, " receive ACTION_SHUTDOWN");
                }
                StepCounterService.this.save();
            }
        };
        registerReceiver(this.mBatInfoReceiver, filter);
    }

    @Nullable
    public IBinder onBind(Intent intent) {
        if (StepCounterUtil.isKitkatWithStepSensor(this) && StepsPreference.isStepOpen()) {
            Helper.showLog(TAG, "onBind");
            save();
            checkDate();
            return this.messenger.getBinder();
        }
        stopService();
        return null;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public int onStartCommand(android.content.Intent r7, int r8, int r9) {
        /*
        r6 = this;
        r2 = 2;
        r3 = 0;
        r1 = 1;
        r4 = TAG;
        r5 = "onStartCommand";
        com.boohee.utils.Helper.showLog(r4, r5);
        if (r7 == 0) goto L_0x001d;
    L_0x000d:
        r4 = r7.getAction();
        r5 = "com.boohee.one.step:action_open_step";
        r4 = android.text.TextUtils.equals(r4, r5);
        if (r4 == 0) goto L_0x002e;
    L_0x001a:
        com.boohee.database.StepsPreference.putStepOpen(r1);
    L_0x001d:
        r4 = com.boohee.one.pedometer.StepCounterUtil.isKitkatWithStepSensor(r6);
        if (r4 == 0) goto L_0x0029;
    L_0x0023:
        r4 = com.boohee.database.StepsPreference.isStepOpen();
        if (r4 != 0) goto L_0x003f;
    L_0x0029:
        r6.stopService();
        r1 = r2;
    L_0x002d:
        return r1;
    L_0x002e:
        r4 = r7.getAction();
        r5 = "com.boohee.one.step:action_stop_step";
        r4 = android.text.TextUtils.equals(r4, r5);
        if (r4 == 0) goto L_0x001d;
    L_0x003b:
        com.boohee.database.StepsPreference.putStepOpen(r3);
        goto L_0x001d;
    L_0x003f:
        if (r7 == 0) goto L_0x0057;
    L_0x0041:
        r0 = r7.getAction();
        r4 = android.text.TextUtils.isEmpty(r0);
        if (r4 != 0) goto L_0x0057;
    L_0x004b:
        r4 = -1;
        r5 = r0.hashCode();
        switch(r5) {
            case -1366945214: goto L_0x007b;
            case -836094301: goto L_0x005b;
            case -550503792: goto L_0x0071;
            case -286922874: goto L_0x0066;
            case 919590691: goto L_0x0091;
            case 1003374699: goto L_0x0086;
            default: goto L_0x0053;
        };
    L_0x0053:
        r2 = r4;
    L_0x0054:
        switch(r2) {
            case 0: goto L_0x009c;
            case 1: goto L_0x00a0;
            case 2: goto L_0x00a7;
            case 3: goto L_0x00ae;
            case 4: goto L_0x00b5;
            case 5: goto L_0x00bf;
            default: goto L_0x0057;
        };
    L_0x0057:
        r6.checkDate();
        goto L_0x002d;
    L_0x005b:
        r2 = "com.boohee.one.step:action_save_steps";
        r2 = r0.equals(r2);
        if (r2 == 0) goto L_0x0053;
    L_0x0064:
        r2 = r3;
        goto L_0x0054;
    L_0x0066:
        r2 = "com.boohee.one.step:action_post_steps";
        r2 = r0.equals(r2);
        if (r2 == 0) goto L_0x0053;
    L_0x006f:
        r2 = r1;
        goto L_0x0054;
    L_0x0071:
        r5 = "com.boohee.one.step:action_close_notification";
        r5 = r0.equals(r5);
        if (r5 == 0) goto L_0x0053;
    L_0x007a:
        goto L_0x0054;
    L_0x007b:
        r2 = "com.boohee.one.step:action_open_notification";
        r2 = r0.equals(r2);
        if (r2 == 0) goto L_0x0053;
    L_0x0084:
        r2 = 3;
        goto L_0x0054;
    L_0x0086:
        r2 = "com.boohee.one.step:action_stop_step";
        r2 = r0.equals(r2);
        if (r2 == 0) goto L_0x0053;
    L_0x008f:
        r2 = 4;
        goto L_0x0054;
    L_0x0091:
        r2 = "com.boohee.one.step:action_open_step";
        r2 = r0.equals(r2);
        if (r2 == 0) goto L_0x0053;
    L_0x009a:
        r2 = 5;
        goto L_0x0054;
    L_0x009c:
        r6.save();
        goto L_0x0057;
    L_0x00a0:
        r6.save();
        r6.postData();
        goto L_0x0057;
    L_0x00a7:
        com.boohee.database.StepsPreference.putStepNotification(r3);
        r6.closeForegroundNotification();
        goto L_0x0057;
    L_0x00ae:
        com.boohee.database.StepsPreference.putStepNotification(r1);
        r6.showForegroundNotification();
        goto L_0x0057;
    L_0x00b5:
        com.boohee.database.StepsPreference.putStepOpen(r3);
        r6.closeForegroundNotification();
        r6.stopService();
        goto L_0x0057;
    L_0x00bf:
        com.boohee.database.StepsPreference.putStepOpen(r1);
        r6.sPower = r1;
        r6.initSensor();
        goto L_0x0057;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.boohee.one.service" +
                ".StepCounterService.onStartCommand(android.content.Intent, int, int):int");
    }

    private void stopService() {
        stopSelf();
    }

    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == 19) {
            if (event.values[0] >= ((float) this.MAX_VALUE) || event.values[0] < 0.0f) {
                this.isError = true;
                return;
            }
            this.isError = false;
            int count = (int) event.values[0];
            if (this.lastSteps < 0) {
                this.lastSteps = 0;
            }
            if (this.lastSensorSteps > 0 && count > this.lastSensorSteps && this.shouldAddDx) {
                int countx = count - this.lastSensorSteps;
                if (countx > 20000) {
                    this.isError = true;
                    return;
                }
                this.totalSteps += countx;
                this.isError = false;
                this.lastSensorSteps = 0;
                this.shouldAddDx = false;
            }
            addSteps(count);
        } else if (event.sensor.getType() == 18) {
            addSteps(this.lastSteps + 1);
        }
    }

    private void addSteps(int count) {
        Log.d(TAG, "sensor count : " + count);
        if (this.lastSteps == 0) {
            this.lastSteps = count;
        }
        this.newSteps = count - this.lastSteps;
        if (this.newSteps < 0) {
            this.newSteps = 1;
        } else if (this.newSteps > 500) {
            this.newSteps = 1;
        }
        Log.d(TAG, "lastSteps : " + this.lastSteps);
        this.lastSteps = count;
        checkDate();
        this.totalSteps += this.newSteps;
        this.stepCounter += this.newSteps;
        if (this.stepCounter >= 500) {
            save();
            this.stepCounter = 0;
        }
        this.stepsToday = this.totalSteps - this.stepsBeforeToday;
        Log.d(TAG, "lastSensorSteps : " + this.lastSensorSteps);
        Log.d(TAG, "stepsBeforeToday : " + this.stepsBeforeToday);
        Log.d(TAG, "totalSteps : " + this.totalSteps);
        Log.d(TAG, "stepsToday : " + this.stepsToday);
        if (this.stepsToday < 0 || this.stepsToday >= this.MAX_VALUE) {
            this.isError = true;
            return;
        }
        this.isError = false;
        showForegroundNotification();
    }

    private void checkDate() {
        StepModel stepModel;
        String today;
        if (!StepCounterUtil.isSameDay()) {
            if (TextUtils.isEmpty(this.currentDay)) {
                this.currentDay = DateHelper.today();
            }
            String lastDay = this.currentDay;
            if (this.stepCounterDao == null) {
                this.stepCounterDao = new StepCounterDao(this);
            }
            if (this.stepCounterDao.queryStep(lastDay) == null) {
                StepModel stepModel2 = new StepModel();
                stepModel2.record_on = this.currentDay;
                stepModel2.step = this.stepsToday;
                this.stepCounterDao.add(this.step);
            }
            this.stepsToday = 0;
            this.stepsBeforeToday = this.totalSteps;
            this.step.step = this.stepsToday;
            stepModel = this.step;
            today = DateHelper.today();
            this.currentDay = today;
            stepModel.record_on = today;
            save();
        }
        this.step.step = this.stepsToday;
        stepModel = this.step;
        today = DateHelper.today();
        this.currentDay = today;
        stepModel.record_on = today;
    }

    @Deprecated
    private void postData() {
    }

    @TargetApi(16)
    private void showForegroundNotification() {
        if (StepsPreference.isStepNotificationOpen()) {
            if (this.builder == null) {
                initNotification();
            }
            startForeground(GRAY_SERVICE_ID, this.builder.setContentText(this.stepsToday + " 步")
                    .build());
        }
    }

    private void closeForegroundNotification() {
        stopForeground(true);
    }

    private void calDistance() {
    }

    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    private void save() {
        Log.d(TAG, "save steps data");
        calDistance();
        if (!(this.stepCounterDao == null || this.step == null)) {
            this.stepCounterDao.add(this.step);
        }
        StepsPreference.putStepsTotal(this.totalSteps);
        StepsPreference.putStepsBeforeToday(this.stepsBeforeToday);
        StepsPreference.putStepsCurrentDay(this.currentDay);
        StepsPreference.putLasSensorSteps(this.lastSteps);
    }

    public void onDestroy() {
        Log.d(TAG, "onDestroy");
        save();
        stopAlarmWakeLock();
        unRegisterStepSensor();
        if (this.mBatInfoReceiver != null) {
            unregisterReceiver(this.mBatInfoReceiver);
        }
        super.onDestroy();
    }

    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    private void startAlarmWakeLock() {
        this.am = (AlarmManager) getSystemService("alarm");
        if (this.wakeReceiver == null) {
            this.wakeReceiver = new WakeReceiver();
        }
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(KEY_WAKE_LOCK);
        registerReceiver(this.wakeReceiver, intentFilter);
        this.wakeLockPi = PendingIntent.getBroadcast(this, 0, new Intent(KEY_WAKE_LOCK), 134217728);
        this.am.setRepeating(0, System.currentTimeMillis(), 60000, this.wakeLockPi);
    }

    private void stopAlarmWakeLock() {
        unregisterReceiver(this.wakeReceiver);
        this.am.cancel(this.wakeLockPi);
        this.closeWakeHandler.removeMessages(0);
        unlock();
    }

    private void unlock() {
        if (this.mWakeLock != null && this.mWakeLock.isHeld()) {
            this.mWakeLock.release();
        }
    }

    private synchronized void getLock() {
        if (this.mWakeLock == null) {
            this.mWakeLock = ((PowerManager) getSystemService("power")).newWakeLock(1, StepCounterService.class.getName());
        }
        this.mWakeLock.acquire();
    }

    public void onLowMemory() {
        save();
        super.onLowMemory();
    }
}
