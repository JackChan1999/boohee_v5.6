package com.nineoldandroids.animation;

import android.view.animation.Interpolator;

public abstract class Keyframe implements Cloneable {
    float mFraction;
    boolean mHasValue = false;
    private Interpolator mInterpolator = null;
    Class mValueType;

    static class FloatKeyframe extends Keyframe {
        float mValue;

        FloatKeyframe(float fraction, float value) {
            this.mFraction = fraction;
            this.mValue = value;
            this.mValueType = Float.TYPE;
            this.mHasValue = true;
        }

        FloatKeyframe(float fraction) {
            this.mFraction = fraction;
            this.mValueType = Float.TYPE;
        }

        public float getFloatValue() {
            return this.mValue;
        }

        public Object getValue() {
            return Float.valueOf(this.mValue);
        }

        public void setValue(Object value) {
            if (value != null && value.getClass() == Float.class) {
                this.mValue = ((Float) value).floatValue();
                this.mHasValue = true;
            }
        }

        public FloatKeyframe clone() {
            FloatKeyframe kfClone = new FloatKeyframe(getFraction(), this.mValue);
            kfClone.setInterpolator(getInterpolator());
            return kfClone;
        }
    }

    static class IntKeyframe extends Keyframe {
        int mValue;

        IntKeyframe(float fraction, int value) {
            this.mFraction = fraction;
            this.mValue = value;
            this.mValueType = Integer.TYPE;
            this.mHasValue = true;
        }

        IntKeyframe(float fraction) {
            this.mFraction = fraction;
            this.mValueType = Integer.TYPE;
        }

        public int getIntValue() {
            return this.mValue;
        }

        public Object getValue() {
            return Integer.valueOf(this.mValue);
        }

        public void setValue(Object value) {
            if (value != null && value.getClass() == Integer.class) {
                this.mValue = ((Integer) value).intValue();
                this.mHasValue = true;
            }
        }

        public IntKeyframe clone() {
            IntKeyframe kfClone = new IntKeyframe(getFraction(), this.mValue);
            kfClone.setInterpolator(getInterpolator());
            return kfClone;
        }
    }

    static class ObjectKeyframe extends Keyframe {
        Object mValue;

        ObjectKeyframe(float fraction, Object value) {
            this.mFraction = fraction;
            this.mValue = value;
            this.mHasValue = value != null;
            this.mValueType = this.mHasValue ? value.getClass() : Object.class;
        }

        public Object getValue() {
            return this.mValue;
        }

        public void setValue(Object value) {
            this.mValue = value;
            this.mHasValue = value != null;
        }

        public ObjectKeyframe clone() {
            ObjectKeyframe kfClone = new ObjectKeyframe(getFraction(), this.mValue);
            kfClone.setInterpolator(getInterpolator());
            return kfClone;
        }
    }

    public abstract Keyframe clone();

    public abstract Object getValue();

    public abstract void setValue(Object obj);

    public static Keyframe ofInt(float fraction, int value) {
        return new IntKeyframe(fraction, value);
    }

    public static Keyframe ofInt(float fraction) {
        return new IntKeyframe(fraction);
    }

    public static Keyframe ofFloat(float fraction, float value) {
        return new FloatKeyframe(fraction, value);
    }

    public static Keyframe ofFloat(float fraction) {
        return new FloatKeyframe(fraction);
    }

    public static Keyframe ofObject(float fraction, Object value) {
        return new ObjectKeyframe(fraction, value);
    }

    public static Keyframe ofObject(float fraction) {
        return new ObjectKeyframe(fraction, null);
    }

    public boolean hasValue() {
        return this.mHasValue;
    }

    public float getFraction() {
        return this.mFraction;
    }

    public void setFraction(float fraction) {
        this.mFraction = fraction;
    }

    public Interpolator getInterpolator() {
        return this.mInterpolator;
    }

    public void setInterpolator(Interpolator interpolator) {
        this.mInterpolator = interpolator;
    }

    public Class getType() {
        return this.mValueType;
    }
}
