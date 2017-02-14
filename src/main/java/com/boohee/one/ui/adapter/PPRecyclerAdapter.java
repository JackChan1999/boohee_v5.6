package com.boohee.one.ui.adapter;

import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.ViewGroup;

import java.util.List;

public abstract class PPRecyclerAdapter<PPModel> extends Adapter<ViewHolder> {
    protected final List<PPModel> mDatas;

    public enum ItemType {
        TYPE_HEADER(-1),
        TYPE_FOOTER(-2),
        TYPE_ITEM(-3);

        private int type;

        private ItemType(int position) {
            this.type = -3;
            this.type = position;
        }

        public int getType() {
            return this.type;
        }
    }

    public interface PPModel {
        ItemType type();
    }

    public abstract ViewHolder getItemViewHolder(ViewGroup viewGroup);

    public abstract void onBindItemView(ViewHolder viewHolder, int i);

    public PPRecyclerAdapter(List<PPModel> data) {
        this.mDatas = data;
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ItemType.TYPE_HEADER.getType()) {
            return getHeaderViewHolder(parent);
        }
        if (viewType == ItemType.TYPE_FOOTER.getType()) {
            return getFooterViewHolder(parent);
        }
        return getItemViewHolder(parent);
    }

    public void onBindViewHolder(ViewHolder holder, int position) {
        PPModel model = (PPModel) this.mDatas.get(position);
        if (holder != null && model != null) {
            if (model.type() == ItemType.TYPE_HEADER) {
                onBindHeaderView(holder, position);
            } else if (model.type() == ItemType.TYPE_FOOTER) {
                onBindFooterView(holder, position);
            } else {
                onBindItemView(holder, position);
            }
        }
    }

    public int getItemCount() {
        return this.mDatas == null ? 0 : this.mDatas.size();
    }

    public int getItemViewType(int position) {
        PPModel model = (PPModel) this.mDatas.get(position);
        if (model == null) {
            return ItemType.TYPE_ITEM.getType();
        }
        if (model.type() == ItemType.TYPE_HEADER) {
            return ItemType.TYPE_HEADER.getType();
        }
        if (model.type() == ItemType.TYPE_FOOTER) {
            return ItemType.TYPE_FOOTER.getType();
        }
        return ItemType.TYPE_ITEM.getType();
    }

    public ViewHolder getHeaderViewHolder(ViewGroup parent) {
        return null;
    }

    public void onBindHeaderView(ViewHolder holder, int position) {
    }

    public ViewHolder getFooterViewHolder(ViewGroup parent) {
        return null;
    }

    public void onBindFooterView(ViewHolder holder, int position) {
    }
}
