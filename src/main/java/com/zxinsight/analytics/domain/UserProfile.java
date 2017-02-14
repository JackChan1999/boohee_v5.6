package com.zxinsight.analytics.domain;

import android.text.TextUtils;

import com.zxinsight.common.util.h;
import com.zxinsight.common.util.n;

public class UserProfile {
    public String birthday;
    public String city;
    public String country;
    public String displayName;
    public String email;
    public String gender = "0";
    public String geo;
    public String phone;
    public String profileId;
    public String province;
    public String remark;
    public String userRank;

    public UserProfile(String str) {
        this.profileId = str;
        setTelephone(str);
        setEmail(str);
    }

    public UserProfile setUserId(String str) {
        this.profileId = str.trim();
        return this;
    }

    public UserProfile setTelephone(String str) {
        if (n.c(str)) {
            this.phone = n.b(str);
        }
        return this;
    }

    public UserProfile setDisplayName(String str) {
        this.displayName = str;
        return this;
    }

    public UserProfile setGender(String str) {
        this.gender = str.trim();
        return this;
    }

    public UserProfile setBirthday(String str) {
        if (!(TextUtils.isEmpty(str) || TextUtils.isEmpty(str.trim()) || str.trim().length() <=
                18)) {
            str = str.trim().substring(0, 18);
        }
        this.birthday = str;
        return this;
    }

    public UserProfile setEmail(String str) {
        if (n.d(str)) {
            this.email = str.trim();
        }
        return this;
    }

    public UserProfile setCountry(String str) {
        this.country = str.trim();
        return this;
    }

    public UserProfile setProvince(String str) {
        this.province = str.trim();
        return this;
    }

    public UserProfile setCity(String str) {
        this.city = str.trim();
        return this;
    }

    public UserProfile setRemark(String str) {
        this.remark = str;
        return this;
    }

    public UserProfile setGeo(String str) {
        this.geo = str;
        return this;
    }

    public UserProfile setUserRank(String str) {
        this.userRank = str;
        return this;
    }

    public String toString() {
        return h.a((Object) this);
    }
}
