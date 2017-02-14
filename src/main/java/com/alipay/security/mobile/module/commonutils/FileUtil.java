package com.alipay.security.mobile.module.commonutils;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class FileUtil {
    public static boolean createDirs(String str) {
        File file = new File(str);
        return !file.exists() ? file.mkdirs() : false;
    }

    public static boolean createFile(String str) {
        boolean z = false;
        File file = new File(str);
        if (!file.exists()) {
            try {
                z = file.createNewFile();
            } catch (Exception e) {
            }
        }
        return z;
    }

    public static long getFileSize(String str, String str2) {
        File file = new File(str, str2);
        return file.exists() ? file.length() : 0;
    }

    public static boolean isBlank(String str) {
        if (str != null) {
            int length = str.length();
            if (length != 0) {
                for (int i = 0; i < length; i++) {
                    if (!Character.isWhitespace(str.charAt(i))) {
                        return false;
                    }
                }
                return true;
            }
        }
        return true;
    }

    public static String readFile(String str, String str2) {
        FileReader fileReader;
        Exception e;
        Throwable th;
        try {
            File file = new File(str, str2);
            if (file.exists()) {
                char[] cArr = new char[((int) file.length())];
                fileReader = new FileReader(file);
                try {
                    fileReader.read(cArr);
                    String valueOf = String.valueOf(cArr);
                    try {
                        fileReader.close();
                        return valueOf;
                    } catch (IOException e2) {
                        e2.printStackTrace();
                        return valueOf;
                    }
                } catch (Exception e3) {
                    e = e3;
                    try {
                        e.getMessage();
                        try {
                            fileReader.close();
                        } catch (IOException e4) {
                            e4.printStackTrace();
                        }
                        return "";
                    } catch (Throwable th2) {
                        th = th2;
                        try {
                            fileReader.close();
                        } catch (IOException e22) {
                            e22.printStackTrace();
                        }
                        throw th;
                    }
                }
            }
            fileReader = null;
            try {
                fileReader.close();
                return null;
            } catch (IOException e222) {
                e222.printStackTrace();
                return null;
            }
        } catch (Exception e5) {
            Exception exception = e5;
            fileReader = null;
            e = exception;
            e.getMessage();
            fileReader.close();
            return "";
        } catch (Throwable th3) {
            Throwable th4 = th3;
            fileReader = null;
            th = th4;
            fileReader.close();
            throw th;
        }
    }

    public static void writeFile(String str, String str2) {
        FileWriter fileWriter;
        Throwable th;
        FileWriter fileWriter2 = null;
        try {
            fileWriter = new FileWriter(new File(str), false);
            try {
                fileWriter.write(str2);
                try {
                    fileWriter.close();
                } catch (IOException e) {
                }
            } catch (Exception e2) {
                if (fileWriter != null) {
                    try {
                        fileWriter.close();
                    } catch (IOException e3) {
                    }
                }
            } catch (Throwable th2) {
                Throwable th3 = th2;
                fileWriter2 = fileWriter;
                th = th3;
                if (fileWriter2 != null) {
                    try {
                        fileWriter2.close();
                    } catch (IOException e4) {
                    }
                }
                throw th;
            }
        } catch (Exception e5) {
            fileWriter = null;
            if (fileWriter != null) {
                fileWriter.close();
            }
        } catch (Throwable th4) {
            th = th4;
            if (fileWriter2 != null) {
                fileWriter2.close();
            }
            throw th;
        }
    }
}
