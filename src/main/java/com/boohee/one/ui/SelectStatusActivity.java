package com.boohee.one.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.boohee.api.StatusApi;
import com.boohee.main.GestureActivity;
import com.boohee.model.FoodMealDetailBean;
import com.boohee.model.MealBean;
import com.boohee.one.R;
import com.boohee.one.cache.CacheKey;
import com.boohee.one.cache.FileCache;
import com.boohee.one.http.JsonCallback;
import com.boohee.one.ui.adapter.SelectStatusAdapter;
import com.boohee.status.CommentListActivity;
import com.boohee.utils.AccountUtils;
import com.boohee.utils.Helper;
import com.boohee.widgets.CheckAccountPopwindow;
import com.boohee.widgets.TurboLoadingFooter;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnLastItemVisibleListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.nostra13.universalimageloader.core.listener.PauseOnScrollListener;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

public class SelectStatusActivity extends GestureActivity implements OnItemClickListener {
    public static final String             KEY_MEAL_TYPE = "meal_type";
    public static final String             VAL_BREAKFAST = "breakfast";
    public static final String             VAL_LUNCH     = "lunch";
    public static final String             VAL_SUPPER    = "supper";
    private             boolean            isFromHead    = true;
    private             TurboLoadingFooter loadingFooter = null;
    private SelectStatusAdapter   mAdapter;
    private FoodMealDetailBean    mBean;
    private List<MealBean>        mDatas;
    private PullToRefreshListView mListView;
    private String                mMealType;
    private int mPage = 1;
    private FileCache mStatusCache;

    private class LoadMoreL implements OnLastItemVisibleListener {
        private LoadMoreL() {
        }

        public void onLastItemVisible() {
            SelectStatusActivity.this.isFromHead = false;
            SelectStatusActivity.this.loadData();
        }
    }

    private class RefreshL implements OnRefreshListener<ListView> {
        private RefreshL() {
        }

        public void onRefresh(PullToRefreshBase<ListView> pullToRefreshBase) {
            SelectStatusActivity.this.isFromHead = true;
            SelectStatusActivity.this.mPage = 1;
            SelectStatusActivity.this.loadData();
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cy);
        init();
        loadData();
    }

    private void init() {
        this.mStatusCache = FileCache.get(this.ctx, getClass().getName());
        this.mMealType = getIntent().getStringExtra("meal_type");
        setActionBarTitle();
        this.mBean = getCacheData();
        this.mDatas = this.mBean != null ? this.mBean.getMeals() : new ArrayList();
        this.loadingFooter = new TurboLoadingFooter(this);
        this.mAdapter = new SelectStatusAdapter(this.ctx, this.mDatas);
        this.mListView = (PullToRefreshListView) this.finder.find(R.id.listView);
        ((ListView) this.mListView.getRefreshableView()).addFooterView(this.loadingFooter.getView
                ());
        this.mListView.setOnScrollListener(new PauseOnScrollListener(this.imageLoader, true, true));
        this.mListView.setOnRefreshListener(new RefreshL());
        this.mListView.setOnLastItemVisibleListener(new LoadMoreL());
        this.mListView.setOnItemClickListener(this);
        this.mListView.setAdapter(this.mAdapter);
    }

    private void setActionBarTitle() {
        if ("breakfast".equalsIgnoreCase(this.mMealType)) {
            setTitle("早餐精选");
        } else if ("lunch".equalsIgnoreCase(this.mMealType)) {
            setTitle("午餐精选");
        } else if (VAL_SUPPER.equalsIgnoreCase(this.mMealType)) {
            setTitle("晚餐精选");
        }
    }

    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        if (AccountUtils.isVisitorAccount(this)) {
            CheckAccountPopwindow.showVisitorPopWindow(this);
        } else if (position <= this.mDatas.size() && this.mDatas.get(position - 1) != null) {
            MealBean bean = (MealBean) this.mDatas.get(position - 1);
            Intent intent = new Intent(this, CommentListActivity.class);
            intent.putExtra("user_id", bean.getUser_id());
            intent.putExtra(CommentListActivity.POST_ID, bean.getPost_id());
            startActivity(intent);
        }
    }

    private void loadData() {
        this.mMealType = TextUtils.isEmpty(this.mMealType) ? "breakfast" : this.mMealType;
        Context context = this.activity;
        String str = this.mMealType;
        int i = this.mPage;
        this.mPage = i + 1;
        StatusApi.getRecommendedMeals(context, str, i, new JsonCallback(this.activity) {
            public void ok(JSONObject object) {
                super.ok(object);
                SelectStatusActivity.this.mBean = FoodMealDetailBean.parse(object.toString());
                Helper.showLog("-->", "" + SelectStatusActivity.this.isFromHead);
                if (SelectStatusActivity.this.isFromHead) {
                    SelectStatusActivity.this.mStatusCache.put(CacheKey.KEY_SELECT_STATUS, object
                            .toString());
                    SelectStatusActivity.this.mDatas.clear();
                    SelectStatusActivity.this.mDatas.addAll(SelectStatusActivity.this.mBean
                            .getMeals());
                } else {
                    SelectStatusActivity.this.mDatas.addAll(SelectStatusActivity.this.mBean
                            .getMeals());
                }
                SelectStatusActivity.this.mAdapter.notifyDataSetChanged();
                if (SelectStatusActivity.this.mBean.getCurrent_page() == SelectStatusActivity
                        .this.mBean.getTotal_page()) {
                    Helper.showToast((CharSequence) "已加载完全部~~");
                    ((ListView) SelectStatusActivity.this.mListView.getRefreshableView())
                            .removeFooterView(SelectStatusActivity.this.loadingFooter.getView());
                }
            }

            public void onFinish() {
                super.onFinish();
                SelectStatusActivity.this.completeRefresh();
            }
        });
    }

    private FoodMealDetailBean getCacheData() {
        String responseData = this.mStatusCache.getAsString(CacheKey.KEY_SELECT_STATUS);
        return TextUtils.isEmpty(responseData) ? null : FoodMealDetailBean.parse(responseData);
    }

    private void completeRefresh() {
        if (this.mListView != null && this.mListView.isRefreshing()) {
            this.mListView.onRefreshComplete();
        }
    }
}
