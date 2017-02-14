package com.boohee.one.bet;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.SimpleOnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import com.boohee.account.HomeImagePagerAdapter;
import com.boohee.one.R;
import com.boohee.one.bet.adapter.BetAdapter;
import com.boohee.one.bet.model.Bet;
import com.boohee.one.bet.model.BetBannerBottom;
import com.boohee.one.http.JsonCallback;
import com.boohee.one.http.client.BooheeClient;
import com.boohee.one.tinker.reporter.SampleTinkerReport;
import com.boohee.one.transform.TransformManager;
import com.boohee.one.ui.BrowserActivity;
import com.boohee.one.ui.fragment.BaseFragment;
import com.boohee.utility.BooheeScheme;
import com.boohee.utility.ImageLoaderOptions;
import com.boohee.utils.FastJsonUtils;
import com.boohee.utils.ViewUtils;
import com.boohee.utils.WheelUtils;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.viewpagerindicator.LinePageIndicator;

import java.util.List;

import org.json.JSONObject;

public class BetFragment extends BaseFragment {
    private HomeImagePagerAdapter bannerAdapter;
    private List<BetBannerBottom> bannerBottoms;
    private List<Bet>             bets;
    private int                   currentItem;
    @InjectView(2131428199)
    FrameLayout flTips;
    private Runnable girlRunnable = new Runnable() {
        public void run() {
            try {
                if (BetFragment.this.bannerBottoms != null && BetFragment.this.bannerBottoms.size
                        () != 0 && BetFragment.this.bannerAdapter != null && BetFragment.this
                        .bannerAdapter.getCount() > 1) {
                    if (BetFragment.this.currentItem > BetFragment.this.bannerBottoms.size() - 1) {
                        BetFragment.this.currentItem = 0;
                    }
                    BetFragment.this.viewpagerTips.setCurrentItem(BetFragment.this.currentItem,
                            true);
                    BetFragment.this.tipsIndicator.setCurrentItem(BetFragment.this.currentItem);
                    BetFragment.this.currentItem = BetFragment.this.currentItem + 1;
                    BetFragment.this.handler.postDelayed(BetFragment.this.girlRunnable, 5000);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };
    private Handler  handler      = new Handler();
    @InjectView(2131428191)
    ImageView ivBetDes;
    @InjectView(2131428189)
    ImageView ivBetTop;
    private JSONObject jsonObject;
    @InjectView(2131428192)
    LinearLayout            llBets;
    @InjectView(2131427647)
    LinearLayout            llContent;
    @InjectView(2131428194)
    LinearLayout            llTeam;
    @InjectView(2131427340)
    PullToRefreshScrollView scrollview;
    private boolean showTeam;
    @InjectView(2131428195)
    LinearLayout teamContent;
    private List<Bet> teams;
    @InjectView(2131428201)
    LinePageIndicator tipsIndicator;
    @InjectView(2131428200)
    ViewPager         viewpagerTips;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        View view = inflater.inflate(R.layout.fg, container, false);
        ButterKnife.inject((Object) this, view);
        return view;
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
        showLoading();
        loadData();
    }

    private void initView() {
        this.scrollview.setOnRefreshListener(new OnRefreshListener<ScrollView>() {
            public void onRefresh(PullToRefreshBase<ScrollView> pullToRefreshBase) {
                BetFragment.this.loadData();
            }
        });
        this.tipsIndicator.setOnPageChangeListener(new SimpleOnPageChangeListener() {
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                BetFragment.this.currentItem = position;
            }
        });
        ViewUtils.setViewScaleHeight(getActivity(), this.ivBetTop, 750, 250);
    }

    private void loadData() {
        BooheeClient.build("status").get("/api/v1/bet_nats", new JsonCallback(getActivity()) {
            public void ok(JSONObject object) {
                super.ok(object);
                BetFragment.this.jsonObject = object;
                BetFragment.this.initShow();
            }

            public void onFinish() {
                super.onFinish();
                BetFragment.this.scrollview.onRefreshComplete();
                BetFragment.this.dismissLoading();
            }
        }, getActivity());
    }

    private void initShow() {
        if (this.jsonObject != null) {
            JSONObject topObject = this.jsonObject.optJSONObject("banner_top");
            if (topObject != null) {
                this.imageLoader.displayImage(topObject.optString("pic_url"), this.ivBetTop,
                        ImageLoaderOptions.global((int) R.drawable.s_));
            }
            this.bannerBottoms = FastJsonUtils.parseList(this.jsonObject.optString
                    ("banner_bottom"), BetBannerBottom.class);
            this.bets = FastJsonUtils.parseList(this.jsonObject.optString(BooheeScheme.BETS), Bet
                    .class);
            this.teams = FastJsonUtils.parseList(this.jsonObject.optString("teams"), Bet.class);
            this.showTeam = this.jsonObject.optBoolean("show_team");
            initBetList();
            initTeamList();
            initBannerBottom();
        }
    }

    private void initTeamList() {
        if (!this.showTeam) {
            this.llTeam.setVisibility(8);
        } else if (this.teams != null && this.teams.size() > 0) {
            this.llTeam.setVisibility(0);
            this.teamContent.removeAllViews();
            BetAdapter adapter = new BetAdapter(getActivity(), this.teams);
            for (int i = 0; i < this.teams.size(); i++) {
                View view = adapter.getView(i, null, this.teamContent);
                if (view != null) {
                    this.teamContent.addView(view);
                }
            }
        }
    }

    private void initBetList() {
        if (this.bets == null || this.bets.size() <= 0) {
            this.llBets.setVisibility(8);
            return;
        }
        this.llBets.setVisibility(0);
        this.llContent.removeAllViews();
        BetAdapter adapter = new BetAdapter(getActivity(), this.bets);
        for (int i = 0; i < this.bets.size(); i++) {
            View view = adapter.getView(i, null, this.llContent);
            if (view != null) {
                this.llContent.addView(view);
            }
        }
    }

    private View getSelectItemView(Bet bet) {
        if (bet == null) {
            return null;
        }
        return LayoutInflater.from(getActivity()).inflate(R.layout.hj, null);
    }

    private void startPlayGirls() {
        this.handler.removeCallbacks(this.girlRunnable);
        if (this.bannerBottoms.size() > 1) {
            this.currentItem = 0;
            this.tipsIndicator.setVisibility(0);
            this.handler.post(this.girlRunnable);
            return;
        }
        this.tipsIndicator.setVisibility(8);
    }

    private void initBannerBottom() {
        if (this.bannerBottoms == null || this.bannerBottoms.size() <= 0) {
            this.flTips.setVisibility(8);
        }
        ViewUtils.setViewScaleHeight(getActivity(), this.viewpagerTips, 750, SampleTinkerReport
                .KEY_LOADED_PACKAGE_CHECK_SIGNATURE);
        this.flTips.setVisibility(0);
        this.bannerAdapter = new HomeImagePagerAdapter(getChildFragmentManager(), this
                .bannerBottoms);
        this.viewpagerTips.setAdapter(this.bannerAdapter);
        this.viewpagerTips.setPageTransformer(true, TransformManager.getRandomTransform());
        this.tipsIndicator.setViewPager(this.viewpagerTips);
        startPlayGirls();
    }

    @OnClick({2131428190, 2131428193, 2131428198, 2131428196})
    public void onClick(View view) {
        if (!WheelUtils.isFastDoubleClick() && !isDetached()) {
            switch (view.getId()) {
                case R.id.ll_bet_des:
                    BrowserActivity.comeOnBaby(getActivity(), "", "http://shop.boohee" +
                            ".com/store/pages/dssm");
                    return;
                case R.id.btn_all_bet:
                    BetAllActivity.comeOn(getActivity(), "bet");
                    return;
                case R.id.view_become_leader:
                    BrowserActivity.comeOnBaby(getActivity(), null, BooheeClient.build("status")
                            .getDefaultURL("/api/v1/bet_teams/new"));
                    return;
                case R.id.btn_all_team:
                    BetAllActivity.comeOn(getActivity(), "team");
                    return;
                default:
                    return;
            }
        }
    }

    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    public void onDestroy() {
        super.onDestroy();
        if (this.girlRunnable != null) {
            this.handler.removeCallbacks(this.girlRunnable);
        }
    }
}
