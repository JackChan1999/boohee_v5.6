package com.boohee.one.ui.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.boohee.model.status.Broadcast;
import com.boohee.one.R;

import java.util.List;

public class BroadcastAdapter extends MyAdapter {

    static class Holder {
        public TextView  body;
        public ImageView isRead;
        public TextView  title;

        Holder(View view) {
            this.isRead = (ImageView) view.findViewById(R.id.isRead);
            this.title = (TextView) view.findViewById(R.id.tv_title);
            this.body = (TextView) view.findViewById(R.id.tv_body);
        }
    }

    public BroadcastAdapter(Activity activity, List<? extends Object> items) {
        super(activity, items);
    }

    protected View getItemView(int position, View convertView, ViewGroup parent) {
        Holder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(this.activity).inflate(R.layout.k0, null);
            holder = new Holder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        Broadcast cast = (Broadcast) this.items.get(position);
        holder.title.setText(cast.title);
        holder.body.setText(cast.preview_body);
        if (cast.read) {
            holder.isRead.setVisibility(4);
        } else {
            holder.isRead.setVisibility(0);
        }
        return convertView;
    }
}
