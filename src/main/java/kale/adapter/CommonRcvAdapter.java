package kale.adapter;

import android.databinding.ObservableList;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.internal.view.SupportMenu;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.ViewGroup;
import java.util.ArrayList;
import java.util.List;
import kale.adapter.util.DataBindingJudgement;
import kale.adapter.util.IAdapter;
import kale.adapter.util.ItemTypeUtil;

public abstract class CommonRcvAdapter<T> extends Adapter implements IAdapter<T> {
    private List<T> mDataList;
    private Object mType;
    private ItemTypeUtil mUtil;

    public CommonRcvAdapter(@Nullable List<T> data) {
        if (data == null) {
            data = new ArrayList();
        }
        if (DataBindingJudgement.SUPPORT_DATABINDING && (data instanceof ObservableList)) {
            ((ObservableList) data).addOnListChangedCallback(new 1(this));
        }
        this.mDataList = data;
        this.mUtil = new ItemTypeUtil();
    }

    public int getItemCount() {
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
        return this.mUtil.getIntType(this.mType);
    }

    public Object getItemType(T t) {
        return Integer.valueOf(-1);
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new RcvAdapterItem(parent.getContext(), parent, createItem(this.mType));
    }

    public void onBindViewHolder(ViewHolder holder, int position) {
        debug((RcvAdapterItem) holder);
        ((RcvAdapterItem) holder).item.handleData(getConvertedData(this.mDataList.get(position), this.mType), position);
    }

    @NonNull
    public Object getConvertedData(T data, Object type) {
        return data;
    }

    private void debug(RcvAdapterItem holder) {
        if (false) {
            holder.itemView.setBackgroundColor(holder.isNew ? SupportMenu.CATEGORY_MASK : -16711936);
            holder.isNew = false;
        }
    }
}
