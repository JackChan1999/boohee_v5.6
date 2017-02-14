package com.boohee.one.ui.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

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
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.umeng.analytics.MobclickAgent;
import com.viewpagerindicator.LinePageIndicator;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

public class ShopLabelFragment extends BaseFragment {
    private View             headerView;
    private ShopTopicAdapter mAdapter;
    private ArrayList<Showcase> mBannerList   = new ArrayList();
    private int                 mCurrentIndex = 0;
    private List<Goods>         mGoodsList    = new ArrayList();
    private Handler             mHandler      = new Handler();
    private LinePageIndicator mIndicator;
    private int mLabelId = 0;
    private int mPage    = 1;
    private ViewPager              mPager;
    private ShopBannerPagerAdapter mPagerAdapter;
    private Runnable mPlayRunnable = new Runnable() {
        public void run() {
            if (ShopLabelFragment.this.mCurrentIndex > ShopLabelFragment.this.mPagerAdapter
                    .getCount() - 1) {
                ShopLabelFragment.this.mCurrentIndex = 0;
            }
            ShopLabelFragment.this.mPager.setCurrentItem(ShopLabelFragment.this.mCurrentIndex,
                    true);
            ShopLabelFragment.this.mIndicator.setCurrentItem(ShopLabelFragment.this.mCurrentIndex);
            ShopLabelFragment.this.mCurrentIndex = ShopLabelFragment.this.mCurrentIndex + 1;
            ShopLabelFragment.this.mHandler.postDelayed(this, 5000);
        }
    };
    private PullToRefreshListView mPullListView;

    public static ShopLabelFragment newInstance(int label_id) {
        ShopLabelFragment fragment = new ShopLabelFragment();
        fragment.mLabelId = label_id;
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
        this.mPullListView = (PullToRefreshListView) getView().findViewById(R.id.listview);
        this.headerView = View.inflate(getActivity(), R.layout.qs, null);
        this.mPager = (ViewPager) this.headerView.findViewById(R.id.vp_banner);
        this.mIndicator = (LinePageIndicator) this.headerView.findViewById(R.id.indicator);
        this.mPullListView.setMode(Mode.BOTH);
        this.mPullListView.setOnRefreshListener(new OnRefreshListener2<ListView>() {
            public void onPullDownToRefresh(PullToRefreshBase<ListView> pullToRefreshBase) {
                ShopLabelFragment.this.getLabelInit(true);
            }

            public void onPullUpToRefresh(PullToRefreshBase<ListView> pullToRefreshBase) {
                ShopLabelFragment.this.getLabelInit(false);
            }
        });
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.mPagerAdapter = new ShopBannerPagerAdapter(getChildFragmentManager(), this
                .mBannerList);
        this.mPager.setAdapter(this.mPagerAdapter);
        this.mIndicator.setViewPager(this.mPager);
        ViewUtils.setViewScaleHeight(getActivity(), this.mPager, 640, 320);
        this.mAdapter = new ShopTopicAdapter(getActivity(), this.mGoodsList);
        this.mPullListView.setAdapter(this.mAdapter);
        this.mHandler.postDelayed(new Runnable() {
            public void run() {
                ShopLabelFragment.this.mPullListView.setRefreshing(true);
            }
        }, 500);
    }

    public void onDestroy() {
        super.onDestroy();
        this.mHandler.removeCallbacksAndMessages(null);
    }

    private void getLabelInit(final boolean isFirstLoad) {
        if (isFirstLoad) {
            this.mPage = 1;
        }
        ShopApi.getLablesDetail(this.mLabelId, this.mPage, getActivity(), new JsonCallback
                (getActivity()) {
            public void ok(JSONObject object) {
                ShopList shopList = (ShopList) FastJsonUtils.fromJson(object, ShopList.class);
                if (shopList != null) {
                    if (isFirstLoad) {
                        if (ShopLabelFragment.this.getActivity() != null) {
                            ShopLabelFragment.this.getActivity().setTitle(shopList.name);
                        }
                        int headCount = ((ListView) ShopLabelFragment.this.mPullListView
                                .getRefreshableView()).getHeaderViewsCount();
                        if (shopList.banner_showcases.size() > 0 && headCount < 2) {
                            ((ListView) ShopLabelFragment.this.mPullListView.getRefreshableView()
                            ).addHeaderView(ShopLabelFragment.this.headerView);
                            ShopLabelFragment.this.mBannerList.clear();
                            ShopLabelFragment.this.mBannerList.addAll(shopList.banner_showcases);
                            ShopLabelFragment.this.mPagerAdapter.notifyDataSetChanged();
                            ShopLabelFragment.this.startPlay();
                        }
                        ShopLabelFragment.this.mGoodsList.clear();
                    }
                    if (ShopLabelFragment.this.mGoodsList.size() == 0) {
                        ShopLabelFragment.this.mGoodsList.addAll(shopList.goods);
                        ShopLabelFragment.this.mAdapter = new ShopTopicAdapter(ShopLabelFragment
                                .this.getActivity(), ShopLabelFragment.this.mGoodsList);
                        ShopLabelFragment.this.mPullListView.setAdapter(ShopLabelFragment.this
                                .mAdapter);
                    } else {
                        ShopLabelFragment.this.mGoodsList.addAll(shopList.goods);
                    }
                    ShopLabelFragment.this.mAdapter.notifyDataSetChanged();
                    ShopLabelFragment.this.mPage = ShopLabelFragment.this.mPage + 1;
                }
            }

            public void onFinish() {
                super.onFinish();
                ShopLabelFragment.this.mPullListView.onRefreshComplete();
            }
        });
    }

    private void startPlay() {
        this.mHandler.removeCallbacksAndMessages(null);
        if (this.mPagerAdapter.getCount() < 2) {
            this.mIndicator.setVisibility(8);
            return;
        }
        this.mCurrentIndex = 0;
        this.mHandler.post(this.mPlayRunnable);
    }
}
