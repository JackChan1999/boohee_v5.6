package com.boohee.one.ui.fragment;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.SimpleOnPageChangeListener;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import com.boohee.api.ShopApi;
import com.boohee.apn.ApnActivity;
import com.boohee.main.FeedBackSwitcher;
import com.boohee.model.BaseShowcase;
import com.boohee.model.BaseShowcase.TYPE_SHOWCASE;
import com.boohee.model.Goods;
import com.boohee.model.Label;
import com.boohee.model.OrderState;
import com.boohee.model.ShopPage;
import com.boohee.model.Showcase;
import com.boohee.myview.NewBadgeView;
import com.boohee.one.R;
import com.boohee.one.event.ConstEvent;
import com.boohee.one.http.JsonCallback;
import com.boohee.one.transform.TransformManager;
import com.boohee.one.ui.ShopCategoryActivity;
import com.boohee.one.ui.adapter.ShopBannerPagerAdapter;
import com.boohee.uchoice.CartActivity;
import com.boohee.uchoice.GoodsVerticalAdapter;
import com.boohee.uchoice.OrderListActivity;
import com.boohee.uchoice.ShopShowcaseAdapter;
import com.boohee.uchoice.ShopTopicAdapter;
import com.boohee.utility.Event;
import com.boohee.utility.ImageLoaderOptions;
import com.boohee.utils.FastJsonUtils;
import com.boohee.utils.MeiQiaHelper;
import com.boohee.utils.ResolutionUtils;
import com.boohee.utils.ShopUtils;
import com.boohee.utils.ViewUtils;
import com.boohee.widgets.NoScrollGridView;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnLastItemVisibleListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.analytics.MobclickAgent;
import com.viewpagerindicator.LinePageIndicator;

import de.greenrobot.event.EventBus;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

public class ShopMainFragment extends BaseFragment implements OnClickListener {
    @InjectView(2131427531)
    FloatingActionButton fabButton;
    private boolean hasMore     = true;
    private boolean isFirstLoad = true;
    private LinearLayout ll_header_content;
    private List<Showcase> mBannerList = new ArrayList();
    private NewBadgeView mCartBadgeView;
    public  List<Label>  mCategories;
    private int          mCurrentIndex;
    private int mCurrentPage = this.mPage;
    private ShopTopicAdapter mGoodsAdapter;
    private List    mGoodsList = new ArrayList();
    private Handler mHandler   = new Handler();
    private LinePageIndicator mIndicator;
    private NewBadgeView      mOrderBadgeView;
    private int mPage = 1;
    private PopupWindow mPopupWindow;
    @InjectView(2131427552)
    PullToRefreshListView mPullListView;
    private Runnable mRunnable = new Runnable() {
        public void run() {
            if (ShopMainFragment.this.mShopBannerAdapter.getCount() > 1) {
                if (ShopMainFragment.this.mViewPager.getCurrentItem() + 1 >= ShopMainFragment
                        .this.mShopBannerAdapter.getCount()) {
                    ShopMainFragment.this.mCurrentIndex = 0;
                }
                ShopMainFragment.this.mViewPager.setCurrentItem(ShopMainFragment.this
                        .mCurrentIndex, true);
                ShopMainFragment.this.mIndicator.setCurrentItem(ShopMainFragment.this
                        .mCurrentIndex);
                ShopMainFragment.this.mCurrentIndex = ShopMainFragment.this.mCurrentIndex + 1;
                ShopMainFragment.this.mHandler.postDelayed(ShopMainFragment.this.mRunnable, 5000);
            }
        }
    };
    private ShopBannerPagerAdapter mShopBannerAdapter;
    private ShopShowcaseAdapter    mShopShowcaseAdapter;
    private List<Showcase> mShowcaseList = new ArrayList();
    private ViewPager mViewPager;
    RelativeLayout rl_category;
    TextView       tv_badge_cart;
    TextView       tv_badge_order;
    private TextView tv_shop_type;
    private View     view_banner;

    private class goodsListListener implements OnClickListener {
        Showcase showcase;

        public goodsListListener(Showcase showcase) {
            this.showcase = showcase;
        }

        public void onClick(View v) {
            MobclickAgent.onEvent(ShopMainFragment.this.getActivity(), Event.shop_clickListBanner);
            ShopUtils.handleExhibit(ShopMainFragment.this.getActivity(), this.showcase);
        }
    }

    @OnClick({2131427531})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab_button:
                MobclickAgent.onEvent(getActivity(), Event.SHOP_CLICKCLIENTSERVICEPAGE);
                if (FeedBackSwitcher.isFeedbackTime()) {
                    MeiQiaHelper.startChat(getActivity());
                    return;
                } else {
                    startActivity(new Intent(getActivity(), ApnActivity.class));
                    return;
                }
            case R.id.rl_category:
                if (this.mCategories != null && this.mCategories.size() > 0) {
                    MobclickAgent.onEvent(getActivity(), Event.shop_clickCategory);
                    showPopwindow(this.rl_category, this.mCategories);
                    return;
                }
                return;
            case R.id.rl_cart:
                MobclickAgent.onEvent(getActivity(), "shop_clickCart");
                startActivity(new Intent(getActivity(), CartActivity.class));
                return;
            case R.id.fl_order:
                MobclickAgent.onEvent(getActivity(), Event.shop_clickOrder);
                startActivity(new Intent(getActivity(), OrderListActivity.class));
                return;
            default:
                return;
        }
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        View view = inflater.inflate(R.layout.ShopMainFragment, container, false);
        ButterKnife.inject((Object) this, view);
        return view;
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((ListView) this.mPullListView.getRefreshableView()).addHeaderView(getHeaderView());
        this.mShopBannerAdapter = new ShopBannerPagerAdapter(getChildFragmentManager(), this
                .mBannerList);
        this.mViewPager.setAdapter(this.mShopBannerAdapter);
        this.mViewPager.setPageTransformer(true, TransformManager.getRandomTransform());
        this.mIndicator.setViewPager(this.mViewPager);
        this.mGoodsAdapter = new ShopTopicAdapter(getActivity(), this.mGoodsList);
        this.mShopShowcaseAdapter = new ShopShowcaseAdapter(getActivity(), this.mShowcaseList);
        addListener();
    }

    public void onResume() {
        super.onResume();
        EventBus.getDefault().post(new ConstEvent().setFlag(2));
        requestGoodsCount();
        requestOrderCount();
    }

    private View getHeaderView() {
        View headerView = View.inflate(getActivity(), R.layout.ShopMainFragment_Header, null);
        this.view_banner = headerView.findViewById(R.id.view_banner);
        this.ll_header_content = (LinearLayout) headerView.findViewById(R.id.ll_header_content);
        this.rl_category = (RelativeLayout) headerView.findViewById(R.id.rl_category);
        this.rl_category.setOnClickListener(this);
        headerView.findViewById(R.id.rl_cart).setOnClickListener(this);
        headerView.findViewById(R.id.fl_order).setOnClickListener(this);
        this.tv_badge_order = (TextView) headerView.findViewById(R.id.tv_badge_order);
        this.tv_badge_cart = (TextView) headerView.findViewById(R.id.tv_badge_cart);
        this.mViewPager = (ViewPager) headerView.findViewById(R.id.vp_banner);
        this.mIndicator = (LinePageIndicator) headerView.findViewById(R.id.indicator);
        this.tv_shop_type = (TextView) headerView.findViewById(R.id.tv_shop_type);
        return headerView;
    }

    private void addListener() {
        this.mPullListView.setOnRefreshListener(new OnRefreshListener<ListView>() {
            public void onRefresh(PullToRefreshBase<ListView> pullToRefreshBase) {
                ShopMainFragment.this.requestShopHomePages();
            }
        });
        this.mPullListView.setOnLastItemVisibleListener(new OnLastItemVisibleListener() {
            public void onLastItemVisible() {
                if (ShopMainFragment.this.mPage > ShopMainFragment.this.mCurrentPage &&
                        ShopMainFragment.this.hasMore) {
                    ShopMainFragment.this.mCurrentPage = ShopMainFragment.this.mPage;
                    ShopMainFragment.this.requestShopHomeMoreGoods();
                }
            }
        });
        this.mIndicator.setOnPageChangeListener(new SimpleOnPageChangeListener() {
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                ShopMainFragment.this.mCurrentIndex = position;
            }
        });
    }

    public void loadFirst() {
        this.mHandler.postDelayed(new Runnable() {
            public void run() {
                if (ShopMainFragment.this.isFirstLoad && ShopMainFragment.this.mPullListView !=
                        null) {
                    ShopMainFragment.this.mPullListView.setRefreshing(true);
                }
            }
        }, 500);
    }

    private void requestShopHomePages() {
        this.mPage = 1;
        this.mCurrentPage = this.mPage;
        this.hasMore = true;
        ShopApi.getShopHomePages(getActivity(), new JsonCallback(getActivity()) {
            public void ok(JSONObject object) {
                if (ShopMainFragment.this.isAdded() && !ShopMainFragment.this.isDetached()) {
                    ShopMainFragment.this.isFirstLoad = false;
                    ShopPage shopPage = (ShopPage) FastJsonUtils.fromJson(object, ShopPage.class);
                    if (shopPage != null) {
                        ShopMainFragment.this.initBanner(shopPage.banner_showcases);
                        ShopMainFragment.this.mCategories = shopPage.categories;
                        ShopMainFragment.this.initDatas(shopPage.datas);
                    }
                }
            }

            public void onFinish() {
                ShopMainFragment.this.mPullListView.onRefreshComplete();
            }
        });
    }

    private void requestShopHomeMoreGoods() {
        ShopApi.getShopHomeMoreGoods(getActivity(), this.mPage, new JsonCallback(getActivity()) {
            public void ok(JSONObject object) {
                super.ok(object);
                if (ShopMainFragment.this.isAdded() && !ShopMainFragment.this.isDetached()) {
                    BaseShowcase baseShowcase = (BaseShowcase) FastJsonUtils.fromJson(object,
                            BaseShowcase.class);
                    if (baseShowcase != null && TextUtils.equals(TYPE_SHOWCASE.homepage_goods
                            .name(), baseShowcase.type)) {
                        List<Goods> goodsList = FastJsonUtils.parseList(baseShowcase.list
                                .toString(), Goods.class);
                        if (goodsList == null || goodsList.size() <= 0) {
                            ShopMainFragment.this.hasMore = false;
                            return;
                        }
                        ShopMainFragment.this.mPage = ShopMainFragment.this.mPage + 1;
                        ShopMainFragment.this.mGoodsList.addAll(goodsList);
                        if (ShopMainFragment.this.mGoodsAdapter != null) {
                            ShopMainFragment.this.mGoodsAdapter.notifyDataSetChanged();
                        }
                    }
                }
            }

            public void onFinish() {
                super.onFinish();
                ShopMainFragment.this.mPullListView.onRefreshComplete();
            }
        });
    }

    private void initBanner(List<Showcase> banner_showcases) {
        if (getActivity() != null && !getActivity().isFinishing()) {
            this.mBannerList.clear();
            if (banner_showcases == null || banner_showcases.size() <= 0) {
                this.view_banner.setVisibility(8);
                return;
            }
            ViewUtils.setViewScaleHeight(getActivity(), this.mViewPager, 640, 320);
            this.view_banner.setVisibility(0);
            this.mBannerList.addAll(banner_showcases);
            this.mShopBannerAdapter.notifyDataSetChanged();
            if (this.mShopBannerAdapter.getCount() < 2) {
                this.mIndicator.setVisibility(8);
                return;
            }
            this.mIndicator.setVisibility(0);
            this.mCurrentIndex = 0;
            this.mHandler.removeCallbacks(this.mRunnable);
            this.mHandler.post(this.mRunnable);
        }
    }

    private void initDatas(List<BaseShowcase> baseShowcases) {
        if (baseShowcases != null && baseShowcases.size() > 0) {
            BaseShowcase baseShowcase;
            this.ll_header_content.removeAllViews();
            if (baseShowcases.size() > 1) {
                for (int i = 0; i < baseShowcases.size() - 1; i++) {
                    baseShowcase = (BaseShowcase) baseShowcases.get(i);
                    if (baseShowcase != null && TextUtils.equals(TYPE_SHOWCASE.homepage_showcase
                            .name(), baseShowcase.type)) {
                        initAdView(baseShowcase);
                    } else if (baseShowcase != null && TextUtils.equals(TYPE_SHOWCASE
                            .homepage_goods.name(), baseShowcase.type)) {
                        initGoodsView(baseShowcase.title, FastJsonUtils.parseList(baseShowcase
                                .list.toString(), Goods.class));
                    }
                }
            }
            baseShowcase = (BaseShowcase) baseShowcases.get(baseShowcases.size() - 1);
            if (baseShowcase != null) {
                if (!TextUtils.isEmpty(baseShowcase.title)) {
                    this.tv_shop_type.setText(baseShowcase.title);
                }
                this.mPage++;
                String data = baseShowcase.list.toString();
                if (TextUtils.equals(TYPE_SHOWCASE.homepage_goods.name(), baseShowcase.type)) {
                    List<Goods> goodsList = FastJsonUtils.parseList(data, Goods.class);
                    this.mGoodsList.clear();
                    this.mGoodsList.addAll(goodsList);
                    this.mPullListView.setAdapter(this.mGoodsAdapter);
                    this.mGoodsAdapter.notifyDataSetChanged();
                } else if (TextUtils.equals(TYPE_SHOWCASE.homepage_showcase.name(), baseShowcase
                        .type)) {
                    List<Showcase> showcaseList = FastJsonUtils.parseList(data, Showcase.class);
                    this.mShowcaseList.clear();
                    this.mShowcaseList.addAll(showcaseList);
                    this.mPullListView.setAdapter(this.mShopShowcaseAdapter);
                    this.mShopShowcaseAdapter.notifyDataSetChanged();
                }
            }
        }
    }

    private void initAdView(BaseShowcase adShowcase) {
        if (adShowcase != null && adShowcase.list != null && adShowcase.list.size() > 0) {
            View view_ad = LayoutInflater.from(getActivity()).inflate(R.layout.iy, null);
            TextView tv_shop_type = (TextView) view_ad.findViewById(R.id.tv_shop_type);
            if (!TextUtils.isEmpty(adShowcase.title)) {
                tv_shop_type.setText(adShowcase.title);
            }
            LinearLayout ll_list = (LinearLayout) view_ad.findViewById(R.id.ll_list);
            ll_list.removeAllViews();
            int i = 0;
            while (i < adShowcase.list.size()) {
                View view = getAdsView((Showcase) FastJsonUtils.fromJson(FastJsonUtils.toJson
                        (adShowcase.list.get(i)), Showcase.class), i == adShowcase.list.size() +
                        -1);
                if (view != null) {
                    ll_list.addView(view);
                }
                i++;
            }
            this.ll_header_content.addView(view_ad);
        }
    }

    private View getAdsView(final Showcase showcase, boolean isLast) {
        View adView = LayoutInflater.from(getActivity()).inflate(R.layout.nh, null);
        final ImageView iv_ad = (ImageView) adView.findViewById(R.id.iv_ad);
        adView.findViewById(R.id.view_line).setVisibility(isLast ? 8 : 0);
        iv_ad.getLayoutParams().height = ResolutionUtils.getHeight(getActivity(), showcase
                .default_photo_height, showcase.default_photo_width);
        iv_ad.post(new Runnable() {
            public void run() {
                ImageLoader.getInstance().displayImage(showcase.default_photo_url, iv_ad,
                        ImageLoaderOptions.global((int) R.drawable.aa3));
            }
        });
        iv_ad.setOnClickListener(new goodsListListener(showcase));
        return adView;
    }

    private void initGoodsView(String title, List<Goods> goodsList) {
        if (goodsList != null && goodsList.size() > 0) {
            View view_ad = LayoutInflater.from(getActivity()).inflate(R.layout.iz, null);
            TextView tv_shop_type = (TextView) view_ad.findViewById(R.id.tv_shop_type);
            if (!TextUtils.isEmpty(title)) {
                tv_shop_type.setText(title);
            }
            NoScrollGridView gv_list = (NoScrollGridView) view_ad.findViewById(R.id.gv_list);
            for (int i = 0; i < goodsList.size(); i++) {
                gv_list.setAdapter(new GoodsVerticalAdapter(getActivity(), goodsList));
            }
            this.ll_header_content.addView(view_ad);
        }
    }

    private void showPopwindow(View view, List<Label> labelList) {
        this.mPopupWindow = new PopupWindow(getCategoryView(labelList), -2, -2);
        this.mPopupWindow.setFocusable(true);
        this.mPopupWindow.setOutsideTouchable(true);
        this.mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
        view.getLocationOnScreen(new int[2]);
        this.mPopupWindow.showAsDropDown(view, 0, 0);
    }

    private View getCategoryView(List<Label> labelList) {
        View view_shop_category = LayoutInflater.from(getActivity()).inflate(R.layout.qr, null);
        LinearLayout ll_category = (LinearLayout) view_shop_category.findViewById(R.id.ll_category);
        ScrollView sv_category = (ScrollView) view_shop_category.findViewById(R.id.sv_category);
        if (labelList != null && labelList.size() > 4) {
            sv_category.getLayoutParams().height = ViewUtils.dip2px(getActivity(), 200.0f);
        }
        if (labelList != null && labelList.size() > 0) {
            int i = 0;
            while (i < labelList.size()) {
                View view = getCategoryItemView((Label) labelList.get(i), i == labelList.size() +
                        -1);
                if (view != null) {
                    ll_category.addView(view);
                }
                i++;
            }
        }
        return view_shop_category;
    }

    private View getCategoryItemView(final Label label, boolean isLast) {
        View item_shop_category = LayoutInflater.from(getActivity()).inflate(R.layout.ix, null);
        TextView tv_label = (TextView) item_shop_category.findViewById(R.id.tv_category);
        View view_category = item_shop_category.findViewById(R.id.view_category);
        tv_label.setText(label.name);
        view_category.setVisibility(isLast ? 8 : 0);
        item_shop_category.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                ShopCategoryActivity.start(ShopMainFragment.this.getActivity(), label.id);
            }
        });
        return item_shop_category;
    }

    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
        EventBus.getDefault().unregister(this);
    }

    private void requestGoodsCount() {
        ShopApi.getCarts(getActivity(), new JsonCallback(getActivity()) {
            public void ok(JSONObject object) {
                super.ok(object);
                int total = 0;
                List<Goods> goodsList = FastJsonUtils.parseList(object.optString("item"), Goods
                        .class);
                if (goodsList != null && goodsList.size() > 0) {
                    for (Goods goods : goodsList) {
                        total += goods.quantity;
                    }
                }
                if (ShopMainFragment.this.mCartBadgeView == null) {
                    ShopMainFragment.this.mCartBadgeView = new NewBadgeView(ShopMainFragment.this
                            .getActivity());
                    ShopMainFragment.this.mCartBadgeView.setTargetView(ShopMainFragment.this
                            .tv_badge_cart);
                    ShopMainFragment.this.mCartBadgeView.setTextColor(-1);
                    ShopMainFragment.this.mCartBadgeView.setBackground(10, ShopMainFragment.this
                            .getResources().getColor(R.color.he));
                }
                ShopMainFragment.this.mCartBadgeView.setText(String.valueOf(total));
                ShopMainFragment.this.mCartBadgeView.setVisibility(total > 0 ? 0 : 8);
            }
        });
    }

    private void requestOrderCount() {
        ShopApi.getOrdersStats(getActivity(), new JsonCallback(getActivity()) {
            public void ok(JSONObject object) {
                int i = 0;
                super.ok(object);
                OrderState state = (OrderState) FastJsonUtils.fromJson(object, OrderState.class);
                if (state != null) {
                    int initialNum = state.initial;
                    if (ShopMainFragment.this.mOrderBadgeView == null) {
                        ShopMainFragment.this.mOrderBadgeView = new NewBadgeView(ShopMainFragment
                                .this.getActivity());
                        ShopMainFragment.this.mOrderBadgeView.setTargetView(ShopMainFragment.this
                                .tv_badge_order);
                        ShopMainFragment.this.mOrderBadgeView.setTextColor(-1);
                        ShopMainFragment.this.mOrderBadgeView.setBackground(10, ShopMainFragment
                                .this.getResources().getColor(R.color.he));
                        ShopMainFragment.this.mOrderBadgeView.setBadgeMargin(8, 0, 0, 0);
                    }
                    ShopMainFragment.this.mOrderBadgeView.setBadgeCount(initialNum);
                    NewBadgeView access$1200 = ShopMainFragment.this.mOrderBadgeView;
                    if (initialNum <= 0) {
                        i = 8;
                    }
                    access$1200.setVisibility(i);
                }
            }

            public void onFinish() {
                super.onFinish();
                ShopMainFragment.this.dismissLoading();
            }
        });
    }

    public void onDestroy() {
        super.onDestroy();
        this.mHandler.removeCallbacksAndMessages(null);
    }

    public void onEventMainThread(ConstEvent constEvent) {
    }
}
