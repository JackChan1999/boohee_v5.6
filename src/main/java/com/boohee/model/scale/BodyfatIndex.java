package com.boohee.model.scale;

import com.boohee.one.R;

public class BodyfatIndex extends ScaleIndex {
    private float bodyfat;

    public BodyfatIndex(float bodyfat, int sex) {
        this.bodyfat = bodyfat;
        if (sex == 1) {
            this.division = new float[]{5.0f, 11.0f, 16.0f, 21.0f, 26.0f, 40.0f};
        } else {
            this.division = new float[]{16.0f, 21.0f, 26.0f, 31.0f, 36.0f, 40.0f};
        }
        float[] fArr = this.division;
        fArr[4] = fArr[4] + 0.001f;
        fArr = this.division;
        fArr[3] = fArr[3] + 0.001f;
        this.LEVEL_NAME = new String[]{"瘦", "偏瘦", "标准", "微胖", "肥胖"};
    }

    public int getColor() {
        int level = getLevel();
        if (level == 1 || level == 2) {
            return ScaleIndex.COLOR_STANDARD;
        }
        return ScaleIndex.COLOR_FAIL;
    }

    public String getUnit() {
        return "%";
    }

    public float getValue() {
        return this.bodyfat;
    }

    public String getName() {
        return "体脂率";
    }

    public int getDes() {
        return R.string.a2o;
    }
}
