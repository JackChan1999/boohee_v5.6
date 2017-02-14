package com.boohee.food;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.boohee.api.FoodApi;
import com.boohee.model.FavourFood;
import com.boohee.one.R;
import com.boohee.one.ui.adapter.SimpleBaseAdapter;
import com.boohee.one.ui.adapter.SimpleBaseAdapter.ViewHolder;
import com.boohee.utility.ImageLoaderOptions;
import com.boohee.utils.FoodUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

public class FavourFoodListAdapter extends SimpleBaseAdapter<FavourFood> {
    public FavourFoodListAdapter(Context context, List<FavourFood> data) {
        super(context, data);
    }

    public int getItemResource() {
        return R.layout.hf;
    }

    public View getItemView(int position, View convertView, ViewHolder holder) {
        FavourFood food = (FavourFood) getItem(position);
        if (food != null) {
            ImageView civ_title = (ImageView) holder.getView(R.id.civ_icon);
            TextView tv_name = (TextView) holder.getView(R.id.tv_name);
            TextView tv_calory = (TextView) holder.getView(R.id.tv_calory);
            TextView tv_unit = (TextView) holder.getView(R.id.tv_unit);
            ImageView iv_health_light = (ImageView) holder.getView(R.id.iv_light);
            if (!TextUtils.isEmpty(food.thumb_image_name)) {
                ImageLoader.getInstance().displayImage(FoodApi.HOST_IMAGE + food
                        .thumb_image_name, civ_title, ImageLoaderOptions.global((int) R.drawable
                        .aa2));
            }
            tv_name.setText(food.name);
            tv_calory.setText(Math.round(Float.parseFloat(food.calory)) + "");
            tv_unit.setText(this.context.getResources().getString(R.string.en));
            FoodUtils.switchToLight(food.health_light, iv_health_light);
        }
        return convertView;
    }
}
