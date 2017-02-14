package com.boohee.one.video.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.boohee.one.R;

import java.util.ArrayList;
import java.util.List;

public class HorizontalProgressView extends View {
    public static final String TAG = HorizontalProgressView.class.getSimpleName();
    private int         completedColor;
    private int         currentNumber;
    private int         gapWidth;
    private int         height;
    private List<RectF> itemList;
    private int         itemWidth;
    private Paint       paint;
    private int         totalNumber;
    private int         uncompletedColor;
    private int         width;

    public HorizontalProgressView(Context context) {
        this(context, null);
    }

    public HorizontalProgressView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HorizontalProgressView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.paint = new Paint();
        this.gapWidth = 2;
        this.itemList = new ArrayList();
        this.completedColor = Color.parseColor("#FFFFFF");
        this.uncompletedColor = Color.parseColor("#33FFFFFF");
        this.itemWidth = 0;
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        if (attrs != null) {
            TypedArray array = getContext().obtainStyledAttributes(attrs, R.styleable
                    .HorizontalProgressView);
            this.totalNumber = array.getInt(0, 4);
            this.currentNumber = array.getInt(1, 0);
            this.completedColor = array.getColor(2, this.completedColor);
            this.uncompletedColor = array.getColor(3, this.uncompletedColor);
            this.gapWidth = (int) array.getDimension(4, (float) dip(this.gapWidth));
            this.itemWidth = (int) array.getDimension(5, 0.0f);
            array.recycle();
        } else {
            this.gapWidth = dip(this.gapWidth);
        }
        this.paint.setAntiAlias(true);
        this.paint.setStyle(Style.FILL);
    }

    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.width = w;
        this.height = h;
        calculatePoints();
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (this.totalNumber > 0) {
            drawRect(canvas);
            drawLeftOval(canvas);
            drawRightOval(canvas);
        }
    }

    private void drawRightOval(Canvas canvas) {
        if (this.currentNumber == this.totalNumber) {
            initCompletedPaint();
        } else {
            initUncompletedPaint();
        }
        canvas.drawArc(new RectF(((RectF) this.itemList.get(this.itemList.size() - 1)).right - (
                (float) (this.height / 2)), 0.0f, ((RectF) this.itemList.get(this.itemList.size()
                - 1)).right + ((float) (this.height / 2)), (float) this.height), 270.0f, 180.0f,
                false, this.paint);
    }

    private void drawLeftOval(Canvas canvas) {
        if (this.currentNumber > 0) {
            initCompletedPaint();
        } else {
            initUncompletedPaint();
        }
        canvas.drawArc(new RectF(((RectF) this.itemList.get(0)).left - ((float) (this.height / 2)
        ), 0.0f, ((RectF) this.itemList.get(0)).left + ((float) (this.height / 2)), (float) this
                .height), 270.0f, -180.0f, false, this.paint);
    }

    private void drawRect(Canvas canvas) {
        for (int i = 0; i < this.itemList.size(); i++) {
            if (i < this.currentNumber) {
                initCompletedPaint();
            } else {
                initUncompletedPaint();
            }
            canvas.drawRect((RectF) this.itemList.get(i), this.paint);
        }
    }

    private void initCompletedPaint() {
        this.paint.setColor(this.completedColor);
    }

    private void initUncompletedPaint() {
        this.paint.setColor(this.uncompletedColor);
    }

    private void calculatePoints() {
        this.itemList.clear();
        int originLeft = getOriginLeft();
        int itemWidth = getItemWidth();
        for (int i = 0; i < this.totalNumber; i++) {
            this.itemList.add(new RectF((float) (((this.gapWidth + itemWidth) * i) + originLeft),
                    0.0f, (float) ((originLeft + itemWidth) + ((this.gapWidth + itemWidth) * i)),
                    (float) this.height));
        }
    }

    private int getOriginLeft() {
        if (this.itemWidth == 0) {
            return this.height;
        }
        if (((this.itemWidth * this.totalNumber) + ((this.totalNumber - 1) * this.gapWidth)) +
                (this.height * 2) >= this.width) {
            this.itemWidth = getMaxItemWidth();
        }
        return ((((this.width - ((this.totalNumber - 1) * this.gapWidth)) - (this.itemWidth *
                this.totalNumber)) - (this.height * 2)) / 2) + this.height;
    }

    private int getItemWidth() {
        return this.itemWidth == 0 ? getMaxItemWidth() : this.itemWidth;
    }

    private int getMaxItemWidth() {
        return ((this.width - (this.height * 2)) - ((this.totalNumber - 1) * this.gapWidth)) /
                this.totalNumber;
    }

    private int dip(int dip) {
        return ((int) getContext().getResources().getDisplayMetrics().density) * dip;
    }

    public void setProgress(int currentNumber, int totalNumber) {
        if (currentNumber >= 0 && totalNumber >= 0) {
            if (currentNumber <= totalNumber) {
                this.currentNumber = currentNumber;
                this.totalNumber = totalNumber;
            } else {
                this.totalNumber = totalNumber;
                this.currentNumber = totalNumber;
            }
            calculatePoints();
            invalidate();
        }
    }

    public void setItemWidth(int width) {
        this.itemWidth = width;
        calculatePoints();
        invalidate();
    }
}
