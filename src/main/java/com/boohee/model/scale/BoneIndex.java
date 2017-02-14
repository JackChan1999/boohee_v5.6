package com.boohee.model.scale;

import com.baidu.location.aj;
import com.boohee.one.R;

import uk.co.senab.photoview.IPhotoView;

public class BoneIndex extends ScaleIndex {
    private float bone;

    public BoneIndex(float bone, int sex, float weight) {
        this.bone = bone;
        if (sex == 1) {
            if (weight < 60.0f) {
                this.division = new float[]{1.0f, 2.3f, 2.7f, aj.hA};
            } else if (weight <= 75.0f) {
                this.division = new float[]{1.0f, 2.7f, 3.1f, aj.hA};
            } else {
                this.division = new float[]{1.0f, IPhotoView.DEFAULT_MAX_SCALE, 3.4f, aj.hA};
            }
        } else if (weight < 45.0f) {
            this.division = new float[]{1.0f, 1.6f, 2.0f, aj.hA};
        } else if (weight <= 60.0f) {
            this.division = new float[]{1.0f, 2.0f, 2.4f, aj.hA};
        } else {
            this.division = new float[]{1.0f, 2.3f, 2.7f, aj.hA};
        }
        float[] fArr = this.division;
        fArr[2] = fArr[2] + 0.001f;
        this.LEVEL_NAME = new String[]{"偏低", "标准", "偏高"};
    }

    public String getUnit() {
        return "kg";
    }

    public float getValue() {
        return this.bone;
    }

    public String getName() {
        return "骨量";
    }

    public int getColor() {
        if (getLevel() == 1) {
            return ScaleIndex.COLOR_STANDARD;
        }
        return ScaleIndex.COLOR_FAIL;
    }

    public int getDes() {
        return R.string.a2r;
    }
}
