package com.boohee.one.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Selection;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import com.boohee.api.StatusApi;
import com.boohee.main.GestureActivity;
import com.boohee.model.status.Comment;
import com.boohee.one.R;
import com.boohee.one.http.JsonCallback;
import com.boohee.status.UserTimelineActivity;
import com.boohee.utility.BooheeScheme;
import com.boohee.utility.ImageLoaderOptions;
import com.boohee.utility.TimeLineUtility;
import com.boohee.utils.DateHelper;
import com.boohee.utils.DateKnife;
import com.boohee.utils.Helper;
import com.boohee.utils.Keyboard;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnLastItemVisibleListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONObject;

public class StoryCommentActivity extends GestureActivity {
    public static final String COMMENT_NAME = "comment_name";
    public static final String ID           = "id";
    @InjectView(2131427555)
    Button   commentBtn;
    @InjectView(2131427553)
    EditText commentEdit;
    private CommentListAdapter mAdapter;
    private List<Comment> mComments = new ArrayList();
    @InjectView(2131427552)
    PullToRefreshListView mPullRefreshListView;
    private String mStoryId;
    private int page = 1;

    private class CommentListAdapter extends BaseAdapter {
        private CommentListAdapter() {
        }

        public int getCount() {
            if (StoryCommentActivity.this.mComments == null) {
                return 0;
            }
            return StoryCommentActivity.this.mComments.size();
        }

        public Comment getItem(int position) {
            return (Comment) StoryCommentActivity.this.mComments.get(position);
        }

        public long getItemId(int position) {
            return (long) position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = LayoutInflater.from(StoryCommentActivity.this.ctx).inflate(R.layout
                        .ei, null);
                holder.iv_avatar = (ImageView) convertView.findViewById(R.id.iv_avatar);
                holder.tv_nickname = (TextView) convertView.findViewById(R.id.tv_nickname);
                holder.tv_post_time = (TextView) convertView.findViewById(R.id.tv_post_time);
                holder.tv_comment_text = (TextView) convertView.findViewById(R.id.tv_comment_text);
                holder.iv_comment = (ImageView) convertView.findViewById(R.id.iv_comment);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            final Comment comment = getItem(position);
            StoryCommentActivity.this.imageLoader.displayImage(comment.user.avatar_url, holder
                    .iv_avatar, ImageLoaderOptions.avatar());
            holder.tv_nickname.setText(comment.user.nickname);
            holder.tv_post_time.setText(DateKnife.display(new Date(), DateHelper.parseFromString
                    (comment.created_at, "yyyy-MM-dd'T'HH:mm:ss")));
            holder.tv_comment_text.setText(comment.body);
            TimeLineUtility.addLinks(holder.tv_comment_text);
            holder.iv_avatar.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    Intent intent = new Intent(StoryCommentActivity.this.ctx,
                            UserTimelineActivity.class);
                    intent.putExtra(UserTimelineActivity.NICK_NAME, comment.user.nickname);
                    StoryCommentActivity.this.startActivity(intent);
                }
            });
            holder.iv_comment.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    if ("".equals(StoryCommentActivity.this.commentEdit.getText().toString().trim
                            ())) {
                        StoryCommentActivity.this.commentEdit.setText("回复@" + comment.user
                                .nickname + ": ");
                    } else {
                        StoryCommentActivity.this.commentEdit.setText(StoryCommentActivity.this
                                .commentEdit.getText().toString() + "@" + comment.user.nickname +
                                " ");
                    }
                    StoryCommentActivity.this.setSelection();
                    Keyboard.open(StoryCommentActivity.this.activity, StoryCommentActivity.this
                            .commentEdit);
                }
            });
            return convertView;
        }
    }

    static class ViewHolder {
        public ImageView iv_avatar;
        public ImageView iv_comment;
        public TextView  tv_comment_text;
        public TextView  tv_nickname;
        public TextView  tv_post_time;

        ViewHolder() {
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.at);
        ButterKnife.inject((Activity) this);
        this.mStoryId = getStringExtra("id");
        if (!TextUtils.isEmpty(this.mStoryId)) {
            if (!TextUtils.isEmpty(getStringExtra(COMMENT_NAME))) {
                this.commentEdit.setText(getStringExtra(COMMENT_NAME));
                setSelection();
            }
            initView();
            firstLoad();
        }
    }

    public void firstLoad() {
        new Handler().postDelayed(new Runnable() {
            public void run() {
                StoryCommentActivity.this.mPullRefreshListView.setRefreshing();
            }
        }, 500);
    }

    @OnClick({2131427555})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_comment:
                String content = this.commentEdit.getText().toString().trim();
                if (TextUtils.isEmpty(content)) {
                    Helper.showToast((int) R.string.ex);
                    return;
                } else {
                    sendComment(content);
                    return;
                }
            default:
                return;
        }
    }

    private void initView() {
        this.mPullRefreshListView.setOnRefreshListener(new OnRefreshListener<ListView>() {
            public void onRefresh(PullToRefreshBase<ListView> pullToRefreshBase) {
                StoryCommentActivity.this.getComments();
            }
        });
        this.mPullRefreshListView.setOnLastItemVisibleListener(new OnLastItemVisibleListener() {
            public void onLastItemVisible() {
                StoryCommentActivity.this.getNextComments();
            }
        });
    }

    private void getComments() {
        this.page = 1;
        StatusApi.getStoryComment(this.mStoryId, this.page, this.activity, new JsonCallback(this
                .activity) {
            public void ok(JSONObject object) {
                super.ok(object);
                StoryCommentActivity.this.mComments = Comment.parseComments(object.optJSONArray
                        (BooheeScheme.COMMENTS));
                if (StoryCommentActivity.this.mComments != null && StoryCommentActivity.this
                        .mComments.size() > 0) {
                    StoryCommentActivity.this.mAdapter = new CommentListAdapter();
                    StoryCommentActivity.this.mPullRefreshListView.setAdapter
                            (StoryCommentActivity.this.mAdapter);
                    StoryCommentActivity.this.page = StoryCommentActivity.this.page + 1;
                }
            }

            public void onFinish() {
                super.onFinish();
                StoryCommentActivity.this.mPullRefreshListView.onRefreshComplete();
            }
        });
    }

    private void getNextComments() {
        StatusApi.getStoryComment(this.mStoryId, this.page, this.activity, new JsonCallback(this
                .activity) {
            public void ok(JSONObject object) {
                super.ok(object);
                List<Comment> list = Comment.parseComments(object.optJSONArray(BooheeScheme
                        .COMMENTS));
                if (list != null && list.size() > 0) {
                    StoryCommentActivity.this.mComments.addAll(list);
                    StoryCommentActivity.this.mAdapter.notifyDataSetChanged();
                    StoryCommentActivity.this.page = StoryCommentActivity.this.page + 1;
                }
            }
        });
    }

    private void sendComment(final String content) {
        showLoading();
        StatusApi.postStoryComment(this.mStoryId, content, this.activity, new JsonCallback(this
                .activity) {
            public void ok(JSONObject object) {
                super.ok(object);
                StoryCommentActivity.this.commentEdit.setText("");
                Keyboard.close(StoryCommentActivity.this.ctx, StoryCommentActivity.this
                        .commentEdit);
                StoryCommentActivity.this.getComments();
            }

            public void fail(String message) {
                super.fail(message);
                Helper.showToast((int) R.string.r6);
                StoryCommentActivity.this.commentEdit.setText(content);
            }

            public void onFinish() {
                super.onFinish();
                StoryCommentActivity.this.dismissLoading();
            }
        });
    }

    private void setSelection() {
        Selection.setSelection(this.commentEdit.getText(), this.commentEdit.length());
    }

    public static void comeOn(Context context, String id) {
        if (context != null && !TextUtils.isEmpty(id)) {
            Intent intent = new Intent(context, StoryCommentActivity.class);
            intent.putExtra("id", id);
            context.startActivity(intent);
        }
    }

    public static void comeOnWithComment(Context context, String id, String reply_name) {
        if (context != null && !TextUtils.isEmpty(id)) {
            Intent intent = new Intent(context, StoryCommentActivity.class);
            intent.putExtra("id", id);
            intent.putExtra(COMMENT_NAME, "回复@" + reply_name + ": ");
            context.startActivity(intent);
        }
    }
}
