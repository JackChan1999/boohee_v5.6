package com.baidu.location;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

class a implements b {
    private static String kR = "LocLog.txt";
    private static Boolean kS = Boolean.valueOf(true);
    private static char kT = 'v';
    private static String kU = "/sdcard/baidu";
    private static SimpleDateFormat kV = new SimpleDateFormat("yyyy-MM-dd");
    private static int kW = 0;
    private static Boolean kX = Boolean.valueOf(true);
    private static SimpleDateFormat kY = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    a() {
    }

    public static void byte(String str, String str2) {
        if(str, str2, 'd');
    }

    public static void c0() {
        File file = new File(kU, kV.format(c1()) + kR);
        if (file.exists()) {
            file.delete();
        }
    }

    private static Date c1() {
        Date date = new Date();
        Calendar instance = Calendar.getInstance();
        instance.setTime(date);
        instance.set(5, instance.get(5) - kW);
        return instance.getTime();
    }

    public static void case(String str, String str2) {
        if(str, str2, 'w');
    }

    public static void char(String str, String str2) {
        if(str, str2, 'i');
    }

    public static void do(String str, Object obj) {
        if(str, obj.toString(), 'i');
    }

    public static void else(String str, String str2) {
        if(str, str2, 'v');
    }

    public static void for(String str, Object obj) {
        if(str, obj.toString(), 'v');
    }

    public static void goto(String str, String str2) {
        if(str, str2, 'e');
    }

    public static void if(String str, Object obj) {
        if(str, obj.toString(), 'w');
    }

    private static void if(String str, String str2, char c) {
    }

    public static void int(String str, Object obj) {
        if(str, obj.toString(), 'e');
    }

    private static void int(String str, String str2, String str3) {
        Date date = new Date();
        String format = kV.format(date);
        String str4 = kY.format(date) + "    " + str + "    " + str2 + "    " + str3;
        try {
            Writer fileWriter = new FileWriter(new File(kU, format + kR), true);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write(str4);
            bufferedWriter.newLine();
            bufferedWriter.close();
            fileWriter.close();
        } catch (IOException e) {
        }
    }

    public static void new(String str, Object obj) {
        if(str, obj.toString(), 'd');
    }
}
