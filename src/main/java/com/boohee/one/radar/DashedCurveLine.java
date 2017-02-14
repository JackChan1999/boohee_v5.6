package com.boohee.one.radar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

import com.boohee.utility.DensityUtil;

import uk.co.senab.photoview.IPhotoView;

public class DashedCurveLine extends View {
    private       int BOX_LENGTH  = 50;
    private final int BOX_NUMBER  = 5;
    private       int FACE_LENGTH = 15;
    private       int HEIGHT      = 34;
    private       int INTERVAL    = 15;
    private int     height;
    private Context mContext;
    private Paint   mPaint;
    private Path    mPath;
    private int     width;

    public DashedCurveLine(Context context) {
        super(context);
        init(context);
    }

    public DashedCurveLine(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public DashedCurveLine(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        this.mContext = context;
        this.mPaint = new Paint();
        this.mPaint.setAntiAlias(true);
        this.mPaint.setColor(-2105377);
        this.mPaint.setStrokeWidth(2.0f);
        this.mPaint.setStyle(Style.STROKE);
        this.mPaint.setPathEffect(new DashPathEffect(new float[]{IPhotoView.DEFAULT_MAX_SCALE, 6
                .0f}, 0.0f));
        this.mPath = new Path();
        this.BOX_LENGTH = DensityUtil.dip2px(context, (float) this.BOX_LENGTH);
        this.INTERVAL = DensityUtil.dip2px(context, (float) this.INTERVAL);
        this.FACE_LENGTH = DensityUtil.dip2px(context, (float) this.FACE_LENGTH);
        this.HEIGHT = DensityUtil.dip2px(context, (float) this.HEIGHT) - 1;
    }

    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.width = w;
        this.height = h;
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        this.mPath.reset();
        int i = 0;
        while (i < 5) {
            if (i != 2) {
                int radius;
                int left = i < 2 ? 1 : -1;
                if (i == 0 || i == 4) {
                    radius = 10;
                } else {
                    radius = 8;
                }
                float start = (((float) this.BOX_LENGTH) / 2.0f) + ((float) ((this.BOX_LENGTH +
                        this.INTERVAL) * i));
                this.mPath.moveTo(start, 0.0f);
                this.mPath.lineTo(start, (float) (this.HEIGHT - radius));
                this.mPath.quadTo(start, (float) this.HEIGHT, ((float) (radius * left)) + start,
                        (float) this.HEIGHT);
                if (i == 0 || i == 4) {
                    this.mPath.lineTo(((float) this.width) / 2.0f, (float) this.HEIGHT);
                }
            }
            i++;
        }
        this.mPath.moveTo(((float) this.width) / 2.0f, 0.0f);
        this.mPath.lineTo(((float) this.width) / 2.0f, (float) getHeight());
        canvas.drawPath(this.mPath, this.mPaint);
    }
}
