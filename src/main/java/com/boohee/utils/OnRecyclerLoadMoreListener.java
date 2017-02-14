package com.boohee.utils;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.OnScrollListener;

public abstract class OnRecyclerLoadMoreListener extends OnScrollListener {
    private int mLastLoad;

    public abstract void onLoadMore();

    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {
            LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView
                    .getLayoutManager();
            int itemCount = layoutManager.getItemCount();
            int lastVisible = layoutManager.findLastCompletelyVisibleItemPosition();
            if (this.mLastLoad != itemCount && itemCount <= lastVisible + 1 && dy > 0) {
                this.mLastLoad = itemCount;
                onLoadMore();
            }
        }
    }
}
