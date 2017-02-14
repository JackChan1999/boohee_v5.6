package com.nineoldandroids.animation;

import java.util.ArrayList;

class FloatKeyframeSet extends KeyframeSet {
    private float deltaValue;
    private boolean firstTime = true;
    private float firstValue;
    private float lastValue;

    public FloatKeyframeSet(FloatKeyframe... keyframes) {
        super(keyframes);
    }

    public Object getValue(float fraction) {
        return Float.valueOf(getFloatValue(fraction));
    }

    public FloatKeyframeSet clone() {
        ArrayList<Keyframe> keyframes = this.mKeyframes;
        int numKeyframes = this.mKeyframes.size();
        FloatKeyframe[] newKeyframes = new FloatKeyframe[numKeyframes];
        for (int i = 0; i < numKeyframes; i++) {
            newKeyframes[i] = (FloatKeyframe) ((Keyframe) keyframes.get(i)).clone();
        }
        return new FloatKeyframeSet(newKeyframes);
    }

    public float getFloatValue(float fraction) {
        if (this.mNumKeyframes == 2) {
            if (this.firstTime) {
                this.firstTime = false;
                this.firstValue = ((FloatKeyframe) this.mKeyframes.get(0)).getFloatValue();
                this.lastValue = ((FloatKeyframe) this.mKeyframes.get(1)).getFloatValue();
                this.deltaValue = this.lastValue - this.firstValue;
            }
            if (this.mInterpolator != null) {
                fraction = this.mInterpolator.getInterpolation(fraction);
            }
            if (this.mEvaluator == null) {
                return this.firstValue + (this.deltaValue * fraction);
            }
            return ((Number) this.mEvaluator.evaluate(fraction, Float.valueOf(this.firstValue),
                    Float.valueOf(this.lastValue))).floatValue();
        } else if (fraction <= 0.0f) {
            prevKeyframe = (FloatKeyframe) this.mKeyframes.get(0);
            nextKeyframe = (FloatKeyframe) this.mKeyframes.get(1);
            prevValue = prevKeyframe.getFloatValue();
            nextValue = nextKeyframe.getFloatValue();
            prevFraction = prevKeyframe.getFraction();
            nextFraction = nextKeyframe.getFraction();
            interpolator = nextKeyframe.getInterpolator();
            if (interpolator != null) {
                fraction = interpolator.getInterpolation(fraction);
            }
            intervalFraction = (fraction - prevFraction) / (nextFraction - prevFraction);
            return this.mEvaluator == null ? ((nextValue - prevValue) * intervalFraction) +
                    prevValue : ((Number) this.mEvaluator.evaluate(intervalFraction, Float
                    .valueOf(prevValue), Float.valueOf(nextValue))).floatValue();
        } else if (fraction >= 1.0f) {
            prevKeyframe = (FloatKeyframe) this.mKeyframes.get(this.mNumKeyframes - 2);
            nextKeyframe = (FloatKeyframe) this.mKeyframes.get(this.mNumKeyframes - 1);
            prevValue = prevKeyframe.getFloatValue();
            nextValue = nextKeyframe.getFloatValue();
            prevFraction = prevKeyframe.getFraction();
            nextFraction = nextKeyframe.getFraction();
            interpolator = nextKeyframe.getInterpolator();
            if (interpolator != null) {
                fraction = interpolator.getInterpolation(fraction);
            }
            intervalFraction = (fraction - prevFraction) / (nextFraction - prevFraction);
            return this.mEvaluator == null ? ((nextValue - prevValue) * intervalFraction) +
                    prevValue : ((Number) this.mEvaluator.evaluate(intervalFraction, Float
                    .valueOf(prevValue), Float.valueOf(nextValue))).floatValue();
        } else {
            prevKeyframe = (FloatKeyframe) this.mKeyframes.get(0);
            int i = 1;
            while (i < this.mNumKeyframes) {
                nextKeyframe = (FloatKeyframe) this.mKeyframes.get(i);
                if (fraction < nextKeyframe.getFraction()) {
                    interpolator = nextKeyframe.getInterpolator();
                    if (interpolator != null) {
                        fraction = interpolator.getInterpolation(fraction);
                    }
                    intervalFraction = (fraction - prevKeyframe.getFraction()) / (nextKeyframe
                            .getFraction() - prevKeyframe.getFraction());
                    prevValue = prevKeyframe.getFloatValue();
                    nextValue = nextKeyframe.getFloatValue();
                    return this.mEvaluator == null ? ((nextValue - prevValue) * intervalFraction)
                            + prevValue : ((Number) this.mEvaluator.evaluate(intervalFraction,
                            Float.valueOf(prevValue), Float.valueOf(nextValue))).floatValue();
                } else {
                    prevKeyframe = nextKeyframe;
                    i++;
                }
            }
            return ((Number) ((Keyframe) this.mKeyframes.get(this.mNumKeyframes - 1)).getValue())
                    .floatValue();
        }
    }
}
