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
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import com.boohee.api.FoodApi;
import com.boohee.food.adapter.CustomCookAdapter;
import com.boohee.main.GestureActivity;
import com.boohee.model.CustomCookItem;
import com.boohee.one.R;
import com.boohee.one.event.CustomCookItemEvent;
import com.boohee.one.event.MyFoodEvent;
import com.boohee.one.http.JsonCallback;
import com.boohee.utils.FastJsonUtils;
import com.boohee.utils.Helper;
import com.boohee.widgets.BooheeListView;
import com.boohee.widgets.BooheeListView.OnLoadMoreListener;
import com.boohee.widgets.BooheeRippleLayout;

import de.greenrobot.event.EventBus;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

public class CustomCookListActivity extends GestureActivity {
    @InjectView(2131427545)
    BooheeListView blvContent;
    private CustomCookAdapter mAdapter;
    private List<CustomCookItem> mDataList   = new ArrayList();
    private List<Boolean>        mDataSelect = new ArrayList();
    private int                  mPage       = 1;
    @InjectView(2131427338)
    BooheeRippleLayout rippleLayout;
    @InjectView(2131427551)
    TextView           tvAddCustom;
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
                AddCustomCookActivity.comeOnBaby(this.activity);
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
        this.mAdapter = new CustomCookAdapter(this, this.mDataList, this.mDataSelect);
        this.tvAddCustom.setText("我的菜肴");
        this.blvContent.setAdapter(this.mAdapter);
        this.blvContent.setOnLoadMoreListener(new OnLoadMoreListener() {
            public void onLoadMore() {
                CustomCookListActivity.this.sendRequestCollection(true);
            }
        });
        this.blvContent.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                CustomCookActivity.comeOnBaby(CustomCookListActivity.this.activity, (
                        (CustomCookItem) CustomCookListActivity.this.mDataList.get(position)).id);
            }
        });
    }

    private void initData(boolean showLoading) {
        this.mDataSelect.clear();
        this.mDataList.clear();
        this.mAdapter.notifyDataSetChanged();
        this.mPage = 1;
        sendRequestCollection(showLoading);
    }

    public void onEventMainThread(CustomCookItemEvent customCookItemEvent) {
        if (customCookItemEvent != null && customCookItemEvent.getCustomCookItem() != null) {
            this.mDataList.add(customCookItemEvent.getCustomCookItem());
            this.mDataSelect.add(Boolean.valueOf(false));
            this.mAdapter.notifyDataSetChanged();
        }
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
                        CustomCookListActivity.this.viewOperate.setVisibility(8);
                        CustomCookListActivity.this.rippleLayout.setVisibility(0);
                        CustomCookListActivity.this.viewOperate.setAlpha(1.0f);
                    }
                }).start();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        if (this.mDataList != null) {
            this.mDataList.clear();
        }
        if (this.mDataSelect != null) {
            this.mDataSelect.clear();
        }
    }

    private void sendRequestCollection(final boolean showLoading) {
        if (showLoading) {
            showLoading();
        }
        FoodApi.getCustomMenus(this.activity, this.mPage, new JsonCallback(this) {
            public void ok(JSONObject object) {
                if (CustomCookListActivity.this.mPage == 1) {
                    CustomCookListActivity.this.mDataList.clear();
                }
                List<CustomCookItem> foodList = FastJsonUtils.parseList(object.optString("menus")
                        , CustomCookItem.class);
                if (foodList != null && foodList.size() > 0) {
                    CustomCookListActivity.this.mDataList.addAll(foodList);
                    for (int i = 0; i < foodList.size(); i++) {
                        CustomCookListActivity.this.mDataSelect.add(Boolean.valueOf(false));
                    }
                    CustomCookListActivity.this.mPage = CustomCookListActivity.this.mPage + 1;
                }
                CustomCookListActivity.this.mAdapter.notifyDataSetChanged();
            }

            public void onFinish() {
                if (showLoading) {
                    CustomCookListActivity.this.dismissLoading();
                }
            }
        });
    }

    private void sendDeleteCollection() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < this.mDataSelect.size(); i++) {
            if (((Boolean) this.mDataSelect.get(i)).booleanValue()) {
                builder.append(((CustomCookItem) this.mDataList.get(i)).id);
                builder.append(",");
            }
        }
        String param = builder.toString();
        if (param.length() == 0) {
            Helper.showToast(getString(R.string.ek));
            return;
        }
        param = param.substring(0, param.length() - 1);
        showLoading();
        FoodApi.deleteCustomCook(this.activity, param, new JsonCallback(this) {
            public void ok(JSONObject object) {
                CustomCookListActivity.this.initData(false);
                EventBus.getDefault().post(new MyFoodEvent().setFlag(3));
            }

            public void onFinish() {
                CustomCookListActivity.this.dismissLoading();
            }
        });
    }

    public static void comeOnBaby(Context context) {
        context.startActivity(new Intent(context, CustomCookListActivity.class));
    }
}
