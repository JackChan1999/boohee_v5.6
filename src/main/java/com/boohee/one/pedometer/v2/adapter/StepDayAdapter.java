package com.boohee.one.pedometer.v2.adapter;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.boohee.one.R;
import com.boohee.one.pedometer.StepCounterUtil;
import com.boohee.one.pedometer.v2.model.StepDayItem;
import com.boohee.one.pedometer.v2.model.StepDayItem.ReceivedRewardsBean;
import com.boohee.one.ui.adapter.SimpleBaseAdapter;
import com.boohee.one.ui.adapter.SimpleBaseAdapter.ViewHolder;
import com.boohee.utils.DateFormatUtils;
import com.boohee.utils.ViewUtils;

import java.util.Date;
import java.util.List;

public class StepDayAdapter extends SimpleBaseAdapter<StepDayItem> {
    private static final int    MAX_HEIGHT = 200;
    private static final int    MAX_STEP   = 20000;
    private static final int    MIN_HEIGHT = 100;
    private static final int    MIN_STEP   = 3000;
    private              String today      = DateFormatUtils.date2string(new Date(), "yyyy-MM-dd");

    public StepDayAdapter(Context context, List<StepDayItem> data) {
        super(context, data);
    }

    public int getItemResource() {
        return R.layout.j8;
    }

    public View getItemView(int position, View convertView, ViewHolder holder) {
        if (this.data.size() != 0) {
            int itemHeight;
            View view_content = holder.getView(R.id.view_content);
            if (position % 2 == 0) {
                view_content.setBackgroundResource(R.drawable.ht);
            } else {
                view_content.setBackgroundResource(R.drawable.hu);
            }
            StepDayItem item = (StepDayItem) this.data.get(position);
            TextView tv_day = (TextView) holder.getView(R.id.tv_day);
            String recordOn = item.getRecord_on();
            if (recordOn.equals(this.today)) {
                tv_day.setText("今日");
            } else {
                tv_day.setText(DateFormatUtils.string2String(recordOn, "M.dd"));
            }
            TextView tv_step = (TextView) holder.getView(R.id.tv_step);
            int step = item.getStep();
            tv_step.setText(String.valueOf(item.getStep()));
            ((TextView) holder.getView(R.id.tv_step_calory)).setText(String.valueOf
                    (StepCounterUtil.getCalory(item.getStep()) + "千卡"));
            LinearLayout ll_step_diamond = (LinearLayout) holder.getView(R.id.ll_step_diamond);
            TextView tv_step_diamond = (TextView) holder.getView(R.id.tv_step_diamond);
            LinearLayout ll_step_coupon = (LinearLayout) holder.getView(R.id.ll_step_coupon);
            TextView tv_step_coupon = (TextView) holder.getView(R.id.tv_step_coupon);
            List<ReceivedRewardsBean> rewards = item.getReceived_rewards();
            if (rewards == null || rewards.size() <= 0) {
                ll_step_diamond.setVisibility(8);
                ll_step_coupon.setVisibility(8);
            } else {
                for (ReceivedRewardsBean reward : rewards) {
                    if (reward == null || !reward.getReward().contains("钻石")) {
                        ll_step_diamond.setVisibility(8);
                    } else {
                        ll_step_diamond.setVisibility(0);
                        tv_step_diamond.setText(String.valueOf(reward.getReward()));
                    }
                    if (reward == null || !reward.getReward().contains("优惠券")) {
                        ll_step_coupon.setVisibility(8);
                    } else {
                        ll_step_coupon.setVisibility(0);
                        tv_step_coupon.setText(String.valueOf(reward.getReward()));
                    }
                }
            }
            if (step <= 3000) {
                itemHeight = ViewUtils.dip2px(this.context, 100.0f);
            } else if (step > 20000) {
                itemHeight = ViewUtils.dip2px(this.context, 200.0f);
            } else {
                itemHeight = ViewUtils.dip2px(this.context, 100.0f + ((float) (((step - 3000) *
                        100) / 17000)));
            }
            if (view_content.getLayoutParams() != null) {
                view_content.getLayoutParams().height = itemHeight;
            }
        }
        return convertView;
    }
}
