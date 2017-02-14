package com.boohee.one.ui;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.RectF;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.design.widget.TabLayout.OnTabSelectedListener;
import android.support.design.widget.TabLayout.Tab;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.Toolbar.LayoutParams;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;

import com.boohee.api.MessengerApi;
import com.boohee.api.OneApi;
import com.boohee.api.StatusApi;
import com.boohee.database.OnePreference;
import com.boohee.database.UserPreference;
import com.boohee.food.AddFoodListActivity;
import com.boohee.food.AddSportListActivity;
import com.boohee.model.mine.Measure.MeasureType;
import com.boohee.modeldao.UserDao;
import com.boohee.myview.BadgeView;
import com.boohee.myview.highlight.HighLight;
import com.boohee.myview.highlight.HighLight.MarginInfo;
import com.boohee.myview.highlight.HighLight.OnHighLightClickListener;
import com.boohee.myview.highlight.HighLight.OnPosCallback;
import com.boohee.myview.homeMenu.PopMenu;
import com.boohee.myview.homeMenu.PopMenu.Builder;
import com.boohee.myview.homeMenu.PopMenu.PopMenuItemListener;
import com.boohee.myview.homeMenu.PopMenuItem;
import com.boohee.one.MyApplication;
import com.boohee.one.R;
import com.boohee.one.bet.BetUtil;
import com.boohee.one.cache.CacheKey;
import com.boohee.one.cache.FileCache;
import com.boohee.one.event.ConstEvent;
import com.boohee.one.event.LogoutEvent;
import com.boohee.one.http.JsonCallback;
import com.boohee.one.http.client.BooheeClient;
import com.boohee.one.sync.SyncHelper;
import com.boohee.one.ui.fragment.AddMeasureFragment;
import com.boohee.one.ui.fragment.BaseDialogFragment.onChangeListener;
import com.boohee.one.ui.fragment.BaseFragment;
import com.boohee.one.ui.fragment.HomeMineFragment;
import com.boohee.one.ui.fragment.HomeNewFragment;
import com.boohee.one.ui.fragment.PartnerFragment;
import com.boohee.one.ui.fragment.ShopMainFragment;
import com.boohee.one.update.UpdateAgent;
import com.boohee.push.PushManager;
import com.boohee.record.DimensionRecordActivity;
import com.boohee.record.WeightRecordFragment;
import com.boohee.status.MsgCategoryActivity;
import com.boohee.utility.BuilderIntent;
import com.boohee.utility.DensityUtil;
import com.boohee.utility.Event;
import com.boohee.utils.AccountUtils;
import com.boohee.utils.BadgeUtils;
import com.boohee.utils.BooheeAlert;
import com.boohee.utils.DateHelper;
import com.boohee.utils.Helper;
import com.boohee.utils.WheelUtils;
import com.boohee.widgets.CheckAccountPopwindow;
import com.boohee.widgets.LightAlertDialog;
import com.meizu.flyme.reflect.StatusBarProxy;
import com.umeng.analytics.MobclickAgent;

import de.greenrobot.event.EventBus;
import de.hdodenhof.circleimageview.CircleImageView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.json.JSONObject;

public class MainActivity extends BaseActivity {
    public static final int    ADD_MENU        = 2;
    public static final int    COUPLE          = 1;
    public static final int    HOME            = 0;
    public static final String KEY_ONNEWINTENT = "onnewintent";
    public static final int    MENU_BREAKFAST  = 0;
    public static final int    MENU_DINNER     = 2;
    public static final int    MENU_EXTRA_MEAL = 8;
    public static final int    MENU_LUNCH      = 1;
    public static final int    MENU_MC         = 6;
    public static final int    MENU_MEASURE    = 5;
    public static final int    MENU_SPORT      = 3;
    public static final int    MENU_STATUS     = 7;
    public static final int    MENU_WEIGHT     = 4;
    public static final int    MINE            = 4;
    public static final int    SHOP            = 3;
    public static final String TAG             = MainActivity.class.getSimpleName();
    public static CircleImageView ivAvatar;
    private static final String[] mTabs   = new String[]{"减肥", "伙伴", "商店", "我"};
    private static final int[]    mTabsId = new int[]{R.drawable.i_, R.drawable.ib, R.drawable
            .ic, R.drawable.id};
    private FileCache cache;
    private Context   ctx;
    @InjectView(2131427479)
    ImageView divider;
    private ImageView ivAddMenu;
    private long               mExitTime  = 0;
    private List<BaseFragment> mFragments = new ArrayList();
    private Handler            mHandler   = new Handler();
    private BadgeView mMessageBadge;
    private TabLayout mPartnerTab;
    private ImageView mShopBadge;
    private TextView  mTitle;
    private Menu      menu;
    private int       messageCount;
    private int       oldPosition;
    private PopMenu   popMenu;
    private ImageView searchView;
    private Toolbar   toolbar;
    public String userName = "";
    @InjectView(2131427497)
    TabLayout viewTabs;
    private WeightRecordFragment weightRecordFragment;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.ctx = this;
        setContentView(View.inflate(this, R.layout.bz, null));
        ButterKnife.inject((Activity) this);
        initToolbar();
        initPartner();
        EventBus.getDefault().register(this);
        init();
        initFragments();
        showAlert();
        StatusBarProxy.setStatusBarDarkIcon(getWindow(), false);
        BetUtil.showBetDialog(this.ctx);
        initFoodUnit();
    }

    private void checkGuide() {
        if (!OnePreference.getPrefHomeMenu()) {
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    MainActivity.this.showHighLight();
                }
            }, 500);
        }
    }

    private void showHighLight() {
        try {
            HighLight highLight = new HighLight(this.ctx).addHighLight(this.ivAddMenu, (int) R
                    .layout.pb, new OnPosCallback() {
                public void getPos(float rightMargin, float bottomMargin, RectF rectF, MarginInfo
                        marginInfo) {
                    marginInfo.bottomMargin = (float) DensityUtil.dip2px(MainActivity.this.ctx,
                            60.0f);
                }
            });
            highLight.show();
            highLight.setOnHighLightClickListener(new OnHighLightClickListener() {
                public void onClick() {
                    OnePreference.setPrefHomeMenu(true);
                }
            });
        } catch (Exception e) {
        }
    }

    private void initFoodUnit() {
        this.cache = FileCache.get(this.ctx);
        if (this.cache.getAsJSONObject(CacheKey.EATING_RECENT) == null) {
            BooheeClient.build("record").get("/api/v2/eatings/recent.json", new JsonCallback(this
                    .ctx) {
                public void ok(JSONObject object) {
                    super.ok(object);
                    if (object != null) {
                        MainActivity.this.cache.put(CacheKey.EATING_RECENT, object);
                    }
                }
            }, this.ctx);
        }
    }

    public void setToolBarColor(final int color, final boolean dividerVisible) {
        this.mHandler.post(new Runnable() {
            public void run() {
                boolean z = false;
                MainActivity.this.toolbar.setBackgroundColor(ContextCompat.getColor(MainActivity
                        .this.ctx, color));
                MainActivity.this.divider.setVisibility(dividerVisible ? 0 : 4);
                MainActivity mainActivity = MainActivity.this;
                if (!dividerVisible) {
                    z = true;
                }
                mainActivity.setMenuColor(z);
            }
        });
    }

    private void setMenuColor(boolean firstTab) {
        if (this.menu != null && this.menu.size() > 0) {
            MenuItem messageItem = this.menu.findItem(R.id.action_message);
            if (messageItem == null) {
                return;
            }
            if (firstTab) {
                messageItem.setIcon(R.drawable.a53);
                getSupportActionBar().setHomeAsUpIndicator((int) R.drawable.a56);
                return;
            }
            messageItem.setIcon(R.drawable.a52);
            getSupportActionBar().setHomeAsUpIndicator((int) R.drawable.a55);
        }
    }

    private void initToolbar() {
        this.toolbar = (Toolbar) findViewById(R.id.toolbar);
        setToolBarColor(17170445, false);
        if (this.toolbar != null) {
            setSupportActionBar(this.toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(false);
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setHomeAsUpIndicator((int) R.drawable.a56);
        }
    }

    private void init() {
        if (AccountUtils.isReleaseUser()) {
            MobclickAgent.onEvent(this, Event.HOME_HOMEPAGE);
            PushManager.getInstance().initPush(this);
            PushManager.getInstance().bindRegId(this);
            UpdateAgent.update(this);
            MyApplication.setIsMainActivityOpened(true);
            BadgeUtils.setIconBadge(this, 0);
            ColorStateList color = getResources().getColorStateList(R.color.a3);
            for (int i = 0; i < mTabs.length; i++) {
                View view = View.inflate(this, R.layout.mx, null);
                ((ImageView) view.findViewById(R.id.iv_icon)).setImageResource(mTabsId[i]);
                TextView textView = (TextView) view.findViewById(R.id.tv_tab);
                textView.setText(mTabs[i]);
                textView.setTextColor(color);
                if (i == 3) {
                    this.mShopBadge = (ImageView) view.findViewById(R.id.iv_index);
                }
                this.viewTabs.addTab(this.viewTabs.newTab().setCustomView(view));
            }
            this.ivAddMenu = new ImageView(this.ctx);
            this.ivAddMenu.setScaleType(ScaleType.CENTER_INSIDE);
            this.ivAddMenu.setImageResource(R.drawable.a4l);
            this.viewTabs.addTab(this.viewTabs.newTab().setCustomView(this.ivAddMenu), 2, false);
            return;
        }
        WelcomeActivity.comeOnBaby(this);
        finish();
    }

    public TabLayout getPartnerTab() {
        if (this.mPartnerTab == null) {
            initPartner();
        }
        return this.mPartnerTab;
    }

    public TextView getToolbarTitle() {
        if (this.mTitle == null) {
            initPartner();
        }
        return this.mTitle;
    }

    private void initPartner() {
        if (this.mPartnerTab == null) {
            this.mPartnerTab = (TabLayout) LayoutInflater.from(this).inflate(R.layout.qd, null,
                    false);
            this.mPartnerTab.setVisibility(8);
            Tab tabOne = this.mPartnerTab.newTab();
            Tab tabTwo = this.mPartnerTab.newTab();
            this.mPartnerTab.addTab(tabOne, true);
            this.mPartnerTab.addTab(tabTwo, false);
            this.mPartnerTab.setTabMode(0);
            this.mPartnerTab.setTabTextColors(ContextCompat.getColor(this.ctx, R.color.il),
                    ContextCompat.getColor(this.ctx, R.color.hb));
            this.mPartnerTab.setSelectedTabIndicatorColor(ContextCompat.getColor(this.ctx, R
                    .color.hb));
            this.mPartnerTab.setPadding(0, 0, 0, 0);
            LayoutParams params = new LayoutParams(-2, -1);
            params.gravity = 17;
            this.toolbar.addView(this.mPartnerTab, params);
            this.mTitle = (TextView) View.inflate(this, R.layout.n7, null);
            this.mTitle.setVisibility(8);
            this.toolbar.addView(this.mTitle, params);
        }
    }

    private void showAlert() {
        this.mHandler.postDelayed(new Runnable() {
            public void run() {
                BooheeAlert.showAllDialog(MainActivity.this);
            }
        }, 2000);
    }

    private void initFragments() {
        this.mFragments.add(new HomeNewFragment());
        this.mFragments.add(new PartnerFragment());
        this.mFragments.add(new ShopMainFragment());
        this.mFragments.add(new HomeMineFragment());
        Fragment homeFragment = (BaseFragment) this.mFragments.get(0);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add((int) R.id.frame_content, homeFragment);
        transaction.commitAllowingStateLoss();
        homeFragment.loadFirst();
        this.viewTabs.setOnTabSelectedListener(new OnTabSelectedListener() {
            public void onTabSelected(Tab tab) {
                MainActivity.this.supportInvalidateOptionsMenu();
                if (tab.getPosition() != 2) {
                    MainActivity.this.oldPosition = tab.getPosition();
                }
                switch (tab.getPosition()) {
                    case 0:
                        MainActivity.this.switchFragment((BaseFragment) MainActivity.this
                                .mFragments.get(0));
                        MobclickAgent.onEvent(MainActivity.this.ctx, Event.HOME_PAGE);
                        MainActivity.this.setToolBarColor(17170445, false);
                        MainActivity.this.mPartnerTab.setVisibility(8);
                        MainActivity.this.mTitle.setVisibility(8);
                        return;
                    case 1:
                        MainActivity.this.switchFragment((BaseFragment) MainActivity.this
                                .mFragments.get(1));
                        MainActivity.this.checkGuide();
                        MobclickAgent.onEvent(MainActivity.this.ctx, Event.STATUS_HOMEPAGE);
                        MainActivity.this.setToolBarColor(R.color.ik, true);
                        MainActivity.this.mPartnerTab.setVisibility(0);
                        MainActivity.this.mTitle.setVisibility(8);
                        return;
                    case 2:
                        MainActivity.this.showPopMenu();
                        return;
                    case 3:
                        MainActivity.this.switchFragment((BaseFragment) MainActivity.this
                                .mFragments.get(2));
                        MobclickAgent.onEvent(MainActivity.this.ctx, Event.SHOP_HOMEPAGE);
                        if (MainActivity.this.mShopBadge != null) {
                            MainActivity.this.mShopBadge.setVisibility(8);
                        }
                        MainActivity.this.setToolBarColor(R.color.ik, true);
                        MainActivity.this.mPartnerTab.setVisibility(8);
                        MainActivity.this.mTitle.setText(MainActivity.mTabs[2]);
                        MainActivity.this.mTitle.setVisibility(0);
                        EventBus.getDefault().post(new ConstEvent().setFlag(2));
                        return;
                    case 4:
                        MainActivity.this.switchFragment((BaseFragment) MainActivity.this
                                .mFragments.get(3));
                        MobclickAgent.onEvent(MainActivity.this.ctx, Event.mine_homePage);
                        MainActivity.this.setToolBarColor(R.color.ik, true);
                        MainActivity.this.mPartnerTab.setVisibility(8);
                        MainActivity.this.mTitle.setText(MainActivity.this.userName);
                        MainActivity.this.mTitle.setVisibility(0);
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
                        ((BaseFragment) MainActivity.this.mFragments.get(1)).loadFirst();
                        return;
                    default:
                        return;
                }
            }
        });
    }

    private void showPopMenu() {
        MobclickAgent.onEvent(this.ctx, Event.OTHER_CLICKPLUS);
        if (this.popMenu == null) {
            Builder builder = new Builder().attachToActivity(this);
            builder.addMenuItem(new PopMenuItem(0, getString(R.string.dp), ContextCompat
                    .getDrawable(this.ctx, R.drawable.a8e))).addMenuItem(new PopMenuItem(1,
                    getString(R.string.ri), ContextCompat.getDrawable(this.ctx, R.drawable.a8t)))
                    .addMenuItem(new PopMenuItem(2, getString(R.string.a7m), ContextCompat
                            .getDrawable(this.ctx, R.drawable.a8m))).addMenuItem(new PopMenuItem
                    (8, getString(R.string.a5a), ContextCompat.getDrawable(this.ctx, R.drawable
                            .a8o))).addMenuItem(new PopMenuItem(3, getString(R.string.a5q),
                    ContextCompat.getDrawable(this.ctx, R.drawable.a93))).addMenuItem(new
                    PopMenuItem(4, getString(R.string.ad_), ContextCompat.getDrawable(this.ctx, R
                    .drawable.a99))).addMenuItem(new PopMenuItem(5, getString(R.string.fj),
                    ContextCompat.getDrawable(this.ctx, R.drawable.a8h)));
            if (!new UserDao(this.ctx).queryWithToken(UserPreference.getToken(this.ctx)).isMale()) {
                builder.addMenuItem(new PopMenuItem(6, getString(R.string.mc_period),
                        ContextCompat.getDrawable(this.ctx, R.drawable.a8w)));
            }
            builder.setOnItemClickListener(new PopMenuItemListener() {
                public void onItemClick(PopMenu popMenu, PopMenuItem item) {
                    switch (item.getIndex()) {
                        case 0:
                            MobclickAgent.onEvent(MainActivity.this.ctx, Event
                                    .OTHER_CLICKPLUSBREAKFAST);
                            AddFoodListActivity.start(MainActivity.this.ctx, 1, DateHelper.today
                                    (), 0.0f);
                            return;
                        case 1:
                            MobclickAgent.onEvent(MainActivity.this.ctx, Event
                                    .OTHER_CLICKPLUSLUNCH);
                            AddFoodListActivity.start(MainActivity.this.ctx, 2, DateHelper.today
                                    (), 0.0f);
                            return;
                        case 2:
                            MobclickAgent.onEvent(MainActivity.this.ctx, Event
                                    .OTHER_CLICKPLUSDINNER);
                            AddFoodListActivity.start(MainActivity.this.ctx, 3, DateHelper.today
                                    (), 0.0f);
                            return;
                        case 3:
                            MobclickAgent.onEvent(MainActivity.this.ctx, Event
                                    .OTHER_CLICKPLUSSPORT);
                            AddSportListActivity.start(MainActivity.this.ctx, DateHelper.today());
                            return;
                        case 4:
                            MobclickAgent.onEvent(MainActivity.this.ctx, Event
                                    .OTHER_CLICKPLUSWEIGHT);
                            MainActivity.this.weightRecordFragment = WeightRecordFragment
                                    .newInstance(null, DateHelper.format(new Date()));
                            MainActivity.this.weightRecordFragment.show(MainActivity.this
                                    .getSupportFragmentManager(), "weight_record");
                            MainActivity.this.weightRecordFragment.setChangeListener(new onChangeListener() {
                                public void onFinish() {
                                    MainActivity.this.viewTabs.getTabAt(0).select();
                                }
                            });
                            return;
                        case 5:
                            MobclickAgent.onEvent(MainActivity.this.ctx, Event
                                    .OTHER_CLICKPLUSWAIST);
                            AddMeasureFragment addMeasureFragment = AddMeasureFragment
                                    .newInstance(DateHelper.format(new Date()));
                            addMeasureFragment.show(MainActivity.this.getSupportFragmentManager()
                                    , "addMeasureFragment");
                            addMeasureFragment.setChangeListener(new onChangeListener() {
                                public void onFinish() {
                                    MainActivity.this.startActivity(new Intent(MainActivity.this
                                            .ctx, DimensionRecordActivity.class).putExtra
                                            ("key_record_type", MeasureType.WAIST.getType()));
                                }
                            });
                            return;
                        case 6:
                            MobclickAgent.onEvent(MainActivity.this.ctx, Event.OTHER_CLICKPLUSMC);
                            MainActivity.this.startActivity(new Intent(MainActivity.this.ctx,
                                    PeriodCalendarActivity.class));
                            return;
                        case 8:
                            MobclickAgent.onEvent(MainActivity.this.ctx, Event
                                    .OTHER_CLICKPLUSSNACK);
                            int hour = Calendar.getInstance().get(11);
                            if (hour >= 5 && hour < 12) {
                                AddFoodListActivity.start(MainActivity.this.ctx, 6, DateHelper
                                        .today(), 0.0f);
                                return;
                            } else if (hour < 12 || hour >= 18) {
                                AddFoodListActivity.start(MainActivity.this.ctx, 8, DateHelper
                                        .today(), 0.0f);
                                return;
                            } else {
                                AddFoodListActivity.start(MainActivity.this.ctx, 7, DateHelper
                                        .today(), 0.0f);
                                return;
                            }
                        default:
                            return;
                    }
                }

                public void onClose() {
                    MainActivity.this.viewTabs.getTabAt(MainActivity.this.oldPosition).select();
                }
            });
            this.popMenu = builder.build();
        }
        this.popMenu.show();
    }

    private void switchFragment(BaseFragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        for (BaseFragment item : this.mFragments) {
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

    public void onResume() {
        super.onResume();
        SyncHelper.syncWeight(false);
        this.messageCount = 0;
        refreshUnreadMsg();
        getApnUnread();
    }

    protected void onStart() {
        super.onStart();
        getShopUpdatedTime();
    }

    private void getShopUpdatedTime() {
        OneApi.getTabbarSettings(this.ctx, new JsonCallback(this.ctx) {
            public void ok(JSONObject object) {
                super.ok(object);
                String shop_updated_at = object.optString("shop_updated_at");
                if (!TextUtils.equals(OnePreference.getInstance(MainActivity.this.ctx)
                        .getShopUpdateAt(), shop_updated_at)) {
                    OnePreference.getInstance(MainActivity.this.ctx).setShopUpdateAt
                            (shop_updated_at);
                    MainActivity.this.mShopBadge.setVisibility(0);
                }
            }

            public void fail(String message) {
            }
        });
    }

    private void refreshUnreadMsg() {
        if (this.mMessageBadge != null) {
            StatusApi.getUnread(this, new JsonCallback(this) {
                public void ok(JSONObject object) {
                    super.ok(object);
                    MainActivity.this.messageCount = MainActivity.this.messageCount + object
                            .optInt("count");
                    MainActivity.this.updateMessageBager(MainActivity.this.messageCount);
                }

                public void fail(String message) {
                }
            });
        }
    }

    private void getApnUnread() {
        if (this.mMessageBadge != null) {
            MessengerApi.v2CheckUnread(this, new JsonCallback(this) {
                public void ok(JSONObject object) {
                    super.ok(object);
                    MainActivity.this.messageCount = MainActivity.this.messageCount + object
                            .optInt("count");
                    MainActivity.this.updateMessageBager(MainActivity.this.messageCount);
                }

                public void fail(String message) {
                }
            });
        }
    }

    private void updateMessageBager(int count) {
        if (this.mMessageBadge != null) {
            if (count > 0) {
                this.mMessageBadge.setText(count > 99 ? "99+" : count + "");
                this.mMessageBadge.show();
                return;
            }
            this.mMessageBadge.hide();
        }
    }

    public boolean onPrepareOptionsMenu(Menu menu) {
        this.menu = menu;
        menu.clear();
        getMenuInflater().inflate(R.menu.a, menu);
        new Handler().postDelayed(new Runnable() {
            public void run() {
                MainActivity.this.mMessageBadge = new BadgeView(MainActivity.this, MainActivity
                        .this.findViewById(R.id.action_message));
                MainActivity.this.mMessageBadge.setBadgeBackgroundColor(MainActivity.this
                        .getResources().getColor(R.color.he));
                MainActivity.this.mMessageBadge.setTextColor(MainActivity.this.getResources()
                        .getColor(R.color.ju));
                MainActivity.this.updateMessageBager(MainActivity.this.messageCount);
                if (MainActivity.this.messageCount <= 0) {
                    MainActivity.this.messageCount = 0;
                    MainActivity.this.refreshUnreadMsg();
                    MainActivity.this.getApnUnread();
                }
            }
        }, 100);
        return super.onPrepareOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 16908332:
                if (WheelUtils.isFastDoubleClick()) {
                    return true;
                }
                SearcherActivity.comeOnBaby(this.ctx);
                return true;
            case R.id.action_message:
                MobclickAgent.onEvent(this.ctx, Event.OTHER_CLICKMSGPAGE);
                new BuilderIntent(this.activity, MsgCategoryActivity.class).startActivity();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void doLogout() {
        LightAlertDialog.create((Context) this, (int) R.string.rf).setNegativeButton((int) R
                .string.eq, null).setPositiveButton((int) R.string.y8, new OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                AccountUtils.logout();
                WelcomeActivity.comeOnBaby(MainActivity.this);
                MainActivity.this.finish();
            }
        }).show();
    }

    public void onDestroy() {
        ivAvatar = null;
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        MyApplication.setIsMainActivityOpened(false);
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode != 4) {
            return super.onKeyDown(keyCode, event);
        }
        if (CheckAccountPopwindow.isShowing()) {
            CheckAccountPopwindow.dismiss();
            return true;
        } else if (this.popMenu != null && this.popMenu.isShowing()) {
            this.popMenu.hide();
            return true;
        } else if (System.currentTimeMillis() - this.mExitTime >= 2000) {
            Helper.showToast((int) R.string.la);
            this.mExitTime = System.currentTimeMillis();
            return true;
        } else {
            finish();
            return true;
        }
    }

    protected void onNewIntent(Intent intent) {
        switch (intent.getIntExtra(KEY_ONNEWINTENT, -1)) {
            case 0:
                this.viewTabs.getTabAt(0).select();
                return;
            case 1:
                this.viewTabs.getTabAt(1).select();
                return;
            case 3:
                this.viewTabs.getTabAt(3).select();
                return;
            default:
                return;
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 0 && this.weightRecordFragment != null) {
            this.weightRecordFragment.onActivityResult(requestCode, resultCode, data);
        }
    }

    public void onEventMainThread(LogoutEvent logoutEvent) {
        finish();
    }

    public static void comeOnBaby(Context context) {
        if (context != null) {
            context.startActivity(new Intent(context, MainActivity.class));
        }
    }
}
