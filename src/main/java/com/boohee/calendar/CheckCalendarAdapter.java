package com.boohee.calendar;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.boohee.one.R;

import java.util.Date;
import java.util.List;

public class CheckCalendarAdapter extends BaseCalendarAdapter {
    private int currentPosition = -1;
    private int lastPosition    = -1;

    final class ViewHolder {
        public ImageView      checkView;
        public TextView       dayView;
        public RelativeLayout ll_diamond_content;

        ViewHolder() {
        }
    }

    public CheckCalendarAdapter(Context context, Date date, List<? extends CountDate> records) {
        super(context, date, records, date);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(this.ctx).inflate(R.layout.e_, null);
            holder.ll_diamond_content = (RelativeLayout) convertView.findViewById(R.id
                    .ll_diamond_content);
            holder.dayView = (TextView) convertView.findViewById(R.id.day);
            holder.checkView = (ImageView) convertView.findViewById(R.id.weight);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.dayView.setText(this.dayNumber[position]);
        if (this.currentFlag == position) {
            holder.ll_diamond_content.setBackgroundResource(R.drawable.h6);
            holder.dayView.setTextColor(this.ctx.getResources().getColor(R.color.ju));
        } else if (position >= this.daysOfMonth + this.dayOfWeek || position < this.dayOfWeek) {
            holder.dayView.setTextColor(this.ctx.getResources().getColor(R.color.du));
        } else {
            holder.dayView.setTextColor(this.ctx.getResources().getColor(R.color.e4));
        }
        holder.checkView.setBackgroundResource(R.color.in);
        if (this.recordTagFlag != null && this.recordTagFlag.length > 0) {
            for (int i : this.recordTagFlag) {
                if (i == position) {
                    if (this.currentFlag == position) {
                        holder.checkView.setBackgroundResource(R.drawable.li);
                    } else {
                        holder.checkView.setBackgroundResource(R.drawable.lh);
                        holder.dayView.setTextColor(this.ctx.getResources().getColor(R.color.hk));
                    }
                }
            }
        }
        if (this.lastPosition == position) {
            holder.ll_diamond_content.setBackgroundResource(R.drawable.e8);
        }
        if (this.currentPosition == position) {
            holder.ll_diamond_content.setBackgroundResource(R.drawable.cp);
        }
        return convertView;
    }

    public boolean isChecked(int position) {
        if (this.recordTagFlag == null || this.recordTagFlag.length <= 0) {
            return false;
        }
        for (int i : this.recordTagFlag) {
            if (i == position) {
                return true;
            }
        }
        return false;
    }

    public void setCurrentPosition(int currentPosition) {
        this.lastPosition = this.currentPosition;
        this.currentPosition = currentPosition;
    }
}
