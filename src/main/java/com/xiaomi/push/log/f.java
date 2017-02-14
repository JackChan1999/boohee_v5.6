package com.xiaomi.push.log;

import android.content.Context;
import android.util.Log;
import android.util.Pair;

import com.xiaomi.channel.commonutils.logger.LoggerInterface;
import com.xiaomi.channel.commonutils.misc.b;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.RandomAccessFile;
import java.nio.channels.FileLock;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class f implements LoggerInterface {
    private static final SimpleDateFormat              a = new SimpleDateFormat("yyyy-MM-dd " +
            "HH:mm:ss aaa");
    private static       b                             b = new b(true);
    private static       String                        c = "/MiPushLog";
    private static       List<Pair<String, Throwable>> f = Collections.synchronizedList(new
            ArrayList());
    private String  d;
    private Context e;

    public f(Context context) {
        this.e = context;
        if (context.getApplicationContext() != null) {
            this.e = context.getApplicationContext();
        }
        this.d = this.e.getPackageName();
    }

    private void b() {
        RandomAccessFile randomAccessFile;
        FileLock lock;
        Throwable e;
        BufferedWriter bufferedWriter;
        FileLock fileLock;
        RandomAccessFile randomAccessFile2;
        String str;
        BufferedWriter bufferedWriter2 = null;
        RandomAccessFile randomAccessFile3 = null;
        FileLock fileLock2 = null;
        BufferedWriter bufferedWriter3 = null;
        try {
            File file = new File(this.e.getExternalFilesDir(null) + c);
            String str2;
            if ((file.exists() && file.isDirectory()) || file.mkdirs()) {
                File file2 = new File(file, "log.lock");
                if (!file2.exists() || file2.isDirectory()) {
                    file2.createNewFile();
                }
                randomAccessFile = new RandomAccessFile(file2, "rw");
                try {
                    lock = randomAccessFile.getChannel().lock();
                    try {
                        bufferedWriter3 = new BufferedWriter(new OutputStreamWriter(new
                                FileOutputStream(new File(file, "log1.txt"), true)));
                        while (!f.isEmpty()) {
                            try {
                                Pair pair = (Pair) f.remove(0);
                                str2 = (String) pair.first;
                                if (pair.second != null) {
                                    str2 = (str2 + "\n") + Log.getStackTraceString((Throwable)
                                            pair.second);
                                }
                                bufferedWriter3.write(str2 + "\n");
                            } catch (Exception e2) {
                                e = e2;
                                bufferedWriter = bufferedWriter3;
                                fileLock = lock;
                                randomAccessFile2 = randomAccessFile;
                            } catch (Throwable th) {
                                e = th;
                                bufferedWriter2 = bufferedWriter3;
                            }
                        }
                        bufferedWriter3.flush();
                        if (bufferedWriter3 != null) {
                            bufferedWriter3.close();
                            bufferedWriter = bufferedWriter2;
                        } else {
                            bufferedWriter = bufferedWriter3;
                        }
                    } catch (Exception e3) {
                        e = e3;
                        bufferedWriter = bufferedWriter2;
                        randomAccessFile2 = randomAccessFile;
                        fileLock = lock;
                        try {
                            Log.e(this.d, "", e);
                            if (bufferedWriter != null) {
                                try {
                                    bufferedWriter.close();
                                } catch (Throwable e4) {
                                    Log.e(this.d, "", e4);
                                }
                            }
                            if (fileLock != null && fileLock.isValid()) {
                                try {
                                    fileLock.release();
                                } catch (Throwable e42) {
                                    Log.e(this.d, "", e42);
                                }
                            }
                            if (randomAccessFile2 == null) {
                                try {
                                    randomAccessFile2.close();
                                } catch (IOException e5) {
                                    e42 = e5;
                                    str2 = this.d;
                                    str = "";
                                    Log.e(str2, str, e42);
                                    return;
                                }
                            }
                        } catch (Throwable th2) {
                            e42 = th2;
                            lock = fileLock;
                            randomAccessFile = randomAccessFile2;
                            bufferedWriter2 = bufferedWriter;
                            if (bufferedWriter2 != null) {
                                try {
                                    bufferedWriter2.close();
                                } catch (Throwable e6) {
                                    Log.e(this.d, "", e6);
                                }
                            }
                            if (lock != null && lock.isValid()) {
                                try {
                                    lock.release();
                                } catch (Throwable e62) {
                                    Log.e(this.d, "", e62);
                                }
                            }
                            if (randomAccessFile != null) {
                                try {
                                    randomAccessFile.close();
                                } catch (Throwable e622) {
                                    Log.e(this.d, "", e622);
                                }
                            }
                            throw e42;
                        }
                    } catch (Throwable th3) {
                        e42 = th3;
                        if (bufferedWriter2 != null) {
                            bufferedWriter2.close();
                        }
                        lock.release();
                        if (randomAccessFile != null) {
                            randomAccessFile.close();
                        }
                        throw e42;
                    }
                } catch (Exception e7) {
                    e42 = e7;
                    bufferedWriter = bufferedWriter2;
                    randomAccessFile2 = randomAccessFile;
                    Log.e(this.d, "", e42);
                    if (bufferedWriter != null) {
                        bufferedWriter.close();
                    }
                    fileLock.release();
                    if (randomAccessFile2 == null) {
                        randomAccessFile2.close();
                    }
                } catch (Throwable th4) {
                    e42 = th4;
                    Object obj = bufferedWriter2;
                    if (bufferedWriter2 != null) {
                        bufferedWriter2.close();
                    }
                    lock.release();
                    if (randomAccessFile != null) {
                        randomAccessFile.close();
                    }
                    throw e42;
                }
                try {
                    file2 = new File(file, "log1.txt");
                    if (file2.length() >= 1048576) {
                        File file3 = new File(file, "log0.txt");
                        if (file3.exists() && file3.isFile()) {
                            file3.delete();
                        }
                        file2.renameTo(file3);
                    }
                    if (bufferedWriter != null) {
                        try {
                            bufferedWriter.close();
                        } catch (Throwable e422) {
                            Log.e(this.d, "", e422);
                        }
                    }
                    if (lock != null && lock.isValid()) {
                        try {
                            lock.release();
                        } catch (Throwable e4222) {
                            Log.e(this.d, "", e4222);
                        }
                    }
                    if (randomAccessFile != null) {
                        try {
                            randomAccessFile.close();
                            return;
                        } catch (IOException e8) {
                            e4222 = e8;
                            str2 = this.d;
                            str = "";
                            Log.e(str2, str, e4222);
                            return;
                        }
                    }
                    return;
                } catch (Exception e9) {
                    e4222 = e9;
                    fileLock = lock;
                    randomAccessFile2 = randomAccessFile;
                    Log.e(this.d, "", e4222);
                    if (bufferedWriter != null) {
                        bufferedWriter.close();
                    }
                    fileLock.release();
                    if (randomAccessFile2 == null) {
                        randomAccessFile2.close();
                    }
                } catch (Throwable th5) {
                    e4222 = th5;
                    bufferedWriter2 = bufferedWriter;
                    if (bufferedWriter2 != null) {
                        bufferedWriter2.close();
                    }
                    lock.release();
                    if (randomAccessFile != null) {
                        randomAccessFile.close();
                    }
                    throw e4222;
                }
            }
            Log.w(this.d, "Create mipushlog directory fail.");
            if (bufferedWriter2 != null) {
                try {
                    bufferedWriter3.close();
                } catch (Throwable e10) {
                    Log.e(this.d, "", e10);
                }
            }
            if (bufferedWriter2 != null && bufferedWriter2.isValid()) {
                try {
                    fileLock2.release();
                } catch (Throwable e6222) {
                    Log.e(this.d, "", e6222);
                }
            }
            if (bufferedWriter2 != null) {
                try {
                    randomAccessFile3.close();
                } catch (IOException e11) {
                    e4222 = e11;
                    str2 = this.d;
                    str = "";
                }
            }
        } catch (Exception e12) {
            e4222 = e12;
            bufferedWriter = bufferedWriter2;
            Object obj2 = bufferedWriter2;
            Log.e(this.d, "", e4222);
            if (bufferedWriter != null) {
                bufferedWriter.close();
            }
            fileLock.release();
            if (randomAccessFile2 == null) {
                randomAccessFile2.close();
            }
        } catch (Throwable th6) {
            e4222 = th6;
            lock = bufferedWriter2;
            randomAccessFile = bufferedWriter2;
            if (bufferedWriter2 != null) {
                bufferedWriter2.close();
            }
            lock.release();
            if (randomAccessFile != null) {
                randomAccessFile.close();
            }
            throw e4222;
        }
    }

    public final void log(String str) {
        log(str, null);
    }

    public final void log(String str, Throwable th) {
        f.add(new Pair(String.format("%1$s %2$s %3$s ", new Object[]{a.format(new Date()), this
                .d, str}), th));
        b.a(new g(this));
    }

    public final void setTag(String str) {
        this.d = str;
    }
}
