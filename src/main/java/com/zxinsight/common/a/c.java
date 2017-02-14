package com.zxinsight.common.a;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.zxinsight.common.util.l;

public class c {
    public static synchronized int a(Context context) {
        int b;
        synchronized (c.class) {
            b = b.a(context.getApplicationContext()).b("analytics_event");
        }
        return b;
    }

    public static synchronized long a(Context context, String str) {
        long j;
        synchronized (c.class) {
            if (l.a(str)) {
                j = -1;
            } else {
                b a = b.a(context.getApplicationContext());
                if (a(context) >= BaseImageDownloader.DEFAULT_HTTP_CONNECT_TIMEOUT) {
                    a.a("delete from analytics_event limit 1");
                }
                ContentValues contentValues = new ContentValues();
                contentValues.put("event_type", "");
                contentValues.put("event_data", str);
                j = a.a("analytics_event", contentValues);
            }
        }
        return j;
    }

    public static synchronized long b(Context context, String str) {
        long a;
        synchronized (c.class) {
            a = (long) b.a(context.getApplicationContext()).a("analytics_event", "_id= ?", new
                    String[]{str});
        }
        return a;
    }

    private static synchronized d a(Context context, String str, String[] strArr, String str2,
                                    String str3) {
        d dVar;
        synchronized (c.class) {
            dVar = new d();
            Cursor a = b.a(context.getApplicationContext()).a("analytics_event", new
                    String[]{"_id", "event_data"}, str, strArr, null, str3);
            while (a != null) {
                try {
                    if (!a.moveToNext()) {
                        break;
                    }
                    String string = a.getString(a.getColumnIndex("_id"));
                    if (!string.equals(str2)) {
                        String string2 = a.getString(a.getColumnIndex("event_data"));
                        dVar.a(string);
                        dVar.b(string2);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (a != null) {
                a.close();
            }
        }
        return dVar;
    }

    public static synchronized d a(Context context, long j) {
        d a;
        synchronized (c.class) {
            String str = "";
            if (j != -1) {
                str = String.valueOf(j);
            }
            int b = b.a(context.getApplicationContext()).b("analytics_event");
            com.zxinsight.common.util.c.b("db get message count ==>>" + b);
            if (b > 0) {
                a = a(context, null, null, str, String.valueOf(BaseImageDownloader
                        .DEFAULT_HTTP_CONNECT_TIMEOUT));
            } else {
                a = new d();
            }
        }
        return a;
    }
}
