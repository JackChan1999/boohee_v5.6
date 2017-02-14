package com.boohee.more;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.boohee.model.Notice;
import com.boohee.one.R;

import java.util.ArrayList;

public class NoticeListAdapter extends BaseAdapter {
    private Context ctx;
    private ArrayList<Notice> notices = null;

    static class ViewHolder {
        public TextView  category_label;
        public ImageView is_opened_image;
        public TextView  message_label;

        ViewHolder() {
        }
    }

    public NoticeListAdapter(Context context, ArrayList<Notice> notices) {
        this.ctx = context;
        this.notices = notices;
    }

    public int getCount() {
        return this.notices.size();
    }

    public Notice getItem(int position) {
        return (Notice) this.notices.get(position);
    }

    public long getItemId(int position) {
        return (long) position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(this.ctx).inflate(R.layout.n4, null);
            holder.message_label = (TextView) convertView.findViewById(R.id.message);
            holder.is_opened_image = (ImageView) convertView.findViewById(R.id.is_opened_dot);
            holder.category_label = (TextView) convertView.findViewById(R.id.category);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.message_label.setText(getItem(position).alarm_tip_message);
        if (getItem(position).isOpened()) {
            holder.is_opened_image.setVisibility(4);
        } else {
            holder.is_opened_image.setVisibility(0);
        }
        if (getItem(position).alarm_tip_id == 0) {
            holder.category_label.setText(R.string.st);
        } else {
            holder.category_label.setText(R.string.y6);
        }
        return convertView;
    }
}
