package com.boohee.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;

public class BooheeListView extends ListView implements OnScrollListener {
    private boolean            isLastStatus;
    private OnLoadMoreListener mListener;

    public interface OnLoadMoreListener {
        void onLoadMore();
    }

    public BooheeListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOnScrollListener(this);
    }

    public void setOnLoadMoreListener(OnLoadMoreListener listener) {
        this.mListener = listener;
    }

    public void onScrollStateChanged(AbsListView view, int scrollState) {
    }

    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int
            totalItemCount) {
        if (firstVisibleItem + visibleItemCount != totalItemCount || totalItemCount <= 0) {
            this.isLastStatus = false;
            return;
        }
        loadMore();
        this.isLastStatus = true;
    }

    private void loadMore() {
        if (this.mListener != null && !this.isLastStatus) {
            this.mListener.onLoadMore();
        }
    }
}
