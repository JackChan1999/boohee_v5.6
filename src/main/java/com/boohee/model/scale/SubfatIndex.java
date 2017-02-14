package com.boohee.model.scale;

import com.boohee.one.R;

import uk.co.senab.photoview.IPhotoView;

public class SubfatIndex extends ScaleIndex {
    private float subfat;

    public SubfatIndex(float subfat, int sex) {
        this.subfat = subfat;
        if (sex == 1) {
            this.division = new float[]{IPhotoView.DEFAULT_MAX_SCALE, 8.6f, 16.7f, 20.7f, 40.0f};
        } else {
            this.division = new float[]{IPhotoView.DEFAULT_MAX_SCALE, 18.5f, 26.7f, 30.8f, 40.0f};
        }
        float[] fArr = this.division;
        fArr[2] = fArr[2] + 0.001f;
        this.LEVEL_NAME = new String[]{"偏瘦", "标准", "偏高", "严重偏高"};
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
        return this.subfat;
    }

    public String getName() {
        return "皮下脂肪";
    }

    public int getDes() {
        return R.string.a2v;
    }
}
