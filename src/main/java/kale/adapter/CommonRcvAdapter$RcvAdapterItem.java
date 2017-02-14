package kale.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import kale.adapter.item.AdapterItem;

class CommonRcvAdapter$RcvAdapterItem extends ViewHolder {
    boolean isNew = true;
    protected AdapterItem item;

    protected CommonRcvAdapter$RcvAdapterItem(Context context, ViewGroup parent, AdapterItem item) {
        super(LayoutInflater.from(context).inflate(item.getLayoutResId(), parent, false));
        this.item = item;
        this.item.bindViews(this.itemView);
        this.item.setViews();
    }
}
