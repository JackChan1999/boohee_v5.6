package com.boohee.one.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.boohee.one.R;

public class HorizontalIconListAdapter extends BaseAdapter {
    private Context mContext;
    private DataSet mData;

    public static class DataSet {
        private int[]    resIdArray;
        private String[] textArray;

        public DataSet(int[] resIds) {
            this.resIdArray = resIds;
        }

        public DataSet(int[] resIds, String[] textIds) {
            this.resIdArray = resIds;
            this.textArray = textIds;
        }

        public int[] getResIdArray() {
            return this.resIdArray;
        }

        public DataSet setResIdArray(int[] resIdArray) {
            this.resIdArray = resIdArray;
            return this;
        }

        public String[] getTextArray() {
            return this.textArray;
        }

        public DataSet setTextArray(String[] textArray) {
            this.textArray = textArray;
            return this;
        }
    }

    static class ViewHolder {
        ImageView imageView;
        TextView  textView;

        ViewHolder() {
        }
    }

    public HorizontalIconListAdapter(Context context, DataSet data) {
        this.mContext = context;
        this.mData = data;
    }

    public void updateData(DataSet data) {
        this.mData = data;
        notifyDataSetChanged();
    }

    public int getCount() {
        return (this.mData.getTextArray() == null || this.mData.getTextArray().length <= this
                .mData.getResIdArray().length) ? this.mData.getResIdArray().length : this.mData
                .getTextArray().length;
    }

    public Object getItem(int arg0) {
        return Integer.valueOf(arg0);
    }

    public long getItemId(int arg0) {
        return (long) arg0;
    }

    public int getItemResId(int position) {
        return this.mData.getResIdArray()[position];
    }

    public String getItemText(int position) {
        if (this.mData.getTextArray() == null || this.mData.getTextArray().length <= 0) {
            return "";
        }
        return this.mData.getTextArray()[position];
    }

    public View getView(int position, View containerView, ViewGroup parentView) {
        ViewHolder holder;
        int layoutId = R.layout.k1;
        if (this.mData.getTextArray() == null || this.mData.getTextArray().length == 0) {
            layoutId = R.layout.k2;
        }
        if (containerView == null) {
            containerView = View.inflate(this.mContext, layoutId, null);
            holder = new ViewHolder();
            holder.imageView = (ImageView) containerView.findViewById(R.id.charlet_icon);
            holder.textView = (TextView) containerView.findViewById(R.id.chartlet_text);
            containerView.setTag(holder);
        } else {
            holder = (ViewHolder) containerView.getTag();
        }
        holder.imageView.setBackgroundResource(this.mData.getResIdArray()[position]);
        if (!(holder.textView == null || this.mData.getTextArray() == null || this.mData
                .getTextArray().length <= 0)) {
            holder.textView.setVisibility(0);
            holder.textView.setText(this.mData.getTextArray()[position]);
        }
        return containerView;
    }
}
