package com.mob.tools.gui;

import android.view.View;
import android.view.ViewGroup;

public abstract class PullToRefreshBaseListAdapter extends PullToRefreshAdatper {
    public PullToRefreshBaseListAdapter(PullToRefreshView pullToRefreshView) {
        super(pullToRefreshView);
    }

    public abstract int getCount();

    public abstract Object getItem(int i);

    public abstract long getItemId(int i);

    public int getItemViewType(int i) {
        return 0;
    }

    public abstract View getView(int i, View view, ViewGroup viewGroup);

    public int getViewTypeCount() {
        return 1;
    }

    public abstract boolean isFling();

    public abstract void onScroll(Scrollable scrollable, int i, int i2, int i3);
}
