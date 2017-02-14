package com.boohee.model.scale;

import com.boohee.one.R;
import com.boohee.utils.ArithmeticUtil;

public class WeightIndex extends ScaleIndex {
    private float weight;

    public WeightIndex(int height, float weight) {
        this.weight = weight;
        this.LEVEL_NAME = new String[]{"偏轻", "健康", "超重", "肥胖"};
        this.division = new float[5];
        this.division[0] = 20.0f;
        this.division[1] = 18.5f * (((float) (height * height)) / 10000.0f);
        this.division[2] = 24.0f * (((float) (height * height)) / 10000.0f);
        this.division[3] = 28.0f * (((float) (height * height)) / 10000.0f);
        this.division[4] = 100.0f;
    }

    public float getValue() {
        return ArithmeticUtil.round(this.weight, 1);
    }

    public String getName() {
        return "体重";
    }

    public String getUnit() {
        return "kg";
    }

    public int getColor() {
        if (getLevel() == 1) {
            return ScaleIndex.COLOR_STANDARD;
        }
        return ScaleIndex.COLOR_FAIL;
    }

    public int getDes() {
        return R.string.a2y;
    }
}
