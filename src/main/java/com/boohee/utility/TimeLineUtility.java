package com.boohee.utility;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Vibrator;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.RelativeSizeSpan;
import android.text.style.URLSpan;
import android.text.util.Linkify;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.DecelerateInterpolator;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.PopupMenu.OnMenuItemClickListener;
import android.widget.TextView;

import boohee.lib.share.ShareManager;

import com.boohee.api.FavoriteApi;
import com.boohee.api.StatusApi;
import com.boohee.model.status.AttachMent;
import com.boohee.model.status.Photo;
import com.boohee.model.status.Post;
import com.boohee.model.status.StatusUser;
import com.boohee.one.R;
import com.boohee.one.http.JsonCallback;
import com.boohee.one.player.ExVideoView;
import com.boohee.one.player.FullScreenVideoActivity;
import com.boohee.one.ui.BaseActivity;
import com.boohee.status.CommentListActivity;
import com.boohee.utils.AccountUtils;
import com.boohee.utils.Helper;
import com.boohee.utils.Utils;
import com.boohee.widgets.CheckAccountPopwindow;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.Animator.AnimatorListener;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONObject;

public class TimeLineUtility {

    public interface PostMenuListener {
        void onPostAddFavorite();

        void onPostDelete();

        void onPostDeleteFavorite();

        void onPostRepost();
    }

    public static abstract class SimplePostMenuListener implements PostMenuListener {
        public void onPostDeleteFavorite() {
        }

        public void onPostRepost() {
        }

        public void onPostDelete() {
        }

        public void onPostAddFavorite() {
        }
    }

    public static class PraiseClickListener implements OnClickListener {
        Activity activity;
        CheckBox cb_praise;
        Post     post;
        View     rl_punch_praise;
        TextView tv_praise_plus;

        public PraiseClickListener(Activity activity, Post post, View rl_punch_praise, CheckBox
                cb_praise, TextView tv_praise_plus) {
            this.activity = activity;
            this.post = post;
            this.rl_punch_praise = rl_punch_praise;
            this.cb_praise = cb_praise;
            this.tv_praise_plus = tv_praise_plus;
        }

        public void onClick(View view) {
            this.rl_punch_praise.setClickable(false);
            if (TextUtils.isEmpty(this.post.feedback)) {
                StatusApi.putFeedbacks(this.activity, this.post.id, new JsonCallback(this
                        .activity) {
                    public void ok(JSONObject object) {
                        super.ok(object);
                        if (!TextUtils.isEmpty(object.optString("body"))) {
                            Helper.showToast(PraiseClickListener.this.activity, object.optString
                                    ("body"));
                        }
                        MobclickAgent.onEvent(PraiseClickListener.this.activity, Event
                                .STATUS_ADD_ATTITUTE_OK);
                        MobclickAgent.onEvent(PraiseClickListener.this.activity, Event
                                .STATUS_ADD_INTERACT_OK);
                        MobclickAgent.onEvent(PraiseClickListener.this.activity, Event
                                .MINE_ALL_RECORD_OK);
                        PraiseClickListener.this.post.feedback = "envious";
                        Post post = PraiseClickListener.this.post;
                        post.envious_count++;
                        PraiseClickListener.this.cb_praise.setChecked(true);
                        PraiseClickListener.this.cb_praise.setText(PraiseClickListener.this.post
                                .envious_count + "");
                        PraiseClickListener.this.cb_praise.setTextColor(PraiseClickListener.this
                                .activity.getResources().getColor(R.color.hb));
                        TimeLineUtility.setPlusAnimation(PraiseClickListener.this.tv_praise_plus);
                    }

                    public void onFinish() {
                        super.onFinish();
                        PraiseClickListener.this.rl_punch_praise.setClickable(true);
                    }
                });
            } else {
                StatusApi.deleteFeedbacks(this.activity, this.post.id, new JsonCallback(this
                        .activity) {
                    public void ok(JSONObject object) {
                        super.ok(object);
                        PraiseClickListener.this.post.feedback = null;
                        Post post = PraiseClickListener.this.post;
                        post.envious_count--;
                        PraiseClickListener.this.cb_praise.setChecked(false);
                        PraiseClickListener.this.cb_praise.setText(PraiseClickListener.this.post
                                .envious_count + "");
                        PraiseClickListener.this.cb_praise.setTextColor(PraiseClickListener.this
                                .activity.getResources().getColor(R.color.du));
                    }

                    public void onFinish() {
                        super.onFinish();
                        PraiseClickListener.this.rl_punch_praise.setClickable(true);
                    }
                });
            }
        }
    }

    public static void addLinks(TextView view) {
        view.setText(convertNormalStringToSpannableString(view.getText().toString()));
        if (view.getLinksClickable()) {
            view.setMovementMethod(LongClickableLinkMovementMethod.getInstance());
        }
    }

    public static void addLinksWithShowMore(TextView view, Post post) {
        if (post.cut) {
            handleLinksWithShowMore(view, post);
        } else {
            view.setText(convertNormalStringToSpannableString(post.body));
        }
        if (view.getLinksClickable()) {
            view.setMovementMethod(LongClickableLinkMovementMethod.getInstance());
        }
    }

    private static void handleLinksWithShowMore(TextView view, Post post) {
        String showMore = " [详细] ";
        String content = post.body + showMore;
        SpannableString spannable = convertNormalStringToSpannableString(content);
        spannable.setSpan(new RelativeSizeSpan(0.8f), content.length() - showMore.length(),
                content.length(), 17);
        spannable.setSpan(new ShowMoreSpan("showMore"), content.length() - showMore.length(),
                content.length(), 17);
        view.setText(spannable);
        view.setTag(R.id.timeline_post_id, Integer.valueOf(post.id));
    }

    private static int getCustomCutOff(int CUT_OFF, SpannableString oriSpannable, boolean
            strictLimit) {
        int i = 0;
        if (oriSpannable == null) {
            return 0;
        }
        int customCutOff = CUT_OFF;
        MyURLSpan[] spans = (MyURLSpan[]) oriSpannable.getSpans(0, oriSpannable.length(),
                MyURLSpan.class);
        int length = spans.length;
        while (i < length) {
            MyURLSpan span = spans[i];
            int start = oriSpannable.getSpanStart(span);
            int end = oriSpannable.getSpanEnd(span);
            if (start > CUT_OFF || end <= CUT_OFF) {
                i++;
            } else if (strictLimit) {
                return end;
            } else {
                return start;
            }
        }
        return customCutOff;
    }

    private static SpannableString convertNormalStringToSpannableString(String txt) {
        int i = 0;
        if (TextUtils.isEmpty(txt)) {
            return new SpannableString("");
        }
        String hackTxt;
        if (txt.startsWith("[") && txt.endsWith("]")) {
            hackTxt = txt + " ";
        } else {
            hackTxt = txt;
        }
        SpannableString value = SpannableString.valueOf(hackTxt);
        Linkify.addLinks(value, TimeLinePatterns.MENTION_URL, TimeLinePatterns.MENTION_SCHEME);
        Linkify.addLinks(value, TimeLinePatterns.WEB_URL, TimeLinePatterns.WEB_SCHEME);
        Linkify.addLinks(value, TimeLinePatterns.TOPIC_URL, TimeLinePatterns.TOPIC_SCHEME);
        Linkify.addLinks(value, TimeLinePatterns.BOOHEE_URL, "boohee://");
        URLSpan[] urlSpans = (URLSpan[]) value.getSpans(0, value.length(), URLSpan.class);
        int length = urlSpans.length;
        while (i < length) {
            URLSpan urlSpan = urlSpans[i];
            MyURLSpan weiboSpan = new MyURLSpan(urlSpan.getURL());
            int start = value.getSpanStart(urlSpan);
            int end = value.getSpanEnd(urlSpan);
            value.removeSpan(urlSpan);
            value.setSpan(weiboSpan, start, end, 33);
            i++;
        }
        return value;
    }

    public static void copyText(Context context, String post) {
        if (context != null && !TextUtils.isEmpty(post)) {
            try {
                ((Vibrator) context.getSystemService("vibrator")).vibrate(100);
                ((ClipboardManager) context.getSystemService("clipboard")).setPrimaryClip
                        (ClipData.newPlainText(post, post));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void initCommentButton(final Activity activity, View button, TextView
            tv_comment, final Post post, int position) {
        tv_comment.setText(post.comment_count + "");
        button.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (post.cut) {
                    CommentListActivity.startActivity(activity, post.id, null, false);
                } else {
                    CommentListActivity.startActivity(activity, post.id, null, true);
                }
            }
        });
    }

    public static void setPlusAnimation(final TextView tv_praise_plus) {
        tv_praise_plus.setVisibility(0);
        ObjectAnimator alpha = ObjectAnimator.ofFloat(tv_praise_plus, "alpha", new float[]{1.0f,
                0.3f});
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(tv_praise_plus, "scaleX", new float[]{1
                .0f, 1.5f});
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(tv_praise_plus, "scaleY", new float[]{1
                .0f, 1.5f});
        ObjectAnimator.ofFloat(tv_praise_plus, "translationY", new float[]{0.0f, -20.0f})
                .setInterpolator(new DecelerateInterpolator());
        AnimatorSet set = new AnimatorSet();
        set.playTogether(alpha, scaleX, scaleY, transY);
        set.setDuration(500);
        set.start();
        set.addListener(new AnimatorListener() {
            public void onAnimationStart(Animator animation) {
            }

            public void onAnimationRepeat(Animator animation) {
            }

            public void onAnimationEnd(Animator animation) {
                tv_praise_plus.setVisibility(8);
            }

            public void onAnimationCancel(Animator animation) {
            }
        });
    }

    public static void showCommentPopView(final Activity activity, View view, final Post post,
                                          final StatusUser statusUser, final PostMenuListener
                                                  postMenuListener) {
        if (post != null) {
            PopupMenu popup = new PopupMenu(activity, view);
            if (post.reposted) {
                popup.getMenu().add(0, 0, 0, "已推荐");
            } else {
                popup.getMenu().add(0, 1, 0, "推荐");
            }
            popup.getMenu().add(0, 2, 0, "分享");
            if (post.favorite) {
                popup.getMenu().add(0, 3, 0, "取消收藏");
            } else {
                popup.getMenu().add(0, 4, 0, "收藏");
            }
            if (post.own) {
                popup.getMenu().add(0, 5, 0, "删除");
            }
            popup.show();
            popup.setOnMenuItemClickListener(new OnMenuItemClickListener() {
                public boolean onMenuItemClick(MenuItem item) {
                    switch (item.getItemId()) {
                        case 1:
                            if (!AccountUtils.isVisitorAccount(activity)) {
                                TimeLineUtility.repost(activity, post, postMenuListener);
                                break;
                            }
                            CheckAccountPopwindow.showVisitorPopWindow(activity);
                            break;
                        case 2:
                            TimeLineUtility.share(activity, post, statusUser);
                            break;
                        case 3:
                            TimeLineUtility.deleteFavorite(activity, post, postMenuListener);
                            break;
                        case 4:
                            TimeLineUtility.addFavorite(activity, post, postMenuListener);
                            break;
                        case 5:
                            TimeLineUtility.showDeleteDialog(activity, post, postMenuListener);
                            break;
                    }
                    return false;
                }
            });
        }
    }

    public static void repost(final Activity activity, final Post post, final PostMenuListener
            postMenuListener) {
        StatusApi.repostPost(activity, post.id, new JsonCallback(activity) {
            public void ok(JSONObject object) {
                super.ok(object);
                MobclickAgent.onEvent(activity, Event.STATUS_ADD_REOST_OK);
                post.reposted = true;
                Helper.showToast((CharSequence) "推荐成功");
                if (postMenuListener != null) {
                    postMenuListener.onPostRepost();
                }
            }
        });
    }

    public static void share(Activity activity, Post post, StatusUser fromUser) {
        if (post == null) {
            Helper.showLog("share", "---> post is null");
            return;
        }
        String content;
        String imageUrl;
        String contentUrl = "http://shop.boohee.com/store/pages/status_details?id=" + post.id;
        String append = "";
        if (post.user != null) {
            append = " From 薄荷 @" + post.user.nickname;
        } else if (fromUser != null) {
            append = " From 薄荷 @" + fromUser.nickname;
        }
        int cutOff = 140 - append.length();
        if (TextUtils.isEmpty(post.body)) {
            content = "";
        } else if (post.body.length() < cutOff) {
            content = post.body;
        } else {
            content = post.body.substring(0, getCustomCutOff(cutOff - "...".length(),
                    convertNormalStringToSpannableString(post.body), true)) + "...";
        }
        String desc = content + append;
        if (post.photos == null || post.photos.size() == 0) {
            imageUrl = "http://bohe-house.u.qiniudn.com/android/logo_256x256.png";
        } else {
            imageUrl = ((Photo) post.photos.get(0)).middle_url;
        }
        if (!TextUtils.isEmpty(imageUrl)) {
            imageUrl = imageUrl.replaceAll("\\|", "%7C");
        }
        ShareManager.share((Context) activity, content, desc, contentUrl, imageUrl);
    }

    public static void addFavorite(final Activity activity, final Post post, final
    PostMenuListener postMenuListener) {
        ((BaseActivity) activity).showLoading();
        FavoriteApi.addFavoritePost(post.id, new JsonCallback(activity) {
            public void ok(JSONObject object) {
                super.ok(object);
                post.favorite = true;
                Helper.showToast((int) R.string.b4);
                if (postMenuListener != null) {
                    postMenuListener.onPostAddFavorite();
                }
            }

            public void onFinish() {
                super.onFinish();
                ((BaseActivity) activity).dismissLoading();
            }
        }, activity);
    }

    public static void deleteFavorite(final Activity activity, final Post post, final
    PostMenuListener postMenuListener) {
        ((BaseActivity) activity).showLoading();
        FavoriteApi.deleteFavoritePost(post.id, new JsonCallback(activity) {
            public void onFinish() {
                super.onFinish();
                ((BaseActivity) activity).dismissLoading();
            }

            public void ok(JSONObject object) {
                super.ok(object);
                Helper.showToast((int) R.string.i8);
                post.favorite = false;
                if (postMenuListener != null) {
                    postMenuListener.onPostDeleteFavorite();
                }
            }
        }, activity);
    }

    public static void showDeleteDialog(final Activity activity, final Post post, final
    PostMenuListener postMenuListener) {
        new Builder(activity).setMessage("确定要删除吗？").setPositiveButton("删除", new DialogInterface
                .OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                StatusApi.deletePost(activity, post.id, new JsonCallback(activity) {
                    public void ok(JSONObject object) {
                        super.ok(object);
                        if (postMenuListener != null) {
                            postMenuListener.onPostDelete();
                        }
                    }
                });
            }
        }).setNegativeButton("取消", null).show();
    }

    public static void initAttachment(final Context activity, LinearLayout layout, TextView
            tvAttachment, ImageView ivAttachment, ExVideoView video, Post post) {
        final AttachMent attachMent = post.attachments;
        if (attachMent == null) {
            layout.setVisibility(8);
            video.setVisibility(8);
        } else if (!TextUtils.equals(attachMent.type, "video") || TextUtils.isEmpty(attachMent
                .cover)) {
            layout.setVisibility(0);
            video.setVisibility(8);
            tvAttachment.setText(attachMent.title);
            ImageLoader.getInstance().displayImage(attachMent.pic, ivAttachment);
            if (TextUtils.equals(attachMent.type, "show")) {
                ivAttachment.getLayoutParams().width = -2;
                ivAttachment.getLayoutParams().height = -2;
                ivAttachment.setScaleType(ScaleType.CENTER);
                layout.setBackgroundColor(0);
            } else {
                ivAttachment.getLayoutParams().width = DensityUtil.dip2px(activity, 70.0f);
                ivAttachment.getLayoutParams().height = DensityUtil.dip2px(activity, 70.0f);
                ivAttachment.setScaleType(ScaleType.CENTER_CROP);
                layout.setBackgroundColor(activity.getResources().getColor(R.color.an));
            }
            layout.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    if (!TextUtils.isEmpty(attachMent.url)) {
                        if (TextUtils.equals(attachMent.type, Utils.RESPONSE_CONTENT)) {
                            BooheeScheme.handleUrl(activity, attachMent.url);
                        } else if (TextUtils.equals(attachMent.type, "video")) {
                            FullScreenVideoActivity.startActivity(activity, attachMent.url);
                        }
                    }
                }
            });
        } else {
            video.setVisibility(0);
            layout.setVisibility(8);
            video.setData(attachMent.cover, attachMent.url, post.id);
            video.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                }
            });
        }
    }
}
