package com.prolificinteractive.materialcalendarview;

import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;

import com.prolificinteractive.materialcalendarview.format.DayFormatter;

public class BooheeDayView extends FrameLayout {
    private ImageView   imageTag;
    private View        lineView;
    private CalendarDay mCalDay;
    private DayView     mDayView;

    public BooheeDayView(Context context, CalendarDay day) {
        super(context);
        this.mCalDay = day;
        View rootView = inflate(getContext(), R.layout.item_day_view, null);
        addView(rootView, new LayoutParams(-1, -1));
        findView(rootView);
        init();
    }

    private void findView(View rootView) {
        this.mDayView = (DayView) rootView.findViewById(R.id.day_view);
        this.imageTag = (ImageView) rootView.findViewById(R.id.image_tag);
        this.lineView = rootView.findViewById(R.id.v_line);
    }

    private void init() {
        this.mDayView.setDay(this.mCalDay);
        setLineColor(this.mCalDay.bottomLineColorId);
        setImageTag(this.mCalDay.bottomDrawableId);
    }

    public void setTextAppearance(Context context, int taId) {
        this.mDayView.setTextAppearance(context, taId);
    }

    public void setSelectionColor(int selectionColor) {
        this.mDayView.setSelectionColor(selectionColor);
    }

    public void setDay(CalendarDay day) {
        this.mCalDay = day;
        this.mDayView.setDay(day);
    }

    public void setDayFormatter(DayFormatter dayFormatter) {
        this.mDayView.setDayFormatter(dayFormatter);
    }

    public CalendarDay getDate() {
        return this.mDayView.getDate();
    }

    public void setupSelection(boolean showOtherDates, boolean inRange, boolean b) {
        this.mDayView.setupSelection(showOtherDates, inRange, b);
    }

    public void setChecked(boolean checked) {
        this.mDayView.setChecked(checked);
    }

    public void applyFacade(DayViewFacade facadeAccumulator) {
        this.mDayView.applyFacade(facadeAccumulator);
    }

    public void setLineColor(int color) {
        if (this.lineView != null) {
            if (color == -1) {
                this.lineView.setVisibility(8);
                return;
            }
            this.lineView.setVisibility(0);
            this.lineView.setBackgroundColor(color);
        }
    }

    public void setImageTag(int drawableId) {
        if (this.imageTag != null) {
            if (drawableId == -1) {
                this.imageTag.setVisibility(8);
                return;
            }
            this.imageTag.setVisibility(0);
            this.imageTag.setBackgroundResource(drawableId);
        }
    }
}
