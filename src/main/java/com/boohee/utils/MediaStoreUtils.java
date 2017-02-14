package com.boohee.utils;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.net.Uri;
import android.provider.MediaStore.Images.Media;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class MediaStoreUtils {
    static final String TAG = MediaStoreUtils.class.getSimpleName();

    public static String getRealPathFromUri(Context context, Uri uri) {
        if (uri != null) {
            Helper.showLog(TAG, uri.toString());
        }
        String realPath = null;
        Cursor cursor = null;
        try {
            cursor = context.getContentResolver().query(uri, new String[]{"_data"}, null, null,
                    null);
            if (cursor != null) {
                int column_index = cursor.getColumnIndexOrThrow("_data");
                cursor.moveToFirst();
                realPath = cursor.getString(column_index);
            }
            Helper.showLog(TAG + "real path", realPath);
            return realPath;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    public static Bitmap getBitmapFromUri(Context context, Uri uri, Options options) {
        Bitmap bitmap = null;
        InputStream is = null;
        try {
            is = context.getContentResolver().openInputStream(uri);
            bitmap = BitmapFactory.decodeStream(is, null, options);
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (FileNotFoundException e2) {
            e2.printStackTrace();
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e3) {
                    e3.printStackTrace();
                }
            }
        } catch (Throwable th) {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e32) {
                    e32.printStackTrace();
                }
            }
        }
        return bitmap;
    }

    public static String getLatestPath(Context context) {
        String imageLocation = null;
        Cursor cursor = null;
        try {
            cursor = context.getContentResolver().query(Media.EXTERNAL_CONTENT_URI, new
                    String[]{"_id", "_data", "bucket_display_name", "datetaken", "mime_type"},
                    null, null, "_id DESC");
            if (cursor != null && cursor.moveToFirst()) {
                imageLocation = cursor.getString(cursor.getColumnIndex("_data"));
            }
            if (cursor != null) {
                cursor.close();
            }
            return imageLocation;
        } catch (Throwable th) {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    public static Uri getCompressedUri(Context context, String imagePath) {
        if (context == null || imagePath == null) {
            return null;
        }
        Bitmap bmp = BitmapFactory.decodeFile(imagePath);
        try {
            String fileName = context.getExternalCacheDir().toString() + File.separatorChar +
                    System.currentTimeMillis() + ".png";
            BitmapUtil.saveBitmap(fileName, bmp);
            return Uri.fromFile(new File(fileName));
        } catch (Exception e) {
            return null;
        }
    }
}
