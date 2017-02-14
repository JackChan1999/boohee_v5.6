package com.boohee.one.ui.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import butterknife.ButterKnife;
import butterknife.InjectView;

import com.boohee.api.ShopApi;
import com.boohee.model.Goods;
import com.boohee.model.ShopList;
import com.boohee.model.Showcase;
import com.boohee.one.R;
import com.boohee.one.http.JsonCallback;
import com.boohee.one.ui.adapter.ShopBannerPagerAdapter;
import com.boohee.uchoice.ShopTopicAdapter;
import com.boohee.utility.Event;
import com.boohee.utils.FastJsonUtils;
import com.boohee.utils.ViewUtils;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnLastItemVisibleListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.umeng.analytics.MobclickAgent;
import com.viewpagerindicator.LinePageIndicator;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

public class ShopCategoryFragment extends BaseFragment {
    private boolean hasMore     = true;
    public  boolean isFirstLoad = true;
    private ShopTopicAdapter mAdapter;
    private List<Showcase> mBannerList   = new ArrayList();
    private int            mChildLabelId = -1;
    private int            mCurrentIndex = 0;
    private int            mCurrentPage  = 1;
    private List<Goods>    mGoodsList    = new ArrayList();
    private Handler        mHandler      = new Handler();
    private LinePageIndicator mIndicator;
    private int mLabelId = -1;
    private int mPage    = 1;
    private ViewPager              mPager;
    private ShopBannerPagerAdapter mPagerAdapter;
    private Runnable mPlayRunnable = new Runnable() {
        public void run() {
            if (ShopCategoryFragment.this.mCurrentIndex > ShopCategoryFragment.this.mPagerAdapter
                    .getCount() - 1) {
                ShopCategoryFragment.this.mCurrentIndex = 0;
            }
            ShopCategoryFragment.this.mPager.setCurrentItem(ShopCategoryFragment.this
                    .mCurrentIndex, true);
            ShopCategoryFragment.this.mIndicator.setCurrentItem(ShopCategoryFragment.this
                    .mCurrentIndex);
            ShopCategoryFragment.this.mCurrentIndex = ShopCategoryFragment.this.mCurrentIndex + 1;
            ShopCategoryFragment.this.mHandler.postDelayed(this, 5000);
        }
    };
    @InjectView(2131427552)
    PullToRefreshListView mPullListView;
    private ShopList mShopList;

    public static ShopCategoryFragment newInstance(int label_id, int child_label_id) {
        ShopCategoryFragment fragment = new ShopCategoryFragment();
        fragment.mLabelId = label_id;
        fragment.mChildLabelId = child_label_id;
        return fragment;
    }

    public static ShopCategoryFragment newInstance(int label_id, int child_label_id, ShopList
            shopList) {
        ShopCategoryFragment fragment = new ShopCategoryFragment();
        fragment.mLabelId = label_id;
        fragment.mChildLabelId = child_label_id;
        fragment.mShopList = shopList;
        return fragment;
    }

    public void onAttach(Activity activity) {
        super.onAttach(activity);
        MobclickAgent.onEvent(getActivity(), Event.SHOP_SPECIAL_LIST_PAGE);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        return inflater.inflate(R.layout.gk, container, false);
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.inject((Object) this, view);
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (this.mShopList != null) {
            loadAll();
        }
        this.mPullListView.setOnRefreshListener(new OnRefreshListener<ListView>() {
            public void onRefresh(PullToRefreshBase<ListView> pullToRefreshBase) {
                ShopCategoryFragment.this.mPage = 1;
                ShopCategoryFragment.this.mCurrentPage = ShopCategoryFragment.this.mPage;
                ShopCategoryFragment.this.hasMore = true;
                ShopCategoryFragment.this.getLabelInit(true);
            }
        });
        this.mPullListView.setOnLastItemVisibleListener(new OnLastItemVisibleListener() {
            public void onLastItemVisible() {
                if (ShopCategoryFragment.this.mPage > ShopCategoryFragment.this.mCurrentPage &&
                        ShopCategoryFragment.this.hasMore) {
                    ShopCategoryFragment.this.mCurrentPage = ShopCategoryFragment.this.mPage;
                    ShopCategoryFragment.this.getLabelInit(false);
                }
            }
        });
    }

    private View getHeaderView() {
        View headerView = View.inflate(getActivity(), R.layout.qs, null);
        this.mPager = (ViewPager) headerView.findViewById(R.id.vp_banner);
        this.mIndicator = (LinePageIndicator) headerView.findViewById(R.id.indicator);
        this.mPagerAdapter = new ShopBannerPagerAdapter(getChildFragmentManager(), this
                .mBannerList);
        this.mPager.setAdapter(this.mPagerAdapter);
        this.mIndicator.setViewPager(this.mPager);
        ViewUtils.setViewScaleHeight(getActivity(), this.mPager, 640, 320);
        return headerView;
    }

    public void loadAll() {
        if (this.mShopList != null) {
            this.isFirstLoad = false;
            if (this.mShopList.banner_showcases != null && this.mShopList.banner_showcases.size()
                    > 0) {
                ((ListView) this.mPullListView.getRefreshableView()).addHeaderView(getHeaderView());
                this.mBannerList.clear();
                this.mBannerList.addAll(this.mShopList.banner_showcases);
                this.mPagerAdapter.notifyDataSetChanged();
                startPlay();
            }
            if (this.mShopList.goods != null && this.mShopList.goods.size() > 0) {
                this.mGoodsList.clear();
                this.mGoodsList.addAll(this.mShopList.goods);
                this.mAdapter = new ShopTopicAdapter(getActivity(), this.mGoodsList);
                this.mPullListView.setAdapter(this.mAdapter);
                this.mPage++;
                return;
            }
            return;
        }
        loadFirst();
    }

    public void loadFirst() {
        super.loadFirst();
        this.mHandler.postDelayed(new Runnable() {
            public void run() {
                ShopCategoryFragment.this.mPullListView.setRefreshing(true);
            }
        }, 500);
    }

    private void getLabelInit(final boolean isFirst) {
        ShopApi.getCategoriesSubs(this.mLabelId, this.mChildLabelId, this.mPage, getActivity(),
                new JsonCallback(getActivity()) {
            public void ok(JSONObject object) {
                ShopCategoryFragment.this.isFirstLoad = false;
                ShopList shopList = (ShopList) FastJsonUtils.fromJson(object, ShopList.class);
                if (shopList == null) {
                    ShopCategoryFragment.this.hasMore = false;
                } else if (isFirst) {
                    if (shopList.banner_showcases.size() > 0) {
                        if (((ListView) ShopCategoryFragment.this.mPullListView
                                .getRefreshableView()).getHeaderViewsCount() < 2) {
                            ((ListView) ShopCategoryFragment.this.mPullListView
                                    .getRefreshableView()).addHeaderView(ShopCategoryFragment
                                    .this.getHeaderView());
                        }
                        ShopCategoryFragment.this.mBannerList.clear();
                        ShopCategoryFragment.this.mBannerList.addAll(shopList.banner_showcases);
                        ShopCategoryFragment.this.mPagerAdapter.notifyDataSetChanged();
                        ShopCategoryFragment.this.startPlay();
                    }
                    if (shopList.goods != null && shopList.goods.size() > 0) {
                        ShopCategoryFragment.this.mGoodsList.clear();
                        ShopCategoryFragment.this.mGoodsList.addAll(shopList.goods);
                        ShopCategoryFragment.this.mAdapter = new ShopTopicAdapter
                                (ShopCategoryFragment.this.getActivity(), ShopCategoryFragment
                                        .this.mGoodsList);
                        ShopCategoryFragment.this.mPullListView.setAdapter(ShopCategoryFragment
                                .this.mAdapter);
                        ShopCategoryFragment.this.mPage = ShopCategoryFragment.this.mPage + 1;
                    }
                } else if (shopList.goods == null || shopList.goods.size() <= 0) {
                    ShopCategoryFragment.this.hasMore = false;
                } else {
                    ShopCategoryFragment.this.mGoodsList.addAll(shopList.goods);
                    ShopCategoryFragment.this.mAdapter.notifyDataSetChanged();
                    ShopCategoryFragment.this.mPage = ShopCategoryFragment.this.mPage + 1;
                }
            }

            public void onFinish() {
                super.onFinish();
                ShopCategoryFragment.this.mPullListView.onRefreshComplete();
            }
        });
    }

    private void startPlay() {
        this.mHandler.removeCallbacks(this.mPlayRunnable);
        if (this.mPagerAdapter.getCount() < 2) {
            this.mIndicator.setVisibility(8);
            return;
        }
        this.mCurrentIndex = 0;
        this.mHandler.post(this.mPlayRunnable);
    }

    public void onDestroy() {
        super.onDestroy();
        this.mHandler.removeCallbacks(this.mPlayRunnable);
    }
}
