package com.boohee.record;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import boohee.lib.share.ShareManager;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import com.boohee.main.GestureActivity;
import com.boohee.model.WeightPhoto;
import com.boohee.one.R;
import com.boohee.utils.BitmapUtil;
import com.boohee.utils.DateFormatUtils;
import com.boohee.utils.FileUtil;
import com.boohee.utils.Helper;
import com.boohee.utils.TextUtil;

import java.io.File;
import java.text.DecimalFormat;

public class WeightCompareActivity extends GestureActivity {
    private static final String PHOTO_BEFORE = "photo_before";
    private static final String PHOTO_NOW    = "photo_now";
    @InjectView(2131428017)
    Button bt_save_compare;
    private String      mLocalFile;
    private WeightPhoto mPhotoBefore;
    private WeightPhoto mPhotoNow;
    @InjectView(2131428014)
    TextView     tv_day;
    @InjectView(2131428016)
    TextView     tv_reduce;
    @InjectView(2131428015)
    TextView     tv_state;
    @InjectView(2131428010)
    LinearLayout view_compare;
    @InjectView(2131428011)
    ImageView    view_photo_before;
    @InjectView(2131428012)
    ImageView    view_photo_now;

    @OnClick({2131428017, 2131428018})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_save_compare:
                saveCompare();
                return;
            case R.id.bt_share_compare:
                share();
                return;
            default:
                return;
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dw);
        ButterKnife.inject((Activity) this);
        handleIntent();
        initView();
    }

    private void handleIntent() {
        Intent intent = getIntent();
        this.mPhotoBefore = (WeightPhoto) intent.getParcelableExtra(PHOTO_BEFORE);
        this.mPhotoNow = (WeightPhoto) intent.getParcelableExtra(PHOTO_NOW);
    }

    private void initView() {
        this.imageLoader.displayImage(this.mPhotoBefore.photo_url, this.view_photo_before);
        this.imageLoader.displayImage(this.mPhotoNow.photo_url, this.view_photo_now);
        this.tv_day.setText(String.valueOf(DateFormatUtils.countDay(this.mPhotoBefore.record_on,
                this.mPhotoNow.record_on)));
        float reduce = this.mPhotoBefore.weight - this.mPhotoNow.weight;
        this.tv_state.setText(reduce >= 0.0f ? "减去" : "胖了");
        if (reduce == 0.0f) {
            this.tv_reduce.setText(String.valueOf(0));
        } else {
            this.tv_reduce.setText(new DecimalFormat("0.00").format((double) Math.abs(reduce)));
        }
    }

    private void saveCompare() {
        this.bt_save_compare.setEnabled(false);
        File file = FileUtil.saveImage(this, BitmapUtil.viewToBitmap(this.view_compare), String
                .valueOf(System.currentTimeMillis()));
        this.bt_save_compare.setEnabled(true);
        this.mLocalFile = file.getPath();
        Helper.showToast(this.mLocalFile);
    }

    private void share() {
        if (TextUtil.isEmpty(this.mLocalFile)) {
            saveCompare();
        }
        ShareManager.shareLocalImage(this.activity, this.mLocalFile);
    }

    public static void comeOnBaby(Context context, WeightPhoto photoBefore, WeightPhoto photoNow) {
        if (context != null && photoBefore != null && photoNow != null) {
            Intent intent = new Intent(context, WeightCompareActivity.class);
            intent.putExtra(PHOTO_BEFORE, photoBefore);
            intent.putExtra(PHOTO_NOW, photoNow);
            context.startActivity(intent);
        }
    }
}
