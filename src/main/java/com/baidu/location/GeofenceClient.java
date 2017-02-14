package com.baidu.location;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import java.util.List;

public class GeofenceClient implements ax, n {
    public static final String BUNDLE_FOR_GEOFENCE_ID = "geofence_id";
    private static final int bj = 1;
    private static long bn = a0.i2;
    private Context bf;
    private OnGeofenceTriggerListener bg;
    private ServiceConnection bh = new ServiceConnection(this) {
        final /* synthetic */ GeofenceClient a;

        {
            this.a = r1;
        }

        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            this.a.bk = new Messenger(iBinder);
            if (this.a.bk != null) {
                this.a.bl = true;
                this.a.startGeofenceScann();
            }
        }

        public void onServiceDisconnected(ComponentName componentName) {
            this.a.bk = null;
            this.a.bl = false;
        }
    };
    private Messenger bi = new Messenger(this.bm);
    private Messenger bk = null;
    private boolean bl = false;
    private a bm = new a();

    public interface OnAddBDGeofencesResultListener {
        void onAddBDGeofencesResult(int i, String str);
    }

    public interface OnGeofenceTriggerListener {
        void onGeofenceExit(String str);

        void onGeofenceTrigger(String str);
    }

    public interface OnRemoveBDGeofencesResultListener {
        void onRemoveBDGeofencesByRequestIdsResult(int i, String[] strArr);
    }

    private class a extends Handler {
        final /* synthetic */ GeofenceClient a;

        private a(GeofenceClient geofenceClient) {
            this.a = geofenceClient;
        }

        public void handleMessage(Message message) {
            Bundle data = message.getData();
            switch (message.what) {
                case 1:
                    this.a.void();
                    return;
                case 208:
                    if (data != null) {
                        this.a.for(data.getString("geofence_id"));
                        return;
                    }
                    return;
                case 209:
                    if (data != null) {
                        this.a.int(data.getString("geofence_id"));
                        return;
                    }
                    return;
                default:
                    return;
            }
        }
    }

    public GeofenceClient(Context context) {
        this.bf = context;
    }

    protected static long d() {
        return bn;
    }

    private void for(String str) {
        if (this.bg != null) {
            this.bg.onGeofenceTrigger(str);
        }
    }

    private void int(String str) {
        if (this.bg != null) {
            this.bg.onGeofenceExit(str);
        }
    }

    private void long() {
        try {
            Message obtain = Message.obtain(null, 207);
            obtain.replyTo = this.bi;
            this.bk.send(obtain);
        } catch (Exception e) {
        }
    }

    private void void() {
        if (!this.bl) {
            Intent intent = new Intent(this.bf, f.class);
            intent.putExtra("interval", bn);
            try {
                this.bf.bindService(intent, this.bh, 1);
            } catch (Exception e) {
                this.bl = false;
            }
        }
    }

    public void addBDGeofence(BDGeofence bDGeofence, OnAddBDGeofencesResultListener onAddBDGeofencesResultListener) throws NullPointerException, IllegalArgumentException, IllegalStateException {
        an.a((Object) bDGeofence, (Object) "geofence is null");
        if (bDGeofence != null) {
            an.if(bDGeofence instanceof aq, "BDGeofence must be created using BDGeofence.Builder");
        }
        au.for(this.bf).if((aq) bDGeofence, onAddBDGeofencesResultListener);
    }

    public boolean isStarted() {
        return this.bl;
    }

    public void registerGeofenceTriggerListener(OnGeofenceTriggerListener onGeofenceTriggerListener) {
        if (this.bg == null) {
            this.bg = onGeofenceTriggerListener;
        }
    }

    public void removeBDGeofences(List list, OnRemoveBDGeofencesResultListener onRemoveBDGeofencesResultListener) throws NullPointerException, IllegalArgumentException {
        au.for(this.bf).if(list, onRemoveBDGeofencesResultListener);
    }

    public void setInterval(long j) {
        if (j > bn) {
            bn = j;
        }
    }

    public void start() throws NullPointerException {
        an.a(this.bg, (Object) "OnGeofenceTriggerListener not register!");
        this.bm.obtainMessage(1).sendToTarget();
    }

    public void startGeofenceScann() {
        if (this.bl) {
            try {
                Message obtain = Message.obtain(null, 206);
                obtain.replyTo = this.bi;
                this.bk.send(obtain);
            } catch (Exception e) {
            }
        }
    }

    public void stop() {
        long();
    }
}
