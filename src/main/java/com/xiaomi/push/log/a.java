package com.xiaomi.push.log;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.os.Build.VERSION;

import com.umeng.socialize.common.SocializeConstants;
import com.xiaomi.channel.commonutils.logger.b;
import com.xiaomi.smack.util.h;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class a {
    private static String           b = "/MiPushLog";
    @SuppressLint({"SimpleDateFormat"})
    private final  SimpleDateFormat a = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private String  c;
    private String  d;
    private boolean e;
    private int     f;
    private int             g = 2097152;
    private ArrayList<File> h = new ArrayList();

    a() {
    }

    private void a(BufferedReader bufferedReader, BufferedWriter bufferedWriter, Pattern pattern) {
        char[] cArr = new char[4096];
        int read = bufferedReader.read(cArr);
        boolean z = false;
        while (read != -1 && !z) {
            boolean z2;
            String str = new String(cArr, 0, read);
            Matcher matcher = pattern.matcher(str);
            int i = 0;
            int i2 = 0;
            while (i < read && matcher.find(i)) {
                i = matcher.start();
                String substring = str.substring(i, this.c.length() + i);
                if (this.e) {
                    if (substring.compareTo(this.d) > 0) {
                        z2 = true;
                        break;
                    }
                } else if (substring.compareTo(this.c) >= 0) {
                    this.e = true;
                    i2 = i;
                }
                int indexOf = str.indexOf(10, i);
                i = indexOf != -1 ? i + indexOf : i + this.c.length();
            }
            i = read;
            z2 = z;
            if (this.e) {
                i -= i2;
                this.f += i;
                if (z2) {
                    bufferedWriter.write(cArr, i2, i);
                    return;
                }
                bufferedWriter.write(cArr, i2, i);
                if (this.f > this.g) {
                    return;
                }
            }
            z = z2;
            read = bufferedReader.read(cArr);
        }
    }

    private void b(File file) {
        Writer bufferedWriter;
        FileNotFoundException e;
        IOException e2;
        Throwable th;
        Reader reader = null;
        String str = "\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}";
        Pattern compile = Pattern.compile("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}");
        try {
            bufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)));
            try {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("model :").append(Build.MODEL);
                stringBuilder.append("; os :").append(VERSION.INCREMENTAL);
                stringBuilder.append("; uid :").append(h.b());
                stringBuilder.append("; lng :").append(Locale.getDefault().toString());
                stringBuilder.append("; sdk :").append(8);
                stringBuilder.append("\n");
                bufferedWriter.write(stringBuilder.toString());
                this.f = 0;
                Iterator it = this.h.iterator();
                Reader reader2 = null;
                while (it.hasNext()) {
                    try {
                        reader = new BufferedReader(new InputStreamReader(new FileInputStream(
                                (File) it.next())));
                        a(reader, bufferedWriter, compile);
                        reader.close();
                        reader2 = reader;
                    } catch (FileNotFoundException e3) {
                        e = e3;
                        reader = reader2;
                    } catch (IOException e4) {
                        e2 = e4;
                        reader = reader2;
                    } catch (Throwable th2) {
                        th = th2;
                        reader = reader2;
                    }
                }
                com.xiaomi.channel.commonutils.file.a.a(bufferedWriter);
                com.xiaomi.channel.commonutils.file.a.a(reader2);
            } catch (FileNotFoundException e5) {
                e = e5;
            } catch (IOException e6) {
                e2 = e6;
            }
        } catch (FileNotFoundException e7) {
            e = e7;
            bufferedWriter = null;
            try {
                b.c("LOG: filter error = " + e.getMessage());
                com.xiaomi.channel.commonutils.file.a.a(bufferedWriter);
                com.xiaomi.channel.commonutils.file.a.a(reader);
            } catch (Throwable th3) {
                th = th3;
                com.xiaomi.channel.commonutils.file.a.a(bufferedWriter);
                com.xiaomi.channel.commonutils.file.a.a(reader);
                throw th;
            }
        } catch (IOException e8) {
            e2 = e8;
            bufferedWriter = null;
            b.c("LOG: filter error = " + e2.getMessage());
            com.xiaomi.channel.commonutils.file.a.a(bufferedWriter);
            com.xiaomi.channel.commonutils.file.a.a(reader);
        } catch (Throwable th4) {
            th = th4;
            bufferedWriter = null;
            com.xiaomi.channel.commonutils.file.a.a(bufferedWriter);
            com.xiaomi.channel.commonutils.file.a.a(reader);
            throw th;
        }
    }

    a a(File file) {
        if (file.exists()) {
            this.h.add(file);
        }
        return this;
    }

    a a(Date date, Date date2) {
        if (date.after(date2)) {
            this.c = this.a.format(date2);
            this.d = this.a.format(date);
        } else {
            this.c = this.a.format(date);
            this.d = this.a.format(date2);
        }
        return this;
    }

    File a(Context context, Date date, Date date2, File file) {
        File filesDir;
        if ("com.xiaomi.xmsf".equalsIgnoreCase(context.getPackageName())) {
            filesDir = context.getFilesDir();
            a(new File(filesDir, "xmsf.log.1"));
            a(new File(filesDir, "xmsf.log"));
        } else {
            filesDir = new File(context.getExternalFilesDir(null) + b);
            a(new File(filesDir, "log0.txt"));
            a(new File(filesDir, "log1.txt"));
        }
        if (!filesDir.isDirectory()) {
            return null;
        }
        filesDir = new File(file, date.getTime() + SocializeConstants.OP_DIVIDER_MINUS + date2
                .getTime() + ".zip");
        if (filesDir.exists()) {
            return null;
        }
        a(date, date2);
        long currentTimeMillis = System.currentTimeMillis();
        File file2 = new File(file, "log.txt");
        b(file2);
        b.c("LOG: filter cost = " + (System.currentTimeMillis() - currentTimeMillis));
        if (file2.exists()) {
            currentTimeMillis = System.currentTimeMillis();
            com.xiaomi.channel.commonutils.file.a.a(filesDir, file2);
            b.c("LOG: zip cost = " + (System.currentTimeMillis() - currentTimeMillis));
            file2.delete();
            if (filesDir.exists()) {
                return filesDir;
            }
        }
        return null;
    }

    void a(int i) {
        if (i != 0) {
            this.g = i;
        }
    }
}
