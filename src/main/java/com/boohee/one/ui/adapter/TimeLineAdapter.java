package com.boohee.one.ui.adapter;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Vibrator;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.PopupMenu.OnMenuItemClickListener;
import android.widget.ProgressBar;
import android.widget.TextView;

import boohee.lib.share.ShareManager;

import com.boohee.api.FavoriteApi;
import com.boohee.api.StatusApi;
import com.boohee.model.status.Photo;
import com.boohee.model.status.Post;
import com.boohee.myview.NineGridLayout;
import com.boohee.myview.NineGridLayout.OnItemClickListerner;
import com.boohee.one.R;
import com.boohee.one.http.JsonCallback;
import com.boohee.one.ui.BaseActivity;
import com.boohee.one.ui.NineGridGalleryActivity;
import com.boohee.one.ui.adapter.SimpleBaseAdapter.ViewHolder;
import com.boohee.status.CommentListActivity;
import com.boohee.status.FriendShipActivity;
import com.boohee.status.LargeImageActivity;
import com.boohee.status.UserTimelineActivity;
import com.boohee.utility.Event;
import com.boohee.utility.ImageLoaderOptions;
import com.boohee.utility.TimeLineUtility;
import com.boohee.utils.DateHelper;
import com.boohee.utils.Helper;
import com.boohee.widgets.LightAlertDialog;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.analytics.MobclickAgent;

import de.hdodenhof.circleimageview.CircleImageView;

import java.util.List;

import org.json.JSONObject;

public class TimeLineAdapter extends SimpleBaseAdapter<Post> {
    private Context    context;
    private List<Post> mDelPost;
    private Resources  mRes;

    protected class AvartaClickListener implements OnClickListener {
        private String nickName;

        public AvartaClickListener(String nickName) {
            this.nickName = nickName;
        }

        public void onClick(View v) {
            Intent intent = new Intent(TimeLineAdapter.this.context, UserTimelineActivity.class);
            intent.putExtra(UserTimelineActivity.NICK_NAME, this.nickName);
            TimeLineAdapter.this.context.startActivity(intent);
        }
    }

    protected class CommentClickListener implements OnClickListener {
        private final int  position;
        private final Post post;

        public CommentClickListener(Post post, int position) {
            this.post = post;
            this.position = position;
        }

        public void onClick(View v) {
            Intent intent = new Intent(TimeLineAdapter.this.context, CommentListActivity.class);
            intent.putExtra(CommentListActivity.POST_ID, this.post.id);
            intent.putExtra(FriendShipActivity.FRIENDSHIP_POSITION, this.position);
            TimeLineAdapter.this.context.startActivity(intent);
        }
    }

    protected class DelBtnClickListener implements OnClickListener {
        private final Post post;

        public DelBtnClickListener(Post post) {
            this.post = post;
        }

        public void onClick(View v) {
            LightAlertDialog.create(TimeLineAdapter.this.context, "确定删除？").setNegativeButton(
                    (int) R.string.eq, null).setPositiveButton((int) R.string.y8, new
                    DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    DelBtnClickListener.this.sendDelRequest();
                }
            }).show();
        }

        private void sendDelRequest() {
            StatusApi.deletePost(TimeLineAdapter.this.context, this.post.id, new JsonCallback(
                    (Activity) TimeLineAdapter.this.context) {
                public void ok(JSONObject object) {
                    super.ok(object);
                    TimeLineAdapter.this.data.remove(DelBtnClickListener.this.post);
                    if (TimeLineAdapter.this.mDelPost != null) {
                        TimeLineAdapter.this.mDelPost.remove(DelBtnClickListener.this.post);
                    }
                    TimeLineAdapter.this.notifyDataSetChanged();
                }
            });
        }
    }

    protected class DisplaBigImageListener implements OnClickListener {
        private String imageUrl;

        public DisplaBigImageListener(String url) {
            this.imageUrl = url;
        }

        public void onClick(View v) {
            Intent intent = new Intent(TimeLineAdapter.this.context, LargeImageActivity.class);
            intent.putExtra("image_url", this.imageUrl);
            TimeLineAdapter.this.context.startActivity(intent);
        }
    }

    protected class FavClickListener implements OnClickListener {
        private int position;

        FavClickListener(int position) {
            this.position = position;
        }

        public void onClick(final View v) {
            Post post = (Post) TimeLineAdapter.this.data.get(this.position);
            v.setEnabled(false);
            if (TimeLineAdapter.this.isFav(post)) {
                StatusApi.deleteFeedbacks(TimeLineAdapter.this.context, post.id, new JsonCallback
                        ((Activity) TimeLineAdapter.this.context) {
                    public void ok(JSONObject object) {
                        super.ok(object);
                        if (TimeLineAdapter.this.data.size() != 0 && FavClickListener.this
                                .position < TimeLineAdapter.this.data.size() && TimeLineAdapter
                                .this.context != null) {
                            CharSequence body = object.optString("body");
                            if (!TextUtils.isEmpty(body)) {
                                Helper.showToast(body);
                            }
                            ((CheckBox) v).setChecked(false);
                            CheckBox checkBox = (CheckBox) v;
                            StringBuilder stringBuilder = new StringBuilder();
                            Post post = (Post) TimeLineAdapter.this.data.get(FavClickListener
                                    .this.position);
                            int i = post.envious_count - 1;
                            post.envious_count = i;
                            checkBox.setText(stringBuilder.append(i).append("").toString());
                            ((Post) TimeLineAdapter.this.data.get(FavClickListener.this.position)
                            ).feedback = "hateful";
                        }
                    }

                    public void onFinish() {
                        super.onFinish();
                        v.setEnabled(true);
                    }
                });
            } else {
                StatusApi.putFeedbacks(TimeLineAdapter.this.context, post.id, new JsonCallback(
                        (Activity) TimeLineAdapter.this.context) {
                    public void ok(JSONObject object) {
                        super.ok(object);
                        MobclickAgent.onEvent(TimeLineAdapter.this.context, Event
                                .STATUS_ADD_ATTITUTE_OK);
                        MobclickAgent.onEvent(TimeLineAdapter.this.context, Event
                                .STATUS_ADD_INTERACT_OK);
                        MobclickAgent.onEvent(TimeLineAdapter.this.context, Event
                                .MINE_ALL_RECORD_OK);
                        if (TimeLineAdapter.this.data.size() != 0 && FavClickListener.this
                                .position < TimeLineAdapter.this.data.size() && TimeLineAdapter
                                .this.context != null) {
                            CharSequence body = object.optString("body");
                            if (!TextUtils.isEmpty(body)) {
                                Helper.showToast(body);
                            }
                            ((CheckBox) v).setChecked(true);
                            CheckBox checkBox = (CheckBox) v;
                            StringBuilder stringBuilder = new StringBuilder();
                            Post post = (Post) TimeLineAdapter.this.data.get(FavClickListener
                                    .this.position);
                            int i = post.envious_count + 1;
                            post.envious_count = i;
                            checkBox.setText(stringBuilder.append(i).append("").toString());
                            ((Post) TimeLineAdapter.this.data.get(FavClickListener.this.position)
                            ).feedback = "envious";
                        }
                    }

                    public void onFinish() {
                        v.setEnabled(true);
                    }
                });
            }
        }
    }

    protected class LongClickCopy implements OnLongClickListener {
        private final Post post;

        public LongClickCopy(Post post) {
            this.post = post;
        }

        public boolean onLongClick(View v) {
            try {
                ((Vibrator) TimeLineAdapter.this.context.getSystemService("vibrator")).vibrate(100);
                ((ClipboardManager) TimeLineAdapter.this.context.getSystemService("clipboard"))
                        .setPrimaryClip(ClipData.newPlainText(this.post.body, this.post.body));
                Helper.showToast((CharSequence) "内容已复制到剪切板");
            } catch (Exception e) {
                e.printStackTrace();
            }
            return true;
        }
    }

    protected class ShareClickListener implements OnClickListener {
        private final Post post;

        public ShareClickListener(Post post) {
            this.post = post;
        }

        public void onClick(View v) {
            if (this.post != null) {
                TimeLineAdapter.this.share(this.post);
            }
        }
    }

    public TimeLineAdapter(Context context, List<Post> posts, List<Post> delPost) {
        super(context, posts);
        this.context = context;
        this.mRes = context.getResources();
        this.mDelPost = delPost;
    }

    public TimeLineAdapter(Context context, List<Post> data) {
        this(context, data, null);
    }

    public int getItemResource() {
        return R.layout.je;
    }

    public View getItemView(int position, View convertView, ViewHolder holder) {
        CircleImageView iv_avatar = (CircleImageView) holder.getView(R.id.iv_avatar);
        TextView tv_username = (TextView) holder.getView(R.id.tv_username);
        TextView tv_date = (TextView) holder.getView(R.id.tv_date);
        TextView tv_content = (TextView) holder.getView(R.id.tv_content);
        CheckBox cb_fav = (CheckBox) holder.getView(R.id.cb_fav);
        TextView tv_comment = (TextView) holder.getView(R.id.tv_comment);
        TextView tv_share = (TextView) holder.getView(R.id.tv_share);
        ImageView iv_tag = (ImageView) holder.getView(R.id.iv_tag);
        ImageView iv_avatar_tag = (ImageView) holder.getView(R.id.iv_avatar_tag);
        ProgressBar pb_active = (ProgressBar) holder.getView(R.id.pb_active);
        TextView tv_del = (TextView) holder.getView(R.id.tv_del);
        NineGridLayout nineGridLayout = (NineGridLayout) holder.getView(R.id.iv_content);
        final int i = position;
        ((Button) holder.getView(R.id.btn_menu)).setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                TimeLineAdapter.this.showPopupMenu(v, i);
            }
        });
        Post post = (Post) this.data.get(position);
        tv_username.setText(post.user != null ? post.user.nickname : "");
        tv_date.setText(DateHelper.timezoneFormat(post.created_at, "MM-dd HH:mm"));
        tv_content.setText(post.body);
        TimeLineUtility.addLinks(tv_content);
        loadAvatar(iv_avatar, post);
        loadContentImg(nineGridLayout, post);
        cb_fav.setText(post.envious_count + "");
        tv_comment.setText(post.comment_count + "");
        cb_fav.setChecked(isFav(post));
        setUpTopTag(iv_tag, post);
        setUpAvatarTag(pb_active, iv_avatar_tag, post);
        setUpOwner(tv_del, post);
        AvartaClickListener listener = new AvartaClickListener(post.user.nickname);
        iv_avatar.setOnClickListener(listener);
        tv_username.setOnClickListener(listener);
        cb_fav.setOnClickListener(new FavClickListener(position));
        tv_comment.setOnClickListener(new CommentClickListener(post, position));
        tv_share.setOnClickListener(new ShareClickListener(post));
        convertView.setOnLongClickListener(new LongClickCopy(post));
        return convertView;
    }

    protected void showPopupMenu(View view, int position) {
        final Post post = (Post) getItem(position);
        PopupMenu popup = new PopupMenu(this.context, view);
        popup.getMenu().add(0, 0, 0, "分享");
        if (post.favorite) {
            popup.getMenu().add(0, 1, 0, "取消收藏");
        } else {
            popup.getMenu().add(0, 2, 0, "收藏");
        }
        popup.show();
        popup.setOnMenuItemClickListener(new OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case 0:
                        TimeLineAdapter.this.share(post);
                        break;
                    case 1:
                        TimeLineAdapter.this.deleteFavorite(post);
                        break;
                    case 2:
                        TimeLineAdapter.this.addFavorite(post);
                        break;
                }
                return false;
            }
        });
    }

    private void addFavorite(final Post post) {
        ((BaseActivity) this.context).showLoading();
        FavoriteApi.addFavoritePost(post.id, new JsonCallback((Activity) this.context) {
            public void ok(JSONObject object) {
                super.ok(object);
                post.favorite = true;
                Helper.showToast((int) R.string.b4);
            }

            public void onFinish() {
                super.onFinish();
                ((BaseActivity) TimeLineAdapter.this.context).dismissLoading();
            }
        }, this.context);
    }

    private void deleteFavorite(final Post post) {
        ((BaseActivity) this.context).showLoading();
        FavoriteApi.deleteFavoritePost(post.id, new JsonCallback((Activity) this.context) {
            public void onFinish() {
                super.onFinish();
                ((BaseActivity) TimeLineAdapter.this.context).dismissLoading();
            }

            public void ok(JSONObject object) {
                super.ok(object);
                Helper.showToast((int) R.string.i8);
                post.favorite = false;
                TimeLineAdapter.this.notifyDataSetChanged();
            }
        }, this.context);
    }

    protected void setUpTopTag(ImageView ivTag, Post post) {
        if (post.isPrivate) {
            ivTag.setBackgroundResource(R.drawable.a3e);
        } else if (post.isTop) {
            ivTag.setBackgroundResource(R.drawable.a3g);
        } else {
            ivTag.setBackgroundResource(R.color.in);
        }
    }

    protected void setUpAvatarTag(ProgressBar progressBar, ImageView officialImage, Post post) {
        progressBar.setVisibility(post.user.light ? 0 : 4);
        int drawableRes = post.user.getAvatarTagResource();
        officialImage.setVisibility(drawableRes != 0 ? 0 : 8);
        if (drawableRes != 0) {
            officialImage.setImageResource(drawableRes);
            officialImage.setVisibility(0);
        }
    }

    protected void setUpOwner(View delBtn, Post post) {
        delBtn.setVisibility(post.own ? 0 : 8);
        if (post.own) {
            delBtn.setOnClickListener(new DelBtnClickListener(post));
        }
    }

    protected void setUpBigPhoto(ImageView imageView, Post post) {
        if (post.photos != null && post.photos.size() != 0) {
            imageView.setOnClickListener(new DisplaBigImageListener(((Photo) post.photos.get(0))
                    .big_url));
        }
    }

    protected void loadAvatar(ImageView imageView, Post post) {
        if (post.user == null || TextUtils.isEmpty(post.user.avatar_url)) {
            imageView.setVisibility(4);
        } else {
            loadImage(imageView, post.user.avatar_url);
        }
    }

    protected void loadContentImg(NineGridLayout nineGridLayout, final Post post) {
        if (post.photos == null || post.photos.size() == 0) {
            nineGridLayout.setVisibility(8);
            return;
        }
        nineGridLayout.setVisibility(0);
        nineGridLayout.setImagesData(post.photos);
        nineGridLayout.setOnItemClickListerner(new OnItemClickListerner() {
            public void onItemClick(View view, int position) {
                NineGridGalleryActivity.comeOn(TimeLineAdapter.this.context, post.photos, position);
            }
        });
    }

    protected void loadImage(ImageView imageView, String imgUrl) {
        imageView.setVisibility(0);
        if (!TextUtils.isEmpty(imgUrl) && !imgUrl.equals(imageView.getTag())) {
            ImageLoader.getInstance().displayImage(imgUrl, imageView, ImageLoaderOptions.color(R
                    .color.ay));
            imageView.setTag(imgUrl);
        }
    }

    public boolean isFav(Post post) {
        return "envious".equals(post.feedback);
    }

    private void share(Post post) {
        String imageUrl;
        String contentUrl = "http://shop.boohee.com/store/pages/status_details?id=" + post.id;
        String shareContent = post.body + " From 薄荷";
        if (post.user != null) {
            shareContent = post.body + " From 薄荷 @" + post.user.nickname;
        }
        if (post.photos == null || post.photos.size() == 0) {
            imageUrl = "http://bohe-house.u.qiniudn.com/android/logo_256x256.png";
        } else {
            imageUrl = ((Photo) post.photos.get(0)).middle_url;
        }
        ShareManager.share(this.context, "分享", shareContent, contentUrl, imageUrl);
    }
}
