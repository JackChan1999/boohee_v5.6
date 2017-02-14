package com.boohee.one.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import butterknife.ButterKnife;
import butterknife.InjectView;

import com.boohee.one.R;
import com.boohee.utility.ImageLoaderOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;
import uk.co.senab.photoview.PhotoViewAttacher.OnPhotoTapListener;

public class DushouExampleActivity extends BaseNoToolbarActivity {
    private PhotoViewAttacher mAttacher;
    @InjectView(2131427433)
    PhotoView mIvPhoto;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.b8);
        ButterKnife.inject((Activity) this);
        this.mAttacher = new PhotoViewAttacher(this.mIvPhoto);
        this.mAttacher.setOnPhotoTapListener(new OnPhotoTapListener() {
            public void onPhotoTap(View view, float v, float v2) {
                DushouExampleActivity.this.finish();
            }
        });
        ImageLoader.getInstance().displayImage("", this.mIvPhoto, ImageLoaderOptions.global((int)
                R.drawable.rs));
    }
}
