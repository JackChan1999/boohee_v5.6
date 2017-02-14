package com.boohee.food;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import com.boohee.api.RecordApi;
import com.boohee.main.GestureActivity;
import com.boohee.model.RecordPhoto;
import com.boohee.one.R;
import com.boohee.one.event.PhotoDietEvent;
import com.boohee.one.http.JsonCallback;
import com.boohee.utility.ImageLoaderOptions;
import com.boohee.utils.TextUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

import de.greenrobot.event.EventBus;

import org.json.JSONObject;

import uk.co.senab.photoview.PhotoView;

public class EditCameraRecordActivity extends GestureActivity {
    private static final String KEY_INDEX       = "key_index";
    private static final String KEY_RECORDPHOTO = "key_recordphoto";
    @InjectView(2131427433)
    PhotoView    iv_photo;
    @InjectView(2131427516)
    LinearLayout ll_bingo_estimate;
    private int         mIndex;
    private RecordPhoto mRecordPhoto;
    @InjectView(2131427461)
    TextView tv_calory;
    @InjectView(2131427518)
    TextView tv_message;
    @InjectView(2131427514)
    TextView tv_name;
    @InjectView(2131427436)
    TextView tv_unit;
    @InjectView(2131427517)
    View     view_divide_estimate;
    @InjectView(2131427519)
    View     view_divide_messsage;
    @InjectView(2131427515)
    View     view_divide_name;

    public static void start(Context context, RecordPhoto recordPhoto, int index) {
        Intent starter = new Intent(context, EditCameraRecordActivity.class);
        starter.putExtra(KEY_RECORDPHOTO, recordPhoto);
        starter.putExtra("key_index", index);
        context.startActivity(starter);
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.an);
        ButterKnife.inject((Activity) this);
        handleIntent();
        initView();
    }

    private void handleIntent() {
        this.mRecordPhoto = (RecordPhoto) getIntent().getParcelableExtra(KEY_RECORDPHOTO);
        this.mIndex = getIntExtra("key_index");
    }

    private void initView() {
        if (this.mRecordPhoto != null) {
            ImageLoader.getInstance().displayImage(this.mRecordPhoto.photo_url, this.iv_photo,
                    ImageLoaderOptions.color(R.color.ju));
            if (TextUtils.isEmpty(this.mRecordPhoto.name)) {
                this.tv_name.setVisibility(8);
                this.view_divide_name.setVisibility(8);
            } else {
                this.tv_name.setText(this.mRecordPhoto.name);
            }
            int status = this.mRecordPhoto.status;
            if (status == 1) {
                this.tv_message.setVisibility(0);
                this.tv_message.setText("请等待顾问估算热量...");
                this.view_divide_messsage.setVisibility(0);
            } else if (status == 2) {
                if (this.mRecordPhoto.calory > 0.0f) {
                    this.ll_bingo_estimate.setVisibility(0);
                    this.tv_calory.setText(Math.round(this.mRecordPhoto.calory) + "千卡");
                    if (!TextUtil.isNull(this.mRecordPhoto.consultor_name)) {
                        this.tv_unit.setText("由" + this.mRecordPhoto.consultor_name + "顾问估算");
                    }
                    this.tv_message.setVisibility(0);
                    this.view_divide_messsage.setVisibility(0);
                    this.tv_message.setText(TextUtils.isEmpty(this.mRecordPhoto.comment) ? "" :
                            this.mRecordPhoto.comment);
                }
            } else if (status == 3) {
                this.ll_bingo_estimate.setVisibility(0);
                this.tv_message.setVisibility(0);
                this.tv_message.setText("您选择的照片有误，无法估算");
                this.tv_calory.setText("0千卡");
                if (TextUtils.isEmpty(this.mRecordPhoto.consultor_name)) {
                    this.tv_unit.setText("由" + this.mRecordPhoto.consultor_name + "顾问估算");
                }
            } else if (status == 4) {
                if (this.mRecordPhoto.calory > 0.0f) {
                    this.ll_bingo_estimate.setVisibility(0);
                    this.view_divide_estimate.setVisibility(0);
                    this.tv_calory.setText(Math.round(this.mRecordPhoto.calory) + "千卡");
                    if (!TextUtil.isNull(this.mRecordPhoto.consultor_name)) {
                        this.tv_unit.setText("由" + this.mRecordPhoto.consultor_name + "顾问估算");
                    }
                } else {
                    this.view_divide_estimate.setVisibility(8);
                    this.ll_bingo_estimate.setVisibility(8);
                }
                this.tv_message.setVisibility(8);
                this.view_divide_messsage.setVisibility(8);
            }
        }
    }

    private void deletePhotoEating() {
        if (this.mRecordPhoto != null) {
            showLoading();
            RecordApi.deleteDietPhotos(this.activity, this.mRecordPhoto.id, new JsonCallback(this
                    .activity) {
                public void ok(JSONObject object) {
                    super.ok(object);
                    if (EditCameraRecordActivity.this.mRecordPhoto != null) {
                        EventBus.getDefault().post(new PhotoDietEvent().setTimeType
                                (EditCameraRecordActivity.this.mRecordPhoto.time_type).setIndex
                                (EditCameraRecordActivity.this.mIndex).setEditType(3));
                        EditCameraRecordActivity.this.finish();
                    }
                }

                public void onFinish() {
                    super.onFinish();
                    EditCameraRecordActivity.this.dismissLoading();
                }
            });
        }
    }

    @OnClick({2131427520})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_delete:
                new Builder(this.activity).setMessage("确定要删除吗？").setPositiveButton("删除", new
                        OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        EditCameraRecordActivity.this.deletePhotoEating();
                    }
                }).setNegativeButton("取消", null).show();
                return;
            default:
                return;
        }
    }
}
