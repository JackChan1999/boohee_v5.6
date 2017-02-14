package com.tencent.tinker.bsdiff;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.GZIPOutputStream;

public class BSDiff {
    private static final byte[] MAGIC_BYTES = new byte[]{(byte) 77, (byte) 105, (byte) 99, (byte)
            114, (byte) 111, (byte) 77, (byte) 115, (byte) 103};

    private static class IntByRef {
        private int value;

        private IntByRef() {
        }
    }

    private static void split(int[] arrayI, int[] arrayV, int start, int len, int h) {
        int k;
        int j;
        int x;
        int i;
        if (len < 16) {
            for (k = start; k < start + len; k += j) {
                j = 1;
                x = arrayV[arrayI[k] + h];
                for (i = 1; k + i < start + len; i++) {
                    if (arrayV[arrayI[k + i] + h] < x) {
                        x = arrayV[arrayI[k + i] + h];
                        j = 0;
                    }
                    if (arrayV[arrayI[k + i] + h] == x) {
                        int tmp = arrayI[k + j];
                        arrayI[k + j] = arrayI[k + i];
                        arrayI[k + i] = tmp;
                        j++;
                    }
                }
                for (i = 0; i < j; i++) {
                    arrayV[arrayI[k + i]] = (k + j) - 1;
                }
                if (j == 1) {
                    arrayI[k] = -1;
                }
            }
            return;
        }
        x = arrayV[arrayI[(len / 2) + start] + h];
        int jj = 0;
        int kk = 0;
        for (i = start; i < start + len; i++) {
            if (arrayV[arrayI[i] + h] < x) {
                jj++;
            }
            if (arrayV[arrayI[i] + h] == x) {
                kk++;
            }
        }
        jj += start;
        kk += jj;
        i = start;
        j = 0;
        k = 0;
        while (i < jj) {
            if (arrayV[arrayI[i] + h] < x) {
                i++;
            } else if (arrayV[arrayI[i] + h] == x) {
                tmp = arrayI[i];
                arrayI[i] = arrayI[jj + j];
                arrayI[jj + j] = tmp;
                j++;
            } else {
                tmp = arrayI[i];
                arrayI[i] = arrayI[kk + k];
                arrayI[kk + k] = tmp;
                k++;
            }
        }
        while (jj + j < kk) {
            if (arrayV[arrayI[jj + j] + h] == x) {
                j++;
            } else {
                tmp = arrayI[jj + j];
                arrayI[jj + j] = arrayI[kk + k];
                arrayI[kk + k] = tmp;
                k++;
            }
        }
        if (jj > start) {
            split(arrayI, arrayV, start, jj - start, h);
        }
        for (i = 0; i < kk - jj; i++) {
            arrayV[arrayI[jj + i]] = kk - 1;
        }
        if (jj == kk - 1) {
            arrayI[jj] = -1;
        }
        if (start + len > kk) {
            split(arrayI, arrayV, kk, (start + len) - kk, h);
        }
    }

    private static void qsufsort(int[] arrayI, int[] arrayV, byte[] oldBuf, int oldsize) {
        int i;
        int[] buckets = new int[256];
        for (i = 0; i < oldsize; i++) {
            int i2 = oldBuf[i] & 255;
            buckets[i2] = buckets[i2] + 1;
        }
        for (i = 1; i < 256; i++) {
            buckets[i] = buckets[i] + buckets[i - 1];
        }
        for (i = 255; i > 0; i--) {
            buckets[i] = buckets[i - 1];
        }
        buckets[0] = 0;
        for (i = 0; i < oldsize; i++) {
            i2 = oldBuf[i] & 255;
            int i3 = buckets[i2] + 1;
            buckets[i2] = i3;
            arrayI[i3] = i;
        }
        arrayI[0] = oldsize;
        for (i = 0; i < oldsize; i++) {
            arrayV[i] = buckets[oldBuf[i] & 255];
        }
        arrayV[oldsize] = 0;
        for (i = 1; i < 256; i++) {
            if (buckets[i] == buckets[i - 1] + 1) {
                arrayI[buckets[i]] = -1;
            }
        }
        arrayI[0] = -1;
        int h = 1;
        while (arrayI[0] != (-(oldsize + 1))) {
            int len = 0;
            i = 0;
            while (i < oldsize + 1) {
                if (arrayI[i] < 0) {
                    len -= arrayI[i];
                    i -= arrayI[i];
                } else {
                    if (len != 0) {
                        arrayI[i - len] = -len;
                    }
                    len = (arrayV[arrayI[i]] + 1) - i;
                    split(arrayI, arrayV, i, len, h);
                    i += len;
                    len = 0;
                }
            }
            if (len != 0) {
                arrayI[i - len] = -len;
            }
            h += h;
        }
        for (i = 0; i < oldsize + 1; i++) {
            arrayI[arrayV[i]] = i;
        }
    }

    private static int search(int[] arrayI, byte[] oldBuf, int oldSize, byte[] newBuf, int
            newSize, int newBufOffset, int start, int end, IntByRef pos) {
        int x;
        if (end - start < 2) {
            x = matchlen(oldBuf, oldSize, arrayI[start], newBuf, newSize, newBufOffset);
            int y = matchlen(oldBuf, oldSize, arrayI[end], newBuf, newSize, newBufOffset);
            if (x > y) {
                pos.value = arrayI[start];
                return x;
            }
            pos.value = arrayI[end];
            return y;
        }
        x = start + ((end - start) / 2);
        if (memcmp(oldBuf, oldSize, arrayI[x], newBuf, newSize, newBufOffset) < 0) {
            return search(arrayI, oldBuf, oldSize, newBuf, newSize, newBufOffset, x, end, pos);
        }
        return search(arrayI, oldBuf, oldSize, newBuf, newSize, newBufOffset, start, x, pos);
    }

    private static int matchlen(byte[] oldBuf, int oldSize, int oldOffset, byte[] newBuf, int
            newSize, int newOffset) {
        int end = Math.min(oldSize - oldOffset, newSize - newOffset);
        for (int i = 0; i < end; i++) {
            if (oldBuf[oldOffset + i] != newBuf[newOffset + i]) {
                return i;
            }
        }
        return end;
    }

    private static int memcmp(byte[] s1, int s1Size, int s1offset, byte[] s2, int s2Size, int
            s2offset) {
        int n = s1Size - s1offset;
        if (n > s2Size - s2offset) {
            n = s2Size - s2offset;
        }
        int i = 0;
        while (i < n) {
            if (s1[i + s1offset] != s2[i + s2offset]) {
                return s1[i + s1offset] < s2[i + s2offset] ? -1 : 1;
            } else {
                i++;
            }
        }
        return 0;
    }

    public static void bsdiff(File oldFile, File newFile, File diffFile) throws IOException {
        InputStream oldInputStream = new BufferedInputStream(new FileInputStream(oldFile));
        InputStream newInputStream = new BufferedInputStream(new FileInputStream(newFile));
        OutputStream diffOutputStream = new FileOutputStream(diffFile);
        try {
            diffOutputStream.write(bsdiff(oldInputStream, (int) oldFile.length(), newInputStream,
                    (int) newFile.length()));
        } finally {
            diffOutputStream.close();
        }
    }

    public static byte[] bsdiff(InputStream oldInputStream, int oldsize, InputStream
            newInputStream, int newsize) throws IOException {
        byte[] oldBuf = new byte[oldsize];
        BSUtil.readFromStream(oldInputStream, oldBuf, 0, oldsize);
        oldInputStream.close();
        byte[] newBuf = new byte[newsize];
        BSUtil.readFromStream(newInputStream, newBuf, 0, newsize);
        newInputStream.close();
        return bsdiff(oldBuf, oldsize, newBuf, newsize);
    }

    public static byte[] bsdiff(byte[] oldBuf, int oldsize, byte[] newBuf, int newsize) throws
            IOException {
        int[] arrayI = new int[(oldsize + 1)];
        qsufsort(arrayI, new int[(oldsize + 1)], oldBuf, oldsize);
        int diffBLockLen = 0;
        byte[] diffBlock = new byte[newsize];
        int extraBlockLen = 0;
        byte[] extraBlock = new byte[newsize];
        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        OutputStream dataOutputStream = new DataOutputStream(byteOut);
        dataOutputStream.write(MAGIC_BYTES);
        dataOutputStream.writeLong(-1);
        dataOutputStream.writeLong(-1);
        dataOutputStream.writeLong((long) newsize);
        dataOutputStream.flush();
        GZIPOutputStream bzip2Out = new GZIPOutputStream(dataOutputStream);
        DataOutputStream dataOut = new DataOutputStream(bzip2Out);
        int scan = 0;
        int matchLen = 0;
        int lastscan = 0;
        int lastpos = 0;
        int lastoffset = 0;
        IntByRef pos = new IntByRef();
        while (scan < newsize) {
            int oldscore = 0;
            scan += matchLen;
            int scsc = scan;
            while (scan < newsize) {
                matchLen = search(arrayI, oldBuf, oldsize, newBuf, newsize, scan, 0, oldsize, pos);
                while (scsc < scan + matchLen) {
                    if (scsc + lastoffset < oldsize && oldBuf[scsc + lastoffset] == newBuf[scsc]) {
                        oldscore++;
                    }
                    scsc++;
                }
                if ((matchLen == oldscore && matchLen != 0) || matchLen > oldscore + 8) {
                    break;
                }
                if (scan + lastoffset < oldsize && oldBuf[scan + lastoffset] == newBuf[scan]) {
                    oldscore--;
                }
                scan++;
            }
            if (matchLen != oldscore || scan == newsize) {
                int equalNum = 0;
                int sf = 0;
                int lenFromOld = 0;
                int i = 0;
                while (lastscan + i < scan && lastpos + i < oldsize) {
                    if (oldBuf[lastpos + i] == newBuf[lastscan + i]) {
                        equalNum++;
                    }
                    i++;
                    if ((equalNum * 2) - i > (sf * 2) - lenFromOld) {
                        sf = equalNum;
                        lenFromOld = i;
                    }
                }
                int lenb = 0;
                if (scan < newsize) {
                    equalNum = 0;
                    int sb = 0;
                    i = 1;
                    while (scan >= lastscan + i && pos.value >= i) {
                        if (oldBuf[pos.value - i] == newBuf[scan - i]) {
                            equalNum++;
                        }
                        if ((equalNum * 2) - i > (sb * 2) - lenb) {
                            sb = equalNum;
                            lenb = i;
                        }
                        i++;
                    }
                }
                if (lastscan + lenFromOld > scan - lenb) {
                    int overlap = (lastscan + lenFromOld) - (scan - lenb);
                    equalNum = 0;
                    int ss = 0;
                    int lens = 0;
                    for (i = 0; i < overlap; i++) {
                        if (newBuf[((lastscan + lenFromOld) - overlap) + i] == oldBuf[((lastpos +
                                lenFromOld) - overlap) + i]) {
                            equalNum++;
                        }
                        if (newBuf[(scan - lenb) + i] == oldBuf[(pos.value - lenb) + i]) {
                            equalNum--;
                        }
                        if (equalNum > ss) {
                            ss = equalNum;
                            lens = i + 1;
                        }
                    }
                    lenFromOld += lens - overlap;
                    lenb -= lens;
                }
                for (i = 0; i < lenFromOld; i++) {
                    diffBlock[diffBLockLen + i] = (byte) (newBuf[lastscan + i] - oldBuf[lastpos +
                            i]);
                }
                for (i = 0; i < (scan - lenb) - (lastscan + lenFromOld); i++) {
                    extraBlock[extraBlockLen + i] = newBuf[(lastscan + lenFromOld) + i];
                }
                diffBLockLen += lenFromOld;
                extraBlockLen += (scan - lenb) - (lastscan + lenFromOld);
                dataOut.writeInt(lenFromOld);
                dataOut.writeInt((scan - lenb) - (lastscan + lenFromOld));
                dataOut.writeInt((pos.value - lenb) - (lastpos + lenFromOld));
                lastscan = scan - lenb;
                lastpos = pos.value - lenb;
                lastoffset = pos.value - scan;
            }
        }
        dataOut.flush();
        bzip2Out.finish();
        int ctrlBlockLen = dataOutputStream.size() - 32;
        bzip2Out = new GZIPOutputStream(dataOutputStream);
        bzip2Out.write(diffBlock, 0, diffBLockLen);
        bzip2Out.finish();
        bzip2Out.flush();
        int diffBlockLen = (dataOutputStream.size() - ctrlBlockLen) - 32;
        bzip2Out = new GZIPOutputStream(dataOutputStream);
        bzip2Out.write(extraBlock, 0, extraBlockLen);
        bzip2Out.finish();
        bzip2Out.flush();
        dataOutputStream.close();
        ByteArrayOutputStream byteHeaderOut = new ByteArrayOutputStream(32);
        DataOutputStream dataOutputStream2 = new DataOutputStream(byteHeaderOut);
        dataOutputStream2.write(MAGIC_BYTES);
        dataOutputStream2.writeLong((long) ctrlBlockLen);
        dataOutputStream2.writeLong((long) diffBlockLen);
        dataOutputStream2.writeLong((long) newsize);
        dataOutputStream2.close();
        Object diffBytes = byteOut.toByteArray();
        Object headerBytes = byteHeaderOut.toByteArray();
        System.arraycopy(headerBytes, 0, diffBytes, 0, headerBytes.length);
        return diffBytes;
    }
}
