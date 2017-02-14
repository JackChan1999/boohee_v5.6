package com.boohee.one.ui.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import com.boohee.api.ShopApi;
import com.boohee.model.FormatModel;
import com.boohee.model.Goods;
import com.boohee.model.Goods.goods_state;
import com.boohee.model.Goods.goods_type;
import com.boohee.model.GoodsFormat;
import com.boohee.model.Promotion;
import com.boohee.model.Promotion.FLASH_TYPE;
import com.boohee.model.Promotion.STATE_TYPE;
import com.boohee.model.status.Mention;
import com.boohee.myview.hybrid.HomeView;
import com.boohee.one.R;
import com.boohee.one.http.JsonCallback;
import com.boohee.one.http.client.BooheeClient;
import com.boohee.status.UserTimelineActivity;
import com.boohee.uchoice.GoodsDetailActivity.GoodsTab;
import com.boohee.uchoice.ShareGoodsDetails;
import com.boohee.utility.BooheeScheme;
import com.boohee.utility.Const;
import com.boohee.utility.Event;
import com.boohee.utility.ImageLoaderOptions;
import com.boohee.utils.DateFormatUtils;
import com.boohee.utils.DateHelper;
import com.boohee.utils.FastJsonUtils;
import com.boohee.utils.UrlUtils;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.analytics.MobclickAgent;
import com.viewpagerindicator.IconPageIndicator;
import com.viewpagerindicator.IconPagerAdapter;

import de.hdodenhof.circleimageview.CircleImageView;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

public class GoodsHomeFragment extends BaseFragment {
    private static final String URL_GOODS       = "/api/v1/goods/%d/detail.html";
    private static final int    WTAT_EXTRA_TIME = 0;
    @InjectView(2131427619)
    CircleImageView avatar;
    private Button btn_cart_add;
    @InjectView(2131427825)
    IconPageIndicator indicator;
    private boolean isTimeOut = false;
    @InjectView(2131429309)
    ImageView iv_arrow_right;
    private HeadImagePagerAdapter mAdapter;
    private Goods                 mGoods;
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) {
                GoodsHomeFragment.this.mRemainTime = GoodsHomeFragment.this.mRemainTime - 1000;
                if (GoodsHomeFragment.this.mRemainTime > 0 || GoodsHomeFragment.this.isTimeOut) {
                    GoodsHomeFragment.this.tv_month_quantity.setText(String.format
                            (GoodsHomeFragment.this.remainTimeString.toString(), new
                                    Object[]{DateFormatUtils.getRemainTime(GoodsHomeFragment.this
                                    .mRemainTime / 1000)}));
                    GoodsHomeFragment.this.tv_month_quantity.setTextColor(GoodsHomeFragment.this
                            .getResources().getColor(R.color.ju));
                    GoodsHomeFragment.this.tv_month_quantity.setBackgroundResource(R.drawable.c3);
                    return;
                }
                GoodsHomeFragment.this.isTimeOut = true;
                GoodsHomeFragment.this.mHandler.removeCallbacks(GoodsHomeFragment.this.mRunnable);
                GoodsHomeFragment.this.requestGood();
            }
        }
    };
    private OnOpenGoodsFormatListener mOnOpenGoodsFormatListener;
    private long     mRemainTime = 0;
    private Runnable mRunnable   = new Runnable() {
        public void run() {
            GoodsHomeFragment.this.mHandler.postDelayed(GoodsHomeFragment.this.mRunnable, 1000);
            GoodsHomeFragment.this.mHandler.sendEmptyMessage(0);
        }
    };
    private OnGoodsPageChangeListener onGoodsPageChangeListener;
    private List<String> photoList        = new ArrayList();
    private StringBuffer remainTimeString = new StringBuffer();
    @InjectView(2131428381)
    TextView     tvDescription;
    @InjectView(2131428889)
    TextView     tvEvaluate;
    @InjectView(2131428888)
    TextView     tvEvaluateCount;
    @InjectView(2131428097)
    TextView     tvNickname;
    @InjectView(2131428098)
    TextView     tvPostTime;
    @InjectView(2131428891)
    TextView     tvSale;
    @InjectView(2131429308)
    TextView     tv_format;
    @InjectView(2131428369)
    TextView     tv_good_title;
    @InjectView(2131429050)
    TextView     tv_market_price;
    @InjectView(2131429049)
    TextView     tv_market_title;
    @InjectView(2131429051)
    TextView     tv_month_quantity;
    @InjectView(2131427851)
    TextView     tv_price;
    @InjectView(2131428175)
    TextView     tv_title;
    @InjectView(2131428887)
    LinearLayout viewEvaluate;
    @InjectView(2131428371)
    View         view_divide_format;
    @InjectView(2131428370)
    View         view_format_tips;
    @InjectView(2131427463)
    ViewPager    viewpager;
    WebView webview;

    public class HeadImagePagerAdapter extends FragmentPagerAdapter implements IconPagerAdapter {
        protected final int[] ICONS = new int[]{R.drawable.fc, R.drawable.fd, R.drawable.fe, R
                .drawable.ff, R.drawable.fg, R.drawable.fh, R.drawable.fi, R.drawable.fj, R
                .drawable.fk};
        private List<String> mUrls;

        public HeadImagePagerAdapter(FragmentManager fm, List<String> urls) {
            super(fm);
            this.mUrls = urls;
        }

        public Fragment getItem(int arg0) {
            return HeadImageFragment.newInstance((String) this.mUrls.get(arg0));
        }

        public int getCount() {
            return this.mUrls == null ? 0 : this.mUrls.size();
        }

        public int getIconResId(int index) {
            return this.ICONS[index % this.ICONS.length];
        }
    }

    public interface OnGoodsPageChangeListener {
        void onGoodsPageChange(int i);
    }

    public interface OnOpenGoodsFormatListener {
        void onOpenGoodsFormat(TextView textView);
    }

    @OnClick({2131428370, 2131428890})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.view_format_tips:
                if (TextUtils.equals(goods_type.SpecGoods.name(), this.mGoods.type) && this
                        .mOnOpenGoodsFormatListener != null) {
                    this.mOnOpenGoodsFormatListener.onOpenGoodsFormat(this.tv_format);
                    return;
                }
                return;
            case R.id.view_more:
                MobclickAgent.onEvent(getActivity(), Event.SHOP_CLICKMORECOMMENTS);
                if (this.onGoodsPageChangeListener != null) {
                    this.onGoodsPageChangeListener.onGoodsPageChange(GoodsTab.POST.getPosition());
                    return;
                }
                return;
            default:
                return;
        }
    }

    private void startCountdown() {
        this.mHandler.removeCallbacks(this.mRunnable);
        this.mHandler.post(this.mRunnable);
        this.isTimeOut = false;
    }

    public static GoodsHomeFragment newInstance(Goods goods, Button button) {
        GoodsHomeFragment fragment = new GoodsHomeFragment();
        fragment.mGoods = goods;
        fragment.btn_cart_add = button;
        return fragment;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        HomeView view = new HomeView(inflater.getContext());
        view.setHeaderView(inflater.inflate(R.layout.h2, container, false));
        this.webview = view.getWebView();
        return view;
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.inject((Object) this, view);
        initGoodsView();
        initWebView();
        initGoodsIntroducePriceEvaluateSale();
    }

    @SuppressLint({"SetJavaScriptEnabled"})
    private void initWebView() {
        this.webview.getSettings().setJavaScriptEnabled(true);
        this.webview.getSettings().setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
        this.webview.setVerticalScrollBarEnabled(false);
        this.webview.setVerticalScrollbarOverlay(false);
        this.webview.setHorizontalScrollBarEnabled(false);
        this.webview.setHorizontalScrollbarOverlay(false);
        this.webview.loadUrl(UrlUtils.handleUrl(BooheeClient.build(BooheeClient.ONE)
                .getDefaultURL(String.format(URL_GOODS, new Object[]{Integer.valueOf(this.mGoods
                        .id)}))));
        this.webview.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (TextUtils.isEmpty(url) || GoodsHomeFragment.this.mGoods == null) {
                    return false;
                }
                if (url.contains("boohee://channel_posts")) {
                    GoodsHomeFragment.this.onGoodsPageChangeListener.onGoodsPageChange(GoodsTab
                            .POST.getPosition());
                } else {
                    BooheeScheme.handleUrl(GoodsHomeFragment.this.getActivity(), url);
                }
                return true;
            }
        });
    }

    private void requestGood() {
        if (this.mGoods != null) {
            ShopApi.getGoodsDetail(this.mGoods.id, getActivity(), new JsonCallback(getActivity()) {
                public void ok(JSONObject object) {
                    super.ok(object);
                    GoodsHomeFragment.this.mGoods = (Goods) FastJsonUtils.fromJson(object
                            .optJSONObject(BooheeScheme.GOODS), Goods.class);
                    if (GoodsHomeFragment.this.mGoods != null) {
                        if (TextUtils.equals(GoodsHomeFragment.this.mGoods.state, goods_state
                                .not_sale.name())) {
                            GoodsHomeFragment.this.btn_cart_add.setEnabled(false);
                            GoodsHomeFragment.this.btn_cart_add.setText(TextUtils.isEmpty
                                    (GoodsHomeFragment.this.mGoods.state_text) ? "补货中" :
                                    GoodsHomeFragment.this.mGoods.state_text);
                            GoodsHomeFragment.this.btn_cart_add.setBackgroundResource(R.drawable
                                    .df);
                        } else if (TextUtils.equals(GoodsHomeFragment.this.mGoods.state,
                                goods_state.on_sale.name())) {
                            GoodsHomeFragment.this.btn_cart_add.setEnabled(true);
                            GoodsHomeFragment.this.btn_cart_add.setText("加入购物车");
                            GoodsHomeFragment.this.btn_cart_add.setBackgroundResource(R.drawable
                                    .dk);
                        }
                        GoodsHomeFragment.this.initPromotion();
                        GoodsHomeFragment.this.initGoodsIntroducePriceEvaluateSale();
                    }
                }
            });
        }
    }

    private void initGoodsIntroducePriceEvaluateSale() {
        if (this.mGoods != null) {
            if (TextUtils.isEmpty(this.mGoods.description)) {
                this.tvDescription.setVisibility(8);
            } else {
                this.tvDescription.setVisibility(0);
                this.tvDescription.setText(this.mGoods.description);
            }
            this.tvSale.setText(String.format(getString(R.string.u9), new Object[]{Integer
                    .valueOf(this.mGoods.month_quantity), this.mGoods.unit_name}));
            if (this.mGoods != null && !TextUtils.isEmpty(this.mGoods.slug)) {
                ShopApi.getGoodsDetailEvaluate(this.mGoods.slug, getActivity(), new JsonCallback
                        (getActivity()) {
                    public void ok(JSONObject object) {
                        if (TextUtils.isEmpty(object.optString("errors"))) {
                            JSONObject post = object.optJSONObject(Mention.POST);
                            if (post != null) {
                                GoodsHomeFragment.this.viewEvaluate.setVisibility(0);
                                JSONObject user = post.optJSONObject(Const.USER);
                                String nickname = user.optString(UserTimelineActivity.NICK_NAME);
                                String avatarURL = user.optString("avatar_url");
                                int postCount = object.optInt("posts_count");
                                String body = post.optString("body");
                                String createdAt = post.optString("created_at");
                                GoodsHomeFragment.this.tvEvaluateCount.setText(String.format(" " +
                                        "(%s)", new Object[]{String.valueOf(postCount)}));
                                GoodsHomeFragment.this.tvEvaluate.setText(body);
                                GoodsHomeFragment.this.tvNickname.setText(nickname);
                                GoodsHomeFragment.this.tvPostTime.setText(DateHelper
                                        .timezoneFormat(createdAt, "MM-dd HH:mm"));
                                ImageLoader.getInstance().displayImage(avatarURL,
                                        GoodsHomeFragment.this.avatar, ImageLoaderOptions.avatar());
                            }
                        }
                    }

                    public void fail(String message) {
                        GoodsHomeFragment.this.viewEvaluate.setVisibility(8);
                    }
                });
            }
        }
    }

    private void initGoodsView() {
        if (this.mGoods != null) {
            this.tv_good_title.setText(this.mGoods.title);
            List<String> list = this.mGoods.square_photo_urls;
            if (list != null && list.size() > 0) {
                this.photoList.addAll(list);
                this.mAdapter = new HeadImagePagerAdapter(getChildFragmentManager(), this
                        .photoList);
                this.viewpager.setAdapter(this.mAdapter);
                if (this.photoList.size() > 1) {
                    this.indicator.setViewPager(this.viewpager);
                    this.indicator.setVisibility(0);
                } else {
                    this.indicator.setVisibility(8);
                }
                this.viewpager.getLayoutParams().height = getResources().getDisplayMetrics()
                        .widthPixels;
            }
            String basePrice = this.mGoods.base_price;
            String marketPrice = this.mGoods.market_price;
            this.tv_price.setText(getString(R.string.ae4) + " " + basePrice);
            initPromotion();
            initFormat();
        }
    }

    private void initPromotion() {
        this.remainTimeString.setLength(0);
        Promotion promotion = this.mGoods.flash_sale;
        if (promotion != null) {
            if (TextUtils.equals(STATE_TYPE.preview.name(), promotion.state)) {
                this.btn_cart_add.setText(TextUtils.isEmpty(promotion.state_text) ? "即将开始" :
                        promotion.state_text);
                if (TextUtils.equals(FLASH_TYPE.time.name(), promotion.flash_type)) {
                    this.remainTimeString.append(" %s 后开抢 ");
                } else if (TextUtils.equals(FLASH_TYPE.quota.name(), promotion.flash_type) ||
                        TextUtils.equals(FLASH_TYPE.both.name(), promotion.flash_type)) {
                    this.remainTimeString.append(" %s 后开抢\n限量 " + promotion.total_quota + " " +
                            this.mGoods.unit_name + " ");
                }
                this.mRemainTime = DateFormatUtils.getDifference(promotion.current_timestamp,
                        promotion.starts_at);
                if (this.mRemainTime > 0) {
                    this.mRemainTime += 3000;
                    startCountdown();
                }
            } else if (TextUtils.equals(STATE_TYPE.active.name(), promotion.state)) {
                if (TextUtils.equals(FLASH_TYPE.time.name(), promotion.flash_type)) {
                    this.remainTimeString.append(" 剩 %s ");
                } else if (TextUtils.equals(FLASH_TYPE.quota.name(), promotion.flash_type) ||
                        TextUtils.equals(FLASH_TYPE.both.name(), promotion.flash_type)) {
                    this.remainTimeString.append(" 剩 %s \n剩 " + promotion.current_quota + " " +
                            this.mGoods.unit_name + " ");
                }
                this.mRemainTime = DateFormatUtils.getDifference(promotion.current_timestamp,
                        promotion.expires_at);
                this.mRemainTime += 3000;
                if (this.mRemainTime > 0) {
                    startCountdown();
                }
            } else if (TextUtils.equals(STATE_TYPE.completed.name(), promotion.state)) {
                this.btn_cart_add.setText(TextUtils.isEmpty(promotion.state_text) ? "已抢光" :
                        promotion.state_text);
                this.btn_cart_add.setEnabled(false);
                this.tv_month_quantity.setText(TextUtils.isEmpty(promotion.state_text) ? "已抢光" :
                        promotion.state_text);
                this.tv_month_quantity.setTextColor(getResources().getColor(R.color.ju));
                this.tv_month_quantity.setBackgroundResource(R.drawable.c3);
            }
        }
    }

    private void initFormat() {
        if (TextUtils.equals(goods_type.SpecGoods.name(), this.mGoods.type) && this.mGoods.specs
                != null && this.mGoods.specs.size() > 0) {
            String format = getFormat(this.mGoods.specs);
            TextView textView = this.tv_format;
            StringBuilder append = new StringBuilder().append("请选择");
            if (TextUtils.isEmpty(format)) {
                format = " 商品规格";
            }
            textView.setText(append.append(format).toString());
            this.view_format_tips.setVisibility(0);
            this.view_divide_format.setVisibility(0);
            this.iv_arrow_right.setVisibility(0);
        } else if (this.mGoods.chosen_specs != null) {
            this.view_format_tips.setVisibility(0);
            this.view_divide_format.setVisibility(0);
            this.iv_arrow_right.setVisibility(8);
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < this.mGoods.chosen_specs.size(); i++) {
                sb.append(((FormatModel) this.mGoods.chosen_specs.get(i)).name + " ");
            }
            if (!TextUtils.isEmpty(sb.toString())) {
                this.tv_format.setText(sb.toString());
            }
        }
    }

    private String getFormat(List<GoodsFormat> specs) {
        StringBuffer stringBuffer = new StringBuffer();
        for (GoodsFormat format : specs) {
            stringBuffer.append(" " + format.name);
        }
        return stringBuffer.toString();
    }

    public void shareGoods() {
        if (this.mGoods != null) {
            new ShareGoodsDetails(getActivity(), 1, this.mGoods.share.share_description).execute
                    (new String[]{this.mGoods.share.share_image, this.mGoods.share.share_link});
        }
    }

    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    public void setOnGoodsPageChangeListener(OnGoodsPageChangeListener onGoodsPageChangeListener) {
        this.onGoodsPageChangeListener = onGoodsPageChangeListener;
    }

    public void setOnOpenGoodsFormatListener(OnOpenGoodsFormatListener onOpenGoodsFormatListener) {
        this.mOnOpenGoodsFormatListener = onOpenGoodsFormatListener;
    }

    public void onDestroy() {
        super.onDestroy();
        this.mHandler.removeCallbacks(this.mRunnable);
    }
}
