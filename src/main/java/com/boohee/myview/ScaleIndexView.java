package com.boohee.myview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import com.boohee.model.scale.ScaleIndex;
import com.boohee.utility.DensityUtil;

import java.text.DecimalFormat;

import kankan.wheel.widget.adapters.AbstractWheelTextAdapter;
import lecho.lib.hellocharts.model.PieChartData;

public class ScaleIndexView extends View {
    private final int     BACKGROUND_COLOR = -2105377;
    private       float[] POSITION         = new float[]{0.0f, 0.3f, PieChartData
            .DEFAULT_CENTER_CIRCLE_SCALE, 0.8f};
    private final int     TEXT_COLOR       = AbstractWheelTextAdapter.DEFAULT_TEXT_COLOR;
    private Paint backgroundPaint;
    private DecimalFormat df = new DecimalFormat("#.#");
    private Paint dividerPaint;
    private int   height;
    private int lineHeight = 5;
    private Context    mContext;
    private ScaleIndex mIndex;
    private float[]    percent;
    private Rect rect = new Rect();
    private Paint textPaint;
    private int textSize = 14;
    private int width;

    public ScaleIndexView(Context context) {
        super(context);
        init(context);
    }

    public ScaleIndexView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        setLayerType(1, null);
        this.mContext = context;
        this.lineHeight = DensityUtil.dip2px(context, (float) this.lineHeight);
        this.textSize = DensityUtil.dip2px(context, (float) this.textSize);
        this.textPaint = new Paint();
        this.textPaint.setAntiAlias(true);
        this.textPaint.setTextSize((float) this.textSize);
        this.textPaint.setTextAlign(Align.LEFT);
        this.textPaint.setColor(AbstractWheelTextAdapter.DEFAULT_TEXT_COLOR);
        this.dividerPaint = new Paint();
        this.dividerPaint.setAntiAlias(true);
        this.dividerPaint.setTextSize((float) this.textSize);
        this.dividerPaint.setTextAlign(Align.LEFT);
        this.dividerPaint.setColor(-1);
        this.dividerPaint.setStrokeWidth(2.0f);
        this.backgroundPaint = new Paint();
        this.backgroundPaint.setAntiAlias(true);
        this.backgroundPaint.setStyle(Style.FILL_AND_STROKE);
        this.backgroundPaint.setStrokeCap(Cap.ROUND);
    }

    public void setIndex(ScaleIndex index) {
        this.mIndex = index;
        float[] division = this.mIndex.getDivision();
        int length = division.length;
        this.percent = new float[length];
        for (int i = 0; i < length; i++) {
            this.percent[i] = getPercent(division[i]);
        }
    }

    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.width = w;
        this.height = h;
    }

    protected void onDraw(Canvas canvas) {
        if (this.mIndex != null) {
            int i;
            String[] levelName = this.mIndex.getAllLevel();
            for (i = 0; i < levelName.length; i++) {
                Canvas canvas2 = canvas;
                drawTextCenter(canvas2, this.textPaint, levelName[i], ((this.percent[i] + this
                        .percent[i + 1]) / 2.0f) * ((float) this.width), 0.0f);
            }
            this.backgroundPaint.setStrokeWidth((float) this.lineHeight);
            this.backgroundPaint.setColor(-2105377);
            canvas.drawLine(((float) this.lineHeight) / 2.0f, this.POSITION[2] * ((float) this
                    .height), ((float) this.width) - (((float) this.lineHeight) / 2.0f), this
                    .POSITION[2] * ((float) this.height), this.backgroundPaint);
            this.backgroundPaint.setColor(this.mIndex.getColor());
            float current = getPercent(this.mIndex.getValue());
            if (((double) current) > 0.01d) {
                canvas.drawLine(((float) this.lineHeight) / 2.0f, this.POSITION[2] * ((float)
                        this.height), (((float) this.width) - (((float) this.lineHeight) / 2.0f))
                        * current, this.POSITION[2] * ((float) this.height), this.backgroundPaint);
            }
            drawIndicator(canvas, this.mIndex.getValueWithUnit(), ((float) this.width) * current);
            for (i = 1; i < this.percent.length - 1; i++) {
                canvas.drawLine(this.percent[i] * ((float) this.width), (this.POSITION[2] * (
                        (float) this.height)) - (((float) this.lineHeight) / 2.0f), this
                        .percent[i] * ((float) this.width), (this.POSITION[2] * ((float) this
                        .height)) + (((float) this.lineHeight) / 2.0f), this.dividerPaint);
                Canvas canvas3 = canvas;
                drawTextCenter(canvas3, this.textPaint, this.df.format((double) this.mIndex
                        .getDivision()[i]), this.percent[i] * ((float) this.width), this
                        .POSITION[3] * ((float) this.height));
            }
        }
    }

    private void drawTextCenter(Canvas canvas, Paint paint, String text, float xCenter, float
            yBegin) {
        paint.getTextBounds(text, 0, text.length(), this.rect);
        canvas.drawText(text, (xCenter - (((float) this.rect.width()) / 2.0f)) - ((float) this
                .rect.left), (((float) this.rect.height()) + yBegin) - ((float) this.rect.bottom)
                , paint);
    }

    private float getPercent(float value) {
        float[] division = this.mIndex.getDivision();
        return (value - division[0]) / (division[division.length - 1] - division[0]);
    }

    private void drawIndicator(Canvas canvas, String text, float xCenter) {
        this.dividerPaint.getTextBounds(text, 0, text.length(), this.rect);
        float indicatorWidth = (float) (this.rect.width() + 10);
        float indicatorHeight = ((float) this.textSize) * 1.2f;
        this.backgroundPaint.setStrokeWidth(indicatorHeight);
        if ((xCenter - (indicatorWidth / 2.0f)) - (indicatorHeight / 2.0f) < 0.0f) {
            xCenter = (indicatorWidth / 2.0f) + (indicatorHeight / 2.0f);
        }
        if (((indicatorWidth / 2.0f) + xCenter) + (indicatorHeight / 2.0f) > ((float) this.width)) {
            xCenter = (((float) this.width) - (indicatorWidth / 2.0f)) - (indicatorHeight / 2.0f);
        }
        Canvas canvas2 = canvas;
        canvas2.drawLine(xCenter - (indicatorWidth / 2.0f), (indicatorHeight / 2.0f) + (this
                .POSITION[1] * ((float) this.height)), xCenter + (indicatorWidth / 2.0f),
                (indicatorHeight / 2.0f) + (this.POSITION[1] * ((float) this.height)), this
                        .backgroundPaint);
        drawTextCenter(canvas, this.dividerPaint, text, xCenter, (this.POSITION[1] * ((float)
                this.height)) + ((indicatorHeight - ((float) this.rect.height())) / 2.0f));
    }
}
