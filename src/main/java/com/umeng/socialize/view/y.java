package com.umeng.socialize.view;

import android.location.Location;
import android.widget.Toast;

import com.umeng.socialize.location.DefaultLocationProvider;
import com.umeng.socialize.location.GetLocationTask;

/* compiled from: ShareActivity */
class y extends GetLocationTask {
    final /* synthetic */ ShareActivity a;

    y(ShareActivity shareActivity, DefaultLocationProvider defaultLocationProvider) {
        this.a = shareActivity;
        super(defaultLocationProvider);
    }

    protected /* synthetic */ void onPostExecute(Object obj) {
        a((Location) obj);
    }

    protected void onPreExecute() {
        super.onPreExecute();
        this.a.a(true);
    }

    protected void a(Location location) {
        super.onPostExecute(location);
        this.a.z = location;
        this.a.a(false);
        if (location == null && !this.a.isFinishing()) {
            Toast.makeText(this.a.t, "获取地理位置失败，请稍候重试.", 0).show();
        }
    }

    protected void onCancelled() {
        super.onCancelled();
        this.a.a(false);
    }
}
