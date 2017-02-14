package com.boohee.one.tinker;

import android.os.Environment;
import android.os.StatFs;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;

public class Utils {
    public static final int     ERROR_PATCH_ALREADY_APPLY           = -8;
    public static final int     ERROR_PATCH_CONDITION_NOT_SATISFIED = -10;
    public static final int     ERROR_PATCH_CRASH_LIMIT             = -9;
    public static final int     ERROR_PATCH_GOOGLEPLAY_CHANNEL      = -5;
    public static final int     ERROR_PATCH_MEMORY_LIMIT            = -7;
    public static final int     ERROR_PATCH_ROM_SPACE               = -6;
    public static final int     MIN_MEMORY_HEAP_SIZE                = 45;
    public static final String  PLATFORM                            = "platform";
    private static      boolean background                          = false;

    public static boolean isGooglePlay() {
        return false;
    }

    public static boolean isBackground() {
        return background;
    }

    public static void setBackground(boolean back) {
        background = back;
    }

    public static int checkForPatchRecover(long roomSize, int maxMemory) {
        if (isGooglePlay()) {
            return -5;
        }
        if (maxMemory < 45) {
            return -7;
        }
        if (checkRomSpaceEnough(roomSize)) {
            return 0;
        }
        return -6;
    }

    public static boolean isXposedExists(Throwable thr) {
        for (StackTraceElement stackTrace : thr.getStackTrace()) {
            String clazzName = stackTrace.getClassName();
            if (clazzName != null && clazzName.contains("de.robv.android.xposed.XposedBridge")) {
                return true;
            }
        }
        return false;
    }

    @Deprecated
    public static boolean checkRomSpaceEnough(long limitSize) {
        long allSize;
        long availableSize = 0;
        try {
            StatFs sf = new StatFs(Environment.getDataDirectory().getPath());
            availableSize = ((long) sf.getAvailableBlocks()) * ((long) sf.getBlockSize());
            allSize = ((long) sf.getBlockCount()) * ((long) sf.getBlockSize());
        } catch (Exception e) {
            allSize = 0;
        }
        if (allSize == 0 || availableSize <= limitSize) {
            return false;
        }
        return true;
    }

    public static String getExceptionCauseString(Throwable ex) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(bos);
        Throwable t = ex;
        while (t.getCause() != null) {
            try {
                t = t.getCause();
            } finally {
                try {
                    bos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        t.printStackTrace(ps);
        String toVisualString = toVisualString(bos.toString());
        return toVisualString;
    }

    private static String toVisualString(String src) {
        boolean cutFlg = false;
        if (src == null) {
            return null;
        }
        char[] chr = src.toCharArray();
        if (chr == null) {
            return null;
        }
        int i = 0;
        while (i < chr.length) {
            if (chr[i] > '') {
                chr[i] = '\u0000';
                cutFlg = true;
                break;
            }
            i++;
        }
        if (cutFlg) {
            return new String(chr, 0, i);
        }
        return src;
    }
}
