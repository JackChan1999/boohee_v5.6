package com.boohee.one.sport.model;

import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.boohee.one.R;
import com.boohee.utils.DateFormatUtils;

import kale.adapter.item.AdapterItem;

public class CourseRecordItem implements AdapterItem<CourseRecord> {
    public CheckBox cb_no;
    public TextView tv_date;
    public TextView tv_id;

    public int getLayoutResId() {
        return R.layout.j6;
    }

    public void bindViews(View view) {
        this.tv_id = (TextView) view.findViewById(R.id.tv_id);
        this.cb_no = (CheckBox) view.findViewById(R.id.cb_no);
        this.tv_date = (TextView) view.findViewById(R.id.tv_date);
    }

    public void setViews() {
    }

    public void handleData(CourseRecord record, int i) {
        boolean z = true;
        int i2 = 0;
        this.tv_id.setText(String.format("第%d课", new Object[]{Integer.valueOf(record.no + 1)}));
        CheckBox checkBox = this.cb_no;
        if (TextUtils.isEmpty(record.date)) {
            z = false;
        }
        checkBox.setChecked(z);
        TextView textView = this.tv_date;
        if (TextUtils.isEmpty(record.date)) {
            i2 = 4;
        }
        textView.setVisibility(i2);
        if (!TextUtils.isEmpty(record.date)) {
            this.tv_date.setText(DateFormatUtils.string2String(record.date, "MM/dd"));
        }
    }
}
