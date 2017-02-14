package cn.sharesdk.wechat.utils;

import android.os.Bundle;
import cn.sharesdk.wechat.utils.WXMediaMessage.a;
import com.mob.tools.utils.Ln;

public class d extends m {
    public WXMediaMessage a;
    public int b;

    public int a() {
        return 2;
    }

    public void a(Bundle bundle) {
        super.a(bundle);
        bundle.putAll(a.a(this.a));
        bundle.putInt("_wxapi_sendmessagetowx_req_scene", this.b);
    }

    public boolean b() {
        if (this.a.getType() == 8 && (this.a.thumbData == null || this.a.thumbData.length <= 0)) {
            Ln.e("checkArgs fail, thumbData should not be null when send emoji", new Object[0]);
            return false;
        } else if (this.a.thumbData != null && this.a.thumbData.length > 32768) {
            Ln.e("checkArgs fail, thumbData is invalid", new Object[0]);
            return false;
        } else if (this.a.title == null || this.a.title.length() <= 512) {
            if (this.a.description != null && this.a.description.length() > 1024) {
                this.a.description = this.a.description.substring(0, 1021) + "...";
            }
            if (this.a.mediaObject != null) {
                return this.a.mediaObject.checkArgs();
            }
            Ln.e("checkArgs fail, mediaObject is null", new Object[0]);
            return false;
        } else {
            Ln.e("checkArgs fail, title is invalid", new Object[0]);
            return false;
        }
    }
}
