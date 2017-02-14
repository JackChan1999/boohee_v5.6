package com.boohee.one.video.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ListView;

import butterknife.ButterKnife;
import butterknife.InjectView;

import com.boohee.main.GestureActivity;
import com.boohee.one.R;
import com.boohee.one.http.JsonCallback;
import com.boohee.one.http.client.BooheeClient;
import com.boohee.one.video.adapter.SpecialTrainAdapter;
import com.boohee.one.video.api.SportApi;
import com.boohee.one.video.entity.SpecialTrain;
import com.boohee.utils.FastJsonUtils;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import de.greenrobot.event.EventBus;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

public class AddSpecialLessonActivity extends GestureActivity {
    public static final String REFRESH_ADD_SPECIAL = "refresh_add_special";
    private SpecialTrainAdapter adapter;
    private JSONArray           joinedIds;
    @InjectView(2131427472)
    PullToRefreshListView pullToRefresh;
    private List<SpecialTrain> specialTrains = new ArrayList();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ab);
        ButterKnife.inject((Activity) this);
        EventBus.getDefault().register(this);
        this.adapter = new SpecialTrainAdapter(this.ctx, this.specialTrains);
        initView();
        loadData();
        showLoading();
    }

    private void initView() {
        this.pullToRefresh.setOnRefreshListener(new OnRefreshListener<ListView>() {
            public void onRefresh(PullToRefreshBase<ListView> pullToRefreshBase) {
                AddSpecialLessonActivity.this.loadData();
            }
        });
        this.pullToRefresh.setAdapter(this.adapter);
    }

    private void loadData() {
        BooheeClient.build(BooheeClient.BINGO).get(SportApi.SPECIAL_LESSON, new JsonCallback(this
                .ctx) {
            public void onFinish() {
                super.onFinish();
                AddSpecialLessonActivity.this.pullToRefresh.onRefreshComplete();
                AddSpecialLessonActivity.this.dismissLoading();
            }

            public void ok(JSONObject object) {
                super.ok(object);
                AddSpecialLessonActivity.this.handleData(object);
            }
        }, this.ctx);
    }

    private void handleData(JSONObject object) {
        try {
            this.specialTrains.clear();
            this.specialTrains.addAll(FastJsonUtils.parseList(object.optString("trainings"),
                    SpecialTrain.class));
            this.joinedIds = object.optJSONArray("joined_ids");
            for (int j = 0; j < this.joinedIds.length(); j++) {
                for (int i = 0; i < this.specialTrains.size(); i++) {
                    SpecialTrain specialTrain = (SpecialTrain) this.specialTrains.get(i);
                    if (specialTrain.id == Integer.valueOf((String) this.joinedIds.get(j))
                            .intValue()) {
                        specialTrain.isJioned = true;
                    }
                }
            }
            this.adapter.notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onEvent(String event) {
        if (TextUtils.equals(event, REFRESH_ADD_SPECIAL)) {
            loadData();
        }
    }

    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    public static void comeOn(Context context) {
        context.startActivity(new Intent(context, AddSpecialLessonActivity.class));
    }
}
