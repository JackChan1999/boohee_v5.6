package com.baidu.location;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.wifi.ScanResult;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.os.Messenger;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.os.RemoteException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

class a0 implements n {
    public static final long i2 = 1800000;
    private static final String i4 = "1";
    private static final String iR = "GeofenceStrategyService";
    private static final int iT = 30000;
    private static final int iU = 360000;
    private static final int iW = 6;
    private static final String iZ = "GeofenceStrategyService";
    private static final String ja = "com.baidu.locsdk.geofence.geofencestrategyservice";
    private static final int jb = 180000;
    private static final int jd = 60000;
    public static a0 jf = null;
    private static final String jh = "0";
    public static final String ji = "http://loc.map.baidu.com/fence";
    private static final String jk = "&gf=1";
    private String i0;
    private d i1;
    private boolean i3 = true;
    private Handler i5 = new Handler();
    private Map i6 = new HashMap();
    private String i7;
    private c i8;
    private WakeLock i9;
    private List iS;
    private String iV;
    private boolean iX;
    private boolean iY;
    private int jc;
    private HandlerThread je;
    private Messenger jg;
    private b jj;
    private List jl;
    private b jm;

    private class a extends s {
        private static final String dQ = "fence";
        private static final String dS = "bloc";
        private static final String dT = "ext";
        private static final String dU = "error";
        private static final String dW = "in";
        final /* synthetic */ a0 dP;
        private aq dR;
        private final String dV;

        public a(a0 a0Var, aq aqVar, String str) {
            this.dP = a0Var;
            this.dR = aqVar;
            this.dV = str;
            this.cT = new ArrayList();
        }

        private void ah() throws RemoteException {
            this.dP.i3 = false;
            au.for(f.getServiceContext()).if(this.dR);
            if (this.dP.jg != null) {
                Message obtain = Message.obtain(null, 209);
                Bundle bundle = new Bundle();
                bundle.putString("geofence_id", this.dR.getGeofenceId());
                obtain.setData(bundle);
                this.dP.jg.send(obtain);
            }
        }

        private void aj() throws RemoteException {
            this.dP.i3 = false;
            this.dP.i6.put(this.dR.getGeofenceId(), Long.valueOf(System.currentTimeMillis()));
            au.for(f.getServiceContext()).do(this.dR);
            if (this.dP.jg != null) {
                Message obtain = Message.obtain(null, 208);
                Bundle bundle = new Bundle();
                bundle.putString("geofence_id", this.dR.getGeofenceId());
                obtain.setData(bundle);
                this.dP.jg.send(obtain);
            }
        }

        void T() {
            this.cR = a0.ji;
            DecimalFormat decimalFormat = new DecimalFormat("0.00000");
            String str = "&x=%s&y=%s&r=%s&coord=%s&type=%s&cu=%s&fence_type=%s&wf_on=%s";
            Object[] objArr = new Object[8];
            objArr[0] = decimalFormat.format(this.dR.a());
            objArr[1] = decimalFormat.format(this.dR.case());
            objArr[2] = String.valueOf(this.dR.do());
            objArr[3] = String.valueOf(this.dR.int());
            objArr[4] = Integer.valueOf(au.do(f.getServiceContext()));
            objArr[5] = com.baidu.location.b.a.a.if(f.getServiceContext());
            objArr[6] = Integer.valueOf(this.dR.char());
            objArr[7] = ar.bW().bZ() ? "1" : "0";
            this.cT.add(new BasicNameValuePair(dQ, Jni.i(String.format(str, objArr))));
            this.cT.add(new BasicNameValuePair(dS, this.dV));
            this.cT.add(new BasicNameValuePair("ext", Jni.i(String.format(Locale.CHINA, "&ki=%s&sn=%s", new Object[]{v.a(f.getServiceContext()), v.if(f.getServiceContext())}))));
        }

        public void ai() {
            N();
        }

        void do(boolean z) {
            boolean z2 = false;
            this.dP.iX = false;
            if (z && this.cS != null) {
                try {
                    JSONObject jSONObject = new JSONObject(EntityUtils.toString(this.cS, "UTF-8"));
                    if (jSONObject != null) {
                        int intValue = Integer.valueOf(jSONObject.getString(dU)).intValue();
                        if (jSONObject.has(dW)) {
                            z2 = Integer.valueOf(jSONObject.getString(dW)).intValue();
                        }
                        if (intValue == 0) {
                            this.dP.i7 = null;
                            this.dP.jj = null;
                            au.for(f.getServiceContext()).b7();
                            if (this.dP.i3) {
                                if (z2) {
                                    aj();
                                } else if (!z2) {
                                    ah();
                                }
                            } else if (z2 && this.dR.try() == 1) {
                                aj();
                            } else if (!z2 && this.dR.try() == 0) {
                                ah();
                            }
                        }
                    }
                } catch (Exception e) {
                }
            }
        }
    }

    private class b extends ag {
        final /* synthetic */ a0 eC;

        private b(a0 a0Var) {
            this.eC = a0Var;
        }

        void at() {
        }

        void byte(Message message) {
        }
    }

    public class c extends BroadcastReceiver {
        final /* synthetic */ a0 a;

        public c(a0 a0Var) {
            this.a = a0Var;
        }

        public void onReceive(Context context, Intent intent) {
            this.a.byte(context);
            this.a.i5.post(this.a.i1);
        }
    }

    private class d implements Runnable, b {
        final /* synthetic */ a0 kQ;

        private d(a0 a0Var) {
            this.kQ = a0Var;
        }

        public void run() {
            try {
                List list = this.kQ.else(true);
                if (!this.kQ.i0.equals(this.kQ.i7) || this.kQ.cs() || !this.kQ.iX) {
                    this.kQ.i7 = this.kQ.i0;
                    this.kQ.jj = this.kQ.jm;
                    com.baidu.location.t.a ak = t.an().ak();
                    String j = Jni.j(String.format("%s|%s|%s|0", new Object[]{Integer.valueOf(ak.do), Integer.valueOf(ak.if), Integer.valueOf(ak.for)}));
                    this.kQ.iV = String.format("%s|%s|%s|0", new Object[]{Integer.valueOf(ak.do), Integer.valueOf(ak.if), Integer.valueOf(ak.for)});
                    this.kQ.jl = this.kQ.for(j, true);
                    this.kQ.iS = this.kQ.for(j, false);
                    this.kQ.for(list);
                    this.kQ.ct();
                }
            } catch (Exception e) {
                this.kQ.if(f.getServiceContext(), (int) a0.iU);
            }
        }
    }

    a0() {
    }

    private void byte(Context context) {
        if (this.i9 == null) {
            this.i9 = ((PowerManager) context.getSystemService("power")).newWakeLock(1, "GeofenceStrategyService");
            this.i9.setReferenceCounted(false);
            this.i9.acquire(60000);
        }
    }

    public static a0 cq() {
        if (jf == null) {
            jf = new a0();
            jf.cv();
        }
        return jf;
    }

    private Map cr() {
        Cursor cursor = null;
        Map hashMap = new HashMap();
        SQLiteDatabase readableDatabase = m.a(f.getServiceContext()).getReadableDatabase();
        try {
            long currentTimeMillis = System.currentTimeMillis();
            cursor = readableDatabase.rawQuery(String.format("SELECT a.ap, b.geofence_id FROM %s AS a LEFT JOIN %s AS b WHERE (a.geofence_id = b.geofence_id) AND ((b.valid_date + b.duration_millis) >= %d) AND (b.next_exit_active_time < %d) ", new Object[]{com.baidu.location.a.b.a, com.baidu.location.a.a.goto, Long.valueOf(currentTimeMillis), Long.valueOf(currentTimeMillis)}), null);
            if (cursor == null || cursor.getCount() <= 0) {
                if (cursor != null) {
                    cursor.close();
                }
                readableDatabase.close();
                return hashMap;
            }
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex("geofence_id");
            int columnIndex2 = cursor.getColumnIndex(com.baidu.location.a.b.for);
            do {
                String string = cursor.getString(columnIndex);
                String string2 = cursor.getString(columnIndex2);
                if (hashMap.containsKey(string)) {
                    ((List) hashMap.get(string)).add(string2);
                } else {
                    List arrayList = new ArrayList();
                    arrayList.add(string2);
                    hashMap.put(string, arrayList);
                }
            } while (cursor.moveToNext());
            if (cursor != null) {
                cursor.close();
            }
            readableDatabase.close();
            return hashMap;
        } catch (Exception e) {
            e.printStackTrace();
            if (cursor != null) {
                cursor.close();
            }
        } catch (Throwable th) {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    private boolean cs() {
        return this.jj == null ? true : this.jm == this.jj ? false : !this.jj.a(this.jm);
    }

    private void ct() {
        if (this.iX) {
            if(f.getServiceContext(), (int) iT);
        } else if (this.jc > 0) {
            if(f.getServiceContext(), this.jc >= 6 ? jb : this.jc * iT);
        } else if (this.jl == null || this.jl.size() <= 0) {
            if(f.getServiceContext(), (int) iU);
        } else {
            Object obj = null;
            for (aq aqVar : this.jl) {
                Object obj2;
                if (aqVar.if() || aqVar.for()) {
                    obj2 = obj;
                } else {
                    for(aqVar);
                    obj2 = 1;
                }
                obj = obj2;
            }
            if (this.iS != null && this.iS.size() > 0) {
                for (aq aqVar2 : this.iS) {
                    if (!(aqVar2.if() || aqVar2.for())) {
                        for(aqVar2);
                        obj = 1;
                    }
                }
            }
            if (obj != null) {
                if(f.getServiceContext(), (int) iT);
            } else {
                if(f.getServiceContext(), (int) jb);
            }
        }
    }

    private void cu() {
        if (this.i9 != null && this.i9.isHeld()) {
            this.i9.release();
            this.i9 = null;
        }
    }

    private void cv() {
        this.je = new HandlerThread("GeofenceStrategyService", 10);
        this.je.start();
        this.i5 = new Handler(this.je.getLooper());
        this.i1 = new d();
    }

    private List else(boolean z) {
        com.baidu.location.t.a ak = t.an().ak();
        this.jm = ar.bW().bS();
        List arrayList = new ArrayList();
        if (z) {
            this.i0 = String.format("%s|%s|%s|%s", new Object[]{Integer.valueOf(ak.do), Integer.valueOf(ak.if), Integer.valueOf(ak.for), Integer.valueOf(ak.try)});
            arrayList.add(Jni.j(this.i0));
        }
        if (this.jm != null) {
            List<ScanResult> list = this.jm.for;
            if (list != null) {
                for (ScanResult scanResult : list) {
                    if (scanResult != null) {
                        arrayList.add(Jni.j(scanResult.BSSID.replace(":", "")));
                    }
                }
            }
        }
        return arrayList;
    }

    private void for(List list) {
        if (list != null && list.size() != 0) {
            int i;
            aq aqVar;
            List list2;
            List<aq> arrayList;
            int size = list.size();
            for (i = 0; i < size; i++) {
                list.set(i, String.format("'%s'", new Object[]{list.get(i)}));
            }
            Collection collection = if(list, true);
            if (collection != null) {
                Iterator it = collection.iterator();
                while (it.hasNext()) {
                    aqVar = (aq) it.next();
                    Iterator it2 = this.i6.entrySet().iterator();
                    while (it2.hasNext()) {
                        Entry entry = (Entry) it2.next();
                        if (System.currentTimeMillis() - ((Long) entry.getValue()).longValue() >= i2) {
                            it2.remove();
                        }
                        if (aqVar.getGeofenceId().equals(entry.getKey())) {
                            it.remove();
                        }
                    }
                }
            }
            List<String> list3 = else(false);
            if (list3 != null && list3.size() > 0) {
                Map cr = cr();
                if (cr.size() > 0) {
                    for (String str : list3) {
                        Iterator it3 = cr.entrySet().iterator();
                        while (it3.hasNext()) {
                            if (((List) ((Entry) it3.next()).getValue()).contains(str)) {
                                it3.remove();
                                break;
                            }
                        }
                    }
                }
                if (cr.size() > 0) {
                    list3.clear();
                    list3.addAll(cr.keySet());
                    size = list3.size();
                    for (i = 0; i < size; i++) {
                        list3.set(i, String.format("'%s'", new Object[]{list3.get(i)}));
                    }
                    list2 = if((List) list3, false);
                    arrayList = new ArrayList();
                    if (collection != null) {
                        arrayList.addAll(collection);
                    }
                    if (r0 != null) {
                        for (aq aqVar2 : r0) {
                            if (arrayList.contains(aqVar2) || this.i3) {
                                arrayList.add(aqVar2);
                            }
                        }
                    }
                    if (arrayList != null || arrayList.size() <= 0) {
                        this.iX = false;
                    }
                    for (aq aqVar22 : arrayList) {
                        if (aqVar22 != null) {
                            this.iX = true;
                            for(aqVar22);
                            this.i7 = this.i0;
                            this.jj = this.jm;
                            this.jc = 0;
                        } else {
                            this.iX = false;
                            this.jc++;
                            this.jc = this.jc == ActivityChooserViewAdapter.MAX_ACTIVITY_COUNT_UNLIMITED ? 1 : this.jc;
                        }
                    }
                    return;
                }
            }
            list2 = null;
            arrayList = new ArrayList();
            if (collection != null) {
                arrayList.addAll(collection);
            }
            if (r0 != null) {
                for (aq aqVar222 : r0) {
                    if (arrayList.contains(aqVar222)) {
                    }
                    arrayList.add(aqVar222);
                }
            }
            if (arrayList != null) {
            }
            this.iX = false;
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.util.List for(java.lang.String r30, boolean r31) {
        /*
        r29 = this;
        r2 = com.baidu.location.f.getServiceContext();
        r2 = com.baidu.location.m.a(r2);
        r17 = r2.getReadableDatabase();
        r2 = 0;
        if (r17 == 0) goto L_0x0132;
    L_0x000f:
        r3 = 0;
        r4 = java.lang.System.currentTimeMillis();
        if (r31 == 0) goto L_0x0133;
    L_0x0016:
        r6 = "SELECT distinct b.geofence_id, b.longitude, b.latitude, b.radius, b.coord_type, b.duration_millis, b.is_lac, b.is_cell, b.is_wifi, b.radius_type FROM %s AS a LEFT JOIN %s AS b WHERE (a.geofence_id = b.geofence_id) AND (a.ap = '%s' AND  (b.valid_date + b.duration_millis >= %d) AND b.next_active_time < %d)";
        r7 = 5;
        r7 = new java.lang.Object[r7];	 Catch:{ Exception -> 0x0168, all -> 0x016f }
        r8 = 0;
        r9 = "geofence_detail";
        r7[r8] = r9;	 Catch:{ Exception -> 0x0168, all -> 0x016f }
        r8 = 1;
        r9 = "geofence";
        r7[r8] = r9;	 Catch:{ Exception -> 0x0168, all -> 0x016f }
        r8 = 2;
        r7[r8] = r30;	 Catch:{ Exception -> 0x0168, all -> 0x016f }
        r8 = 3;
        r9 = java.lang.Long.valueOf(r4);	 Catch:{ Exception -> 0x0168, all -> 0x016f }
        r7[r8] = r9;	 Catch:{ Exception -> 0x0168, all -> 0x016f }
        r8 = 4;
        r4 = java.lang.Long.valueOf(r4);	 Catch:{ Exception -> 0x0168, all -> 0x016f }
        r7[r8] = r4;	 Catch:{ Exception -> 0x0168, all -> 0x016f }
        r4 = java.lang.String.format(r6, r7);	 Catch:{ Exception -> 0x0168, all -> 0x016f }
    L_0x003d:
        r5 = 0;
        r0 = r17;
        r12 = r0.rawQuery(r4, r5);	 Catch:{ Exception -> 0x0168, all -> 0x016f }
        if (r12 == 0) goto L_0x012a;
    L_0x0046:
        r3 = r12.getCount();	 Catch:{ Exception -> 0x0179, all -> 0x0177 }
        if (r3 <= 0) goto L_0x012a;
    L_0x004c:
        r13 = new java.util.ArrayList;	 Catch:{ Exception -> 0x0179, all -> 0x0177 }
        r13.<init>();	 Catch:{ Exception -> 0x0179, all -> 0x0177 }
        r12.moveToFirst();	 Catch:{ Exception -> 0x017c, all -> 0x0177 }
        r2 = "geofence_id";
        r18 = r12.getColumnIndex(r2);	 Catch:{ Exception -> 0x017c, all -> 0x0177 }
        r2 = "longitude";
        r19 = r12.getColumnIndex(r2);	 Catch:{ Exception -> 0x017c, all -> 0x0177 }
        r2 = "latitude";
        r20 = r12.getColumnIndex(r2);	 Catch:{ Exception -> 0x017c, all -> 0x0177 }
        r2 = "radius";
        r21 = r12.getColumnIndex(r2);	 Catch:{ Exception -> 0x017c, all -> 0x0177 }
        r2 = "coord_type";
        r22 = r12.getColumnIndex(r2);	 Catch:{ Exception -> 0x017c, all -> 0x0177 }
        r2 = "duration_millis";
        r23 = r12.getColumnIndex(r2);	 Catch:{ Exception -> 0x017c, all -> 0x0177 }
        r2 = "is_lac";
        r24 = r12.getColumnIndex(r2);	 Catch:{ Exception -> 0x017c, all -> 0x0177 }
        r2 = "is_cell";
        r25 = r12.getColumnIndex(r2);	 Catch:{ Exception -> 0x017c, all -> 0x0177 }
        r2 = "is_wifi";
        r26 = r12.getColumnIndex(r2);	 Catch:{ Exception -> 0x017c, all -> 0x0177 }
        r2 = "radius_type";
        r27 = r12.getColumnIndex(r2);	 Catch:{ Exception -> 0x017c, all -> 0x0177 }
    L_0x009a:
        r0 = r18;
        r3 = r12.getString(r0);	 Catch:{ Exception -> 0x017c, all -> 0x0177 }
        r0 = r19;
        r2 = r12.getString(r0);	 Catch:{ Exception -> 0x017c, all -> 0x0177 }
        r2 = java.lang.Float.valueOf(r2);	 Catch:{ Exception -> 0x017c, all -> 0x0177 }
        r4 = r2.floatValue();	 Catch:{ Exception -> 0x017c, all -> 0x0177 }
        r0 = r20;
        r2 = r12.getString(r0);	 Catch:{ Exception -> 0x017c, all -> 0x0177 }
        r2 = java.lang.Float.valueOf(r2);	 Catch:{ Exception -> 0x017c, all -> 0x0177 }
        r6 = r2.floatValue();	 Catch:{ Exception -> 0x017c, all -> 0x0177 }
        r0 = r21;
        r2 = r12.getString(r0);	 Catch:{ Exception -> 0x017c, all -> 0x0177 }
        r2 = java.lang.Float.valueOf(r2);	 Catch:{ Exception -> 0x017c, all -> 0x0177 }
        r28 = r2.floatValue();	 Catch:{ Exception -> 0x017c, all -> 0x0177 }
        r0 = r22;
        r11 = r12.getString(r0);	 Catch:{ Exception -> 0x017c, all -> 0x0177 }
        r0 = r23;
        r9 = r12.getLong(r0);	 Catch:{ Exception -> 0x017c, all -> 0x0177 }
        r0 = r24;
        r2 = r12.getInt(r0);	 Catch:{ Exception -> 0x017c, all -> 0x0177 }
        if (r2 == 0) goto L_0x015c;
    L_0x00de:
        r2 = 1;
        r16 = r2;
    L_0x00e1:
        r0 = r25;
        r2 = r12.getInt(r0);	 Catch:{ Exception -> 0x017c, all -> 0x0177 }
        if (r2 == 0) goto L_0x0160;
    L_0x00e9:
        r2 = 1;
        r15 = r2;
    L_0x00eb:
        r0 = r26;
        r2 = r12.getInt(r0);	 Catch:{ Exception -> 0x017c, all -> 0x0177 }
        if (r2 == 0) goto L_0x0163;
    L_0x00f3:
        r2 = 1;
        r14 = r2;
    L_0x00f5:
        r0 = r27;
        r8 = r12.getInt(r0);	 Catch:{ Exception -> 0x017c, all -> 0x0177 }
        r2 = new com.baidu.location.aq;	 Catch:{ Exception -> 0x017c, all -> 0x0177 }
        r4 = (double) r4;	 Catch:{ Exception -> 0x017c, all -> 0x0177 }
        r6 = (double) r6;	 Catch:{ Exception -> 0x017c, all -> 0x0177 }
        r2.<init>(r3, r4, r6, r8, r9, r11);	 Catch:{ Exception -> 0x017c, all -> 0x0177 }
        if (r2 == 0) goto L_0x0120;
    L_0x0104:
        r0 = r28;
        r2.a(r0);	 Catch:{ Exception -> 0x017c, all -> 0x0177 }
        r0 = r16;
        r2.do(r0);	 Catch:{ Exception -> 0x017c, all -> 0x0177 }
        r2.a(r15);	 Catch:{ Exception -> 0x017c, all -> 0x0177 }
        r2.if(r14);	 Catch:{ Exception -> 0x017c, all -> 0x0177 }
        r0 = r29;
        r3 = r0.i3;	 Catch:{ Exception -> 0x017c, all -> 0x0177 }
        if (r3 != 0) goto L_0x0120;
    L_0x011a:
        if (r31 == 0) goto L_0x0166;
    L_0x011c:
        r3 = 1;
    L_0x011d:
        r2.a(r3);	 Catch:{ Exception -> 0x017c, all -> 0x0177 }
    L_0x0120:
        r13.add(r2);	 Catch:{ Exception -> 0x017c, all -> 0x0177 }
        r2 = r12.moveToNext();	 Catch:{ Exception -> 0x017c, all -> 0x0177 }
        if (r2 != 0) goto L_0x009a;
    L_0x0129:
        r2 = r13;
    L_0x012a:
        if (r12 == 0) goto L_0x012f;
    L_0x012c:
        r12.close();
    L_0x012f:
        r17.close();
    L_0x0132:
        return r2;
    L_0x0133:
        r6 = "SELECT distinct b.geofence_id, b.longitude, b.latitude, b.radius, b.coord_type, b.duration_millis, b.is_lac, b.is_cell, b.is_wifi, b.radius_type FROM %s AS a LEFT JOIN %s AS b WHERE (a.geofence_id = b.geofence_id) AND (a.ap <> '%s' AND  (b.valid_date + b.duration_millis >= %d) AND b.next_exit_active_time < %d)";
        r7 = 5;
        r7 = new java.lang.Object[r7];	 Catch:{ Exception -> 0x0168, all -> 0x016f }
        r8 = 0;
        r9 = "geofence_detail";
        r7[r8] = r9;	 Catch:{ Exception -> 0x0168, all -> 0x016f }
        r8 = 1;
        r9 = "geofence";
        r7[r8] = r9;	 Catch:{ Exception -> 0x0168, all -> 0x016f }
        r8 = 2;
        r7[r8] = r30;	 Catch:{ Exception -> 0x0168, all -> 0x016f }
        r8 = 3;
        r9 = java.lang.Long.valueOf(r4);	 Catch:{ Exception -> 0x0168, all -> 0x016f }
        r7[r8] = r9;	 Catch:{ Exception -> 0x0168, all -> 0x016f }
        r8 = 4;
        r4 = java.lang.Long.valueOf(r4);	 Catch:{ Exception -> 0x0168, all -> 0x016f }
        r7[r8] = r4;	 Catch:{ Exception -> 0x0168, all -> 0x016f }
        r4 = java.lang.String.format(r6, r7);	 Catch:{ Exception -> 0x0168, all -> 0x016f }
        goto L_0x003d;
    L_0x015c:
        r2 = 0;
        r16 = r2;
        goto L_0x00e1;
    L_0x0160:
        r2 = 0;
        r15 = r2;
        goto L_0x00eb;
    L_0x0163:
        r2 = 0;
        r14 = r2;
        goto L_0x00f5;
    L_0x0166:
        r3 = 0;
        goto L_0x011d;
    L_0x0168:
        r4 = move-exception;
    L_0x0169:
        if (r3 == 0) goto L_0x012f;
    L_0x016b:
        r3.close();
        goto L_0x012f;
    L_0x016f:
        r2 = move-exception;
        r12 = r3;
    L_0x0171:
        if (r12 == 0) goto L_0x0176;
    L_0x0173:
        r12.close();
    L_0x0176:
        throw r2;
    L_0x0177:
        r2 = move-exception;
        goto L_0x0171;
    L_0x0179:
        r3 = move-exception;
        r3 = r12;
        goto L_0x0169;
    L_0x017c:
        r2 = move-exception;
        r3 = r12;
        r2 = r13;
        goto L_0x0169;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.baidu.location.a0.for(java.lang.String, boolean):java.util.List");
    }

    public void for(aq aqVar) {
        new a(this, aqVar, Jni.i(new b().e(jk).replace(BDGeofence.COORD_TYPE_GCJ, aqVar.int()))).ai();
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.util.List if(java.util.List r30, boolean r31) {
        /*
        r29 = this;
        r2 = ",";
        r0 = r30;
        r4 = android.text.TextUtils.join(r2, r0);
        r2 = com.baidu.location.f.getServiceContext();
        r2 = com.baidu.location.m.a(r2);
        r17 = r2.getReadableDatabase();
        r2 = 0;
        if (r17 == 0) goto L_0x013e;
    L_0x0018:
        r3 = 0;
        r6 = java.lang.System.currentTimeMillis();	 Catch:{ Exception -> 0x0174, all -> 0x017b }
        if (r31 == 0) goto L_0x013f;
    L_0x001f:
        r5 = "SELECT distinct b.geofence_id, b.longitude, b.latitude, b.radius, b.coord_type, b.duration_millis, b.is_lac, b.is_cell, b.is_wifi, b.radius_type FROM %s AS a LEFT JOIN %s AS b WHERE (a.geofence_id = b.geofence_id) AND (a.ap IN (%s) AND  (b.valid_date + b.duration_millis) >= %d) AND (b.next_active_time < %d)";
        r8 = 5;
        r8 = new java.lang.Object[r8];	 Catch:{ Exception -> 0x0174, all -> 0x017b }
        r9 = 0;
        r10 = "geofence_detail";
        r8[r9] = r10;	 Catch:{ Exception -> 0x0174, all -> 0x017b }
        r9 = 1;
        r10 = "geofence";
        r8[r9] = r10;	 Catch:{ Exception -> 0x0174, all -> 0x017b }
        r9 = 2;
        r8[r9] = r4;	 Catch:{ Exception -> 0x0174, all -> 0x017b }
        r4 = 3;
        r9 = java.lang.Long.valueOf(r6);	 Catch:{ Exception -> 0x0174, all -> 0x017b }
        r8[r4] = r9;	 Catch:{ Exception -> 0x0174, all -> 0x017b }
        r4 = 4;
        r6 = java.lang.Long.valueOf(r6);	 Catch:{ Exception -> 0x0174, all -> 0x017b }
        r8[r4] = r6;	 Catch:{ Exception -> 0x0174, all -> 0x017b }
        r4 = java.lang.String.format(r5, r8);	 Catch:{ Exception -> 0x0174, all -> 0x017b }
    L_0x0046:
        r5 = 0;
        r0 = r17;
        r12 = r0.rawQuery(r4, r5);	 Catch:{ Exception -> 0x0174, all -> 0x017b }
        if (r12 == 0) goto L_0x0136;
    L_0x004f:
        r3 = r12.getCount();	 Catch:{ Exception -> 0x0185, all -> 0x0183 }
        if (r3 <= 0) goto L_0x0136;
    L_0x0055:
        r13 = new java.util.ArrayList;	 Catch:{ Exception -> 0x0185, all -> 0x0183 }
        r13.<init>();	 Catch:{ Exception -> 0x0185, all -> 0x0183 }
        r13.clear();	 Catch:{ Exception -> 0x0188, all -> 0x0183 }
        r12.moveToFirst();	 Catch:{ Exception -> 0x0188, all -> 0x0183 }
        r2 = "geofence_id";
        r18 = r12.getColumnIndex(r2);	 Catch:{ Exception -> 0x0188, all -> 0x0183 }
        r2 = "longitude";
        r19 = r12.getColumnIndex(r2);	 Catch:{ Exception -> 0x0188, all -> 0x0183 }
        r2 = "latitude";
        r20 = r12.getColumnIndex(r2);	 Catch:{ Exception -> 0x0188, all -> 0x0183 }
        r2 = "radius";
        r21 = r12.getColumnIndex(r2);	 Catch:{ Exception -> 0x0188, all -> 0x0183 }
        r2 = "coord_type";
        r22 = r12.getColumnIndex(r2);	 Catch:{ Exception -> 0x0188, all -> 0x0183 }
        r2 = "duration_millis";
        r23 = r12.getColumnIndex(r2);	 Catch:{ Exception -> 0x0188, all -> 0x0183 }
        r2 = "is_lac";
        r24 = r12.getColumnIndex(r2);	 Catch:{ Exception -> 0x0188, all -> 0x0183 }
        r2 = "is_cell";
        r25 = r12.getColumnIndex(r2);	 Catch:{ Exception -> 0x0188, all -> 0x0183 }
        r2 = "is_wifi";
        r26 = r12.getColumnIndex(r2);	 Catch:{ Exception -> 0x0188, all -> 0x0183 }
        r2 = "radius_type";
        r27 = r12.getColumnIndex(r2);	 Catch:{ Exception -> 0x0188, all -> 0x0183 }
    L_0x00a6:
        r0 = r18;
        r3 = r12.getString(r0);	 Catch:{ Exception -> 0x0188, all -> 0x0183 }
        r0 = r19;
        r2 = r12.getString(r0);	 Catch:{ Exception -> 0x0188, all -> 0x0183 }
        r2 = java.lang.Float.valueOf(r2);	 Catch:{ Exception -> 0x0188, all -> 0x0183 }
        r4 = r2.floatValue();	 Catch:{ Exception -> 0x0188, all -> 0x0183 }
        r0 = r20;
        r2 = r12.getString(r0);	 Catch:{ Exception -> 0x0188, all -> 0x0183 }
        r2 = java.lang.Float.valueOf(r2);	 Catch:{ Exception -> 0x0188, all -> 0x0183 }
        r6 = r2.floatValue();	 Catch:{ Exception -> 0x0188, all -> 0x0183 }
        r0 = r21;
        r2 = r12.getString(r0);	 Catch:{ Exception -> 0x0188, all -> 0x0183 }
        r2 = java.lang.Float.valueOf(r2);	 Catch:{ Exception -> 0x0188, all -> 0x0183 }
        r28 = r2.floatValue();	 Catch:{ Exception -> 0x0188, all -> 0x0183 }
        r0 = r22;
        r11 = r12.getString(r0);	 Catch:{ Exception -> 0x0188, all -> 0x0183 }
        r0 = r23;
        r9 = r12.getLong(r0);	 Catch:{ Exception -> 0x0188, all -> 0x0183 }
        r0 = r24;
        r2 = r12.getInt(r0);	 Catch:{ Exception -> 0x0188, all -> 0x0183 }
        if (r2 == 0) goto L_0x0168;
    L_0x00ea:
        r2 = 1;
        r16 = r2;
    L_0x00ed:
        r0 = r25;
        r2 = r12.getInt(r0);	 Catch:{ Exception -> 0x0188, all -> 0x0183 }
        if (r2 == 0) goto L_0x016c;
    L_0x00f5:
        r2 = 1;
        r15 = r2;
    L_0x00f7:
        r0 = r26;
        r2 = r12.getInt(r0);	 Catch:{ Exception -> 0x0188, all -> 0x0183 }
        if (r2 == 0) goto L_0x016f;
    L_0x00ff:
        r2 = 1;
        r14 = r2;
    L_0x0101:
        r0 = r27;
        r8 = r12.getInt(r0);	 Catch:{ Exception -> 0x0188, all -> 0x0183 }
        r2 = new com.baidu.location.aq;	 Catch:{ Exception -> 0x0188, all -> 0x0183 }
        r4 = (double) r4;	 Catch:{ Exception -> 0x0188, all -> 0x0183 }
        r6 = (double) r6;	 Catch:{ Exception -> 0x0188, all -> 0x0183 }
        r2.<init>(r3, r4, r6, r8, r9, r11);	 Catch:{ Exception -> 0x0188, all -> 0x0183 }
        if (r2 == 0) goto L_0x012c;
    L_0x0110:
        r0 = r28;
        r2.a(r0);	 Catch:{ Exception -> 0x0188, all -> 0x0183 }
        r0 = r16;
        r2.do(r0);	 Catch:{ Exception -> 0x0188, all -> 0x0183 }
        r2.a(r15);	 Catch:{ Exception -> 0x0188, all -> 0x0183 }
        r2.if(r14);	 Catch:{ Exception -> 0x0188, all -> 0x0183 }
        r0 = r29;
        r3 = r0.i3;	 Catch:{ Exception -> 0x0188, all -> 0x0183 }
        if (r3 != 0) goto L_0x012c;
    L_0x0126:
        if (r31 == 0) goto L_0x0172;
    L_0x0128:
        r3 = 1;
    L_0x0129:
        r2.a(r3);	 Catch:{ Exception -> 0x0188, all -> 0x0183 }
    L_0x012c:
        r13.add(r2);	 Catch:{ Exception -> 0x0188, all -> 0x0183 }
        r2 = r12.moveToNext();	 Catch:{ Exception -> 0x0188, all -> 0x0183 }
        if (r2 != 0) goto L_0x00a6;
    L_0x0135:
        r2 = r13;
    L_0x0136:
        if (r12 == 0) goto L_0x013b;
    L_0x0138:
        r12.close();
    L_0x013b:
        r17.close();
    L_0x013e:
        return r2;
    L_0x013f:
        r5 = "SELECT distinct b.geofence_id, b.longitude, b.latitude, b.radius, b.coord_type, b.duration_millis, b.is_lac, b.is_cell, b.is_wifi, b.radius_type FROM %s AS a LEFT JOIN %s AS b WHERE (a.geofence_id = b.geofence_id) AND (b.geofence_id IN (%s) AND  (b.valid_date + b.duration_millis) >= %d) AND (b.next_exit_active_time < %d)";
        r8 = 5;
        r8 = new java.lang.Object[r8];	 Catch:{ Exception -> 0x0174, all -> 0x017b }
        r9 = 0;
        r10 = "geofence_detail";
        r8[r9] = r10;	 Catch:{ Exception -> 0x0174, all -> 0x017b }
        r9 = 1;
        r10 = "geofence";
        r8[r9] = r10;	 Catch:{ Exception -> 0x0174, all -> 0x017b }
        r9 = 2;
        r8[r9] = r4;	 Catch:{ Exception -> 0x0174, all -> 0x017b }
        r4 = 3;
        r9 = java.lang.Long.valueOf(r6);	 Catch:{ Exception -> 0x0174, all -> 0x017b }
        r8[r4] = r9;	 Catch:{ Exception -> 0x0174, all -> 0x017b }
        r4 = 4;
        r6 = java.lang.Long.valueOf(r6);	 Catch:{ Exception -> 0x0174, all -> 0x017b }
        r8[r4] = r6;	 Catch:{ Exception -> 0x0174, all -> 0x017b }
        r4 = java.lang.String.format(r5, r8);	 Catch:{ Exception -> 0x0174, all -> 0x017b }
        goto L_0x0046;
    L_0x0168:
        r2 = 0;
        r16 = r2;
        goto L_0x00ed;
    L_0x016c:
        r2 = 0;
        r15 = r2;
        goto L_0x00f7;
    L_0x016f:
        r2 = 0;
        r14 = r2;
        goto L_0x0101;
    L_0x0172:
        r3 = 0;
        goto L_0x0129;
    L_0x0174:
        r4 = move-exception;
    L_0x0175:
        if (r3 == 0) goto L_0x013b;
    L_0x0177:
        r3.close();
        goto L_0x013b;
    L_0x017b:
        r2 = move-exception;
        r12 = r3;
    L_0x017d:
        if (r12 == 0) goto L_0x0182;
    L_0x017f:
        r12.close();
    L_0x0182:
        throw r2;
    L_0x0183:
        r2 = move-exception;
        goto L_0x017d;
    L_0x0185:
        r3 = move-exception;
        r3 = r12;
        goto L_0x0175;
    L_0x0188:
        r2 = move-exception;
        r3 = r12;
        r2 = r13;
        goto L_0x0175;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.baidu.location.a0.if(java.util.List, boolean):java.util.List");
    }

    public void if(Context context, int i) {
        Intent intent = new Intent(ja);
        PendingIntent broadcast = PendingIntent.getBroadcast(context, 0, intent, 134217728);
        if (i <= 0) {
            i.a(context, broadcast);
            context.sendBroadcast(intent);
            return;
        }
        i.a(context, broadcast, i);
    }

    public void if(Context context, Message message) {
        if (!this.iY) {
            this.jg = message.replyTo;
            this.iY = true;
            this.i8 = new c(this);
            context.registerReceiver(this.i8, new IntentFilter(ja));
            if(context, 0);
        }
    }

    public void new(Context context) {
        if(context, null);
    }

    public void try(Context context) {
        this.iY = false;
        i.a(context, PendingIntent.getBroadcast(context, 0, new Intent(ja), 134217728));
        cu();
        if (this.i8 != null) {
            try {
                context.unregisterReceiver(this.i8);
            } catch (Exception e) {
            }
        }
    }
}
