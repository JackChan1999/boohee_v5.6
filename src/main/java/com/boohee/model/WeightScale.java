package com.boohee.model;

import android.text.TextUtils;

public class WeightScale {
    public String deviceName;
    public String mac;
    public String model;

    public WeightScale(String deviceName, String mac, String model) {
        this.deviceName = deviceName;
        this.mac = mac;
        this.model = model;
    }

    public String showName() {
        if (TextUtils.isEmpty(this.model)) {
            return "云康宝体重秤";
        }
        return "Yolanda " + this.model;
    }
}
