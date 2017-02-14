package com.umeng.socialize.bean;

public class CustomPlatform extends SnsPlatform {
    public CustomPlatform(String str, int i) {
        super(str);
        this.mShowWord = str;
        this.mIcon = i;
    }

    public CustomPlatform(String str, String str2, int i) {
        super(str);
        this.mShowWord = str2;
        this.mIcon = i;
    }

    public String toString() {
        return "CustomPlatform [keyword=" + this.mKeyword + ", showWord=" + this.mShowWord + ", " +
                "icon=" + this.mIcon + ", grayIcon=" + this.mGrayIcon + ", oauth=" + this.mOauth
                + ", bind=" + this.mBind + ", usid=" + this.mUsid + ", account=" + this.mAccount
                + "]";
    }
}
