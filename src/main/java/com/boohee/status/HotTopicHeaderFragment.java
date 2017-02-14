package com.boohee.status;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.boohee.model.status.HotTopicSliders;
import com.boohee.one.R;
import com.boohee.one.ui.fragment.BaseFragment;
import com.boohee.utility.BooheeScheme;
import com.boohee.utility.ImageLoaderOptions;

public class HotTopicHeaderFragment extends BaseFragment implements OnClickListener {
    private ImageView       mImageView;
    public  HotTopicSliders mSlider;

    public static HotTopicHeaderFragment newInstance(HotTopicSliders slider) {
        HotTopicHeaderFragment fragment = new HotTopicHeaderFragment();
        fragment.mSlider = slider;
        return fragment;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        return inflater.inflate(R.layout.g0, null);
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.mImageView = (ImageView) getView().findViewById(R.id.image);
        if (!TextUtils.isEmpty(this.mSlider.pic_url)) {
            this.imageLoader.displayImage(this.mSlider.pic_url, this.mImageView,
                    ImageLoaderOptions.color(R.color.ju));
        }
        this.mImageView.setOnClickListener(this);
    }

    public void onClick(View v) {
        if (!TextUtils.isEmpty(this.mSlider.url)) {
            BooheeScheme.handleUrl(getActivity(), this.mSlider.url);
        }
    }
}
