package com.umeng.socialize.view;

import android.graphics.Bitmap;
import android.text.TextUtils;

import com.umeng.socialize.common.UMAsyncTask;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.utils.BitmapUtils;

/* compiled from: ShareActivity */
class ah extends UMAsyncTask<Bitmap> {
    final /* synthetic */ UMImage       a;
    final /* synthetic */ int           b;
    final /* synthetic */ ShareActivity c;

    ah(ShareActivity shareActivity, UMImage uMImage, int i) {
        this.c = shareActivity;
        this.a = uMImage;
        this.b = i;
    }

    protected /* synthetic */ Object doInBackground() {
        return a();
    }

    protected /* synthetic */ void onPostExecute(Object obj) {
        a((Bitmap) obj);
    }

    protected Bitmap a() {
        String imageCachePath = this.a.getImageCachePath();
        if (TextUtils.isEmpty(imageCachePath)) {
            imageCachePath = this.a.toUrl();
        }
        return BitmapUtils.getBitmapFromFile(imageCachePath, 150, 150);
    }

    protected void a(Bitmap bitmap) {
        super.onPostExecute(bitmap);
        this.c.s.setVisibility(4);
        this.c.a.setVisibility(0);
        this.c.a(this.b, bitmap);
    }
}
