package com.boohee.one.ui.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.boohee.model.MealBean;
import com.boohee.one.R;
import com.boohee.one.ui.adapter.SimpleBaseAdapter.ViewHolder;
import com.boohee.utility.ImageLoaderOptions;
import com.boohee.widgets.RoundedCornersImage;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.List;

public class SelectStatusAdapter extends SimpleBaseAdapter<MealBean> {
    private final ImageLoader imageLoader = ImageLoader.getInstance();

    public SelectStatusAdapter(Context context, List<MealBean> data) {
        super(context, data);
    }

    public int getItemResource() {
        return R.layout.iw;
    }

    public View getItemView(int position, View convertView, ViewHolder holder) {
        RoundedCornersImage avatar = (RoundedCornersImage) holder.getView(R.id.avatar);
        TextView userName = (TextView) holder.getView(R.id.userName);
        ImageView imageView = (ImageView) holder.getView(R.id.imageView);
        MealBean bean = (MealBean) this.data.get(position);
        if (bean == null) {
            return null;
        }
        userName.setText(bean.getNickname());
        loadImage(avatar, bean.getUser_img());
        loadImage(imageView, bean.getImg_url());
        return convertView;
    }

    private void loadImage(final ImageView image, String imageUrl) {
        if (!TextUtils.isEmpty(imageUrl) && !imageUrl.equals(image.getTag())) {
            this.imageLoader.displayImage(imageUrl, image, ImageLoaderOptions.global((int) R
                    .color.d5), new ImageLoadingListener() {
                public void onLoadingStarted(String arg0, View arg1) {
                }

                public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {
                }

                public void onLoadingComplete(String arg0, View arg1, Bitmap arg2) {
                    image.startAnimation(AnimationUtils.loadAnimation(SelectStatusAdapter.this
                            .context, 17432576));
                }

                public void onLoadingCancelled(String arg0, View arg1) {
                }
            });
            image.setTag(imageUrl);
        }
    }
}
