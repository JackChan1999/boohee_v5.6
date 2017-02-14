package cn.sharesdk.wechat.utils;

import android.os.Bundle;

public abstract class WechatResp {
    public int f;
    public String g;
    public String h;

    public interface ErrCode {
        public static final int ERR_AUTH_DENIED = -4;
        public static final int ERR_COMM = -1;
        public static final int ERR_OK = 0;
        public static final int ERR_SENT_FAILED = -3;
        public static final int ERR_UNSUPPORT = -5;
        public static final int ERR_USER_CANCEL = -2;
    }

    public WechatResp(Bundle bundle) {
        a(bundle);
    }

    public abstract int a();

    public void a(Bundle bundle) {
        this.f = bundle.getInt("_wxapi_baseresp_errcode");
        this.g = bundle.getString("_wxapi_baseresp_errstr");
        this.h = bundle.getString("_wxapi_baseresp_transaction");
    }

    public void b(Bundle bundle) {
        bundle.putInt("_wxapi_command_type", a());
        bundle.putInt("_wxapi_baseresp_errcode", this.f);
        bundle.putString("_wxapi_baseresp_errstr", this.g);
        bundle.putString("_wxapi_baseresp_transaction", this.h);
    }
}
