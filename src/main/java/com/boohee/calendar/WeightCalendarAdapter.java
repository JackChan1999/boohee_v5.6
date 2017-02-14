package com.boohee.calendar;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.boohee.model.mine.BaseRecord;
import com.boohee.model.mine.Measure;
import com.boohee.model.mine.WeightRecord;
import com.boohee.one.R;
import com.boohee.utils.DateHelper;
import com.umeng.socialize.common.SocializeConstants;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class WeightCalendarAdapter extends BaseAdapter {
    static final String TAG = WeightCalendarAdapter.class.getName();
    private Context ctx;
    private int currentFlag = -1;
    private int currentMonth;
    private int currentSelected = -1;
    private int  currentYear;
    private Date date;
    private String[] dayNumber       = new String[42];
    private int      dayOfWeek       = 0;
    private int      daysOfMonth     = 0;
    private boolean  isLeapyear      = false;
    private int      lastDaysOfMonth = 0;
    public onCalendarItemSelectedListener onCalendarItemSelectedListener;
    private int[] recordTagFlag = null;
    private List<BaseRecord> records;
    private SpecialCalendar sc = null;
    private int sys_day;
    private int sys_month;
    private int sys_year;

    final class ViewHolder {
        public TextView     dayView;
        public LinearLayout ll_weight_content;
        public View         tagPhoto;
        public View         tagScale;
        public TextView     weightView;

        ViewHolder() {
        }
    }

    public interface onCalendarItemSelectedListener {
        void onCalendarItemSelected(int i);
    }

    public WeightCalendarAdapter(Context context, Date date, List<BaseRecord> records) {
        this.ctx = context;
        this.sc = new SpecialCalendar();
        this.date = date;
        getSysDate();
        this.records = records;
        init();
    }

    private void init() {
        this.currentYear = DateHelper.getYear(this.date);
        this.currentMonth = DateHelper.getMonth(this.date);
        getCalendar(this.currentYear, this.currentMonth);
    }

    private void getSysDate() {
        Date d = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(d);
        this.sys_year = c.get(1);
        this.sys_month = c.get(2) + 1;
        this.sys_day = c.get(5);
    }

    public int getCount() {
        if (endPosition() <= 35) {
            return 35;
        }
        return this.dayNumber.length;
    }

    public Object getItem(int position) {
        return Integer.valueOf(position);
    }

    public String getDateString(int position) {
        return this.currentYear + SocializeConstants.OP_DIVIDER_MINUS + String.format("%02d", new
                Object[]{Integer.valueOf(this.currentMonth)}) + SocializeConstants
                .OP_DIVIDER_MINUS + String.format("%02d", new Object[]{Integer.valueOf(Integer
                .parseInt(this.dayNumber[position]))});
    }

    public float getValue(int position) {
        String record_on = getDateString(position);
        for (BaseRecord record : this.records) {
            if (record_on.equals(record.record_on)) {
                if (record instanceof WeightRecord) {
                    return Float.parseFloat(((WeightRecord) record).weight);
                }
                if (record instanceof Measure) {
                    return ((Measure) record).value;
                }
            }
        }
        return 0.0f;
    }

    public BaseRecord getData(int position) {
        String record_on = getDateString(position);
        for (BaseRecord record : this.records) {
            if (record_on.equals(record.record_on)) {
                return record;
            }
        }
        return null;
    }

    public Date getDate(int position) {
        return DateHelper.parseString(getDateString(position));
    }

    public long getItemId(int position) {
        return (long) position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(this.ctx).inflate(R.layout.ec, null);
            holder.ll_weight_content = (LinearLayout) convertView.findViewById(R.id
                    .ll_weight_content);
            holder.dayView = (TextView) convertView.findViewById(R.id.day);
            holder.weightView = (TextView) convertView.findViewById(R.id.weight);
            holder.tagScale = convertView.findViewById(R.id.tag_scale);
            holder.tagPhoto = convertView.findViewById(R.id.tag_picture);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.dayView.setText(this.dayNumber[position]);
        holder.tagScale.setVisibility(8);
        holder.tagPhoto.setVisibility(8);
        if (this.currentFlag == position) {
            holder.ll_weight_content.setBackgroundResource(R.drawable.a_w);
            holder.dayView.setTextColor(this.ctx.getResources().getColor(R.color.ju));
            holder.weightView.setTextColor(this.ctx.getResources().getColor(R.color.ju));
        } else if (position >= this.daysOfMonth + this.dayOfWeek || position < this.dayOfWeek) {
            holder.dayView.setTextColor(this.ctx.getResources().getColor(R.color.du));
        } else {
            holder.dayView.setTextColor(this.ctx.getResources().getColor(R.color.e4));
            holder.weightView.setTextColor(this.ctx.getResources().getColor(R.color.hx));
        }
        if (this.recordTagFlag != null && this.recordTagFlag.length > 0) {
            for (int i = 0; i < this.recordTagFlag.length; i++) {
                if (this.recordTagFlag[i] == position) {
                    if (this.records.get(i) instanceof WeightRecord) {
                        WeightRecord record = (WeightRecord) this.records.get(i);
                        holder.weightView.setText(record.weight + "");
                        if (!record.isByHand()) {
                            holder.tagScale.setVisibility(0);
                        }
                        if (record.photos != null && record.photos.size() > 0) {
                            holder.tagPhoto.setVisibility(0);
                        }
                    } else if (this.records.get(i) instanceof Measure) {
                        holder.weightView.setText(((Measure) this.records.get(i)).value + "");
                    }
                }
            }
        }
        return convertView;
    }

    public void getCalendar(int year, int month) {
        this.isLeapyear = this.sc.isLeapYear(year);
        this.daysOfMonth = this.sc.getDaysOfMonth(this.isLeapyear, month);
        this.dayOfWeek = this.sc.getWeekdayOfMonth(year, month);
        this.lastDaysOfMonth = this.sc.getDaysOfMonth(this.isLeapyear, month - 1);
        getweek(year, month);
    }

    private void getweek(int year, int month) {
        int j = 1;
        int flag = 0;
        if (this.records != null && this.records.size() > 0) {
            this.recordTagFlag = new int[this.records.size()];
        }
        for (int i = 0; i < this.dayNumber.length; i++) {
            if (i < this.dayOfWeek) {
                this.dayNumber[i] = (((this.lastDaysOfMonth - this.dayOfWeek) + 1) + i) + "";
            } else if (i < this.daysOfMonth + this.dayOfWeek) {
                int day = (i - this.dayOfWeek) + 1;
                this.dayNumber[i] = ((i - this.dayOfWeek) + 1) + "";
                if (this.sys_year == year && this.sys_month == month && this.sys_day == day) {
                    this.currentFlag = i;
                }
                if (this.records != null && this.records.size() > 0) {
                    for (int m = 0; m < this.records.size(); m++) {
                        BaseRecord record = (BaseRecord) this.records.get(m);
                        int matchYear = record.getYear();
                        int matchMonth = record.getMonth();
                        int matchDay = record.getDay();
                        if (matchYear == year && matchMonth == month && matchDay == day) {
                            this.recordTagFlag[flag] = i;
                            flag++;
                        }
                    }
                }
            } else {
                this.dayNumber[i] = j + "";
                j++;
            }
        }
    }

    public int startPosition() {
        return this.dayOfWeek;
    }

    public int endPosition() {
        return this.daysOfMonth + this.dayOfWeek;
    }

    public void setChangeListener(onCalendarItemSelectedListener onCalendarItemSelectedListener) {
        this.onCalendarItemSelectedListener = onCalendarItemSelectedListener;
    }
}
