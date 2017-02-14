package cn.sharesdk.framework.statistics.a;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.mob.tools.utils.Ln;

public class b {
    private static b c = null;
    private Context a;
    private a b = new a(this.a);

    private b(Context context) {
        this.a = context.getApplicationContext();
    }

    public static synchronized b a(Context context) {
        b bVar;
        synchronized (b.class) {
            if (c == null) {
                c = new b(context);
            }
            bVar = c;
        }
        return bVar;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public int a(java.lang.String r6) {
        /*
        r5 = this;
        r2 = 0;
        r0 = 0;
        r1 = r5.b;
        r1 = r1.getWritableDatabase();
        r3 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0030 }
        r3.<init>();	 Catch:{ Exception -> 0x0030 }
        r4 = "select count(*) from ";
        r3 = r3.append(r4);	 Catch:{ Exception -> 0x0030 }
        r3 = r3.append(r6);	 Catch:{ Exception -> 0x0030 }
        r3 = r3.toString();	 Catch:{ Exception -> 0x0030 }
        r4 = 0;
        r2 = r1.rawQuery(r3, r4);	 Catch:{ Exception -> 0x0030 }
        r1 = r2.moveToNext();	 Catch:{ Exception -> 0x0030 }
        if (r1 == 0) goto L_0x002c;
    L_0x0027:
        r1 = 0;
        r0 = r2.getInt(r1);	 Catch:{ Exception -> 0x0030 }
    L_0x002c:
        r2.close();
    L_0x002f:
        return r0;
    L_0x0030:
        r1 = move-exception;
        com.mob.tools.utils.Ln.e(r1);	 Catch:{ all -> 0x0038 }
        r2.close();
        goto L_0x002f;
    L_0x0038:
        r0 = move-exception;
        r2.close();
        throw r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: cn.sharesdk.framework.statistics.a.b.a(java.lang.String):int");
    }

    public int a(String str, String str2, String[] strArr) {
        int delete;
        Throwable e;
        try {
            delete = this.b.getWritableDatabase().delete(str, str2, strArr);
            try {
                Ln.d("Deleted %d rows from table: %s", Integer.valueOf(delete), str);
            } catch (Exception e2) {
                e = e2;
                Ln.e(e, "when delete database occur error table:%s,", str);
                return delete;
            }
        } catch (Throwable e3) {
            e = e3;
            delete = 0;
            Ln.e(e, "when delete database occur error table:%s,", str);
            return delete;
        }
        return delete;
    }

    public long a(String str, ContentValues contentValues) {
        long j = -1;
        try {
            j = this.b.getWritableDatabase().replace(str, null, contentValues);
        } catch (Throwable e) {
            Ln.e(e, "when insert database occur error table:%s,", str);
        }
        return j;
    }

    public Cursor a(String str, String[] strArr, String str2, String[] strArr2, String str3) {
        SQLiteDatabase writableDatabase = this.b.getWritableDatabase();
        Ln.d("Query table: %s", str);
        try {
            return writableDatabase.query(str, strArr, str2, strArr2, null, null, str3);
        } catch (Throwable e) {
            Ln.e(e, "when query database occur error table:%s,", str);
            return null;
        }
    }
}
