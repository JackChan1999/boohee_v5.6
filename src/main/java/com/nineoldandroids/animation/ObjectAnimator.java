package com.nineoldandroids.animation;

import android.view.View;

import com.nineoldandroids.util.Property;
import com.nineoldandroids.view.animation.AnimatorProxy;

import java.util.HashMap;
import java.util.Map;

public final class ObjectAnimator extends ValueAnimator {
    private static final boolean               DBG              = false;
    private static final Map<String, Property> PROXY_PROPERTIES = new HashMap();
    private Property mProperty;
    private String   mPropertyName;
    private Object   mTarget;

    static {
        PROXY_PROPERTIES.put("alpha", PreHoneycombCompat.ALPHA);
        PROXY_PROPERTIES.put("pivotX", PreHoneycombCompat.PIVOT_X);
        PROXY_PROPERTIES.put("pivotY", PreHoneycombCompat.PIVOT_Y);
        PROXY_PROPERTIES.put("translationX", PreHoneycombCompat.TRANSLATION_X);
        PROXY_PROPERTIES.put("translationY", PreHoneycombCompat.TRANSLATION_Y);
        PROXY_PROPERTIES.put("rotation", PreHoneycombCompat.ROTATION);
        PROXY_PROPERTIES.put("rotationX", PreHoneycombCompat.ROTATION_X);
        PROXY_PROPERTIES.put("rotationY", PreHoneycombCompat.ROTATION_Y);
        PROXY_PROPERTIES.put("scaleX", PreHoneycombCompat.SCALE_X);
        PROXY_PROPERTIES.put("scaleY", PreHoneycombCompat.SCALE_Y);
        PROXY_PROPERTIES.put("scrollX", PreHoneycombCompat.SCROLL_X);
        PROXY_PROPERTIES.put("scrollY", PreHoneycombCompat.SCROLL_Y);
        PROXY_PROPERTIES.put("x", PreHoneycombCompat.X);
        PROXY_PROPERTIES.put("y", PreHoneycombCompat.Y);
    }

    public void setPropertyName(String propertyName) {
        if (this.mValues != null) {
            PropertyValuesHolder valuesHolder = this.mValues[0];
            String oldName = valuesHolder.getPropertyName();
            valuesHolder.setPropertyName(propertyName);
            this.mValuesMap.remove(oldName);
            this.mValuesMap.put(propertyName, valuesHolder);
        }
        this.mPropertyName = propertyName;
        this.mInitialized = false;
    }

    public void setProperty(Property property) {
        if (this.mValues != null) {
            PropertyValuesHolder valuesHolder = this.mValues[0];
            String oldName = valuesHolder.getPropertyName();
            valuesHolder.setProperty(property);
            this.mValuesMap.remove(oldName);
            this.mValuesMap.put(this.mPropertyName, valuesHolder);
        }
        if (this.mProperty != null) {
            this.mPropertyName = property.getName();
        }
        this.mProperty = property;
        this.mInitialized = false;
    }

    public String getPropertyName() {
        return this.mPropertyName;
    }

    private ObjectAnimator(Object target, String propertyName) {
        this.mTarget = target;
        setPropertyName(propertyName);
    }

    private <T> ObjectAnimator(T target, Property<T, ?> property) {
        this.mTarget = target;
        setProperty(property);
    }

    public static ObjectAnimator ofInt(Object target, String propertyName, int... values) {
        ObjectAnimator anim = new ObjectAnimator(target, propertyName);
        anim.setIntValues(values);
        return anim;
    }

    public static <T> ObjectAnimator ofInt(T target, Property<T, Integer> property, int... values) {
        ObjectAnimator anim = new ObjectAnimator((Object) target, (Property) property);
        anim.setIntValues(values);
        return anim;
    }

    public static ObjectAnimator ofFloat(Object target, String propertyName, float... values) {
        ObjectAnimator anim = new ObjectAnimator(target, propertyName);
        anim.setFloatValues(values);
        return anim;
    }

    public static <T> ObjectAnimator ofFloat(T target, Property<T, Float> property, float...
            values) {
        ObjectAnimator anim = new ObjectAnimator((Object) target, (Property) property);
        anim.setFloatValues(values);
        return anim;
    }

    public static ObjectAnimator ofObject(Object target, String propertyName, TypeEvaluator
            evaluator, Object... values) {
        ObjectAnimator anim = new ObjectAnimator(target, propertyName);
        anim.setObjectValues(values);
        anim.setEvaluator(evaluator);
        return anim;
    }

    public static <T, V> ObjectAnimator ofObject(T target, Property<T, V> property,
                                                 TypeEvaluator<V> evaluator, V... values) {
        ObjectAnimator anim = new ObjectAnimator((Object) target, (Property) property);
        anim.setObjectValues(values);
        anim.setEvaluator(evaluator);
        return anim;
    }

    public static ObjectAnimator ofPropertyValuesHolder(Object target, PropertyValuesHolder...
            values) {
        ObjectAnimator anim = new ObjectAnimator();
        anim.mTarget = target;
        anim.setValues(values);
        return anim;
    }

    public void setIntValues(int... values) {
        if (this.mValues != null && this.mValues.length != 0) {
            super.setIntValues(values);
        } else if (this.mProperty != null) {
            setValues(new PropertyValuesHolder[]{PropertyValuesHolder.ofInt(this.mProperty,
                    values)});
        } else {
            setValues(new PropertyValuesHolder[]{PropertyValuesHolder.ofInt(this.mPropertyName,
                    values)});
        }
    }

    public void setFloatValues(float... values) {
        if (this.mValues != null && this.mValues.length != 0) {
            super.setFloatValues(values);
        } else if (this.mProperty != null) {
            setValues(new PropertyValuesHolder[]{PropertyValuesHolder.ofFloat(this.mProperty,
                    values)});
        } else {
            setValues(new PropertyValuesHolder[]{PropertyValuesHolder.ofFloat(this.mPropertyName,
                    values)});
        }
    }

    public void setObjectValues(Object... values) {
        if (this.mValues != null && this.mValues.length != 0) {
            super.setObjectValues(values);
        } else if (this.mProperty != null) {
            setValues(new PropertyValuesHolder[]{PropertyValuesHolder.ofObject(this.mProperty,
                    (TypeEvaluator) null, values)});
        } else {
            setValues(new PropertyValuesHolder[]{PropertyValuesHolder.ofObject(this
                    .mPropertyName, (TypeEvaluator) null, values)});
        }
    }

    public void start() {
        super.start();
    }

    void initAnimation() {
        if (!this.mInitialized) {
            if (this.mProperty == null && AnimatorProxy.NEEDS_PROXY && (this.mTarget instanceof
                    View) && PROXY_PROPERTIES.containsKey(this.mPropertyName)) {
                setProperty((Property) PROXY_PROPERTIES.get(this.mPropertyName));
            }
            for (PropertyValuesHolder propertyValuesHolder : this.mValues) {
                propertyValuesHolder.setupSetterAndGetter(this.mTarget);
            }
            super.initAnimation();
        }
    }

    public ObjectAnimator setDuration(long duration) {
        super.setDuration(duration);
        return this;
    }

    public Object getTarget() {
        return this.mTarget;
    }

    public void setTarget(Object target) {
        if (this.mTarget != target) {
            Object oldTarget = this.mTarget;
            this.mTarget = target;
            if (oldTarget == null || target == null || oldTarget.getClass() != target.getClass()) {
                this.mInitialized = false;
            }
        }
    }

    public void setupStartValues() {
        initAnimation();
        for (PropertyValuesHolder propertyValuesHolder : this.mValues) {
            propertyValuesHolder.setupStartValue(this.mTarget);
        }
    }

    public void setupEndValues() {
        initAnimation();
        for (PropertyValuesHolder propertyValuesHolder : this.mValues) {
            propertyValuesHolder.setupEndValue(this.mTarget);
        }
    }

    void animateValue(float fraction) {
        super.animateValue(fraction);
        for (PropertyValuesHolder animatedValue : this.mValues) {
            animatedValue.setAnimatedValue(this.mTarget);
        }
    }

    public ObjectAnimator clone() {
        return (ObjectAnimator) super.clone();
    }

    public String toString() {
        String returnVal = "ObjectAnimator@" + Integer.toHexString(hashCode()) + ", target " +
                this.mTarget;
        if (this.mValues != null) {
            for (PropertyValuesHolder propertyValuesHolder : this.mValues) {
                returnVal = returnVal + "\n    " + propertyValuesHolder.toString();
            }
        }
        return returnVal;
    }
}
