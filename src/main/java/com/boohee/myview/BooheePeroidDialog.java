package com.boohee.myview;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.boohee.one.R;
import com.boohee.utils.DateHelper;
import com.prolificinteractive.materialcalendarview.CalendarDay;

public class BooheePeroidDialog extends Dialog {
    CalendarDay calendarDay;
    TextView    dateTaoday;
    TextView    deleteMc;
    TextView    mcComing;
    TextView    mcLeave;

    public BooheePeroidDialog(Context context, CalendarDay day) {
        super(context, R.style.fp);
        this.calendarDay = day;
        init();
    }

    private void init() {
        View rootView = LayoutInflater.from(getContext()).inflate(R.layout.qe, null);
        setContentView(rootView);
        setCancelable(true);
        setCanceledOnTouchOutside(true);
        initView(rootView);
    }

    private void initView(View rootView) {
        findView(rootView);
        addListener(rootView);
        this.dateTaoday.setTag(this.calendarDay);
        this.mcComing.setTag(this.calendarDay);
        this.mcLeave.setTag(this.calendarDay);
        this.deleteMc.setTag(this.calendarDay);
        this.dateTaoday.setText(this.calendarDay.getDay() + "/" + DateHelper.getWeekOfDate(this
                .calendarDay.getDate(), getContext()));
    }

    private void findView(View rootView) {
        this.dateTaoday = (TextView) rootView.findViewById(R.id.date_taoday);
        this.mcComing = (TextView) rootView.findViewById(R.id.mc_coming);
        this.mcLeave = (TextView) rootView.findViewById(R.id.mc_leave);
        this.deleteMc = (TextView) rootView.findViewById(R.id.delete_mc);
    }

    private void addListener(View rootView) {
        rootView.findViewById(R.id.cancel_dialog).setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                BooheePeroidDialog.this.dismiss();
            }
        });
    }

    public void setMcLeaveListener(OnClickListener listener) {
        if (listener == null) {
            this.mcLeave.setVisibility(8);
            return;
        }
        this.mcLeave.setVisibility(0);
        this.mcLeave.setOnClickListener(listener);
    }

    public void setMcComingListener(OnClickListener listener) {
        if (listener == null) {
            this.mcComing.setVisibility(8);
            return;
        }
        this.mcComing.setVisibility(0);
        this.mcComing.setOnClickListener(listener);
    }

    public void setMcDeleteListener(OnClickListener listener) {
        if (listener == null) {
            this.deleteMc.setVisibility(8);
            return;
        }
        this.deleteMc.setVisibility(0);
        this.deleteMc.setOnClickListener(listener);
    }
}
