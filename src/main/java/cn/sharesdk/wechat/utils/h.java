package cn.sharesdk.wechat.utils;

import android.text.TextUtils;
import cn.sharesdk.framework.authorize.AuthorizeListener;
import com.boohee.utils.Utils;
import com.mob.tools.network.KVPair;
import com.mob.tools.utils.Ln;
import java.util.ArrayList;

class h extends Thread {
    final /* synthetic */ String a;
    final /* synthetic */ AuthorizeListener b;
    final /* synthetic */ g c;

    h(g gVar, String str, AuthorizeListener authorizeListener) {
        this.c = gVar;
        this.a = str;
        this.b = authorizeListener;
    }

    public void run() {
        try {
            ArrayList arrayList = new ArrayList();
            arrayList.add(new KVPair("appid", this.c.a));
            arrayList.add(new KVPair("secret", this.c.b));
            arrayList.add(new KVPair("code", this.a));
            arrayList.add(new KVPair("grant_type", "authorization_code"));
            String a = this.c.c.a("https://api.weixin.qq.com/sns/oauth2/access_token", arrayList, "/sns/oauth2/access_token", this.c.e);
            if (TextUtils.isEmpty(a)) {
                this.b.onError(new Throwable("Authorize token is empty"));
            } else if (!a.contains(Utils.RESPONSE_ERRCODE)) {
                this.c.a(a);
                this.b.onComplete(null);
            } else if (this.b != null) {
                this.b.onError(new Throwable(a));
            }
        } catch (Throwable th) {
            Ln.e(th);
        }
    }
}
