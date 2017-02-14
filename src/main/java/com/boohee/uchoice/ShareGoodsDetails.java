package com.boohee.uchoice;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import boohee.lib.share.ShareManager;

import com.boohee.one.ui.BaseActivity;
import com.boohee.utils.FileUtil;
import com.boohee.utils.Helper;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class ShareGoodsDetails extends AsyncTask<String, Integer, Bitmap> {
    static final String TAG = ShareGoodsDetails.class.getSimpleName();
    private Context ctx;
    private String  mShareLink;
    private String  photoUrl;
    private String  shareText;
    private int     where;

    public ShareGoodsDetails(Context ctx, int where, String shareText) {
        this.ctx = ctx;
        this.where = where;
        this.shareText = shareText;
    }

    protected void onPreExecute() {
        Helper.showLog(TAG, "onPreExecute() enter");
    }

    protected Bitmap doInBackground(String... params) {
        MalformedURLException e;
        HttpURLConnection urlConn;
        InputStream is;
        Helper.showLog(TAG, "doInBackground(String... params) enter");
        URL imageUrl = null;
        Bitmap mDownLoadBtBitmap = null;
        publishProgress(new Integer[]{Integer.valueOf(0)});
        try {
            this.photoUrl = params[0];
            URL imageUrl2 = new URL(params[0]);
            try {
                if (this.where == 1) {
                    this.mShareLink = params[1];
                }
                imageUrl = imageUrl2;
            } catch (MalformedURLException e2) {
                e = e2;
                imageUrl = imageUrl2;
                e.printStackTrace();
                Log.e(TAG, e.getMessage());
                urlConn = (HttpURLConnection) imageUrl.openConnection();
                urlConn.setDoInput(true);
                urlConn.connect();
                is = urlConn.getInputStream();
                mDownLoadBtBitmap = BitmapFactory.decodeStream(is);
                is.close();
                return mDownLoadBtBitmap;
            }
        } catch (MalformedURLException e3) {
            e = e3;
            e.printStackTrace();
            Log.e(TAG, e.getMessage());
            urlConn = (HttpURLConnection) imageUrl.openConnection();
            urlConn.setDoInput(true);
            urlConn.connect();
            is = urlConn.getInputStream();
            mDownLoadBtBitmap = BitmapFactory.decodeStream(is);
            is.close();
            return mDownLoadBtBitmap;
        }
        try {
            urlConn = (HttpURLConnection) imageUrl.openConnection();
            urlConn.setDoInput(true);
            urlConn.connect();
            is = urlConn.getInputStream();
            mDownLoadBtBitmap = BitmapFactory.decodeStream(is);
            is.close();
            return mDownLoadBtBitmap;
        } catch (IOException e4) {
            Log.e(TAG, e4.getMessage());
            return mDownLoadBtBitmap;
        }
    }

    protected void onProgressUpdate(Integer... progresses) {
        ((BaseActivity) this.ctx).showLoading();
        Helper.showLog(TAG, "onProgressUpdate(Integer... progresses) enter" + progresses[0]);
    }

    protected void onPostExecute(Bitmap result) {
        ((BaseActivity) this.ctx).dismissLoading();
        Helper.showLog(TAG, "onPostExecute(Result result) called" + result);
        if (result != null) {
            FileUtil.saveTempFile(result);
        }
        if (this.where == 1) {
            ShareManager.share(this.ctx, "分享", this.shareText + this.mShareLink, this.mShareLink,
                    this.photoUrl);
        }
    }

    protected void onCancelled() {
        Helper.showLog(TAG, "onCancelled() called");
    }
}
