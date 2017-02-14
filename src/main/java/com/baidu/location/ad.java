package com.baidu.location;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.support.v4.media.session.PlaybackStateCompat;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.RandomAccessFile;
import java.util.zip.GZIPOutputStream;

class ad implements ax, SensorEventListener, b {
    private static ad kg;
    private Runnable j6 = null;
    private Sensor j7;
    private final int j8 = 2;
    private boolean j9 = false;
    private boolean ka = false;
    private int kb = 0;
    private boolean kc = false;
    private Sensor kd;
    private boolean ke = false;
    private Handler kf;
    private boolean kh = false;
    private int ki = 0;
    private StringBuffer kj = null;
    private SensorManager kk;
    private StringBuffer kl = null;
    private Runnable km = null;
    private final int kn = 1;

    class a {
        final /* synthetic */ ad a;
        StringBuffer do = null;
        public boolean if = false;

        a(ad adVar) {
            this.a = adVar;
        }

        public void a() {
        }

        public void do() {
        }

        public void if() {
        }
    }

    class b {
        final /* synthetic */ ad a;

        public b(ad adVar) {
            this.a = adVar;
        }
    }

    private ad() {
        try {
            this.kk = (SensorManager) f.getServiceContext().getSystemService("sensor");
            this.j7 = this.kk.getDefaultSensor(1);
            this.kd = this.kk.getDefaultSensor(2);
        } catch (Exception e) {
        }
        this.kf = new Handler();
    }

    private void cC() {
        if (this.kk != null && this.kd != null && !this.j9) {
            try {
                this.kk.registerListener(this, this.kd, 2, this.kf);
                this.j9 = true;
            } catch (Exception e) {
            }
        }
    }

    private void cD() {
        if (this.j9) {
            try {
                this.kk.unregisterListener(this, this.kd);
                this.j9 = false;
            } catch (Exception e) {
            }
        }
    }

    private void cE() {
        if (this.kk != null && this.j7 != null && !this.ka) {
            try {
                this.kk.registerListener(this, this.j7, 2, this.kf);
                this.ka = true;
            } catch (Exception e) {
            }
        }
    }

    private void cF() {
        if (this.kc || this.ke) {
            cE();
        }
        if (this.kc) {
            cC();
        }
    }

    private String cH() {
        int i = 0;
        String str = void(1);
        String[] strArr = new String[]{"lmibaca.dat", "lmibacb.dat"};
        int length = strArr.length;
        while (i < length) {
            String str2 = strArr[i];
            if (!new File(str + File.separator + str2).exists()) {
                return str + File.separator + str2;
            }
            i++;
        }
        return null;
    }

    private void cI() {
        if (this.ka) {
            try {
                this.kk.unregisterListener(this, this.j7);
                this.ka = false;
            } catch (Exception e) {
            }
        }
    }

    private void cJ() {
        this.kc = true;
        this.kj = new StringBuffer(8192);
        cF();
        this.km = new Runnable(this) {
            final /* synthetic */ ad a;

            {
                this.a = r1;
            }

            public void run() {
                this.a.cP();
            }
        };
        this.kf.postDelayed(this.km, 60000);
    }

    private void cL() {
        if (!this.kc) {
            if (this.ke) {
                cD();
                return;
            }
            cD();
            cI();
            this.ki = 0;
            this.kb = 0;
        }
    }

    public static ad cM() {
        if (kg == null) {
            kg = new ad();
        }
        return kg;
    }

    private String cN() {
        int i = 0;
        String str = void(2);
        String[] strArr = new String[]{"lbaca.dat", "lbacb.dat", "lbacc.dat", "lbacd.dat"};
        int length = strArr.length;
        while (i < length) {
            String str2 = strArr[i];
            if (!new File(str + File.separator + str2).exists()) {
                return str + File.separator + str2;
            }
            i++;
        }
        return null;
    }

    private boolean cQ() {
        String cR = cR();
        if (cR == null) {
            return false;
        }
        try {
            File file = new File(cR);
            RandomAccessFile randomAccessFile;
            if (file.exists()) {
                randomAccessFile = new RandomAccessFile(file, "rw");
                randomAccessFile.seek(4);
                long readLong = randomAccessFile.readLong();
                int readInt = randomAccessFile.readInt();
                if (randomAccessFile.readInt() == 3321) {
                    readLong = System.currentTimeMillis() - readLong;
                    if (readLong < 0 || readLong > com.umeng.analytics.a.h) {
                        randomAccessFile.seek(4);
                        randomAccessFile.writeLong(System.currentTimeMillis());
                        randomAccessFile.writeInt(0);
                        randomAccessFile.close();
                        return true;
                    } else if (readInt > 96000) {
                        randomAccessFile.close();
                        return false;
                    } else {
                        randomAccessFile.close();
                        return true;
                    }
                }
                if(randomAccessFile, 0);
                randomAccessFile.close();
                return true;
            }
            file.createNewFile();
            randomAccessFile = new RandomAccessFile(file, "rw");
            if(randomAccessFile, 0);
            randomAccessFile.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private String cR() {
        String str = void(1);
        if (str == null) {
            return null;
        }
        str = str + File.separator + "lscts.dat";
        File file = new File(str);
        if (!file.exists()) {
            try {
                file.createNewFile();
                RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw");
                if(randomAccessFile, 0);
                randomAccessFile.close();
            } catch (Exception e) {
                return null;
            }
        }
        return str;
    }

    private void d(int i) {
        String cR = cR();
        if (cR != null) {
            try {
                File file = new File(cR);
                if (file.exists()) {
                    RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw");
                    randomAccessFile.seek(0);
                    int readInt = randomAccessFile.readInt();
                    randomAccessFile.readLong();
                    int readInt2 = randomAccessFile.readInt();
                    if (randomAccessFile.readInt() == 3321 && readInt == 3321) {
                        readInt = readInt2 + i;
                        randomAccessFile.seek(12);
                        randomAccessFile.writeInt(readInt);
                    } else {
                        if(randomAccessFile, 48000 + i);
                    }
                    randomAccessFile.close();
                }
            } catch (Exception e) {
            }
        }
    }

    private void do(StringBuffer stringBuffer) {
        if (this.kc && this.kj != null) {
            if(this.kj, stringBuffer, e(2));
        }
    }

    private String e(int i) {
        String str = void(i);
        return str == null ? null : i == 2 ? str + File.separator + "lbacz.dat" : i == 1 ? str + File.separator + "lmibacz.dat" : null;
    }

    private void if(RandomAccessFile randomAccessFile, int i) {
        try {
            randomAccessFile.seek(0);
            randomAccessFile.writeInt(3321);
            randomAccessFile.writeLong(System.currentTimeMillis());
            randomAccessFile.writeInt(i);
            randomAccessFile.writeInt(3321);
        } catch (Exception e) {
        }
    }

    private void if(StringBuffer stringBuffer) {
        if (this.ke && this.kl != null) {
            if(this.kl, stringBuffer, e(1));
        }
    }

    private void if(StringBuffer stringBuffer, File file) {
        Object obj = 1;
        if (file.exists()) {
            try {
                GZIPOutputStream gZIPOutputStream = new GZIPOutputStream(new BufferedOutputStream(new FileOutputStream(file, true)));
                int i = 0;
                while (i < 3) {
                    try {
                        gZIPOutputStream.write(stringBuffer.toString().getBytes());
                    } catch (Exception e) {
                        obj = null;
                    }
                    if (obj == null) {
                        i++;
                    }
                }
                gZIPOutputStream.close();
            } catch (Exception e2) {
            }
        }
    }

    private void if(StringBuffer stringBuffer, StringBuffer stringBuffer2, String str) {
        if (stringBuffer.length() + stringBuffer2.length() < 8190) {
            stringBuffer.append(stringBuffer2);
            return;
        }
        File file = new File(str);
        d(stringBuffer.length());
        if(stringBuffer, file);
        this.kh = true;
        stringBuffer.delete(0, stringBuffer.length());
        stringBuffer.append(stringBuffer2);
    }

    private boolean if(File file) {
        try {
            file.createNewFile();
            StringBuffer stringBuffer = new StringBuffer(256);
            stringBuffer.append("C");
            stringBuffer.append("\t");
            stringBuffer.append(Jni.i(az.cn().cl()));
            stringBuffer.append("\n");
            if(stringBuffer, file);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private boolean if(File file, int i) {
        String str = null;
        if (i == 2) {
            str = cN();
        } else if (i == 1) {
            str = cH();
        }
        return str == null ? false : file.renameTo(new File(str));
    }

    private boolean long(int i) {
        boolean z = false;
        String e = e(i);
        if (e == null) {
            return z;
        }
        File file;
        File file2;
        if (i == 2) {
            file = new File(e);
            if (file.exists()) {
                if (file.length() <= 30720) {
                    return true;
                }
                if (!if(file, i)) {
                    return z;
                }
            }
            file2 = new File(e);
            if (file2.exists()) {
                return z;
            }
            try {
                return if(file2);
            } catch (Exception e2) {
                return z;
            }
        } else if (i != 1 || !cQ()) {
            return z;
        } else {
            file = new File(e);
            if (file.exists()) {
                if (file.length() <= 30720) {
                    return true;
                }
                if (!if(file, i)) {
                    return z;
                }
            }
            file2 = new File(e);
            if (file2.exists()) {
                return z;
            }
            try {
                return if(file2);
            } catch (Exception e3) {
                return z;
            }
        }
    }

    private String void(int i) {
        String str = c.else();
        if (str == null) {
            return null;
        }
        if (i == 1) {
            str = str + File.separator + "llmis1";
        } else if (i != 2) {
            return null;
        } else {
            str = str + File.separator + "llmis2";
        }
        File file = new File(str);
        if (file.exists()) {
            return str;
        }
        try {
            return !file.mkdirs() ? null : str;
        } catch (Exception e) {
            return null;
        }
    }

    public String cG() {
        String str = null;
        int i = 0;
        String str2 = void(1);
        String[] strArr = new String[]{"lmibaca.dat", "lmibacb.dat", "lmibacz.dat"};
        try {
            int length = strArr.length;
            while (i < length) {
                String str3 = strArr[i];
                File file = new File(str2 + File.separator + str3);
                if (file.exists()) {
                    if (file.length() > 92160) {
                        file.delete();
                    } else if (file.length() >= PlaybackStateCompat.ACTION_SKIP_TO_QUEUE_ITEM) {
                        if (!(str3.equals("lmibacz.dat") && this.ke)) {
                            str = str2 + File.separator + str3;
                        }
                        return str;
                    }
                }
                i++;
            }
        } catch (Exception e) {
        }
        return str;
    }

    public void cK() {
        this.kf.post(new Runnable(this) {
            final /* synthetic */ ad a;

            {
                this.a = r1;
            }

            public void run() {
                this.a.kf.removeCallbacks(this.a.km);
                this.a.kf.removeCallbacks(this.a.j6);
                this.a.cO();
                this.a.cP();
            }
        });
    }

    public void cO() {
        this.ke = false;
        if ((this.kl != null && this.kl.length() > 3800) || this.kh) {
            File file = new File(e(1));
            d(this.kl.length());
            if(this.kl, file);
            this.kh = false;
        }
        this.kl = null;
        cL();
    }

    public void cP() {
        this.kc = false;
        if (this.kj != null && this.kj.length() > 3800) {
            if(this.kj, new File(e(2)));
        }
        this.kj = null;
        cL();
    }

    public void goto(int i) {
        if (!this.ke && long(1)) {
            StringBuffer stringBuffer = new StringBuffer(128);
            this.ke = true;
            this.kl = new StringBuffer(8192);
            stringBuffer.append("T1");
            stringBuffer.append("\t");
            stringBuffer.append(i);
            stringBuffer.append("\n");
            if(stringBuffer);
            cF();
            this.j6 = new Runnable(this) {
                final /* synthetic */ ad a;

                {
                    this.a = r1;
                }

                public void run() {
                    this.a.cO();
                }
            };
            this.kf.postDelayed(this.j6, 8000);
        }
    }

    public void onAccuracyChanged(Sensor sensor, int i) {
    }

    public void onSensorChanged(SensorEvent sensorEvent) {
        int type = sensorEvent.sensor.getType();
        StringBuffer stringBuffer = new StringBuffer(256);
        if (type == 1) {
            this.ki++;
            stringBuffer.append("A");
            stringBuffer.append("\t");
            stringBuffer.append(sensorEvent.values[0]);
            stringBuffer.append("\t");
            stringBuffer.append(sensorEvent.values[1]);
            stringBuffer.append("\t");
            stringBuffer.append(sensorEvent.values[2]);
            if (this.ki == 1) {
                stringBuffer.append("\t");
                stringBuffer.append(sensorEvent.timestamp);
            }
            if (this.ki >= 14) {
                this.ki = 0;
            }
            stringBuffer.append("\n");
            do(stringBuffer);
            if(stringBuffer);
        }
        if (type == 2) {
            this.kb++;
            stringBuffer.append("M");
            stringBuffer.append("\t");
            stringBuffer.append(sensorEvent.values[0]);
            stringBuffer.append("\t");
            stringBuffer.append(sensorEvent.values[1]);
            stringBuffer.append("\t");
            stringBuffer.append(sensorEvent.values[2]);
            if (this.kb == 1) {
                stringBuffer.append("\t");
                stringBuffer.append(sensorEvent.timestamp);
            }
            if (this.kb > 13) {
                this.kb = 0;
            }
            stringBuffer.append("\n");
            do(stringBuffer);
        }
    }
}
