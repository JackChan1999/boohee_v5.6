package kale.adapter;

import android.databinding.ObservableList;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import java.util.ArrayList;
import java.util.List;
import kale.adapter.item.AdapterItem;
import kale.adapter.util.DataBindingJudgement;
import kale.adapter.util.IAdapter;
import kale.adapter.util.ItemTypeUtil;

public abstract class CommonAdapter<T> extends BaseAdapter implements IAdapter<T> {
    private List<T> mDataList;
    private LayoutInflater mInflater;
    private Object mType;
    private int mViewTypeCount = 1;
    private ItemTypeUtil util;

    public CommonAdapter(@Nullable List<T> data, int viewTypeCount) {
        if (data == null) {
            data = new ArrayList();
        }
        if (DataBindingJudgement.SUPPORT_DATABINDING && (data instanceof ObservableList)) {
            ((ObservableList) data).addOnListChangedCallback(new 1(this));
        }
        this.mDataList = data;
        this.mViewTypeCount = viewTypeCount;
        this.util = new ItemTypeUtil();
    }

    public int getCount() {
        return this.mDataList.size();
    }

    public void setData(@NonNull List<T> data) {
        this.mDataList = data;
    }

    public List<T> getData() {
        return this.mDataList;
    }

    public long getItemId(int position) {
        return (long) position;
    }

    @Deprecated
    public int getItemViewType(int position) {
        this.mType = getItemType(this.mDataList.get(position));
        return this.util.getIntType(this.mType);
    }

    public Object getItemType(T t) {
        return Integer.valueOf(-1);
    }

    public int getViewTypeCount() {
        return this.mViewTypeCount;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        AdapterItem item;
        if (this.mInflater == null) {
            this.mInflater = LayoutInflater.from(parent.getContext());
        }
        if (convertView == null) {
            item = createItem(this.mType);
            convertView = this.mInflater.inflate(item.getLayoutResId(), parent, false);
            convertView.setTag(R.id.tag_item, item);
            item.bindViews(convertView);
            item.setViews();
        } else {
            item = (AdapterItem) convertView.getTag(R.id.tag_item);
        }
        item.handleData(getConvertedData(this.mDataList.get(position), this.mType), position);
        return convertView;
    }

    @NonNull
    public Object getConvertedData(T data, Object type) {
        return data;
    }

    public Object getItem(int position) {
        return this.mDataList.get(position);
    }
}
