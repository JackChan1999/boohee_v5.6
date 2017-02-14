package com.boohee.food.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.boohee.model.CustomCookItem;
import com.boohee.one.R;
import com.boohee.one.ui.adapter.SimpleBaseAdapter;
import com.boohee.one.ui.adapter.SimpleBaseAdapter.ViewHolder;
import com.boohee.utility.ImageLoaderOptions;
import com.boohee.utility.TimeLinePatterns;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

public class AddCustomCookAdapter extends SimpleBaseAdapter<CustomCookItem> {
    public AddCustomCookAdapter(Context context, List<CustomCookItem> data) {
        super(context, data);
    }

    public int getItemResource() {
        return R.layout.he;
    }

    public View getItemView(int position, View convertView, ViewHolder holder) {
        CustomCookItem cook = (CustomCookItem) getItem(position);
        if (cook != null) {
            ImageView civIcon = (ImageView) holder.getView(R.id.civ_icon);
            if (!TextUtils.isEmpty(cook.photo)) {
                ImageLoader.getInstance().displayImage(cook.photo.contains(TimeLinePatterns
                        .WEB_SCHEME) ? cook.photo : TimeLinePatterns.WEB_SCHEME + cook.photo,
                        civIcon, ImageLoaderOptions.global((int) R.drawable.aa2));
            }
            ((TextView) holder.getView(R.id.tv_name)).setText(cook.name);
            ((TextView) holder.getView(R.id.tv_calory)).setText(String.format("%.1f 千卡/份", new
                    Object[]{Float.valueOf(cook.calory)}));
            ((CheckBox) holder.getView(R.id.cb_select)).setVisibility(8);
        }
        return convertView;
    }
}
