package com.boohee.one.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import com.boohee.model.mine.WeightPreviewPhoto;
import com.boohee.one.R;
import com.boohee.utility.ImageLoaderOptions;
import com.boohee.widgets.PathListView;
import com.nostra13.universalimageloader.core.ImageLoader;

public class WeightImageFragment extends BaseFragment {
    @InjectView(2131428006)
    ImageView iv_weight;
    private float mHeight;
    @InjectView(2131428363)
    TextView tv_bmi;
    @InjectView(2131427614)
    TextView tv_date;
    @InjectView(2131427651)
    TextView tv_weight;
    public WeightPreviewPhoto weightRecord;

    public void setWeightRecord(WeightPreviewPhoto weightRecord) {
        this.weightRecord = weightRecord;
    }

    public void setHeight(float height) {
        this.mHeight = height;
    }

    public static WeightImageFragment newInstance(WeightPreviewPhoto weightRecord, float height) {
        WeightImageFragment fragment = new WeightImageFragment();
        fragment.weightRecord = weightRecord;
        fragment.mHeight = height;
        return fragment;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        return inflater.inflate(R.layout.gt, null);
    }

    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.inject((Object) this, view);
        init();
    }

    private void init() {
        if (this.weightRecord != null) {
            String url = this.weightRecord.photo_url;
            if (!TextUtils.isEmpty(url)) {
                ImageLoader.getInstance().displayImage(url, this.iv_weight, ImageLoaderOptions
                        .color(17170445));
                this.iv_weight.setVisibility(0);
            }
            this.tv_date.setText(String.format(getResources().getString(R.string.adf), new
                    Object[]{this.weightRecord.record_on}));
            this.tv_weight.setText(String.format(getResources().getString(R.string.ado), new
                    Object[]{Float.valueOf(this.weightRecord.weight)}));
            if (this.mHeight != 0.0f) {
                double bmi = ((double) this.weightRecord.weight) / Math.pow((double) (this
                        .mHeight / 100.0f), PathListView.ZOOM_X2);
                this.tv_bmi.setText(String.format(getResources().getString(R.string.adb), new
                        Object[]{Float.valueOf(Float.parseFloat(String.valueOf(bmi)))}));
                this.tv_bmi.setVisibility(0);
                return;
            }
            this.tv_bmi.setVisibility(8);
        }
    }

    @OnClick({2131428006})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_weight:
                getActivity().finish();
                getActivity().overridePendingTransition(0, R.anim.ap);
                return;
            default:
                return;
        }
    }
}
