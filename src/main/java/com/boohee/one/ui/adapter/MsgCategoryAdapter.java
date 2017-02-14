package com.boohee.one.ui.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.boohee.model.MsgCategory;
import com.boohee.one.R;

import java.util.List;

public class MsgCategoryAdapter extends MyAdapter {
    public MsgCategoryAdapter(Activity activity, List<? extends Object> items) {
        super(activity, items);
    }

    protected View getItemView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(this.activity).inflate(R.layout.k5, null);
        }
        MsgCategory category = (MsgCategory) getItem(position);
        ((ImageView) convertView.findViewById(R.id.iv_icon)).setImageResource(category.iconId);
        ((TextView) convertView.findViewById(R.id.tv_title)).setText(((MsgCategory) getItem
                (position)).title);
        TextView bubble = (TextView) convertView.findViewById(R.id.tv_bubble);
        if (category.count > 0) {
            bubble.setVisibility(0);
            bubble.setText(category.count > 99 ? "99+" : category.count + "");
        } else {
            bubble.setVisibility(8);
        }
        return convertView;
    }
}
