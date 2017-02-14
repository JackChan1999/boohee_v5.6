package com.umeng.socialize.utils;

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

import com.umeng.socialize.common.SocializeConstants;
import com.umeng.socialize.net.utils.AesHelper;
import com.umeng.socialize.net.utils.UResponse.STATUS;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Collections;
import java.util.Date;
import java.util.Map;
import java.util.Stack;
import java.util.WeakHashMap;

public class ResUtil {
    private static final long                   EXTERNAL_CACHE_SIZE = 104857600;
    private static final Map<ImageView, String> IMAGE_VIEWS         = Collections.synchronizedMap
            (new WeakHashMap());
    private static final long                   INTERNAL_CACHE_SIZE = 10485760;
    public static        boolean                RESUTIL_DEBUG       = false;
    private static final String                 TAG                 = ResUtil.class.getName();

    final class AnonymousClass1 implements Runnable {
        final /* synthetic */ File val$dir;

        AnonymousClass1(File file) {
            this.val$dir = file;
        }

        public void run() {
            ResUtil.cleanDir(this.val$dir);
        }
    }

    public interface BindDrawableListener {
        void onEnd(STATUS status);

        void onStart(BindMode bindMode);
    }

    public enum BindMode {
        BIND_FORM_CACHE,
        BIND_FROM_NET
    }

    static class FetchTask extends AsyncTask<Object, Integer, Drawable> {
        private boolean              isBackground;
        private Animation            mBindAnim;
        private BindDrawableListener mBindListener;
        private File                 mCacheFile;
        private Context              mContext;
        private ImageView            mImageView;
        private BindMode             mMode;
        private String               mUrl;

        public FetchTask(Context context, ImageView imageView, String str, BindMode bindMode,
                         File file, boolean z, BindDrawableListener bindDrawableListener,
                         Animation animation) {
            this.mCacheFile = file;
            this.mContext = context;
            this.mUrl = str;
            this.mBindListener = bindDrawableListener;
            this.mMode = bindMode;
            this.isBackground = z;
            this.mBindAnim = animation;
            this.mImageView = imageView;
        }

        protected void onPreExecute() {
            super.onPreExecute();
            if (this.mBindListener != null) {
                this.mBindListener.onStart(this.mMode);
            }
        }

        protected void onPostExecute(Drawable drawable) {
            ResUtil.doBind(this.mContext, this.mImageView, drawable, this.isBackground, this
                    .mBindListener, this.mBindAnim, this.mUrl);
        }

        protected Drawable doInBackground(Object... objArr) {
            if (ResUtil.RESUTIL_DEBUG) {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            Drawable drawable;
            if (this.mCacheFile == null || !this.mCacheFile.exists()) {
                try {
                    ResUtil.getResource(this.mContext, this.mUrl);
                    File cachedFile = ResUtil.getCachedFile(this.mContext, this.mUrl);
                    if (cachedFile == null || !cachedFile.exists()) {
                        drawable = null;
                    } else {
                        drawable = ResUtil.createFromPathBuffer(cachedFile.getAbsolutePath());
                    }
                    Log.d(ResUtil.TAG, "get drawable from net else file.");
                    return drawable;
                } catch (Exception e2) {
                    Log.w(ResUtil.TAG, e2.toString(), e2);
                    return null;
                }
            }
            drawable = ResUtil.createFromPathBuffer(this.mCacheFile.getAbsolutePath());
            if (drawable == null) {
                this.mCacheFile.delete();
            }
            Log.d(ResUtil.TAG, "get drawable from cacheFile.");
            return drawable;
        }
    }

    private static String getMd5FileName(String str) {
        return AesHelper.md5(str);
    }

    public static String getResource(Context context, String str) {
        File file;
        Exception e;
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        try {
            String canonicalPath;
            long j;
            String str2 = getMd5FileName(str) + ".tmp";
            if (DeviceConfig.isSdCardWrittenable()) {
                canonicalPath = Environment.getExternalStorageDirectory().getCanonicalPath();
                j = EXTERNAL_CACHE_SIZE;
            } else {
                canonicalPath = context.getCacheDir().getCanonicalPath();
                j = INTERNAL_CACHE_SIZE;
            }
            File file2 = new File(canonicalPath + SocializeConstants.CACHE_PATH);
            if (file2.exists()) {
                if (dirSize(file2.getCanonicalFile()) > j) {
                    new Thread(new AnonymousClass1(file2)).start();
                }
            } else if (!file2.mkdirs()) {
                Log.e(TAG, "Failed to create directory" + file2.getAbsolutePath() + ". Check " +
                        "permission. Make sure WRITE_EXTERNAL_STORAGE is added in your Manifest" +
                        ".xml");
            }
            file = new File(file2, str2);
            try {
                file.createNewFile();
                FileOutputStream fileOutputStream = new FileOutputStream(file);
                InputStream inputStream = (InputStream) new URL(str).openConnection().getContent();
                byte[] bArr = new byte[4096];
                while (true) {
                    int read = inputStream.read(bArr);
                    if (read != -1) {
                        fileOutputStream.write(bArr, 0, read);
                    } else {
                        fileOutputStream.flush();
                        inputStream.close();
                        fileOutputStream.close();
                        File file3 = new File(file.getParent(), file.getName().replace(".tmp", ""));
                        file.renameTo(file3);
                        Log.i(TAG, "download img[" + str + "]  to " + file3.getCanonicalPath());
                        return file3.getCanonicalPath();
                    }
                }
            } catch (Exception e2) {
                e = e2;
                Log.i(TAG, e.getStackTrace().toString() + "\t url:\t" + str);
                file.deleteOnExit();
                return null;
            }
        } catch (Exception e3) {
            e = e3;
            file = null;
            Log.i(TAG, e.getStackTrace().toString() + "\t url:\t" + str);
            if (file != null && file.exists()) {
                file.deleteOnExit();
            }
            return null;
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
        File file = new File(new File(canonicalPath + SocializeConstants.CACHE_PATH), md5FileName);
        return file.exists() ? file : null;
    }

    public static void bindDrawable(Context context, ImageView imageView, String str, boolean z,
                                    BindDrawableListener bindDrawableListener, Animation
                                            animation) {
        if (imageView != null) {
            IMAGE_VIEWS.put(imageView, str);
            try {
                File cachedFile = getCachedFile(context, str);
                if (cachedFile == null || !cachedFile.exists() || RESUTIL_DEBUG) {
                    new FetchTask(context, imageView, str, BindMode.BIND_FROM_NET, null, z,
                            bindDrawableListener, animation).execute(new Object[0]);
                    return;
                }
                if (bindDrawableListener != null) {
                    bindDrawableListener.onStart(BindMode.BIND_FORM_CACHE);
                }
                Drawable createFromPathBuffer = createFromPathBuffer(cachedFile.getAbsolutePath());
                if (createFromPathBuffer == null) {
                    cachedFile.delete();
                }
                doBind(context, imageView, createFromPathBuffer, z, bindDrawableListener,
                        animation, str);
            } catch (Exception e) {
                Log.e(TAG, "", e);
                if (bindDrawableListener != null) {
                    bindDrawableListener.onEnd(STATUS.FAIL);
                }
            }
        }
    }

    private static boolean imageViewReused(ImageView imageView, String str) {
        String str2 = (String) IMAGE_VIEWS.get(imageView);
        if (str2 == null || str2.equals(str)) {
            return false;
        }
        return true;
    }

    private static synchronized void doBind(Context context, ImageView imageView, Drawable
            drawable, boolean z, BindDrawableListener bindDrawableListener, Animation animation,
                                            String str) {
        synchronized (ResUtil.class) {
            if (drawable != null) {
                try {
                    drawable = new BitmapDrawable(getRoundedCornerBitmap(((BitmapDrawable)
                            drawable).getBitmap()));
                } catch (Exception e) {
                    Log.e(TAG, "bind failed", e);
                    if (bindDrawableListener != null) {
                        bindDrawableListener.onEnd(STATUS.FAIL);
                    }
                }
            }
            if (drawable == null || imageView == null) {
                if (bindDrawableListener != null) {
                    bindDrawableListener.onEnd(STATUS.FAIL);
                }
                Log.w(TAG, "bind drawable failed. drawable [" + drawable + "]  imageView[+" +
                        imageView + "+]");
            } else {
                if (!imageViewReused(imageView, str)) {
                    if (z) {
                        imageView.setBackgroundDrawable(drawable);
                    } else {
                        imageView.setImageDrawable(drawable);
                    }
                    if (animation != null) {
                        imageView.startAnimation(animation);
                    }
                    if (bindDrawableListener != null) {
                        bindDrawableListener.onEnd(STATUS.SUCCESS);
                    }
                } else if (bindDrawableListener != null) {
                    bindDrawableListener.onEnd(STATUS.FAIL);
                }
            }
        }
    }

    private static Drawable createFromPathBuffer(String str) {
        Drawable drawable = null;
        try {
            drawable = Drawable.createFromPath(str);
        } catch (OutOfMemoryError e) {
            Log.w(TAG, "Resutil fetchImage OutOfMemoryError:" + e.toString());
        }
        return drawable;
    }

    private static Bitmap getRoundedCornerBitmap(Bitmap bitmap) {
        try {
            Bitmap createBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(),
                    Config.ARGB_8888);
            Canvas canvas = new Canvas(createBitmap);
            Paint paint = new Paint();
            Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
            RectF rectF = new RectF(rect);
            paint.setAntiAlias(true);
            canvas.drawARGB(0, 0, 0, 0);
            paint.setColor(-12434878);
            canvas.drawRoundRect(rectF, (float) (bitmap.getWidth() / 6), (float) (bitmap
                    .getHeight() / 6), paint);
            paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
            canvas.drawBitmap(bitmap, rect, rect, paint);
            bitmap.recycle();
            return createBitmap;
        } catch (OutOfMemoryError e) {
            Log.w(TAG, "Cant`t create round corner bitmap. [OutOfMemoryError] ");
            return null;
        }
    }
}
