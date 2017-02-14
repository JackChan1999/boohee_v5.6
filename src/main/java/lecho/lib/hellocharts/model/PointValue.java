package lecho.lib.hellocharts.model;

import java.util.Arrays;

public class PointValue {
    private Object data;
    private float diffX;
    private float diffY;
    private char[] label;
    private float originX;
    private float originY;
    private float x;
    private float y;

    public PointValue() {
        if (this.data == null) {
            set(0.0f, 0.0f);
        } else {
            set(0.0f, 0.0f, this.data);
        }
    }

    public PointValue(float x, float y) {
        set(x, y);
    }

    public PointValue(float x, float y, Object data) {
        set(x, y, data);
    }

    public PointValue(PointValue pointValue) {
        if (this.data == null) {
            set(pointValue.x, pointValue.y);
        } else {
            set(pointValue.x, pointValue.y, pointValue.data);
        }
        this.label = pointValue.label;
    }

    public void update(float scale) {
        this.x = this.originX + (this.diffX * scale);
        this.y = this.originY + (this.diffY * scale);
    }

    public void finish() {
        if (this.data == null) {
            set(this.originX + this.diffX, this.originY + this.diffY);
        } else {
            set(this.originX + this.diffX, this.originY + this.diffY, this.data);
        }
    }

    public PointValue set(float x, float y) {
        this.x = x;
        this.y = y;
        this.originX = x;
        this.originY = y;
        this.diffX = 0.0f;
        this.diffY = 0.0f;
        return this;
    }

    public PointValue set(float x, float y, Object data) {
        this.x = x;
        this.y = y;
        this.originX = x;
        this.originY = y;
        this.diffX = 0.0f;
        this.diffY = 0.0f;
        this.data = data;
        return this;
    }

    public PointValue setTarget(float targetX, float targetY, Object data) {
        set(this.x, this.y, data);
        this.diffX = targetX - this.originX;
        this.diffY = targetY - this.originY;
        return this;
    }

    public float getX() {
        return this.x;
    }

    public float getY() {
        return this.y;
    }

    @Deprecated
    public char[] getLabel() {
        return this.label;
    }

    public PointValue setLabel(String label) {
        this.label = label.toCharArray();
        return this;
    }

    public char[] getLabelAsChars() {
        return this.label;
    }

    @Deprecated
    public PointValue setLabel(char[] label) {
        this.label = label;
        return this;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public Object getData() {
        return this.data;
    }

    public String toString() {
        return "PointValue [x=" + this.x + ", y=" + this.y + "]";
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PointValue that = (PointValue) o;
        if (Float.compare(that.diffX, this.diffX) != 0) {
            return false;
        }
        if (Float.compare(that.diffY, this.diffY) != 0) {
            return false;
        }
        if (Float.compare(that.originX, this.originX) != 0) {
            return false;
        }
        if (Float.compare(that.originY, this.originY) != 0) {
            return false;
        }
        if (Float.compare(that.x, this.x) != 0) {
            return false;
        }
        if (Float.compare(that.y, this.y) != 0) {
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
        if (this.x != 0.0f) {
            result = Float.floatToIntBits(this.x);
        } else {
            result = 0;
        }
        int i2 = result * 31;
        if (this.y != 0.0f) {
            floatToIntBits = Float.floatToIntBits(this.y);
        } else {
            floatToIntBits = 0;
        }
        i2 = (i2 + floatToIntBits) * 31;
        if (this.originX != 0.0f) {
            floatToIntBits = Float.floatToIntBits(this.originX);
        } else {
            floatToIntBits = 0;
        }
        i2 = (i2 + floatToIntBits) * 31;
        if (this.originY != 0.0f) {
            floatToIntBits = Float.floatToIntBits(this.originY);
        } else {
            floatToIntBits = 0;
        }
        i2 = (i2 + floatToIntBits) * 31;
        if (this.diffX != 0.0f) {
            floatToIntBits = Float.floatToIntBits(this.diffX);
        } else {
            floatToIntBits = 0;
        }
        i2 = (i2 + floatToIntBits) * 31;
        if (this.diffY != 0.0f) {
            floatToIntBits = Float.floatToIntBits(this.diffY);
        } else {
            floatToIntBits = 0;
        }
        floatToIntBits = (i2 + floatToIntBits) * 31;
        if (this.label != null) {
            i = Arrays.hashCode(this.label);
        }
        return floatToIntBits + i;
    }
}
