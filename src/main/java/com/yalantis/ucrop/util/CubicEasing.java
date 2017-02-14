package com.yalantis.ucrop.util;

public final class CubicEasing {
    public static float easeOut(float time, float start, float end, float duration) {
        time = (time / duration) - 1.0f;
        return ((((time * time) * time) + 1.0f) * end) + start;
    }

    public static float easeIn(float time, float start, float end, float duration) {
        time /= duration;
        return (((end * time) * time) * time) + start;
    }

    public static float easeInOut(float time, float start, float end, float duration) {
        time /= duration / 2.0f;
        if (time < 1.0f) {
            return ((((end / 2.0f) * time) * time) * time) + start;
        }
        time -= 2.0f;
        return ((end / 2.0f) * (((time * time) * time) + 2.0f)) + start;
    }
}
