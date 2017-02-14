package com.boohee.one.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView.LayoutParams;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.ListView;

import com.boohee.api.StatusApi;
import com.boohee.main.GestureActivity;
import com.boohee.model.status.Post;
import com.boohee.model.status.Topic;
import com.boohee.one.R;
import com.boohee.one.http.JsonCallback;
import com.boohee.one.player.PlayerManager;
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

public class ChannelPostsActivity extends GestureActivity {
    public static final int    DEFAULT_HEIGHT = 320;
    public static final int    DEFAULT_WIDTH  = 640;
    public static final String EXTRA_SLUG     = "extra_slug";
    static final        String TAG            = ChannelPostsActivity.class.getSimpleName();
    private FloatingActionButton fab_button;
    private boolean isLastVisible = false;
    private HomeTimelineAdapter   mAdapter;
    private ImageView             mHeaderView;
    private ListView              mListView;
    private Menu                  mMenu;
    private ArrayList<Post>       mPosts;
    private PullToRefreshListView mPullRefreshListView;
    private String                mSlug;
    private Topic                 mTopic;

    public void onCreate(Bundle outState) {
        super.onCreate(outState);
        setContentView(R.layout.ap);
        this.mSlug = getStringExtra(EXTRA_SLUG);
        findViews();
        PullToRefreshHelper.loadFirst(this);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        this.mMenu = menu;
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 2:
                startImageLinkActivity();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void findViews() {
        this.fab_button = (FloatingActionButton) findViewById(R.id.fab_button);
        this.fab_button.setVisibility(8);
        this.mHeaderView = new ImageView(this);
        this.mHeaderView.setScaleType(ScaleType.CENTER_CROP);
        this.mHeaderView.setLayoutParams(new LayoutParams(-1, -2));
        this.mPullRefreshListView = (PullToRefreshListView) findViewById(R.id.listview);
        this.mListView = (ListView) this.mPullRefreshListView.getRefreshableView();
        this.mListView.addHeaderView(this.mHeaderView);
        this.mPullRefreshListView.setOnRefreshListener(new OnRefreshListener<ListView>() {
            public void onRefresh(PullToRefreshBase<ListView> pullToRefreshBase) {
                ChannelPostsActivity.this.getCurrentTopic();
            }
        });
        this.mPullRefreshListView.setOnLastItemVisibleListener(new OnLastItemVisibleListener() {
            public void onLastItemVisible() {
                if (!ChannelPostsActivity.this.isLastVisible) {
                    ChannelPostsActivity.this.getNextTopic();
                }
            }
        });
    }

    private void getCurrentTopic() {
        if (!TextUtils.isEmpty(this.mSlug)) {
            StatusApi.getChannelPosts(this.activity, this.mSlug, new JsonCallback(this.activity) {
                public void ok(JSONObject object) {
                    super.ok(object);
                    ChannelPostsActivity.this.mTopic = Topic.parseSelf(object.optJSONObject
                            ("channel"));
                    if (!(ChannelPostsActivity.this.mTopic == null || TextUtils.isEmpty
                            (ChannelPostsActivity.this.mTopic.title))) {
                        ChannelPostsActivity.this.setTitle(ChannelPostsActivity.this.mTopic.title);
                    }
                    ChannelPostsActivity.this.mPosts = Post.parsePosts(object.optString("posts"));
                    ChannelPostsActivity.this.initUI();
                }

                public void onFinish() {
                    super.onFinish();
                    ChannelPostsActivity.this.mPullRefreshListView.onRefreshComplete();
                }
            });
        }
    }

    private void getNextTopic() {
        if (this.mPosts != null && this.mPosts.size() != 0) {
            this.isLastVisible = true;
            StatusApi.getChannelPostsPrevious(this.activity, ((Post) this.mPosts.get(this.mPosts
                    .size() - 1)).id, this.mSlug, new JsonCallback(this.activity) {
                public void ok(JSONObject object) {
                    super.ok(object);
                    try {
                        ArrayList<Post> morePosts = Post.parsePosts(object.getJSONArray("posts")
                                .toString());
                        if (morePosts != null) {
                            ChannelPostsActivity.this.mPosts.addAll(morePosts);
                            ChannelPostsActivity.this.mAdapter.notifyDataSetChanged();
                            ChannelPostsActivity.this.isLastVisible = false;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                public void onFinish() {
                    super.onFinish();
                    ChannelPostsActivity.this.mPullRefreshListView.onRefreshComplete();
                }
            });
        }
    }

    private void initUI() {
        if (!(this.mTopic == null || TextUtils.isEmpty(this.mTopic.head_image_url))) {
            ViewUtils.setViewScaleHeight(this, this.mHeaderView, 640, 320);
            this.imageLoader.displayImage(this.mTopic.head_image_url, this.mHeaderView,
                    ImageLoaderOptions.global(new ColorDrawable(866805452)));
            if (!TextUtils.isEmpty(this.mTopic.page_url)) {
                this.mMenu.clear();
                this.mMenu.add(0, 2, 1, "详情").setShowAsAction(2);
                this.mHeaderView.setOnClickListener(new OnClickListener() {
                    public void onClick(View v) {
                        ChannelPostsActivity.this.startImageLinkActivity();
                    }
                });
            }
        }
        this.mAdapter = new HomeTimelineAdapter(this, this.mPosts);
        this.mPullRefreshListView.setAdapter(this.mAdapter);
    }

    private void startImageLinkActivity() {
        Intent intent = new Intent(this, BrowserActivity.class);
        intent.putExtra("title", this.mTopic.title);
        intent.putExtra("url", this.mTopic.page_url);
        startActivity(intent);
    }

    public static void comeOnBaby(Context context, String slug) {
        if (context != null) {
            Intent intent = new Intent(context, ChannelPostsActivity.class);
            intent.putExtra(EXTRA_SLUG, slug);
            context.startActivity(intent);
        }
    }

    protected void onPause() {
        super.onPause();
        if (!PlayerManager.getInstance().isStartFullScreen()) {
            PlayerManager.getInstance().releaseAll();
        }
    }
}
