package com.alipay.security.mobile.module.localstorage.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

public class FileUtil {
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

    public static String readFile(String str) {
        BufferedReader bufferedReader;
        Throwable th;
        BufferedReader bufferedReader2 = null;
        StringBuilder stringBuilder = new StringBuilder();
        try {
            if (!new File(str).exists()) {
                return null;
            }
            bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(str), "UTF-8"));
            while (true) {
                try {
                    String readLine = bufferedReader.readLine();
                    if (readLine != null) {
                        stringBuilder.append(readLine);
                    } else {
                        try {
                            break;
                        } catch (Throwable th2) {
                        }
                    }
                } catch (IOException e) {
                    bufferedReader2 = bufferedReader;
                } catch (Throwable th3) {
                    th = th3;
                }
            }
            bufferedReader.close();
            return stringBuilder.toString();
        } catch (IOException e2) {
            if (bufferedReader2 != null) {
                try {
                    bufferedReader2.close();
                } catch (Throwable th4) {
                }
            }
            return stringBuilder.toString();
        } catch (Throwable th5) {
            Throwable th6 = th5;
            bufferedReader = null;
            th = th6;
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (Throwable th7) {
                }
            }
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
