package com.boohee.status;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.boohee.model.status.HotTopic;
import com.boohee.one.R;
import com.boohee.utility.ImageLoaderOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

public class HotTopicAdapter extends BaseAdapter {
    private static final List<HotTopic> mDataList = new ArrayList();
    private Context mContext;

    private static class ViewHolder {
        public ImageView iv_header;
        public TextView  tv_desc;
        public TextView  tv_title;

        private ViewHolder() {
        }
    }

    public HotTopicAdapter(Context context) {
        this.mContext = context;
    }

    public List<HotTopic> getData() {
        return mDataList;
    }

    public int getCount() {
        return mDataList.size();
    }

    public Object getItem(int position) {
        return mDataList.get(position);
    }

    public long getItemId(int position) {
        return (long) position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(this.mContext).inflate(R.layout.i7, null);
            holder.iv_header = (ImageView) convertView.findViewById(R.id.iv_header);
            holder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
            holder.tv_desc = (TextView) convertView.findViewById(R.id.tv_desc);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        HotTopic topic = (HotTopic) getItem(position);
        holder.tv_title.setText(TextUtils.isEmpty(topic.title) ? "" : String.valueOf(topic.title));
        holder.tv_desc.setText(TextUtils.isEmpty(topic.desc) ? "" : String.valueOf(topic.desc));
        ImageLoader.getInstance().displayImage(topic.pic_url, holder.iv_header,
                ImageLoaderOptions.randomColor());
        return convertView;
    }
}
