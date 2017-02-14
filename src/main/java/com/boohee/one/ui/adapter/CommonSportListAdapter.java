package com.boohee.one.ui.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.boohee.model.Sport;
import com.boohee.one.R;
import com.boohee.one.ui.adapter.SimpleBaseAdapter.ViewHolder;
import com.boohee.utility.ImageLoaderOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

public class CommonSportListAdapter extends SimpleBaseAdapter<Sport> {
    private float mWeight;

    public CommonSportListAdapter(Context context, List<Sport> data, float weight) {
        super(context, data);
        this.mWeight = weight;
    }

    public int getItemResource() {
        return R.layout.hg;
    }

    public View getItemView(int position, View convertView, ViewHolder holder) {
        Sport recordSport = (Sport) this.data.get(position);
        if (recordSport != null) {
            ImageView civ_icon = (ImageView) holder.getView(R.id.civ_icon);
            TextView tv_calory = (TextView) holder.getView(R.id.tv_calory);
            ((TextView) holder.getView(R.id.tv_name)).setText(recordSport.name);
            tv_calory.setText(recordSport.calcCalory(this.mWeight) + "");
            if (!TextUtils.isEmpty(recordSport.big_photo_url)) {
                ImageLoader.getInstance().displayImage(recordSport.big_photo_url, civ_icon,
                        ImageLoaderOptions.global((int) R.drawable.aa5));
            }
        }
        return convertView;
    }
}
