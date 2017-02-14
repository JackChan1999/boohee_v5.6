package org.eclipse.mat.collect;

import com.tencent.tinker.android.dx.instruction.Opcodes;

public class ArrayUtils {
    public static void sort(int[] keys, int[] values) {
        hybridsort(keys, values, 0, keys.length - 1);
    }

    public static void sortDesc(long[] keys, int[] values) {
        hybridsortDesc(keys, values, null, null, 0, keys.length - 1);
    }

    public static void sortDesc(long[] a, int[] b, long[] tmpa, int[] tmpb) {
        hybridsortDesc(a, b, tmpa, tmpb, 0, a.length - 1);
    }

    public static void sort(int[] keys, int[] values, int offset, int length) {
        hybridsort(keys, values, offset, (offset + length) - 1);
    }

    private static void swap(int[] keys, int[] values, int a, int b) {
        int tmp = keys[a];
        keys[a] = keys[b];
        keys[b] = tmp;
        tmp = values[a];
        values[a] = values[b];
        values[b] = tmp;
    }

    private static void swap(long[] keys, int[] values, int a, int b) {
        long tmpKey = keys[a];
        keys[a] = keys[b];
        keys[b] = tmpKey;
        int tmpValue = values[a];
        values[a] = values[b];
        values[b] = tmpValue;
    }

    private static int median(int[] x, int pos1, int pos2, int pos3) {
        int v1 = x[pos1];
        int v2 = x[pos2];
        int v3 = x[pos3];
        if (v1 < v2) {
            if (v2 <= v3) {
                return pos2;
            }
            if (v1 >= v3) {
                return pos1;
            }
            return pos3;
        } else if (v1 <= v3) {
            return pos1;
        } else {
            return v2 >= v3 ? pos2 : pos3;
        }
    }

    private static int median(long[] x, int pos1, int pos2, int pos3) {
        long v1 = x[pos1];
        long v2 = x[pos2];
        long v3 = x[pos3];
        if (v1 < v2) {
            if (v2 <= v3) {
                return pos2;
            }
            if (v1 >= v3) {
                return pos1;
            }
            return pos3;
        } else if (v1 <= v3) {
            return pos1;
        } else {
            return v2 >= v3 ? pos2 : pos3;
        }
    }

    private static int[] split(int[] keys, int[] values, int left, int right) {
        int splittingIdx = median(keys, left, right, ((right - left) >> 1) + left);
        int splittingValue = keys[splittingIdx];
        swap(keys, values, left, splittingIdx);
        int i = left;
        int c = 0;
        for (int j = left + 1; j <= right; j++) {
            if (keys[j] < splittingValue) {
                i++;
                swap(keys, values, i, j);
                if (c > 0) {
                    swap(keys, values, i + c, j);
                }
            } else if (keys[j] == splittingValue) {
                c++;
                swap(keys, values, i + c, j);
            }
        }
        swap(keys, values, left, i);
        return new int[]{i, i + c};
    }

    private static int[] splitDesc(long[] keys, int[] values, int left, int right) {
        int splittingIdx = median(keys, left, right, ((right - left) >> 1) + left);
        long splittingValue = keys[splittingIdx];
        swap(keys, values, left, splittingIdx);
        int i = left;
        int c = 0;
        for (int j = left + 1; j <= right; j++) {
            if (keys[j] > splittingValue) {
                i++;
                swap(keys, values, i, j);
                if (c > 0) {
                    swap(keys, values, i + c, j);
                }
            } else if (keys[j] == splittingValue) {
                c++;
                swap(keys, values, i + c, j);
            }
        }
        swap(keys, values, left, i);
        return new int[]{i, i + c};
    }

    private static void hybridsort(int[] keys, int[] values, int left, int right) {
        while (right - left >= 1) {
            if (right - left < 5000000) {
                radixsort(keys, values, left, (right - left) + 1);
                return;
            }
            int[] i = split(keys, values, left, right);
            if (i[0] - left <= right - i[1]) {
                hybridsort(keys, values, left, i[0] - 1);
                left = i[1] + 1;
            } else {
                hybridsort(keys, values, i[1] + 1, right);
                right = i[0] - 1;
            }
        }
    }

    private static void hybridsortDesc(long[] keys, int[] values, long[] tmpKeys, int[] tmpValues, int left, int right) {
        while (right - left >= 1) {
            if (right - left >= 5000000) {
                int[] i = splitDesc(keys, values, left, right);
                if (i[0] - left <= right - i[1]) {
                    hybridsortDesc(keys, values, tmpKeys, tmpValues, left, i[0] - 1);
                    left = i[1] + 1;
                } else {
                    hybridsortDesc(keys, values, tmpKeys, tmpValues, i[1] + 1, right);
                    right = i[0] - 1;
                }
            } else if (right - left < 12) {
                for (int i2 = left; i2 <= right; i2++) {
                    int j = i2;
                    while (j > left && keys[j - 1] < keys[j]) {
                        swap(keys, values, j, j - 1);
                        j--;
                    }
                }
                return;
            } else {
                radixsortDesc(keys, values, tmpKeys, tmpValues, left, (right - left) + 1);
                return;
            }
        }
    }

    private static void radixsort(int[] keys, int[] values, int offset, int length) {
        int[] tempKeys = new int[length];
        int[] tempValues = new int[length];
        countsort(keys, tempKeys, values, tempValues, offset, 0, length, 0);
        countsort(tempKeys, keys, tempValues, values, 0, offset, length, 1);
        countsort(keys, tempKeys, values, tempValues, offset, 0, length, 2);
        countsort(tempKeys, keys, tempValues, values, 0, offset, length, 3);
    }

    private static void radixsortDesc(long[] keys, int[] values, long[] tempKeys, int[] tempValues, int offset, int length) {
        if (tempKeys == null) {
            tempKeys = new long[length];
        }
        if (tempValues == null) {
            tempValues = new int[length];
        }
        countsortDesc(keys, tempKeys, values, tempValues, offset, 0, length, 0);
        countsortDesc(tempKeys, keys, tempValues, values, 0, offset, length, 1);
        countsortDesc(keys, tempKeys, values, tempValues, offset, 0, length, 2);
        countsortDesc(tempKeys, keys, tempValues, values, 0, offset, length, 3);
        countsortDesc(keys, tempKeys, values, tempValues, offset, 0, length, 4);
        countsortDesc(tempKeys, keys, tempValues, values, 0, offset, length, 5);
        countsortDesc(keys, tempKeys, values, tempValues, offset, 0, length, 6);
        countsortDesc(tempKeys, keys, tempValues, values, 0, offset, length, 7);
    }

    private static void countsort(int[] srcKeys, int[] destKeys, int[] srcValues, int[] destValues, int srcOffset, int trgOffset, int length, int sortByte) {
        int i;
        int[] count = new int[256];
        int[] index = new int[256];
        int shiftBits = sortByte * 8;
        int srcEnd = srcOffset + length;
        for (i = srcOffset; i < srcEnd; i++) {
            int i2 = (srcKeys[i] >> shiftBits) & 255;
            count[i2] = count[i2] + 1;
        }
        if (sortByte == 3) {
            for (i = Opcodes.INT_TO_LONG; i < 256; i++) {
                index[i] = index[i - 1] + count[i - 1];
            }
            index[0] = index[255] + count[255];
            for (i = 1; i < 128; i++) {
                index[i] = index[i - 1] + count[i - 1];
            }
        } else {
            for (i = 1; i < 256; i++) {
                index[i] = index[i - 1] + count[i - 1];
            }
        }
        for (i = srcOffset; i < srcEnd; i++) {
            int idx = (srcKeys[i] >> shiftBits) & 255;
            destValues[index[idx] + trgOffset] = srcValues[i];
            i2 = index[idx];
            index[idx] = i2 + 1;
            destKeys[i2 + trgOffset] = srcKeys[i];
        }
    }

    private static void countsortDesc(long[] srcKeys, long[] destKeys, int[] srcValues, int[] destValues, int srcOffset, int trgOffset, int length, int sortByte) {
        int i;
        int[] count = new int[256];
        int[] index = new int[256];
        int shiftBits = sortByte * 8;
        int srcEnd = srcOffset + length;
        for (i = srcOffset; i < srcEnd; i++) {
            int i2 = (int) ((srcKeys[i] >> shiftBits) & 255);
            count[i2] = count[i2] + 1;
        }
        if (sortByte == 7) {
            for (i = 126; i >= 0; i--) {
                index[i] = index[i + 1] + count[i + 1];
            }
            index[255] = index[0] + count[0];
            for (i = 254; i >= 128; i--) {
                index[i] = index[i + 1] + count[i + 1];
            }
        } else {
            for (i = 254; i >= 0; i--) {
                index[i] = index[i + 1] + count[i + 1];
            }
        }
        for (i = srcOffset; i < srcEnd; i++) {
            int idx = (int) ((srcKeys[i] >> shiftBits) & 255);
            destValues[index[idx] + trgOffset] = srcValues[i];
            i2 = index[idx];
            index[idx] = i2 + 1;
            destKeys[i2 + trgOffset] = srcKeys[i];
        }
    }
}
