package com.boohee.myview.homeMenu;

import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.boohee.one.R;
import com.boohee.one.ui.StatusPostTextActivity;
import com.boohee.utility.Event;
import com.boohee.utils.viewanimator.AnimationListener.Stop;
import com.boohee.utils.viewanimator.ViewAnimator;
import com.facebook.rebound.SimpleSpringListener;
import com.facebook.rebound.Spring;
import com.facebook.rebound.SpringConfig;
import com.facebook.rebound.SpringSystem;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class PopMenu {
    private static final int DEFAULT_COLUMN_COUNT       = 4;
    private static final int DEFAULT_DURATION           = 300;
    private static final int DEFAULT_FRICTION           = 7;
    private static final int DEFAULT_HORIZONTAL_PADDING = 40;
    private static final int DEFAULT_TENSION            = 20;
    private static final int DEFAULT_VERTICAL_PADDING   = 28;
    private PopMenuItem         clickItem;
    private ViewGroup           decorView;
    private boolean             isShowing;
    private Activity            mActivity;
    private LinearLayout        mAnimateLayout;
    private ImageView           mCloseIv;
    private int                 mColumnCount;
    private int                 mDuration;
    private double              mFriction;
    private GridLayout          mGridLayout;
    private int                 mHorizontalPadding;
    private List<PopMenuItem>   mMenuItems;
    private PopMenuItemListener mPopMenuItemListener;
    private int                 mScreenHeight;
    private int                 mScreenWidth;
    private TextView            mSendPostTv;
    private SpringSystem        mSpringSystem;
    private double              mTension;
    private ImageView           mTimeView;
    private int                 mVerticalPadding;

    public static class Builder {
        private Activity activity;
        private int               columnCount       = 4;
        private int               duration          = 300;
        private double            friction          = 7.0d;
        private int               horizontalPadding = 40;
        private List<PopMenuItem> itemList          = new ArrayList();
        private PopMenuItemListener popMenuItemListener;
        private double tension         = 20.0d;
        private int    verticalPadding = 28;

        public Builder attachToActivity(Activity activity) {
            this.activity = activity;
            return this;
        }

        public Builder columnCount(int count) {
            this.columnCount = count;
            return this;
        }

        public Builder addMenuItem(PopMenuItem menuItem) {
            this.itemList.add(menuItem);
            return this;
        }

        public Builder duration(int duration) {
            this.duration = duration;
            return this;
        }

        public Builder tension(double tension) {
            this.tension = tension;
            return this;
        }

        public Builder friction(double friction) {
            this.friction = friction;
            return this;
        }

        public Builder horizontalPadding(int padding) {
            this.horizontalPadding = padding;
            return this;
        }

        public Builder verticalPadding(int padding) {
            this.verticalPadding = padding;
            return this;
        }

        public Builder setOnItemClickListener(PopMenuItemListener listener) {
            this.popMenuItemListener = listener;
            return this;
        }

        public PopMenu build() {
            return new PopMenu();
        }
    }

    public interface PopMenuItemListener {
        void onClose();

        void onItemClick(PopMenu popMenu, PopMenuItem popMenuItem);
    }

    private PopMenu(Builder builder) {
        this.mMenuItems = new ArrayList();
        this.isShowing = false;
        this.mSpringSystem = SpringSystem.create();
        this.mActivity = builder.activity;
        this.mMenuItems.clear();
        this.mMenuItems.addAll(builder.itemList);
        this.mColumnCount = builder.columnCount;
        this.mDuration = builder.duration;
        this.mTension = builder.tension;
        this.mFriction = builder.friction;
        this.mHorizontalPadding = builder.horizontalPadding;
        this.mVerticalPadding = builder.verticalPadding;
        this.mPopMenuItemListener = builder.popMenuItemListener;
        this.mScreenWidth = this.mActivity.getResources().getDisplayMetrics().widthPixels;
        this.mScreenHeight = this.mActivity.getResources().getDisplayMetrics().heightPixels;
    }

    public void show() {
        buildAnimateGridLayout();
        if (this.mAnimateLayout.getParent() != null) {
            ((ViewGroup) this.mAnimateLayout.getParent()).removeView(this.mAnimateLayout);
        }
        this.decorView = (ViewGroup) this.mActivity.getWindow().getDecorView().findViewById
                (16908290);
        this.decorView.addView(this.mAnimateLayout, new LayoutParams(-1, -1));
        this.mAnimateLayout.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                PopMenu.this.clickItem = null;
                PopMenu.this.hide();
            }
        });
        animateViewDirection(this.mGridLayout, (float) this.mScreenHeight, 0.0f, this.mTension,
                this.mFriction);
        ViewAnimator.animate(this.mCloseIv).rotation(-90.0f, 0.0f).duration((long) this
                .mDuration).start();
        this.isShowing = true;
    }

    public void hide() {
        if (this.isShowing && this.mGridLayout != null) {
            ViewAnimator.animate(this.mGridLayout).translationY((float) this.mScreenHeight)
                    .duration((long) this.mDuration).andAnimate(this.mAnimateLayout).alpha(1.0f,
                    0.0f).duration((long) this.mDuration).onStop(new Stop() {
                public void onStop() {
                    if (!(PopMenu.this.clickItem == null || PopMenu.this.mPopMenuItemListener ==
                            null)) {
                        PopMenu.this.mPopMenuItemListener.onItemClick(PopMenu.this, PopMenu.this
                                .clickItem);
                    }
                    if (PopMenu.this.mPopMenuItemListener != null) {
                        PopMenu.this.mPopMenuItemListener.onClose();
                    }
                    PopMenu.this.decorView.removeView(PopMenu.this.mAnimateLayout);
                }
            }).start();
            this.isShowing = false;
        }
    }

    public boolean isShowing() {
        return this.isShowing;
    }

    private void buildAnimateGridLayout() {
        this.mAnimateLayout = (LinearLayout) LayoutInflater.from(this.mActivity).inflate(R.layout
                .h_, null);
        this.mGridLayout = (GridLayout) this.mAnimateLayout.findViewById(R.id.grid_layout);
        this.mGridLayout.setColumnCount(this.mColumnCount);
        int hPadding = dp2px(this.mActivity, this.mHorizontalPadding);
        int vPadding = dp2px(this.mActivity, this.mVerticalPadding);
        int itemWidth = (this.mScreenWidth - ((this.mColumnCount + 1) * hPadding)) / this
                .mColumnCount;
        for (int i = 0; i < this.mMenuItems.size(); i++) {
            PopSubView subView = new PopSubView(this.mActivity);
            final PopMenuItem menuItem = (PopMenuItem) this.mMenuItems.get(i);
            subView.setPopMenuItem(menuItem);
            subView.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    PopMenu.this.clickItem = menuItem;
                    PopMenu.this.hide();
                }
            });
            GridLayout.LayoutParams lp = new GridLayout.LayoutParams();
            lp.width = itemWidth;
            lp.leftMargin = hPadding;
            lp.topMargin = vPadding / 2;
            lp.bottomMargin = vPadding / 2;
            this.mGridLayout.addView(subView, lp);
        }
        this.mCloseIv = (ImageView) this.mAnimateLayout.findViewById(R.id.iv_close);
        this.mCloseIv.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                PopMenu.this.clickItem = null;
                PopMenu.this.hide();
            }
        });
        this.mSendPostTv = (TextView) this.mAnimateLayout.findViewById(R.id.tv_send_post);
        this.mSendPostTv.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                MobclickAgent.onEvent(PopMenu.this.mActivity, Event.OTHER_CLICKPLUSPOST);
                PopMenu.this.mActivity.startActivity(new Intent(PopMenu.this.mActivity,
                        StatusPostTextActivity.class));
            }
        });
        this.mTimeView = (ImageView) this.mAnimateLayout.findViewById(R.id.iv_time);
        int hour = Calendar.getInstance().get(11);
        if (hour >= 5 && hour < 12) {
            this.mTimeView.setBackgroundResource(R.drawable.aaa);
        } else if (hour < 12 || hour >= 18) {
            this.mTimeView.setBackgroundResource(R.drawable.aa6);
        } else {
            this.mTimeView.setBackgroundResource(R.drawable.a_t);
        }
    }

    private void showSubMenus(ViewGroup viewGroup) {
        if (viewGroup != null) {
        }
    }

    private void hideSubMenus(ViewGroup viewGroup, AnimatorListenerAdapter listener) {
        if (viewGroup != null) {
            int childCount = viewGroup.getChildCount();
            for (int i = 0; i < childCount; i++) {
                viewGroup.getChildAt(i).animate().translationY((float) this.mScreenHeight)
                        .setDuration((long) this.mDuration).setListener(listener).start();
            }
        }
    }

    private void animateViewDirection(final View v, float from, float to, double tension, double
            friction) {
        Spring spring = this.mSpringSystem.createSpring();
        spring.setCurrentValue((double) from);
        spring.setSpringConfig(SpringConfig.fromOrigamiTensionAndFriction(tension, friction));
        spring.addListener(new SimpleSpringListener() {
            public void onSpringUpdate(Spring spring) {
                v.setTranslationY((float) spring.getCurrentValue());
            }
        });
        spring.setEndValue((double) to);
    }

    protected int dp2px(Context context, int dpVal) {
        return (int) TypedValue.applyDimension(1, (float) dpVal, context.getResources().getDisplayMetrics());
    }
}
