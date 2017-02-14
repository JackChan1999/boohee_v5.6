package com.boohee.food;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import com.boohee.api.FoodApi;
import com.boohee.main.GestureActivity;
import com.boohee.model.CustomCook;
import com.boohee.model.CustomCookItem;
import com.boohee.modeldao.FoodRecordDao;
import com.boohee.one.R;
import com.boohee.one.event.CustomCookEvent;
import com.boohee.one.event.CustomCookItemEvent;
import com.boohee.one.event.MyFoodEvent;
import com.boohee.one.http.JsonCallback;
import com.boohee.one.http.JsonParams;
import com.boohee.uploader.QiniuConfig.Prefix;
import com.boohee.uploader.QiniuModel;
import com.boohee.uploader.QiniuUploader;
import com.boohee.uploader.UploadHandler;
import com.boohee.utility.ImageLoaderOptions;
import com.boohee.utils.FastJsonUtils;
import com.boohee.utils.Helper;
import com.boohee.widgets.RoundedCornersImage;
import com.nostra13.universalimageloader.core.ImageLoader;

import de.greenrobot.event.EventBus;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import me.nereo.multi_image_selector.MultiImageSelectorActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class AddCustomCookActivity extends GestureActivity {
    private static final int REQUEST_IMAGE = 1;
    private String cookName;
    @InjectView(2131427453)
    EditText etCookName;
    private String imagePath;
    @InjectView(2131427457)
    RoundedCornersImage ivCookImg;
    @InjectView(2131427459)
    LinearLayout        llFoodMaterial;
    private List<CustomCook> materialList = new ArrayList();
    private String           qiniuKey     = "";
    @InjectView(2131427456)
    RelativeLayout rlCookImg;
    private float totalCalory = 0.0f;
    @InjectView(2131427461)
    TextView tvCalory;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_);
        ButterKnife.inject((Activity) this);
        EventBus.getDefault().register(this);
    }

    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.j, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_finish:
                uploadCustomCook();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void uploadCustomCook() {
        this.cookName = this.etCookName.getText().toString();
        if (TextUtils.isEmpty(this.cookName)) {
            Helper.showToast((int) R.string.abd);
        } else if (this.materialList.isEmpty()) {
            Helper.showToast((CharSequence) "请至少添加一种食材");
        } else if (TextUtils.isEmpty(this.imagePath)) {
            createCustomCook();
        } else {
            showLoading();
            QiniuUploader.upload(Prefix.messenger, new UploadHandler() {
                public void onSuccess(List<QiniuModel> infos) {
                    AddCustomCookActivity.this.qiniuKey = ((QiniuModel) infos.get(0)).key;
                    AddCustomCookActivity.this.createCustomCook();
                }

                public void onError(String msg) {
                    Helper.showToast((CharSequence) msg);
                }

                public void onFinish() {
                    AddCustomCookActivity.this.dismissLoading();
                }
            }, this.imagePath);
        }
    }

    private void createCustomCook() {
        showLoading();
        FoodApi.addCustomMenus(createJsonParams(), this.activity, new JsonCallback(this.activity) {
            public void ok(JSONObject object) {
                super.ok(object);
                CustomCookItem customCookItem = (CustomCookItem) FastJsonUtils.fromJson(object,
                        CustomCookItem.class);
                if (customCookItem != null) {
                    Helper.showToast((CharSequence) "上传成功");
                    EventBus.getDefault().post(new MyFoodEvent().setFlag(3));
                    EventBus.getDefault().post(new CustomCookItemEvent().setCustomCookItem
                            (customCookItem));
                    AddCustomCookActivity.this.activity.finish();
                }
            }

            public void onFinish() {
                super.onFinish();
                AddCustomCookActivity.this.dismissLoading();
            }
        });
    }

    private JsonParams createJsonParams() {
        JsonParams root = new JsonParams();
        JSONObject menu = new JSONObject();
        try {
            menu.put("menu_name", this.cookName);
            menu.put("qiniu_key", this.qiniuKey);
            menu.put("calory", (double) this.totalCalory);
            root.put("menu", menu);
            root.put("materials", createMaterialJSONArray());
        } catch (JSONException e) {
        }
        return root;
    }

    private JSONArray createMaterialJSONArray() {
        if (this.materialList.isEmpty()) {
            return null;
        }
        JSONArray materials = new JSONArray();
        try {
            for (CustomCook cook : this.materialList) {
                if (cook != null) {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("food_id", cook.id);
                    jsonObject.put(FoodRecordDao.AMOUNT, (double) cook.amount);
                    jsonObject.put(FoodRecordDao.FOOD_UNIT_ID, cook.food_unit_id);
                    materials.put(jsonObject);
                }
            }
            return materials;
        } catch (JSONException e) {
            e.printStackTrace();
            return materials;
        }
    }

    @OnClick({2131427452, 2131427454, 2131427460, 2131427458})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_cook_name:
                this.etCookName.requestFocus();
                return;
            case R.id.rl_cook_photo:
                showTakePhotoDialog(1);
                return;
            case R.id.iv_cook_delete:
                this.imagePath = "";
                this.rlCookImg.setVisibility(8);
                return;
            case R.id.ll_add_material:
                CookSearchActivity.comeOn(this.activity);
                return;
            default:
                return;
        }
    }

    public static void comeOnBaby(Context context) {
        context.startActivity(new Intent(context, AddCustomCookActivity.class));
    }

    private void showTakePhotoDialog(int requestCode) {
        Intent intent = new Intent(this.activity, MultiImageSelectorActivity.class);
        intent.putExtra("show_camera", true);
        intent.putExtra("max_select_count", 1);
        intent.putExtra("select_count_mode", 0);
        startActivityForResult(intent, requestCode);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data != null) {
            switch (requestCode) {
                case 1:
                    List<String> path = data.getStringArrayListExtra(MultiImageSelectorActivity
                            .EXTRA_RESULT);
                    if (path != null && path.size() > 0) {
                        this.imagePath = (String) path.get(0);
                        ImageLoader.getInstance().displayImage(Uri.decode(Uri.fromFile(new File
                                (this.imagePath)).toString()), this.ivCookImg, ImageLoaderOptions
                                .color(R.color.ju));
                        this.rlCookImg.setVisibility(0);
                        break;
                    }
            }
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    public void onEventMainThread(CustomCookEvent cookEvent) {
        if (cookEvent.getCustomCook() != null) {
            addFoodMaterial(cookEvent.getCustomCook());
        }
    }

    private void addFoodMaterial(final CustomCook customCook) {
        final View item = LayoutInflater.from(this.activity).inflate(R.layout.hs, null);
        ((TextView) item.findViewById(R.id.tv_name)).setText(customCook.food_name);
        ((TextView) item.findViewById(R.id.tv_amount)).setText(customCook.amount + customCook
                .unit_name);
        item.findViewById(R.id.iv_delete).setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                AddCustomCookActivity.this.llFoodMaterial.removeView(item);
                AddCustomCookActivity.this.materialList.remove(customCook);
                AddCustomCookActivity.this.refreshCalory();
            }
        });
        this.materialList.add(customCook);
        this.llFoodMaterial.addView(item);
        refreshCalory();
    }

    private void refreshCalory() {
        this.totalCalory = 0.0f;
        for (CustomCook customCook : this.materialList) {
            this.totalCalory += customCook.calory;
        }
        this.tvCalory.setText(String.format("%.0f", new Object[]{Float.valueOf(this.totalCalory)}));
    }
}
