package com.boohee.model.scale;

import com.boohee.one.R;

public class BmiIndex extends ScaleIndex {
    private float bmi;

    public BmiIndex(float bmi) {
        this.bmi = bmi;
        this.division = new float[]{15.0f, 18.5f, 24.0f, 28.0f, 35.0f};
        this.LEVEL_NAME = new String[]{"偏轻", "健康", "超重", "肥胖"};
    }

    public String getUnit() {
        return "";
    }

    public float getValue() {
        return this.bmi;
    }

    public String getName() {
        return "BMI";
    }

    public int getColor() {
        if (getLevel() == 1) {
            return ScaleIndex.COLOR_STANDARD;
        }
        return ScaleIndex.COLOR_FAIL;
    }

    public int getDes() {
        return R.string.a2o;
    }
}
