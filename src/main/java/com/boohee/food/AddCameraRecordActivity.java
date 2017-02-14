package com.boohee.food;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Selection;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ToggleButton;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import com.boohee.api.RecordApi;
import com.boohee.main.GestureActivity;
import com.boohee.model.RecordPhoto;
import com.boohee.one.R;
import com.boohee.one.event.AddFinishAnimEvent;
import com.boohee.one.event.PhotoDietEvent;
import com.boohee.one.http.JsonCallback;
import com.boohee.uploader.QiniuConfig.Prefix;
import com.boohee.uploader.QiniuModel;
import com.boohee.uploader.QiniuUploader;
import com.boohee.uploader.UploadHandler;
import com.boohee.utility.ImageLoaderOptions;
import com.boohee.utils.FastJsonUtils;
import com.boohee.utils.Helper;
import com.nostra13.universalimageloader.core.ImageLoader;

import de.greenrobot.event.EventBus;

import java.io.File;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import uk.co.senab.photoview.PhotoView;

public class AddCameraRecordActivity extends GestureActivity {
    private static final String KEY_DATE      = "key_date";
    private static final String KEY_IMG_PATH  = "key_img_path";
    private static final String KEY_TIME_TYPE = "key_time_type";
    @InjectView(2131427435)
    EditText et_calory;
    @InjectView(2131427434)
    EditText et_name;
    private boolean isAnalysis = false;
    @InjectView(2131427433)
    PhotoView iv_photo;
    private int    mCalory;
    private String mImagePath;
    private String mInputCalory;
    private int    mTimeType;
    private String record_on;
    @InjectView(2131427438)
    ToggleButton toggle_bingo;

    public static void start(Context context, int timeType, String record_on, String imgPath) {
        Intent starter = new Intent(context, AddCameraRecordActivity.class);
        starter.putExtra(KEY_TIME_TYPE, timeType);
        starter.putExtra("key_date", record_on);
        starter.putExtra(KEY_IMG_PATH, imgPath);
        context.startActivity(starter);
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a7);
        ButterKnife.inject((Activity) this);
        handleIntent();
        initBingo();
        addListener();
    }

    private void handleIntent() {
        this.mTimeType = getIntExtra(KEY_TIME_TYPE);
        this.record_on = getStringExtra("key_date");
        this.mImagePath = getStringExtra(KEY_IMG_PATH);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.b, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_finish:
                if (TextUtils.isEmpty(this.mImagePath)) {
                    Helper.showToast((CharSequence) "请先选择食物图片");
                } else {
                    uploadPhoto();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void addListener() {
        this.toggle_bingo.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    AddCameraRecordActivity.this.mInputCalory = AddCameraRecordActivity.this
                            .et_calory.getText().toString();
                    AddCameraRecordActivity.this.et_calory.getText().clear();
                    AddCameraRecordActivity.this.et_calory.setHint("请等待顾问为你估算");
                    AddCameraRecordActivity.this.et_calory.setEnabled(false);
                    return;
                }
                if (!TextUtils.isEmpty(AddCameraRecordActivity.this.mInputCalory)) {
                    AddCameraRecordActivity.this.et_calory.setText(AddCameraRecordActivity.this
                            .mInputCalory);
                    Selection.setSelection(AddCameraRecordActivity.this.et_calory.getText(),
                            AddCameraRecordActivity.this.et_calory.getText().length());
                }
                AddCameraRecordActivity.this.et_calory.setHint("所含的热量（可不填）");
                AddCameraRecordActivity.this.et_calory.setEnabled(true);
            }
        });
    }

    private void initBingo() {
        ImageLoader.getInstance().displayImage(Uri.decode(Uri.fromFile(new File(this.mImagePath))
                .toString()), this.iv_photo, ImageLoaderOptions.color(R.color.ju));
    }

    public void uploadPhoto() {
        showLoading();
        QiniuUploader.upload(Prefix.record, new UploadHandler() {
            public void onSuccess(List<QiniuModel> infos) {
                QiniuModel model = (QiniuModel) infos.get(0);
                try {
                    JSONArray array = new JSONArray();
                    JSONObject obj = new JSONObject();
                    obj.put("qiniu_key", model.key);
                    obj.put("qiniu_hash", model.hash);
                    array.put(obj);
                    AddCameraRecordActivity.this.postWeight(model.key);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            public void onError(String msg) {
                Helper.showToast((CharSequence) msg);
            }

            public void onFinish() {
                AddCameraRecordActivity.this.dismissLoading();
            }
        }, this.mImagePath);
    }

    private void postWeight(String qiniu_key) {
        String calory = this.et_calory.getText().toString().trim();
        String name = this.et_name.getText().toString().trim();
        if (!TextUtils.isEmpty(calory)) {
            this.mCalory = Integer.parseInt(calory);
        }
        this.isAnalysis = this.toggle_bingo.isChecked();
        showLoading();
        RecordApi.postDietPhotos(this.activity, this.mTimeType, this.record_on, qiniu_key, this
                .isAnalysis, name, this.mCalory, new JsonCallback(this.activity) {
            public void ok(JSONObject object) {
                super.ok(object);
                RecordPhoto recordPhoto = (RecordPhoto) FastJsonUtils.fromJson(object,
                        RecordPhoto.class);
                if (recordPhoto != null) {
                    EventBus.getDefault().post(new PhotoDietEvent().setTimeType
                            (AddCameraRecordActivity.this.mTimeType).setRecordPhoto(recordPhoto)
                            .setEditType(1));
                    EventBus.getDefault().post(new AddFinishAnimEvent().setThumb_image_name
                            (AddCameraRecordActivity.this.mImagePath));
                }
                AddCameraRecordActivity.this.finish();
            }

            public void onFinish() {
                super.onFinish();
                AddCameraRecordActivity.this.dismissLoading();
            }
        });
    }

    @OnClick({2131427437})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_invite_bingo:
                this.toggle_bingo.setChecked(!this.toggle_bingo.isChecked());
                return;
            default:
                return;
        }
    }
}
