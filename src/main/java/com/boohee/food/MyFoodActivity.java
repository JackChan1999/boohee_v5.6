package com.boohee.food;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import com.boohee.api.FoodApi;
import com.boohee.main.GestureActivity;
import com.boohee.one.R;
import com.boohee.one.event.MyFoodEvent;
import com.boohee.one.http.JsonCallback;
import com.boohee.utility.Event;
import com.boohee.utils.Helper;
import com.umeng.analytics.MobclickAgent;

import de.greenrobot.event.EventBus;

import org.json.JSONObject;

public class MyFoodActivity extends GestureActivity {
    private int collectCount;
    @InjectView(2131427784)
    TextView collectNum;
    @InjectView(2131427792)
    TextView customCook;
    private int customCookCount;
    private int customCount;
    @InjectView(2131427786)
    TextView customNum;
    private int uploadCount;
    @InjectView(2131427789)
    TextView uploadNum;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.c7);
        ButterKnife.inject((Activity) this);
        EventBus.getDefault().register(this);
        refreshCount();
    }

    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void refreshCount() {
        refreshFoodFavoriteCount();
        refreshCustomFoodCount();
        refreshUploadFoodCount();
        refreshCustomCookCount();
    }

    private void refreshUploadFoodCount() {
        FoodApi.getUploadFoodCount(this.activity, new JsonCallback(this) {
            public void ok(JSONObject object) {
                super.ok(object);
                try {
                    MyFoodActivity.this.uploadCount = object.optInt("count");
                    if (MyFoodActivity.this.uploadCount > 0) {
                        MyFoodActivity.this.uploadNum.setText(String.format("我上传的包装食品 (%d)", new
                                Object[]{Integer.valueOf(MyFoodActivity.this.uploadCount)}));
                        return;
                    }
                    MyFoodActivity.this.uploadNum.setText("我上传的包装食品");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void refreshCustomFoodCount() {
        FoodApi.getCustomFoodCount(this.activity, new JsonCallback(this) {
            public void ok(JSONObject object) {
                super.ok(object);
                try {
                    MyFoodActivity.this.customCount = object.optInt("count");
                    if (MyFoodActivity.this.customCount > 0) {
                        MyFoodActivity.this.customNum.setText(String.format("自定义的食物 (%d)", new
                                Object[]{Integer.valueOf(MyFoodActivity.this.customCount)}));
                        return;
                    }
                    MyFoodActivity.this.customNum.setText("自定义的食物");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void refreshFoodFavoriteCount() {
        FoodApi.getFoodFavoriteCount(this.activity, new JsonCallback(this) {
            public void ok(JSONObject object) {
                super.ok(object);
                try {
                    MyFoodActivity.this.collectCount = object.optInt("count");
                    if (MyFoodActivity.this.collectCount > 0) {
                        MyFoodActivity.this.collectNum.setText(String.format("我收藏的食物 (%d)", new
                                Object[]{Integer.valueOf(MyFoodActivity.this.collectCount)}));
                        return;
                    }
                    MyFoodActivity.this.collectNum.setText("我收藏的食物");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void refreshCustomCookCount() {
        FoodApi.getCustomCookCount(this.activity, new JsonCallback(this) {
            public void ok(JSONObject object) {
                super.ok(object);
                try {
                    MyFoodActivity.this.customCookCount = object.optInt("count");
                    if (MyFoodActivity.this.customCookCount > 0) {
                        MyFoodActivity.this.customCook.setText(String.format("我的菜肴 (%d)", new
                                Object[]{Integer.valueOf(MyFoodActivity.this.customCookCount)}));
                        return;
                    }
                    MyFoodActivity.this.customCook.setText("我的菜肴");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @OnClick({2131427783, 2131427785, 2131427788, 2131427787, 2131427790, 2131427793, 2131427791})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_collect_food:
                MobclickAgent.onEvent(this.ctx, Event.tool_myfood_myfavorite);
                if (this.collectCount > 0) {
                    CollectionActivity.comeOnBaby(this.activity);
                    return;
                } else {
                    Helper.showToast((int) R.string.g8);
                    return;
                }
            case R.id.rl_custom_food:
                MobclickAgent.onEvent(this.ctx, Event.tool_myfood_mycustom);
                if (this.customCount > 0) {
                    CustomFoodListActivity.comeOnBaby(this.activity);
                    return;
                } else {
                    Helper.showToast((int) R.string.hy);
                    return;
                }
            case R.id.rl_add_custom_food:
                AddCustomFoodActivity.comeWithoutAddDiet(this.ctx);
                return;
            case R.id.ll_upload_food:
                MobclickAgent.onEvent(this.ctx, Event.tool_myfood_myupload);
                if (this.uploadCount > 0) {
                    ListUploadActivity.comeOnBaby(this.activity);
                    return;
                } else {
                    Helper.showToast((int) R.string.abf);
                    return;
                }
            case R.id.rl_upload_food:
                MobclickAgent.onEvent(this.ctx, Event.tool_myfood_upload);
                UploadFoodActivity.comeOnBaby(this.activity);
                return;
            case R.id.ll_custom_cook:
                if (this.customCookCount > 0) {
                    CustomCookListActivity.comeOnBaby(this.activity);
                    return;
                } else {
                    Helper.showToast((int) R.string.abe);
                    return;
                }
            case R.id.rl_custom_cook:
                AddCustomCookActivity.comeOnBaby(this.activity);
                return;
            default:
                return;
        }
    }

    public void onEventMainThread(MyFoodEvent myFoodEvent) {
        if (myFoodEvent != null) {
            switch (myFoodEvent.getFlag()) {
                case 0:
                    refreshFoodFavoriteCount();
                    return;
                case 1:
                    refreshCustomFoodCount();
                    return;
                case 2:
                    refreshUploadFoodCount();
                    return;
                case 3:
                    refreshCustomCookCount();
                    return;
                default:
                    return;
            }
        }
    }

    public static void comeOn(Context context) {
        if (context != null) {
            context.startActivity(new Intent(context, MyFoodActivity.class));
        }
    }
}
