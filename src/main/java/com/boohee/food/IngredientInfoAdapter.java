package com.boohee.food;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.boohee.model.IngredientInfo;
import com.boohee.one.R;
import com.boohee.one.ui.adapter.SimpleBaseAdapter;
import com.boohee.one.ui.adapter.SimpleBaseAdapter.ViewHolder;
import com.boohee.utils.FoodUtils;

import java.util.List;

public class IngredientInfoAdapter extends SimpleBaseAdapter<IngredientInfo> {
    public IngredientInfoAdapter(Context context, List<IngredientInfo> data) {
        super(context, data);
    }

    public int getItemResource() {
        return R.layout.i8;
    }

    public View getItemView(int position, View convertView, ViewHolder holder) {
        IngredientInfo info = (IngredientInfo) getItem(position);
        if (info != null) {
            ((TextView) holder.getView(R.id.tv_name)).setText(info.name);
            TextView tvValue = (TextView) holder.getView(R.id.tv_value);
            if ("--".equals(info.value)) {
                tvValue.setText(info.value);
            } else {
                tvValue.setText(String.format("%s %s", new Object[]{FoodUtils.formatPoint(info
                        .value), info.unit}));
            }
            ((TextView) holder.getView(R.id.tv_memo)).setText(info.memo);
        }
        return convertView;
    }
}
