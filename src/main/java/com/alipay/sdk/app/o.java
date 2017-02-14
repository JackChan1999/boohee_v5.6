package com.alipay.sdk.app;

import com.nostra13.universalimageloader.core.download.BaseImageDownloader;

public enum o {
    SUCCEEDED(9000, "处理成功"),
    FAILED(4000, "系统繁忙，请稍后再试"),
    CANCELED(6001, "用户取消"),
    NETWORK_ERROR(6002, "网络连接异常"),
    PARAMS_ERROR(4001, "参数错误"),
    DOUBLE_REQUEST(BaseImageDownloader.DEFAULT_HTTP_CONNECT_TIMEOUT, "重复请求"),
    PAY_WAITTING(8000, "支付结果确认中");
    
    private int h;
    private String i;

    private o(int i, String str) {
        this.h = i;
        this.i = str;
    }

    private void b(int i) {
        this.h = i;
    }

    public final int a() {
        return this.h;
    }

    private void a(String str) {
        this.i = str;
    }

    public final String b() {
        return this.i;
    }

    public static o a(int i) {
        switch (i) {
            case 4001:
                return PARAMS_ERROR;
            case BaseImageDownloader.DEFAULT_HTTP_CONNECT_TIMEOUT /*5000*/:
                return DOUBLE_REQUEST;
            case 6001:
                return CANCELED;
            case 6002:
                return NETWORK_ERROR;
            case 8000:
                return PAY_WAITTING;
            case 9000:
                return SUCCEEDED;
            default:
                return FAILED;
        }
    }
}
