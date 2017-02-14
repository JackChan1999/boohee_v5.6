package com.boohee.uchoice;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.Keyframe;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.SimpleOnPageChangeListener;
import android.support.v7.app.ActionBar.LayoutParams;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import com.boohee.api.ShopApi;
import com.boohee.apn.ApnActivity;
import com.boohee.main.FeedBackSwitcher;
import com.boohee.main.GestureActivity;
import com.boohee.model.Goods;
import com.boohee.model.Goods.goods_state;
import com.boohee.model.Goods.goods_type;
import com.boohee.myview.IntFloatWheelView;
import com.boohee.myview.NewBadgeView;
import com.boohee.one.R;
import com.boohee.one.http.JsonCallback;
import com.boohee.one.ui.fragment.GoodsHomeFragment;
import com.boohee.one.ui.fragment.GoodsHomeFragment.OnGoodsPageChangeListener;
import com.boohee.one.ui.fragment.GoodsHomeFragment.OnOpenGoodsFormatListener;
import com.boohee.one.ui.fragment.GoodsPostsFragment;
import com.boohee.utility.BooheeScheme;
import com.boohee.utility.Event;
import com.boohee.utils.FastJsonUtils;
import com.boohee.utils.Helper;
import com.boohee.utils.MeiQiaHelper;
import com.boohee.widgets.GoodsFormatPopupWindow;
import com.boohee.widgets.GoodsFormatPopupWindow.OnSelectListener;
import com.boohee.widgets.PagerSlidingTabStrip;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

public class GoodsDetailActivity extends GestureActivity implements OnGoodsPageChangeListener {
    public static final String GOODS_ID = "goods_id";
    @InjectView(2131428287)
    Button btn_buy_immediately;
    @InjectView(2131428975)
    Button btn_cart_add;
    private Goods              goods;
    private GoodsHomeFragment  goodsHomeFragment;
    private GoodsPostsFragment goodsPostsFragment;
    private boolean isSecondLoad = false;
    @InjectView(2131427689)
    ImageView iv_shopping_cart;
    private ObjectAnimator mAnimator;
    private NewBadgeView   mCartBadgeView;
    private List<Fragment> mFragments = new ArrayList();
    private int mGoodsId;
    private Handler mHandler = new Handler();
    private PagerSlidingTabStrip mSlidingTab;
    TextView mTvFormat;
    Runnable runnable = new Runnable() {
        public void run() {
            if (GoodsDetailActivity.this.viewBuy.getVisibility() == 0) {
                Animation anim = AnimationUtils.loadAnimation(GoodsDetailActivity.this.activity,
                        R.anim.al);
                anim.setAnimationListener(new AnimationListener() {
                    public void onAnimationStart(Animation animation) {
                    }

                    public void onAnimationEnd(Animation animation) {
                        GoodsDetailActivity.this.viewBuy.setVisibility(4);
                    }

                    public void onAnimationRepeat(Animation animation) {
                    }
                });
                GoodsDetailActivity.this.viewBuy.startAnimation(anim);
            }
        }
    };
    private int total;
    @InjectView(2131427687)
    LinearLayout viewBuy;
    @InjectView(2131428973)
    ImageView    viewCart;
    @InjectView(2131427463)
    ViewPager    viewpager;

    private class GoodsPagerAdapter extends FragmentPagerAdapter {
        private List<Fragment> mFragments;
        private String[] mTitles = new String[]{GoodsTab.GOODS.getName(), GoodsTab.POST.getName()};

        public GoodsPagerAdapter(FragmentManager fm, List<Fragment> fragments) {
            super(fm);
            this.mFragments = fragments;
        }

        public Fragment getItem(int position) {
            return (Fragment) this.mFragments.get(position);
        }

        public int getCount() {
            return this.mFragments.size();
        }

        public CharSequence getPageTitle(int position) {
            return this.mTitles[position];
        }
    }

    public enum GoodsTab {
        GOODS(0, "商品"),
        POST(1, "评价");

        private String name;
        private int    position;

        private GoodsTab(int position, String name) {
            this.position = position;
            this.name = name;
        }

        public String getName() {
            return this.name;
        }

        public int getPosition() {
            return this.position;
        }
    }

    @OnClick({2131428973, 2131427688, 2131428975, 2131428286, 2131428287})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_buy_now:
                gotoCart();
                return;
            case R.id.view_contact_us:
                MobclickAgent.onEvent(this.ctx, Event.SHOP_CLICKPRODUCTSERVICE);
                if (FeedBackSwitcher.isFeedbackTime()) {
                    MeiQiaHelper.startChat(this);
                    return;
                } else {
                    ApnActivity.comeOnBaby(this, true);
                    return;
                }
            case R.id.btn_buy_immediately:
                MobclickAgent.onEvent(this.ctx, Event.SHOP_CLICKQUICKBUY);
                if (this.goods == null || !TextUtils.equals(goods_type.SpecGoods.name(), this
                        .goods.type)) {
                    OrderEditActivity.start(this, this.mGoodsId, 1, false);
                    return;
                } else {
                    openFormatPop(true);
                    return;
                }
            case R.id.view_cart:
                gotoCart();
                return;
            case R.id.btn_cart_add:
                MobclickAgent.onEvent(this.ctx, "shop_clickCart");
                if (this.goods == null || !TextUtils.equals(goods_type.SpecGoods.name(), this
                        .goods.type)) {
                    addGoodToCart(this.mGoodsId, 1);
                    return;
                } else {
                    openFormatPop(false);
                    return;
                }
            default:
                return;
        }
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MobclickAgent.onEvent(this.ctx, Event.SHOP_VIEW_PRODUCT_DETAIL_PAGE);
        setContentView(R.layout.be);
        ButterKnife.inject((Activity) this);
        initBadgeView();
        handleIntent();
        requestGood();
        requestGoodsCount();
    }

    private void initBadgeView() {
        if (this.mCartBadgeView == null) {
            this.mCartBadgeView = new NewBadgeView(this.ctx);
            this.mCartBadgeView.setTargetView(this.viewCart);
            this.mCartBadgeView.setTextColor(-1);
            this.mCartBadgeView.setBackground(10, getResources().getColor(R.color.he));
        }
    }

    private void handleIntent() {
        Intent intent = getIntent();
        if (intent != null) {
            this.mGoodsId = intent.getIntExtra("goods_id", 0);
            if (this.mGoodsId > 0) {
                return;
            }
        }
        Helper.showLong((CharSequence) "缺少参数");
        finish();
    }

    private void openFormatPop(final boolean isBuyNow) {
        final GoodsFormatPopupWindow goodsFormatPop = GoodsFormatPopupWindow.getInstance();
        goodsFormatPop.show(this.activity, this.goods);
        goodsFormatPop.setOnSelectListener(new OnSelectListener() {
            public void onSelect(boolean canDismiss, int good_id, int quantity, String tips) {
                if (!(GoodsDetailActivity.this.mTvFormat == null || TextUtils.isEmpty(tips))) {
                    GoodsDetailActivity.this.mTvFormat.setText(tips);
                }
                if (good_id != -1 && canDismiss) {
                    goodsFormatPop.dismiss();
                    if (isBuyNow) {
                        OrderEditActivity.start(GoodsDetailActivity.this, good_id, quantity, false);
                    } else {
                        GoodsDetailActivity.this.addGoodToCart(good_id, quantity);
                    }
                }
            }
        });
    }

    private void requestGood() {
        ShopApi.getGoodsDetail(this.mGoodsId, this, new JsonCallback(this) {
            public void ok(JSONObject object) {
                super.ok(object);
                GoodsDetailActivity.this.goods = (Goods) FastJsonUtils.fromJson(object
                        .optJSONObject(BooheeScheme.GOODS), Goods.class);
                if (GoodsDetailActivity.this.goods != null) {
                    GoodsDetailActivity.this.setTitle(GoodsDetailActivity.this.goods.title);
                    GoodsDetailActivity.this.initGoodsView();
                    GoodsDetailActivity.this.initFragments();
                }
            }
        });
    }

    private void initActionbar() {
        View view_tab = LayoutInflater.from(this).inflate(R.layout.h1, null);
        this.mSlidingTab = (PagerSlidingTabStrip) view_tab.findViewById(R.id.sliding_tabs);
        LayoutParams layoutParams = new LayoutParams(-2, -1);
        layoutParams.gravity = GravityCompat.END;
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(view_tab, layoutParams);
        this.mSlidingTab.setViewPager(this.viewpager);
        this.mSlidingTab.setOnPageChangeListener(new SimpleOnPageChangeListener() {
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                if (position == 1 && !GoodsDetailActivity.this.isSecondLoad) {
                    GoodsDetailActivity.this.goodsPostsFragment.loadFirst();
                    GoodsDetailActivity.this.isSecondLoad = true;
                }
            }
        });
    }

    private void initFragments() {
        if (this.goods != null && !TextUtils.isEmpty(this.goods.slug)) {
            this.goodsHomeFragment = GoodsHomeFragment.newInstance(this.goods, this.btn_cart_add);
            this.goodsHomeFragment.setOnGoodsPageChangeListener(this);
            this.goodsHomeFragment.setOnOpenGoodsFormatListener(new OnOpenGoodsFormatListener() {
                public void onOpenGoodsFormat(TextView tv_format) {
                    GoodsDetailActivity.this.mTvFormat = tv_format;
                    GoodsDetailActivity.this.openFormatPop(false);
                }
            });
            this.mFragments.add(this.goodsHomeFragment);
            this.goodsPostsFragment = GoodsPostsFragment.newInstance(this.goods.slug);
            this.mFragments.add(this.goodsPostsFragment);
            this.viewpager.setOffscreenPageLimit(2);
            this.viewpager.setAdapter(new GoodsPagerAdapter(getSupportFragmentManager(), this
                    .mFragments));
            initActionbar();
        }
    }

    public void onGoodsPageChange(int position) {
        this.viewpager.setCurrentItem(position, true);
    }

    protected void onResume() {
        super.onResume();
        this.mHandler.post(new Runnable() {
            public void run() {
                GoodsDetailActivity.this.requestGoodsCount();
            }
        });
    }

    protected void onDestroy() {
        super.onDestroy();
        this.mHandler.removeCallbacksAndMessages(null);
    }

    public void onBackPressed() {
        finish();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.f, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_share:
                if (this.goodsHomeFragment != null) {
                    this.goodsHomeFragment.shareGoods();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void gotoCart() {
        Intent cartIntent = new Intent(this.ctx, CartActivity.class);
        cartIntent.addFlags(67108864);
        cartIntent.addFlags(268435456);
        this.ctx.startActivity(cartIntent);
    }

    private void gotoEdit() {
        Intent orderIntent = new Intent(this.ctx, OrderEditActivity.class);
        orderIntent.putExtra("goods_id", this.mGoodsId);
        orderIntent.putExtra("isCart", false);
        this.ctx.startActivity(orderIntent);
    }

    private void requestGoodsCount() {
        ShopApi.getCarts(this, new JsonCallback(this) {
            public void ok(JSONObject object) {
                super.ok(object);
                int total = 0;
                List<Goods> goodsList = FastJsonUtils.parseList(object.optString("item"), Goods
                        .class);
                if (goodsList != null && goodsList.size() > 0) {
                    for (Goods goods : goodsList) {
                        total += goods.quantity;
                    }
                }
                GoodsDetailActivity.this.mCartBadgeView.setText(String.valueOf(total));
                GoodsDetailActivity.this.mCartBadgeView.setVisibility(total > 0 ? 0 : 8);
            }
        });
    }

    private void addGoodToCart(int goodsId, int quantity) {
        ShopApi.addCarts(goodsId, quantity, this, new JsonCallback(this) {
            public void ok(JSONObject object) {
                super.ok(object);
                GoodsDetailActivity.this.total = 0;
                List<Goods> goodsList = FastJsonUtils.parseList(object.optString("item"), Goods
                        .class);
                if (goodsList != null && goodsList.size() > 0) {
                    for (Goods goods : goodsList) {
                        GoodsDetailActivity.this.total = GoodsDetailActivity.this.total + goods
                                .quantity;
                    }
                }
                if (GoodsDetailActivity.this.total > 0) {
                    GoodsDetailActivity.this.setTranslateAnim();
                }
            }
        });
    }

    private void setTranslateAnim() {
        if (this.mAnimator == null) {
            int i;
            float width = (float) (getResources().getDisplayMetrics().widthPixels / 2);
            float padding = getResources().getDimension(R.dimen.hl);
            float x2 = padding - width;
            float x3 = x2 / 2.0f;
            float a = (((0.0f * (x2 - x3)) + (0.0f * (x3 - 0.0f))) + (IntFloatWheelView
                    .DEFAULT_VALUE * (0.0f - x2))) / (((0.0f * (x2 - x3)) + ((x2 * x2) * (x3 - 0
            .0f))) + ((x3 * x3) * (0.0f - x2)));
            float b = (0.0f / (0.0f - x2)) - ((0.0f + x2) * a);
            float c = (0.0f - (0.0f * a)) - (0.0f * b);
            int count = (int) Math.abs(width - padding);
            Keyframe[] keyframes = new Keyframe[count];
            float keyStep = 1.0f / ((float) count);
            float key = keyStep;
            for (i = 0; i < count; i++) {
                keyframes[i] = Keyframe.ofFloat(key, (float) (-i));
                key += keyStep;
            }
            PropertyValuesHolder pvhX = PropertyValuesHolder.ofKeyframe("translationX", keyframes);
            key = keyStep;
            for (i = 0; i < count; i++) {
                keyframes[i] = Keyframe.ofFloat(key, -getY(a, b, c, (float) (-i)));
                key += keyStep;
            }
            PropertyValuesHolder pvhY = PropertyValuesHolder.ofKeyframe("translationY", keyframes);
            this.mAnimator = ObjectAnimator.ofPropertyValuesHolder(this.iv_shopping_cart, new
                    PropertyValuesHolder[]{pvhY, pvhX}).setDuration(400);
            this.mAnimator.setInterpolator(new AccelerateInterpolator());
            this.mAnimator.addListener(new AnimatorListenerAdapter() {
                public void onAnimationCancel(Animator animation) {
                    GoodsDetailActivity.this.iv_shopping_cart.setVisibility(8);
                    GoodsDetailActivity.this.iv_shopping_cart.animate().translationX(0.0f)
                            .translationY(0.0f).scaleX(1.0f).scaleY(1.0f);
                    GoodsDetailActivity.this.viewBuy.setVisibility(4);
                    GoodsDetailActivity.this.mHandler.removeCallbacks(GoodsDetailActivity.this
                            .runnable);
                }

                public void onAnimationEnd(Animator animation) {
                    GoodsDetailActivity.this.animEnd();
                }
            });
        }
        if (!this.mAnimator.isRunning()) {
            this.mHandler.removeCallbacks(this.runnable);
            this.iv_shopping_cart.setVisibility(0);
            this.mAnimator.start();
        }
    }

    private void animEnd() {
        int i = 8;
        this.iv_shopping_cart.setVisibility(8);
        this.iv_shopping_cart.animate().translationX(0.0f).translationY(0.0f).scaleX(1.0f).scaleY
                (1.0f);
        this.mCartBadgeView.setText(String.valueOf(this.total));
        NewBadgeView newBadgeView = this.mCartBadgeView;
        if (this.total > 0) {
            i = 0;
        }
        newBadgeView.setVisibility(i);
        if (this.viewBuy.getVisibility() != 0) {
            this.viewBuy.setVisibility(0);
            this.viewBuy.startAnimation(AnimationUtils.loadAnimation(this.activity, R.anim.s));
        }
        this.mHandler.postDelayed(this.runnable, 3000);
    }

    private float getY(float a, float b, float c, float x) {
        return (((a * x) * x) + (b * x)) + c;
    }

    private void initGoodsView() {
        boolean status = TextUtils.equals(this.goods.state, goods_state.not_sale.name()) ||
                TextUtils.equals(this.goods.state, goods_state.initial.name());
        if (status || TextUtils.equals(this.goods.state, goods_state.stop_sale.name())) {
            status = true;
        } else {
            status = false;
        }
        if (status) {
            this.btn_buy_immediately.setEnabled(false);
            this.btn_cart_add.setEnabled(false);
            this.btn_cart_add.setText(TextUtils.isEmpty(this.goods.state_text) ? "" : this.goods
                    .state_text);
            this.btn_cart_add.setBackgroundResource(R.drawable.d9);
            return;
        }
        this.btn_buy_immediately.setEnabled(true);
        this.btn_cart_add.setEnabled(true);
        this.btn_cart_add.setText(getResources().getString(R.string.b8));
        this.btn_cart_add.setBackgroundResource(R.drawable.d9);
    }

    public static void comeOnBaby(Context context, int goodsID) {
        if (context != null && goodsID > 0) {
            Intent intent = new Intent(context, GoodsDetailActivity.class);
            intent.putExtra("goods_id", goodsID);
            context.startActivity(intent);
        }
    }
}
