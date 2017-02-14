package com.tencent.tinker.loader.shareutil;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.os.Process;
import android.support.v4.os.EnvironmentCompat;
import android.util.Log;

import com.boohee.model.ModelName;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class ShareTinkerInternals {
    private static final String  TAG         = "Tinker.TinkerInternals";
    private static final boolean VM_IS_ART   = isVmArt(System.getProperty("java.vm.version"));
    private static       String  processName = null;
    private static       String  tinkerID    = null;

    public static boolean isVmArt() {
        return VM_IS_ART;
    }

    public static boolean isNullOrNil(String object) {
        if (object == null || object.length() <= 0) {
            return true;
        }
        return false;
    }

    public static int checkSignatureAndTinkerID(Context context, File patchFile,
                                                ShareSecurityCheck securityCheck) {
        if (!securityCheck.verifyPatchMetaSignature(patchFile)) {
            return -1;
        }
        String oldTinkerId = getManifestTinkerID(context);
        if (oldTinkerId == null) {
            return -5;
        }
        HashMap<String, String> properties = securityCheck.getPackagePropertiesIfPresent();
        if (properties == null) {
            return -2;
        }
        String patchTinkerId = (String) properties.get(ShareConstants.TINKER_ID);
        if (patchTinkerId == null) {
            return -6;
        }
        if (oldTinkerId.equals(patchTinkerId)) {
            return 0;
        }
        return -7;
    }

    public static int checkPackageAndTinkerFlag(ShareSecurityCheck securityCheck, int tinkerFlag) {
        return 0;
    }

    public static Properties fastGetPatchPackageMeta(File patchFile) {
        IOException e;
        Throwable th;
        if (patchFile == null || !patchFile.isFile() || patchFile.length() == 0) {
            Log.e(TAG, "patchFile is illegal");
            return null;
        }
        ZipFile zipFile = null;
        try {
            ZipFile zipFile2 = new ZipFile(patchFile);
            InputStream inputStream;
            try {
                ZipEntry packageEntry = zipFile2.getEntry(ShareConstants.PACKAGE_META_FILE);
                if (packageEntry == null) {
                    Log.e(TAG, "patch meta entry not found");
                    SharePatchFileUtil.closeZip(zipFile2);
                    return null;
                }
                inputStream = null;
                inputStream = zipFile2.getInputStream(packageEntry);
                Properties properties = new Properties();
                properties.load(inputStream);
                SharePatchFileUtil.closeQuietly(inputStream);
                SharePatchFileUtil.closeZip(zipFile2);
                return properties;
            } catch (IOException e2) {
                e = e2;
                zipFile = zipFile2;
                try {
                    Log.e(TAG, "fastGetPatchPackageMeta exception:" + e.getMessage());
                    SharePatchFileUtil.closeZip(zipFile);
                    return null;
                } catch (Throwable th2) {
                    th = th2;
                    SharePatchFileUtil.closeZip(zipFile);
                    throw th;
                }
            } catch (Throwable th3) {
                th = th3;
                zipFile = zipFile2;
                SharePatchFileUtil.closeZip(zipFile);
                throw th;
            }
        } catch (IOException e3) {
            e = e3;
            Log.e(TAG, "fastGetPatchPackageMeta exception:" + e.getMessage());
            SharePatchFileUtil.closeZip(zipFile);
            return null;
        }
    }

    public static String getManifestTinkerID(Context context) {
        if (tinkerID != null) {
            return tinkerID;
        }
        try {
            Object object = context.getPackageManager().getApplicationInfo(context.getPackageName
                    (), 128).metaData.get(ShareConstants.TINKER_ID);
            if (object != null) {
                tinkerID = String.valueOf(object);
            } else {
                tinkerID = null;
            }
            return tinkerID;
        } catch (Exception e) {
            Log.e(TAG, "getManifestTinkerID exception:" + e.getMessage());
            return null;
        }
    }

    public static boolean isTinkerEnabledForDex(int flag) {
        return (flag & 1) != 0;
    }

    public static boolean isTinkerEnabledForNativeLib(int flag) {
        return (flag & 2) != 0;
    }

    public static boolean isTinkerEnabledForResource(int flag) {
        return (flag & 4) != 0;
    }

    public static String getTypeString(int type) {
        switch (type) {
            case 1:
                return "patch_file";
            case 2:
                return "patch_info";
            case 3:
                return ShareConstants.DEX_PATH;
            case 4:
                return "dex_art";
            case 5:
                return "dex_opt";
            case 6:
                return ShareConstants.SO_PATH;
            case 7:
                return "resource";
            default:
                return EnvironmentCompat.MEDIA_UNKNOWN;
        }
    }

    public static void setTinkerDisableWithSharedPreferences(Context context) {
        context.getSharedPreferences(ShareConstants.TINKER_SHARE_PREFERENCE_CONFIG, 4).edit()
                .putBoolean(ShareConstants.TINKER_ENABLE_CONFIG, false).commit();
    }

    public static boolean isTinkerEnableWithSharedPreferences(Context context) {
        return context.getSharedPreferences(ShareConstants.TINKER_SHARE_PREFERENCE_CONFIG, 4)
                .getBoolean(ShareConstants.TINKER_ENABLE_CONFIG, true);
    }

    public static boolean isTinkerEnabled(int flag) {
        return flag != 0;
    }

    public static boolean isTinkerEnabledAll(int flag) {
        return flag == 7;
    }

    public static boolean isInMainProcess(Context context) {
        String pkgName = context.getPackageName();
        String processName = getProcessName(context);
        if (processName == null || processName.length() == 0) {
            processName = "";
        }
        return pkgName.equals(processName);
    }

    public static void killAllOtherProcess(Context context) {
        for (RunningAppProcessInfo ai : ((ActivityManager) context.getSystemService(ModelName
                .ACTIVITY)).getRunningAppProcesses()) {
            if (ai.uid == Process.myUid() && ai.pid != Process.myPid()) {
                Process.killProcess(ai.pid);
            }
        }
    }

    public static String getProcessName(Context context) {
        if (processName != null) {
            return processName;
        }
        processName = getProcessNameInternal(context);
        return processName;
    }

    private static String getProcessNameInternal(Context context) {
        Exception e;
        Throwable th;
        int myPid = Process.myPid();
        if (context == null || myPid <= 0) {
            return "";
        }
        RunningAppProcessInfo myProcess = null;
        try {
            for (RunningAppProcessInfo process : ((ActivityManager) context.getSystemService
                    (ModelName.ACTIVITY)).getRunningAppProcesses()) {
                if (process.pid == myPid) {
                    myProcess = process;
                    break;
                }
            }
        } catch (Exception e2) {
            Log.e(TAG, "getProcessNameInternal exception:" + e2.getMessage());
        }
        if (myProcess != null) {
            return myProcess.processName;
        }
        byte[] b = new byte[128];
        FileInputStream in = null;
        try {
            FileInputStream in2 = new FileInputStream("/proc/" + myPid + "/cmdline");
            try {
                int len = in2.read(b);
                if (len > 0) {
                    int i = 0;
                    while (i < len) {
                        if (b[i] > Byte.MIN_VALUE || b[i] <= (byte) 0) {
                            len = i;
                            break;
                        }
                        i++;
                    }
                    String str = new String(b, 0, len);
                    if (in2 == null) {
                        return str;
                    }
                    try {
                        in2.close();
                        return str;
                    } catch (Exception e3) {
                        return str;
                    }
                }
                if (in2 != null) {
                    try {
                        in2.close();
                    } catch (Exception e4) {
                        in = in2;
                    }
                }
                in = in2;
                return "";
            } catch (Exception e5) {
                e2 = e5;
                in = in2;
                try {
                    e2.printStackTrace();
                    if (in != null) {
                        try {
                            in.close();
                        } catch (Exception e6) {
                        }
                    }
                    return "";
                } catch (Throwable th2) {
                    th = th2;
                    if (in != null) {
                        try {
                            in.close();
                        } catch (Exception e7) {
                        }
                    }
                    throw th;
                }
            } catch (Throwable th3) {
                th = th3;
                in = in2;
                if (in != null) {
                    in.close();
                }
                throw th;
            }
        } catch (Exception e8) {
            e2 = e8;
            e2.printStackTrace();
            if (in != null) {
                in.close();
            }
            return "";
        }
    }

    private static boolean isVmArt(String versionString) {
        if (versionString == null) {
            return false;
        }
        Matcher matcher = Pattern.compile("(\\d+)\\.(\\d+)(\\.\\d+)?").matcher(versionString);
        if (!matcher.matches()) {
            return false;
        }
        try {
            int major = Integer.parseInt(matcher.group(1));
            return major > 2 || (major == 2 && Integer.parseInt(matcher.group(2)) >= 1);
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
