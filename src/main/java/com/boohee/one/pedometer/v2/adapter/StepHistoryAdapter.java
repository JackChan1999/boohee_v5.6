package com.boohee.one.pedometer.v2.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.boohee.one.R;
import com.boohee.one.pedometer.v2.model.StepHistoryFullItem;
import com.boohee.one.pedometer.v2.model.StepHistoryItem;
import com.boohee.one.ui.adapter.SimpleBaseAdapter;
import com.boohee.one.ui.adapter.SimpleBaseAdapter.ViewHolder;
import com.boohee.utils.DateFormatUtils;

import java.util.List;

public class StepHistoryAdapter extends SimpleBaseAdapter<StepHistoryFullItem> {
    public StepHistoryAdapter(Context context, List<StepHistoryFullItem> data) {
        super(context, data);
    }

    public int getItemResource() {
        return R.layout.j9;
    }

    public View getItemView(int position, View convertView, ViewHolder holder) {
        if (this.data.size() != 0) {
            StepHistoryFullItem itemData = (StepHistoryFullItem) this.data.get(position);
            TextView tvYear = (TextView) holder.getView(R.id.tv_year);
            View viewContent = holder.getView(R.id.view_content);
            if (TextUtils.isEmpty(itemData.year)) {
                tvYear.setVisibility(8);
                viewContent.setVisibility(0);
                StepHistoryItem item = itemData.itemData;
                ((TextView) holder.getView(R.id.tv_month)).setText(DateFormatUtils.string2String
                        (item.getDate(), "M月"));
                ((TextView) holder.getView(R.id.tv_step)).setText(String.format("月累计步数 %d", new
                        Object[]{Integer.valueOf(item.getSteps())}));
                TextView tv_info = (TextView) holder.getView(R.id.tv_info);
                if (TextUtils.isEmpty(item.getRewards())) {
                    tv_info.setVisibility(8);
                } else {
                    tv_info.setText(item.getRewards());
                    tv_info.setVisibility(0);
                }
                convertView.setTag(R.id.step_v2_history_key, item);
            } else {
                tvYear.setText(itemData.year);
                tvYear.setVisibility(0);
                viewContent.setVisibility(8);
            }
        }
        return convertView;
    }
}
