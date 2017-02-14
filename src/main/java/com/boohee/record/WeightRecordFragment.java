package com.boohee.record;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager.LayoutParams;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import com.boohee.database.OnePreference;
import com.boohee.model.mine.WeightPhoto;
import com.boohee.model.mine.WeightRecord;
import com.boohee.myview.BooheeRulerView;
import com.boohee.myview.BooheeRulerView.OnValueChangeListener;
import com.boohee.myview.IntFloatWheelView;
import com.boohee.one.R;
import com.boohee.one.ui.fragment.BaseDialogFragment;
import com.boohee.utility.ImageLoaderOptions;
import com.boohee.utils.ArithmeticUtil;
import com.boohee.utils.DateFormatUtils;
import com.boohee.utils.ViewUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.File;
import java.util.List;

import me.nereo.multi_image_selector.MultiImageSelectorActivity;

public class WeightRecordFragment extends BaseDialogFragment {
    public static final String TAG        = "weight_record";
    private final       int    MAX_WEIGHT = 200;
    private final       int    MIN_WEIGHT = 30;
    @InjectView(2131427455)
    View      ivCamera;
    @InjectView(2131428339)
    ImageView ivImage;
    private WeightRecordHelper mHelper;
    private String             mImagePath;
    private WeightRecord       mRecord;
    private String             record_on;
    @InjectView(2131428186)
    View            rippleCancle;
    @InjectView(2131428187)
    View            rippleSend;
    @InjectView(2131428183)
    BooheeRulerView ruler;
    @InjectView(2131427614)
    TextView        tvDate;
    @InjectView(2131428185)
    TextView        tvDelete;
    @InjectView(2131427651)
    TextView        tvWeight;

    public static WeightRecordFragment newInstance(WeightRecord record, String record_on) {
        WeightRecordFragment fragment = new WeightRecordFragment();
        fragment.mRecord = record;
        fragment.record_on = record_on;
        return fragment;
    }

    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.mHelper = new WeightRecordHelper(this);
    }

    @Nullable
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.gu, container, false);
        ButterKnife.inject((Object) this, view);
        return view;
    }

    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        updateDate(this.record_on);
        this.ruler.init(30.0f, 200.0f, getWeight(this.mRecord), 1.0f, 10, new
                OnValueChangeListener() {
            public void onValueChange(float value) {
                if (!WeightRecordFragment.this.isRemoved()) {
                    WeightRecordFragment.this.updateWeightView(value);
                }
            }
        });
        showImage(this.mRecord);
        if (this.mRecord == null) {
            this.tvDelete.setVisibility(4);
        }
    }

    @OnClick({2131428186, 2131428187, 2131427455, 2131428185})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_camera:
                if (hasPhoto(this.mRecord)) {
                    this.mHelper.goLargeImage(getActivity(), this.mRecord);
                    return;
                } else {
                    this.mHelper.showTakePhotoDialog();
                    return;
                }
            case R.id.tv_cancel:
                dismiss();
                return;
            case R.id.tv_delete:
                new Builder(getActivity()).setMessage("确定要删除吗？").setCancelable(false)
                        .setPositiveButton("删除", new OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        WeightRecordFragment.this.mHelper.deleteWeight(WeightRecordFragment.this
                                .record_on);
                    }
                }).setNegativeButton("取消", null).show();
                return;
            case R.id.tv_send:
                if (needUpdate()) {
                    sendRequest();
                    return;
                } else {
                    dismiss();
                    return;
                }
            default:
                return;
        }
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Dialog dialog = getDialog();
        dialog.getWindow().setGravity(80);
        LayoutParams lp = dialog.getWindow().getAttributes();
        lp.width = -1;
        dialog.getWindow().setAttributes(lp);
        dialog.getWindow().setWindowAnimations(R.style.de);
    }

    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0) {
            if (data != null) {
                List<String> path = data.getStringArrayListExtra(MultiImageSelectorActivity
                        .EXTRA_RESULT);
                if (path != null && path.size() > 0) {
                    this.mImagePath = (String) path.get(0);
                    ViewUtils.initImageView(getActivity(), Uri.fromFile(new File(this.mImagePath)
                    ), this.ivImage);
                }
            }
        } else if (requestCode == 1 && resultCode == -1) {
            this.mRecord.photos = null;
            this.ivImage.setImageResource(R.color.in);
        }
    }

    private void updateDate(String record_on) {
        if (DateFormatUtils.isToday(record_on)) {
            this.tvDate.setText("今天");
        } else {
            this.tvDate.setText(record_on);
        }
    }

    private void showImage(WeightRecord record) {
        if (hasPhoto(record)) {
            ImageLoader.getInstance().displayImage(((WeightPhoto) record.photos.get(0))
                    .thumb_photo_url, this.ivImage, ImageLoaderOptions.global((int) R.drawable
                    .aay));
        }
    }

    private float getWeight(WeightRecord record) {
        float weight = 0.0f;
        if (record != null) {
            weight = ArithmeticUtil.safeParseFloat(record.weight);
        }
        if (!isWeightValid(weight)) {
            weight = OnePreference.getLatestWeight();
        }
        if (isWeightValid(weight)) {
            return weight;
        }
        return IntFloatWheelView.DEFAULT_VALUE;
    }

    private boolean hasPhoto(WeightRecord record) {
        if (record == null || record.photos == null || record.photos.size() <= 0) {
            return false;
        }
        return true;
    }

    private void updateWeightView(float value) {
        this.tvWeight.setText(String.valueOf(value));
    }

    private boolean isWeightValid(float weight) {
        return weight >= 30.0f && weight <= 200.0f;
    }

    private boolean needUpdate() {
        if (this.mRecord != null && this.tvWeight.getText().toString().equals(this.mRecord
                .weight) && this.mImagePath == null) {
            return false;
        }
        return true;
    }

    private void sendRequest() {
        this.mHelper.sendRequest(this.tvWeight.getText().toString(), this.record_on, this.mImagePath);
    }
}
