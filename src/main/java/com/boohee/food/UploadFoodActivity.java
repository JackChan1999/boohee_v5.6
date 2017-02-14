package com.boohee.food;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import com.boohee.api.FoodApi;
import com.boohee.main.GestureActivity;
import com.boohee.model.QiniuPhoto;
import com.boohee.modeldao.WeightRecordDao;
import com.boohee.one.R;
import com.boohee.one.event.MyFoodEvent;
import com.boohee.one.http.JsonCallback;
import com.boohee.one.http.JsonParams;
import com.boohee.one.ui.ScannerActivity;
import com.boohee.uploader.QiniuConfig.Prefix;
import com.boohee.uploader.QiniuModel;
import com.boohee.uploader.QiniuUploader;
import com.boohee.uploader.UploadHandler;
import com.boohee.utility.ImageLoaderOptions;
import com.boohee.utils.Helper;
import com.boohee.utils.TextUtil;
import com.boohee.utils.WheelUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

import de.greenrobot.event.EventBus;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import me.nereo.multi_image_selector.MultiImageSelectorActivity;

import org.json.JSONArray;
import org.json.JSONObject;

public class UploadFoodActivity extends GestureActivity {
    private static final String FOOD_CODE           = "FOOD_CODE";
    private static final int    REQUEST_BACK_IMAGE  = 2;
    private static final int    REQUEST_FRONT_IMAGE = 1;
    private String backImgPath;
    private String brand;
    private String code;
    @InjectView(2131427966)
    EditText  etBrand;
    @InjectView(2131427439)
    EditText  etFoodName;
    @InjectView(2131427979)
    ImageView foodBackImg;
    @InjectView(2131427974)
    ImageView foodFrontImg;
    private String frontImgPath;
    private String name;
    @InjectView(2131427978)
    RelativeLayout rlFoodBack;
    @InjectView(2131427973)
    RelativeLayout rlFoodFront;
    @InjectView(2131427970)
    TextView       tvCode;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dp);
        ButterKnife.inject((Activity) this);
        handlerIntent();
    }

    private void handlerIntent() {
        if (!TextUtil.isEmpty(getIntent().getStringExtra(FOOD_CODE))) {
            this.tvCode.setText(getIntent().getStringExtra(FOOD_CODE));
        }
    }

    @OnClick({2131427968, 2131427971, 2131427976, 2131427975, 2131427980, 2131427965, 2131427967})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_brand:
                this.etBrand.requestFocus();
                return;
            case R.id.ll_food_name:
                this.etFoodName.requestFocus();
                return;
            case R.id.rl_food_code:
                ScannerActivity.startScannerForResult(this.activity, 175);
                return;
            case R.id.rl_food_front:
                showTakePhotoDialog(1);
                return;
            case R.id.iv_front_delete:
                this.frontImgPath = "";
                this.rlFoodFront.setVisibility(8);
                return;
            case R.id.rl_food_back:
                showTakePhotoDialog(2);
                return;
            case R.id.iv_back_delete:
                this.backImgPath = "";
                this.rlFoodBack.setVisibility(8);
                return;
            default:
                return;
        }
    }

    private void showTakePhotoDialog(int requestCode) {
        Intent intent = new Intent(this.activity, MultiImageSelectorActivity.class);
        intent.putExtra("show_camera", true);
        intent.putExtra("max_select_count", 1);
        intent.putExtra("select_count_mode", 0);
        startActivityForResult(intent, requestCode);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.z, menu);
        return true;
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data != null) {
            switch (requestCode) {
                case 1:
                case 2:
                    List<String> path = data.getStringArrayListExtra(MultiImageSelectorActivity
                            .EXTRA_RESULT);
                    if (path != null && path.size() > 0) {
                        if (requestCode != 1) {
                            this.backImgPath = (String) path.get(0);
                            ImageLoader.getInstance().displayImage(Uri.decode(Uri.fromFile(new
                                    File(this.backImgPath)).toString()), this.foodBackImg,
                                    ImageLoaderOptions.color(R.color.ju));
                            this.rlFoodBack.setVisibility(0);
                            break;
                        }
                        this.frontImgPath = (String) path.get(0);
                        ImageLoader.getInstance().displayImage(Uri.decode(Uri.fromFile(new File
                                (this.frontImgPath)).toString()), this.foodFrontImg,
                                ImageLoaderOptions.color(R.color.ju));
                        this.rlFoodFront.setVisibility(0);
                        break;
                    }
                case 175:
                    String contents = data.getStringExtra(ScannerActivity.CODE_DATA);
                    if (contents != null) {
                        this.tvCode.setText(contents);
                        break;
                    }
                    break;
            }
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void uploadFood() {
        this.code = this.tvCode.getText().toString();
        this.brand = this.etBrand.getText().toString();
        this.name = this.etFoodName.getText().toString().trim();
        if (TextUtils.isEmpty(this.code)) {
            Helper.showToast((int) R.string.abj);
        } else if (TextUtils.isEmpty(this.name)) {
            Helper.showToast((int) R.string.abd);
        } else if (TextUtils.isEmpty(this.frontImgPath)) {
            Helper.showToast((int) R.string.abc);
        } else if (TextUtils.isEmpty(this.backImgPath)) {
            Helper.showToast((int) R.string.aay);
        } else {
            List pathList = new ArrayList();
            pathList.add(this.frontImgPath);
            pathList.add(this.backImgPath);
            showLoading();
            QiniuUploader.upload(Prefix.food, new UploadHandler() {
                public void onSuccess(List<QiniuModel> infos) {
                    UploadFoodActivity.this.uploadToServer(infos);
                }

                public void onError(String msg) {
                    Helper.showToast((CharSequence) msg);
                }

                public void onFinish() {
                    UploadFoodActivity.this.dismissLoading();
                }
            }, pathList);
        }
    }

    private void uploadToServer(List<QiniuModel> list) {
        if (list != null && list.size() >= 2) {
            try {
                JSONArray photos = new JSONArray();
                for (QiniuModel model : list) {
                    JSONObject photo = new JSONObject();
                    photo.put("_type", TextUtils.isEmpty(model.hash) ? QiniuPhoto.TYPE_BOOHEE :
                            QiniuPhoto.TYPE_QINIU);
                    if (this.frontImgPath.equals(model.path)) {
                        photo.put("photo_type", "front");
                    } else if (this.backImgPath.equals(model.path)) {
                        photo.put("photo_type", "back");
                    }
                    photo.put("qiniu_key", model.key);
                    photo.put("qiniu_hash", model.hash);
                    photo.put("origin_width", 120);
                    photo.put("origin_height", 120);
                    photos.put(photo);
                }
                JsonParams food_draft = new JsonParams();
                food_draft.put("food_name", this.name);
                food_draft.put("barcode", this.code);
                food_draft.put("brand", this.brand);
                food_draft.put(WeightRecordDao.PHOTOS, photos);
                JsonParams json_param = new JsonParams();
                json_param.put("food_draft", food_draft);
                showLoading();
                FoodApi.uploadFood(json_param, this.activity, new JsonCallback(this.activity) {
                    public void ok(JSONObject object) {
                        super.ok(object);
                        Helper.showToast((int) R.string.ab1);
                        EventBus.getDefault().post(new MyFoodEvent().setFlag(2));
                        UploadFoodActivity.this.finish();
                    }

                    public void onFinish() {
                        UploadFoodActivity.this.dismissLoading();
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (WheelUtils.isFastDoubleClick()) {
            return true;
        }
        switch (item.getItemId()) {
            case R.id.action_upload_food:
                uploadFood();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public static void comeOnBaby(Context context) {
        context.startActivity(new Intent(context, UploadFoodActivity.class));
    }

    public static void comeOnBabyWithCode(Context context, String code) {
        Intent intent = new Intent(context, UploadFoodActivity.class);
        intent.putExtra(FOOD_CODE, code);
        context.startActivity(intent);
    }
}
