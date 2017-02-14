package com.alipay.security.mobile.module.deviceinfo;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.graphics.Point;
import android.hardware.Camera;
import android.hardware.Camera.Size;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Environment;
import android.os.StatFs;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import com.alipay.security.mobile.module.commonutils.CommonUtils;
import com.qiniu.android.dns.Record;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;

public class DeviceInfo {
    private static DeviceInfo a = new DeviceInfo();

    private class CameraSizeComparator implements Comparator<Size> {
        final /* synthetic */ DeviceInfo a;

        private CameraSizeComparator(DeviceInfo deviceInfo) {
            this.a = deviceInfo;
        }

        public int compare(Size size, Size size2) {
            return size.width == size2.width ? 0 : size.width > size2.width ? 1 : -1;
        }
    }

    private DeviceInfo() {
    }

    private static float a(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        Point point = new Point(displayMetrics.widthPixels, displayMetrics.heightPixels);
        return ((float) point.y) / ((float) point.x);
    }

    private static String a() {
        BufferedReader bufferedReader;
        Throwable th;
        String str = null;
        FileReader fileReader;
        try {
            fileReader = new FileReader("/sys/devices/system/cpu/cpu0/cpufreq/cpuinfo_max_freq");
            try {
                bufferedReader = new BufferedReader(fileReader, 8192);
            } catch (IOException e) {
                bufferedReader = null;
                try {
                    bufferedReader.close();
                } catch (IOException e2) {
                }
                try {
                    fileReader.close();
                } catch (IOException e3) {
                }
                return str;
            } catch (Throwable th2) {
                Throwable th3 = th2;
                bufferedReader = null;
                th = th3;
                try {
                    bufferedReader.close();
                } catch (IOException e4) {
                }
                try {
                    fileReader.close();
                } catch (IOException e5) {
                }
                throw th;
            }
            try {
                String readLine = bufferedReader.readLine();
                if (CommonUtils.isBlank(readLine)) {
                    try {
                        bufferedReader.close();
                    } catch (IOException e6) {
                    }
                    try {
                        fileReader.close();
                    } catch (IOException e7) {
                    }
                } else {
                    str = readLine.trim();
                    try {
                        bufferedReader.close();
                    } catch (IOException e8) {
                    }
                    try {
                        fileReader.close();
                    } catch (IOException e9) {
                    }
                }
            } catch (IOException e10) {
                bufferedReader.close();
                fileReader.close();
                return str;
            } catch (Throwable th4) {
                th = th4;
                bufferedReader.close();
                fileReader.close();
                throw th;
            }
        } catch (IOException e11) {
            bufferedReader = null;
            fileReader = null;
            bufferedReader.close();
            fileReader.close();
            return str;
        } catch (Throwable th22) {
            fileReader = null;
            th = th22;
            bufferedReader = null;
            bufferedReader.close();
            fileReader.close();
            throw th;
        }
        return str;
    }

    private String a(Context context, int i) {
        Camera open;
        Throwable th;
        String str = "%2$d*%3$d";
        String str2 = "";
        float a = a(context.getApplicationContext());
        Camera camera = null;
        try {
            open = Camera.open(i);
            try {
                List<Size> supportedPreviewSizes = open.getParameters().getSupportedPreviewSizes();
                Collections.sort(supportedPreviewSizes, new CameraSizeComparator());
                int i2 = 0;
                for (Size size : supportedPreviewSizes) {
                    if (size.width >= Record.TTL_MIN_SECONDS) {
                        if ((((double) Math.abs((((float) size.width) / ((float) size.height)) - a)) <= 0.03d ? 1 : null) != null) {
                            break;
                        }
                    }
                    i2++;
                }
                String format = ((Size) supportedPreviewSizes.get(i2 == supportedPreviewSizes.size() ? supportedPreviewSizes.size() - 1 : i2)) != null ? String.format(Locale.ENGLISH, str, new Object[]{Integer.valueOf(i), Integer.valueOf(((Size) supportedPreviewSizes.get(i2 == supportedPreviewSizes.size() ? supportedPreviewSizes.size() - 1 : i2)).width), Integer.valueOf(((Size) supportedPreviewSizes.get(i2 == supportedPreviewSizes.size() ? supportedPreviewSizes.size() - 1 : i2)).height)}) : str2;
                if (open == null) {
                    return format;
                }
                open.release();
                return format;
            } catch (RuntimeException e) {
                camera = open;
                if (camera != null) {
                    camera.release();
                    return str2;
                }
                return str2;
            } catch (Exception e2) {
                if (open != null) {
                    open.release();
                    return str2;
                }
                return str2;
            } catch (Throwable th2) {
                th = th2;
                if (open != null) {
                    open.release();
                }
                throw th;
            }
        } catch (RuntimeException e3) {
            if (camera != null) {
                camera.release();
                return str2;
            }
            return str2;
        } catch (Exception e4) {
            open = null;
            if (open != null) {
                open.release();
                return str2;
            }
            return str2;
        } catch (Throwable th3) {
            open = null;
            th = th3;
            if (open != null) {
                open.release();
            }
            throw th;
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static java.lang.String b() {
        /*
        r6 = 1;
        r0 = 0;
        r1 = "/proc/cpuinfo";
        r2 = new java.io.FileReader;	 Catch:{ IOException -> 0x0043, all -> 0x0053 }
        r2.<init>(r1);	 Catch:{ IOException -> 0x0043, all -> 0x0053 }
        r1 = new java.io.BufferedReader;	 Catch:{ IOException -> 0x0074, all -> 0x006d }
        r3 = 8192; // 0x2000 float:1.14794E-41 double:4.0474E-320;
        r1.<init>(r2, r3);	 Catch:{ IOException -> 0x0074, all -> 0x006d }
    L_0x0011:
        r3 = r1.readLine();	 Catch:{ IOException -> 0x0077, all -> 0x0072 }
        if (r3 == 0) goto L_0x003c;
    L_0x0017:
        r4 = com.alipay.security.mobile.module.commonutils.CommonUtils.isBlank(r3);	 Catch:{ IOException -> 0x0077, all -> 0x0072 }
        if (r4 != 0) goto L_0x0011;
    L_0x001d:
        r4 = ":";
        r3 = r3.split(r4);	 Catch:{ IOException -> 0x0077, all -> 0x0072 }
        if (r3 == 0) goto L_0x0011;
    L_0x0026:
        r4 = r3.length;	 Catch:{ IOException -> 0x0077, all -> 0x0072 }
        if (r4 <= r6) goto L_0x0011;
    L_0x0029:
        r4 = 0;
        r4 = r3[r4];	 Catch:{ IOException -> 0x0077, all -> 0x0072 }
        r5 = "BogoMIPS";
        r4 = r4.contains(r5);	 Catch:{ IOException -> 0x0077, all -> 0x0072 }
        if (r4 == 0) goto L_0x0011;
    L_0x0035:
        r4 = 1;
        r3 = r3[r4];	 Catch:{ IOException -> 0x0077, all -> 0x0072 }
        r0 = r3.trim();	 Catch:{ IOException -> 0x0077, all -> 0x0072 }
    L_0x003c:
        r2.close();	 Catch:{ IOException -> 0x0063 }
    L_0x003f:
        r1.close();	 Catch:{ IOException -> 0x0065 }
    L_0x0042:
        return r0;
    L_0x0043:
        r1 = move-exception;
        r1 = r0;
        r2 = r0;
    L_0x0046:
        if (r2 == 0) goto L_0x004b;
    L_0x0048:
        r2.close();	 Catch:{ IOException -> 0x0067 }
    L_0x004b:
        if (r1 == 0) goto L_0x0042;
    L_0x004d:
        r1.close();	 Catch:{ IOException -> 0x0051 }
        goto L_0x0042;
    L_0x0051:
        r1 = move-exception;
        goto L_0x0042;
    L_0x0053:
        r1 = move-exception;
        r2 = r0;
        r7 = r0;
        r0 = r1;
        r1 = r7;
    L_0x0058:
        if (r2 == 0) goto L_0x005d;
    L_0x005a:
        r2.close();	 Catch:{ IOException -> 0x0069 }
    L_0x005d:
        if (r1 == 0) goto L_0x0062;
    L_0x005f:
        r1.close();	 Catch:{ IOException -> 0x006b }
    L_0x0062:
        throw r0;
    L_0x0063:
        r2 = move-exception;
        goto L_0x003f;
    L_0x0065:
        r1 = move-exception;
        goto L_0x0042;
    L_0x0067:
        r2 = move-exception;
        goto L_0x004b;
    L_0x0069:
        r2 = move-exception;
        goto L_0x005d;
    L_0x006b:
        r1 = move-exception;
        goto L_0x0062;
    L_0x006d:
        r1 = move-exception;
        r7 = r1;
        r1 = r0;
        r0 = r7;
        goto L_0x0058;
    L_0x0072:
        r0 = move-exception;
        goto L_0x0058;
    L_0x0074:
        r1 = move-exception;
        r1 = r0;
        goto L_0x0046;
    L_0x0077:
        r3 = move-exception;
        goto L_0x0046;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alipay.security.mobile.module.deviceinfo.DeviceInfo.b():java.lang.String");
    }

    public static DeviceInfo getInstance() {
        return a;
    }

    public Map<String, Integer> getAllAppName(Context context) {
        Map<String, Integer> hashMap = new HashMap();
        try {
            List<PackageInfo> installedPackages = context.getPackageManager().getInstalledPackages(0);
            if (installedPackages != null && installedPackages.size() > 0) {
                for (PackageInfo packageInfo : installedPackages) {
                    hashMap.put(packageInfo.packageName, Integer.valueOf(packageInfo.applicationInfo.uid));
                }
            }
        } catch (Throwable th) {
        }
        return hashMap;
    }

    public String getAndroidID(Context context) {
        try {
            return Secure.getString(context.getContentResolver(), "android_id");
        } catch (Exception e) {
            return null;
        }
    }

    public String getBandVer() {
        try {
            Class cls = Class.forName("android.os.SystemProperties");
            Object newInstance = cls.newInstance();
            return (String) cls.getMethod("get", new Class[]{String.class, String.class}).invoke(newInstance, new Object[]{"gsm.version.baseband", "no message"});
        } catch (Exception e) {
            return null;
        }
    }

    public String getBluMac() {
        try {
            BluetoothAdapter defaultAdapter = BluetoothAdapter.getDefaultAdapter();
            return (defaultAdapter == null || defaultAdapter.isEnabled()) ? defaultAdapter.getAddress() : "";
        } catch (Exception e) {
            return null;
        }
    }

    public boolean getBluStatus() {
        try {
            BluetoothAdapter defaultAdapter = BluetoothAdapter.getDefaultAdapter();
            if (defaultAdapter != null && defaultAdapter.isEnabled()) {
                return true;
            }
        } catch (Exception e) {
        }
        return false;
    }

    public String getCPUHardware() {
        LineNumberReader lineNumberReader;
        Throwable th;
        InputStreamReader inputStreamReader = null;
        String str = "-1";
        InputStreamReader inputStreamReader2;
        try {
            inputStreamReader2 = new InputStreamReader(Runtime.getRuntime().exec("cat /proc/cpuinfo | grep Hardware").getInputStream());
            try {
                lineNumberReader = new LineNumberReader(inputStreamReader2);
                int i = 1;
                while (i < 100) {
                    try {
                        String readLine = lineNumberReader.readLine();
                        if (readLine != null) {
                            if (readLine.indexOf("Hardware") >= 0) {
                                str = readLine.substring(readLine.indexOf(":") + 1, readLine.length()).trim();
                                break;
                            }
                            i++;
                        }
                    } catch (Exception e) {
                        inputStreamReader = inputStreamReader2;
                    } catch (Throwable th2) {
                        th = th2;
                    }
                }
                try {
                    lineNumberReader.close();
                } catch (IOException e2) {
                }
                try {
                    inputStreamReader2.close();
                } catch (IOException e3) {
                }
            } catch (Exception e4) {
                lineNumberReader = null;
                inputStreamReader = inputStreamReader2;
                if (lineNumberReader != null) {
                    try {
                        lineNumberReader.close();
                    } catch (IOException e5) {
                    }
                }
                if (inputStreamReader != null) {
                    try {
                        inputStreamReader.close();
                    } catch (IOException e6) {
                    }
                }
                return str;
            } catch (Throwable th3) {
                th = th3;
                lineNumberReader = null;
                if (lineNumberReader != null) {
                    try {
                        lineNumberReader.close();
                    } catch (IOException e7) {
                    }
                }
                if (inputStreamReader2 != null) {
                    try {
                        inputStreamReader2.close();
                    } catch (IOException e8) {
                    }
                }
                throw th;
            }
        } catch (Exception e9) {
            lineNumberReader = null;
            if (lineNumberReader != null) {
                lineNumberReader.close();
            }
            if (inputStreamReader != null) {
                inputStreamReader.close();
            }
            return str;
        } catch (Throwable th4) {
            th = th4;
            lineNumberReader = null;
            inputStreamReader2 = null;
            if (lineNumberReader != null) {
                lineNumberReader.close();
            }
            if (inputStreamReader2 != null) {
                inputStreamReader2.close();
            }
            throw th;
        }
        return str;
    }

    public String getCPUSerial() {
        LineNumberReader lineNumberReader;
        Throwable th;
        InputStreamReader inputStreamReader = null;
        String str = "0000000000000000";
        InputStreamReader inputStreamReader2;
        try {
            inputStreamReader2 = new InputStreamReader(Runtime.getRuntime().exec("cat /proc/cpuinfo | grep Serial").getInputStream());
            try {
                lineNumberReader = new LineNumberReader(inputStreamReader2);
                int i = 1;
                while (i < 100) {
                    try {
                        String readLine = lineNumberReader.readLine();
                        if (readLine != null) {
                            if (readLine.indexOf("Serial") >= 0) {
                                str = readLine.substring(readLine.indexOf(":") + 1, readLine.length()).trim();
                                break;
                            }
                            i++;
                        }
                    } catch (Exception e) {
                        inputStreamReader = inputStreamReader2;
                    } catch (Throwable th2) {
                        th = th2;
                    }
                }
                try {
                    lineNumberReader.close();
                } catch (IOException e2) {
                }
                try {
                    inputStreamReader2.close();
                } catch (IOException e3) {
                }
            } catch (Exception e4) {
                lineNumberReader = null;
                inputStreamReader = inputStreamReader2;
                if (lineNumberReader != null) {
                    try {
                        lineNumberReader.close();
                    } catch (IOException e5) {
                    }
                }
                if (inputStreamReader != null) {
                    try {
                        inputStreamReader.close();
                    } catch (IOException e6) {
                    }
                }
                return str;
            } catch (Throwable th3) {
                th = th3;
                lineNumberReader = null;
                if (lineNumberReader != null) {
                    try {
                        lineNumberReader.close();
                    } catch (IOException e7) {
                    }
                }
                if (inputStreamReader2 != null) {
                    try {
                        inputStreamReader2.close();
                    } catch (IOException e8) {
                    }
                }
                throw th;
            }
        } catch (Exception e9) {
            lineNumberReader = null;
            if (lineNumberReader != null) {
                lineNumberReader.close();
            }
            if (inputStreamReader != null) {
                inputStreamReader.close();
            }
            return str;
        } catch (Throwable th4) {
            th = th4;
            lineNumberReader = null;
            inputStreamReader2 = null;
            if (lineNumberReader != null) {
                lineNumberReader.close();
            }
            if (inputStreamReader2 != null) {
                inputStreamReader2.close();
            }
            throw th;
        }
        return str;
    }

    public String getCpuCount() {
        try {
            return String.valueOf(new File("/sys/devices/system/cpu/").listFiles(new FileFilter(this) {
                final /* synthetic */ DeviceInfo a;

                {
                    this.a = r1;
                }

                public boolean accept(File file) {
                    return Pattern.matches("cpu[0-9]+", file.getName());
                }
            }).length);
        } catch (Exception e) {
            return "1";
        }
    }

    public String getCpuFrequent() {
        String a = a();
        return !CommonUtils.isBlank(a) ? a : b();
    }

    public String getCpuName() {
        FileReader fileReader;
        BufferedReader bufferedReader;
        Throwable th;
        String str = null;
        try {
            fileReader = new FileReader("/proc/cpuinfo");
            try {
                bufferedReader = new BufferedReader(fileReader);
                try {
                    String[] split = bufferedReader.readLine().split(":\\s+", 2);
                    if (split == null || split.length <= 1) {
                        try {
                            fileReader.close();
                        } catch (IOException e) {
                        }
                        try {
                            bufferedReader.close();
                        } catch (IOException e2) {
                        }
                        return str;
                    }
                    str = split[1];
                    try {
                        fileReader.close();
                    } catch (IOException e3) {
                    }
                    try {
                        bufferedReader.close();
                    } catch (IOException e4) {
                    }
                    return str;
                } catch (IOException e5) {
                    if (fileReader != null) {
                        try {
                            fileReader.close();
                        } catch (IOException e6) {
                        }
                    }
                    if (bufferedReader != null) {
                        try {
                            bufferedReader.close();
                        } catch (IOException e7) {
                        }
                    }
                    return str;
                } catch (Throwable th2) {
                    th = th2;
                    if (fileReader != null) {
                        try {
                            fileReader.close();
                        } catch (IOException e8) {
                        }
                    }
                    if (bufferedReader != null) {
                        try {
                            bufferedReader.close();
                        } catch (IOException e9) {
                        }
                    }
                    throw th;
                }
            } catch (IOException e10) {
                bufferedReader = null;
                if (fileReader != null) {
                    fileReader.close();
                }
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
                return str;
            } catch (Throwable th3) {
                Throwable th4 = th3;
                bufferedReader = null;
                th = th4;
                if (fileReader != null) {
                    fileReader.close();
                }
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
                throw th;
            }
        } catch (IOException e11) {
            bufferedReader = null;
            fileReader = null;
            if (fileReader != null) {
                fileReader.close();
            }
            if (bufferedReader != null) {
                bufferedReader.close();
            }
            return str;
        } catch (Throwable th32) {
            fileReader = null;
            th = th32;
            bufferedReader = null;
            if (fileReader != null) {
                fileReader.close();
            }
            if (bufferedReader != null) {
                bufferedReader.close();
            }
            throw th;
        }
    }

    public String getIMEI(Context context) {
        if (context == null) {
            return null;
        }
        try {
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService("phone");
            return telephonyManager != null ? telephonyManager.getDeviceId() : null;
        } catch (Exception e) {
            return null;
        }
    }

    public String getIMSI(Context context) {
        if (context == null) {
            return null;
        }
        try {
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService("phone");
            return telephonyManager != null ? telephonyManager.getSubscriberId() : null;
        } catch (Exception e) {
            return null;
        }
    }

    public String getKernelVersion() {
        Throwable th;
        String str = "";
        String str2 = "";
        try {
            InputStream fileInputStream = new FileInputStream("/proc/version");
            BufferedReader bufferedReader;
            try {
                bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream), 8192);
                while (true) {
                    try {
                        String readLine = bufferedReader.readLine();
                        if (readLine != null) {
                            str2 = str2 + readLine;
                        } else {
                            try {
                                break;
                            } catch (IOException e) {
                            }
                        }
                    } catch (IOException e2) {
                    } catch (Throwable th2) {
                        th = th2;
                    }
                }
                bufferedReader.close();
                fileInputStream.close();
            } catch (IOException e3) {
                bufferedReader = null;
                try {
                    bufferedReader.close();
                    fileInputStream.close();
                } catch (IOException e4) {
                }
                if (CommonUtils.isNotBlank(str2)) {
                    str2 = str2.substring(str2.indexOf("version ") + 8);
                    str = str2.substring(0, str2.indexOf(" "));
                }
                return str;
            } catch (Throwable th3) {
                th = th3;
                bufferedReader = null;
                try {
                    bufferedReader.close();
                    fileInputStream.close();
                } catch (IOException e5) {
                }
                throw th;
            }
            try {
                if (CommonUtils.isNotBlank(str2)) {
                    str2 = str2.substring(str2.indexOf("version ") + 8);
                    str = str2.substring(0, str2.indexOf(" "));
                }
            } catch (Exception e6) {
            }
        } catch (FileNotFoundException e7) {
        }
        return str;
    }

    public String getMACAddress(Context context) {
        try {
            return ((WifiManager) context.getSystemService("wifi")).getConnectionInfo().getMacAddress();
        } catch (Exception e) {
            return null;
        }
    }

    public String getMemorySize() {
        BufferedReader bufferedReader;
        Throwable th;
        BufferedReader bufferedReader2 = null;
        long j = 0;
        FileReader fileReader;
        try {
            fileReader = new FileReader("/proc/meminfo");
            try {
                bufferedReader = new BufferedReader(fileReader, 8192);
            } catch (IOException e) {
                if (fileReader != null) {
                    try {
                        fileReader.close();
                    } catch (IOException e2) {
                    }
                }
                if (bufferedReader2 != null) {
                    try {
                        bufferedReader2.close();
                    } catch (IOException e3) {
                    }
                }
                return String.valueOf(j);
            } catch (Throwable th2) {
                th = th2;
                bufferedReader = null;
                if (fileReader != null) {
                    try {
                        fileReader.close();
                    } catch (IOException e4) {
                    }
                }
                if (bufferedReader != null) {
                    try {
                        bufferedReader.close();
                    } catch (IOException e5) {
                    }
                }
                throw th;
            }
            try {
                String readLine = bufferedReader.readLine();
                if (readLine != null) {
                    j = (long) Integer.parseInt(readLine.split("\\s+")[1]);
                }
                try {
                    fileReader.close();
                } catch (IOException e6) {
                }
                try {
                    bufferedReader.close();
                } catch (IOException e7) {
                }
            } catch (IOException e8) {
                bufferedReader2 = bufferedReader;
                if (fileReader != null) {
                    fileReader.close();
                }
                if (bufferedReader2 != null) {
                    bufferedReader2.close();
                }
                return String.valueOf(j);
            } catch (Throwable th3) {
                th = th3;
                if (fileReader != null) {
                    fileReader.close();
                }
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
                throw th;
            }
        } catch (IOException e9) {
            fileReader = null;
            if (fileReader != null) {
                fileReader.close();
            }
            if (bufferedReader2 != null) {
                bufferedReader2.close();
            }
            return String.valueOf(j);
        } catch (Throwable th4) {
            th = th4;
            fileReader = null;
            bufferedReader = null;
            if (fileReader != null) {
                fileReader.close();
            }
            if (bufferedReader != null) {
                bufferedReader.close();
            }
            throw th;
        }
        return String.valueOf(j);
    }

    public String getNetworkType(Context context) {
        try {
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService("phone");
            if (telephonyManager != null) {
                return String.valueOf(telephonyManager.getNetworkType());
            }
        } catch (Exception e) {
        }
        return null;
    }

    public String getOperatorName(Context context) {
        if (context == null) {
            return null;
        }
        try {
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService("phone");
            return telephonyManager != null ? telephonyManager.getSimOperatorName() : null;
        } catch (Exception e) {
            return null;
        }
    }

    public String getOperatorType(Context context) {
        if (context == null) {
            return null;
        }
        try {
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService("phone");
            return telephonyManager != null ? telephonyManager.getSimOperator() : null;
        } catch (Exception e) {
            return null;
        }
    }

    public String getPhoneNumber(Context context) {
        String str = "";
        if (context == null) {
            return str;
        }
        try {
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService("phone");
            return telephonyManager != null ? telephonyManager.getLine1Number() : str;
        } catch (Exception e) {
            return str;
        }
    }

    public String getPropPreviewsSizeOfCamera(Context context) {
        a(context.getApplicationContext());
        int i = -1;
        StringBuilder stringBuilder = new StringBuilder();
        try {
            if (Integer.parseInt(VERSION.SDK) > 8) {
                i = Camera.getNumberOfCameras();
            }
        } catch (Throwable th) {
        }
        for (int i2 = 0; i2 < i; i2++) {
            String format;
            if (i2 == 0) {
                format = String.format(Locale.ENGLISH, "%1$d:%2$s", new Object[]{Integer.valueOf(i2), a(context, i2)});
            } else {
                format = String.format(Locale.ENGLISH, "#%1$d:%2$s", new Object[]{Integer.valueOf(i2), a(context, i2)});
            }
            stringBuilder.append(format);
        }
        return stringBuilder.toString();
    }

    public String getSDCardSize() {
        long j = 0;
        try {
            if ("mounted".equals(Environment.getExternalStorageState())) {
                StatFs statFs = new StatFs(Environment.getExternalStorageDirectory().getPath());
                j = ((long) statFs.getBlockCount()) * ((long) statFs.getBlockSize());
            }
        } catch (Exception e) {
        }
        return String.valueOf(j);
    }

    public String getSIMSerial(Context context) {
        try {
            return ((TelephonyManager) context.getSystemService("phone")).getSimSerialNumber();
        } catch (Exception e) {
            return "";
        }
    }

    public String getScreenDpi(Context context) {
        try {
            return context.getResources().getDisplayMetrics().densityDpi;
        } catch (Exception e) {
            return null;
        }
    }

    public String getScreenHeight(Context context) {
        try {
            return context.getResources().getDisplayMetrics().heightPixels;
        } catch (Exception e) {
            return null;
        }
    }

    public String getScreenResolution(Context context) {
        try {
            DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
            return Integer.toString(displayMetrics.widthPixels) + "*" + Integer.toString(displayMetrics.heightPixels);
        } catch (Exception e) {
            return null;
        }
    }

    public String getScreenWidth(Context context) {
        try {
            return context.getResources().getDisplayMetrics().widthPixels;
        } catch (Exception e) {
            return null;
        }
    }

    public String getSensorDigest(Context context) {
        if (context == null) {
            return null;
        }
        try {
            String sha1ByString;
            SensorManager sensorManager = (SensorManager) context.getSystemService("sensor");
            if (sensorManager != null) {
                List<Sensor> sensorList = sensorManager.getSensorList(-1);
                if (sensorList != null && sensorList.size() > 0) {
                    StringBuilder stringBuilder = new StringBuilder();
                    for (Sensor sensor : sensorList) {
                        stringBuilder.append(sensor.getName());
                        stringBuilder.append(sensor.getVersion());
                        stringBuilder.append(sensor.getVendor());
                    }
                    sha1ByString = CommonUtils.sha1ByString(stringBuilder.toString());
                    return sha1ByString;
                }
            }
            sha1ByString = null;
            return sha1ByString;
        } catch (Exception e) {
            return null;
        }
    }

    public String getSerialNumber() {
        String str = "";
        try {
            return Build.SERIAL;
        } catch (Exception e) {
            return str;
        }
    }

    public String getTotalInternalMemorySize() {
        long j = 0;
        try {
            StatFs statFs = new StatFs(Environment.getDataDirectory().getPath());
            j = ((long) statFs.getBlockCount()) * ((long) statFs.getBlockSize());
        } catch (Exception e) {
        }
        return String.valueOf(j);
    }

    public String getVoiceMailNumber(Context context) {
        if (context == null) {
            return null;
        }
        try {
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService("phone");
            return telephonyManager != null ? telephonyManager.getVoiceMailNumber() : null;
        } catch (Exception e) {
            return null;
        }
    }

    public String getVoiceMailTag(Context context) {
        if (context == null) {
            return null;
        }
        try {
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService("phone");
            return telephonyManager != null ? telephonyManager.getVoiceMailAlphaTag() : null;
        } catch (Exception e) {
            return null;
        }
    }

    public String getWifiBssid(Context context) {
        try {
            WifiManager wifiManager = (WifiManager) context.getSystemService("wifi");
            if (wifiManager.isWifiEnabled()) {
                return wifiManager.getConnectionInfo().getBSSID();
            }
        } catch (Throwable th) {
        }
        return "";
    }
}
