package cn.sharesdk.wechat.utils;

import android.os.Bundle;

public abstract class m {
    public String c;

    public abstract int a();

    public void a(Bundle bundle) {
        bundle.putInt("_wxapi_command_type", a());
        bundle.putString("_wxapi_basereq_transaction", this.c);
    }

    public abstract boolean b();
}
