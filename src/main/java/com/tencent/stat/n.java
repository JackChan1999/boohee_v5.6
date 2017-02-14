package com.tencent.stat;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.os.Handler;
import android.os.HandlerThread;

import com.boohee.utility.Const;
import com.boohee.utils.Utils;
import com.tencent.stat.a.e;
import com.tencent.stat.common.StatLogger;
import com.tencent.stat.common.k;
import com.umeng.socialize.net.utils.SocializeProtocolConstants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class n {
    private static StatLogger e = k.b();
    private static n          f = null;
    Handler a = null;
    volatile int b = 0;
    DeviceInfo c = null;
    private w d;
    private HashMap<String, String> g = new HashMap();

    private n(Context context) {
        try {
            HandlerThread handlerThread = new HandlerThread("StatStore");
            handlerThread.start();
            e.w("Launch store thread:" + handlerThread);
            this.a = new Handler(handlerThread.getLooper());
            Context applicationContext = context.getApplicationContext();
            this.d = new w(applicationContext);
            this.d.getWritableDatabase();
            this.d.getReadableDatabase();
            b(applicationContext);
            c();
            f();
            this.a.post(new o(this));
        } catch (Object th) {
            e.e(th);
        }
    }

    public static synchronized n a(Context context) {
        n nVar;
        synchronized (n.class) {
            if (f == null) {
                f = new n(context);
            }
            nVar = f;
        }
        return nVar;
    }

    public static n b() {
        return f;
    }

    private synchronized void b(int i) {
        try {
            if (this.b > 0 && i > 0) {
                e.i("Load " + Integer.toString(this.b) + " unsent events");
                List arrayList = new ArrayList();
                List<x> arrayList2 = new ArrayList();
                if (i == -1 || i > StatConfig.a()) {
                    i = StatConfig.a();
                }
                this.b -= i;
                c(arrayList2, i);
                e.i("Peek " + Integer.toString(arrayList2.size()) + " unsent events.");
                if (!arrayList2.isEmpty()) {
                    b((List) arrayList2, 2);
                    for (x xVar : arrayList2) {
                        arrayList.add(xVar.b);
                    }
                    d.b().b(arrayList, new u(this, arrayList2, i));
                }
            }
        } catch (Object th) {
            e.e(th);
        }
    }

    private synchronized void b(e eVar, c cVar) {
        if (StatConfig.getMaxStoreEventCount() > 0) {
            try {
                this.d.getWritableDatabase().beginTransaction();
                if (this.b > StatConfig.getMaxStoreEventCount()) {
                    e.warn("Too many events stored in db.");
                    this.b -= this.d.getWritableDatabase().delete("events", "event_id in (select " +
                            "event_id from events where timestamp in (select min(timestamp) from " +
                            "events) limit 1)", null);
                }
                ContentValues contentValues = new ContentValues();
                String c = k.c(eVar.d());
                contentValues.put(Utils.RESPONSE_CONTENT, c);
                contentValues.put("send_count", "0");
                contentValues.put("status", Integer.toString(1));
                contentValues.put("timestamp", Long.valueOf(eVar.b()));
                if (this.d.getWritableDatabase().insert("events", null, contentValues) == -1) {
                    e.error("Failed to store event:" + c);
                } else {
                    this.b++;
                    this.d.getWritableDatabase().setTransactionSuccessful();
                    if (cVar != null) {
                        cVar.a();
                    }
                }
                try {
                    this.d.getWritableDatabase().endTransaction();
                } catch (Throwable th) {
                }
            } catch (Throwable th2) {
            }
        }
        return;
    }

    private synchronized void b(b bVar) {
        Cursor query;
        Throwable th;
        Object obj;
        try {
            long update;
            String a = bVar.a();
            String a2 = k.a(a);
            ContentValues contentValues = new ContentValues();
            contentValues.put(Utils.RESPONSE_CONTENT, bVar.b.toString());
            contentValues.put("md5sum", a2);
            bVar.c = a2;
            contentValues.put("version", Integer.valueOf(bVar.d));
            query = this.d.getReadableDatabase().query("config", null, null, null, null, null,
                    null);
            do {
                try {
                    if (!query.moveToNext()) {
                        obj = null;
                        break;
                    }
                } catch (Throwable th2) {
                    obj = th2;
                }
            } while (query.getInt(0) != bVar.a);
            obj = 1;
            if (1 == obj) {
                update = (long) this.d.getWritableDatabase().update("config", contentValues,
                        "type=?", new String[]{Integer.toString(bVar.a)});
            } else {
                contentValues.put("type", Integer.valueOf(bVar.a));
                update = this.d.getWritableDatabase().insert("config", null, contentValues);
            }
            if (update == -1) {
                e.e("Failed to store cfg:" + a);
            } else {
                e.d("Sucessed to store cfg:" + a);
            }
            if (query != null) {
                query.close();
            }
        } catch (Throwable th3) {
            th = th3;
            query = null;
            if (query != null) {
                query.close();
            }
            throw th;
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private synchronized void b(java.util.List<com.tencent.stat.x> r11) {
        /*
        r10 = this;
        monitor-enter(r10);
        r0 = e;	 Catch:{ all -> 0x00a4 }
        r1 = new java.lang.StringBuilder;	 Catch:{ all -> 0x00a4 }
        r1.<init>();	 Catch:{ all -> 0x00a4 }
        r2 = "Delete ";
        r1 = r1.append(r2);	 Catch:{ all -> 0x00a4 }
        r2 = r11.size();	 Catch:{ all -> 0x00a4 }
        r1 = r1.append(r2);	 Catch:{ all -> 0x00a4 }
        r2 = " sent events in thread:";
        r1 = r1.append(r2);	 Catch:{ all -> 0x00a4 }
        r2 = java.lang.Thread.currentThread();	 Catch:{ all -> 0x00a4 }
        r1 = r1.append(r2);	 Catch:{ all -> 0x00a4 }
        r1 = r1.toString();	 Catch:{ all -> 0x00a4 }
        r0.i(r1);	 Catch:{ all -> 0x00a4 }
        r0 = r10.d;	 Catch:{ Throwable -> 0x0069 }
        r0 = r0.getWritableDatabase();	 Catch:{ Throwable -> 0x0069 }
        r0.beginTransaction();	 Catch:{ Throwable -> 0x0069 }
        r1 = r11.iterator();	 Catch:{ Throwable -> 0x0069 }
    L_0x003a:
        r0 = r1.hasNext();	 Catch:{ Throwable -> 0x0069 }
        if (r0 == 0) goto L_0x007a;
    L_0x0040:
        r0 = r1.next();	 Catch:{ Throwable -> 0x0069 }
        r0 = (com.tencent.stat.x) r0;	 Catch:{ Throwable -> 0x0069 }
        r2 = r10.b;	 Catch:{ Throwable -> 0x0069 }
        r3 = r10.d;	 Catch:{ Throwable -> 0x0069 }
        r3 = r3.getWritableDatabase();	 Catch:{ Throwable -> 0x0069 }
        r4 = "events";
        r5 = "event_id = ?";
        r6 = 1;
        r6 = new java.lang.String[r6];	 Catch:{ Throwable -> 0x0069 }
        r7 = 0;
        r8 = r0.a;	 Catch:{ Throwable -> 0x0069 }
        r0 = java.lang.Long.toString(r8);	 Catch:{ Throwable -> 0x0069 }
        r6[r7] = r0;	 Catch:{ Throwable -> 0x0069 }
        r0 = r3.delete(r4, r5, r6);	 Catch:{ Throwable -> 0x0069 }
        r0 = r2 - r0;
        r10.b = r0;	 Catch:{ Throwable -> 0x0069 }
        goto L_0x003a;
    L_0x0069:
        r0 = move-exception;
        r1 = e;	 Catch:{ all -> 0x00ae }
        r1.e(r0);	 Catch:{ all -> 0x00ae }
        r0 = r10.d;	 Catch:{ SQLiteException -> 0x00a7 }
        r0 = r0.getWritableDatabase();	 Catch:{ SQLiteException -> 0x00a7 }
        r0.endTransaction();	 Catch:{ SQLiteException -> 0x00a7 }
    L_0x0078:
        monitor-exit(r10);
        return;
    L_0x007a:
        r0 = r10.d;	 Catch:{ Throwable -> 0x0069 }
        r0 = r0.getWritableDatabase();	 Catch:{ Throwable -> 0x0069 }
        r0.setTransactionSuccessful();	 Catch:{ Throwable -> 0x0069 }
        r0 = r10.d;	 Catch:{ Throwable -> 0x0069 }
        r0 = r0.getReadableDatabase();	 Catch:{ Throwable -> 0x0069 }
        r1 = "events";
        r0 = android.database.DatabaseUtils.queryNumEntries(r0, r1);	 Catch:{ Throwable ->
        0x0069 }
        r0 = (int) r0;	 Catch:{ Throwable -> 0x0069 }
        r10.b = r0;	 Catch:{ Throwable -> 0x0069 }
        r0 = r10.d;	 Catch:{ SQLiteException -> 0x009d }
        r0 = r0.getWritableDatabase();	 Catch:{ SQLiteException -> 0x009d }
        r0.endTransaction();	 Catch:{ SQLiteException -> 0x009d }
        goto L_0x0078;
    L_0x009d:
        r0 = move-exception;
        r1 = e;	 Catch:{ all -> 0x00a4 }
        r1.e(r0);	 Catch:{ all -> 0x00a4 }
        goto L_0x0078;
    L_0x00a4:
        r0 = move-exception;
        monitor-exit(r10);
        throw r0;
    L_0x00a7:
        r0 = move-exception;
        r1 = e;	 Catch:{ all -> 0x00a4 }
        r1.e(r0);	 Catch:{ all -> 0x00a4 }
        goto L_0x0078;
    L_0x00ae:
        r0 = move-exception;
        r1 = r10.d;	 Catch:{ SQLiteException -> 0x00b9 }
        r1 = r1.getWritableDatabase();	 Catch:{ SQLiteException -> 0x00b9 }
        r1.endTransaction();	 Catch:{ SQLiteException -> 0x00b9 }
    L_0x00b8:
        throw r0;	 Catch:{ all -> 0x00a4 }
    L_0x00b9:
        r1 = move-exception;
        r2 = e;	 Catch:{ all -> 0x00a4 }
        r2.e(r1);	 Catch:{ all -> 0x00a4 }
        goto L_0x00b8;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.tencent.stat.n.b(java" +
                ".util.List):void");
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private synchronized void b(java.util.List<com.tencent.stat.x> r13, int r14) {
        /*
        r12 = this;
        monitor-enter(r12);
        r0 = e;	 Catch:{ all -> 0x011a }
        r1 = new java.lang.StringBuilder;	 Catch:{ all -> 0x011a }
        r1.<init>();	 Catch:{ all -> 0x011a }
        r2 = "Update ";
        r1 = r1.append(r2);	 Catch:{ all -> 0x011a }
        r2 = r13.size();	 Catch:{ all -> 0x011a }
        r1 = r1.append(r2);	 Catch:{ all -> 0x011a }
        r2 = " sending events to status:";
        r1 = r1.append(r2);	 Catch:{ all -> 0x011a }
        r1 = r1.append(r14);	 Catch:{ all -> 0x011a }
        r2 = " in thread:";
        r1 = r1.append(r2);	 Catch:{ all -> 0x011a }
        r2 = java.lang.Thread.currentThread();	 Catch:{ all -> 0x011a }
        r1 = r1.append(r2);	 Catch:{ all -> 0x011a }
        r1 = r1.toString();	 Catch:{ all -> 0x011a }
        r0.i(r1);	 Catch:{ all -> 0x011a }
        r1 = new android.content.ContentValues;	 Catch:{ Throwable -> 0x008d }
        r1.<init>();	 Catch:{ Throwable -> 0x008d }
        r0 = "status";
        r2 = java.lang.Integer.toString(r14);	 Catch:{ Throwable -> 0x008d }
        r1.put(r0, r2);	 Catch:{ Throwable -> 0x008d }
        r0 = r12.d;	 Catch:{ Throwable -> 0x008d }
        r0 = r0.getWritableDatabase();	 Catch:{ Throwable -> 0x008d }
        r0.beginTransaction();	 Catch:{ Throwable -> 0x008d }
        r2 = r13.iterator();	 Catch:{ Throwable -> 0x008d }
    L_0x0054:
        r0 = r2.hasNext();	 Catch:{ Throwable -> 0x008d }
        if (r0 == 0) goto L_0x011d;
    L_0x005a:
        r0 = r2.next();	 Catch:{ Throwable -> 0x008d }
        r0 = (com.tencent.stat.x) r0;	 Catch:{ Throwable -> 0x008d }
        r3 = r0.d;	 Catch:{ Throwable -> 0x008d }
        r3 = r3 + 1;
        r4 = com.tencent.stat.StatConfig.getMaxSendRetryCount();	 Catch:{ Throwable -> 0x008d }
        if (r3 <= r4) goto L_0x009e;
    L_0x006a:
        r3 = r12.b;	 Catch:{ Throwable -> 0x008d }
        r4 = r12.d;	 Catch:{ Throwable -> 0x008d }
        r4 = r4.getWritableDatabase();	 Catch:{ Throwable -> 0x008d }
        r5 = "events";
        r6 = "event_id=?";
        r7 = 1;
        r7 = new java.lang.String[r7];	 Catch:{ Throwable -> 0x008d }
        r8 = 0;
        r10 = r0.a;	 Catch:{ Throwable -> 0x008d }
        r0 = java.lang.Long.toString(r10);	 Catch:{ Throwable -> 0x008d }
        r7[r8] = r0;	 Catch:{ Throwable -> 0x008d }
        r0 = r4.delete(r5, r6, r7);	 Catch:{ Throwable -> 0x008d }
        r0 = r3 - r0;
        r12.b = r0;	 Catch:{ Throwable -> 0x008d }
        goto L_0x0054;
    L_0x008d:
        r0 = move-exception;
        r1 = e;	 Catch:{ all -> 0x010f }
        r1.e(r0);	 Catch:{ all -> 0x010f }
        r0 = r12.d;	 Catch:{ SQLiteException -> 0x0149 }
        r0 = r0.getWritableDatabase();	 Catch:{ SQLiteException -> 0x0149 }
        r0.endTransaction();	 Catch:{ SQLiteException -> 0x0149 }
    L_0x009c:
        monitor-exit(r12);
        return;
    L_0x009e:
        r3 = "send_count";
        r4 = r0.d;	 Catch:{ Throwable -> 0x008d }
        r4 = r4 + 1;
        r4 = java.lang.Integer.valueOf(r4);	 Catch:{ Throwable -> 0x008d }
        r1.put(r3, r4);	 Catch:{ Throwable -> 0x008d }
        r3 = e;	 Catch:{ Throwable -> 0x008d }
        r4 = new java.lang.StringBuilder;	 Catch:{ Throwable -> 0x008d }
        r4.<init>();	 Catch:{ Throwable -> 0x008d }
        r5 = "Update event:";
        r4 = r4.append(r5);	 Catch:{ Throwable -> 0x008d }
        r6 = r0.a;	 Catch:{ Throwable -> 0x008d }
        r4 = r4.append(r6);	 Catch:{ Throwable -> 0x008d }
        r5 = " for content:";
        r4 = r4.append(r5);	 Catch:{ Throwable -> 0x008d }
        r4 = r4.append(r1);	 Catch:{ Throwable -> 0x008d }
        r4 = r4.toString();	 Catch:{ Throwable -> 0x008d }
        r3.i(r4);	 Catch:{ Throwable -> 0x008d }
        r3 = r12.d;	 Catch:{ Throwable -> 0x008d }
        r3 = r3.getWritableDatabase();	 Catch:{ Throwable -> 0x008d }
        r4 = "events";
        r5 = "event_id=?";
        r6 = 1;
        r6 = new java.lang.String[r6];	 Catch:{ Throwable -> 0x008d }
        r7 = 0;
        r8 = r0.a;	 Catch:{ Throwable -> 0x008d }
        r0 = java.lang.Long.toString(r8);	 Catch:{ Throwable -> 0x008d }
        r6[r7] = r0;	 Catch:{ Throwable -> 0x008d }
        r0 = r3.update(r4, r1, r5, r6);	 Catch:{ Throwable -> 0x008d }
        if (r0 > 0) goto L_0x0054;
    L_0x00f0:
        r3 = e;	 Catch:{ Throwable -> 0x008d }
        r4 = new java.lang.StringBuilder;	 Catch:{ Throwable -> 0x008d }
        r4.<init>();	 Catch:{ Throwable -> 0x008d }
        r5 = "Failed to update db, error code:";
        r4 = r4.append(r5);	 Catch:{ Throwable -> 0x008d }
        r0 = java.lang.Integer.toString(r0);	 Catch:{ Throwable -> 0x008d }
        r0 = r4.append(r0);	 Catch:{ Throwable -> 0x008d }
        r0 = r0.toString();	 Catch:{ Throwable -> 0x008d }
        r3.e(r0);	 Catch:{ Throwable -> 0x008d }
        goto L_0x0054;
    L_0x010f:
        r0 = move-exception;
        r1 = r12.d;	 Catch:{ SQLiteException -> 0x0151 }
        r1 = r1.getWritableDatabase();	 Catch:{ SQLiteException -> 0x0151 }
        r1.endTransaction();	 Catch:{ SQLiteException -> 0x0151 }
    L_0x0119:
        throw r0;	 Catch:{ all -> 0x011a }
    L_0x011a:
        r0 = move-exception;
        monitor-exit(r12);
        throw r0;
    L_0x011d:
        r0 = r12.d;	 Catch:{ Throwable -> 0x008d }
        r0 = r0.getWritableDatabase();	 Catch:{ Throwable -> 0x008d }
        r0.setTransactionSuccessful();	 Catch:{ Throwable -> 0x008d }
        r0 = r12.d;	 Catch:{ Throwable -> 0x008d }
        r0 = r0.getReadableDatabase();	 Catch:{ Throwable -> 0x008d }
        r1 = "events";
        r0 = android.database.DatabaseUtils.queryNumEntries(r0, r1);	 Catch:{ Throwable ->
        0x008d }
        r0 = (int) r0;	 Catch:{ Throwable -> 0x008d }
        r12.b = r0;	 Catch:{ Throwable -> 0x008d }
        r0 = r12.d;	 Catch:{ SQLiteException -> 0x0141 }
        r0 = r0.getWritableDatabase();	 Catch:{ SQLiteException -> 0x0141 }
        r0.endTransaction();	 Catch:{ SQLiteException -> 0x0141 }
        goto L_0x009c;
    L_0x0141:
        r0 = move-exception;
        r1 = e;	 Catch:{ all -> 0x011a }
        r1.e(r0);	 Catch:{ all -> 0x011a }
        goto L_0x009c;
    L_0x0149:
        r0 = move-exception;
        r1 = e;	 Catch:{ all -> 0x011a }
        r1.e(r0);	 Catch:{ all -> 0x011a }
        goto L_0x009c;
    L_0x0151:
        r1 = move-exception;
        r2 = e;	 Catch:{ all -> 0x011a }
        r2.e(r1);	 Catch:{ all -> 0x011a }
        goto L_0x0119;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.tencent.stat.n.b(java" +
                ".util.List, int):void");
    }

    private void c(List<x> list, int i) {
        Object th;
        Cursor cursor;
        Throwable th2;
        Cursor cursor2 = null;
        try {
            Cursor query = this.d.getReadableDatabase().query("events", null, "status=?", new
                    String[]{Integer.toString(1)}, null, null, "event_id", Integer.toString(i));
            while (query.moveToNext()) {
                try {
                    list.add(new x(query.getLong(0), k.d(query.getString(1)), query.getInt(2),
                            query.getInt(3)));
                } catch (Throwable th3) {
                    th2 = th3;
                    cursor2 = query;
                }
            }
            if (query != null) {
                query.close();
            }
        } catch (Throwable th4) {
            th2 = th4;
            if (cursor2 != null) {
                cursor2.close();
            }
            throw th2;
        }
    }

    private void e() {
        try {
            ContentValues contentValues = new ContentValues();
            contentValues.put("status", Integer.valueOf(1));
            this.d.getWritableDatabase().update("events", contentValues, "status=?", new
                    String[]{Long.toString(2)});
            this.b = (int) DatabaseUtils.queryNumEntries(this.d.getReadableDatabase(), "events");
            e.i("Total " + this.b + " unsent events.");
        } catch (Object th) {
            e.e(th);
        }
    }

    private void f() {
        Cursor query;
        Object th;
        Throwable th2;
        try {
            query = this.d.getReadableDatabase().query("keyvalues", null, null, null, null, null,
                    null);
            while (query.moveToNext()) {
                try {
                    this.g.put(query.getString(0), query.getString(1));
                } catch (Throwable th3) {
                    th = th3;
                }
            }
            if (query != null) {
                query.close();
            }
        } catch (Throwable th4) {
            th2 = th4;
            query = null;
            if (query != null) {
                query.close();
            }
            throw th2;
        }
    }

    public int a() {
        return this.b;
    }

    void a(int i) {
        this.a.post(new v(this, i));
    }

    void a(e eVar, c cVar) {
        if (StatConfig.isEnableStatService()) {
            try {
                if (Thread.currentThread().getId() == this.a.getLooper().getThread().getId()) {
                    b(eVar, cVar);
                } else {
                    this.a.post(new r(this, eVar, cVar));
                }
            } catch (Object th) {
                e.e(th);
            }
        }
    }

    void a(b bVar) {
        if (bVar != null) {
            this.a.post(new s(this, bVar));
        }
    }

    void a(List<x> list) {
        try {
            if (Thread.currentThread().getId() == this.a.getLooper().getThread().getId()) {
                b((List) list);
            } else {
                this.a.post(new q(this, list));
            }
        } catch (Exception e) {
            e.e(e);
        }
    }

    void a(List<x> list, int i) {
        try {
            if (Thread.currentThread().getId() == this.a.getLooper().getThread().getId()) {
                b((List) list, i);
            } else {
                this.a.post(new p(this, list, i));
            }
        } catch (Object th) {
            e.e(th);
        }
    }

    public synchronized DeviceInfo b(Context context) {
        DeviceInfo deviceInfo;
        Cursor query;
        Object obj;
        Cursor cursor;
        Throwable th;
        if (this.c != null) {
            deviceInfo = this.c;
        } else {
            try {
                query = this.d.getReadableDatabase().query(Const.USER, null, null, null, null,
                        null, null, null);
                obj = null;
                try {
                    String d;
                    String str;
                    String l;
                    String str2 = "";
                    if (query.moveToNext()) {
                        Object obj2;
                        d = k.d(query.getString(0));
                        int i = query.getInt(1);
                        str2 = query.getString(2);
                        long currentTimeMillis = System.currentTimeMillis() / 1000;
                        int i2 = (i == 1 || k.a(query.getLong(3) * 1000).equals(k.a(1000 *
                                currentTimeMillis))) ? i : 1;
                        int i3 = !str2.equals(k.r(context)) ? i2 | 2 : i2;
                        String[] split = d.split(",");
                        if (split == null || split.length <= 0) {
                            str2 = k.b(context);
                            d = str2;
                            str = str2;
                            int i4 = 1;
                        } else {
                            str2 = split[0];
                            if (str2 == null || str2.length() < 11) {
                                l = k.l(context);
                                if (l == null || l.length() <= 10) {
                                    l = str2;
                                    obj2 = null;
                                } else {
                                    obj2 = 1;
                                }
                                str = d;
                                d = l;
                            } else {
                                String str3 = str2;
                                obj2 = null;
                                str = d;
                                d = str3;
                            }
                        }
                        if (split == null || split.length < 2) {
                            l = k.c(context);
                            if (l != null && l.length() > 0) {
                                str = d + "," + l;
                                obj2 = 1;
                            }
                        } else {
                            l = split[1];
                            str = d + "," + l;
                        }
                        this.c = new DeviceInfo(d, l, i3);
                        ContentValues contentValues = new ContentValues();
                        contentValues.put(SocializeProtocolConstants.PROTOCOL_KEY_UID, k.c(str));
                        contentValues.put("user_type", Integer.valueOf(i3));
                        contentValues.put("app_ver", k.r(context));
                        contentValues.put(DeviceInfo.TAG_TIMESTAMPS, Long.valueOf
                                (currentTimeMillis));
                        if (obj2 != null) {
                            this.d.getWritableDatabase().update(Const.USER, contentValues,
                                    "uid=?", new String[]{r10});
                        }
                        if (i3 != i) {
                            this.d.getWritableDatabase().replace(Const.USER, null, contentValues);
                            obj = 1;
                        } else {
                            i2 = 1;
                        }
                    }
                    if (obj == null) {
                        str2 = k.b(context);
                        str = k.c(context);
                        l = (str == null || str.length() <= 0) ? str2 : str2 + "," + str;
                        long currentTimeMillis2 = System.currentTimeMillis() / 1000;
                        d = k.r(context);
                        ContentValues contentValues2 = new ContentValues();
                        contentValues2.put(SocializeProtocolConstants.PROTOCOL_KEY_UID, k.c(l));
                        contentValues2.put("user_type", Integer.valueOf(0));
                        contentValues2.put("app_ver", d);
                        contentValues2.put(DeviceInfo.TAG_TIMESTAMPS, Long.valueOf
                                (currentTimeMillis2));
                        this.d.getWritableDatabase().insert(Const.USER, null, contentValues2);
                        this.c = new DeviceInfo(str2, str, 0);
                    }
                    if (query != null) {
                        query.close();
                    }
                } catch (Throwable th2) {
                    th = th2;
                    if (query != null) {
                        query.close();
                    }
                    throw th;
                }
            } catch (Throwable th3) {
                th = th3;
                query = null;
                if (query != null) {
                    query.close();
                }
                throw th;
            }
            deviceInfo = this.c;
        }
        return deviceInfo;
    }

    void c() {
        this.a.post(new t(this));
    }
}
