package com.boohee.calendar;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.boohee.one.R;

import java.util.Date;
import java.util.List;

public class PeriodCalendarAdapter extends BaseCalendarAdapter {
    private int currentPosition = -1;
    private int lastPosition    = -1;

    final class ViewHolder {
        public TextView  dayView;
        public ImageView imageView;
        public View      ll_content;

        ViewHolder() {
        }
    }

    public PeriodCalendarAdapter(Context context, Date date, List<? extends CountDate> records) {
        super(context, date, records, date);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(this.ctx).inflate(R.layout.eb, null);
            holder.dayView = (TextView) convertView.findViewById(R.id.day);
            holder.imageView = (ImageView) convertView.findViewById(R.id.img);
            holder.ll_content = convertView.findViewById(R.id.ll_content);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.dayView.setText(this.dayNumber[position]);
        if (this.currentFlag == position) {
            holder.dayView.setBackgroundResource(R.drawable.h6);
            holder.dayView.setTextColor(this.ctx.getResources().getColor(R.color.ju));
            holder.dayView.setTypeface(Typeface.DEFAULT_BOLD);
        } else if (position >= this.daysOfMonth + this.dayOfWeek || position < this.dayOfWeek) {
            holder.dayView.setTextColor(this.ctx.getResources().getColor(R.color.du));
        } else {
            holder.dayView.setTextColor(this.ctx.getResources().getColor(R.color.e4));
        }
        holder.imageView.setVisibility(8);
        refreshUiWithPosition(position, holder);
        if (this.lastPosition == position && this.lastPosition != this.currentFlag) {
            refreshUiWithPosition(position, holder);
            holder.dayView.setBackgroundResource(R.color.in);
            holder.dayView.setTypeface(Typeface.DEFAULT);
        }
        if (this.currentPosition == position && this.lastPosition != this.currentFlag) {
            holder.dayView.setBackgroundResource(R.drawable.en);
            holder.dayView.setTextColor(this.ctx.getResources().getColor(R.color.ju));
            holder.dayView.setTypeface(Typeface.DEFAULT_BOLD);
        }
        return convertView;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void refreshUiWithPosition(int r12, com.boohee.calendar.PeriodCalendarAdapter
            .ViewHolder r13) {
        /*
        r11 = this;
        r10 = 2130903344; // 0x7f030130 float:1.7413503E38 double:1.052806137E-314;
        r7 = 2;
        r6 = 1;
        r5 = -1;
        r4 = 0;
        r3 = r11.recordTagFlag;
        if (r3 == 0) goto L_0x015a;
    L_0x000b:
        r3 = r11.recordTagFlag;
        r3 = r3.length;
        if (r3 <= 0) goto L_0x015a;
    L_0x0010:
        r0 = 0;
    L_0x0011:
        r3 = r11.recordTagFlag;
        r3 = r3.length;
        if (r0 >= r3) goto L_0x015a;
    L_0x0016:
        r3 = r11.recordTagFlag;
        r3 = r3[r0];
        if (r3 != r12) goto L_0x004e;
    L_0x001c:
        r3 = r11.records;
        r2 = r3.get(r0);
        r2 = (com.boohee.model.PeriodRecord) r2;
        r3 = "mc";
        r8 = r2.type;
        r3 = r3.equals(r8);
        if (r3 == 0) goto L_0x008d;
    L_0x002f:
        r3 = r2.location;
        r8 = r3.hashCode();
        switch(r8) {
            case -1364013995: goto L_0x005c;
            case 3317767: goto L_0x0051;
            case 108511772: goto L_0x0067;
            default: goto L_0x0038;
        };
    L_0x0038:
        r3 = r5;
    L_0x0039:
        switch(r3) {
            case 0: goto L_0x0072;
            case 1: goto L_0x007b;
            case 2: goto L_0x0084;
            default: goto L_0x003c;
        };
    L_0x003c:
        r3 = r13.dayView;
        r8 = r11.ctx;
        r8 = r8.getResources();
        r9 = 2130903454; // 0x7f03019e float:1.7413726E38 double:1.052806191E-314;
        r8 = r8.getColor(r9);
        r3.setTextColor(r8);
    L_0x004e:
        r0 = r0 + 1;
        goto L_0x0011;
    L_0x0051:
        r8 = "left";
        r3 = r3.equals(r8);
        if (r3 == 0) goto L_0x0038;
    L_0x005a:
        r3 = r4;
        goto L_0x0039;
    L_0x005c:
        r8 = "center";
        r3 = r3.equals(r8);
        if (r3 == 0) goto L_0x0038;
    L_0x0065:
        r3 = r6;
        goto L_0x0039;
    L_0x0067:
        r8 = "right";
        r3 = r3.equals(r8);
        if (r3 == 0) goto L_0x0038;
    L_0x0070:
        r3 = r7;
        goto L_0x0039;
    L_0x0072:
        r3 = r13.ll_content;
        r8 = 2130968648; // 0x7f040048 float:1.7545956E38 double:1.0528384013E-314;
        r3.setBackgroundResource(r8);
        goto L_0x003c;
    L_0x007b:
        r3 = r13.ll_content;
        r8 = 2130968647; // 0x7f040047 float:1.7545954E38 double:1.052838401E-314;
        r3.setBackgroundResource(r8);
        goto L_0x003c;
    L_0x0084:
        r3 = r13.ll_content;
        r8 = 2130968649; // 0x7f040049 float:1.7545958E38 double:1.052838402E-314;
        r3.setBackgroundResource(r8);
        goto L_0x003c;
    L_0x008d:
        r3 = "prediction";
        r8 = r2.type;
        r3 = r3.equals(r8);
        if (r3 == 0) goto L_0x011d;
    L_0x0098:
        r3 = r2.location;
        r8 = r3.hashCode();
        switch(r8) {
            case -1364013995: goto L_0x00c3;
            case 3317767: goto L_0x00b8;
            case 108511772: goto L_0x00ce;
            default: goto L_0x00a1;
        };
    L_0x00a1:
        r3 = r5;
    L_0x00a2:
        switch(r3) {
            case 0: goto L_0x00d9;
            case 1: goto L_0x010b;
            case 2: goto L_0x0114;
            default: goto L_0x00a5;
        };
    L_0x00a5:
        r3 = r13.dayView;
        r8 = r11.ctx;
        r8 = r8.getResources();
        r9 = 2130903342; // 0x7f03012e float:1.74135E38 double:1.052806136E-314;
        r8 = r8.getColor(r9);
        r3.setTextColor(r8);
        goto L_0x004e;
    L_0x00b8:
        r8 = "left";
        r3 = r3.equals(r8);
        if (r3 == 0) goto L_0x00a1;
    L_0x00c1:
        r3 = r4;
        goto L_0x00a2;
    L_0x00c3:
        r8 = "center";
        r3 = r3.equals(r8);
        if (r3 == 0) goto L_0x00a1;
    L_0x00cc:
        r3 = r6;
        goto L_0x00a2;
    L_0x00ce:
        r8 = "right";
        r3 = r3.equals(r8);
        if (r3 == 0) goto L_0x00a1;
    L_0x00d7:
        r3 = r7;
        goto L_0x00a2;
    L_0x00d9:
        if (r0 <= 0) goto L_0x0102;
    L_0x00db:
        r3 = r11.records;
        r8 = r0 + -1;
        r1 = r3.get(r8);
        r1 = (com.boohee.model.PeriodRecord) r1;
        r3 = "mc";
        r8 = r1.type;
        r3 = r3.equals(r8);
        if (r3 == 0) goto L_0x00f9;
    L_0x00f0:
        r3 = r13.ll_content;
        r8 = 2130968650; // 0x7f04004a float:1.754596E38 double:1.0528384023E-314;
        r3.setBackgroundResource(r8);
        goto L_0x00a5;
    L_0x00f9:
        r3 = r13.ll_content;
        r8 = 2130968651; // 0x7f04004b float:1.7545962E38 double:1.052838403E-314;
        r3.setBackgroundResource(r8);
        goto L_0x00a5;
    L_0x0102:
        r3 = r13.ll_content;
        r8 = 2130968651; // 0x7f04004b float:1.7545962E38 double:1.052838403E-314;
        r3.setBackgroundResource(r8);
        goto L_0x00a5;
    L_0x010b:
        r3 = r13.ll_content;
        r8 = 2130968650; // 0x7f04004a float:1.754596E38 double:1.0528384023E-314;
        r3.setBackgroundResource(r8);
        goto L_0x00a5;
    L_0x0114:
        r3 = r13.ll_content;
        r8 = 2130968652; // 0x7f04004c float:1.7545964E38 double:1.0528384033E-314;
        r3.setBackgroundResource(r8);
        goto L_0x00a5;
    L_0x011d:
        r3 = "pregnancy";
        r8 = r2.type;
        r3 = r3.equals(r8);
        if (r3 == 0) goto L_0x0139;
    L_0x0128:
        r3 = r13.dayView;
        r8 = r11.ctx;
        r8 = r8.getResources();
        r8 = r8.getColor(r10);
        r3.setTextColor(r8);
        goto L_0x004e;
    L_0x0139:
        r3 = "oviposit_day";
        r8 = r2.type;
        r3 = r3.equals(r8);
        if (r3 == 0) goto L_0x004e;
    L_0x0144:
        r3 = r13.imageView;
        r3.setVisibility(r4);
        r3 = r13.dayView;
        r8 = r11.ctx;
        r8 = r8.getResources();
        r8 = r8.getColor(r10);
        r3.setTextColor(r8);
        goto L_0x004e;
    L_0x015a:
        r3 = r11.currentFlag;
        if (r3 != r12) goto L_0x0170;
    L_0x015e:
        r3 = r13.dayView;
        r4 = r11.ctx;
        r4 = r4.getResources();
        r5 = 2130903454; // 0x7f03019e float:1.7413726E38 double:1.052806191E-314;
        r4 = r4.getColor(r5);
        r3.setTextColor(r4);
    L_0x0170:
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.boohee.calendar" +
                ".PeriodCalendarAdapter.refreshUiWithPosition(int, com.boohee.calendar" +
                ".PeriodCalendarAdapter$ViewHolder):void");
    }

    public void setCurrentPosition(int currentPosition) {
        this.lastPosition = this.currentPosition;
        this.currentPosition = currentPosition;
    }
}
