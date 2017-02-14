package com.baidu.location;

import android.os.Bundle;
import android.os.DeadObjectException;
import android.os.Message;
import android.os.Messenger;
import android.util.Log;
import com.baidu.location.LocationClientOption.LocationMode;
import com.boohee.modeldao.StepCounterDao;
import com.qiniu.android.dns.NetworkInfo;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Locale;

class k implements ax, n {
    private static k bv = null;
    private boolean bt;
    private ArrayList bu;
    private boolean bw;

    private class a {
        final /* synthetic */ k a;
        public LocationClientOption do = new LocationClientOption();
        public Messenger for = null;
        public int if = 0;
        public String int = null;

        public a(k kVar, Message message) {
            this.a = kVar;
            this.for = message.replyTo;
            this.int = message.getData().getString("packName");
            this.do.if = message.getData().getString("prodName");
            az.cn().try(this.do.if, this.int);
            this.do.do = message.getData().getString("coorType");
            this.do.else = message.getData().getString("addrType");
            c.aw = this.do.else;
            this.do.for = message.getData().getBoolean("openGPS");
            this.do.int = message.getData().getInt("scanSpan");
            this.do.d = message.getData().getInt("timeOut");
            this.do.h = message.getData().getInt("priority");
            this.do.goto = message.getData().getBoolean("location_change_notify");
            this.do.g = message.getData().getBoolean("needDirect");
            if (this.do.g) {
                af.bw().try(this.do.g);
                af.bw().bx();
            }
            if (this.do.int > 1000) {
                az.cn().cm();
                h.for().int();
            }
            if (this.do.getLocationMode() == LocationMode.Hight_Accuracy) {
                if (!ar.bW().bZ()) {
                    Log.w(ax.i, "use hight accuracy mode does not use open wifi");
                }
                if (!x.a4().a1()) {
                    Log.w(ax.i, "use hight accuracy mode does not use open gps");
                }
            }
        }

        private void a(int i) {
            Message obtain = Message.obtain(null, i);
            try {
                if (this.for != null) {
                    this.for.send(obtain);
                }
                this.if = 0;
            } catch (Exception e) {
                if (e instanceof DeadObjectException) {
                    this.if++;
                }
            }
        }

        private void a(int i, String str, BDLocation bDLocation) {
            Bundle bundle = new Bundle();
            bundle.putParcelable(str, bDLocation);
            Message obtain = Message.obtain(null, i);
            obtain.setData(bundle);
            try {
                if (this.for != null) {
                    this.for.send(obtain);
                }
                this.if = 0;
            } catch (Exception e) {
                if (e instanceof DeadObjectException) {
                    this.if++;
                }
            }
        }

        public void a() {
            a(23);
        }

        public void a(BDLocation bDLocation) {
            a(bDLocation, 21);
        }

        public void a(BDLocation bDLocation, int i) {
            BDLocation bDLocation2 = new BDLocation(bDLocation);
            bDLocation2.internalSet(0, az.cn().iP);
            if (bDLocation2 != null) {
                if (i == 21) {
                    a(27, "locStr", bDLocation2);
                }
                if (!(this.do.do == null || this.do.do.equals(BDGeofence.COORD_TYPE_GCJ))) {
                    double longitude = bDLocation2.getLongitude();
                    double latitude = bDLocation2.getLatitude();
                    if (!(longitude == Double.MIN_VALUE || latitude == Double.MIN_VALUE)) {
                        double[] dArr = Jni.if(longitude, latitude, this.do.do);
                        bDLocation2.setLongitude(dArr[0]);
                        bDLocation2.setLatitude(dArr[1]);
                    }
                }
                a(i, "locStr", bDLocation2);
            }
        }

        public void if() {
            if (!this.do.goto) {
                return;
            }
            if (c.am) {
                a(54);
            } else {
                a(55);
            }
        }

        public void if(BDLocation bDLocation) {
            if (this.do.goto && !ae.bp().bq()) {
                a(bDLocation);
                ak.a().a(null);
                ak.a().if(ak.a().new);
            }
        }
    }

    private k() {
        this.bu = null;
        this.bw = false;
        this.bt = false;
        this.bu = new ArrayList();
    }

    private a if(Messenger messenger) {
        if (this.bu == null) {
            return null;
        }
        Iterator it = this.bu.iterator();
        while (it.hasNext()) {
            a aVar = (a) it.next();
            if (aVar.for.equals(messenger)) {
                return aVar;
            }
        }
        return null;
    }

    private void if(a aVar) {
        if (aVar != null) {
            if (if(aVar.for) != null) {
                aVar.a(14);
                return;
            }
            this.bu.add(aVar);
            aVar.a(13);
        }
    }

    private void j() {
        l();
        m();
    }

    private void l() {
        Iterator it = this.bu.iterator();
        boolean z = false;
        boolean z2 = false;
        while (it.hasNext()) {
            a aVar = (a) it.next();
            if (aVar.do.for) {
                z2 = true;
            }
            z = aVar.do.goto ? true : z;
        }
        c.a3 = z;
        if (this.bw != z2) {
            this.bw = z2;
            x.a4().int(this.bw);
        }
    }

    public static k p() {
        if (bv == null) {
            bv = new k();
        }
        return bv;
    }

    public void byte(String str) {
        BDLocation bDLocation = new BDLocation(str);
        ak.a().new = str;
        Iterator it = this.bu.iterator();
        while (it.hasNext()) {
            ((a) it.next()).if(bDLocation);
        }
    }

    public void do(Message message) {
        a aVar = if(message.replyTo);
        if (aVar != null) {
            this.bu.remove(aVar);
        }
        az.cn().cp();
        af.bw().bv();
        h.for().do();
        j();
    }

    public void do(BDLocation bDLocation) {
        ArrayList arrayList = new ArrayList();
        Iterator it = this.bu.iterator();
        while (it.hasNext()) {
            a aVar = (a) it.next();
            aVar.a(bDLocation);
            if (aVar.if > 4) {
                arrayList.add(aVar);
            }
        }
        if (arrayList != null && arrayList.size() > 0) {
            Iterator it2 = arrayList.iterator();
            while (it2.hasNext()) {
                this.bu.remove((a) it2.next());
            }
        }
    }

    public int for(Message message) {
        if (message == null || message.replyTo == null) {
            return 1;
        }
        a aVar = if(message.replyTo);
        return (aVar == null || aVar.do == null) ? 1 : aVar.do.h;
    }

    public void i() {
        Iterator it = this.bu.iterator();
        while (it.hasNext()) {
            ((a) it.next()).a();
        }
    }

    public String if(Message message) {
        if (message == null || message.replyTo == null) {
            return null;
        }
        a aVar = if(message.replyTo);
        if (aVar == null) {
            return null;
        }
        aVar.do.long = message.getData().getInt("num", aVar.do.long);
        aVar.do.c = message.getData().getFloat(StepCounterDao.DISTANCE, aVar.do.c);
        aVar.do.e = message.getData().getBoolean("extraInfo", aVar.do.e);
        aVar.do.new = true;
        String format = String.format(Locale.CHINA, "&poi=%.1f|%d", new Object[]{Float.valueOf(aVar.do.c), Integer.valueOf(aVar.do.long)});
        return aVar.do.e ? format + "|1" : format;
    }

    public void if(BDLocation bDLocation, int i) {
        ArrayList arrayList = new ArrayList();
        Iterator it = this.bu.iterator();
        while (it.hasNext()) {
            a aVar = (a) it.next();
            aVar.a(bDLocation, i);
            if (aVar.if > 4) {
                arrayList.add(aVar);
            }
        }
        if (arrayList != null && arrayList.size() > 0) {
            Iterator it2 = arrayList.iterator();
            while (it2.hasNext()) {
                this.bu.remove((a) it2.next());
            }
        }
    }

    public void if(BDLocation bDLocation, Message message) {
        if (bDLocation != null && message != null) {
            a aVar = if(message.replyTo);
            if (aVar != null) {
                aVar.a(bDLocation);
                if (aVar.if > 4) {
                    this.bu.remove(aVar);
                }
            }
        }
    }

    public boolean int(Message message) {
        boolean z = false;
        a aVar = if(message.replyTo);
        if (aVar != null) {
            int i = aVar.do.int;
            aVar.do.int = message.getData().getInt("scanSpan", aVar.do.int);
            if (aVar.do.int < 1000) {
                h.for().a();
                az.cn().cp();
                af.bw().bv();
                x.a4().a0();
            } else {
                h.for().if();
            }
            if (aVar.do.int > NetworkInfo.ISP_OTHER && i < 1000) {
                z = true;
                if (aVar.do.g) {
                    af.bw().try(aVar.do.g);
                    af.bw().bx();
                }
                az.cn().cm();
            }
            aVar.do.for = message.getData().getBoolean("openGPS", aVar.do.for);
            String string = message.getData().getString("coorType");
            LocationClientOption locationClientOption = aVar.do;
            if (string == null || string.equals("")) {
                string = aVar.do.do;
            }
            locationClientOption.do = string;
            string = message.getData().getString("addrType");
            locationClientOption = aVar.do;
            if (string == null || string.equals("")) {
                string = aVar.do.else;
            }
            locationClientOption.else = string;
            if (!c.aw.equals(aVar.do.else)) {
                ah.ay().az();
            }
            c.aw = aVar.do.else;
            aVar.do.d = message.getData().getInt("timeOut", aVar.do.d);
            aVar.do.goto = message.getData().getBoolean("location_change_notify", aVar.do.goto);
            aVar.do.h = message.getData().getInt("priority", aVar.do.h);
            j();
        }
        return z;
    }

    public boolean k() {
        return this.bw;
    }

    public void m() {
        Iterator it = this.bu.iterator();
        while (it.hasNext()) {
            ((a) it.next()).if();
        }
    }

    public void n() {
        this.bu.clear();
        j();
    }

    public void new(Message message) {
        if (message != null && message.replyTo != null) {
            if(new a(this, message));
            j();
        }
    }

    public String o() {
        StringBuffer stringBuffer = new StringBuffer(256);
        if (this.bu.isEmpty()) {
            return "&prod=" + az.iM + ":" + az.iH;
        }
        a aVar = (a) this.bu.get(0);
        if (aVar.do.if != null) {
            stringBuffer.append(aVar.do.if);
        }
        if (aVar.int != null) {
            stringBuffer.append(":");
            stringBuffer.append(aVar.int);
            stringBuffer.append("|");
        }
        String stringBuffer2 = stringBuffer.toString();
        return (stringBuffer2 == null || stringBuffer2.equals("")) ? null : "&prod=" + stringBuffer2;
    }
}
