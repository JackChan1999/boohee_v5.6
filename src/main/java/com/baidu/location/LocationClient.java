package com.baidu.location;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.text.TextUtils;
import android.util.Log;
import com.baidu.location.LocationClientOption.LocationMode;
import com.boohee.modeldao.StepCounterDao;
import java.util.ArrayList;
import java.util.Iterator;

public final class LocationClient implements n, ax {
    private static final int j1 = 11;
    private static final int j4 = 4;
    private static final int jB = 3;
    private static final int jC = 8;
    private static final int jF = 9;
    private static final int jH = 7;
    private static final int jO = 5;
    private static final int jP = 12;
    private static final int jR = 6;
    private static final int jS = 2;
    private static final int jo = 10;
    private static final String jq = "baidu_location_Client";
    private static final int jt = 1;
    private static final int jx = 1000;
    private BDLocation j0 = null;
    private String j2 = null;
    private String j3 = null;
    private ArrayList j5 = null;
    private boolean jA = false;
    private BDLocationListener jD = null;
    private boolean jE = false;
    private boolean jG = false;
    private boolean jI;
    private final Messenger jJ = new Messenger(this.jp);
    private Context jK = null;
    private Messenger jL = null;
    private long jM = 0;
    private LocationClientOption jN = new LocationClientOption();
    private Boolean jQ = Boolean.valueOf(true);
    private boolean jT = false;
    private long jU = 0;
    private long jV = 0;
    private ServiceConnection jW = new ServiceConnection(this) {
        final /* synthetic */ LocationClient a;

        {
            this.a = r1;
        }

        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            this.a.jL = new Messenger(iBinder);
            if (this.a.jL != null) {
                this.a.jT = true;
                Log.d("baidu_location_client", "baidu location connected ...");
                try {
                    Message obtain = Message.obtain(null, 11);
                    obtain.replyTo = this.a.jJ;
                    obtain.setData(this.a.cx());
                    this.a.jL.send(obtain);
                    this.a.jT = true;
                    if (this.a.jN != null) {
                        if (this.a.jQ.booleanValue()) {
                            this.a.jp.obtainMessage(4).sendToTarget();
                        } else {
                            this.a.jp.obtainMessage(4).sendToTarget();
                        }
                    }
                } catch (Exception e) {
                }
            }
        }

        public void onServiceDisconnected(ComponentName componentName) {
            this.a.jL = null;
            this.a.jT = false;
        }
    };
    private String jX;
    private boolean jY = false;
    private boolean jZ = false;
    private boolean jn = false;
    private a jp = new a();
    private final Object jr = new Object();
    private BDErrorReport js = null;
    private b ju = null;
    private Boolean jv = Boolean.valueOf(false);
    private z jw = null;
    private long jy = 0;
    private Boolean jz = Boolean.valueOf(false);

    private class a extends Handler {
        final /* synthetic */ LocationClient a;

        private a(LocationClient locationClient) {
            this.a = locationClient;
        }

        public void handleMessage(Message message) {
            switch (message.what) {
                case 1:
                    this.a.cy();
                    return;
                case 2:
                    this.a.cz();
                    return;
                case 3:
                    this.a.n(message);
                    return;
                case 4:
                    this.a.o(message);
                    return;
                case 5:
                    this.a.r(message);
                    return;
                case 6:
                    this.a.m(message);
                    return;
                case 7:
                    return;
                case 8:
                    this.a.s(message);
                    return;
                case 9:
                    this.a.l(message);
                    return;
                case 10:
                    this.a.q(message);
                    return;
                case 11:
                    this.a.cA();
                    return;
                case 12:
                    this.a.cB();
                    return;
                case 21:
                    this.a.if(message, 21);
                    return;
                case 26:
                    this.a.if(message, 26);
                    return;
                case 27:
                    this.a.p(message);
                    return;
                case 54:
                    if (this.a.jN.goto) {
                        this.a.jn = true;
                        return;
                    }
                    return;
                case 55:
                    if (this.a.jN.goto) {
                        this.a.jn = false;
                        return;
                    }
                    return;
                case 204:
                    this.a.goto(false);
                    return;
                case 205:
                    this.a.goto(true);
                    return;
                default:
                    super.handleMessage(message);
                    return;
            }
        }
    }

    private class b implements Runnable {
        final /* synthetic */ LocationClient a;

        private b(LocationClient locationClient) {
            this.a = locationClient;
        }

        /* JADX WARNING: inconsistent code. */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void run() {
            /*
            r3 = this;
            r0 = r3.a;
            r1 = r0.jr;
            monitor-enter(r1);
            r0 = r3.a;	 Catch:{ all -> 0x0036 }
            r2 = 0;
            r0.jG = r2;	 Catch:{ all -> 0x0036 }
            r0 = r3.a;	 Catch:{ all -> 0x0036 }
            r0 = r0.jL;	 Catch:{ all -> 0x0036 }
            if (r0 == 0) goto L_0x001d;
        L_0x0015:
            r0 = r3.a;	 Catch:{ all -> 0x0036 }
            r0 = r0.jJ;	 Catch:{ all -> 0x0036 }
            if (r0 != 0) goto L_0x001f;
        L_0x001d:
            monitor-exit(r1);	 Catch:{ all -> 0x0036 }
        L_0x001e:
            return;
        L_0x001f:
            r0 = r3.a;	 Catch:{ all -> 0x0036 }
            r0 = r0.j5;	 Catch:{ all -> 0x0036 }
            if (r0 == 0) goto L_0x0034;
        L_0x0027:
            r0 = r3.a;	 Catch:{ all -> 0x0036 }
            r0 = r0.j5;	 Catch:{ all -> 0x0036 }
            r0 = r0.size();	 Catch:{ all -> 0x0036 }
            r2 = 1;
            if (r0 >= r2) goto L_0x0039;
        L_0x0034:
            monitor-exit(r1);	 Catch:{ all -> 0x0036 }
            goto L_0x001e;
        L_0x0036:
            r0 = move-exception;
            monitor-exit(r1);	 Catch:{ all -> 0x0036 }
            throw r0;
        L_0x0039:
            r0 = r3.a;	 Catch:{ all -> 0x0036 }
            r0 = r0.jp;	 Catch:{ all -> 0x0036 }
            r2 = 4;
            r0 = r0.obtainMessage(r2);	 Catch:{ all -> 0x0036 }
            r0.sendToTarget();	 Catch:{ all -> 0x0036 }
            monitor-exit(r1);	 Catch:{ all -> 0x0036 }
            goto L_0x001e;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.baidu.location.LocationClient.b.run():void");
        }
    }

    public LocationClient(Context context) {
        this.jK = context;
        this.jN = new LocationClientOption();
        this.jw = new z(this.jK, this);
    }

    public LocationClient(Context context, LocationClientOption locationClientOption) {
        this.jK = context;
        this.jN = locationClientOption;
        this.jw = new z(this.jK, this);
    }

    private void cA() {
        if (this.jL != null) {
            Message obtain = Message.obtain(null, 22);
            try {
                obtain.replyTo = this.jJ;
                this.jL.send(obtain);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void cB() {
        Message obtain = Message.obtain(null, 28);
        try {
            obtain.replyTo = this.jJ;
            this.jL.send(obtain);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void char(int i) {
        if (this.jE || ((this.jN.goto && this.j0.getLocType() == 61) || this.j0.getLocType() == 66 || this.j0.getLocType() == 67 || this.jA)) {
            Iterator it = this.j5.iterator();
            while (it.hasNext()) {
                ((BDLocationListener) it.next()).onReceiveLocation(this.j0);
            }
            if (this.j0.getLocType() != 66 && this.j0.getLocType() != 67) {
                this.jE = false;
                this.jV = System.currentTimeMillis();
            }
        }
    }

    private Bundle cw() {
        if (this.jN == null) {
            return null;
        }
        Bundle bundle = new Bundle();
        bundle.putInt("num", this.jN.long);
        bundle.putFloat(StepCounterDao.DISTANCE, this.jN.c);
        bundle.putBoolean("extraInfo", this.jN.e);
        return bundle;
    }

    private Bundle cx() {
        if (this.jN == null) {
            return null;
        }
        Bundle bundle = new Bundle();
        bundle.putString("packName", this.j3);
        bundle.putString("prodName", this.jN.if);
        bundle.putString("coorType", this.jN.do);
        bundle.putString("addrType", this.jN.else);
        bundle.putBoolean("openGPS", this.jN.for);
        bundle.putBoolean("location_change_notify", this.jN.goto);
        bundle.putInt("scanSpan", this.jN.int);
        bundle.putInt("timeOut", this.jN.d);
        bundle.putInt("priority", this.jN.h);
        bundle.putBoolean("map", this.jz.booleanValue());
        bundle.putBoolean("import", this.jv.booleanValue());
        bundle.putBoolean("needDirect", this.jN.g);
        return bundle;
    }

    private void cy() {
        if (!this.jT) {
            c.char();
            this.j3 = this.jK.getPackageName();
            this.j2 = this.j3 + "_bdls_v2.9";
            getAccessKey();
            Intent intent = new Intent(this.jK, f.class);
            try {
                intent.putExtra("debug_dev", this.jI);
            } catch (Exception e) {
            }
            if (this.jN == null) {
                this.jN = new LocationClientOption();
            }
            if (this.jN.getLocationMode() == LocationMode.Device_Sensors) {
                this.jN.setIsNeedAddress(false);
            }
            intent.putExtra("cache_exception", this.jN.b);
            intent.putExtra("kill_process", this.jN.char);
            try {
                this.jK.bindService(intent, this.jW, 1);
            } catch (Exception e2) {
                e2.printStackTrace();
                this.jT = false;
            }
        }
    }

    private void cz() {
        if (this.jT && this.jL != null) {
            Message obtain = Message.obtain(null, 12);
            obtain.replyTo = this.jJ;
            try {
                this.jL.send(obtain);
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                this.jK.unbindService(this.jW);
            } catch (Exception e2) {
            }
            synchronized (this.jr) {
                try {
                    if (this.jG) {
                        this.jp.removeCallbacks(this.ju);
                        this.jG = false;
                    }
                } catch (Exception e3) {
                }
            }
            this.jw.be();
            this.jL = null;
            c.case();
            this.jA = false;
            this.jT = false;
        }
    }

    private boolean else(int i) {
        if (this.jL == null || !this.jT) {
            return false;
        }
        try {
            this.jL.send(Message.obtain(null, i));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private void goto(boolean z) {
        if (this.js != null) {
            this.js.onReportResult(z);
        }
        this.js = null;
        this.jy = 0;
    }

    private void if(Message message, int i) {
        Bundle data = message.getData();
        data.setClassLoader(BDLocation.class.getClassLoader());
        this.j0 = (BDLocation) data.getParcelable("locStr");
        if (this.j0.getLocType() == 61) {
            this.jM = System.currentTimeMillis();
        }
        char(i);
    }

    private void l(Message message) {
        if (message != null && message.obj != null) {
            this.jw.do((BDNotifyListener) message.obj);
        }
    }

    private void m(Message message) {
        if (message != null && message.obj != null) {
            BDLocationListener bDLocationListener = (BDLocationListener) message.obj;
            if (this.j5 != null && this.j5.contains(bDLocationListener)) {
                this.j5.remove(bDLocationListener);
            }
        }
    }

    private void n(Message message) {
        if (message != null && message.obj != null) {
            LocationClientOption locationClientOption = (LocationClientOption) message.obj;
            if (!this.jN.equals(locationClientOption)) {
                if (this.jN.int != locationClientOption.int) {
                    try {
                        synchronized (this.jr) {
                            if (this.jG) {
                                this.jp.removeCallbacks(this.ju);
                                this.jG = false;
                            }
                            if (locationClientOption.int >= 1000 && !this.jG) {
                                if (this.ju == null) {
                                    this.ju = new b();
                                }
                                this.jp.postDelayed(this.ju, (long) locationClientOption.int);
                                this.jG = true;
                            }
                        }
                    } catch (Exception e) {
                    }
                }
                this.jN = new LocationClientOption(locationClientOption);
                if (this.jL != null) {
                    try {
                        Message obtain = Message.obtain(null, 15);
                        obtain.replyTo = this.jJ;
                        obtain.setData(cx());
                        this.jL.send(obtain);
                    } catch (Exception e2) {
                        e2.printStackTrace();
                    }
                }
            }
        }
    }

    private void o(Message message) {
        if (this.jL != null) {
            if ((System.currentTimeMillis() - this.jM > 3000 || !this.jN.goto) && (!this.jA || System.currentTimeMillis() - this.jV > 20000)) {
                Message obtain = Message.obtain(null, 22);
                try {
                    obtain.replyTo = this.jJ;
                    obtain.arg1 = message.arg1;
                    this.jL.send(obtain);
                    this.jU = System.currentTimeMillis();
                    this.jE = true;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            synchronized (this.jr) {
                if (!(this.jN == null || this.jN.int < 1000 || this.jG)) {
                    if (this.ju == null) {
                        this.ju = new b();
                    }
                    this.jp.postDelayed(this.ju, (long) this.jN.int);
                    this.jG = true;
                }
            }
        }
    }

    private void p(Message message) {
        Bundle data = message.getData();
        data.setClassLoader(BDLocation.class.getClassLoader());
        BDLocation bDLocation = (BDLocation) data.getParcelable("locStr");
        if (this.jD == null) {
            return;
        }
        if (this.jN == null || !this.jN.a() || bDLocation.getLocType() != 65) {
            this.jD.onReceiveLocation(bDLocation);
        }
    }

    private void q(Message message) {
        if (message != null && message.obj != null) {
            this.jw.for((BDNotifyListener) message.obj);
        }
    }

    private void r(Message message) {
        if (message != null && message.obj != null) {
            BDLocationListener bDLocationListener = (BDLocationListener) message.obj;
            if (this.j5 == null) {
                this.j5 = new ArrayList();
            }
            this.j5.add(bDLocationListener);
        }
    }

    private void s(Message message) {
        if (message != null && message.obj != null) {
            this.jD = (BDLocationListener) message.obj;
        }
    }

    public void cancleError() {
        else(202);
    }

    public String getAccessKey() {
        this.jX = v.a(this.jK);
        if (TextUtils.isEmpty(this.jX)) {
            throw new IllegalStateException("please setting key from Manifest.xml");
        }
        return String.format("KEY=%s;SHA1=%s", new Object[]{this.jX, v.if(this.jK)});
    }

    public BDLocation getLastKnownLocation() {
        return this.j0;
    }

    public LocationClientOption getLocOption() {
        return this.jN;
    }

    public String getVersion() {
        return n.T;
    }

    public boolean isStarted() {
        return this.jT;
    }

    public boolean notifyError() {
        return else(201);
    }

    public void registerLocationListener(BDLocationListener bDLocationListener) {
        if (bDLocationListener == null) {
            throw new IllegalStateException("please set a non-null listener");
        }
        Message obtainMessage = this.jp.obtainMessage(5);
        obtainMessage.obj = bDLocationListener;
        obtainMessage.sendToTarget();
    }

    public void registerNotify(BDNotifyListener bDNotifyListener) {
        Message obtainMessage = this.jp.obtainMessage(9);
        obtainMessage.obj = bDNotifyListener;
        obtainMessage.sendToTarget();
    }

    public void registerNotifyLocationListener(BDLocationListener bDLocationListener) {
        Message obtainMessage = this.jp.obtainMessage(8);
        obtainMessage.obj = bDLocationListener;
        obtainMessage.sendToTarget();
    }

    public void removeNotifyEvent(BDNotifyListener bDNotifyListener) {
        Message obtainMessage = this.jp.obtainMessage(10);
        obtainMessage.obj = bDNotifyListener;
        obtainMessage.sendToTarget();
    }

    public int reportErrorWithInfo(BDErrorReport bDErrorReport) {
        if (this.jL == null || !this.jT) {
            return 1;
        }
        if (bDErrorReport == null) {
            return 2;
        }
        if (System.currentTimeMillis() - this.jy < 50000 && this.js != null) {
            return 4;
        }
        Bundle errorInfo = bDErrorReport.getErrorInfo();
        if (errorInfo == null) {
            return 3;
        }
        try {
            Message obtain = Message.obtain(null, 203);
            obtain.replyTo = this.jJ;
            obtain.setData(errorInfo);
            this.jL.send(obtain);
            this.js = bDErrorReport;
            this.jy = System.currentTimeMillis();
            return 0;
        } catch (Exception e) {
            return 1;
        }
    }

    public int requestLocation() {
        if (this.jL == null || this.jJ == null) {
            return 1;
        }
        if (this.j5 == null || this.j5.size() < 1) {
            return 2;
        }
        if (System.currentTimeMillis() - this.jU < 1000) {
            return 6;
        }
        Message obtainMessage = this.jp.obtainMessage(4);
        obtainMessage.arg1 = 1;
        obtainMessage.sendToTarget();
        return 0;
    }

    public void requestNotifyLocation() {
        this.jp.obtainMessage(11).sendToTarget();
    }

    public int requestOfflineLocation() {
        if (this.jL == null || this.jJ == null) {
            return 1;
        }
        if (this.j5 == null || this.j5.size() < 1) {
            return 2;
        }
        this.jp.obtainMessage(12).sendToTarget();
        return 0;
    }

    public void setDebug(boolean z) {
        this.jI = z;
    }

    public void setForBaiduMap(boolean z) {
        this.jz = Boolean.valueOf(z);
    }

    public void setLocOption(LocationClientOption locationClientOption) {
        Object locationClientOption2;
        if (locationClientOption != null) {
            switch (locationClientOption.h) {
                case 1:
                    if (locationClientOption.int != 0 && locationClientOption.int < 1000) {
                        Log.w(ax.i, String.format("scanSpan time %d less than 1 second, location services may not star", new Object[]{Integer.valueOf(locationClientOption.int)}));
                        break;
                    }
                case 2:
                    if (locationClientOption.int > 1000 && locationClientOption.int < LocationClientOption.MIN_SCAN_SPAN_NETWORK) {
                        locationClientOption.int = LocationClientOption.MIN_SCAN_SPAN_NETWORK;
                        Log.w(ax.i, String.format("scanSpan time %d less than 3 second, location services may not star", new Object[]{Integer.valueOf(locationClientOption.int)}));
                        break;
                    }
                case 3:
                    if (locationClientOption.int == 0 || locationClientOption.int >= 1000) {
                        if (locationClientOption.int == 0) {
                            locationClientOption.int = 1000;
                            break;
                        }
                    }
                    Log.w(ax.i, String.format("scanSpan time %d less than 1 second, location services may not star", new Object[]{Integer.valueOf(locationClientOption.int)}));
                    break;
                    break;
                default:
                    break;
            }
        }
        locationClientOption2 = new LocationClientOption();
        Message obtainMessage = this.jp.obtainMessage(3);
        obtainMessage.obj = locationClientOption2;
        obtainMessage.sendToTarget();
    }

    public void start() {
        this.jp.obtainMessage(1).sendToTarget();
    }

    public void stop() {
        cz();
    }

    public void unRegisterLocationListener(BDLocationListener bDLocationListener) {
        if (bDLocationListener == null) {
            throw new IllegalStateException("please set a non-null listener");
        }
        Message obtainMessage = this.jp.obtainMessage(6);
        obtainMessage.obj = bDLocationListener;
        obtainMessage.sendToTarget();
    }

    public boolean updateLocation(Location location) {
        if (this.jL == null || this.jJ == null || location == null) {
            return false;
        }
        try {
            Message obtain = Message.obtain(null, 57);
            obtain.obj = location;
            this.jL.send(obtain);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }
}
