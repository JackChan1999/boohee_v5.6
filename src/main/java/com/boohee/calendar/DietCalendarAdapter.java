package com.boohee.calendar;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.boohee.one.R;

import java.util.Date;
import java.util.List;

public class DietCalendarAdapter extends BaseCalendarAdapter {

    final class ViewHolder {
        public TextView     dayView;
        public LinearLayout ll_weight_content;
        public TextView     weightView;

        ViewHolder() {
        }
    }

    public DietCalendarAdapter(Context context, Date date, List<? extends CountDate> records,
                               Date chooseDate) {
        super(context, date, records, chooseDate);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(this.ctx).inflate(R.layout.ea, null);
            holder.ll_weight_content = (LinearLayout) convertView.findViewById(R.id
                    .ll_weight_content);
            holder.dayView = (TextView) convertView.findViewById(R.id.day);
            holder.weightView = (TextView) convertView.findViewById(R.id.weight);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.dayView.setText(this.dayNumber[position]);
        holder.weightView.setBackgroundResource(R.color.in);
        if (position >= this.daysOfMonth + this.dayOfWeek || position < this.dayOfWeek) {
            holder.dayView.setTextColor(this.ctx.getResources().getColor(R.color.du));
        } else {
            holder.dayView.setTextColor(this.ctx.getResources().getColor(R.color.e4));
            holder.weightView.setTextColor(this.ctx.getResources().getColor(R.color.hx));
            if (this.recordTagFlag != null && this.recordTagFlag.length > 0) {
                for (int i : this.recordTagFlag) {
                    if (i == position) {
                        if (this.currentDayPosition == position) {
                            holder.weightView.setBackgroundResource(R.drawable.iu);
                        } else {
                            holder.weightView.setBackgroundResource(R.drawable.h6);
                            holder.dayView.setTextColor(this.ctx.getResources().getColor(R.color
                                    .hk));
                        }
                    }
                }
            }
            if (this.currentDayPosition == position) {
                holder.ll_weight_content.setBackgroundResource(R.drawable.h6);
                holder.dayView.setTextColor(this.ctx.getResources().getColor(R.color.ju));
            }
        }
        return convertView;
    }
}
