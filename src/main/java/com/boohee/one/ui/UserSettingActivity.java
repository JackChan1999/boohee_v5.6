package com.boohee.one.ui;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import boohee.lib.share.ShareManager;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import com.boohee.api.OneApi;
import com.boohee.api.ShopApi;
import com.boohee.apn.ApnActivity;
import com.boohee.main.GestureActivity;
import com.boohee.model.Coupon;
import com.boohee.model.OrderState;
import com.boohee.more.EstimateFoodActivity;
import com.boohee.myview.SettingItemView;
import com.boohee.one.R;
import com.boohee.one.event.LogoutEvent;
import com.boohee.one.http.JsonCallback;
import com.boohee.one.pedometer.StepCounterUtil;
import com.boohee.one.pedometer.StepSettingActivity;
import com.boohee.uchoice.AddressListActivity;
import com.boohee.uchoice.OrderListActivity;
import com.boohee.utility.Event;
import com.boohee.utils.AccountUtils;
import com.boohee.utils.BlackTech;
import com.boohee.utils.FastJsonUtils;
import com.boohee.utils.FileUtil;
import com.boohee.utils.Helper;
import com.boohee.utils.SystemUtil;
import com.boohee.utils.TextUtil;
import com.boohee.widgets.LightAlertDialog;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.analytics.MobclickAgent;

import de.greenrobot.event.EventBus;

import java.util.List;

import org.json.JSONObject;

public class UserSettingActivity extends GestureActivity {
    @InjectView(2131427998)
    SettingItemView aboutLine;
    @InjectView(2131427906)
    SettingItemView cacheLine;
    @InjectView(2131427808)
    SettingItemView changeEnvironment;
    @InjectView(2131427993)
    SettingItemView clvStep;
    @InjectView(2131427992)
    SettingItemView clv_coupon;
    @InjectView(2131427990)
    SettingItemView clv_order;

    @OnClick({2131427990, 2131427989, 2131427991, 2131427992, 2131427988, 2131427906, 2131427996,
            2131427997, 2131427998, 2131427999, 2131427994, 2131427995, 2131427808, 2131427993})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.clv_change_environment:
                ChangeEnvironmentActivity.comeOnBaby(this);
                return;
            case R.id.clv_clean_cache:
                MobclickAgent.onEvent(this.ctx, Event.MINE_CLEAR_CACHE);
                cleanPictureCache();
                return;
            case R.id.clv_account_setting:
                startActivity(new Intent(this.activity, AccountSettingActivity.class));
                return;
            case R.id.clv_mi_band:
                MobclickAgent.onEvent(this.activity, Event.MINE_CLICKADDRESSPAGE);
                startActivity(new Intent(this.activity, MiBandActivity.class));
                return;
            case R.id.clv_order:
                MobclickAgent.onEvent(this.activity, Event.MINE_CLICKORDERPAGE);
                this.clv_order.setIndicateVisibility(false);
                startActivity(new Intent(this.activity, OrderListActivity.class));
                return;
            case R.id.clv_address:
                MobclickAgent.onEvent(this.activity, Event.MINE_CLICKADDRESSPAGE);
                startActivity(new Intent(this.activity, AddressListActivity.class));
                return;
            case R.id.clv_coupon:
                MobclickAgent.onEvent(this.activity, Event.MINE_CLICKGIFTCOUPONS);
                this.clv_coupon.setIndicateVisibility(false);
                startActivity(new Intent(this.activity, CouponActivity.class));
                return;
            case R.id.clv_step:
                startActivity(new Intent(this.ctx, StepSettingActivity.class));
                return;
            case R.id.clv_question:
                ApnActivity.comeOnBaby(this, false);
                return;
            case R.id.clv_estimate:
                EstimateFoodActivity.comeOnBaby(this.activity);
                return;
            case R.id.clv_score:
                MobclickAgent.onEvent(this.ctx, Event.MINE_RANK);
                scoreUs();
                return;
            case R.id.clv_share:
                share();
                return;
            case R.id.clv_about_boohee:
                AboutBooheeActivity.comeOnBaby(this.activity);
                return;
            case R.id.logoutBtn:
                doLogout();
                return;
            default:
                return;
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dr);
        BlackTech.startDebugActivity(this.toolbar, this);
        ButterKnife.inject((Activity) this);
        getCacheSize();
        requestOrderCount();
        getCouponsInfo();
        getVersionInfo();
        initChangeEnvironment();
        if (StepCounterUtil.isKitkatWithStepSensor(this.ctx)) {
            this.clvStep.setVisibility(0);
        } else {
            this.clvStep.setVisibility(8);
        }
    }

    private void initChangeEnvironment() {
        this.changeEnvironment.setVisibility(8);
    }

    private void requestOrderCount() {
        ShopApi.getOrdersStats(this, new JsonCallback(this) {
            public void ok(JSONObject object) {
                try {
                    OrderState state = (OrderState) FastJsonUtils.fromJson(object, OrderState
                            .class);
                    if (state != null) {
                        UserSettingActivity.this.clv_order.setIndicateVisibility(state.initial > 0);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void getCouponsInfo() {
        ShopApi.getCoupons(this, new JsonCallback(this) {
            public void ok(JSONObject object) {
                super.ok(object);
                List<Coupon> coupons = Coupon.parseLists(object.optString("coupons"));
                boolean isShowContent = coupons != null && coupons.size() > 0;
                UserSettingActivity.this.clv_coupon.setIndicateVisibility(isShowContent);
            }

            public void onFinish() {
                super.onFinish();
            }
        });
    }

    private void getVersionInfo() {
        if (!TextUtil.isEmpty(SystemUtil.getAppVersionName())) {
            this.aboutLine.setIndicateText(SystemUtil.getAppVersionName());
        }
    }

    private void getCacheSize() {
        new Thread() {
            public void run() {
                final long size = FileUtil.getFolderSize(ImageLoader.getInstance().getDiskCache()
                        .getDirectory());
                UserSettingActivity.this.runOnUiThread(new Runnable() {
                    public void run() {
                        UserSettingActivity.this.cacheLine.setIndicateText((size / 1000) + "." +
                                ((size / 100) % 10) + "M");
                    }
                });
            }
        }.start();
    }

    public void cleanPictureCache() {
        LightAlertDialog.create((Context) this, (int) R.string.fn).setNegativeButton((int) R
                .string.eq, null).setPositiveButton((int) R.string.y8, new OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                ImageLoader.getInstance().clearDiskCache();
                UserSettingActivity.this.cacheLine.setIndicateText("0.0M");
            }
        }).show();
    }

    private void doLogout() {
        LightAlertDialog.create((Context) this, (int) R.string.rf).setNegativeButton((int) R
                .string.eq, null).setPositiveButton((int) R.string.y8, new OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                AccountUtils.logout();
                WelcomeActivity.comeOnBaby(UserSettingActivity.this.activity);
                EventBus.getDefault().post(new LogoutEvent());
                UserSettingActivity.this.finish();
            }
        }).show();
    }

    private void share() {
        ShareManager.share(this.activity, "最有效的减肥APP",
                "推荐“薄荷”app给大家哦，简直专业到令人感动！它会根据你的身高体重建议你一天该摄取的卡路里是多少，还有很全的食物卡路里数据，知道食物热量就不担心吃错东西长肉啦！传送门>>>", "http://a.app.qq.com/o/simple.jsp?pkgname=com.boohee.one&g_f=991653", "http://up.boohee.cn/house/u/one/ad/boohee_weibo.png");
    }

    private void scoreUs() {
        try {
            startActivity(new Intent("android.intent.action.VIEW", Uri.parse
                    ("market://details?id=" + this.activity.getPackageName())));
            OneApi.getUserBehaviorAppraise(this.activity, null);
        } catch (ActivityNotFoundException e) {
            Helper.showToast((int) R.string.xt);
        }
    }

    public static void comeOnBaby(Context context) {
        if (context != null) {
            context.startActivity(new Intent(context, UserSettingActivity.class));
        }
    }
}
