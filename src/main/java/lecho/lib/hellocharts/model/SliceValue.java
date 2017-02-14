package lecho.lib.hellocharts.model;

import java.util.Arrays;
import lecho.lib.hellocharts.util.ChartUtils;

public class SliceValue {
    private static final int DEFAULT_SLICE_SPACING_DP = 2;
    private int color = ChartUtils.DEFAULT_COLOR;
    private int darkenColor = ChartUtils.DEFAULT_DARKEN_COLOR;
    private float diff;
    private char[] label;
    private float originValue;
    @Deprecated
    private int sliceSpacing = 2;
    private float value;

    public SliceValue() {
        setValue(0.0f);
    }

    public SliceValue(float value) {
        setValue(value);
    }

    public SliceValue(float value, int color) {
        setValue(value);
        setColor(color);
    }

    public SliceValue(float value, int color, int sliceSpacing) {
        setValue(value);
        setColor(color);
        this.sliceSpacing = sliceSpacing;
    }

    public SliceValue(SliceValue sliceValue) {
        setValue(sliceValue.value);
        setColor(sliceValue.color);
        this.sliceSpacing = sliceValue.sliceSpacing;
        this.label = sliceValue.label;
    }

    public void update(float scale) {
        this.value = this.originValue + (this.diff * scale);
    }

    public void finish() {
        setValue(this.originValue + this.diff);
    }

    public float getValue() {
        return this.value;
    }

    public SliceValue setValue(float value) {
        this.value = value;
        this.originValue = value;
        this.diff = 0.0f;
        return this;
    }

    public SliceValue setTarget(float target) {
        setValue(this.value);
        this.diff = target - this.originValue;
        return this;
    }

    public int getColor() {
        return this.color;
    }

    public SliceValue setColor(int color) {
        this.color = color;
        this.darkenColor = ChartUtils.darkenColor(color);
        return this;
    }

    public int getDarkenColor() {
        return this.darkenColor;
    }

    @Deprecated
    public int getSliceSpacing() {
        return this.sliceSpacing;
    }

    @Deprecated
    public SliceValue setSliceSpacing(int sliceSpacing) {
        this.sliceSpacing = sliceSpacing;
        return this;
    }

    @Deprecated
    public char[] getLabel() {
        return this.label;
    }

    @Deprecated
    public SliceValue setLabel(char[] label) {
        this.label = label;
        return this;
    }

    public SliceValue setLabel(String label) {
        this.label = label.toCharArray();
        return this;
    }

    public char[] getLabelAsChars() {
        return this.label;
    }

    public String toString() {
        return "SliceValue [value=" + this.value + "]";
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SliceValue that = (SliceValue) o;
        if (this.color != that.color) {
            return false;
        }
        if (this.darkenColor != that.darkenColor) {
            return false;
        }
        if (Float.compare(that.diff, this.diff) != 0) {
            return false;
        }
        if (Float.compare(that.originValue, this.originValue) != 0) {
            return false;
        }
        if (this.sliceSpacing != that.sliceSpacing) {
            return false;
        }
        if (Float.compare(that.value, this.value) != 0) {
            return false;
        }
        if (Arrays.equals(this.label, that.label)) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        int result;
        int floatToIntBits;
        int i = 0;
        if (this.value != 0.0f) {
            result = Float.floatToIntBits(this.value);
        } else {
            result = 0;
        }
        int i2 = result * 31;
        if (this.originValue != 0.0f) {
            floatToIntBits = Float.floatToIntBits(this.originValue);
        } else {
            floatToIntBits = 0;
        }
        i2 = (i2 + floatToIntBits) * 31;
        if (this.diff != 0.0f) {
            floatToIntBits = Float.floatToIntBits(this.diff);
        } else {
            floatToIntBits = 0;
        }
        floatToIntBits = (((((((i2 + floatToIntBits) * 31) + this.color) * 31) + this.darkenColor) * 31) + this.sliceSpacing) * 31;
        if (this.label != null) {
            i = Arrays.hashCode(this.label);
        }
        return floatToIntBits + i;
    }
}
