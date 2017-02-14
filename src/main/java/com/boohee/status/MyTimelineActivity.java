package com.boohee.status;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.boohee.api.ApiUrls;
import com.boohee.api.StatusApi;
import com.boohee.database.UserPreference;
import com.boohee.main.GestureActivity;
import com.boohee.model.status.Post;
import com.boohee.model.status.StatusUser;
import com.boohee.one.R;
import com.boohee.one.http.JsonCallback;
import com.boohee.one.http.client.BooheeClient;
import com.boohee.one.player.PlayerManager;
import com.boohee.one.ui.BrowserActivity;
import com.boohee.one.ui.PullToRefreshHelper;
import com.boohee.one.ui.adapter.UserTimelineAdapter;
import com.boohee.utility.Const;
import com.boohee.utility.Event;
import com.boohee.utility.ImageLoaderOptions;
import com.boohee.utils.FastJsonUtils;
import com.boohee.utils.Helper;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnLastItemVisibleListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.analytics.MobclickAgent;

import de.hdodenhof.circleimageview.CircleImageView;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

public class MyTimelineActivity extends GestureActivity implements OnClickListener {
    static final String TAG = MyTimelineActivity.class.getSimpleName();
    private CircleImageView       iv_avatar;
    private UserTimelineAdapter   mAdapter;
    private ListView              mListView;
    private ArrayList<Post>       mPosts;
    private PullToRefreshListView mPullRefreshListView;
    private StatusUser            mUser;
    private TextView              tv_description;
    private TextView              tv_diamond_count;
    private TextView              tv_fans_count;
    private TextView              tv_friends_count;
    private TextView              tv_post_count;
    private TextView              tv_username;

    public void onCreate(Bundle outState) {
        super.onCreate(outState);
        setContentView(R.layout.c8);
        MobclickAgent.onEvent(this.ctx, Event.STATUS_VIEW_USER_PAGE);
        setTitle(R.string.wp);
        findViews();
        PullToRefreshHelper.loadFirst(this);
    }

    private void findViews() {
        this.mPullRefreshListView = (PullToRefreshListView) findViewById(R.id.listview);
        this.mListView = (ListView) this.mPullRefreshListView.getRefreshableView();
        this.mListView.addHeaderView(getHeaderView());
        this.mListView.setSelector(R.color.in);
        this.mPullRefreshListView.setOnRefreshListener(new OnRefreshListener<ListView>() {
            public void onRefresh(PullToRefreshBase<ListView> pullToRefreshBase) {
                MyTimelineActivity.this.getCurrentStatus();
            }
        });
        this.mPullRefreshListView.setOnLastItemVisibleListener(new OnLastItemVisibleListener() {
            public void onLastItemVisible() {
                if (MyTimelineActivity.this.mPosts.size() > 0) {
                    MyTimelineActivity.this.getPreviousStatus();
                }
            }
        });
    }

    private View getHeaderView() {
        View headerView = View.inflate(this.ctx, R.layout.pv, null);
        this.tv_username = (TextView) headerView.findViewById(R.id.tv_username);
        this.tv_description = (TextView) headerView.findViewById(R.id.tv_description);
        this.iv_avatar = (CircleImageView) headerView.findViewById(R.id.iv_avatar);
        this.tv_post_count = (TextView) headerView.findViewById(R.id.tv_post_count);
        this.tv_diamond_count = (TextView) headerView.findViewById(R.id.tv_diamond_count);
        this.tv_diamond_count.setOnClickListener(this);
        headerView.findViewById(R.id.tv_diamond_des).setOnClickListener(this);
        this.tv_friends_count = (TextView) headerView.findViewById(R.id.tv_friends_count);
        this.tv_friends_count.setOnClickListener(this);
        headerView.findViewById(R.id.tv_friends_des).setOnClickListener(this);
        this.tv_fans_count = (TextView) headerView.findViewById(R.id.tv_fans_count);
        this.tv_fans_count.setOnClickListener(this);
        headerView.findViewById(R.id.tv_fans_des).setOnClickListener(this);
        return headerView;
    }

    private void getCurrentStatus() {
        if (this.activity != null) {
            StatusApi.getMyTimeline(this.activity, new JsonCallback(this.activity) {
                public void ok(JSONObject object) {
                    super.ok(object);
                    try {
                        MyTimelineActivity.this.initHeader(object);
                        MyTimelineActivity.this.mPosts = Post.parsePosts(object.getJSONArray
                                ("posts").toString());
                        MyTimelineActivity.this.mAdapter = new UserTimelineAdapter
                                (MyTimelineActivity.this, MyTimelineActivity.this.mPosts,
                                        MyTimelineActivity.this.mUser);
                        MyTimelineActivity.this.mListView.setAdapter(MyTimelineActivity.this
                                .mAdapter);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                public void onFinish() {
                    super.onFinish();
                    MyTimelineActivity.this.mPullRefreshListView.onRefreshComplete();
                }
            });
        }
    }

    private void initHeader(JSONObject object) {
        this.mUser = StatusUser.parseUser(object.optJSONObject(Const.USER));
        if (this.mUser != null) {
            if (!TextUtils.isEmpty(this.mUser.avatar_url)) {
                ImageLoader.getInstance().displayImage(this.mUser.avatar_url, this.iv_avatar,
                        ImageLoaderOptions.global((int) R.drawable.aa0));
            }
            this.iv_avatar.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    Intent intent = new Intent(MyTimelineActivity.this.activity,
                            LargeImageActivity.class);
                    if (!TextUtils.isEmpty(MyTimelineActivity.this.mUser.avatar_url)) {
                        if (MyTimelineActivity.this.mUser.avatar_url.contains("?")) {
                            intent.putExtra("image_url", MyTimelineActivity.this.mUser.avatar_url
                                    .substring(0, MyTimelineActivity.this.mUser.avatar_url
                                            .indexOf("?")));
                            MyTimelineActivity.this.activity.startActivity(intent);
                            return;
                        }
                        intent.putExtra("image_url", MyTimelineActivity.this.mUser.avatar_url);
                        MyTimelineActivity.this.activity.startActivity(intent);
                    }
                }
            });
            this.tv_username.setText(this.mUser.nickname);
            this.tv_description.setText(this.mUser.description == null ? "暂无描述" : this.mUser
                    .description);
            TextView textView = this.tv_post_count;
            StatusUser statusUser = this.mUser;
            textView.setText(StatusUser.displayCount(this.mUser.post_count));
            textView = this.tv_diamond_count;
            statusUser = this.mUser;
            textView.setText(StatusUser.displayCount(this.mUser.envious_count));
            textView = this.tv_friends_count;
            statusUser = this.mUser;
            textView.setText(StatusUser.displayCount(this.mUser.following_count));
            textView = this.tv_fans_count;
            statusUser = this.mUser;
            textView.setText(StatusUser.displayCount(this.mUser.follower_count));
        }
    }

    private void getPreviousStatus() {
        if (this.activity != null) {
            StatusApi.getMyPreviousTimeline(this.activity, ((Post) this.mPosts.get(this.mPosts
                    .size() - 1)).id, new JsonCallback(this.activity) {
                public void ok(JSONObject object) {
                    super.ok(object);
                    List<Post> morePosts = FastJsonUtils.parseList(object.optString("posts"),
                            Post.class);
                    if (morePosts != null && morePosts.size() > 0) {
                        MyTimelineActivity.this.mPosts.addAll(morePosts);
                        MyTimelineActivity.this.mAdapter.notifyDataSetChanged();
                    }
                }
            });
        }
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_friends:
                Intent intent = new Intent(this.ctx, FriendShipActivity.class);
                intent.putExtra(FriendShipActivity.FRIENDSHIP_POSITION, 0);
                startActivity(intent);
                return;
            case R.id.tv_diamond_count:
            case R.id.tv_diamond_des:
                startDiamond();
                return;
            case R.id.tv_friends_count:
            case R.id.tv_friends_des:
                startActivity(new Intent(this.activity, FriendShipActivity.class).putExtra
                        (FriendShipActivity.FRIENDSHIP_POSITION, 0));
                return;
            case R.id.tv_fans_count:
            case R.id.tv_fans_des:
                startActivity(new Intent(this.activity, FriendShipActivity.class).putExtra
                        (FriendShipActivity.FRIENDSHIP_POSITION, 1));
                return;
            default:
                return;
        }
    }

    private void startDiamond() {
        String url = BooheeClient.build("status").getDefaultURL(String.format(ApiUrls
                .DIAMOND_CHECHIN, new Object[]{UserPreference.getToken(this.activity)}));
        if (!TextUtils.isEmpty(url)) {
            Intent browserIntent = new Intent(this.activity, BrowserActivity.class);
            browserIntent.putExtra("url", url);
            browserIntent.putExtra("title", getResources().getString(R.string.iz));
            startActivity(browserIntent);
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Helper.showLog(TAG, "requestCode:" + requestCode);
        switch (requestCode) {
            case 2:
                if (resultCode == -1) {
                    int position = data.getIntExtra(FriendShipActivity.FRIENDSHIP_POSITION, 0);
                    Helper.showLog(TAG, "position:" + position);
                    Post item = this.mAdapter.getItem(position);
                    item.comment_count++;
                    this.mAdapter.notifyDataSetChanged();
                    return;
                }
                return;
            default:
                return;
        }
    }

    public static void comeOnBaby(Context context) {
        if (context != null) {
            context.startActivity(new Intent(context, MyTimelineActivity.class));
        }
    }

    protected void onPause() {
        super.onPause();
        if (!PlayerManager.getInstance().isStartFullScreen()) {
            PlayerManager.getInstance().releaseAll();
        }
    }
}
