package com.umeng.socialize.view;

import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.common.ResContainer;
import com.umeng.socialize.utils.DeviceConfig;
import com.umeng.socialize.utils.SocializeUtils;

/* compiled from: ShareActivity */
class ag implements OnClickListener {
    final /* synthetic */ ShareActivity a;

    ag(ShareActivity shareActivity) {
        this.a = shareActivity;
    }

    public void onClick(View view) {
        if (DeviceConfig.isNetworkAvailable(this.a)) {
            String obj = this.a.f.getText().toString();
            if (TextUtils.isEmpty(obj.trim()) && this.a.D == null) {
                Toast.makeText(this.a, "输入内容为空...", 0).show();
                return;
            } else if (SocializeUtils.countContentLength(obj) > 140) {
                Toast.makeText(this.a, "输入内容超过140个字.", 0).show();
                return;
            } else if (this.a.u) {
                Toast.makeText(this.a.t, "超出最大字数限制....", 0).show();
                return;
            } else {
                if (this.a.x == SHARE_MEDIA.QQ) {
                    this.a.g();
                } else {
                    this.a.f();
                }
                this.a.finish();
                return;
            }
        }
        Toast.makeText(this.a, ResContainer.getString(this.a,
                "umeng_socialize_network_break_alert"), 1).show();
    }
}
