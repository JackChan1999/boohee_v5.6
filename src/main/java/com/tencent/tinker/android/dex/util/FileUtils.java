package com.tencent.tinker.android.dex.util;

import com.tencent.tinker.loader.shareutil.ShareConstants;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public final class FileUtils {
    private FileUtils() {
    }

    public static byte[] readFile(String fileName) throws IOException {
        return readFile(new File(fileName));
    }

    public static byte[] readFile(File file) throws IOException {
        Throwable th;
        if (!file.exists()) {
            throw new RuntimeException(file + ": file not found");
        } else if (!file.isFile()) {
            throw new RuntimeException(file + ": not a file");
        } else if (file.canRead()) {
            long longLength = file.length();
            int length = (int) longLength;
            if (((long) length) != longLength) {
                throw new RuntimeException(file + ": file too long");
            }
            ByteArrayOutputStream baos = new ByteArrayOutputStream(length);
            InputStream in = null;
            try {
                InputStream in2 = new BufferedInputStream(new FileInputStream(file));
                try {
                    byte[] buffer = new byte[8192];
                    while (true) {
                        int bytesRead = in2.read(buffer);
                        if (bytesRead <= 0) {
                            break;
                        }
                        baos.write(buffer, 0, bytesRead);
                    }
                    if (in2 != null) {
                        try {
                            in2.close();
                        } catch (Exception e) {
                        }
                    }
                    return baos.toByteArray();
                } catch (Throwable th2) {
                    th = th2;
                    in = in2;
                    if (in != null) {
                        try {
                            in.close();
                        } catch (Exception e2) {
                        }
                    }
                    throw th;
                }
            } catch (Throwable th3) {
                th = th3;
                if (in != null) {
                    in.close();
                }
                throw th;
            }
        } else {
            throw new RuntimeException(file + ": file not readable");
        }
    }

    public static byte[] readStream(InputStream is) throws IOException {
        return readStream(is, 32768);
    }

    public static byte[] readStream(InputStream is, int initSize) throws IOException {
        if (initSize <= 0) {
            initSize = 32768;
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream(initSize);
        byte[] buffer = new byte[8192];
        while (true) {
            int bytesRead = is.read(buffer);
            if (bytesRead <= 0) {
                return baos.toByteArray();
            }
            baos.write(buffer, 0, bytesRead);
        }
    }

    public static boolean hasArchiveSuffix(String fileName) {
        return fileName.endsWith(".zip") || fileName.endsWith(ShareConstants.JAR_SUFFIX) ||
                fileName.endsWith(ShareConstants.PATCH_SUFFIX);
    }
}
