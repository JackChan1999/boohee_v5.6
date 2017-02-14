package com.boohee.uchoice;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.SimpleOnPageChangeListener;
import android.view.View;
import android.widget.LinearLayout;

import com.boohee.api.ShopApi;
import com.boohee.main.GestureActivity;
import com.boohee.model.OrderState;
import com.boohee.myview.NewBadgeView;
import com.boohee.one.R;
import com.boohee.one.http.JsonCallback;
import com.boohee.one.ui.fragment.OrderListFragment;
import com.boohee.one.ui.fragment.OrderListFragment.StateType;
import com.boohee.utils.FastJsonUtils;
import com.boohee.widgets.PagerSlidingTabStrip;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

public class OrderListActivity extends GestureActivity {
    public static final String EXTRA_INDEX = "extra_index";
    static              String TAG         = OrderListActivity.class.getName();
    private NewBadgeView  finishBadge;
    private NewBadgeView  initialBadge;
    private boolean       isFinishPageLoad;
    private boolean       isInitPageLoad;
    private boolean       isPayedPageLoad;
    private boolean       isSentPageLoad;
    private MPagerAdapter mAdapter;
    private Context       mContext;
    private List<OrderListFragment> mFragments = new ArrayList();
    private PagerSlidingTabStrip mTabs;
    private ViewPager            mViewPager;
    private NewBadgeView         payedBadge;
    private NewBadgeView         sentBadge;

    private class MPagerAdapter extends FragmentPagerAdapter {
        String[] TITLES = new String[]{"待付款", "待发货", "已发货", "已完成"};

        public MPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        public Fragment getItem(int position) {
            return (Fragment) OrderListActivity.this.mFragments.get(position);
        }

        public int getCount() {
            return this.TITLES.length;
        }

        public CharSequence getPageTitle(int position) {
            return this.TITLES[position];
        }
    }

    private class MPagerChangeListener extends SimpleOnPageChangeListener {
        private MPagerChangeListener() {
        }

        public void onPageSelected(int position) {
            OrderListActivity.this.mTabs.getTabsContainer().getChildAt(position);
            switch (position) {
                case 0:
                    if (!OrderListActivity.this.isInitPageLoad) {
                        ((OrderListFragment) OrderListActivity.this.mFragments.get(position))
                                .initLoadData();
                        OrderListActivity.this.isInitPageLoad = true;
                        return;
                    }
                    return;
                case 1:
                    if (!OrderListActivity.this.isPayedPageLoad) {
                        ((OrderListFragment) OrderListActivity.this.mFragments.get(position))
                                .initLoadData();
                        OrderListActivity.this.isPayedPageLoad = true;
                        return;
                    }
                    return;
                case 2:
                    if (!OrderListActivity.this.isSentPageLoad) {
                        ((OrderListFragment) OrderListActivity.this.mFragments.get(position))
                                .initLoadData();
                        OrderListActivity.this.isSentPageLoad = true;
                        return;
                    }
                    return;
                case 3:
                    if (!OrderListActivity.this.isFinishPageLoad) {
                        ((OrderListFragment) OrderListActivity.this.mFragments.get(position))
                                .initLoadData();
                        OrderListActivity.this.isFinishPageLoad = true;
                        return;
                    }
                    return;
                default:
                    return;
            }
        }
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.wq);
        setContentView(R.layout.cf);
        this.mContext = this;
        initView();
    }

    protected void onResume() {
        super.onResume();
        loadCountData();
        reloadCurrentItem();
    }

    private void reloadCurrentItem() {
        try {
            OrderListFragment fragment = (OrderListFragment) this.mFragments.get(this.mViewPager
                    .getCurrentItem());
            if (fragment != null) {
                fragment.initLoadData();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initView() {
        this.mTabs = (PagerSlidingTabStrip) findViewById(R.id.pst_tabs);
        this.mViewPager = (ViewPager) findViewById(R.id.vp_content);
        initFragments();
        initViewPager();
    }

    private void initFragments() {
        this.mFragments.add(OrderListFragment.newInstance(StateType.initial));
        this.mFragments.add(OrderListFragment.newInstance(StateType.payed));
        this.mFragments.add(OrderListFragment.newInstance(StateType.sent));
        this.mFragments.add(OrderListFragment.newInstance(StateType.finished));
    }

    private void initViewPager() {
        this.mAdapter = new MPagerAdapter(getSupportFragmentManager());
        this.mViewPager.setAdapter(this.mAdapter);
        this.mViewPager.setOffscreenPageLimit(3);
        this.mTabs.setViewPager(this.mViewPager);
        this.mTabs.setOnPageChangeListener(new MPagerChangeListener());
        setCurrentIndex(getIntent().getIntExtra(EXTRA_INDEX, 0));
    }

    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setCurrentIndex(intent.getIntExtra(EXTRA_INDEX, 0));
    }

    private void setCurrentIndex(int currentIndex) {
        if (currentIndex > this.mAdapter.getCount()) {
            currentIndex = this.mAdapter.getCount() - 1;
        }
        this.mViewPager.setCurrentItem(currentIndex);
        if (currentIndex == 0 && !this.isInitPageLoad) {
            ((OrderListFragment) this.mFragments.get(currentIndex)).initLoadData();
            this.isInitPageLoad = true;
        }
    }

    private void loadCountData() {
        showLoading();
        ShopApi.getOrdersStats(this, new JsonCallback(this) {
            public void ok(JSONObject object) {
                super.ok(object);
                if (OrderListActivity.this.mTabs != null && !OrderListActivity.this.isFinishing()
                        && OrderListActivity.this.getWindow().getContext() != null) {
                    OrderState state = (OrderState) FastJsonUtils.fromJson(object, OrderState
                            .class);
                    if (state != null) {
                        LinearLayout tbContainer = OrderListActivity.this.mTabs.getTabsContainer();
                        if (tbContainer != null && tbContainer.getChildCount() == 4) {
                            View sentTab;
                            int i;
                            int initialNum = state.initial;
                            if (OrderListActivity.this.initialBadge == null) {
                                View initialTab = tbContainer.getChildAt(0);
                                OrderListActivity.this.initialBadge = new NewBadgeView
                                        (OrderListActivity.this.mContext);
                                OrderListActivity.this.initialBadge.setTargetView(initialTab);
                                OrderListActivity.this.initialBadge.setBadgeGravity(8388661);
                            }
                            OrderListActivity.this.initialBadge.setBadgeCount(initialNum);
                            OrderListActivity.this.initialBadge.setVisibility(initialNum > 0 ? 0
                                    : 8);
                            int payedNum = state.payed;
                            if (OrderListActivity.this.payedBadge == null) {
                                View payedTab = tbContainer.getChildAt(1);
                                OrderListActivity.this.payedBadge = new NewBadgeView
                                        (OrderListActivity.this.mContext);
                                OrderListActivity.this.payedBadge.setTargetView(payedTab);
                                OrderListActivity.this.payedBadge.setBadgeGravity(8388661);
                            }
                            OrderListActivity.this.payedBadge.setBadgeCount(payedNum);
                            OrderListActivity.this.payedBadge.setVisibility(payedNum > 0 ? 0 : 8);
                            int sentNum = state.sent + state.part_sent;
                            if (OrderListActivity.this.sentBadge == null) {
                                sentTab = tbContainer.getChildAt(2);
                                OrderListActivity.this.sentBadge = new NewBadgeView
                                        (OrderListActivity.this.mContext);
                                OrderListActivity.this.sentBadge.setTargetView(sentTab);
                                OrderListActivity.this.sentBadge.setBadgeGravity(8388661);
                            }
                            OrderListActivity.this.sentBadge.setBadgeCount(sentNum);
                            OrderListActivity.this.sentBadge.setVisibility(sentNum > 0 ? 0 : 8);
                            int finishNum = state.finished;
                            if (OrderListActivity.this.finishBadge == null) {
                                sentTab = tbContainer.getChildAt(3);
                                OrderListActivity.this.finishBadge = new NewBadgeView
                                        (OrderListActivity.this.mContext);
                                OrderListActivity.this.finishBadge.setTargetView(sentTab);
                                OrderListActivity.this.finishBadge.setBadgeGravity(8388661);
                            }
                            OrderListActivity.this.finishBadge.setBadgeCount(finishNum);
                            NewBadgeView access$600 = OrderListActivity.this.finishBadge;
                            if (finishNum > 0) {
                                i = 0;
                            } else {
                                i = 8;
                            }
                            access$600.setVisibility(i);
                        }
                    }
                }
            }

            public void onFinish() {
                super.onFinish();
                OrderListActivity.this.dismissLoading();
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (this.mFragments != null && this.mFragments.size() > 0) {
            ((OrderListFragment) this.mFragments.get(0)).onActivityResult(requestCode,
                    resultCode, data);
        }
    }

    public static void comeOnBaby(Context context, int index) {
        if (context != null) {
            if (index < 0 || index > 3) {
                index = 0;
            }
            Intent intent = new Intent(context, OrderListActivity.class);
            intent.putExtra(EXTRA_INDEX, index);
            context.startActivity(intent);
        }
    }
}
