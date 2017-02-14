package com.alipay.security.mobile.module.commonutils;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

public class LOG {
    private static String a = "";
    private static String b = "";
    private static String c = "";

    public static String getStackString(Throwable th) {
        if (th == null) {
            return "";
        }
        Writer stringWriter = new StringWriter();
        th.printStackTrace(new PrintWriter(stringWriter));
        return stringWriter.toString();
    }

    public static synchronized void init(String str, String str2, String str3) {
        synchronized (LOG.class) {
            a = str;
            b = str2;
            c = str3;
        }
    }

    public static synchronized void logException(Throwable th) {
        synchronized (LOG.class) {
            List arrayList = new ArrayList();
            arrayList.add(getStackString(th));
            logMessage(arrayList);
        }
    }

    public static synchronized void logMessage(String str) {
        synchronized (LOG.class) {
            List arrayList = new ArrayList();
            arrayList.add(str);
            logMessage(arrayList);
        }
    }

    public static synchronized void logMessage(List<String> list) {
        synchronized (LOG.class) {
            if (!(CommonUtils.isBlank(b) || CommonUtils.isBlank(c))) {
                StringBuffer stringBuffer = new StringBuffer();
                stringBuffer.append(c);
                for (String str : list) {
                    stringBuffer.append(", " + str);
                }
                stringBuffer.append("\n");
                try {
                    File file = new File(a);
                    if (!file.exists()) {
                        file.mkdirs();
                    }
                    File file2 = new File(a, b);
                    if (!file2.exists()) {
                        file2.createNewFile();
                    }
                    FileWriter fileWriter = file2.length() + ((long) stringBuffer.length()) <= 51200 ? new FileWriter(file2, true) : new FileWriter(file2);
                    fileWriter.write(stringBuffer.toString());
                    fileWriter.flush();
                    fileWriter.close();
                } catch (Exception e) {
                    e.toString();
                }
            }
        }
    }
}
