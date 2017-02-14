package com.boohee.utils.viewanimator;

import android.view.View;

public class AnimationListener {

    public interface Stop {
        void onStop();
    }

    public interface Update<V extends View> {
        void update(V v, float f);
    }

    public interface Start {
        void onStart();
    }

    private AnimationListener() {
    }
}
