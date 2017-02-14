package com.baidu.location;

import android.location.Location;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import com.alipay.security.mobile.module.deviceinfo.constant.DeviceInfoConstant;
import com.baidu.location.t.a;
import com.boohee.myview.IntFloatWheelView;
import java.io.File;
import java.util.Calendar;
import java.util.Locale;

class c implements ax, n {
    public static int W = 10;
    public static int X = 7;
    public static String Y = "http://loc.map.baidu.com/sdk_ep.php";
    public static float Z = 10.0f;
    public static float a0 = 200.0f;
    private static String a1 = "http://loc.map.baidu.com/user_err.php";
    public static float a2 = 10.0f;
    public static boolean a3 = false;
    public static int a4 = 120;
    public static int a5 = 100;
    public static int a6 = 100;
    public static boolean a7 = true;
    public static int a8 = 30;
    public static double a9 = 0.0d;
    private static boolean aA = false;
    public static byte[] aB = null;
    private static boolean aC = false;
    public static long aD = DeviceInfoConstant.REQUEST_LOCATE_INTERVAL;
    private static String aE = "http://loc.map.baidu.com/tcu.php";
    public static int aF = 70;
    public static final boolean aG = true;
    public static double aH = 0.0d;
    public static int aI = 3;
    public static int aJ = 2;
    public static long aK = 420000;
    private static String aL = "[baidu_location_service]";
    public static int aM = 3;
    private static String aN = "http://loc.map.baidu.com/sdk.php";
    public static int aO = 30000;
    private static Process aP = null;
    public static long aQ = 900000;
    public static float aR = 2.2f;
    public static long aS = 15;
    public static float aT = 3.8f;
    public static float aU = 0.5f;
    public static int aV = 0;
    public static int aW = 30000;
    public static int aX = 16;
    private static String aY = "http://loc.map.baidu.com/oqur.php";
    public static boolean aZ = true;
    public static int aa = 20;
    public static float ab = 0.9f;
    public static double ac = 0.0d;
    public static long ad = 180000;
    public static float ae = 1.1f;
    public static float af = 6.0f;
    public static float ag = 2.3f;
    public static boolean ah = false;
    public static int ai = 0;
    public static int aj = 420000;
    public static int ak = 0;
    public static int al = 0;
    public static boolean am = false;
    public static double an = 0.0d;
    public static long ao = 180000;
    public static float ap = IntFloatWheelView.DEFAULT_VALUE;
    public static int aq = 60;
    public static boolean ar = false;
    public static int as = 300;
    public static int at = 10000;
    public static int au = 6;
    public static int av = 20;
    public static String aw = "no";
    public static int ax = 70;
    public static int ay = 1000;
    public static boolean az = true;
    public static float ba = 0.1f;
    public static float bb = 10.0f;
    public static String bc = BDGeofence.COORD_TYPE_GCJ;
    public static float bd = 0.0f;
    public static float be = 2.0f;

    c() {
    }

    public static String byte() {
        if (!Environment.getExternalStorageState().equals("mounted")) {
            return null;
        }
        try {
            String path = Environment.getExternalStorageDirectory().getPath();
            File file = new File(path + "/baidu/tempdata");
            if (file.exists()) {
                return path;
            }
            file.mkdirs();
            return path;
        } catch (Exception e) {
            return null;
        }
    }

    public static void case() {
    }

    public static void char() {
        try {
            if (aP != null) {
                aP.destroy();
                aP = null;
            }
        } catch (Exception e) {
        }
    }

    static int do(String str, String str2, String str3) {
        int i = Integer.MIN_VALUE;
        if (!(str == null || str.equals(""))) {
            int indexOf = str.indexOf(str2);
            if (indexOf != -1) {
                indexOf += str2.length();
                int indexOf2 = str.indexOf(str3, indexOf);
                if (indexOf2 != -1) {
                    String substring = str.substring(indexOf, indexOf2);
                    if (!(substring == null || substring.equals(""))) {
                        try {
                            i = Integer.parseInt(substring);
                        } catch (NumberFormatException e) {
                        }
                    }
                }
            }
        }
        return i;
    }

    public static String do() {
        return aY;
    }

    public static void do(String str) {
        if (str != null) {
            aN = str;
        }
    }

    public static void do(String str, String str2) {
    }

    public static String else() {
        String str = byte();
        return str == null ? null : str + "/baidu/tempdata";
    }

    static double for(String str, String str2, String str3) {
        double d = Double.MIN_VALUE;
        if (!(str == null || str.equals(""))) {
            int indexOf = str.indexOf(str2);
            if (indexOf != -1) {
                indexOf += str2.length();
                int indexOf2 = str.indexOf(str3, indexOf);
                if (indexOf2 != -1) {
                    String substring = str.substring(indexOf, indexOf2);
                    if (!(substring == null || substring.equals(""))) {
                        try {
                            d = Double.parseDouble(substring);
                        } catch (NumberFormatException e) {
                        }
                    }
                }
            }
        }
        return d;
    }

    public static String for() {
        return aN;
    }

    public static String goto() {
        try {
            File file = new File(f.getServiceContext().getFilesDir() + File.separator + "lldt");
            if (!file.exists()) {
                file.mkdirs();
            }
            return file.getAbsolutePath();
        } catch (Exception e) {
            return null;
        }
    }

    static float if(String str, String str2, String str3) {
        float f = Float.MIN_VALUE;
        if (!(str == null || str.equals(""))) {
            int indexOf = str.indexOf(str2);
            if (indexOf != -1) {
                indexOf += str2.length();
                int indexOf2 = str.indexOf(str3, indexOf);
                if (indexOf2 != -1) {
                    String substring = str.substring(indexOf, indexOf2);
                    if (!(substring == null || substring.equals(""))) {
                        try {
                            f = Float.parseFloat(substring);
                        } catch (NumberFormatException e) {
                        }
                    }
                }
            }
        }
        return f;
    }

    static String if() {
        Calendar instance = Calendar.getInstance();
        int i = instance.get(5);
        int i2 = instance.get(1);
        int i3 = instance.get(2) + 1;
        int i4 = instance.get(11);
        int i5 = instance.get(12);
        int i6 = instance.get(13);
        return String.format(Locale.CHINA, "%d_%d_%d_%d_%d_%d", new Object[]{Integer.valueOf(i2), Integer.valueOf(i3), Integer.valueOf(i), Integer.valueOf(i4), Integer.valueOf(i5), Integer.valueOf(i6)});
    }

    public static String if(a aVar, b bVar, Location location, String str) {
        String aVar2;
        StringBuffer stringBuffer = new StringBuffer();
        if (aVar != null) {
            aVar2 = aVar.toString();
            if (aVar2 != null) {
                stringBuffer.append(aVar2);
            }
        }
        if (bVar != null) {
            try {
                aVar2 = bVar.a(5);
            } catch (Exception e) {
                aVar2 = null;
            }
            if (aVar2 != null) {
                stringBuffer.append(aVar2);
            }
        }
        if (location != null) {
            aVar2 = al != 0 ? x.new(location) : x.byte(location);
            if (aVar2 != null) {
                stringBuffer.append(aVar2);
            }
        }
        aVar2 = az.cn().char(false);
        if (aVar2 != null) {
            stringBuffer.append(aVar2);
        }
        if (str != null) {
            stringBuffer.append(str);
        }
        if (aVar != null) {
            aVar2 = aVar.int();
            if (aVar2 != null && aVar2.length() + stringBuffer.length() < 750) {
                stringBuffer.append(aVar2);
            }
        }
        return stringBuffer.toString();
    }

    public static String if(a aVar, b bVar, Location location, String str, int i) {
        String aVar2;
        StringBuffer stringBuffer = new StringBuffer();
        if (aVar != null) {
            aVar2 = aVar.toString();
            if (aVar2 != null) {
                stringBuffer.append(aVar2);
            }
        }
        if (bVar != null) {
            aVar2 = i == 0 ? bVar.char() : bVar.byte();
            if (aVar2 != null) {
                stringBuffer.append(aVar2);
            }
        }
        if (location != null) {
            aVar2 = (al == 0 || i == 0) ? x.byte(location) : x.new(location);
            if (aVar2 != null) {
                stringBuffer.append(aVar2);
            }
        }
        boolean z = false;
        if (i == 0) {
            z = true;
        }
        aVar2 = az.cn().char(z);
        if (aVar2 != null) {
            stringBuffer.append(aVar2);
        }
        if (str != null) {
            stringBuffer.append(str);
        }
        Object a = aw.do().a();
        if (!TextUtils.isEmpty(a)) {
            stringBuffer.append("&bc=").append(a);
        }
        if (aVar != null) {
            aVar2 = aVar.int();
            if (aVar2 != null && aVar2.length() + stringBuffer.length() < 750) {
                stringBuffer.append(aVar2);
            }
        }
        aVar2 = stringBuffer.toString();
        if (location == null || bVar == null) {
            aM = 3;
        } else {
            try {
                float speed = location.getSpeed();
                int i2 = al;
                int i3 = bVar.do();
                int i4 = bVar.try();
                boolean z2 = bVar.case();
                if (speed < af && ((i2 == 1 || i2 == 0) && (i3 < aq || z2))) {
                    aM = 1;
                } else if (speed >= Z || (!(i2 == 1 || i2 == 0 || i2 == 3) || (i3 >= aF && i4 <= au))) {
                    aM = 3;
                } else {
                    aM = 2;
                }
            } catch (Exception e) {
                aM = 3;
            }
        }
        return aVar2;
    }

    static String if(String str, String str2, String str3, double d) {
        if (str == null || str.equals("")) {
            return null;
        }
        int indexOf = str.indexOf(str2);
        if (indexOf == -1) {
            return null;
        }
        indexOf += str2.length();
        int indexOf2 = str.indexOf(str3, indexOf);
        if (indexOf2 == -1) {
            return null;
        }
        String substring = str.substring(0, indexOf);
        return substring + String.format(Locale.CHINA, "%.7f", new Object[]{Double.valueOf(d)}) + str.substring(indexOf2);
    }

    public static void if(String str) {
        if (aC) {
            Log.d(aL, str);
        }
    }

    public static void if(String str, String str2) {
        if (aA) {
            Log.d(str, str2);
        }
    }

    public static boolean if(BDLocation bDLocation) {
        int locType = bDLocation.getLocType();
        return locType > 100 && locType < 200;
    }

    public static String int() {
        return aE;
    }

    static String new() {
        Calendar instance = Calendar.getInstance();
        int i = instance.get(5);
        int i2 = instance.get(1);
        int i3 = instance.get(2) + 1;
        int i4 = instance.get(11);
        int i5 = instance.get(12);
        int i6 = instance.get(13);
        return String.format(Locale.CHINA, "%d-%d-%d %d:%d:%d", new Object[]{Integer.valueOf(i2), Integer.valueOf(i3), Integer.valueOf(i), Integer.valueOf(i4), Integer.valueOf(i5), Integer.valueOf(i6)});
    }

    public static String try() {
        return a1;
    }
}
