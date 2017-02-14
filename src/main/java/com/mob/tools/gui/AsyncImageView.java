package com.mob.tools.gui;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler.Callback;
import android.os.Message;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;

import com.mob.tools.gui.BitmapProcessor.BitmapCallback;
import com.mob.tools.utils.BitmapHelper;
import com.mob.tools.utils.Ln;
import com.mob.tools.utils.UIHandler;

import java.util.Random;

public class AsyncImageView extends ImageView implements BitmapCallback, Callback {
    private static final int    MSG_IMG_GOT = 1;
    private static final Random rnd         = new Random();
    private Bitmap  defaultBm;
    private int     defaultRes;
    private float[] rect;
    private boolean scaleToCrop;
    private String  url;

    public AsyncImageView(Context context) {
        super(context);
        init(context);
    }

    public AsyncImageView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init(context);
    }

    public AsyncImageView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        init(context);
    }

    private int[] getSize() {
        int width = getWidth();
        int height = getHeight();
        if (width == 0 || height == 0) {
            LayoutParams layoutParams = getLayoutParams();
            if (layoutParams != null) {
                width = layoutParams.width;
                height = layoutParams.height;
            }
        }
        if (width == 0 || r0 == 0) {
            measure(0, 0);
            width = getMeasuredWidth();
            height = getMeasuredHeight();
        }
        return new int[]{width, height};
    }

    private Bitmap goCrop(Bitmap bitmap) {
        float width = (float) bitmap.getWidth();
        float height = (float) bitmap.getHeight();
        if (!(width == 0.0f || height == 0.0f)) {
            int[] size = getSize();
            if (!(size[0] == 0 || size[1] == 0)) {
                float f = (((float) size[1]) * width) / ((float) size[0]);
                if (f != height) {
                    int[] iArr = new int[4];
                    if (f < height) {
                        iArr[1] = (int) ((height - f) / 2.0f);
                        iArr[3] = iArr[1];
                    } else {
                        iArr[0] = (int) ((width - ((height * ((float) size[0])) / ((float)
                                size[1]))) / 2.0f);
                        iArr[2] = iArr[0];
                    }
                    try {
                        bitmap = BitmapHelper.cropBitmap(bitmap, iArr[0], iArr[1], iArr[2],
                                iArr[3]);
                    } catch (Throwable th) {
                        Ln.w(th);
                    }
                }
            }
        }
        return bitmap;
    }

    private Bitmap goRound(Bitmap bitmap) {
        try {
            int[] size = getSize();
            bitmap = BitmapHelper.roundBitmap(bitmap, size[0], size[1], this.rect[0], this
                    .rect[1], this.rect[2], this.rect[3]);
        } catch (Throwable th) {
            Ln.w(th);
        }
        return bitmap;
    }

    private void init(Context context) {
        if (isInEditMode()) {
            setBackgroundColor(-16777216);
        } else {
            BitmapProcessor.prepare(context);
        }
    }

    public void execute(String str, int i) {
        Ln.d("AsyncImageView, %s", new Object[]{str});
        this.url = str;
        this.defaultRes = i;
        if (TextUtils.isEmpty(str)) {
            setImageResource(i);
            return;
        }
        Bitmap bitmapFromCache = BitmapProcessor.getBitmapFromCache(str);
        if (bitmapFromCache == null || bitmapFromCache.isRecycled()) {
            if (i > 0) {
                setImageResource(i);
            }
            Ln.w("BitmapProcessor, %s", new Object[]{str});
            BitmapProcessor.process(str, this);
            return;
        }
        setBitmap(bitmapFromCache);
    }

    public void execute(String str, Bitmap bitmap) {
        Ln.d("AsyncImageView, %s", new Object[]{str});
        this.url = str;
        this.defaultBm = bitmap;
        if (TextUtils.isEmpty(str)) {
            setImageBitmap(bitmap);
            return;
        }
        Bitmap bitmapFromCache = BitmapProcessor.getBitmapFromCache(str);
        if (bitmapFromCache == null || bitmapFromCache.isRecycled()) {
            if (!(bitmap == null || bitmap.isRecycled())) {
                setImageBitmap(bitmap);
            }
            Ln.w("BitmapProcessor, %s", new Object[]{str});
            BitmapProcessor.process(str, this);
            return;
        }
        setBitmap(bitmapFromCache);
    }

    public boolean handleMessage(Message message) {
        if (message.what == 1) {
            Object obj = ((Object[]) message.obj)[0];
            Object obj2 = ((Object[]) message.obj)[1];
            if (obj2 != null && obj != null && obj.equals(this.url)) {
                setImageBitmap((Bitmap) obj2);
            } else if (this.defaultBm == null || this.defaultBm.isRecycled()) {
                setImageResource(this.defaultRes);
            } else {
                setImageBitmap(this.defaultBm);
            }
        }
        return false;
    }

    public void onImageGot(String str, Bitmap bitmap) {
        Bitmap bitmap2 = null;
        if (str != null && str.trim().length() > 0 && str.equals(this.url)) {
            bitmap2 = bitmap;
        }
        if (bitmap2 != null) {
            if (this.scaleToCrop) {
                bitmap2 = goCrop(bitmap2);
            }
            if (this.rect != null) {
                bitmap2 = goRound(bitmap2);
            }
        }
        Message message = new Message();
        message.what = 1;
        message.obj = new Object[]{str, bitmap2};
        UIHandler.sendMessageDelayed(message, (long) rnd.nextInt(300), this);
    }

    public void setBitmap(Bitmap bitmap) {
        if (this.scaleToCrop) {
            bitmap = goCrop(bitmap);
        }
        if (this.rect != null) {
            bitmap = goRound(bitmap);
        }
        setImageBitmap(bitmap);
    }

    public void setRound(float f) {
        setRound(f, f, f, f);
    }

    public void setRound(float f, float f2, float f3, float f4) {
        this.rect = new float[]{f, f2, f3, f4};
    }

    public void setScaleToCropCenter(boolean z) {
        this.scaleToCrop = z;
    }
}
