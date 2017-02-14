package com.boohee.one.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.boohee.model.Showcase;
import com.boohee.one.R;
import com.boohee.utility.Event;
import com.boohee.utility.ImageLoaderOptions;
import com.boohee.utils.ShopUtils;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.analytics.MobclickAgent;

public class ShopBannerFragment extends BaseFragment {
    protected ImageLoader imageLoader = ImageLoader.getInstance();
    private ImageView mImageView;
    public  Showcase  showcase;

    public void setShowcase(Showcase showcase) {
        this.showcase = showcase;
    }

    public static ShopBannerFragment newInstance(Showcase showcase) {
        ShopBannerFragment fragment = new ShopBannerFragment();
        fragment.showcase = showcase;
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
        if (this.showcase != null) {
            this.imageLoader.displayImage(this.showcase.default_photo_url, this.mImageView,
                    ImageLoaderOptions.global((int) R.color.ju));
            this.mImageView.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    MobclickAgent.onEvent(ShopBannerFragment.this.getActivity(), Event
                            .shop_clickBigBanner);
                    ShopUtils.handleExhibit(ShopBannerFragment.this.getActivity(),
                            ShopBannerFragment.this.showcase);
                }
            });
        }
    }
}
