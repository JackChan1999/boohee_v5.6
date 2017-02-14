package com.boohee.myview;

import android.content.Context;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.boohee.one.R;
import com.boohee.utility.Event;
import com.boohee.utils.DateFormatUtils;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class WeekView extends LinearLayout {
    public static final int             DAYS_OF_WEEK   = 7;
    private             OnClickListener mClickListener = new OnClickListener() {
        public void onClick(View v) {
            if (v.getTag() instanceof Integer) {
                MobclickAgent.onEvent(v.getContext(), Event.bingo_clickDateDiet);
                WeekView.this.select(((Integer) v.getTag()).intValue());
            }
        }
    };
    private Context          mContext;
    private OnSelectListener mSelectListener;
    private int              orderOfToday;
    private int              selected;
    private List<TextView> tvList = new ArrayList();
    private Date[] week;

    public interface OnSelectListener {
        void onSelect(int i);
    }

    public WeekView(Context context) {
        super(context);
        init(context);
    }

    public WeekView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public WeekView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        this.mContext = context;
        setOrientation(0);
        setWeightSum(7.0f);
        for (int i = 0; i < 7; i++) {
            View view = generateDayView();
            this.tvList.add((TextView) view.findViewById(R.id.tv));
            addView(view);
        }
    }

    private View generateDayView() {
        View view = LayoutInflater.from(this.mContext).inflate(R.layout.r8, this, false);
        view.setOnClickListener(this.mClickListener);
        return view;
    }

    public void setCurrentDay(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        this.orderOfToday = c.get(7) - 1;
        this.week = DateFormatUtils.getWeekDays(date);
        for (int i = 0; i < 7; i++) {
            refreshDayView((TextView) this.tvList.get(i), this.week[i], i);
        }
    }

    public void select(int dayOfOrder) {
        this.selected = dayOfOrder;
        resetSelect();
        if (this.mSelectListener != null) {
            this.mSelectListener.onSelect(dayOfOrder);
        }
        TextView tv = (TextView) this.tvList.get(dayOfOrder);
        tv.setTextColor(getResources().getColor(R.color.ju));
        ShapeDrawable oval = new ShapeDrawable(new OvalShape());
        if (dayOfOrder == this.orderOfToday) {
            oval.getPaint().setColor(getResources().getColor(R.color.he));
        } else {
            oval.getPaint().setColor(getResources().getColor(R.color.hb));
        }
        tv.setBackgroundDrawable(oval);
    }

    private void resetSelect() {
        for (int i = 0; i < this.tvList.size(); i++) {
            TextView tv = (TextView) this.tvList.get(i);
            tv.setTextColor(getTextColor(i));
            tv.setBackgroundResource(0);
        }
    }

    private void refreshDayView(TextView tv, Date date, int orderOfDay) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int day = c.get(5);
        ((View) tv.getParent()).setTag(Integer.valueOf(orderOfDay));
        if (orderOfDay == this.orderOfToday) {
            tv.setText("今");
        } else {
            tv.setText(String.valueOf(day));
        }
        tv.setTextColor(getTextColor(orderOfDay));
    }

    private int getTextColor(int orderOfDay) {
        if (orderOfDay == this.orderOfToday) {
            return getResources().getColor(R.color.he);
        }
        if (orderOfDay < this.orderOfToday) {
            return getResources().getColor(R.color.e4);
        }
        return getResources().getColor(R.color.du);
    }

    public int getOrderOfToday() {
        return this.orderOfToday;
    }

    public String getSelectDay() {
        return DateFormatUtils.date2string(this.week[this.selected], "yyyy-MM-dd");
    }

    public void setSelectListener(OnSelectListener selectListener) {
        this.mSelectListener = selectListener;
    }
}
