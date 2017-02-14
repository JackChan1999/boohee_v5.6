package com.boohee.model.scale;

import com.boohee.myview.IntFloatWheelView;
import com.boohee.one.R;

public class SkeletalMuscleIndex extends ScaleIndex {
    private float muscle;

    public SkeletalMuscleIndex(float muscle, int sex) {
        this.muscle = muscle;
        if (sex == 1) {
            this.division = new float[]{20.0f, 49.0f, 59.0f, 70.0f};
        } else {
            this.division = new float[]{20.0f, 40.0f, IntFloatWheelView.DEFAULT_VALUE, 70.0f};
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
        return this.muscle;
    }

    public String getName() {
        return "骨骼肌率";
    }

    public int getDes() {
        return R.string.a2u;
    }
}
