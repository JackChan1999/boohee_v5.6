package com.boohee.uchoice;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.boohee.model.Showcase;
import com.boohee.one.R;
import com.boohee.utility.Event;
import com.boohee.utility.ImageLoaderOptions;
import com.boohee.utils.ResolutionUtils;
import com.boohee.utils.ShopUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.analytics.MobclickAgent;

import java.util.List;

public class ShopShowcaseAdapter extends BaseAdapter implements OnClickListener {
    private Context        mContext;
    private List<Showcase> mDataList;
    private final ImageLoader         mLoader         = ImageLoader.getInstance();
    private final DisplayImageOptions mOptions_normal = ImageLoaderOptions.global((int) R
            .drawable.aa3);

    public static class ViewHolder {
        public ImageView iv_ad;
    }

    public ShopShowcaseAdapter(Context context, List<Showcase> showcaseList) {
        this.mContext = context;
        this.mDataList = showcaseList;
    }

    public int getCount() {
        return this.mDataList.size();
    }

    public Object getItem(int position) {
        return Integer.valueOf(position);
    }

    public long getItemId(int position) {
        return (long) position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(this.mContext).inflate(R.layout.nh, null);
            holder.iv_ad = (ImageView) convertView.findViewById(R.id.iv_ad);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Showcase showcase = (Showcase) this.mDataList.get(position);
        if (showcase != null) {
            holder.iv_ad.getLayoutParams().height = ResolutionUtils.getHeight(this.mContext,
                    showcase.default_photo_height, showcase.default_photo_width);
            this.mLoader.displayImage(showcase.default_photo_url, holder.iv_ad, this
                    .mOptions_normal);
            holder.iv_ad.setTag(showcase);
            holder.iv_ad.setOnClickListener(this);
        }
        return convertView;
    }

    public void onClick(View v) {
        Showcase showcase = (Showcase) v.getTag();
        MobclickAgent.onEvent(this.mContext, Event.shop_clickListBanner);
        ShopUtils.handleExhibit(this.mContext, showcase);
    }
}
