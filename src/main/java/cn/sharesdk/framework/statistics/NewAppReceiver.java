package cn.sharesdk.framework.statistics;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.text.TextUtils;
import com.mob.tools.utils.DeviceHelper;
import com.mob.tools.utils.Ln;
import com.mob.tools.utils.LocalDB;
import com.mob.tools.utils.R;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

public class NewAppReceiver extends BroadcastReceiver implements Callback {
    private static final String[] a = new String[]{"android.intent.action.PACKAGE_ADDED", "android.intent.action.PACKAGE_CHANGED", "android.intent.action.PACKAGE_REMOVED", "android.intent.action.PACKAGE_REPLACED"};
    private static NewAppReceiver b;
    private Context c;
    private IntentFilter[] d = new IntentFilter[]{new IntentFilter(), new IntentFilter()};
    private Handler e;

    private static class a {
        private LocalDB a;

        public a(Context context) {
            try {
                DeviceHelper instance = DeviceHelper.getInstance(context);
                String cachePath = R.getCachePath(context, null);
                if (instance.getSdcardState()) {
                    File file = new File(instance.getSdcardPath(), "ShareSDK");
                    if (file.exists()) {
                        this.a = new LocalDB();
                        this.a.open(new File(file, ".ba").getAbsolutePath());
                        return;
                    }
                }
                this.a = new LocalDB();
                File file2 = new File(cachePath, ".ba");
                if (!file2.getParentFile().exists()) {
                    file2.getParentFile().mkdirs();
                }
                this.a.open(file2.getAbsolutePath());
            } catch (Throwable e) {
                Ln.e(e);
                if (this.a == null) {
                    this.a = new LocalDB();
                }
            }
        }

        public ArrayList<HashMap<String, String>> a() {
            Object object = this.a.getObject("buffered_apps");
            return object == null ? new ArrayList() : (ArrayList) object;
        }

        public void a(long j) {
            this.a.putLong("buffered_apps_time", Long.valueOf(j));
        }

        public void a(ArrayList<HashMap<String, String>> arrayList) {
            this.a.putObject("buffered_apps", arrayList);
        }

        public long b() {
            return this.a.getLong("buffered_apps_time");
        }
    }

    private static class b extends Thread {
        private Context a;
        private a b;

        private b(Context context) {
            this.a = context;
            this.b = new a(context);
        }

        private ArrayList<HashMap<String, String>> a(HashMap<String, HashMap<String, String>> hashMap) {
            ArrayList<HashMap<String, String>> arrayList = new ArrayList();
            for (Entry value : hashMap.entrySet()) {
                arrayList.add(value.getValue());
            }
            return arrayList;
        }

        private HashMap<String, HashMap<String, String>> a(ArrayList<HashMap<String, String>> arrayList) {
            HashMap<String, HashMap<String, String>> hashMap = new HashMap();
            Iterator it = arrayList.iterator();
            while (it.hasNext()) {
                HashMap hashMap2 = (HashMap) it.next();
                String str = (String) hashMap2.get("pkg");
                if (!TextUtils.isEmpty(str)) {
                    hashMap.put(str, hashMap2);
                }
            }
            return hashMap;
        }

        public static void a(Context context) {
            new b(context).start();
        }

        public void run() {
            DeviceHelper instance = DeviceHelper.getInstance(this.a);
            ArrayList installedApp = instance.getInstalledApp(false);
            ArrayList a = this.b.a();
            this.b.a(installedApp);
            HashMap a2 = a(installedApp);
            HashMap a3 = a(a);
            Iterator it = a.iterator();
            while (it.hasNext()) {
                String str = (String) ((HashMap) it.next()).get("pkg");
                if (!TextUtils.isEmpty(str)) {
                    a2.remove(str);
                }
            }
            it = installedApp.iterator();
            while (it.hasNext()) {
                str = (String) ((HashMap) it.next()).get("pkg");
                if (!TextUtils.isEmpty(str)) {
                    a3.remove(str);
                }
            }
            ArrayList a4 = a(a2);
            ArrayList a5 = a(a3);
            if ((System.currentTimeMillis() - this.b.b() >= 2592000000L) || a.size() <= 0) {
                this.b.a(System.currentTimeMillis());
                try {
                    a.a(this.a).a("APPS_ALL", installedApp);
                } catch (Throwable th) {
                    Ln.e(th);
                }
            } else if (a4.size() > 0) {
                Ln.d("================== upload new apps: " + instance.getPackageName(), new Object[0]);
                try {
                    a.a(this.a).a("APPS_INCR", a4);
                } catch (Throwable th2) {
                    Ln.e(th2);
                }
            }
            if (a5.size() > 0) {
                Ln.d("================== upload new removes: " + instance.getPackageName(), new Object[0]);
                try {
                    a.a(this.a).a("UNINSTALL", a5);
                } catch (Throwable th22) {
                    Ln.e(th22);
                }
            }
        }
    }

    private NewAppReceiver(Context context) {
        int i = 0;
        this.c = context;
        this.d[0].addAction("cn.sharesdk.START_UP");
        String[] strArr = a;
        int length = strArr.length;
        while (i < length) {
            this.d[1].addAction(strArr[i]);
            i++;
        }
        this.d[1].addDataScheme("package");
        this.e = new Handler(this);
        this.e.sendEmptyMessage(1);
    }

    public static synchronized void a() {
        synchronized (NewAppReceiver.class) {
            if (b != null) {
                try {
                    b.c.unregisterReceiver(b);
                } catch (Throwable th) {
                    Ln.w(th);
                }
            }
        }
    }

    public static synchronized void a(Context context) {
        synchronized (NewAppReceiver.class) {
            if (b == null) {
                b = new NewAppReceiver(context);
            }
            a();
            try {
                for (IntentFilter registerReceiver : b.d) {
                    context.registerReceiver(b, registerReceiver);
                }
            } catch (Throwable th) {
                Ln.w(th);
            }
        }
        return;
    }

    private boolean a(String str) {
        for (String equals : a) {
            if (equals.equals(str)) {
                return true;
            }
        }
        return false;
    }

    public boolean handleMessage(Message message) {
        switch (message.what) {
            case 1:
                b.a(this.c);
                break;
        }
        return false;
    }

    public void onReceive(Context context, Intent intent) {
        int i;
        String str = null;
        if (intent != null) {
            str = intent.getAction();
        }
        if ("cn.sharesdk.START_UP".equals(str)) {
            String packageName = DeviceHelper.getInstance(context).getPackageName();
            String stringExtra = intent.getStringExtra("packageName");
            if (stringExtra != null && stringExtra.equals(packageName)) {
                i = 1;
            }
            i = 0;
        } else {
            if (a(str)) {
                i = 1;
            }
            i = 0;
        }
        if (i != 0) {
            Ln.d("========= receive broadcast: " + str, new Object[0]);
            this.e.removeMessages(1);
            this.e.sendEmptyMessageDelayed(1, 60000);
        }
    }
}
