package com.boohee.record;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.boohee.model.HistoryRecord;
import com.boohee.one.R;
import com.boohee.one.ui.adapter.SimpleBaseAdapter;
import com.boohee.one.ui.adapter.SimpleBaseAdapter.ViewHolder;
import com.boohee.utils.DateFormatUtils;

import java.util.List;

public class HistoryRecordListAdapter extends SimpleBaseAdapter<HistoryRecord> {
    String record_on;

    public HistoryRecordListAdapter(Context context, List<HistoryRecord> data, String record_on) {
        super(context, data);
        this.record_on = record_on;
    }

    public int getItemResource() {
        return R.layout.i6;
    }

    public View getItemView(int position, View convertView, ViewHolder holder) {
        final HistoryRecord historyRecord = (HistoryRecord) getItem(position);
        if (historyRecord != null) {
            TextView tv_week = (TextView) holder.getView(R.id.tv_week);
            TextView tv_des = (TextView) holder.getView(R.id.tv_des);
            Button btn_copy = (Button) holder.getView(R.id.btn_copy);
            ((TextView) holder.getView(R.id.tv_date)).setText(DateFormatUtils.string2String
                    (historyRecord.record_on, "M-d"));
            tv_week.setText(DateFormatUtils.getWeekOfDate(historyRecord.record_on));
            tv_des.setText(String.format("摄入：%1$s千卡 / 消耗：%2$s千卡", new Object[]{historyRecord
                    .eating_calory.substring(0, historyRecord.eating_calory.lastIndexOf(".")),
                    historyRecord.activity_calory.substring(0, historyRecord.activity_calory
                            .lastIndexOf("."))}));
            btn_copy.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    CopyRecordActivity.start(HistoryRecordListAdapter.this.context, historyRecord
                            .record_on, HistoryRecordListAdapter.this.record_on);
                }
            });
        }
        return convertView;
    }
}
