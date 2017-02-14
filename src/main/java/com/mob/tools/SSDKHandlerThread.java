package com.mob.tools;

import android.os.Handler;
import android.os.Handler.Callback;
import android.os.HandlerThread;
import android.os.Message;

public abstract class SSDKHandlerThread implements Callback {
    private static final int MSG_START = -1;
    private static final int MSG_STOP  = -2;
    protected final Handler handler;
    private         String  name;

    public SSDKHandlerThread(String str) {
        this.name = str;
        HandlerThread handlerThread = new HandlerThread(str);
        handlerThread.start();
        this.handler = new Handler(handlerThread.getLooper(), this);
    }

    public String getName() {
        return this.name;
    }

    public final boolean handleMessage(Message message) {
        switch (message.what) {
            case -2:
                onStop(message);
                break;
            case -1:
                onStart(message);
                break;
            default:
                onMessage(message);
                break;
        }
        return false;
    }

    protected abstract void onMessage(Message message);

    protected void onStart(Message message) {
    }

    protected void onStop(Message message) {
    }

    public void startThread() {
        startThread(0, 0, null);
    }

    public void startThread(int i, int i2, Object obj) {
        Message message = new Message();
        message.what = -1;
        message.arg1 = i;
        message.arg2 = i2;
        message.obj = obj;
        this.handler.sendMessage(message);
    }

    public void stopThread() {
        stopThread(0, 0, null);
    }

    public void stopThread(int i, int i2, Object obj) {
        Message message = new Message();
        message.what = -2;
        message.arg1 = i;
        message.arg2 = i2;
        message.obj = obj;
        this.handler.sendMessage(message);
    }
}
