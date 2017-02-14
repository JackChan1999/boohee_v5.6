package com.boohee.status;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.text.Selection;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import cn.dreamtobe.kpswitch.util.KPSwitchConflictUtil;
import cn.dreamtobe.kpswitch.util.KeyboardUtil;
import cn.dreamtobe.kpswitch.widget.KPSwitchPanelLinearLayout;

import com.boohee.api.StatusApi;
import com.boohee.main.GestureActivity;
import com.boohee.model.ApiError;
import com.boohee.model.status.Comment;
import com.boohee.model.status.Mention;
import com.boohee.model.status.Post;
import com.boohee.myview.NineGridLayout;
import com.boohee.myview.NineGridLayout.OnItemClickListerner;
import com.boohee.one.R;
import com.boohee.one.http.JsonCallback;
import com.boohee.one.player.ExVideoView;
import com.boohee.one.player.PlayerManager;
import com.boohee.one.ui.NineGridGalleryActivity;
import com.boohee.utility.BuilderIntent;
import com.boohee.utility.DensityUtil;
import com.boohee.utility.Event;
import com.boohee.utility.ImageLoaderOptions;
import com.boohee.utility.TimeLineUtility;
import com.boohee.utility.TimeLineUtility.PraiseClickListener;
import com.boohee.utility.TimeLineUtility.SimplePostMenuListener;
import com.boohee.utils.DateHelper;
import com.boohee.utils.DateKnife;
import com.boohee.utils.Helper;
import com.boohee.utils.Keyboard;
import com.chaowen.commentlibrary.emoji.EmojiViewPagerAdapter;
import com.chaowen.commentlibrary.emoji.EmojiViewPagerAdapter.OnClickEmojiListener;
import com.chaowen.commentlibrary.emoji.Emojicon;
import com.chaowen.commentlibrary.emoji.EmojiconEditText;
import com.chaowen.commentlibrary.emoji.People;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnLastItemVisibleListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.umeng.analytics.MobclickAgent;
import com.viewpagerindicator.CirclePageIndicator;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONObject;

public class CommentListActivity extends GestureActivity implements OnClickListener,
        OnClickEmojiListener {
    public static final String COMMENT_PREFIX = "comment_prefix";
    public static final String NEED_SCROLL    = "need_scroll";
    public static final String POST_ID        = "post_id";
    static final        String TAG            = CommentListActivity.class.getSimpleName();
    public static final String USER_ID        = "user_id";
    private LinearLayout attachmentLayout;
    private TextView     bottomText;
    @InjectView(2131427554)
    ImageButton btnEmoji;
    private CheckBox         cb_praise;
    private EmojiconEditText commentEdit;
    private View             headerView;
    @InjectView(2131427558)
    CirclePageIndicator indicatorEmoji;
    private ImageView    ivAttachment;
    private LinearLayout ll_comment;
    private LinearLayout ll_menu;
    @InjectView(2131427556)
    KPSwitchPanelLinearLayout lyEmoji;
    private CommentListAdapter    mAdapter;
    private ImageView             mAvatarImage;
    private ArrayList<Comment>    mComments;
    private EmojiViewPagerAdapter mEmojiPagerAdapter;
    private Handler mHandler = new Handler();
    private ListView              mListView;
    private TextView              mNameText;
    private Post                  mPost;
    private TextView              mPostBody;
    private NineGridLayout        mPostImage;
    private PullToRefreshListView mPullRefreshListView;
    private TextView              mTimeText;
    private boolean               needScroll;
    private int page = 1;
    private int            postId;
    private String         prefix;
    private RelativeLayout rl_praise;
    private LinearLayout   topLayout;
    private TextView       tvAttachment;
    private TextView       tv_comment;
    private TextView       tv_praise_plus;
    private ExVideoView    videoView;
    @InjectView(2131427557)
    ViewPager viewPagerEmoji;

    private class CommentListAdapter extends BaseAdapter {
        private CommentListAdapter() {
        }

        public int getCount() {
            if (CommentListActivity.this.mComments == null) {
                return 0;
            }
            return CommentListActivity.this.mComments.size();
        }

        public Comment getItem(int position) {
            return (Comment) CommentListActivity.this.mComments.get(position);
        }

        public long getItemId(int position) {
            return (long) position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = LayoutInflater.from(CommentListActivity.this.ctx).inflate(R.layout
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
            CommentListActivity.this.imageLoader.displayImage(comment.user.avatar_url, holder
                    .iv_avatar, ImageLoaderOptions.avatar());
            holder.tv_nickname.setText(comment.user.nickname);
            holder.tv_post_time.setText(DateKnife.display(new Date(), DateHelper.parseFromString
                    (comment.created_at, "yyyy-MM-dd'T'HH:mm:ss")));
            holder.tv_comment_text.setText(comment.body);
            TimeLineUtility.addLinks(holder.tv_comment_text);
            holder.iv_avatar.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    Intent intent = new Intent(CommentListActivity.this.ctx, UserTimelineActivity
                            .class);
                    intent.putExtra(UserTimelineActivity.NICK_NAME, comment.user.nickname);
                    CommentListActivity.this.startActivity(intent);
                }
            });
            holder.iv_comment.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    if ("".equals(CommentListActivity.this.commentEdit.getText().toString().trim
                            ())) {
                        CommentListActivity.this.commentEdit.setText("回复@" + comment.user
                                .nickname + " ：");
                    } else {
                        CommentListActivity.this.commentEdit.setText(CommentListActivity.this
                                .commentEdit.getText().toString() + "@" + comment.user.nickname +
                                " ");
                    }
                    CommentListActivity.this.setSelection();
                    Keyboard.openImplicit(CommentListActivity.this.activity, CommentListActivity
                            .this.commentEdit);
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

    public static void startActivity(Context context, int postId, String content, boolean
            needScroll) {
        Intent intent = new Intent(context, CommentListActivity.class);
        intent.putExtra(POST_ID, postId);
        intent.putExtra(COMMENT_PREFIX, content);
        intent.putExtra(NEED_SCROLL, needScroll);
        context.startActivity(intent);
    }

    public void onCreate(Bundle outState) {
        super.onCreate(outState);
        setContentView(R.layout.at);
        ButterKnife.inject((Activity) this);
        setTitle(R.string.ga);
        MobclickAgent.onEvent(this.ctx, Event.STATUS_VIEW_COMMENT_PAGE);
        this.postId = getIntExtra(POST_ID);
        this.prefix = getIntent().getStringExtra(COMMENT_PREFIX);
        this.needScroll = getIntent().getBooleanExtra(NEED_SCROLL, false);
        init();
        getComments();
    }

    public void firstLoad() {
        this.mHandler.postDelayed(new Runnable() {
            public void run() {
                CommentListActivity.this.mPullRefreshListView.setRefreshing();
            }
        }, 500);
    }

    private void init() {
        showLoading();
        this.mPullRefreshListView = (PullToRefreshListView) findViewById(R.id.listview);
        this.mListView = (ListView) this.mPullRefreshListView.getRefreshableView();
        this.headerView = View.inflate(this, R.layout.eh, null);
        this.mListView.addHeaderView(this.headerView);
        this.topLayout = (LinearLayout) this.headerView.findViewById(R.id.ll_top);
        this.mAvatarImage = (ImageView) this.headerView.findViewById(R.id.iv_avatar);
        this.mNameText = (TextView) this.headerView.findViewById(R.id.tv_nickname);
        this.mTimeText = (TextView) this.headerView.findViewById(R.id.tv_post_time);
        this.mPostBody = (TextView) this.headerView.findViewById(R.id.tv_body);
        this.mPostImage = (NineGridLayout) this.headerView.findViewById(R.id.iv_post_grid);
        this.bottomText = (TextView) this.headerView.findViewById(R.id.tv_bottom);
        this.attachmentLayout = (LinearLayout) this.headerView.findViewById(R.id.attachment_layout);
        this.ivAttachment = (ImageView) this.headerView.findViewById(R.id.iv_attachment);
        this.tvAttachment = (TextView) this.headerView.findViewById(R.id.tv_attachment);
        this.videoView = (ExVideoView) this.headerView.findViewById(R.id.video);
        this.tv_comment = (TextView) this.headerView.findViewById(R.id.tv_comment);
        this.cb_praise = (CheckBox) this.headerView.findViewById(R.id.cb_praise);
        this.tv_praise_plus = (TextView) this.headerView.findViewById(R.id.tv_praise_plus);
        this.rl_praise = (RelativeLayout) this.headerView.findViewById(R.id.rl_praise);
        this.ll_comment = (LinearLayout) this.headerView.findViewById(R.id.ll_comment);
        this.ll_menu = (LinearLayout) this.headerView.findViewById(R.id.ll_menu);
        this.commentEdit = (EmojiconEditText) findViewById(R.id.et_comment);
        this.commentEdit.setText(this.prefix);
        setSelection();
        this.mListView.setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                Keyboard.close(CommentListActivity.this.ctx, CommentListActivity.this.commentEdit);
                return false;
            }
        });
        this.mPullRefreshListView.setOnRefreshListener(new OnRefreshListener<ListView>() {
            public void onRefresh(PullToRefreshBase<ListView> pullToRefreshBase) {
                CommentListActivity.this.getComments();
            }
        });
        this.mPullRefreshListView.setOnLastItemVisibleListener(new OnLastItemVisibleListener() {
            public void onLastItemVisible() {
                CommentListActivity.this.getNextComments();
            }
        });
        if (!TextUtils.isEmpty(this.prefix)) {
            this.mHandler.postDelayed(new Runnable() {
                public void run() {
                    Keyboard.openImplicit(CommentListActivity.this, CommentListActivity.this
                            .commentEdit);
                }
            }, 300);
        }
        initEmoji();
    }

    private void initEmoji() {
        KeyboardUtil.attach(this, this.lyEmoji);
        KPSwitchConflictUtil.attach(this.lyEmoji, this.btnEmoji, this.commentEdit);
        Emojicon[] emojis = People.DATA;
        List<List<Emojicon>> pagers = new ArrayList();
        List<Emojicon> es = null;
        int size = 0;
        boolean justAdd = false;
        for (Emojicon ej : emojis) {
            if (size == 0) {
                es = new ArrayList();
            }
            if (size == 27) {
                es.add(new Emojicon(""));
            } else {
                es.add(ej);
            }
            size++;
            if (size == 28) {
                pagers.add(es);
                size = 0;
                justAdd = true;
            } else {
                justAdd = false;
            }
        }
        if (!(justAdd || es == null)) {
            int exSize = 28 - es.size();
            for (int i = 0; i < exSize; i++) {
                es.add(new Emojicon(""));
            }
            pagers.add(es);
        }
        this.mEmojiPagerAdapter = new EmojiViewPagerAdapter(this.ctx, pagers, DensityUtil.dip2px
                (this.ctx, 160.0f) / 4, this);
        this.viewPagerEmoji.setAdapter(this.mEmojiPagerAdapter);
        this.indicatorEmoji.setViewPager(this.viewPagerEmoji);
    }

    private void setSelection() {
        Selection.setSelection(this.commentEdit.getText(), this.commentEdit.length());
    }

    private void getComments() {
        this.page = 1;
        StatusApi.getComments(this.activity, this.postId, this.page, new JsonCallback(this
                .activity) {
            public void ok(JSONObject object) {
                super.ok(object);
                CommentListActivity.this.mPost = Post.parseSelf(object.optJSONObject(Mention.POST));
                if (!(CommentListActivity.this.mPost == null || CommentListActivity.this.mPost
                        .comments == null)) {
                    CommentListActivity.this.initHeader();
                    CommentListActivity.this.mComments = CommentListActivity.this.mPost.comments;
                    CommentListActivity.this.mAdapter = new CommentListAdapter();
                    CommentListActivity.this.mListView.setAdapter(CommentListActivity.this
                            .mAdapter);
                    if (CommentListActivity.this.mComments.size() == 0) {
                        CommentListActivity.this.bottomText.setText(R.string.xn);
                    } else {
                        CommentListActivity.this.bottomText.setText("共有 " + CommentListActivity
                                .this.mPost.comment_count + " 条评论");
                    }
                }
                CommentListActivity.this.page = CommentListActivity.this.page + 1;
                if (CommentListActivity.this.needScroll) {
                    CommentListActivity.this.mListView.setSelection(CommentListActivity.this
                            .mListView.getHeaderViewsCount());
                    CommentListActivity.this.needScroll = false;
                }
                CommentListActivity.this.hideEmojiPanel();
            }

            public void onFinish() {
                super.onFinish();
                CommentListActivity.this.dismissLoading();
                CommentListActivity.this.mPullRefreshListView.onRefreshComplete();
            }
        });
    }

    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getAction() == 1 && event.getKeyCode() == 4 && hideEmojiPanel()) {
            return true;
        }
        return super.dispatchKeyEvent(event);
    }

    private boolean hideEmojiPanel() {
        if (this.lyEmoji.getVisibility() != 0) {
            return false;
        }
        KPSwitchConflictUtil.hidePanelAndKeyboard(this.lyEmoji);
        return true;
    }

    private void initHeader() {
        if (this.mPost.user == null) {
            Helper.showToast((CharSequence) "数据加载失败，请下拉刷新~~");
            return;
        }
        boolean z;
        this.imageLoader.displayImage(this.mPost.user.avatar_url, this.mAvatarImage,
                ImageLoaderOptions.avatar());
        this.mNameText.setText(this.mPost.user.nickname);
        this.mTimeText.setText(DateKnife.display(new Date(), DateHelper.parseFromString(this
                .mPost.created_at, "yyyy-MM-dd'T'HH:mm:ss")));
        this.topLayout.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                new BuilderIntent(CommentListActivity.this.ctx, UserTimelineActivity.class)
                        .putExtra(UserTimelineActivity.NICK_NAME, CommentListActivity.this.mPost
                                .user.nickname).startActivity();
            }
        });
        this.mPostBody.setText(this.mPost.body);
        this.mPostBody.setOnLongClickListener(new OnLongClickListener() {
            public boolean onLongClick(View view) {
                TimeLineUtility.copyText(CommentListActivity.this.activity, CommentListActivity
                        .this.mPost.body);
                Helper.showToast(CommentListActivity.this.activity, (CharSequence) "内容已复制到剪切板");
                return true;
            }
        });
        TimeLineUtility.addLinks(this.mPostBody);
        if (this.mPost.photos == null || this.mPost.photos.size() <= 0) {
            this.mPostImage.setVisibility(8);
        } else {
            this.mPostImage.setVisibility(0);
            this.mPostImage.setImagesData(this.mPost.photos);
            this.mPostImage.setOnItemClickListerner(new OnItemClickListerner() {
                public void onItemClick(View view, int position) {
                    NineGridGalleryActivity.comeOn(CommentListActivity.this.activity,
                            CommentListActivity.this.mPost.photos, position);
                }
            });
        }
        this.rl_praise.setOnClickListener(new PraiseClickListener(this.activity, this.mPost, this
                .rl_praise, this.cb_praise, this.tv_praise_plus));
        this.cb_praise.setText(String.valueOf(this.mPost.envious_count));
        CheckBox checkBox = this.cb_praise;
        if (TextUtils.isEmpty(this.mPost.feedback)) {
            z = false;
        } else {
            z = true;
        }
        checkBox.setChecked(z);
        this.tv_comment.setText(this.mPost.comment_count + "");
        this.ll_comment.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                Keyboard.openImplicit(CommentListActivity.this.ctx, CommentListActivity.this
                        .commentEdit);
            }
        });
        initMenu(this.ll_menu, this.mPost);
        TimeLineUtility.initAttachment(this.ctx, this.attachmentLayout, this.tvAttachment, this
                .ivAttachment, this.videoView, this.mPost);
    }

    private void initMenu(View menuBtn, final Post post) {
        menuBtn.setVisibility(0);
        menuBtn.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                TimeLineUtility.showCommentPopView(CommentListActivity.this.activity, v, post,
                        null, new SimplePostMenuListener() {
                    public void onPostDelete() {
                        super.onPostDelete();
                        CommentListActivity.this.finish();
                    }
                });
            }
        });
    }

    private void getNextComments() {
        StatusApi.getComments(this.activity, this.postId, this.page, new JsonCallback(this
                .activity) {
            public void ok(JSONObject object) {
                super.ok(object);
                Post post = Post.parseSelf(object.optJSONObject(Mention.POST));
                if (post != null && post.comments != null && post.comments.size() > 0) {
                    CommentListActivity.this.mComments.addAll(post.comments);
                    CommentListActivity.this.mAdapter.notifyDataSetChanged();
                    CommentListActivity.this.page = CommentListActivity.this.page + 1;
                }
            }
        });
    }

    @OnClick({2131427555, 2131427554})
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

    public void onEmojiClick(Emojicon emoji) {
        input(emoji);
    }

    public void onDelete() {
        backspace();
    }

    private void input(Emojicon emojicon) {
        if (this.commentEdit != null && emojicon != null) {
            int start = this.commentEdit.getSelectionStart();
            int end = this.commentEdit.getSelectionEnd();
            if (start < 0) {
                this.commentEdit.append(emojicon.getEmoji());
            } else {
                this.commentEdit.getText().replace(Math.min(start, end), Math.max(start, end),
                        emojicon.getEmoji(), 0, emojicon.getEmoji().length());
            }
        }
    }

    public void backspace() {
        this.commentEdit.dispatchKeyEvent(new KeyEvent(0, 0, 0, 67, 0, 0, 0, 0, 6));
    }

    private void sendComment(final String content) {
        showLoading();
        StatusApi.sendComments(this.activity, this.postId, content, new JsonCallback(this
                .activity) {
            public void ok(JSONObject object) {
                super.ok(object);
                CommentListActivity.this.commentEdit.setText("");
                MobclickAgent.onEvent(CommentListActivity.this.ctx, Event.STATUS_ADD_COMMENT_OK);
                MobclickAgent.onEvent(CommentListActivity.this.ctx, Event.MINE_ALL_RECORD_OK);
                MobclickAgent.onEvent(CommentListActivity.this.ctx, Event.STATUS_ADD_INTERACT_OK);
                Keyboard.close(CommentListActivity.this.ctx, CommentListActivity.this.commentEdit);
                CommentListActivity.this.needScroll = true;
                CommentListActivity.this.getComments();
            }

            public void fail(String message) {
                super.fail(message);
                Helper.showToast((int) R.string.r6);
                CommentListActivity.this.commentEdit.setText(content);
            }

            public void ok(JSONObject object, boolean hasError) {
                if (hasError) {
                    Helper.showToast(ApiError.getErrorMessage(object));
                }
            }

            public void onFinish() {
                super.onFinish();
                CommentListActivity.this.dismissLoading();
            }
        });
    }

    protected void onPause() {
        super.onPause();
        if (!PlayerManager.getInstance().isStartFullScreen()) {
            PlayerManager.getInstance().releaseAll();
        }
    }

    public void finish() {
        super.finish();
        Keyboard.close(this, this.commentEdit);
    }
}
