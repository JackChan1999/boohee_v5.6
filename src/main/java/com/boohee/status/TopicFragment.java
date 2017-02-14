package com.boohee.status;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;

import butterknife.ButterKnife;
import butterknife.InjectView;

import com.boohee.api.StatusApi;
import com.boohee.model.status.Post;
import com.boohee.model.status.Topic;
import com.boohee.myview.ViewPagerHeaderScroll.delegate.AbsListViewDelegate;
import com.boohee.myview.ViewPagerHeaderScroll.fragment.BaseViewPagerFragment;
import com.boohee.one.R;
import com.boohee.one.http.JsonCallback;
import com.boohee.one.http.JsonParams;
import com.boohee.one.http.client.BooheeClient;
import com.boohee.one.player.PlayerManager;
import com.boohee.one.ui.adapter.HomeTimelineAdapter;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnLastItemVisibleListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

public class TopicFragment extends BaseViewPagerFragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private boolean isChoice;
    private boolean             isLastVisible        = false;
    private AbsListViewDelegate mAbsListViewDelegate = new AbsListViewDelegate();
    private HomeTimelineAdapter mAdapter;
    private ArrayList<Post>     mPosts;
    @InjectView(2131427552)
    PullToRefreshListView mPullRefreshListView;
    private String mTitle;
    private Topic  mTopic;

    public static TopicFragment newInstance(String title, boolean isChoice) {
        TopicFragment fragment = new TopicFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, title);
        args.putBoolean(ARG_PARAM2, isChoice);
        fragment.setArguments(args);
        return fragment;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.mTitle = getArguments().getString(ARG_PARAM1);
            this.isChoice = getArguments().getBoolean(ARG_PARAM2);
        }
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        View view = inflater.inflate(R.layout.n2, container, false);
        ButterKnife.inject((Object) this, view);
        return view;
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
        getCurrentTopic();
    }

    private void initView() {
        this.mPullRefreshListView.setOnRefreshListener(new OnRefreshListener<ListView>() {
            public void onRefresh(PullToRefreshBase<ListView> pullToRefreshBase) {
                TopicFragment.this.getCurrentTopic();
            }
        });
        this.mPullRefreshListView.setOnLastItemVisibleListener(new OnLastItemVisibleListener() {
            public void onLastItemVisible() {
                if (!TopicFragment.this.isLastVisible) {
                    TopicFragment.this.getNextTopic();
                }
            }
        });
    }

    private void getCurrentTopic() {
        JsonParams jsonParams = new JsonParams();
        jsonParams.put("title", this.mTitle);
        String url = "";
        if (this.isChoice) {
            url = StatusApi.URL_TOPIC_CHOICE;
        } else {
            url = "/api/v1/topics/posts";
        }
        BooheeClient.build("status").get(url, jsonParams, new JsonCallback(getActivity()) {
            public void ok(JSONObject object) {
                super.ok(object);
                TopicFragment.this.mTopic = Topic.parseSelf(object.optJSONObject("topic"));
                TopicFragment.this.mPosts = Post.parsePosts(object.optString("posts"));
                TopicFragment.this.initUI();
            }

            public void onFinish() {
                super.onFinish();
                TopicFragment.this.mPullRefreshListView.onRefreshComplete();
            }
        }, getActivity());
    }

    private void getNextTopic() {
        if (this.mPosts != null && this.mPosts.size() != 0) {
            this.isLastVisible = true;
            int previous_id = ((Post) this.mPosts.get(this.mPosts.size() - 1)).id;
            JsonParams jsonParams = new JsonParams();
            jsonParams.put("title", this.mTitle);
            jsonParams.put("previous_id", previous_id);
            String url = "";
            if (this.isChoice) {
                url = StatusApi.URL_TOPIC_CHOICE;
            } else {
                url = "/api/v1/topics/posts";
            }
            BooheeClient.build("status").get(url, jsonParams, new JsonCallback(getActivity()) {
                public void ok(JSONObject object) {
                    super.ok(object);
                    try {
                        ArrayList<Post> morePosts = Post.parsePosts(object.getJSONArray("posts")
                                .toString());
                        if (morePosts != null) {
                            TopicFragment.this.mPosts.addAll(morePosts);
                            TopicFragment.this.mAdapter.notifyDataSetChanged();
                            TopicFragment.this.isLastVisible = false;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, getActivity());
        }
    }

    private void initUI() {
        if (this.mPosts != null) {
            this.mAdapter = new HomeTimelineAdapter(getActivity(), this.mPosts);
            this.mPullRefreshListView.setAdapter(this.mAdapter);
        }
    }

    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    public boolean isViewBeingDragged(MotionEvent event) {
        if (this.mPullRefreshListView == null) {
            return false;
        }
        return this.mAbsListViewDelegate.isViewBeingDragged(event, (AbsListView) this
                .mPullRefreshListView.getRefreshableView());
    }

    public void onPause() {
        super.onPause();
        if (!PlayerManager.getInstance().isStartFullScreen()) {
            PlayerManager.getInstance().releaseAll();
        }
    }
}
