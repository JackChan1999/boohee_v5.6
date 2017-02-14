package com.boohee.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.OnScrollListener;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;

import com.boohee.one.R;

public class PPRecyclerView extends FrameLayout {
    private static Handler mHandler = new Handler(Looper.getMainLooper());
    private boolean             isLoadMore;
    private boolean             isRefreshing;
    private int                 lastNum;
    private LinearLayoutManager mLayoutManager;
    private LoadingListener     mListener;
    private RecyclerView        mRecyclerView;
    private SwipeRefreshLayout  mSwipeLayout;

    public interface LoadingListener {
        void loadMore();

        void refresh();
    }

    public PPRecyclerView(Context context) {
        this(context, null);
    }

    public PPRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        parseAttributes(attrs);
        init();
    }

    private void parseAttributes(AttributeSet attrs) {
        if (attrs != null) {
            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.PPRecyclerView);
            this.lastNum = a.getInteger(0, this.lastNum);
            a.recycle();
        }
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.qh, this, true);
        this.mRecyclerView = (RecyclerView) findViewById(R.id.rv_list);
        this.mSwipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_layout);
        this.mLayoutManager = new LinearLayoutManager(getContext());
        initPullToRefresh();
        initLoadMore();
        this.mRecyclerView.setLayoutManager(this.mLayoutManager);
        this.mRecyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    private void initPullToRefresh() {
        this.mSwipeLayout.setOnRefreshListener(new OnRefreshListener() {
            public void onRefresh() {
                if (PPRecyclerView.this.mListener == null) {
                    PPRecyclerView.this.mSwipeLayout.setRefreshing(false);
                } else if (!PPRecyclerView.this.isRefreshing) {
                    PPRecyclerView.this.isRefreshing = true;
                    PPRecyclerView.this.mListener.refresh();
                }
            }
        });
    }

    private void initLoadMore() {
        this.mRecyclerView.setOnScrollListener(new OnScrollListener() {
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int lastVisibleItem = PPRecyclerView.this.mLayoutManager
                        .findLastVisibleItemPosition();
                int totalItemCount = PPRecyclerView.this.mLayoutManager.getItemCount();
                if (PPRecyclerView.this.mListener != null && !PPRecyclerView.this.isLoadMore &&
                        lastVisibleItem >= totalItemCount - PPRecyclerView.this.lastNum) {
                    PPRecyclerView.this.isLoadMore = true;
                    PPRecyclerView.this.mListener.loadMore();
                }
            }
        });
    }

    public void setLoadingListener(LoadingListener listener) {
        this.mListener = listener;
    }

    public void setAdapter(Adapter<ViewHolder> adapter) {
        this.mRecyclerView.setAdapter(adapter);
    }

    public void setLoadMoreComplete() {
        this.isLoadMore = false;
    }

    public void setRefreshComplete() {
        this.mSwipeLayout.setRefreshing(false);
        this.isRefreshing = false;
    }

    public void setLastNum(int lastLoadNum) {
        this.lastNum = lastLoadNum;
    }

    public void scrollTopOrRefresh() {
        if (this.mLayoutManager.findFirstCompletelyVisibleItemPosition() == 0) {
            this.mSwipeLayout.setRefreshing(true);
        } else {
            this.mRecyclerView.scrollToPosition(0);
        }
    }

    public void initLoad() {
        if (this.mListener != null && this.mSwipeLayout != null) {
            mHandler.postDelayed(new Runnable() {
                public void run() {
                    PPRecyclerView.this.mSwipeLayout.setRefreshing(true);
                }
            }, 500);
        }
    }

    public RecyclerView getRecyclerView() {
        return this.mRecyclerView;
    }
}
