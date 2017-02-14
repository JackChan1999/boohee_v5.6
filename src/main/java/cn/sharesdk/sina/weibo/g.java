package cn.sharesdk.sina.weibo;

import android.os.Bundle;
import cn.sharesdk.framework.Platform;
import com.mob.tools.utils.Hashon;
import com.mob.tools.utils.Ln;
import com.umeng.socialize.net.utils.SocializeProtocolConstants;
import java.util.HashMap;

class g extends Thread {
    final /* synthetic */ Platform a;
    final /* synthetic */ String b;
    final /* synthetic */ f c;

    g(f fVar, Platform platform, String str) {
        this.c = fVar;
        this.a = platform;
        this.b = str;
    }

    public void run() {
        try {
            String a = i.a(this.a).a(this.a.getContext(), this.b);
        } catch (Throwable th) {
            Ln.e(th);
            return;
        }
        if (a == null) {
            this.c.c.onError(new Throwable("Authorize token is empty"));
            return;
        }
        HashMap fromJson = new Hashon().fromJson(a);
        Bundle bundle = new Bundle();
        bundle.putString("access_token", String.valueOf(fromJson.get("access_token")));
        bundle.putString("remind_in", String.valueOf(fromJson.get("remind_in")));
        bundle.putString("expires_in", String.valueOf(fromJson.get("expires_in")));
        bundle.putString(SocializeProtocolConstants.PROTOCOL_KEY_UID, String.valueOf(fromJson.get(SocializeProtocolConstants.PROTOCOL_KEY_UID)));
        this.c.c.onComplete(bundle);
    }
}
