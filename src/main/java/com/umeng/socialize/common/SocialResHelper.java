package com.umeng.socialize.common;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Environment;
import android.text.TextUtils;
import android.view.animation.Animation;
import android.widget.ImageView;

import com.umeng.socialize.net.utils.AesHelper;
import com.umeng.socialize.net.utils.UResponse.STATUS;
import com.umeng.socialize.utils.DeviceConfig;
import com.umeng.socialize.utils.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Date;
import java.util.Stack;

public class SocialResHelper {
    private static final String  CACHE_PATH          = "/download/.um/";
    private static final long    EXTERNAL_CACHE_SIZE = 104857600;
    private static final long    INTERNAL_CACHE_SIZE = 10485760;
    public static        boolean RESUTIL_V2_DUBUG    = false;
    private static final String  TAG                 = SocialResHelper.class.getName();

    public interface BindDrawableListener {
        void onEnd(STATUS status, File file, Drawable drawable);

        void onFetchStart(FetchLocale fetchLocale);

        void onStart(LoadMode loadMode);
    }

    public static class Builder {
        boolean isBackground = false;
        Animation            mBindAnimation;
        BindDrawableListener mBindListener;
        Context              mContext;
        int mDefaultRid = -1;
        ImageView mImageView;
        LoadMode mLoadMode         = LoadMode.LOAD_CACHE_ELSE_NETWORK;
        boolean  mTransRoundCorner = false;
        String mUrl;

        public Builder(Context context, ImageView imageView, String str) {
            if (context != null && imageView != null && !TextUtils.isEmpty(str)) {
                this.mContext = context;
                this.mImageView = imageView;
                this.mUrl = str;
            }
        }

        public Builder setBindListener(BindDrawableListener bindDrawableListener) {
            this.mBindListener = bindDrawableListener;
            return this;
        }

        public Builder setBindAnimation(Animation animation) {
            this.mBindAnimation = animation;
            return this;
        }

        public Builder setRoundCorner(boolean z) {
            this.mTransRoundCorner = z;
            return this;
        }

        public Builder setBindBackground(boolean z) {
            this.isBackground = z;
            return this;
        }

        public Builder setDefaultImg(int i) {
            this.mDefaultRid = i;
            return this;
        }

        public Builder setLoadMode(LoadMode loadMode) {
            this.mLoadMode = loadMode;
            return this;
        }

        public void doBindTask() {
            File cachedFile;
            try {
                cachedFile = SocialResHelper.getCachedFile(this.mContext, this.mUrl);
            } catch (Exception e) {
                Log.e(SocialResHelper.TAG, "can't get from cache.", e);
                if (this.mBindListener != null) {
                    this.mBindListener.onEnd(STATUS.FAIL, null, null);
                }
                cachedFile = null;
            }
            Drawable createFromPath;
            switch (this.mLoadMode) {
                case LOAD_CACHE_ONLY:
                    if (this.mBindListener != null) {
                        this.mBindListener.onStart(LoadMode.LOAD_CACHE_ONLY);
                        this.mBindListener.onFetchStart(FetchLocale.FETCH_FROM_LOCALE_CACHE);
                    }
                    if (cachedFile == null || !cachedFile.exists()) {
                        Log.e(SocialResHelper.TAG, "cache is not exists");
                        return;
                    }
                    createFromPath = Drawable.createFromPath(cachedFile.getAbsolutePath());
                    if (createFromPath == null) {
                        cachedFile.delete();
                    }
                    doBind(this.mContext, this.mImageView, createFromPath, this.isBackground,
                            this.mBindListener, this.mBindAnimation, this.mTransRoundCorner, this
                                    .mDefaultRid);
                    return;
                case LOAD_CACHE_ELSE_NETWORK:
                    if (this.mBindListener != null) {
                        this.mBindListener.onStart(LoadMode.LOAD_CACHE_ELSE_NETWORK);
                        this.mBindListener.onFetchStart(FetchLocale.FETCH_FROM_LOCALE_CACHE);
                    }
                    if (cachedFile == null || !cachedFile.exists()) {
                        fetchNetElsCache(null);
                        return;
                    }
                    createFromPath = Drawable.createFromPath(cachedFile.getAbsolutePath());
                    if (createFromPath == null) {
                        cachedFile.delete();
                    }
                    doBind(this.mContext, this.mImageView, createFromPath, this.isBackground,
                            this.mBindListener, this.mBindAnimation, this.mTransRoundCorner, this
                                    .mDefaultRid);
                    return;
                case LOAD_NETWORK_ELSE_CACHE:
                    if (this.mBindListener != null) {
                        this.mBindListener.onStart(LoadMode.LOAD_NETWORK_ELSE_CACHE);
                    }
                    fetchNetElsCache(null);
                    return;
                default:
                    return;
            }
        }

        private void fetchNetElsCache(Drawable drawable) {
            if (drawable == null) {
                new AsyncTask<Object, Integer, Drawable>() {
                    protected void onPostExecute(Drawable drawable) {
                        Builder.this.doBind(Builder.this.mContext, Builder.this.mImageView,
                                drawable, Builder.this.isBackground, Builder.this.mBindListener,
                                Builder.this.mBindAnimation, Builder.this.mTransRoundCorner,
                                Builder.this.mDefaultRid);
                    }

                    protected void onProgressUpdate(Integer... numArr) {
                        super.onProgressUpdate(numArr);
                        if (numArr != null && numArr.length >= 1) {
                            switch (numArr[0].intValue()) {
                                case 0:
                                    if (Builder.this.mBindListener != null) {
                                        Builder.this.mBindListener.onFetchStart(FetchLocale
                                                .FETCH_FROM_NETWORK);
                                        return;
                                    }
                                    return;
                                case 1:
                                    if (Builder.this.mBindListener != null) {
                                        Builder.this.mBindListener.onFetchStart(FetchLocale
                                                .FETCH_FROM_LOCALE_CACHE);
                                        return;
                                    }
                                    return;
                                default:
                                    return;
                            }
                        }
                    }

                    protected Drawable doInBackground(Object... objArr) {
                        try {
                            if (SocialResHelper.RESUTIL_V2_DUBUG) {
                                Thread.sleep(3000);
                            }
                        } catch (InterruptedException e) {
                        }
                        publishProgress(new Integer[]{Integer.valueOf(0)});
                        SocialResHelper.getResource(Builder.this.mContext, Builder.this.mUrl);
                        Drawable drawable = null;
                        try {
                            publishProgress(new Integer[]{Integer.valueOf(1)});
                            File cachedFile = SocialResHelper.getCachedFile(Builder.this
                                    .mContext, Builder.this.mUrl);
                            if (cachedFile != null && cachedFile.exists()) {
                                drawable = Drawable.createFromPath(cachedFile.getAbsolutePath());
                                if (drawable == null) {
                                    cachedFile.delete();
                                }
                            }
                        } catch (IOException e2) {
                            Log.w(SocialResHelper.TAG, e2.toString());
                        }
                        return drawable;
                    }
                }.execute(new Object[0]);
                return;
            }
            doBind(this.mContext, this.mImageView, drawable, this.isBackground, this
                    .mBindListener, this.mBindAnimation, this.mTransRoundCorner, this.mDefaultRid);
        }

        private void doBind(Context context, ImageView imageView, Drawable drawable, boolean z,
                            BindDrawableListener bindDrawableListener, Animation animation,
                            boolean z2, int i) {
            if (drawable == null || imageView == null) {
                if (imageView != null && i > 0) {
                    imageView.setImageResource(i);
                }
                if (bindDrawableListener != null) {
                    bindDrawableListener.onEnd(STATUS.FAIL, null, drawable);
                }
                Log.w(SocialResHelper.TAG, "bind drawable failed. drawable [" + drawable + "]  " +
                        "imageView[+" + imageView + "+]");
                return;
            }
            File cachedFile;
            if (z2) {
                drawable = new BitmapDrawable(context.getResources(), SocialResHelper
                        .getRoundedCornerBitmap(((BitmapDrawable) drawable).getBitmap()));
            }
            if (z) {
                imageView.setBackground(drawable);
            } else {
                imageView.setImageDrawable(drawable);
            }
            if (animation != null) {
                imageView.startAnimation(animation);
            }
            try {
                cachedFile = SocialResHelper.getCachedFile(this.mContext, this.mUrl);
            } catch (IOException e) {
                e.printStackTrace();
                cachedFile = null;
            }
            if (bindDrawableListener != null) {
                bindDrawableListener.onEnd(STATUS.SUCCESS, cachedFile, drawable);
            }
        }
    }

    public enum FetchLocale {
        FETCH_FROM_LOCALE_CACHE,
        FETCH_FROM_NETWORK
    }

    public enum LoadMode {
        LOAD_CACHE_ELSE_NETWORK,
        LOAD_CACHE_ONLY,
        LOAD_NETWORK_ELSE_CACHE
    }

    private static String getMd5FileName(String str) {
        return AesHelper.md5(str);
    }

    public static String getResource(Context context, String str) {
        File file;
        Exception e;
        String str2 = null;
        if (!TextUtils.isEmpty(str)) {
            try {
                String canonicalPath;
                long j;
                String str3 = getMd5FileName(str) + ".tmp";
                if (DeviceConfig.isSdCardWrittenable()) {
                    canonicalPath = Environment.getExternalStorageDirectory().getCanonicalPath();
                    j = EXTERNAL_CACHE_SIZE;
                } else {
                    canonicalPath = context.getCacheDir().getCanonicalPath();
                    j = INTERNAL_CACHE_SIZE;
                }
                File file2 = new File(canonicalPath + CACHE_PATH);
                if (file2.exists()) {
                    if (dirSize(file2.getCanonicalFile()) > j) {
                        cleanDir(file2);
                    }
                } else if (!file2.mkdirs()) {
                    Log.e(TAG, "Failed to create directory" + file2.getAbsolutePath() + ". Check " +
                            "permission. Make sure WRITE_EXTERNAL_STORAGE is added in your " +
                            "Manifest.xml");
                }
                file = new File(file2, str3);
                try {
                    file.createNewFile();
                    persistenceUrlData(str, file);
                    File file3 = new File(file.getParent(), file.getName().replace(".tmp", ""));
                    file.renameTo(file3);
                    Log.i(TAG, "download img[" + str + "]  to " + file3.getCanonicalPath());
                    str2 = file3.getCanonicalPath();
                } catch (Exception e2) {
                    e = e2;
                    Log.i(TAG, e.getStackTrace().toString() + "\t url:\t" + str);
                    if (file != null && file.exists()) {
                        file.deleteOnExit();
                    }
                    return str2;
                }
            } catch (Exception e3) {
                e = e3;
                file = str2;
                Log.i(TAG, e.getStackTrace().toString() + "\t url:\t" + str);
                file.deleteOnExit();
                return str2;
            }
        }
        return str2;
    }

    private static void persistenceUrlData(String str, File file) {
        Throwable th;
        Throwable th2;
        InputStream inputStream = null;
        FileOutputStream fileOutputStream;
        try {
            fileOutputStream = new FileOutputStream(file);
            try {
                InputStream inputStream2 = (InputStream) new URL(str).openConnection().getContent();
                try {
                    byte[] bArr = new byte[4096];
                    while (true) {
                        int read = inputStream2.read(bArr);
                        if (read == -1) {
                            break;
                        }
                        fileOutputStream.write(bArr, 0, read);
                    }
                    fileOutputStream.flush();
                    if (inputStream2 != null) {
                        try {
                            inputStream2.close();
                            if (fileOutputStream != null) {
                                try {
                                    fileOutputStream.close();
                                } catch (IOException e) {
                                    Log.d(TAG, e.getMessage());
                                }
                            }
                        } catch (IOException e2) {
                            Log.d(TAG, e2.getMessage());
                            if (fileOutputStream != null) {
                                try {
                                    fileOutputStream.close();
                                } catch (IOException e22) {
                                    Log.d(TAG, e22.getMessage());
                                }
                            }
                        } catch (Throwable th3) {
                            if (fileOutputStream != null) {
                                try {
                                    fileOutputStream.close();
                                } catch (IOException e3) {
                                    Log.d(TAG, e3.getMessage());
                                }
                            }
                        }
                    }
                } catch (Throwable e4) {
                    th = e4;
                    inputStream = inputStream2;
                    th2 = th;
                    try {
                        throw new RuntimeException(th2);
                    } catch (Throwable th4) {
                        th2 = th4;
                        if (inputStream != null) {
                            try {
                                inputStream.close();
                                if (fileOutputStream != null) {
                                    try {
                                        fileOutputStream.close();
                                    } catch (IOException e32) {
                                        Log.d(TAG, e32.getMessage());
                                    }
                                }
                            } catch (IOException e322) {
                                Log.d(TAG, e322.getMessage());
                                if (fileOutputStream != null) {
                                    try {
                                        fileOutputStream.close();
                                    } catch (IOException e3222) {
                                        Log.d(TAG, e3222.getMessage());
                                    }
                                }
                            } catch (Throwable th5) {
                                if (fileOutputStream != null) {
                                    try {
                                        fileOutputStream.close();
                                    } catch (IOException e32222) {
                                        Log.d(TAG, e32222.getMessage());
                                    }
                                }
                            }
                        }
                        throw th2;
                    }
                } catch (Throwable e42) {
                    th = e42;
                    inputStream = inputStream2;
                    th2 = th;
                    if (inputStream != null) {
                        inputStream.close();
                        if (fileOutputStream != null) {
                            fileOutputStream.close();
                        }
                    }
                    throw th2;
                }
            } catch (Exception e5) {
                th2 = e5;
                throw new RuntimeException(th2);
            }
        } catch (Exception e6) {
            th2 = e6;
            fileOutputStream = null;
            throw new RuntimeException(th2);
        } catch (Throwable th6) {
            th2 = th6;
            fileOutputStream = null;
            if (inputStream != null) {
                inputStream.close();
                if (fileOutputStream != null) {
                    fileOutputStream.close();
                }
            }
            throw th2;
        }
    }

    private static long dirSize(File file) {
        if (file == null || !file.exists() || !file.isDirectory()) {
            return 0;
        }
        Stack stack = new Stack();
        stack.clear();
        stack.push(file);
        long j = 0;
        while (!stack.isEmpty()) {
            File[] listFiles = ((File) stack.pop()).listFiles();
            long j2 = j;
            int i = 0;
            while (i < listFiles.length) {
                long j3;
                if (listFiles[i].isDirectory()) {
                    stack.push(listFiles[i]);
                    j3 = j2;
                } else {
                    j3 = listFiles[i].length() + j2;
                }
                i++;
                j2 = j3;
            }
            j = j2;
        }
        return j;
    }

    private static void cleanDir(File file) {
        if (file != null && file.exists() && file.canWrite() && file.isDirectory()) {
            File[] listFiles = file.listFiles();
            for (int i = 0; i < listFiles.length; i++) {
                if (listFiles[i].isDirectory()) {
                    cleanDir(listFiles[i]);
                } else if (new Date().getTime() - listFiles[i].lastModified() > 1800) {
                    listFiles[i].delete();
                }
            }
        }
    }

    protected static File getCachedFile(Context context, String str) throws IOException {
        String canonicalPath;
        String md5FileName = getMd5FileName(str);
        if (DeviceConfig.isSdCardWrittenable()) {
            canonicalPath = Environment.getExternalStorageDirectory().getCanonicalPath();
        } else {
            canonicalPath = context.getCacheDir().getCanonicalPath();
        }
        File file = new File(new File(canonicalPath + CACHE_PATH), md5FileName);
        return file.exists() ? file : null;
    }

    private static Bitmap getRoundedCornerBitmap(Bitmap bitmap) {
        Bitmap createBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config
                .ARGB_8888);
        Canvas canvas = new Canvas(createBitmap);
        Paint paint = new Paint();
        Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        RectF rectF = new RectF(rect);
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(-12434878);
        if (RESUTIL_V2_DUBUG) {
            canvas.drawRoundRect(rectF, (float) (bitmap.getWidth() / 2), (float) (bitmap
                    .getHeight() / 2), paint);
        } else {
            canvas.drawRoundRect(rectF, (float) (bitmap.getWidth() / 6), (float) (bitmap
                    .getHeight() / 6), paint);
        }
        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        bitmap.recycle();
        return createBitmap;
    }
}
