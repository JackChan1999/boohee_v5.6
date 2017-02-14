package com.boohee.myview;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import com.boohee.api.OneApi;
import com.boohee.database.OnePreference;
import com.boohee.database.UserPreference;
import com.boohee.model.BooheeAdvertisementBean;
import com.boohee.one.R;
import com.boohee.one.http.JsonCallback;
import com.boohee.one.ui.BrowserActivity;
import com.boohee.utility.ImageLoaderOptions;
import com.boohee.utils.DateHelper;
import com.boohee.utils.HttpUtils;
import com.boohee.widgets.Util.DensityUtil;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.Date;
import java.util.LinkedList;

import org.json.JSONObject;

public class BooheeAdvertisementBanner extends FrameLayout {
    static final String TAG                  = BooheeAdvertisementBanner.class.getSimpleName();
    private      int    advertismentPosition = 2;
    private Button      closeBtn;
    private ImageLoader imageLoader;
    private ImageView   imageView;

    public interface AdType {
        public static final int MINE_HOME_AD   = 2;
        public static final int STATUS_HOME_AD = 1;
    }

    private class BannerImageLoadingL implements ImageLoadingListener {
        private BooheeAdvertisementBean bean;

        public BannerImageLoadingL(BooheeAdvertisementBean bean) {
            this.bean = bean;
        }

        public void onLoadingCancelled(String arg0, View arg1) {
        }

        public void onLoadingComplete(String arg0, View arg1, Bitmap bitmap) {
            BooheeAdvertisementBanner.this.imageView.setImageBitmap(bitmap);
            BooheeAdvertisementBanner.this.imageView.setVisibility(0);
            BooheeAdvertisementBanner.this.imageView.setScaleType(ScaleType.FIT_XY);
            BooheeAdvertisementBanner.this.setVisibility(0);
            BooheeAdvertisementBanner.this.imageView.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.setClass(BooheeAdvertisementBanner.this.getContext(), BrowserActivity
                            .class);
                    intent.putExtra("url", BannerImageLoadingL.this.bean.link_url +
                            "?passport_token=" + UserPreference.getToken
                            (BooheeAdvertisementBanner.this.getContext()));
                    intent.putExtra("title", BannerImageLoadingL.this.bean.body);
                    BooheeAdvertisementBanner.this.getContext().startActivity(intent);
                }
            });
            BooheeAdvertisementBanner.this.closeBtn.setVisibility(0);
            BooheeAdvertisementBanner.this.closeBtn.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    BooheeAdvertisementBanner.this.removeAd();
                }
            });
        }

        public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {
            BooheeAdvertisementBanner.this.imageView.setVisibility(8);
        }

        public void onLoadingStarted(String arg0, View arg1) {
            BooheeAdvertisementBanner.this.imageView.setVisibility(8);
        }
    }

    public BooheeAdvertisementBanner(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public BooheeAdvertisementBanner(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BooheeAdvertisementBanner(Context context) {
        super(context);
        init();
    }

    private void init() {
        this.imageLoader = ImageLoader.getInstance();
        initView();
    }

    private void initView() {
        LayoutParams imageLP = new LayoutParams(-1, -1, 17);
        this.imageView = new ImageView(getContext());
        this.imageView.setVisibility(4);
        this.imageView.setScaleType(ScaleType.FIT_CENTER);
        addView(this.imageView, imageLP);
        LayoutParams closeLP = new LayoutParams(DensityUtil.dip2px(getContext(), 44.0f),
                DensityUtil.dip2px(getContext(), 44.0f));
        closeLP.gravity = 21;
        this.closeBtn = new Button(getContext());
        this.closeBtn.setBackgroundResource(R.drawable.l7);
        this.closeBtn.setVisibility(4);
        addView(this.closeBtn, closeLP);
    }

    public void startLoadAdvertisement(int position) {
        if (HttpUtils.isNetworkAvailable(getContext())) {
            String dateStr = OnePreference.getInstance(getContext()).getString("Ad" + position);
            if (!TextUtils.isEmpty(dateStr)) {
                Date adDate = DateHelper.parseString(dateStr);
                if (adDate == null || ((((System.currentTimeMillis() - adDate.getTime()) / 1000)
                        / 60) / 60) / 24 < 7) {
                    return;
                }
            }
            this.advertismentPosition = position;
            OneApi.getAdvertisement(getContext(), this.advertismentPosition + "", new
                    JsonCallback((Activity) getContext()) {
                public void ok(JSONObject object) {
                    super.ok(object);
                    LinkedList<BooheeAdvertisementBean> beans = BooheeAdvertisementBean.parseList
                            (object);
                    if (beans == null || beans.size() <= 0) {
                        BooheeAdvertisementBanner.this.imageView.setVisibility(8);
                        return;
                    }
                    BooheeAdvertisementBean bean = (BooheeAdvertisementBean) beans.get(0);
                    if (bean != null) {
                        BooheeAdvertisementBanner.this.imageLoader.displayImage(bean.photo_url,
                                BooheeAdvertisementBanner.this.imageView, ImageLoaderOptions
                                        .global(), new BannerImageLoadingL(bean));
                    }
                }
            });
        }
    }

    private void removeAd() {
        setVisibility(8);
        OnePreference.getInstance(getContext()).putString("Ad" + this.advertismentPosition, DateHelper.format(new Date()));
    }
}
