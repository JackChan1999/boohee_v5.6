package com.tencent.tinker.bsdiff;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.util.zip.GZIPInputStream;

public class BSPatch {
    public static final int RETURN_DIFF_FILE_ERR = 2;
    public static final int RETURN_NEW_FILE_ERR  = 4;
    public static final int RETURN_OLD_FILE_ERR  = 3;
    public static final int RETURN_SUCCESS       = 1;

    public static int patchLessMemory(RandomAccessFile oldFile, File newFile, File diffFile, int
            extLen) throws IOException {
        if (oldFile == null || oldFile.length() <= 0) {
            return 3;
        }
        if (newFile == null) {
            return 4;
        }
        if (diffFile == null || diffFile.length() <= 0) {
            return 2;
        }
        byte[] diffBytes = new byte[((int) diffFile.length())];
        InputStream diffInputStream = new FileInputStream(diffFile);
        try {
            BSUtil.readFromStream(diffInputStream, diffBytes, 0, diffBytes.length);
            return patchLessMemory(oldFile, (int) oldFile.length(), diffBytes, diffBytes.length,
                    newFile, extLen);
        } finally {
            diffInputStream.close();
        }
    }

    public static int patchLessMemory(RandomAccessFile oldFile, int oldsize, byte[] diffBuf, int
            diffSize, File newFile, int extLen) throws IOException {
        if (oldFile == null || oldsize <= 0) {
            return 3;
        }
        if (newFile == null) {
            return 4;
        }
        if (diffBuf == null || diffSize <= 0) {
            return 2;
        }
        int commentLenPos = (oldsize - extLen) - 2;
        if (commentLenPos <= 2) {
            return 3;
        }
        DataInputStream diffIn = new DataInputStream(new ByteArrayInputStream(diffBuf, 0,
                diffSize));
        diffIn.skip(8);
        long ctrlBlockLen = diffIn.readLong();
        long diffBlockLen = diffIn.readLong();
        int newsize = (int) diffIn.readLong();
        diffIn.close();
        InputStream byteArrayInputStream = new ByteArrayInputStream(diffBuf, 0, diffSize);
        byteArrayInputStream.skip(32);
        DataInputStream ctrlBlockIn = new DataInputStream(new GZIPInputStream
                (byteArrayInputStream));
        byteArrayInputStream = new ByteArrayInputStream(diffBuf, 0, diffSize);
        byteArrayInputStream.skip(32 + ctrlBlockLen);
        InputStream diffBlockIn = new GZIPInputStream(byteArrayInputStream);
        byteArrayInputStream = new ByteArrayInputStream(diffBuf, 0, diffSize);
        byteArrayInputStream.skip((diffBlockLen + ctrlBlockLen) + 32);
        InputStream extraBlockIn = new GZIPInputStream(byteArrayInputStream);
        OutputStream fileOutputStream = new FileOutputStream(newFile);
        int oldpos = 0;
        int newpos = 0;
        int[] ctrl = new int[3];
        while (newpos < newsize) {
            int i;
            for (i = 0; i <= 2; i++) {
                ctrl[i] = ctrlBlockIn.readInt();
            }
            if (ctrl[0] + newpos > newsize) {
                fileOutputStream.close();
                oldFile.close();
                fileOutputStream.close();
                return 2;
            }
            try {
                byte[] buffer = new byte[ctrl[0]];
                if (BSUtil.readFromStream(diffBlockIn, buffer, 0, ctrl[0])) {
                    byte[] oldBuffer = new byte[ctrl[0]];
                    if (oldFile.read(oldBuffer, 0, ctrl[0]) < ctrl[0]) {
                        fileOutputStream.close();
                        oldFile.close();
                        fileOutputStream.close();
                        return 2;
                    }
                    i = 0;
                    while (i < ctrl[0]) {
                        if (oldpos + i == commentLenPos) {
                            oldBuffer[i] = (byte) 0;
                            oldBuffer[i + 1] = (byte) 0;
                        }
                        if (oldpos + i >= 0 && oldpos + i < oldsize) {
                            buffer[i] = (byte) (buffer[i] + oldBuffer[i]);
                        }
                        i++;
                    }
                    fileOutputStream.write(buffer);
                    newpos += ctrl[0];
                    oldpos += ctrl[0];
                    if (ctrl[1] + newpos > newsize) {
                        fileOutputStream.close();
                        oldFile.close();
                        fileOutputStream.close();
                        return 2;
                    }
                    buffer = new byte[ctrl[1]];
                    if (BSUtil.readFromStream(extraBlockIn, buffer, 0, ctrl[1])) {
                        fileOutputStream.write(buffer);
                        fileOutputStream.flush();
                        newpos += ctrl[1];
                        oldpos += ctrl[2];
                        oldFile.seek((long) oldpos);
                    } else {
                        fileOutputStream.close();
                        return 2;
                    }
                }
                fileOutputStream.close();
                oldFile.close();
                fileOutputStream.close();
                return 2;
            } finally {
                oldFile.close();
                fileOutputStream.close();
            }
        }
        ctrlBlockIn.close();
        diffBlockIn.close();
        extraBlockIn.close();
        oldFile.close();
        fileOutputStream.close();
        return 1;
    }

    public static int patchFast(File oldFile, File newFile, File diffFile, int extLen) throws
            IOException {
        if (oldFile == null || oldFile.length() <= 0) {
            return 3;
        }
        if (newFile == null) {
            return 4;
        }
        if (diffFile == null || diffFile.length() <= 0) {
            return 2;
        }
        InputStream oldInputStream = new BufferedInputStream(new FileInputStream(oldFile));
        byte[] diffBytes = new byte[((int) diffFile.length())];
        InputStream diffInputStream = new FileInputStream(diffFile);
        try {
            BSUtil.readFromStream(diffInputStream, diffBytes, 0, diffBytes.length);
            byte[] newBytes = patchFast(oldInputStream, (int) oldFile.length(), diffBytes, extLen);
            OutputStream newOutputStream = new FileOutputStream(newFile);
            try {
                newOutputStream.write(newBytes);
                return 1;
            } finally {
                newOutputStream.close();
            }
        } finally {
            diffInputStream.close();
        }
    }

    public static int patchFast(InputStream oldInputStream, InputStream diffInputStream, File
            newFile) throws IOException {
        if (oldInputStream == null) {
            return 3;
        }
        if (newFile == null) {
            return 4;
        }
        if (diffInputStream == null) {
            return 2;
        }
        byte[] oldBytes = BSUtil.inputStreamToByte(oldInputStream);
        byte[] diffBytes = BSUtil.inputStreamToByte(diffInputStream);
        byte[] newBytes = patchFast(oldBytes, oldBytes.length, diffBytes, diffBytes.length, 0);
        OutputStream newOutputStream = new FileOutputStream(newFile);
        try {
            newOutputStream.write(newBytes);
            return 1;
        } finally {
            newOutputStream.close();
        }
    }

    public static byte[] patchFast(InputStream oldInputStream, InputStream diffInputStream)
            throws IOException {
        if (oldInputStream == null || diffInputStream == null) {
            return null;
        }
        byte[] oldBytes = BSUtil.inputStreamToByte(oldInputStream);
        byte[] diffBytes = BSUtil.inputStreamToByte(diffInputStream);
        return patchFast(oldBytes, oldBytes.length, diffBytes, diffBytes.length, 0);
    }

    public static byte[] patchFast(InputStream oldInputStream, int oldsize, byte[] diffBytes, int
            extLen) throws IOException {
        byte[] oldBuf = new byte[oldsize];
        BSUtil.readFromStream(oldInputStream, oldBuf, 0, oldsize);
        oldInputStream.close();
        return patchFast(oldBuf, oldsize, diffBytes, diffBytes.length, extLen);
    }

    public static byte[] patchFast(byte[] oldBuf, int oldsize, byte[] diffBuf, int diffSize, int
            extLen) throws IOException {
        DataInputStream diffIn = new DataInputStream(new ByteArrayInputStream(diffBuf, 0,
                diffSize));
        diffIn.skip(8);
        long ctrlBlockLen = diffIn.readLong();
        long diffBlockLen = diffIn.readLong();
        int newsize = (int) diffIn.readLong();
        diffIn.close();
        InputStream in = new ByteArrayInputStream(diffBuf, 0, diffSize);
        in.skip(32);
        DataInputStream ctrlBlockIn = new DataInputStream(new GZIPInputStream(in));
        in = new ByteArrayInputStream(diffBuf, 0, diffSize);
        in.skip(32 + ctrlBlockLen);
        InputStream diffBlockIn = new GZIPInputStream(in);
        in = new ByteArrayInputStream(diffBuf, 0, diffSize);
        in.skip((diffBlockLen + ctrlBlockLen) + 32);
        InputStream extraBlockIn = new GZIPInputStream(in);
        byte[] newBuf = new byte[newsize];
        int oldpos = 0;
        int newpos = 0;
        int[] ctrl = new int[3];
        while (newpos < newsize) {
            int i;
            for (i = 0; i <= 2; i++) {
                ctrl[i] = ctrlBlockIn.readInt();
            }
            if (ctrl[0] + newpos > newsize) {
                throw new IOException("Corrupt by wrong patch file.");
            } else if (BSUtil.readFromStream(diffBlockIn, newBuf, newpos, ctrl[0])) {
                i = 0;
                while (i < ctrl[0]) {
                    if (oldpos + i >= 0 && oldpos + i < oldsize) {
                        int i2 = newpos + i;
                        newBuf[i2] = (byte) (newBuf[i2] + oldBuf[oldpos + i]);
                    }
                    i++;
                }
                newpos += ctrl[0];
                oldpos += ctrl[0];
                if (ctrl[1] + newpos > newsize) {
                    throw new IOException("Corrupt by wrong patch file.");
                } else if (BSUtil.readFromStream(extraBlockIn, newBuf, newpos, ctrl[1])) {
                    newpos += ctrl[1];
                    oldpos += ctrl[2];
                } else {
                    throw new IOException("Corrupt by wrong patch file.");
                }
            } else {
                throw new IOException("Corrupt by wrong patch file.");
            }
        }
        ctrlBlockIn.close();
        diffBlockIn.close();
        extraBlockIn.close();
        return newBuf;
    }
}
