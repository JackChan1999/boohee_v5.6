package com.boohee.myview;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.app.Dialog;
import android.app.WallpaperManager;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.KeyCharacterMap;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import boohee.lib.share.ShareManager;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import com.boohee.account.HomeImagePagerAdapter;
import com.boohee.model.HomeWallpager;
import com.boohee.myview.HomePopSlidingLayout.OnSlidingUpListener;
import com.boohee.one.R;
import com.boohee.one.ui.fragment.BaseDialogFragment;
import com.boohee.utility.Event;
import com.boohee.utils.FileUtil;
import com.boohee.utils.Helper;
import com.boohee.utils.ResolutionUtils;
import com.boohee.utils.WheelUtils;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.umeng.analytics.MobclickAgent;

import java.io.IOException;
import java.util.List;

public class HomePopView extends BaseDialogFragment {
    private HomeImagePagerAdapter<HomeWallpager> adapter;
    @InjectView(2131429317)
    ImageView btnWallLeft;
    @InjectView(2131429319)
    ImageView btnWallRight;
    private String date;
    private Handler handler = new Handler();
    @InjectView(2131429311)
    HomePopSlidingLayout homePopSlidingLayout;
    private ImageLoader imageLoader = ImageLoader.getInstance();
    private String imageUrl;
    private int    index;
    @InjectView(2131429315)
    ImageView    ivDownload;
    @InjectView(2131429314)
    ImageView    ivShare;
    @InjectView(2131429313)
    ImageView    ivWallpaper;
    @InjectView(2131429312)
    LinearLayout llDownloadShare;
    @InjectView(2131429316)
    LinearLayout llWallTitle;
    Runnable        runnable    = new Runnable() {
        public void run() {
            HomePopView.this.setTitleInVisible();
        }
    };
    GestureDetector tapDetector = new GestureDetector(getActivity(), new SimpleOnGestureListener() {
        public boolean onSingleTapConfirmed(MotionEvent e) {
            if (!(HomePopView.this.isRemoved() || HomePopView.this.llWallTitle == null)) {
                if (HomePopView.this.llWallTitle.getVisibility() == 0) {
                    HomePopView.this.setTitleInVisible();
                } else {
                    HomePopView.this.setTitleVisible();
                }
            }
            return false;
        }
    });
    @InjectView(2131429318)
    TextView tvWallDate;
    private View view;
    @InjectView(2131427463)
    ViewPager viewpager;
    private List<HomeWallpager> wallpagers;

    public static HomePopView newInstance(List<HomeWallpager> list) {
        HomePopView homePopView = new HomePopView();
        homePopView.wallpagers = list;
        return homePopView;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(1, 16973829);
    }

    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.getWindow().getAttributes().windowAnimations = R.style.ks;
        if (VERSION.SDK_INT >= 19) {
            dialog.getWindow().setFlags(1024, 1024);
        }
        return dialog;
    }

    @Nullable
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        View view = inflater.inflate(R.layout.pd, container, false);
        ButterKnife.inject((Object) this, view);
        return view;
    }

    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
    }

    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
    }

    private void initView() {
        if (this.wallpagers != null && this.wallpagers.size() != 0) {
            this.llWallTitle.setBackgroundColor(Color.parseColor("#64ffffff"));
            this.homePopSlidingLayout.setOnSlidingUpListener(new OnSlidingUpListener() {
                public void onClick() {
                }

                public void onSlidingUp() {
                    HomePopView.this.dismiss();
                }
            });
            this.adapter = new HomeImagePagerAdapter(getChildFragmentManager(), this.wallpagers);
            this.viewpager.setAdapter(this.adapter);
            this.viewpager.addOnPageChangeListener(new OnPageChangeListener() {
                public void onPageScrolled(int position, float positionOffset, int
                        positionOffsetPixels) {
                }

                public void onPageSelected(int position) {
                    HomePopView.this.index = position;
                    HomePopView.this.refreshWallPaper();
                }

                public void onPageScrollStateChanged(int state) {
                }
            });
            this.viewpager.setOnTouchListener(new OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    HomePopView.this.tapDetector.onTouchEvent(event);
                    return false;
                }
            });
            this.viewpager.postDelayed(new Runnable() {
                public void run() {
                    HomePopView.this.viewpager.setCurrentItem(HomePopView.this.wallpagers.size()
                            - 1);
                    HomePopView.this.refreshWallPaper();
                }
            }, 50);
            boolean hasMenuKey = ViewConfiguration.get(getActivity()).hasPermanentMenuKey();
            boolean hasBackKey = KeyCharacterMap.deviceHasKey(4);
            if (!hasMenuKey && !hasBackKey) {
                ((MarginLayoutParams) this.ivDownload.getLayoutParams()).bottomMargin = (int)
                        TypedValue.applyDimension(1, IntFloatWheelView.DEFAULT_VALUE, getActivity
                                ().getResources().getDisplayMetrics());
            }
        }
    }

    private void setTitleInVisible() {
        if (this.llWallTitle.getVisibility() == 0) {
            this.llWallTitle.animate().translationY((float) (-this.llWallTitle.getHeight()))
                    .setDuration(300).setListener(new AnimatorListener() {
                public void onAnimationStart(Animator animation) {
                }

                public void onAnimationEnd(Animator animation) {
                    if (!HomePopView.this.isRemoved() && HomePopView.this.llWallTitle != null) {
                        HomePopView.this.llWallTitle.setVisibility(4);
                    }
                }

                public void onAnimationCancel(Animator animation) {
                }

                public void onAnimationRepeat(Animator animation) {
                }
            }).start();
        }
    }

    private void setTitleVisible() {
        if (this.llWallTitle.getVisibility() != 0) {
            this.handler.removeCallbacks(this.runnable);
            this.llWallTitle.setVisibility(0);
            this.llWallTitle.animate().translationY(0.0f).setDuration(300).setListener(new AnimatorListener() {
                public void onAnimationStart(Animator animation) {
                }

                public void onAnimationEnd(Animator animation) {
                    if (!HomePopView.this.isRemoved() && HomePopView.this.handler != null &&
                            HomePopView.this.runnable != null) {
                        HomePopView.this.handler.postDelayed(HomePopView.this.runnable, 3000);
                    }
                }

                public void onAnimationCancel(Animator animation) {
                }

                public void onAnimationRepeat(Animator animation) {
                }
            }).start();
        }
    }

    private void refreshWallPaper() {
        boolean z = false;
        if (this.index >= 0 && this.index <= this.wallpagers.size() - 1) {
            this.btnWallLeft.setEnabled(this.index != 0);
            ImageView imageView = this.btnWallRight;
            if (this.index != this.wallpagers.size() - 1) {
                z = true;
            }
            imageView.setEnabled(z);
            this.handler.removeCallbacks(this.runnable);
            this.imageUrl = ((HomeWallpager) this.wallpagers.get(this.index)).getPicUrl();
            this.date = ((HomeWallpager) this.wallpagers.get(this.index)).date;
            this.tvWallDate.setText(this.date);
            this.handler.postDelayed(this.runnable, 3000);
        }
    }

    @OnClick({2131429315, 2131429314, 2131429313, 2131429317, 2131429319})
    public void onClick(View view) {
        if (!isRemoved() && !WheelUtils.isFastDoubleClick()) {
            switch (view.getId()) {
                case R.id.iv_wallpaper:
                    MobclickAgent.onEvent(getActivity(), Event.bingo_set_wallPaper);
                    this.ivWallpaper.setEnabled(false);
                    doSetWallPaper();
                    return;
                case R.id.iv_share:
                    MobclickAgent.onEvent(getActivity(), Event.bingo_sharePicture);
                    doShare();
                    return;
                case R.id.iv_download:
                    MobclickAgent.onEvent(getActivity(), Event.HOME_DOWN_PICTURE);
                    this.ivDownload.setEnabled(false);
                    downloadImage();
                    return;
                case R.id.btn_wall_left:
                    if (this.index > 0) {
                        this.index--;
                    }
                    this.viewpager.setCurrentItem(this.index);
                    return;
                case R.id.btn_wall_right:
                    if (this.wallpagers != null) {
                        if (this.index < this.wallpagers.size() - 1) {
                            this.index++;
                        }
                        this.viewpager.setCurrentItem(this.index);
                        return;
                    }
                    return;
                default:
                    return;
            }
        }
    }

    private void doSetWallPaper() {
        this.imageLoader.loadImage(this.imageUrl, new SimpleImageLoadingListener() {
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                if (!HomePopView.this.isRemoved()) {
                    Bitmap scaledBitmap;
                    WallpaperManager wm = WallpaperManager.getInstance(HomePopView.this
                            .getActivity());
                    int desiredMinimumHeight = wm.getDesiredMinimumHeight();
                    int desiredMinimumWidth = wm.getDesiredMinimumWidth();
                    if (desiredMinimumHeight != desiredMinimumWidth || desiredMinimumHeight <= 0) {
                        scaledBitmap = HomePopView.this.getResizedBitmap(loadedImage,
                                ResolutionUtils.getScreenWidth(HomePopView.this.getActivity()),
                                ResolutionUtils.getScreenHeight(HomePopView.this.getActivity()));
                    } else {
                        scaledBitmap = HomePopView.overlayIntoCentre(HomePopView.createNewBitmap
                                (desiredMinimumWidth, desiredMinimumHeight), loadedImage);
                    }
                    try {
                        wm.setBitmap(scaledBitmap);
                        Helper.showToast((CharSequence) "设置壁纸成功");
                    } catch (IOException e) {
                        e.printStackTrace();
                        Helper.showToast((CharSequence) "设置壁纸失败，请重试~");
                        Helper.showLog("setWallpaper", "Cannot set image as wallpaper");
                    } finally {
                        HomePopView.this.ivWallpaper.setEnabled(true);
                    }
                }
            }
        });
    }

    private Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {
        float scale;
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / ((float) width);
        float scaleHeight = ((float) newHeight) / ((float) height);
        if (scaleWidth > scaleHeight) {
            scale = scaleHeight;
        } else {
            scale = scaleWidth;
        }
        Matrix matrix = new Matrix();
        matrix.postScale(scale, scale);
        return Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);
    }

    public static Bitmap overlayIntoCentre(Bitmap bmp1, Bitmap bmp2) {
        Bitmap bmOverlay = Bitmap.createBitmap(bmp1.getWidth(), bmp1.getHeight(), bmp1.getConfig());
        Canvas canvas = new Canvas(bmOverlay);
        canvas.drawBitmap(bmp1, new Matrix(), null);
        canvas.drawBitmap(bmp2, (float) ((bmp1.getWidth() / 2) - (bmp2.getWidth() / 2)), (float)
                ((bmp1.getHeight() / 2) - (bmp2.getHeight() / 2)), null);
        return bmOverlay;
    }

    public static Bitmap createNewBitmap(int width, int height) {
        return Bitmap.createBitmap(width, height, Config.ARGB_8888);
    }

    private void downloadImage() {
        MobclickAgent.onEvent(getActivity(), Event.STATUS_SAVE_IMAGE_OK);
        this.imageLoader.loadImage(this.imageUrl, new SimpleImageLoadingListener() {
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                if (!HomePopView.this.isRemoved()) {
                    String result = FileUtil.downloadImage2Gallery(HomePopView.this.getActivity()
                            , loadedImage, new Md5FileNameGenerator().generate(HomePopView.this
                                    .imageUrl));
                    if (TextUtils.isEmpty(result)) {
                        Helper.showToast((CharSequence) "保存图片失败，请重新保存~~");
                    } else {
                        Helper.showToast("图片已保存到" + result);
                    }
                    HomePopView.this.ivDownload.setEnabled(true);
                }
            }
        });
    }

    private void doShare() {
        if (!TextUtils.isEmpty(this.imageUrl)) {
            ShareManager.shareWithImage(getActivity(), "", "薄荷每日壁纸", this.imageUrl);
        }
    }

    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
        this.handler.removeCallbacks(this.runnable);
    }
}
