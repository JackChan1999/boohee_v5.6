package com.nineoldandroids.animation;

import java.util.ArrayList;

class ValueAnimator$2 extends ThreadLocal<ArrayList<ValueAnimator>> {
    ValueAnimator$2() {
    }

    protected ArrayList<ValueAnimator> initialValue() {
        return new ArrayList();
    }
}
