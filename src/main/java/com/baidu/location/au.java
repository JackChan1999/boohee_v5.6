package com.baidu.location;

import android.content.ContentValues;
import android.content.Context;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import com.baidu.location.GeofenceClient.OnAddBDGeofencesResultListener;
import com.baidu.location.GeofenceClient.OnRemoveBDGeofencesResultListener;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

class au implements ax, n {
    private static final String h2 = "GeofenceMan";
    private static final int h3 = 3;
    public static final int h4 = 10;
    private static final String h5 = "http://loc.map.baidu.com/fence";
    private static final String h6 = "geofence_id";
    private static final String h7 = ";";
    private static final String h9 = "status_code";
    private static final String i = "GeofenceMan";
    private static au ia = null;
    private static final int ic = 5;
    private static final int id = 2;
    private static final int ie = 1;
    private static final String ih = "geofence_ids";
    private Context h1;
    private Object h8 = new Object();
    private HandlerThread ib;
    private a ig;

    private class a extends Handler {
        public static final int do = 2;
        public static final int for = 3;
        public static final int if = 0;
        final /* synthetic */ au a;

        public a(au auVar, Looper looper) {
            this.a = auVar;
            super(looper);
        }

        public void handleMessage(Message message) {
            super.handleMessage(message);
            int i = message.what;
            Bundle data = message.getData();
            switch (i) {
                case 0:
                    String string;
                    if (data != null) {
                        i = data.getInt(au.h9, 1);
                        string = data.getString("geofence_id");
                    } else {
                        i = 1;
                        string = null;
                    }
                    this.a.if(i, string, (OnAddBDGeofencesResultListener) message.obj);
                    return;
                case 2:
                    String[] stringArray;
                    if (data != null) {
                        i = data.getInt(au.h9, 1);
                        stringArray = data.getStringArray(au.ih);
                    } else {
                        i = 1;
                        stringArray = null;
                    }
                    this.a.if(i, stringArray, (OnRemoveBDGeofencesResultListener) message.obj);
                    return;
                case 3:
                    this.a.b7();
                    return;
                default:
                    return;
            }
        }
    }

    class b extends s {
        private static final String dE = "error";
        private static final int dF = -3;
        private static final String dG = "ext";
        private static final String dH = "cl";
        private static final String dI = "fence";
        private static final String dJ = "lac";
        private static final String dL = "wf";
        private static final String dN = "radius";
        private OnAddBDGeofencesResultListener dD;
        final /* synthetic */ au dK;
        private int dM;
        private final aq dO;

        public b(au auVar, aq aqVar, OnAddBDGeofencesResultListener onAddBDGeofencesResultListener) {
            this.dK = auVar;
            this.dO = aqVar;
            this.dD = onAddBDGeofencesResultListener;
            this.cT = new ArrayList();
        }

        public void T() {
            this.cR = "http://loc.map.baidu.com/fence";
            DecimalFormat decimalFormat = new DecimalFormat("0.00000");
            this.cT.add(new BasicNameValuePair(dI, Jni.i(String.format("&x=%s&y=%s&r=%s&coord=%s&type=%s&cu=%s&sdk=%s", new Object[]{decimalFormat.format(this.dO.a()), decimalFormat.format(this.dO.case()), String.valueOf(this.dO.new()), String.valueOf(this.dO.int()), Integer.valueOf(au.do(this.dK.h1)), com.baidu.location.b.a.a.if(this.dK.h1), Float.valueOf(n.V)}))));
            this.cT.add(new BasicNameValuePair("ext", Jni.i(String.format("&ki=%s&sn=%s", new Object[]{v.a(this.dK.h1), v.if(this.dK.h1)}))));
        }

        public void ag() {
            N();
        }

        public void do(boolean z) {
            if (!z || this.cS == null) {
                this.dK.if(this.dD, 1, this.dO.getGeofenceId());
                return;
            }
            Object obj = null;
            try {
                JSONObject jSONObject = new JSONObject(EntityUtils.toString(this.cS, "UTF-8"));
                if (jSONObject != null) {
                    Object string;
                    StringBuilder stringBuilder = new StringBuilder();
                    if (jSONObject.has(dJ)) {
                        string = jSONObject.getString(dJ);
                        if (!TextUtils.isEmpty(string)) {
                            stringBuilder.append(string);
                            this.dO.do(true);
                        }
                    }
                    if (jSONObject.has(dH)) {
                        string = jSONObject.getString(dH);
                        if (!TextUtils.isEmpty(string)) {
                            stringBuilder.append(string);
                            this.dO.a(true);
                        }
                    }
                    if (jSONObject.has(dL)) {
                        string = jSONObject.getString(dL);
                        if (!TextUtils.isEmpty(string)) {
                            stringBuilder.append(string);
                            this.dO.if(true);
                        }
                    }
                    obj = stringBuilder.toString();
                    if (jSONObject.has("radius")) {
                        this.dO.a(Float.valueOf(jSONObject.getString("radius")).floatValue());
                    }
                    if (jSONObject.has(dE)) {
                        this.dM = Integer.valueOf(jSONObject.getString(dE)).intValue();
                    }
                }
                if (!TextUtils.isEmpty(obj)) {
                    this.dK.ig.post(new c(this.dK, this.dO, obj, this.dD));
                } else if (this.dM == -3) {
                    this.dK.if(this.dD, 1002, this.dO.getGeofenceId());
                } else {
                    this.dK.if(this.dD, this.dM, this.dO.getGeofenceId());
                }
            } catch (Exception e) {
                this.dK.if(this.dD, 1, this.dO.getGeofenceId());
            }
        }
    }

    private class c implements Runnable {
        final /* synthetic */ au a;
        private final aq do;
        private final OnAddBDGeofencesResultListener for;
        private final String if;

        public c(au auVar, aq aqVar, String str, OnAddBDGeofencesResultListener onAddBDGeofencesResultListener) {
            this.a = auVar;
            this.do = aqVar;
            this.if = str;
            this.for = onAddBDGeofencesResultListener;
        }

        public void run() {
            this.a.if(this.for, this.a.if(this.do, this.if), this.do.getGeofenceId());
            if (f.getServiceContext() != null) {
                a0.cq().for(this.do);
            }
        }
    }

    private class d implements Runnable {
        final /* synthetic */ au a;
        private final OnRemoveBDGeofencesResultListener do;
        private final List if;

        public d(au auVar, List list, OnRemoveBDGeofencesResultListener onRemoveBDGeofencesResultListener) {
            this.a = auVar;
            this.if = list;
            this.do = onRemoveBDGeofencesResultListener;
        }

        public void run() {
            int i = this.a.do(this.if);
            Message obtain = Message.obtain(this.a.ig);
            obtain.what = 2;
            obtain.obj = this.do;
            Bundle bundle = new Bundle();
            bundle.putInt(au.h9, i);
            bundle.putStringArray(au.ih, (String[]) this.if.toArray(new String[this.if.size()]));
            obtain.setData(bundle);
            this.a.ig.sendMessage(obtain);
        }
    }

    au() {
    }

    private void b8() {
        this.ib = new HandlerThread("GeofenceMan", 10);
        this.ib.start();
        this.ig = new a(this, this.ib.getLooper());
    }

    private synchronized void b9() {
        SQLiteDatabase writableDatabase = m.a(this.h1).getWritableDatabase();
        if (writableDatabase != null) {
            writableDatabase.beginTransaction();
            try {
                long currentTimeMillis = System.currentTimeMillis();
                writableDatabase.execSQL(String.format("DELETE FROM %s WHERE EXISTS (SELECT * FROM %s WHERE (%s + %s) < %d)", new Object[]{com.baidu.location.a.b.a, com.baidu.location.a.a.goto, com.baidu.location.a.a.if, com.baidu.location.a.a.b, Long.valueOf(currentTimeMillis)}));
                writableDatabase.execSQL(String.format("DELETE FROM %s WHERE (%s + %s) < %d", new Object[]{com.baidu.location.a.a.goto, com.baidu.location.a.a.if, com.baidu.location.a.a.b, Long.valueOf(currentTimeMillis)}));
                writableDatabase.setTransactionSuccessful();
                writableDatabase.endTransaction();
                writableDatabase.close();
            } catch (Exception e) {
                writableDatabase.endTransaction();
                writableDatabase.close();
            } catch (Throwable th) {
                writableDatabase.endTransaction();
                writableDatabase.close();
            }
        }
    }

    private void ca() {
        this.ig.sendEmptyMessage(3);
    }

    private final void cb() {
        if (!s.if(this.h1)) {
            throw new IllegalStateException("Not net connection");
        }
    }

    private synchronized long cc() {
        long j;
        j = 0;
        try {
            SQLiteDatabase readableDatabase = m.a(this.h1).getReadableDatabase();
            if (readableDatabase != null) {
                j = DatabaseUtils.queryNumEntries(readableDatabase, com.baidu.location.a.a.goto);
                readableDatabase.close();
            }
        } catch (Exception e) {
        }
        return j;
    }

    public static int do(Context context) {
        String subscriberId = ((TelephonyManager) context.getSystemService("phone")).getSubscriberId();
        if (subscriberId != null) {
            if (subscriberId.startsWith("46000") || subscriberId.startsWith("46002") || subscriberId.startsWith("46007")) {
                return 1;
            }
            if (subscriberId.startsWith("46001")) {
                return 2;
            }
            if (subscriberId.startsWith("46003")) {
                return 3;
            }
        }
        return 5;
    }

    private synchronized int do(List list) {
        int i;
        SQLiteDatabase writableDatabase = m.a(this.h1).getWritableDatabase();
        if (writableDatabase != null) {
            writableDatabase.beginTransaction();
            try {
                Iterator it = list.iterator();
                while (it.hasNext()) {
                    String[] strArr = new String[]{(String) it.next()};
                    writableDatabase.delete(com.baidu.location.a.a.goto, String.format("%s=?", new Object[]{"geofence_id"}), strArr);
                    writableDatabase.delete(com.baidu.location.a.b.a, String.format("%s=?", new Object[]{"geofence_id"}), strArr);
                }
                writableDatabase.setTransactionSuccessful();
                writableDatabase.endTransaction();
                i = 0;
            } catch (Exception e) {
                writableDatabase.endTransaction();
                i = 1;
            } catch (Throwable th) {
                writableDatabase.endTransaction();
            }
            writableDatabase.close();
        } else {
            i = 0;
        }
        return i;
    }

    public static au for(Context context) {
        if (ia == null) {
            ia = new au();
            ia.b8();
            ia.h1 = context;
        }
        return ia;
    }

    private synchronized int if(aq aqVar, String str) {
        int i = 0;
        synchronized (this) {
            SQLiteDatabase writableDatabase = m.a(this.h1).getWritableDatabase();
            if (writableDatabase != null) {
                writableDatabase.beginTransaction();
                long currentTimeMillis = System.currentTimeMillis();
                try {
                    ContentValues contentValues = new ContentValues();
                    String geofenceId = aqVar.getGeofenceId();
                    contentValues.put("geofence_id", geofenceId);
                    contentValues.put(com.baidu.location.a.a.char, Double.valueOf(aqVar.a()));
                    contentValues.put(com.baidu.location.a.a.int, Double.valueOf(aqVar.case()));
                    contentValues.put(com.baidu.location.a.a.else, Float.valueOf(aqVar.do()));
                    contentValues.put(com.baidu.location.a.a.byte, Integer.valueOf(aqVar.new()));
                    contentValues.put(com.baidu.location.a.a.if, Long.valueOf(currentTimeMillis));
                    contentValues.put(com.baidu.location.a.a.b, Long.valueOf(aqVar.goto()));
                    contentValues.put(com.baidu.location.a.a.new, aqVar.int());
                    contentValues.put(com.baidu.location.a.a.case, Integer.valueOf(aqVar.byte() ? 1 : 0));
                    contentValues.put(com.baidu.location.a.a.void, Integer.valueOf(aqVar.if() ? 1 : 0));
                    contentValues.put(com.baidu.location.a.a.long, Integer.valueOf(aqVar.for() ? 1 : 0));
                    contentValues.put(com.baidu.location.a.a.try, Integer.valueOf(0));
                    contentValues.put(com.baidu.location.a.a.a, Integer.valueOf(0));
                    writableDatabase.insert(com.baidu.location.a.a.goto, null, contentValues);
                    for (String str2 : str.split(";")) {
                        String str22;
                        ContentValues contentValues2 = new ContentValues();
                        contentValues2.put("geofence_id", geofenceId);
                        contentValues2.put(com.baidu.location.a.b.int, str22);
                        int lastIndexOf = str22.lastIndexOf("|");
                        if (lastIndexOf != -1) {
                            str22 = str22.substring(0, lastIndexOf);
                        }
                        contentValues2.put(com.baidu.location.a.b.for, str22);
                        writableDatabase.insert(com.baidu.location.a.b.a, null, contentValues2);
                    }
                    writableDatabase.setTransactionSuccessful();
                    writableDatabase.endTransaction();
                } catch (Exception e) {
                    writableDatabase.endTransaction();
                    i = 1;
                } catch (Throwable th) {
                    writableDatabase.endTransaction();
                }
                writableDatabase.close();
            }
        }
        return i;
    }

    private void if(int i, String str, OnAddBDGeofencesResultListener onAddBDGeofencesResultListener) {
        if (i == 1) {
            onAddBDGeofencesResultListener.onAddBDGeofencesResult(i, str);
        } else {
            onAddBDGeofencesResultListener.onAddBDGeofencesResult(i, str);
        }
    }

    private void if(int i, String[] strArr, OnRemoveBDGeofencesResultListener onRemoveBDGeofencesResultListener) {
        onRemoveBDGeofencesResultListener.onRemoveBDGeofencesByRequestIdsResult(i, strArr);
    }

    private void if(OnAddBDGeofencesResultListener onAddBDGeofencesResultListener, int i, String str) {
        Message obtain = Message.obtain(this.ig);
        obtain.what = 0;
        obtain.obj = onAddBDGeofencesResultListener;
        Bundle bundle = new Bundle();
        bundle.putInt(h9, i);
        bundle.putString("geofence_id", str);
        obtain.setData(bundle);
        this.ig.sendMessage(obtain);
    }

    public static void int(Context context) {
        a0.cq().try(f.getServiceContext());
    }

    public void b7() {
        synchronized (this.h8) {
            this.ig.post(new Runnable(this) {
                final /* synthetic */ au a;

                {
                    this.a = r1;
                }

                public void run() {
                    this.a.b9();
                }
            });
        }
    }

    public void do(final aq aqVar) {
        this.ig.post(new Runnable(this) {
            final /* synthetic */ au a;

            public void run() {
                this.a.do(aqVar.getGeofenceId(), true);
            }
        });
    }

    public synchronized void do(String str, boolean z) {
        long j = a0.i2;
        synchronized (this) {
            if (!TextUtils.isEmpty(str)) {
                SQLiteDatabase writableDatabase = m.a(this.h1).getWritableDatabase();
                if (writableDatabase != null) {
                    try {
                        ContentValues contentValues = new ContentValues();
                        String str2;
                        long currentTimeMillis;
                        if (z) {
                            str2 = com.baidu.location.a.a.try;
                            currentTimeMillis = System.currentTimeMillis();
                            if (!(GeofenceClient.d() == 0 || GeofenceClient.d() == a0.i2)) {
                                j = GeofenceClient.d();
                            }
                            contentValues.put(str2, Long.valueOf(j + currentTimeMillis));
                        } else {
                            str2 = com.baidu.location.a.a.a;
                            currentTimeMillis = System.currentTimeMillis();
                            if (!(GeofenceClient.d() == 0 || GeofenceClient.d() == a0.i2)) {
                                j = GeofenceClient.d();
                            }
                            contentValues.put(str2, Long.valueOf(j + currentTimeMillis));
                        }
                        writableDatabase.update(com.baidu.location.a.a.goto, contentValues, "geofence_id= ?", new String[]{str});
                    } catch (Exception e) {
                    } finally {
                        writableDatabase.close();
                    }
                }
            }
        }
    }

    public void if(final aq aqVar) {
        this.ig.post(new Runnable(this) {
            final /* synthetic */ au a;

            public void run() {
                this.a.do(aqVar.getGeofenceId(), false);
            }
        });
    }

    public void if(aq aqVar, OnAddBDGeofencesResultListener onAddBDGeofencesResultListener) {
        cb();
        an.a((Object) onAddBDGeofencesResultListener, (Object) "OnAddBDGeofenceRecesResultListener not provided.");
        if (cc() >= 10) {
            onAddBDGeofencesResultListener.onAddBDGeofencesResult(1001, aqVar.getGeofenceId());
            return;
        }
        new b(this, aqVar, onAddBDGeofencesResultListener).ag();
        ca();
    }

    public void if(List list, OnRemoveBDGeofencesResultListener onRemoveBDGeofencesResultListener) {
        boolean z = list != null && list.size() > 0;
        an.if(z, "geofenceRequestIds can't be null nor empty.");
        an.a((Object) onRemoveBDGeofencesResultListener, (Object) "onRemoveBDGeofencesResultListener not provided.");
        this.ig.post(new d(this, list, onRemoveBDGeofencesResultListener));
    }
}
