package com.boohee.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.graphics.Shader;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

import com.boohee.one.R;

public class ProgressWheel extends View {
    private int   barColor  = -1442840576;
    private int   barLength = 60;
    private Paint barPaint  = new Paint();
    private int   barWidth  = 20;
    private String centerText;
    private RectF circleBounds = new RectF();
    private int   circleColor  = 0;
    private Paint circlePaint  = new Paint();
    private int   circleRadius = 80;
    private int   delayMillis  = 0;
    private int   fullRadius   = 100;
    boolean isSpinning = false;
    private boolean isTextCenter  = false;
    private int     layout_height = 0;
    private int     layout_width  = 0;
    private int     paddingBottom = 5;
    private int     paddingLeft   = 5;
    private int     paddingRight  = 5;
    private int     paddingTop    = 5;
    int progress = 0;
    private RectF    rectBounds  = new RectF();
    private int      rimColor    = -1428300323;
    private Paint    rimPaint    = new Paint();
    private int      rimWidth    = 20;
    private Handler  spinHandler = new Handler() {
        public void handleMessage(Message msg) {
            ProgressWheel.this.invalidate();
            if (ProgressWheel.this.isSpinning) {
                ProgressWheel progressWheel = ProgressWheel.this;
                progressWheel.progress += ProgressWheel.this.spinSpeed;
                if (ProgressWheel.this.progress > 360) {
                    ProgressWheel.this.progress = 0;
                }
                ProgressWheel.this.spinHandler.sendEmptyMessageDelayed(0, (long) ProgressWheel
                        .this.delayMillis);
            }
        }
    };
    private int      spinSpeed   = 2;
    private String[] splitText   = new String[0];
    private String   text        = "";
    private int      textColor   = -16777216;
    private Paint    textPaint   = new Paint();
    private int      textSize    = 20;

    public ProgressWheel(Context context, AttributeSet attrs) {
        super(context, attrs);
        parseAttributes(context.obtainStyledAttributes(attrs, R.styleable.ProgressWheel));
    }

    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.layout_width = w;
        this.layout_height = h;
        setupBounds();
        setupPaints();
        invalidate();
    }

    private void setupPaints() {
        this.barPaint.setColor(this.barColor);
        this.barPaint.setAntiAlias(true);
        this.barPaint.setStyle(Style.STROKE);
        this.barPaint.setStrokeWidth((float) this.barWidth);
        this.rimPaint.setColor(this.rimColor);
        this.rimPaint.setAntiAlias(true);
        this.rimPaint.setStyle(Style.STROKE);
        this.rimPaint.setStrokeWidth((float) this.rimWidth);
        this.circlePaint.setColor(this.circleColor);
        this.circlePaint.setAntiAlias(true);
        this.circlePaint.setStyle(Style.FILL);
        this.textPaint.setColor(this.textColor);
        this.textPaint.setStyle(Style.FILL);
        this.textPaint.setAntiAlias(true);
        this.textPaint.setTextSize((float) this.textSize);
    }

    private void setupBounds() {
        int minValue = Math.min(this.layout_width, this.layout_height);
        int xOffset = this.layout_width - minValue;
        int yOffset = this.layout_height - minValue;
        this.paddingTop = getPaddingTop() + (yOffset / 2);
        this.paddingBottom = getPaddingBottom() + (yOffset / 2);
        this.paddingLeft = getPaddingLeft() + (xOffset / 2);
        this.paddingRight = getPaddingRight() + (xOffset / 2);
        this.rectBounds = new RectF((float) this.paddingLeft, (float) this.paddingTop, (float)
                (getLayoutParams().width - this.paddingRight), (float) (getLayoutParams().height
                - this.paddingBottom));
        this.circleBounds = new RectF((float) (this.paddingLeft + this.barWidth), (float) (this
                .paddingTop + this.barWidth), (float) ((getLayoutParams().width - this
                .paddingRight) - this.barWidth), (float) ((getLayoutParams().height - this
                .paddingBottom) - this.barWidth));
        this.fullRadius = ((getLayoutParams().width - this.paddingRight) - this.barWidth) / 2;
        this.circleRadius = (this.fullRadius - this.barWidth) + 1;
    }

    private void parseAttributes(TypedArray a) {
        this.barWidth = (int) a.getDimension(9, (float) this.barWidth);
        this.rimWidth = (int) a.getDimension(5, (float) this.rimWidth);
        this.spinSpeed = (int) a.getDimension(6, (float) this.spinSpeed);
        this.delayMillis = a.getInteger(7, this.delayMillis);
        if (this.delayMillis < 0) {
            this.delayMillis = 0;
        }
        this.barColor = a.getColor(3, this.barColor);
        this.barLength = (int) a.getDimension(10, (float) this.barLength);
        this.textSize = (int) a.getDimension(2, (float) this.textSize);
        this.textColor = a.getColor(1, this.textColor);
        if (a.hasValue(0)) {
            setText(a.getString(0));
        }
        this.rimColor = a.getColor(4, this.rimColor);
        this.circleColor = a.getColor(8, this.circleColor);
        a.recycle();
    }

    protected void onDraw(Canvas canvas) {
        int i = 0;
        super.onDraw(canvas);
        canvas.drawArc(this.circleBounds, 360.0f, 360.0f, false, this.rimPaint);
        if (this.isSpinning) {
            canvas.drawArc(this.circleBounds, (float) (this.progress - 90), (float) this
                    .barLength, false, this.barPaint);
        } else {
            canvas.drawArc(this.circleBounds, -90.0f, (float) this.progress, false, this.barPaint);
        }
        canvas.drawCircle(((this.circleBounds.width() / 2.0f) + ((float) this.rimWidth)) + (
                (float) this.paddingLeft), ((this.circleBounds.height() / 2.0f) + ((float) this
                .rimWidth)) + ((float) this.paddingTop), (float) this.circleRadius, this
                .circlePaint);
        int offsetNum = 0;
        String[] strArr = this.splitText;
        int length = strArr.length;
        while (i < length) {
            String s = strArr[i];
            canvas.drawText(s, ((float) (getWidth() / 2)) - (this.textPaint.measureText(s) / 2
            .0f), (float) (((getHeight() / 2) + (this.textSize * offsetNum)) - ((this.splitText
                    .length - 1) * (this.textSize / 2))), this.textPaint);
            offsetNum++;
            i++;
        }
        if (!TextUtils.isEmpty(this.centerText) && this.isTextCenter) {
            canvas.drawText(this.centerText, (float) ((int) (((float) (canvas.getWidth() / 2)) -
                    (this.textPaint.measureText(this.centerText) / 2.0f))), (float) ((int) ((
                    (float) (canvas.getHeight() / 2)) - ((this.textPaint.descent() + this
                    .textPaint.ascent()) / 2.0f))), this.textPaint);
        }
    }

    public void resetCount() {
        this.progress = 0;
        setText("0%");
        invalidate();
    }

    public void stopSpinning() {
        this.isSpinning = false;
        this.progress = 0;
        this.spinHandler.removeMessages(0);
    }

    public void spin() {
        this.isSpinning = true;
        this.spinHandler.sendEmptyMessage(0);
    }

    public void incrementProgress() {
        this.isSpinning = false;
        this.progress++;
        setText(Math.round((((float) this.progress) / 360.0f) * 100.0f) + "%");
        this.spinHandler.sendEmptyMessage(0);
    }

    public void setProgress(int i) {
        this.isSpinning = false;
        this.progress = i;
        this.spinHandler.sendEmptyMessage(0);
    }

    public void setText(String text) {
        this.text = text;
        this.splitText = this.text.split("\n");
    }

    public void setTextInCenter(String text) {
        this.isTextCenter = true;
        this.centerText = text;
    }

    public int getCircleRadius() {
        return this.circleRadius;
    }

    public void setCircleRadius(int circleRadius) {
        this.circleRadius = circleRadius;
    }

    public int getBarLength() {
        return this.barLength;
    }

    public void setBarLength(int barLength) {
        this.barLength = barLength;
    }

    public int getBarWidth() {
        return this.barWidth;
    }

    public void setBarWidth(int barWidth) {
        this.barWidth = barWidth;
    }

    public int getTextSize() {
        return this.textSize;
    }

    public void setTextSize(int textSize) {
        this.textSize = textSize;
    }

    public int getPaddingTop() {
        return this.paddingTop;
    }

    public void setPaddingTop(int paddingTop) {
        this.paddingTop = paddingTop;
    }

    public int getPaddingBottom() {
        return this.paddingBottom;
    }

    public void setPaddingBottom(int paddingBottom) {
        this.paddingBottom = paddingBottom;
    }

    public int getPaddingLeft() {
        return this.paddingLeft;
    }

    public void setPaddingLeft(int paddingLeft) {
        this.paddingLeft = paddingLeft;
    }

    public int getPaddingRight() {
        return this.paddingRight;
    }

    public void setPaddingRight(int paddingRight) {
        this.paddingRight = paddingRight;
    }

    public int getBarColor() {
        return this.barColor;
    }

    public void setBarColor(int barColor) {
        this.barColor = barColor;
    }

    public int getCircleColor() {
        return this.circleColor;
    }

    public void setCircleColor(int circleColor) {
        this.circleColor = circleColor;
    }

    public int getRimColor() {
        return this.rimColor;
    }

    public void setRimColor(int rimColor) {
        this.rimColor = rimColor;
    }

    public Shader getRimShader() {
        return this.rimPaint.getShader();
    }

    public void setRimShader(Shader shader) {
        this.rimPaint.setShader(shader);
    }

    public int getTextColor() {
        return this.textColor;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
    }

    public int getSpinSpeed() {
        return this.spinSpeed;
    }

    public void setSpinSpeed(int spinSpeed) {
        this.spinSpeed = spinSpeed;
    }

    public int getRimWidth() {
        return this.rimWidth;
    }

    public void setRimWidth(int rimWidth) {
        this.rimWidth = rimWidth;
    }

    public int getDelayMillis() {
        return this.delayMillis;
    }

    public void setDelayMillis(int delayMillis) {
        this.delayMillis = delayMillis;
    }
}
