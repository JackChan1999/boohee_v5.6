package com.mob.tools.utils;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.ActivityManager.RunningTaskInfo;
import android.bluetooth.BluetoothAdapter;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Environment;
import android.os.IBinder;
import android.os.Parcel;
import android.os.Process;
import android.os.SystemClock;
import android.provider.Settings.Secure;
import android.support.v4.os.EnvironmentCompat;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.boohee.model.ModelName;
import com.boohee.utility.Channel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.json.JSONArray;
import org.json.JSONException;

public class DeviceHelper {
    private static DeviceHelper deviceHelper;
    private        Context      context;

    private class GSConnection implements ServiceConnection {
        boolean got;
        private final BlockingQueue<IBinder> iBinders;

        private GSConnection() {
            this.got = false;
            this.iBinders = new LinkedBlockingQueue();
        }

        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            try {
                this.iBinders.put(iBinder);
            } catch (Throwable th) {
                Ln.w(th);
            }
        }

        public void onServiceDisconnected(ComponentName componentName) {
        }

        public IBinder takeBinder() throws InterruptedException {
            if (this.got) {
                throw new IllegalStateException();
            }
            this.got = true;
            return (IBinder) this.iBinders.poll(1500, TimeUnit.MILLISECONDS);
        }
    }

    private DeviceHelper(Context context) {
        this.context = context.getApplicationContext();
    }

    public static DeviceHelper getInstance(Context context) {
        if (deviceHelper == null && context != null) {
            deviceHelper = new DeviceHelper(context);
        }
        return deviceHelper;
    }

    private String getLocalDeviceKey() throws Throwable {
        if (!getSdcardState()) {
            return null;
        }
        File file = new File(getSdcardPath(), "ShareSDK");
        if (!file.exists()) {
            return null;
        }
        File file2 = new File(file, ".dk");
        if (!file2.exists()) {
            return null;
        }
        ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(file2));
        Object readObject = objectInputStream.readObject();
        String valueOf = (readObject == null || !(readObject instanceof char[])) ? null : String
                .valueOf((char[]) readObject);
        objectInputStream.close();
        return valueOf;
    }

    private Object getSystemService(String str) {
        try {
            return this.context.getSystemService(str);
        } catch (Throwable th) {
            Ln.e(th);
            return null;
        }
    }

    private boolean is4GMobileNetwork() {
        TelephonyManager telephonyManager = (TelephonyManager) getSystemService("phone");
        if (telephonyManager == null) {
            return false;
        }
        return telephonyManager.getNetworkType() == 13;
    }

    private boolean isFastMobileNetwork() {
        TelephonyManager telephonyManager = (TelephonyManager) getSystemService("phone");
        if (telephonyManager == null) {
            return false;
        }
        switch (telephonyManager.getNetworkType()) {
            case 0:
                return false;
            case 1:
                return false;
            case 2:
                return false;
            case 3:
                return true;
            case 4:
                return false;
            case 5:
                return true;
            case 6:
                return true;
            case 7:
                return false;
            case 8:
                return true;
            case 9:
                return true;
            case 10:
                return true;
            case 11:
                return false;
            case 12:
                return true;
            case 13:
                return true;
            case 14:
                return true;
            case 15:
                return true;
            default:
                return false;
        }
    }

    private boolean isSystemApp(PackageInfo packageInfo) {
        return ((packageInfo.applicationInfo.flags & 1) == 1) || ((packageInfo.applicationInfo
                .flags & 128) == 1);
    }

    private void saveLocalDeviceKey(String str) throws Throwable {
        if (getSdcardState()) {
            File file = new File(getSdcardPath(), "ShareSDK");
            if (!file.exists()) {
                file.mkdirs();
            }
            File file2 = new File(file, ".dk");
            if (file2.exists()) {
                file2.delete();
            }
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream
                    (file2));
            objectOutputStream.writeObject(str.toCharArray());
            objectOutputStream.flush();
            objectOutputStream.close();
        }
    }

    public String Base64AES(String str, String str2) {
        String encodeToString;
        Throwable th;
        try {
            encodeToString = Base64.encodeToString(Data.AES128Encode(str2, str), 0);
            try {
                if (encodeToString.contains("\n")) {
                    encodeToString = encodeToString.replace("\n", "");
                }
            } catch (Throwable th2) {
                th = th2;
                Ln.w(th);
                return encodeToString;
            }
        } catch (Throwable th3) {
            Throwable th4 = th3;
            encodeToString = null;
            th = th4;
            Ln.w(th);
            return encodeToString;
        }
        return encodeToString;
    }

    public boolean checkPermission(String str) throws Throwable {
        return this.context.getPackageManager().checkPermission(str, getPackageName()) == 0;
    }

    public String getAdvertisingID() {
        try {
            Intent intent = new Intent("com.google.android.gms.ads.identifier.service.START");
            intent.setPackage("com.google.android.gms");
            Object gSConnection = new GSConnection();
            this.context.bindService(intent, gSConnection, 1);
            IBinder takeBinder = gSConnection.takeBinder();
            Parcel obtain = Parcel.obtain();
            Parcel obtain2 = Parcel.obtain();
            obtain.writeInterfaceToken("com.google.android.gms.ads.identifier.internal" +
                    ".IAdvertisingIdService");
            takeBinder.transact(1, obtain, obtain2, 0);
            obtain2.readException();
            String readString = obtain2.readString();
            obtain2.recycle();
            obtain.recycle();
            Ln.i("getAdvertisingID === %s", readString);
            return readString;
        } catch (Throwable th) {
            Ln.w(th);
            return null;
        }
    }

    public String getAndroidID() {
        Ln.i("getAndroidID === %s", Secure.getString(this.context.getContentResolver(),
                "android_id"));
        return Secure.getString(this.context.getContentResolver(), "android_id");
    }

    public String getAppName() {
        String str = this.context.getApplicationInfo().name;
        if (str != null) {
            return str;
        }
        int i = this.context.getApplicationInfo().labelRes;
        return i > 0 ? this.context.getString(i) : str;
    }

    public int getAppVersion() {
        int i = 0;
        try {
            return this.context.getPackageManager().getPackageInfo(this.context.getPackageName(),
                    0).versionCode;
        } catch (Throwable th) {
            Ln.d(th);
            return i;
        }
    }

    public String getAppVersionName() {
        try {
            return this.context.getPackageManager().getPackageInfo(this.context.getPackageName(),
                    0).versionName;
        } catch (Throwable th) {
            Ln.d(th);
            return "1.0";
        }
    }

    public String getBluetoothName() {
        try {
            BluetoothAdapter defaultAdapter = BluetoothAdapter.getDefaultAdapter();
            if (defaultAdapter != null) {
                return defaultAdapter.getName();
            }
        } catch (Throwable e) {
            Ln.e(e);
        }
        return null;
    }

    public String getBssid() {
        WifiManager wifiManager = (WifiManager) getSystemService("wifi");
        if (wifiManager == null) {
            return null;
        }
        WifiInfo connectionInfo = wifiManager.getConnectionInfo();
        if (connectionInfo == null) {
            return null;
        }
        String bssid = connectionInfo.getBSSID();
        if (bssid == null) {
            bssid = null;
        }
        return bssid;
    }

    public String getCarrier() {
        TelephonyManager telephonyManager = (TelephonyManager) getSystemService("phone");
        if (telephonyManager == null) {
            return "-1";
        }
        Object simOperator = telephonyManager.getSimOperator();
        return TextUtils.isEmpty(simOperator) ? "-1" : simOperator;
    }

    public String getCharAndNumr(int i) {
        long currentTimeMillis = System.currentTimeMillis() ^ SystemClock.elapsedRealtime();
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(currentTimeMillis);
        Random random = new Random();
        for (int i2 = 0; i2 < i; i2++) {
            if ("char".equalsIgnoreCase(random.nextInt(2) % 2 == 0 ? "char" : "num")) {
                stringBuffer.insert(i2 + 1, (char) (random.nextInt(26) + 97));
            } else {
                stringBuffer.insert(stringBuffer.length(), random.nextInt(10));
            }
        }
        return stringBuffer.toString().substring(0, 40);
    }

    public String getDetailNetworkTypeForStatic() {
        String toLowerCase = getNetworkType().toLowerCase();
        return (TextUtils.isEmpty(toLowerCase) || "none".equals(toLowerCase)) ? "none" :
                toLowerCase.startsWith("wifi") ? "wifi" : toLowerCase.startsWith("4g") ? "4g" :
                        toLowerCase.startsWith("3g") ? "3g" : toLowerCase.startsWith("2g") ? "2g"
                                : toLowerCase.startsWith("bluetooth") ? "bluetooth" : toLowerCase;
    }

    public String getDeviceData() {
        return Base64AES(getModel() + "|" + getOSVersion() + "|" + getManufacturer() + "|" +
                getCarrier() + "|" + getScreenSize(), getDeviceKey().substring(0, 16));
    }

    public String getDeviceDataNotAES() {
        return getModel() + "|" + getOSVersion() + "|" + getManufacturer() + "|" + getCarrier() +
                "|" + getScreenSize();
    }

    public String getDeviceId() {
        String mime = getMime();
        return mime == null ? getSerialNo() : mime;
    }

    public String getDeviceKey() {
        String localDeviceKey;
        String str = null;
        try {
            localDeviceKey = getLocalDeviceKey();
        } catch (Throwable th) {
            Ln.w(th);
            localDeviceKey = str;
        }
        if (localDeviceKey != null) {
            return localDeviceKey;
        }
        try {
            localDeviceKey = getMacAddress();
            String deviceId = getDeviceId();
            str = Data.byteToHex(Data.SHA1(localDeviceKey + ":" + deviceId + ":" + getModel()));
        } catch (Throwable th2) {
            Ln.d(th2);
        }
        if (TextUtils.isEmpty(str)) {
            str = getCharAndNumr(40);
        }
        if (str == null) {
            return str;
        }
        try {
            saveLocalDeviceKey(str);
            return str;
        } catch (Throwable th22) {
            Ln.w(th22);
            return str;
        }
    }

    public ArrayList<HashMap<String, String>> getInstalledApp(boolean z) {
        try {
            PackageManager packageManager = this.context.getPackageManager();
            List<PackageInfo> installedPackages = packageManager.getInstalledPackages(0);
            ArrayList<HashMap<String, String>> arrayList = new ArrayList();
            for (PackageInfo packageInfo : installedPackages) {
                if (z || !isSystemApp(packageInfo)) {
                    HashMap hashMap = new HashMap();
                    hashMap.put("pkg", packageInfo.packageName);
                    hashMap.put("name", packageInfo.applicationInfo.loadLabel(packageManager)
                            .toString());
                    hashMap.put("version", packageInfo.versionName);
                    arrayList.add(hashMap);
                }
            }
            return arrayList;
        } catch (Throwable th) {
            Ln.w(th);
            return new ArrayList();
        }
    }

    public String getLine1Number() {
        TelephonyManager telephonyManager = (TelephonyManager) getSystemService("phone");
        return telephonyManager == null ? "-1" : telephonyManager.getLine1Number();
    }

    public String getMacAddress() {
        WifiManager wifiManager = (WifiManager) getSystemService("wifi");
        if (wifiManager == null) {
            return null;
        }
        WifiInfo connectionInfo = wifiManager.getConnectionInfo();
        if (connectionInfo == null) {
            return null;
        }
        String macAddress = connectionInfo.getMacAddress();
        if (macAddress == null) {
            macAddress = null;
        }
        return macAddress;
    }

    public String getManufacturer() {
        return Build.MANUFACTURER;
    }

    public String getMime() {
        TelephonyManager telephonyManager = (TelephonyManager) getSystemService("phone");
        if (telephonyManager == null) {
            return null;
        }
        String deviceId = telephonyManager.getDeviceId();
        String str = "";
        if (deviceId != null) {
            str = new String(deviceId).replace("0", "");
        }
        return (deviceId == null || str.length() <= 0) ? null : deviceId;
    }

    public String getModel() {
        return Build.MODEL;
    }

    public String getNetworkOperator() {
        TelephonyManager telephonyManager = (TelephonyManager) getSystemService("phone");
        return telephonyManager == null ? null : telephonyManager.getNetworkOperator();
    }

    public String getNetworkType() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService
                ("connectivity");
        if (connectivityManager == null) {
            return "none";
        }
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetworkInfo == null || !activeNetworkInfo.isAvailable()) {
            return "none";
        }
        int type = activeNetworkInfo.getType();
        switch (type) {
            case 0:
                return is4GMobileNetwork() ? "4G" : isFastMobileNetwork() ? "3G" : "2G";
            case 1:
                return "wifi";
            case 6:
                return "wimax";
            case 7:
                return "bluetooth";
            case 8:
                return "dummy";
            case 9:
                return "ethernet";
            default:
                return String.valueOf(type);
        }
    }

    public String getNetworkTypeForStatic() {
        String toLowerCase = getNetworkType().toLowerCase();
        return (TextUtils.isEmpty(toLowerCase) || "none".equals(toLowerCase)) ? "none" :
                (toLowerCase.startsWith("4g") || toLowerCase.startsWith("3g") || toLowerCase
                        .startsWith("2g")) ? "cell" : toLowerCase.startsWith("wifi") ? "wifi" :
                        Channel.OTHER;
    }

    public String getOSLanguage() {
        return Locale.getDefault().getLanguage();
    }

    public String getOSVersion() {
        return String.valueOf(VERSION.SDK_INT);
    }

    public String getOSVersionName() {
        return VERSION.RELEASE;
    }

    public String getPackageName() {
        return this.context.getPackageName();
    }

    public int getPlatformCode() {
        return 1;
    }

    public JSONArray getRunningApp() {
        JSONArray jSONArray = new JSONArray();
        ActivityManager activityManager = (ActivityManager) getSystemService(ModelName.ACTIVITY);
        if (activityManager == null) {
            return jSONArray;
        }
        List<RunningAppProcessInfo> runningAppProcesses = activityManager.getRunningAppProcesses();
        if (runningAppProcesses == null) {
            return jSONArray;
        }
        for (RunningAppProcessInfo runningAppProcessInfo : runningAppProcesses) {
            jSONArray.put(runningAppProcessInfo.processName);
        }
        return jSONArray;
    }

    public String getRunningAppStr() throws JSONException {
        JSONArray runningApp = getRunningApp();
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < runningApp.length(); i++) {
            if (i > 0) {
                stringBuilder.append(',');
            }
            stringBuilder.append(String.valueOf(runningApp.get(i)));
        }
        return stringBuilder.toString();
    }

    public String getSSID() {
        WifiManager wifiManager = (WifiManager) getSystemService("wifi");
        if (wifiManager == null) {
            return null;
        }
        WifiInfo connectionInfo = wifiManager.getConnectionInfo();
        if (connectionInfo == null) {
            return null;
        }
        String ssid = connectionInfo.getSSID();
        if (ssid == null) {
            ssid = null;
        }
        return ssid;
    }

    public String getScreenSize() {
        int[] screenSize = R.getScreenSize(this.context);
        return this.context.getResources().getConfiguration().orientation == 1 ? screenSize[0] +
                "x" + screenSize[1] : screenSize[1] + "x" + screenSize[0];
    }

    public String getSdcardPath() {
        return Environment.getExternalStorageDirectory().getAbsolutePath();
    }

    public boolean getSdcardState() {
        try {
            return "mounted".equals(Environment.getExternalStorageState());
        } catch (Throwable th) {
            Ln.w(th);
            return false;
        }
    }

    public String getSerialNo() {
        if (VERSION.SDK_INT < 9) {
            return null;
        }
        try {
            Class cls = Class.forName("android.os.SystemProperties");
            return (String) cls.getMethod("get", new Class[]{String.class, String.class}).invoke
                    (cls, new Object[]{"ro.serialno", EnvironmentCompat.MEDIA_UNKNOWN});
        } catch (Throwable th) {
            Ln.d(th);
            return null;
        }
    }

    public String getSignMD5() {
        try {
            return Data.MD5(this.context.getPackageManager().getPackageInfo(getPackageName(), 64)
                    .signatures[0].toByteArray());
        } catch (Throwable e) {
            Ln.e(e);
            return null;
        }
    }

    public String getSimSerialNumber() {
        TelephonyManager telephonyManager = (TelephonyManager) getSystemService("phone");
        return telephonyManager == null ? "-1" : telephonyManager.getSimSerialNumber();
    }

    public String getTopTaskPackageName() {
        boolean checkPermission;
        try {
            checkPermission = checkPermission("android.permission.GET_TASKS");
        } catch (Throwable th) {
            Ln.w(th);
            checkPermission = false;
        }
        if (checkPermission) {
            try {
                ActivityManager activityManager = (ActivityManager) getSystemService(ModelName
                        .ACTIVITY);
                return activityManager == null ? null : ((RunningTaskInfo) activityManager
                        .getRunningTasks(1).get(0)).topActivity.getPackageName();
            } catch (Throwable th2) {
                Ln.w(th2);
            }
        }
        return null;
    }

    public void hideSoftInput(View view) {
        Object systemService = getSystemService("input_method");
        if (systemService != null) {
            ((InputMethodManager) systemService).hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public boolean isMainProcess(Context context) {
        if (context == null) {
            return false;
        }
        Object obj;
        int myPid = Process.myPid();
        for (RunningAppProcessInfo runningAppProcessInfo : ((ActivityManager) context
                .getSystemService(ModelName.ACTIVITY)).getRunningAppProcesses()) {
            if (runningAppProcessInfo.pid == myPid) {
                obj = runningAppProcessInfo.processName;
                break;
            }
        }
        obj = null;
        return context.getPackageName().equals(obj);
    }

    public boolean isRooted() {
        return false;
    }

    public void showSoftInput(View view) {
        Object systemService = getSystemService("input_method");
        if (systemService != null) {
            ((InputMethodManager) systemService).toggleSoftInputFromWindow(view.getWindowToken(),
                    2, 0);
        }
    }
}
