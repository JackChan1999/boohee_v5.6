package com.boohee.model.scale;

import com.boohee.one.R;

public class ProteinIndex extends ScaleIndex {
    private float protein;

    public ProteinIndex(float protein, int sex) {
        this.protein = protein;
        if (sex == 1) {
            this.division = new float[]{10.0f, 16.0f, 18.0f, 30.0f};
        } else {
            this.division = new float[]{10.0f, 14.0f, 16.0f, 30.0f};
        }
        float[] fArr = this.division;
        fArr[2] = fArr[2] + 0.001f;
        this.LEVEL_NAME = new String[]{"不足", "标准", "优"};
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
        return this.protein;
    }

    public String getName() {
        return "蛋白质";
    }

    public int getDes() {
        return R.string.a2t;
    }
}
