package com.boohee.record;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.SimpleOnPageChangeListener;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import com.boohee.api.RecordApi;
import com.boohee.model.WeightPhoto;
import com.boohee.one.R;
import com.boohee.one.event.DeleteWeightPhoto;
import com.boohee.one.event.RefreshWeightEvent;
import com.boohee.one.http.JsonCallback;
import com.boohee.one.ui.BaseNoToolbarActivity;
import com.boohee.one.ui.fragment.PhotoBrowserFragment;
import com.boohee.utils.DateHelper;
import com.boohee.utils.FileUtil;
import com.boohee.utils.Helper;

import de.greenrobot.event.EventBus;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class NewWeightGalleryActivity extends BaseNoToolbarActivity {
    public static final String KEY_INDEX         = "key_index";
    public static final String KEY_WEIGHT_PHOTOS = "key_weight_photos";
    @InjectView(2131427548)
    ImageButton btDelete;
    @InjectView(2131428808)
    ImageButton btDownload;
    private ImagePagerAdapter mAdapter;
    private int               mCurrentIndex;
    private List<WeightPhoto> mPhotoList;
    @InjectView(2131427736)
    TextView  tvTime;
    @InjectView(2131427651)
    TextView  tvWeight;
    @InjectView(2131427463)
    ViewPager viewpager;

    private class ImagePagerAdapter extends FragmentStatePagerAdapter {
        private SparseArray<PhotoBrowserFragment> fragmentMap = new SparseArray();
        private List<WeightPhoto> mPhotoList;

        public ImagePagerAdapter(FragmentManager fm, List<WeightPhoto> photoList) {
            super(fm);
            this.mPhotoList = photoList;
        }

        public Fragment getItem(int position) {
            PhotoBrowserFragment fragment = PhotoBrowserFragment.newInstance(((WeightPhoto) this
                    .mPhotoList.get(position)).photo_url);
            this.fragmentMap.put(position, fragment);
            return fragment;
        }

        public void destroyItem(ViewGroup container, int position, Object object) {
            this.fragmentMap.delete(position);
            super.destroyItem(container, position, object);
        }

        public int getCount() {
            return this.mPhotoList.size();
        }

        public int getItemPosition(Object object) {
            return -2;
        }

        public PhotoBrowserFragment getFragment(int position) {
            return (PhotoBrowserFragment) this.fragmentMap.get(position);
        }
    }

    @OnClick({2131428808, 2131427548})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_delete:
                deletePhoto();
                return;
            case R.id.bt_downlaod:
                savePhoto();
                return;
            default:
                return;
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.lg);
        ButterKnife.inject((Activity) this);
        init();
    }

    private void init() {
        if (getIntent() == null) {
            finish();
            return;
        }
        this.mCurrentIndex = getIntent().getIntExtra(KEY_INDEX, 0);
        this.mPhotoList = getIntent().getParcelableArrayListExtra(KEY_WEIGHT_PHOTOS);
        if (this.mPhotoList == null || this.mPhotoList.size() == 0) {
            finish();
            return;
        }
        this.mAdapter = new ImagePagerAdapter(getSupportFragmentManager(), this.mPhotoList);
        this.viewpager.setAdapter(this.mAdapter);
        this.viewpager.addOnPageChangeListener(new SimpleOnPageChangeListener() {
            public void onPageSelected(int position) {
                NewWeightGalleryActivity.this.mCurrentIndex = position;
                NewWeightGalleryActivity.this.setCurrentItem();
            }
        });
        setCurrentItem();
    }

    private void setCurrentItem() {
        if (this.mPhotoList.size() == 0) {
            finish();
            return;
        }
        WeightPhoto photo = (WeightPhoto) this.mPhotoList.get(this.mCurrentIndex);
        if (photo != null) {
            this.viewpager.setCurrentItem(this.mCurrentIndex);
            this.tvWeight.setText(String.format("%s 公斤", new Object[]{String.valueOf(photo
                    .weight)}));
            this.tvTime.setText(DateHelper.formatString(photo.record_on, "yyyy年M月d日"));
        }
    }

    private void savePhoto() {
        if (this.mAdapter.getFragment(this.mCurrentIndex) == null) {
            Helper.showToast((CharSequence) "下载失败");
            return;
        }
        final Bitmap bmp = this.mAdapter.getFragment(this.mCurrentIndex).getBitmapImage();
        if (bmp == null) {
            Helper.showToast((CharSequence) "下载失败");
            return;
        }
        this.btDownload.setEnabled(false);
        new Thread(new Runnable() {
            public void run() {
                final File file = FileUtil.saveImage(NewWeightGalleryActivity.this.ctx, bmp,
                        String.valueOf(System.currentTimeMillis()));
                NewWeightGalleryActivity.this.runOnUiThread(new Runnable() {
                    public void run() {
                        if (file != null && file.getPath() != null) {
                            Helper.showToast("图片已保存到" + file.getPath());
                            NewWeightGalleryActivity.this.btDownload.setEnabled(true);
                        }
                    }
                });
            }
        }).start();
    }

    private void deletePhoto() {
        WeightPhoto photo = (WeightPhoto) this.mPhotoList.get(this.mCurrentIndex);
        JsonCallback callback = new JsonCallback(this) {
            public void ok(String response) {
                EventBus.getDefault().post(new DeleteWeightPhoto());
                EventBus.getDefault().post(new RefreshWeightEvent());
                NewWeightGalleryActivity.this.mPhotoList.remove(NewWeightGalleryActivity.this
                        .mCurrentIndex);
                if (NewWeightGalleryActivity.this.mPhotoList.size() == 0) {
                    NewWeightGalleryActivity.this.setResult(-1);
                    NewWeightGalleryActivity.this.finish();
                    return;
                }
                if (NewWeightGalleryActivity.this.mCurrentIndex > 0 && NewWeightGalleryActivity
                        .this.mCurrentIndex == NewWeightGalleryActivity.this.mPhotoList.size() -
                        1) {
                    NewWeightGalleryActivity.this.mCurrentIndex = NewWeightGalleryActivity.this
                            .mCurrentIndex - 1;
                }
                NewWeightGalleryActivity.this.mAdapter.notifyDataSetChanged();
                NewWeightGalleryActivity.this.setCurrentItem();
            }
        };
        if (photo.id != -1) {
            RecordApi.deleteWeightPhoto((Context) this, photo.id, callback);
        } else {
            RecordApi.deleteWeightPhoto((Context) this, photo.record_on, callback);
        }
    }

    public static void comeOn(Context context, List<WeightPhoto> photos, int currentIndex) {
        if (context != null) {
            Intent intent = new Intent(context, NewWeightGalleryActivity.class);
            intent.putParcelableArrayListExtra(KEY_WEIGHT_PHOTOS, (ArrayList) photos);
            intent.putExtra(KEY_INDEX, currentIndex);
            context.startActivity(intent);
        }
    }
}
