package com.alipay.sdk.tid;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;
import com.alipay.sdk.encrypt.b;
import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public final class a extends SQLiteOpenHelper {
    private static final String a = "msp.db";
    private static final int b = 1;
    private WeakReference<Context> c;

    public final void a(java.lang.String r7, java.lang.String r8) {
        /* JADX: method processing error */
/*
Error: java.util.NoSuchElementException
	at java.util.HashMap$HashIterator.nextNode(HashMap.java:1439)
	at java.util.HashMap$KeyIterator.next(HashMap.java:1461)
	at jadx.core.dex.visitors.blocksmaker.BlockFinallyExtract.applyRemove(BlockFinallyExtract.java:535)
	at jadx.core.dex.visitors.blocksmaker.BlockFinallyExtract.extractFinally(BlockFinallyExtract.java:175)
	at jadx.core.dex.visitors.blocksmaker.BlockFinallyExtract.processExceptionHandler(BlockFinallyExtract.java:79)
	at jadx.core.dex.visitors.blocksmaker.BlockFinallyExtract.visit(BlockFinallyExtract.java:51)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
*/
        /*
        r6 = this;
        r1 = 0;
        r1 = r6.getWritableDatabase();	 Catch:{ Exception -> 0x0024, all -> 0x0031 }
        r4 = "";	 Catch:{ Exception -> 0x0024, all -> 0x0031 }
        r5 = "";	 Catch:{ Exception -> 0x0024, all -> 0x0031 }
        r0 = r6;	 Catch:{ Exception -> 0x0024, all -> 0x0031 }
        r2 = r7;	 Catch:{ Exception -> 0x0024, all -> 0x0031 }
        r3 = r8;	 Catch:{ Exception -> 0x0024, all -> 0x0031 }
        r0.b(r1, r2, r3, r4, r5);	 Catch:{ Exception -> 0x0024, all -> 0x0031 }
        r0 = e(r7, r8);	 Catch:{ Exception -> 0x0024, all -> 0x0031 }
        a(r1, r0);	 Catch:{ Exception -> 0x0024, all -> 0x0031 }
        if (r1 == 0) goto L_0x0023;
    L_0x001a:
        r0 = r1.isOpen();
        if (r0 == 0) goto L_0x0023;
    L_0x0020:
        r1.close();
    L_0x0023:
        return;
    L_0x0024:
        r0 = move-exception;
        if (r1 == 0) goto L_0x0023;
    L_0x0027:
        r0 = r1.isOpen();
        if (r0 == 0) goto L_0x0023;
    L_0x002d:
        r1.close();
        goto L_0x0023;
    L_0x0031:
        r0 = move-exception;
        if (r1 == 0) goto L_0x003d;
    L_0x0034:
        r2 = r1.isOpen();
        if (r2 == 0) goto L_0x003d;
    L_0x003a:
        r1.close();
    L_0x003d:
        throw r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alipay.sdk.tid.a.a(java.lang.String, java.lang.String):void");
    }

    public final void a(java.lang.String r8, java.lang.String r9, java.lang.String r10, java.lang.String r11) {
        /* JADX: method processing error */
/*
Error: java.util.NoSuchElementException
	at java.util.HashMap$HashIterator.nextNode(HashMap.java:1439)
	at java.util.HashMap$KeyIterator.next(HashMap.java:1461)
	at jadx.core.dex.visitors.blocksmaker.BlockFinallyExtract.applyRemove(BlockFinallyExtract.java:535)
	at jadx.core.dex.visitors.blocksmaker.BlockFinallyExtract.extractFinally(BlockFinallyExtract.java:175)
	at jadx.core.dex.visitors.blocksmaker.BlockFinallyExtract.processExceptionHandler(BlockFinallyExtract.java:79)
	at jadx.core.dex.visitors.blocksmaker.BlockFinallyExtract.visit(BlockFinallyExtract.java:51)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
*/
        /*
        r7 = this;
        r1 = 0;
        r2 = 0;
        r1 = r7.getWritableDatabase();	 Catch:{ Exception -> 0x005b, all -> 0x009f }
        r0 = a(r1, r8, r9);	 Catch:{ Exception -> 0x005b, all -> 0x009f }
        if (r0 == 0) goto L_0x0020;	 Catch:{ Exception -> 0x005b, all -> 0x009f }
    L_0x000c:
        r0 = r7;	 Catch:{ Exception -> 0x005b, all -> 0x009f }
        r2 = r8;	 Catch:{ Exception -> 0x005b, all -> 0x009f }
        r3 = r9;	 Catch:{ Exception -> 0x005b, all -> 0x009f }
        r4 = r10;	 Catch:{ Exception -> 0x005b, all -> 0x009f }
        r5 = r11;	 Catch:{ Exception -> 0x005b, all -> 0x009f }
        r0.b(r1, r2, r3, r4, r5);	 Catch:{ Exception -> 0x005b, all -> 0x009f }
    L_0x0014:
        if (r1 == 0) goto L_0x001f;
    L_0x0016:
        r0 = r1.isOpen();
        if (r0 == 0) goto L_0x001f;
    L_0x001c:
        r1.close();
    L_0x001f:
        return;
    L_0x0020:
        r3 = "insert into tb_tid (name, tid, key_tid, dt) values (?, ?, ?, datetime('now', 'localtime'))";	 Catch:{ Exception -> 0x005b, all -> 0x009f }
        r0 = r7.c;	 Catch:{ Exception -> 0x005b, all -> 0x009f }
        r0 = r0.get();	 Catch:{ Exception -> 0x005b, all -> 0x009f }
        r0 = (android.content.Context) r0;	 Catch:{ Exception -> 0x005b, all -> 0x009f }
        r0 = com.alipay.sdk.util.a.c(r0);	 Catch:{ Exception -> 0x005b, all -> 0x009f }
        r4 = 1;	 Catch:{ Exception -> 0x005b, all -> 0x009f }
        r0 = com.alipay.sdk.encrypt.b.a(r4, r10, r0);	 Catch:{ Exception -> 0x005b, all -> 0x009f }
        r4 = 3;	 Catch:{ Exception -> 0x005b, all -> 0x009f }
        r4 = new java.lang.Object[r4];	 Catch:{ Exception -> 0x005b, all -> 0x009f }
        r5 = 0;	 Catch:{ Exception -> 0x005b, all -> 0x009f }
        r6 = e(r8, r9);	 Catch:{ Exception -> 0x005b, all -> 0x009f }
        r4[r5] = r6;	 Catch:{ Exception -> 0x005b, all -> 0x009f }
        r5 = 1;	 Catch:{ Exception -> 0x005b, all -> 0x009f }
        r4[r5] = r0;	 Catch:{ Exception -> 0x005b, all -> 0x009f }
        r0 = 2;	 Catch:{ Exception -> 0x005b, all -> 0x009f }
        r4[r0] = r11;	 Catch:{ Exception -> 0x005b, all -> 0x009f }
        r1.execSQL(r3, r4);	 Catch:{ Exception -> 0x005b, all -> 0x009f }
        r0 = "select name from tb_tid where tid!='' order by dt asc";	 Catch:{ Exception -> 0x005b, all -> 0x009f }
        r3 = 0;	 Catch:{ Exception -> 0x005b, all -> 0x009f }
        r3 = r1.rawQuery(r0, r3);	 Catch:{ Exception -> 0x005b, all -> 0x009f }
        r0 = r3.getCount();	 Catch:{ Exception -> 0x005b, all -> 0x009f }
        r4 = 14;	 Catch:{ Exception -> 0x005b, all -> 0x009f }
        if (r0 > r4) goto L_0x0068;	 Catch:{ Exception -> 0x005b, all -> 0x009f }
    L_0x0057:
        r3.close();	 Catch:{ Exception -> 0x005b, all -> 0x009f }
        goto L_0x0014;
    L_0x005b:
        r0 = move-exception;
        if (r1 == 0) goto L_0x001f;
    L_0x005e:
        r0 = r1.isOpen();
        if (r0 == 0) goto L_0x001f;
    L_0x0064:
        r1.close();
        goto L_0x001f;
    L_0x0068:
        r0 = r3.getCount();	 Catch:{ Exception -> 0x005b, all -> 0x009f }
        r4 = r0 + -14;	 Catch:{ Exception -> 0x005b, all -> 0x009f }
        r5 = new java.lang.String[r4];	 Catch:{ Exception -> 0x005b, all -> 0x009f }
        r0 = r3.moveToFirst();	 Catch:{ Exception -> 0x005b, all -> 0x009f }
        if (r0 == 0) goto L_0x0088;	 Catch:{ Exception -> 0x005b, all -> 0x009f }
    L_0x0076:
        r0 = r2;	 Catch:{ Exception -> 0x005b, all -> 0x009f }
    L_0x0077:
        r6 = 0;	 Catch:{ Exception -> 0x005b, all -> 0x009f }
        r6 = r3.getString(r6);	 Catch:{ Exception -> 0x005b, all -> 0x009f }
        r5[r0] = r6;	 Catch:{ Exception -> 0x005b, all -> 0x009f }
        r0 = r0 + 1;	 Catch:{ Exception -> 0x005b, all -> 0x009f }
        r6 = r3.moveToNext();	 Catch:{ Exception -> 0x005b, all -> 0x009f }
        if (r6 == 0) goto L_0x0088;	 Catch:{ Exception -> 0x005b, all -> 0x009f }
    L_0x0086:
        if (r4 > r0) goto L_0x0077;	 Catch:{ Exception -> 0x005b, all -> 0x009f }
    L_0x0088:
        r3.close();	 Catch:{ Exception -> 0x005b, all -> 0x009f }
        r0 = r2;	 Catch:{ Exception -> 0x005b, all -> 0x009f }
    L_0x008c:
        r2 = r5.length;	 Catch:{ Exception -> 0x005b, all -> 0x009f }
        if (r0 >= r2) goto L_0x0014;	 Catch:{ Exception -> 0x005b, all -> 0x009f }
    L_0x008f:
        r2 = r5[r0];	 Catch:{ Exception -> 0x005b, all -> 0x009f }
        r2 = android.text.TextUtils.isEmpty(r2);	 Catch:{ Exception -> 0x005b, all -> 0x009f }
        if (r2 != 0) goto L_0x009c;	 Catch:{ Exception -> 0x005b, all -> 0x009f }
    L_0x0097:
        r2 = r5[r0];	 Catch:{ Exception -> 0x005b, all -> 0x009f }
        a(r1, r2);	 Catch:{ Exception -> 0x005b, all -> 0x009f }
    L_0x009c:
        r0 = r0 + 1;
        goto L_0x008c;
    L_0x009f:
        r0 = move-exception;
        if (r1 == 0) goto L_0x00ab;
    L_0x00a2:
        r2 = r1.isOpen();
        if (r2 == 0) goto L_0x00ab;
    L_0x00a8:
        r1.close();
    L_0x00ab:
        throw r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alipay.sdk.tid.a.a(java.lang.String, java.lang.String, java.lang.String, java.lang.String):void");
    }

    public a(Context context) {
        super(context, a, null, 1);
        this.c = new WeakReference(context);
    }

    public final void onCreate(SQLiteDatabase sQLiteDatabase) {
        sQLiteDatabase.execSQL("create table if not exists tb_tid (name text primary key, tid text, key_tid text, dt datetime);");
    }

    public final void onUpgrade(SQLiteDatabase sQLiteDatabase, int i, int i2) {
        sQLiteDatabase.execSQL("drop table if exists tb_tid");
        sQLiteDatabase.execSQL("create table if not exists tb_tid (name text primary key, tid text, key_tid text, dt datetime);");
    }

    public final String b(String str, String str2) {
        SQLiteDatabase readableDatabase;
        Cursor rawQuery;
        Throwable th;
        String str3 = null;
        String str4 = "select tid from tb_tid where name=?";
        try {
            readableDatabase = getReadableDatabase();
            try {
                rawQuery = readableDatabase.rawQuery(str4, new String[]{e(str, str2)});
                try {
                    if (rawQuery.moveToFirst()) {
                        str3 = rawQuery.getString(0);
                    }
                    if (rawQuery != null) {
                        rawQuery.close();
                    }
                    if (readableDatabase == null || !readableDatabase.isOpen()) {
                        str4 = str3;
                    } else {
                        readableDatabase.close();
                        str4 = str3;
                    }
                } catch (Exception e) {
                    if (rawQuery != null) {
                        rawQuery.close();
                    }
                    if (readableDatabase == null) {
                    }
                    str4 = null;
                    if (TextUtils.isEmpty(str4)) {
                        return str4;
                    }
                    return b.a(2, str4, com.alipay.sdk.util.a.c((Context) this.c.get()));
                } catch (Throwable th2) {
                    th = th2;
                    if (rawQuery != null) {
                        rawQuery.close();
                    }
                    readableDatabase.close();
                    throw th;
                }
            } catch (Exception e2) {
                rawQuery = null;
                if (rawQuery != null) {
                    rawQuery.close();
                }
                if (readableDatabase == null) {
                }
                str4 = null;
                if (TextUtils.isEmpty(str4)) {
                    return b.a(2, str4, com.alipay.sdk.util.a.c((Context) this.c.get()));
                }
                return str4;
            } catch (Throwable th3) {
                Throwable th4 = th3;
                rawQuery = null;
                th = th4;
                if (rawQuery != null) {
                    rawQuery.close();
                }
                readableDatabase.close();
                throw th;
            }
        } catch (Exception e3) {
            rawQuery = null;
            readableDatabase = null;
            if (rawQuery != null) {
                rawQuery.close();
            }
            if (readableDatabase == null && readableDatabase.isOpen()) {
                readableDatabase.close();
                str4 = null;
            } else {
                str4 = null;
            }
            if (TextUtils.isEmpty(str4)) {
                return str4;
            }
            return b.a(2, str4, com.alipay.sdk.util.a.c((Context) this.c.get()));
        } catch (Throwable th32) {
            readableDatabase = null;
            th = th32;
            rawQuery = null;
            if (rawQuery != null) {
                rawQuery.close();
            }
            if (readableDatabase != null && readableDatabase.isOpen()) {
                readableDatabase.close();
            }
            throw th;
        }
        if (TextUtils.isEmpty(str4)) {
            return b.a(2, str4, com.alipay.sdk.util.a.c((Context) this.c.get()));
        }
        return str4;
    }

    private long d(String str, String str2) {
        Throwable th;
        Cursor cursor = null;
        long j = 0;
        String str3 = "select dt from tb_tid where name=?";
        SQLiteDatabase readableDatabase;
        try {
            readableDatabase = getReadableDatabase();
            try {
                cursor = readableDatabase.rawQuery(str3, new String[]{e(str, str2)});
                if (cursor.moveToFirst()) {
                    j = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).parse(cursor.getString(0)).getTime();
                }
                if (cursor != null) {
                    cursor.close();
                }
                if (readableDatabase != null && readableDatabase.isOpen()) {
                    readableDatabase.close();
                }
            } catch (Exception e) {
                if (cursor != null) {
                    cursor.close();
                }
                if (readableDatabase != null && readableDatabase.isOpen()) {
                    readableDatabase.close();
                }
                return j;
            } catch (Throwable th2) {
                th = th2;
                if (cursor != null) {
                    cursor.close();
                }
                if (readableDatabase != null && readableDatabase.isOpen()) {
                    readableDatabase.close();
                }
                throw th;
            }
        } catch (Exception e2) {
            readableDatabase = cursor;
            if (cursor != null) {
                cursor.close();
            }
            readableDatabase.close();
            return j;
        } catch (Throwable th3) {
            th = th3;
            readableDatabase = cursor;
            if (cursor != null) {
                cursor.close();
            }
            readableDatabase.close();
            throw th;
        }
        return j;
    }

    private List<String> a() {
        SQLiteDatabase readableDatabase;
        Cursor rawQuery;
        SQLiteDatabase sQLiteDatabase;
        Throwable th;
        Cursor cursor = null;
        List<String> arrayList = new ArrayList();
        try {
            readableDatabase = getReadableDatabase();
            try {
                rawQuery = readableDatabase.rawQuery("select tid from tb_tid", null);
                while (rawQuery.moveToNext()) {
                    try {
                        Object string = rawQuery.getString(0);
                        if (!TextUtils.isEmpty(string)) {
                            arrayList.add(b.a(2, string, com.alipay.sdk.util.a.c((Context) this.c.get())));
                        }
                    } catch (Exception e) {
                        cursor = rawQuery;
                        sQLiteDatabase = readableDatabase;
                    } catch (Throwable th2) {
                        th = th2;
                    }
                }
                if (rawQuery != null) {
                    rawQuery.close();
                }
                if (readableDatabase != null && readableDatabase.isOpen()) {
                    readableDatabase.close();
                }
            } catch (Exception e2) {
                sQLiteDatabase = readableDatabase;
                if (cursor != null) {
                    cursor.close();
                }
                if (sQLiteDatabase != null && sQLiteDatabase.isOpen()) {
                    sQLiteDatabase.close();
                }
                return arrayList;
            } catch (Throwable th3) {
                Throwable th4 = th3;
                rawQuery = null;
                th = th4;
                if (rawQuery != null) {
                    rawQuery.close();
                }
                if (readableDatabase != null && readableDatabase.isOpen()) {
                    readableDatabase.close();
                }
                throw th;
            }
        } catch (Exception e3) {
            sQLiteDatabase = null;
            if (cursor != null) {
                cursor.close();
            }
            sQLiteDatabase.close();
            return arrayList;
        } catch (Throwable th32) {
            readableDatabase = null;
            th = th32;
            rawQuery = null;
            if (rawQuery != null) {
                rawQuery.close();
            }
            readableDatabase.close();
            throw th;
        }
        return arrayList;
    }

    public final String c(String str, String str2) {
        Cursor rawQuery;
        Throwable th;
        String str3 = null;
        String str4 = "select key_tid from tb_tid where name=?";
        SQLiteDatabase readableDatabase;
        try {
            readableDatabase = getReadableDatabase();
            try {
                rawQuery = readableDatabase.rawQuery(str4, new String[]{e(str, str2)});
                try {
                    if (rawQuery.moveToFirst()) {
                        str3 = rawQuery.getString(0);
                    }
                    if (rawQuery != null) {
                        rawQuery.close();
                    }
                    if (readableDatabase != null && readableDatabase.isOpen()) {
                        readableDatabase.close();
                    }
                } catch (Exception e) {
                    if (rawQuery != null) {
                        rawQuery.close();
                    }
                    readableDatabase.close();
                    return str3;
                } catch (Throwable th2) {
                    th = th2;
                    if (rawQuery != null) {
                        rawQuery.close();
                    }
                    readableDatabase.close();
                    throw th;
                }
            } catch (Exception e2) {
                rawQuery = null;
                if (rawQuery != null) {
                    rawQuery.close();
                }
                readableDatabase.close();
                return str3;
            } catch (Throwable th3) {
                Throwable th4 = th3;
                rawQuery = null;
                th = th4;
                if (rawQuery != null) {
                    rawQuery.close();
                }
                readableDatabase.close();
                throw th;
            }
        } catch (Exception e3) {
            rawQuery = null;
            readableDatabase = null;
            if (rawQuery != null) {
                rawQuery.close();
            }
            if (readableDatabase != null && readableDatabase.isOpen()) {
                readableDatabase.close();
            }
            return str3;
        } catch (Throwable th32) {
            readableDatabase = null;
            th = th32;
            rawQuery = null;
            if (rawQuery != null) {
                rawQuery.close();
            }
            if (readableDatabase != null && readableDatabase.isOpen()) {
                readableDatabase.close();
            }
            throw th;
        }
        return str3;
    }

    private static boolean a(SQLiteDatabase sQLiteDatabase, String str, String str2) {
        int i;
        Cursor cursor = null;
        try {
            int i2;
            cursor = sQLiteDatabase.rawQuery("select count(*) from tb_tid where name=?", new String[]{e(str, str2)});
            if (cursor.moveToFirst()) {
                i2 = cursor.getInt(0);
            } else {
                i2 = 0;
            }
            if (cursor != null) {
                cursor.close();
                i = i2;
            } else {
                i = i2;
            }
        } catch (Exception e) {
            if (cursor != null) {
                cursor.close();
                i = 0;
            } else {
                i = 0;
            }
        } catch (Throwable th) {
            if (cursor != null) {
                cursor.close();
            }
        }
        if (i > 0) {
            return true;
        }
        return false;
    }

    private static String e(String str, String str2) {
        return str + str2;
    }

    private void a(SQLiteDatabase sQLiteDatabase, String str, String str2, String str3, String str4) {
        int i = 0;
        String a = b.a(1, str3, com.alipay.sdk.util.a.c((Context) this.c.get()));
        sQLiteDatabase.execSQL("insert into tb_tid (name, tid, key_tid, dt) values (?, ?, ?, datetime('now', 'localtime'))", new Object[]{e(str, str2), a, str4});
        Cursor rawQuery = sQLiteDatabase.rawQuery("select name from tb_tid where tid!='' order by dt asc", null);
        if (rawQuery.getCount() <= 14) {
            rawQuery.close();
            return;
        }
        int count = rawQuery.getCount() - 14;
        String[] strArr = new String[count];
        if (rawQuery.moveToFirst()) {
            int i2 = 0;
            do {
                strArr[i2] = rawQuery.getString(0);
                i2++;
                if (!rawQuery.moveToNext()) {
                    break;
                }
            } while (count > i2);
        }
        rawQuery.close();
        while (i < strArr.length) {
            if (!TextUtils.isEmpty(strArr[i])) {
                a(sQLiteDatabase, strArr[i]);
            }
            i++;
        }
    }

    private void b(SQLiteDatabase sQLiteDatabase, String str, String str2, String str3, String str4) {
        sQLiteDatabase.execSQL("update tb_tid set tid=?, key_tid=?, dt=datetime('now', 'localtime') where name=?", new Object[]{b.a(1, str3, com.alipay.sdk.util.a.c((Context) this.c.get())), str4, e(str, str2)});
    }

    private static void a(SQLiteDatabase sQLiteDatabase, String str) {
        try {
            sQLiteDatabase.delete("tb_tid", "name=?", new String[]{str});
        } catch (Exception e) {
        }
    }

    private static void a(SQLiteDatabase sQLiteDatabase) {
        int i = 0;
        Cursor rawQuery = sQLiteDatabase.rawQuery("select name from tb_tid where tid!='' order by dt asc", null);
        if (rawQuery.getCount() <= 14) {
            rawQuery.close();
            return;
        }
        int count = rawQuery.getCount() - 14;
        String[] strArr = new String[count];
        if (rawQuery.moveToFirst()) {
            int i2 = 0;
            do {
                strArr[i2] = rawQuery.getString(0);
                i2++;
                if (!rawQuery.moveToNext()) {
                    break;
                }
            } while (count > i2);
        }
        rawQuery.close();
        while (i < strArr.length) {
            if (!TextUtils.isEmpty(strArr[i])) {
                a(sQLiteDatabase, strArr[i]);
            }
            i++;
        }
    }
}
