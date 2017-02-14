package cn.sharesdk.onekeyshare;

import android.os.Handler.Callback;
import android.os.Message;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.Platform.ShareParams;
import com.mob.tools.utils.UIHandler;

public class ReflectableShareContentCustomizeCallback implements ShareContentCustomizeCallback {
    private Callback onShareCallback;
    private int onShareWhat;

    public void setOnShareCallback(int what, Callback callback) {
        this.onShareWhat = what;
        this.onShareCallback = callback;
    }

    public void onShare(Platform platform, ShareParams paramsToShare) {
        if (this.onShareCallback != null) {
            Message msg = new Message();
            msg.what = this.onShareWhat;
            msg.obj = new Object[]{platform, paramsToShare};
            UIHandler.sendMessage(msg, this.onShareCallback);
        }
    }
}
