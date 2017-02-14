package com.baidu.location;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import dalvik.system.DexClassLoader;
import java.io.File;

public class f extends Service implements ax, n {
    private static final String ij = "app.jar";
    public static Context mC = null;
    public static String replaceFileName = "repll.jar";
    LLSInterface ii = null;
    LLSInterface ik = null;
    LLSInterface il = null;

    public static float getFrameVersion() {
        return n.V;
    }

    public static String getJarFileName() {
        return ij;
    }

    public static Context getServiceContext() {
        return mC;
    }

    public IBinder onBind(Intent intent) {
        return this.il.onBind(intent);
    }

    public void onCreate() {
        mC = getApplicationContext();
        System.currentTimeMillis();
        this.ik = new ab();
        try {
            File file = new File(c.goto() + File.separator + replaceFileName);
            if (file.exists()) {
                File file2 = new File(c.goto() + File.separator + ij);
                if (file2.exists()) {
                    file2.delete();
                }
                file.renameTo(file2);
            }
            this.ii = (LLSInterface) new DexClassLoader(c.goto() + File.separator + ij, c.goto(), null, getClassLoader()).loadClass("com.baidu.serverLoc.LocationService").newInstance();
        } catch (Exception e) {
            this.ii = null;
        }
        if (this.ii == null || this.ii.getVersion() <= this.ik.getVersion()) {
            this.il = this.ik;
            this.ii = null;
        } else {
            this.il = this.ii;
            this.ik = null;
        }
        this.il.onCreate(this);
    }

    public void onDestroy() {
        this.il.onDestroy();
    }

    public int onStartCommand(Intent intent, int i, int i2) {
        return this.il.onStartCommand(intent, i, i2);
    }

    public boolean onUnbind(Intent intent) {
        return this.il.onUnBind(intent);
    }
}
