package com.boohee.status;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.design.widget.TabLayout.OnTabSelectedListener;
import android.support.design.widget.TabLayout.Tab;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewConfiguration;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.ViewFlipper;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import com.boohee.api.StatusApi;
import com.boohee.main.GestureActivity;
import com.boohee.model.status.Topic;
import com.boohee.model.status.TopicNotice;
import com.boohee.myview.ViewPagerHeaderScroll.fragment.BaseViewPagerFragment;
import com.boohee.myview.ViewPagerHeaderScroll.tools.ViewPagerHeaderHelper;
import com.boohee.myview.ViewPagerHeaderScroll.tools.ViewPagerHeaderHelper.OnViewPagerTouchListener;
import com.boohee.myview.ViewPagerHeaderScroll.widget.TouchCallbackLayout;
import com.boohee.myview.ViewPagerHeaderScroll.widget.TouchCallbackLayout.TouchEventListener;
import com.boohee.one.R;
import com.boohee.one.http.JsonCallback;
import com.boohee.one.http.JsonParams;
import com.boohee.one.http.client.BooheeClient;
import com.boohee.one.ui.StatusPostTextActivity;
import com.boohee.one.ui.fragment.BaseFragment;
import com.boohee.utility.BooheeScheme;
import com.boohee.utility.BuilderIntent;
import com.boohee.utility.ImageLoaderOptions;
import com.boohee.utils.Helper;
import com.boohee.utils.ResolutionUtils;
import com.boohee.utils.URLDecoderUtils;
import com.boohee.utils.ViewUtils;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

public class TopicActivity extends GestureActivity implements OnClickListener,
        TouchEventListener, OnViewPagerTouchListener {
    private static final float  DEFAULT_DAMPING  = 1.5f;
    private static final long   DEFAULT_DURATION = 300;
    public static final  int    DEFAULT_HEIGHT   = 320;
    public static final  int    DEFAULT_WIDTH    = 640;
    public static final  String EXTRA_TOPIC      = "extra_topic";
    static final         String TAG              = TopicActivity.class.getSimpleName();
    private final        String CARE_TOPIC_URL   = "/api/v1/topics/favorite";
    private BaseViewPagerFragment cummonFragment;
    private int currentIndex = 0;
    @InjectView(2131427531)
    FloatingActionButton fabButton;
    @InjectView(2131427768)
    FrameLayout          frameContent;
    @InjectView(2131427960)
    LinearLayout         headerLayout;
    private boolean isCare = false;
    @InjectView(2131427735)
    ImageView           ivTop;
    @InjectView(2131427959)
    TouchCallbackLayout layout;
    List<BaseViewPagerFragment> mFragments = new ArrayList();
    private int mHeaderHeight;
    private Interpolator mInterpolator = new DecelerateInterpolator();
    private Menu                  mMenu;
    private int                   mTabHeight;
    private String                mTitle;
    private Topic                 mTopic;
    private List<TopicNotice>     mTopicNotice;
    private int                   mTouchSlop;
    private ViewPagerHeaderHelper mViewPagerHeaderHelper;
    private MenuItem              menuItem;
    @InjectView(2131427961)
    ViewFlipper vfNotice;
    @InjectView(2131427497)
    TabLayout   viewTabs;

    public void onCreate(Bundle outState) {
        super.onCreate(outState);
        setContentView(R.layout.dn);
        ButterKnife.inject((Activity) this);
        this.mTitle = getStringExtra(EXTRA_TOPIC);
        this.mTitle = URLDecoderUtils.replaceAndDecode(this.mTitle);
        setTitle(this.mTitle);
        initView();
        getCurrentTopic();
        showLoading();
    }

    private void initView() {
        this.mTouchSlop = ViewConfiguration.get(this).getScaledTouchSlop();
        this.mViewPagerHeaderHelper = new ViewPagerHeaderHelper(this, this);
        ((TouchCallbackLayout) findViewById(R.id.layout)).setTouchEventListener(this);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        this.mMenu = menu;
        this.menuItem = menu.add(0, 3, 1, "关注");
        this.menuItem.setShowAsAction(2);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 3:
                careTopic();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void careTopic() {
        showLoading();
        if (this.mTopic != null) {
            JsonParams params = new JsonParams();
            params.put("id", this.mTopic.id);
            params.put("favorite", !this.mTopic.favorite);
            BooheeClient.build("status").post("/api/v1/topics/favorite", params, new JsonCallback
                    (this.ctx) {
                public void onFinish() {
                    super.onFinish();
                    TopicActivity.this.dismissLoading();
                }

                public void ok(JSONObject object) {
                    super.ok(object);
                    TopicActivity.this.mTopic.favorite = !TopicActivity.this.isCare;
                    TopicActivity.this.refreshMenu();
                }
            }, this.ctx);
        }
    }

    private void getCurrentTopic() {
        StatusApi.getTopicPosts(this.activity, this.mTitle, new JsonCallback(this.activity) {
            public void ok(JSONObject object) {
                super.ok(object);
                TopicActivity.this.mTopic = Topic.parseSelf(object.optJSONObject("topic"));
                TopicActivity.this.mTopicNotice = TopicNotice.parseList(object.optString
                        ("top_lines"));
                TopicActivity.this.initUI();
                TopicActivity.this.refreshMenu();
            }

            public void onFinish() {
                super.onFinish();
                TopicActivity.this.dismissLoading();
            }
        });
    }

    private void refreshMenu() {
        if (this.mTopic != null && this.menuItem != null) {
            this.isCare = this.mTopic.favorite;
            if (this.mTopic.favorite) {
                this.menuItem.setTitle("取消关注");
            } else {
                this.menuItem.setTitle("关注");
            }
        }
    }

    private void startImageLinkActivity() {
        BooheeScheme.handleUrl(this.ctx, this.mTopic.page_url);
    }

    @OnClick({2131427531})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab_button:
                new BuilderIntent(this.ctx, StatusPostTextActivity.class).putExtra
                        (StatusPostTextActivity.EXTRA_TEXT, "#" + this.mTitle + "#")
                        .startActivity();
                return;
            default:
                return;
        }
    }

    private void initUI() {
        if (this.mTopic != null) {
            if (TextUtils.isEmpty(this.mTopic.head_image_url)) {
                this.mHeaderHeight = 0;
            } else {
                ViewUtils.setViewScaleHeight(this, this.ivTop, 640, 320);
                this.imageLoader.displayImage(this.mTopic.head_image_url, this.ivTop,
                        ImageLoaderOptions.global(new ColorDrawable(866805452)));
                this.mHeaderHeight = ResolutionUtils.getHeight(this.ctx, 2, 1);
                ViewCompat.setTranslationY(this.frameContent, (float) this.mHeaderHeight);
                if (!TextUtils.isEmpty(this.mTopic.page_url)) {
                    this.ivTop.setOnClickListener(new OnClickListener() {
                        public void onClick(View v) {
                            TopicActivity.this.startImageLinkActivity();
                        }
                    });
                }
            }
            initFragments();
            initNotice();
        }
    }

    private void initNotice() {
        if (this.mTopicNotice == null || this.mTopicNotice.size() == 0) {
            this.vfNotice.setVisibility(8);
            return;
        }
        int marginTop = 48;
        if (this.mTopic.choice) {
            marginTop = 56;
        } else {
            ((LayoutParams) this.vfNotice.getLayoutParams()).bottomMargin = 0;
        }
        this.mHeaderHeight += ViewUtils.dip2px(this.ctx, (float) marginTop);
        ViewCompat.setTranslationY(this.frameContent, (float) this.mHeaderHeight);
        this.vfNotice.removeAllViews();
        for (final TopicNotice notice : this.mTopicNotice) {
            View itemNotice = LayoutInflater.from(this).inflate(R.layout.jg, null);
            TextView title = (TextView) itemNotice.findViewById(R.id.tv_title);
            this.imageLoader.displayImage(notice.icon_url, (ImageView) itemNotice.findViewById(R
                    .id.iv_icon), ImageLoaderOptions.global(new ColorDrawable(866805452)));
            title.setText(notice.title);
            itemNotice.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    BooheeScheme.handleUrl(TopicActivity.this.ctx, notice.url);
                }
            });
            this.vfNotice.addView(itemNotice);
        }
        this.vfNotice.setFlipInterval(BaseImageDownloader.DEFAULT_HTTP_CONNECT_TIMEOUT);
        this.vfNotice.startFlipping();
    }

    private void initFragments() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        this.cummonFragment = TopicFragment.newInstance(this.mTitle, false);
        this.mFragments.add(this.cummonFragment);
        if (this.mTopic.choice) {
            this.mFragments.add(TopicFragment.newInstance(this.mTitle, true));
            this.viewTabs.addTab(this.viewTabs.newTab().setText((CharSequence) "最新"));
            this.viewTabs.addTab(this.viewTabs.newTab().setText((CharSequence) "精选"));
            this.viewTabs.setTabMode(1);
            this.viewTabs.setVisibility(0);
            this.mTabHeight = getResources().getDimensionPixelSize(R.dimen.h0);
        } else {
            this.mTabHeight = 0;
            this.viewTabs.setVisibility(8);
        }
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) this.frameContent
                .getLayoutParams();
        params.topMargin = this.mTabHeight;
        this.frameContent.setLayoutParams(params);
        transaction.add((int) R.id.frame_content, this.cummonFragment);
        transaction.commitAllowingStateLoss();
        this.viewTabs.setOnTabSelectedListener(new OnTabSelectedListener() {
            public void onTabSelected(Tab tab) {
                TopicActivity.this.supportInvalidateOptionsMenu();
                switch (tab.getPosition()) {
                    case 0:
                        TopicActivity.this.switchFragment((BaseFragment) TopicActivity.this
                                .mFragments.get(0));
                        return;
                    case 1:
                        TopicActivity.this.switchFragment((BaseFragment) TopicActivity.this
                                .mFragments.get(1));
                        ((BaseViewPagerFragment) TopicActivity.this.mFragments.get(1)).loadFirst();
                        return;
                    default:
                        return;
                }
            }

            public void onTabUnselected(Tab tab) {
            }

            public void onTabReselected(Tab tab) {
                switch (tab.getPosition()) {
                    case 1:
                        ((BaseViewPagerFragment) TopicActivity.this.mFragments.get(1)).loadFirst();
                        return;
                    default:
                        return;
                }
            }
        });
    }

    private void switchFragment(BaseFragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        for (int i = 0; i < this.mFragments.size(); i++) {
            BaseFragment item = (BaseFragment) this.mFragments.get(i);
            this.currentIndex = i;
            if (item == fragment) {
                if (!fragment.isAdded()) {
                    transaction.add((int) R.id.frame_content, (Fragment) fragment);
                    fragment.loadFirst();
                }
                transaction.show(fragment);
            } else if (item.isAdded()) {
                transaction.hide(item);
            }
        }
        transaction.commitAllowingStateLoss();
    }

    public boolean onLayoutInterceptTouchEvent(MotionEvent event) {
        return this.mViewPagerHeaderHelper.onLayoutInterceptTouchEvent(event, this.mTabHeight +
                this.mHeaderHeight);
    }

    public boolean onLayoutTouchEvent(MotionEvent event) {
        return this.mViewPagerHeaderHelper.onLayoutTouchEvent(event);
    }

    public void onMoveStarted(float y) {
    }

    public void onMove(float y, float yDx) {
        float headerTranslationY = ViewCompat.getTranslationY(this.headerLayout) + yDx;
        if (headerTranslationY >= 0.0f) {
            headerExpand(0);
        } else if (headerTranslationY <= ((float) (-this.mHeaderHeight))) {
            headerFold(0);
        } else {
            ViewCompat.animate(this.headerLayout).translationY(headerTranslationY).setDuration(0)
                    .start();
            ViewCompat.animate(this.frameContent).translationY(((float) this.mHeaderHeight) +
                    headerTranslationY).setDuration(0).start();
        }
    }

    public void onMoveEnded(boolean isFling, float flingVelocityY) {
        float headerY = ViewCompat.getTranslationY(this.headerLayout);
        if (headerY != 0.0f && headerY != ((float) (-this.mHeaderHeight))) {
            if (this.mViewPagerHeaderHelper.getInitialMotionY() - this.mViewPagerHeaderHelper
                    .getLastMotionY() < ((float) (-this.mTouchSlop))) {
                headerExpand(headerMoveDuration(true, headerY, isFling, flingVelocityY));
            } else if (this.mViewPagerHeaderHelper.getInitialMotionY() - this
                    .mViewPagerHeaderHelper.getLastMotionY() > ((float) this.mTouchSlop)) {
                headerFold(headerMoveDuration(false, headerY, isFling, flingVelocityY));
            } else if (headerY > ((float) (-this.mHeaderHeight)) / 2.0f) {
                headerExpand(headerMoveDuration(true, headerY, isFling, flingVelocityY));
            } else {
                headerFold(headerMoveDuration(false, headerY, isFling, flingVelocityY));
            }
        }
    }

    private long headerMoveDuration(boolean isExpand, float currentHeaderY, boolean isFling,
                                    float velocityY) {
        if (!isFling) {
            return DEFAULT_DURATION;
        }
        float distance;
        if (isExpand) {
            distance = ((float) Math.abs(this.mHeaderHeight)) - Math.abs(currentHeaderY);
        } else {
            distance = Math.abs(currentHeaderY);
        }
        long defaultDuration = (long) ((distance / (Math.abs(velocityY) / 1000.0f)) *
                DEFAULT_DAMPING);
        return defaultDuration > DEFAULT_DURATION ? DEFAULT_DURATION : defaultDuration;
    }

    private void headerFold(long duration) {
        ViewCompat.animate(this.headerLayout).translationY((float) (-this.mHeaderHeight))
                .setDuration(duration).setInterpolator(this.mInterpolator).start();
        ViewCompat.animate(this.frameContent).translationY(0.0f).setDuration(duration)
                .setInterpolator(this.mInterpolator).start();
        this.mViewPagerHeaderHelper.setHeaderExpand(false);
    }

    private void headerExpand(long duration) {
        ViewCompat.animate(this.headerLayout).translationY(0.0f).setDuration(duration)
                .setInterpolator(this.mInterpolator).start();
        ViewCompat.animate(this.frameContent).translationY((float) this.mHeaderHeight)
                .setDuration(duration).setInterpolator(this.mInterpolator).start();
        this.mViewPagerHeaderHelper.setHeaderExpand(true);
    }

    public boolean isViewBeingDragged(MotionEvent event) {
        if (this.mFragments == null || this.mFragments.size() == 0) {
            return true;
        }
        return ((BaseViewPagerFragment) this.mFragments.get(this.currentIndex))
                .isViewBeingDragged(event);
    }

    public static void comeOnBaby(Context context, String topic) {
        if (context == null) {
            Helper.showToast((CharSequence) "Start SearcherActivity fail, context is null!");
            return;
        }
        Intent intent = new Intent(context, TopicActivity.class);
        intent.putExtra(EXTRA_TOPIC, topic);
        context.startActivity(intent);
    }
}
