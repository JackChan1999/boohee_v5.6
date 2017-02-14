package com.boohee.record;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import com.boohee.model.mine.WeightPhoto;
import com.boohee.model.mine.WeightRecord;
import com.boohee.one.R;
import com.boohee.one.ui.adapter.WeightDetailAdapter;
import com.boohee.one.ui.fragment.BaseDialogFragment;
import com.boohee.utility.ImageLoaderOptions;
import com.boohee.utils.DateFormatUtils;
import com.boohee.utils.DateHelper;
import com.boohee.utils.ViewUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.File;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;

import me.nereo.multi_image_selector.MultiImageSelectorActivity;

public class ScaleRecordFragment extends BaseDialogFragment {
    public static final String TAG   = "scale_record";
    public static final String TODAY = DateHelper.format(new Date());
    private boolean byScale;
    private DecimalFormat format = new DecimalFormat("#.0");
    @InjectView(2131428340)
    GridView  gv;
    @InjectView(2131428337)
    ImageView ivAchieve;
    @InjectView(2131427455)
    View      ivCamera;
    @InjectView(2131428339)
    ImageView ivImage;
    @InjectView(2131428338)
    ImageView ivRunning;
    private WeightDetailAdapter mAdapter;
    private WeightRecordHelper  mHelper;
    private String              mImagePath;
    private WeightRecord        mRecord;
    private Random random = new Random(System.currentTimeMillis());
    private String         record_on;
    private ObjectAnimator rotateAnim;
    private float          tempWeight;
    @InjectView(2131427614)
    TextView tvDate;
    @InjectView(2131428185)
    TextView tvDelete;
    @InjectView(2131428188)
    TextView tvSend;
    @InjectView(2131427651)
    TextView tvWeight;

    public static ScaleRecordFragment newInstance(WeightRecord record, String record_on) {
        ScaleRecordFragment fragment = new ScaleRecordFragment();
        fragment.mRecord = record;
        fragment.record_on = record_on;
        return fragment;
    }

    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getActivity(), R.style.di) {
            public void onBackPressed() {
                if (ScaleConnHelper.getInstance() != null) {
                    ScaleConnHelper.getInstance().notShowWithUnsteadyWeight();
                }
                super.onBackPressed();
            }
        };
        dialog.getWindow().setWindowAnimations(R.style.de);
        return dialog;
    }

    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.mHelper = new WeightRecordHelper(this);
    }

    @Nullable
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.gi, container, false);
        ButterKnife.inject((Object) this, view);
        return view;
    }

    @OnClick({2131427455, 2131428185, 2131428186, 2131428187})
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
                if (ScaleConnHelper.getInstance() != null) {
                    ScaleConnHelper.getInstance().notShowWithUnsteadyWeight();
                }
                dismiss();
                return;
            case R.id.tv_delete:
                if (!this.byScale) {
                    new Builder(getActivity()).setMessage("确定要删除吗？").setCancelable(false)
                            .setPositiveButton("删除", new OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            ScaleRecordFragment.this.mHelper.deleteWeight(ScaleRecordFragment
                                    .this.record_on);
                        }
                    }).setNegativeButton("取消", null).show();
                    return;
                }
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

    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.mAdapter = new WeightDetailAdapter(getActivity(), this);
        this.gv.setAdapter(this.mAdapter);
        setRecord(this.mRecord);
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

    public void onStop() {
        super.onStop();
        if (this.mRecord == null) {
            showUnknownResult();
        }
    }

    public void setRecord(WeightRecord record) {
        if (record == null) {
            showMeasuring();
        } else {
            refreshView(record);
        }
    }

    private void refreshView(WeightRecord record) {
        if (record != null) {
            if (this.byScale) {
                completeMeasure();
            }
            this.tvWeight.setText(record.weight);
            this.mAdapter.setData(record);
            if (DateFormatUtils.isToday(record.record_on)) {
                this.tvDate.setText("今天");
            } else {
                this.tvDate.setText(record.record_on);
            }
            showImage(record);
            this.mRecord = record;
        }
    }

    public void showUnSteadyWeight(float tempWeight) {
        this.tempWeight = tempWeight;
        showMeasuring();
    }

    public void showMeasuring() {
        if (!(this.mRecord == null || TODAY.equals(this.mRecord.record_on))) {
            this.ivImage.setImageResource(R.color.in);
            this.mImagePath = null;
        }
        this.mRecord = null;
        this.mAdapter.setData(null);
        this.byScale = true;
        this.tvDate.setText("今天");
        this.ivCamera.setEnabled(false);
        this.tvSend.setEnabled(false);
        this.tvSend.setBackgroundColor(-4134200);
        this.tvDelete.setVisibility(0);
        this.tvDelete.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        this.tvDelete.setText("测量中...");
        if (this.rotateAnim == null || !this.rotateAnim.isRunning()) {
            this.ivAchieve.setVisibility(8);
            this.ivRunning.setVisibility(0);
            this.rotateAnim = ObjectAnimator.ofFloat(this.ivRunning, "rotation", new float[]{0
                    .0f, 360.0f}).setDuration(800);
            this.rotateAnim.setRepeatCount(-1);
            this.rotateAnim.setInterpolator(new LinearInterpolator());
            this.rotateAnim.addUpdateListener(new AnimatorUpdateListener() {
                public void onAnimationUpdate(ValueAnimator animation) {
                    if (((double) ScaleRecordFragment.this.tempWeight) > 0.001d) {
                        ScaleRecordFragment.this.tvWeight.setText(ScaleRecordFragment.this.format
                                .format((double) ScaleRecordFragment.this.tempWeight));
                        return;
                    }
                    ScaleRecordFragment.this.tvWeight.setText(ScaleRecordFragment.this.format
                            .format((double) ((ScaleRecordFragment.this.random.nextFloat() * 70
                    .0f) + 20.0f)));
                }
            });
            this.rotateAnim.start();
        }
    }

    public void disconnect() {
        if (this.mRecord == null) {
            showUnknownResult();
        }
        this.tempWeight = 0.0f;
    }

    private void showUnknownResult() {
        if (this.rotateAnim != null) {
            this.rotateAnim.cancel();
            this.rotateAnim = null;
        }
        this.ivAchieve.setVisibility(0);
        this.ivRunning.setVisibility(8);
        this.tvDelete.setVisibility(0);
        this.tvDelete.setText("请重新测量");
        this.ivCamera.setEnabled(false);
        this.tvSend.setEnabled(false);
        this.tvWeight.setText("?");
    }

    private void completeMeasure() {
        if (this.rotateAnim != null) {
            this.rotateAnim.cancel();
            this.rotateAnim = null;
        }
        this.ivAchieve.setVisibility(0);
        this.ivRunning.setVisibility(8);
        this.tvDelete.setVisibility(8);
        this.ivCamera.setEnabled(true);
        this.tvSend.setEnabled(true);
        this.tvSend.setBackgroundColor(getResources().getColor(R.color.hb));
    }

    private void showImage(WeightRecord record) {
        if (TextUtils.isEmpty(this.mImagePath) && hasPhoto(record)) {
            ImageLoader.getInstance().displayImage(((WeightPhoto) record.photos.get(0))
                    .thumb_photo_url, this.ivImage, ImageLoaderOptions.global((int) R.drawable
                    .aay));
        }
    }

    private boolean needUpdate() {
        if (this.mRecord == null) {
            return false;
        }
        if (this.byScale) {
            return true;
        }
        if (this.mImagePath != null) {
            return true;
        }
        return false;
    }

    private void sendRequest() {
        this.mHelper.sendRequest(this.mRecord, this.mImagePath);
    }

    private boolean hasPhoto(WeightRecord record) {
        if (record == null || record.photos == null || record.photos.size() <= 0) {
            return false;
        }
        return true;
    }
}
