package me.nereo.multi_image_selector;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager.SimpleOnPageChangeListener;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.umeng.socialize.common.SocializeConstants;
import java.util.ArrayList;
import java.util.List;
import me.nereo.multi_image_selector.adapter.MultiImagePreviewAdapter;
import me.nereo.multi_image_selector.bean.Image;
import me.nereo.multi_image_selector.view.ViewPagerFixed;

public class MultiImagePreviewActivity extends FragmentActivity {
    private static final String KEY_SELECT_POSITION = "key_select_position";
    public static final int PREVIEW_REQ_CODE = 1;
    static List<Image> mImageList;
    static int mMaxImageNum;
    static ArrayList<String> mSelectList;
    int currentPosition = 0;
    ImageView mBtnBack;
    LinearLayout mCheckArea;
    ImageView mCheckmarkImage;
    MultiImagePreviewAdapter mPagerAdapter;
    Button mSubmitButton;
    TextView mTvBack;
    ViewPagerFixed mViewPager;
    TextView tvPhotoPosition;

    public static void startMe(Activity activity, List<Image> imageList, ArrayList<String> selectList, int maxImageNum, int position) {
        if (activity != null && imageList != null && imageList.size() != 0) {
            if (selectList == null) {
                selectList = new ArrayList();
            }
            if (position < 0) {
                position = 0;
            }
            if (maxImageNum <= 1) {
                maxImageNum = 1;
            }
            mMaxImageNum = maxImageNum;
            mSelectList = selectList;
            mImageList = imageList;
            Intent intent = new Intent(activity, MultiImagePreviewActivity.class);
            intent.putExtra(KEY_SELECT_POSITION, position);
            activity.startActivityForResult(intent, 1);
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multi_image_preview);
        findView();
        addListener();
        init();
    }

    private void findView() {
        this.tvPhotoPosition = (TextView) findViewById(R.id.tv_photo_position);
        this.mViewPager = (ViewPagerFixed) findViewById(R.id.vp_multi_image);
        this.mSubmitButton = (Button) findViewById(R.id.commit);
        this.mCheckArea = (LinearLayout) findViewById(R.id.ll_select);
        this.mCheckmarkImage = (ImageView) findViewById(R.id.checkmark);
        this.mTvBack = (TextView) findViewById(R.id.tv_back);
        this.mTvBack.setText(R.string.preview);
        this.mBtnBack = (ImageView) findViewById(R.id.btn_back);
    }

    private void addListener() {
        this.mCheckArea.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (MultiImagePreviewActivity.mImageList != null && MultiImagePreviewActivity.this.currentPosition >= 0 && MultiImagePreviewActivity.this.currentPosition < MultiImagePreviewActivity.mImageList.size()) {
                    Image image = (Image) MultiImagePreviewActivity.mImageList.get(MultiImagePreviewActivity.this.currentPosition);
                    if (image == null || image != MultiImagePreviewActivity.this.mCheckmarkImage.getTag()) {
                        if (MultiImagePreviewActivity.this.onSelect(image)) {
                            MultiImagePreviewActivity.this.mCheckmarkImage.setImageResource(R.drawable.btn_selected);
                            MultiImagePreviewActivity.this.mCheckmarkImage.setTag(image);
                        }
                    } else if (MultiImagePreviewActivity.this.onUnSelect(image)) {
                        MultiImagePreviewActivity.this.mCheckmarkImage.setImageResource(R.drawable.btn_unselected);
                        MultiImagePreviewActivity.this.mCheckmarkImage.setTag(null);
                    }
                    MultiImagePreviewActivity.this.refreshSubmitBtn();
                }
            }
        });
        this.mViewPager.setOnPageChangeListener(new SimpleOnPageChangeListener() {
            public void onPageSelected(int position) {
                MultiImagePreviewActivity.this.currentPosition = position;
                MultiImagePreviewActivity.this.tvPhotoPosition.setText((position + 1) + "/" + MultiImagePreviewActivity.this.mPagerAdapter.getCount());
                MultiImagePreviewActivity.this.refreshCheckmarkImage();
            }
        });
        this.mBtnBack.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                MultiImagePreviewActivity.this.finish();
            }
        });
        this.mSubmitButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (MultiImagePreviewActivity.mSelectList != null && MultiImagePreviewActivity.mSelectList.size() > 0) {
                    Intent data = new Intent();
                    data.putStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT, MultiImagePreviewActivity.mSelectList);
                    MultiImagePreviewActivity.this.setResult(-1, data);
                    MultiImagePreviewActivity.this.finish();
                }
            }
        });
    }

    private void init() {
        Intent intent = getIntent();
        if (intent != null) {
            this.currentPosition = intent.getIntExtra(KEY_SELECT_POSITION, this.currentPosition);
            this.mPagerAdapter = new MultiImagePreviewAdapter(getSupportFragmentManager(), mImageList);
            this.mViewPager.setAdapter(this.mPagerAdapter);
            this.mViewPager.setCurrentItem(this.currentPosition);
            refreshSubmitBtn();
            refreshCheckmarkImage();
            this.tvPhotoPosition.setText((this.currentPosition + 1) + "/" + this.mPagerAdapter.getCount());
        }
    }

    private void refreshSubmitBtn() {
        if (mSelectList.size() == 0) {
            this.mSubmitButton.setText(R.string.ok);
            this.mSubmitButton.setEnabled(false);
            return;
        }
        this.mSubmitButton.setEnabled(true);
        this.mSubmitButton.setText(getResources().getString(R.string.ok) + SocializeConstants.OP_OPEN_PAREN + mSelectList.size() + "/" + mMaxImageNum + SocializeConstants.OP_CLOSE_PAREN);
    }

    private void refreshCheckmarkImage() {
        if (mImageList != null && this.currentPosition >= 0 && this.currentPosition < mImageList.size()) {
            Image image = (Image) mImageList.get(this.currentPosition);
            if (image == null || !mSelectList.contains(image.path)) {
                this.mCheckmarkImage.setImageResource(R.drawable.btn_unselected);
                this.mCheckmarkImage.setTag(null);
                return;
            }
            this.mCheckmarkImage.setImageResource(R.drawable.btn_selected);
            this.mCheckmarkImage.setTag(image);
        }
    }

    private boolean onSelect(Image image) {
        if (mSelectList.size() == mMaxImageNum) {
            Toast.makeText(this, R.string.msg_amount_limit, 0).show();
            return false;
        } else if (image == null || TextUtils.isEmpty(image.path) || !mSelectList.add(image.path)) {
            return false;
        } else {
            return true;
        }
    }

    private boolean onUnSelect(Image image) {
        return (image == null || TextUtils.isEmpty(image.path) || !mSelectList.remove(image.path)) ? false : true;
    }
}
