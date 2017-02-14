package com.boohee.one.cache;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Process;

import com.boohee.utils.FileUtil;
import com.umeng.socialize.common.SocializeConstants;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.RandomAccessFile;
import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import org.json.JSONArray;
import org.json.JSONObject;

public class FileCache {
    private static final int                    MAX_COUNT    = Integer.MAX_VALUE;
    private static final int                    MAX_SIZE     = 50000000;
    public static final  int                    TIME_DAY     = 86400;
    public static final  int                    TIME_HOUR    = 3600;
    private static       Map<String, FileCache> mInstanceMap = new HashMap();
    private ACacheManager mCache;

    public class ACacheManager {
        private final AtomicInteger   cacheCount;
        protected     File            cacheDir;
        private final AtomicLong      cacheSize;
        private final int             countLimit;
        private final Map<File, Long> lastUsageDates;
        private final long            sizeLimit;

        private ACacheManager(File cacheDir, long sizeLimit, int countLimit) {
            this.lastUsageDates = Collections.synchronizedMap(new HashMap());
            this.cacheDir = cacheDir;
            this.sizeLimit = sizeLimit;
            this.countLimit = countLimit;
            this.cacheSize = new AtomicLong();
            this.cacheCount = new AtomicInteger();
            calculateCacheSizeAndCacheCount();
        }

        private void calculateCacheSizeAndCacheCount() {
            new Thread(new Runnable() {
                public void run() {
                    int size = 0;
                    int count = 0;
                    File[] cachedFiles = ACacheManager.this.cacheDir.listFiles();
                    if (cachedFiles != null) {
                        for (File cachedFile : cachedFiles) {
                            size = (int) (((long) size) + ACacheManager.this.calculateSize
                                    (cachedFile));
                            count++;
                            ACacheManager.this.lastUsageDates.put(cachedFile, Long.valueOf
                                    (cachedFile.lastModified()));
                        }
                        ACacheManager.this.cacheSize.set((long) size);
                        ACacheManager.this.cacheCount.set(count);
                    }
                }
            }).start();
        }

        private void put(File file) {
            int curCacheCount = this.cacheCount.get();
            while (curCacheCount + 1 > this.countLimit) {
                this.cacheSize.addAndGet(-removeNext());
                curCacheCount = this.cacheCount.addAndGet(-1);
            }
            this.cacheCount.addAndGet(1);
            long valueSize = calculateSize(file);
            long curCacheSize = this.cacheSize.get();
            while (curCacheSize + valueSize > this.sizeLimit) {
                curCacheSize = this.cacheSize.addAndGet(-removeNext());
            }
            this.cacheSize.addAndGet(valueSize);
            Long currentTime = Long.valueOf(System.currentTimeMillis());
            file.setLastModified(currentTime.longValue());
            this.lastUsageDates.put(file, currentTime);
        }

        private File get(String key) {
            File file = newFile(key);
            Long currentTime = Long.valueOf(System.currentTimeMillis());
            file.setLastModified(currentTime.longValue());
            this.lastUsageDates.put(file, currentTime);
            return file;
        }

        private File newFile(String key) {
            return new File(this.cacheDir, key.hashCode() + "");
        }

        private boolean remove(String key) {
            return get(key).delete();
        }

        private void clear() {
            this.lastUsageDates.clear();
            this.cacheSize.set(0);
            File[] files = this.cacheDir.listFiles();
            if (files != null) {
                for (File f : files) {
                    f.delete();
                }
            }
        }

        private long removeNext() {
            if (this.lastUsageDates.isEmpty()) {
                return 0;
            }
            Long oldestUsage = null;
            File mostLongUsedFile = null;
            Set<Entry<File, Long>> entries = this.lastUsageDates.entrySet();
            synchronized (this.lastUsageDates) {
                for (Entry<File, Long> entry : entries) {
                    if (mostLongUsedFile == null) {
                        mostLongUsedFile = (File) entry.getKey();
                        oldestUsage = (Long) entry.getValue();
                    } else {
                        Long lastValueUsage = (Long) entry.getValue();
                        if (lastValueUsage.longValue() < oldestUsage.longValue()) {
                            oldestUsage = lastValueUsage;
                            mostLongUsedFile = (File) entry.getKey();
                        }
                    }
                }
            }
            long fileSize = calculateSize(mostLongUsedFile);
            if (!mostLongUsedFile.delete()) {
                return fileSize;
            }
            this.lastUsageDates.remove(mostLongUsedFile);
            return fileSize;
        }

        private long calculateSize(File file) {
            return file.length();
        }
    }

    private static class Utils {
        private static final char mSeparator = ' ';

        private Utils() {
        }

        private static boolean isDue(String str) {
            return isDue(str.getBytes());
        }

        private static boolean isDue(byte[] data) {
            String[] strs = getDateInfoFromDate(data);
            if (strs != null && strs.length == 2) {
                String saveTimeStr = strs[0];
                while (saveTimeStr.startsWith("0")) {
                    saveTimeStr = saveTimeStr.substring(1, saveTimeStr.length());
                }
                try {
                    if (System.currentTimeMillis() > (1000 * Long.valueOf(strs[1]).longValue()) +
                            Long.valueOf(saveTimeStr).longValue()) {
                        return true;
                    }
                } catch (Exception e) {
                }
            }
            return false;
        }

        private static String newStringWithDateInfo(int second, String strInfo) {
            return createDateInfo(second) + strInfo;
        }

        private static byte[] newByteArrayWithDateInfo(int second, byte[] data2) {
            byte[] data1 = createDateInfo(second).getBytes();
            byte[] retdata = new byte[(data1.length + data2.length)];
            System.arraycopy(data1, 0, retdata, 0, data1.length);
            System.arraycopy(data2, 0, retdata, data1.length, data2.length);
            return retdata;
        }

        private static String clearDateInfo(String strInfo) {
            if (strInfo == null || !hasDateInfo(strInfo.getBytes())) {
                return strInfo;
            }
            return strInfo.substring(strInfo.indexOf(32) + 1, strInfo.length());
        }

        private static byte[] clearDateInfo(byte[] data) {
            if (hasDateInfo(data)) {
                return copyOfRange(data, indexOf(data, mSeparator) + 1, data.length);
            }
            return data;
        }

        private static boolean hasDateInfo(byte[] data) {
            return data != null && data.length > 15 && data[13] == (byte) 45 && indexOf(data,
                    mSeparator) > 14;
        }

        private static String[] getDateInfoFromDate(byte[] data) {
            if (!hasDateInfo(data)) {
                return null;
            }
            String saveDate = new String(copyOfRange(data, 0, 13));
            String deleteAfter = new String(copyOfRange(data, 14, indexOf(data, mSeparator)));
            return new String[]{saveDate, deleteAfter};
        }

        private static int indexOf(byte[] data, char c) {
            for (int i = 0; i < data.length; i++) {
                if (data[i] == c) {
                    return i;
                }
            }
            return -1;
        }

        private static byte[] copyOfRange(byte[] original, int from, int to) {
            int newLength = to - from;
            if (newLength < 0) {
                throw new IllegalArgumentException(from + " > " + to);
            }
            byte[] copy = new byte[newLength];
            System.arraycopy(original, from, copy, 0, Math.min(original.length - from, newLength));
            return copy;
        }

        private static String createDateInfo(int second) {
            String currentTime = System.currentTimeMillis() + "";
            while (currentTime.length() < 13) {
                currentTime = "0" + currentTime;
            }
            return currentTime + SocializeConstants.OP_DIVIDER_MINUS + second + mSeparator;
        }

        private static byte[] Bitmap2Bytes(Bitmap bm) {
            if (bm == null) {
                return null;
            }
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bm.compress(CompressFormat.PNG, 100, baos);
            return baos.toByteArray();
        }

        private static Bitmap Bytes2Bimap(byte[] b) {
            if (b.length == 0) {
                return null;
            }
            return BitmapFactory.decodeByteArray(b, 0, b.length);
        }

        private static Bitmap drawable2Bitmap(Drawable drawable) {
            if (drawable == null) {
                return null;
            }
            int w = drawable.getIntrinsicWidth();
            int h = drawable.getIntrinsicHeight();
            Bitmap bitmap = Bitmap.createBitmap(w, h, drawable.getOpacity() != -1 ? Config
                    .ARGB_8888 : Config.RGB_565);
            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, w, h);
            drawable.draw(canvas);
            return bitmap;
        }

        private static Drawable bitmap2Drawable(Bitmap bm) {
            if (bm == null) {
                return null;
            }
            return new BitmapDrawable(bm);
        }
    }

    public static void init(Context context) {
        if (com.boohee.utility.Config.getVersionCode() > 104) {
            FileUtil.removeDirectoryToAnother(new File(context.getCacheDir(), "ACache"),
                    getCacheDirFile(context));
        }
    }

    public static FileCache get(Context ctx) {
        return get(ctx, "ACache");
    }

    public static FileCache get(Context ctx, String cacheName) {
        return get(getCacheDirFile(ctx), 50000000, Integer.MAX_VALUE);
    }

    public static FileCache get(File cacheDir) {
        return get(cacheDir, 50000000, Integer.MAX_VALUE);
    }

    public static FileCache get(Context ctx, long max_zise, int max_count) {
        return get(getCacheDirFile(ctx), max_zise, max_count);
    }

    public static File getCacheDirFile(Context context) {
        return new File(context.getFilesDir(), "ACache");
    }

    public static FileCache get(File cacheDir, long max_zise, int max_count) {
        FileCache manager = (FileCache) mInstanceMap.get(cacheDir.getAbsoluteFile() + myPid());
        if (manager != null) {
            return manager;
        }
        manager = new FileCache(cacheDir, max_zise, max_count);
        mInstanceMap.put(cacheDir.getAbsolutePath() + myPid(), manager);
        return manager;
    }

    private static String myPid() {
        return "_" + Process.myPid();
    }

    private FileCache(File cacheDir, long max_size, int max_count) {
        if (cacheDir.exists() || cacheDir.mkdirs()) {
            this.mCache = new ACacheManager(cacheDir, max_size, max_count);
            return;
        }
        throw new RuntimeException("can't make dirs in " + cacheDir.getAbsolutePath());
    }

    public void put(String key, String value) {
        IOException e;
        Throwable th;
        File file = this.mCache.newFile(key);
        BufferedWriter out = null;
        try {
            BufferedWriter out2 = new BufferedWriter(new FileWriter(file), 1024);
            try {
                out2.write(value);
                if (out2 != null) {
                    try {
                        out2.flush();
                        out2.close();
                    } catch (IOException e2) {
                        e2.printStackTrace();
                    }
                }
                this.mCache.put(file);
                out = out2;
            } catch (IOException e3) {
                e2 = e3;
                out = out2;
                try {
                    e2.printStackTrace();
                    if (out != null) {
                        try {
                            out.flush();
                            out.close();
                        } catch (IOException e22) {
                            e22.printStackTrace();
                        }
                    }
                    this.mCache.put(file);
                } catch (Throwable th2) {
                    th = th2;
                    if (out != null) {
                        try {
                            out.flush();
                            out.close();
                        } catch (IOException e222) {
                            e222.printStackTrace();
                        }
                    }
                    this.mCache.put(file);
                    throw th;
                }
            } catch (Throwable th3) {
                th = th3;
                out = out2;
                if (out != null) {
                    out.flush();
                    out.close();
                }
                this.mCache.put(file);
                throw th;
            }
        } catch (IOException e4) {
            e222 = e4;
            e222.printStackTrace();
            if (out != null) {
                out.flush();
                out.close();
            }
            this.mCache.put(file);
        }
    }

    public void put(String key, String value, int saveTime) {
        put(key, Utils.newStringWithDateInfo(saveTime, value));
    }

    public String getAsString(String key) {
        IOException e;
        Throwable th;
        String str = null;
        File file = this.mCache.get(key);
        if (file.exists()) {
            BufferedReader in = null;
            try {
                BufferedReader in2 = new BufferedReader(new FileReader(file));
                try {
                    String readString = "";
                    while (true) {
                        String currentLine = in2.readLine();
                        if (currentLine == null) {
                            break;
                        }
                        readString = readString + currentLine;
                    }
                    if (Utils.isDue(readString)) {
                        if (in2 != null) {
                            try {
                                in2.close();
                            } catch (IOException e2) {
                                e2.printStackTrace();
                            }
                        }
                        if (true) {
                            remove(key);
                        }
                    } else {
                        str = Utils.clearDateInfo(readString);
                        if (in2 != null) {
                            try {
                                in2.close();
                            } catch (IOException e22) {
                                e22.printStackTrace();
                            }
                        }
                        if (null != null) {
                            remove(key);
                        }
                    }
                } catch (IOException e3) {
                    e22 = e3;
                    in = in2;
                    try {
                        e22.printStackTrace();
                        if (in != null) {
                            try {
                                in.close();
                            } catch (IOException e222) {
                                e222.printStackTrace();
                            }
                        }
                        if (null != null) {
                            remove(key);
                        }
                        return str;
                    } catch (Throwable th2) {
                        th = th2;
                        if (in != null) {
                            try {
                                in.close();
                            } catch (IOException e2222) {
                                e2222.printStackTrace();
                            }
                        }
                        if (null != null) {
                            remove(key);
                        }
                        throw th;
                    }
                } catch (Throwable th3) {
                    th = th3;
                    in = in2;
                    if (in != null) {
                        in.close();
                    }
                    if (null != null) {
                        remove(key);
                    }
                    throw th;
                }
            } catch (IOException e4) {
                e2222 = e4;
                e2222.printStackTrace();
                if (in != null) {
                    in.close();
                }
                if (null != null) {
                    remove(key);
                }
                return str;
            }
        }
        return str;
    }

    public void put(String key, JSONObject value) {
        put(key, value.toString());
    }

    public void put(String key, JSONObject value, int saveTime) {
        put(key, value.toString(), saveTime);
    }

    public JSONObject getAsJSONObject(String key) {
        try {
            return new JSONObject(getAsString(key));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void put(String key, JSONArray value) {
        put(key, value.toString());
    }

    public void put(String key, JSONArray value, int saveTime) {
        put(key, value.toString(), saveTime);
    }

    public JSONArray getAsJSONArray(String key) {
        try {
            return new JSONArray(getAsString(key));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void put(String key, byte[] value) {
        Exception e;
        Throwable th;
        File file = this.mCache.newFile(key);
        FileOutputStream out = null;
        try {
            FileOutputStream out2 = new FileOutputStream(file);
            try {
                out2.write(value);
                if (out2 != null) {
                    try {
                        out2.flush();
                        out2.close();
                    } catch (IOException e2) {
                        e2.printStackTrace();
                    }
                }
                this.mCache.put(file);
                out = out2;
            } catch (Exception e3) {
                e = e3;
                out = out2;
                try {
                    e.printStackTrace();
                    if (out != null) {
                        try {
                            out.flush();
                            out.close();
                        } catch (IOException e22) {
                            e22.printStackTrace();
                        }
                    }
                    this.mCache.put(file);
                } catch (Throwable th2) {
                    th = th2;
                    if (out != null) {
                        try {
                            out.flush();
                            out.close();
                        } catch (IOException e222) {
                            e222.printStackTrace();
                        }
                    }
                    this.mCache.put(file);
                    throw th;
                }
            } catch (Throwable th3) {
                th = th3;
                out = out2;
                if (out != null) {
                    out.flush();
                    out.close();
                }
                this.mCache.put(file);
                throw th;
            }
        } catch (Exception e4) {
            e = e4;
            e.printStackTrace();
            if (out != null) {
                out.flush();
                out.close();
            }
            this.mCache.put(file);
        }
    }

    public void put(String key, byte[] value, int saveTime) {
        put(key, Utils.newByteArrayWithDateInfo(saveTime, value));
    }

    public byte[] getAsBinary(String key) {
        Exception e;
        Throwable th;
        byte[] bArr = null;
        RandomAccessFile RAFile = null;
        try {
            File file = this.mCache.get(key);
            if (file.exists()) {
                RandomAccessFile RAFile2 = new RandomAccessFile(file, "r");
                try {
                    byte[] byteArray = new byte[((int) RAFile2.length())];
                    RAFile2.read(byteArray);
                    if (Utils.isDue(byteArray)) {
                        if (RAFile2 != null) {
                            try {
                                RAFile2.close();
                            } catch (IOException e2) {
                                e2.printStackTrace();
                            }
                        }
                        if (true) {
                            remove(key);
                        }
                        RAFile = RAFile2;
                    } else {
                        bArr = Utils.clearDateInfo(byteArray);
                        if (RAFile2 != null) {
                            try {
                                RAFile2.close();
                            } catch (IOException e22) {
                                e22.printStackTrace();
                            }
                        }
                        if (null != null) {
                            remove(key);
                        }
                        RAFile = RAFile2;
                    }
                } catch (Exception e3) {
                    e = e3;
                    RAFile = RAFile2;
                    try {
                        e.printStackTrace();
                        if (RAFile != null) {
                            try {
                                RAFile.close();
                            } catch (IOException e222) {
                                e222.printStackTrace();
                            }
                        }
                        if (null != null) {
                            remove(key);
                        }
                        return bArr;
                    } catch (Throwable th2) {
                        th = th2;
                        if (RAFile != null) {
                            try {
                                RAFile.close();
                            } catch (IOException e2222) {
                                e2222.printStackTrace();
                            }
                        }
                        if (null != null) {
                            remove(key);
                        }
                        throw th;
                    }
                } catch (Throwable th3) {
                    th = th3;
                    RAFile = RAFile2;
                    if (RAFile != null) {
                        RAFile.close();
                    }
                    if (null != null) {
                        remove(key);
                    }
                    throw th;
                }
            }
            if (RAFile != null) {
                try {
                    RAFile.close();
                } catch (IOException e22222) {
                    e22222.printStackTrace();
                }
            }
            if (null != null) {
                remove(key);
            }
        } catch (Exception e4) {
            e = e4;
            e.printStackTrace();
            if (RAFile != null) {
                RAFile.close();
            }
            if (null != null) {
                remove(key);
            }
            return bArr;
        }
        return bArr;
    }

    public void put(String key, Serializable value) {
        put(key, value, -1);
    }

    public void put(String key, Serializable value, int saveTime) {
        Exception e;
        Throwable th;
        ObjectOutputStream oos = null;
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ByteArrayOutputStream byteArrayOutputStream;
            try {
                ObjectOutputStream oos2 = new ObjectOutputStream(baos);
                try {
                    oos2.writeObject(value);
                    byte[] data = baos.toByteArray();
                    if (saveTime != -1) {
                        put(key, data, saveTime);
                    } else {
                        put(key, data);
                    }
                    try {
                        oos2.close();
                        oos = oos2;
                        byteArrayOutputStream = baos;
                    } catch (IOException e2) {
                        oos = oos2;
                        byteArrayOutputStream = baos;
                    }
                } catch (Exception e3) {
                    e = e3;
                    oos = oos2;
                    byteArrayOutputStream = baos;
                    try {
                        e.printStackTrace();
                        try {
                            oos.close();
                        } catch (IOException e4) {
                        }
                    } catch (Throwable th2) {
                        th = th2;
                        try {
                            oos.close();
                        } catch (IOException e5) {
                        }
                        throw th;
                    }
                } catch (Throwable th3) {
                    th = th3;
                    oos = oos2;
                    byteArrayOutputStream = baos;
                    oos.close();
                    throw th;
                }
            } catch (Exception e6) {
                e = e6;
                byteArrayOutputStream = baos;
                e.printStackTrace();
                oos.close();
            } catch (Throwable th4) {
                th = th4;
                byteArrayOutputStream = baos;
                oos.close();
                throw th;
            }
        } catch (Exception e7) {
            e = e7;
            e.printStackTrace();
            oos.close();
        }
    }

    public Object getAsObject(String key) {
        ObjectInputStream ois;
        Exception e;
        Throwable th;
        Object reObject = null;
        byte[] data = getAsBinary(key);
        if (data != null) {
            ByteArrayInputStream bais = null;
            ObjectInputStream ois2 = null;
            try {
                ByteArrayInputStream bais2 = new ByteArrayInputStream(data);
                try {
                    ois = new ObjectInputStream(bais2);
                } catch (Exception e2) {
                    e = e2;
                    bais = bais2;
                    try {
                        e.printStackTrace();
                        if (bais != null) {
                            try {
                                bais.close();
                            } catch (IOException e3) {
                                e3.printStackTrace();
                            }
                        }
                        if (ois2 != null) {
                            try {
                                ois2.close();
                            } catch (IOException e32) {
                                e32.printStackTrace();
                            }
                        }
                        return reObject;
                    } catch (Throwable th2) {
                        th = th2;
                        if (bais != null) {
                            try {
                                bais.close();
                            } catch (IOException e322) {
                                e322.printStackTrace();
                            }
                        }
                        if (ois2 != null) {
                            try {
                                ois2.close();
                            } catch (IOException e3222) {
                                e3222.printStackTrace();
                            }
                        }
                        throw th;
                    }
                } catch (Throwable th3) {
                    th = th3;
                    bais = bais2;
                    if (bais != null) {
                        bais.close();
                    }
                    if (ois2 != null) {
                        ois2.close();
                    }
                    throw th;
                }
                try {
                    reObject = ois.readObject();
                    if (bais2 != null) {
                        try {
                            bais2.close();
                        } catch (IOException e32222) {
                            e32222.printStackTrace();
                        }
                    }
                    if (ois != null) {
                        try {
                            ois.close();
                        } catch (IOException e322222) {
                            e322222.printStackTrace();
                        }
                    }
                } catch (Exception e4) {
                    e = e4;
                    ois2 = ois;
                    bais = bais2;
                    e.printStackTrace();
                    if (bais != null) {
                        bais.close();
                    }
                    if (ois2 != null) {
                        ois2.close();
                    }
                    return reObject;
                } catch (Throwable th4) {
                    th = th4;
                    ois2 = ois;
                    bais = bais2;
                    if (bais != null) {
                        bais.close();
                    }
                    if (ois2 != null) {
                        ois2.close();
                    }
                    throw th;
                }
            } catch (Exception e5) {
                e = e5;
                e.printStackTrace();
                if (bais != null) {
                    bais.close();
                }
                if (ois2 != null) {
                    ois2.close();
                }
                return reObject;
            }
        }
        return reObject;
    }

    public void put(String key, Bitmap value) {
        put(key, Utils.Bitmap2Bytes(value));
    }

    public void put(String key, Bitmap value, int saveTime) {
        put(key, Utils.Bitmap2Bytes(value), saveTime);
    }

    public Bitmap getAsBitmap(String key) {
        if (getAsBinary(key) == null) {
            return null;
        }
        return Utils.Bytes2Bimap(getAsBinary(key));
    }

    public void put(String key, Drawable value) {
        put(key, Utils.drawable2Bitmap(value));
    }

    public void put(String key, Drawable value, int saveTime) {
        put(key, Utils.drawable2Bitmap(value), saveTime);
    }

    public Drawable getAsDrawable(String key) {
        if (getAsBinary(key) == null) {
            return null;
        }
        return Utils.bitmap2Drawable(Utils.Bytes2Bimap(getAsBinary(key)));
    }

    public File file(String key) {
        File f = this.mCache.newFile(key);
        return f.exists() ? f : null;
    }

    public boolean remove(String key) {
        return this.mCache.remove(key);
    }

    public void clear() {
        this.mCache.clear();
    }
}
