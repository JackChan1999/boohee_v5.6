package lecho.lib.hellocharts.model;

import java.util.Arrays;

public class AxisValue {
    private Object data;
    private char[] label;
    private float value;

    public AxisValue(float value) {
        setValue(value);
    }

    public AxisValue(float value, char[] label) {
        this.value = value;
        this.label = label;
    }

    public AxisValue(float value, char[] label, Object data) {
        this.value = value;
        this.label = label;
        this.data = data;
    }

    public AxisValue(AxisValue axisValue) {
        this.value = axisValue.value;
        this.label = axisValue.label;
    }

    public float getValue() {
        return this.value;
    }

    public AxisValue setValue(float value) {
        this.value = value;
        return this;
    }

    @Deprecated
    public char[] getLabel() {
        return this.label;
    }

    public AxisValue setLabel(String label) {
        this.label = label.toCharArray();
        return this;
    }

    public char[] getLabelAsChars() {
        return this.label;
    }

    @Deprecated
    public AxisValue setLabel(char[] label) {
        this.label = label;
        return this;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public Object getData() {
        return this.data;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AxisValue axisValue = (AxisValue) o;
        if (Float.compare(axisValue.value, this.value) != 0) {
            return false;
        }
        if (Arrays.equals(this.label, axisValue.label)) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        int result;
        int i = 0;
        if (this.value != 0.0f) {
            result = Float.floatToIntBits(this.value);
        } else {
            result = 0;
        }
        int i2 = result * 31;
        if (this.label != null) {
            i = Arrays.hashCode(this.label);
        }
        return i2 + i;
    }
}
