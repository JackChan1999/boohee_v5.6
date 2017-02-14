package com.boohee.food.adapter;

import android.content.Context;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

import com.boohee.model.CustomFood;
import com.boohee.one.R;
import com.boohee.one.ui.adapter.SimpleBaseAdapter;
import com.boohee.one.ui.adapter.SimpleBaseAdapter.ViewHolder;
import com.boohee.utility.ImageLoaderOptions;
import com.boohee.utils.FoodUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

import de.hdodenhof.circleimageview.CircleImageView;

import java.util.List;

public class CustomFoodAdapter extends SimpleBaseAdapter<CustomFood> {
    private boolean       isEdit;
    private List<Boolean> mDataSelect;

    public CustomFoodAdapter(Context context, List<CustomFood> data, List<Boolean> dataSelect) {
        super(context, data);
        this.mDataSelect = dataSelect;
    }

    public int getItemResource() {
        return R.layout.hv;
    }

    public View getItemView(final int position, View convertView, ViewHolder holder) {
        CustomFood food = (CustomFood) getItem(position);
        if (food != null) {
            ImageLoader.getInstance().displayImage(food.image_url, (CircleImageView) holder
                    .getView(R.id.civ_icon), ImageLoaderOptions.global((int) R.drawable.aa2));
            ((TextView) holder.getView(R.id.tv_name)).setText(food.food_name);
            ((TextView) holder.getView(R.id.tv_calory)).setText(FoodUtils.showUnitValue(food
                    .calory, false));
            ((TextView) holder.getView(R.id.tv_unit)).setText(String.format(" 千卡/%1$s%2$s", new
                    Object[]{food.amount, food.unit_name}));
            CheckBox cbSelect = (CheckBox) holder.getView(R.id.cb_select);
            if (this.isEdit) {
                cbSelect.setVisibility(0);
                cbSelect.setOnCheckedChangeListener(new OnCheckedChangeListener() {
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (position < CustomFoodAdapter.this.mDataSelect.size()) {
                            CustomFoodAdapter.this.mDataSelect.set(position, Boolean.valueOf
                                    (isChecked));
                        }
                    }
                });
            } else {
                cbSelect.setVisibility(8);
            }
            cbSelect.setChecked(((Boolean) this.mDataSelect.get(position)).booleanValue());
        }
        return convertView;
    }

    public void setEdit(boolean edit) {
        this.isEdit = edit;
        notifyDataSetChanged();
    }
}
