package com.boohee.status;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.design.widget.TabLayout.OnTabSelectedListener;
import android.support.design.widget.TabLayout.Tab;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.SimpleOnPageChangeListener;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import butterknife.ButterKnife;
import butterknife.InjectView;

import com.boohee.main.GestureActivity;
import com.boohee.model.status.HotTopic;
import com.boohee.model.status.HotTopicSliders;
import com.boohee.one.R;
import com.boohee.one.http.JsonCallback;
import com.boohee.one.http.client.BooheeClient;
import com.boohee.one.transform.TransformManager;
import com.boohee.utility.BooheeScheme;
import com.boohee.utils.Helper;
import com.boohee.utils.ViewUtils;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.viewpagerindicator.LinePageIndicator;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

public class HotTopicActivity extends GestureActivity {
    private HotTopicAdapter mAdapter;
    private int                   mCurrentIndex = 0;
    private List<HotTopicSliders> mDataList     = new ArrayList();
    private Handler               mHandler      = new Handler();
    @InjectView(2131427825)
    LinePageIndicator mIndicator;
    private int mPage = 1;
    private PullToRefreshListView mPulltorefresh;
    private Runnable mRunnable = new Runnable() {
        public void run() {
            HotTopicActivity.this.mHandler.removeCallbacksAndMessages(null);
            if (HotTopicActivity.this.mSliderAdapter.getCount() > 1) {
                if (HotTopicActivity.this.viewPager.getCurrentItem() + 1 >= HotTopicActivity.this
                        .mSliderAdapter.getCount()) {
                    HotTopicActivity.this.mCurrentIndex = 0;
                }
                HotTopicActivity.this.viewPager.setCurrentItem(HotTopicActivity.this
                        .mCurrentIndex, true);
                HotTopicActivity.this.mIndicator.setCurrentItem(HotTopicActivity.this
                        .mCurrentIndex);
                HotTopicActivity.this.mCurrentIndex = HotTopicActivity.this.mCurrentIndex + 1;
                HotTopicActivity.this.mHandler.postDelayed(HotTopicActivity.this.mRunnable, 3000);
            }
        }
    };
    private HeaderAdapter mSliderAdapter;
    private String mURL = "/api/v1/topics/hots";
    @InjectView(2131428910)
    TabLayout tlTab;
    @InjectView(2131428374)
    View      viewBanner;
    @InjectView(2131427463)
    ViewPager viewPager;

    public class HeaderAdapter extends FragmentPagerAdapter {
        private List<HotTopicSliders> mDataList;

        public HeaderAdapter(FragmentManager fm, List<HotTopicSliders> sliders) {
            super(fm);
            this.mDataList = sliders;
        }

        public Fragment getItem(int arg0) {
            return HotTopicHeaderFragment.newInstance((HotTopicSliders) this.mDataList.get(arg0));
        }

        public int getCount() {
            return this.mDataList.size();
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bo);
        this.mSliderAdapter = new HeaderAdapter(getSupportFragmentManager(), this.mDataList);
        this.mAdapter = new HotTopicAdapter(this);
        initView();
        this.mHandler.postDelayed(new Runnable() {
            public void run() {
                HotTopicActivity.this.mPulltorefresh.setRefreshing(true);
            }
        }, 500);
    }

    private void initView() {
        this.mPulltorefresh = (PullToRefreshListView) findViewById(R.id.pulltorefresh);
        View header = LayoutInflater.from(this).inflate(R.layout.mi, null);
        ButterKnife.inject((Object) this, header);
        ((ListView) this.mPulltorefresh.getRefreshableView()).addHeaderView(header);
        this.mPulltorefresh.setAdapter(this.mAdapter);
        this.mPulltorefresh.setMode(Mode.BOTH);
        this.mPulltorefresh.setOnRefreshListener(new OnRefreshListener2<ListView>() {
            public void onPullDownToRefresh(PullToRefreshBase<ListView> pullToRefreshBase) {
                HotTopicActivity.this.requestData(false);
            }

            public void onPullUpToRefresh(PullToRefreshBase<ListView> pullToRefreshBase) {
                HotTopicActivity.this.requestData(true);
            }
        });
        this.mPulltorefresh.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                if (HotTopicActivity.this.mAdapter.getData().size() > 0 && HotTopicActivity.this
                        .mAdapter.getData().size() >= position - 2) {
                    HotTopic hotTopic = (HotTopic) HotTopicActivity.this.mAdapter.getData().get
                            (position - 2);
                    BooheeScheme.handleUrl(HotTopicActivity.this.ctx, hotTopic.url, hotTopic.title);
                }
            }
        });
        this.viewPager.setAdapter(this.mSliderAdapter);
        this.viewPager.setPageTransformer(true, TransformManager.getRandomTransform());
        this.mIndicator.setViewPager(this.viewPager);
        this.mIndicator.setOnPageChangeListener(new SimpleOnPageChangeListener() {
            public void onPageSelected(int position) {
                HotTopicActivity.this.mCurrentIndex = position;
            }
        });
        ViewUtils.setViewScaleHeight(this, this.viewPager, 750, 250);
        this.tlTab.addTab(this.tlTab.newTab().setText((CharSequence) "精选话题"));
        this.tlTab.addTab(this.tlTab.newTab().setText((CharSequence) "我的话题"));
        this.tlTab.setTabMode(1);
        this.tlTab.setOnTabSelectedListener(new OnTabSelectedListener() {
            public void onTabSelected(Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        HotTopicActivity.this.mURL = "/api/v1/topics/hots";
                        break;
                    case 1:
                        HotTopicActivity.this.mURL = "/api/v1/topics/favorites";
                        break;
                    default:
                        HotTopicActivity.this.mURL = "/api/v1/topics/hots";
                        break;
                }
                HotTopicActivity.this.requestData(false);
            }

            public void onTabUnselected(Tab tab) {
            }

            public void onTabReselected(Tab tab) {
            }
        });
    }

    protected void onDestroy() {
        super.onDestroy();
        this.mHandler.removeCallbacksAndMessages(null);
    }

    private void requestData(final boolean isLoadMore) {
        if (isLoadMore) {
            showLoading();
        } else {
            this.mAdapter.getData().clear();
            this.mPage = 1;
        }
        BooheeClient.build("status").get(String.format("%s?page=%d&category=2&with_slider=true",
                new Object[]{this.mURL, Integer.valueOf(this.mPage)}), new JsonCallback(this) {
            public void ok(JSONObject object) {
                String topic = object.optString("topics");
                if (!TextUtils.isEmpty(topic)) {
                    List<HotTopic> data = HotTopic.parseList(topic);
                    if (data != null && data.size() > 0) {
                        HotTopicActivity.this.mAdapter.getData().addAll(data);
                        HotTopicActivity.this.mPage = HotTopicActivity.this.mPage + 1;
                    }
                }
                HotTopicActivity.this.mAdapter.notifyDataSetChanged();
                if (!isLoadMore) {
                    String sliders = object.optString("sliders");
                    HotTopicActivity.this.mHandler.removeCallbacksAndMessages(HotTopicActivity
                            .this.mRunnable);
                    if (TextUtils.isEmpty(sliders)) {
                        HotTopicActivity.this.viewBanner.setVisibility(8);
                        return;
                    }
                    HotTopicActivity.this.viewBanner.setVisibility(0);
                    List<HotTopicSliders> data2 = HotTopicSliders.parseList(sliders);
                    if (data2 != null && data2.size() > 0) {
                        HotTopicActivity.this.mIndicator.setVisibility(data2.size() == 1 ? 4 : 0);
                        HotTopicActivity.this.mDataList.clear();
                        HotTopicActivity.this.mDataList.addAll(data2);
                        HotTopicActivity.this.mSliderAdapter.notifyDataSetChanged();
                        HotTopicActivity.this.mCurrentIndex = 0;
                        HotTopicActivity.this.mHandler.post(HotTopicActivity.this.mRunnable);
                    }
                }
            }

            public void fail(String message) {
                Helper.showToast((CharSequence) message);
            }

            public void onFinish() {
                HotTopicActivity.this.mPulltorefresh.onRefreshComplete();
                HotTopicActivity.this.dismissLoading();
            }
        }, this);
    }

    public static void comeOnBaby(Context context) {
        if (context != null) {
            context.startActivity(new Intent(context, HotTopicActivity.class));
        }
    }
}
