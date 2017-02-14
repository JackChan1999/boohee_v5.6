package com.boohee.one.ui.adapter;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.boohee.model.status.Post;
import com.boohee.model.status.StatusUser;
import com.boohee.myview.NineGridLayout;
import com.boohee.one.R;
import com.boohee.one.player.ExVideoView;
import com.boohee.status.MyTimelineActivity;
import com.boohee.status.UserTimelineActivity;
import com.boohee.utility.ImageLoaderOptions;
import com.boohee.utility.TimeLineUtility;
import com.boohee.utility.TimeLineUtility.PraiseClickListener;

import java.util.ArrayList;

public class UserTimelineAdapter extends BaseTimelineAdapter {
    static final String TAG = UserTimelineAdapter.class.getSimpleName();

    private class AvatarClickListener implements OnClickListener {
        private Post post;

        public AvatarClickListener(Post post) {
            this.post = post;
        }

        public void onClick(View v) {
            if (!this.post.own || this.post.repost) {
                Intent intent = new Intent(UserTimelineAdapter.this.activity,
                        UserTimelineActivity.class);
                if (this.post.user != null) {
                    intent.putExtra(UserTimelineActivity.NICK_NAME, this.post.user.nickname);
                } else {
                    intent.putExtra(UserTimelineActivity.NICK_NAME, UserTimelineAdapter.this.user
                            .nickname);
                }
                UserTimelineAdapter.this.activity.startActivity(intent);
                return;
            }
            UserTimelineAdapter.this.activity.startActivity(new Intent(UserTimelineAdapter.this
                    .activity, MyTimelineActivity.class));
        }
    }

    private class Holder {
        public LinearLayout   attachmentLayout;
        public ImageView      avatarImage;
        public TextView       body;
        public CheckBox       cb_praise;
        public ImageView      ivAttachment;
        public ProgressBar    lightProgress;
        public View           ll_bottom;
        public View           ll_comment;
        public View           ll_menu;
        public ImageView      markImage;
        public TextView       nickName;
        public NineGridLayout nineGridLayout;
        public ImageView      officialImage;
        public TextView       postTime;
        public TextView       repostUser;
        public View           rl_praise;
        public TextView       tvAttachment;
        public TextView       tv_comment;
        public TextView       tv_praise_plus;
        public ExVideoView    video;

        public Holder(View view) {
            this.lightProgress = (ProgressBar) view.findViewById(R.id.pb_light);
            this.avatarImage = (ImageView) view.findViewById(R.id.avatar);
            this.officialImage = (ImageView) view.findViewById(R.id.iv_official);
            this.markImage = (ImageView) view.findViewById(R.id.iv_mark);
            this.nickName = (TextView) view.findViewById(R.id.nickname);
            this.postTime = (TextView) view.findViewById(R.id.post_time);
            this.body = (TextView) view.findViewById(R.id.body);
            this.repostUser = (TextView) view.findViewById(R.id.tv_reposted_user);
            this.tv_comment = (TextView) view.findViewById(R.id.tv_comment);
            this.nineGridLayout = (NineGridLayout) view.findViewById(R.id.iv_post_grid);
            this.cb_praise = (CheckBox) view.findViewById(R.id.cb_praise);
            this.tv_praise_plus = (TextView) view.findViewById(R.id.tv_praise_plus);
            this.rl_praise = view.findViewById(R.id.rl_praise);
            this.ll_comment = view.findViewById(R.id.ll_comment);
            this.ll_menu = view.findViewById(R.id.ll_menu);
            this.ll_bottom = view.findViewById(R.id.ll_bottom);
            this.attachmentLayout = (LinearLayout) view.findViewById(R.id.attachment_layout);
            this.ivAttachment = (ImageView) view.findViewById(R.id.iv_attachment);
            this.tvAttachment = (TextView) view.findViewById(R.id.tv_attachment);
            this.video = (ExVideoView) view.findViewById(R.id.video);
        }
    }

    public UserTimelineAdapter(Activity activity, ArrayList<Post> posts, StatusUser user) {
        super(activity, posts);
        this.user = user;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder;
        if (convertView == null) {
            convertView = View.inflate(this.activity, R.layout.n0, null);
            holder = new Holder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        Post post = getItem(position);
        initAvatar(holder.avatarImage, post);
        initAvatarTag(holder.lightProgress, holder.officialImage, post);
        initTimeAndBody(holder.postTime, holder.body, post);
        initPostImage(holder.nineGridLayout, post.photos);
        initMarkImage(holder.markImage, post);
        initAttachment(holder.attachmentLayout, holder.tvAttachment, holder.ivAttachment, holder
                .video, post);
        if (post.disabled) {
            holder.ll_bottom.setVisibility(8);
            holder.repostUser.setVisibility(8);
        } else {
            int color;
            holder.ll_bottom.setVisibility(0);
            holder.repostUser.setVisibility(0);
            holder.rl_praise.setOnClickListener(new PraiseClickListener(this.activity, post,
                    holder.rl_praise, holder.cb_praise, holder.tv_praise_plus));
            holder.tv_comment.setText(post.comment_count + "");
            holder.cb_praise.setText(post.envious_count + "");
            holder.cb_praise.setChecked(!TextUtils.isEmpty(post.feedback));
            CheckBox checkBox = holder.cb_praise;
            if (TextUtils.isEmpty(post.feedback)) {
                color = this.activity.getResources().getColor(R.color.du);
            } else {
                color = this.activity.getResources().getColor(R.color. do);
            }
            checkBox.setTextColor(color);
            TimeLineUtility.initCommentButton(this.activity, holder.ll_comment, holder
                    .tv_comment, post, position);
            initMenu(holder.ll_menu, position);
            initRepostUser(holder.nickName, holder.repostUser, post);
        }
        return convertView;
    }

    private void initMenu(View menuBtn, final int position) {
        menuBtn.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                UserTimelineAdapter.this.showPopupMenu(v, position);
            }
        });
    }

    private void initAvatar(ImageView avatar, Post post) {
        String avatar_url = this.user.avatar_url;
        if (post.user != null) {
            avatar_url = post.user.avatar_url;
        }
        if (TextUtils.isEmpty(avatar_url)) {
            avatar.setBackgroundResource(R.drawable.aa0);
        } else {
            this.imageLoader.displayImage(avatar_url, avatar, ImageLoaderOptions.avatar());
        }
        avatar.setOnClickListener(new AvatarClickListener(post));
    }

    private void initAvatarTag(ProgressBar lightProgress, ImageView officialImage, Post post) {
        if (this.user.light) {
            lightProgress.setVisibility(0);
        } else {
            lightProgress.setVisibility(4);
        }
        int drawableRes = this.user.getAvatarTagResource();
        if (drawableRes != 0) {
            officialImage.setImageResource(drawableRes);
            officialImage.setVisibility(0);
            return;
        }
        officialImage.setVisibility(8);
    }

    private void initRepostUser(TextView nickName, TextView repostUser, Post post) {
        if (post.repost) {
            nickName.setText(post.original_post_user);
            repostUser.setText("推荐内容");
            repostUser.setVisibility(0);
            return;
        }
        nickName.setText(this.user.nickname);
        repostUser.setVisibility(8);
    }

    private void initMarkImage(ImageView markImage, Post post) {
        if (post.repost) {
            markImage.setImageResource(R.drawable.a3f);
            markImage.setVisibility(0);
        } else if (post.isPrivate) {
            markImage.setImageResource(R.drawable.a3e);
            markImage.setVisibility(0);
        } else {
            markImage.setVisibility(4);
        }
    }
}
