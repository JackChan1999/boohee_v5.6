package com.mob.tools.gui;

import android.content.Context;
import android.view.View;

public abstract class PullToRefreshAdatper {
    private Context           context;
    private PullToRefreshView parent;

    public PullToRefreshAdatper(PullToRefreshView pullToRefreshView) {
        this.context = pullToRefreshView.getContext();
        this.parent = pullToRefreshView;
    }

    public abstract Scrollable getBodyView();

    public Context getContext() {
        return this.context;
    }

    public abstract View getHeaderView();

    protected PullToRefreshView getParent() {
        return this.parent;
    }

    public abstract boolean isPullReady();

    public void notifyDataSetChanged() {
        this.parent.stopPulling();
    }

    public abstract void onPullDown(int i);

    public abstract void onRequest();

    public void onReversed() {
    }
}
