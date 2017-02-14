package com.tencent.stat;

import android.database.Cursor;

import org.json.JSONObject;

class t implements Runnable {
    final /* synthetic */ n a;

    t(n nVar) {
        this.a = nVar;
    }

    public void run() {
        Cursor query;
        Object th;
        Throwable th2;
        try {
            query = this.a.d.getReadableDatabase().query("config", null, null, null, null, null,
                    null);
            while (query.moveToNext()) {
                try {
                    int i = query.getInt(0);
                    String string = query.getString(1);
                    String string2 = query.getString(2);
                    int i2 = query.getInt(3);
                    b bVar = new b(i);
                    bVar.a = i;
                    bVar.b = new JSONObject(string);
                    bVar.c = string2;
                    bVar.d = i2;
                    StatConfig.a(bVar);
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
}
