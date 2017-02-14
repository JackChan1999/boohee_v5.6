package com.boohee.one.video.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import com.boohee.api.ApiUrls;
import com.boohee.main.GestureActivity;
import com.boohee.one.R;
import com.boohee.one.cache.FileCache;
import com.boohee.one.http.JsonCallback;
import com.boohee.one.http.client.BooheeClient;
import com.boohee.one.ui.BrowserActivity;
import com.boohee.one.video.adapter.SportPlanAdapter;
import com.boohee.one.video.api.SportApi;
import com.boohee.one.video.entity.Plan;
import com.boohee.one.video.entity.SportPlan;
import com.boohee.utils.FastJsonUtils;
import com.boohee.utils.WheelUtils;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import de.greenrobot.event.EventBus;

import org.json.JSONObject;

public class SportPlanActivity extends GestureActivity {
    public static final String REFRESH_SPORT_PLAN = "refresh_sport_plan";
    private   SportPlanAdapter adapter;
    private   ImageView        completedImg;
    protected FileCache        fileCache;
    private   View             headerView;
    private   LinearLayout     numberLayout;
    @InjectView(2131427552)
    PullToRefreshListView pullToRefreshListView;
    private SportPlan sportPlan;
    @InjectView(2131427900)
    Button testBtn;
    private LinearLayout timeLayout;
    private TextView     tvCalory;
    private TextView     tvNumber;
    private TextView     tvTime;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.d7);
        ButterKnife.inject((Activity) this);
        initView();
        getSportPlan();
        EventBus.getDefault().register(this);
        this.fileCache = FileCache.get(this.ctx);
    }

    protected void onResume() {
        super.onResume();
    }

    public void onEvent(String event) {
        if (TextUtils.equals(event, REFRESH_SPORT_PLAN)) {
            getSportPlan();
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

    @OnClick({2131427900})
    public void onClick(View v) {
        BrowserActivity.comeOnBaby(this.ctx, "评测", BooheeClient.build(BooheeClient.BINGO)
                .getDefaultURL(ApiUrls.SPORT_QUESTIONS_URL));
        finish();
    }

    private void initView() {
        this.headerView = View.inflate(this.activity, R.layout.qw, null);
        this.tvTime = (TextView) this.headerView.findViewById(R.id.tv_lesson_time);
        this.tvNumber = (TextView) this.headerView.findViewById(R.id.tv_lesson_number);
        this.tvCalory = (TextView) this.headerView.findViewById(R.id.tv_lesson_calory);
        this.timeLayout = (LinearLayout) this.headerView.findViewById(R.id.ll_lesson_time);
        this.numberLayout = (LinearLayout) this.headerView.findViewById(R.id.ll_lesson_number);
        this.completedImg = (ImageView) this.headerView.findViewById(R.id.iv_completed);
        ((ListView) this.pullToRefreshListView.getRefreshableView()).addHeaderView(this.headerView);
        this.pullToRefreshListView.setOnRefreshListener(new OnRefreshListener<ListView>() {
            public void onRefresh(PullToRefreshBase<ListView> pullToRefreshBase) {
                SportPlanActivity.this.getSportPlan();
            }
        });
        ((ListView) this.pullToRefreshListView.getRefreshableView()).setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    LessonDetailActivity.comeOn(SportPlanActivity.this, ((Plan) parent.getAdapter
                            ().getItem(position)).id);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void getSportPlan() {
        showLoading();
        SportApi.getTodayLessons(this.activity, new JsonCallback(this.activity) {
            public void ok(JSONObject object) {
                super.ok(object);
                SportPlanActivity.this.handlerData(object);
                SportPlanActivity.this.fileCache.put("sport_lesson", object);
            }

            public void fail(String message) {
                super.fail(message);
                SportPlanActivity.this.handlerData(SportPlanActivity.this.fileCache
                        .getAsJSONObject("sport_lesson"));
            }

            public void onFinish() {
                super.onFinish();
                SportPlanActivity.this.dismissLoading();
                if (SportPlanActivity.this.pullToRefreshListView != null && SportPlanActivity
                        .this.pullToRefreshListView.isRefreshing()) {
                    SportPlanActivity.this.pullToRefreshListView.onRefreshComplete();
                }
            }
        });
    }

    private void handlerData(JSONObject object) {
        this.sportPlan = (SportPlan) FastJsonUtils.fromJson(object, SportPlan.class);
        refreshView();
    }

    private void refreshView() {
        if (this.sportPlan != null) {
            if (this.sportPlan.sections.size() > 0) {
                this.tvTime.setText(this.sportPlan.total_time + "");
                this.tvCalory.setText(this.sportPlan.calorie_today + "");
                this.tvNumber.setText(this.sportPlan.sections.size() + "");
                this.adapter = new SportPlanAdapter(this.activity, this.sportPlan.sections);
                this.pullToRefreshListView.setAdapter(this.adapter);
                this.testBtn.setVisibility(8);
                this.pullToRefreshListView.setVisibility(0);
                return;
            }
            this.testBtn.setVisibility(0);
            this.pullToRefreshListView.setVisibility(8);
        }
    }

    public static void comeOnBaby(Context context) {
        if (context != null) {
            context.startActivity(new Intent(context, SportPlanActivity.class));
        }
    }

    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
