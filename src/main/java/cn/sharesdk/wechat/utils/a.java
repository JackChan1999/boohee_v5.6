package cn.sharesdk.wechat.utils;

import android.os.Bundle;
import com.mob.tools.utils.Ln;

public class a extends m {
    public String a;
    public String b;

    public int a() {
        return 1;
    }

    public void a(Bundle bundle) {
        super.a(bundle);
        bundle.putString("_wxapi_sendauth_req_scope", this.a);
        bundle.putString("_wxapi_sendauth_req_state", this.b);
    }

    public boolean b() {
        if (this.a == null || this.a.length() == 0 || this.a.length() > 1024) {
            Ln.e("MicroMsg.SDK.SendAuth.Req", "checkArgs fail, scope is invalid");
            return false;
        } else if (this.b == null || this.b.length() <= 1024) {
            return true;
        } else {
            Ln.e("MicroMsg.SDK.SendAuth.Req", "checkArgs fail, state is invalid");
            return false;
        }
    }
}
