package com.boohee.one.ui.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.boohee.model.HomeTopic;
import com.boohee.one.R;
import com.boohee.one.ui.adapter.SimpleBaseAdapter.ViewHolder;
import com.boohee.utility.DensityUtil;
import com.boohee.utility.ImageLoaderOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

public class HomeTopicAdapter extends SimpleBaseAdapter<HomeTopic> {
    private ImageLoader mLoader = ImageLoader.getInstance();

    public HomeTopicAdapter(Context context, List<HomeTopic> data) {
        super(context, data);
    }

    public int getItemResource() {
        return R.layout.jf;
    }

    public View getItemView(int position, View convertView, ViewHolder holder) {
        HomeTopic topic = (HomeTopic) getItem(position);
        if (topic.user != null) {
            this.mLoader.displayImage(topic.user.avatar_url, (ImageView) holder.getView(R.id
                    .iv_user_avatar), ImageLoaderOptions.color(R.color.h6));
        }
        ((TextView) holder.getView(R.id.tv_topic_name)).setText(topic.name);
        ((TextView) holder.getView(R.id.tv_joiner_count)).setText(topic.joiners_count + "");
        ((TextView) holder.getView(R.id.tv_posts_count)).setText(topic.posts_count + "");
        ((TextView) holder.getView(R.id.tv_tags_count)).setText(topic.tags_count + "");
        initImage((LinearLayout) holder.getView(R.id.image_layout), topic);
        return convertView;
    }

    private void initImage(LinearLayout imageLayout, HomeTopic topic) {
        imageLayout.removeAllViews();
        if (topic.images == null || topic.images.length <= 0) {
            this.mLoader.displayImage(topic.image_url, getImageView(imageLayout, true),
                    ImageLoaderOptions.color(R.color.h6));
            return;
        }
        for (String imageUrl : topic.images) {
            this.mLoader.displayImage(imageUrl, getImageView(imageLayout, false),
                    ImageLoaderOptions.color(R.color.h6));
        }
    }

    private ImageView getImageView(LinearLayout imageLayout, boolean isSingle) {
        LayoutParams params;
        ImageView imageView = new ImageView(this.context);
        imageLayout.addView(imageView);
        imageView.setScaleType(ScaleType.CENTER_CROP);
        if (isSingle) {
            params = new LayoutParams(-1, DensityUtil.dip2px(this.context, 150.0f));
        } else {
            params = new LayoutParams(0, DensityUtil.dip2px(this.context, 150.0f), 1.0f);
        }
        params.setMargins(DensityUtil.dip2px(this.context, 8.0f), 0, 0, DensityUtil.dip2px(this
                .context, 8.0f));
        imageView.setLayoutParams(params);
        imageView.setAdjustViewBounds(false);
        return imageView;
    }
}
