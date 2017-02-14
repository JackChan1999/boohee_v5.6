package com.boohee.food;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.SimpleOnPageChangeListener;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import com.boohee.main.GestureActivity;
import com.boohee.myview.NewBadgeView;
import com.boohee.one.R;
import com.boohee.one.event.AddFinishAnimEvent;
import com.boohee.one.ui.adapter.ArrayPagerAdapter;
import com.boohee.record.CommonSportFragmennt;
import com.boohee.record.DietSportCalendarActivity;
import com.boohee.utility.ImageLoaderOptions;
import com.boohee.utils.ResolutionUtils;
import com.boohee.utils.ViewUtils;
import com.boohee.widgets.PagerSlidingTabStrip;
import com.nostra13.universalimageloader.core.ImageLoader;

import de.greenrobot.event.EventBus;

import java.util.ArrayList;
import java.util.List;

public class AddSportListActivity extends GestureActivity {
    private static final String KEY_DATE = "key_date";
    private int addCount;
    @InjectView(2131427474)
    ImageView iv_sport_cart;
    private CommonSportFragmennt mCommonSportFragmennt;
    private List<Fragment> mContentFragments = new ArrayList();
    private CustomSportFragment mCustomSportFragment;
    private NewBadgeView        mMessageBadge;
    @InjectView(2131427462)
    PagerSlidingTabStrip mSlidingTab;
    private String[] mTitles = new String[]{"常见", "我的"};
    @InjectView(2131427463)
    ViewPager mViewPager;
    private String record_on;

    @OnClick({2131427473, 2131429433})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_add_custom_sport:
                AddCustomSportActivity.start(this.activity, this.record_on);
                return;
            case R.id.rl_sport_search:
                SearchSportActivity.start(this.ctx, this.record_on);
                return;
            default:
                return;
        }
    }

    public static void start(Context context, String record_on) {
        Intent starter = new Intent(context, AddSportListActivity.class);
        starter.putExtra("key_date", record_on);
        context.startActivity(starter);
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac);
        EventBus.getDefault().register(this);
        ButterKnife.inject((Activity) this);
        handleIntent();
        initFragments();
        initViewPager();
    }

    private void handleIntent() {
        this.record_on = getStringExtra("key_date");
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.d, menu);
        new Handler().post(new Runnable() {
            public void run() {
                AddSportListActivity.this.mMessageBadge = new NewBadgeView(AddSportListActivity
                        .this.activity);
                AddSportListActivity.this.mMessageBadge.setTargetView(AddSportListActivity.this
                        .findViewById(R.id.action_finish));
                AddSportListActivity.this.mMessageBadge.setBadgeMargin(10, 5, 5, 0);
                AddSportListActivity.this.mMessageBadge.setBadgeGravity(53);
                AddSportListActivity.this.mMessageBadge.setTextColor(AddSportListActivity.this
                        .getResources().getColor(R.color.ju));
                AddSportListActivity.this.mMessageBadge.setBackground(10, ContextCompat.getColor
                        (AddSportListActivity.this.ctx, R.color.he));
            }
        });
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_finish:
                startActivity(new Intent(this.ctx, DietSportCalendarActivity.class));
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void initFragments() {
        this.mCommonSportFragmennt = CommonSportFragmennt.newInstance(this.record_on);
        this.mContentFragments.add(this.mCommonSportFragmennt);
        this.mCustomSportFragment = CustomSportFragment.newInstance(this.record_on);
        this.mContentFragments.add(this.mCustomSportFragment);
    }

    private void initViewPager() {
        this.mViewPager.setOffscreenPageLimit(this.mTitles.length);
        this.mViewPager.setAdapter(new ArrayPagerAdapter(getSupportFragmentManager(), this
                .mContentFragments, this.mTitles));
        this.mSlidingTab.setViewPager(this.mViewPager);
        this.mSlidingTab.setOnPageChangeListener(new SimpleOnPageChangeListener() {
            public void onPageSelected(int position) {
                if (position == 1 && AddSportListActivity.this.mCustomSportFragment.isFirstLoad) {
                    AddSportListActivity.this.mCustomSportFragment.firstLoad();
                }
            }
        });
    }

    private void setTranslateAnim(int total) {
        this.iv_sport_cart.setVisibility(0);
        int width = ResolutionUtils.getScreenWidth(this.ctx);
        float translationX = (((float) width) / 2.0f) - ((float) ViewUtils.dip2px(this.activity,
                2.0f));
        float translationY = (float) (-((ResolutionUtils.getScreenHeight(this.ctx) / 2) -
                ViewUtils.dip2px(this.activity, 24.0f)));
        float[] fArr = new float[2];
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(this.iv_sport_cart, "scaleX", new
                float[]{0.0f, 2.0f});
        fArr = new float[2];
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(this.iv_sport_cart, "scaleY", new
                float[]{0.0f, 2.0f});
        fArr = new float[2];
        ObjectAnimator scaleX2 = ObjectAnimator.ofFloat(this.iv_sport_cart, "scaleX", new
                float[]{2.0f, 0.5f});
        fArr = new float[2];
        ObjectAnimator scaleY2 = ObjectAnimator.ofFloat(this.iv_sport_cart, "scaleY", new
                float[]{2.0f, 0.5f});
        ObjectAnimator.ofFloat(this.iv_sport_cart, "translationX", new float[]{0.0f,
                translationX}).setInterpolator(new LinearInterpolator());
        ObjectAnimator.ofFloat(this.iv_sport_cart, "translationY", new float[]{0.0f,
                translationY}).setInterpolator(new DecelerateInterpolator());
        AnimatorSet set1 = new AnimatorSet();
        set1.setInterpolator(new OvershootInterpolator());
        set1.playTogether(new Animator[]{scaleX, scaleY});
        set1.setDuration(500);
        set1.start();
        final AnimatorSet set2 = new AnimatorSet();
        set2.playTogether(new Animator[]{transX, transY, scaleX2, scaleY2});
        set2.setDuration(600);
        set1.addListener(new AnimatorListenerAdapter() {
            public void onAnimationEnd(Animator animation) {
                set2.start();
            }
        });
        final int i = total;
        set2.addListener(new AnimatorListenerAdapter() {
            public void onAnimationEnd(Animator animation) {
                int i = 8;
                AddSportListActivity.this.iv_sport_cart.setVisibility(8);
                AddSportListActivity.this.iv_sport_cart.animate().translationX(0.0f).translationY
                        (0.0f).scaleX(1.0f).scaleY(1.0f);
                AddSportListActivity.this.mMessageBadge.setBadgeCount(i);
                NewBadgeView access$000 = AddSportListActivity.this.mMessageBadge;
                if (i > 0) {
                    i = 0;
                }
                access$000.setVisibility(i);
            }
        });
    }

    public void onEventMainThread(AddFinishAnimEvent event) {
        if (TextUtils.isEmpty(event.getThumb_image_name())) {
            ImageLoader.getInstance().displayImage("", this.iv_sport_cart, ImageLoaderOptions
                    .global((int) R.drawable.aa5));
        } else {
            ImageLoader.getInstance().displayImage(event.getThumb_image_name(), this
                    .iv_sport_cart, ImageLoaderOptions.global((int) R.drawable.aa5));
        }
        int i = this.addCount + 1;
        this.addCount = i;
        setTranslateAnim(i);
    }

    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
