package com.alipay.apmobilesecuritysdk.b;

import com.alipay.security.mobile.module.commonutils.CommonUtils;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public final class a {
    private String a;
    private String b;
    private String c;
    private String d;
    private String e;
    private String f;
    private String g;

    public a(String str, String str2, String str3, String str4, String str5, String str6, String str7) {
        this.a = str;
        this.b = str2;
        this.c = str3;
        this.d = str4;
        this.e = str5;
        this.f = str6;
        this.g = str7;
    }

    public final String toString() {
        StringBuffer stringBuffer = new StringBuffer(new SimpleDateFormat("yyyyMMddHHmmssSSS").format(Calendar.getInstance().getTime()));
        stringBuffer.append("," + this.a);
        stringBuffer.append("," + this.b);
        stringBuffer.append("," + this.c);
        stringBuffer.append("," + this.d);
        if (CommonUtils.isBlank(this.e) || this.e.length() < 20) {
            stringBuffer.append("," + this.e);
        } else {
            stringBuffer.append("," + this.e.substring(0, 20));
        }
        if (CommonUtils.isBlank(this.f) || this.f.length() < 20) {
            stringBuffer.append("," + this.f);
        } else {
            stringBuffer.append("," + this.f.substring(0, 20));
        }
        if (CommonUtils.isBlank(this.g) || this.g.length() < 20) {
            stringBuffer.append("," + this.g);
        } else {
            stringBuffer.append("," + this.g.substring(0, 20));
        }
        return stringBuffer.toString();
    }
}
