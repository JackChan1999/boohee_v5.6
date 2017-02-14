package com.baidu.location;

import android.os.Environment;
import android.os.Process;
import com.qiniu.android.common.Constants;
import java.io.File;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

public class aa implements UncaughtExceptionHandler, ax, n {
    private static aa gu = null;

    private aa() {
    }

    public static aa bg() {
        if (gu == null) {
            gu = new aa();
        }
        return gu;
    }

    private String if(Throwable th) {
        Writer stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        th.printStackTrace(printWriter);
        printWriter.close();
        return stringWriter.toString();
    }

    public void bh() {
        String str = null;
        try {
            File file = new File((Environment.getExternalStorageDirectory().getPath() + "/traces") + "/error_fs.dat");
            if (file.exists()) {
                RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw");
                randomAccessFile.seek(280);
                if (1326 == randomAccessFile.readInt()) {
                    String str2;
                    byte[] bArr;
                    randomAccessFile.seek(308);
                    int readInt = randomAccessFile.readInt();
                    if (readInt <= 0 || readInt >= 2048) {
                        str2 = null;
                    } else {
                        bArr = new byte[readInt];
                        randomAccessFile.read(bArr, 0, readInt);
                        str2 = new String(bArr, 0, readInt);
                    }
                    randomAccessFile.seek(600);
                    readInt = randomAccessFile.readInt();
                    if (readInt > 0 && readInt < 2048) {
                        bArr = new byte[readInt];
                        randomAccessFile.read(bArr, 0, readInt);
                        str = new String(bArr, 0, readInt);
                    }
                    if (new(str2, str)) {
                        randomAccessFile.seek(280);
                        randomAccessFile.writeInt(12346);
                    }
                }
                randomAccessFile.close();
            }
        } catch (Exception e) {
        }
    }

    public void if(File file, String str, String str2) {
        try {
            RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw");
            randomAccessFile.seek(280);
            randomAccessFile.writeInt(12346);
            randomAccessFile.seek(300);
            randomAccessFile.writeLong(System.currentTimeMillis());
            byte[] bytes = str.getBytes();
            randomAccessFile.writeInt(bytes.length);
            randomAccessFile.write(bytes, 0, bytes.length);
            randomAccessFile.seek(600);
            bytes = str2.getBytes();
            randomAccessFile.writeInt(bytes.length);
            randomAccessFile.write(bytes, 0, bytes.length);
            if (!new(str, str2)) {
                randomAccessFile.seek(280);
                randomAccessFile.writeInt(1326);
            }
            randomAccessFile.close();
        } catch (Exception e) {
        }
    }

    boolean new(String str, String str2) {
        if (!ar.bU()) {
            return false;
        }
        try {
            HttpUriRequest httpPost = new HttpPost(c.Y);
            List arrayList = new ArrayList();
            arrayList.add(new BasicNameValuePair("e0", str));
            arrayList.add(new BasicNameValuePair("e1", str2));
            httpPost.setEntity(new UrlEncodedFormEntity(arrayList, Constants.UTF_8));
            HttpClient defaultHttpClient = new DefaultHttpClient();
            defaultHttpClient.getParams().setParameter("http.connection.timeout", Integer.valueOf(ax.F));
            defaultHttpClient.getParams().setParameter("http.socket.timeout", Integer.valueOf(ax.F));
            return defaultHttpClient.execute(httpPost).getStatusLine().getStatusCode() == 200;
        } catch (Exception e) {
            return false;
        }
    }

    public void uncaughtException(Thread thread, Throwable th) {
        File file;
        String str;
        String i;
        Object obj;
        String str2;
        RandomAccessFile randomAccessFile;
        File file2;
        File file3 = null;
        if (System.currentTimeMillis() - ab.bi() < 10000 && n.V > f.getFrameVersion()) {
            if (System.currentTimeMillis() - e.do().a() < 40000) {
                file = new File(c.goto() + File.separator + f.getJarFileName());
                if (file.exists()) {
                    file.delete();
                }
            } else {
                e.do().if(System.currentTimeMillis());
            }
        }
        if (y.f0) {
            try {
                str = if(th);
                try {
                    String str3 = az.cn().char(false) + k.p().o();
                    i = str3 != null ? Jni.i(str3) : null;
                } catch (Exception e) {
                    Object obj2 = str;
                    obj = file;
                    i = null;
                    str2 = Environment.getExternalStorageDirectory().getPath() + "/traces";
                    file = new File(str2 + "/error_fs.dat");
                    if (file.exists()) {
                        randomAccessFile = new RandomAccessFile(file, "rw");
                        randomAccessFile.seek(300);
                        if (System.currentTimeMillis() - randomAccessFile.readLong() > 604800000) {
                            if(file, i, str);
                        }
                        randomAccessFile.close();
                    } else {
                        file2 = new File(str2);
                        if (!file2.exists()) {
                            file2.mkdirs();
                        }
                        if (file.createNewFile()) {
                            file3 = file;
                        }
                        if(file3, i, str);
                    }
                    Process.killProcess(Process.myPid());
                    return;
                }
            } catch (Exception e2) {
                file = null;
                obj = file;
                i = null;
                str2 = Environment.getExternalStorageDirectory().getPath() + "/traces";
                file = new File(str2 + "/error_fs.dat");
                if (file.exists()) {
                    randomAccessFile = new RandomAccessFile(file, "rw");
                    randomAccessFile.seek(300);
                    if (System.currentTimeMillis() - randomAccessFile.readLong() > 604800000) {
                        if(file, i, str);
                    }
                    randomAccessFile.close();
                } else {
                    file2 = new File(str2);
                    if (file2.exists()) {
                        file2.mkdirs();
                    }
                    if (file.createNewFile()) {
                        file3 = file;
                    }
                    if(file3, i, str);
                }
                Process.killProcess(Process.myPid());
                return;
            }
            try {
                str2 = Environment.getExternalStorageDirectory().getPath() + "/traces";
                file = new File(str2 + "/error_fs.dat");
                if (file.exists()) {
                    file2 = new File(str2);
                    if (file2.exists()) {
                        file2.mkdirs();
                    }
                    if (file.createNewFile()) {
                        file3 = file;
                    }
                    if(file3, i, str);
                } else {
                    randomAccessFile = new RandomAccessFile(file, "rw");
                    randomAccessFile.seek(300);
                    if (System.currentTimeMillis() - randomAccessFile.readLong() > 604800000) {
                        if(file, i, str);
                    }
                    randomAccessFile.close();
                }
            } catch (Exception e3) {
            }
            Process.killProcess(Process.myPid());
            return;
        }
        Process.killProcess(Process.myPid());
    }
}
