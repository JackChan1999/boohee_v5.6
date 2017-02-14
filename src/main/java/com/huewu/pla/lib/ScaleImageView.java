package com.huewu.pla.lib;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View.MeasureSpec;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;

public class ScaleImageView extends ImageView {
    private ImageChangeListener imageChangeListener;
    private boolean scaleToWidth = false;

    public interface ImageChangeListener {
        void changed(boolean z);
    }

    public ScaleImageView(Context context) {
        super(context);
        init();
    }

    public ScaleImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public ScaleImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        setScaleType(ScaleType.CENTER_INSIDE);
    }

    public void setImageBitmap(Bitmap bm) {
        super.setImageBitmap(bm);
        if (this.imageChangeListener != null) {
            this.imageChangeListener.changed(bm == null);
        }
    }

    public void setImageDrawable(Drawable d) {
        super.setImageDrawable(d);
        if (this.imageChangeListener != null) {
            this.imageChangeListener.changed(d == null);
        }
    }

    public void setImageResource(int id) {
        super.setImageResource(id);
    }

    public ImageChangeListener getImageChangeListener() {
        return this.imageChangeListener;
    }

    public void setImageChangeListener(ImageChangeListener imageChangeListener) {
        this.imageChangeListener = imageChangeListener;
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        if (widthMode == 1073741824 || widthMode == Integer.MIN_VALUE) {
            this.scaleToWidth = true;
        } else if (heightMode == 1073741824 || heightMode == Integer.MIN_VALUE) {
            this.scaleToWidth = false;
        } else {
            throw new IllegalStateException("width or height needs to be set to match_parent or a" +
                    " specific dimension");
        }
        if (getDrawable() == null || getDrawable().getIntrinsicWidth() == 0) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        } else if (this.scaleToWidth) {
            iw = getDrawable().getIntrinsicWidth();
            int ih = getDrawable().getIntrinsicHeight();
            int heightC = (width * ih) / iw;
            if (height > 0 && heightC > height) {
                heightC = height;
                width = (heightC * iw) / ih;
            }
            setScaleType(ScaleType.CENTER_CROP);
            setMeasuredDimension(width, heightC);
        } else {
            int marg = 0;
            if (!(getParent() == null || getParent().getParent() == null)) {
                marg = (0 + ((RelativeLayout) getParent().getParent()).getPaddingTop()) + (
                        (RelativeLayout) getParent().getParent()).getPaddingBottom();
            }
            iw = getDrawable().getIntrinsicWidth();
            setMeasuredDimension((height * iw) / getDrawable().getIntrinsicHeight(), height - marg);
        }
    }
}
