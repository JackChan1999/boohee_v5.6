package com.boohee.one.bet.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.boohee.one.R;
import com.boohee.one.bet.model.Bet;
import com.boohee.one.ui.adapter.SimpleBaseAdapter;
import com.boohee.one.ui.adapter.SimpleBaseAdapter.ViewHolder;
import com.boohee.utility.BooheeScheme;
import com.boohee.utils.WheelUtils;

import java.util.List;

public class BetAdapter extends SimpleBaseAdapter<Bet> {
    public BetAdapter(Context context, List<Bet> data) {
        super(context, data);
    }

    public int getItemResource() {
        return R.layout.hj;
    }

    public View getItemView(int position, View convertView, ViewHolder holder) {
        final Bet bet = (Bet) getItem(position);
        View view = holder.getView(R.id.ll_bet);
        TextView tvBetName = (TextView) holder.getView(R.id.tv_bet_name);
        TextView tvBetTime = (TextView) holder.getView(R.id.tv_bet_time);
        TextView tvBetPercent = (TextView) holder.getView(R.id.tv_bet_percent);
        TextView tvBetScope = (TextView) holder.getView(R.id.tv_bet_scope);
        TextView btnBet = (TextView) holder.getView(R.id.btn_bet);
        ImageView ivFruit = (ImageView) holder.getView(R.id.iv_fruit);
        this.imageLoader.displayImage(bet.icon, (ImageView) holder.getView(R.id.iv_bet));
        tvBetName.setText(String.valueOf(bet.title));
        tvBetTime.setText(String.format("报名时间:%s", new Object[]{bet.date_range}));
        tvBetPercent.setText(String.format("减重:%s", new Object[]{bet.weight_ratio}));
        tvBetScope.setText(String.format("报名费:%s元", new Object[]{bet.entry_fee}));
        btnBet.setVisibility(8);
        ivFruit.setVisibility(8);
        if (TextUtils.equals(bet.status, "open")) {
            btnBet.setVisibility(0);
            btnBet.setBackgroundResource(R.drawable.cx);
            btnBet.setText("报名中");
        } else if (TextUtils.equals(bet.status, "underway")) {
            btnBet.setVisibility(0);
            btnBet.setBackgroundResource(R.drawable.cx);
            btnBet.setText("进行中");
        } else if (TextUtils.equals(bet.status, "initial")) {
            btnBet.setVisibility(0);
            btnBet.setBackgroundResource(R.drawable.bx);
            btnBet.setText("未开启");
        } else if (TextUtils.equals(bet.status, "close")) {
            ivFruit.setVisibility(0);
            if (TextUtils.equals(bet.fruit, "succeeded")) {
                ivFruit.setBackgroundResource(R.drawable.pg);
            } else if (TextUtils.equals(bet.fruit, "failed")) {
                ivFruit.setBackgroundResource(R.drawable.pd);
            } else {
                btnBet.setVisibility(0);
                ivFruit.setVisibility(8);
                btnBet.setBackgroundResource(R.drawable.bx);
                btnBet.setText("已结束");
            }
        } else if (TextUtils.equals(bet.status, "canceled")) {
            ivFruit.setVisibility(0);
            ivFruit.setBackgroundResource(R.drawable.pb);
        }
        view.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                if (!WheelUtils.isFastDoubleClick()) {
                    BooheeScheme.handleUrl(BetAdapter.this.context, bet.def_link);
                }
            }
        });
        return convertView;
    }
}
