package com.boohee.one.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;

import com.boohee.model.status.Post;
import com.boohee.one.R;
import com.boohee.one.http.JsonCallback;
import com.boohee.one.http.client.BooheeClient;
import com.boohee.one.player.PlayerManager;
import com.boohee.one.ui.adapter.HomeTimelineAdapter;
import com.boohee.status.CommentListActivity;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnLastItemVisibleListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

public class StatusFavoriteFragment extends BaseFragment {
    private final String STATUS_API = "/api/v1/favorite_posts?page=%d&per_page=%d";
    private HomeTimelineAdapter adapter;
    private boolean isFirstLoad = false;
    private ListView listView;
    private int             page     = 1;
    private int             perPage  = 20;
    private ArrayList<Post> postList = new ArrayList();
    @InjectView(2131427552)
    PullToRefreshListView pullRefreshLayout;
    @InjectView(2131428357)
    TextView              tvHint;

    @Nullable
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        View view = inflater.inflate(R.layout.gq, container, false);
        ButterKnife.inject((Object) this, view);
        return view;
    }

    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
    }

    private void init() {
        showLoading();
        initView();
        pullToRefresh();
    }

    private void initView() {
        this.listView = (ListView) this.pullRefreshLayout.getRefreshableView();
        this.pullRefreshLayout.setOnRefreshListener(new OnRefreshListener<ListView>() {
            public void onRefresh(PullToRefreshBase<ListView> pullToRefreshBase) {
                StatusFavoriteFragment.this.page = 1;
                StatusFavoriteFragment.this.loadData(true);
            }
        });
        this.pullRefreshLayout.setOnLastItemVisibleListener(new OnLastItemVisibleListener() {
            public void onLastItemVisible() {
                StatusFavoriteFragment.this.loadMoreData(false);
            }
        });
        this.listView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Intent intent = new Intent(StatusFavoriteFragment.this.getActivity(),
                        CommentListActivity.class);
                intent.putExtra(CommentListActivity.POST_ID, ((Post) StatusFavoriteFragment.this
                        .postList.get(position - 1)).id);
                StatusFavoriteFragment.this.startActivity(intent);
            }
        });
        initAdapter();
    }

    private void initAdapter() {
        this.adapter = new HomeTimelineAdapter(getActivity(), this.postList);
        this.adapter.setIsFavorite(true);
        this.pullRefreshLayout.setAdapter(this.adapter);
    }

    private void loadMoreData(boolean isRefresh) {
        this.page++;
        loadData(isRefresh);
    }

    private void loadData(final boolean isRefresh) {
        BooheeClient.build("status").get(String.format
                ("/api/v1/favorite_posts?page=%d&per_page=%d", new Object[]{Integer.valueOf(this
                        .page), Integer.valueOf(this.perPage)}), new JsonCallback(getActivity()) {
            public void ok(JSONObject object) {
                super.ok(object);
                if (!StatusFavoriteFragment.this.isDetached() && StatusFavoriteFragment.this
                        .getActivity() != null) {
                    StatusFavoriteFragment.this.handleData(object, isRefresh);
                }
            }

            public void onFinish() {
                super.onFinish();
                StatusFavoriteFragment.this.pullRefreshLayout.onRefreshComplete();
                StatusFavoriteFragment.this.dismissLoading();
            }
        }, getActivity());
    }

    private void handleData(JSONObject object, boolean isRefresh) {
        try {
            List<Post> tmpList = Post.removeDisablePost(Post.parsePosts(object.optString("posts")));
            if ((tmpList == null || tmpList.size() == 0) && 1 == this.page) {
                this.tvHint.setVisibility(0);
                this.pullRefreshLayout.setVisibility(8);
                return;
            }
            if (isRefresh) {
                this.postList.clear();
            }
            this.postList.addAll(tmpList);
            this.adapter.notifyDataSetChanged();
        } catch (Exception e) {
        }
    }

    public boolean isFirstLoad() {
        return this.isFirstLoad;
    }

    public void setIsFirstLoad(boolean isFirstLoad) {
        this.isFirstLoad = isFirstLoad;
    }

    public void pullToRefresh() {
        new Handler().postDelayed(new Runnable() {
            public void run() {
                if (StatusFavoriteFragment.this.pullRefreshLayout != null) {
                    StatusFavoriteFragment.this.pullRefreshLayout.setRefreshing();
                }
            }
        }, 500);
    }

    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    public void onPause() {
        super.onPause();
        if (!PlayerManager.getInstance().isStartFullScreen()) {
            PlayerManager.getInstance().releaseAll();
        }
    }
}
