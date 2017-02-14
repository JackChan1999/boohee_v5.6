package com.nineoldandroids.animation;

import java.util.ArrayList;

class ValueAnimator$1 extends ThreadLocal<ArrayList<ValueAnimator>> {
    ValueAnimator$1() {
    }

    protected ArrayList<ValueAnimator> initialValue() {
        return new ArrayList();
    }
}
