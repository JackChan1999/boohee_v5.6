package com.boohee.widgets;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.view.View.MeasureSpec;
import android.widget.ImageView;

import com.boohee.one.R;

public class SquareImageView extends ImageView {
    private boolean isCheck;
    private int     mBound;
    private int     mMarginTop;

    public SquareImageView(Context context) {
        this(context, null);
    }

    public SquareImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SquareImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mMarginTop = (int) getResources().getDimension(R.dimen.gv);
        this.mBound = (int) getResources().getDimension(R.dimen.gu);
        setPadding(0, this.mMarginTop, 0, 0);
    }

    public void setChecked() {
        this.isCheck = !this.isCheck;
        invalidate();
    }

    public void setChecked(boolean checked) {
        this.isCheck = checked;
        invalidate();
    }

    public boolean getChecked() {
        return this.isCheck;
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(getDefaultSize(0, widthMeasureSpec), getDefaultSize(0,
                heightMeasureSpec));
        widthMeasureSpec = MeasureSpec.makeMeasureSpec(getMeasuredWidth(), 1073741824);
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
    }

    protected void onDraw(Canvas canvas) {
        int width = getWidth();
        int radius = getWidth() - (this.mBound * 2);
        int radiusLarge = radius / 2;
        int radiusSmall = radiusLarge - this.mBound;
        if (this.isCheck) {
            Paint paintRing = new Paint();
            paintRing.setAntiAlias(true);
            paintRing.setStyle(Style.FILL);
            paintRing.setColor(getResources().getColor(R.color.he));
            canvas.drawCircle((float) (width / 2), (float) ((radius / 2) + this.mMarginTop),
                    (float) radiusLarge, paintRing);
        }
        Paint paintBg = new Paint();
        paintBg.setAntiAlias(true);
        paintBg.setStyle(Style.FILL);
        paintBg.setColor(-1);
        canvas.drawCircle((float) (width / 2), (float) ((radius / 2) + this.mMarginTop), (float)
                radiusSmall, paintBg);
        super.onDraw(canvas);
        try {
            if (this.isCheck) {
                double r = (double) (width / 2);
                Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.rd);
                canvas.drawBitmap(bmp, (float) getXPoint(r, bmp), (float) getYPoint(r, bmp), null);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private int getXPoint(double r, Bitmap bmp) {
        return (int) (r + (countMiddle(bmp) * Math.sin(0.5d)));
    }

    private int getYPoint(double r, Bitmap bmp) {
        return (int) ((((double) this.mMarginTop) + r) - (countMiddle(bmp) * Math.cos(0.5d)));
    }

    private double countMiddle(Bitmap bmp) {
        return ((double) (getWidth() / 2)) - (Math.sqrt((double) ((bmp.getWidth() ^ (bmp
                .getHeight() + 2)) ^ 2)) / PathListView.ZOOM_X2);
    }
}
