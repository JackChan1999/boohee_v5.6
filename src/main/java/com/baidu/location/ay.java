package com.baidu.location;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.net.wifi.ScanResult;
import android.os.AsyncTask;
import android.os.Message;
import com.boohee.model.PartnerBlock;
import com.boohee.one.sport.DownloadService;
import com.boohee.utils.Utils;
import java.io.File;
import java.util.Locale;
import org.json.JSONObject;

class ay implements ax, n {
    private static ay im = null;
    private volatile boolean iA = false;
    private final String iB = (I + "/ls.db");
    private int iC = 0;
    private final String iD = "wof";
    private boolean iE = false;
    private final int iF = 10000;
    private String iG = null;
    private boolean in = false;
    private String io = null;
    private long ip = 0;
    private final int iq = 6;
    private double ir = 0.0d;
    private double is = 0.0d;
    private final String it = "bdcltb09";
    private double iu = 0.0d;
    private double iv = 0.0d;
    private boolean iw = false;
    private boolean ix = true;
    private boolean iy = false;
    private double iz = 0.0d;

    private class a extends AsyncTask {
        final /* synthetic */ ay a;

        private a(ay ayVar) {
            this.a = ayVar;
        }

        protected Boolean a(Boolean... boolArr) {
            SQLiteDatabase sQLiteDatabase = null;
            if (boolArr.length != 4) {
                return Boolean.valueOf(false);
            }
            try {
                sQLiteDatabase = SQLiteDatabase.openOrCreateDatabase(this.a.iB, null);
            } catch (Exception e) {
            }
            if (sQLiteDatabase == null) {
                return Boolean.valueOf(false);
            }
            int currentTimeMillis = (int) (System.currentTimeMillis() >> 28);
            try {
                sQLiteDatabase.beginTransaction();
                if (boolArr[0].booleanValue()) {
                    try {
                        sQLiteDatabase.execSQL("delete from wof where ac < " + (currentTimeMillis - 35));
                    } catch (Exception e2) {
                    }
                }
                if (boolArr[1].booleanValue()) {
                    try {
                        sQLiteDatabase.execSQL("delete from bdcltb09 where ac is NULL or ac < " + (currentTimeMillis - 130));
                    } catch (Exception e3) {
                    }
                }
                sQLiteDatabase.setTransactionSuccessful();
                sQLiteDatabase.endTransaction();
                sQLiteDatabase.close();
            } catch (Exception e4) {
            }
            return Boolean.valueOf(true);
        }

        protected /* bridge */ /* synthetic */ Object doInBackground(Object[] objArr) {
            return a((Boolean[]) objArr);
        }
    }

    private class b extends AsyncTask {
        final /* synthetic */ ay a;

        private b(ay ayVar) {
            this.a = ayVar;
        }

        protected Boolean a(Object... objArr) {
            SQLiteDatabase sQLiteDatabase = null;
            if (objArr.length != 4) {
                this.a.iA = false;
                return Boolean.valueOf(false);
            }
            SQLiteDatabase openOrCreateDatabase;
            try {
                openOrCreateDatabase = SQLiteDatabase.openOrCreateDatabase(this.a.iB, null);
            } catch (Exception e) {
                openOrCreateDatabase = sQLiteDatabase;
            }
            if (openOrCreateDatabase == null) {
                this.a.iA = false;
                return Boolean.valueOf(false);
            }
            try {
                openOrCreateDatabase.beginTransaction();
                this.a.if((String) objArr[0], (com.baidu.location.t.a) objArr[1], openOrCreateDatabase);
                this.a.if((b) objArr[2], (BDLocation) objArr[3], openOrCreateDatabase);
                openOrCreateDatabase.setTransactionSuccessful();
                openOrCreateDatabase.endTransaction();
                openOrCreateDatabase.close();
            } catch (Exception e2) {
            }
            this.a.iA = false;
            return Boolean.valueOf(true);
        }

        protected /* bridge */ /* synthetic */ Object doInBackground(Object[] objArr) {
            return a(objArr);
        }
    }

    private ay() {
        try {
            cg();
        } catch (Exception e) {
        }
    }

    private String byte(boolean z) {
        Object obj;
        double d;
        double d2 = 0.0d;
        double d3 = 0.0d;
        boolean z2 = false;
        if (this.iE) {
            d2 = this.is;
            d3 = this.ir;
            z2 = true;
            obj = 1;
            d = 246.4d;
        } else if (this.iy) {
            d2 = this.iv;
            d3 = this.iu;
            double d4 = this.iz;
            z2 = ah.ay().aA();
            int i = 1;
            d = d4;
        } else {
            obj = null;
            d = 0.0d;
        }
        j.cZ().if(this.iy, this.iE, d2, d3);
        if (obj == null) {
            return z ? "{\"result\":{\"time\":\"" + c.new() + "\",\"error\":\"67\"}}" : "{\"result\":{\"time\":\"" + c.new() + "\",\"error\":\"63\"}}";
        } else {
            if (z) {
                return String.format(Locale.CHINA, "{\"result\":{\"time\":\"" + c.new() + "\",\"error\":\"66\"},\"content\":{\"point\":{\"x\":" + "\"%f\",\"y\":\"%f\"},\"radius\":\"%f\",\"isCellChanged\":\"%b\"}}", new Object[]{Double.valueOf(d2), Double.valueOf(d3), Double.valueOf(d), Boolean.valueOf(true)});
            }
            return String.format(Locale.CHINA, "{\"result\":{\"time\":\"" + c.new() + "\",\"error\":\"68\"},\"content\":{\"point\":{\"x\":" + "\"%f\",\"y\":\"%f\"},\"radius\":\"%f\",\"isCellChanged\":\"%b\"}}", new Object[]{Double.valueOf(d2), Double.valueOf(d3), Double.valueOf(d), Boolean.valueOf(z2)});
        }
    }

    public static ay cd() {
        if (im == null) {
            im = new ay();
        }
        return im;
    }

    private void ce() {
        SQLiteDatabase openOrCreateDatabase;
        try {
            openOrCreateDatabase = SQLiteDatabase.openOrCreateDatabase(this.iB, null);
        } catch (Exception e) {
            openOrCreateDatabase = null;
        }
        if (openOrCreateDatabase != null) {
            long queryNumEntries = DatabaseUtils.queryNumEntries(openOrCreateDatabase, "wof");
            long queryNumEntries2 = DatabaseUtils.queryNumEntries(openOrCreateDatabase, "bdcltb09");
            boolean z = queryNumEntries > 10000;
            boolean z2 = queryNumEntries2 > 10000;
            if (z || z2) {
                new a().execute(new Boolean[]{Boolean.valueOf(z), Boolean.valueOf(z2)});
            }
            openOrCreateDatabase.close();
        }
    }

    private void cf() {
        com.baidu.location.t.a ak = t.an().ak();
        if (ak != null) {
            r(ak.a());
        }
        for(ar.bW().bS());
    }

    private void cg() {
        try {
            File file = new File(I);
            File file2 = new File(this.iB);
            if (!file.exists()) {
                file.mkdirs();
            }
            if (!file2.exists()) {
                file2.createNewFile();
            }
            if (file2.exists()) {
                SQLiteDatabase openOrCreateDatabase = SQLiteDatabase.openOrCreateDatabase(file2, null);
                openOrCreateDatabase.execSQL("CREATE TABLE IF NOT EXISTS bdcltb09(id CHAR(40) PRIMARY KEY,time DOUBLE,tag DOUBLE, type DOUBLE , ac INT);");
                openOrCreateDatabase.execSQL("CREATE TABLE IF NOT EXISTS wof(id CHAR(15) PRIMARY KEY,mktime DOUBLE,time DOUBLE, ac INT, bc INT, cc INT);");
                openOrCreateDatabase.setVersion(1);
                openOrCreateDatabase.close();
            }
        } catch (Exception e) {
        }
    }

    private void for(b bVar) {
        System.currentTimeMillis();
        this.iE = false;
        if (bVar.for != null) {
            SQLiteDatabase openOrCreateDatabase;
            try {
                openOrCreateDatabase = SQLiteDatabase.openOrCreateDatabase(this.iB, null);
            } catch (Exception e) {
                openOrCreateDatabase = null;
            }
            if (openOrCreateDatabase != null && bVar != null) {
                int i = 0;
                double[] dArr = null;
                Object obj = null;
                int i2 = 0;
                double d = 0.0d;
                double d2 = 0.0d;
                int i3 = 0;
                for (ScanResult scanResult : bVar.for) {
                    int i4 = i3 + 1;
                    if (i4 > 10) {
                        break;
                    }
                    int i5;
                    double[] dArr2;
                    Object obj2;
                    int i6;
                    double d3;
                    double d4;
                    try {
                        Cursor rawQuery = openOrCreateDatabase.rawQuery("select * from wof where id = \"" + Jni.j(scanResult.BSSID.replace(":", "")) + "\";", null);
                        if (rawQuery.moveToFirst()) {
                            double d5 = rawQuery.getDouble(1) - 113.2349d;
                            double d6 = rawQuery.getDouble(2) - 432.1238d;
                            rawQuery.getInt(3);
                            int i7 = rawQuery.getInt(4);
                            int i8 = rawQuery.getInt(5);
                            rawQuery.close();
                            if (i8 <= 8 || i8 <= i7) {
                                float[] fArr;
                                if (this.iy) {
                                    fArr = new float[1];
                                    Location.distanceBetween(d6, d5, this.iu, this.iv, fArr);
                                    if (((double) fArr[0]) > this.iz + 2000.0d) {
                                        i3 = i4;
                                    } else {
                                        d2 += d5;
                                        d += d6;
                                        i5 = i;
                                        dArr2 = dArr;
                                        obj2 = 1;
                                        i6 = i2 + 1;
                                        d3 = d;
                                        d4 = d2;
                                    }
                                } else if (obj != null) {
                                    fArr = new float[1];
                                    Location.distanceBetween(d6, d5, d / ((double) i2), d2 / ((double) i2), fArr);
                                    if (fArr[0] > 1000.0f) {
                                        i3 = i4;
                                    } else {
                                        i5 = i;
                                        dArr2 = dArr;
                                        obj2 = obj;
                                        i6 = i2;
                                        d3 = d;
                                        d4 = d2;
                                    }
                                } else if (dArr == null) {
                                    dArr = new double[8];
                                    r6 = i + 1;
                                    try {
                                        dArr[i] = d5;
                                        i = r6 + 1;
                                        dArr[r6] = d6;
                                        i5 = i;
                                        dArr2 = dArr;
                                        obj2 = obj;
                                        i6 = i2;
                                        d3 = d;
                                        d4 = d2;
                                    } catch (Exception e2) {
                                        i5 = r6;
                                        dArr2 = dArr;
                                        obj2 = obj;
                                        i6 = i2;
                                        d4 = d2;
                                        d3 = d;
                                    }
                                } else {
                                    int i9 = 0;
                                    while (i9 < i) {
                                        Object obj3;
                                        fArr = new float[1];
                                        Location.distanceBetween(d6, d5, dArr[i9 + 1], dArr[i9], fArr);
                                        if (fArr[0] < 1000.0f) {
                                            obj3 = 1;
                                            try {
                                                d2 += dArr[i9];
                                                d4 = dArr[i9 + 1] + d;
                                                i7 = i2 + 1;
                                                d = d2;
                                            } catch (Exception e3) {
                                                i5 = i;
                                                dArr2 = dArr;
                                                int i10 = 1;
                                                i6 = i2;
                                                d4 = d2;
                                                d3 = d;
                                            }
                                        } else {
                                            obj3 = obj;
                                            i7 = i2;
                                            d4 = d;
                                            d = d2;
                                        }
                                        i9 += 2;
                                        obj = obj3;
                                        i2 = i7;
                                        d2 = d;
                                        d = d4;
                                    }
                                    if (obj != null) {
                                        d2 += d5;
                                        d += d6;
                                        i5 = i;
                                        dArr2 = dArr;
                                        obj2 = obj;
                                        i6 = i2 + 1;
                                        d3 = d;
                                        d4 = d2;
                                    } else if (i < 8) {
                                        r6 = i + 1;
                                        dArr[i] = d5;
                                        i = r6 + 1;
                                        dArr[r6] = d6;
                                        i5 = i;
                                        dArr2 = dArr;
                                        obj2 = obj;
                                        i6 = i2;
                                        d3 = d;
                                        d4 = d2;
                                    } else {
                                        openOrCreateDatabase.close();
                                        return;
                                    }
                                }
                                if (i6 > 4) {
                                    i2 = i6;
                                    d = d3;
                                    d2 = d4;
                                    break;
                                }
                                i = i5;
                                dArr = dArr2;
                                obj = obj2;
                                i2 = i6;
                                d = d3;
                                d2 = d4;
                                i3 = i4;
                            } else {
                                i3 = i4;
                            }
                        } else {
                            rawQuery.close();
                            i3 = i4;
                        }
                    } catch (Exception e4) {
                        i5 = i;
                        dArr2 = dArr;
                        obj2 = obj;
                        i6 = i2;
                        d3 = d;
                        d4 = d2;
                    }
                }
                if (i2 > 0) {
                    this.iE = true;
                    this.is = d2 / ((double) i2);
                    this.ir = d / ((double) i2);
                }
                openOrCreateDatabase.close();
            }
        }
    }

    private void if(b bVar, BDLocation bDLocation, SQLiteDatabase sQLiteDatabase) {
        if (bDLocation != null && bDLocation.getLocType() == 161) {
            if (("wf".equals(bDLocation.getNetworkLocationType()) || bDLocation.getRadius() < 300.0f) && bVar.for != null) {
                int currentTimeMillis = (int) (System.currentTimeMillis() >> 28);
                System.currentTimeMillis();
                int i = 0;
                for (ScanResult scanResult : bVar.for) {
                    if (scanResult.level != 0) {
                        int i2 = i + 1;
                        if (i2 <= 6) {
                            ContentValues contentValues = new ContentValues();
                            String j = Jni.j(scanResult.BSSID.replace(":", ""));
                            try {
                                int i3;
                                int i4;
                                double d;
                                Object obj;
                                double d2;
                                Cursor rawQuery = sQLiteDatabase.rawQuery("select * from wof where id = \"" + j + "\";", null);
                                if (rawQuery == null || !rawQuery.moveToFirst()) {
                                    i3 = 0;
                                    i4 = 0;
                                    d = 0.0d;
                                    obj = null;
                                    d2 = 0.0d;
                                } else {
                                    double d3 = rawQuery.getDouble(1) - 113.2349d;
                                    double d4 = rawQuery.getDouble(2) - 432.1238d;
                                    rawQuery.getInt(3);
                                    int i5 = rawQuery.getInt(4);
                                    i3 = rawQuery.getInt(5);
                                    i4 = i5;
                                    d = d3;
                                    double d5 = d4;
                                    obj = 1;
                                    d2 = d5;
                                }
                                rawQuery.close();
                                if (obj == null) {
                                    contentValues.put("mktime", Double.valueOf(bDLocation.getLongitude() + 113.2349d));
                                    contentValues.put("time", Double.valueOf(bDLocation.getLatitude() + 432.1238d));
                                    contentValues.put("bc", Integer.valueOf(1));
                                    contentValues.put("cc", Integer.valueOf(1));
                                    contentValues.put("ac", Integer.valueOf(currentTimeMillis));
                                    contentValues.put("id", j);
                                    sQLiteDatabase.insert("wof", null, contentValues);
                                } else if (i3 == 0) {
                                    i = i2;
                                } else {
                                    float[] fArr = new float[1];
                                    Location.distanceBetween(d2, d, bDLocation.getLatitude(), bDLocation.getLongitude(), fArr);
                                    if (fArr[0] > 1500.0f) {
                                        int i6 = i3 + 1;
                                        if (i6 <= 10 || i6 <= i4 * 3) {
                                            contentValues.put("cc", Integer.valueOf(i6));
                                        } else {
                                            contentValues.put("mktime", Double.valueOf(bDLocation.getLongitude() + 113.2349d));
                                            contentValues.put("time", Double.valueOf(bDLocation.getLatitude() + 432.1238d));
                                            contentValues.put("bc", Integer.valueOf(1));
                                            contentValues.put("cc", Integer.valueOf(1));
                                            contentValues.put("ac", Integer.valueOf(currentTimeMillis));
                                        }
                                    } else {
                                        d2 = ((d2 * ((double) i4)) + bDLocation.getLatitude()) / ((double) (i4 + 1));
                                        ContentValues contentValues2 = contentValues;
                                        contentValues2.put("mktime", Double.valueOf((((d * ((double) i4)) + bDLocation.getLongitude()) / ((double) (i4 + 1))) + 113.2349d));
                                        contentValues.put("time", Double.valueOf(d2 + 432.1238d));
                                        contentValues.put("bc", Integer.valueOf(i4 + 1));
                                        contentValues.put("ac", Integer.valueOf(currentTimeMillis));
                                    }
                                    try {
                                        if (sQLiteDatabase.update("wof", contentValues, "id = \"" + j + com.alipay.sdk.sys.a.e, null) <= 0) {
                                        }
                                    } catch (Exception e) {
                                    }
                                }
                            } catch (Exception e2) {
                            }
                            i = i2;
                        } else {
                            return;
                        }
                    }
                }
            }
        }
    }

    private void if(String str, com.baidu.location.t.a aVar, SQLiteDatabase sQLiteDatabase) {
        Object obj = null;
        double d = 0.0d;
        if (aVar.for() && ah.ay().aA()) {
            System.currentTimeMillis();
            int currentTimeMillis = (int) (System.currentTimeMillis() >> 28);
            String a = aVar.a();
            try {
                double parseDouble;
                float parseFloat;
                JSONObject jSONObject = new JSONObject(str);
                int parseInt = Integer.parseInt(jSONObject.getJSONObject("result").getString("error"));
                int i;
                if (parseInt == 161) {
                    JSONObject jSONObject2 = jSONObject.getJSONObject(Utils.RESPONSE_CONTENT);
                    if (jSONObject2.has("clf")) {
                        String string = jSONObject2.getString("clf");
                        if (string.equals("0")) {
                            JSONObject jSONObject3 = jSONObject2.getJSONObject(PartnerBlock.TYPE_POINT);
                            d = Double.parseDouble(jSONObject3.getString("x"));
                            parseDouble = Double.parseDouble(jSONObject3.getString("y"));
                            parseFloat = Float.parseFloat(jSONObject2.getString(com.baidu.location.a.a.else));
                        } else {
                            String[] split = string.split("\\|");
                            d = Double.parseDouble(split[0]);
                            parseDouble = Double.parseDouble(split[1]);
                            parseFloat = Float.parseFloat(split[2]);
                        }
                    }
                    i = 1;
                    parseFloat = 0.0f;
                    parseDouble = 0.0d;
                } else {
                    if (parseInt == 167) {
                        sQLiteDatabase.delete("bdcltb09", "id = \"" + a + com.alipay.sdk.sys.a.e, null);
                        return;
                    }
                    i = 1;
                    parseFloat = 0.0f;
                    parseDouble = 0.0d;
                }
                if (obj == null) {
                    d += 1235.4323d;
                    parseDouble += 2367.3217d;
                    float f = 4326.0f + parseFloat;
                    ContentValues contentValues = new ContentValues();
                    contentValues.put("time", Double.valueOf(d));
                    contentValues.put(DownloadService.EXTRA_TAG, Float.valueOf(f));
                    contentValues.put("type", Double.valueOf(parseDouble));
                    contentValues.put("ac", Integer.valueOf(currentTimeMillis));
                    try {
                        if (sQLiteDatabase.update("bdcltb09", contentValues, "id = \"" + a + com.alipay.sdk.sys.a.e, null) <= 0) {
                            contentValues.put("id", a);
                            sQLiteDatabase.insert("bdcltb09", null, contentValues);
                        }
                    } catch (Exception e) {
                    }
                }
            } catch (Exception e2) {
            }
        }
    }

    private void k(Message message) {
        k.p().if(case(true), message);
    }

    private void r(String str) {
        SQLiteDatabase sQLiteDatabase = null;
        if (str != null && !str.equals(this.iG)) {
            try {
                sQLiteDatabase = SQLiteDatabase.openOrCreateDatabase(this.iB, null);
            } catch (Exception e) {
            }
            if (sQLiteDatabase == null || str == null) {
                this.iy = false;
                return;
            }
            this.iy = false;
            try {
                Cursor rawQuery = sQLiteDatabase.rawQuery("select * from bdcltb09 where id = \"" + str + "\";", null);
                this.iG = str;
                this.ip = System.currentTimeMillis();
                if (rawQuery != null) {
                    if (rawQuery.moveToFirst()) {
                        this.iv = rawQuery.getDouble(1) - 1235.4323d;
                        this.iz = rawQuery.getDouble(2) - 4326.0d;
                        this.iu = rawQuery.getDouble(3) - 2367.3217d;
                        this.iy = true;
                    }
                    rawQuery.close();
                }
            } catch (Exception e2) {
            }
            sQLiteDatabase.close();
        }
    }

    public BDLocation case(boolean z) {
        cf();
        return new BDLocation(byte(z));
    }

    public void ch() {
    }

    public void ci() {
        this.ix = true;
        ab.bj().postDelayed(new Runnable(this) {
            final /* synthetic */ ay a;

            {
                this.a = r1;
            }

            public void run() {
                if (ab.gv) {
                    this.a.ce();
                }
            }
        }, 3000);
    }

    public void if(String str, com.baidu.location.t.a aVar, b bVar, BDLocation bDLocation) {
        int i = (aVar.for() && ah.ay().aA()) ? 0 : true;
        int i2 = (bDLocation == null || bDLocation.getLocType() != 161 || (!"wf".equals(bDLocation.getNetworkLocationType()) && bDLocation.getRadius() >= 300.0f)) ? true : 0;
        if (bVar.for == null) {
            i2 = true;
        }
        if ((i == 0 || r0 == 0) && !this.iA) {
            this.iA = true;
            new b().execute(new Object[]{str, aVar, bVar, bDLocation});
        }
    }

    public void j(Message message) {
        k(message);
    }
}
