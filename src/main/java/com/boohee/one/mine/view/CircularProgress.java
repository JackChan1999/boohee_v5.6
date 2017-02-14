package com.boohee.one.mine.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.boohee.model.PeriodRecord;
import com.boohee.model.User;
import com.boohee.model.mine.MonthMc.Section;
import com.boohee.one.R;
import com.boohee.utils.CalendarUtil;

import java.util.ArrayList;
import java.util.Iterator;

public class CircularProgress extends View {
    static final String TAG = CircularProgress.class.getSimpleName();
    private int                mCircularWidth;
    private int                mDefaultColor;
    private int                mMax;
    private Paint              mPaint;
    private int                mPeriodColor;
    private int                mPregnancyColor;
    private ArrayList<Section> mSections;
    private User               user;

    public CircularProgress(Context context) {
        this(context, null);
    }

    public CircularProgress(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircularProgress(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mPaint = new Paint();
        this.mMax = CalendarUtil.getMonthDays();
        this.mDefaultColor = getResources().getColor(R.color.mc_default);
        this.mPeriodColor = getResources().getColor(R.color.mc_period);
        this.mPregnancyColor = getResources().getColor(R.color.mc_oviposite);
        this.mCircularWidth = 8;
        this.mPaint.setStyle(Style.STROKE);
        this.mPaint.setStrokeWidth((float) this.mCircularWidth);
        this.mPaint.setAntiAlias(true);
        setDrawingCacheEnabled(true);
    }

    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        invalidate();
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int centerX = getWidth() / 2;
        int radius = centerX - (this.mCircularWidth / 2);
        this.mPaint.setColor(this.mDefaultColor);
        canvas.drawCircle((float) centerX, (float) centerX, (float) radius, this.mPaint);
        if (this.user != null && !this.user.isMale() && this.mSections != null && this.mSections
                .size() > 0) {
            RectF oval = new RectF((float) (centerX - radius), (float) (centerX - radius),
                    (float) (centerX + radius), (float) (centerX + radius));
            Iterator it = this.mSections.iterator();
            while (it.hasNext()) {
                Section section = (Section) it.next();
                if ("mc".equals(section.type)) {
                    this.mPaint.setColor(this.mPeriodColor);
                } else if (PeriodRecord.PREGNACY.equals(section.type)) {
                    this.mPaint.setColor(this.mPregnancyColor);
                } else {
                    this.mPaint.setColor(this.mDefaultColor);
                }
                canvas.drawArc(oval, ((((float) (section.start - 1)) * 360.0f) / ((float) this
                        .mMax)) - 90.0f, (((float) ((section.end - section.start) + 1)) * 360.0f)
                        / ((float) this.mMax), false, this.mPaint);
            }
        }
    }

    public void setSections(ArrayList<Section> sections) {
        this.mSections = sections;
        invalidate();
    }

    public void setUser(User user) {
        this.user = user;
        invalidate();
    }
}
