package com.boohee.one.ui;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import com.alipay.sdk.sys.a;
import com.boohee.api.StatusApi;
import com.boohee.database.UserPreference;
import com.boohee.model.BetWeight;
import com.boohee.modeldao.WeightRecordDao;
import com.boohee.one.R;
import com.boohee.one.bet.BetBrowserFragment;
import com.boohee.one.http.JsonCallback;
import com.boohee.one.http.JsonParams;
import com.boohee.one.sport.DownloadService;
import com.boohee.status.LargeImageActivity;
import com.boohee.uploader.QiniuConfig.Prefix;
import com.boohee.uploader.QiniuModel;
import com.boohee.uploader.QiniuUploader;
import com.boohee.uploader.UploadHandler;
import com.boohee.utility.ImageLoaderOptions;
import com.boohee.utils.FastJsonUtils;
import com.boohee.utils.Helper;
import com.boohee.utils.ViewUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

import de.greenrobot.event.EventBus;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.nereo.multi_image_selector.MultiImageSelectorActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class WebViewPicUploadActivity extends BaseActivity {
    public static final  String KEY_WEIGHT_PARAMS   = "key_weight_params";
    public static final  int    MODE_WEIGHT         = 0;
    public static final  int    MODE_WHOLE          = 1;
    private static final int    REQUEST_IMAGE       = 0;
    public static final  int    REQUEST_METHOD_POST = 1;
    public static final  int    REQUEST_METHOD_PUT  = 2;
    private int bet_id;
    private boolean isNeedUpload = false;
    @InjectView(2131428008)
    CheckBox mCbSyncPost;
    private float mCurrentWeight;
    @InjectView(2131428003)
    EditText  mEtCurrentWeight;
    @InjectView(2131428006)
    ImageView mIvWeight;
    @InjectView(2131428007)
    ImageView mIvWeightDelete;
    @InjectView(2131428004)
    ImageView mIvWhole;
    @InjectView(2131428005)
    ImageView mIvWholeDelete;
    private int          mMode          = -1;
    private int          mRequestMethod = -1;
    private List<String> mUploadList    = new ArrayList();
    private String mWeightParams;
    private String mWeightPath;
    private String mWholePath;
    private int    order_id;
    private String type;

    public enum UPLOAD_TYPE {
        base_upload,
        end_upload
    }

    private class UploadWeightCallBack extends JsonCallback {
        public UploadWeightCallBack(Activity activity) {
            super(activity);
        }

        public void ok(JSONObject object) {
            super.ok(object);
            EventBus.getDefault().post(BetBrowserFragment.REFFRESH);
            WebViewPicUploadActivity.this.finish();
        }

        public void fail(String message) {
            super.fail(message);
            Helper.showToast((CharSequence) message);
        }

        public void onFinish() {
            super.onFinish();
            WebViewPicUploadActivity.this.dismissLoading();
        }
    }

    @OnClick({2131428009, 2131428002, 2131428006, 2131428007, 2131428004, 2131428005})
    public void OnClick(View v) {
        switch (v.getId()) {
            case R.id.iv_example:
                startActivity(new Intent(this.activity, DushouExampleActivity.class));
                return;
            case R.id.iv_whole:
                if (TextUtils.isEmpty(this.mWholePath)) {
                    this.mMode = 1;
                    showTakePhotoDialog();
                    return;
                }
                LargeImageActivity.start(this.activity, this.mWholePath);
                return;
            case R.id.iv_whole_delete:
                new Builder(this.activity).setMessage("确定要删除图片吗？").setPositiveButton("删除", new
                        OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        WebViewPicUploadActivity.this.mIvWholeDelete.setVisibility(8);
                        WebViewPicUploadActivity.this.mWholePath = null;
                        WebViewPicUploadActivity.this.mIvWhole.setImageDrawable
                                (WebViewPicUploadActivity.this.getResources().getDrawable(R
                                        .drawable.qv));
                    }
                }).setNegativeButton("取消", null).show();
                return;
            case R.id.iv_weight:
                if (TextUtils.isEmpty(this.mWeightPath)) {
                    this.mMode = 0;
                    showTakePhotoDialog();
                    return;
                }
                LargeImageActivity.start(this.activity, this.mWeightPath);
                return;
            case R.id.iv_weight_delete:
                new Builder(this.activity).setMessage("确定要删除图片吗？").setPositiveButton("删除", new
                        OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        WebViewPicUploadActivity.this.mIvWeightDelete.setVisibility(8);
                        WebViewPicUploadActivity.this.mWeightPath = null;
                        WebViewPicUploadActivity.this.mIvWeight.setImageDrawable
                                (WebViewPicUploadActivity.this.getResources().getDrawable(R
                                        .drawable.qv));
                    }
                }).setNegativeButton("取消", null).show();
                return;
            case R.id.btn_commit:
                postBetWeight();
                return;
            default:
                return;
        }
    }

    public static void startMe(Context context, String params) {
        Intent intent = new Intent(context, WebViewPicUploadActivity.class);
        intent.putExtra(KEY_WEIGHT_PARAMS, params);
        context.startActivity(intent);
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dv);
        ButterKnife.inject((Activity) this);
        this.mWeightParams = getIntent().getStringExtra(KEY_WEIGHT_PARAMS);
        init();
        requestBetWeight();
    }

    private void requestBetWeight() {
        if (this.bet_id >= 0) {
            StatusApi.getBetWeight(this.activity, this.bet_id, this.order_id, new JsonCallback
                    (this.activity) {
                public void ok(JSONObject object) {
                    super.ok(object);
                    WebViewPicUploadActivity.this.bet_id = object.optInt("bet_id");
                    WebViewPicUploadActivity.this.type = object.optString("type");
                    BetWeight betWeight = (BetWeight) FastJsonUtils.fromJson(object.optJSONObject
                            ("bet_weight"), BetWeight.class);
                    String title = "";
                    if (TextUtils.equals(WebViewPicUploadActivity.this.type, UPLOAD_TYPE
                            .base_upload.name())) {
                        if (betWeight == null || betWeight.base_weight == 0.0f) {
                            WebViewPicUploadActivity.this.mRequestMethod = 1;
                            title = "上传";
                        } else {
                            title = "修改";
                            WebViewPicUploadActivity.this.mRequestMethod = 2;
                            WebViewPicUploadActivity.this.mCurrentWeight = betWeight.base_weight;
                        }
                        WebViewPicUploadActivity.this.setTitle(title + "初始体重");
                    } else if (TextUtils.equals(WebViewPicUploadActivity.this.type, UPLOAD_TYPE
                            .end_upload.name())) {
                        WebViewPicUploadActivity.this.mRequestMethod = 2;
                        if (betWeight == null || betWeight.end_weight == 0.0f) {
                            title = "上传";
                        } else {
                            title = "修改";
                            WebViewPicUploadActivity.this.mCurrentWeight = betWeight.end_weight;
                        }
                        WebViewPicUploadActivity.this.setTitle(title + "结束体重");
                    }
                    if (!(betWeight == null || TextUtils.isEmpty(betWeight.photos_0))) {
                        WebViewPicUploadActivity.this.mWeightPath = betWeight.photos_0;
                        ImageLoader.getInstance().displayImage(betWeight.photos_0,
                                WebViewPicUploadActivity.this.mIvWeight, ImageLoaderOptions
                                        .global((int) R.drawable.qv));
                        WebViewPicUploadActivity.this.mIvWeightDelete.setVisibility(0);
                    }
                    if (!(betWeight == null || TextUtils.isEmpty(betWeight.photos_1))) {
                        WebViewPicUploadActivity.this.mWholePath = betWeight.photos_1;
                        ImageLoader.getInstance().displayImage(betWeight.photos_1,
                                WebViewPicUploadActivity.this.mIvWhole, ImageLoaderOptions.global
                                        ((int) R.drawable.qv));
                        WebViewPicUploadActivity.this.mIvWholeDelete.setVisibility(0);
                    }
                    if (WebViewPicUploadActivity.this.mCurrentWeight > 0.0f) {
                        WebViewPicUploadActivity.this.mEtCurrentWeight.setText
                                (WebViewPicUploadActivity.this.mCurrentWeight + "");
                        WebViewPicUploadActivity.this.mEtCurrentWeight.setSelection
                                (WebViewPicUploadActivity.this.mEtCurrentWeight.length());
                    }
                }

                public void onFinish() {
                    super.onFinish();
                    WebViewPicUploadActivity.this.dismissLoading();
                }
            });
        }
    }

    private void postBetWeight() {
        this.mUploadList.clear();
        String weightStr = this.mEtCurrentWeight.getText().toString();
        if (TextUtils.isEmpty(weightStr)) {
            Helper.showToast((CharSequence) "体重输入不能为空");
            return;
        }
        this.mCurrentWeight = Float.valueOf(weightStr).floatValue();
        if (this.mCurrentWeight < 30.0f || this.mCurrentWeight > 200.0f) {
            Helper.showToast((CharSequence) "体重输入不在合理范围，请重新输入~~");
        } else if (TextUtils.isEmpty(this.mWeightPath)) {
            Helper.showToast((CharSequence) "请选择称重照");
        } else {
            if (!this.mWeightPath.startsWith("http")) {
                this.mUploadList.add(this.mWeightPath);
            }
            if (TextUtils.isEmpty(this.mWholePath)) {
                Helper.showToast((CharSequence) "请选择正面全身照");
                return;
            }
            if (!this.mWholePath.startsWith("http")) {
                this.mUploadList.add(this.mWholePath);
            }
            if (!this.isNeedUpload || this.mUploadList.size() <= 0) {
                sendWeightRequest(null, this.mCbSyncPost.isChecked());
                return;
            }
            showLoading();
            QiniuUploader.upload(Prefix.status, new UploadHandler() {
                public void onSuccess(List<QiniuModel> infos) {
                    if (WebViewPicUploadActivity.this.mCbSyncPost != null &&
                            WebViewPicUploadActivity.this.activity != null &&
                            !WebViewPicUploadActivity.this.activity.isFinishing()) {
                        JSONArray photosArray = new JSONArray();
                        int i = 0;
                        while (i < infos.size()) {
                            try {
                                QiniuModel model = (QiniuModel) infos.get(i);
                                JSONObject photo = new JSONObject();
                                if (model.path.equals(WebViewPicUploadActivity.this.mWeightPath)) {
                                    photo.put(DownloadService.EXTRA_TAG, 0);
                                } else if (model.path.equals(WebViewPicUploadActivity.this
                                        .mWholePath)) {
                                    photo.put(DownloadService.EXTRA_TAG, 1);
                                }
                                photo.put("qiniu_key", model.key);
                                photo.put("qiniu_hash", model.hash);
                                photosArray.put(photo);
                                i++;
                            } catch (JSONException e) {
                                return;
                            }
                        }
                        WebViewPicUploadActivity.this.sendWeightRequest(photosArray,
                                WebViewPicUploadActivity.this.mCbSyncPost.isChecked());
                    }
                }

                public void onError(String msg) {
                    if (WebViewPicUploadActivity.this.activity != null &&
                            !WebViewPicUploadActivity.this.activity.isFinishing()) {
                        Helper.showToast((CharSequence) msg);
                    }
                }

                public void onFinish() {
                    WebViewPicUploadActivity.this.dismissLoading();
                }
            }, this.mUploadList);
        }
    }

    private void showTakePhotoDialog() {
        Intent intent = new Intent(this.activity, MultiImageSelectorActivity.class);
        intent.putExtra("show_camera", true);
        intent.putExtra("max_select_count", 1);
        intent.putExtra("select_count_mode", 0);
        startActivityForResult(intent, 0);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            List<String> path = data.getStringArrayListExtra(MultiImageSelectorActivity
                    .EXTRA_RESULT);
            if (path != null || path.size() != 0) {
                this.isNeedUpload = true;
                if (this.mMode == 0) {
                    this.mWeightPath = (String) path.get(0);
                    ViewUtils.initImageView(this.activity, Uri.fromFile(new File(this
                            .mWeightPath)), this.mIvWeight);
                    this.mIvWeightDelete.setVisibility(0);
                } else if (this.mMode == 1) {
                    this.mWholePath = (String) path.get(0);
                    ViewUtils.initImageView(this.activity, Uri.fromFile(new File(this.mWholePath)
                    ), this.mIvWhole);
                    this.mIvWholeDelete.setVisibility(0);
                }
            }
        }
    }

    private void init() {
        if (!TextUtils.isEmpty(this.mWeightParams)) {
            String[] keyValues = this.mWeightParams.trim().split(a.b);
            Map<String, String> kvParams = new HashMap();
            for (String str : keyValues) {
                String[] kv = str.split("=");
                if (kv.length == 2) {
                    kvParams.put(kv[0], kv[1]);
                }
            }
            this.bet_id = Integer.valueOf((String) kvParams.get("bet_id")).intValue();
            this.order_id = Integer.valueOf((String) kvParams.get("order_id")).intValue();
        }
    }

    private void sendWeightRequest(JSONArray photosArray, boolean isSync) {
        if (this.mRequestMethod == 1) {
            StatusApi.postBetWeight(this.activity, this.bet_id, this.order_id, getJsonParams
                    (photosArray, isSync, this.bet_id, this.order_id, this.type), new
                    UploadWeightCallBack(this));
        } else if (this.mRequestMethod == 2) {
            StatusApi.putBetWeight(this.activity, this.bet_id, this.order_id, getJsonParams
                    (photosArray, isSync, this.bet_id, this.order_id, this.type), new
                    UploadWeightCallBack(this));
        }
    }

    private JsonParams getJsonParams(JSONArray photosArray, boolean isSync, int bet_id, int
            order_id, String type) {
        JsonParams root = new JsonParams();
        root.put("token", UserPreference.getToken(this.activity));
        JsonParams bet_weight = new JsonParams();
        if (TextUtils.equals(type, UPLOAD_TYPE.base_upload.name())) {
            bet_weight.put("base_weight", this.mCurrentWeight);
        } else if (TextUtils.equals(type, UPLOAD_TYPE.end_upload.name())) {
            bet_weight.put("end_weight", this.mCurrentWeight);
        }
        String str = WeightRecordDao.PHOTOS;
        if (photosArray == null) {
            photosArray = new JSONArray();
        }
        bet_weight.put(str, photosArray);
        root.put("bet_weight", bet_weight);
        root.put("bet_id", bet_id);
        root.put("order_id", order_id);
        root.put("type", type);
        root.put("sync", isSync ? "yes" : "no");
        return root;
    }
}
