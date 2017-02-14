package com.boohee.status;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.boohee.model.status.CommentNotification;
import com.boohee.model.status.FeedbackNotification;
import com.boohee.model.status.FriendshipNotification;
import com.boohee.model.status.Mention;
import com.boohee.model.status.MentionNotification;
import com.boohee.model.status.Notification;
import com.boohee.model.status.RepostNotification;
import com.boohee.model.status.StoryCommentNotification;
import com.boohee.model.status.StoryMentionNotification;
import com.boohee.one.R;
import com.boohee.one.ui.StoryCommentActivity;
import com.boohee.utility.ImageLoaderOptions;
import com.boohee.utils.DateHelper;
import com.boohee.utils.DateKnife;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.Date;

public class NotificationAdapter extends BaseAdapter {
    static final String TAG = NotificationAdapter.class.getSimpleName();
    private Context mContext;
    private ImageLoader mImageLoader = ImageLoader.getInstance();
    private ArrayList<Notification> mNotifications;
    private int                     mUserId;

    static class ViewHolder {
        public ImageView    avatarImage;
        public LinearLayout contentLayout;
        int flag = -1;
        public ImageView hasReadImage;
        public TextView  nickName;
        public TextView  postTime;

        ViewHolder() {
        }
    }

    public NotificationAdapter(Context context, ArrayList<Notification> notifications, int
            user_id) {
        this.mContext = context;
        this.mNotifications = notifications;
        this.mUserId = user_id;
    }

    public int getCount() {
        if (this.mNotifications != null) {
            return this.mNotifications.size();
        }
        return 0;
    }

    public Notification getItem(int position) {
        return (Notification) this.mNotifications.get(position);
    }

    public long getItemId(int position) {
        return (long) position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        Notification notification = getItem(position);
        if (convertView == null || ((ViewHolder) convertView.getTag()).flag != position) {
            ViewHolder holder = new ViewHolder();
            holder.flag = position;
            convertView = LayoutInflater.from(this.mContext).inflate(R.layout.lh, null);
            holder.hasReadImage = (ImageView) convertView.findViewById(R.id.has_read);
            if (notification.read) {
                holder.hasReadImage.setVisibility(4);
            } else {
                holder.hasReadImage.setVisibility(0);
            }
            holder.postTime = (TextView) convertView.findViewById(R.id.post_time);
            holder.postTime.setText(DateKnife.display(new Date(), DateHelper.parseFromString
                    (notification.created_at, "yyyy-MM-dd'T'HH:mm:ss")));
            holder.avatarImage = (ImageView) convertView.findViewById(R.id.avatar_image);
            holder.nickName = (TextView) convertView.findViewById(R.id.nickname);
            holder.contentLayout = (LinearLayout) convertView.findViewById(R.id.content_layout);
            String type = notification.type;
            View view;
            if (Notification.FEEDBACK.equals(type)) {
                final FeedbackNotification fn = (FeedbackNotification) notification;
                holder.nickName.setText(fn.feedback.user.nickname);
                holder.avatarImage.setOnClickListener(new OnClickListener() {
                    public void onClick(View v) {
                        Intent intent = new Intent(NotificationAdapter.this.mContext,
                                UserTimelineActivity.class);
                        intent.putExtra(UserTimelineActivity.NICK_NAME, fn.feedback.user.nickname);
                        NotificationAdapter.this.mContext.startActivity(intent);
                    }
                });
                this.mImageLoader.displayImage(fn.feedback.user.avatar_url, holder.avatarImage,
                        ImageLoaderOptions.avatar());
                view = LayoutInflater.from(this.mContext).inflate(R.layout.f6, null);
                holder.contentLayout.addView(view);
                TextView diamond = (TextView) view.findViewById(R.id.diamond);
                ((TextView) view.findViewById(R.id.body)).setText("我说：\n" + fn.feedback.post.body);
                diamond.setText(String.format("送给你%d颗", new Object[]{Integer.valueOf(fn.feedback
                        .diamond)}));
            } else if (Notification.REPOST.equals(type)) {
                RepostNotification rn = (RepostNotification) notification;
                holder.nickName.setText(rn.repost.user.nickname);
                final RepostNotification repostNotification = rn;
                holder.avatarImage.setOnClickListener(new OnClickListener() {
                    public void onClick(View v) {
                        Intent intent = new Intent(NotificationAdapter.this.mContext,
                                UserTimelineActivity.class);
                        intent.putExtra(UserTimelineActivity.NICK_NAME, repostNotification.repost
                                .user.nickname);
                        NotificationAdapter.this.mContext.startActivity(intent);
                    }
                });
                this.mImageLoader.displayImage(rn.repost.user.avatar_url, holder.avatarImage,
                        ImageLoaderOptions.avatar());
                view = LayoutInflater.from(this.mContext).inflate(R.layout.ly, null);
                holder.contentLayout.addView(view);
                ((TextView) view.findViewById(R.id.body)).setText("我说：\n" + rn.repost.post.body);
            } else if (Notification.MENTION.equals(type)) {
                final MentionNotification mn = (MentionNotification) notification;
                holder.nickName.setText(mn.mention.nickname());
                holder.avatarImage.setOnClickListener(new OnClickListener() {
                    public void onClick(View v) {
                        Intent intent = new Intent(NotificationAdapter.this.mContext,
                                UserTimelineActivity.class);
                        intent.putExtra(UserTimelineActivity.NICK_NAME, mn.mention.nickname());
                        NotificationAdapter.this.mContext.startActivity(intent);
                    }
                });
                view = LayoutInflater.from(this.mContext).inflate(R.layout.kg, null);
                holder.contentLayout.addView(view);
                ((TextView) view.findViewById(R.id.body)).setText(mn.mention.body());
                TextView originBodyText = (TextView) view.findViewById(R.id.origin_body);
                if (Mention.POST.equals(mn.mention.type)) {
                    this.mImageLoader.displayImage(mn.mention.post.user.avatar_url, holder
                            .avatarImage, ImageLoaderOptions.avatar());
                    originBodyText.setVisibility(8);
                } else if ("comment".equals(mn.mention.type)) {
                    this.mImageLoader.displayImage(mn.mention.comment.user.avatar_url, holder
                            .avatarImage, ImageLoaderOptions.avatar());
                    originBodyText.setText(mn.mention.originBody(this.mUserId));
                    originBodyText.setVisibility(0);
                }
                ((Button) view.findViewById(R.id.comment_btn)).setOnClickListener(new OnClickListener() {
                    public void onClick(View v) {
                        NotificationAdapter.this.startCommentListActivity(mn.mention.nickname(),
                                mn.mention.postId());
                    }
                });
            } else if (Notification.FRIENDSHIP.equals(type)) {
                final FriendshipNotification followNotification = (FriendshipNotification)
                        notification;
                holder.nickName.setText(followNotification.follower.nickname);
                this.mImageLoader.displayImage(followNotification.follower.avatar_url, holder
                        .avatarImage, ImageLoaderOptions.avatar());
                holder.avatarImage.setOnClickListener(new OnClickListener() {
                    public void onClick(View v) {
                        Intent intent = new Intent(NotificationAdapter.this.mContext,
                                UserTimelineActivity.class);
                        intent.putExtra(UserTimelineActivity.NICK_NAME, followNotification
                                .follower.nickname);
                        NotificationAdapter.this.mContext.startActivity(intent);
                    }
                });
                holder.contentLayout.addView(LayoutInflater.from(this.mContext).inflate(R.layout
                        .gy, null));
            } else if ("comment".equals(type)) {
                final CommentNotification cn = (CommentNotification) notification;
                holder.nickName.setText(cn.comment.user.nickname);
                this.mImageLoader.displayImage(cn.comment.user.avatar_url, holder.avatarImage,
                        ImageLoaderOptions.avatar());
                holder.avatarImage.setOnClickListener(new OnClickListener() {
                    public void onClick(View v) {
                        Intent intent = new Intent(NotificationAdapter.this.mContext,
                                UserTimelineActivity.class);
                        intent.putExtra(UserTimelineActivity.NICK_NAME, cn.comment.user.nickname);
                        NotificationAdapter.this.mContext.startActivity(intent);
                    }
                });
                view = LayoutInflater.from(this.mContext).inflate(R.layout.ej, null);
                holder.contentLayout.addView(view);
                body = (TextView) view.findViewById(R.id.body);
                ((TextView) view.findViewById(R.id.comment_text)).setText(cn.comment.body);
                if (this.mUserId == cn.comment.post.user.id) {
                    body.setText("我说：\n" + cn.comment.post.body);
                } else {
                    body.setText(cn.comment.post.user.nickname + "说：\n" + cn.comment.post.body);
                }
                ((Button) view.findViewById(R.id.comment_btn)).setOnClickListener(new OnClickListener() {
                    public void onClick(View v) {
                        NotificationAdapter.this.startCommentListActivity(cn.comment.user
                                .nickname, cn.comment.post.id);
                    }
                });
            } else if (Notification.STORY_MENTION.equals(type)) {
                StoryMentionNotification smn = (StoryMentionNotification) notification;
                if (!(smn.story_mention == null || smn.story_mention.story_comment == null)) {
                    holder.nickName.setText(smn.story_mention.story_comment.user.nickname);
                    this.mImageLoader.displayImage(smn.story_mention.story_comment.user
                            .avatar_url, holder.avatarImage, ImageLoaderOptions.avatar());
                    final StoryMentionNotification storyMentionNotification = smn;
                    holder.avatarImage.setOnClickListener(new OnClickListener() {
                        public void onClick(View v) {
                            Intent intent = new Intent(NotificationAdapter.this.mContext,
                                    UserTimelineActivity.class);
                            intent.putExtra(UserTimelineActivity.NICK_NAME,
                                    storyMentionNotification.story_mention.story_comment.user
                                            .nickname);
                            NotificationAdapter.this.mContext.startActivity(intent);
                        }
                    });
                    view = LayoutInflater.from(this.mContext).inflate(R.layout.ej, null);
                    holder.contentLayout.addView(view);
                    body = (TextView) view.findViewById(R.id.body);
                    ((TextView) view.findViewById(R.id.comment_text)).setText(smn.story_mention
                            .story_comment.body);
                    body.setText(smn.story_mention.story_comment.story.body);
                    storyMentionNotification = smn;
                    ((Button) view.findViewById(R.id.comment_btn)).setOnClickListener(new OnClickListener() {
                        public void onClick(View v) {
                            StoryCommentActivity.comeOnWithComment(NotificationAdapter.this
                                    .mContext, String.valueOf(storyMentionNotification
                                    .story_mention.story_comment.story.id),
                                    storyMentionNotification.story_mention.story_comment.user
                                            .nickname);
                        }
                    });
                }
            } else if (Notification.STORY_COMMENT.equals(type)) {
                StoryCommentNotification smn2 = (StoryCommentNotification) notification;
                if (smn2.story_comment != null) {
                    final StoryCommentNotification storyCommentNotification;
                    if (smn2.story_comment.user != null) {
                        holder.nickName.setText(smn2.story_comment.user.nickname);
                        this.mImageLoader.displayImage(smn2.story_comment.user.avatar_url, holder
                                .avatarImage, ImageLoaderOptions.avatar());
                        storyCommentNotification = smn2;
                        holder.avatarImage.setOnClickListener(new OnClickListener() {
                            public void onClick(View v) {
                                Intent intent = new Intent(NotificationAdapter.this.mContext,
                                        UserTimelineActivity.class);
                                intent.putExtra(UserTimelineActivity.NICK_NAME,
                                        storyCommentNotification.story_comment.user.nickname);
                                NotificationAdapter.this.mContext.startActivity(intent);
                            }
                        });
                    }
                    view = LayoutInflater.from(this.mContext).inflate(R.layout.ej, null);
                    holder.contentLayout.addView(view);
                    body = (TextView) view.findViewById(R.id.body);
                    ((TextView) view.findViewById(R.id.comment_text)).setText(smn2.story_comment
                            .body);
                    body.setText(smn2.story_comment.story.body);
                    storyCommentNotification = smn2;
                    ((Button) view.findViewById(R.id.comment_btn)).setOnClickListener(new OnClickListener() {
                        public void onClick(View v) {
                            StoryCommentActivity.comeOnWithComment(NotificationAdapter.this
                                    .mContext, String.valueOf(storyCommentNotification
                                    .story_comment.story.id), storyCommentNotification
                                    .story_comment.user.nickname);
                        }
                    });
                }
            }
            convertView.setTag(holder);
        }
        return convertView;
    }

    private void startCommentListActivity(String comment_prefix, int post_id) {
        CommentListActivity.startActivity(this.mContext, post_id, "回复@" + comment_prefix + ": ", false);
    }
}
