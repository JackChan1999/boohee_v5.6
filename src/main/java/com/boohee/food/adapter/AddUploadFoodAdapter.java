package com.boohee.food.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.boohee.model.UploadFoodItem;
import com.boohee.one.R;
import com.boohee.one.ui.adapter.SimpleBaseAdapter;
import com.boohee.one.ui.adapter.SimpleBaseAdapter.ViewHolder;
import com.boohee.utility.ImageLoaderOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

public class AddUploadFoodAdapter extends SimpleBaseAdapter<UploadFoodItem> {
    public AddUploadFoodAdapter(Context context, List<UploadFoodItem> data) {
        super(context, data);
    }

    public int getItemResource() {
        return R.layout.jj;
    }

    public View getItemView(int position, View convertView, ViewHolder holder) {
        UploadFoodItem food = (UploadFoodItem) getItem(position);
        if (food != null) {
            ImageView civIcon = (ImageView) holder.getView(R.id.civ_icon);
            if (!TextUtils.isEmpty(food.thumb_image_url)) {
                ImageLoader.getInstance().displayImage(food.thumb_image_url, civIcon,
                        ImageLoaderOptions.global((int) R.drawable.aa2));
            }
            ((TextView) holder.getView(R.id.tv_name)).setText(food.food_name);
            ((TextView) holder.getView(R.id.tv_calory)).setText(String.format("%.1f 千卡/100克", new
                    Object[]{Float.valueOf(food.calory)}));
        }
        return convertView;
    }
}
