package com.boohee.status;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.boohee.one.R;
import com.boohee.one.ui.BaseNoToolbarActivity;
import com.boohee.one.ui.fragment.PhotoImageFragment;

public class LargeImageActivity extends BaseNoToolbarActivity {
    static final String KEY_IMAGE_URL = "image_url";
    static final String TAG           = LargeImageActivity.class.getSimpleName();
    private String imageUrl;

    public static void start(Context context, String imageUrl) {
        Intent starter = new Intent(context, LargeImageActivity.class);
        starter.putExtra(KEY_IMAGE_URL, imageUrl);
        context.startActivity(starter);
    }

    public void onCreate(Bundle outState) {
        super.onCreate(outState);
        setContentView((int) R.layout.bu);
        this.imageUrl = getIntent().getStringExtra(KEY_IMAGE_URL);
        initUI();
    }

    private void initUI() {
        if (!TextUtils.isEmpty(this.imageUrl)) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    PhotoImageFragment.newInstance(this.imageUrl)).commitAllowingStateLoss();
        }
    }
}
