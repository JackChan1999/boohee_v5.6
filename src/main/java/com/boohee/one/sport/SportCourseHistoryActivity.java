package com.boohee.one.sport;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;

import com.boohee.main.GestureActivity;
import com.boohee.myview.DividerItemDecoration;
import com.boohee.one.R;
import com.boohee.one.http.JsonCallback;
import com.boohee.one.sport.model.CourseHistory;
import com.boohee.utils.FastJsonUtils;
import com.boohee.utils.Helper;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

public class SportCourseHistoryActivity extends GestureActivity {
    private CourseHistoryAdapter mAdapter;
    private List<CourseHistory> mDataList = new ArrayList();
    @InjectView(2131427889)
    RecyclerView mRecyclerView;
    @InjectView(2131427873)
    View         view_no_result;

    private class CourseHistoryAdapter extends Adapter<ViewHolder> {
        private List<CourseHistory> mDataList;

        public CourseHistoryAdapter(List<CourseHistory> dataList) {
            this.mDataList = dataList;
        }

        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout
                    .j3, viewGroup, false));
        }

        public void onBindViewHolder(ViewHolder viewHolder, int index) {
            CourseHistory history = (CourseHistory) this.mDataList.get(index);
            viewHolder.view_content.setTag(history);
            viewHolder.tv_name.setText(history.name);
            viewHolder.tv_date.setText(history.start_date + " ~ " + history.end_date);
        }

        public int getItemCount() {
            return this.mDataList.size();
        }
    }

    private static class ViewHolder extends android.support.v7.widget.RecyclerView.ViewHolder
            implements OnClickListener {
        public TextView tv_date;
        public TextView tv_name;
        public View     view_content;

        public ViewHolder(View view) {
            super(view);
            this.tv_name = (TextView) view.findViewById(R.id.tv_name);
            this.tv_date = (TextView) view.findViewById(R.id.tv_date);
            this.view_content = view.findViewById(R.id.view_content);
            this.view_content.setOnClickListener(this);
        }

        public void onClick(View v) {
            SportStatisticsActivity.comeOnBaby(v.getContext(), ((CourseHistory) v.getTag()).id,
                    true);
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.d5);
        ButterKnife.inject((Activity) this);
        initView();
        requestData();
    }

    private void initView() {
        this.mRecyclerView.setHasFixedSize(true);
        this.mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        this.mAdapter = new CourseHistoryAdapter(this.mDataList);
        this.mRecyclerView.setAdapter(this.mAdapter);
        this.mRecyclerView.addItemDecoration(new DividerItemDecoration(this, 1));
    }

    private void requestData() {
        showLoading();
        SportV3Api.requestCourseHistory(new JsonCallback(this) {
            public void ok(JSONObject object) {
                List<CourseHistory> historyList = FastJsonUtils.parseList(object.optString
                        ("history"), CourseHistory.class);
                if (historyList == null || historyList.size() == 0) {
                    SportCourseHistoryActivity.this.view_no_result.setVisibility(0);
                    SportCourseHistoryActivity.this.mRecyclerView.setVisibility(8);
                    return;
                }
                SportCourseHistoryActivity.this.mRecyclerView.setVisibility(0);
                SportCourseHistoryActivity.this.view_no_result.setVisibility(8);
                SportCourseHistoryActivity.this.mDataList.addAll(historyList);
                SportCourseHistoryActivity.this.mAdapter.notifyDataSetChanged();
            }

            public void fail(String message) {
                Helper.showToast((CharSequence) message);
            }

            public void onFinish() {
                SportCourseHistoryActivity.this.dismissLoading();
            }
        }, this);
    }

    public static void comeOnBaby(Context context) {
        if (context != null) {
            Intent intent = new Intent();
            intent.setClass(context, SportCourseHistoryActivity.class);
            context.startActivity(intent);
        }
    }
}
