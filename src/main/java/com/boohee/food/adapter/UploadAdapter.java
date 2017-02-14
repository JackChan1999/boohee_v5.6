package com.boohee.food.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.boohee.food.FoodDetailActivity;
import com.boohee.food.UploadStateActivity;
import com.boohee.model.UploadFood;
import com.boohee.one.R;
import com.boohee.one.ui.adapter.SimpleBaseAdapter;
import com.boohee.one.ui.adapter.SimpleBaseAdapter.ViewHolder;
import com.boohee.utility.ImageLoaderOptions;
import com.boohee.widgets.BooheeListView;
import com.nostra13.universalimageloader.core.ImageLoader;

import de.hdodenhof.circleimageview.CircleImageView;

import java.util.List;

public class UploadAdapter extends SimpleBaseAdapter<UploadFood> implements OnItemClickListener {
    private BooheeListView blvContent;

    public UploadAdapter(Context context, List<UploadFood> data, BooheeListView blv) {
        super(context, data);
        this.blvContent = blv;
        this.blvContent.setOnItemClickListener(this);
    }

    public int getItemResource() {
        return R.layout.ji;
    }

    public View getItemView(int position, View convertView, ViewHolder holder) {
        UploadFood food = (UploadFood) getItem(position);
        if (food != null) {
            ImageView civIcon = (CircleImageView) holder.getView(R.id.civ_icon);
            if (!TextUtils.isEmpty(food.thumb_img_url)) {
                ImageLoader.getInstance().displayImage(food.thumb_img_url, civIcon,
                        ImageLoaderOptions.global((int) R.drawable.aa2));
            }
            ((TextView) holder.getView(R.id.tv_name)).setText(TextUtils.isEmpty(food.food_name) ?
                    food.barcode : food.food_name);
            try {
                String state = getStateString(this.context, food.state);
                if (!(TextUtils.isEmpty(food.message) || "null".equals(food.message))) {
                    state = state + ":" + food.message;
                }
                ((TextView) holder.getView(R.id.tv_state)).setText(state);
            } catch (Exception e) {
                e.printStackTrace();
            }
            ImageView ivState = (ImageView) holder.getView(R.id.iv_success);
            if (food.state == 0) {
                ImageLoader.getInstance().displayImage("", ivState, ImageLoaderOptions.global(
                        (int) R.drawable.a9i));
            } else if (food.state == 1) {
                ImageLoader.getInstance().displayImage("", ivState, ImageLoaderOptions.global(
                        (int) R.drawable.a9h));
            } else if (food.state == 2) {
                ImageLoader.getInstance().displayImage("", ivState, ImageLoaderOptions.global(
                        (int) R.drawable.a9g));
            }
        }
        return convertView;
    }

    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        UploadFood draftFood = (UploadFood) getItem(position);
        if (TextUtils.isEmpty(draftFood.code)) {
            UploadStateActivity.comeOnBaby(this.context, draftFood);
        } else {
            FoodDetailActivity.comeOnBaby(this.context, draftFood.code, false);
        }
    }

    public static final String getStateString(Context context, int state) {
        try {
            return context.getResources().getStringArray(R.array.e)[state];
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
}
