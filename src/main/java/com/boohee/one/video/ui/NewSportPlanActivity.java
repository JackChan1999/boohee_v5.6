package com.boohee.one.video.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.design.widget.TabLayout.OnTabSelectedListener;
import android.support.design.widget.TabLayout.Tab;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AlertDialog.Builder;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;

import com.boohee.database.OnePreference;
import com.boohee.database.UserPreference;
import com.boohee.main.GestureActivity;
import com.boohee.modeldao.UserDao;
import com.boohee.one.R;
import com.boohee.one.http.JsonCallback;
import com.boohee.one.http.client.BooheeClient;
import com.boohee.one.ui.fragment.BaseFragment;
import com.boohee.one.video.api.SportApi;
import com.boohee.one.video.entity.DashBoard;
import com.boohee.one.video.fragment.SpecialTrainPlanFragment;
import com.boohee.one.video.fragment.SportPlanFragment;
import com.boohee.utils.FastJsonUtils;
import com.boohee.utils.Utils;
import com.boohee.utils.WheelUtils;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

public class NewSportPlanActivity extends GestureActivity {
    public static final String IS_FIRST              = "IS_FIRST";
    public static final String REFERSH               = "REFRESH";
    private final       int    FRAGMENT_SPECIAL_PLAN = 1;
    private final       int    FRAGMENT_SPORT_PLAN   = 0;
    private DashBoard dashBoard;
    private List<BaseFragment> fragments = new ArrayList();
    private boolean isFirst;
    @InjectView(2131427811)
    TabLayout tabLayout;
    @InjectView(2131427594)
    TextView  tvContinueDay;
    @InjectView(2131427809)
    TextView  tvTotalCalory;
    @InjectView(2131427810)
    TextView  tvTrainTime;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.c_);
        ButterKnife.inject((Activity) this);
        initView();
        getData();
        showLoading();
        if (getIntent() != null) {
            this.isFirst = getIntent().getBooleanExtra(IS_FIRST, false);
        }
    }

    private void initView() {
        initFragments();
        initTabLayout();
        Fragment homeFragment = (BaseFragment) this.fragments.get(0);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add((int) R.id.frame_content, homeFragment);
        transaction.commitAllowingStateLoss();
    }

    private void initFragments() {
        this.fragments.add(new SportPlanFragment());
        this.fragments.add(new SpecialTrainPlanFragment());
    }

    private void initTabLayout() {
        this.tabLayout.addTab(this.tabLayout.newTab().setText((int) R.string.a68));
        this.tabLayout.addTab(this.tabLayout.newTab().setText((int) R.string.a5n));
        this.tabLayout.setOnTabSelectedListener(new OnTabSelectedListener() {
            public void onTabSelected(Tab tab) {
                NewSportPlanActivity.this.supportInvalidateOptionsMenu();
                switch (tab.getPosition()) {
                    case 0:
                        NewSportPlanActivity.this.switchFragment((BaseFragment)
                                NewSportPlanActivity.this.fragments.get(0));
                        return;
                    case 1:
                        NewSportPlanActivity.this.switchFragment((BaseFragment)
                                NewSportPlanActivity.this.fragments.get(1));
                        return;
                    default:
                        return;
                }
            }

            public void onTabUnselected(Tab tab) {
            }

            public void onTabReselected(Tab tab) {
            }
        });
    }

    private void switchFragment(BaseFragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        for (BaseFragment item : this.fragments) {
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

    private void getData() {
        BooheeClient.build(BooheeClient.BINGO).get(SportApi.DASH_BOARD, new JsonCallback(this.ctx) {
            public void onFinish() {
                super.onFinish();
                NewSportPlanActivity.this.dismissLoading();
            }

            public void ok(JSONObject object) {
                super.ok(object);
                NewSportPlanActivity.this.dashBoard = (DashBoard) FastJsonUtils.fromJson(object,
                        DashBoard.class);
                NewSportPlanActivity.this.refreshDashBoard();
            }
        }, this.ctx);
    }

    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent != null) {
            this.isFirst = intent.getBooleanExtra(IS_FIRST, false);
        }
        getData();
    }

    private void refreshDashBoard() {
        if (this.dashBoard != null) {
            TextView tvLevel = null;
            if (this.isFirst) {
                final AlertDialog dialog = new Builder(this.ctx).create();
                View view = LayoutInflater.from(this.ctx).inflate(R.layout.r7, null, false);
                dialog.show();
                dialog.setCanceledOnTouchOutside(false);
                dialog.setContentView(view);
                tvLevel = (TextView) view.findViewById(R.id.tv_dialog_level);
                TextView tvBmi = (TextView) view.findViewById(R.id.tv_dialog_bmi);
                TextView tvWeight = (TextView) view.findViewById(R.id.tv_dialog_weight);
                TextView btnOk = (TextView) view.findViewById(R.id.btn_ok);
                float height = new UserDao(this).queryWithToken(UserPreference.getToken(this))
                        .height();
                float weight = OnePreference.getLatestWeight();
                tvWeight.setText(String.valueOf(weight));
                tvBmi.setText(String.valueOf(Utils.calBmi(height, weight)));
                btnOk.setOnClickListener(new OnClickListener() {
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                this.isFirst = false;
            }
            if (this.dashBoard.level == 1) {
                if (tvLevel != null) {
                    tvLevel.setText("达人");
                }
            } else if (this.dashBoard.level == 2) {
                if (tvLevel != null) {
                    tvLevel.setText("健康");
                }
            } else if (this.dashBoard.level == 3 && tvLevel != null) {
                tvLevel.setText("较弱");
            }
            this.tvTotalCalory.setText(String.valueOf(this.dashBoard.total_calorie));
            this.tvTrainTime.setText(String.valueOf(this.dashBoard.total_time));
            this.tvContinueDay.setText(String.valueOf(this.dashBoard.max_continue_day));
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.s, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (WheelUtils.isFastDoubleClick()) {
            return true;
        }
        switch (item.getItemId()) {
            case R.id.action_sport_setting:
                SportSettingActivity.comeOnBaby(this.activity);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public static void comeOnBaby(Context context) {
        context.startActivity(new Intent(context, NewSportPlanActivity.class));
    }

    public static void comeWithFirst(Context context) {
        Intent intent = new Intent(context, NewSportPlanActivity.class);
        intent.putExtra(IS_FIRST, true);
        context.startActivity(intent);
    }
}
