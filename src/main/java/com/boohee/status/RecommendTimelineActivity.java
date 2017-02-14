package com.boohee.status;

import android.os.Bundle;
import android.widget.ListView;

import com.boohee.api.StatusApi;
import com.boohee.main.GestureActivity;
import com.boohee.model.status.Post;
import com.boohee.one.R;
import com.boohee.one.http.JsonCallback;
import com.boohee.one.player.PlayerManager;
import com.boohee.one.ui.PullToRefreshHelper;
import com.boohee.one.ui.adapter.HomeTimelineAdapter;
import com.boohee.utils.Helper;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnLastItemVisibleListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

public class RecommendTimelineActivity extends GestureActivity {
    static final String  TAG           = TopicActivity.class.getSimpleName();
    private      boolean isLastVisible = false;
    private HomeTimelineAdapter   mAdapter;
    private ListView              mListView;
    private ArrayList<Post>       mPosts;
    private PullToRefreshListView mPullRefreshListView;

    public void onCreate(Bundle outState) {
        super.onCreate(outState);
        setContentView(R.layout.cm);
        setTitle("热门动态");
        findViews();
        PullToRefreshHelper.loadFirst(this);
    }

    private void findViews() {
        this.mPullRefreshListView = (PullToRefreshListView) findViewById(R.id.listview);
        this.mListView = (ListView) this.mPullRefreshListView.getRefreshableView();
        this.mPullRefreshListView.setOnRefreshListener(new OnRefreshListener<ListView>() {
            public void onRefresh(PullToRefreshBase<ListView> pullToRefreshBase) {
                RecommendTimelineActivity.this.getCurrentTopic();
            }
        });
        this.mPullRefreshListView.setOnLastItemVisibleListener(new OnLastItemVisibleListener() {
            public void onLastItemVisible() {
                if (!RecommendTimelineActivity.this.isLastVisible) {
                    RecommendTimelineActivity.this.getNextTopic();
                }
            }
        });
    }

    private void getCurrentTopic() {
        StatusApi.getRecommended(this.activity, new JsonCallback(this.activity) {
            public void ok(JSONObject object) {
                super.ok(object);
                RecommendTimelineActivity.this.mPosts = Post.parsePosts(object.optString("posts"));
                Helper.showLog(RecommendTimelineActivity.TAG, RecommendTimelineActivity.this
                        .mPosts.size() + "");
                RecommendTimelineActivity.this.initUI();
            }

            public void onFinish() {
                super.onFinish();
                RecommendTimelineActivity.this.mPullRefreshListView.onRefreshComplete();
            }
        });
    }

    private void getNextTopic() {
        if (this.mPosts != null && this.mPosts.size() != 0) {
            this.isLastVisible = true;
            StatusApi.getPreviousRecommended(this.activity, ((Post) this.mPosts.get(this.mPosts
                    .size() - 1)).id, new JsonCallback(this.activity) {
                public void ok(JSONObject object) {
                    super.ok(object);
                    try {
                        ArrayList<Post> morePosts = Post.parsePosts(object.getJSONArray("posts")
                                .toString());
                        if (morePosts != null) {
                            RecommendTimelineActivity.this.mPosts.addAll(morePosts);
                            RecommendTimelineActivity.this.mAdapter.notifyDataSetChanged();
                            RecommendTimelineActivity.this.isLastVisible = false;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    private void initUI() {
        if (this.mPosts != null && this.mPosts.size() != 0) {
            this.mAdapter = new HomeTimelineAdapter(this, this.mPosts);
            this.mPullRefreshListView.setAdapter(this.mAdapter);
        }
    }

    protected void onPause() {
        super.onPause();
        if (!PlayerManager.getInstance().isStartFullScreen()) {
            PlayerManager.getInstance().releaseAll();
        }
    }
}
