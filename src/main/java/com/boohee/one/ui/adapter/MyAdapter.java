package com.boohee.one.ui.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

public abstract class MyAdapter extends BaseAdapter {
    protected Activity activity;
    protected ImageLoader imageLoader = ImageLoader.getInstance();
    protected List<? extends Object> items;

    protected abstract View getItemView(int i, View view, ViewGroup viewGroup);

    protected MyAdapter(Activity activity, List<? extends Object> items) {
        this.items = items;
        this.activity = activity;
    }

    public int getCount() {
        if (this.items != null) {
            return this.items.size();
        }
        return 0;
    }

    public Object getItem(int position) {
        if (this.items != null) {
            return this.items.get(position);
        }
        return null;
    }

    public long getItemId(int position) {
        return (long) position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        return getItemView(position, convertView, parent);
    }
}
