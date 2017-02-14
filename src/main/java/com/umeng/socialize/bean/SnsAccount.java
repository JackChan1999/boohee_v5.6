package com.umeng.socialize.bean;

public class SnsAccount {
    private String a;
    private String b;
    private Gender c;
    private String d;
    private String e;
    private String f;
    private String g;
    private String h;

    public SnsAccount(String str, Gender gender, String str2, String str3) {
        this.b = str;
        this.c = gender;
        this.d = str2;
        this.e = str3;
    }

    public String getProfileUrl() {
        return this.f;
    }

    public void setProfileUrl(String str) {
        this.f = str;
    }

    public String getPlatform() {
        return this.a;
    }

    public void setPlatform(String str) {
        this.a = str;
    }

    public String getUserName() {
        return this.b;
    }

    public void setUserName(String str) {
        this.b = str;
    }

    public Gender getGender() {
        return this.c;
    }

    public void setGender(Gender gender) {
        this.c = gender;
    }

    public String getAccountIconUrl() {
        return this.d;
    }

    public void setAccountIconUrl(String str) {
        this.d = str;
    }

    public String getUsid() {
        return this.e;
    }

    public void setUsid(String str) {
        this.e = str;
    }

    public String getExtendArgs() {
        return this.h;
    }

    public void setExtendArgs(String str) {
        this.h = str;
    }

    public String getBirthday() {
        return this.g;
    }

    public void setBirthday(String str) {
        this.g = str;
    }

    public String toString() {
        return "SnsAccount [mPlatform=" + this.a + ", mUserName=" + this.b + ", mGender=" + this
                .c + ", mAccountIconUrl=" + this.d + ", mUsid=" + this.e + ", mProfileUrl=" +
                this.f + ", mBirthday=" + this.g + ", mExtendArgs=" + this.h + "]";
    }
}
