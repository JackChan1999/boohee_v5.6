package cn.sharesdk.wechat.utils;

import android.os.Handler.Callback;
import android.os.Message;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import com.mob.tools.utils.DeviceHelper;
import com.mob.tools.utils.UIHandler;
import java.util.HashMap;

class l implements Callback {
    int a = 0;
    final /* synthetic */ DeviceHelper b;
    final /* synthetic */ String c;
    final /* synthetic */ PlatformActionListener d;
    final /* synthetic */ Platform e;
    final /* synthetic */ HashMap f;
    final /* synthetic */ WechatHelper g;

    l(WechatHelper wechatHelper, DeviceHelper deviceHelper, String str, PlatformActionListener platformActionListener, Platform platform, HashMap hashMap) {
        this.g = wechatHelper;
        this.b = deviceHelper;
        this.c = str;
        this.d = platformActionListener;
        this.e = platform;
        this.f = hashMap;
    }

    public boolean handleMessage(Message message) {
        if (this.c.equals(this.b.getTopTaskPackageName())) {
            if (this.a < 5) {
                this.a++;
                UIHandler.sendEmptyMessageDelayed(0, 500, this);
            }
        } else if (this.d != null) {
            this.d.onComplete(this.e, 9, this.f);
        }
        return true;
    }
}
