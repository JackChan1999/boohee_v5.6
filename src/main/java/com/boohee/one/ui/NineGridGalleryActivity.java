package com.boohee.one.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.SimpleOnPageChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;

import com.boohee.model.status.Photo;
import com.boohee.one.R;
import com.boohee.one.ui.adapter.PhotoImagePagerAdapter;
import com.boohee.status.FriendShipActivity;

import java.util.ArrayList;
import java.util.List;

public class NineGridGalleryActivity extends BaseNoToolbarActivity {
    public static final String PHOTO_LIST = "PHOTO_LIST";
    private PhotoImagePagerAdapter photoImagePagerAdapter;
    private List<Photo> photoList = new ArrayList();
    private int position;
    @InjectView(2131428020)
    RelativeLayout rlGallery;
    @InjectView(2131427774)
    TextView       tvIndex;
    @InjectView(2131427463)
    ViewPager      viewpager;

    public static void comeOn(Context context, List<Photo> photoList, int position) {
        if (context != null && photoList != null && photoList.size() != 0) {
            Intent intent = new Intent(context, NineGridGalleryActivity.class);
            intent.putParcelableArrayListExtra(PHOTO_LIST, (ArrayList) photoList);
            intent.putExtra(FriendShipActivity.FRIENDSHIP_POSITION, position);
            context.startActivity(intent);
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.dy);
        ButterKnife.inject((Activity) this);
        init();
    }

    private void init() {
        if (getIntent() != null) {
            this.photoList = getIntent().getParcelableArrayListExtra(PHOTO_LIST);
            this.position = getIntent().getIntExtra(FriendShipActivity.FRIENDSHIP_POSITION, 0);
        }
        if (this.photoList != null && this.photoList.size() != 0) {
            this.photoImagePagerAdapter = new PhotoImagePagerAdapter(getSupportFragmentManager(),
                    this.photoList);
            this.viewpager.setAdapter(this.photoImagePagerAdapter);
            this.viewpager.setOnPageChangeListener(new SimpleOnPageChangeListener() {
                public void onPageSelected(int position) {
                    super.onPageSelected(position);
                    NineGridGalleryActivity.this.setCurrentIndex(position);
                }
            });
            this.viewpager.setCurrentItem(this.position);
            setCurrentIndex(this.position);
        }
    }

    private void setCurrentIndex(int position) {
        this.tvIndex.setText(String.format(getResources().getString(R.string.adg), new
                Object[]{Integer.valueOf(position + 1), Integer.valueOf(this.photoList.size())}));
    }
}
