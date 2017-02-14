package com.boohee.food.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.boohee.model.CollectionFood;
import com.boohee.one.R;
import com.boohee.one.ui.adapter.SimpleBaseAdapter;
import com.boohee.one.ui.adapter.SimpleBaseAdapter.ViewHolder;
import com.boohee.utility.ImageLoaderOptions;
import com.boohee.utils.FoodUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

import de.hdodenhof.circleimageview.CircleImageView;

import java.util.List;

public class CollectionAdapter extends SimpleBaseAdapter<CollectionFood> {
    private boolean       isEdit;
    private List<Boolean> mDataSelect;

    public CollectionAdapter(Context context, List<CollectionFood> data, List<Boolean> dataSelect) {
        super(context, data);
        this.mDataSelect = dataSelect;
    }

    public int getItemResource() {
        return R.layout.hl;
    }

    public View getItemView(final int position, View convertView, ViewHolder holder) {
        CollectionFood food = (CollectionFood) getItem(position);
        if (food != null) {
            ImageView civIcon = (CircleImageView) holder.getView(R.id.civ_icon);
            if (!TextUtils.isEmpty(food.thumb_image_url)) {
                ImageLoader.getInstance().displayImage(food.thumb_image_url, civIcon,
                        ImageLoaderOptions.global((int) R.drawable.aa2));
            }
            ((TextView) holder.getView(R.id.tv_name)).setText(food.name);
            ((TextView) holder.getView(R.id.tv_calory)).setText(FoodUtils.showUnitValue(food
                    .calory, false));
            ((TextView) holder.getView(R.id.tv_unit)).setText(FoodUtils.changeUnitAndWeight(food
                    .weight, food.is_liquid, false));
            ImageView ivLight = (ImageView) holder.getView(R.id.iv_light);
            FoodUtils.switchToLight(food.health_light, ivLight);
            CheckBox cbSelect = (CheckBox) holder.getView(R.id.cb_select);
            if (this.isEdit) {
                cbSelect.setVisibility(0);
                ivLight.setVisibility(8);
                cbSelect.setOnCheckedChangeListener(new OnCheckedChangeListener() {
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (position < CollectionAdapter.this.mDataSelect.size()) {
                            CollectionAdapter.this.mDataSelect.set(position, Boolean.valueOf
                                    (isChecked));
                        }
                    }
                });
            } else {
                cbSelect.setVisibility(8);
                ivLight.setVisibility(0);
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
