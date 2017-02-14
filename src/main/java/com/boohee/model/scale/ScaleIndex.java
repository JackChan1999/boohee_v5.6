package com.boohee.model.scale;

public abstract class ScaleIndex {
    public static final int COLOR_FAIL     = -28566;
    public static final int COLOR_STANDARD = -10302348;
    protected String[] LEVEL_NAME;
    protected float[]  division;

    public abstract int getColor();

    public abstract int getDes();

    public abstract String getName();

    public abstract String getUnit();

    public abstract float getValue();

    public float[] getDivision() {
        return this.division;
    }

    public String getValueWithUnit() {
        return String.valueOf(getValue()) + getUnit();
    }

    public int getLevel() {
        if (this.division == null) {
            return 0;
        }
        float value = getValue();
        for (int i = 0; i < this.division.length; i++) {
            if (i == this.division.length - 1) {
                return i - 1;
            }
            if (value < this.division[i]) {
                return Math.max(0, i - 1);
            }
        }
        return 0;
    }

    public String getLevelName() {
        int level = getLevel();
        if (this.LEVEL_NAME == null || this.LEVEL_NAME.length <= level) {
            return "";
        }
        return this.LEVEL_NAME[getLevel()];
    }

    public String[] getAllLevel() {
        return this.LEVEL_NAME;
    }
}
