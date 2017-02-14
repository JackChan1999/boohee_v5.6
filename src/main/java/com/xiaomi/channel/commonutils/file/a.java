package com.xiaomi.channel.commonutils.file;

import android.text.TextUtils;

import com.xiaomi.channel.commonutils.logger.b;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.util.Date;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class a {
    public static final String[] a = new String[]{"jpg", "png", "bmp", "gif", "webp"};

    public static void a(File file, File file2) {
        OutputStream zipOutputStream;
        IOException iOException;
        Throwable th;
        OutputStream outputStream = null;
        try {
            zipOutputStream = new ZipOutputStream(new FileOutputStream(file, false));
            try {
                a(zipOutputStream, file2, null, null);
                a(zipOutputStream);
            } catch (FileNotFoundException e) {
                a(zipOutputStream);
            } catch (IOException e2) {
                IOException iOException2 = e2;
                outputStream = zipOutputStream;
                iOException = iOException2;
                try {
                    b.a("zip file failure + " + iOException.getMessage());
                    a(outputStream);
                } catch (Throwable th2) {
                    th = th2;
                    a(outputStream);
                    throw th;
                }
            } catch (Throwable th3) {
                Throwable th4 = th3;
                outputStream = zipOutputStream;
                th = th4;
                a(outputStream);
                throw th;
            }
        } catch (FileNotFoundException e3) {
            zipOutputStream = null;
            a(zipOutputStream);
        } catch (IOException e4) {
            iOException = e4;
            b.a("zip file failure + " + iOException.getMessage());
            a(outputStream);
        }
    }

    public static void a(InputStream inputStream) {
        if (inputStream != null) {
            try {
                inputStream.close();
            } catch (IOException e) {
            }
        }
    }

    public static void a(OutputStream outputStream) {
        if (outputStream != null) {
            try {
                outputStream.flush();
            } catch (IOException e) {
            }
            try {
                outputStream.close();
            } catch (IOException e2) {
            }
        }
    }

    public static void a(Reader reader) {
        if (reader != null) {
            try {
                reader.close();
            } catch (IOException e) {
            }
        }
    }

    public static void a(Writer writer) {
        if (writer != null) {
            try {
                writer.close();
            } catch (IOException e) {
            }
        }
    }

    public static void a(ZipOutputStream zipOutputStream, File file, String str, FileFilter
            fileFilter) {
        InputStream inputStream;
        IOException iOException;
        Throwable th;
        InputStream inputStream2 = null;
        int i = 0;
        if (str == null) {
            Object obj = "";
        }
        try {
            if (file.isDirectory()) {
                File[] listFiles = fileFilter != null ? file.listFiles(fileFilter) : file
                        .listFiles();
                zipOutputStream.putNextEntry(new ZipEntry(obj + File.separator));
                String str2 = TextUtils.isEmpty(obj) ? "" : obj + File.separator;
                for (int i2 = 0; i2 < listFiles.length; i2++) {
                    a(zipOutputStream, listFiles[i2], str2 + listFiles[i2].getName(), null);
                }
                File[] listFiles2 = file.listFiles(new b());
                if (listFiles2 != null) {
                    int length = listFiles2.length;
                    while (i < length) {
                        File file2 = listFiles2[i];
                        a(zipOutputStream, file2, str2 + File.separator + file2.getName(),
                                fileFilter);
                        i++;
                    }
                }
                inputStream = null;
            } else {
                if (TextUtils.isEmpty(obj)) {
                    zipOutputStream.putNextEntry(new ZipEntry(String.valueOf(new Date().getTime()
                    ) + ".txt"));
                } else {
                    zipOutputStream.putNextEntry(new ZipEntry(obj));
                }
                inputStream = new FileInputStream(file);
                try {
                    byte[] bArr = new byte[1024];
                    while (true) {
                        i = inputStream.read(bArr);
                        if (i == -1) {
                            break;
                        }
                        zipOutputStream.write(bArr, 0, i);
                    }
                } catch (IOException e) {
                    IOException iOException2 = e;
                    inputStream2 = inputStream;
                    iOException = iOException2;
                    try {
                        b.d("zipFiction failed with exception:" + iOException.toString());
                        a(inputStream2);
                    } catch (Throwable th2) {
                        th = th2;
                        a(inputStream2);
                        throw th;
                    }
                } catch (Throwable th3) {
                    Throwable th4 = th3;
                    inputStream2 = inputStream;
                    th = th4;
                    a(inputStream2);
                    throw th;
                }
            }
            a(inputStream);
        } catch (IOException e2) {
            iOException = e2;
            b.d("zipFiction failed with exception:" + iOException.toString());
            a(inputStream2);
        }
    }
}
