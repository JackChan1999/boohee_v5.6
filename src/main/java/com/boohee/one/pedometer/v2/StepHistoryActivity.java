package com.boohee.one.pedometer.v2;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;

import com.boohee.myview.swipeback.SwipeBackActivity;
import com.boohee.one.R;
import com.boohee.one.http.JsonCallback;
import com.boohee.one.pedometer.StepApi;
import com.boohee.one.pedometer.StepModel;
import com.boohee.one.pedometer.manager.AbstractStepManager;
import com.boohee.one.pedometer.manager.StepListener;
import com.boohee.one.pedometer.manager.StepManagerFactory;
import com.boohee.one.pedometer.v2.adapter.StepHistoryAdapter;
import com.boohee.one.pedometer.v2.model.StepHistoryFullItem;
import com.boohee.one.pedometer.v2.model.StepHistoryItem;
import com.boohee.utils.DateFormatUtils;
import com.boohee.utils.Helper;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

public class StepHistoryActivity extends SwipeBackActivity implements StepListener {
    private StepHistoryAdapter mAdapter;
    private int                mCurrentMonthStep;
    private List<StepHistoryFullItem> mDataList = new ArrayList();
    @InjectView(2131427552)
    PullToRefreshListView mPullRefreshListView;
    private StepModel           mStepModel;
    private int                 mTotalStep;
    private AbstractStepManager stepManager;
    private TextView            tvStep;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dd);
        ButterKnife.inject((Activity) this);
        init();
        requestData();
    }

    private void init() {
        this.stepManager = StepManagerFactory.getInstance().createStepManager(this);
        this.stepManager.setListener(this);
        this.stepManager.getCurrentStepAsyncs();
        View headerView = LayoutInflater.from(this).inflate(R.layout.ms, null, false);
        this.tvStep = (TextView) headerView.findViewById(R.id.tv_step);
        ((ListView) this.mPullRefreshListView.getRefreshableView()).addHeaderView(headerView);
        this.mAdapter = new StepHistoryAdapter(this, this.mDataList);
        this.mPullRefreshListView.setAdapter(this.mAdapter);
        this.mPullRefreshListView.setMode(Mode.PULL_FROM_START);
        this.mPullRefreshListView.setOnRefreshListener(new OnRefreshListener<ListView>() {
            public void onRefresh(PullToRefreshBase<ListView> pullToRefreshBase) {
                StepHistoryActivity.this.requestData();
            }
        });
        ((ListView) this.mPullRefreshListView.getRefreshableView()).setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                StepHistoryItem viewTag = view.getTag(R.id.step_v2_history_key);
                if (viewTag != null && (viewTag instanceof StepHistoryItem)) {
                    StepMainActivity.comeOnBaby(StepHistoryActivity.this.ctx, viewTag.getDate());
                }
            }
        });
    }

    public void requestData() {
        showLoading();
        StepApi.requestStepHistory(this, new JsonCallback(this) {
            public void ok(JSONObject object) {
                StepHistoryActivity.this.mTotalStep = object.optInt("total_steps");
                if (StepHistoryActivity.this.mStepModel != null) {
                    StepHistoryActivity.this.tvStep.setText(String.valueOf(StepHistoryActivity
                            .this.mTotalStep + StepHistoryActivity.this.mStepModel.step));
                } else {
                    StepHistoryActivity.this.tvStep.setText(String.valueOf(StepHistoryActivity
                            .this.mTotalStep));
                }
                List<StepHistoryItem> tempData = StepHistoryItem.parseArray(object.optString
                        ("monthly"));
                if (tempData != null && tempData.size() > 0) {
                    StepHistoryActivity.this.mDataList.clear();
                    String year = null;
                    int size = tempData.size();
                    for (int i = 0; i < size; i++) {
                        StepHistoryItem item = (StepHistoryItem) tempData.get(i);
                        if (StepApi.isCurrentMonth(item.getDate())) {
                            StepHistoryActivity.this.mCurrentMonthStep = item.getSteps();
                            if (StepHistoryActivity.this.mStepModel != null) {
                                item.setSteps(StepHistoryActivity.this.mCurrentMonthStep +
                                        StepHistoryActivity.this.mStepModel.step);
                            }
                        }
                        String tempYear = DateFormatUtils.string2String(item.getDate(), "yyyy");
                        if (TextUtils.isEmpty(year) || !year.equals(tempYear)) {
                            StepHistoryFullItem dataItemYear = new StepHistoryFullItem();
                            dataItemYear.year = tempYear;
                            StepHistoryActivity.this.mDataList.add(dataItemYear);
                            year = tempYear;
                        }
                        StepHistoryFullItem dataItem = new StepHistoryFullItem();
                        dataItem.year = null;
                        dataItem.itemData = item;
                        StepHistoryActivity.this.mDataList.add(dataItem);
                    }
                    StepHistoryActivity.this.mAdapter.notifyDataSetChanged();
                }
            }

            public void fail(String message) {
                Helper.showToast((CharSequence) message);
            }

            public void onFinish() {
                StepHistoryActivity.this.dismissLoading();
                StepHistoryActivity.this.mPullRefreshListView.onRefreshComplete();
            }
        });
    }

    public void onGetCurrentStep(StepModel stepModel, boolean Error) {
        this.mStepModel = stepModel;
        if (stepModel != null) {
            if (this.tvStep != null) {
                this.tvStep.setText(String.valueOf(this.mTotalStep + stepModel.step));
            }
            if (this.mDataList.size() > 0) {
                for (StepHistoryFullItem item : this.mDataList) {
                    StepHistoryItem data = item.itemData;
                    if (data != null && StepApi.isCurrentMonth(data.getDate())) {
                        data.setSteps(this.mCurrentMonthStep + stepModel.step);
                        this.mAdapter.notifyDataSetChanged();
                        return;
                    }
                }
            }
        }
    }
}
