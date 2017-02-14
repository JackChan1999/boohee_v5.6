package com.boohee.myview.highlight;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BlurMaskFilter;
import android.graphics.BlurMaskFilter.Blur;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.MeasureSpec;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;

import com.boohee.myview.highlight.HighLight.ViewPosInfo;

import java.util.List;

public class HightLightView extends FrameLayout {
    private static final int                DEFAULT_RADIUS     = 6;
    private static final int                DEFAULT_WIDTH_BLUR = 15;
    private static final PorterDuffXfermode MODE_DST_OUT       = new PorterDuffXfermode(Mode
            .DST_OUT);
    private              boolean            isBlur             = true;
    private HighLight         mHighLight;
    private LayoutInflater    mInflater;
    private Bitmap            mMaskBitmap;
    private Paint             mPaint;
    private List<ViewPosInfo> mViewRects;
    private int maskColor = -872415232;

    public HightLightView(Context context, HighLight highLight, int maskColor, boolean isBlur,
                          List<ViewPosInfo> viewRects) {
        super(context);
        this.mHighLight = highLight;
        this.mInflater = LayoutInflater.from(context);
        this.mViewRects = viewRects;
        this.maskColor = maskColor;
        this.isBlur = isBlur;
        setWillNotDraw(false);
        init();
    }

    private void init() {
        this.mPaint = new Paint();
        this.mPaint.setDither(true);
        this.mPaint.setAntiAlias(true);
        if (this.isBlur) {
            this.mPaint.setMaskFilter(new BlurMaskFilter(15.0f, Blur.SOLID));
        }
        this.mPaint.setStyle(Style.FILL);
        addViewForTip();
    }

    private void addViewForTip() {
        for (ViewPosInfo viewPosInfo : this.mViewRects) {
            View view = this.mInflater.inflate(viewPosInfo.layoutId, this, false);
            LayoutParams lp = buildTipLayoutParams(view, viewPosInfo);
            if (lp != null) {
                lp.leftMargin = (int) viewPosInfo.marginInfo.leftMargin;
                lp.topMargin = (int) viewPosInfo.marginInfo.topMargin;
                lp.rightMargin = (int) viewPosInfo.marginInfo.rightMargin;
                lp.bottomMargin = (int) viewPosInfo.marginInfo.bottomMargin;
                if (lp.leftMargin == 0 && lp.topMargin == 0) {
                    lp.gravity = 85;
                }
                addView(view, lp);
            }
        }
    }

    private void buildMask() {
        this.mMaskBitmap = Bitmap.createBitmap(getMeasuredWidth(), getMeasuredHeight(), Config
                .ARGB_8888);
        Canvas canvas = new Canvas(this.mMaskBitmap);
        canvas.drawColor(this.maskColor);
        this.mPaint.setXfermode(MODE_DST_OUT);
        this.mHighLight.updateInfo();
        for (ViewPosInfo viewPosInfo : this.mViewRects) {
            canvas.drawRoundRect(viewPosInfo.rectF, 6.0f, 6.0f, this.mPaint);
        }
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        measureChildren(MeasureSpec.makeMeasureSpec(width, 1073741824), MeasureSpec
                .makeMeasureSpec(height, 1073741824));
        setMeasuredDimension(width, height);
    }

    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (changed) {
            buildMask();
            updateTipPos();
        }
    }

    private void updateTipPos() {
        int n = getChildCount();
        for (int i = 0; i < n; i++) {
            View view = getChildAt(i);
            LayoutParams lp = buildTipLayoutParams(view, (ViewPosInfo) this.mViewRects.get(i));
            if (lp != null) {
                view.setLayoutParams(lp);
            }
        }
    }

    private LayoutParams buildTipLayoutParams(View view, ViewPosInfo viewPosInfo) {
        LayoutParams lp = (LayoutParams) view.getLayoutParams();
        if (lp.leftMargin == ((int) viewPosInfo.marginInfo.leftMargin) && lp.topMargin == ((int)
                viewPosInfo.marginInfo.topMargin) && lp.rightMargin == ((int) viewPosInfo
                .marginInfo.rightMargin) && lp.bottomMargin == ((int) viewPosInfo.marginInfo
                .bottomMargin)) {
            return null;
        }
        lp.leftMargin = (int) viewPosInfo.marginInfo.leftMargin;
        lp.topMargin = (int) viewPosInfo.marginInfo.topMargin;
        lp.rightMargin = (int) viewPosInfo.marginInfo.rightMargin;
        lp.bottomMargin = (int) viewPosInfo.marginInfo.bottomMargin;
        if (lp.leftMargin != 0 || lp.topMargin != 0) {
            return lp;
        }
        lp.gravity = 85;
        return lp;
    }

    protected void onDraw(Canvas canvas) {
        if (this.mMaskBitmap != null) {
            canvas.drawBitmap(this.mMaskBitmap, 0.0f, 0.0f, null);
            super.onDraw(canvas);
        }
    }
}
