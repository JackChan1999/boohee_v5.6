package com.boohee.one.ui.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.boohee.model.MicroNutrientsItem;
import com.boohee.model.TodayItem.DESCRIPTION_TYPE;
import com.boohee.one.R;
import com.boohee.one.ui.adapter.SimpleBaseAdapter.ViewHolder;

import java.util.List;

public class MicroNutrientsAdapter extends SimpleBaseAdapter<MicroNutrientsItem> {
    public MicroNutrientsAdapter(Context context, List<MicroNutrientsItem> data) {
        super(context, data);
    }

    public int getItemResource() {
        return R.layout.i_;
    }

    public View getItemView(int position, View convertView, ViewHolder holder) {
        MicroNutrientsItem item = (MicroNutrientsItem) getItem(position);
        if (item != null) {
            ((TextView) holder.getView(R.id.tv_micro_name)).setText(item.name);
            ((TextView) holder.getView(R.id.tv_micro_value)).setText(item.value);
            ((TextView) holder.getView(R.id.tv_micro_rec_value)).setText(item.rec_value);
            TextView desc = (TextView) holder.getView(R.id.tv_micro_desc);
            ImageView state = (ImageView) holder.getView(R.id.iv_micro_state);
            if (TextUtils.equals(DESCRIPTION_TYPE.less.name(), item.desc)) {
                desc.setText(this.context.getString(R.string.sw));
                state.setImageResource(R.drawable.a1k);
            } else if (TextUtils.equals(DESCRIPTION_TYPE.much.name(), item.desc)) {
                desc.setText(this.context.getString(R.string.sv));
                state.setImageResource(R.drawable.a1o);
            } else if (TextUtils.equals(DESCRIPTION_TYPE.good.name(), item.desc)) {
                desc.setText(this.context.getString(R.string.sx));
                state.setImageResource(R.drawable.a1j);
            }
        }
        return convertView;
    }
}
