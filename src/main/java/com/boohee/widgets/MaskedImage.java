package com.boohee.widgets;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Xfermode;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

public abstract class MaskedImage extends ImageView {
    private static final Xfermode MASK_XFERMODE = new PorterDuffXfermode(Mode.DST_IN);
    private Bitmap mask;
    private Paint  paint;

    public abstract Bitmap createMask();

    public MaskedImage(Context paramContext) {
        super(paramContext);
    }

    public MaskedImage(Context paramContext, AttributeSet paramAttributeSet) {
        super(paramContext, paramAttributeSet);
    }

    public MaskedImage(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
        super(paramContext, paramAttributeSet, paramInt);
    }

    protected void onDraw(Canvas paramCanvas) {
        Drawable localDrawable = getDrawable();
        if (localDrawable != null) {
            try {
                if (this.paint == null) {
                    this.paint = new Paint();
                    this.paint.setFilterBitmap(false);
                    this.paint.setXfermode(MASK_XFERMODE);
                }
                int i = paramCanvas.saveLayer(0.0f, 0.0f, (float) getWidth(), (float) getHeight()
                        , null, 31);
                localDrawable.setBounds(0, 0, getWidth(), getHeight());
                localDrawable.draw(paramCanvas);
                if (this.mask == null || this.mask.isRecycled()) {
                    this.mask = createMask();
                }
                paramCanvas.drawBitmap(this.mask, 0.0f, 0.0f, this.paint);
                paramCanvas.restoreToCount(i);
            } catch (Exception e) {
                System.out.println("localStringBuilder==" + new StringBuilder().append
                        ("Attempting to draw with recycled bitmap. View ID = "));
            }
        }
    }
}
