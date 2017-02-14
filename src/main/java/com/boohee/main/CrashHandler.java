package com.boohee.main;

import android.os.Process;
import android.text.TextUtils;
import android.util.Log;

import com.boohee.one.MyApplication;
import com.boohee.utils.SDcard;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.lang.Thread.UncaughtExceptionHandler;
import java.util.Date;

public class CrashHandler implements UncaughtExceptionHandler {
    private static CrashHandler INSTANCE = new CrashHandler();
    static final   String       TAG      = CrashHandler.class.getName();

    private CrashHandler() {
    }

    public static CrashHandler getInstance() {
        return INSTANCE;
    }

    public void uncaughtException(Thread thread, Throwable ex) {
        if (ex == null || !SDcard.hasSdcard()) {
            Process.killProcess(Process.myPid());
            return;
        }
        writeMsgToFile(new File(MyApplication.getContext().getExternalFilesDir("log"), "log.txt")
                , Log.getStackTraceString(ex));
        Process.killProcess(Process.myPid());
    }

    private void writeMsgToFile(File targetFile, String msg) {
        FileNotFoundException e;
        Throwable th;
        IOException e2;
        try {
            if (!targetFile.exists()) {
                targetFile.createNewFile();
            }
            if (targetFile != null && targetFile.exists() && !TextUtils.isEmpty(msg)) {
                RandomAccessFile raf = null;
                try {
                    RandomAccessFile raf2 = new RandomAccessFile(targetFile, "rw");
                    try {
                        raf2.seek(targetFile.length());
                        raf2.writeChars("\n<----" + new Date().toString() + "---->\n");
                        raf2.writeChars(msg);
                        if (raf2 != null) {
                            try {
                                raf2.close();
                            } catch (IOException e3) {
                                raf = raf2;
                                return;
                            }
                        }
                        raf = raf2;
                    } catch (FileNotFoundException e4) {
                        e = e4;
                        raf = raf2;
                        try {
                            e.printStackTrace();
                            if (raf != null) {
                                try {
                                    raf.close();
                                } catch (IOException e5) {
                                }
                            }
                        } catch (Throwable th2) {
                            th = th2;
                            if (raf != null) {
                                try {
                                    raf.close();
                                } catch (IOException e6) {
                                }
                            }
                            throw th;
                        }
                    } catch (IOException e7) {
                        e2 = e7;
                        raf = raf2;
                        e2.printStackTrace();
                        if (raf != null) {
                            try {
                                raf.close();
                            } catch (IOException e8) {
                            }
                        }
                    } catch (Throwable th3) {
                        th = th3;
                        raf = raf2;
                        if (raf != null) {
                            raf.close();
                        }
                        throw th;
                    }
                } catch (FileNotFoundException e9) {
                    e = e9;
                    e.printStackTrace();
                    if (raf != null) {
                        raf.close();
                    }
                } catch (IOException e10) {
                    e2 = e10;
                    e2.printStackTrace();
                    if (raf != null) {
                        raf.close();
                    }
                }
            }
        } catch (IOException e11) {
        }
    }
}
