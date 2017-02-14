package com.boohee.food;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import com.alibaba.fastjson.JSON;
import com.boohee.api.FoodApi;
import com.boohee.food.adapter.UploadAdapter;
import com.boohee.main.GestureActivity;
import com.boohee.model.UploadFood;
import com.boohee.one.R;
import com.boohee.one.event.MyFoodEvent;
import com.boohee.one.http.JsonCallback;
import com.boohee.utility.Event;
import com.boohee.utils.Helper;
import com.boohee.widgets.BooheeListView;
import com.boohee.widgets.BooheeListView.OnLoadMoreListener;
import com.umeng.analytics.MobclickAgent;

import de.greenrobot.event.EventBus;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

public class ListUploadActivity extends GestureActivity {
    @InjectView(2131427545)
    BooheeListView blvContent;
    private UploadAdapter mAdapter;
    private List<UploadFood> mDataList   = new ArrayList();
    private int              mPage       = 0;
    private int              mTotalPages = 1;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.by);
        ButterKnife.inject((Activity) this);
        EventBus.getDefault().register(this);
        initView();
        sendRequestUpload();
    }

    private void initView() {
        this.mAdapter = new UploadAdapter(this, this.mDataList, this.blvContent);
        this.blvContent.setAdapter(this.mAdapter);
        this.blvContent.setOnLoadMoreListener(new OnLoadMoreListener() {
            public void onLoadMore() {
                ListUploadActivity.this.sendRequestUpload();
            }
        });
    }

    @OnClick({2131427550})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_add_custom:
                MobclickAgent.onEvent(this.ctx, Event.tool_myfood_upload);
                UploadFoodActivity.comeOnBaby(this.activity);
                return;
            default:
                return;
        }
    }

    private void sendRequestUpload() {
        int currentPage = this.mPage + 1;
        if (currentPage <= 1 || currentPage <= this.mTotalPages) {
            showLoading();
            FoodApi.getUploadFoodList(currentPage, this.activity, new JsonCallback(this) {
                public void ok(JSONObject object) {
                    super.ok(object);
                    try {
                        ListUploadActivity.this.mPage = object.optInt("page");
                        ListUploadActivity.this.mTotalPages = object.optInt("total_pages");
                        List<UploadFood> foods = JSON.parseArray(object.optString("food_drafts"),
                                UploadFood.class);
                        if (foods == null || foods.size() <= 0) {
                            Helper.showLong(ListUploadActivity.this.getString(R.string.abf));
                            ListUploadActivity.this.blvContent.postDelayed(new Runnable() {
                                public void run() {
                                    ListUploadActivity.this.finish();
                                }
                            }, 1000);
                            return;
                        }
                        ListUploadActivity.this.mDataList.addAll(foods);
                        ListUploadActivity.this.mAdapter.notifyDataSetChanged();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                public void onFinish() {
                    ListUploadActivity.this.dismissLoading();
                }
            });
        }
    }

    public static void comeOnBaby(Context context) {
        context.startActivity(new Intent(context, ListUploadActivity.class));
    }

    public void onEventMainThread(MyFoodEvent myFoodEvent) {
        if (myFoodEvent != null) {
            switch (myFoodEvent.getFlag()) {
                case 2:
                    this.mPage = 0;
                    this.mDataList.clear();
                    sendRequestUpload();
                    return;
                default:
                    return;
            }
        }
    }

    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
