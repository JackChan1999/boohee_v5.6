package com.tencent.tinker.android.dex.util;

import java.util.Comparator;

public final class CompareUtils {
    private CompareUtils() {
    }

    public static int uCompare(byte ubyteA, byte ubyteB) {
        if (ubyteA == ubyteB) {
            return 0;
        }
        return (ubyteA & 255) < (ubyteB & 255) ? -1 : 1;
    }

    public static int uCompare(short ushortA, short ushortB) {
        if (ushortA == ushortB) {
            return 0;
        }
        return (ushortA & 65535) < (ushortB & 65535) ? -1 : 1;
    }

    public static int uCompare(int uintA, int uintB) {
        if (uintA == uintB) {
            return 0;
        }
        return (((long) uintA) & 4294967295L) < (((long) uintB) & 4294967295L) ? -1 : 1;
    }

    public static int uArrCompare(byte[] ubyteArrA, byte[] ubyteArrB) {
        int lenA = ubyteArrA.length;
        int lenB = ubyteArrB.length;
        if (lenA < lenB) {
            return -1;
        }
        if (lenA > lenB) {
            return 1;
        }
        for (int i = 0; i < lenA; i++) {
            int res = uCompare(ubyteArrA[i], ubyteArrB[i]);
            if (res != 0) {
                return res;
            }
        }
        return 0;
    }

    public static int uArrCompare(short[] ushortArrA, short[] ushortArrB) {
        int lenA = ushortArrA.length;
        int lenB = ushortArrB.length;
        if (lenA < lenB) {
            return -1;
        }
        if (lenA > lenB) {
            return 1;
        }
        for (int i = 0; i < lenA; i++) {
            int res = uCompare(ushortArrA[i], ushortArrB[i]);
            if (res != 0) {
                return res;
            }
        }
        return 0;
    }

    public static int uArrCompare(int[] uintArrA, int[] uintArrB) {
        int lenA = uintArrA.length;
        int lenB = uintArrB.length;
        if (lenA < lenB) {
            return -1;
        }
        if (lenA > lenB) {
            return 1;
        }
        for (int i = 0; i < lenA; i++) {
            int res = uCompare(uintArrA[i], uintArrB[i]);
            if (res != 0) {
                return res;
            }
        }
        return 0;
    }

    public static int sCompare(byte sbyteA, byte sbyteB) {
        if (sbyteA == sbyteB) {
            return 0;
        }
        return sbyteA < sbyteB ? -1 : 1;
    }

    public static int sCompare(short sshortA, short sshortB) {
        if (sshortA == sshortB) {
            return 0;
        }
        return sshortA < sshortB ? -1 : 1;
    }

    public static int sCompare(int sintA, int sintB) {
        if (sintA == sintB) {
            return 0;
        }
        return sintA < sintB ? -1 : 1;
    }

    public static int sCompare(long slongA, long slongB) {
        if (slongA == slongB) {
            return 0;
        }
        return slongA < slongB ? -1 : 1;
    }

    public static int sArrCompare(byte[] sbyteArrA, byte[] sbyteArrB) {
        int lenA = sbyteArrA.length;
        int lenB = sbyteArrB.length;
        if (lenA < lenB) {
            return -1;
        }
        if (lenA > lenB) {
            return 1;
        }
        for (int i = 0; i < lenA; i++) {
            int res = sCompare(sbyteArrA[i], sbyteArrB[i]);
            if (res != 0) {
                return res;
            }
        }
        return 0;
    }

    public static int sArrCompare(short[] sshortArrA, short[] sshortArrB) {
        int lenA = sshortArrA.length;
        int lenB = sshortArrB.length;
        if (lenA < lenB) {
            return -1;
        }
        if (lenA > lenB) {
            return 1;
        }
        for (int i = 0; i < lenA; i++) {
            int res = sCompare(sshortArrA[i], sshortArrB[i]);
            if (res != 0) {
                return res;
            }
        }
        return 0;
    }

    public static int sArrCompare(int[] sintArrA, int[] sintArrB) {
        int lenA = sintArrA.length;
        int lenB = sintArrB.length;
        if (lenA < lenB) {
            return -1;
        }
        if (lenA > lenB) {
            return 1;
        }
        for (int i = 0; i < lenA; i++) {
            int res = sCompare(sintArrA[i], sintArrB[i]);
            if (res != 0) {
                return res;
            }
        }
        return 0;
    }

    public static int sArrCompare(long[] slongArrA, long[] slongArrB) {
        int lenA = slongArrA.length;
        int lenB = slongArrB.length;
        if (lenA < lenB) {
            return -1;
        }
        if (lenA > lenB) {
            return 1;
        }
        for (int i = 0; i < lenA; i++) {
            int res = sCompare(slongArrA[i], slongArrB[i]);
            if (res != 0) {
                return res;
            }
        }
        return 0;
    }

    public static <T extends Comparable<T>> int aArrCompare(T[] aArrA, T[] aArrB) {
        int lenA = aArrA.length;
        int lenB = aArrB.length;
        if (lenA < lenB) {
            return -1;
        }
        if (lenA > lenB) {
            return 1;
        }
        for (int i = 0; i < lenA; i++) {
            int res = aArrA[i].compareTo(aArrB[i]);
            if (res != 0) {
                return res;
            }
        }
        return 0;
    }

    public static <T> int aArrCompare(T[] aArrA, T[] aArrB, Comparator<T> cmptor) {
        int lenA = aArrA.length;
        int lenB = aArrB.length;
        if (lenA < lenB) {
            return -1;
        }
        if (lenA > lenB) {
            return 1;
        }
        for (int i = 0; i < lenA; i++) {
            int res = cmptor.compare(aArrA[i], aArrB[i]);
            if (res != 0) {
                return res;
            }
        }
        return 0;
    }
}
