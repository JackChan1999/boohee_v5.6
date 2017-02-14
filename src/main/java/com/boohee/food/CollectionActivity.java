package com.boohee.food;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import com.alibaba.fastjson.JSON;
import com.boohee.api.FoodApi;
import com.boohee.food.adapter.CollectionAdapter;
import com.boohee.main.GestureActivity;
import com.boohee.model.CollectionFood;
import com.boohee.one.R;
import com.boohee.one.event.MyFoodEvent;
import com.boohee.one.http.JsonCallback;
import com.boohee.utils.Helper;
import com.boohee.widgets.BooheeListView;
import com.boohee.widgets.BooheeListView.OnLoadMoreListener;
import com.boohee.widgets.BooheeRippleLayout;

import de.greenrobot.event.EventBus;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

public class CollectionActivity extends GestureActivity {
    @InjectView(2131427545)
    BooheeListView blvContent;
    private CollectionAdapter mAdapter;
    private List<CollectionFood> mDataList   = new ArrayList();
    private List<Boolean>        mDataSelect = new ArrayList();
    private int                  mPage       = 0;
    private int                  mTotalPages = 1;
    @InjectView(2131427338)
    BooheeRippleLayout rippleLayout;
    @InjectView(2131427546)
    View               viewOperate;

    @OnClick({2131427548, 2131427549, 2131427550})
    public void onClick(View view) {
        boolean isSelect = false;
        switch (view.getId()) {
            case R.id.bt_delete:
                sendDeleteCollection();
                return;
            case R.id.tv_all:
                if (this.mDataSelect.size() > 0) {
                    if (!((Boolean) this.mDataSelect.get(0)).booleanValue()) {
                        isSelect = true;
                    }
                    for (int i = 0; i < this.mDataSelect.size(); i++) {
                        this.mDataSelect.set(i, Boolean.valueOf(isSelect));
                    }
                    this.mAdapter.notifyDataSetChanged();
                    return;
                }
                return;
            case R.id.btn_add_custom:
                AddCustomFoodActivity.comeWithoutAddDiet(this.ctx);
                return;
            default:
                return;
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.as);
        ButterKnife.inject((Activity) this);
        EventBus.getDefault().register(this);
        initView();
        initData(true);
    }

    private void initView() {
        this.mAdapter = new CollectionAdapter(this, this.mDataList, this.mDataSelect);
        this.blvContent.setAdapter(this.mAdapter);
        this.blvContent.setOnLoadMoreListener(new OnLoadMoreListener() {
            public void onLoadMore() {
                CollectionActivity.this.sendRequestCollection(true);
            }
        });
        this.blvContent.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                FoodDetailActivity.comeOnBaby(CollectionActivity.this, ((CollectionFood)
                        CollectionActivity.this.mDataList.get(position)).code, false);
            }
        });
    }

    private void initData(boolean showLoading) {
        this.mPage = 0;
        this.mTotalPages = 1;
        this.mDataSelect.clear();
        this.mDataList.clear();
        this.mAdapter.notifyDataSetChanged();
        sendRequestCollection(showLoading);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, 1, 1, R.string.g6).setShowAsAction(2);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 1:
                if (getString(R.string.g6).equals(item.getTitle())) {
                    item.setTitle(R.string.g7);
                    this.mAdapter.setEdit(true);
                    this.rippleLayout.setVisibility(8);
                    this.viewOperate.setVisibility(0);
                    this.viewOperate.startAnimation(AnimationUtils.loadAnimation(this, R.anim.o));
                    return true;
                }
                item.setTitle(R.string.g6);
                this.mAdapter.setEdit(false);
                this.viewOperate.animate().alphaBy(1.0f).alpha(0.0f).setDuration(300).setListener
                        (new AnimatorListenerAdapter() {
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        CollectionActivity.this.viewOperate.setVisibility(8);
                        CollectionActivity.this.rippleLayout.setVisibility(0);
                        CollectionActivity.this.viewOperate.setAlpha(1.0f);
                    }
                }).start();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void onEventMainThread(MyFoodEvent myFoodEvent) {
        if (myFoodEvent != null) {
            switch (myFoodEvent.getFlag()) {
                case 0:
                    initData(false);
                    return;
                default:
                    return;
            }
        }
    }

    protected void onDestroy() {
        super.onDestroy();
        if (this.mDataList != null) {
            this.mDataSelect.clear();
        }
        if (this.mDataSelect != null) {
            this.mDataSelect.clear();
        }
        EventBus.getDefault().unregister(this);
    }

    private void sendRequestCollection(final boolean showLoading) {
        int currentPage = this.mPage + 1;
        if (currentPage <= 1 || currentPage <= this.mTotalPages) {
            if (showLoading) {
                showLoading();
            }
            FoodApi.getCollectionFoodList(currentPage, this.activity, new JsonCallback(this) {
                public void ok(JSONObject object) {
                    try {
                        CollectionActivity.this.mPage = object.optInt("page");
                        CollectionActivity.this.mTotalPages = object.optInt("total_pages");
                        List<CollectionFood> foods = JSON.parseArray(object.optString("foods"),
                                CollectionFood.class);
                        if (foods == null || foods.size() <= 0) {
                            Helper.showLong(CollectionActivity.this.getString(R.string.g8));
                            CollectionActivity.this.blvContent.postDelayed(new Runnable() {
                                public void run() {
                                    CollectionActivity.this.finish();
                                }
                            }, 1000);
                            return;
                        }
                        CollectionActivity.this.mDataList.addAll(foods);
                        for (int i = 0; i < foods.size(); i++) {
                            CollectionActivity.this.mDataSelect.add(Boolean.valueOf(false));
                        }
                        CollectionActivity.this.mAdapter.notifyDataSetChanged();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                public void fail(String message) {
                    Helper.showLong((CharSequence) message);
                }

                public void onFinish() {
                    if (showLoading) {
                        CollectionActivity.this.dismissLoading();
                    }
                }
            });
        }
    }

    private void sendDeleteCollection() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < this.mDataSelect.size(); i++) {
            if (((Boolean) this.mDataSelect.get(i)).booleanValue()) {
                builder.append(((CollectionFood) this.mDataList.get(i)).id);
                builder.append(",");
            }
        }
        String param = builder.toString();
        if (param.length() == 0) {
            Helper.showToast(getString(R.string.g5));
            return;
        }
        param = param.substring(0, param.length() - 1);
        showLoading();
        FoodApi.deleteCollectionFood(param, this.activity, new JsonCallback(this) {
            public void ok(JSONObject object) {
                EventBus.getDefault().post(new MyFoodEvent().setFlag(0));
            }

            public void onFinish() {
                CollectionActivity.this.dismissLoading();
            }
        });
    }

    public static void comeOnBaby(Context context) {
        context.startActivity(new Intent(context, CollectionActivity.class));
    }
}
