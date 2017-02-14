package cn.sharesdk.sina.weibo;

import android.os.Handler.Callback;
import android.os.Message;
import cn.sharesdk.framework.PlatformActionListener;
import com.mob.tools.utils.DeviceHelper;
import com.mob.tools.utils.UIHandler;
import java.util.HashMap;

class k implements Callback {
    int a = 0;
    final /* synthetic */ DeviceHelper b;
    final /* synthetic */ String c;
    final /* synthetic */ PlatformActionListener d;
    final /* synthetic */ HashMap e;
    final /* synthetic */ i f;

    k(i iVar, DeviceHelper deviceHelper, String str, PlatformActionListener platformActionListener, HashMap hashMap) {
        this.f = iVar;
        this.b = deviceHelper;
        this.c = str;
        this.d = platformActionListener;
        this.e = hashMap;
    }

    public boolean handleMessage(Message message) {
        if (this.c.equals(this.b.getTopTaskPackageName())) {
            if (this.a < 5) {
                this.a++;
                UIHandler.sendEmptyMessageDelayed(0, 500, this);
            }
        } else if (this.d != null) {
            this.d.onComplete(this.f.a, 9, this.e);
        }
        return true;
    }
}
