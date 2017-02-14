package com.boohee.one.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.PopupMenu.OnMenuItemClickListener;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;

import com.boohee.api.FavoriteApi;
import com.boohee.model.FavoriteArticle;
import com.boohee.one.R;
import com.boohee.one.http.JsonCallback;
import com.boohee.one.ui.BrowserActivity;
import com.boohee.one.ui.StoryDetailActivity;
import com.boohee.one.ui.adapter.SimpleBaseAdapter;
import com.boohee.one.ui.adapter.SimpleBaseAdapter.ViewHolder;
import com.boohee.utils.FastJsonUtils;
import com.boohee.utils.TextUtil;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnLastItemVisibleListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

public class WebFavoriteFragment extends BaseFragment {
    private ArticleAdapter adapter;
    private List<FavoriteArticle> articleList = new ArrayList();
    private boolean               isFirstLoad = true;
    private ListView listView;
    private int page    = 1;
    private int perPage = 20;
    @InjectView(2131427552)
    PullToRefreshListView pullRefreshLayout;
    @InjectView(2131428357)
    TextView              tvHint;

    public class ArticleAdapter extends SimpleBaseAdapter<FavoriteArticle> {
        public ArticleAdapter(Context context, List<FavoriteArticle> data) {
            super(context, data);
        }

        public int getItemResource() {
            return R.layout.i0;
        }

        public View getItemView(final int position, View convertView, ViewHolder holder) {
            ImageView menu = (ImageView) holder.getView(R.id.btn_menu);
            setTitle((TextView) holder.getView(R.id.favorite_article_title), position);
            menu.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    ArticleAdapter.this.showPopupMenu(v, position);
                }
            });
            return convertView;
        }

        private void setTitle(TextView title, int position) {
            title.setText(((FavoriteArticle) getItem(position)).title);
        }

        protected void showPopupMenu(View view, final int position) {
            final FavoriteArticle article = (FavoriteArticle) getItem(position);
            PopupMenu popup = new PopupMenu(this.context, view);
            popup.getMenu().add(0, 0, 0, "取消收藏");
            popup.show();
            popup.setOnMenuItemClickListener(new OnMenuItemClickListener() {
                public boolean onMenuItemClick(MenuItem item) {
                    switch (item.getItemId()) {
                        case 0:
                            WebFavoriteFragment.this.deleteFavoriteArticle(article, position);
                            break;
                    }
                    return false;
                }
            });
        }
    }

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
        initView();
    }

    private void initView() {
        this.listView = (ListView) this.pullRefreshLayout.getRefreshableView();
        this.pullRefreshLayout.setOnRefreshListener(new OnRefreshListener<ListView>() {
            public void onRefresh(PullToRefreshBase<ListView> pullToRefreshBase) {
                WebFavoriteFragment.this.page = 1;
                WebFavoriteFragment.this.loadData(true);
            }
        });
        this.pullRefreshLayout.setOnLastItemVisibleListener(new OnLastItemVisibleListener() {
            public void onLastItemVisible() {
                WebFavoriteFragment.this.loadMoreData();
            }
        });
        this.listView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                FavoriteArticle article = (FavoriteArticle) parent.getAdapter().getItem(position);
                if (article.url != null && article.url.contains("http://status.boohee.com/") &&
                        article.url.contains("story") && article.url.contains("id=")) {
                    StoryDetailActivity.comeOn(WebFavoriteFragment.this.getActivity(), article
                            .url, TextUtil.checkId(article.url), article.title);
                } else {
                    BrowserActivity.comeOnBaby(WebFavoriteFragment.this.getActivity(), article
                            .title, article.url);
                }
            }
        });
        initAdapter();
    }

    private void initAdapter() {
        this.adapter = new ArticleAdapter(getActivity(), this.articleList);
        this.pullRefreshLayout.setAdapter(this.adapter);
    }

    private void loadMoreData() {
        this.page++;
        loadData(false);
    }

    private void loadData(final boolean isRefresh) {
        FavoriteApi.getFavoriteArticle(this.page, new JsonCallback(getActivity()) {
            public void ok(JSONObject object) {
                super.ok(object);
                if (!WebFavoriteFragment.this.isDetached() && WebFavoriteFragment.this
                        .getActivity() != null) {
                    WebFavoriteFragment.this.handlerData(object, isRefresh);
                }
            }

            public void onFinish() {
                super.onFinish();
                WebFavoriteFragment.this.pullRefreshLayout.onRefreshComplete();
            }
        }, getActivity());
    }

    private void handlerData(JSONObject object, boolean isRefresh) {
        List tmpList = FastJsonUtils.parseList(object.optString("favorite_articles"),
                FavoriteArticle.class);
        if ((tmpList == null || tmpList.size() == 0) && 1 == this.page) {
            this.tvHint.setVisibility(0);
            this.pullRefreshLayout.setVisibility(8);
            return;
        }
        if (isRefresh) {
            this.articleList.clear();
        }
        this.articleList.addAll(tmpList);
        this.adapter.notifyDataSetChanged();
    }

    public boolean isFirstLoad() {
        return this.isFirstLoad;
    }

    public void setIsFirstLoad(boolean isFirstLoad) {
        this.isFirstLoad = isFirstLoad;
    }

    public void pullToRefresh() {
        if (this.pullRefreshLayout != null) {
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    WebFavoriteFragment.this.pullRefreshLayout.setRefreshing();
                }
            }, 500);
        }
    }

    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    private void deleteFavoriteArticle(FavoriteArticle article, final int position) {
        showLoading();
        FavoriteApi.deleteFavoriteArticle(article.id, new JsonCallback(getActivity()) {
            public void ok(JSONObject object) {
                super.ok(object);
                if (!WebFavoriteFragment.this.isDetached() && WebFavoriteFragment.this
                        .getActivity() != null) {
                    if (WebFavoriteFragment.this.articleList != null && WebFavoriteFragment.this
                            .articleList.size() > 0) {
                        WebFavoriteFragment.this.articleList.remove(position);
                        WebFavoriteFragment.this.adapter.notifyDataSetChanged();
                    }
                    if (WebFavoriteFragment.this.articleList.size() == 0) {
                        WebFavoriteFragment.this.tvHint.setVisibility(0);
                        WebFavoriteFragment.this.pullRefreshLayout.setVisibility(8);
                    }
                }
            }

            public void onFinish() {
                super.onFinish();
                WebFavoriteFragment.this.dismissLoading();
            }
        }, getActivity());
    }
}
