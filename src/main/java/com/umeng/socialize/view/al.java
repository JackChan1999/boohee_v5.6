package com.umeng.socialize.view;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.SnsPlatform;
import com.umeng.socialize.utils.DeviceConfig;

/* compiled from: ShareBoard */
class al implements OnClickListener {
    final /* synthetic */ SnsPlatform a;
    final /* synthetic */ ak          b;

    al(ak akVar, SnsPlatform snsPlatform) {
        this.b = akVar;
        this.a = snsPlatform;
    }

    public void onClick(View view) {
        this.b.b.dismiss();
        SHARE_MEDIA convertToEmun = SHARE_MEDIA.convertToEmun(this.a.mKeyword);
        if (DeviceConfig.isNetworkAvailable(this.b.b.a) || convertToEmun == SHARE_MEDIA.SMS) {
            this.b.a(this.a, convertToEmun);
        } else {
            Toast.makeText(this.b.b.a, "您的网络不可用,请检查网络连接...", 0).show();
        }
    }
}
