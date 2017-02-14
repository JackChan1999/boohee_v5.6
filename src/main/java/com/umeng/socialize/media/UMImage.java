package com.umeng.socialize.media;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Looper;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.text.TextUtils;

import com.tencent.tinker.android.dx.instruction.Opcodes;
import com.umeng.socialize.common.ImageFormat;
import com.umeng.socialize.media.UMediaObject.FetchMediaDataListener;
import com.umeng.socialize.media.UMediaObject.MediaType;
import com.umeng.socialize.net.utils.AesHelper;
import com.umeng.socialize.net.utils.SocializeNetUtils;
import com.umeng.socialize.net.utils.SocializeProtocolConstants;
import com.umeng.socialize.utils.BitmapUtils;
import com.umeng.socialize.utils.DeviceConfig;
import com.umeng.socialize.utils.Log;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class UMImage extends BaseMediaObject {
    public static final  Creator<UMImage> CREATOR    = new Creator<UMImage>() {
        public UMImage createFromParcel(Parcel parcel) {
            return new UMImage(parcel);
        }

        public UMImage[] newArray(int i) {
            return new UMImage[i];
        }
    };
    public static        int              MAX_HEIGHT = 1024;
    public static        int              MAX_WIDTH  = Opcodes.FILL_ARRAY_DATA_PAYLOAD;
    private static final String           h          = UMImage.class.getName();
    private static final String           i          = "/umeng_cache/";
    private File j;
    private String                k = "";
    private boolean               l = false;
    private SoftReference<byte[]> m = null;
    private float                 n = 2048.0f;
    private Lock                  o = new ReentrantLock();
    private Condition             p = this.o.newCondition();

    public UMImage(Context context, File file) {
        super("");
        a(context);
        if (file != null && file.exists()) {
            String absolutePath = file.getAbsolutePath();
            if (absolutePath != null && absolutePath.startsWith("/data/data")) {
                a(BitmapFactory.decodeFile(absolutePath));
                return;
            }
        }
        a((Object) file);
    }

    public UMImage(Context context, String str) {
        super(str);
        a(context);
        if (TextUtils.isEmpty(str) || SocializeNetUtils.startWithHttp(str)) {
            a((Object) str);
            return;
        }
        setMediaUrl(null);
        a(new File(str));
    }

    public UMImage(Context context, int i) {
        super("");
        a(context);
        try {
            a(context.getResources().openRawResourceFd(i).createInputStream());
        } catch (Exception e) {
            e.printStackTrace();
            b();
        }
    }

    public UMImage(Context context, byte[] bArr) {
        super("");
        a(context);
        a((Object) bArr);
    }

    public UMImage(Context context, Bitmap bitmap) {
        super("");
        a(context);
        a((Object) bitmap);
    }

    protected void a(Context context) {
        try {
            this.k = context.getCacheDir().getCanonicalPath();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void b() {
        this.l = true;
        c();
    }

    private void c() {
        this.o.lock();
        this.p.signal();
        this.o.unlock();
        Log.d(h, "*********  UMImage序列化完成");
    }

    protected void a(Object obj) {
        this.l = false;
        if (obj instanceof Bitmap) {
            a((Bitmap) obj);
        } else if (obj instanceof byte[]) {
            a((byte[]) obj);
        } else if (obj instanceof File) {
            File file = (File) obj;
            if (file == null || !file.exists()) {
                Log.e(h, "the image file is no exist..");
            }
            this.j = file;
            b();
        } else if (obj instanceof BitmapDrawable) {
            try {
                a(((BitmapDrawable) obj).getBitmap());
            } catch (Exception e) {
                Log.e(h, "Sorry cannot setImage..[" + e.toString() + "]");
            }
        } else if (obj instanceof InputStream) {
            a((InputStream) obj);
        } else if (obj instanceof String) {
            this.l = true;
        }
    }

    private void a(final InputStream inputStream) {
        if (inputStream != null) {
            new Thread(this) {
                final /* synthetic */ UMImage b;

                public void run() {
                    IOException e;
                    Throwable th;
                    FileOutputStream fileOutputStream;
                    try {
                        File a = this.b.a(AesHelper.md5(inputStream.toString()));
                        fileOutputStream = new FileOutputStream(a);
                        try {
                            byte[] bArr = new byte[4096];
                            while (inputStream.read(bArr) != -1) {
                                fileOutputStream.write(bArr);
                            }
                            fileOutputStream.flush();
                            Log.v("10.13", "cacheFile=");
                            if (a != null) {
                                this.b.j = a;
                            }
                            this.b.b();
                            try {
                                if (inputStream != null) {
                                    inputStream.close();
                                }
                                if (fileOutputStream != null) {
                                    fileOutputStream.close();
                                }
                            } catch (IOException e2) {
                                e2.printStackTrace();
                            }
                        } catch (IOException e3) {
                            e2 = e3;
                            try {
                                e2.printStackTrace();
                                this.b.b();
                                try {
                                    if (inputStream != null) {
                                        inputStream.close();
                                    }
                                    if (fileOutputStream != null) {
                                        fileOutputStream.close();
                                    }
                                } catch (IOException e22) {
                                    e22.printStackTrace();
                                }
                            } catch (Throwable th2) {
                                th = th2;
                                this.b.b();
                                try {
                                    if (inputStream != null) {
                                        inputStream.close();
                                    }
                                    if (fileOutputStream != null) {
                                        fileOutputStream.close();
                                    }
                                } catch (IOException e4) {
                                    e4.printStackTrace();
                                }
                                throw th;
                            }
                        }
                    } catch (IOException e5) {
                        e22 = e5;
                        fileOutputStream = null;
                        e22.printStackTrace();
                        this.b.b();
                        if (inputStream != null) {
                            inputStream.close();
                        }
                        if (fileOutputStream != null) {
                            fileOutputStream.close();
                        }
                    } catch (Throwable th3) {
                        th = th3;
                        fileOutputStream = null;
                        this.b.b();
                        if (inputStream != null) {
                            inputStream.close();
                        }
                        if (fileOutputStream != null) {
                            fileOutputStream.close();
                        }
                        throw th;
                    }
                }
            }.start();
        }
    }

    private void a(final byte[] bArr) {
        new Thread(new Runnable(this) {
            final /* synthetic */ UMImage b;

            public void run() {
                try {
                    this.b.j = UMImage.b(bArr, this.b.a(this.b.getFileName()));
                } catch (IOException e) {
                    Log.e(UMImage.h, "Sorry cannot setImage..[" + e.toString() + "]");
                } finally {
                    this.b.b();
                }
            }
        }).start();
    }

    public void asFile() throws IOException {
        if (this.j == null) {
            this.j = a("bitmap");
        }
    }

    private void a(final Bitmap bitmap) {
        if (bitmap != null) {
            new Thread(new Runnable(this) {
                final /* synthetic */ UMImage b;

                public void run() {
                    OutputStream fileOutputStream;
                    Exception e;
                    Throwable th;
                    try {
                        long currentTimeMillis = System.currentTimeMillis();
                        File a = this.b.a(AesHelper.md5(bitmap.toString()));
                        fileOutputStream = new FileOutputStream(a);
                        try {
                            int rowBytes = (bitmap.getRowBytes() * bitmap.getHeight()) / 1024;
                            Log.d(UMImage.h, "### bitmap size = " + rowBytes + " KB");
                            int i = 100;
                            if (((float) rowBytes) > this.b.n) {
                                i = (int) (((float) 100) * (this.b.n / ((float) rowBytes)));
                            }
                            Log.d(UMImage.h, "### 压缩质量 : " + i);
                            if (!bitmap.isRecycled()) {
                                bitmap.compress(CompressFormat.JPEG, i, fileOutputStream);
                            }
                            this.b.j = a;
                            Log.d(UMImage.h, "##save bitmap " + a.getAbsolutePath());
                            Log.d(UMImage.h, "#### 图片序列化耗时 : " + (System.currentTimeMillis() -
                                    currentTimeMillis) + " ms.");
                            this.b.b();
                            if (fileOutputStream != null) {
                                try {
                                    fileOutputStream.close();
                                } catch (IOException e2) {
                                }
                            }
                        } catch (Exception e3) {
                            e = e3;
                            try {
                                Log.e(UMImage.h, "Sorry cannot setImage..[" + e.toString() + "]");
                                this.b.b();
                                if (fileOutputStream != null) {
                                    try {
                                        fileOutputStream.close();
                                    } catch (IOException e4) {
                                    }
                                }
                            } catch (Throwable th2) {
                                th = th2;
                                this.b.b();
                                if (fileOutputStream != null) {
                                    try {
                                        fileOutputStream.close();
                                    } catch (IOException e5) {
                                    }
                                }
                                throw th;
                            }
                        }
                    } catch (Exception e6) {
                        e = e6;
                        fileOutputStream = null;
                        Log.e(UMImage.h, "Sorry cannot setImage..[" + e.toString() + "]");
                        this.b.b();
                        if (fileOutputStream != null) {
                            fileOutputStream.close();
                        }
                    } catch (Throwable th3) {
                        th = th3;
                        fileOutputStream = null;
                        this.b.b();
                        if (fileOutputStream != null) {
                            fileOutputStream.close();
                        }
                        throw th;
                    }
                }
            }).start();
        }
    }

    private File a(String str) throws IOException {
        BitmapUtils.cleanCache();
        File file = new File(getCache(), str.substring(0, 5));
        if (file.exists()) {
            file.delete();
        }
        Log.v("xxxx name = " + getCache());
        file.createNewFile();
        return file;
    }

    public File getCacheFileObj() {
        return this.j;
    }

    public void setCacheFileObj(File file) {
        this.j = file;
    }

    public File getCache() throws IOException {
        String canonicalPath;
        if (DeviceConfig.isSdCardWrittenable()) {
            canonicalPath = Environment.getExternalStorageDirectory().getCanonicalPath();
        } else if (TextUtils.isEmpty(this.k)) {
            throw new IOException("dirpath is unknow");
        } else {
            canonicalPath = this.k;
        }
        File file = new File(canonicalPath + i);
        if (!(file == null || file.exists())) {
            file.mkdirs();
        }
        return file;
    }

    public void toByte(final FetchMediaDataListener fetchMediaDataListener) {
        if (fetchMediaDataListener != null) {
            fetchMediaDataListener.onStart();
            if (Looper.myLooper() == Looper.getMainLooper()) {
                new AsyncTask<Void, Void, byte[]>(this) {
                    final /* synthetic */ UMImage b;

                    protected /* synthetic */ Object doInBackground(Object[] objArr) {
                        return a((Void[]) objArr);
                    }

                    protected /* synthetic */ void onPostExecute(Object obj) {
                        a((byte[]) obj);
                    }

                    protected byte[] a(Void... voidArr) {
                        return this.b.d();
                    }

                    protected void a(byte[] bArr) {
                        if (fetchMediaDataListener != null) {
                            fetchMediaDataListener.onComplete(bArr);
                        }
                    }
                }.execute(new Void[0]);
            } else {
                fetchMediaDataListener.onComplete(d());
            }
        }
    }

    public byte[] toByte() {
        return d();
    }

    private byte[] d() {
        int i = 0;
        if (this.m == null || this.m.get() == null || ((byte[]) this.m.get()).length <= 0) {
            byte[] bArr = new byte[0];
            if (isUrlMedia()) {
                try {
                    String toUrl = toUrl();
                    if (TextUtils.isEmpty(toUrl)) {
                        return bArr;
                    }
                    if (!toUrl.endsWith(".png") && !toUrl.endsWith("jpeg") && !toUrl.endsWith
                            ("jpg") && !toUrl.endsWith("gif")) {
                        return bArr;
                    }
                    Bitmap loadImage = BitmapUtils.loadImage(toUrl, 150, 150);
                    if (loadImage != null) {
                        bArr = BitmapUtils.bitmap2Bytes(loadImage);
                    } else {
                        bArr = SocializeNetUtils.getNetData(toUrl);
                        BitmapUtils.saveBitmap(toUrl, BitmapFactory.decodeByteArray(bArr, 0, bArr
                                .length));
                    }
                } catch (Exception e) {
                    Exception exception = e;
                    byte[] bArr2 = bArr;
                    Log.w(h, "get image data from network failed.", exception);
                    bArr = bArr2;
                }
            } else if (!this.l || this.j == null) {
                while (i < 30) {
                    if (!this.l || this.j == null) {
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e2) {
                            e2.printStackTrace();
                        }
                    } else {
                        bArr = a(this.j);
                    }
                    i++;
                }
            } else {
                bArr = a(this.j);
            }
            if (bArr == null || bArr.length <= 0) {
                return bArr;
            }
            this.m = new SoftReference(bArr);
            Log.d(h, "### 首次生成图片二进制数据");
            return bArr;
        }
        Log.d(h, "### 从缓存中获取图片数据 ");
        return (byte[]) this.m.get();
    }

    public final Map<String, Object> toUrlExtraParams() {
        Map<String, Object> hashMap = new HashMap();
        if (isUrlMedia()) {
            hashMap.put(SocializeProtocolConstants.PROTOCOL_KEY_FURL, this.a);
            hashMap.put(SocializeProtocolConstants.PROTOCOL_KEY_FTYPE, getMediaType());
        }
        return hashMap;
    }

    public String getImageCachePath() {
        if (this.j == null || !(this.j instanceof File)) {
            return "";
        }
        return this.j.getAbsolutePath();
    }

    private static File b(byte[] bArr, File file) {
        BufferedOutputStream bufferedOutputStream;
        Exception e;
        Throwable th;
        BufferedOutputStream bufferedOutputStream2 = null;
        try {
            bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(file));
            try {
                bufferedOutputStream.write(bArr);
                if (bufferedOutputStream != null) {
                    try {
                        bufferedOutputStream.close();
                    } catch (IOException e2) {
                    }
                }
            } catch (Exception e3) {
                e = e3;
                try {
                    e.printStackTrace();
                    if (bufferedOutputStream != null) {
                        try {
                            bufferedOutputStream.close();
                        } catch (IOException e4) {
                        }
                    }
                    return file;
                } catch (Throwable th2) {
                    th = th2;
                    bufferedOutputStream2 = bufferedOutputStream;
                    if (bufferedOutputStream2 != null) {
                        try {
                            bufferedOutputStream2.close();
                        } catch (IOException e5) {
                        }
                    }
                    throw th;
                }
            }
        } catch (Exception e6) {
            e = e6;
            bufferedOutputStream = null;
            e.printStackTrace();
            if (bufferedOutputStream != null) {
                bufferedOutputStream.close();
            }
            return file;
        } catch (Throwable th3) {
            th = th3;
            if (bufferedOutputStream2 != null) {
                bufferedOutputStream2.close();
            }
            throw th;
        }
        return file;
    }

    private byte[] a(File file) {
        if (file == null || !file.exists()) {
            return null;
        }
        byte[] b = b(file);
        if (b == null || b.length <= 0) {
            return null;
        }
        if (ImageFormat.FORMAT_NAMES[1].equals(ImageFormat.checkFormat(b))) {
            return b;
        }
        return b(b);
    }

    private static byte[] b(File file) {
        InputStream fileInputStream;
        ByteArrayOutputStream byteArrayOutputStream;
        Exception e;
        Throwable th;
        byte[] bArr = null;
        try {
            fileInputStream = new FileInputStream(file);
            try {
                byteArrayOutputStream = new ByteArrayOutputStream();
                try {
                    byte[] bArr2 = new byte[4096];
                    while (true) {
                        int read = fileInputStream.read(bArr2);
                        if (read == -1) {
                            break;
                        }
                        byteArrayOutputStream.write(bArr2, 0, read);
                    }
                    bArr = byteArrayOutputStream.toByteArray();
                    if (fileInputStream != null) {
                        try {
                            fileInputStream.close();
                        } catch (IOException e2) {
                        }
                    }
                    if (byteArrayOutputStream != null) {
                        byteArrayOutputStream.close();
                    }
                } catch (Exception e3) {
                    e = e3;
                    try {
                        Log.w(h, "", e);
                        if (fileInputStream != null) {
                            try {
                                fileInputStream.close();
                            } catch (IOException e4) {
                            }
                        }
                        if (byteArrayOutputStream != null) {
                            byteArrayOutputStream.close();
                        }
                        return bArr;
                    } catch (Throwable th2) {
                        th = th2;
                        if (fileInputStream != null) {
                            try {
                                fileInputStream.close();
                            } catch (IOException e5) {
                                throw th;
                            }
                        }
                        if (byteArrayOutputStream != null) {
                            byteArrayOutputStream.close();
                        }
                        throw th;
                    }
                }
            } catch (Exception e6) {
                e = e6;
                byteArrayOutputStream = bArr;
                Log.w(h, "", e);
                if (fileInputStream != null) {
                    fileInputStream.close();
                }
                if (byteArrayOutputStream != null) {
                    byteArrayOutputStream.close();
                }
                return bArr;
            } catch (Throwable th3) {
                byteArrayOutputStream = bArr;
                th = th3;
                if (fileInputStream != null) {
                    fileInputStream.close();
                }
                if (byteArrayOutputStream != null) {
                    byteArrayOutputStream.close();
                }
                throw th;
            }
        } catch (Exception e7) {
            e = e7;
            byteArrayOutputStream = bArr;
            fileInputStream = bArr;
            Log.w(h, "", e);
            if (fileInputStream != null) {
                fileInputStream.close();
            }
            if (byteArrayOutputStream != null) {
                byteArrayOutputStream.close();
            }
            return bArr;
        } catch (Throwable th32) {
            byteArrayOutputStream = bArr;
            fileInputStream = bArr;
            th = th32;
            if (fileInputStream != null) {
                fileInputStream.close();
            }
            if (byteArrayOutputStream != null) {
                byteArrayOutputStream.close();
            }
            throw th;
        }
        return bArr;
    }

    private static byte[] b(byte[] bArr) {
        ByteArrayOutputStream byteArrayOutputStream;
        Throwable th;
        byte[] bArr2 = null;
        try {
            Bitmap decodeByteArray = BitmapFactory.decodeByteArray(bArr, 0, bArr.length,
                    BitmapUtils.getBitmapOptions(bArr));
            byteArrayOutputStream = new ByteArrayOutputStream();
            if (decodeByteArray != null) {
                try {
                    decodeByteArray.compress(CompressFormat.PNG, 100, byteArrayOutputStream);
                    decodeByteArray.recycle();
                    System.gc();
                } catch (Exception e) {
                    if (byteArrayOutputStream != null) {
                        try {
                            byteArrayOutputStream.close();
                        } catch (IOException e2) {
                        }
                    }
                    return bArr2;
                } catch (Throwable th2) {
                    th = th2;
                    if (byteArrayOutputStream != null) {
                        try {
                            byteArrayOutputStream.close();
                        } catch (IOException e3) {
                        }
                    }
                    throw th;
                }
            }
            bArr2 = byteArrayOutputStream.toByteArray();
            if (byteArrayOutputStream != null) {
                try {
                    byteArrayOutputStream.close();
                } catch (IOException e4) {
                }
            }
        } catch (Exception e5) {
            byteArrayOutputStream = null;
            if (byteArrayOutputStream != null) {
                byteArrayOutputStream.close();
            }
            return bArr2;
        } catch (Throwable th3) {
            Throwable th4 = th3;
            byteArrayOutputStream = null;
            th = th4;
            if (byteArrayOutputStream != null) {
                byteArrayOutputStream.close();
            }
            throw th;
        }
        return bArr2;
    }

    public MediaType getMediaType() {
        return MediaType.IMAGE;
    }

    protected UMImage(Parcel parcel) {
        super(parcel);
        this.j = new File(parcel.readString());
        this.k = parcel.readString();
    }

    public void writeToParcel(Parcel parcel, int i) {
        super.writeToParcel(parcel, i);
        if (this.j != null) {
            parcel.writeString(this.j.getAbsolutePath());
        } else {
            parcel.writeString("");
        }
        parcel.writeString(this.k);
    }

    public String toString() {
        return "UMImage [fileObj=" + this.j + ", sandCache=" + this.k + ", isSerialized=" + this
                .l + "media_url=" + this.a + ", qzone_title=" + this.b + ", qzone_thumb=" + this
                .c + "]";
    }

    public boolean isMultiMedia() {
        return true;
    }

    public float getImageSizeLimit() {
        return this.n;
    }

    public void setImageSizeLimit(float f) {
        this.n = f;
    }

    public String getFileName() {
        return AesHelper.md5(String.valueOf(System.currentTimeMillis()));
    }

    public boolean isSerialized() {
        return this.l;
    }

    public void waitImageToSerialize() {
        try {
            this.o.lock();
            while (!this.l) {
                this.p.await(2, TimeUnit.SECONDS);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            this.o.unlock();
        }
    }

    public Object clone() throws CloneNotSupportedException {
        UMImage uMImage = (UMImage) super.clone();
        if (this.m != null) {
            uMImage.m = new SoftReference(this.m.get());
        }
        return super.clone();
    }
}
