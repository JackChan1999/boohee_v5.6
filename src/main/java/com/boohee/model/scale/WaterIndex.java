package com.boohee.model.scale;

import com.boohee.one.R;

public class WaterIndex extends ScaleIndex {
    private float water;

    public WaterIndex(float water, int sex) {
        this.water = water;
        if (sex == 1) {
            this.division = new float[]{30.0f, 55.0f, 65.0f, 80.0f};
        } else {
            this.division = new float[]{30.0f, 45.0f, 60.0f, 80.0f};
        }
        float[] fArr = this.division;
        fArr[2] = fArr[2] + 0.001f;
        this.LEVEL_NAME = new String[]{"偏低", "标准", "偏高"};
    }

    public int getColor() {
        if (getLevel() == 1) {
            return ScaleIndex.COLOR_STANDARD;
        }
        return ScaleIndex.COLOR_FAIL;
    }

    public String getUnit() {
        return "%";
    }

    public float getValue() {
        return this.water;
    }

    public String getName() {
        return "体水分";
    }

    public int getDes() {
        return R.string.a2x;
    }
}
