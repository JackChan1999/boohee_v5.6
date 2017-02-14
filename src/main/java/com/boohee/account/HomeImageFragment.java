package com.boohee.account;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.boohee.model.CustomSliderImage;
import com.boohee.one.R;
import com.boohee.one.ui.fragment.BaseFragment;
import com.boohee.utility.BooheeScheme;
import com.boohee.utility.ImageLoaderOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.analytics.MobclickAgent;

public class HomeImageFragment<T extends CustomSliderImage> extends BaseFragment {
    public T data;
    protected ImageLoader imageLoader = ImageLoader.getInstance();

    public void setData(T data) {
        this.data = data;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        return inflater.inflate(R.layout.g0, null);
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initUI();
    }

    public void initUI() {
        if (getView() != null) {
            ImageView mImageView = (ImageView) getView().findViewById(R.id.image);
            if (this.data != null) {
                this.imageLoader.displayImage(this.data.getPicUrl(), mImageView,
                        ImageLoaderOptions.global(this.data.getDefaultImage()));
                if (TextUtils.isEmpty(this.data.getLink())) {
                    mImageView.setEnabled(false);
                    return;
                }
                mImageView.setEnabled(true);
                mImageView.setOnClickListener(new OnClickListener() {
                    public void onClick(View v) {
                        if (TextUtils.isEmpty(HomeImageFragment.this.data.getMobEvent())) {
                            MobclickAgent.onEvent(HomeImageFragment.this.getActivity(),
                                    HomeImageFragment.this.data.getMobEvent());
                        }
                        BooheeScheme.handleUrl(HomeImageFragment.this.getActivity(),
                                HomeImageFragment.this.data.getLink(), HomeImageFragment.this
                                        .data.getTitle());
                    }
                });
            }
        }
    }
}
