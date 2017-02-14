package com.boohee.model.scale;

import com.boohee.one.R;

public class VisfatIndex extends ScaleIndex {
    private float visfat;

    public VisfatIndex(float visfat) {
        this.visfat = visfat;
        this.division = new float[]{1.0f, 10.0f, 15.0f, 20.0f};
        float[] fArr = this.division;
        fArr[2] = fArr[2] + 0.001f;
        this.LEVEL_NAME = new String[]{"标准", "偏高", "严重偏高"};
    }

    public int getColor() {
        if (getLevel() == 0) {
            return ScaleIndex.COLOR_STANDARD;
        }
        return ScaleIndex.COLOR_FAIL;
    }

    public String getUnit() {
        return "级";
    }

    public float getValue() {
        return this.visfat;
    }

    public String getName() {
        return "内脏脂肪";
    }

    public int getDes() {
        return R.string.a2w;
    }
}
