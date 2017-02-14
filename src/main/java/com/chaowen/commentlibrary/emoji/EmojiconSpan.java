package com.chaowen.commentlibrary.emoji;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.FontMetricsInt;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.style.DynamicDrawableSpan;

class EmojiconSpan extends DynamicDrawableSpan {
    private final Context  mContext;
    private       Drawable mDrawable;
    private final int      mResourceId;
    private final int      mSize;

    public EmojiconSpan(Context context, int resourceId, int size) {
        super(1);
        this.mContext = context;
        this.mResourceId = resourceId;
        this.mSize = size;
    }

    public int getSize(Paint paint, CharSequence text, int start, int end, FontMetricsInt fm) {
        Rect rect = getDrawable().getBounds();
        if (fm != null) {
            FontMetricsInt fmPaint = paint.getFontMetricsInt();
            int fontHeight = fmPaint.bottom - fmPaint.top;
            int drHeight = rect.bottom - rect.top;
            int top = (drHeight / 2) - (fontHeight / 4);
            int bottom = (drHeight / 2) + (fontHeight / 4);
            fm.ascent = -bottom;
            fm.top = -bottom;
            fm.bottom = top;
            fm.descent = top;
        }
        return rect.right;
    }

    public void draw(Canvas canvas, CharSequence text, int start, int end, float x, int top, int
            y, int bottom, Paint paint) {
        Drawable drawable = getDrawable();
        canvas.save();
        canvas.translate(x, (float) ((((bottom - top) - drawable.getBounds().bottom) / 2) + top));
        drawable.draw(canvas);
        canvas.restore();
    }

    public Drawable getDrawable() {
        if (this.mDrawable == null) {
            try {
                this.mDrawable = this.mContext.getResources().getDrawable(this.mResourceId);
                int size = this.mSize;
                this.mDrawable.setBounds(0, 0, size, size);
            } catch (Exception e) {
            }
        }
        return this.mDrawable;
    }
}
