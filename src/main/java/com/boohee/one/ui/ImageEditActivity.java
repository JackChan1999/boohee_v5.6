package com.boohee.one.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.boohee.database.UserPreference;
import com.boohee.one.R;
import com.boohee.one.http.client.BooheeClient;
import com.boohee.uploader.utils.BitmapUtils;
import com.boohee.utils.BitmapUtil;
import com.boohee.utils.FileUtil;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class ImageEditActivity extends BaseActivity implements OnClickListener {
    private static final String        FILTER_HELP_URL = "/api/v1/articles/filter_help.html";
    private              AtomicBoolean isLoading       = new AtomicBoolean(false);
    private              List<String>  mSyncDatas      = new ArrayList();
    private              List<String>  mSyncTags       = new ArrayList();
    private Uri mUri;
    private ArrayList<Uri> mUriList = new ArrayList();
    private int            position = 0;
    private UserPreference preference;
    private ImageView      previewImage;
    private TextView       tab1;
    private TextView       tab2;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bq);
        setTitle("图像编辑");
        findView();
        addListener();
        init();
    }

    private void findView() {
        this.previewImage = (ImageView) findViewById(R.id.editImageView);
        this.tab1 = (TextView) findViewById(R.id.editImageTab1);
        this.tab2 = (TextView) findViewById(R.id.editImageTab2);
    }

    private void addListener() {
        this.tab1.setOnClickListener(this);
        this.tab2.setOnClickListener(this);
        findViewById(R.id.editImageTab3).setOnClickListener(this);
        findViewById(R.id.editImageTab4).setOnClickListener(this);
        findViewById(R.id.editImageTab5).setOnClickListener(this);
    }

    private void init() {
        this.preference = UserPreference.getInstance(this);
        showLoading();
        this.isLoading.set(true);
        this.mUri = getIntent().getData();
        if (this.mUri != null) {
            this.mUriList.add(this.mUri);
        }
        this.mSyncDatas.add("");
        this.mSyncTags.add("");
        updateBackorPreview();
        dismissLoading();
        this.isLoading.set(false);
        initPreviewImage(this.mUri);
    }

    private void initPreviewImage(Uri mUri) {
        Bitmap bmp = BitmapUtil.getResizedBitmapWithUri(this.ctx, mUri);
        WeakReference<Bitmap> weakbmp = BitmapUtils.autoRotateBitmap(mUri.getPath(), new
                WeakReference(bmp));
        if (weakbmp != null) {
            if (!(bmp == null || bmp == weakbmp.get())) {
                bmp.recycle();
            }
            this.previewImage.setImageBitmap((Bitmap) weakbmp.get());
        } else if (bmp != null) {
            bmp.recycle();
        }
    }

    protected void onResume() {
        super.onResume();
        updateBackorPreview();
    }

    public void onBackPressed() {
        removeTempFilterRecord();
        super.onBackPressed();
    }

    public void onClick(View arg0) {
        if (!this.isLoading.get()) {
            ArrayList arrayList;
            int i;
            Intent intent;
            switch (arg0.getId()) {
                case R.id.editImageTab1:
                    showLoading();
                    arrayList = this.mUriList;
                    i = this.position - 1;
                    this.position = i;
                    this.mUri = (Uri) arrayList.get(i);
                    initPreviewImage(this.mUri);
                    updateBackorPreview();
                    dismissLoading();
                    return;
                case R.id.editImageTab2:
                    showLoading();
                    arrayList = this.mUriList;
                    i = this.position + 1;
                    this.position = i;
                    this.mUri = (Uri) arrayList.get(i);
                    initPreviewImage(this.mUri);
                    updateBackorPreview();
                    dismissLoading();
                    return;
                case R.id.editImageTab3:
                    intent = new Intent(this, ImageFilterActivity.class);
                    intent.setData(this.mUri);
                    startActivityForResult(intent, 0);
                    return;
                case R.id.editImageTab4:
                    intent = new Intent(this, ImageChartletActivity.class);
                    intent.setData(this.mUri);
                    startActivityForResult(intent, 0);
                    return;
                case R.id.editImageTab5:
                    intent = new Intent(this, BrowserActivity.class);
                    intent.putExtra("url", BooheeClient.build(BooheeClient.ONE).getDefaultURL
                            (FILTER_HELP_URL));
                    intent.putExtra("title", "帮助");
                    startActivity(intent);
                    return;
                default:
                    return;
            }
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == -1 && data != null && this.mUriList != null) {
            this.mUri = data.getData();
            this.mUriList.add(this.mUri);
            this.mSyncDatas.add(this.preference.getString(ImageFilterActivity.KEY_POST_DATA));
            this.mSyncTags.add(this.preference.getString(ImageFilterActivity.KEY_POST_TAG));
            initPreviewImage(this.mUri);
        }
    }

    private void updateBackorPreview() {
        this.position = this.mUriList.indexOf(this.mUri);
        if ((this.position == 0 && this.mUriList.size() == 1) || this.mUriList.size() == 0) {
            this.tab1.setEnabled(false);
            this.tab2.setEnabled(false);
        } else if (this.position == this.mUriList.size() - 1) {
            this.tab1.setEnabled(true);
            this.tab2.setEnabled(false);
        } else if (this.position < this.mUriList.size() - 1) {
            this.tab2.setEnabled(true);
            this.tab1.setEnabled(false);
        } else {
            this.tab1.setEnabled(true);
            this.tab2.setEnabled(true);
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, 1, 1, R.string.y8).setShowAsAction(2);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == 1) {
            postAction();
            return true;
        } else if (item.getItemId() != 16908332) {
            return super.onOptionsItemSelected(item);
        } else {
            removeTempFilterRecord();
            finish();
            return true;
        }
    }

    private void removeTempFilterRecord() {
        this.preference.remove(ImageFilterActivity.KEY_POST_TAG);
        this.preference.remove(ImageFilterActivity.KEY_POST_DATA);
    }

    public void postAction() {
        if (!this.isLoading.get()) {
            if (this.mUriList != null) {
                Iterator it = this.mUriList.iterator();
                while (it.hasNext()) {
                    Uri uri = (Uri) it.next();
                    if (!(uri == this.mUri || uri == this.mUriList.get(0))) {
                        FileUtil.delFile(uri.getPath());
                    }
                }
                this.mUriList.clear();
            }
            if (this.position != -1) {
                this.preference.putString(ImageFilterActivity.KEY_POST_DATA, (String) this
                        .mSyncDatas.get(this.position));
                this.preference.putString(ImageFilterActivity.KEY_POST_TAG, (String) this
                        .mSyncTags.get(this.position));
            }
            setResult(-1, new Intent(this, StatusPostTextActivity.class).setData(this.mUri));
            finish();
        }
    }
}
