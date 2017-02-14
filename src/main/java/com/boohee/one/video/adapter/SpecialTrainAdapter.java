package com.boohee.one.video.adapter;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.boohee.one.R;
import com.boohee.one.ui.adapter.SimpleBaseAdapter;
import com.boohee.one.ui.adapter.SimpleBaseAdapter.ViewHolder;
import com.boohee.one.video.entity.SpecialTrain;
import com.boohee.one.video.ui.SpecialLessonDetailActivity;
import com.boohee.utility.ImageLoaderOptions;
import com.boohee.utils.ViewUtils;
import com.boohee.utils.WheelUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

public class SpecialTrainAdapter extends SimpleBaseAdapter<SpecialTrain> {
    public SpecialTrainAdapter(Context context, List<SpecialTrain> data) {
        super(context, data);
    }

    public int getItemResource() {
        return R.layout.j0;
    }

    public View getItemView(int position, View convertView, ViewHolder holder) {
        View layout = holder.getView(R.id.layout);
        TextView tvTrainName = (TextView) holder.getView(R.id.tv_train_name);
        TextView tvTrainDes = (TextView) holder.getView(R.id.tv_train_des);
        TextView tvColory = (TextView) holder.getView(R.id.tv_calory);
        TextView tvTime = (TextView) holder.getView(R.id.tv_time);
        TextView tvPersonCount = (TextView) holder.getView(R.id.tv_person_count);
        ImageView ivBg = (ImageView) holder.getView(R.id.iv_bg);
        ImageView ivIsJoined = (ImageView) holder.getView(R.id.iv_is_joined);
        ViewUtils.setViewScaleHeight(this.context, layout, 2, 1);
        ViewUtils.setViewScaleHeight(this.context, ivBg, 2, 1);
        final SpecialTrain specialTrain = (SpecialTrain) getItem(position);
        tvTrainName.setText(specialTrain.name);
        tvTrainDes.setText(specialTrain.description);
        tvColory.setText(String.format(this.context.getString(R.string.a5j), new Object[]{Integer
                .valueOf(specialTrain.calorie)}));
        tvTime.setText(String.format(this.context.getString(R.string.a6d), new Object[]{Integer
                .valueOf(specialTrain.minutes)}));
        tvPersonCount.setText(String.format(this.context.getString(R.string.a5l), new
                Object[]{Integer.valueOf(specialTrain.join_count)}));
        if (specialTrain.isJioned) {
            ivIsJoined.setVisibility(0);
            tvTrainDes.setVisibility(8);
            tvPersonCount.setVisibility(8);
        } else {
            ivIsJoined.setVisibility(4);
            tvTrainDes.setVisibility(0);
            tvPersonCount.setVisibility(0);
        }
        layout.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                if (SpecialTrainAdapter.this.context != null && !WheelUtils.isFastDoubleClick()) {
                    SpecialLessonDetailActivity.comeOn(SpecialTrainAdapter.this.context,
                            specialTrain.id, specialTrain.isJioned);
                }
            }
        });
        ImageLoader.getInstance().displayImage(specialTrain.pic_url, ivBg, ImageLoaderOptions
                .randomColorWithOrder(position));
        return convertView;
    }
}
