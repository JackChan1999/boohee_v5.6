package com.boohee.status;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.boohee.api.StatusApi;
import com.boohee.main.GestureActivity;
import com.boohee.model.status.Post;
import com.boohee.model.status.StatusUser;
import com.boohee.one.R;
import com.boohee.one.http.JsonCallback;
import com.boohee.one.player.PlayerManager;
import com.boohee.one.ui.PullToRefreshHelper;
import com.boohee.one.ui.adapter.UserTimelineAdapter;
import com.boohee.status.ReportActivity.ReportType;
import com.boohee.utility.Const;
import com.boohee.utility.Event;
import com.boohee.utility.ImageLoaderOptions;
import com.boohee.utils.Helper;
import com.boohee.widgets.LightAlertDialog;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnLastItemVisibleListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.analytics.MobclickAgent;

import de.hdodenhof.circleimageview.CircleImageView;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

public class UserTimelineActivity extends GestureActivity {
    public static final String  NICK_NAME     = "nickname";
    static final        String  TAG           = UserTimelineActivity.class.getSimpleName();
    public static final String  USER_ID       = "user_id";
    private             boolean isLastVisible = false;
    private CircleImageView       iv_avatar;
    private UserTimelineAdapter   mAdapter;
    private ListView              mListView;
    private String                mNickname;
    private ArrayList<Post>       mPosts;
    private PullToRefreshListView mPullRefreshListView;
    private StatusUser            mUser;
    private String                mUserId;
    private ToggleButton          tb_follow;
    private TextView              tv_description;
    private TextView              tv_diamond_count;
    private TextView              tv_fans_count;
    private TextView              tv_friends_count;
    private TextView              tv_post_count;
    private TextView              tv_username;

    private class AvatarListener implements OnClickListener {
        private AvatarListener() {
        }

        public void onClick(View v) {
            Intent intent = new Intent(UserTimelineActivity.this.activity, LargeImageActivity
                    .class);
            if (!TextUtils.isEmpty(UserTimelineActivity.this.mUser.avatar_url)) {
                if (UserTimelineActivity.this.mUser.avatar_url.contains("?")) {
                    intent.putExtra("image_url", UserTimelineActivity.this.mUser.avatar_url
                            .substring(0, UserTimelineActivity.this.mUser.avatar_url.indexOf("?")));
                    UserTimelineActivity.this.activity.startActivity(intent);
                    return;
                }
                intent.putExtra("image_url", UserTimelineActivity.this.mUser.avatar_url);
                UserTimelineActivity.this.activity.startActivity(intent);
            }
        }
    }

    private class FollowListener implements OnClickListener {
        private FollowListener() {
        }

        public void onClick(View v) {
            if (UserTimelineActivity.this.mUser != null) {
                if (UserTimelineActivity.this.mUser.following) {
                    StatusApi.deleteFriendships(UserTimelineActivity.this.activity,
                            UserTimelineActivity.this.mUser.id, new JsonCallback
                                    (UserTimelineActivity.this.activity) {
                        public void ok(JSONObject object) {
                            super.ok(object);
                            UserTimelineActivity.this.mUser.following = false;
                            UserTimelineActivity.this.tb_follow.setChecked(false);
                            TextView access$1200 = UserTimelineActivity.this.tv_fans_count;
                            UserTimelineActivity.this.mUser;
                            StatusUser access$800 = UserTimelineActivity.this.mUser;
                            int i = access$800.follower_count - 1;
                            access$800.follower_count = i;
                            access$1200.setText(StatusUser.displayCount(i));
                        }
                    });
                } else {
                    StatusApi.createFriendships(UserTimelineActivity.this.activity,
                            UserTimelineActivity.this.mUser.id, new JsonCallback
                                    (UserTimelineActivity.this.activity) {
                        public void ok(JSONObject object) {
                            super.ok(object);
                            UserTimelineActivity.this.mUser.following = true;
                            UserTimelineActivity.this.tb_follow.setChecked(true);
                            TextView access$1200 = UserTimelineActivity.this.tv_fans_count;
                            UserTimelineActivity.this.mUser;
                            StatusUser access$800 = UserTimelineActivity.this.mUser;
                            int i = access$800.follower_count + 1;
                            access$800.follower_count = i;
                            access$1200.setText(StatusUser.displayCount(i));
                        }
                    });
                }
            }
        }
    }

    public void onCreate(Bundle outState) {
        super.onCreate(outState);
        setContentView(R.layout.ds);
        MobclickAgent.onEvent(this, Event.STATUS_VIEW_USER_PAGE);
        setTitle(R.string.abr);
        handleIntent();
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
                UserTimelineActivity.this.getCurrentStatus();
            }
        });
        this.mPullRefreshListView.setOnLastItemVisibleListener(new OnLastItemVisibleListener() {
            public void onLastItemVisible() {
                if (!UserTimelineActivity.this.isLastVisible) {
                    UserTimelineActivity.this.getPreviousStatus();
                }
            }
        });
    }

    private View getHeaderView() {
        View headerView = View.inflate(this.ctx, R.layout.h7, null);
        this.tv_username = (TextView) headerView.findViewById(R.id.tv_username);
        this.tv_description = (TextView) headerView.findViewById(R.id.tv_description);
        this.iv_avatar = (CircleImageView) headerView.findViewById(R.id.iv_avatar);
        this.tv_post_count = (TextView) headerView.findViewById(R.id.tv_post_count);
        this.tv_diamond_count = (TextView) headerView.findViewById(R.id.tv_diamond_count);
        this.tv_friends_count = (TextView) headerView.findViewById(R.id.tv_friends_count);
        this.tv_fans_count = (TextView) headerView.findViewById(R.id.tv_fans_count);
        this.tb_follow = (ToggleButton) headerView.findViewById(R.id.tb_follow);
        return headerView;
    }

    private void handleIntent() {
        this.mNickname = getStringExtra(NICK_NAME);
        this.mUserId = getStringExtra("user_id");
    }

    private void getCurrentStatus() {
        if (TextUtils.isEmpty(this.mNickname)) {
            StatusApi.getUserTimelineById(this.activity, this.mUserId, new JsonCallback(this
                    .activity) {
                public void ok(JSONObject object) {
                    super.ok(object);
                    UserTimelineActivity.this.refreshCurrentData(object);
                }

                public void onFinish() {
                    super.onFinish();
                    UserTimelineActivity.this.mPullRefreshListView.onRefreshComplete();
                }
            });
        } else {
            StatusApi.getUserTimelineByNickName(this.activity, this.mNickname, new JsonCallback
                    (this.activity) {
                public void ok(JSONObject object) {
                    super.ok(object);
                    UserTimelineActivity.this.refreshCurrentData(object);
                }

                public void onFinish() {
                    super.onFinish();
                    UserTimelineActivity.this.mPullRefreshListView.onRefreshComplete();
                }
            });
        }
    }

    private void refreshCurrentData(JSONObject object) {
        try {
            initHeader(object);
            this.mPosts = Post.removeDisablePost(Post.parsePosts(object.getJSONArray("posts")
                    .toString()));
            this.mAdapter = new UserTimelineAdapter(this, this.mPosts, this.mUser);
            this.mListView.setAdapter(this.mAdapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void getPreviousStatus() {
        if (this.mPosts != null && this.mPosts.size() != 0) {
            this.isLastVisible = true;
            if (TextUtils.isEmpty(this.mNickname)) {
                StatusApi.getUserPreviousTimelineById(this.activity, this.mUserId, ((Post) this
                        .mPosts.get(this.mPosts.size() - 1)).id, new JsonCallback(this.activity) {
                    public void ok(JSONObject object) {
                        super.ok(object);
                        UserTimelineActivity.this.refreshPreviousData(object);
                    }
                });
            } else {
                StatusApi.getUserPreviousTimelineByNickName(this.activity, this.mNickname, (
                        (Post) this.mPosts.get(this.mPosts.size() - 1)).id, new JsonCallback(this
                        .activity) {
                    public void ok(JSONObject object) {
                        super.ok(object);
                        UserTimelineActivity.this.refreshPreviousData(object);
                    }
                });
            }
        }
    }

    private void refreshPreviousData(JSONObject object) {
        try {
            ArrayList<Post> morePosts = Post.removeDisablePost(Post.parsePosts(object
                    .getJSONArray("posts").toString()));
            if (morePosts != null) {
                this.mPosts.addAll(morePosts);
                this.mAdapter.notifyDataSetChanged();
                this.isLastVisible = false;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void initHeader(JSONObject object) {
        this.mUser = StatusUser.parseUser(object.optJSONObject(Const.USER));
        if (this.mUser != null) {
            if (!TextUtils.isEmpty(this.mUser.avatar_url)) {
                ImageLoader.getInstance().displayImage(this.mUser.avatar_url, this.iv_avatar,
                        ImageLoaderOptions.global((int) R.drawable.aa0));
            }
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
            this.tb_follow.setChecked(this.mUser.following);
            this.tb_follow.setOnClickListener(new FollowListener());
            this.iv_avatar.setOnClickListener(new AvatarListener());
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.a2, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_black:
                showBlock();
                return true;
            case R.id.action_report:
                if (this.mUser == null) {
                    return true;
                }
                Intent intent = new Intent(this, ReportActivity.class);
                intent.putExtra(ReportActivity.EXTRA_ID, this.mUser.id);
                intent.putExtra(ReportActivity.EXTRA_TYPE, ReportType.User.toString());
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void showBlock() {
        LightAlertDialog.create(this.activity, "确定把TA拉黑？", "TA将在你的世界里永远消失...").setNegativeButton(
                (CharSequence) "不了", null).setPositiveButton((CharSequence) "一定以及肯定", new
                DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                if (UserTimelineActivity.this.mUser != null) {
                    StatusApi.createBlocks(UserTimelineActivity.this.activity,
                            UserTimelineActivity.this.mUser.id, new JsonCallback
                                    (UserTimelineActivity.this.activity) {
                        public void ok(JSONObject object) {
                            super.ok(object);
                            Helper.showToast((CharSequence) "拉黑成功");
                        }
                    });
                }
            }
        }).show();
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

    public static void comeOnBaby(Context context, String nickName) {
        if (context != null && !TextUtils.isEmpty(nickName)) {
            Intent intent = new Intent(context, UserTimelineActivity.class);
            intent.putExtra(NICK_NAME, nickName);
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
