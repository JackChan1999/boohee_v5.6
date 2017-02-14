package com.prolificinteractive.materialcalendarview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

import com.prolificinteractive.materialcalendarview.MaterialCalendarView.OnDayClickListener;
import com.prolificinteractive.materialcalendarview.format.DayFormatter;
import com.prolificinteractive.materialcalendarview.format.WeekDayFormatter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

@SuppressLint({"ViewConstructor"})
class MonthView extends LinearLayout implements OnClickListener {
    protected static final int DEFAULT_DAYS_IN_WEEK      = 7;
    protected static final int DEFAULT_MAX_WEEKS         = 6;
    protected static final int DEFAULT_MONTH_TILE_HEIGHT = 7;
    private       Callbacks                  callbacks;
    private final ArrayList<DecoratorResult> decoratorResults;
    private       int                        firstDayOfWeek;
    private       CalendarDay                maxDate;
    private       CalendarDay                minDate;
    private final CalendarDay                month;
    private final ArrayList<BooheeDayView>   monthDayViews;
    private       OnDayClickListener         onDayClickListener;
    private       CalendarDay                selection;
    private       boolean                    showOtherDates;
    private final Calendar                   tempWorkingCalendar;
    private final ArrayList<WeekDayView>     weekDayViews;

    public interface Callbacks {
        void onDateChanged(CalendarDay calendarDay);
    }

    public MonthView(Context context, CalendarDay month, int firstDayOfWeek) {
        this(context, month, firstDayOfWeek, null);
    }

    public MonthView(Context context, CalendarDay month, int firstDayOfWeek, BooheeCalendarDay
            booheeCalendarDay) {
        int i;
        super(context);
        this.weekDayViews = new ArrayList();
        this.monthDayViews = new ArrayList();
        this.tempWorkingCalendar = CalendarUtils.getInstance();
        this.selection = null;
        this.minDate = null;
        this.maxDate = null;
        this.showOtherDates = false;
        this.decoratorResults = new ArrayList();
        this.month = month;
        this.firstDayOfWeek = firstDayOfWeek;
        setOrientation(1);
        setClipChildren(false);
        setClipToPadding(false);
        Calendar calendar = resetAndGetWorkingCalendar();
        LinearLayout row = makeRow(this);
        for (i = 0; i < 7; i++) {
            WeekDayView weekDayView = new WeekDayView(context, CalendarUtils.getDayOfWeek
                    (calendar));
            this.weekDayViews.add(weekDayView);
            row.addView(weekDayView, new LayoutParams(0, -1, 1.0f));
            calendar.add(5, 1);
        }
        calendar = resetAndGetWorkingCalendar();
        for (int r = 0; r < 6; r++) {
            row = makeRow(this);
            for (i = 0; i < 7; i++) {
                CalendarDay day = CalendarDay.from(calendar);
                if (booheeCalendarDay != null) {
                    day.bottomDrawableId = booheeCalendarDay.getDrawableId(day);
                    day.bottomLineColorId = booheeCalendarDay.getColorId(day);
                }
                BooheeDayView dayView = new BooheeDayView(context, day);
                dayView.setLayoutParams(new LayoutParams(-1, -1));
                dayView.setOnClickListener(this);
                this.monthDayViews.add(dayView);
                row.addView(dayView, new LayoutParams(0, -1, 1.0f));
                calendar.add(5, 1);
            }
        }
        setSelectedDate(CalendarDay.today());
    }

    void setDayViewDecorators(List<DecoratorResult> results) {
        this.decoratorResults.clear();
        if (results != null) {
            this.decoratorResults.addAll(results);
        }
        invalidateDecorators();
    }

    private static LinearLayout makeRow(LinearLayout parent) {
        LinearLayout row = new LinearLayout(parent.getContext());
        row.setOrientation(0);
        parent.addView(row, new LayoutParams(-1, 0, 1.0f));
        View v = new View(parent.getContext());
        v.setBackgroundColor(Color.parseColor("#60000000"));
        parent.addView(v, new LayoutParams(-1, 1));
        return row;
    }

    public void setWeekDayTextAppearance(int taId) {
        Iterator i$ = this.weekDayViews.iterator();
        while (i$.hasNext()) {
            ((WeekDayView) i$.next()).setTextAppearance(getContext(), taId);
        }
    }

    public void setDateTextAppearance(int taId) {
        Iterator i$ = this.monthDayViews.iterator();
        while (i$.hasNext()) {
            ((BooheeDayView) i$.next()).setTextAppearance(getContext(), taId);
        }
    }

    public void setShowOtherDates(boolean show) {
        this.showOtherDates = show;
        updateUi();
    }

    public boolean getShowOtherDates() {
        return this.showOtherDates;
    }

    public CalendarDay getMonth() {
        return this.month;
    }

    public void setSelectionColor(int color) {
        Iterator i$ = this.monthDayViews.iterator();
        while (i$.hasNext()) {
            ((BooheeDayView) i$.next()).setSelectionColor(color);
        }
    }

    private Calendar resetAndGetWorkingCalendar() {
        boolean removeRow = true;
        this.month.copyTo(this.tempWorkingCalendar);
        this.tempWorkingCalendar.setFirstDayOfWeek(this.firstDayOfWeek);
        int delta = this.firstDayOfWeek - CalendarUtils.getDayOfWeek(this.tempWorkingCalendar);
        if (this.showOtherDates) {
            if (delta < 0) {
                removeRow = false;
            }
        } else if (delta <= 0) {
            removeRow = false;
        }
        if (removeRow) {
            delta -= 7;
        }
        this.tempWorkingCalendar.add(5, delta);
        return this.tempWorkingCalendar;
    }

    public void setFirstDayOfWeek(int dayOfWeek) {
        this.firstDayOfWeek = dayOfWeek;
        Calendar calendar = resetAndGetWorkingCalendar();
        calendar.set(7, dayOfWeek);
        Iterator i$ = this.weekDayViews.iterator();
        while (i$.hasNext()) {
            ((WeekDayView) i$.next()).setDayOfWeek(calendar);
            calendar.add(5, 1);
        }
        calendar = resetAndGetWorkingCalendar();
        i$ = this.monthDayViews.iterator();
        while (i$.hasNext()) {
            ((BooheeDayView) i$.next()).setDay(CalendarDay.from(calendar));
            calendar.add(5, 1);
        }
        updateUi();
    }

    public void setWeekDayFormatter(WeekDayFormatter formatter) {
        Iterator i$ = this.weekDayViews.iterator();
        while (i$.hasNext()) {
            ((WeekDayView) i$.next()).setWeekDayFormatter(formatter);
        }
    }

    public void setDayFormatter(DayFormatter formatter) {
        Iterator i$ = this.monthDayViews.iterator();
        while (i$.hasNext()) {
            ((BooheeDayView) i$.next()).setDayFormatter(formatter);
        }
    }

    public void setMinimumDate(CalendarDay minDate) {
        this.minDate = minDate;
        updateUi();
    }

    public void setMaximumDate(CalendarDay maxDate) {
        this.maxDate = maxDate;
        updateUi();
    }

    public void setSelectedDate(CalendarDay cal) {
        this.selection = cal;
        updateUi();
    }

    private void updateUi() {
        int ourMonth = this.month.getMonth();
        Iterator i$ = this.monthDayViews.iterator();
        while (i$.hasNext()) {
            BooheeDayView dayView = (BooheeDayView) i$.next();
            CalendarDay day = dayView.getDate();
            dayView.setupSelection(this.showOtherDates, day.isInRange(this.minDate, this.maxDate)
                    , day.getMonth() == ourMonth);
            dayView.setChecked(day.equals(this.selection));
        }
        postInvalidate();
    }

    private void invalidateDecorators() {
        DayViewFacade facadeAccumulator = new DayViewFacade();
        Iterator it = this.monthDayViews.iterator();
        while (it.hasNext()) {
            BooheeDayView dayView = (BooheeDayView) it.next();
            facadeAccumulator.reset();
            Iterator i$ = this.decoratorResults.iterator();
            while (i$.hasNext()) {
                DecoratorResult result = (DecoratorResult) i$.next();
                if (result.decorator.shouldDecorate(dayView.getDate())) {
                    result.result.applyTo(facadeAccumulator);
                }
            }
            dayView.applyFacade(facadeAccumulator);
        }
    }

    public void setCallbacks(Callbacks callbacks) {
        this.callbacks = callbacks;
    }

    public void onClick(View v) {
        if (v instanceof BooheeDayView) {
            Iterator i$ = this.monthDayViews.iterator();
            while (i$.hasNext()) {
                ((BooheeDayView) i$.next()).setChecked(false);
            }
            BooheeDayView dayView = (BooheeDayView) v;
            dayView.setChecked(true);
            this.selection = dayView.getDate();
            if (this.callbacks != null) {
                this.callbacks.onDateChanged(dayView.getDate());
            }
            if (this.onDayClickListener != null) {
                this.onDayClickListener.onClick((BooheeDayView) v);
            }
        }
    }

    public void setOnDayClickListener(OnDayClickListener listener) {
        this.onDayClickListener = listener;
    }

    public void invalidateDays(BooheeCalendarDay booheeCalendarDay) {
        if (booheeCalendarDay != null && this.monthDayViews != null && this.monthDayViews.size()
                != 0) {
            Iterator i$ = this.monthDayViews.iterator();
            while (i$.hasNext()) {
                BooheeDayView dayView = (BooheeDayView) i$.next();
                CalendarDay day = dayView.getDate();
                dayView.setImageTag(booheeCalendarDay.getDrawableId(day));
                dayView.setLineColor(booheeCalendarDay.getColorId(day));
            }
        }
    }
}
