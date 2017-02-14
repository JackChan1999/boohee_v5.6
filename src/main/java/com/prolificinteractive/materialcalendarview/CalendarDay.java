package com.prolificinteractive.materialcalendarview;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public final class CalendarDay implements Parcelable {
    public static final Creator<CalendarDay> CREATOR = new
    1();
    private transient Calendar _calendar;
    private transient Date     _date;
    public            int      bottomDrawableId;
    public            int      bottomLineColorId;
    private final     int      day;
    private final     int      month;
    private final     int      year;

    @NonNull
    public static CalendarDay today() {
        return from(CalendarUtils.getInstance());
    }

    @NonNull
    public static CalendarDay from(int year, int month, int day) {
        return new CalendarDay(year, month, day);
    }

    public static CalendarDay from(@Nullable Calendar calendar) {
        if (calendar == null) {
            return null;
        }
        return from(CalendarUtils.getYear(calendar), CalendarUtils.getMonth(calendar),
                CalendarUtils.getDay(calendar));
    }

    public static CalendarDay from(@Nullable Date date) {
        if (date == null) {
            return null;
        }
        return from(CalendarUtils.getInstance(date));
    }

    @Deprecated
    public CalendarDay() {
        this(CalendarUtils.getInstance());
    }

    @Deprecated
    public CalendarDay(Calendar calendar) {
        this(CalendarUtils.getYear(calendar), CalendarUtils.getMonth(calendar), CalendarUtils
                .getDay(calendar));
    }

    @Deprecated
    public CalendarDay(int year, int month, int day) {
        this.bottomLineColorId = -1;
        this.bottomDrawableId = -1;
        this.year = year;
        this.month = month;
        this.day = day;
    }

    @Deprecated
    public CalendarDay(Date date) {
        this(CalendarUtils.getInstance(date));
    }

    public int getYear() {
        return this.year;
    }

    public int getMonth() {
        return this.month;
    }

    public int getDay() {
        return this.day;
    }

    @NonNull
    public Date getDate() {
        if (this._date == null) {
            this._date = getCalendar().getTime();
        }
        return this._date;
    }

    @NonNull
    public Calendar getCalendar() {
        if (this._calendar == null) {
            this._calendar = CalendarUtils.getInstance();
            copyTo(this._calendar);
        }
        return this._calendar;
    }

    public void copyTo(Calendar calendar) {
        calendar.clear();
        calendar.set(this.year, this.month, this.day);
    }

    public boolean isInRange(CalendarDay minDate, CalendarDay maxDate) {
        return (minDate == null || !minDate.isAfter(this)) && (maxDate == null || !maxDate
                .isBefore(this));
    }

    public boolean isBefore(@NonNull CalendarDay other) {
        if (other == null) {
            throw new IllegalArgumentException("other cannot be null");
        } else if (this.year == other.year) {
            if (this.month == other.month) {
                if (this.day < other.day) {
                    return true;
                }
                return false;
            } else if (this.month >= other.month) {
                return false;
            } else {
                return true;
            }
        } else if (this.year >= other.year) {
            return false;
        } else {
            return true;
        }
    }

    public boolean isAfter(@NonNull CalendarDay other) {
        if (other == null) {
            throw new IllegalArgumentException("other cannot be null");
        } else if (this.year == other.year) {
            if (this.month == other.month) {
                if (this.day > other.day) {
                    return true;
                }
                return false;
            } else if (this.month <= other.month) {
                return false;
            } else {
                return true;
            }
        } else if (this.year <= other.year) {
            return false;
        } else {
            return true;
        }
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CalendarDay that = (CalendarDay) o;
        if (this.day == that.day && this.month == that.month && this.year == that.year) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        return hashCode(this.year, this.month, this.day);
    }

    private static int hashCode(int year, int month, int day) {
        return ((year * 10000) + (month * 100)) + day;
    }

    public String toString() {
        return String.format(Locale.US, "CalendarDay{%d-%d-%d}", new Object[]{Integer.valueOf
                (this.year), Integer.valueOf(this.month + 1), Integer.valueOf(this.day)});
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.year);
        dest.writeInt(this.month);
        dest.writeInt(this.day);
        dest.writeSerializable(this._calendar);
        dest.writeLong(this._date != null ? this._date.getTime() : -1);
        dest.writeInt(this.bottomLineColorId);
        dest.writeInt(this.bottomDrawableId);
    }

    protected CalendarDay(Parcel in) {
        this.bottomLineColorId = -1;
        this.bottomDrawableId = -1;
        this.year = in.readInt();
        this.month = in.readInt();
        this.day = in.readInt();
        this._calendar = (Calendar) in.readSerializable();
        long tmp_date = in.readLong();
        this._date = tmp_date == -1 ? null : new Date(tmp_date);
        this.bottomLineColorId = in.readInt();
        this.bottomDrawableId = in.readInt();
    }
}
