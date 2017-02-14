package com.boohee.widgets;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.SeekBar;

import com.baidu.location.aj;
import com.boohee.one.R;

public class NumberSeekBar extends SeekBar {
    private Bitmap    bm;
    private int       imagepaddingleft;
    private int       imagepaddingtop;
    private boolean   isMysetPadding;
    private boolean   isOnclick;
    private float     mImgHei;
    private float     mImgWidth;
    private Paint     mPaint;
    private String    mText;
    private float     mTextWidth;
    private int       mTogglePadding;
    private Paint     mTogglePaint;
    private RectF     mToggleRectf;
    private int       oldPaddingBottom;
    private int       oldPaddingLeft;
    private int       oldPaddingRight;
    private int       oldPaddingTop;
    private Resources res;
    private int       textpaddingleft;
    private int       textpaddingtop;
    private int       textsize;

    public NumberSeekBar(Context context) {
        this(context, null);
    }

    public NumberSeekBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NumberSeekBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.isMysetPadding = true;
        this.textsize = 13;
        this.mTogglePadding = 5;
        init();
        initAttrs(context, attrs);
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable
                    .NumberSeekBar, 0, 0);
            setBitmap(typedArray.getResourceId(0, 0));
            this.mPaint.setColor(typedArray.getColor(2, 0));
            this.mPaint.setTextSize(typedArray.getDimension(1, 0.0f));
            typedArray.recycle();
        }
    }

    public boolean onTouchEvent(MotionEvent event) {
        return false;
    }

    public void setPadding(int left, int top, int right, int bottom) {
        if (this.isMysetPadding) {
            super.setPadding(left, top, right, bottom);
        }
    }

    public void setOnclick(boolean isOnclick) {
        this.isOnclick = isOnclick;
    }

    public void setProgressText(String mText) {
        this.mText = mText;
        this.isOnclick = true;
        invalidate();
    }

    private void init() {
        this.res = getResources();
        initDraw();
        initToggleDraw();
        setPadding();
    }

    private void initDraw() {
        this.mPaint = new Paint(1);
        this.mPaint.setTypeface(Typeface.DEFAULT);
        this.mPaint.setTextSize((float) this.textsize);
    }

    private void initToggleDraw() {
        this.mTogglePaint = new Paint();
        this.mTogglePaint.setAntiAlias(true);
        this.mTogglePaint.setDither(true);
        this.mToggleRectf = new RectF();
    }

    private void initBitmap() {
        this.bm = BitmapFactory.decodeResource(this.res, R.drawable.p1);
        if (this.bm != null) {
            this.mImgWidth = (float) this.bm.getWidth();
            this.mImgHei = (float) this.bm.getHeight();
            return;
        }
        this.mImgWidth = 0.0f;
        this.mImgHei = 0.0f;
    }

    protected synchronized void onDraw(Canvas canvas) {
        try {
            super.onDraw(canvas);
            if (!this.isOnclick) {
                this.mText = ((getProgress() * 100) / getMax()) + "%";
            }
            this.mTextWidth = this.mPaint.measureText(this.mText);
            float xImg = ((float) ((getProgressDrawable().getBounds().width() * getProgress()) /
                    getMax())) - (this.mImgWidth / 2.0f);
            if (xImg <= ((float) this.mTogglePadding)) {
                xImg = (float) this.mTogglePadding;
            }
            if (((float) getBitmapWidth()) + xImg > ((float) (getWidth() - this.mTogglePadding))) {
                xImg = (float) ((getWidth() - getBitmapWidth()) - this.mTogglePadding);
            }
            float yImg = (float) ((getHeight() / 2) - (getBitmapHeigh() / 2));
            float xText = ((this.mImgWidth / 2.0f) + xImg) - (this.mTextWidth / 2.0f);
            float yText = (((float) (getBitmapHeigh() / 2)) + yImg) + (getTextHei() / aj.hA);
            this.mTogglePaint.setColor(getResources().getColor(R.color.hx));
            this.mToggleRectf.set(xImg - ((float) this.mTogglePadding), yImg, (((float) this.bm
                    .getWidth()) + xImg) + ((float) this.mTogglePadding), (float) getHeight());
            canvas.drawRoundRect(this.mToggleRectf, (float) (this.bm.getHeight() / 2), (float)
                    (this.bm.getHeight() / 2), this.mTogglePaint);
            canvas.drawText(this.mText, xText, yText, this.mPaint);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setPadding() {
        int top = (getBitmapHeigh() / 2) + this.oldPaddingTop;
        int left = (getBitmapWidth() / 2) + this.oldPaddingLeft;
        int right = (getBitmapWidth() / 2) + this.oldPaddingRight;
        int bottom = (getBitmapHeigh() / 2) + this.oldPaddingBottom;
        this.isMysetPadding = true;
        setPadding(0, top, 0, bottom);
        this.isMysetPadding = false;
    }

    public void setBitmap(int resid) {
        this.bm = BitmapFactory.decodeResource(this.res, resid);
        if (this.bm != null) {
            this.mImgWidth = (float) this.bm.getWidth();
            this.mImgHei = (float) this.bm.getHeight();
        } else {
            this.mImgWidth = 0.0f;
            this.mImgHei = 0.0f;
        }
        setPadding();
    }

    public void setMyPadding(int left, int top, int right, int bottom) {
        this.oldPaddingTop = top;
        this.oldPaddingLeft = left;
        this.oldPaddingRight = right;
        this.oldPaddingBottom = bottom;
        this.isMysetPadding = true;
        setPadding((getBitmapWidth() / 2) + left, getBitmapHeigh() + top, (getBitmapWidth() / 2)
                + right, bottom);
        this.isMysetPadding = false;
    }

    public void setTextSize(int textsize) {
        this.textsize = textsize;
        this.mPaint.setTextSize((float) textsize);
    }

    public void setTextColor(int color) {
        this.mPaint.setColor(color);
    }

    public void setTextPadding(int top, int left) {
        this.textpaddingleft = left;
        this.textpaddingtop = top;
    }

    public void setImagePadding(int top, int left) {
        this.imagepaddingleft = left;
        this.imagepaddingtop = top;
    }

    private int getBitmapWidth() {
        return (int) Math.ceil((double) this.mImgWidth);
    }

    private int getBitmapHeigh() {
        return (int) Math.ceil((double) this.mImgHei);
    }

    private float getTextHei() {
        FontMetrics fm = this.mPaint.getFontMetrics();
        return ((float) Math.ceil((double) (fm.descent - fm.top))) + 2.0f;
    }

    public int getTextpaddingleft() {
        return this.textpaddingleft;
    }

    public int getTextpaddingtop() {
        return this.textpaddingtop;
    }

    public int getImagepaddingleft() {
        return this.imagepaddingleft;
    }

    public int getImagepaddingtop() {
        return this.imagepaddingtop;
    }

    public int getTextsize() {
        return this.textsize;
    }
}
