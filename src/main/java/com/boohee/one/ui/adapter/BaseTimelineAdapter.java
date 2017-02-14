package com.boohee.one.ui.adapter;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.boohee.api.StatusApi;
import com.boohee.model.status.Photo;
import com.boohee.model.status.Post;
import com.boohee.model.status.StatusUser;
import com.boohee.myview.NineGridLayout;
import com.boohee.myview.NineGridLayout.OnItemClickListerner;
import com.boohee.one.MyApplication;
import com.boohee.one.R;
import com.boohee.one.http.JsonCallback;
import com.boohee.one.player.ExVideoView;
import com.boohee.one.ui.NineGridGalleryActivity;
import com.boohee.status.LargeImageActivity;
import com.boohee.utility.DensityUtil;
import com.boohee.utility.ImageLoaderOptions;
import com.boohee.utility.TimeLineUtility;
import com.boohee.utility.TimeLineUtility.SimplePostMenuListener;
import com.boohee.utils.AccountUtils;
import com.boohee.utils.DateHelper;
import com.boohee.utils.Helper;
import com.boohee.widgets.CheckAccountPopwindow;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.json.JSONObject;

public abstract class BaseTimelineAdapter extends BaseAdapter {
    protected static final int[] COLORS         = new int[]{R.color.d0, R.color.cy, R.color.d1, R
            .color.cz};
    protected static       float sDensity       = DensityUtil.getDensity(MyApplication.getContext
            ());
    protected final        int[] PREVIEW_COLORS = new int[]{R.color.dg, R.color.dh, R.color.di, R
            .color.dj, R.color.dk};
    protected Activity activity;
    protected ImageLoader imageLoader = ImageLoader.getInstance();
    protected boolean     isFavorite  = false;
    protected Drawable mDefaultImageDrawable;
    DisplayImageOptions options;
    protected ArrayList<Post> posts;
    protected Resources       resource;
    protected StatusUser      user;

    private static class AnimateFirstDisplayListener extends SimpleImageLoadingListener {
        static final List<String> displayedImages = Collections.synchronizedList(new LinkedList());

        private AnimateFirstDisplayListener() {
        }

        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
            if (loadedImage != null) {
                ImageView imageView = (ImageView) view;
                if (!displayedImages.contains(imageUri)) {
                    FadeInBitmapDisplayer.animate(imageView, 500);
                    displayedImages.add(imageUri);
                }
            }
        }
    }

    protected BaseTimelineAdapter(Activity activity, ArrayList<Post> posts) {
        this.activity = activity;
        this.posts = posts;
        if (activity != null) {
            this.resource = activity.getResources();
            this.mDefaultImageDrawable = new ColorDrawable(this.resource.getColor(R.color.dj));
        }
    }

    public void setIsFavorite(boolean isFavorite) {
        this.isFavorite = isFavorite;
    }

    public int getCount() {
        if (this.posts != null) {
            return this.posts.size();
        }
        return 0;
    }

    public Post getItem(int position) {
        if (this.posts == null || this.posts.size() <= 0) {
            return null;
        }
        return (Post) this.posts.get(position);
    }

    public long getItemId(int position) {
        return (long) position;
    }

    protected void initTimeAndBody(TextView timeText, TextView bodyText, final Post post) {
        timeText.setText(DateHelper.timezoneFormat(post.created_at, "MM-dd HH:mm"));
        bodyText.setOnLongClickListener(new OnLongClickListener() {
            public boolean onLongClick(View v) {
                TimeLineUtility.copyText(BaseTimelineAdapter.this.activity, post.body);
                Helper.showToast(BaseTimelineAdapter.this.activity, (CharSequence) "内容已复制到剪切板");
                return true;
            }
        });
        TimeLineUtility.addLinksWithShowMore(bodyText, post);
    }

    protected void initPostImage(NineGridLayout nineGridLayout, final ArrayList<Photo> photos) {
        if (photos == null || photos.size() <= 0) {
            nineGridLayout.setVisibility(8);
            return;
        }
        nineGridLayout.setVisibility(0);
        nineGridLayout.setImagesData(photos);
        nineGridLayout.setOnItemClickListerner(new OnItemClickListerner() {
            public void onItemClick(View view, int position) {
                NineGridGalleryActivity.comeOn(BaseTimelineAdapter.this.activity, photos, position);
            }
        });
    }

    private void displayImage(ImageView image, String imageUrl, int width, int height, final
    String bigUrl) {
        image.getLayoutParams().width = width;
        image.getLayoutParams().height = height;
        this.imageLoader.displayImage(imageUrl, image, ImageLoaderOptions.global(this
                .mDefaultImageDrawable), new AnimateFirstDisplayListener());
        image.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(BaseTimelineAdapter.this.activity, LargeImageActivity
                        .class);
                intent.putExtra("image_url", bigUrl);
                BaseTimelineAdapter.this.activity.startActivity(intent);
            }
        });
    }

    protected void initDeleteButton(Button button, final Post post) {
        if (post.own) {
            button.setVisibility(0);
            button.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    if (AccountUtils.isVisitorAccount(BaseTimelineAdapter.this.activity)) {
                        CheckAccountPopwindow.showVisitorPopWindow(BaseTimelineAdapter.this
                                .activity);
                    } else {
                        BaseTimelineAdapter.this.showDeleteDialog(post);
                    }
                }
            });
            return;
        }
        button.setVisibility(8);
    }

    private void showDeleteDialog(final Post post) {
        new Builder(this.activity).setMessage("确定要删除吗？").setPositiveButton("删除", new
                DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                StatusApi.deletePost(BaseTimelineAdapter.this.activity, post.id, new JsonCallback
                        (BaseTimelineAdapter.this.activity) {
                    public void ok(JSONObject object) {
                        super.ok(object);
                        BaseTimelineAdapter.this.posts.remove(post);
                        BaseTimelineAdapter.this.notifyDataSetChanged();
                    }
                });
            }
        }).setNegativeButton("取消", null).show();
    }

    protected void showPopupMenu(View view, int position) {
        final Post post = getItem(position);
        if (post != null) {
            TimeLineUtility.showCommentPopView(this.activity, view, post, this.user, new
                    SimplePostMenuListener() {
                public void onPostDeleteFavorite() {
                    super.onPostDeleteFavorite();
                    if (BaseTimelineAdapter.this.isFavorite) {
                        BaseTimelineAdapter.this.posts.remove(post);
                    }
                    BaseTimelineAdapter.this.notifyDataSetChanged();
                }

                public void onPostRepost() {
                    super.onPostRepost();
                    BaseTimelineAdapter.this.notifyDataSetChanged();
                }

                public void onPostDelete() {
                    super.onPostDelete();
                    BaseTimelineAdapter.this.posts.remove(post);
                    BaseTimelineAdapter.this.notifyDataSetChanged();
                }

                public void onPostAddFavorite() {
                    super.onPostAddFavorite();
                }
            });
        }
    }

    protected void initAttachment(LinearLayout layout, TextView tvAttachment, ImageView
            ivAttachment, ExVideoView video, Post post) {
        TimeLineUtility.initAttachment(this.activity, layout, tvAttachment, ivAttachment, video, post);
    }
}
