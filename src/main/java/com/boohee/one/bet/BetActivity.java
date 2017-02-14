package com.boohee.one.bet;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.design.widget.TabLayout.OnTabSelectedListener;
import android.support.design.widget.TabLayout.Tab;
import android.support.design.widget.TabLayout.TabLayoutOnPageChangeListener;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AlertDialog.Builder;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;

import boohee.lib.share.ShareManager;
import butterknife.ButterKnife;
import butterknife.InjectView;

import com.boohee.database.UserPreference;
import com.boohee.main.GestureActivity;
import com.boohee.model.User;
import com.boohee.model.status.AttachMent;
import com.boohee.modeldao.UserDao;
import com.boohee.one.R;
import com.boohee.one.bet.model.Bet;
import com.boohee.one.http.JsonCallback;
import com.boohee.one.http.client.BooheeClient;
import com.boohee.one.ui.StatusPostTextActivity;
import com.boohee.one.ui.adapter.BaseFragmentPagerAdapter;
import com.boohee.utility.TimeLineUtility;
import com.boohee.utils.FastJsonUtils;
import com.boohee.utils.Helper;
import com.boohee.utils.Utils;

import org.json.JSONObject;

public class BetActivity extends GestureActivity {
    public static final String BET_ROOT_URL = "/api/v1/bets/";
    public static final String KEY_BET_ID   = "betID";
    private Bet                bet;
    private BetBrowserFragment betBrowserFragment;
    private int                betId;
    BaseFragmentPagerAdapter fragmentAdapter;
    private boolean isSecondLoad;
    private String  url;
    @InjectView(2131427350)
    ViewPager viewPager;
    @InjectView(2131427497)
    TabLayout viewTabs;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ah);
        ButterKnife.inject((Activity) this);
        this.betId = getIntent().getIntExtra(KEY_BET_ID, 0);
        this.url = String.format(BooheeClient.build("status").getDefaultURL(String.format
                ("/api/v1/bets/%d", new Object[]{Integer.valueOf(this.betId)})), new Object[0]);
        initView();
        initBet();
    }

    private void initBet() {
        BooheeClient.build("status").get("/api/v1/bet_nats/" + this.betId, new JsonCallback(this
                .ctx) {
            public void ok(JSONObject object) {
                super.ok(object);
                try {
                    BetActivity.this.bet = (Bet) FastJsonUtils.fromJson(object, Bet.class);
                    if (!TextUtils.isEmpty(BetActivity.this.bet.title)) {
                        BetActivity.this.setTitle(BetActivity.this.bet.title);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, this.ctx);
    }

    private void initView() {
        initFragments();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, 1, 1, "邀请").setShowAsAction(2);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() != 1) {
            return super.onOptionsItemSelected(item);
        }
        shareBet();
        return true;
    }

    private void shareBet() {
        if (this.bet != null) {
            User user = new UserDao(this.ctx).queryWithUserKey(UserPreference.getUserKey(this.ctx));
            Builder builder = new Builder(this.ctx);
            View view = LayoutInflater.from(this.ctx).inflate(R.layout.hk, null);
            builder.setView(view);
            final AlertDialog dialog = builder.create();
            view.findViewById(R.id.tv_timeline).setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    AttachMent attachMent = new AttachMent();
                    String text = "小伙伴们，" + BetActivity.this.bet.title + "开放报名啦，减肥周期" +
                            (TextUtils.equals(BetActivity.this.bet.genre, "four") ? "四" : "八") +
                            "周，报名费" + BetActivity.this.bet.entry_fee + "元，甩肉" + BetActivity.this
                            .bet.weight_ratio + "~戳下面的附件就可以报名咯！我们互相监督，一起甩肉赚奖金~#我赌我会瘦#";
                    attachMent.type = Utils.RESPONSE_CONTENT;
                    attachMent.title = BetActivity.this.bet.title;
                    attachMent.pic = BetActivity.this.bet.icon;
                    attachMent.url = BetActivity.this.bet.def_link;
                    StatusPostTextActivity.comeWithAttachmentAndExtraText(BetActivity.this.ctx,
                            text, attachMent);
                    dialog.dismiss();
                }
            });
            view.findViewById(R.id.tv_third).setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    String content = String.format("复制这条消息，打开【薄荷】手机App，即可查看最新期的【我赌我会瘦(" +
                            BetActivity.this.betId + ")】活动，为你的肥肉下注，减肥成功即可收获现金奖励！和我一起甩肉吧！%s", new
                            Object[]{BooheeClient.build("status").getDefaultURL(BetActivity
                            .BET_ROOT_URL + BetActivity.this.betId)});
                    TimeLineUtility.copyText(BetActivity.this.ctx, content);
                    Helper.showToast((CharSequence) "内容已复制到剪切板");
                    ShareManager.share(BetActivity.this.ctx, content);
                    dialog.dismiss();
                }
            });
            dialog.show();
        }
    }

    private void initFragments() {
        this.betBrowserFragment = BetBrowserFragment.newInstance(this.url);
        this.fragmentAdapter = new BaseFragmentPagerAdapter(getSupportFragmentManager());
        this.fragmentAdapter.addFragment(this.betBrowserFragment, "介绍");
        this.fragmentAdapter.addFragment(BetDiscussFragment.newInstance(this.betId), "讨论");
        this.viewPager.addOnPageChangeListener(new TabLayoutOnPageChangeListener(this.viewTabs));
        this.viewPager.setAdapter(this.fragmentAdapter);
        this.viewTabs.setupWithViewPager(this.viewPager);
        this.viewTabs.setOnTabSelectedListener(new OnTabSelectedListener() {
            public void onTabSelected(Tab tab) {
                BetActivity.this.supportInvalidateOptionsMenu();
                BetActivity.this.viewPager.setCurrentItem(tab.getPosition());
                if (tab.getPosition() == 1 && !BetActivity.this.isSecondLoad) {
                    BetActivity.this.fragmentAdapter.getItem(tab.getPosition()).loadFirst();
                    BetActivity.this.isSecondLoad = true;
                }
            }

            public void onTabUnselected(Tab tab) {
            }

            public void onTabReselected(Tab tab) {
            }
        });
    }
}
