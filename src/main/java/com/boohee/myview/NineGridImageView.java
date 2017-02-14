package com.boohee.myview;

import android.content.Context;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageView;

import com.boohee.one.R;
import com.boohee.utility.ImageLoaderOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.Random;

public class NineGridImageView extends ImageView {
    protected final int[]   PREVIEW_COLORS;
    private         Context context;
    ImageLoader imageLoader;
    private boolean isAttachedToWindow;
    private String  url;

    public NineGridImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.PREVIEW_COLORS = new int[]{R.color.dg, R.color.dh, R.color.di, R.color.dj, R.color.dk};
        this.context = context;
        this.imageLoader = ImageLoader.getInstance();
    }

    public NineGridImageView(Context context) {
        this(context, null);
    }

    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case 0:
                Drawable drawable = getDrawable();
                if (drawable != null) {
                    drawable.mutate().setColorFilter(-7829368, Mode.MULTIPLY);
                    break;
                }
                break;
            case 1:
            case 3:
                Drawable drawableUp = getDrawable();
                if (drawableUp != null) {
                    drawableUp.mutate().clearColorFilter();
                    break;
                }
                break;
        }
        return super.onTouchEvent(event);
    }

    public void onAttachedToWindow() {
        this.isAttachedToWindow = true;
        setImageUrl(this.url);
        super.onAttachedToWindow();
    }

    public void onDetachedFromWindow() {
        this.imageLoader.cancelDisplayTask((ImageView) this);
        this.isAttachedToWindow = false;
        setImageBitmap(null);
        super.onDetachedFromWindow();
    }

    public void setImageUrl(String url) {
        if (!TextUtils.isEmpty(url)) {
            this.url = url;
            if (this.isAttachedToWindow) {
                this.imageLoader.displayImage(url, (ImageView) this, ImageLoaderOptions.color
                        (this.PREVIEW_COLORS[new Random().nextInt(this.PREVIEW_COLORS.length)]));
            }
        }
    }
}
