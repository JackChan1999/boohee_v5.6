package com.boohee.one.ui.fragment;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView.LayoutParams;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.ListView;

import butterknife.ButterKnife;
import butterknife.InjectView;

import com.boohee.api.StatusApi;
import com.boohee.model.status.Post;
import com.boohee.model.status.Topic;
import com.boohee.one.R;
import com.boohee.one.http.JsonCallback;
import com.boohee.one.player.PlayerManager;
import com.boohee.one.ui.BrowserActivity;
import com.boohee.one.ui.PullToRefreshHelper;
import com.boohee.one.ui.adapter.HomeTimelineAdapter;
import com.boohee.utility.ImageLoaderOptions;
import com.boohee.utils.ViewUtils;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnLastItemVisibleListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

public class GoodsPostsFragment extends BaseFragment {
    protected static final String  ARG_PARAM_SLUG = "ARG_PARAM_SLUG";
    private                boolean isLastVisible  = false;
    private HomeTimelineAdapter mAdapter;
    private ImageView           mHeaderView;
    private ArrayList<Post> mPosts = new ArrayList();
    @InjectView(2131427552)
    protected PullToRefreshListView mPullRefreshListView;
    protected String                mSlug;

    public static GoodsPostsFragment newInstance(String slug) {
        GoodsPostsFragment fragment = new GoodsPostsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM_SLUG, slug);
        fragment.setArguments(args);
        return fragment;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.mSlug = getArguments().getString(ARG_PARAM_SLUG);
        }
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        return inflater.inflate(R.layout.fw, container, false);
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.inject((Object) this, view);
        addListener();
    }

    public void loadFirst() {
        PullToRefreshHelper.loadFirst(getActivity());
    }

    protected void addListener() {
        this.mHeaderView = new ImageView(getActivity());
        this.mHeaderView.setScaleType(ScaleType.CENTER_CROP);
        this.mHeaderView.setLayoutParams(new LayoutParams(-1, -2));
        ((ListView) this.mPullRefreshListView.getRefreshableView()).addHeaderView(this.mHeaderView);
        this.mAdapter = new HomeTimelineAdapter(getActivity(), this.mPosts);
        this.mPullRefreshListView.setAdapter(this.mAdapter);
        this.mPullRefreshListView.setOnRefreshListener(new OnRefreshListener<ListView>() {
            public void onRefresh(PullToRefreshBase<ListView> pullToRefreshBase) {
                GoodsPostsFragment.this.getCurrentTopic();
            }
        });
        this.mPullRefreshListView.setOnLastItemVisibleListener(new OnLastItemVisibleListener() {
            public void onLastItemVisible() {
                if (!GoodsPostsFragment.this.isLastVisible) {
                    GoodsPostsFragment.this.getNextTopic();
                }
            }
        });
    }

    protected void getCurrentTopic() {
        StatusApi.getChannelPosts(getActivity(), this.mSlug, new JsonCallback(getActivity()) {
            public void ok(JSONObject object) {
                super.ok(object);
                final Topic topic = Topic.parseSelf(object.optJSONObject("channel"));
                if (!(topic == null || TextUtils.isEmpty(topic.head_image_url))) {
                    ViewUtils.setViewScaleHeight(GoodsPostsFragment.this.getActivity(),
                            GoodsPostsFragment.this.mHeaderView, 640, 320);
                    GoodsPostsFragment.this.imageLoader.displayImage(topic.head_image_url,
                            GoodsPostsFragment.this.mHeaderView, ImageLoaderOptions.global(new
                                    ColorDrawable(866805452)));
                    GoodsPostsFragment.this.mHeaderView.setOnClickListener(new OnClickListener() {
                        public void onClick(View v) {
                            BrowserActivity.comeOnBaby(GoodsPostsFragment.this.getActivity(),
                                    topic.title, topic.page_url);
                        }
                    });
                }
                ArrayList<Post> posts = Post.parsePosts(object.optString("posts"));
                if (posts != null && posts.size() > 0) {
                    GoodsPostsFragment.this.mPosts.clear();
                    GoodsPostsFragment.this.mPosts.addAll(posts);
                    GoodsPostsFragment.this.mAdapter.notifyDataSetChanged();
                }
            }

            public void onFinish() {
                super.onFinish();
                GoodsPostsFragment.this.mPullRefreshListView.onRefreshComplete();
            }
        });
    }

    private void getNextTopic() {
        if (this.mPosts != null && this.mPosts.size() != 0) {
            this.isLastVisible = true;
            StatusApi.getChannelPostsPrevious(getActivity(), ((Post) this.mPosts.get(this.mPosts
                    .size() - 1)).id, this.mSlug, new JsonCallback(getActivity()) {
                public void ok(JSONObject object) {
                    super.ok(object);
                    try {
                        ArrayList<Post> morePosts = Post.parsePosts(object.getJSONArray("posts")
                                .toString());
                        if (morePosts != null) {
                            GoodsPostsFragment.this.mPosts.addAll(morePosts);
                            GoodsPostsFragment.this.mAdapter.notifyDataSetChanged();
                            GoodsPostsFragment.this.isLastVisible = false;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
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
}
