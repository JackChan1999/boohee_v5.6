package cn.sharesdk.wechat.utils;

import android.os.Bundle;
import cn.sharesdk.wechat.utils.WXMediaMessage.a;

public class c extends WechatResp {
    public WXMediaMessage a;

    public c(Bundle bundle) {
        super(bundle);
    }

    public int a() {
        return 3;
    }

    public void a(Bundle bundle) {
        super.a(bundle);
        this.a = a.a(bundle);
    }

    public void b(Bundle bundle) {
        super.b(bundle);
        bundle.putAll(a.a(this.a));
    }
}
