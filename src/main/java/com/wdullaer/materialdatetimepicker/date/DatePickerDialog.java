package com.wdullaer.materialdatetimepicker.date;

import android.animation.ObjectAnimator;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnDismissListener;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.ActivityChooserView.ActivityChooserViewAdapter;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wdullaer.materialdatetimepicker.HapticFeedbackController;
import com.wdullaer.materialdatetimepicker.R;
import com.wdullaer.materialdatetimepicker.TypefaceHelper;
import com.wdullaer.materialdatetimepicker.Utils;
import com.wdullaer.materialdatetimepicker.date.MonthAdapter.CalendarDay;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Locale;

public class DatePickerDialog extends DialogFragment implements OnClickListener,
        DatePickerController {
    private static final int              ANIMATION_DELAY          = 500;
    private static final int              ANIMATION_DURATION       = 300;
    private static       SimpleDateFormat DAY_FORMAT               = new SimpleDateFormat("dd",
            Locale.getDefault());
    private static final int              DEFAULT_END_YEAR         = 2100;
    private static final int              DEFAULT_START_YEAR       = 1900;
    private static final String           KEY_ACCENT               = "accent";
    private static final String           KEY_CURRENT_VIEW         = "current_view";
    private static final String           KEY_DEFAULT_VIEW         = "default_view";
    private static final String           KEY_DISMISS              = "dismiss";
    private static final String           KEY_HIGHLIGHTED_DAYS     = "highlighted_days";
    private static final String           KEY_LIST_POSITION        = "list_position";
    private static final String           KEY_LIST_POSITION_OFFSET = "list_position_offset";
    private static final String           KEY_MAX_DATE             = "max_date";
    private static final String           KEY_MIN_DATE             = "min_date";
    private static final String           KEY_SELECTABLE_DAYS      = "selectable_days";
    private static final String           KEY_SELECTED_DAY         = "day";
    private static final String           KEY_SELECTED_MONTH       = "month";
    private static final String           KEY_SELECTED_YEAR        = "year";
    private static final String           KEY_THEME_DARK           = "theme_dark";
    private static final String           KEY_VIBRATE              = "vibrate";
    private static final String           KEY_WEEK_START           = "week_start";
    private static final String           KEY_YEAR_END             = "year_end";
    private static final String           KEY_YEAR_START           = "year_start";
    private static final int              MONTH_AND_DAY_VIEW       = 0;
    private static final String           TAG                      = "DatePickerDialog";
    private static final int              UNINITIALIZED            = -1;
    private static       SimpleDateFormat YEAR_FORMAT              = new SimpleDateFormat("yyyy",
            Locale.getDefault());
    private static final int              YEAR_VIEW                = 1;
    private Calendar[] highlightedDays;
    private int mAccentColor = -1;
    private AccessibleDateAnimator mAnimator;
    private final Calendar mCalendar = Calendar.getInstance();
    private OnDateSetListener mCallBack;
    private int mCurrentView = -1;
    private TextView      mDayOfWeekView;
    private String        mDayPickerDescription;
    private DayPickerView mDayPickerView;
    private int     mDefaultView    = 0;
    private boolean mDelayAnimation = true;
    private boolean mDismissOnPause = false;
    private HapticFeedbackController mHapticFeedbackController;
    private HashSet<OnDateChangedListener> mListeners = new HashSet();
    private Calendar mMaxDate;
    private int mMaxYear = DEFAULT_END_YEAR;
    private Calendar mMinDate;
    private int mMinYear = DEFAULT_START_YEAR;
    private LinearLayout      mMonthAndDayView;
    private OnCancelListener  mOnCancelListener;
    private OnDismissListener mOnDismissListener;
    private String            mSelectDay;
    private String            mSelectYear;
    private TextView          mSelectedDayTextView;
    private TextView          mSelectedMonthTextView;
    private boolean mThemeDark = false;
    private boolean mVibrate   = true;
    private int     mWeekStart = this.mCalendar.getFirstDayOfWeek();
    private String         mYearPickerDescription;
    private YearPickerView mYearPickerView;
    private TextView       mYearView;
    private Calendar[]     selectableDays;

    public interface OnDateChangedListener {
        void onDateChanged();
    }

    public interface OnDateSetListener {
        void onDateSet(DatePickerDialog datePickerDialog, int i, int i2, int i3);
    }

    public static DatePickerDialog newInstance(OnDateSetListener callBack, int year, int
            monthOfYear, int dayOfMonth) {
        DatePickerDialog ret = new DatePickerDialog();
        ret.initialize(callBack, year, monthOfYear, dayOfMonth);
        return ret;
    }

    public void initialize(OnDateSetListener callBack, int year, int monthOfYear, int dayOfMonth) {
        this.mCallBack = callBack;
        this.mCalendar.set(1, year);
        this.mCalendar.set(2, monthOfYear);
        this.mCalendar.set(5, dayOfMonth);
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().getWindow().setSoftInputMode(3);
        this.mCurrentView = -1;
        if (savedInstanceState != null) {
            this.mCalendar.set(1, savedInstanceState.getInt("year"));
            this.mCalendar.set(2, savedInstanceState.getInt("month"));
            this.mCalendar.set(5, savedInstanceState.getInt(KEY_SELECTED_DAY));
            this.mDefaultView = savedInstanceState.getInt(KEY_DEFAULT_VIEW);
        }
    }

    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("year", this.mCalendar.get(1));
        outState.putInt("month", this.mCalendar.get(2));
        outState.putInt(KEY_SELECTED_DAY, this.mCalendar.get(5));
        outState.putInt("week_start", this.mWeekStart);
        outState.putInt(KEY_YEAR_START, this.mMinYear);
        outState.putInt(KEY_YEAR_END, this.mMaxYear);
        outState.putInt(KEY_CURRENT_VIEW, this.mCurrentView);
        int listPosition = -1;
        if (this.mCurrentView == 0) {
            listPosition = this.mDayPickerView.getMostVisiblePosition();
        } else if (this.mCurrentView == 1) {
            listPosition = this.mYearPickerView.getFirstVisiblePosition();
            outState.putInt(KEY_LIST_POSITION_OFFSET, this.mYearPickerView.getFirstPositionOffset
                    ());
        }
        outState.putInt(KEY_LIST_POSITION, listPosition);
        outState.putSerializable(KEY_MIN_DATE, this.mMinDate);
        outState.putSerializable(KEY_MAX_DATE, this.mMaxDate);
        outState.putSerializable(KEY_HIGHLIGHTED_DAYS, this.highlightedDays);
        outState.putSerializable(KEY_SELECTABLE_DAYS, this.selectableDays);
        outState.putBoolean(KEY_THEME_DARK, this.mThemeDark);
        outState.putInt(KEY_ACCENT, this.mAccentColor);
        outState.putBoolean(KEY_VIBRATE, this.mVibrate);
        outState.putBoolean(KEY_DISMISS, this.mDismissOnPause);
        outState.putInt(KEY_DEFAULT_VIEW, this.mDefaultView);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        Log.d(TAG, "onCreateView: ");
        getDialog().getWindow().requestFeature(1);
        View view = inflater.inflate(R.layout.mdtp_date_picker_dialog, container);
        this.mDayOfWeekView = (TextView) view.findViewById(R.id.date_picker_header);
        this.mMonthAndDayView = (LinearLayout) view.findViewById(R.id.date_picker_month_and_day);
        this.mMonthAndDayView.setOnClickListener(this);
        this.mSelectedMonthTextView = (TextView) view.findViewById(R.id.date_picker_month);
        this.mSelectedDayTextView = (TextView) view.findViewById(R.id.date_picker_day);
        this.mYearView = (TextView) view.findViewById(R.id.date_picker_year);
        this.mYearView.setOnClickListener(this);
        int listPosition = -1;
        int listPositionOffset = 0;
        int currentView = this.mDefaultView;
        if (savedInstanceState != null) {
            this.mWeekStart = savedInstanceState.getInt("week_start");
            this.mMinYear = savedInstanceState.getInt(KEY_YEAR_START);
            this.mMaxYear = savedInstanceState.getInt(KEY_YEAR_END);
            currentView = savedInstanceState.getInt(KEY_CURRENT_VIEW);
            listPosition = savedInstanceState.getInt(KEY_LIST_POSITION);
            listPositionOffset = savedInstanceState.getInt(KEY_LIST_POSITION_OFFSET);
            this.mMinDate = (Calendar) savedInstanceState.getSerializable(KEY_MIN_DATE);
            this.mMaxDate = (Calendar) savedInstanceState.getSerializable(KEY_MAX_DATE);
            this.highlightedDays = (Calendar[]) savedInstanceState.getSerializable
                    (KEY_HIGHLIGHTED_DAYS);
            this.selectableDays = (Calendar[]) savedInstanceState.getSerializable
                    (KEY_SELECTABLE_DAYS);
            this.mThemeDark = savedInstanceState.getBoolean(KEY_THEME_DARK);
            this.mAccentColor = savedInstanceState.getInt(KEY_ACCENT);
            this.mVibrate = savedInstanceState.getBoolean(KEY_VIBRATE);
            this.mDismissOnPause = savedInstanceState.getBoolean(KEY_DISMISS);
        }
        Context activity = getActivity();
        this.mDayPickerView = new SimpleDayPickerView(activity, (DatePickerController) this);
        this.mYearPickerView = new YearPickerView(activity, this);
        Resources res = getResources();
        this.mDayPickerDescription = res.getString(R.string.mdtp_day_picker_description);
        this.mSelectDay = res.getString(R.string.mdtp_select_day);
        this.mYearPickerDescription = res.getString(R.string.mdtp_year_picker_description);
        this.mSelectYear = res.getString(R.string.mdtp_select_year);
        view.setBackgroundColor(activity.getResources().getColor(this.mThemeDark ? R.color
                .mdtp_date_picker_view_animator_dark_theme : R.color
                .mdtp_date_picker_view_animator));
        this.mAnimator = (AccessibleDateAnimator) view.findViewById(R.id.animator);
        this.mAnimator.addView(this.mDayPickerView);
        this.mAnimator.addView(this.mYearPickerView);
        this.mAnimator.setDateMillis(this.mCalendar.getTimeInMillis());
        Animation animation = new AlphaAnimation(0.0f, 1.0f);
        animation.setDuration(300);
        this.mAnimator.setInAnimation(animation);
        Animation animation2 = new AlphaAnimation(1.0f, 0.0f);
        animation2.setDuration(300);
        this.mAnimator.setOutAnimation(animation2);
        Button okButton = (Button) view.findViewById(R.id.ok);
        okButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                DatePickerDialog.this.tryVibrate();
                if (DatePickerDialog.this.mCallBack != null) {
                    DatePickerDialog.this.mCallBack.onDateSet(DatePickerDialog.this,
                            DatePickerDialog.this.mCalendar.get(1), DatePickerDialog.this
                                    .mCalendar.get(2), DatePickerDialog.this.mCalendar.get(5));
                }
                DatePickerDialog.this.dismiss();
            }
        });
        okButton.setTypeface(TypefaceHelper.get(activity, "Roboto-Medium"));
        Button cancelButton = (Button) view.findViewById(R.id.cancel);
        cancelButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                DatePickerDialog.this.tryVibrate();
                if (DatePickerDialog.this.getDialog() != null) {
                    DatePickerDialog.this.getDialog().cancel();
                }
            }
        });
        cancelButton.setTypeface(TypefaceHelper.get(activity, "Roboto-Medium"));
        cancelButton.setVisibility(isCancelable() ? 0 : 8);
        if (this.mAccentColor == -1) {
            this.mAccentColor = Utils.getAccentColorFromThemeIfAvailable(getActivity());
        }
        if (this.mDayOfWeekView != null) {
            this.mDayOfWeekView.setBackgroundColor(Utils.darkenColor(this.mAccentColor));
        }
        view.findViewById(R.id.day_picker_selected_date_layout).setBackgroundColor(this
                .mAccentColor);
        okButton.setTextColor(this.mAccentColor);
        cancelButton.setTextColor(this.mAccentColor);
        updateDisplay(false);
        setCurrentView(currentView);
        if (listPosition != -1) {
            if (currentView == 0) {
                this.mDayPickerView.postSetSelection(listPosition);
            } else if (currentView == 1) {
                this.mYearPickerView.postSetSelectionFromTop(listPosition, listPositionOffset);
            }
        }
        this.mHapticFeedbackController = new HapticFeedbackController(activity);
        return view;
    }

    public void onResume() {
        super.onResume();
        this.mHapticFeedbackController.start();
    }

    public void onPause() {
        super.onPause();
        this.mHapticFeedbackController.stop();
        if (this.mDismissOnPause) {
            dismiss();
        }
    }

    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
        if (this.mOnCancelListener != null) {
            this.mOnCancelListener.onCancel(dialog);
        }
    }

    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if (this.mOnDismissListener != null) {
            this.mOnDismissListener.onDismiss(dialog);
        }
    }

    private void setCurrentView(int viewIndex) {
        long millis = this.mCalendar.getTimeInMillis();
        ObjectAnimator pulseAnimator;
        switch (viewIndex) {
            case 0:
                pulseAnimator = Utils.getPulseAnimator(this.mMonthAndDayView, 0.9f, 1.05f);
                if (this.mDelayAnimation) {
                    pulseAnimator.setStartDelay(500);
                    this.mDelayAnimation = false;
                }
                this.mDayPickerView.onDateChanged();
                if (this.mCurrentView != viewIndex) {
                    this.mMonthAndDayView.setSelected(true);
                    this.mYearView.setSelected(false);
                    this.mAnimator.setDisplayedChild(0);
                    this.mCurrentView = viewIndex;
                }
                pulseAnimator.start();
                this.mAnimator.setContentDescription(this.mDayPickerDescription + ": " +
                        DateUtils.formatDateTime(getActivity(), millis, 16));
                Utils.tryAccessibilityAnnounce(this.mAnimator, this.mSelectDay);
                return;
            case 1:
                pulseAnimator = Utils.getPulseAnimator(this.mYearView, 0.85f, 1.1f);
                if (this.mDelayAnimation) {
                    pulseAnimator.setStartDelay(500);
                    this.mDelayAnimation = false;
                }
                this.mYearPickerView.onDateChanged();
                if (this.mCurrentView != viewIndex) {
                    this.mMonthAndDayView.setSelected(false);
                    this.mYearView.setSelected(true);
                    this.mAnimator.setDisplayedChild(1);
                    this.mCurrentView = viewIndex;
                }
                pulseAnimator.start();
                this.mAnimator.setContentDescription(this.mYearPickerDescription + ": " +
                        YEAR_FORMAT.format(Long.valueOf(millis)));
                Utils.tryAccessibilityAnnounce(this.mAnimator, this.mSelectYear);
                return;
            default:
                return;
        }
    }

    private void updateDisplay(boolean announce) {
        if (this.mDayOfWeekView != null) {
            this.mDayOfWeekView.setText(this.mCalendar.getDisplayName(7, 2, Locale.getDefault())
                    .toUpperCase(Locale.getDefault()));
        }
        this.mSelectedMonthTextView.setText(this.mCalendar.getDisplayName(2, 1, Locale.getDefault
                ()).toUpperCase(Locale.getDefault()));
        this.mSelectedDayTextView.setText(DAY_FORMAT.format(this.mCalendar.getTime()));
        this.mYearView.setText(YEAR_FORMAT.format(this.mCalendar.getTime()));
        long millis = this.mCalendar.getTimeInMillis();
        this.mAnimator.setDateMillis(millis);
        this.mMonthAndDayView.setContentDescription(DateUtils.formatDateTime(getActivity(),
                millis, 24));
        if (announce) {
            Utils.tryAccessibilityAnnounce(this.mAnimator, DateUtils.formatDateTime(getActivity()
                    , millis, 20));
        }
    }

    public void vibrate(boolean vibrate) {
        this.mVibrate = vibrate;
    }

    public void dismissOnPause(boolean dismissOnPause) {
        this.mDismissOnPause = dismissOnPause;
    }

    public void setThemeDark(boolean themeDark) {
        this.mThemeDark = themeDark;
    }

    public boolean isThemeDark() {
        return this.mThemeDark;
    }

    public void setAccentColor(int accentColor) {
        this.mAccentColor = accentColor;
    }

    public int getAccentColor() {
        return this.mAccentColor;
    }

    public void showYearPickerFirst(boolean yearPicker) {
        this.mDefaultView = yearPicker ? 1 : 0;
    }

    public void setFirstDayOfWeek(int startOfWeek) {
        if (startOfWeek < 1 || startOfWeek > 7) {
            throw new IllegalArgumentException("Value must be between Calendar.SUNDAY and " +
                    "Calendar.SATURDAY");
        }
        this.mWeekStart = startOfWeek;
        if (this.mDayPickerView != null) {
            this.mDayPickerView.onChange();
        }
    }

    public void setYearRange(int startYear, int endYear) {
        if (endYear < startYear) {
            throw new IllegalArgumentException("Year end must be larger than or equal to year " +
                    "start");
        }
        this.mMinYear = startYear;
        this.mMaxYear = endYear;
        if (this.mDayPickerView != null) {
            this.mDayPickerView.onChange();
        }
    }

    public void setMinDate(Calendar calendar) {
        this.mMinDate = calendar;
        if (this.mDayPickerView != null) {
            this.mDayPickerView.onChange();
        }
    }

    public Calendar getMinDate() {
        return this.mMinDate;
    }

    public void setMaxDate(Calendar calendar) {
        this.mMaxDate = calendar;
        if (this.mDayPickerView != null) {
            this.mDayPickerView.onChange();
        }
    }

    public Calendar getMaxDate() {
        return this.mMaxDate;
    }

    public void setHighlightedDays(Calendar[] highlightedDays) {
        Arrays.sort(highlightedDays);
        this.highlightedDays = highlightedDays;
    }

    public Calendar[] getHighlightedDays() {
        return this.highlightedDays;
    }

    public void setSelectableDays(Calendar[] selectableDays) {
        Arrays.sort(selectableDays);
        this.selectableDays = selectableDays;
    }

    public Calendar[] getSelectableDays() {
        return this.selectableDays;
    }

    public void setOnDateSetListener(OnDateSetListener listener) {
        this.mCallBack = listener;
    }

    public void setOnCancelListener(OnCancelListener onCancelListener) {
        this.mOnCancelListener = onCancelListener;
    }

    public void setOnDismissListener(OnDismissListener onDismissListener) {
        this.mOnDismissListener = onDismissListener;
    }

    private void adjustDayInMonthIfNeeded(Calendar calendar) {
        int day = calendar.get(5);
        int daysInMonth = calendar.getActualMaximum(5);
        if (day > daysInMonth) {
            calendar.set(5, daysInMonth);
        }
        setToNearestDate(calendar);
    }

    public void onClick(View v) {
        tryVibrate();
        if (v.getId() == R.id.date_picker_year) {
            setCurrentView(1);
        } else if (v.getId() == R.id.date_picker_month_and_day) {
            setCurrentView(0);
        }
    }

    public void onYearSelected(int year) {
        this.mCalendar.set(1, year);
        adjustDayInMonthIfNeeded(this.mCalendar);
        updatePickers();
        setCurrentView(0);
        updateDisplay(true);
    }

    public void onDayOfMonthSelected(int year, int month, int day) {
        this.mCalendar.set(1, year);
        this.mCalendar.set(2, month);
        this.mCalendar.set(5, day);
        updatePickers();
        updateDisplay(true);
    }

    private void updatePickers() {
        Iterator i$ = this.mListeners.iterator();
        while (i$.hasNext()) {
            ((OnDateChangedListener) i$.next()).onDateChanged();
        }
    }

    public CalendarDay getSelectedDay() {
        return new CalendarDay(this.mCalendar);
    }

    public int getMinYear() {
        if (this.selectableDays != null) {
            return this.selectableDays[0].get(1);
        }
        return (this.mMinDate == null || this.mMinDate.get(1) <= this.mMinYear) ? this.mMinYear :
                this.mMinDate.get(1);
    }

    public int getMaxYear() {
        if (this.selectableDays != null) {
            return this.selectableDays[this.selectableDays.length - 1].get(1);
        }
        return (this.mMaxDate == null || this.mMaxDate.get(1) >= this.mMaxYear) ? this.mMaxYear :
                this.mMaxDate.get(1);
    }

    public boolean isOutOfRange(int year, int month, int day) {
        if (this.selectableDays != null) {
            if (isSelectable(year, month, day)) {
                return false;
            }
            return true;
        } else if (isBeforeMin(year, month, day) || isAfterMax(year, month, day)) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isOutOfRange(Calendar calendar) {
        return isOutOfRange(calendar.get(1), calendar.get(2), calendar.get(5));
    }

    private boolean isSelectable(int year, int month, int day) {
        for (Calendar c : this.selectableDays) {
            if (year < c.get(1)) {
                break;
            }
            if (year <= c.get(1)) {
                if (month >= c.get(2)) {
                    if (month <= c.get(2)) {
                        if (day < c.get(5)) {
                            break;
                        } else if (day <= c.get(5)) {
                            return true;
                        }
                    } else {
                        continue;
                    }
                } else {
                    break;
                }
            }
        }
        return false;
    }

    private boolean isBeforeMin(int year, int month, int day) {
        if (this.mMinDate == null) {
            return false;
        }
        if (year < this.mMinDate.get(1)) {
            return true;
        }
        if (year > this.mMinDate.get(1)) {
            return false;
        }
        if (month < this.mMinDate.get(2)) {
            return true;
        }
        if (month > this.mMinDate.get(2) || day >= this.mMinDate.get(5)) {
            return false;
        }
        return true;
    }

    private boolean isBeforeMin(Calendar calendar) {
        return isBeforeMin(calendar.get(1), calendar.get(2), calendar.get(5));
    }

    private boolean isAfterMax(int year, int month, int day) {
        if (this.mMaxDate == null) {
            return false;
        }
        if (year > this.mMaxDate.get(1)) {
            return true;
        }
        if (year < this.mMaxDate.get(1)) {
            return false;
        }
        if (month > this.mMaxDate.get(2)) {
            return true;
        }
        if (month < this.mMaxDate.get(2) || day <= this.mMaxDate.get(5)) {
            return false;
        }
        return true;
    }

    private boolean isAfterMax(Calendar calendar) {
        return isAfterMax(calendar.get(1), calendar.get(2), calendar.get(5));
    }

    private void setToNearestDate(Calendar calendar) {
        if (this.selectableDays != null) {
            int distance = ActivityChooserViewAdapter.MAX_ACTIVITY_COUNT_UNLIMITED;
            Calendar[] arr$ = this.selectableDays;
            int len$ = arr$.length;
            int i$ = 0;
            while (i$ < len$) {
                Calendar c = arr$[i$];
                int newDistance = Math.abs(calendar.compareTo(c));
                if (newDistance < distance) {
                    distance = newDistance;
                    i$++;
                } else {
                    calendar.setTimeInMillis(c.getTimeInMillis());
                    return;
                }
            }
        } else if (isBeforeMin(calendar)) {
            calendar.setTimeInMillis(this.mMinDate.getTimeInMillis());
        } else if (isAfterMax(calendar)) {
            calendar.setTimeInMillis(this.mMaxDate.getTimeInMillis());
        }
    }

    public int getFirstDayOfWeek() {
        return this.mWeekStart;
    }

    public void registerOnDateChangedListener(OnDateChangedListener listener) {
        this.mListeners.add(listener);
    }

    public void unregisterOnDateChangedListener(OnDateChangedListener listener) {
        this.mListeners.remove(listener);
    }

    public void tryVibrate() {
        if (this.mVibrate) {
            this.mHapticFeedbackController.tryVibrate();
        }
    }
}
