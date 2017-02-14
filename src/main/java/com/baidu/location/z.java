package com.baidu.location;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.support.v4.widget.AutoScrollHelper;
import com.alipay.security.mobile.module.deviceinfo.constant.DeviceInfoConstant;
import java.util.ArrayList;
import java.util.Iterator;

public class z implements ax, n {
    public static final String gi = "android.com.baidu.location.TIMER.NOTIFY";
    private int ge = 0;
    private Context gf = null;
    private AlarmManager gg = null;
    private a gh = new a(this);
    private PendingIntent gj = null;
    private ArrayList gk = null;
    private BDLocation gl = null;
    private long gm = 0;
    private b gn = null;
    private float go = AutoScrollHelper.NO_MAX;
    private boolean gp = false;
    private boolean gq = false;
    private long gr = 0;
    private boolean gs = false;
    private LocationClient gt = null;

    public class a implements BDLocationListener {
        final /* synthetic */ z a;

        public a(z zVar) {
            this.a = zVar;
        }

        public void a(BDLocation bDLocation) {
        }

        public void onReceiveLocation(BDLocation bDLocation) {
            this.a.int(bDLocation);
        }
    }

    public class b extends BroadcastReceiver {
        final /* synthetic */ z a;

        public b(z zVar) {
            this.a = zVar;
        }

        public void onReceive(Context context, Intent intent) {
            if (this.a.gk != null && !this.a.gk.isEmpty()) {
                this.a.gt.requestNotifyLocation();
            }
        }
    }

    public z(Context context, LocationClient locationClient) {
        this.gf = context;
        this.gt = locationClient;
        this.gt.registerNotifyLocationListener(this.gh);
        this.gg = (AlarmManager) this.gf.getSystemService("alarm");
        this.gn = new b(this);
        this.gs = false;
    }

    private void bd() {
        int i = 10000;
        if (bf()) {
            boolean z;
            int i2 = this.go > 5000.0f ? 600000 : this.go > 1000.0f ? 120000 : this.go > 500.0f ? 60000 : 10000;
            if (this.gp) {
                this.gp = false;
            } else {
                i = i2;
            }
            if (this.ge != 0) {
                if (((long) i) > (this.gm + ((long) this.ge)) - System.currentTimeMillis()) {
                    z = false;
                    if (z) {
                        this.ge = i;
                        this.gm = System.currentTimeMillis();
                        if((long) this.ge);
                    }
                }
            }
            z = true;
            if (z) {
                this.ge = i;
                this.gm = System.currentTimeMillis();
                if((long) this.ge);
            }
        }
    }

    private boolean bf() {
        if (this.gk == null || this.gk.isEmpty()) {
            return false;
        }
        Iterator it = this.gk.iterator();
        boolean z = false;
        while (it.hasNext()) {
            z = ((BDNotifyListener) it.next()).Notified < 3 ? true : z;
        }
        return z;
    }

    private void if(long j) {
        if (this.gq) {
            this.gg.cancel(this.gj);
        }
        this.gj = PendingIntent.getBroadcast(this.gf, 0, new Intent(gi), 134217728);
        this.gg.set(0, System.currentTimeMillis() + j, this.gj);
    }

    private void int(BDLocation bDLocation) {
        this.gq = false;
        if (bDLocation.getLocType() != 61 && bDLocation.getLocType() != 161 && bDLocation.getLocType() != 65) {
            if(120000);
        } else if (System.currentTimeMillis() - this.gr >= 5000 && this.gk != null) {
            this.gl = bDLocation;
            this.gr = System.currentTimeMillis();
            float[] fArr = new float[1];
            Iterator it = this.gk.iterator();
            float f = AutoScrollHelper.NO_MAX;
            while (it.hasNext()) {
                BDNotifyListener bDNotifyListener = (BDNotifyListener) it.next();
                Location.distanceBetween(bDLocation.getLatitude(), bDLocation.getLongitude(), bDNotifyListener.mLatitudeC, bDNotifyListener.mLongitudeC, fArr);
                float radius = (fArr[0] - bDNotifyListener.mRadius) - bDLocation.getRadius();
                if (radius > 0.0f) {
                    if (radius < f) {
                    }
                    radius = f;
                } else {
                    if (bDNotifyListener.Notified < 3) {
                        bDNotifyListener.Notified++;
                        bDNotifyListener.onNotify(bDLocation, fArr[0]);
                        if (bDNotifyListener.Notified < 3) {
                            this.gp = true;
                        }
                    }
                    radius = f;
                }
                f = radius;
            }
            if (f < this.go) {
                this.go = f;
            }
            this.ge = 0;
            bd();
        }
    }

    public void be() {
        if (this.gq) {
            this.gg.cancel(this.gj);
        }
        this.gl = null;
        this.gr = 0;
        if (this.gs) {
            this.gf.unregisterReceiver(this.gn);
        }
        this.gs = false;
    }

    public int do(BDNotifyListener bDNotifyListener) {
        if (this.gk == null) {
            this.gk = new ArrayList();
        }
        this.gk.add(bDNotifyListener);
        bDNotifyListener.isAdded = true;
        bDNotifyListener.mNotifyCache = this;
        if (!this.gs) {
            this.gf.registerReceiver(this.gn, new IntentFilter(gi));
            this.gs = true;
        }
        if (bDNotifyListener.mCoorType != null) {
            if (!bDNotifyListener.mCoorType.equals(BDGeofence.COORD_TYPE_GCJ)) {
                double[] dArr = Jni.if(bDNotifyListener.mLongitude, bDNotifyListener.mLatitude, bDNotifyListener.mCoorType + "2gcj");
                bDNotifyListener.mLongitudeC = dArr[0];
                bDNotifyListener.mLatitudeC = dArr[1];
            }
            if (this.gl == null || System.currentTimeMillis() - this.gr > 30000) {
                this.gt.requestNotifyLocation();
            } else {
                float[] fArr = new float[1];
                Location.distanceBetween(this.gl.getLatitude(), this.gl.getLongitude(), bDNotifyListener.mLatitudeC, bDNotifyListener.mLongitudeC, fArr);
                float radius = (fArr[0] - bDNotifyListener.mRadius) - this.gl.getRadius();
                if (radius > 0.0f) {
                    if (radius < this.go) {
                        this.go = radius;
                    }
                } else if (bDNotifyListener.Notified < 3) {
                    bDNotifyListener.Notified++;
                    bDNotifyListener.onNotify(this.gl, fArr[0]);
                    if (bDNotifyListener.Notified < 3) {
                        this.gp = true;
                    }
                }
            }
            bd();
        }
        return 1;
    }

    public int for(BDNotifyListener bDNotifyListener) {
        if (this.gk == null) {
            return 0;
        }
        if (this.gk.contains(bDNotifyListener)) {
            this.gk.remove(bDNotifyListener);
        }
        if (this.gk.size() == 0 && this.gq) {
            this.gg.cancel(this.gj);
        }
        return 1;
    }

    public void if(BDNotifyListener bDNotifyListener) {
        if (bDNotifyListener.mCoorType != null) {
            if (!bDNotifyListener.mCoorType.equals(BDGeofence.COORD_TYPE_GCJ)) {
                double[] dArr = Jni.if(bDNotifyListener.mLongitude, bDNotifyListener.mLatitude, bDNotifyListener.mCoorType + "2gcj");
                bDNotifyListener.mLongitudeC = dArr[0];
                bDNotifyListener.mLatitudeC = dArr[1];
            }
            if (this.gl == null || System.currentTimeMillis() - this.gr > DeviceInfoConstant.REQUEST_LOCATE_INTERVAL) {
                this.gt.requestNotifyLocation();
            } else {
                float[] fArr = new float[1];
                Location.distanceBetween(this.gl.getLatitude(), this.gl.getLongitude(), bDNotifyListener.mLatitudeC, bDNotifyListener.mLongitudeC, fArr);
                float radius = (fArr[0] - bDNotifyListener.mRadius) - this.gl.getRadius();
                if (radius > 0.0f) {
                    if (radius < this.go) {
                        this.go = radius;
                    }
                } else if (bDNotifyListener.Notified < 3) {
                    bDNotifyListener.Notified++;
                    bDNotifyListener.onNotify(this.gl, fArr[0]);
                    if (bDNotifyListener.Notified < 3) {
                        this.gp = true;
                    }
                }
            }
            bd();
        }
    }
}
