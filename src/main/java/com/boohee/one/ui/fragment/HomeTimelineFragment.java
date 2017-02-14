package com.boohee.one.ui.fragment;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.widget.ListView;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;

import com.boohee.api.StatusApi;
import com.boohee.database.UserPreference;
import com.boohee.model.status.Post;
import com.boohee.model.status.StatusUser;
import com.boohee.one.R;
import com.boohee.one.cache.CacheKey;
import com.boohee.one.http.JsonCallback;
import com.boohee.one.player.PlayerManager;
import com.boohee.one.ui.adapter.HomeTimelineAdapter;
import com.boohee.one.ui.fragment.PartnerFragment.RefreshListener;
import com.boohee.one.ui.fragment.PartnerFragment.ShowHintListener;
import com.boohee.status.MyTimelineActivity;
import com.boohee.status.SearchFriendsActivity;
import com.boohee.utils.Helper;
import com.boohee.utils.ListViewUtils;
import com.boohee.utils.NumberUtils;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnLastItemVisibleListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

public class HomeTimelineFragment extends BaseFragment implements RefreshListener,
        ShowHintListener {
    static final String  TAG           = HomeTimelineFragment.class.getSimpleName();
    private      boolean isLastVisible = false;
    private HomeTimelineAdapter mAdapter;
    private String              mDefaultUserKey;
    private Handler mHandler = new Handler();
    private ListView        mListView;
    private ArrayList<Post> mPosts;
    @InjectView(2131427552)
    PullToRefreshListView mPullRefreshListView;
    private StatusUser mUser;
    @InjectView(2131427652)
    TextView tvHint;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        return inflater.inflate(R.layout.fz, container, false);
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.inject((Object) this, view);
        initViews();
        this.mDefaultUserKey = UserPreference.getUserKey(getActivity());
        if (this.mCache.getAsJSONObject(CacheKey.HOME_TIMELINE) != null) {
            initTimeline(this.mCache.getAsJSONObject(CacheKey.HOME_TIMELINE));
        }
        loadFirst();
    }

    private void initViews() {
        this.mListView = (ListView) this.mPullRefreshListView.getRefreshableView();
        View headerView = View.inflate(getActivity(), R.layout.pf, null);
        this.mListView.addHeaderView(headerView);
        OnClickListener listener = new OnClickListener() {
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.tv_hint:
                        HomeTimelineFragment.this.loadFirst();
                        return;
                    case R.id.tv_my_page:
                        MyTimelineActivity.comeOnBaby(HomeTimelineFragment.this.getActivity());
                        return;
                    case R.id.tv_find_friends:
                        SearchFriendsActivity.comeOnBaby(HomeTimelineFragment.this.getActivity());
                        return;
                    default:
                        return;
                }
            }
        };
        headerView.findViewById(R.id.tv_my_page).setOnClickListener(listener);
        headerView.findViewById(R.id.tv_find_friends).setOnClickListener(listener);
        this.tvHint.setOnClickListener(listener);
        this.mPullRefreshListView.setOnRefreshListener(new OnRefreshListener<ListView>() {
            public void onRefresh(PullToRefreshBase<ListView> pullToRefreshBase) {
                HomeTimelineFragment.this.hideHint();
                HomeTimelineFragment.this.getCurrentStatus();
            }
        });
        this.mPullRefreshListView.setOnLastItemVisibleListener(new OnLastItemVisibleListener() {
            public void onLastItemVisible() {
                if (!HomeTimelineFragment.this.isLastVisible && HomeTimelineFragment.this
                        .getActivity() != null) {
                    HomeTimelineFragment.this.getPreviousStatus();
                }
            }
        });
    }

    public void onResume() {
        super.onResume();
        Helper.showLog(TAG, UserPreference.getUserKey(getActivity()));
        if (this.mDefaultUserKey != null && !this.mDefaultUserKey.equals(UserPreference
                .getUserKey(getActivity()))) {
            loadFirst();
            this.mDefaultUserKey = UserPreference.getUserKey(getActivity());
        }
    }

    public void onDestroy() {
        super.onDestroy();
        this.mHandler.removeCallbacksAndMessages(null);
    }

    public void loadFirst() {
        if (this.mPullRefreshListView != null && getActivity() != null) {
            ListViewUtils.smoothScrollListViewToTop((ListView) this.mPullRefreshListView
                    .getRefreshableView());
            this.mHandler.postDelayed(new Runnable() {
                public void run() {
                    HomeTimelineFragment.this.mPullRefreshListView.setRefreshing();
                }
            }, 500);
        }
    }

    private void getCurrentStatus() {
        if (getActivity() != null) {
            StatusApi.getHomeTimeline(getActivity(), new JsonCallback(getActivity()) {
                public void ok(JSONObject object) {
                    super.ok(object);
                    HomeTimelineFragment.this.mCache.put(CacheKey.HOME_TIMELINE, object);
                    HomeTimelineFragment.this.initTimeline(object);
                }

                public void onFinish() {
                    super.onFinish();
                    HomeTimelineFragment.this.mPullRefreshListView.onRefreshComplete();
                }
            });
        }
    }

    private void initTimeline(JSONObject timelineObject) {
        if (timelineObject != null) {
            if (timelineObject.has("posts")) {
                this.mPosts = Post.removeDisablePost(Post.parsePosts(timelineObject.optString
                        ("posts")));
            }
            if (getActivity() != null) {
                this.mAdapter = new HomeTimelineAdapter(getActivity(), this.mPosts);
                this.mPullRefreshListView.setAdapter(this.mAdapter);
            }
        }
    }

    private void getPreviousStatus() {
        if (getActivity() != null && this.mPosts != null && this.mPosts.size() != 0) {
            this.isLastVisible = true;
            StatusApi.getPreviousHomeTimeline(getActivity(), ((Post) this.mPosts.get(this.mPosts
                    .size() - 1)).id, new JsonCallback(getActivity()) {
                public void ok(JSONObject object) {
                    super.ok(object);
                    try {
                        ArrayList<Post> morePosts = Post.removeDisablePost(Post.parsePosts(object
                                .getJSONArray("posts").toString()));
                        if (morePosts != null) {
                            HomeTimelineFragment.this.mPosts.addAll(morePosts);
                            HomeTimelineFragment.this.mAdapter.notifyDataSetChanged();
                            HomeTimelineFragment.this.isLastVisible = false;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                public void onFinish() {
                    super.onFinish();
                    HomeTimelineFragment.this.mPullRefreshListView.onRefreshComplete();
                }
            });
        }
    }

    public void onPause() {
        super.onPause();
        if (!PlayerManager.getInstance().isStartFullScreen()) {
            PlayerManager.getInstance().releaseAll();
        }
    }

    public void onRefresh() {
        loadFirst();
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
                    HomeTimelineFragment.this.tvHint.setVisibility(8);
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
