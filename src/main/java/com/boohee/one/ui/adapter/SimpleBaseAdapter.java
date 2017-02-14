package com.boohee.one.ui.adapter;

import android.content.Context;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

public abstract class SimpleBaseAdapter<T> extends BaseAdapter {
    protected final Context context;
    protected final List<T> data;
    protected ImageLoader imageLoader = ImageLoader.getInstance();

    public class ViewHolder {
        private View convertView;
        private SparseArray<View> views = new SparseArray();

        public ViewHolder(View convertView) {
            this.convertView = convertView;
        }

        public <V extends View> V getView(int resId) {
            View v = (View) this.views.get(resId);
            if (v != null) {
                return v;
            }
            v = this.convertView.findViewById(resId);
            this.views.put(resId, v);
            return v;
        }
    }

    public abstract int getItemResource();

    public abstract View getItemView(int i, View view, ViewHolder viewHolder);

    public SimpleBaseAdapter(Context context, List<T> list) {
        List arrayList;
        this.context = context;
        if (list == null) {
            arrayList = new ArrayList();
        }
        this.data = arrayList;
    }

    public int getCount() {
        return this.data.size();
    }

    public T getItem(int position) {
        if (position >= this.data.size()) {
            return null;
        }
        return this.data.get(position);
    }

    public long getItemId(int position) {
        return (long) position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(this.context, getItemResource(), null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        return getItemView(position, convertView, holder);
    }

    public void addAll(List<T> elem) {
        this.data.addAll(elem);
        notifyDataSetChanged();
    }

    public void remove(T elem) {
        this.data.remove(elem);
        notifyDataSetChanged();
    }

    public void remove(int index) {
        this.data.remove(index);
        notifyDataSetChanged();
    }

    public void replaceAll(List<T> elem) {
        this.data.clear();
        this.data.addAll(elem);
        notifyDataSetChanged();
    }
}
