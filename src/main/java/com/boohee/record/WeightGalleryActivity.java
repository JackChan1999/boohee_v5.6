package com.boohee.record;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.SimpleOnPageChangeListener;
import android.text.TextUtils;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;

import com.boohee.database.UserPreference;
import com.boohee.model.mine.WeightPhoto;
import com.boohee.model.mine.WeightPreviewPhoto;
import com.boohee.model.mine.WeightRecord;
import com.boohee.modeldao.UserDao;
import com.boohee.modeldao.WeightRecordDao;
import com.boohee.one.R;
import com.boohee.one.http.JsonCallback;
import com.boohee.one.http.client.BooheeClient;
import com.boohee.one.ui.BaseNoToolbarActivity;
import com.boohee.one.ui.adapter.WeightImagePagerAdapter;
import com.boohee.utils.FastJsonUtils;

import java.util.List;

import org.json.JSONObject;

public class WeightGalleryActivity extends BaseNoToolbarActivity {
    public static final String KEY_DATE          = "key_date";
    public static final String KEY_WEIGHT_RECORD = "key_weight_record";
    public static final String PHOTOS            =
            "/api/v2/photos?date_position=%s&size=%s&token=%s";
    private String mDate;
    private String mDatePosition;
    private float mHeight = 160.0f;
    private List<WeightPreviewPhoto> mPreviewPhotos;
    private WeightImagePagerAdapter  mWeightImagePagerAdapter;
    private WeightRecord             mWeightRecord;
    @InjectView(2131428020)
    RelativeLayout rl_gallery;
    @InjectView(2131427774)
    TextView       tv_index;
    @InjectView(2131427463)
    ViewPager      viewpager;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.dy);
        ButterKnife.inject((Activity) this);
        handleIntent();
        requestPhotos();
        init();
    }

    private void handleIntent() {
        this.mWeightRecord = (WeightRecord) getIntent().getSerializableExtra(KEY_WEIGHT_RECORD);
        this.mDate = getIntent().getStringExtra(KEY_DATE);
        this.mHeight = new UserDao(this).queryWithToken(UserPreference.getToken(this)).height();
    }

    public void requestPhotos() {
        if (this.mWeightRecord != null) {
            List<WeightPhoto> photos = this.mWeightRecord.photos;
            this.mDate = this.mWeightRecord.record_on;
            if (photos != null && photos.size() > 0) {
                this.mDatePosition = ((WeightPhoto) photos.get(0)).date_position;
            }
            if (!TextUtils.isEmpty(this.mDatePosition)) {
                BooheeClient.build("record").get(String.format(PHOTOS, new Object[]{this
                        .mDatePosition, "xxl", UserPreference.getToken(this)}), new JsonCallback
                        (this) {
                    public void ok(JSONObject object) {
                        super.ok(object);
                        WeightGalleryActivity.this.mPreviewPhotos = FastJsonUtils.parseList
                                (object.optString(WeightRecordDao.PHOTOS), WeightPreviewPhoto
                                        .class);
                        WeightGalleryActivity.this.init();
                    }
                }, this);
            }
        }
    }

    private void init() {
        if (this.mPreviewPhotos != null && this.mPreviewPhotos.size() != 0) {
            int size = this.mPreviewPhotos.size();
            this.mWeightImagePagerAdapter = new WeightImagePagerAdapter(getSupportFragmentManager
                    (), this.mPreviewPhotos, this.mHeight);
            this.viewpager.setAdapter(this.mWeightImagePagerAdapter);
            this.viewpager.setOnPageChangeListener(new SimpleOnPageChangeListener() {
                public void onPageSelected(int position) {
                    super.onPageSelected(position);
                    WeightGalleryActivity.this.tv_index.setText(String.format
                            (WeightGalleryActivity.this.getResources().getString(R.string.adg),
                                    new Object[]{Integer.valueOf(position + 1), Integer.valueOf
                                            (WeightGalleryActivity.this.mPreviewPhotos.size())}));
                }
            });
            setCurrentItem(size);
        }
    }

    private void startAnim() {
        ObjectAnimator alpha = ObjectAnimator.ofFloat(this.rl_gallery, "alpha", new float[]{0.0f,
                1.0f});
        alpha.setDuration(500);
        alpha.start();
    }

    private void setCurrentItem(int size) {
        int index = 0;
        if (!TextUtils.isEmpty(this.mDate)) {
            for (int i = 0; i < size; i++) {
                if (this.mDate.equals(((WeightPreviewPhoto) this.mPreviewPhotos.get(i))
                        .record_on)) {
                    index = i;
                    break;
                }
            }
        }
        this.viewpager.setCurrentItem(index);
        this.tv_index.setText(String.format(getString(R.string.adg), new Object[]{Integer.valueOf
                (index + 1), Integer.valueOf(size)}));
        startAnim();
    }
}
