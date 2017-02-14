package cn.sharesdk.framework.authorize;

import android.os.Message;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import cn.sharesdk.framework.ShareSDK;
import com.mob.tools.utils.DeviceHelper;
import com.mob.tools.utils.Ln;
import com.mob.tools.utils.UIHandler;

class j extends Thread {
    final /* synthetic */ g a;

    j(g gVar) {
        this.a = gVar;
    }

    public void run() {
        try {
            Message message = new Message();
            message.what = 2;
            if ("none".equals(DeviceHelper.getInstance(this.a.activity).getDetailNetworkTypeForStatic())) {
                message.arg1 = 1;
                UIHandler.sendMessage(message, this.a);
                return;
            }
            if (ShareSDK.isRemoveCookieOnAuthorize()) {
                CookieSyncManager.createInstance(this.a.activity);
                CookieManager.getInstance().removeAllCookie();
            }
            message.obj = this.a.a.getAuthorizeUrl();
            UIHandler.sendMessage(message, this.a);
        } catch (Throwable th) {
            Ln.e(th);
        }
    }
}
