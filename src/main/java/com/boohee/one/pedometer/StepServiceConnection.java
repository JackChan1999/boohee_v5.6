package com.boohee.one.pedometer;

import android.content.ComponentName;
import android.content.Context;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;

import com.baidu.location.LocationClientOption;
import com.boohee.one.service.StepCounterService;

public class StepServiceConnection implements ServiceConnection, Callback {
    private int TIME_DELAY = LocationClientOption.MIN_SCAN_SPAN_NETWORK;
    private Context         context;
    private Handler         handler;
    private Messenger       messenger;
    private OnGetSensorStep onGetSensorStep;
    private Messenger       replyMessenger;

    public interface OnGetSensorStep {
        void onConnect();

        void onGetSensorStep(StepModel stepModel, boolean z);

        void onServiceDisConnected();
    }

    public static abstract class OnGetSensorStepAdapter implements OnGetSensorStep {
        public void onConnect() {
        }

        public void onServiceDisConnected() {
        }
    }

    public StepServiceConnection(Context context, OnGetSensorStep onGetSensorStep) {
        this.context = context;
        this.onGetSensorStep = onGetSensorStep;
        this.handler = new Handler(context.getMainLooper(), this);
        this.replyMessenger = new Messenger(this.handler);
    }

    public void onServiceConnected(ComponentName name, IBinder service) {
        try {
            this.messenger = new Messenger(service);
            Message msg = Message.obtain(null, 1);
            msg.replyTo = this.replyMessenger;
            this.messenger.send(msg);
            if (this.onGetSensorStep != null) {
                this.onGetSensorStep.onConnect();
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void onServiceDisconnected(ComponentName name) {
        if (this.onGetSensorStep != null) {
            this.onGetSensorStep.onServiceDisConnected();
        }
    }

    public void release() {
        this.handler.removeMessages(1);
        this.handler.removeMessages(2);
    }

    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            case 2:
                try {
                    Message msg1 = Message.obtain(null, 1);
                    msg1.replyTo = this.replyMessenger;
                    this.messenger.send(msg1);
                    break;
                } catch (RemoteException e) {
                    e.printStackTrace();
                    break;
                }
            case 4:
                StepModel step = (StepModel) msg.getData().getSerializable(StepCounterService
                        .KEY_DATA_STEP);
                boolean isError = msg.getData().getBoolean(StepCounterService.KEY_IS_ERROR);
                if (!(step == null || this.onGetSensorStep == null)) {
                    this.onGetSensorStep.onGetSensorStep(step, isError);
                }
                this.handler.sendEmptyMessageDelayed(2, (long) this.TIME_DELAY);
                break;
        }
        return false;
    }
}
