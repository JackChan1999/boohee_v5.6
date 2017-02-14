package com.boohee.one.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup.LayoutParams;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;

import com.boohee.api.RecordApi;
import com.boohee.main.GestureActivity;
import com.boohee.model.Sport;
import com.boohee.one.R;
import com.boohee.one.cache.CacheKey;
import com.boohee.one.cache.FileCache;
import com.boohee.one.event.SportEvent;
import com.boohee.one.http.JsonCallback;
import com.boohee.one.ui.adapter.GoSportAdapter;
import com.boohee.utils.FastJsonUtils;
import com.boohee.utils.FormulaUtils;
import com.boohee.utils.Helper;

import de.greenrobot.event.EventBus;

import java.util.ArrayList;
import java.util.List;

import kale.adapter.RcvAdapterWrapper;

import org.json.JSONObject;

public class GoSportActivity extends GestureActivity {
    public static final String CALORY        = "calory";
    public static final String DIET_CALORY   = "diet_calory";
    public static final String RECORD_ON     = "record_on";
    public static final String SPORT_CALORY  = "sport_calory";
    public static final String TARGET_CALORY = "target_calory";
    private GoSportAdapter adapter;
    TextView desc;
    private float     dietCalory;
    private FileCache mCache;
    private List<Sport> mSportList = new ArrayList();
    private String record_on;
    @InjectView(2131427637)
    RecyclerView recyclerView;
    private float sportCalory;
    private float targetCalory;

    public static void startActivity(Context context, String record_on, float targetCalory, float
            dietCalory, float sportCalory) {
        Intent i = new Intent(context, GoSportActivity.class);
        i.putExtra("record_on", record_on);
        i.putExtra("target_calory", targetCalory);
        i.putExtra(DIET_CALORY, dietCalory);
        i.putExtra(SPORT_CALORY, sportCalory);
        context.startActivity(i);
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bd);
        ButterKnife.inject((Activity) this);
        initParams();
        initViews();
        initData();
    }

    private void initParams() {
        this.record_on = getIntent().getStringExtra("record_on");
        this.targetCalory = getIntent().getFloatExtra("target_calory", 0.0f);
        this.dietCalory = getIntent().getFloatExtra(DIET_CALORY, 0.0f);
        this.sportCalory = getIntent().getFloatExtra(SPORT_CALORY, 0.0f);
    }

    private void initViews() {
        this.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        this.desc = (TextView) LayoutInflater.from(this).inflate(R.layout.p7, null, false);
        this.desc.setLayoutParams(new LayoutParams(-1, -2));
        int overCalory = -((int) FormulaUtils.needCalorie(this.targetCalory, this.dietCalory,
                this.sportCalory));
        updateDesc(overCalory);
        this.adapter = new GoSportAdapter(this, this.mSportList, overCalory, this.record_on);
        RcvAdapterWrapper wrapper = new RcvAdapterWrapper(this.adapter, this.recyclerView
                .getLayoutManager());
        wrapper.setHeaderView(this.desc);
        this.recyclerView.setAdapter(wrapper);
    }

    private void initData() {
        this.mCache = FileCache.get((Context) this);
        JSONObject data = this.mCache.getAsJSONObject(CacheKey.COMMON_SPORT);
        if (data != null) {
            refreshView(data);
            return;
        }
        showLoading();
        RecordApi.getActivitiesHot(this, 0, new JsonCallback(this) {
            public void ok(JSONObject object) {
                super.ok(object);
                GoSportActivity.this.refreshView(object);
                GoSportActivity.this.mCache.put(CacheKey.COMMON_SPORT, object);
            }

            public void onFinish() {
                super.onFinish();
                GoSportActivity.this.dismissLoading();
            }
        });
    }

    private void refreshView(JSONObject object) {
        if (object != null) {
            List<Sport> sports = FastJsonUtils.parseList(object.optString("activities"), Sport
                    .class);
            if (sports != null) {
                this.mSportList.addAll(sports);
                this.adapter.notifyDataSetChanged();
            }
        }
    }

    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    public void onEventMainThread(SportEvent event) {
        Helper.showToast((CharSequence) "添加运动成功");
        if (event.getEditType() == 1) {
            this.sportCalory += event.getRecordSport().calory;
            int overCalory = -((int) FormulaUtils.needCalorie(this.targetCalory, this.dietCalory,
                    this.sportCalory));
            updateDesc(overCalory);
            this.adapter.updateCalory(overCalory);
        }
    }

    private void updateDesc(int calory) {
        if (calory > 0) {
            this.desc.setText(String.format("多吃了 %d 千卡\n可以选择以下运动来弥补", new Object[]{Integer
                    .valueOf(calory)}));
            return;
        }
        this.desc.setText("卡路里摄入正常");
    }
}
