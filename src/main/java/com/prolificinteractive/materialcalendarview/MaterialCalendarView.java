package com.prolificinteractive.materialcalendarview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.support.annotation.ArrayRes;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v4.view.ViewPager.PageTransformer;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.SparseArray;
import android.util.TypedValue;
import android.view.View;
import android.view.View.BaseSavedState;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.prolificinteractive.materialcalendarview.MonthView.Callbacks;
import com.prolificinteractive.materialcalendarview.format.ArrayWeekDayFormatter;
import com.prolificinteractive.materialcalendarview.format.DateFormatTitleFormatter;
import com.prolificinteractive.materialcalendarview.format.DayFormatter;
import com.prolificinteractive.materialcalendarview.format.MonthArrayTitleFormatter;
import com.prolificinteractive.materialcalendarview.format.TitleFormatter;
import com.prolificinteractive.materialcalendarview.format.WeekDayFormatter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class MaterialCalendarView extends FrameLayout {
    public static final  int            DEFAULT_TILE_SIZE_DP    = 44;
    private static final TitleFormatter DEFAULT_TITLE_FORMATTER = new DateFormatTitleFormatter();
    private       int                         accentColor;
    private final MonthPagerAdapter           adapter;
    private       int                         arrowColor;
    private       BooheeCalendarDay           booheeCalendarDay;
    private final DirectionButton             buttonFuture;
    private final DirectionButton             buttonPast;
    private       CalendarDay                 currentMonth;
    private final ArrayList<DayViewDecorator> dayViewDecorators;
    private       Drawable                    leftArrowMask;
    private       OnDateChangedListener       listener;
    private       CalendarDay                 maxDate;
    private       CalendarDay                 minDate;
    private       OnMonthChangedListener      monthListener;
    private final Callbacks                   monthViewCallbacks;
    private final OnClickListener             onClickListener;
    private       OnDayClickListener          onDayClickListener;
    private final OnPageChangeListener        pageChangeListener;
    private final ViewPager                   pager;
    private       Drawable                    rightArrowMask;
    private       LinearLayout                root;
    private final TextView                    title;
    private final TitleChanger                titleChanger;
    private       LinearLayout                topbar;

    private class MonthPagerAdapter extends PagerAdapter {
        private       Callbacks              callbacks;
        private       Integer                color;
        private final LinkedList<MonthView>  currentViews;
        private       Integer                dateTextAppearance;
        private       DayFormatter           dayFormatter;
        private       List<DecoratorResult>  decoratorResults;
        private       List<DayViewDecorator> decorators;
        private       int                    firstDayOfTheWeek;
        private       CalendarDay            maxDate;
        private       CalendarDay            minDate;
        private final ArrayList<CalendarDay> months;
        private       CalendarDay            selectedDate;
        private       Boolean                showOtherDates;
        private final MaterialCalendarView   view;
        private       WeekDayFormatter       weekDayFormatter;
        private       Integer                weekDayTextAppearance;

        private MonthPagerAdapter(MaterialCalendarView view) {
            this.callbacks = null;
            this.color = null;
            this.dateTextAppearance = null;
            this.weekDayTextAppearance = null;
            this.showOtherDates = null;
            this.minDate = null;
            this.maxDate = null;
            this.selectedDate = null;
            this.weekDayFormatter = WeekDayFormatter.DEFAULT;
            this.dayFormatter = DayFormatter.DEFAULT;
            this.decorators = null;
            this.decoratorResults = null;
            this.firstDayOfTheWeek = 1;
            this.view = view;
            this.currentViews = new LinkedList();
            this.months = new ArrayList();
            setRangeDates(null, null);
        }

        public void setDecorators(List<DayViewDecorator> decorators) {
            if (decorators != null && decorators.size() != 0) {
                this.decorators = decorators;
                invalidateDecorators();
            }
        }

        public void invalidateDecorators() {
            if (this.decorators != null && this.decorators.size() != 0) {
                this.decoratorResults = new ArrayList();
                for (DayViewDecorator decorator : this.decorators) {
                    DayViewFacade facade = new DayViewFacade();
                    decorator.decorate(facade);
                    if (facade.isDecorated()) {
                        this.decoratorResults.add(new DecoratorResult(decorator, facade));
                    }
                }
                Iterator i$ = this.currentViews.iterator();
                while (i$.hasNext()) {
                    ((MonthView) i$.next()).setDayViewDecorators(this.decoratorResults);
                }
            }
        }

        public void invalidateDays() {
            Iterator i$ = this.currentViews.iterator();
            while (i$.hasNext()) {
                ((MonthView) i$.next()).invalidateDays(MaterialCalendarView.this.booheeCalendarDay);
            }
        }

        public int getCount() {
            return this.months.size();
        }

        public int getIndexForDay(CalendarDay day) {
            if (day == null) {
                return getCount() / 2;
            }
            if (this.minDate != null && day.isBefore(this.minDate)) {
                return 0;
            }
            if (this.maxDate != null && day.isAfter(this.maxDate)) {
                return getCount() - 1;
            }
            for (int i = 0; i < this.months.size(); i++) {
                CalendarDay month = (CalendarDay) this.months.get(i);
                if (day.getYear() == month.getYear() && day.getMonth() == month.getMonth()) {
                    return i;
                }
            }
            return getCount() / 2;
        }

        public int getItemPosition(Object object) {
            if (!(object instanceof MonthView)) {
                return -2;
            }
            CalendarDay month = ((MonthView) object).getMonth();
            if (month == null) {
                return -2;
            }
            int index = this.months.indexOf(month);
            if (index < 0) {
                return -2;
            }
            return index;
        }

        public Object instantiateItem(ViewGroup container, int position) {
            MonthView monthView = new MonthView(container.getContext(), (CalendarDay) this.months
                    .get(position), this.firstDayOfTheWeek, MaterialCalendarView.this
                    .booheeCalendarDay);
            monthView.setWeekDayFormatter(this.weekDayFormatter);
            monthView.setDayFormatter(this.dayFormatter);
            monthView.setCallbacks(this.callbacks);
            monthView.setOnDayClickListener(MaterialCalendarView.this.onDayClickListener);
            if (this.color != null) {
                monthView.setSelectionColor(this.color.intValue());
            }
            if (this.dateTextAppearance != null) {
                monthView.setDateTextAppearance(this.dateTextAppearance.intValue());
            }
            if (this.weekDayTextAppearance != null) {
                monthView.setWeekDayTextAppearance(this.weekDayTextAppearance.intValue());
            }
            if (this.showOtherDates != null) {
                monthView.setShowOtherDates(this.showOtherDates.booleanValue());
            }
            monthView.setMinimumDate(this.minDate);
            monthView.setMaximumDate(this.maxDate);
            monthView.setSelectedDate(this.selectedDate);
            container.addView(monthView);
            this.currentViews.add(monthView);
            monthView.setDayViewDecorators(this.decoratorResults);
            return monthView;
        }

        public void setFirstDayOfWeek(int day) {
            this.firstDayOfTheWeek = day;
            Iterator i$ = this.currentViews.iterator();
            while (i$.hasNext()) {
                ((MonthView) i$.next()).setFirstDayOfWeek(this.firstDayOfTheWeek);
            }
        }

        public void destroyItem(ViewGroup container, int position, Object object) {
            MonthView monthView = (MonthView) object;
            this.currentViews.remove(monthView);
            container.removeView(monthView);
        }

        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        public void setCallbacks(Callbacks callbacks) {
            this.callbacks = callbacks;
            Iterator i$ = this.currentViews.iterator();
            while (i$.hasNext()) {
                ((MonthView) i$.next()).setCallbacks(callbacks);
            }
        }

        public void setSelectionColor(int color) {
            this.color = Integer.valueOf(color);
            Iterator i$ = this.currentViews.iterator();
            while (i$.hasNext()) {
                ((MonthView) i$.next()).setSelectionColor(color);
            }
        }

        public void setDateTextAppearance(int taId) {
            if (taId != 0) {
                this.dateTextAppearance = Integer.valueOf(taId);
                Iterator i$ = this.currentViews.iterator();
                while (i$.hasNext()) {
                    ((MonthView) i$.next()).setDateTextAppearance(taId);
                }
            }
        }

        public void setShowOtherDates(boolean show) {
            this.showOtherDates = Boolean.valueOf(show);
            Iterator i$ = this.currentViews.iterator();
            while (i$.hasNext()) {
                ((MonthView) i$.next()).setShowOtherDates(show);
            }
        }

        public void setWeekDayFormatter(WeekDayFormatter formatter) {
            this.weekDayFormatter = formatter;
            Iterator i$ = this.currentViews.iterator();
            while (i$.hasNext()) {
                ((MonthView) i$.next()).setWeekDayFormatter(formatter);
            }
        }

        public void setDayFormatter(DayFormatter formatter) {
            this.dayFormatter = formatter;
            Iterator i$ = this.currentViews.iterator();
            while (i$.hasNext()) {
                ((MonthView) i$.next()).setDayFormatter(formatter);
            }
        }

        public boolean getShowOtherDates() {
            return this.showOtherDates.booleanValue();
        }

        public void setWeekDayTextAppearance(int taId) {
            if (taId != 0) {
                this.weekDayTextAppearance = Integer.valueOf(taId);
                Iterator i$ = this.currentViews.iterator();
                while (i$.hasNext()) {
                    ((MonthView) i$.next()).setWeekDayTextAppearance(taId);
                }
            }
        }

        public void setRangeDates(CalendarDay min, CalendarDay max) {
            Calendar worker;
            this.minDate = min;
            this.maxDate = max;
            Iterator i$ = this.currentViews.iterator();
            while (i$.hasNext()) {
                MonthView monthView = (MonthView) i$.next();
                monthView.setMinimumDate(min);
                monthView.setMaximumDate(max);
            }
            if (min == null) {
                worker = CalendarUtils.getInstance();
                worker.add(1, -200);
                min = CalendarDay.from(worker);
            }
            if (max == null) {
                worker = CalendarUtils.getInstance();
                worker.add(1, 200);
                max = CalendarDay.from(worker);
            }
            worker = CalendarUtils.getInstance();
            min.copyTo(worker);
            CalendarUtils.setToFirstDay(worker);
            this.months.clear();
            for (CalendarDay workingMonth = CalendarDay.from(worker); !max.isBefore(workingMonth)
                    ; workingMonth = CalendarDay.from(worker)) {
                this.months.add(CalendarDay.from(worker));
                worker.add(2, 1);
            }
            CalendarDay prevDate = this.selectedDate;
            notifyDataSetChanged();
            setSelectedDate(prevDate);
            if (prevDate != null && !prevDate.equals(this.selectedDate)) {
                this.callbacks.onDateChanged(this.selectedDate);
            }
        }

        public void setSelectedDate(@Nullable CalendarDay date) {
            CalendarDay prevDate = this.selectedDate;
            this.selectedDate = getValidSelectedDate(date);
            Iterator i$ = this.currentViews.iterator();
            while (i$.hasNext()) {
                ((MonthView) i$.next()).setSelectedDate(this.selectedDate);
            }
            if (date == null && prevDate != null) {
                this.callbacks.onDateChanged(null);
            }
        }

        private CalendarDay getValidSelectedDate(CalendarDay date) {
            if (date == null) {
                return null;
            }
            if (this.minDate != null && this.minDate.isAfter(date)) {
                return this.minDate;
            }
            if (this.maxDate == null || !this.maxDate.isBefore(date)) {
                return date;
            }
            return this.maxDate;
        }

        public CalendarDay getItem(int position) {
            return (CalendarDay) this.months.get(position);
        }

        public CalendarDay getSelectedDate() {
            return this.selectedDate;
        }

        protected int getDateTextAppearance() {
            return this.dateTextAppearance == null ? 0 : this.dateTextAppearance.intValue();
        }

        protected int getWeekDayTextAppearance() {
            return this.weekDayTextAppearance == null ? 0 : this.weekDayTextAppearance.intValue();
        }

        public int getFirstDayOfWeek() {
            return this.firstDayOfTheWeek;
        }
    }

    public interface OnDayClickListener {
        void onClick(BooheeDayView booheeDayView);
    }

    public static class SavedState extends BaseSavedState {
        public static final Creator<SavedState> CREATOR = new Creator<SavedState>() {
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
        int         color;
        int         dateTextAppearance;
        int         firstDayOfWeek;
        CalendarDay maxDate;
        CalendarDay minDate;
        CalendarDay selectedDate;
        boolean     showOtherDates;
        int         tileSizePx;
        boolean     topbarVisible;
        int         weekDayTextAppearance;

        SavedState(Parcelable superState) {
            super(superState);
            this.color = 0;
            this.dateTextAppearance = 0;
            this.weekDayTextAppearance = 0;
            this.showOtherDates = false;
            this.minDate = null;
            this.maxDate = null;
            this.selectedDate = null;
            this.firstDayOfWeek = 1;
            this.tileSizePx = 0;
            this.topbarVisible = true;
        }

        public void writeToParcel(Parcel out, int flags) {
            int i = 1;
            super.writeToParcel(out, flags);
            out.writeInt(this.color);
            out.writeInt(this.dateTextAppearance);
            out.writeInt(this.weekDayTextAppearance);
            out.writeInt(this.showOtherDates ? 1 : 0);
            out.writeParcelable(this.minDate, 0);
            out.writeParcelable(this.maxDate, 0);
            out.writeParcelable(this.selectedDate, 0);
            out.writeInt(this.firstDayOfWeek);
            out.writeInt(this.tileSizePx);
            if (!this.topbarVisible) {
                i = 0;
            }
            out.writeInt(i);
        }

        private SavedState(Parcel in) {
            boolean z;
            boolean z2 = true;
            super(in);
            this.color = 0;
            this.dateTextAppearance = 0;
            this.weekDayTextAppearance = 0;
            this.showOtherDates = false;
            this.minDate = null;
            this.maxDate = null;
            this.selectedDate = null;
            this.firstDayOfWeek = 1;
            this.tileSizePx = 0;
            this.topbarVisible = true;
            this.color = in.readInt();
            this.dateTextAppearance = in.readInt();
            this.weekDayTextAppearance = in.readInt();
            if (in.readInt() == 1) {
                z = true;
            } else {
                z = false;
            }
            this.showOtherDates = z;
            ClassLoader loader = CalendarDay.class.getClassLoader();
            this.minDate = (CalendarDay) in.readParcelable(loader);
            this.maxDate = (CalendarDay) in.readParcelable(loader);
            this.selectedDate = (CalendarDay) in.readParcelable(loader);
            this.firstDayOfWeek = in.readInt();
            this.tileSizePx = in.readInt();
            if (in.readInt() != 1) {
                z2 = false;
            }
            this.topbarVisible = z2;
        }
    }

    public MaterialCalendarView(Context context) {
        this(context, null);
    }

    public MaterialCalendarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.dayViewDecorators = new ArrayList();
        this.monthViewCallbacks = new Callbacks() {
            public void onDateChanged(CalendarDay date) {
                MaterialCalendarView.this.setSelectedDate(date);
                if (MaterialCalendarView.this.listener != null) {
                    MaterialCalendarView.this.listener.onDateChanged(MaterialCalendarView.this,
                            date);
                }
            }
        };
        this.onClickListener = new OnClickListener() {
            public void onClick(View v) {
                if (v == MaterialCalendarView.this.buttonFuture) {
                    MaterialCalendarView.this.pager.setCurrentItem(MaterialCalendarView.this
                            .pager.getCurrentItem() + 1, true);
                } else if (v == MaterialCalendarView.this.buttonPast) {
                    MaterialCalendarView.this.pager.setCurrentItem(MaterialCalendarView.this
                            .pager.getCurrentItem() - 1, true);
                }
            }
        };
        this.pageChangeListener = new OnPageChangeListener() {
            public void onPageSelected(int position) {
                MaterialCalendarView.this.titleChanger.setPreviousMonth(MaterialCalendarView.this
                        .currentMonth);
                MaterialCalendarView.this.currentMonth = MaterialCalendarView.this.adapter
                        .getItem(position);
                MaterialCalendarView.this.updateUi();
                if (MaterialCalendarView.this.monthListener != null) {
                    MaterialCalendarView.this.monthListener.onMonthChanged(MaterialCalendarView
                            .this, MaterialCalendarView.this.currentMonth);
                }
            }

            public void onPageScrollStateChanged(int state) {
            }

            public void onPageScrolled(int position, float positionOffset, int
                    positionOffsetPixels) {
            }
        };
        this.minDate = null;
        this.maxDate = null;
        this.accentColor = 0;
        this.arrowColor = -16777216;
        setClipChildren(false);
        setClipToPadding(false);
        this.buttonPast = new DirectionButton(getContext());
        this.title = new TextView(getContext());
        this.buttonFuture = new DirectionButton(getContext());
        this.pager = new ViewPager(getContext());
        setupChildren();
        this.title.setOnClickListener(this.onClickListener);
        this.buttonPast.setOnClickListener(this.onClickListener);
        this.buttonFuture.setOnClickListener(this.onClickListener);
        this.titleChanger = new TitleChanger(this.title);
        this.titleChanger.setTitleFormatter(DEFAULT_TITLE_FORMATTER);
        this.adapter = new MonthPagerAdapter(this);
        this.pager.setAdapter(this.adapter);
        this.pager.setOnPageChangeListener(this.pageChangeListener);
        this.pager.setPageTransformer(false, new PageTransformer() {
            public void transformPage(View page, float position) {
                page.setAlpha((float) Math.sqrt((double) (1.0f - Math.abs(position))));
            }
        });
        this.adapter.setCallbacks(this.monthViewCallbacks);
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable
                .MaterialCalendarView, 0, 0);
        try {
            int tileSize = a.getDimensionPixelSize(R.styleable.MaterialCalendarView_mcv_tileSize,
                    -1);
            if (tileSize > 0) {
                setTileSize(tileSize);
            }
            setArrowColor(a.getColor(R.styleable.MaterialCalendarView_mcv_arrowColor, -16777216));
            Drawable leftMask = a.getDrawable(R.styleable.MaterialCalendarView_mcv_leftArrowMask);
            if (leftMask == null) {
                leftMask = getResources().getDrawable(R.drawable.mcv_action_previous);
            }
            setLeftArrowMask(leftMask);
            Drawable rightMask = a.getDrawable(R.styleable.MaterialCalendarView_mcv_rightArrowMask);
            if (rightMask == null) {
                rightMask = getResources().getDrawable(R.drawable.mcv_action_next);
            }
            setRightArrowMask(rightMask);
            setSelectionColor(a.getColor(R.styleable.MaterialCalendarView_mcv_selectionColor,
                    getThemeAccentColor(context)));
            CharSequence[] array = a.getTextArray(R.styleable
                    .MaterialCalendarView_mcv_weekDayLabels);
            if (array != null) {
                setWeekDayFormatter(new ArrayWeekDayFormatter(array));
            }
            array = a.getTextArray(R.styleable.MaterialCalendarView_mcv_monthLabels);
            if (array != null) {
                setTitleFormatter(new MonthArrayTitleFormatter(array));
            }
            setHeaderTextAppearance(a.getResourceId(R.styleable
                    .MaterialCalendarView_mcv_headerTextAppearance, R.style
                    .TextAppearance_MaterialCalendarWidget_Header));
            setWeekDayTextAppearance(a.getResourceId(R.styleable
                    .MaterialCalendarView_mcv_weekDayTextAppearance, R.style
                    .TextAppearance_MaterialCalendarWidget_WeekDay));
            setDateTextAppearance(a.getResourceId(R.styleable
                    .MaterialCalendarView_mcv_dateTextAppearance, R.style
                    .TextAppearance_MaterialCalendarWidget_Date));
            setShowOtherDates(a.getBoolean(R.styleable.MaterialCalendarView_mcv_showOtherDates,
                    false));
            setFirstDayOfWeek(a.getInt(R.styleable.MaterialCalendarView_mcv_firstDayOfWeek, 1));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            a.recycle();
        }
        this.currentMonth = CalendarDay.today();
        setCurrentDate(this.currentMonth);
    }

    private void setupChildren() {
        int tileSize = (int) TypedValue.applyDimension(1, 44.0f, getResources().getDisplayMetrics
                ());
        this.root = new LinearLayout(getContext());
        this.root.setOrientation(1);
        this.root.setClipChildren(false);
        this.root.setClipToPadding(false);
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        LayoutParams p = new LayoutParams(metrics.widthPixels + 1, (metrics.widthPixels / 7) * 9);
        p.gravity = 17;
        addView(this.root, p);
        this.topbar = new LinearLayout(getContext());
        this.topbar.setOrientation(0);
        this.topbar.setClipChildren(false);
        this.topbar.setClipToPadding(false);
        this.root.addView(this.topbar, new LinearLayout.LayoutParams(-1, 0, 1.0f));
        this.buttonPast.setScaleType(ScaleType.CENTER_INSIDE);
        this.buttonPast.setImageResource(R.drawable.mcv_action_previous);
        this.topbar.addView(this.buttonPast, new LinearLayout.LayoutParams(0, -1, 1.0f));
        this.title.setGravity(17);
        this.topbar.addView(this.title, new LinearLayout.LayoutParams(0, -1, 5.0f));
        this.buttonFuture.setScaleType(ScaleType.CENTER_INSIDE);
        this.buttonFuture.setImageResource(R.drawable.mcv_action_next);
        this.topbar.addView(this.buttonFuture, new LinearLayout.LayoutParams(0, -1, 1.0f));
        this.pager.setId(R.id.mcv_pager);
        this.pager.setOffscreenPageLimit(1);
        this.root.addView(this.pager, new LinearLayout.LayoutParams(-1, 0, 7.0f));
    }

    public void setOnDateChangedListener(OnDateChangedListener listener) {
        this.listener = listener;
    }

    public void setOnMonthChangedListener(OnMonthChangedListener listener) {
        this.monthListener = listener;
    }

    private void updateUi() {
        this.titleChanger.change(this.currentMonth);
        this.buttonPast.setEnabled(canGoBack());
        this.buttonFuture.setEnabled(canGoForward());
    }

    public int getTileSize() {
        return this.root.getLayoutParams().width / 7;
    }

    public void setTileSize(int size) {
        LayoutParams p;
        if (getTopbarVisible()) {
            p = new LayoutParams(size * 7, size * 8);
        } else {
            p = new LayoutParams(size * 7, size * 7);
        }
        p.gravity = 17;
        this.root.setLayoutParams(p);
    }

    public void setTileSizeDp(int tileSizeDp) {
        setTileSize((int) TypedValue.applyDimension(1, (float) tileSizeDp, getResources()
                .getDisplayMetrics()));
    }

    private boolean canGoForward() {
        return this.pager.getCurrentItem() < this.adapter.getCount() + -1;
    }

    private boolean canGoBack() {
        return this.pager.getCurrentItem() > 0;
    }

    public int getSelectionColor() {
        return this.accentColor;
    }

    public void setSelectionColor(int color) {
        if (color != 0) {
            this.accentColor = color;
            this.adapter.setSelectionColor(color);
            invalidate();
        }
    }

    public int getArrowColor() {
        return this.arrowColor;
    }

    public void setArrowColor(int color) {
        if (color != 0) {
            this.arrowColor = color;
            this.buttonPast.setColor(color);
            this.buttonFuture.setColor(color);
            invalidate();
        }
    }

    public Drawable getLeftArrowMask() {
        return this.leftArrowMask;
    }

    public void setLeftArrowMask(Drawable icon) {
        this.leftArrowMask = icon;
        this.buttonPast.setImageDrawable(icon);
    }

    public Drawable getRightArrowMask() {
        return this.rightArrowMask;
    }

    public void setRightArrowMask(Drawable icon) {
        this.rightArrowMask = icon;
        this.buttonFuture.setImageDrawable(icon);
    }

    public void setHeaderTextAppearance(int resourceId) {
        this.title.setTextAppearance(getContext(), resourceId);
    }

    public void setDateTextAppearance(int resourceId) {
        this.adapter.setDateTextAppearance(resourceId);
    }

    public void setWeekDayTextAppearance(int resourceId) {
        this.adapter.setWeekDayTextAppearance(resourceId);
    }

    public CalendarDay getSelectedDate() {
        return this.adapter.getSelectedDate();
    }

    public void clearSelection() {
        setSelectedDate((CalendarDay) null);
    }

    public void setSelectedDate(@Nullable Calendar calendar) {
        setSelectedDate(CalendarDay.from(calendar));
    }

    public void setSelectedDate(@Nullable Date date) {
        setSelectedDate(CalendarDay.from(date));
    }

    public void setSelectedDate(@Nullable CalendarDay day) {
        this.adapter.setSelectedDate(day);
        setCurrentDate(day);
    }

    public void setCurrentDate(@Nullable Calendar calendar) {
        setCurrentDate(CalendarDay.from(calendar));
    }

    public void setCurrentDate(@Nullable Date date) {
        setCurrentDate(CalendarDay.from(date));
    }

    public CalendarDay getCurrentDate() {
        return this.adapter.getItem(this.pager.getCurrentItem());
    }

    public void setCurrentDate(@Nullable CalendarDay day) {
        if (day != null) {
            this.pager.setCurrentItem(this.adapter.getIndexForDay(day));
            updateUi();
        }
    }

    public CalendarDay getMinimumDate() {
        return this.minDate;
    }

    public void setMinimumDate(@Nullable Calendar calendar) {
        setMinimumDate(CalendarDay.from(calendar));
    }

    public void setMinimumDate(@Nullable Date date) {
        setMinimumDate(CalendarDay.from(date));
    }

    public void setMinimumDate(@Nullable CalendarDay calendar) {
        this.minDate = calendar;
        setRangeDates(this.minDate, this.maxDate);
    }

    public CalendarDay getMaximumDate() {
        return this.maxDate;
    }

    public void setMaximumDate(@Nullable Calendar calendar) {
        setMaximumDate(CalendarDay.from(calendar));
    }

    public void setMaximumDate(@Nullable Date date) {
        setMaximumDate(CalendarDay.from(date));
    }

    public void setMaximumDate(@Nullable CalendarDay calendar) {
        this.maxDate = calendar;
        setRangeDates(this.minDate, this.maxDate);
    }

    public void setShowOtherDates(boolean showOtherDates) {
        this.adapter.setShowOtherDates(showOtherDates);
    }

    public void setWeekDayFormatter(WeekDayFormatter formatter) {
        MonthPagerAdapter monthPagerAdapter = this.adapter;
        if (formatter == null) {
            formatter = WeekDayFormatter.DEFAULT;
        }
        monthPagerAdapter.setWeekDayFormatter(formatter);
    }

    public void setDayFormatter(DayFormatter formatter) {
        MonthPagerAdapter monthPagerAdapter = this.adapter;
        if (formatter == null) {
            formatter = DayFormatter.DEFAULT;
        }
        monthPagerAdapter.setDayFormatter(formatter);
    }

    public void setWeekDayLabels(CharSequence[] weekDayLabels) {
        setWeekDayFormatter(new ArrayWeekDayFormatter(weekDayLabels));
    }

    public void setWeekDayLabels(@ArrayRes int arrayRes) {
        setWeekDayLabels(getResources().getTextArray(arrayRes));
    }

    public boolean getShowOtherDates() {
        return this.adapter.getShowOtherDates();
    }

    public void setTitleFormatter(TitleFormatter titleFormatter) {
        TitleChanger titleChanger = this.titleChanger;
        if (titleFormatter == null) {
            titleFormatter = DEFAULT_TITLE_FORMATTER;
        }
        titleChanger.setTitleFormatter(titleFormatter);
        updateUi();
    }

    public void setTitleMonths(CharSequence[] monthLabels) {
        setTitleFormatter(new MonthArrayTitleFormatter(monthLabels));
    }

    public void setTitleMonths(@ArrayRes int arrayRes) {
        setTitleMonths(getResources().getTextArray(arrayRes));
    }

    public void setTopbarVisible(boolean visible) {
        int tileSize = getTileSize();
        if (visible) {
            this.topbar.setVisibility(0);
        } else {
            this.topbar.setVisibility(8);
        }
        setTileSize(tileSize);
    }

    public boolean getTopbarVisible() {
        return this.topbar.getVisibility() == 0;
    }

    protected Parcelable onSaveInstanceState() {
        SavedState ss = new SavedState(super.onSaveInstanceState());
        ss.color = getSelectionColor();
        ss.dateTextAppearance = this.adapter.getDateTextAppearance();
        ss.weekDayTextAppearance = this.adapter.getWeekDayTextAppearance();
        ss.showOtherDates = getShowOtherDates();
        ss.minDate = getMinimumDate();
        ss.maxDate = getMaximumDate();
        ss.selectedDate = getSelectedDate();
        ss.firstDayOfWeek = getFirstDayOfWeek();
        ss.tileSizePx = getTileSize();
        ss.topbarVisible = getTopbarVisible();
        return ss;
    }

    protected void onRestoreInstanceState(Parcelable state) {
        SavedState ss = (SavedState) state;
        super.onRestoreInstanceState(ss.getSuperState());
        setSelectionColor(ss.color);
        setDateTextAppearance(ss.dateTextAppearance);
        setWeekDayTextAppearance(ss.weekDayTextAppearance);
        setShowOtherDates(ss.showOtherDates);
        setRangeDates(ss.minDate, ss.maxDate);
        setSelectedDate(ss.selectedDate);
        setFirstDayOfWeek(ss.firstDayOfWeek);
        setTileSize(ss.tileSizePx);
        setTopbarVisible(ss.topbarVisible);
    }

    protected void dispatchSaveInstanceState(SparseArray<Parcelable> container) {
        super.dispatchFreezeSelfOnly(container);
    }

    protected void dispatchRestoreInstanceState(SparseArray<Parcelable> container) {
        super.dispatchThawSelfOnly(container);
    }

    private void setRangeDates(CalendarDay min, CalendarDay max) {
        CalendarDay c = this.currentMonth;
        this.adapter.setRangeDates(min, max);
        this.currentMonth = c;
        this.pager.setCurrentItem(this.adapter.getIndexForDay(c), false);
    }

    private static int getThemeAccentColor(Context context) {
        int colorAttr;
        if (VERSION.SDK_INT >= 21) {
            colorAttr = 16843829;
        } else {
            colorAttr = context.getResources().getIdentifier("colorAccent", "attr", context
                    .getPackageName());
        }
        TypedValue outValue = new TypedValue();
        context.getTheme().resolveAttribute(colorAttr, outValue, true);
        return outValue.data;
    }

    public void setFirstDayOfWeek(int day) {
        this.adapter.setFirstDayOfWeek(day);
    }

    public int getFirstDayOfWeek() {
        return this.adapter.getFirstDayOfWeek();
    }

    public void addDecorators(Collection<? extends DayViewDecorator> decorators) {
        if (decorators != null) {
            this.dayViewDecorators.addAll(decorators);
            this.adapter.setDecorators(this.dayViewDecorators);
        }
    }

    public void addDecorators(DayViewDecorator... decorators) {
        addDecorators(Arrays.asList(decorators));
    }

    public void addDecorator(DayViewDecorator decorator) {
        if (decorator != null) {
            this.dayViewDecorators.add(decorator);
            this.adapter.setDecorators(this.dayViewDecorators);
        }
    }

    public void removeDecorators() {
        this.dayViewDecorators.clear();
        this.adapter.setDecorators(this.dayViewDecorators);
    }

    public void removeDecorator(DayViewDecorator decorator) {
        this.dayViewDecorators.remove(decorator);
        this.adapter.setDecorators(this.dayViewDecorators);
    }

    public void invalidateDecorators() {
        this.adapter.invalidateDecorators();
    }

    public void setBooheeCalendarDay(BooheeCalendarDay booheeCalendarDay) {
        if (booheeCalendarDay != null) {
            this.booheeCalendarDay = booheeCalendarDay;
        }
    }

    public void setOnDayClickListener(OnDayClickListener listener) {
        this.onDayClickListener = listener;
    }

    public void invalidateDays() {
        if (this.adapter != null) {
            this.adapter.invalidateDays();
        }
    }
}
