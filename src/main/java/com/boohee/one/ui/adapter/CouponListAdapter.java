package com.boohee.one.ui.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

import com.boohee.model.Coupon;
import com.boohee.one.R;
import com.boohee.one.ui.adapter.SimpleBaseAdapter.ViewHolder;
import com.boohee.utils.DateHelper;

import java.util.HashMap;
import java.util.List;

public class CouponListAdapter extends SimpleBaseAdapter<Coupon> {
    private final HashMap<Integer, Boolean> CHECK_STATUS = new HashMap();

    public CouponListAdapter(Context context, List<Coupon> data) {
        super(context, data);
        for (int i = 0; i < data.size(); i++) {
            this.CHECK_STATUS.put(Integer.valueOf(i), Boolean.valueOf(false));
        }
    }

    public int getItemResource() {
        return R.layout.ho;
    }

    public View getItemView(int position, View convertView, ViewHolder holder) {
        TextView tv_title = (TextView) holder.getView(R.id.tv_title);
        TextView tv_valid = (TextView) holder.getView(R.id.tv_valid);
        TextView tv_limit_money = (TextView) holder.getView(R.id.tv_limit_money);
        final TextView tv_description = (TextView) holder.getView(R.id.tv_description);
        Coupon coupon = (Coupon) this.data.get(position);
        try {
            ((TextView) holder.getView(R.id.tv_money)).setText(String.valueOf((int) Float
                    .parseFloat(coupon.amount)));
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        tv_title.setText(coupon.title);
        String startTime = DateHelper.formatString(coupon.started_at, "yyyy-MM-dd");
        String endTime = DateHelper.formatString(coupon.expired_at, "yyyy-MM-dd");
        tv_valid.setText(String.format("有效期%s至%s", new Object[]{startTime, endTime}));
        tv_limit_money.setText(String.format("满%s使用", new Object[]{coupon.order_amount}));
        tv_description.setText(TextUtils.isEmpty(coupon.description) ? "无" : coupon.description);
        CheckBox view_status = (CheckBox) holder.getView(R.id.view_status);
        view_status.setOnCheckedChangeListener(null);
        boolean isChecked = false;
        if (this.CHECK_STATUS.size() <= 0 || !this.CHECK_STATUS.containsKey(Integer.valueOf
                (position))) {
            tv_description.setVisibility(8);
        } else {
            isChecked = ((Boolean) this.CHECK_STATUS.get(Integer.valueOf(position))).booleanValue();
            tv_description.setVisibility(isChecked ? 0 : 8);
        }
        view_status.setChecked(isChecked);
        final int i = position;
        view_status.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                tv_description.setVisibility(isChecked ? 0 : 8);
                CouponListAdapter.this.CHECK_STATUS.put(Integer.valueOf(i), Boolean.valueOf
                        (isChecked));
            }
        });
        return convertView;
    }
}
