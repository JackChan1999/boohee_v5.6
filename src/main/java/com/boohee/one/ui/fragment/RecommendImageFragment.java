package com.boohee.one.ui.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.boohee.model.Recommend;
import com.boohee.one.R;
import com.boohee.utility.BooheeScheme;
import com.boohee.utility.ImageLoaderOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.analytics.MobclickAgent;

public class RecommendImageFragment extends BaseFragment {
    protected ImageLoader imageLoader = ImageLoader.getInstance();
    private ImageView mImageView;
    public  Recommend mRecommend;

    public void setmRecommend(Recommend recommend) {
        this.mRecommend = recommend;
    }

    public static RecommendImageFragment newInstance(Recommend recommend) {
        RecommendImageFragment fragment = new RecommendImageFragment();
        fragment.mRecommend = recommend;
        return fragment;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        return inflater.inflate(R.layout.g0, null);
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initUI();
    }

    private void initUI() {
        this.mImageView = (ImageView) getView().findViewById(R.id.image);
        if (this.mRecommend != null) {
            this.imageLoader.displayImage(this.mRecommend.pic_url, this.mImageView,
                    ImageLoaderOptions.global((int) R.color.ju));
            this.mImageView.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    MobclickAgent.onEvent(RecommendImageFragment.this.getActivity(),
                            "home_clickRecommendAd");
                    if (!TextUtils.isEmpty(RecommendImageFragment.this.mRecommend.page_url)) {
                        BooheeScheme.handleUrl(RecommendImageFragment.this.getActivity(),
                                RecommendImageFragment.this.mRecommend.page_url,
                                RecommendImageFragment.this.mRecommend.title);
                    }
                }
            });
        }
    }
}
