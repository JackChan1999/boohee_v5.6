package com.boohee.one.pedometer.v2.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.boohee.one.R;
import com.boohee.one.pedometer.v2.model.StepReward;
import com.boohee.one.ui.adapter.SimpleBaseAdapter;
import com.boohee.one.ui.adapter.SimpleBaseAdapter.ViewHolder;
import com.boohee.utils.DateFormatUtils;

import java.util.List;

public class RewardsAdapter extends SimpleBaseAdapter<StepReward> {
    public RewardsAdapter(Context context, List<StepReward> data) {
        super(context, data);
    }

    public int getItemResource() {
        return R.layout.ja;
    }

    public View getItemView(int position, View convertView, ViewHolder holder) {
        if (this.data.size() != 0) {
            StepReward reward = (StepReward) this.data.get(position);
            ImageView iv_icon = (ImageView) holder.getView(R.id.iv_icon);
            if ("diamond".equals(reward.getReward_type())) {
                iv_icon.setImageResource(R.drawable.a33);
            } else {
                iv_icon.setImageResource(R.drawable.a2q);
            }
            ((TextView) holder.getView(R.id.tv_step)).setText(DateFormatUtils.string2String
                    (reward.getReward_date(), "M月dd日"));
            ((TextView) holder.getView(R.id.tv_info)).setText(reward.getDescription());
            ((TextView) holder.getView(R.id.tv_value)).setText(String.valueOf(reward
                    .getReward_value()));
        }
        return convertView;
    }
}
