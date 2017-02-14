package com.mob.tools.gui;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;

public abstract class PullToRefreshListAdapter extends PullToRefreshBaseListAdapter {
    private ListInnerAdapter adapter;
    private boolean          fling;
    private ScrollableListView listView = onNewListView(getContext());
    private OnListStopScrollListener osListener;

    public PullToRefreshListAdapter(PullToRefreshView pullToRefreshView) {
        super(pullToRefreshView);
        this.listView.setOnScrollListener(new OnScrollListener() {
            private int firstVisibleItem;
            private int visibleItemCount;

            public void onScroll(AbsListView absListView, int i, int i2, int i3) {
                this.firstVisibleItem = i;
                this.visibleItemCount = i2;
                PullToRefreshListAdapter.this.onScroll(PullToRefreshListAdapter.this.listView, i,
                        i2, i3);
            }

            public void onScrollStateChanged(AbsListView absListView, int i) {
                PullToRefreshListAdapter.this.fling = i == 2;
                if (i != 0) {
                    return;
                }
                if (PullToRefreshListAdapter.this.osListener != null) {
                    PullToRefreshListAdapter.this.osListener.onListStopScrolling(this
                            .firstVisibleItem, this.visibleItemCount);
                } else if (PullToRefreshListAdapter.this.adapter != null) {
                    PullToRefreshListAdapter.this.adapter.notifyDataSetChanged();
                }
            }
        });
        this.adapter = new ListInnerAdapter(this);
        this.listView.setAdapter(this.adapter);
    }

    public Scrollable getBodyView() {
        return this.listView;
    }

    public ListView getListView() {
        return this.listView;
    }

    public boolean isFling() {
        return this.fling;
    }

    public boolean isPullReady() {
        return this.listView.isReadyToPull();
    }

    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
        this.adapter.notifyDataSetChanged();
    }

    protected ScrollableListView onNewListView(Context context) {
        return new ScrollableListView(context);
    }

    public void onScroll(Scrollable scrollable, int i, int i2, int i3) {
    }

    public void setDivider(Drawable drawable) {
        this.listView.setDivider(drawable);
    }

    public void setDividerHeight(int i) {
        this.listView.setDividerHeight(i);
    }
}
