package com.tencent.tinker.commons.resutil;

import com.tencent.tinker.commons.ziputil.TinkerZipEntry;
import com.tencent.tinker.commons.ziputil.TinkerZipFile;
import com.tencent.tinker.commons.ziputil.TinkerZipOutputStream;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class ResUtil {
    private static final int BUFFER_SIZE = 16384;

    public static void extractTinkerEntry(TinkerZipFile apk, TinkerZipEntry zipEntry,
                                          TinkerZipOutputStream outputStream) throws IOException {
        InputStream in = null;
        try {
            in = apk.getInputStream(zipEntry);
            outputStream.putNextEntry(new TinkerZipEntry(zipEntry));
            byte[] buffer = new byte[16384];
            for (int length = in.read(buffer); length != -1; length = in.read(buffer)) {
                outputStream.write(buffer, 0, length);
            }
            outputStream.closeEntry();
        } finally {
            if (in != null) {
                in.close();
            }
        }
    }

    public static void extractLargeModifyFile(TinkerZipEntry sourceArscEntry, File newFile, long
            newFileCrc, TinkerZipOutputStream outputStream) throws IOException {
        Throwable th;
        TinkerZipEntry newArscZipEntry = new TinkerZipEntry(sourceArscEntry);
        newArscZipEntry.setMethod(0);
        newArscZipEntry.setSize(newFile.length());
        newArscZipEntry.setCompressedSize(newFile.length());
        newArscZipEntry.setCrc(newFileCrc);
        FileInputStream in = null;
        try {
            FileInputStream in2 = new FileInputStream(newFile);
            try {
                outputStream.putNextEntry(new TinkerZipEntry(newArscZipEntry));
                byte[] buffer = new byte[16384];
                for (int length = in2.read(buffer); length != -1; length = in2.read(buffer)) {
                    outputStream.write(buffer, 0, length);
                }
                outputStream.closeEntry();
                if (in2 != null) {
                    in2.close();
                }
            } catch (Throwable th2) {
                th = th2;
                in = in2;
                if (in != null) {
                    in.close();
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
    }
}
