package com.boohee.one.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.boohee.model.AppRecommend;
import com.boohee.one.R;
import com.boohee.one.http.client.BooheeClient;
import com.boohee.utility.ImageLoaderOptions;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

public class AppRecommendListAdapter extends BaseAdapter {
    private ArrayList<AppRecommend> apps = null;
    private Context             ctx;
    private ImageLoader         imageLoader;
    private DisplayImageOptions option;

    final class ViewHolder {
        public ImageView icon;
        public TextView  name;

        ViewHolder() {
        }
    }

    public AppRecommendListAdapter(Context ctx, ArrayList<AppRecommend> apps) {
        this.ctx = ctx;
        this.apps = apps;
        this.imageLoader = ImageLoader.getInstance();
        this.option = ImageLoaderOptions.global();
    }

    public int getCount() {
        if (this.apps == null) {
            return 0;
        }
        return this.apps.size();
    }

    public AppRecommend getItem(int position) {
        return (AppRecommend) this.apps.get(position);
    }

    public long getItemId(int position) {
        return (long) position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(this.ctx).inflate(R.layout.e6, null);
            holder.icon = (ImageView) convertView.findViewById(R.id.icon);
            holder.name = (TextView) convertView.findViewById(R.id.name);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        ImageLoader imageLoader = this.imageLoader;
        StringBuilder stringBuilder = new StringBuilder();
        BooheeClient.build(BooheeClient.ONE);
        imageLoader.displayImage(stringBuilder.append(BooheeClient.getHost(BooheeClient.ONE))
                .append(getItem(position).icon).toString(), holder.icon, this.option);
        holder.name.setText(getItem(position).name);
        return convertView;
    }
}
