package com.meiqia.meiqiasdk.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MQImageCaptureManager {
    private static final String CAPTURED_PHOTO_PATH_KEY = "CAPTURED_PHOTO_PATH_KEY";
    private Context mContext;
    private String  mCurrentPhotoPath;
    private File    mImageDir;

    public MQImageCaptureManager(Context context, File imageDir) {
        this.mContext = context;
        this.mImageDir = imageDir;
    }

    private File createImageFile() throws IOException {
        File image = File.createTempFile("JPEG_" + new SimpleDateFormat("yyyyMMdd_HHmmss").format
                (new Date()) + "_", ".jpg", this.mImageDir);
        this.mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    public Intent getTakePictureIntent() throws IOException {
        Intent takePictureIntent = new Intent("android.media.action.IMAGE_CAPTURE");
        if (takePictureIntent.resolveActivity(this.mContext.getPackageManager()) != null) {
            File photoFile = createImageFile();
            if (photoFile != null) {
                takePictureIntent.putExtra("output", Uri.fromFile(photoFile));
            }
        }
        return takePictureIntent;
    }

    public void refreshGallery() {
        Intent mediaScanIntent = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE");
        mediaScanIntent.setData(Uri.fromFile(new File(this.mCurrentPhotoPath)));
        this.mContext.sendBroadcast(mediaScanIntent);
    }

    public String getCurrentPhotoPath() {
        return this.mCurrentPhotoPath;
    }

    public void onSaveInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState != null && this.mCurrentPhotoPath != null) {
            savedInstanceState.putString(CAPTURED_PHOTO_PATH_KEY, this.mCurrentPhotoPath);
        }
    }

    public void onRestoreInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState != null && savedInstanceState.containsKey(CAPTURED_PHOTO_PATH_KEY)) {
            this.mCurrentPhotoPath = savedInstanceState.getString(CAPTURED_PHOTO_PATH_KEY);
        }
    }
}
