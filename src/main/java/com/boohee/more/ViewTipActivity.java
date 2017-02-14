package com.boohee.more;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Vibrator;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.boohee.modeldao.NoticeDao;
import com.boohee.one.R;
import com.boohee.one.ui.BaseNoToolbarActivity;
import com.boohee.utility.Const;
import com.boohee.utility.ImageLoaderOptions;
import com.boohee.utils.FileUtil;
import com.boohee.utils.Helper;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.umeng.analytics.MobclickAgent;

import java.util.Random;

public class ViewTipActivity extends BaseNoToolbarActivity implements OnClickListener {
    private static final int[]  COLORS            = new int[]{R.color.j9, R.color.j_, R.color.ja,
            R.color.jb, R.color.jc};
    public static final  String FROM_NOTIFICATION = "from_notification";
    public static final  String IMAGE_PREFIX      = "http://up.boohee.cn/house/u/one/reminder/%1d" +
            ".jpg";
    static final         String TAG               = ViewTipActivity.class.getName();
    private ImageView backImage;
    private ImageView contentImage;
    private TextView  contentText;
    private TextView  copyText;
    private String    imageUrl;
    private String    message;
    private int       noticeId;
    private TextView  saveText;

    private class SaveImageTask extends AsyncTask<Bitmap, String, String> {
        private SaveImageTask() {
        }

        protected String doInBackground(Bitmap... params) {
            if (params[0] == null || TextUtils.isEmpty(ViewTipActivity.this.imageUrl)) {
                return null;
            }
            return FileUtil.downloadImage2Gallery(ViewTipActivity.this.ctx, params[0], new
                    Md5FileNameGenerator().generate(ViewTipActivity.this.imageUrl));
        }

        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            ViewTipActivity.this.saveText.setEnabled(true);
            if (TextUtils.isEmpty(result)) {
                Helper.showToast((CharSequence) "保存图片失败，请重新保存~~");
            } else {
                Helper.showToast("图片已保存到" + result);
            }
        }
    }

    public void onCreate(Bundle outState) {
        super.onCreate(outState);
        setContentView((int) R.layout.r2);
        setTitle(R.string.aco);
        handleIntent();
        findViews();
        setTipText();
    }

    private void findViews() {
        this.backImage = (ImageView) findViewById(R.id.iv_back);
        this.backImage.setOnClickListener(this);
        this.saveText = (TextView) findViewById(R.id.tv_save);
        this.saveText.setOnClickListener(this);
        this.copyText = (TextView) findViewById(R.id.tv_copy);
        this.copyText.setOnClickListener(this);
        this.contentText = (TextView) findViewById(R.id.tv_content);
        this.contentImage = (ImageView) findViewById(R.id.iv_content);
        this.contentImage.setOnClickListener(this);
        this.imageUrl = String.format(IMAGE_PREFIX, new Object[]{Integer.valueOf(this.noticeId)});
        this.imageLoader.displayImage(this.imageUrl, this.contentImage, ImageLoaderOptions.color
                (R.color.ju), new SimpleImageLoadingListener() {
            public void onLoadingComplete(String imageUri, View view, final Bitmap loadedImage) {
                ViewTipActivity.this.saveText.setVisibility(0);
                ViewTipActivity.this.contentText.setVisibility(8);
                ViewTipActivity.this.saveText.setOnClickListener(new OnClickListener() {
                    public void onClick(View v) {
                        new SaveImageTask().execute(new Bitmap[]{loadedImage});
                    }
                });
            }

            public void onLoadingFailed(String imageUri, View view, FailReason reason) {
                ViewTipActivity.this.saveText.setVisibility(4);
                ViewTipActivity.this.contentText.setVisibility(0);
                ViewTipActivity.this.contentImage.setVisibility(8);
                ViewTipActivity.this.contentText.setText(ViewTipActivity.this.message);
                ViewTipActivity.this.contentText.setBackgroundColor(ViewTipActivity.this
                        .getResources().getColor(ViewTipActivity.COLORS[new Random().nextInt
                                (ViewTipActivity.COLORS.length)]));
            }
        });
    }

    private void handleIntent() {
        Intent intent = getIntent();
        this.noticeId = intent.getIntExtra(Const.NOTICE_ID, 0);
        this.message = intent.getStringExtra(Const.NOTICE_MESSAGE);
        Helper.showLog(TAG, this.message);
        if (intent.getBooleanExtra(FROM_NOTIFICATION, false)) {
            MobclickAgent.onEvent(this.ctx, "other_clickRemind");
        }
    }

    private void setTipText() {
        if (this.noticeId > 0) {
            NoticeDao noticeDao = new NoticeDao(this.ctx);
            noticeDao.updateIsOpened(noticeDao.selectWithId(this.noticeId));
            noticeDao.closeDB();
        }
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_copy:
                copyText();
                return;
            case R.id.iv_content:
                onBackPressed();
                return;
            case R.id.iv_back:
                onBackPressed();
                return;
            default:
                return;
        }
    }

    private void copyText() {
        try {
            ((Vibrator) getSystemService("vibrator")).vibrate(100);
            ((ClipboardManager) getSystemService("clipboard")).setPrimaryClip(ClipData
                    .newPlainText(this.message, this.message));
            Helper.showToast((CharSequence) "已复制到粘贴板");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
