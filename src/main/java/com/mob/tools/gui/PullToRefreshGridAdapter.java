package com.mob.tools.gui;

import android.content.Context;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.GridView;

public abstract class PullToRefreshGridAdapter extends PullToRefreshBaseListAdapter {
    private ListInnerAdapter adapter;
    private boolean          fling;
    private ScrollableGridView gridView = onNewGridView(getContext());
    private OnListStopScrollListener osListener;

    public PullToRefreshGridAdapter(PullToRefreshView pullToRefreshView) {
        super(pullToRefreshView);
        this.gridView.setOnScrollListener(new OnScrollListener() {
            private int firstVisibleItem;
            private int visibleItemCount;

            public void onScroll(AbsListView absListView, int i, int i2, int i3) {
                this.firstVisibleItem = i;
                this.visibleItemCount = i2;
                PullToRefreshGridAdapter.this.onScroll(PullToRefreshGridAdapter.this.gridView, i,
                        i2, i3);
            }

            public void onScrollStateChanged(AbsListView absListView, int i) {
                PullToRefreshGridAdapter.this.fling = i == 2;
                if (i != 0) {
                    return;
                }
                if (PullToRefreshGridAdapter.this.osListener != null) {
                    PullToRefreshGridAdapter.this.osListener.onListStopScrolling(this
                            .firstVisibleItem, this.visibleItemCount);
                } else if (PullToRefreshGridAdapter.this.adapter != null) {
                    PullToRefreshGridAdapter.this.adapter.notifyDataSetChanged();
                }
            }
        });
        this.adapter = new ListInnerAdapter(this);
        this.gridView.setAdapter(this.adapter);
    }

    public Scrollable getBodyView() {
        return this.gridView;
    }

    public GridView getGridView() {
        return this.gridView;
    }

    public boolean isFling() {
        return this.fling;
    }

    public boolean isPullReady() {
        return this.gridView.isReadyToPull();
    }

    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
        this.adapter.notifyDataSetChanged();
    }

    protected ScrollableGridView onNewGridView(Context context) {
        return new ScrollableGridView(context);
    }

    public void onScroll(Scrollable scrollable, int i, int i2, int i3) {
    }

    public void setColumnWidth(int i) {
        this.gridView.setColumnWidth(i);
    }

    public void setHorizontalSpacing(int i) {
        this.gridView.setHorizontalSpacing(i);
    }

    public void setNumColumns(int i) {
        this.gridView.setNumColumns(i);
    }

    public void setStretchMode(int i) {
        this.gridView.setStretchMode(i);
    }

    public void setVerticalSpacing(int i) {
        this.gridView.setVerticalSpacing(i);
    }
}
