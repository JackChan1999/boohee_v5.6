package cn.sharesdk.framework.statistics.a;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import com.mob.tools.utils.Ln;
import java.util.ArrayList;

public class e {
    public static int a = 0;
    public static int b = 1;
    public static int c = 2;

    public static synchronized long a(Context context, String str, long j) {
        long a;
        synchronized (e.class) {
            if (str != null) {
                if (str.trim() != "") {
                    b a2 = b.a(context);
                    ContentValues contentValues = new ContentValues();
                    contentValues.put("post_time", Long.valueOf(j));
                    contentValues.put("message_data", str.toString());
                    a = a2.a("message", contentValues);
                }
            }
            a = -1;
        }
        return a;
    }

    public static synchronized long a(Context context, ArrayList<String> arrayList) {
        long a;
        synchronized (e.class) {
            StringBuilder stringBuilder = new StringBuilder();
            for (int i = 0; i < arrayList.size(); i++) {
                stringBuilder.append("'");
                stringBuilder.append((String) arrayList.get(i));
                stringBuilder.append("'");
                stringBuilder.append(",");
            }
            Ln.e("delete COUNT == %s", Integer.valueOf(b.a(context).a("message", "_id in ( " + stringBuilder.toString().substring(0, stringBuilder.length() - 1) + " )", null)));
            a = (long) b.a(context).a("message", "_id in ( " + stringBuilder.toString().substring(0, stringBuilder.length() - 1) + " )", null);
        }
        return a;
    }

    public static synchronized ArrayList<d> a(Context context) {
        ArrayList<d> a;
        synchronized (e.class) {
            a = b.a(context).a("message") > 0 ? a(context, null, null) : new ArrayList();
        }
        return a;
    }

    private static synchronized ArrayList<d> a(Context context, String str, String[] strArr) {
        ArrayList<d> arrayList;
        synchronized (e.class) {
            arrayList = new ArrayList();
            d dVar = new d();
            StringBuilder stringBuilder = new StringBuilder();
            Cursor a = b.a(context).a("message", new String[]{"_id", "post_time", "message_data"}, str, strArr, null);
            StringBuilder stringBuilder2 = stringBuilder;
            d dVar2 = dVar;
            while (a != null && a.moveToNext()) {
                dVar2.b.add(a.getString(0));
                if (dVar2.b.size() == 100) {
                    stringBuilder2.append(a.getString(2));
                    dVar2.a = stringBuilder2.toString();
                    arrayList.add(dVar2);
                    dVar2 = new d();
                    stringBuilder2 = new StringBuilder();
                } else {
                    stringBuilder2.append(a.getString(2) + "\n");
                }
            }
            a.close();
            if (dVar2.b.size() != 0) {
                dVar2.a = stringBuilder2.toString().substring(0, stringBuilder2.length() - 1);
                arrayList.add(dVar2);
            }
        }
        return arrayList;
    }
}
