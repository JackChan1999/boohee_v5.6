package com.boohee.one.ui.fragment;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.SimpleOnPageChangeListener;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;

import com.boohee.account.HomeImagePagerAdapter;
import com.boohee.api.StatusApi;
import com.boohee.model.HomeSlider;
import com.boohee.model.PartnerLabel;
import com.boohee.model.status.Post;
import com.boohee.one.R;
import com.boohee.one.cache.CacheKey;
import com.boohee.one.event.StatusUnreadCount;
import com.boohee.one.http.JsonCallback;
import com.boohee.one.transform.TransformManager;
import com.boohee.one.ui.adapter.HomeTimelineAdapter;
import com.boohee.one.ui.fragment.PartnerFragment.RefreshListener;
import com.boohee.one.ui.fragment.PartnerFragment.ShowHintListener;
import com.boohee.utility.BooheeScheme;
import com.boohee.utils.FastJsonUtils;
import com.boohee.utils.ListViewUtils;
import com.boohee.utils.NumberUtils;
import com.boohee.utils.ViewUtils;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnLastItemVisibleListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.viewpagerindicator.LinePageIndicator;

import de.greenrobot.event.EventBus;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PartnerDiscoverFragment extends BaseFragment implements RefreshListener,
        ShowHintListener {
    private FrameLayout       flPartner;
    private boolean           hasLoadPost;
    private boolean           hasLoadSlider;
    private LinePageIndicator indicator;
    private boolean isFisrt       = true;
    private boolean isLastVisible = false;
    public  boolean isLoadFirst   = true;
    private List<PartnerLabel>    labels;
    private LinearLayout          llLabel;
    private HomeImagePagerAdapter mBannerAdapter;
    private int     mCurrentItem = 0;
    private Handler mHandler     = new Handler();
    private ListView            mListView;
    private HomeTimelineAdapter mPostAdapter;
    private ArrayList<Post> mPosts = new ArrayList();
    @InjectView(2131427552)
    PullToRefreshListView mPullRefreshListView;
    private Runnable mSliderRunnable = new Runnable() {
        public void run() {
            try {
                if (PartnerDiscoverFragment.this.sliders != null && PartnerDiscoverFragment.this
                        .sliders.size() != 0 && PartnerDiscoverFragment.this.mBannerAdapter !=
                        null && PartnerDiscoverFragment.this.mBannerAdapter.getCount() > 1) {
                    if (PartnerDiscoverFragment.this.mCurrentItem > PartnerDiscoverFragment.this
                            .sliders.size() - 1) {
                        PartnerDiscoverFragment.this.mCurrentItem = 0;
                    }
                    PartnerDiscoverFragment.this.viewPager.setCurrentItem(PartnerDiscoverFragment
                            .this.mCurrentItem, true);
                    PartnerDiscoverFragment.this.indicator.setCurrentItem(PartnerDiscoverFragment
                            .this.mCurrentItem);
                    PartnerDiscoverFragment.this.mCurrentItem = PartnerDiscoverFragment.this
                            .mCurrentItem + 1;
                    PartnerDiscoverFragment.this.mHandler.postDelayed(PartnerDiscoverFragment
                            .this.mSliderRunnable, 5000);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };
    private List<HomeSlider> sliders;
    private StatusUnreadCount statusUnreadCount = new StatusUnreadCount();
    private TextView  tvHint;
    private ViewPager viewPager;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        View view = inflater.inflate(R.layout.g_, container, false);
        ButterKnife.inject((Object) this, view);
        return view;
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initViews();
    }

    private void initViews() {
        View headerView = LayoutInflater.from(getActivity()).inflate(R.layout.qc, null, false);
        this.viewPager = (ViewPager) headerView.findViewById(R.id.viewpager_partner);
        this.indicator = (LinePageIndicator) headerView.findViewById(R.id.indicator);
        this.flPartner = (FrameLayout) headerView.findViewById(R.id.fl_partner);
        this.llLabel = (LinearLayout) headerView.findViewById(R.id.ll_label);
        this.tvHint = (TextView) getView().findViewById(R.id.tv_hint);
        this.tvHint.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                PartnerDiscoverFragment.this.onRefresh();
            }
        });
        this.mListView = (ListView) this.mPullRefreshListView.getRefreshableView();
        this.mListView.addHeaderView(headerView);
        this.mPostAdapter = new HomeTimelineAdapter(getActivity(), this.mPosts);
        this.mPullRefreshListView.setAdapter(this.mPostAdapter);
        this.mPullRefreshListView.setOnRefreshListener(new OnRefreshListener<ListView>() {
            public void onRefresh(PullToRefreshBase<ListView> pullToRefreshBase) {
                PartnerDiscoverFragment.this.hasLoadSlider = false;
                PartnerDiscoverFragment.this.hasLoadPost = false;
                PartnerDiscoverFragment.this.hideHint();
                PartnerDiscoverFragment.this.requestSliders();
                PartnerDiscoverFragment.this.getCurrentTopic();
            }
        });
        this.mPullRefreshListView.setOnLastItemVisibleListener(new OnLastItemVisibleListener() {
            public void onLastItemVisible() {
                if (!PartnerDiscoverFragment.this.isLastVisible) {
                    PartnerDiscoverFragment.this.getNextTopic();
                }
            }
        });
        this.indicator.setOnPageChangeListener(new SimpleOnPageChangeListener() {
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                PartnerDiscoverFragment.this.mCurrentItem = position;
            }
        });
        JSONObject object = this.mCache.getAsJSONObject(CacheKey.NEW_SQUARE_LIGHT);
        initHeader(object);
        initLable(object);
        new Handler().postDelayed(new Runnable() {
            public void run() {
                if (PartnerDiscoverFragment.this.isLoadFirst && PartnerDiscoverFragment.this
                        .mPullRefreshListView != null) {
                    PartnerDiscoverFragment.this.mPullRefreshListView.setRefreshing(true);
                }
            }
        }, 800);
    }

    private void initHeader(JSONObject object) {
        if (getActivity() != null && object != null) {
            JSONArray slidersArray = object.optJSONArray("sliders");
            if (slidersArray == null || slidersArray.length() <= 0) {
                this.sliders = null;
            } else {
                this.sliders = HomeSlider.parseSliders(slidersArray.toString());
            }
            initHeadAd();
        }
    }

    private void initHeadAd() {
        if (this.sliders == null || this.sliders.size() <= 0) {
            this.flPartner.setVisibility(8);
            return;
        }
        this.flPartner.setVisibility(0);
        this.mBannerAdapter = new HomeImagePagerAdapter(getChildFragmentManager(), this.sliders);
        this.viewPager.setAdapter(this.mBannerAdapter);
        this.viewPager.setPageTransformer(true, TransformManager.getRandomTransform());
        this.indicator.setViewPager(this.viewPager);
        ViewUtils.setViewScaleHeight(getActivity(), this.viewPager, 750, 250);
        startPlaySliders();
    }

    private void requestSliders() {
        StatusApi.getMainSquareLight(getActivity(), new JsonCallback(getActivity()) {
            public void ok(JSONObject object) {
                PartnerDiscoverFragment.this.initHeader(object);
                PartnerDiscoverFragment.this.initLable(object);
                PartnerDiscoverFragment.this.initUnread(object);
                PartnerDiscoverFragment.this.mCache.put(CacheKey.NEW_SQUARE_LIGHT, object);
            }

            public void onFinish() {
                super.onFinish();
                PartnerDiscoverFragment.this.hasLoadSlider = true;
                PartnerDiscoverFragment.this.refreshComplete();
            }
        });
    }

    private void initLable(JSONObject object) {
        if (!isRemoved() && object != null) {
            try {
                this.labels = FastJsonUtils.parseList(object.getString("labels"), PartnerLabel
                        .class);
                initLableView();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void initLableView() {
        if (this.labels == null || this.labels.size() == 0) {
            this.llLabel.setVisibility(8);
            return;
        }
        this.llLabel.setVisibility(0);
        this.llLabel.removeAllViews();
        for (final PartnerLabel label : this.labels) {
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.ig, this.llLabel,
                    false);
            TextView tvLabel = (TextView) view.findViewById(R.id.tv_label);
            this.imageLoader.displayImage(label.pic_url, (ImageView) view.findViewById(R.id
                    .iv_label));
            tvLabel.setText(label.title);
            view.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    BooheeScheme.handleUrl(PartnerDiscoverFragment.this.getActivity(), label.url);
                }
            });
            this.llLabel.addView(view);
        }
    }

    public void onStart() {
        super.onStart();
        if (!this.isFisrt) {
            requestSliders();
        }
        this.isFisrt = false;
    }

    private void initUnread(JSONObject object) {
        if (getActivity() != null && object != null && !this.isFisrt && !this.isLoadFirst) {
            this.statusUnreadCount.friend_posts_count = object.optString("friend_posts_count");
            showHint(object.optString("hot_posts_count"));
            EventBus.getDefault().post(this.statusUnreadCount);
        }
    }

    private void refreshComplete() {
        if (this.hasLoadSlider && this.hasLoadPost) {
            this.isLoadFirst = false;
            this.mPullRefreshListView.onRefreshComplete();
        }
    }

    private void getCurrentTopic() {
        StatusApi.getChannelPosts(getActivity(), "hot_posts", new JsonCallback(getActivity()) {
            public void ok(JSONObject object) {
                super.ok(object);
                PartnerDiscoverFragment.this.mPosts.clear();
                PartnerDiscoverFragment.this.mPosts.addAll(Post.parsePosts(object.optString
                        ("posts")));
                PartnerDiscoverFragment.this.mPostAdapter.notifyDataSetChanged();
            }

            public void onFinish() {
                super.onFinish();
                PartnerDiscoverFragment.this.hasLoadPost = true;
                PartnerDiscoverFragment.this.refreshComplete();
            }
        });
    }

    private void getNextTopic() {
        if (this.mPosts != null && this.mPosts.size() != 0) {
            this.isLastVisible = true;
            StatusApi.getChannelPostsPrevious(getActivity(), ((Post) this.mPosts.get(this.mPosts
                    .size() - 1)).id, "hot_posts", new JsonCallback(getActivity()) {
                public void ok(JSONObject object) {
                    super.ok(object);
                    try {
                        ArrayList<Post> morePosts = Post.parsePosts(object.getJSONArray("posts")
                                .toString());
                        if (morePosts != null) {
                            PartnerDiscoverFragment.this.mPosts.addAll(morePosts);
                            PartnerDiscoverFragment.this.mPostAdapter.notifyDataSetChanged();
                            PartnerDiscoverFragment.this.isLastVisible = false;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                public void onFinish() {
                    super.onFinish();
                    PartnerDiscoverFragment.this.mPullRefreshListView.onRefreshComplete();
                }
            });
        }
    }

    private void startPlaySliders() {
        if (this.sliders != null) {
            this.mHandler.removeCallbacks(this.mSliderRunnable);
            if (this.sliders.size() > 0) {
                this.flPartner.setVisibility(0);
                if (this.sliders.size() > 1) {
                    this.mCurrentItem = 0;
                    this.indicator.setVisibility(0);
                    this.mHandler.post(this.mSliderRunnable);
                    return;
                }
                this.indicator.setVisibility(8);
                return;
            }
            this.flPartner.setVisibility(8);
        }
    }

    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    public void onDestroy() {
        super.onDestroy();
        this.mHandler.removeCallbacks(this.mSliderRunnable);
    }

    public void onRefresh() {
        if (this.mPullRefreshListView != null) {
            if (this.mPosts == null || this.mPosts.size() <= 0) {
                this.mPullRefreshListView.onRefreshComplete();
                this.hasLoadPost = false;
                this.hasLoadSlider = false;
                this.mPullRefreshListView.setRefreshing(true);
                return;
            }
            ListViewUtils.smoothScrollListViewToTop(this.mListView);
            if (this.hasLoadSlider && this.hasLoadPost) {
                this.mPullRefreshListView.setRefreshing(true);
            }
        }
    }

    public void showHint(String data) {
        if (!TextUtils.isEmpty(data)) {
            if (this.tvHint.getVisibility() != 0) {
                this.tvHint.setVisibility(0);
                ObjectAnimator animator = ObjectAnimator.ofFloat(this.tvHint, "alpha", new
                        float[]{0.2f, 1.0f}).setDuration(300);
                animator.setInterpolator(new AccelerateInterpolator());
                animator.start();
            }
            if (NumberUtils.safeParseInt(data) > 99) {
                this.tvHint.setText("有 99+ 条新动态，点击更新");
                return;
            }
            this.tvHint.setText(String.format("有 %s 条新动态，点击更新", new Object[]{data}));
        }
    }

    private void hideHint() {
        if (this.tvHint.getVisibility() == 0) {
            ObjectAnimator animator = ObjectAnimator.ofFloat(this.tvHint, "alpha", new float[]{1
                    .0f, 0.0f}).setDuration(200);
            animator.setInterpolator(new AccelerateInterpolator());
            animator.addListener(new AnimatorListener() {
                public void onAnimationStart(Animator animation) {
                }

                public void onAnimationEnd(Animator animation) {
                    PartnerDiscoverFragment.this.tvHint.setVisibility(8);
                }

                public void onAnimationCancel(Animator animation) {
                }

                public void onAnimationRepeat(Animator animation) {
                }
            });
            animator.start();
        }
    }
}
