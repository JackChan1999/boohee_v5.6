package com.meiqia.meiqiasdk.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore.Images.Media;
import android.support.v4.view.ViewCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.meiqia.meiqiasdk.R;
import com.meiqia.meiqiasdk.model.ImageFolderModel;
import com.meiqia.meiqiasdk.pw.MQPhotoFolderPw;
import com.meiqia.meiqiasdk.pw.MQPhotoFolderPw.Callback;
import com.meiqia.meiqiasdk.util.MQConfig;
import com.meiqia.meiqiasdk.util.MQImageCaptureManager;
import com.meiqia.meiqiasdk.util.MQUtils;
import com.meiqia.meiqiasdk.widget.MQImageView;
import com.umeng.socialize.common.SocializeConstants;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

public class MQPhotoPickerActivity extends Activity implements OnClickListener,
        OnItemClickListener {
    private static final String EXTRA_IMAGE_DIR          = "EXTRA_IMAGE_DIR";
    private static final String EXTRA_MAX_CHOOSE_COUNT   = "EXTRA_MAX_CHOOSE_COUNT";
    private static final String EXTRA_SELECTED_IMAGES    = "EXTRA_SELECTED_IMAGES";
    private static final String EXTRA_TOP_RIGHT_BTN_TEXT = "EXTRA_TOP_RIGHT_BTN_TEXT";
    private static final int    REQUEST_CODE_PREVIEW     = 2;
    private static final int    REQUEST_CODE_TAKE_PHOTO  = 1;
    private ImageView                   mArrowIv;
    private GridView                    mContentGv;
    private ImageFolderModel            mCurrentImageFolderModel;
    private MQImageCaptureManager       mImageCaptureManager;
    private ArrayList<ImageFolderModel> mImageFolderModels;
    private long                        mLastShowPhotoFolderTime;
    private int mMaxChooseCount = 1;
    private MQPhotoFolderPw mPhotoFolderPw;
    private PicAdapter      mPicAdapter;
    private TextView        mSubmitTv;
    private boolean         mTakePhotoEnabled;
    private RelativeLayout  mTitleRl;
    private TextView        mTitleTv;
    private String          mTopRightBtnText;

    private class PicAdapter extends BaseAdapter {
        private ArrayList<String> mDatas = new ArrayList();
        private int mImageHeight;
        private int mImageWidth;
        private ArrayList<String> mSelectedImages = new ArrayList();

        public PicAdapter() {
            this.mImageWidth = MQUtils.getScreenWidth(MQPhotoPickerActivity.this
                    .getApplicationContext()) / 9;
            this.mImageHeight = this.mImageWidth;
        }

        public int getCount() {
            return this.mDatas.size();
        }

        public String getItem(int position) {
            return (String) this.mDatas.get(position);
        }

        public long getItemId(int position) {
            return 0;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            PicViewHolder picViewHolder;
            if (convertView == null) {
                convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout
                        .mq_item_square_image, parent, false);
                picViewHolder = new PicViewHolder();
                picViewHolder.photoIv = (MQImageView) convertView.findViewById(R.id.photo_iv);
                picViewHolder.tipTv = (TextView) convertView.findViewById(R.id.tip_tv);
                picViewHolder.flagIv = (ImageView) convertView.findViewById(R.id.flag_iv);
                convertView.setTag(picViewHolder);
            } else {
                picViewHolder = (PicViewHolder) convertView.getTag();
            }
            String imagePath = getItem(position);
            if (MQPhotoPickerActivity.this.mCurrentImageFolderModel.isTakePhotoEnabled() &&
                    position == 0) {
                picViewHolder.tipTv.setVisibility(0);
                picViewHolder.photoIv.setScaleType(ScaleType.CENTER);
                picViewHolder.photoIv.setImageResource(R.drawable.mq_ic_gallery_camera);
                picViewHolder.flagIv.setVisibility(4);
                picViewHolder.photoIv.setColorFilter(null);
            } else {
                picViewHolder.tipTv.setVisibility(4);
                picViewHolder.photoIv.setScaleType(ScaleType.CENTER_CROP);
                MQConfig.getImageLoader(MQPhotoPickerActivity.this).displayImage(picViewHolder
                        .photoIv, imagePath, R.drawable.mq_ic_holder_dark, R.drawable
                        .mq_ic_holder_dark, this.mImageWidth, this.mImageHeight, null);
                picViewHolder.flagIv.setVisibility(0);
                if (this.mSelectedImages.contains(imagePath)) {
                    picViewHolder.flagIv.setImageResource(R.drawable.mq_ic_cb_checked);
                    picViewHolder.photoIv.setColorFilter(MQPhotoPickerActivity.this.getResources
                            ().getColor(R.color.mq_photo_selected_color));
                } else {
                    picViewHolder.flagIv.setImageResource(R.drawable.mq_ic_cb_normal);
                    picViewHolder.photoIv.setColorFilter(null);
                }
                setFlagClickListener(picViewHolder.flagIv, position);
            }
            return convertView;
        }

        private void setFlagClickListener(ImageView flagIv, final int position) {
            flagIv.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    String image = PicAdapter.this.getItem(position);
                    if (PicAdapter.this.mSelectedImages.contains(image) || PicAdapter.this
                            .getSelectedCount() != MQPhotoPickerActivity.this.mMaxChooseCount) {
                        if (PicAdapter.this.mSelectedImages.contains(image)) {
                            PicAdapter.this.mSelectedImages.remove(image);
                        } else {
                            PicAdapter.this.mSelectedImages.add(image);
                        }
                        PicAdapter.this.notifyDataSetChanged();
                        MQPhotoPickerActivity.this.renderTopRightBtn();
                        return;
                    }
                    MQPhotoPickerActivity.this.toastMaxCountTip();
                }
            });
        }

        public void setDatas(ArrayList<String> datas) {
            if (datas != null) {
                this.mDatas = datas;
            } else {
                this.mDatas.clear();
            }
            notifyDataSetChanged();
        }

        public ArrayList<String> getDatas() {
            return this.mDatas;
        }

        public void setSelectedImages(ArrayList<String> selectedImages) {
            if (selectedImages != null) {
                this.mSelectedImages = selectedImages;
            }
            notifyDataSetChanged();
        }

        public ArrayList<String> getSelectedImages() {
            return this.mSelectedImages;
        }

        public int getSelectedCount() {
            return this.mSelectedImages.size();
        }
    }

    private class PicViewHolder {
        public ImageView   flagIv;
        public MQImageView photoIv;
        public TextView    tipTv;

        private PicViewHolder() {
        }
    }

    public static Intent newIntent(Context context, File imageDir, int maxChooseCount,
                                   ArrayList<String> selectedImages, String topRightBtnText) {
        Intent intent = new Intent(context, MQPhotoPickerActivity.class);
        intent.putExtra(EXTRA_IMAGE_DIR, imageDir);
        intent.putExtra(EXTRA_MAX_CHOOSE_COUNT, maxChooseCount);
        intent.putStringArrayListExtra(EXTRA_SELECTED_IMAGES, selectedImages);
        intent.putExtra(EXTRA_TOP_RIGHT_BTN_TEXT, topRightBtnText);
        return intent;
    }

    public static ArrayList<String> getSelectedImages(Intent intent) {
        return intent.getStringArrayListExtra(EXTRA_SELECTED_IMAGES);
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initListener();
        processLogic(savedInstanceState);
    }

    private void initView() {
        setContentView(R.layout.mq_activity_photo_picker);
        this.mTitleRl = (RelativeLayout) findViewById(R.id.title_rl);
        this.mTitleTv = (TextView) findViewById(R.id.title_tv);
        this.mArrowIv = (ImageView) findViewById(R.id.arrow_iv);
        this.mSubmitTv = (TextView) findViewById(R.id.submit_tv);
        this.mContentGv = (GridView) findViewById(R.id.content_gv);
    }

    private void initListener() {
        findViewById(R.id.back_iv).setOnClickListener(this);
        findViewById(R.id.folder_ll).setOnClickListener(this);
        this.mSubmitTv.setOnClickListener(this);
        this.mContentGv.setOnItemClickListener(this);
    }

    private void processLogic(Bundle savedInstanceState) {
        File imageDir = (File) getIntent().getSerializableExtra(EXTRA_IMAGE_DIR);
        if (imageDir != null) {
            this.mTakePhotoEnabled = true;
            this.mImageCaptureManager = new MQImageCaptureManager(this, imageDir);
        }
        this.mMaxChooseCount = getIntent().getIntExtra(EXTRA_MAX_CHOOSE_COUNT, 1);
        if (this.mMaxChooseCount < 1) {
            this.mMaxChooseCount = 1;
        }
        this.mTopRightBtnText = getIntent().getStringExtra(EXTRA_TOP_RIGHT_BTN_TEXT);
        this.mPicAdapter = new PicAdapter();
        this.mPicAdapter.setSelectedImages(getIntent().getStringArrayListExtra
                (EXTRA_SELECTED_IMAGES));
        this.mContentGv.setAdapter(this.mPicAdapter);
        renderTopRightBtn();
    }

    protected void onStart() {
        super.onStart();
        loadDatas();
    }

    public void onClick(View v) {
        if (v.getId() == R.id.back_iv) {
            onBackPressed();
        } else if (v.getId() == R.id.folder_ll && System.currentTimeMillis() - this
                .mLastShowPhotoFolderTime > 300) {
            showPhotoFolderPw();
            this.mLastShowPhotoFolderTime = System.currentTimeMillis();
        } else if (v.getId() == R.id.submit_tv) {
            returnSelectedImages(this.mPicAdapter.getSelectedImages());
        }
    }

    private void returnSelectedImages(ArrayList<String> selectedImages) {
        Intent intent = new Intent();
        intent.putStringArrayListExtra(EXTRA_SELECTED_IMAGES, selectedImages);
        setResult(-1, intent);
        finish();
    }

    private void showPhotoFolderPw() {
        if (this.mPhotoFolderPw == null) {
            this.mPhotoFolderPw = new MQPhotoFolderPw(this, this.mTitleRl, new Callback() {
                public void onSelectedFolder(int position) {
                    MQPhotoPickerActivity.this.reloadPhotos(position);
                }

                public void executeDismissAnim() {
                    ViewCompat.animate(MQPhotoPickerActivity.this.mArrowIv).setDuration(300)
                            .rotation(0.0f).start();
                }
            });
        }
        this.mPhotoFolderPw.setDatas(this.mImageFolderModels);
        this.mPhotoFolderPw.show();
        ViewCompat.animate(this.mArrowIv).setDuration(300).rotation(-180.0f).start();
    }

    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        if (!this.mCurrentImageFolderModel.isTakePhotoEnabled() || position != 0) {
            int currentPosition = position;
            if (this.mCurrentImageFolderModel.isTakePhotoEnabled()) {
                currentPosition--;
            }
            startActivityForResult(MQPhotoPickerPreviewActivity.newIntent(this, this
                    .mMaxChooseCount, this.mPicAdapter.getSelectedImages(), this.mPicAdapter
                    .getDatas(), currentPosition, this.mTopRightBtnText, false), 2);
        } else if (this.mPicAdapter.getSelectedCount() == this.mMaxChooseCount) {
            toastMaxCountTip();
        } else {
            takePhoto();
        }
    }

    private void toastMaxCountTip() {
        MQUtils.show((Context) this, getString(R.string.mq_toast_photo_picker_max, new
                Object[]{Integer.valueOf(this.mMaxChooseCount)}));
    }

    private void takePhoto() {
        try {
            startActivityForResult(this.mImageCaptureManager.getTakePictureIntent(), 1);
        } catch (Exception e) {
            MQUtils.show((Context) this, R.string.mq_photo_not_support);
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == -1) {
            if (requestCode == 1) {
                this.mImageCaptureManager.refreshGallery();
                String photoPath = this.mImageCaptureManager.getCurrentPhotoPath();
                this.mPicAdapter.getSelectedImages().add(photoPath);
                this.mPicAdapter.getDatas().add(0, photoPath);
                renderTopRightBtn();
                startActivityForResult(MQPhotoPickerPreviewActivity.newIntent(this, this
                        .mMaxChooseCount, this.mPicAdapter.getSelectedImages(), this.mPicAdapter
                        .getDatas(), 0, this.mTopRightBtnText, true), 2);
            } else if (requestCode == 2) {
                returnSelectedImages(MQPhotoPickerPreviewActivity.getSelectedImages(data));
            }
        } else if (resultCode == 0 && requestCode == 2) {
            this.mPicAdapter.setSelectedImages(MQPhotoPickerPreviewActivity.getSelectedImages
                    (data));
            renderTopRightBtn();
        }
    }

    private void renderTopRightBtn() {
        if (this.mPicAdapter.getSelectedCount() == 0) {
            this.mSubmitTv.setEnabled(false);
            this.mSubmitTv.setText(this.mTopRightBtnText);
            return;
        }
        this.mSubmitTv.setEnabled(true);
        this.mSubmitTv.setText(this.mTopRightBtnText + SocializeConstants.OP_OPEN_PAREN + this
                .mPicAdapter.getSelectedCount() + "/" + this.mMaxChooseCount + SocializeConstants
                .OP_CLOSE_PAREN);
    }

    public void onSaveInstanceState(Bundle outState) {
        if (this.mTakePhotoEnabled) {
            this.mImageCaptureManager.onSaveInstanceState(outState);
        }
        super.onSaveInstanceState(outState);
    }

    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        if (this.mTakePhotoEnabled) {
            this.mImageCaptureManager.onRestoreInstanceState(savedInstanceState);
        }
        super.onRestoreInstanceState(savedInstanceState);
    }

    private void loadDatas() {
        Cursor cursor = getContentResolver().query(Media.EXTERNAL_CONTENT_URI, new
                String[]{"_data"}, null, null, "date_added DESC");
        ImageFolderModel allImageFolderModel = new ImageFolderModel(this.mTakePhotoEnabled);
        HashMap<String, ImageFolderModel> imageFolderModelMap = new HashMap();
        if (cursor != null && cursor.getCount() > 0) {
            boolean firstInto = true;
            while (cursor.moveToNext()) {
                String imagePath = cursor.getString(cursor.getColumnIndex("_data"));
                if (!TextUtils.isEmpty(imagePath)) {
                    if (firstInto) {
                        allImageFolderModel.name = getString(R.string.mq_all_image);
                        allImageFolderModel.coverPath = imagePath;
                        firstInto = false;
                    }
                    allImageFolderModel.addLastImage(imagePath);
                    String folderPath = null;
                    File folder = new File(imagePath).getParentFile();
                    if (folder != null) {
                        folderPath = folder.getAbsolutePath();
                    }
                    if (TextUtils.isEmpty(folderPath)) {
                        int end = imagePath.lastIndexOf(File.separator);
                        if (end != -1) {
                            folderPath = imagePath.substring(0, end);
                        }
                    }
                    if (!TextUtils.isEmpty(folderPath)) {
                        ImageFolderModel otherImageFolderModel;
                        if (imageFolderModelMap.containsKey(folderPath)) {
                            otherImageFolderModel = (ImageFolderModel) imageFolderModelMap.get
                                    (folderPath);
                        } else {
                            String folderName = folderPath.substring(folderPath.lastIndexOf(File
                                    .separator) + 1);
                            if (TextUtils.isEmpty(folderName)) {
                                folderName = "/";
                            }
                            ImageFolderModel imageFolderModel = new ImageFolderModel(folderName,
                                    imagePath);
                            imageFolderModelMap.put(folderPath, imageFolderModel);
                        }
                        otherImageFolderModel.addLastImage(imagePath);
                    }
                }
            }
            cursor.close();
        }
        this.mImageFolderModels = new ArrayList();
        this.mImageFolderModels.add(allImageFolderModel);
        for (Entry value : imageFolderModelMap.entrySet()) {
            this.mImageFolderModels.add(value.getValue());
        }
        reloadPhotos(this.mPhotoFolderPw == null ? 0 : this.mPhotoFolderPw.getCurrentPosition());
    }

    private void reloadPhotos(int position) {
        this.mCurrentImageFolderModel = (ImageFolderModel) this.mImageFolderModels.get(position);
        this.mTitleTv.setText(this.mCurrentImageFolderModel.name);
        this.mPicAdapter.setDatas(this.mCurrentImageFolderModel.getImages());
    }
}
