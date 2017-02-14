package com.boohee.record;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBar.LayoutParams;
import android.view.LayoutInflater;
import android.view.View;

import butterknife.ButterKnife;
import butterknife.InjectView;

import com.boohee.api.RecordApi;
import com.boohee.database.UserPreference;
import com.boohee.model.User;
import com.boohee.model.mine.DietHistory;
import com.boohee.model.mine.DietRecord;
import com.boohee.modeldao.UserDao;
import com.boohee.one.R;
import com.boohee.one.http.JsonCallback;
import com.boohee.one.ui.BaseActivity;
import com.boohee.utils.DietChartHelper;
import com.boohee.utils.FastJsonUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import lecho.lib.hellocharts.listener.ViewportChangeListener;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.renderer.ColumnChartRenderer;
import lecho.lib.hellocharts.view.ColumnChartView;

import org.json.JSONObject;

public class DietChartActivity extends BaseActivity {
    public static final int     PER_PAGE       = 9;
    public static final float   maxCaloryLimit = 2500.0f;
    private             boolean canLoad        = true;
    @InjectView(2131428043)
    ColumnChartView chart;
    private DietChartHelper chartHelper;
    private int              currentCount   = 0;
    private int              currentPage    = 1;
    private List<DietRecord> dietRecordList = new ArrayList();
    private int              totalPage      = 1;
    private float            viewportLeft   = 0.0f;
    private float            viewportRight  = 0.0f;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.e3);
        ButterKnife.inject((Activity) this);
        initActionbar();
        init();
        initTargetCalory();
        addListener();
    }

    private void addListener() {
        this.chart.setViewportChangeListener(new ViewportChangeListener() {
            public void onViewportChanged(Viewport viewport) {
                if (viewport.left <= -1.0f && DietChartActivity.this.currentPage <=
                        DietChartActivity.this.totalPage && DietChartActivity.this.canLoad) {
                    DietChartActivity.this.getDietRecord();
                }
            }
        });
    }

    private void init() {
        this.chartHelper = new DietChartHelper();
        getDietRecord();
    }

    private void initTargetCalory() {
        User user = new UserDao(this.ctx).queryWithToken(UserPreference.getToken(this.ctx));
        ColumnChartRenderer renderer = (ColumnChartRenderer) this.chart.getChartRenderer();
        if (renderer != null) {
            if (user.target_calory > 0) {
                renderer.setTargetCalory((float) user.target_calory);
            }
            renderer.setMaxCaloryLimit(maxCaloryLimit);
        }
    }

    private void initActionbar() {
        View actionBar = LayoutInflater.from(this).inflate(R.layout.f1, null);
        LayoutParams layoutParams = new LayoutParams(-2, -1);
        layoutParams.gravity = GravityCompat.END;
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(actionBar, layoutParams);
    }

    public static void comeOnBaby(Context context) {
        if (context != null) {
            context.startActivity(new Intent(context, DietChartActivity.class));
        }
    }

    private void getDietRecord() {
        showLoading();
        this.canLoad = false;
        RecordApi.getGetCanRecordsHistory(this.activity, String.valueOf(this.currentPage), String
                .valueOf(9), new JsonCallback(this.activity) {
            public void ok(JSONObject object) {
                super.ok(object);
                DietHistory dietHistory = (DietHistory) FastJsonUtils.fromJson(object,
                        DietHistory.class);
                if (dietHistory.data != null && dietHistory.data.size() > 0) {
                    Collections.reverse(dietHistory.data);
                    DietChartActivity.this.dietRecordList.addAll(0, DietChartActivity.this
                            .filterDate(dietHistory.data));
                    DietChartActivity.this.totalPage = dietHistory.total_pages;
                    DietChartActivity.this.currentCount = dietHistory.data.size();
                    if (dietHistory.page != 1) {
                        DietChartActivity.this.viewportLeft = (float) (DietChartActivity.this
                                .currentCount - 1);
                        DietChartActivity.this.viewportRight = DietChartActivity.this
                                .viewportLeft + 9.0f;
                    }
                    DietChartActivity.this.currentPage = dietHistory.page + 1;
                }
                DietChartActivity.this.chartHelper.initLine(DietChartActivity.this.activity,
                        DietChartActivity.this.chart, DietChartActivity.this.dietRecordList,
                        DietChartActivity.this.viewportLeft, DietChartActivity.this.viewportRight);
            }

            public void onFinish() {
                super.onFinish();
                DietChartActivity.this.dismissLoading();
                DietChartActivity.this.canLoad = true;
            }
        });
    }

    private List<DietRecord> filterDate(List<DietRecord> list) {
        Iterator<DietRecord> iterator = list.iterator();
        while (iterator.hasNext()) {
            if (((DietRecord) iterator.next()).record_on == null) {
                iterator.remove();
            }
        }
        return list;
    }
}
